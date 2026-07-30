package com.Wiinvent.Lotus.domain.reward.repository;

import com.Wiinvent.Lotus.core.repository.BaseRepository;
import com.Wiinvent.Lotus.domain.reward.entity.RewardConfig;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RewardConfigRepository extends BaseRepository<RewardConfig, Long> {

    Optional<RewardConfig> findByDayNumber(Integer dayNumber);

    List<RewardConfig> findAllByOrderByDayNumberAsc();
}
