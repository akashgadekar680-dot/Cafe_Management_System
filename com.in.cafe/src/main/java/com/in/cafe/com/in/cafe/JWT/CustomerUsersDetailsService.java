package com.in.cafe.com.in.cafe.JWT;

import java.util.Collections;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.in.cafe.com.in.cafe.POJO.User;
import com.in.cafe.com.in.cafe.dao.UserDao;

@Service
public class CustomerUsersDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerUsersDetailsService.class);

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        logger.info("Inside loadUserByUsername : {}", username);

        User user = userDao.findByEmail(username);

        if (Objects.isNull(user)) {
            logger.error("User not found with email : {}", username);
            throw new UsernameNotFoundException("User not found with email : " + username);
        }

        String role = user.getRole();

        if (role == null || role.trim().isEmpty()) {
            role = "USER";
        }

        role = role.trim().toUpperCase();

        if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }

        boolean enabled = false;

        if (user.getStatus() != null) {
            enabled = user.getStatus().equalsIgnoreCase("true")
                    || user.getStatus().equalsIgnoreCase("active");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                enabled,
                true,
                true,
                true,
                Collections.singletonList(new SimpleGrantedAuthority(role))
        );
    }

    public User getUserDetails() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        String currentUsername = null;

        if (principal instanceof UserDetails) {
            currentUsername = ((UserDetails) principal).getUsername();
        } else if (principal != null) {
            currentUsername = principal.toString();
        }

        if (currentUsername == null || currentUsername.equalsIgnoreCase("anonymousUser")) {
            return null;
        }

        return userDao.findByEmail(currentUsername);
    }

    public String getCurrentUserEmail() {
        User user = getUserDetails();

        if (user != null) {
            return user.getEmail();
        }

        return null;
    }

    public Boolean isAdmin() {
        User user = getUserDetails();

        if (user != null && user.getRole() != null) {
            String role = user.getRole().trim().toUpperCase();

            return role.equals("ADMIN") || role.equals("ROLE_ADMIN");
        }

        return false;
    }
}