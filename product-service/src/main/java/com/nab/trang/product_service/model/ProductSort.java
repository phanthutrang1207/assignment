package com.nab.trang.product_service.model;

import org.springframework.data.domain.Sort;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProductSort {
	
	PRICE_ASC("PRICE_ASC", buildSortOderByASC("price")),
    PRICE_DES("PRICE_DES", buildSortOderByDESC("price")),
    NAME_ASC("NAME_ASC", buildSortOderByASC("name")),
    NAME_DES("NAME_DES", buildSortOderByDESC("name")),
    DISCOUNT_ASC("DISCOUNT_ASC", buildSortOderByASC("discountRate")),
    DISCOUNT_DES("DISCOUNT_DES", buildSortOderByDESC("discountRate")),
    BRAND_NAME_ASC("BRAND_NAME_ASC", buildSortOderByASC("brandName")),
    BRAND_NAME_DES("BRAND_NAME_DES", buildSortOderByDESC("brandName")),
    IMAGE_LINK_ASC("IMAGE_LINK_ASC", buildSortOderByASC("imageLink")),
    IMAGE_LINK_DES("IMAGE_LINK_DES", buildSortOderByDESC("imageLink")),
    PROVIDER_PRODUCT_ID_ASC("PROVIDER_PRODUCT_ID_ASC", buildSortOderByASC("providerProductId")),
    PROVIDER_PRODUCT_ID_DES("PROVIDER_PRODUCT_ID_DES", buildSortOderByDESC("providerProductId")),
    PROVIDER_NAME_ASC("PROVIDER_NAME_ASC", buildSortOderByASC("providerName")),
    PROVIDER_NAME_DES("PROVIDER_NAME_DES", buildSortOderByDESC("providerName")),
    SEARCH_KEY_ASC("SEARCH_KEY_ASC", buildSortOderByASC("searchKey")),
    SEARCH_KEY_DES("SEARCH_KEY_DES", buildSortOderByDESC("searchKey"));

    private String value;
    private Sort sortType;

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(this.value);
    }

    @JsonCreator
    public static ProductSort fromValue(String text) {
        for (ProductSort b : ProductSort.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }

    private static Sort buildSortOderByASC(String property) {
        return Sort.by(new Sort.Order(Sort.Direction.ASC, property, Sort.NullHandling.NULLS_LAST).ignoreCase());
    }

    private static Sort buildSortOderByDESC(String property) {
    	return Sort.by(new Sort.Order(Sort.Direction.DESC, property, Sort.NullHandling.NULLS_LAST).ignoreCase());
    }

}
