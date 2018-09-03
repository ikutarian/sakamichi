package com.okada.sakamichi;

public class StringUtils {

    public static boolean isBlank(String str) {
        return str == null || trim(str).length() == 0;
    }

    public static String trim(String str) {
        return str == null ? null : str.trim();
    }
}
