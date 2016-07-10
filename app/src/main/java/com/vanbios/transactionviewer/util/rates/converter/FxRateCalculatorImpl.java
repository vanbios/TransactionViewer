package com.vanbios.transactionviewer.util.rates.converter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FxRateCalculatorImpl implements FxRateCalculator {
    private final Map<CurrencyPair, FxRate> rates = new HashMap<>();
    private final BaseFxRateProvider baseFxRateProvider;
    private final MajorCurrencyRanking majorCurrencyRanking;
    private final List<String> orderedCurrenciesForCross;
    private final int precisionForFxRate;
    private final int precisionForInverseFxRate;
    private final int bidRounding;
    private final int askRounding;
    private final boolean cacheResults;
    private final boolean cacheBaseRates;


    public FxRateCalculatorImpl(final FxRateCalculatorBuilder builder) {
        builder.checkValid();
        rates.putAll(builder.getRatesSnapshot());
        this.baseFxRateProvider = null;
        this.majorCurrencyRanking = builder.getMajorCurrencyRanking();
        this.orderedCurrenciesForCross = builder.getOrderedCurrenciesForCross();
        this.precisionForFxRate = builder.getPrecisionForFxRate();
        this.precisionForInverseFxRate = builder.getPrecisionForInverseFxRate();
        this.cacheBaseRates = builder.isCacheBaseRates();
        this.cacheResults = builder.isCacheResults();
        this.bidRounding = builder.getBidRounding();
        this.askRounding = builder.getAskRounding();
    }

    private FxRate getBaseRate(final CurrencyPair ccyPair) {
        FxRate fxRate = rates.get(ccyPair);
        if (fxRate == null && baseFxRateProvider != null) {
            final FxRate latestRate = baseFxRateProvider.getLatestRate(ccyPair);
            if (latestRate != null) {
                fxRate = latestRate;
                if (cacheBaseRates) rates.put(ccyPair, fxRate);
            }
        }
        return fxRate;
    }

    @Override
    public FxRate findFx(final CurrencyPair ccyPair) {
        FxRate fxRate = getBaseRate(ccyPair);
        if (fxRate == null) {
            // try inverse
            final FxRate inverse = getBaseRate(ccyPair.createInverse());
            if (inverse != null) {
                fxRate = inverse.createInverse(precisionForInverseFxRate);
                if (cacheResults) rates.put(ccyPair, fxRate);
            } else {
                for (final String crossCcy : orderedCurrenciesForCross) {
                    fxRate = findViaCrossCcy(ccyPair, crossCcy);
                    if (fxRate != null) {
                        if (cacheResults) rates.put(ccyPair, fxRate);
                        break;
                    }
                }
            }
        }
        return fxRate;
    }

    private FxRate findViaCrossCcy(final CurrencyPair ccyPair, final String crossCcy) {
        final CurrencyPair xCcyPair = CurrencyPair.of(crossCcy, ccyPair.getCcy1());
        FxRate xCcy1 = getBaseRate(xCcyPair);
        if (xCcy1 == null) {
            // try inverse
            final FxRate inverse = getBaseRate(xCcyPair.createInverse());
            if (inverse != null)
                xCcy1 = inverse.createInverse(precisionForInverseFxRate);
        }

        if (xCcy1 != null) {
            final CurrencyPair xCcy2Pair = CurrencyPair.of(crossCcy, ccyPair.getCcy2());
            FxRate xCcy2 = getBaseRate(xCcy2Pair);
            if (xCcy2 == null) {
                // try inverse
                final FxRate inverse = getBaseRate(xCcy2Pair.createInverse());
                if (inverse != null)
                    xCcy2 = inverse.createInverse(precisionForInverseFxRate);
            }
            if (xCcy2 != null)
                return CrossRateCalculator.calculateCross(ccyPair, xCcy1, xCcy2, precisionForFxRate,
                        precisionForInverseFxRate, majorCurrencyRanking, bidRounding, askRounding);
        }
        return null;
    }
}
