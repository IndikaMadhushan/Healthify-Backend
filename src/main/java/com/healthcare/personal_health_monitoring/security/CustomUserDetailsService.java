package com.healthcare.personal_health_monitoring.security;

import com.healthcare.personal_health_monitoring.entity.User;
import com.healthcare.personal_health_monitoring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

//this class is for load user details from Database. spring security uses this


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getEmail())
                .password(user.getPassword())     // encrypted
                .roles("USER")                    // simple role
                .build();
    }
}
