package com.vanbios.transactionviewer.transactions;

import android.content.Context;

import com.vanbios.transactionviewer.common.ui.GenericRecyclerAdapter;

import java.util.List;

/**
 * @author Ihor Bilous
 */

class TransactionRecyclerAdapter extends GenericRecyclerAdapter {

    @SuppressWarnings("unchecked")
    TransactionRecyclerAdapter(Context context, List<TransactionViewModel> list) {
        super(context, list);
    }

    @Override
    public void onBindViewHolder(GenericViewHolder holder, int position) {
        TransactionViewModel model = (TransactionViewModel) list.get(position);
        holder.tvTitle.setText(model.getTitle());
        holder.tvSubTitle.setText(model.getSubTitle());
    }
}