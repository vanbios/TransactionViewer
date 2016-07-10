package com.vanbios.transactionviewer.util.rates.converter;


interface BaseFxRateProvider {

    FxRate getLatestRate(CurrencyPair pair);
}