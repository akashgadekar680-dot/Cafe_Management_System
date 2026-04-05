package com.in.cafe.com.in.cafe.Service;

import java.util.Map;
import java.util.List;   // ✅ Correct import

import org.springframework.http.ResponseEntity;

import com.in.cafe.com.in.cafe.POJO.Bill;

public interface BillService {

    ResponseEntity<String> generateReport(Map<String, Object> requestMap);
    
    ResponseEntity<List<Bill>> getBills();   // ✅ Keep only one
    
    ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap);
    
    ResponseEntity<String> deleteBill(Integer id);   // ✅ fixed parameter name
    
}