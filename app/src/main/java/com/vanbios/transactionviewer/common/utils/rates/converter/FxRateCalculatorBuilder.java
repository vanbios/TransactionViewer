package com.vanbios.transactionviewer.common.utils.rates.converter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;


public class FxRateCalculatorBuilder {

    @Getter
    private Map<CurrencyPair, FxRate> ratesSnapshot = new HashMap<>();
    @Getter
    private MajorCurrencyRanking majorCurrencyRanking = StandardMajorCurrencyRanking.getDefault();
    private List<String> orderedCurrenciesForCross = new ArrayList<>();

    @Getter
    private int precisionForFxRate = 5;
    @Getter
    private int precisionForInverseFxRate = 12;


    public FxRateCalculatorBuilder() {
        orderedCurrenciesForCross.add("GBP");
        orderedCurrenciesForCross.add("AUD");
        orderedCurrenciesForCross.add("CAD");
        orderedCurrenciesForCross.add("EUR");
        orderedCurrenciesForCross.add("USD");
    }

    void checkValid() {
        final StringBuilder b = new StringBuilder();
        if (precisionForFxRate < 2) b.append("Precision for FX should be >=2");
        if (precisionForInverseFxRate < 2) {
            if (b.length() > 0) b.append(",");
            b.append("Precision for 1/FX should be >=2");
        }
        if (ratesSnapshot.isEmpty()) {
            if (b.length() > 0) b.append(",");
            b.append("You must provide Base Rates Snapshots OR a BaseFxRateProvider");
        }
        if (b.length() > 0) throw new IllegalArgumentException(b.toString());
    }

    List<String> getOrderedCurrenciesForCross() {
        return Collections.unmodifiableList(orderedCurrenciesForCross);
    }

    boolean isCacheResults() {
        return true;
    }

    boolean isCacheBaseRates() {
        return true;
    }

    public FxRateCalculatorBuilder orderedCurrenciesForCross(final List<String> orderedCurrenciesForCross) {
        if (orderedCurrenciesForCross != null) {
            this.orderedCurrenciesForCross = orderedCurrenciesForCross;
        }
        return this;
    }

    public FxRateCalculatorBuilder addRateSnapshot(final FxRate rate) {
        if (rate != null) {
            this.ratesSnapshot.put(rate.getCurrencyPair(), rate);
        }
        return this;
    }

    int getBidRounding() {
        return BigDecimal.ROUND_HALF_DOWN;
    }

    int getAskRounding() {
        return BigDecimal.ROUND_HALF_UP;
    }
}
