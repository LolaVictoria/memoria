package com.lolavictoria.celebration.graphql.input;

public record CreateUserInput(
        String name,
        String email,
        String password
) {
}