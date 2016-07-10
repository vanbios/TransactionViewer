package com.vanbios.transactionviewer.util.rates.util;

import java.math.BigDecimal;


public final class BigDecimalUtil {

    private static final int MAX_SCALE_FOR_INVERSE = 20;

    public static BigDecimal bd(final String val) {
        return new BigDecimal(val);
    }

    public static BigDecimal inverse(final BigDecimal value, final int scale, final int rounding) {
        return isNotZero(value) ? BigDecimal.ONE.divide(value, scale, rounding) : null;
    }

    public static BigDecimal inverse(final BigDecimal value) {
        return isNotZero(value) ? BigDecimal.ONE.setScale(MAX_SCALE_FOR_INVERSE).divide(value, BigDecimal.ROUND_HALF_UP) : null;
    }

    private static boolean isNotZero(final BigDecimal value) {
        return value != null && value.signum() != 0;
    }

    public static BigDecimal divide(final int numeratorScale, final BigDecimal numerator, final BigDecimal denominator, final int rounding) {
        return numerator != null && isNotZero(denominator) ? numerator.setScale(numeratorScale, rounding).divide(denominator, rounding) : null;
    }

    public static BigDecimal multiply(final BigDecimal value, final BigDecimal multiplicand) {
        return value != null && multiplicand != null ? value.multiply(multiplicand) : null;
    }

    public static BigDecimal setScale(final BigDecimal bd, final int scale) {
        return setScale(bd, scale, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal setScale(final BigDecimal bd, final Integer scale, final int rounding) {
        return bd != null && scale != null ? bd.setScale(scale, rounding) : null;
    }
}
