package com.vanbios.transactionviewer.transactions;

import android.content.Context;

import com.vanbios.transactionviewer.common.ui.GenericRecyclerAdapter;
import com.vanbios.transactionviewer.common.enums.CurrencyEnum;
import com.vanbios.transactionviewer.common.utils.format.FormatManager;

import java.util.List;

/**
 * @author Ihor Bilous
 */
public class TransactionRecyclerAdapter extends GenericRecyclerAdapter {

    private FormatManager formatManager;

    @SuppressWarnings("unchecked")
    public TransactionRecyclerAdapter(Context context, List<Transaction> list, FormatManager formatManager) {
        super(context, list);
        this.formatManager = formatManager;
    }

    @Override
    public void onBindViewHolder(GenericViewHolder holder, int position) {
        Transaction transaction = (Transaction) list.get(position);
        holder.tvTitle.setText(String.format("%s%s",
                CurrencyEnum.getTitleByCurrency(transaction.getCurrency()),
                formatManager.doubleToStringFormatter(transaction.getAmount())));
        holder.tvSubTitle.setText(String.format("%s%s",
                CurrencyEnum.GBP.getTitle(),
                formatManager.doubleToStringFormatter(transaction.getGbpAmount())));
    }
}