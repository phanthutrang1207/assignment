package com.nab.trang.product_service.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@RedisHash(timeToLive = 24*60*60)
@Data
@Builder
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
	
	@NonNull
	private String name;
	
	private Long price;
	
	private String discountRate;
	
	private String brandName;
	
	private String imageLink;
	
	@NonNull
	@Indexed
	private Long providerProductId;
	
	@NonNull
	@Indexed
	private String providerName;
	
	@NonNull
	@Indexed
	private String searchKey;
}
