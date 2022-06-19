package com.imyvm.economy.util;

import java.text.DecimalFormat;

public class MoneyUtil {
    public static final DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public static String format(long money) {
        return decimalFormat.format(money / 100.0);
    }
}
