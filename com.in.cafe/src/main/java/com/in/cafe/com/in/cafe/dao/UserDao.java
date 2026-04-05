package com.in.cafe.com.in.cafe.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.in.cafe.com.in.cafe.POJO.User;
import com.in.cafe.com.in.cafe.wrapper.UserWrapper;

import jakarta.transaction.Transactional;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {

    // Find user by email
    User findByEmail(String email);

    // Get all users with role 'user' as UserWrapper
    @Query("SELECT new com.in.cafe.com.in.cafe.wrapper.UserWrapper(u.id, u.name, u.email, u.contactNumber, u.status) " +
           "FROM User u WHERE u.role = 'user'")
    List<UserWrapper> getAllUser();

    // Get all admin emails
    @Query("SELECT u.email FROM User u WHERE u.role = 'admin'")
    List<String> getAllAdmin();

    // Update user status
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.status = :status WHERE u.id = :id")
    int updateStatus(@Param("status") String status, @Param("id") Integer id);
}