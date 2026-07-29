package com.Wiinvent.Lotus.domain.product.controller;

import com.Wiinvent.Lotus.core.controller.BaseController;
import com.Wiinvent.Lotus.domain.product.dto.ProductRequest;
import com.Wiinvent.Lotus.domain.product.dto.ProductResponse;
import com.Wiinvent.Lotus.domain.product.entity.Product;
import com.Wiinvent.Lotus.domain.product.service.ProductService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController extends BaseController<Product, Long, ProductRequest, ProductResponse> {

    public ProductController(ProductService productService) {
        super(productService);
    }
}
