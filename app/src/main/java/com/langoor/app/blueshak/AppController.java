package com.langoor.app.blueshak;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
/*import com.android.volley.toolbox.ImageLoader;*/
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
/*import com.google.android.gms.analytics.GoogleAnalytics;*/
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.langoor.app.blueshak.Messaging.activity.ChatActivity;
import com.langoor.app.blueshak.Messaging.activity.MessageActivity;
import com.langoor.app.blueshak.Messaging.manager.MyPreferenceManager;
import com.google.android.gms.analytics.Tracker;
import com.langoor.app.blueshak.global.GlobalFunctions;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.pushwoosh.PushManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Manu
 * Langoor , Bangalore
 * on 1/12/2016.
 */
public class AppController extends MultiDexApplication {
    public static final String TAG = AppController.class.getSimpleName();
    private RequestQueue mRequestQueue;
/*    private ImageLoader mImageLoader;*/
    public boolean mUserFirstTimeLoggedIn = false;
    private static String acname;
    private static String mobile_no;
    private static String email;
    private static AppController mInstance;
    private AppController instance;
    private PowerManager.WakeLock wakeLock;
    MyPreferenceManager pref;
    private Context context;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        context=this;
        Fabric.with(this, new Crashlytics());
        registerActivityLifecycleCallbacks(new BaseActivityLifeCycleListener());
        FacebookSdk.sdkInitialize(getApplicationContext());
        if(GlobalFunctions.is_loggedIn(context))
            PushManager.getInstance(context).registerForPushNotifications();
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),  // replace with your unique package name
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                System.out.println("###KeyHash###"+Base64.encodeToString(md.digest(),Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        // UNIVERSAL IMAGE LOADER SETUP
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
        // END - UNIVERSAL IMAGE LOADER SETUP
    }

    public static String getEmail(Context ctx)
    {   String emai="";
        AccountManager am = AccountManager.get(ctx);
        Account[] accounts = am.getAccounts();
        for (Account ac : accounts)
        {
            acname = ac.name;

            if (acname.startsWith("91")) {
                mobile_no = acname;
            }else if(acname.endsWith("@gmail.com")||acname.endsWith("@yahoo.com")||acname.endsWith("@hotmail.com")){
                email = acname;
                return email;
            }

            // Take your time to look at all available accounts
            //Log.i("Accounts : ", "Accounts : " + email + mobile_no);
        }
        return emai;
    }

    public static String getMobileNumber(Context ctx)
    {

        String s1  ="";
        String main_data[] = {"data1", "is_primary", "data3", "data2", "data1", "is_primary", "photo_uri", "mimetype"};
        Object object = ctx.getContentResolver().query(Uri.withAppendedPath(android.provider.ContactsContract.Profile.CONTENT_URI, "data"),
                main_data, "mimetype=?",
                new String[]{"vnd.android.cursor.item/phone_v2"},
                "is_primary DESC");
        if (object != null)
        {
            do {
                if (!((Cursor) (object)).moveToNext())
                    break;
                s1 = ((Cursor) (object)).getString(4);
                if(s1.length() >2)
                {
                    return s1;
                }

                //Log.d("DATAT ","DATA"+s1);


            }
            while (true);
            ((Cursor) (object)).close();
        }

        return s1;
    }



    public static synchronized AppController getInstance() {
        return mInstance;
    }
    public MyPreferenceManager getPrefManager() {
        if (pref == null) {
            pref = new MyPreferenceManager(this);
        }

        return pref;
    }
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

   /* public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }
*/
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        req.setShouldCache(false);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        req.setShouldCache(false);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    /**
     * Dummy implementation of {@link android.app.Application.ActivityLifecycleCallbacks} such
     * that subclasses don't need override each and every methods in the interface
     */
    public static class BaseActivityLifeCycleListener implements ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            if (activity instanceof MessageActivity) {
                isRecentChatsActivityActivityVisible = true;
            }else if(activity instanceof ChatActivity) {
                isChatActivityVisible = true;
            }

        }

        @Override
        public void onActivityStarted(Activity activity) {
            if (activity instanceof MessageActivity) {
                isRecentChatsActivityActivityVisible = true;
            }else if(activity instanceof ChatActivity) {
                isChatActivityVisible = true;
            }

        }
        private static boolean isRecentChatsActivityActivityVisible=false;
        private static boolean isChatActivityVisible=false;


        public boolean isRecentChatsActivityActivityVisible() {
            return isRecentChatsActivityActivityVisible;
        }
        public boolean isChatActivityVisible() {
            return isChatActivityVisible;
        }

        @Override
        public void onActivityResumed(Activity activity) {
            if (activity instanceof MessageActivity) {
                Log.d(TAG,"MessageActivity################ onActivityResumed");
                isRecentChatsActivityActivityVisible = true;
            }else if(activity instanceof ChatActivity) {
                Log.d(TAG,"ChatActivity################ onActivityResumed");
                isChatActivityVisible = true;
            }


        }
        @Override
        public void onActivityPaused(Activity activity) {
            if (activity instanceof MessageActivity) {
                isRecentChatsActivityActivityVisible = false;
                Log.d(TAG,"MessageActivity################ onActivityPaused");
            }else if(activity instanceof ChatActivity) {
                Log.d(TAG,"ChatActivity################ onActivityPaused");
                isChatActivityVisible = false;
            }

        }

        @Override
        public void onActivityStopped(Activity activity) {
            if (activity instanceof MessageActivity) {
                isRecentChatsActivityActivityVisible = false;
            }else if(activity instanceof ChatActivity) {
                isChatActivityVisible = false;
            }

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            if (activity instanceof MessageActivity) {
                isRecentChatsActivityActivityVisible = false;
            }else if(activity instanceof ChatActivity) {
                isChatActivityVisible = false;
            }

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            if (activity instanceof MessageActivity) {
                isRecentChatsActivityActivityVisible = false;
            }else if(activity instanceof ChatActivity) {
                isChatActivityVisible = false;
            }

        }
    }
    public boolean isNetworkAvailable(Context inContext){
        ConnectivityManager ConnectMgr = (ConnectivityManager) inContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(ConnectMgr == null) return false;
        NetworkInfo NetInfo = ConnectMgr.getActiveNetworkInfo();
        if(NetInfo == null) return false;
        return NetInfo.isConnected();
    }
}
