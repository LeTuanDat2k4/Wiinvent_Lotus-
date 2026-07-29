package com.Wiinvent.Lotus.domain.user.dto;

import com.Wiinvent.Lotus.domain.user.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String phone;
    private String displayName;
    private String avatarUrl;
    private Long lotusBalance;
    private UserRole role;
    private LocalDateTime createdAt;
}
