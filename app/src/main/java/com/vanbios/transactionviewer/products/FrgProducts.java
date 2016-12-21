package com.vanbios.transactionviewer.products;

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
import com.vanbios.transactionviewer.common.app.App;
import com.vanbios.transactionviewer.common.model.Rate;
import com.vanbios.transactionviewer.common.enums.CurrencyEnum;
import com.vanbios.transactionviewer.common.repository.Repository;
import com.vanbios.transactionviewer.common.utils.json.JsonManager;
import com.vanbios.transactionviewer.common.utils.rates.RatesManager;
import com.vanbios.transactionviewer.common.utils.ui.ToastManager;
import com.vanbios.transactionviewer.transactions.Transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

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

    @Inject
    RatesManager ratesManager;

    @Inject
    Repository repository;

    @Inject
    ToastManager toastManager;

    @Inject
    JsonManager jsonManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_products, container, false);

        ((App) getActivity().getApplication()).getComponent().inject(this);

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
            if (repository.isProductMapEmpty()) {
                String json = jsonManager.loadJSONFromAsset(getActivity(),
                        getString(R.string.transactions_file_name));
                if (json != null) {
                    if (json.length() > 0) {
                        repository.setProductMap(jsonManager.parseProducts(json));
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
            items = repository.productMapToList();
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
                                toastManager.showClosableToast(getActivity(), e.getMessage(), ToastManager.LONG);
                            }
                        }
                );
    }

    private void loadRates() {
        Observable.<String>create(subscriber -> {
            if (repository.isRateListEmpty()) {
                String json = jsonManager.loadJSONFromAsset(getActivity(),
                        getString(R.string.rates_file_name));
                if (json != null) {
                    if (json.length() > 0) {
                        Pair<List<Rate>, Set<String>> resultPair = jsonManager.parseRates(json);
                        repository.setRateList(resultPair.first);
                        repository.setCurrencySet(resultPair.second);
                        if (!repository.isRateListEmpty()) {
                            Map<String, Double> rateMap = ratesManager.findRates(
                                    repository.getRateList(),
                                    repository.getCurrencyList()
                            );

                            Stream.of(rateMap)
                                    .forEach(p -> CurrencyEnum.updateRate(p.getKey(), p.getValue()));

                            Stream.of(repository.getProductMap())
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
                        toastManager.showClosableToast(getActivity(), e.getMessage(), ToastManager.LONG);
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