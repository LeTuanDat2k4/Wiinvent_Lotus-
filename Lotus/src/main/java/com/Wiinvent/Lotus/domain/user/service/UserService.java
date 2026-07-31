package com.Wiinvent.Lotus.domain.user.service;

import com.Wiinvent.Lotus.core.config.CacheConfig;
import com.Wiinvent.Lotus.core.exception.DuplicatePhoneException;
import com.Wiinvent.Lotus.core.exception.ResourceNotFoundException;
import com.Wiinvent.Lotus.domain.user.dto.CreateUserRequest;
import com.Wiinvent.Lotus.domain.user.dto.UserProfileResponse;
import com.Wiinvent.Lotus.domain.user.dto.UserResponse;
import com.Wiinvent.Lotus.domain.user.entity.User;
import com.Wiinvent.Lotus.domain.user.entity.UserRole;
import com.Wiinvent.Lotus.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new DuplicatePhoneException(request.getPhone());
        }

        User user = User.builder()
                .phone(request.getPhone())
                .displayName(request.getDisplayName())
                .password(passwordEncoder.encode(request.getPassword()))
                .lotusBalance(0L)
                .role(UserRole.USER)
                .build();

        return toUserResponse(userRepository.save(user));
    }

    @Cacheable(value = CacheConfig.USER_PROFILE_CACHE, key = "#userId")
    public UserProfileResponse getProfile(Long userId) {
        User user = getUserById(userId);
        return UserProfileResponse.builder()
                .id(user.getId())
                .displayName(user.getDisplayName())
                .avatarUrl(user.getAvatarUrl())
                .lotusBalance(user.getLotusBalance())
                .build();
    }

    @CacheEvict(value = CacheConfig.USER_PROFILE_CACHE, key = "#userId")
    public void evictUserProfileCache(Long userId) {
        // Trống: Spring Cache sẽ tự động xóa key tương ứng khỏi Redis
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }

    private UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .phone(user.getPhone())
                .displayName(user.getDisplayName())
                .avatarUrl(user.getAvatarUrl())
                .lotusBalance(user.getLotusBalance())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
