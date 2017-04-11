package com.langoor.app.blueshak.Messaging.gcm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

// a copy from the sample push client project
// https://bitbucket.org/accedo/appgrid-native-push-client-demo-android
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("##############Received#############################");
        final ComponentName service = new ComponentName(context.getPackageName(),GcmIntentService.class.getName());
        intent.setComponent(service);
        startWakefulService(context, intent);
        setResultCode(Activity.RESULT_OK);
    }
}
