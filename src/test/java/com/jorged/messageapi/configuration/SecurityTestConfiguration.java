package com.jorged.messageapi.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Arrays;

@TestConfiguration
public class SecurityTestConfiguration {

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {

        UserDetails userDetails1 = new UserDetailsImpl("test1@test.com", "Test12345");
        UserDetails userDetails2 = new UserDetailsImpl("test0@test.com", "Test12345");

        return new InMemoryUserDetailsManager(Arrays.asList(userDetails1, userDetails2));
    }
}
