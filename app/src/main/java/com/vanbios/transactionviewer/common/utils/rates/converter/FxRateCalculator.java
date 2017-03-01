package com.vanbios.transactionviewer.common.utils.rates.converter;

public interface FxRateCalculator {

    FxRate findFx(CurrencyPair ccyPair);
}