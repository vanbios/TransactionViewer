package com.vanbios.transactionviewer.transactions;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vanbios.transactionviewer.R;
import com.vanbios.transactionviewer.common.app.App;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.Unbinder;

import static butterknife.ButterKnife.bind;

/**
 * @author Ihor Bilous
 */

public class FrgTransactions extends Fragment implements TransactionsMVP.View {

    private View view;
    @BindView(R.id.recyclerFrgTransactions)
    RecyclerView recyclerView;
    @BindView(R.id.tvFrgTransactionsTotal)
    TextView tvTotal;
    private Unbinder unbinder;
    private TransactionRecyclerAdapter recyclerAdapter;
    private List<TransactionViewModel> transactionList;

    public static final String PRODUCT = "product";

    @Inject
    TransactionsMVP.Presenter presenter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_transactions, container, false);

        ((App) getActivity().getApplication()).getComponent().inject(this);

        transactionList = new ArrayList<>();
        initViews();
        return view;
    }

    private void initViews() {
        unbinder = bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerAdapter = new TransactionRecyclerAdapter(getActivity(), transactionList);
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.setView(this);
        presenter.initData(getArguments().getString(PRODUCT));
        presenter.loadData();
    }

    @Override
    public void updateTransactionsList(List<TransactionViewModel> data) {
        transactionList.addAll(data);
        recyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateTransactionsTotal(String total) {
        tvTotal.setText(total);
    }
}