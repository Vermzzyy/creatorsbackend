package com.creatorshub.creatorshub.features.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.creatorshub.creatorshub.features.user.model.User;
import com.creatorshub.creatorshub.features.user.repository.UserRepository;
import com.creatorshub.creatorshub.features.auth.dto.AuthResponse;
import com.creatorshub.creatorshub.features.auth.dto.LoginRequest;
import com.creatorshub.creatorshub.features.auth.dto.RegisterRequest;
import com.creatorshub.creatorshub.shared.security.JwtUtil;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public AuthResponse register(RegisterRequest request){
        
        if(userRepository.findByEmail(request.email).isPresent()){
            throw new RuntimeException("Email already exists");
        }
        if(!request.password.equals(request.confirmPassword)){
            throw new RuntimeException("Passwords do not match");
        }

        User user = new User();

        user.setEmail(request.email);
        user.setFirstname(request.firstName);
        user.setLastname(request.lastName);
        user.setPasswordHash(encoder.encode(request.password));

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail());

        return new AuthResponse(user, token);
    }

    public AuthResponse login(LoginRequest request){

        User user = userRepository.findByEmail(request.email)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if(!encoder.matches(request.password, user.getPasswordHash())){
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        return new AuthResponse(user, token);
    }

}
