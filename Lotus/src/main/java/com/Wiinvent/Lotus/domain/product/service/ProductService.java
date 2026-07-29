package com.Wiinvent.Lotus.domain.product.service;

import com.Wiinvent.Lotus.core.service.BaseService;
import com.Wiinvent.Lotus.domain.product.dto.ProductRequest;
import com.Wiinvent.Lotus.domain.product.dto.ProductResponse;
import com.Wiinvent.Lotus.domain.product.entity.Product;

public interface ProductService extends BaseService<Product, Long, ProductRequest, ProductResponse> {
}
