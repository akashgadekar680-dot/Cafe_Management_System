package com.in.cafe.com.in.cafe.Service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.in.cafe.com.in.cafe.wrapper.UserWrapper;

public interface UserService {

    // ================= SIGNUP =================
    ResponseEntity<String> signUp(Map<String, String> requestMap);

    // ================= LOGIN =================
    ResponseEntity<String> login(Map<String, String> requestMap);

    // ================= GET ALL USERS =================
    ResponseEntity<List<UserWrapper>> getAllUser();

    // ================= UPDATE USER =================
    ResponseEntity<String> update(Map<String, String> requestMap);

    // ================= CHECK TOKEN =================
    ResponseEntity<String> checkToken();

    // ================= CHANGE PASSWORD =================
    ResponseEntity<String> changePassword(Map<String, String> requestMap);

    // ================= FORGOT PASSWORD =================
    ResponseEntity<String> forgotPassword(Map<String, String> requestMap);

    // ================= GET USER ID BY EMAIL =================
    Integer getUserIdByEmail(String email);
}