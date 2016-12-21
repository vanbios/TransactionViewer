package com.vanbios.transactionviewer.common.utils.rates;

import com.vanbios.transactionviewer.common.model.Rate;

import java.util.List;
import java.util.Map;

/**
 * @author Ihor Bilous
 */
public interface RatesManager {
    Map<String, Double> findRates(List<Rate> rateList, List<String> currencyList);
}
