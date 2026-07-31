package com.Wiinvent.Lotus.domain.point.repository;

import com.Wiinvent.Lotus.core.repository.BaseRepository;
import com.Wiinvent.Lotus.domain.point.entity.PointTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface PointTransactionRepository extends BaseRepository<PointTransaction, Long> {

    Page<PointTransaction> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}
