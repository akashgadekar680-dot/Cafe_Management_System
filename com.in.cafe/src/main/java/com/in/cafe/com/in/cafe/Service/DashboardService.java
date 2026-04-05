package com.in.cafe.com.in.cafe.Service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

public interface DashboardService {

    ResponseEntity<Map<String, Object>> getCount();   // ✅ spacing fixed
}