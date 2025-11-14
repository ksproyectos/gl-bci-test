package com.newbank.userservice.security;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilTest {

    @Test
    void generateAndValidateToken_andExtractUsername() {
        JwtUtil jwtUtil = new JwtUtil();

        // set a positive expiration so token is valid for the test
        try {
            java.lang.reflect.Field f = JwtUtil.class.getDeclaredField("jwtExpirationInMs");
            f.setAccessible(true);
            f.setLong(jwtUtil, 3600000L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String token = jwtUtil.generateToken("user@example.com");

        boolean valid = jwtUtil.isTokenValid(token);
        String username = jwtUtil.extractUsername(token);

        assertThat(valid).isTrue();
        assertThat(username).isEqualTo("user@example.com");
    }

    @Test
    void expiredToken_isNotValid() throws Exception {
        JwtUtil jwtUtil = new JwtUtil();

        // set expiration to a negative value so token is immediately expired
        Field f = JwtUtil.class.getDeclaredField("jwtExpirationInMs");
        f.setAccessible(true);
        f.setLong(jwtUtil, -1000L);

        String token = jwtUtil.generateToken("user2@example.com");

        boolean valid = jwtUtil.isTokenValid(token);

        assertThat(valid).isFalse();
    }
}
