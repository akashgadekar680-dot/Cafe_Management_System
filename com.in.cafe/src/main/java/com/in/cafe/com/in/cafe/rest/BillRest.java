package com.in.cafe.com.in.cafe.rest;

import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.in.cafe.com.in.cafe.POJO.Bill;

@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "/bill")
public interface BillRest {

    @PostMapping(
        path = "/generateReport",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_PDF_VALUE
    )
    ResponseEntity<String> generateReport(@RequestBody Map<String, Object> requestMap);

    @GetMapping(path = "/getBills")
    ResponseEntity<List<Bill>> getBills();

    @PostMapping(
        path = "/getPdf",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_PDF_VALUE
    )
    ResponseEntity<byte[]> getPdf(@RequestBody Map<String, Object> requestMap);

    @DeleteMapping(path = "/delete/{id}")
    ResponseEntity<String> deleteBill(@PathVariable Integer id);
}