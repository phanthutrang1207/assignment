package com.nab.trang.product_service.service;

import java.util.List;

import com.nab.trang.product_service.model.Product;

public interface ProviderService {
	
	public List<Product> getProductListBySearchKey(String key);
}
