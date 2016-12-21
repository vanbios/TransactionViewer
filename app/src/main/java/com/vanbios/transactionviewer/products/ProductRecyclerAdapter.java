package com.vanbios.transactionviewer.products;

import android.content.Context;
import android.os.Bundle;

import com.vanbios.transactionviewer.common.enums.FrgEnum;
import com.vanbios.transactionviewer.common.ui.GenericRecyclerAdapter;
import com.vanbios.transactionviewer.common.ui.MainActivity;
import com.vanbios.transactionviewer.transactions.FrgTransactions;

import java.util.List;

/**
 * @author Ihor Bilous
 */

public class ProductRecyclerAdapter extends GenericRecyclerAdapter {

    @SuppressWarnings("unchecked")
    public ProductRecyclerAdapter(Context context, List<ProductViewModel> list) {
        super(context, list);
    }

    @Override
    public void onBindViewHolder(GenericViewHolder holder, int position) {
        ProductViewModel model = (ProductViewModel) list.get(position);
        holder.tvTitle.setText(model.getTitle());
        holder.tvSubTitle.setText(model.getSubTitle());
        holder.view.setOnClickListener(view -> openTransactions(model.getProduct()));
    }

    private void openTransactions(Product product) {
        MainActivity activity = (MainActivity) context;
        FrgTransactions frg = new FrgTransactions();
        Bundle args = new Bundle();
        args.putString(FrgTransactions.PRODUCT, product.getName());
        frg.setArguments(args);
        activity.addFragment(frg, FrgEnum.TRANSACTIONS.name());
    }
}