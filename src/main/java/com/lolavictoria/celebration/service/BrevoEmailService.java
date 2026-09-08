package com.lolavictoria.celebration.service;

import brevo.ApiClient;
import brevo.ApiException;
import brevo.Configuration;
import brevo.auth.ApiKeyAuth;
import brevoApi.TransactionalEmailsApi;
import brevoModel.SendSmtpEmail;
import brevoModel.SendSmtpEmailSender;
import brevoModel.SendSmtpEmailTo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrevoEmailService {

    private final TransactionalEmailsApi emailApi;
    private final String senderEmail;
    private final String senderName;

    public BrevoEmailService(
            @Value("${brevo.api-key}") String apiKey,
            @Value("${brevo.sender-email}") String senderEmail,
            @Value("${brevo.sender-name}") String senderName
    ) {

        ApiClient defaultClient = Configuration.getDefaultApiClient();

        ApiKeyAuth apiKeyAuth =
                (ApiKeyAuth) defaultClient.getAuthentication("api-key");

        apiKeyAuth.setApiKey(apiKey);

        this.emailApi = new TransactionalEmailsApi(defaultClient);
        this.senderEmail = senderEmail;
        this.senderName = senderName;
    }

    public void sendVerificationEmail(
            String recipientEmail,
            String recipientName,
            String verificationLink
    ) {

        SendSmtpEmail email = new SendSmtpEmail();

        SendSmtpEmailSender sender = new SendSmtpEmailSender();
        sender.setEmail(senderEmail);
        sender.setName(senderName);

        SendSmtpEmailTo recipient = new SendSmtpEmailTo();
        recipient.setEmail(recipientEmail);
        recipient.setName(recipientName);

        email.setSender(sender);
        email.setTo(List.of(recipient));

        email.setSubject("Verify your Memoria account");

        email.setHtmlContent(
                """
                <h2>Welcome to Memoria, %s! 🎉</h2>

                <p>
                    We're excited to have you here.
                </p>

                <p>
                    Please verify your email address to activate your account.
                </p>

                <p>
                    <a href="%s">
                        Verify my email
                    </a>
                </p>

                <p>
                    This verification link will expire in 24 hours.
                </p>

                <p>
                    — The Memoria Team
                </p>
                """.formatted(recipientName, verificationLink)
        );

        try {

            emailApi.sendTransacEmail(email);

        } catch (ApiException e) {

            throw new RuntimeException(
                    "Failed to send verification email",
                    e
            );
        }
    }

    public void sendWelcomeEmail(
        String recipientEmail,
        String recipientName
) {

    SendSmtpEmail email = new SendSmtpEmail();

    SendSmtpEmailSender sender = new SendSmtpEmailSender();
    sender.setEmail(senderEmail);
    sender.setName(senderName);

    SendSmtpEmailTo recipient = new SendSmtpEmailTo();
    recipient.setEmail(recipientEmail);
    recipient.setName(recipientName);

    email.setSender(sender);
    email.setTo(List.of(recipient));

    email.setSubject("Welcome to Memoria 🎉");

    email.setHtmlContent(
            """
            <h2>You're officially part of Memoria, %s! 🎉</h2>

            <p>
                Your email has been verified successfully.
            </p>

            <p>
                Welcome to Memoria — a place to create, preserve,
                and share beautiful celebrations with the people
                who matter to you.
            </p>

            <p>
                Whether it's a birthday, graduation, wedding,
                anniversary, or simply a moment worth remembering,
                we're glad you're here.
            </p>

            <p>
                <a href="https://memoria-frontend-v1.vercel.app/">
                    Start creating celebrations
                </a>
            </p>

            <p>
                Here's to making more moments worth keeping. ✨
            </p>

            <p>
                — The Memoria Team
            </p>
            """.formatted(recipientName)
    );

    try {

        emailApi.sendTransacEmail(email);

    } catch (ApiException e) {

        throw new RuntimeException(
                "Failed to send welcome email",
                e
        );
    }
}
}