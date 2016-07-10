package com.vanbios.transactionviewer.util.rates.converter;

import java.math.BigDecimal;


public interface FxRate {

    CurrencyPair getCurrencyPair();

    BigDecimal getRate();

    FxRate createInverse(int precision);
}
