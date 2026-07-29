package com.Wiinvent.Lotus.domain.product.service;

import com.Wiinvent.Lotus.core.dto.PageResponse;
import com.Wiinvent.Lotus.core.exception.ResourceNotFoundException;
import com.Wiinvent.Lotus.domain.product.dto.ProductRequest;
import com.Wiinvent.Lotus.domain.product.dto.ProductResponse;
import com.Wiinvent.Lotus.domain.product.entity.Product;
import com.Wiinvent.Lotus.domain.product.repository.ProductRepository;
import com.Wiinvent.Lotus.domain.product.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private ProductRequest productRequest;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .name("Laptop")
                .description("Gaming Laptop")
                .price(BigDecimal.valueOf(1200))
                .stockQuantity(10)
                .build();
        product.setId(1L);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        productRequest = ProductRequest.builder()
                .name("Laptop")
                .description("Gaming Laptop")
                .price(BigDecimal.valueOf(1200))
                .stockQuantity(10)
                .build();
    }

    @Test
    @DisplayName("Should create entity successfully using generic service")
    void shouldCreateEntitySuccessfully() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponse response = productService.create(productRequest);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Laptop");
        verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("Should find entity by ID using generic service")
    void shouldFindEntityByIdSuccessfully() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponse response = productService.findById(1L);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Laptop");
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when entity ID is missing")
    void shouldThrowExceptionWhenNotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Product not found with id: '99'");
    }

    @Test
    @DisplayName("Should find all entities paginated using generic service")
    void shouldFindAllPaginated() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> page = new PageImpl<>(List.of(product), pageable, 1);
        when(productRepository.findAll(pageable)).thenReturn(page);

        PageResponse<ProductResponse> result = productService.findAll(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should update entity successfully using generic service")
    void shouldUpdateEntitySuccessfully() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductRequest updateReq = ProductRequest.builder()
                .name("Updated Laptop")
                .build();

        ProductResponse response = productService.update(1L, updateReq);

        assertThat(response).isNotNull();
        verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("Should delete entity by ID using generic service")
    void shouldDeleteEntityByIdSuccessfully() {
        when(productRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1L);

        productService.deleteById(1L);

        verify(productRepository).deleteById(1L);
    }
}
