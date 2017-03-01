package com.vanbios.transactionviewer.products;

import android.content.Context;

import com.vanbios.transactionviewer.common.repository.Repository;

import dagger.Module;
import dagger.Provides;

/**
 * @author Ihor Bilous
 */

@Module
public class ProductsModule {

    @Provides
    ProductsMVP.Presenter provideTransactionsPresenter(ProductsMVP.Model model, Context context) {
        return new ProductsPresenter(model, context);
    }

    @Provides
    ProductsMVP.Model provideTransactionsModel(Repository repository) {
        return new ProductsModel(repository);
    }
}