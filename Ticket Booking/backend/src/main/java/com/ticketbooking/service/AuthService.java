package com.ticketbooking.service;

import com.ticketbooking.dto.AuthResponse;
import com.ticketbooking.dto.LoginRequest;
import com.ticketbooking.dto.SignupRequest;
import com.ticketbooking.entity.User;
import com.ticketbooking.repository.UserRepository;
import com.ticketbooking.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtTokenProvider tokenProvider;
    
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        String token = tokenProvider.generateToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(request.getEmail());
        
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        String role = user.getRoles().contains(User.Role.ADMIN) ? "ADMIN" : "USER";
        
        return new AuthResponse(token, refreshToken, user.getId(), user.getEmail(), user.getName(), role);
    }
    
    public AuthResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        
        Set<User.Role> roles = new HashSet<>();
        roles.add(User.Role.USER);
        user.setRoles(roles);
        
        user = userRepository.save(user);
        
        String token = tokenProvider.generateToken(request.getEmail());
        String refreshToken = tokenProvider.generateRefreshToken(request.getEmail());
        
        return new AuthResponse(token, refreshToken, user.getId(), user.getEmail(), user.getName(), "USER");
    }
    
    public AuthResponse refreshToken(String refreshToken) {
        if (tokenProvider.validateToken(refreshToken)) {
            String username = tokenProvider.getUsernameFromToken(refreshToken);
            String newToken = tokenProvider.generateToken(username);
            
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            String role = user.getRoles().contains(User.Role.ADMIN) ? "ADMIN" : "USER";
            
            return new AuthResponse(newToken, refreshToken, user.getId(), user.getEmail(), user.getName(), role);
        }
        throw new RuntimeException("Invalid refresh token");
    }
}

