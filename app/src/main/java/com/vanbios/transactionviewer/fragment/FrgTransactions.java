package com.vanbios.transactionviewer.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vanbios.transactionviewer.R;
import com.vanbios.transactionviewer.adapter.TransactionRecyclerAdapter;
import com.vanbios.transactionviewer.object.Product;
import com.vanbios.transactionviewer.object.Transaction;
import com.vanbios.transactionviewer.singleton.InfoSingleton;
import com.vanbios.transactionviewer.util.FormatUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.observables.MathObservable;
import rx.schedulers.Schedulers;

import static butterknife.ButterKnife.bind;

/**
 * @author Ihor Bilous
 */
public class FrgTransactions extends Fragment {

    private View view;
    @BindView(R.id.recyclerFrgTransactions)
    RecyclerView recyclerView;
    @BindView(R.id.tvFrgTransactionsTotal)
    TextView tvTotal;
    private Unbinder unbinder;
    private TransactionRecyclerAdapter recyclerAdapter;
    private List<Transaction> transactionList;

    public static final String PRODUCT = "product";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_transactions, container, false);
        transactionList = new ArrayList<>();
        initViews();
        loadData();
        return view;
    }

    private void initViews() {
        unbinder = bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerAdapter = new TransactionRecyclerAdapter(getActivity(), transactionList);
        recyclerView.setAdapter(recyclerAdapter);
    }

    private void loadData() {
        String productName = getArguments().getString(PRODUCT);
        if (productName != null) {
            Product product = InfoSingleton.getInstance().getProductByName(productName);
            if (product != null) {
                transactionList.addAll(product.getTransactionsList());
                recyclerAdapter.notifyDataSetChanged();

                MathObservable.sumDouble(Observable.from(transactionList)
                        .map(Transaction::getGbpAmount))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(sum -> {
                            tvTotal.setText(String.format(
                                    getString(R.string.total_placeholder),
                                    FormatUtil.doubleToStringFormatter(sum)
                            ));
                        });
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
