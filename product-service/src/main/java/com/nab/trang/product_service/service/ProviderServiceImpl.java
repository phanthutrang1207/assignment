package com.nab.trang.product_service.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.nab.trang.product_service.model.Product;

@Service
@Primary
public class ProviderServiceImpl implements ProviderService{
	private static final Logger LOGGER = LogManager.getLogger(ProviderServiceImpl.class);
	
	@Autowired
	List<ProviderService> providerServices;
	
	@Override
	public List<Product> getProductListBySearchKey(String key) {
		List<Future<List<Product>>> futures = createCallbleTasksForProviderSearchingService(key);
		List<Product> result = new ArrayList<>();
		try {
			for (Future<List<Product>> future : futures) {
				result.addAll(future.get());
			}			
		} catch (InterruptedException | ExecutionException e) {
			LOGGER.error("Exception while getting products from provider service", e);
		}
		return result;
	}
	
	private List<Future<List<Product>>> createCallbleTasksForProviderSearchingService(String key) {
		try {
			ExecutorService excecutorService = Executors.newCachedThreadPool();
			List<Callable<List<Product>>> callableTasks = providerServices.stream().map(toCallable(key)).collect(Collectors.toList());
			return excecutorService.invokeAll(callableTasks);
		} catch (InterruptedException e) {
			LOGGER.error("Exception while invoking callable task from excecutor service", e);
		} 
		return Collections.emptyList();
	}

	private Function<ProviderService, Callable<List<Product>>> toCallable(String key) {
		return provider -> {
			Callable<List<Product>> search = () -> {
				return provider.getProductListBySearchKey(key);
			};
			return search;
		};
	}

}
