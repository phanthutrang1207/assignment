package com.nab.trang.product_service.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.nab.trang.product_service.model.Product;

public interface ProductService {
	
	public List<Product> findProductBySearchKey(String key, Pageable pagable);
	
	public Optional<Product> getProductByProviderProductIdAndProviderName(Long providerProductId, String providerName);

}
