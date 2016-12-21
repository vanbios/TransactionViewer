package com.vanbios.transactionviewer.common.utils.rates.converter;

import java.math.BigDecimal;

import lombok.Getter;

import static com.vanbios.transactionviewer.common.utils.rates.util.BigDecimalUtil.inverse;
import static com.vanbios.transactionviewer.common.utils.rates.util.BigDecimalUtil.setScale;


public class FxRateImpl implements FxRate {
    private final CurrencyPair currencyPair;
    private final String crossCcy;
    private final boolean marketConvention;
    @Getter
    private final BigDecimal rate;


    public FxRateImpl(final CurrencyPair currencyPair, final String crossCcy, final boolean marketConvention, final BigDecimal rate) {
        this.currencyPair = currencyPair;
        this.crossCcy = crossCcy;
        this.marketConvention = marketConvention;
        this.rate = rate;
    }

    @Override
    public FxRate createInverse(final int precision) {
        return new FxRateImpl(currencyPair.createInverse(), crossCcy, !marketConvention, setScale(inverse(rate), precision));
    }

    @Override
    public CurrencyPair getCurrencyPair() {
        return currencyPair;
    }
}
