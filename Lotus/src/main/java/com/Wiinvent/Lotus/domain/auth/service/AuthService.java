package com.Wiinvent.Lotus.domain.auth.service;

import com.Wiinvent.Lotus.core.config.LotusProperties;
import com.Wiinvent.Lotus.core.exception.UnauthorizedException;
import com.Wiinvent.Lotus.core.security.JwtTokenProvider;
import com.Wiinvent.Lotus.core.security.UserPrincipal;
import com.Wiinvent.Lotus.core.util.TokenHashUtil;
import com.Wiinvent.Lotus.domain.auth.dto.LoginRequest;
import com.Wiinvent.Lotus.domain.auth.dto.RefreshTokenRequest;
import com.Wiinvent.Lotus.domain.auth.dto.TokenResponse;
import com.Wiinvent.Lotus.domain.auth.entity.RefreshToken;
import com.Wiinvent.Lotus.domain.auth.repository.RefreshTokenRepository;
import com.Wiinvent.Lotus.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final LotusProperties lotusProperties;

    @Transactional
    public TokenResponse login(LoginRequest request) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getPhone(), request.getPassword()));
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return issueTokens(principal);
    }

    @Transactional
    public TokenResponse refresh(RefreshTokenRequest request) {
        var claims = jwtTokenProvider.parseToken(request.getRefreshToken());
        if (!jwtTokenProvider.isRefreshToken(claims)) {
            throw new UnauthorizedException("Refresh token không hợp lệ.");
        }

        String tokenHash = TokenHashUtil.hash(request.getRefreshToken());
        RefreshToken storedToken = refreshTokenRepository.findByTokenHashAndRevokedFalse(tokenHash)
                .orElseThrow(() -> new UnauthorizedException("Refresh token không hợp lệ hoặc đã bị thu hồi."));

        if (storedToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new UnauthorizedException("Refresh token đã hết hạn.");
        }

        storedToken.setRevoked(true);
        refreshTokenRepository.save(storedToken);

        Long userId = Long.parseLong(claims.getSubject());
        UserPrincipal principal = userRepository.findById(userId)
                .map(UserPrincipal::new)
                .orElseThrow(() -> new UnauthorizedException("Người dùng không tồn tại."));

        return issueTokens(principal);
    }

    private TokenResponse issueTokens(UserPrincipal principal) {
        String accessToken = jwtTokenProvider.generateAccessToken(principal);
        String refreshToken = jwtTokenProvider.generateRefreshToken(principal);

        RefreshToken entity = RefreshToken.builder()
                .userId(principal.getId())
                .tokenHash(TokenHashUtil.hash(refreshToken))
                .expiresAt(LocalDateTime.now().plusSeconds(
                        lotusProperties.getJwt().getRefreshTokenExpirationMs() / 1000))
                .revoked(false)
                .build();
        refreshTokenRepository.save(entity);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(lotusProperties.getJwt().getAccessTokenExpirationMs() / 1000)
                .build();
    }
}
