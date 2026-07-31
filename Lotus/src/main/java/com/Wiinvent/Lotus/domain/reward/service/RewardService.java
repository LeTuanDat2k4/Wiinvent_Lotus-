package com.Wiinvent.Lotus.domain.reward.service;

import com.Wiinvent.Lotus.core.config.CacheConfig;
import com.Wiinvent.Lotus.domain.reward.entity.RewardConfig;
import com.Wiinvent.Lotus.domain.reward.repository.RewardConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RewardService {

    private final RewardConfigRepository rewardConfigRepository;

    @Cacheable(value = CacheConfig.REWARD_CONFIGS_CACHE, key = "'all'")
    public List<RewardConfig> getAllRewardConfigs() {
        return rewardConfigRepository.findAllByOrderByDayNumberAsc();
    }

    @Cacheable(value = CacheConfig.REWARD_CONFIG_DAY_CACHE, key = "#dayNumber")
    public Optional<RewardConfig> getRewardConfigByDay(Integer dayNumber) {
        return rewardConfigRepository.findByDayNumber(dayNumber);
    }
}
