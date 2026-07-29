package com.Wiinvent.Lotus.core.config;

import com.Wiinvent.Lotus.domain.user.entity.User;
import com.Wiinvent.Lotus.domain.user.entity.UserRole;
import com.Wiinvent.Lotus.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminSeeder implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LotusProperties lotusProperties;

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.existsByRole(UserRole.ADMIN)) {
            return;
        }

        User admin = User.builder()
                .phone(lotusProperties.getAdmin().getPhone())
                .displayName(lotusProperties.getAdmin().getDisplayName())
                .password(passwordEncoder.encode(lotusProperties.getAdmin().getDefaultPassword()))
                .lotusBalance(0L)
                .role(UserRole.ADMIN)
                .build();
        userRepository.save(admin);
        log.info("Seeded default admin user with phone {}", admin.getPhone());
    }
}
