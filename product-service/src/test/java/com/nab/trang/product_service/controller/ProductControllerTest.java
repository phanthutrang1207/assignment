package com.nab.trang.product_service.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.nab.trang.product_service.model.Product;
import com.nab.trang.product_service.model.ProductSort;
import com.nab.trang.product_service.service.MessageProducer;
import com.nab.trang.product_service.service.ProductService;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private ProductService productService;
    
    @MockBean
    private MessageProducer messageProducer;
    
    private Pageable pageable = PageRequest.of(0, 5, ProductSort.PRICE_DES.getSortType());
    
    @Before
    public void setup() {
    	doNothing().when(messageProducer).send(Mockito.anyString());
    }

    @Test
    public void testGetProductBySearchKeySuccessfullyWithEmptyList() throws Exception {
    	when(productService.findProductBySearchKey("iphone", pageable)).thenReturn(Collections.emptyList());
    	
    	mockMvc.perform(MockMvcRequestBuilders
    		      .get("/products")
    		      .param("key", "iphone")
    		      .accept(MediaType.APPLICATION_JSON))
    		      .andExpect(status().isOk())
    		      .andExpect(jsonPath("$", is(Collections.emptyList())));
    }
    
    @Test
    public void testGetProductBySearchKeySuccessfullyWithListOfProduct() throws Exception {
    	Product mockProduct = Product.builder().name("Iphone 1").providerName("tiki").providerProductId(1234L).searchKey("iphone").build();
    	when(productService.findProductBySearchKey("iphone", pageable)).thenReturn(Collections.singletonList(mockProduct));
    	
    	mockMvc.perform(MockMvcRequestBuilders
    		      .get("/products")
    		      .param("key", "iphone")
    		      .accept(MediaType.APPLICATION_JSON))
    		      .andExpect(status().isOk())
    		      .andExpect(jsonPath("$[0].name", is(mockProduct.getName())))
    		      .andExpect(jsonPath("$[0].providerName", is(mockProduct.getProviderName())));
    }
    
    @Test
    public void testGetProductDetailSuccessfully() throws Exception {
    	Product mockProduct = Product.builder().name("Iphone 1").providerName("tiki").providerProductId(1234L).searchKey("iphone").build();
    	when(productService.getProductByProviderProductIdAndProviderName(1234L, "tiki")).thenReturn(Optional.of(mockProduct));
    	
    	mockMvc.perform(MockMvcRequestBuilders
    		      .get("/products/1234")
    		      .param("providerName", "tiki")
    		      .accept(MediaType.APPLICATION_JSON))
    		      .andExpect(status().isOk())
    		      .andExpect(jsonPath("$.name", is(mockProduct.getName())))
    		      .andExpect(jsonPath("$.providerProductId", is(mockProduct.getProviderProductId().intValue())));
    }
    
    @Test
    public void testGetProductDetailNotFound() throws Exception {
    	when(productService.getProductByProviderProductIdAndProviderName(1234L, "tiki")).thenReturn(Optional.empty());
    	
    	mockMvc.perform(MockMvcRequestBuilders
    		      .get("/products/1234")
    		      .param("providerName", "tiki")
    		      .accept(MediaType.APPLICATION_JSON))
    		      .andExpect(status().isNotFound());
    }
}
