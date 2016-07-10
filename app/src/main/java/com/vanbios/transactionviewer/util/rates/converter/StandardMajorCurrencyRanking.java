package com.vanbios.transactionviewer.util.rates.converter;

import java.util.Arrays;


final class StandardMajorCurrencyRanking extends MajorCurrencyRankingImpl {
    private static final MajorCurrencyRanking DEFAULT = new StandardMajorCurrencyRanking();

    private StandardMajorCurrencyRanking() {
        super(Arrays.asList("EUR", "GBP", "AUD", "NZD", "USD", "CAD", "CHF", "NOK", "SEK", "JPY"));
    }

    static MajorCurrencyRanking getDefault() {
        return DEFAULT;
    }
}
