package com.creatorshub.creatorshub.features.user.dto;

public class ProfileResponse {

    public String email;
    public String firstName;
    public String lastName;
    public String profilePhotoUrl;
    public String role;

    public ProfileResponse(String email, String firstName, String lastName, String profilePhotoUrl, String role) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePhotoUrl = profilePhotoUrl;
        this.role = role;
    }

}
