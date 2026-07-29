package com.Wiinvent.Lotus.domain.product.service.impl;

import com.Wiinvent.Lotus.core.service.impl.BaseServiceImpl;
import com.Wiinvent.Lotus.domain.product.dto.ProductRequest;
import com.Wiinvent.Lotus.domain.product.dto.ProductResponse;
import com.Wiinvent.Lotus.domain.product.entity.Product;
import com.Wiinvent.Lotus.domain.product.repository.ProductRepository;
import com.Wiinvent.Lotus.domain.product.service.ProductService;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl
        extends BaseServiceImpl<Product, Long, ProductRequest, ProductResponse>
        implements ProductService {

    public ProductServiceImpl(ProductRepository productRepository) {
        super(productRepository, "Product");
    }

    @Override
    protected Product toEntity(ProductRequest requestDTO) {
        return Product.builder()
                .name(requestDTO.getName())
                .description(requestDTO.getDescription())
                .price(requestDTO.getPrice())
                .stockQuantity(requestDTO.getStockQuantity())
                .build();
    }

    @Override
    protected ProductResponse toResponseDto(Product entity) {
        return ProductResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .stockQuantity(entity.getStockQuantity())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    @Override
    protected void updateEntityFromDto(Product entity, ProductRequest requestDTO) {
        if (requestDTO.getName() != null) {
            entity.setName(requestDTO.getName());
        }
        if (requestDTO.getDescription() != null) {
            entity.setDescription(requestDTO.getDescription());
        }
        if (requestDTO.getPrice() != null) {
            entity.setPrice(requestDTO.getPrice());
        }
        if (requestDTO.getStockQuantity() != null) {
            entity.setStockQuantity(requestDTO.getStockQuantity());
        }
    }
}
