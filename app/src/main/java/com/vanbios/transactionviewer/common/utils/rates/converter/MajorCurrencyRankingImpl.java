package com.vanbios.transactionviewer.common.utils.rates.converter;

import com.vanbios.transactionviewer.common.utils.rates.util.StringUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


class MajorCurrencyRankingImpl implements MajorCurrencyRanking {
    private static final int DEFAULT_UNRANKED_VALUE = 99;
    private final Map<String, Integer> ranks;

    MajorCurrencyRankingImpl(final List<String> orderedCurrencies) {
        if (orderedCurrencies == null)
            ranks = Collections.emptyMap();
        else {
            ranks = new HashMap<>(orderedCurrencies.size());
            int i = 1;
            for (final String s : orderedCurrencies)
                ranks.put(StringUtil.toUpperCase(s), i++);
        }
    }

    @Override
    public String selectMajorCurrency(final String ccy1, final String ccy2) {
        return getOrDefault(StringUtil.toUpperCase(ccy1), DEFAULT_UNRANKED_VALUE) <=
                getOrDefault(StringUtil.toUpperCase(ccy2), DEFAULT_UNRANKED_VALUE) ? ccy1 : ccy2;
    }

    @Override
    public boolean isMarketConvention(final String ccy1, final String ccy2) {
        return selectMajorCurrency(ccy1, ccy2).equals(ccy1);
    }

    @Override
    public boolean isMarketConvention(final CurrencyPair pair) {
        return isMarketConvention(pair.getCcy1(), pair.getCcy2());
    }

    private int getOrDefault(String s, int def) {
        return ranks.containsKey(s) ? ranks.get(s) : def;
    }
}
