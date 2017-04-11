package com.langoor.app.blueshak.Messaging.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.langoor.app.blueshak.Messaging.data.PushSetTagRequest;
import com.langoor.app.blueshak.Messaging.helper.NotificationFactory;
import com.langoor.app.blueshak.Messaging.manager.VolleyManager;
import com.langoor.app.blueshak.Messaging.util.URLUtil;
import com.langoor.app.blueshak.global.GlobalFunctions;
import com.langoor.app.blueshak.root.RootActivity;
import com.pushwoosh.BasePushMessageReceiver;
import com.pushwoosh.BaseRegistrationReceiver;
import com.pushwoosh.PushManager;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;

/**
 * Created by Atabek on 02/25/2016.
 */
public class PushActivity extends RootActivity {
    private static final String LTAG = "PushwooshNotification";
    boolean broadcastPush = true;
    private static Context context;
    private static Activity activity;
    /**
     * Called when the activity is first created.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        activity=this;
        if(GlobalFunctions.is_loggedIn(context)){
            //Register receivers for push notifications
            registerReceivers();
            context=this;
            activity=this;
            final PushManager pushManager = PushManager.getInstance(this);

            //Start push manager, this will count app open for Pushwoosh stats as well
            try {
                pushManager.onStartup(this);
            } catch (Exception e) {
                Log.e(LTAG, e.getLocalizedMessage());
            }

            //Register for push!
            pushManager.registerForPushNotifications();


            //once u register setTag
            setTags(context);

            getTags(context);

            //disable the notification builder
            pushManager.setNotificationFactory(new NotificationFactory());

        }
    }
    /**
     * Called when the activity receives a new intent.
     */
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        //have to check if we've got new intent as a part of push notification
        checkMessage(intent);
    }

    //Registration receiver
    BroadcastReceiver mBroadcastReceiver = new BaseRegistrationReceiver() {
        @Override
        public void onRegisterActionReceive(Context context, Intent intent) {
            checkMessage(intent);
        }
    };

    //Push message receiver
    private BroadcastReceiver mReceiver = new BasePushMessageReceiver() {
        @Override
        protected void onMessageReceive(Intent intent) {
            //JSON_DATA_KEY contains JSON payload of push notification.
            doOnMessageReceive(intent.getExtras().getString(JSON_DATA_KEY));
        }
    };

    //Registration of the receivers
    public void registerReceivers() {
        IntentFilter intentFilter = new IntentFilter(getPackageName() + ".action.PUSH_MESSAGE_RECEIVE");

        if (broadcastPush)
            registerReceiver(mReceiver, intentFilter, getPackageName() + ".permission.C2D_MESSAGE", null);

        registerReceiver(mBroadcastReceiver, new IntentFilter(getPackageName() + "." + PushManager.REGISTER_BROAD_CAST_ACTION));
    }

    public void unregisterReceivers() {
        //Unregister receivers on pause
        try {
            unregisterReceiver(mReceiver);
        } catch (Exception e) {
            // pass.
        }

        try {
            unregisterReceiver(mBroadcastReceiver);
        } catch (Exception e) {
            //pass through
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        //Re-register receivers on resume
        registerReceivers();
    }

    @Override
    public void onPause() {
        super.onPause();

        //Unregister receivers on pause
        unregisterReceivers();
    }

    /**
     * Will check PushWoosh extras in this intent, and fire actual method
     *
     * @param intent activity intent
     */
    private void checkMessage(Intent intent) {
        if (null != intent) {
            if (intent.hasExtra(PushManager.PUSH_RECEIVE_EVENT)) {
                doOnMessageReceive(intent.getExtras().getString(PushManager.PUSH_RECEIVE_EVENT));
            } else if (intent.hasExtra(PushManager.REGISTER_EVENT)) {
                doOnRegistered(intent.getExtras().getString(PushManager.REGISTER_EVENT));
            } else if (intent.hasExtra(PushManager.UNREGISTER_EVENT)) {
                doOnUnregistered(intent.getExtras().getString(PushManager.UNREGISTER_EVENT));
            } else if (intent.hasExtra(PushManager.REGISTER_ERROR_EVENT)) {
                doOnRegisteredError(intent.getExtras().getString(PushManager.REGISTER_ERROR_EVENT));
            } else if (intent.hasExtra(PushManager.UNREGISTER_ERROR_EVENT)) {
                doOnUnregisteredError(intent.getExtras().getString(PushManager.UNREGISTER_ERROR_EVENT));
            }

            resetIntentValues();
        }
    }

    public void doOnRegistered(String registrationId) {
      /*  if(ITNSApplication.debuggable(getApplicationContext()))*/
            Log.d(LTAG, "registered: " + registrationId);
    }

    public void doOnRegisteredError(String errorId) {
     /*   if(ITNSApplication.debuggable(getApplicationContext()))*/
            Log.d(LTAG, "registration error: " + errorId);
    }

    public void doOnUnregistered(String registrationId) {
      /*  if(ITNSApplication.debuggable(getApplicationContext()))*/
            Log.d(LTAG, "unregistered: " + registrationId);
    }

    public void doOnUnregisteredError(String errorId) {
       /* if(ITNSApplication.debuggable(getApplicationContext()))*/
            Log.d(LTAG, "deregistration error: " + errorId);
    }

    public void doOnMessageReceive(String message) {
     /*   if(ITNSApplication.debuggable(getApplicationContext()))*/
            Log.d(LTAG, "received push: " + message);

    }

    /**
     * Will check main Activity intent and if it contains any PushWoosh data, will clear it
     */
    private void resetIntentValues() {
        Intent mainAppIntent = getIntent();

        if (mainAppIntent.hasExtra(PushManager.PUSH_RECEIVE_EVENT)) {
            mainAppIntent.removeExtra(PushManager.PUSH_RECEIVE_EVENT);
        } else if (mainAppIntent.hasExtra(PushManager.REGISTER_EVENT)) {
            mainAppIntent.removeExtra(PushManager.REGISTER_EVENT);
        } else if (mainAppIntent.hasExtra(PushManager.UNREGISTER_EVENT)) {
            mainAppIntent.removeExtra(PushManager.UNREGISTER_EVENT);
        } else if (mainAppIntent.hasExtra(PushManager.REGISTER_ERROR_EVENT)) {
            mainAppIntent.removeExtra(PushManager.REGISTER_ERROR_EVENT);
        } else if (mainAppIntent.hasExtra(PushManager.UNREGISTER_ERROR_EVENT)) {
            mainAppIntent.removeExtra(PushManager.UNREGISTER_ERROR_EVENT);
        }

        setIntent(mainAppIntent);
    }

    public static void setTags(final Context ctx){
        Gson gson = new Gson();
       String android_id = Settings.Secure.getString(ctx.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        String request = gson.toJson(new PushSetTagRequest(Integer.toString(Integer.parseInt(GlobalFunctions.getSingedUser(ctx).getBs_id())), android_id));
        JSONObject jsonRequest;
        JsonObjectRequest jsonObjectRequest = null;
        try{
            jsonRequest = new JSONObject(request);
            jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    URLUtil.PUSHWHOOSH_SET_TAG,
                    jsonRequest,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject res = response;

                           /* if(ITNSApplication.debuggable(getApplicationContext()))*/
                                Log.d("THIS IS RESPONSE","RESPONSE---MES-->"+response.toString());

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                   /* if(ITNSApplication.debuggable(getApplicationContext()))*/
                        Log.d("THIS IS RESPONSE","RESPONSE---MES-->"+error.toString());

                    String file_string ="";
                    setTags(ctx);
                    NetworkResponse response = error.networkResponse;
                    if(response != null && response.data != null)
                    {
                        try
                        {
                            file_string = new String(error.networkResponse.data, "UTF-8");
                            JSONObject errorObj= new JSONObject(file_string);
                            String error_res;

                            if(errorObj.has("error")){
                                error_res  =errorObj.getString("error");
                            }else{
                                error_res="";
                            }
                           /* if(ITNSApplication.debuggable(getApplicationContext()))*/
                                Log.d("THIS IS RESPONSE","RESPONSE---MES-->"+file_string);
                        }
                        catch (UnsupportedEncodingException e)
                        {
                        } catch (JSONException e)
                        {
                        }
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObjectRequest != null) {
            VolleyManager.getInstance(ctx).addToRequestQueue(jsonObjectRequest);
        }
    }
    public void getTags(final Context ctx){
        Gson gson = new Gson();
        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        String request = gson.toJson(new PushSetTagRequest(Integer.toString(Integer.parseInt(GlobalFunctions.getSingedUser(getApplicationContext()).getBs_id())), android_id));
        JSONObject jsonRequest;
        JsonObjectRequest jsonObjectRequest = null;
        try{
            jsonRequest = new JSONObject(request);
            jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    URLUtil.PUSHWHOOSH_GET_TAG,
                    jsonRequest,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject res = response;

                           /* if(ITNSApplication.debuggable(getApplicationContext()))*/
                            Log.d("THIS IS RESPONSE ","###############RESPONSE---getTags-->"+response.toString());

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                   /* if(ITNSApplication.debuggable(getApplicationContext()))*/
                    Log.d("THIS IS RESPONSE","RESPONSE---MES-->"+error.toString());

                    String file_string ="";
                    setTags(ctx);
                    NetworkResponse response = error.networkResponse;
                    if(response != null && response.data != null)
                    {
                        try
                        {
                            file_string = new String(error.networkResponse.data, "UTF-8");
                            JSONObject errorObj= new JSONObject(file_string);
                            String error_res;

                            if(errorObj.has("error")){
                                error_res  =errorObj.getString("error");
                            }else{
                                error_res="";
                            }
                           /* if(ITNSApplication.debuggable(getApplicationContext()))*/
                            Log.d("THIS IS RESPONSE","RESPONSE---MES-->"+file_string);
                        }
                        catch (UnsupportedEncodingException e)
                        {
                        } catch (JSONException e)
                        {
                        }
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObjectRequest != null) {
            VolleyManager.getInstance(ctx).addToRequestQueue(jsonObjectRequest);
        }
    }
}