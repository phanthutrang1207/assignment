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
public class TikiProviderServiceTest {
	
	@TestConfiguration
    static class ProviderServiceTestContextConfiguration {
        @Bean
        public ProviderService providerService() {
            return new TikiProviderService();
        }
    }
	
	@Autowired
	private ProviderService tikiProviderService;
	
	@MockBean
	private RestTemplate mockRestTemplate;
	
	@Test
	public void testGetProductListBySearchKeySuccessfully() {
		ResponseEntity<String> tikiResourceDatas = getMockTikiResourceDatas();
		when(mockRestTemplate.getForEntity(Provider.TIKI.searchApiUrl() + "iphone", String.class)).thenReturn(tikiResourceDatas);
		
		List<Product> products = tikiProviderService.getProductListBySearchKey("iphone");
		
		verify(mockRestTemplate, times(1)).getForEntity(Provider.TIKI.searchApiUrl() + "iphone", String.class);
		assertEquals(2, products.size());
	}
	
	private ResponseEntity<String> getMockTikiResourceDatas() {
		String body = "{\"data\":[{\"id\":4538233,\"name\":\"iphone XR 64Gb\",\"url_path\":\"dien-thoai-iphone-xr-64gb-hang-chinh-hang-p4538233.html\",\"brand_name\":\"Apple\",\"price\":11990000,\"list_price\":22990000,\"code\":\"tikinow\",\"icon\":\"https:\\/\\/salt.tikicdn.com\\/ts\\/upload\\/9f\\/32\\/dd\\/8a8d39d4453399569dfb3e80fe01de75.png\",\"discount\":11000000},{\"id\":4538234,\"name\":\"iphone 11 Promax 64Gb\",\"url_path\":\"dien-thoai-iphone-11-promax-64gb-hang-chinh-hang-p4538233.html\",\"brand_name\":\"Apple\",\"price\":28990000,\"list_price\":32990000,\"code\":\"tikinow\",\"icon\":\"https:\\/\\/salt.tikicdn.com\\/ts\\/upload\\/9f\\/32\\/dd\\/8a8d39d4453399569dfb3e80fe01de75.png\",\"discount\":11000000}]}";
		return new ResponseEntity<String>(body, HttpStatus.OK);
	}

}
