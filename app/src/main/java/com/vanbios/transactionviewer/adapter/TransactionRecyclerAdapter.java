package com.vanbios.transactionviewer.adapter;

import android.content.Context;

import com.vanbios.transactionviewer.enums.CurrencyEnum;
import com.vanbios.transactionviewer.object.Transaction;
import com.vanbios.transactionviewer.util.FormatUtil;

import java.util.List;

/**
 * @author Ihor Bilous
 */
public class TransactionRecyclerAdapter extends GenericRecyclerAdapter {

    @SuppressWarnings("unchecked")
    public TransactionRecyclerAdapter(Context context, List<Transaction> list) {
        super(context, list);
    }

    @Override
    public void onBindViewHolder(GenericViewHolder holder, int position) {
        Transaction transaction = (Transaction) list.get(position);
        holder.tvTitle.setText(String.format("%s%s",
                CurrencyEnum.getTitleByCurrency(transaction.getCurrency()),
                FormatUtil.doubleToStringFormatter(transaction.getAmount())));
        holder.tvSubTitle.setText(String.format("%s%s",
                CurrencyEnum.GBP.getTitle(),
                FormatUtil.doubleToStringFormatter(transaction.getGbpAmount())));
    }
}