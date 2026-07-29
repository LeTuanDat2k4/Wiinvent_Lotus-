package com.Wiinvent.Lotus.core.service;

import com.Wiinvent.Lotus.core.dto.PageResponse;
import com.Wiinvent.Lotus.core.entity.BaseEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;

public interface BaseService<T extends BaseEntity<ID>, ID extends Serializable, ReqDTO, ResDTO> {

    ResDTO create(ReqDTO requestDTO);

    ResDTO findById(ID id);

    PageResponse<ResDTO> findAll(Pageable pageable);

    PageResponse<ResDTO> findAll(Specification<T> spec, Pageable pageable);

    ResDTO update(ID id, ReqDTO requestDTO);

    void deleteById(ID id);

    boolean existsById(ID id);
}
