package com.vanbios.transactionviewer.common.repository;

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
    public Repository provideRepository() {
        return new InMemoryRepository();
    }
}
