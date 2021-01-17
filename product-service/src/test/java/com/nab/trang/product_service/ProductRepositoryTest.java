package com.nab.trang.product_service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.nab.trang.product_service.model.Product;
import com.nab.trang.product_service.model.ProductSort;
import com.nab.trang.product_service.repository.ProductRepository;

import redis.embedded.RedisServer;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
public class ProductRepositoryTest {
	
	private static boolean setUpIsDone = false;
	
	@TestConfiguration
    static class TestRedisConfiguration {
		private RedisServer redisServer;
		
		private int redisPort = 6370;

	    public TestRedisConfiguration() {
	        this.redisServer = new RedisServer(redisPort);
	    }

	    @PostConstruct
	    public void postConstruct() {
	        redisServer.start();
	    }

	    @PreDestroy
	    public void preDestroy() {
	        redisServer.stop();
	    }
    }
	
	@Autowired
	private ProductRepository productRepository;
	
	@Before
	public void setUp() {
	    if (setUpIsDone) {
	        return;
	    }
	    Product p = Product.builder().name("iphone").searchKey("iphone").providerName("tiki").providerProductId(111L).build();
		productRepository.save(p);
	    setUpIsDone = true;
	}
	
	@Test
	public void testFindByProviderProductIdAndProviderNameSuccessful() {
		Product found = productRepository.findByProviderProductIdAndProviderName(111L, "tiki");
		
		assertTrue(Objects.nonNull(found));
		assertEquals("iphone", found.getName());
		assertEquals(111L, found.getProviderProductId());
	}
	
	@Test
	public void testFindByProviderProductIdAndProviderNameWithNotFoundProduct() {
		Product notFound = productRepository.findByProviderProductIdAndProviderName(222L, "tiki");
		
		assertTrue(Objects.isNull(notFound));
	}
	
	@Test
	public void testFindProductsBySearchKeySuccessful() {
		Pageable pageable = PageRequest.of(0, 5, ProductSort.PRICE_DES.getSortType());
		List<Product> products = productRepository.findProductsBySearchKey("iphone", pageable);
		
		assertFalse(products.isEmpty());
		assertEquals(1, products.size());
	}
	
	@Test
	public void testFindProductsBySearchKeySuccessfulWithEmptyList() {
		Pageable pageable = PageRequest.of(0, 5, ProductSort.PRICE_DES.getSortType());
		List<Product> products = productRepository.findProductsBySearchKey("samsung", pageable);
		
		assertTrue(products.isEmpty());
	}

}
