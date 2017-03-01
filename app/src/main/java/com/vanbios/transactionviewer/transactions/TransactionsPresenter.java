package com.vanbios.transactionviewer.transactions;

import android.content.Context;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.vanbios.transactionviewer.R;
import com.vanbios.transactionviewer.common.enums.CurrencyEnum;
import com.vanbios.transactionviewer.common.utils.format.FormatManager;

import java.util.List;

import hu.akarnokd.rxjava2.math.MathFlowable;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Ihor Bilous
 */

class TransactionsPresenter implements TransactionsMVP.Presenter {

    private TransactionsMVP.View view;
    private TransactionsMVP.Model model;
    private Context context;
    private FormatManager formatManager;
    private String productName;

    TransactionsPresenter(TransactionsMVP.Model model, Context context, FormatManager formatManager) {
        this.model = model;
        this.context = context;
        this.formatManager = formatManager;
    }

    @Override
    public void loadData() {
        if (productName != null) {
            List<Transaction> transactionsList = model.getTransactionsList(productName);
            if (transactionsList != null && view != null) {
                view.updateTransactionsList(transactionListToViewModelList(transactionsList));

                Flowable.fromIterable(transactionsList)
                        .map(Transaction::getGbpAmount)
                        .to(MathFlowable::sumDouble)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(sum -> {
                                    String total = String.format(
                                            context.getString(R.string.total_placeholder),
                                            formatManager.doubleToStringFormatter(sum)
                                    );

                                    if (view != null) {
                                        view.updateTransactionsTotal(total);
                                    }
                                },
                                Throwable::printStackTrace);
            }
        }
    }

    @Override
    public void initData(String productName) {
        this.productName = productName;
    }

    @Override
    public void setView(TransactionsMVP.View view) {
        this.view = view;
    }

    private List<TransactionViewModel> transactionListToViewModelList(List<Transaction> transactionList) {
        return Stream.of(transactionList).map(this::convertTransactionToViewModel).collect(Collectors.toList());
    }

    private TransactionViewModel convertTransactionToViewModel(Transaction transaction) {
        String title = String.format("%s%s",
                CurrencyEnum.getTitleByCurrency(transaction.getCurrency()),
                formatManager.doubleToStringFormatter(transaction.getAmount()));
        String subTitle = String.format("%s%s",
                CurrencyEnum.GBP.getTitle(),
                formatManager.doubleToStringFormatter(transaction.getGbpAmount()));
        return new TransactionViewModel(title, subTitle);
    }
}