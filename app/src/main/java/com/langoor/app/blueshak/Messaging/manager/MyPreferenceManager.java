package com.langoor.app.blueshak.Messaging.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.langoor.app.blueshak.Messaging.data.ChatRoom;
import com.langoor.app.blueshak.Messaging.data.User;
import com.langoor.app.blueshak.Messaging.util.CommonUtil;
import com.langoor.app.blueshak.services.model.NotificationModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by Lincoln on 07/01/16.
 */
public class MyPreferenceManager {

    private String TAG = MyPreferenceManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "androidhive_gcm";

    // All Shared Preferences Keys
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_FIRST_NAME = "user_firstname";
    private static final String KEY_LAST_NAME = "user_lastname";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_PHONE_NUMBER = "user_phonenumber";
    private static final String KEY_USER_TOKEN = "user_token";
    private static final String KEY_USER_CC_ID = "user_ccid";
    private static final String KEY_PROFILE_PIC_URL = "user_profile_pic";
    private static final String KEY_PROFILE_UPDATED_AT = "user_profile_updated_at";
    private static final String KEY_PROFILE_CREATED_AT = "user_profile_created_at";
    private static final String KEY_LAST_LOGIN_AT = "user_last_login_at";
    private static final String KEY_NOTIFICATIONS = "notifications";

    private static final String KEY_BLUESHAK_NOTIFICATIONS = "blueshak_notifications";
    private static final String KEY_RECENTS_CHATS= "recents_chats";
    private static final String KEY_TIMEZONE= "timezone";

    // Constructor
    public MyPreferenceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

  /*  public MyPreferenceManager getPrefManager() {
        if (pref == null) {
            pref = new MyPreferenceManager(this);
        }

        return pref;
    }*/
    public void storeUser(User user) {
        editor.putString(KEY_FIRST_NAME, user.getName());
        editor.putString(KEY_LAST_NAME, user.getName());
        editor.putString(KEY_USER_EMAIL, user.getEmail());
        editor.putString(KEY_PHONE_NUMBER, user.getNumber());
        editor.putString(KEY_USER_CC_ID, user.getBs_id());
        editor.putString(KEY_PROFILE_PIC_URL, user.getProfileImageUrl());

        editor.commit();
        Log.e(TAG, "User is stored in shared preferences. " + user.getName() + ", " + user.getEmail());
    }

   /* public User getSingedUser() {
        if (pref.getString(KEY_FIRST_NAME, null) != null) {
            String email,firstName,lastName,profileImageUrl,number,token,profile_updated_at,profile_created_at,last_login_at,timezone;
            int cc_id;
            firstName = pref.getString(KEY_FIRST_NAME, null);
            lastName = pref.getString(KEY_LAST_NAME, null);
            email = pref.getString(KEY_USER_EMAIL, null);
            number=pref.getString(KEY_PHONE_NUMBER, null);
            token=pref.getString(KEY_USER_TOKEN, null);
            timezone=pref.getString(KEY_TIMEZONE,null);
            cc_id=pref.getInt(KEY_USER_CC_ID, 0);
            profileImageUrl=pref.getString(KEY_PROFILE_PIC_URL, null);
            profile_updated_at=pref.getString(KEY_PROFILE_UPDATED_AT, null);
            profile_created_at=pref.getString(KEY_PROFILE_CREATED_AT, null);
            last_login_at=pref.getString(KEY_LAST_LOGIN_AT, null);
            User user = new User(firstName,email,number,token,cc_id,profileImageUrl,profile_created_at,profile_updated_at,last_login_at,timezone);

            User user = new User(firstName,lastName,email,number,token,cc_id,profileImageUrl,profile_created_at,profile_updated_at,last_login_at,timezone);
            return user;
        }
        return null;
    }*/
    public  void saveGenericData(String data,String key,String prefrenceName,Context ctx)
    {
        editor.putString(key, data);
        editor.commit();
    }

    public String returnGenericData(String key,String prefrenceName,Context ctx)
    {
         return pref.getString(key, "");
    }
    public void addNotification(String notification) {

        // get old notifications
        String oldNotifications = getNotifications();
        if (oldNotifications != null) {
            oldNotifications += "|" + notification;
        } else {
            oldNotifications = notification;
        }

        editor.putString(KEY_NOTIFICATIONS, oldNotifications);
        editor.commit();
    }

    public String getNotifications() {
        return pref.getString(KEY_NOTIFICATIONS, null);
    }


    public void clearToken(){
        editor.remove(KEY_USER_TOKEN);
        editor.commit();
    }
    public void clear() {
        editor.clear();
        editor.commit();
    }


    public void storeList(ArrayList<User> data, String key, String prefrenceName, Context ctx) {
// used for store arrayList in json format
        editor = pref.edit();
        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(data);
        editor.putString(key, jsonFavorites);
        editor.commit();
    }
    public ArrayList<User> loadList(String key, String prefrenceName, Context ctx) {
        // used for retrieving arraylist from json formatted string
       // List<User> favorites;
        List<User> favorites;
         if (pref.contains(key)) {
            String jsonFavorites = pref.getString(key, null);
            Gson gson = new Gson();
            //	BeanSampleList[] favoriteItems = gson.fromJson(jsonFavorites,BeanSampleList[].class);
           // User[] users=gson.fromJson(jsonFavorites,User[].class);
             User[] users=gson.fromJson(jsonFavorites,User[].class);
            favorites = Arrays.asList(users);
            favorites = new ArrayList(favorites);
        } else
            return null;
        return (ArrayList) favorites;
    }
    public ArrayList<ChatRoom> getRecentChats() {
        List<ChatRoom> favorites=null;
        if (pref.contains(KEY_RECENTS_CHATS)) {
            String jsonFavorites = pref.getString(KEY_RECENTS_CHATS, null);
            if(jsonFavorites!=null){
                Gson gson = new Gson();
                ChatRoom[] users=gson.fromJson(jsonFavorites,ChatRoom[].class);
                favorites = Arrays.asList(users);
                favorites = new ArrayList(favorites);
                CommonUtil.sortRecentChats((ArrayList) favorites);

              /*  if(favorites.size()>1)
                    CommonUtil.sortRecentChats((ArrayList) favorites);*/
            }
        } else
            return null;

        return (ArrayList) favorites;
    }
    public ArrayList<ChatRoom> getCount() {
        List<ChatRoom> favorites=null;
        if (pref.contains(KEY_RECENTS_CHATS)) {
            String jsonFavorites = pref.getString(KEY_RECENTS_CHATS, null);
            if(jsonFavorites!=null){
                Gson gson = new Gson();
                ChatRoom[] users=gson.fromJson(jsonFavorites,ChatRoom[].class);
                favorites = Arrays.asList(users);
                favorites = new ArrayList(favorites);


            }
        } else
            return null;

        return (ArrayList) favorites;
    }
    public void saveRecentsChats(ChatRoom data) {
       ArrayList<ChatRoom> list= getRecentChats();
        if(list==null)
            list=new ArrayList<ChatRoom>();

        list.add(data);
        editor = pref.edit();
        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(list);
        editor.putString(KEY_RECENTS_CHATS, jsonFavorites);
        editor.commit();
    }
    public void updateRecentsChats(ArrayList<ChatRoom> data) {

        editor = pref.edit();
        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(data);
        editor.putString(KEY_RECENTS_CHATS, jsonFavorites);
        editor.commit();
    }
    public ArrayList<NotificationModel> getBlueshakNotifications() {
        List<NotificationModel> favorites=null;
        if (pref.contains(KEY_BLUESHAK_NOTIFICATIONS)) {
            String jsonFavorites = pref.getString(KEY_BLUESHAK_NOTIFICATIONS, null);
            if(jsonFavorites!=null){
                Gson gson = new Gson();
                //	BeanSampleList[] favoriteItems = gson.fromJson(jsonFavorites,BeanSampleList[].class);
                // User[] users=gson.fromJson(jsonFavorites,User[].class);
                NotificationModel[] users=gson.fromJson(jsonFavorites,NotificationModel[].class);
                favorites = Arrays.asList(users);
                favorites = new ArrayList(favorites);
                CommonUtil.sortNotifications((ArrayList) favorites);
                /*     CommonUtil.sortNotifications((ArrayList) favorites);*/
            }

        } else
            return null;

        return (ArrayList) favorites;
    }
    public void saveNotifications(NotificationModel data) {
        ArrayList<NotificationModel> list= getBlueshakNotifications();
        if(list==null)
            list=new ArrayList<NotificationModel>();

        list.add(data);
        editor = pref.edit();
        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(list);
        editor.putString(KEY_BLUESHAK_NOTIFICATIONS, jsonFavorites);
        editor.commit();
    }

}
