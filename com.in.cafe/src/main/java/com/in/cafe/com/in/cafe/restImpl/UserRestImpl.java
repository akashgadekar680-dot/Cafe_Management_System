package com.in.cafe.com.in.cafe.restImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.in.cafe.com.in.cafe.Service.UserService;
import com.in.cafe.com.in.cafe.constents.CafeConstants;
import com.in.cafe.com.in.cafe.rest.UserRest;
import com.in.cafe.com.in.cafe.utils.CafeUtils;
import com.in.cafe.com.in.cafe.wrapper.UserWrapper;

@RestController
public class UserRestImpl implements UserRest {

    private static final Logger log = LoggerFactory.getLogger(UserRestImpl.class);

    @Autowired
    private UserService userService;

    // ================= SIGNUP =================
    @Override
    public ResponseEntity<String> signUp(@RequestBody Map<String, String> requestMap) {
        log.info("Inside UserRestImpl : signUp");

        try {
            if (!requestMap.containsKey("name")
                    || !requestMap.containsKey("email")
                    || !requestMap.containsKey("password")) {

                log.warn("Invalid signup data: {}", requestMap);

                return CafeUtils.getResponseEntity(
                        CafeConstants.INVALID_DATA,
                        HttpStatus.BAD_REQUEST
                );
            }

            return userService.signUp(requestMap);

        } catch (Exception ex) {
            log.error("Exception in signUp", ex);

            return CafeUtils.getResponseEntity(
                    CafeConstants.SOMETHING_WENT_WRONG,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    // ================= LOGIN =================
    @Override
    public ResponseEntity<String> login(@RequestBody Map<String, String> requestMap) {
        log.info("Inside UserRestImpl : login for email {}", requestMap.get("email"));

        try {
            if (!requestMap.containsKey("email")
                    || !requestMap.containsKey("password")) {

                log.warn("Invalid login data: {}", requestMap);

                return CafeUtils.getResponseEntity(
                        CafeConstants.INVALID_DATA,
                        HttpStatus.BAD_REQUEST
                );
            }

            return userService.login(requestMap);

        } catch (Exception ex) {
            log.error("Exception in login", ex);

            return CafeUtils.getResponseEntity(
                    CafeConstants.SOMETHING_WENT_WRONG,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    // ================= GET ALL USERS =================
    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        log.info("Inside UserRestImpl : getAllUser");

        try {
            List<UserWrapper> users = userService.getAllUser().getBody();
            if (users == null) {
                users = new ArrayList<>();
            }
            return new ResponseEntity<>(users, HttpStatus.OK);

        } catch (Exception ex) {
            log.error("Exception in getAllUser", ex);
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ================= UPDATE USER =================
    @Override
    public ResponseEntity<String> updateprofile(@RequestBody Map<String, String> requestMap) {
        log.info("Inside UserRestImpl : update with request: {}", requestMap);

        try {
            if (!requestMap.containsKey("name")
                    || !requestMap.containsKey("email")
                    || !requestMap.containsKey("contactNumber")) {

                log.warn("Invalid update data: {}", requestMap);

                return CafeUtils.getResponseEntity(
                        CafeConstants.INVALID_DATA,
                        HttpStatus.BAD_REQUEST
                );
            }

            Integer userId = userService.getUserIdByEmail(requestMap.get("email"));

            if (userId == null) {
                log.warn("User not found for email: {}", requestMap.get("email"));

                return CafeUtils.getResponseEntity(
                        CafeConstants.USER_NOT_FOUND,
                        HttpStatus.BAD_REQUEST
                );
            }

            requestMap.put("id", String.valueOf(userId));

            return userService.update(requestMap);

        } catch (Exception ex) {
            log.error("Exception in update", ex);

            return CafeUtils.getResponseEntity(
                    CafeConstants.SOMETHING_WENT_WRONG,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    // ================= CHECK TOKEN =================
    @Override
    public ResponseEntity<String> checkToken() {
        log.info("Inside UserRestImpl : checkToken");

        try {
            return userService.checkToken();

        } catch (Exception ex) {
            log.error("Exception in checkToken", ex);

            return CafeUtils.getResponseEntity(
                    CafeConstants.SOMETHING_WENT_WRONG,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    // ================= CHANGE PASSWORD =================
    @Override
    public ResponseEntity<String> changePassword(@RequestBody Map<String, String> requestMap) {
        log.info("Inside UserRestImpl : changePassword");

        try {
            if (!requestMap.containsKey("oldPassword")
                    || !requestMap.containsKey("newPassword")) {

                log.warn("Invalid changePassword data: {}", requestMap);

                return CafeUtils.getResponseEntity(
                        CafeConstants.INVALID_DATA,
                        HttpStatus.BAD_REQUEST
                );
            }

            if (requestMap.containsKey("confirmPassword") &&
                    !requestMap.get("newPassword").equals(requestMap.get("confirmPassword"))) {

                return CafeUtils.getResponseEntity(
                        "New Password and Confirm Password do not match",
                        HttpStatus.BAD_REQUEST
                );
            }

            String currentUserEmail = null;
            if (SecurityContextHolder.getContext().getAuthentication() != null
                    && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
                currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            }

            if (currentUserEmail == null || currentUserEmail.isEmpty()) {
                return CafeUtils.getResponseEntity(
                        "Unauthorized: Invalid token",
                        HttpStatus.UNAUTHORIZED
                );
            }

            requestMap.put("email", currentUserEmail);

            return userService.changePassword(requestMap);

        } catch (Exception ex) {
            log.error("Exception in changePassword", ex);

            return CafeUtils.getResponseEntity(
                    CafeConstants.SOMETHING_WENT_WRONG,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    // ================= FORGOT PASSWORD =================
    @Override
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> requestMap) {
        log.info("Inside UserRestImpl : forgotPassword");

        try {
            if (!requestMap.containsKey("email")) {
                log.warn("Invalid forgotPassword data: {}", requestMap);

                return CafeUtils.getResponseEntity(
                        CafeConstants.INVALID_DATA,
                        HttpStatus.BAD_REQUEST
                );
            }

            return userService.forgotPassword(requestMap);

        } catch (Exception ex) {
            log.error("Exception in forgotPassword", ex);

            return CafeUtils.getResponseEntity(
                    CafeConstants.SOMETHING_WENT_WRONG,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

	
}