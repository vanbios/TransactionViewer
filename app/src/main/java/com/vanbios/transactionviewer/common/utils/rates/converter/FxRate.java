package com.vanbios.transactionviewer.common.utils.rates.converter;

import java.math.BigDecimal;


public interface FxRate {

    CurrencyPair getCurrencyPair();

    BigDecimal getRate();

    FxRate createInverse(int precision);
}
