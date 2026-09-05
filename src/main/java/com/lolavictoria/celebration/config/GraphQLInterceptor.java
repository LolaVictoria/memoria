package com.lolavictoria.celebration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;


@Configuration
public class GraphQLInterceptor {

    @Bean
    public WebGraphQlInterceptor cookieInterceptor() {

        return (request, chain) -> {

            return chain.next(request).doOnNext(response -> {

                String token = response
                        .getExecutionInput()
                        .getGraphQLContext()
                        .get("access_token");

                if (token != null) {

                    ResponseCookie cookie = ResponseCookie
                            .from("access_token", token)
                            .httpOnly(true)
                            .secure(false)
                            .path("/")
                            .maxAge(60 * 60)
                            .build();

                    response.getResponseHeaders()
                            .add(HttpHeaders.SET_COOKIE, cookie.toString());
                }
            });
        };
    }
}