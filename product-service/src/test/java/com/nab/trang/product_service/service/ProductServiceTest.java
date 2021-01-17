package com.nab.trang.product_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import com.nab.trang.product_service.model.Product;
import com.nab.trang.product_service.model.Provider;
import com.nab.trang.product_service.repository.ProductRepository;


@RunWith(SpringRunner.class)
public class ProductServiceTest {
	
	@TestConfiguration
    static class ProductServiceTestContextConfiguration {
        @Bean
        public ProductService productService() {
            return new ProductServiceImpl();
        }
    }

	@Autowired
	private ProductService productService;
	
	@MockBean
	private ProviderService providerService;
	
	@MockBean
	private ProductRepository productRepository;
	
	private PageRequest pageable = PageRequest.of(1, 5, Sort.by("price"));
	
	@Test
	public void testFindProductBySearchKeySuccessfullyWithEmptyList() {
		List<Product> result = getMockDataFromProvider();
		when(productRepository.saveAll(ArgumentMatchers.anyIterable())).thenReturn(Collections.emptyList());
		when(productRepository.findProductsBySearchKey("iphone", pageable)).thenReturn(Collections.emptyList());
		when(providerService.getProductListBySearchKey(anyString())).thenReturn(result);
		
		List<Product> products = productService.findProductBySearchKey("iphone", pageable);
		assertEquals(0, products.size());
	}
	
	@Test
	public void testFindProductBySearchKeyWithNotCallProviderService() {
		List<Product> result = getMockDataFromProvider();
		when(productRepository.findProductsBySearchKey("iphone", pageable)).thenReturn(result);
		
		List<Product> products = productService.findProductBySearchKey("iphone", pageable);
		assertEquals(3, products.size());
		
		verify(providerService, times(0)).getProductListBySearchKey("iphone");
	}
	
	@Test
	public void testFindProductBySearchKeyWithCallProviderService() {
		List<Product> result = getMockDataFromProvider();
		when(productRepository.findProductsBySearchKey("iphone", pageable))
			.thenReturn(Collections.emptyList())
			.thenReturn(result);
		
		List<Product> products = productService.findProductBySearchKey("iphone", pageable);
		
		assertEquals(3, products.size());
		verify(providerService, times(1)).getProductListBySearchKey("iphone");
		
	}
	
	@Test
	public void testGetProductByProviderProductIdAndProviderNameReturnWithEmptyOptional() {
		when(productRepository.findByProviderProductIdAndProviderName(1234L, "tiki")).thenReturn(null);
		
		Optional<Product> result = productService.getProductByProviderProductIdAndProviderName(1234L, "tiki");
		
		assertEquals(result.isPresent(), false);
	}
	
	@Test
	public void testGetProductByProviderProductIdAndProviderNameReturnProduct() {
		Product product = Product.builder()
				.searchKey("iphone")
				.name("iphone12ProMax")
				.price(34000000L)
				.providerProductId(1234L)
				.brandName("Apple")
				.discountRate("14%")
				.imageLink(null)
				.providerName(Provider.TIKI.value()).build();
		
		when(productRepository.findByProviderProductIdAndProviderName(1234L, "tiki")).thenReturn(product);
		
		Optional<Product> result = productService.getProductByProviderProductIdAndProviderName(1234L, "tiki");
		
		assertEquals(result.isPresent(), true);
		
		Product found = result.get();
		assertEquals(found, product);
	}
	
	
	private List<Product> getMockDataFromProvider() {
		List<Product> products = new ArrayList<>();
		Product product1 = Product.builder()
				.searchKey("iphone")
				.name("iphone11ProMax")
				.price(29000000L)
				.providerProductId(1L)
				.brandName("Apple")
				.discountRate("13%")
				.imageLink(null)
				.providerName(Provider.TIKI.value()).build();
		
		Product product2 = Product.builder()
				.searchKey("iphone")
				.name("iphone12ProMax")
				.price(34000000L)
				.providerProductId(1L)
				.brandName("Apple")
				.discountRate("14%")
				.imageLink(null)
				.providerName(Provider.TIKI.value()).build();
		
		Product product3 = Product.builder()
				.searchKey("iphone")
				.name("iphoneXR")
				.price(12000000L)
				.providerProductId(1L)
				.brandName("Apple")
				.discountRate("15%")
				.imageLink(null)
				.providerName(Provider.TIKI.value()).build();
		
		products.add(product1);
		products.add(product2);
		products.add(product3);
		return products;
	}
}
