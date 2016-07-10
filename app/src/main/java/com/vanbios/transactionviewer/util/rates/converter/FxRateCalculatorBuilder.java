package com.vanbios.transactionviewer.util.rates.converter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FxRateCalculatorBuilder {

    private Map<CurrencyPair, FxRate> ratesSnapshot = new HashMap<>();
    private MajorCurrencyRanking majorCurrencyRanking = StandardMajorCurrencyRanking.getDefault();
    private List<String> orderedCurrenciesForCross = new ArrayList<>();

    private int precisionForFxRate = 5;
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

    MajorCurrencyRanking getMajorCurrencyRanking() {
        return majorCurrencyRanking;
    }

    List<String> getOrderedCurrenciesForCross() {
        return Collections.unmodifiableList(orderedCurrenciesForCross);
    }

    int getPrecisionForFxRate() {
        return precisionForFxRate;
    }

    int getPrecisionForInverseFxRate() {
        return precisionForInverseFxRate;
    }

    boolean isCacheResults() {
        return true;
    }

    boolean isCacheBaseRates() {
        return true;
    }

    public FxRateCalculatorBuilder orderedCurrenciesForCross(final List<String> orderedCurrenciesForCross) {
        if (orderedCurrenciesForCross != null)
            this.orderedCurrenciesForCross = orderedCurrenciesForCross;
        return this;
    }

    public FxRateCalculatorBuilder addRateSnapshot(final FxRate rate) {
        if (rate != null)
            this.ratesSnapshot.put(rate.getCurrencyPair(), rate);
        return this;
    }

    Map<CurrencyPair, FxRate> getRatesSnapshot() {
        return ratesSnapshot;
    }

    int getBidRounding() {
        return BigDecimal.ROUND_HALF_DOWN;
    }

    int getAskRounding() {
        return BigDecimal.ROUND_HALF_UP;
    }
}
