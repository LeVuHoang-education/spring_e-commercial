package dev.backend.ecommercial.model.payload.request;

import lombok.Getter;

@Getter
public class SignUp {
    private String username;
    private String email;
    private String phone;
    private String password;
}
