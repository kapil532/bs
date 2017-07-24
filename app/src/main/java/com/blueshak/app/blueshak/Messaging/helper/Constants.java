package com.blueshak.app.blueshak.Messaging.helper;

import android.content.Context;
import android.content.pm.PackageInfo;

/**
 * Created by Bryan Yang on 9/22/2015.
 */
public class Constants {
    public static final  int NOTIFICATION_ID=1;
   // public static final String APPGRID_APP_KEY = "55dc7c03e4b0f6517c8d6eae";
    public static final String GCM_SENDER_ID = "226750935967";
    public static final String GCM_SERVER_API_KEY="AIzaSyBNhF4789_dZdwKJ756prjAPiFljdaIlso";
    public static final String GCM_TOKEN_KEY="AIzaSyCGH6H8pp1oifzXHDCmZlRKOl78GRTQP-4";
    /*Blueshak Deceloper Api keys*/
    public static final String PUSHWHOOSH_API_KEY = "cZTNSeO0vgtaerWke2rMooZ7wd6wB916VGOh4IhhcGZp6e5gcbdYKMO9z3g0wb6Pz4hdPHDcSfNFQK8UvhCd";
    public static final String PUSHWHOOSH_APP_CODE = "957DB-09E27";
    public static final String BROADCAST_ACTION_NOTIFICATION = "com.langoor.blueshak.gcm.notification";
    public static final String EXTENDED_DATA_MESSAGE = "com.langoor.blueshak.gcm.message";
    public static final String  PREFERENCES_FILENAME = "UserPreferences";

   /* *//*Blueshak Dev Accounts Api keis*//*
    public static final String PUSHWHOOSH_API_KEY = "pLFkGmP0VxOyzaHJmIdaEENX1bxJ6b2EgTzqdaa3n9uWvroYsB1IiJAkdHhgsu4AxeVouOQJ63ong4eAJcob";
    public static final String PUSHWHOOSH_APP_CODE = "8F8FF-9ED14";
   */

   static PackageInfo packageInfo;

    public  static String getVersionName(Context ctx)
    {
        try
        {
            packageInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(),0);
          return   packageInfo.versionName;
        }
        catch (Exception w)
        {

        }
        return "1.3.2";
    }


      public static String  getTextFromId(Context ctx,int id)
      {
        return  ctx.getResources().getString(id);
      }



}
