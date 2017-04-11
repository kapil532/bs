package com.langoor.app.blueshak.Messaging.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.langoor.app.blueshak.Messaging.data.User;
import com.langoor.app.blueshak.Messaging.helper.Constants;
import com.langoor.app.blueshak.global.GlobalFunctions;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Bryan Yang on 9/18/2015.
 */
public class UserManager {

    public interface LogInListener
    {
        void onLogInFinished(boolean isSuccess);
    }

    
    private static final String  PREFERENCES_KEY_GCM_REG_TOKEN = "gcm_reg_token";

    private static UserManager mInstance;
    private static Context mCtx;
    private SharedPreferences mSharedPrefs;
    private List<User> mUserList;
    private LogInListener mLoginListener;
    private User mSignedInUser;

    private String mSessionKey;
    private boolean isGroupChat=false;
    private String groupChatLabel;
    private Boolean isFromNotification=false;
    private Boolean isFromContacts=false;
    private boolean checked = false ;

    private User mTargetUser;
    private UserManager(Context context)
    {
        mCtx = context.getApplicationContext();
        mSharedPrefs = mCtx.getSharedPreferences(Constants.PREFERENCES_FILENAME, Context.MODE_PRIVATE);
    }

    public static synchronized UserManager getInstance(Context context)
    {
        if (mInstance == null)
            mInstance = new UserManager(context);

        return mInstance;
    }

    public List<User> getUserlist(){
        return mUserList;
    }

    public void init()
    {
        if(mUserList == null)
            mUserList = new ArrayList<User>();
    }

    public boolean isLoginNameValid(String loginName)
    {
        if(TextUtils.isEmpty(loginName))
            return false;

        for(User cursorUser : mUserList) {
            if(cursorUser.getName().equalsIgnoreCase(loginName))
                return true;
        }
        return false ;
    }
    public String getGroupChatLabel()
    {
        return groupChatLabel;
    }
    public String setGroupChatLabel(String groupChatLabel)
    {
        return this.groupChatLabel=groupChatLabel;
    }

    private String getGCMRegToken()
    {
        return mSharedPrefs.getString(PREFERENCES_KEY_GCM_REG_TOKEN, "");
    }

    private void setSignedInUser(User mSignedInUser)
    {
       this.mSignedInUser=mSignedInUser;
    }


    public void setLoginListener (LogInListener listener)
    {
        mLoginListener = listener;
    }





    /*
          This is to be called on Volley's worker thread just after the create-session
          web request comes back & before onNewSessionResponse() gets called on the main thread

          This would give us a chance to do extra work without blocking the UI
          Also, the GoogleCloudMessaging.getInstance(mCtx).register() is a blocking call & can NOT
          be called on the main thread.
    */
    private void onNewSessionCallback()
    {
        if(TextUtils.isEmpty(getGCMRegToken()))
        {
            try {
                String gcmToken = GoogleCloudMessaging.getInstance(mCtx).register(Constants.GCM_SENDER_ID);
                SharedPreferences.Editor editor = mSharedPrefs.edit();
                editor.putString(PREFERENCES_KEY_GCM_REG_TOKEN, gcmToken);
                editor.commit();
            } catch (Exception e) { }
        }
    }

    public User getSignedInUser(){
         return GlobalFunctions.getSingedUser(mCtx);
    }

    public String getCurrentSessionKey()
    {
        return mSessionKey;
    }
    public void setTargetUser(User mTargetUser)
    {
        this.mTargetUser=mTargetUser;
    }
    public User getTargetUser(){
        return mTargetUser;
    }
}
