package com.Wiinvent.Lotus.domain.checkin.entity;

import com.Wiinvent.Lotus.core.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "check_in_records")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckInRecord extends BaseEntity<Long> {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "check_in_date", nullable = false)
    private LocalDate checkInDate;

    @Column(name = "day_number_in_month", nullable = false)
    private Integer dayNumberInMonth;

    @Column(name = "reward_point", nullable = false)
    private Long rewardPoint;
}
