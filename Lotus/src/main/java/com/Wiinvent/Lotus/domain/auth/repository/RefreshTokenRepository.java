package com.Wiinvent.Lotus.domain.auth.repository;

import com.Wiinvent.Lotus.core.repository.BaseRepository;
import com.Wiinvent.Lotus.domain.auth.entity.RefreshToken;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends BaseRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByTokenHashAndRevokedFalse(String tokenHash);
}
