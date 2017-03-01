package com.vanbios.transactionviewer.common.utils.rates.converter;


interface MajorCurrencyRanking {

    String selectMajorCurrency(String ccy1, String ccy2);

    boolean isMarketConvention(String ccy1, String ccy2);

    boolean isMarketConvention(CurrencyPair pair);
}