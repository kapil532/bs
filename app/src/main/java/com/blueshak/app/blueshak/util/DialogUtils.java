package com.blueshak.app.blueshak.util;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.blueshak.app.blueshak.MainActivity;
import com.blueshak.app.blueshak.global.GlobalVariables;
import com.blueshak.blueshak.R;

/**
 * Created by lsingh013 on 20/02/18.
 */

public class DialogUtils {

    public static void showAlertDialog(final Activity activity,String message) {
        final com.blueshak.app.blueshak.view.AlertDialog alertDialog = new com.blueshak.app.blueshak.view.AlertDialog(activity);
        alertDialog.setTitle(activity.getString(R.string.app_name));
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("Yes", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });

        alertDialog.setNegativeButton("No", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        alertDialog.show();
    }

    public static void showPositiveAlert(final Activity activity,String message) {
        final com.blueshak.app.blueshak.view.AlertDialog alertDialog = new com.blueshak.app.blueshak.view.AlertDialog(activity);
        alertDialog.setTitle(activity.getString(R.string.app_name));
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                activity.setResult(activity.RESULT_CANCELED);
                activity.finish();
            }
        });
        alertDialog.show();
    }
}
