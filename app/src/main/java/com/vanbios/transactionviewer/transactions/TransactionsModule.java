package com.vanbios.transactionviewer.transactions;

import android.content.Context;

import com.vanbios.transactionviewer.common.repository.Repository;
import com.vanbios.transactionviewer.common.utils.format.FormatManager;

import dagger.Module;
import dagger.Provides;

/**
 * @author Ihor Bilous
 */

@Module
public class TransactionsModule {

    @Provides
    TransactionsMVP.Presenter provideTransactionsPresenter(TransactionsMVP.Model model, Context context, FormatManager formatManager) {
        return new TransactionsPresenter(model, context, formatManager);
    }

    @Provides
    TransactionsMVP.Model provideTransactionsModel(Repository repository) {
        return new TransactionsModel(repository);
    }
}