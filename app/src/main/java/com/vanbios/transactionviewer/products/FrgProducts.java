package com.vanbios.transactionviewer.products;

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
import com.vanbios.transactionviewer.common.utils.ui.ToastManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.Unbinder;

import static butterknife.ButterKnife.bind;

/**
 * @author Ihor Bilous
 */

public class FrgProducts extends Fragment implements ProductsMVP.View {

    private View view;
    @BindView(R.id.recyclerFrgProducts)
    RecyclerView recyclerView;
    @BindView(R.id.tvFrgProductsEmptyList)
    TextView tvEmptyList;
    private Unbinder unbinder;
    private ProductRecyclerAdapter recyclerAdapter;
    private List<ProductViewModel> productList;

    @Inject
    ToastManager toastManager;

    @Inject
    ProductsMVP.Presenter presenter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_products, container, false);

        ((App) getActivity().getApplication()).getComponent().inject(this);

        productList = new ArrayList<>();
        initViews();
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

    @Override
    public void onStart() {
        super.onStart();
        presenter.setView(this);
        presenter.loadData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void updateProductsList(List<ProductViewModel> list) {
        productList.clear();
        productList.addAll(list);
        recyclerAdapter.notifyDataSetChanged();
        setVisibility();
    }

    @Override
    public void showErrorMessage(String message) {
        toastManager.showClosableToast(getActivity(), message, ToastManager.LONG);
        setVisibility();
    }
}