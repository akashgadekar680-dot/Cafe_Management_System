package com.in.cafe.com.in.cafe.Service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.in.cafe.com.in.cafe.wrapper.ProductWrapper;

public interface ProductService {

    ResponseEntity<String> addNewProduct(Map<String, Object> requestMap);

    ResponseEntity<List<ProductWrapper>> getAllProduct();

    ResponseEntity<String> updateProduct(Map<String, Object> requestMap);
    
    ResponseEntity<String> deleteProduct(Integer id);
    
    ResponseEntity<String> updateStatus(Map<String, Object> requestMap);
    
    ResponseEntity<List<ProductWrapper>> getByCategory(Integer id);

    ResponseEntity<ProductWrapper> getProductById(Integer id);

	ResponseEntity<List<ProductWrapper>> getAllProducts();
}