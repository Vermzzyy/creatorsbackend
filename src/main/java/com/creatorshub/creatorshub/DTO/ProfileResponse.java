package com.creatorshub.creatorshub.DTO;

public class ProfileResponse {

    public String email;
    public String firstName;
    public String lastName;

    public ProfileResponse(String email, String firstName, String lastName){
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

}
