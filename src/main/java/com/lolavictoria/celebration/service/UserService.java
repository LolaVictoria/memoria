package com.lolavictoria.celebration.service;

import com.lolavictoria.celebration.entity.User;
import com.lolavictoria.celebration.graphql.input.CreateUserInput;
import com.lolavictoria.celebration.repository.UserRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Value ("${frontend.url}")
    private String frontendUrl;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BrevoEmailService brevoEmailService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, BrevoEmailService brevoEmailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.brevoEmailService = brevoEmailService;
    }

    public User createUser(CreateUserInput input) {

        User user = new User();

        user.setName(input.name());
        user.setEmail(input.email());

        String hashedPassword = passwordEncoder.encode(input.password());
        user.setPasswordHash(hashedPassword);
        // Generate email verification token
        String verificationToken = UUID.randomUUID().toString();

         user.setVerificationToken(verificationToken);

        // Token expires in 24 hours
        user.setVerificationTokenExpiry(
                Instant.now().plus(24, ChronoUnit.HOURS)
        );

        // Save user before sending email
        User savedUser = userRepository.save(user);

       String verificationLink =
        frontendUrl + "/verify-email?token="
                + verificationToken;

            brevoEmailService.sendVerificationEmail(
                    savedUser.getEmail(),
                    savedUser.getName(),
                    verificationLink
            );

            return savedUser;
    }

    
    public User verifyEmail(String token) {

    User user = userRepository.findByVerificationToken(token)
            .orElseThrow(() ->
                    new RuntimeException("Invalid verification token")
            );

    if (user.getVerificationTokenExpiry() == null ||
            user.getVerificationTokenExpiry().isBefore(Instant.now())) {

        throw new RuntimeException("Verification token has expired");
    }

    user.setEmailVerified(true);

    // Clear the token so it cannot be reused
    user.setVerificationToken(null);
    user.setVerificationTokenExpiry(null);

    User verifiedUser = userRepository.save(user);

    // Send welcome email after successful verification
    brevoEmailService.sendWelcomeEmail(
            verifiedUser.getEmail(),
            verifiedUser.getName()
    );

    return verifiedUser;
}
    public User login(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new RuntimeException("Invalid email or password");
        }

        if (!user.isEmailVerified()) {
            throw new RuntimeException(
                    "Please verify your email before signing in."
            );
        }

        return user;
    }



    public User findByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

}