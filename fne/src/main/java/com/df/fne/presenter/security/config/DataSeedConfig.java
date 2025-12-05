package com.df.fne.presenter.security.config;

import com.df.fne.core.domaines.enums.Role;
import com.df.fne.jpa.entities.User;
import com.df.fne.jpa.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@Configuration
public class DataSeedConfig {

    @Bean
    CommandLineRunner seedAdmin(UserRepository userRepository,
                                PasswordEncoder passwordEncoder) {
        return args -> {
            String username = "dg_root@1";
            String email = "root@dg.test.com";
            String rawPassword = "root_hash1234@!";

            User admin = userRepository.findByUsername(username).orElse(null);

            if (admin == null) {
                admin = new User();
                admin.setId(UUID.randomUUID());
                admin.setUsername(username);
                admin.setEmail(email);
                admin.setPassword(passwordEncoder.encode(rawPassword));
                admin.setRole(Role.ADMIN);
                admin.setEnabled(true);
                userRepository.save(admin);
                System.out.println("Admin user created: " + email);
            } else {
                boolean changed = false;
                if (!admin.isEnabled()) {
                    admin.setEnabled(true);
                    changed = true;
                }
                if (admin.getRole() != Role.ADMIN) {
                    admin.setRole(Role.ADMIN);
                    changed = true;
                }
                if (!passwordEncoder.matches(rawPassword, admin.getPassword())) {
                    admin.setPassword(passwordEncoder.encode(rawPassword));
                    changed = true;
                }
                if (!admin.getEmail().equals(email)) {
                    admin.setEmail(email);
                    changed = true;
                }
                if (changed) {
                    userRepository.save(admin);
                    System.out.println("Admin user updated: " + email);
                } else {
                    System.out.println("Admin user already up-to-date: " + email);
                }
            }
        };
    }


}
