package com.healthcare.personal_health_monitoring.security;

import com.healthcare.personal_health_monitoring.entity.User;
import com.healthcare.personal_health_monitoring.entity.UserRole;
import lombok.Getter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

//    Critical: getAuthorities() adds ROLE_ prefix
//
//    Spring Security requires this prefix for role-based security
//    ROLE_PATIENT, ROLE_DOCTOR, ROLE_ADMIN
//    This allows @PreAuthorize("hasRole('PATIENT')") to work
    @Override
    public Collection<SimpleGrantedAuthority> getAuthorities() {
        // ensure role mapping uses ROLE_ prefix
        String roleName = user.getRole() != null ? user.getRole().name() : "PATIENT";
        return List.of(new SimpleGrantedAuthority("ROLE_" + roleName));
    }

    @Override
    public String getPassword() { return user.getPassword(); }

    @Override
    public String getUsername() { return user.getEmail(); }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() {
        return user.isEnabled(); // uses enabled field added to User
    }
}
