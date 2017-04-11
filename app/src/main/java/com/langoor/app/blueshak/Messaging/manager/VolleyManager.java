package com.langoor.app.blueshak.Messaging.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.net.CookieHandler;
import java.net.CookieManager;


/**
 * Created by Bryan Yang on 2/16/2015.
 */

public class VolleyManager {
 
    private static VolleyManager mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static CookieManager mCookieManager;
    private static Context mCtx;

    private VolleyManager(Context context) 
    {
        mCtx = context;
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap>
                    cache = new LruCache<String, Bitmap>(getCacheSize());

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

    public static synchronized VolleyManager getInstance(Context context)
    {
        if (mInstance == null)
        {
            mInstance = new VolleyManager(context);
            mCookieManager = new CookieManager();
            CookieHandler.setDefault(mCookieManager);
        }
        return mInstance;
    }

    private int getCacheSize()
    {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        return cacheSize;
    }


    public RequestQueue getRequestQueue()
    {
        if (mRequestQueue == null) 
        {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req)
    {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader()
    {
        return mImageLoader;
    }

    public void cancelAllRequests()
    {
        mRequestQueue.cancelAll( new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });

    }
    public void clearCookie()
    {
        if(mCookieManager != null)
            mCookieManager.getCookieStore().removeAll();
    }

}