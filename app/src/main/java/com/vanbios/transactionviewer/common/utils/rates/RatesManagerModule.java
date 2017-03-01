package com.vanbios.transactionviewer.common.utils.rates;

import dagger.Module;
import dagger.Provides;

/**
 * @author Ihor Bilous
 */

@Module
public class RatesManagerModule {

    @Provides
    RatesManager provideRatesManager() {
        return new RatesManagerImpl();
    }
}