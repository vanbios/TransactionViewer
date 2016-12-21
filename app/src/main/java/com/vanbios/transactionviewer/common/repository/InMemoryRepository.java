package com.vanbios.transactionviewer.common.repository;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.vanbios.transactionviewer.products.Product;
import com.vanbios.transactionviewer.common.model.Rate;

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
public class InMemoryRepository implements Repository {

    //private static volatile Repository instance;

    @Getter
    private Map<String, Product> productMap;
    @Getter
    private List<Rate> rateList;
    private Set<String> currencySet;

    public InMemoryRepository() {
        productMap = new HashMap<>();
        rateList = new ArrayList<>();
        currencySet = new HashSet<>();
    }

    @Override
    public void setProductMap(Map<String, Product> map) {
        productMap.clear();
        productMap.putAll(map);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Product> productMapToList() {
        return Stream.of(productMap)
                .map(Map.Entry::getValue)
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public Product getProductByName(String productName) {
        return productMap.get(productName);
    }

    @Override
    public boolean isProductMapEmpty() {
        return productMap.isEmpty();
    }

    @Override
    public void setRateList(List<Rate> rateList) {
        this.rateList.clear();
        this.rateList.addAll(rateList);
    }

    @Override
    public boolean isRateListEmpty() {
        return rateList.isEmpty();
    }

    @Override
    public List<String> getCurrencyList() {
        return new ArrayList<>(currencySet);
    }

    @Override
    public void setCurrencySet(Set<String> currencySet) {
        this.currencySet.clear();
        this.currencySet.addAll(currencySet);
    }

    /*public static Repository getInstance() {
        Repository localInstance = instance;
        if (localInstance == null) {
            synchronized (InMemoryRepository.class) {
                localInstance = instance;
                if (localInstance == null)
                    instance = localInstance = new InMemoryRepository();
            }
        }
        return localInstance;
    }*/
}
