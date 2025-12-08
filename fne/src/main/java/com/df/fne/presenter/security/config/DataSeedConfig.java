package com.df.fne.presenter.security.config;

import com.df.fne.core.domaines.enums.Role;
import com.df.fne.jpa.entities.BusinessUnits;
import com.df.fne.jpa.entities.Tva;
import com.df.fne.jpa.entities.User;
import com.df.fne.jpa.repositories.BusinessUnitsRepository;
import com.df.fne.jpa.repositories.TvaRepository;
import com.df.fne.jpa.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Configuration
public class DataSeedConfig {

    private static final String DEFAULT_BU_CODE = "BUD01";
    private static final String DEFAULT_BU_LIBELLE = "Business Unit Default";
    private static final String DEFAULT_BU_DESC = "Default Seed Business Unit";

    private static final String ADMIN_USERNAME = "dg_root@1";
    private static final String ADMIN_EMAIL = "root@dg.test.com";
    private static final String ADMIN_PASSWORD = "root_hash1234@!";

    @Bean
    @Transactional
    CommandLineRunner init(UserRepository userRepository,
                           BusinessUnitsRepository businessUnitsRepository,
                           TvaRepository tvaRepository,
                           PasswordEncoder passwordEncoder) {

        return args -> {
            seedTva(tvaRepository);
            BusinessUnits bu = seedBusinessUnit(businessUnitsRepository);
            seedAdmin(userRepository, passwordEncoder, bu);
        };
    }

    private void seedTva(TvaRepository tvaRepository) {
        if (tvaRepository.count() > 0) {
            System.out.println("ℹ TVA already seeded");
            return;
        }

        List<Tva> tvaList = List.of(
                new Tva(null, "TVA", 18),
                new Tva(null, "TVAB", 9),
                new Tva(null, "TVAC", 0),
                new Tva(null, "TVAD", 0)
        );

        tvaRepository.saveAll(tvaList);
        System.out.println("✔ TVA seeded successfully!");
    }

    private BusinessUnits seedBusinessUnit(BusinessUnitsRepository businessUnitsRepository) {
        return businessUnitsRepository.findByCode(DEFAULT_BU_CODE)
                .orElseGet(() -> {
                    BusinessUnits bu = new BusinessUnits();
                    bu.setLibelle(DEFAULT_BU_LIBELLE);
                    bu.setCode(DEFAULT_BU_CODE);
                    bu.setDescription(DEFAULT_BU_DESC);

                    businessUnitsRepository.save(bu);
                    System.out.println("✔ BusinessUnit created");
                    return bu;
                });
    }

    private void seedAdmin(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           BusinessUnits bu) {

        User admin = userRepository.findByUsername(ADMIN_USERNAME).orElse(null);
        boolean changed = false;

        if (admin == null) {
            admin = new User();
            admin.setId(UUID.randomUUID());
            admin.setUsername(ADMIN_USERNAME);
            admin.setEmail(ADMIN_EMAIL);
            admin.setPassword(passwordEncoder.encode(ADMIN_PASSWORD));
            admin.setRole(Role.ADMIN);
            admin.setEnabled(true);
            admin.setBusinessUnits(bu);

            userRepository.save(admin);
            System.out.println("✔ Admin user created and linked to BU");
            return;
        }

        // 🔄 Update only if needed
        if (!admin.isEnabled()) { admin.setEnabled(true); changed = true; }
        if (admin.getRole() != Role.ADMIN) { admin.setRole(Role.ADMIN); changed = true; }
        if (!passwordEncoder.matches(ADMIN_PASSWORD, admin.getPassword())) {
            admin.setPassword(passwordEncoder.encode(ADMIN_PASSWORD)); changed = true;
        }
        if (!ADMIN_EMAIL.equals(admin.getEmail())) { admin.setEmail(ADMIN_EMAIL); changed = true; }
        if (admin.getBusinessUnits() == null) { admin.setBusinessUnits(bu); changed = true; }

        if (changed) {
            userRepository.save(admin);
            System.out.println("✔ Admin user updated to default state");
        } else {
            System.out.println("ℹ Admin user already up-to-date");
        }
    }
}

