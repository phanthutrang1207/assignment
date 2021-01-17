package com.nab.trang.product_service.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nab.trang.product_service.model.Product;
import com.nab.trang.product_service.model.ProductSort;
import com.nab.trang.product_service.service.MessageProducer;
import com.nab.trang.product_service.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private MessageProducer messageProducer;
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getProductBySearchKey(@RequestParam String key,
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "5") Integer size,
			@RequestParam(value = "sorting", defaultValue = "PRICE_DES") ProductSort sorting) {
		Pageable pagingable = PageRequest.of(page, size, sorting.getSortType());
		List<Product> products = productService.findProductBySearchKey(key, pagingable);
		messageProducer.send(key);
		return ResponseEntity.ok(products);
	}
	
	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getProductDetail(@PathVariable String id, @RequestParam String providerName) {
		Long providerProductId = Long.valueOf(id);
		Optional<Product> product = productService.getProductByProviderProductIdAndProviderName(providerProductId, providerName);
		if (product.isPresent()) {
			return ResponseEntity.ok(product);			
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Product id %s of the providerName %s not found", id, providerName));
	}
}
