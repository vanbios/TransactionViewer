package com.vanbios.transactionviewer.common.repository;

import android.content.Context;
import android.util.Pair;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.vanbios.transactionviewer.R;
import com.vanbios.transactionviewer.common.enums.CurrencyEnum;
import com.vanbios.transactionviewer.common.models.Rate;
import com.vanbios.transactionviewer.common.utils.json.JsonManager;
import com.vanbios.transactionviewer.common.utils.rates.RatesManager;
import com.vanbios.transactionviewer.products.Product;
import com.vanbios.transactionviewer.transactions.Transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Getter;
import rx.Observable;

/**
 * @author Ihor Bilous
 */

public class InMemoryRepository implements Repository {

    @Getter
    private Map<String, Product> productMap;
    @Getter
    private List<Rate> rateList;
    private Set<String> currencySet;
    private Context context;
    private JsonManager jsonManager;
    private RatesManager ratesManager;


    public InMemoryRepository(Context context, JsonManager jsonManager, RatesManager ratesManager) {
        productMap = new HashMap<>();
        rateList = new ArrayList<>();
        currencySet = new HashSet<>();
        this.context = context;
        this.jsonManager = jsonManager;
        this.ratesManager = ratesManager;
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

    @Override
    public Observable<List<Product>> getListProductObservable() {
        return Observable.create(subscriber -> {
            if (isProductMapEmpty()) {
                String json = jsonManager.loadJSONFromAsset(context,
                        context.getString(R.string.transactions_file_name));
                if (json != null) {
                    if (json.length() > 0) {
                        setProductMap(jsonManager.parseProducts(json));
                    } else {
                        subscriber.onError(new IOException(String.format(
                                context.getString(R.string.file_is_empty_placeholder),
                                context.getString(R.string.transactions_file_name))));
                    }
                } else {
                    subscriber.onError(new IOException(String.format(
                            context.getString(R.string.file_not_found_placeholder),
                            context.getString(R.string.transactions_file_name))));
                }
            }
            subscriber.onNext(productMapToList());
            subscriber.onCompleted();
        });
    }

    @Override
    public Observable<String> getLoadRatesObservable() {
        return Observable.create(subscriber -> {
            if (isRateListEmpty()) {
                String json = jsonManager.loadJSONFromAsset(context,
                        context.getString(R.string.rates_file_name));
                if (json != null) {
                    if (json.length() > 0) {
                        Pair<List<Rate>, Set<String>> resultPair = jsonManager.parseRates(json);
                        setRateList(resultPair.first);
                        setCurrencySet(resultPair.second);
                        if (!isRateListEmpty()) {
                            Map<String, Double> rateMap = ratesManager.findRates(getRateList(), getCurrencyList());

                            Stream.of(rateMap)
                                    .forEach(p -> CurrencyEnum.updateRate(p.getKey(), p.getValue()));

                            Stream.of(getProductMap())
                                    .forEach(p -> updateTransactionsGbpAmount(p.getValue()));

                            subscriber.onNext(null);
                        }
                    } else {
                        subscriber.onError(new IOException(String.format(
                                context.getString(R.string.file_is_empty_placeholder),
                                context.getString(R.string.rates_file_name))));
                    }
                } else {
                    subscriber.onError(new IOException(String.format(
                            context.getString(R.string.file_not_found_placeholder),
                            context.getString(R.string.rates_file_name))));
                }
            }
            subscriber.onCompleted();
        });
    }

    private void updateTransactionsGbpAmount(Product product) {
        Stream.of(product.getTransactionsList())
                .forEach(t -> t.setGbpAmount(calculateGbpAmount(t)));
    }

    private double calculateGbpAmount(Transaction transaction) {
        return transaction.getAmount() * CurrencyEnum.getRateByCurrency(transaction.getCurrency());
    }
}
