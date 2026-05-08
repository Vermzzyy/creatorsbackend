package com.creatorshub.creatorshub.features.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.creatorshub.creatorshub.features.user.dto.ProfileResponse;
import com.creatorshub.creatorshub.features.user.dto.UpdatePasswordRequest;
import com.creatorshub.creatorshub.features.user.model.User;
import com.creatorshub.creatorshub.features.user.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public ProfileResponse getProfile(String email){

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new ProfileResponse(
                user.getEmail(),
                user.getFirstname(),
                user.getLastname(),
                user.getProfilePhotoUrl(),
                user.getRole()
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

    public void updateProfilePhoto(String email, String photoUrl) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setProfilePhotoUrl(photoUrl);
        userRepository.save(user);
    }
}
