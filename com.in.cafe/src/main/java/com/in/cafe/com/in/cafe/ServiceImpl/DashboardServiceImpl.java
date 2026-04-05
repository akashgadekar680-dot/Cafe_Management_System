package com.in.cafe.com.in.cafe.ServiceImpl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.in.cafe.com.in.cafe.Service.DashboardService;
import com.in.cafe.com.in.cafe.dao.BillDao;
import com.in.cafe.com.in.cafe.dao.CategoryDao;
import com.in.cafe.com.in.cafe.dao.ProductDao;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private CategoryDao categoryDao;   // ✅ made private

    @Autowired 
    private ProductDao productDao;     // ✅ made private

    @Autowired
    private BillDao billDao;           // ✅ made private

    @Override
    public ResponseEntity<Map<String, Object>> getCount() {
        Map<String, Object> map = new HashMap<>();

        map.put("category", categoryDao.count());
        map.put("product", productDao.count());
        map.put("bill", billDao.count());

        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}