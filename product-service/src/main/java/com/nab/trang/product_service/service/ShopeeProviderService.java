package com.nab.trang.product_service.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nab.trang.product_service.model.Product;
import com.nab.trang.product_service.model.Provider;
import com.nab.trang.product_service.util.ProductConverterUtil;

@Service("shopee")
public class ShopeeProviderService implements ProviderService {
	private static final Logger LOGGER = LogManager.getLogger(ShopeeProviderService.class);
	
	@Autowired
	private RestTemplate restTemplate;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Product> getProductListBySearchKey(String key) {
		LOGGER.info("Start searching products from Shopee provider");
		List<Product> products = new ArrayList<>();
		ResponseEntity<String> shopeeResource = restTemplate.getForEntity(Provider.SHOPEE.searchApiUrl() + key,
				String.class);
		if (shopeeResource.getStatusCode() == HttpStatus.OK && shopeeResource.hasBody()) {
			try {
				String data = shopeeResource.getBody();
				ObjectMapper mapper = new ObjectMapper();
				Map<String, Object> values = mapper.readValue(data, Map.class);
				List<LinkedHashMap<String, Object>> resourceList = (List<LinkedHashMap<String, Object>>) values.get(ProductConverterUtil.ITEMS);
				if (Objects.nonNull(resourceList) && !resourceList.isEmpty()) {
					products = ProductConverterUtil.convertProductFromProviderResult(resourceList, key, Provider.SHOPEE);					
				}
			} catch (Exception e) {
				LOGGER.error("Exception while parsing value from resource mapper to value map", e);
			}
		} else {
			LOGGER.info("Have no result from Shopee provider");
		}
		LOGGER.info("Finish searching products from Shopee provider");
		return products;
	}
}
