package com.vanbios.transactionviewer.common.utils.json;

import android.content.Context;
import android.util.Pair;

import com.annimon.stream.Stream;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.vanbios.transactionviewer.common.models.Rate;
import com.vanbios.transactionviewer.common.models.TransactionData;
import com.vanbios.transactionviewer.products.Product;
import com.vanbios.transactionviewer.transactions.Transaction;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Ihor Bilous
 */

class JsonManagerImpl implements JsonManager {

    @Override
    public String loadJSONFromAsset(Context context, String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return json;
    }

    @Override
    public Map<String, Product> parseProducts(String json) {
        Map<String, Product> productMap = new HashMap<>();
        Type listType = new TypeToken<ArrayList<TransactionData>>() {
        }.getType();
        List<TransactionData> transactionDataList = new GsonBuilder().create().fromJson(json, listType);

        Stream.of(transactionDataList).forEach(transactionData -> {
            String sku = transactionData.getSku();
            String currency = transactionData.getCurrency();
            double amount = transactionData.getAmount();
            if (productMap.get(sku) == null) {
                List<Transaction> transactionList = new ArrayList<>();
                transactionList.add(new Transaction(currency, amount, amount));
                productMap.put(sku, new Product(sku, transactionList));
            } else {
                productMap.get(sku).getTransactionsList().add(new Transaction(currency, amount, amount));
            }
        });

        return productMap;
    }

    @Override
    public Pair<List<Rate>, Set<String>> parseRates(String json) {
        Set<String> currencySet = new HashSet<>();
        Type listType = new TypeToken<ArrayList<Rate>>() {
        }.getType();
        List<Rate> rateList = new GsonBuilder().create().fromJson(json, listType);

        Stream.of(rateList).forEach(rate -> {
            currencySet.add(rate.getFrom());
            currencySet.add(rate.getTo());
        });

        return new Pair<>(rateList, currencySet);
    }
}