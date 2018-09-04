package com.okada.sakamichi.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JSONUtils {

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static String objToStr(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T  strToObj(String str, Class<T> clazz) {
        try {
            return mapper.readValue(str, clazz);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
