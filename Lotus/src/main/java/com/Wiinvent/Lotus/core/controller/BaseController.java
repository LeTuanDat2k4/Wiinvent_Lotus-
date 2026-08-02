package com.Wiinvent.Lotus.core.controller;

import com.Wiinvent.Lotus.core.dto.ApiResponse;
import com.Wiinvent.Lotus.core.dto.PageResponse;
import com.Wiinvent.Lotus.core.dto.PagingRequest;
import com.Wiinvent.Lotus.core.entity.BaseEntity;
import com.Wiinvent.Lotus.core.service.BaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.Serializable;

public abstract class BaseController<T extends BaseEntity<ID>, ID extends Serializable, ReqDTO, ResDTO> {

    protected final BaseService<T, ID, ReqDTO, ResDTO> service;

    protected BaseController(BaseService<T, ID, ReqDTO, ResDTO> service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ResDTO>> create(@RequestBody ReqDTO requestDTO) {
        ResDTO created = service.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ResDTO>> findById(@PathVariable ID id) {
        ResDTO found = service.findById(id);
        return ResponseEntity.ok(ApiResponse.success(found));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ResDTO>>> findAll(PagingRequest pagingRequest) {
        PageResponse<ResDTO> page = service.findAll(pagingRequest.getPageable());
        return ResponseEntity.ok(ApiResponse.success(page));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ResDTO>> update(@PathVariable ID id, @RequestBody ReqDTO requestDTO) {
        ResDTO updated = service.update(id, requestDTO);
        return ResponseEntity.ok(ApiResponse.success("Resource updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable ID id) {
        service.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success("Resource deleted successfully", null));
    }
}
