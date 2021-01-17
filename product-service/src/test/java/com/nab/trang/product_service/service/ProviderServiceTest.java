package com.nab.trang.product_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.nab.trang.product_service.model.Product;

@RunWith(SpringRunner.class)
public class ProviderServiceTest {

	private static final String MOCK_SEARCH_KEY = "iphone";

	@TestConfiguration
    static class ProviderServiceTestContextConfiguration {
        @Bean
        @Qualifier("providerCenter")
        public ProviderService providerService() {
            return new ProviderServiceImpl();
        }
        
        @Bean
        @Qualifier("tiki")
        public ProviderService tikiProviderService() {
            return Mockito.mock(TikiProviderService.class);
        }
        
        @Bean
        @Qualifier("shopee")
        public ProviderService shopeeProviderService() {
            return Mockito.mock(ShopeeProviderService.class);
        }
        
        @Bean
        public List<ProviderService> providerServices() {
            return Arrays.asList(tikiProviderService(), shopeeProviderService());
        }
    }
	
	@Autowired
	@Qualifier("providerCenter")
	private ProviderService testProviderService;
	
	@MockBean
	@Qualifier("tiki")
	private ProviderService tikiProviderService;
	
	@MockBean
	@Qualifier("shopee")
	private ProviderService shopeeProviderService;
	
	@MockBean
	private List<ProviderService> services;
	
	@Test
	public void testGetProductListBySearchKey() {
		Product product = Product.builder().name(MOCK_SEARCH_KEY).providerName("tiki").providerProductId(111L).searchKey(MOCK_SEARCH_KEY).build();
		
		when(tikiProviderService.getProductListBySearchKey(MOCK_SEARCH_KEY)).thenReturn(Arrays.asList(product));
		
		List<Product> products = testProviderService.getProductListBySearchKey(MOCK_SEARCH_KEY);
		
		verify(tikiProviderService, times(1)).getProductListBySearchKey(MOCK_SEARCH_KEY);
		verify(shopeeProviderService, times(1)).getProductListBySearchKey(MOCK_SEARCH_KEY);
		
		assertEquals(products.size(), 1);
	}
	
	@Test
	public void testGetProductListBySearchKeyWithExceptionFromOneProvider() {
		Product product = Product.builder().name(MOCK_SEARCH_KEY).providerName("tiki").providerProductId(111L).searchKey(MOCK_SEARCH_KEY).build();
		
		when(tikiProviderService.getProductListBySearchKey(MOCK_SEARCH_KEY)).thenReturn(Arrays.asList(product));
		when(shopeeProviderService.getProductListBySearchKey(MOCK_SEARCH_KEY)).thenThrow(new RuntimeException());
		
		List<Product> products = testProviderService.getProductListBySearchKey(MOCK_SEARCH_KEY);
		
		verify(tikiProviderService, times(1)).getProductListBySearchKey(MOCK_SEARCH_KEY);
		verify(shopeeProviderService, times(1)).getProductListBySearchKey(MOCK_SEARCH_KEY);
		
		assertEquals(products.size(), 1);
	}
	
	@Test
	public void testGetProductListBySearchKeyWithExceptionFromTwoProvider() {
		
		when(tikiProviderService.getProductListBySearchKey(MOCK_SEARCH_KEY)).thenThrow(new RuntimeException());
		when(shopeeProviderService.getProductListBySearchKey(MOCK_SEARCH_KEY)).thenThrow(new RuntimeException());
		
		List<Product> products = testProviderService.getProductListBySearchKey(MOCK_SEARCH_KEY);
		
		verify(tikiProviderService, times(1)).getProductListBySearchKey(MOCK_SEARCH_KEY);
		verify(shopeeProviderService, times(1)).getProductListBySearchKey(MOCK_SEARCH_KEY);
		
		assertEquals(products.size(), 0);
	}
}
