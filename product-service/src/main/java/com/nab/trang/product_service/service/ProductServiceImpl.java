package com.nab.trang.product_service.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.nab.trang.product_service.model.Product;
import com.nab.trang.product_service.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	ProviderService providerService;
	
	@Autowired
	ProductRepository productRepository;
	
	@Override
	public List<Product> findProductBySearchKey(String key, Pageable pagable) {
		List<Product> products = findProductBySearchKeyInDb(key, pagable);
		if (Objects.isNull(products) || products.isEmpty()) {
			List<Product> result = providerService.getProductListBySearchKey(key);
			productRepository.saveAll(result);
			products = findProductBySearchKeyInDb(key, pagable);
		}
		return products;
	}

	private List<Product> findProductBySearchKeyInDb(String key, Pageable pagable) {
		return productRepository.findProductsBySearchKey(key, pagable);
	}

	@Override
	public Optional<Product> getProductByProviderProductIdAndProviderName(Long providerProductId, String providerName) {
		Product product = productRepository.findByProviderProductIdAndProviderName(providerProductId, providerName);
		return Optional.ofNullable(product);
	}
}
