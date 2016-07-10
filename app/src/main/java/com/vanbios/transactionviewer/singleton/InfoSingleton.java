package com.vanbios.transactionviewer.singleton;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.vanbios.transactionviewer.object.Product;
import com.vanbios.transactionviewer.object.Rate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Getter;

/**
 * @author Ihor Bilous
 */
public class InfoSingleton {

    private static volatile InfoSingleton instance;

    @Getter
    private Map<String, Product> productMap;
    @Getter
    private List<Rate> rateList;
    private Set<String> currencySet;

    private InfoSingleton() {
        productMap = new HashMap<>();
        rateList = new ArrayList<>();
        currencySet = new HashSet<>();
    }

    public void setProductMap(Map<String, Product> map) {
        productMap.clear();
        productMap.putAll(map);
    }

    @SuppressWarnings("unchecked")
    public List<Product> productMapToList() {
        return Stream.of(productMap)
                .map(Map.Entry::getValue)
                .sorted()
                .collect(Collectors.toList());
    }

    public Product getProductByName(String productName) {
        return productMap.get(productName);
    }

    public boolean isProductMapEmpty() {
        return productMap.isEmpty();
    }

    public void setRateList(List<Rate> rateList) {
        this.rateList.clear();
        this.rateList.addAll(rateList);
    }

    public boolean isRateListEmpty() {
        return rateList.isEmpty();
    }

    public List<String> getCurrencyList() {
        return new ArrayList<>(currencySet);
    }

    public void setCurrencySet(Set<String> currencySet) {
        this.currencySet.clear();
        this.currencySet.addAll(currencySet);
    }

    public static InfoSingleton getInstance() {
        InfoSingleton localInstance = instance;
        if (localInstance == null) {
            synchronized (InfoSingleton.class) {
                localInstance = instance;
                if (localInstance == null)
                    instance = localInstance = new InfoSingleton();
            }
        }
        return localInstance;
    }
}
