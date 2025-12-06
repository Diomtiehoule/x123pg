package com.df.fne.presenter.security.config;

import com.df.fne.core.domaines.enums.Role;
import com.df.fne.jpa.entities.BusinessUnits;
import com.df.fne.jpa.entities.User;
import com.df.fne.jpa.repositories.BusinessUnitsRepository;
import com.df.fne.jpa.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Configuration
public class DataSeedConfig {

    @Bean
    @Transactional
    CommandLineRunner init(UserRepository userRepository,
                           BusinessUnitsRepository businessUnitsRepository,
                           PasswordEncoder passwordEncoder) {

        return args -> {

            BusinessUnits bu = businessUnitsRepository.findByCode("BUD01").orElse(null);

            if (bu == null) {
                bu = new BusinessUnits();
                bu.setLibelle("Business Unit Default");
                bu.setCode("BUD01");
                bu.setDescription("Default Seed Business Unit");

                businessUnitsRepository.save(bu);
                System.out.println("✔ BusinessUnit created");
            } else {
                System.out.println("ℹ BusinessUnit exists already");
            }


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

                admin.setBusinessUnits(bu); // 🔗 association user → BU

                userRepository.save(admin);

                System.out.println("✔ Admin user created and linked to BU");
            } else {
                boolean changed = false;

                if (!admin.isEnabled()) { admin.setEnabled(true); changed = true; }
                if (admin.getRole() != Role.ADMIN) { admin.setRole(Role.ADMIN); changed = true; }
                if (!passwordEncoder.matches(rawPassword, admin.getPassword())) {
                    admin.setPassword(passwordEncoder.encode(rawPassword)); changed = true;
                }
                if (!admin.getEmail().equals(email)) { admin.setEmail(email); changed = true; }

                if (admin.getBusinessUnits() == null) {
                    admin.setBusinessUnits(bu);
                    changed = true;
                }

                if (changed) {
                    userRepository.save(admin);
                    System.out.println("✔ Admin user updated and linked to BU");
                } else {
                    System.out.println("ℹ Admin user already up-to-date");
                }
            }
        };
    }
}
