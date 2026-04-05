package com.in.cafe.com.in.cafe.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.in.cafe.com.in.cafe.POJO.Product;
import com.in.cafe.com.in.cafe.wrapper.ProductWrapper;

import jakarta.transaction.Transactional;

public interface ProductDao extends JpaRepository<Product, Integer> {

    @Query("""
        select new com.in.cafe.com.in.cafe.wrapper.ProductWrapper(
            p.id,
            p.name,
            p.description,
            p.price,
            p.status,
            c.id,
            c.name
        )
        from Product p
        join p.category c
    """)
    List<ProductWrapper> getAllProduct();

    @Modifying
    @Transactional
    @Query("update Product p set p.status = :status where p.id = :id")
    Integer updateProductStatus(@Param("status") String status, @Param("id") Integer id);

    @Query("""
        select new com.in.cafe.com.in.cafe.wrapper.ProductWrapper(
            p.id,
            p.name
        )
        from Product p
        join p.category c
        where c.id = :id
        and p.status = 'true'
    """)
    List<ProductWrapper> getProductByCategory(@Param("id") Integer id);

    @Query("""
        select new com.in.cafe.com.in.cafe.wrapper.ProductWrapper(
            p.id,
            p.name,
            p.description,
            p.price,
            p.status,
            c.id,
            c.name
        )
        from Product p
        join p.category c
        where p.id = :id
    """)
    ProductWrapper getProductById(@Param("id") Integer id);
}