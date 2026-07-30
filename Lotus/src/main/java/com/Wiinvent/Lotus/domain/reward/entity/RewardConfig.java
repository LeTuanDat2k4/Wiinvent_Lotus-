package com.Wiinvent.Lotus.domain.reward.entity;

import com.Wiinvent.Lotus.core.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reward_config")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RewardConfig extends BaseEntity<Long> {

    @Column(name = "day_number", nullable = false, unique = true)
    private Integer dayNumber;

    @Column(name = "reward_point", nullable = false)
    private Long rewardPoint;
}
