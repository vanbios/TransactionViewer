package com.vanbios.transactionviewer.common.utils.format;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * @author Ihor Bilous
 */
public class FormatManagerImpl implements FormatManager {

    private static final int PRECISE = 100;
    private static final String FORMAT = "###,##0.00";

    @Override
    public String doubleToStringFormatter(double number) {
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        dfs.setGroupingSeparator(',');

        DecimalFormat dfRate = new DecimalFormat(FORMAT, dfs);
        int precise = 10 ^ PRECISE;
        number *= precise;
        double result = (double) Math.round(number) / precise;

        return dfRate.format(result);
    }
}
