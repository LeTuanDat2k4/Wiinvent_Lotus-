package com.Wiinvent.Lotus.domain.point.entity;

import com.Wiinvent.Lotus.core.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "point_transactions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointTransaction extends BaseEntity<Long> {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PointTransactionType type;

    @Column(nullable = false)
    private Long amount;

    @Column(name = "balance_after", nullable = false)
    private Long balanceAfter;

    @Column(length = 500)
    private String reason;
}
