package com.in.cafe.com.in.cafe.rest;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.in.cafe.com.in.cafe.wrapper.UserWrapper;

@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "/user")
public interface UserRest {

    // ================= SIGNUP =================
    @PostMapping(path = "/signup")
    ResponseEntity<String> signUp(@RequestBody(required = true) Map<String, String> requestMap);

    // ================= LOGIN =================
    @PostMapping(path = "/login")
    ResponseEntity<String> login(@RequestBody(required = true) Map<String, String> requestMap);

    // ================= GET ALL USERS =================
    @GetMapping(path = "/getAllUser")
    ResponseEntity<List<UserWrapper>> getAllUser();

    // ================= UPDATE PROFILE =================
    @PostMapping(path = "/updateProfile")
    ResponseEntity<String> updateprofile(Map<String, String> requestMap);

    // ================= CHECK TOKEN =================
    @GetMapping(path = "/checkToken")
    ResponseEntity<String> checkToken();

    // ================= CHANGE PASSWORD =================
    @PostMapping(path = "/changePassword")
    ResponseEntity<String> changePassword(@RequestBody(required = true) Map<String, String> requestMap);

    // ================= FORGOT PASSWORD =================
    @PostMapping(path = "/forgotPassword")
    ResponseEntity<String> forgotPassword(@RequestBody(required = true) Map<String, String> requestMap);


}