package com.vanbios.transactionviewer.util;

import android.content.Context;
import android.widget.Toast;

/**
 * @author Ihor Bilous
 */
public class ToastUtil {

    private static Toast staticToast;
    public static final int SHORT = 1, LONG = 2;

    public static void showClosableToast(Context context, String text, int duration) {
        if (staticToast != null) staticToast.cancel();
        staticToast = new Toast(context);

        switch (duration) {
            case SHORT:
                duration = Toast.LENGTH_SHORT;
                break;
            case LONG:
                duration = Toast.LENGTH_LONG;
                break;
        }

        staticToast = Toast.makeText(context, text, duration);
        staticToast.show();
    }
}
