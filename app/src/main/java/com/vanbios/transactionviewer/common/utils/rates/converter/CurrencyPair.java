package com.vanbios.transactionviewer.common.utils.rates.converter;

import com.vanbios.transactionviewer.common.utils.rates.util.StringUtil;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public class CurrencyPair {

    private final String ccy1;
    private final String ccy2;


    public CurrencyPair(final String ccy1, final String ccy2) {
        if (!StringUtil.noneBlank(ccy1, ccy2))
            throw new IllegalArgumentException("ccy1 and ccy2 cannot be blank");
        this.ccy1 = StringUtil.toUpperCase(ccy1);
        this.ccy2 = StringUtil.toUpperCase(ccy2);
    }

    public static CurrencyPair of(final String ccy1, final String ccy2) {
        return new CurrencyPair(ccy1, ccy2);
    }

    boolean containsCcy(final String ccy) {
        return ccy1.equalsIgnoreCase(StringUtil.toUpperCase(ccy)) || ccy2.equalsIgnoreCase(StringUtil.toUpperCase(ccy));
    }

    String findCommonCcy(final CurrencyPair otherPair) {
        if (containsCcy(otherPair.getCcy1()))
            return otherPair.getCcy1();
        return containsCcy(otherPair.getCcy2()) ? otherPair.getCcy2() : null;
    }

    CurrencyPair createInverse() {
        return new CurrencyPair(ccy2, ccy1);
    }
}
