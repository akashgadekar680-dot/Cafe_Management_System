package com.in.cafe.com.in.cafe.rest;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.in.cafe.com.in.cafe.wrapper.ProductWrapper;

@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "/product")
public interface ProductRest {

    // Add new product
    @PostMapping(path = "/add")
    ResponseEntity<String> addNewProduct(@RequestBody Map<String, Object> requestMap);

    // Get all products
    @GetMapping(path = "/get")
    ResponseEntity<List<ProductWrapper>> getAllProducts();

    // Update product
    @PutMapping(path = "/update")
    ResponseEntity<String> updateProduct(@RequestBody Map<String, Object> requestMap);

    // Delete product by ID
    @DeleteMapping(path = "/delete/{id}")
    ResponseEntity<String> deleteProduct(@PathVariable("id") Integer id);

    // Update product status
    @PatchMapping(path = "/updateStatus")
    ResponseEntity<String> updateStatus(@RequestBody Map<String, Object> requestMap);

    // Get products by category
    @GetMapping(path = "/getByCategory/{id}")
    ResponseEntity<List<ProductWrapper>> getByCategory(@PathVariable("id") Integer id);

    // Get product by ID
    @GetMapping(path = "/getById/{id}")
    ResponseEntity<ProductWrapper> getProductById(@PathVariable("id") Integer id);
}