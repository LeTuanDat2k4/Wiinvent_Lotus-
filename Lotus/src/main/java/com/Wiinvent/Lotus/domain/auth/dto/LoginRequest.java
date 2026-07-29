package com.Wiinvent.Lotus.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Số điện thoại không được để trống.")
    private String phone;

    @NotBlank(message = "Mật khẩu không được để trống.")
    private String password;
}
