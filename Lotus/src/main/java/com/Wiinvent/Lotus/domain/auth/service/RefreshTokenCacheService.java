package com.Wiinvent.Lotus.domain.auth.service;

import com.Wiinvent.Lotus.domain.auth.dto.RefreshTokenCacheDto;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RefreshTokenCacheService {

    private static final String REFRESH_TOKEN_KEY_PREFIX = "auth:refresh_token:";

    private final RedissonClient redissonClient;

    public void saveToken(RefreshTokenCacheDto dto, long timeToLiveMs) {
        String key = REFRESH_TOKEN_KEY_PREFIX + dto.getTokenHash();
        RBucket<RefreshTokenCacheDto> bucket = redissonClient.getBucket(key);
        bucket.set(dto, java.time.Duration.ofMillis(timeToLiveMs));
    }

    public Optional<RefreshTokenCacheDto> getToken(String tokenHash) {
        String key = REFRESH_TOKEN_KEY_PREFIX + tokenHash;
        RBucket<RefreshTokenCacheDto> bucket = redissonClient.getBucket(key);
        return Optional.ofNullable(bucket.get());
    }

    public void revokeToken(String tokenHash, long remainingTtlMs) {
        String key = REFRESH_TOKEN_KEY_PREFIX + tokenHash;
        RBucket<RefreshTokenCacheDto> bucket = redissonClient.getBucket(key);
        RefreshTokenCacheDto dto = bucket.get();
        if (dto != null) {
            dto.setRevoked(true);
            if (remainingTtlMs > 0) {
                bucket.set(dto, java.time.Duration.ofMillis(remainingTtlMs));
            } else {
                bucket.delete();
            }
        }
    }

    public void deleteToken(String tokenHash) {
        String key = REFRESH_TOKEN_KEY_PREFIX + tokenHash;
        RBucket<RefreshTokenCacheDto> bucket = redissonClient.getBucket(key);
        bucket.delete();
    }
}
