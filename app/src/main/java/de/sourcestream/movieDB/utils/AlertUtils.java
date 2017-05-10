package de.sourcestream.movieDB.utils;

import org.apache.http.conn.ConnectTimeoutException;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

public class AlertUtils {

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

}
