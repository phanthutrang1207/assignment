package com.nab.trang.product_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.nab.trang.product_service.model.Product;
import com.nab.trang.product_service.model.Provider;

@RunWith(SpringRunner.class)
public class ShopeeProviderServiceTest {
	
	@TestConfiguration
    static class ProviderServiceTestContextConfiguration {
        @Bean
        public ProviderService providerService() {
            return new ShopeeProviderService();
        }
    }
	
	@Autowired
	private ProviderService shopeeProviderService;
	
	@MockBean
	private RestTemplate mockRestTemplate;
	
	@Test
	public void testGetProductListBySearchKeySuccessfully() {
		ResponseEntity<String> shopeeResourceDatas = getMockShopeeResourceDatas();
		when(mockRestTemplate.getForEntity(Provider.SHOPEE.searchApiUrl() + "iphone", String.class)).thenReturn(shopeeResourceDatas);
		
		List<Product> products = shopeeProviderService.getProductListBySearchKey("iphone");
		
		verify(mockRestTemplate, times(1)).getForEntity(Provider.SHOPEE.searchApiUrl() + "iphone", String.class);
		assertEquals(2, products.size());
	}
	
	private ResponseEntity<String> getMockShopeeResourceDatas() {
		String body = "{\"show_disclaimer\":true,\"query_rewrite\":{},\"adjust\":{\"count\":1430062},\"items\":[{\"itemid\":6660601659,\"image\":\"f9a4003f182632fac31dad9ea07\",\"raw_discount\":21,\"brand\":\"Apple\",\"price\":1748227609523,\"options\":[\"Black\",\"White\",\"Blue\",\"Coral\",\"Y\",\"Red\"],\"discount\":\"16%\",\"name\":\"Apple iPhone XR\"},{\"itemid\":6660601656,\"image\":\"f9a4003f182632fac31dad9ea07\",\"raw_discount\":20,\"brand\":\"Apple\",\"price\":1748227609524,\"options\":[\"Black\",\"White\",\"Blue\",\"Coral\",\"Y\",\"Red\"],\"discount\":\"18%\",\"name\":\"Apple iPhone XR\"}]}";
		return new ResponseEntity<String>(body, HttpStatus.OK);
	}

}
