package com.vanbios.transactionviewer.util.rates.converter;

public interface FxRateCalculator {

    FxRate findFx(CurrencyPair ccyPair);
}
