package com.Wiinvent.Lotus.core.service.impl;

import com.Wiinvent.Lotus.core.dto.PageResponse;
import com.Wiinvent.Lotus.core.entity.BaseEntity;
import com.Wiinvent.Lotus.core.exception.ResourceNotFoundException;
import com.Wiinvent.Lotus.core.repository.BaseRepository;
import com.Wiinvent.Lotus.core.service.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@Transactional(readOnly = true)
public abstract class BaseServiceImpl<T extends BaseEntity<ID>, ID extends Serializable, ReqDTO, ResDTO>
        implements BaseService<T, ID, ReqDTO, ResDTO> {

    protected final BaseRepository<T, ID> repository;
    protected final String entityName;

    protected BaseServiceImpl(BaseRepository<T, ID> repository, String entityName) {
        this.repository = repository;
        this.entityName = entityName;
    }

    protected abstract T toEntity(ReqDTO requestDTO);

    protected abstract ResDTO toResponseDto(T entity);

    protected abstract void updateEntityFromDto(T entity, ReqDTO requestDTO);

    @Override
    @Transactional
    public ResDTO create(ReqDTO requestDTO) {
        T entity = toEntity(requestDTO);
        T savedEntity = repository.save(entity);
        return toResponseDto(savedEntity);
    }

    @Override
    public ResDTO findById(ID id) {
        T entity = getEntityById(id);
        return toResponseDto(entity);
    }

    @Override
    public PageResponse<ResDTO> findAll(Pageable pageable) {
        Page<T> page = repository.findAll(pageable);
        return PageResponse.from(page.map(this::toResponseDto));
    }

    @Override
    public PageResponse<ResDTO> findAll(Specification<T> spec, Pageable pageable) {
        Page<T> page = repository.findAll(spec, pageable);
        return PageResponse.from(page.map(this::toResponseDto));
    }

    @Override
    @Transactional
    public ResDTO update(ID id, ReqDTO requestDTO) {
        T entity = getEntityById(id);
        updateEntityFromDto(entity, requestDTO);
        T updatedEntity = repository.save(entity);
        return toResponseDto(updatedEntity);
    }

    @Override
    @Transactional
    public void deleteById(ID id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(entityName, "id", id);
        }
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(ID id) {
        return repository.existsById(id);
    }

    protected T getEntityById(ID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(entityName, "id", id));
    }
}
