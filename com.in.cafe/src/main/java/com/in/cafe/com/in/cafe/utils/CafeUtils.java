package com.in.cafe.com.in.cafe.utils;

import java.io.File;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CafeUtils {

    private static final Logger log = LoggerFactory.getLogger(CafeUtils.class);

    // Private constructor (Utility class)
    private CafeUtils() {
    }

    // ✅ Standard API response
    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus) {
        String response = "{\"message\":\"" + responseMessage + "\"}";
        return new ResponseEntity<>(response, httpStatus);
    }

    // ✅ Generate Unique Bill ID
    public static String getUUId() {
        Date date = new Date(System.currentTimeMillis());
        return "BILL-" + date.getTime();
    }

    // ✅ Convert String → JSON Array
    public static JSONArray getJsonArrayFromString(String data) {
        try {
            return new JSONArray(data);
        } catch (JSONException e) {
            log.error("Error converting String to JSONArray: {}", e.getMessage());
            return new JSONArray(); // return empty array instead of crash
        }
    }

    // ✅ Convert JSON String → Map
    public static Map<String, Object> getMapFromJson(String data) {
        try {
            if (!Strings.isNullOrEmpty(data)) {
                return new Gson().fromJson(data, new TypeToken<Map<String, Object>>() {}.getType());
            }
        } catch (Exception e) {
            log.error("Error converting JSON to Map: {}", e.getMessage());
        }
        return new HashMap<>();
    }

    // ✅ Check file exists
    public static Boolean isFileExist(String path) {
        log.info("Checking file existence: {}", path);
        try {
            File file = new File(path);
            return file.exists();
        } catch (Exception ex) {
            log.error("Error checking file: {}", ex.getMessage());
        }
        return false;
    }
}