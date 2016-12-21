package com.vanbios.transactionviewer.common.app;

import android.app.Application;

import com.vanbios.transactionviewer.common.repository.RepositoryModule;
import com.vanbios.transactionviewer.common.utils.format.FormatManagerModule;
import com.vanbios.transactionviewer.common.utils.json.JsonManagerModule;
import com.vanbios.transactionviewer.common.utils.rates.RatesManagerModule;
import com.vanbios.transactionviewer.common.utils.ui.ToastManagerModule;
import com.vanbios.transactionviewer.transactions.TransactionsModule;

import lombok.Getter;

/**
 * @author Ihor Bilous
 */

public class App extends Application {

    @Getter
    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }
}