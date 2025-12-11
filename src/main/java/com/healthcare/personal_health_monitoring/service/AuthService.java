//package com.healthcare.personal_health_monitoring.service;
//
//import com.healthcare.personal_health_monitoring.entity.User;
//import com.healthcare.personal_health_monitoring.repository.UserRepository;
//
//import com.healthcare.personal_health_monitoring.security.JWTUtil;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class AuthService {
//
//    private final UserRepository repo;
//    private final PasswordEncoder encoder;
//    private final JWTUtil jwtUtil;
//
//    public String login(String email, String password){
//
//        User user = repo.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        if(!encoder.matches(password, user.getPassword()))
//            throw new RuntimeException("Invalid password");
//
//        return jwtUtil.generateToken(user.getEmail());
//    }
//}
