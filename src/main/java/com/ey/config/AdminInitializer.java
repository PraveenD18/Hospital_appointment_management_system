package com.ey.config;

import com.ey.enums.Role;
import com.ey.enums.AccountStatus;
import com.ey.model.User;
import com.ey.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminInitializer {

    @Bean
    CommandLineRunner createAdmin(UserRepository userRepository,
                                  PasswordEncoder passwordEncoder) {
        return args -> {

            String adminEmail = "admin@system.com";

            if (!userRepository.existsByEmail(adminEmail)) {
                User admin = new User();
                admin.setEmail(adminEmail);
                admin.setPassword(passwordEncoder.encode("Admin@123"));
                admin.setRole(Role.ADMIN);
                admin.setStatus(AccountStatus.ACTIVE);

                userRepository.save(admin);

                System.out.println("✔ ADMIN created: " + adminEmail + " / Admin@123");
            } else {
                System.out.println("✔ ADMIN already exists. Skipping creation.");
            }
        };
    }
}