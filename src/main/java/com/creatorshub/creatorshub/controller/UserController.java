package com.creatorshub.creatorshub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.creatorshub.creatorshub.DTO.ProfileResponse;
import com.creatorshub.creatorshub.DTO.UpdatePasswordRequest;
import com.creatorshub.creatorshub.security.JwtUtil;
import com.creatorshub.creatorshub.service.UserService;

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

        String token = request.getHeader("Authorization").replace("Bearer ","");
        String email = jwtUtil.extractEmail(token);

        ProfileResponse profile = userService.getProfile(email);

        return ResponseEntity.ok(profile);
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