package com.ticketbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String refreshToken;
    private String type = "Bearer";
    private Long userId;
    private String email;
    private String name;
    private String role;
    
    public AuthResponse(String token, String refreshToken, Long userId, String email, String name, String role) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.role = role;
    }
}

