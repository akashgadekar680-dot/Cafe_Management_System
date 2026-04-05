package com.in.cafe.com.in.cafe.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.in.cafe.com.in.cafe.POJO.Category;

@Repository
public interface CategoryDao extends JpaRepository<Category, Integer> {

    List<Category> getAllCategory();

}     