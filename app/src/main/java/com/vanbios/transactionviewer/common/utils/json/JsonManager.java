package com.vanbios.transactionviewer.common.utils.json;

import android.content.Context;
import android.util.Pair;

import com.vanbios.transactionviewer.common.models.Rate;
import com.vanbios.transactionviewer.products.Product;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Ihor Bilous
 */

public interface JsonManager {

    String loadJSONFromAsset(Context context, String fileName);

    Map<String, Product> parseProducts(String json);

    Pair<List<Rate>, Set<String>> parseRates(String json);
}
