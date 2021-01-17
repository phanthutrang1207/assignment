package com.nab.trang.product_service.model;

import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProviderProduct {
	
	private String id;
	private String name;
	private Map<String, Object> details;

}
