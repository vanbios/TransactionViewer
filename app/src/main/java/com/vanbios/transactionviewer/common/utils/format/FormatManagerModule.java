package com.vanbios.transactionviewer.common.utils.format;

import dagger.Module;
import dagger.Provides;

/**
 * @author Ihor Bilous
 */

@Module
public class FormatManagerModule {

    @Provides
    public FormatManager provideFormatManager() {
        return new FormatManagerImpl();
    }
}
