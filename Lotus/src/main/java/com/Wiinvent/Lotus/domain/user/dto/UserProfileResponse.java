package com.Wiinvent.Lotus.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse implements Serializable {

    private Long id;
    private String displayName;
    private String avatarUrl;
    private Long lotusBalance;
}
