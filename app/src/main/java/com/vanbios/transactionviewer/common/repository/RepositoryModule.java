package com.vanbios.transactionviewer.common.repository;

import android.content.Context;

import com.vanbios.transactionviewer.common.utils.json.JsonManager;
import com.vanbios.transactionviewer.common.utils.rates.RatesManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Ihor Bilous
 */

@Module
public class RepositoryModule {

    @Provides
    @Singleton
    Repository provideRepository(Context context, JsonManager jsonManager, RatesManager ratesManager) {
        return new InMemoryRepository(context, jsonManager, ratesManager);
    }
}