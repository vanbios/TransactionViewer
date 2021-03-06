package com.vanbios.transactionviewer.common.utils.ui;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Ihor Bilous
 */

@Module
public class ToastManagerModule {

    @Provides
    @Singleton
    ToastManager provideToastManager() {
        return new ToastManagerImpl();
    }
}