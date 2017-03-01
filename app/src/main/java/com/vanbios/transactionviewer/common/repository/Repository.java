package com.vanbios.transactionviewer.common.repository;

import com.vanbios.transactionviewer.products.Product;
import com.vanbios.transactionviewer.common.models.Rate;

import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.Flowable;

/**
 * @author Ihor Bilous
 */

public interface Repository {

    void setProductMap(Map<String, Product> map);

    List<Product> productMapToList();

    Product getProductByName(String productName);

    boolean isProductMapEmpty();

    void setRateList(List<Rate> rateList);

    boolean isRateListEmpty();

    List<String> getCurrencyList();

    void setCurrencySet(Set<String> currencySet);

    Map<String, Product> getProductMap();

    List<Rate> getRateList();

    Flowable<List<Product>> getListProductObservable();

    Flowable<String> getLoadRatesObservable();
}