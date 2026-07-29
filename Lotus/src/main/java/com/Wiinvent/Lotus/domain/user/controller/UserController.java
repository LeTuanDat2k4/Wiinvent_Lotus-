package com.Wiinvent.Lotus.domain.user.controller;

import com.Wiinvent.Lotus.core.dto.ApiResponse;
import com.Wiinvent.Lotus.core.security.SecurityUtils;
import com.Wiinvent.Lotus.domain.user.dto.CreateUserRequest;
import com.Wiinvent.Lotus.domain.user.dto.UserProfileResponse;
import com.Wiinvent.Lotus.domain.user.dto.UserResponse;
import com.Wiinvent.Lotus.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserResponse created = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(created));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getProfile() {
        UserProfileResponse profile = userService.getProfile(SecurityUtils.getCurrentUserId());
        return ResponseEntity.ok(ApiResponse.success(profile));
    }
}
