package com.nab.trang.product_service.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import com.nab.trang.product_service.util.ProductConverterUtil;;

@Service("tiki")
public class TikiProviderService implements ProviderService {
	
	private static final Logger LOGGER = LogManager.getLogger(TikiProviderService.class);
	
	@Autowired
	private RestTemplate restTemplate;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Product> getProductListBySearchKey(String key) {
		LOGGER.info("Start searching product from Tiki provider");
		List<Product> products = new ArrayList<>();
		ResponseEntity<String> tikiResource = restTemplate.getForEntity(Provider.TIKI.searchApiUrl() + key, String.class);
		
		if (tikiResource.getStatusCode() == HttpStatus.OK && tikiResource.hasBody()) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				Map<String, List<LinkedHashMap<String, Object>>> valueMap = mapper.readValue(tikiResource.getBody(), Map.class);
				List<LinkedHashMap<String, Object>> resourceList = valueMap.get(ProductConverterUtil.DATA_KEY);
				if (!resourceList.isEmpty()) {
					products.addAll(ProductConverterUtil.convertProductFromProviderResult(resourceList, key, Provider.TIKI));
				}
			} catch (Exception e) {
				LOGGER.error("Exception while parsing value from resource mapper to value map", e);
			}
		} else {
			LOGGER.info("Have no results from Tiki provider");
		}
		LOGGER.info("Finish searching products from Tiki provider");
		return products;
	}
}
