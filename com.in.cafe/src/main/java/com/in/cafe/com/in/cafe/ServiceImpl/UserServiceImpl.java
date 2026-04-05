package com.in.cafe.com.in.cafe.ServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.in.cafe.com.in.cafe.JWT.JwtFilter;
import com.in.cafe.com.in.cafe.JWT.JwtUtil;
import com.in.cafe.com.in.cafe.POJO.User;
import com.in.cafe.com.in.cafe.Service.UserService;
import com.in.cafe.com.in.cafe.constents.CafeConstants;
import com.in.cafe.com.in.cafe.dao.UserDao;
import com.in.cafe.com.in.cafe.utils.CafeUtils;
import com.in.cafe.com.in.cafe.utils.EmailUtils;
import com.in.cafe.com.in.cafe.wrapper.UserWrapper;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private EmailUtils emailUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ================= SIGNUP =================
    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        try {
            if (!validateSignUpMap(requestMap)) {
                return CafeUtils.getResponseEntity(
                        CafeConstants.INVALID_DATA,
                        HttpStatus.BAD_REQUEST
                );
            }

            User existingUser = userDao.findByEmail(requestMap.get("email").trim());

            if (existingUser != null) {
                return CafeUtils.getResponseEntity(
                        "User already exists",
                        HttpStatus.BAD_REQUEST
                );
            }

            User user = getUserFromMap(requestMap);
            userDao.save(user);

            return CafeUtils.getResponseEntity(
                    "User Registered Successfully",
                    HttpStatus.OK
            );

        } catch (Exception ex) {
            log.error("Error in signUp", ex);

            return CafeUtils.getResponseEntity(
                    CafeConstants.SOMETHING_WENT_WRONG,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    private boolean validateSignUpMap(Map<String, String> requestMap) {
        return requestMap.containsKey("name")
                && requestMap.containsKey("contactNumber")
                && requestMap.containsKey("email")
                && requestMap.containsKey("password");
    }

    private User getUserFromMap(Map<String, String> requestMap) {
        User user = new User();

        user.setName(requestMap.get("name").trim());
        user.setContactNumber(requestMap.get("contactNumber").trim());
        user.setEmail(requestMap.get("email").trim());
        user.setPassword(passwordEncoder.encode(requestMap.get("password").trim()));
        user.setStatus("true");
        user.setRole("USER"); // default role as USER

        return user;
    }

    // ================= LOGIN =================
    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        try {
            if (!requestMap.containsKey("email") || !requestMap.containsKey("password")) {
                return CafeUtils.getResponseEntity(
                        "Email and Password required",
                        HttpStatus.BAD_REQUEST
                );
            }

            String email = requestMap.get("email").trim();
            String password = requestMap.get("password").trim();

            User user = userDao.findByEmail(email);

            if (user == null) {
                return CafeUtils.getResponseEntity(
                        "Invalid Email or Password",
                        HttpStatus.BAD_REQUEST
                );
            }

            if (!"true".equalsIgnoreCase(user.getStatus())) {
                return CafeUtils.getResponseEntity(
                        "User not approved",
                        HttpStatus.FORBIDDEN
                );
            }

            if (!passwordEncoder.matches(password, user.getPassword())) {
                return CafeUtils.getResponseEntity(
                        "Invalid Email or Password",
                        HttpStatus.BAD_REQUEST
                );
            }

            String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

            return new ResponseEntity<>(
                    "{\"token\":\"" + token + "\"}",
                    HttpStatus.OK
            );

        } catch (Exception ex) {
            log.error("Login error", ex);

            return CafeUtils.getResponseEntity(
                    "Something went wrong during login",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    // ================= CHANGE PASSWORD =================
    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
            String currentUser = jwtFilter.getCurrentUser();

            if (currentUser == null || currentUser.isEmpty()) {
                return CafeUtils.getResponseEntity(
                        CafeConstants.UNAUTHORIZED_ACCESS,
                        HttpStatus.UNAUTHORIZED
                );
            }

            User userObj = userDao.findByEmail(currentUser);

            if (userObj == null) {
                return CafeUtils.getResponseEntity(
                        "User not found",
                        HttpStatus.BAD_REQUEST
                );
            }

            if (!requestMap.containsKey("oldPassword")
                    || !requestMap.containsKey("newPassword")) {

                return CafeUtils.getResponseEntity(
                        "Old Password and New Password are required",
                        HttpStatus.BAD_REQUEST
                );
            }

            String oldPassword = requestMap.get("oldPassword").trim();
            String newPassword = requestMap.get("newPassword").trim();
            String confirmPassword = requestMap.getOrDefault("confirmPassword", "").trim();

            if (!passwordEncoder.matches(oldPassword, userObj.getPassword())) {
                return CafeUtils.getResponseEntity(
                        "Incorrect Old Password",
                        HttpStatus.BAD_REQUEST
                );
            }

            if (!confirmPassword.isEmpty() && !newPassword.equals(confirmPassword)) {
                return CafeUtils.getResponseEntity(
                        "New Password and Confirm Password do not match",
                        HttpStatus.BAD_REQUEST
                );
            }

            if (passwordEncoder.matches(newPassword, userObj.getPassword())) {
                return CafeUtils.getResponseEntity(
                        "New Password cannot be same as Old Password",
                        HttpStatus.BAD_REQUEST
                );
            }

            userObj.setPassword(passwordEncoder.encode(newPassword));
            userDao.save(userObj);

            return CafeUtils.getResponseEntity(
                    "Password Updated Successfully",
                    HttpStatus.OK
            );

        } catch (Exception ex) {
            log.error("Error in changePassword", ex);

            return CafeUtils.getResponseEntity(
                    CafeConstants.SOMETHING_WENT_WRONG,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    // ================= GET ALL USERS =================
    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try {
            if (!jwtFilter.isAdmin()) {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.FORBIDDEN);
            }

            List<UserWrapper> users = userDao.getAllUser(); // DAO query already filters role='user'

            if (users == null) {
                users = new ArrayList<>();
            }

            return new ResponseEntity<>(users, HttpStatus.OK);

        } catch (Exception ex) {
            log.error("Error in getAllUser", ex);
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ================= UPDATE USER =================
    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try {
            if (!requestMap.containsKey("email")) {
                return CafeUtils.getResponseEntity(
                        "Email is required",
                        HttpStatus.BAD_REQUEST
                );
            }

            User user = userDao.findByEmail(requestMap.get("email").trim());

            if (user == null) {
                return CafeUtils.getResponseEntity(
                        "User not found",
                        HttpStatus.BAD_REQUEST
                );
            }

            String currentUserEmail = jwtFilter.getCurrentUser();

            if (currentUserEmail == null) {
                return CafeUtils.getResponseEntity(
                        CafeConstants.UNAUTHORIZED_ACCESS,
                        HttpStatus.UNAUTHORIZED
                );
            }

            if (!jwtFilter.isAdmin()
                    && !currentUserEmail.equalsIgnoreCase(user.getEmail())) {

                return CafeUtils.getResponseEntity(
                        CafeConstants.UNAUTHORIZED_ACCESS,
                        HttpStatus.FORBIDDEN
                );
            }

            if (requestMap.containsKey("name")) {
                user.setName(requestMap.get("name").trim());
            }

            if (requestMap.containsKey("contactNumber")) {
                user.setContactNumber(requestMap.get("contactNumber").trim());
            }

            if (requestMap.containsKey("status") && jwtFilter.isAdmin()) {
                user.setStatus(requestMap.get("status").trim());
            }

            userDao.save(user);

            return CafeUtils.getResponseEntity(
                    "User Updated Successfully",
                    HttpStatus.OK
            );

        } catch (Exception ex) {
            log.error("Error in update", ex);

            return CafeUtils.getResponseEntity(
                    CafeConstants.SOMETHING_WENT_WRONG,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    // ================= CHECK TOKEN =================
    @Override
    public ResponseEntity<String> checkToken() {
        return CafeUtils.getResponseEntity("true", HttpStatus.OK);
    }

    // ================= FORGOT PASSWORD =================
    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try {
            if (!requestMap.containsKey("email")) {
                return CafeUtils.getResponseEntity(
                        "Email is required",
                        HttpStatus.BAD_REQUEST
                );
            }

            User user = userDao.findByEmail(requestMap.get("email").trim());

            if (user == null) {
                return CafeUtils.getResponseEntity(
                        "User not found",
                        HttpStatus.BAD_REQUEST
                );
            }

            emailUtils.forgotMail(
                    user.getEmail(),
                    "Password Reset Request",
                    "Your password reset request has been received."
            );

            return CafeUtils.getResponseEntity(
                    "Password reset instructions sent to your email.",
                    HttpStatus.OK
            );

        } catch (Exception ex) {
            log.error("Error in forgotPassword", ex);

            return CafeUtils.getResponseEntity(
                    CafeConstants.SOMETHING_WENT_WRONG,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    // ================= GET USER ID BY EMAIL =================
    @Override
    public Integer getUserIdByEmail(String email) {
        try {
            User user = userDao.findByEmail(email.trim());

            if (user != null) {
                return user.getId();
            }

        } catch (Exception ex) {
            log.error("Error in getUserIdByEmail", ex);
        }

        return null;
    }
}