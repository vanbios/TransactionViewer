package com.vanbios.transactionviewer.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.vanbios.transactionviewer.R;
import com.vanbios.transactionviewer.adapter.ProductRecyclerAdapter;
import com.vanbios.transactionviewer.enums.CurrencyEnum;
import com.vanbios.transactionviewer.object.Product;
import com.vanbios.transactionviewer.object.Rate;
import com.vanbios.transactionviewer.object.Transaction;
import com.vanbios.transactionviewer.singleton.InfoSingleton;
import com.vanbios.transactionviewer.util.JSONParser;
import com.vanbios.transactionviewer.util.ToastUtil;
import com.vanbios.transactionviewer.util.rates.RatesHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.Unbinder;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static butterknife.ButterKnife.bind;

/**
 * @author Ihor Bilous
 */
public class FrgProducts extends Fragment {

    private View view;
    @BindView(R.id.recyclerFrgProducts)
    RecyclerView recyclerView;
    @BindView(R.id.tvFrgProductsEmptyList)
    TextView tvEmptyList;
    private Unbinder unbinder;
    private ProductRecyclerAdapter recyclerAdapter;
    private List<Product> productList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_products, container, false);
        productList = new ArrayList<>();
        initViews();
        loadData();
        return view;
    }

    private void initViews() {
        unbinder = bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerAdapter = new ProductRecyclerAdapter(getActivity(), productList);
        recyclerView.setAdapter(recyclerAdapter);
    }

    private void setVisibility() {
        if (recyclerView != null)
            recyclerView.setVisibility(productList.isEmpty() ? View.GONE : View.VISIBLE);
        if (tvEmptyList != null)
            tvEmptyList.setVisibility(productList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void loadData() {
        Observable.<List<Product>>create(subscriber -> {
            List<Product> items = new ArrayList<>();
            if (InfoSingleton.getInstance().isProductMapEmpty()) {
                String json = JSONParser.loadJSONFromAsset(getActivity(),
                        getString(R.string.transactions_file_name));
                if (json != null) {
                    if (json.length() > 0) {
                        InfoSingleton.getInstance().setProductMap(JSONParser.parseProducts(json));
                    } else {
                        subscriber.onError(new IOException(String.format(
                                getString(R.string.file_is_empty_placeholder),
                                getString(R.string.transactions_file_name))));
                    }
                } else {
                    subscriber.onError(new IOException(String.format(
                            getString(R.string.file_not_found_placeholder),
                            getString(R.string.transactions_file_name))));
                }
            }
            items = InfoSingleton.getInstance().productMapToList();
            subscriber.onNext(items);
            subscriber.onCompleted();
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<List<Product>>() {
                            @Override
                            public void onNext(List<Product> items) {
                                productList.clear();
                                productList.addAll(items);
                                recyclerAdapter.notifyDataSetChanged();
                                setVisibility();
                                loadRates();
                            }

                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                setVisibility();
                                ToastUtil.showClosableToast(getActivity(), e.getMessage(), ToastUtil.LONG);
                            }
                        }
                );
    }

    private void loadRates() {
        Observable.<String>create(subscriber -> {
            if (InfoSingleton.getInstance().isRateListEmpty()) {
                String json = JSONParser.loadJSONFromAsset(getActivity(),
                        getString(R.string.rates_file_name));
                if (json != null) {
                    if (json.length() > 0) {
                        Pair<List<Rate>, Set<String>> resultPair = JSONParser.parseRates(json);
                        InfoSingleton.getInstance().setRateList(resultPair.first);
                        InfoSingleton.getInstance().setCurrencySet(resultPair.second);
                        if (!InfoSingleton.getInstance().isRateListEmpty()) {
                            Map<String, Double> rateMap = RatesHelper.findRates(
                                    InfoSingleton.getInstance().getRateList(),
                                    InfoSingleton.getInstance().getCurrencyList()
                            );

                            Stream.of(rateMap)
                                    .forEach(p -> CurrencyEnum.updateRate(p.getKey(), p.getValue()));

                            Stream.of(InfoSingleton.getInstance().getProductMap())
                                    .forEach(p -> updateTransactionsGbpAmount(p.getValue()));

                            subscriber.onNext(null);
                        }
                    } else {
                        subscriber.onError(new IOException(String.format(
                                getString(R.string.file_is_empty_placeholder),
                                getString(R.string.rates_file_name))));
                    }
                } else {
                    subscriber.onError(new IOException(String.format(
                            getString(R.string.file_not_found_placeholder),
                            getString(R.string.rates_file_name))));
                }
            }
            subscriber.onCompleted();
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onNext(String s) {
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showClosableToast(getActivity(), e.getMessage(), ToastUtil.LONG);
                    }
                });
    }

    private void updateTransactionsGbpAmount(Product product) {
        Stream.of(product.getTransactionsList())
                .forEach(t -> t.setGbpAmount(calculateGbpAmount(t)));
    }

    private double calculateGbpAmount(Transaction transaction) {
        return transaction.getAmount() * CurrencyEnum.getRateByCurrency(transaction.getCurrency());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
