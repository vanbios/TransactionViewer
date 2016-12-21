package com.vanbios.transactionviewer.common.utils.ui;

import android.content.Context;

/**
 * @author Ihor Bilous
 */

public interface ToastManager {

    int SHORT = 1, LONG = 2;

    void showClosableToast(Context context, String text, int duration);
}
