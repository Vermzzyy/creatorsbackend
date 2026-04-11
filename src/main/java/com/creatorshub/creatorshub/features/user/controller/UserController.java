package com.creatorshub.creatorshub.features.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.creatorshub.creatorshub.features.user.dto.ProfileResponse;
import com.creatorshub.creatorshub.features.user.dto.UpdatePasswordRequest;
import com.creatorshub.creatorshub.features.user.service.UserService;
import com.creatorshub.creatorshub.shared.security.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(HttpServletRequest request){

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Missing Authorization header");
        }

        String token = authHeader.substring(7).trim();

        try {
            String email = jwtUtil.extractEmail(token);
            ProfileResponse profile = userService.getProfile(email);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            e.printStackTrace(); // 🔥 VERY IMPORTANT
            return ResponseEntity.status(401).body("Invalid token");
        }
    }

    @PutMapping("/password")
    public ResponseEntity<?> updatePassword(
            HttpServletRequest request,
            @RequestBody UpdatePasswordRequest body){

        String token = request.getHeader("Authorization").replace("Bearer ","");
        String email = jwtUtil.extractEmail(token);

        userService.updatePassword(email, body);

        return ResponseEntity.ok("Password updated successfully");
    }
}
