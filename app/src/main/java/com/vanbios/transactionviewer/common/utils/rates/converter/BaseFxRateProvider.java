package com.vanbios.transactionviewer.common.utils.rates.converter;


interface BaseFxRateProvider {

    FxRate getLatestRate(CurrencyPair pair);
}