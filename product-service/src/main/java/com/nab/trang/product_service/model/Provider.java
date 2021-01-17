package com.nab.trang.product_service.model;

public enum Provider {
	
	TIKI("Tiki", "https://tiki.vn/api/v2/products?q="),
	SHOPEE("Shopee", "https://shopee.vn/api/v2/search_items/?by=relevancy&keyword=");
	
	private final String value;
	private final String searchApiUrl;
	
	Provider(String v, String s) {
		value = v;
		searchApiUrl = s;
	}
	
	public String value() {
		return value;
	}
	
	public String searchApiUrl() {
		return searchApiUrl;
	}

}
