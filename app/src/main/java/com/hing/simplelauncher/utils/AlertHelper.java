package com.hing.simplelauncher.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.hing.simplelauncher.R;

/**
 * Created by HingTang on 5/1/18.
 */
public class AlertHelper {
    public static AlertDialog createAlert(Context context, String title, String message,
                                 String positiveText, DialogInterface.OnClickListener positiveClickListener,
                                 String negativeText, DialogInterface.OnClickListener negativeClickListener){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton(positiveText, positiveClickListener);
        alertDialog.setNegativeButton(negativeText, negativeClickListener);
        return alertDialog.create();
    }

    public static void showMessage(Context context, String message, DialogInterface.OnClickListener positiveClickListener){
        new AlertDialog.Builder(context).setMessage(message).setPositiveButton(R.string.message_ok,positiveClickListener)
                .setNegativeButton(R.string.message_cancel, null).create().show();
    }
}
