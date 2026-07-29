package com.Wiinvent.Lotus.domain.user.repository;

import com.Wiinvent.Lotus.core.repository.BaseRepository;
import com.Wiinvent.Lotus.domain.user.entity.User;
import com.Wiinvent.Lotus.domain.user.entity.UserRole;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User, Long> {

    Optional<User> findByPhone(String phone);

    boolean existsByPhone(String phone);

    boolean existsByRole(UserRole role);
}
