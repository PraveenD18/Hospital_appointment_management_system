package com.ey.config;

import com.ey.repository.UserRepository;
import com.ey.model.User; // Your entity with a single Role enum field like: private Role role;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthBeansConfig {

    /**
     * Use BCrypt for hashing user passwords.
     * Make sure the DB contains BCrypt-hashed values (not plaintext).
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Load users by email (since /auth/login uses "email").
     * Builds Spring Security UserDetails and sets roles using .roles(<ENUM_NAME>),
     * which auto-prefixes ROLE_ (e.g., ADMIN -> ROLE_ADMIN).
     */
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return (String usernameOrEmail) -> {
            User account = userRepository.findByEmail(usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + usernameOrEmail));

            // If your entity has a single enum field: com.ey.enums.Role role;
            // .roles() expects raw names (e.g., "ADMIN") and will add ROLE_ prefix internally.
            UserDetails ud = org.springframework.security.core.userdetails.User
                .withUsername(account.getEmail())     // must be a String
                .password(account.getPassword())      // BCrypt hash from DB
                .roles(account.getRole().name())      // -> ROLE_ADMIN / ROLE_DOCTOR / ROLE_PATIENT
                // Optionally map flags if you track them in your entity:
                // .accountLocked(!account.isAccountNonLocked())
                // .disabled(!account.isEnabled())
                .build();

            return ud;
        };
    }

    /**
     * Register DaoAuthenticationProvider that uses our UserDetailsService and PasswordEncoder.
     * AuthenticationManager (below) will use this provider.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider(
            UserDetailsService uds,
            PasswordEncoder encoder
    ) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(uds);
        provider.setPasswordEncoder(encoder);
        return provider;
    }

    /**
     * Expose AuthenticationManager built by Spring from the registered providers.
     * Your AuthService#login(...) should inject and use this to authenticate credentials.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}