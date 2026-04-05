package com.in.cafe.com.in.cafe.restImpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.in.cafe.com.in.cafe.Service.DashboardService;
import com.in.cafe.com.in.cafe.rest.DashboardRest;

@RestController
public class DashboardRestImpl implements DashboardRest {

    @Autowired
    private DashboardService dashboardService;   // ✅ made private (best practice)

    @Override
    public ResponseEntity<Map<String, Object>> getCount() {
        return dashboardService.getCount();
    }
}