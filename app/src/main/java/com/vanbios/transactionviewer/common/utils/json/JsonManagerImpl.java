package com.vanbios.transactionviewer.common.utils.json;

import android.content.Context;
import android.util.Pair;

import com.vanbios.transactionviewer.products.Product;
import com.vanbios.transactionviewer.common.model.Rate;
import com.vanbios.transactionviewer.transactions.Transaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Ihor Bilous
 */
public class JsonManagerImpl implements JsonManager {

    @Override
    public String loadJSONFromAsset(Context context, String fileName) {
        String json;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    public Map<String, Product> parseProducts(String json) {
        Map<String, Product> productMap = new HashMap<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                double amount = jsonObj.getDouble("amount");
                String name = jsonObj.getString("sku");
                String currency = jsonObj.getString("currency");

                if (productMap.get(name) == null) {
                    List<Transaction> transactionList = new ArrayList<>();
                    transactionList.add(new Transaction(currency, amount, amount));
                    productMap.put(name, new Product(name, transactionList));
                } else {
                    productMap.get(name).getTransactionsList().add(new Transaction(currency, amount, amount));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return productMap;
    }

    @Override
    public Pair<List<Rate>, Set<String>> parseRates(String json) {
        List<Rate> rateList = new ArrayList<>();
        Set<String> currencySet = new HashSet<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                String from = jsonObj.getString("from");
                String to = jsonObj.getString("to");
                String rate = jsonObj.getString("rate");

                currencySet.add(from);
                currencySet.add(to);
                rateList.add(new Rate(from, to, rate));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Pair<>(rateList, currencySet);
    }

}
