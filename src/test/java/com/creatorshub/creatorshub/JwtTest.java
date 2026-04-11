package com.creatorshub.creatorshub;

import com.creatorshub.creatorshub.shared.security.JwtUtil;
import org.junit.jupiter.api.Test;

public class JwtTest {
    @Test
    public void testJwt() {
        JwtUtil util = new JwtUtil();
        String token = util.generateToken("test@test.com");
        System.out.println("Token: " + token);
        try {
            String email = util.extractEmail(token);
            System.out.println("Email extracted: " + email);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Validation failed", e);
        }
    }
}
