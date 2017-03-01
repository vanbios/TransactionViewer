package com.vanbios.transactionviewer.transactions;

import java.util.List;

/**
 * @author Ihor Bilous
 */

public interface TransactionsMVP {

    interface Model {

        List<Transaction> getTransactionsList(String productName);
    }

    interface View {

        void updateTransactionsList(List<TransactionViewModel> data);

        void updateTransactionsTotal(String total);
    }

    interface Presenter {

        void loadData();

        void initData(String productName);

        void setView(TransactionsMVP.View view);
    }
}