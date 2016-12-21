package com.vanbios.transactionviewer.common.app;

import com.vanbios.transactionviewer.common.ui.MainActivity;
import com.vanbios.transactionviewer.common.repository.RepositoryModule;
import com.vanbios.transactionviewer.common.utils.format.FormatManagerModule;
import com.vanbios.transactionviewer.common.utils.json.JsonManagerModule;
import com.vanbios.transactionviewer.common.utils.rates.RatesManagerModule;
import com.vanbios.transactionviewer.common.utils.ui.ToastManagerModule;
import com.vanbios.transactionviewer.products.FrgProducts;
import com.vanbios.transactionviewer.products.ProductsModule;
import com.vanbios.transactionviewer.transactions.FrgTransactions;
import com.vanbios.transactionviewer.transactions.TransactionsModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Ihor Bilous
 */

@Singleton
@Component(modules = {
        AppModule.class,
        RatesManagerModule.class,
        RepositoryModule.class,
        ToastManagerModule.class,
        FormatManagerModule.class,
        JsonManagerModule.class,
        TransactionsModule.class,
        ProductsModule.class
})
public interface AppComponent {

    void inject(MainActivity mainActivity);

    void inject(FrgProducts frgProducts);

    void inject(FrgTransactions frgTransactions);
}
