package com.nab.trang.product_service.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.nab.trang.product_service.model.Product;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {
	
	public Product findByProviderProductIdAndProviderName(Long providerProductId, String providerName);
	
	public List<Product> findProductsBySearchKey(String searchKey, Pageable pageable);
}
