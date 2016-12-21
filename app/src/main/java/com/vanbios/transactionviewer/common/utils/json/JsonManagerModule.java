package com.vanbios.transactionviewer.common.utils.json;

import dagger.Module;
import dagger.Provides;

/**
 * @author Ihor Bilous
 */

@Module
public class JsonManagerModule {

    @Provides
    public JsonManager provideJSONManager() {
        return new JsonManagerImpl();
    }
}
