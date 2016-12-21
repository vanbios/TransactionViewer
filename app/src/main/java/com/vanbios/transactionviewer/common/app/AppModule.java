package com.vanbios.transactionviewer.common.app;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import lombok.AllArgsConstructor;

/**
 * @author Ihor Bilous
 */

@Module
@AllArgsConstructor
public class AppModule {

    private Application application;

    @Provides
    @Singleton
    public Context provideContext() {
        return application;
    }
}
