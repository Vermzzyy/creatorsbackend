package com.creatorshub.creatorshub.features.auth.dto;

import com.creatorshub.creatorshub.features.user.model.User;

public class AuthResponse {

    public User user;
    public String token;

    public AuthResponse(User user, String token){
        this.user = user;
        this.token = token;
    }

}
