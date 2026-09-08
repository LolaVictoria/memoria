package com.lolavictoria.celebration.graphql;

import org.springframework.security.access.prepost.PreAuthorize;
import com.lolavictoria.celebration.entity.User;
import com.lolavictoria.celebration.graphql.input.CreateUserInput;
import com.lolavictoria.celebration.graphql.input.LoginInput;
import com.lolavictoria.celebration.service.JwtService;
import com.lolavictoria.celebration.service.UserService;
import org.springframework.security.core.Authentication;
import graphql.GraphQLContext;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class UserGraphQLController {

    private final UserService userService;
    private final JwtService jwtService;

    public UserGraphQLController(
            UserService userService,
            JwtService jwtService
    ) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @MutationMapping
    public User createUser(@Argument CreateUserInput input) {
        return userService.createUser(input);
    }

    @MutationMapping
    public User verifyEmail(@Argument String token) {
        return userService.verifyEmail(token);
    }

    @MutationMapping
    public User login(
            @Argument LoginInput input,
            GraphQLContext context
    ) {

        User user = userService.login(
                input.email(),
                input.password()
        );

        String token = jwtService.generateToken(user.getEmail());

        context.put("access_token", token);

        return user;
    }
    @PreAuthorize("isAuthenticated()")
    @QueryMapping
    public User me(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        String email = authentication.getName();

        return userService.findByEmail(email);
    }
}