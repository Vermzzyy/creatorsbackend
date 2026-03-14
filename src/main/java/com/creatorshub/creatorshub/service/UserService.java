package com.creatorshub.creatorshub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.creatorshub.creatorshub.DTO.ProfileResponse;
import com.creatorshub.creatorshub.DTO.UpdatePasswordRequest;
import com.creatorshub.creatorshub.entity.User;
import com.creatorshub.creatorshub.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public ProfileResponse getProfile(String email){

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new ProfileResponse(
                user.getEmail(),
                user.getFirstname(),
                user.getLastname()
        );
    }

    public void updatePassword(String email, UpdatePasswordRequest request){

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!encoder.matches(request.currentPassword, user.getPasswordHash())){
            throw new RuntimeException("Current password is incorrect");
        }

        if(!request.newPassword.equals(request.confirmPassword)){
            throw new RuntimeException("New passwords do not match");
        }

        if(request.newPassword.length() < 6){
            throw new RuntimeException("Password must be at least 6 characters");
        }

        user.setPasswordHash(encoder.encode(request.newPassword));

        userRepository.save(user);
    }
}