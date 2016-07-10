package com.vanbios.transactionviewer.util.rates;

import android.util.Pair;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.vanbios.transactionviewer.enums.CurrencyEnum;
import com.vanbios.transactionviewer.object.Rate;
import com.vanbios.transactionviewer.util.rates.converter.CurrencyPair;
import com.vanbios.transactionviewer.util.rates.converter.FxRate;
import com.vanbios.transactionviewer.util.rates.converter.FxRateCalculator;
import com.vanbios.transactionviewer.util.rates.converter.FxRateCalculatorBuilder;
import com.vanbios.transactionviewer.util.rates.converter.FxRateCalculatorImpl;
import com.vanbios.transactionviewer.util.rates.converter.FxRateImpl;
import com.vanbios.transactionviewer.util.rates.util.BigDecimalUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Ihor Bilous
 */
public class RatesHelper {

    public static Map<String, Double> findRates(List<Rate> rateList, List<String> currencyList) {
        final FxRateCalculatorBuilder builder = new FxRateCalculatorBuilder();

        Set<String> notCrossSet = new HashSet<>();

        Stream.of(rateList)
                .filter(r -> r.getFrom().equals(CurrencyEnum.GBP.name()) || r.getTo().equals(CurrencyEnum.GBP.name()))
                .forEach(r -> {
                    notCrossSet.add(r.getFrom());
                    notCrossSet.add(r.getTo());
                });

        List<String> curToCrossList = Stream.of(currencyList)
                .filter(s -> !notCrossSet.contains(s))
                .collect(Collectors.toList());

        Stream.of(rateList)
                .forEach(rate -> {
                    if ((!CurrencyEnum.GBP.name().equals(rate.getFrom()) && !curToCrossList.contains(rate.getTo())) || curToCrossList.size() > 1) {
                        builder.addRateSnapshot(
                                new FxRateImpl(CurrencyPair.of(rate.getFrom(), rate.getTo()), null, true, BigDecimalUtil.bd(rate.getRate())));
                    }
                });
        builder.orderedCurrenciesForCross(currencyList);
        final FxRateCalculator calc = new FxRateCalculatorImpl(builder);

        Map<String, Double> rateMap = new HashMap<>();

        Stream.of(currencyList)
                .filter(s -> !CurrencyEnum.GBP.name().equals(s))
                .forEach(s -> addRateToMap(rateMap, calcCurrToGbp(calc, s)));

        return rateMap;
    }

    private static void addRateToMap(Map<String, Double> map, Pair<String, Double> pair) {
        if (pair.second > 0) map.put(pair.first, pair.second);
    }

    private static Pair<String, Double> calcCurrToGbp(final FxRateCalculator calc, String curr) {
        final FxRate fx = calc.findFx(CurrencyPair.of(curr, CurrencyEnum.GBP.name()));
        return new Pair<>(curr, fx != null ? fx.getRate().doubleValue() : 0);
    }
}
