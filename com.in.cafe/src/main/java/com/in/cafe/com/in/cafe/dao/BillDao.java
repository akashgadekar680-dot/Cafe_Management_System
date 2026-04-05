package com.in.cafe.com.in.cafe.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.in.cafe.com.in.cafe.POJO.Bill;

public interface BillDao extends JpaRepository<Bill, Integer> {

    @Query(name = "Bill.getAllBills") 
    List<Bill> getAllBills();

    @Query(name = "Bill.getBillByUserName") 
    List<Bill> getBillByUserName(@Param("username") String username);
}