package com.vanbios.transactionviewer.common.utils.rates.util;


public final class StringUtil {

    public static String toUpperCase(final String str) {
        return str != null ? str.toUpperCase() : null;
    }

    public static boolean noneBlank(final String... text) {
        for (final String txt : text)
            if (isBlank(txt)) return false;
        return true;
    }

    private static boolean isBlank(String str) {
        if (str == null || str.length() == 0) return true;
        for (int i = 0; i < str.length(); i++)
            if ((!Character.isWhitespace(str.charAt(i)))) return false;
        return true;
    }

}