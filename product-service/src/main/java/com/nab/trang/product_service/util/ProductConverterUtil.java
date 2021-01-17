package com.nab.trang.product_service.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.nab.trang.product_service.model.Product;
import com.nab.trang.product_service.model.Provider;

public class ProductConverterUtil {
	
	public static final String DATA_KEY = "data";
	public static final String ICON = "icon";
	public static final String DISCOUNT = "discount";
	public static final String BRAND_NAME = "brand_name";
	public static final String BRAND_NAME_SHOPEE = "brand";
	public static final String TIKI_PRODUCT_PROVIDER_ID = "id";
	public static final String SHOPEE_PRODUCT_PROVIDER_ID = "itemid";
	public static final String PRICE = "price";
	public static final String NAME = "name";
	public static final String PERCENT = "%";
	public static final String ITEMS = "items";
	
	public static List<Product> convertProductFromProviderResult(List<LinkedHashMap<String, Object>> resourceList, String searchKey, Provider provider) {
		List<Product> products = new ArrayList<>();
		switch (provider) {
		case TIKI:
			resourceList.forEach(resource -> {
				Product product = Product.builder()
						.searchKey(searchKey)
						.name((String) resource.get(NAME))
						.price(getLongValueFromObject(resource.get(PRICE)))
						.providerProductId(getLongValueFromObject(resource.get(TIKI_PRODUCT_PROVIDER_ID)))
						.brandName((String)resource.get(BRAND_NAME))
						.discountRate(String.valueOf(getLongValueFromObject(DISCOUNT)) + PERCENT)
						.imageLink((String)resource.get(ICON))
						.providerName(Provider.TIKI.value()).build();
				products.add(product);
			});
			break;
		
		case SHOPEE:
			resourceList.forEach(resource -> {
				Product product = Product.builder()
						.searchKey(searchKey)
						.name((String) resource.get(NAME))
						.price(getLongValueFromObject(resource.get(PRICE)))
						.providerProductId(getLongValueFromObject(resource.get(SHOPEE_PRODUCT_PROVIDER_ID)))
						.brandName((String)resource.get(BRAND_NAME_SHOPEE))
						.discountRate((String) resource.get(DISCOUNT))
						.imageLink((String)resource.get(ICON))
						.providerName(Provider.SHOPEE.value()).build();
				products.add(product);
			});
			break;

		default:
			break;
		}
		return products;
	}
	
	private static long getLongValueFromObject(Object inputValue) {
		long longValue = 0L;
		int intValue = 0;
		if (inputValue instanceof Long) {
			longValue = (Long) inputValue;
		} else if (inputValue instanceof Integer) {
			intValue = ((Integer) inputValue).intValue();
			longValue = (long) intValue;
		}
		return longValue;
	}

}
