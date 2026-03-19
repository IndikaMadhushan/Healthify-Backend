package com.healthcare.personal_health_monitoring.config;

import com.healthcare.personal_health_monitoring.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@RequiredArgsConstructor
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

        private final CorsConfigurationSource corsConfigurationSource;
        private final JwtAuthenticationFilter jwtAuthenticationFilter;
        private final AuthenticationProvider authenticationProvider;

        // SecurityConfig(CorsConfigurationSource corsConfigurationSource) {
        // this.corsConfigurationSource = corsConfigurationSource;
        // }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

                http

                                .csrf(csrf -> csrf.disable())
                                .cors(cors -> cors.configurationSource(corsConfigurationSource))

                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(HttpMethod.GET, "/api/site-reviews/public").permitAll()
                                                .requestMatchers(HttpMethod.POST, "/api/contact-us").permitAll()
                                                .requestMatchers("/api/admin/site-reviews/**").hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.GET, "/api/lab-reports/patient/**")
                                                .hasRole("DOCTOR")
                                                .requestMatchers("/api/lab-reports/my/**").hasRole("PATIENT")
                                                .requestMatchers(HttpMethod.GET, "/api/doctors/reports/patient/**")
                                                .hasAnyRole("DOCTOR", "PATIENT")
                                                .requestMatchers(HttpMethod.POST, "/api/doctors/reports/patient/**")
                                                .hasAnyRole("DOCTOR", "PATIENT")
                                                .requestMatchers(HttpMethod.GET, "/api/doctors/reports/*/download")
                                                .hasAnyRole("DOCTOR", "PATIENT")
                                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                                // .requestMatchers("/uploads/**").permitAll()
                                                .requestMatchers("/api/auth/**").permitAll()
                                                .requestMatchers("/api/v1/cpage/approve-edit/**").permitAll()
                                                .requestMatchers("/api/patients/**")
                                                .hasAnyRole("PATIENT", "DOCTOR", "ADMIN")
                                                .requestMatchers("/api/doctors/**").hasRole("DOCTOR")
                                                .requestMatchers("/api/notes/**").hasRole("DOCTOR")
                                                .requestMatchers("/api/doctors/reports/**").hasRole("DOCTOR")
                                                .requestMatchers(HttpMethod.POST, "/api/metrics/**").hasRole("PATIENT")
                                                .requestMatchers("/api/admin/audit-logs/**").hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.GET, "/api/metrics/**")
                                                .hasAnyRole("PATIENT", "DOCTOR")
                                                .requestMatchers("/api/doctors/patients/**").hasRole("DOCTOR")
                                                // .requestMatchers("/api/reminders/**").hasRole("PATIENT")
                                                // .requestMatchers("/api/appointments/**").hasRole("PATIENT")
                                                // .requestMatchers("/api/reminders/other/**").hasRole("PATIENT")
                                                // .requestMatchers("/api/reminders/period/**").hasRole("PATIENT")
                                                .requestMatchers("/api/ui/reminders/**").hasRole("PATIENT")
                                                // .requestMatchers("/api/reviews/**").permitAll()
                                                // .requestMatchers("/api/metrics/**").hasRole("PATIENT")
                                                .anyRequest().authenticated())

                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
}
