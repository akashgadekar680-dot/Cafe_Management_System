package com.in.cafe.com.in.cafe.Service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.in.cafe.com.in.cafe.POJO.Category;

public interface CategoryService {

    // Add new category
    ResponseEntity<String> addNewCategory(Map<String, String> requestMap);

    // Get all categories (optionally filtered)
    ResponseEntity<List<Category>> getAllCategory(String filterValue);

    // Update existing category
    ResponseEntity<String> updateCategory(Map<String, String> requestMap);

    // Delete category by ID
    ResponseEntity<String> deleteCategory(Long id);
}