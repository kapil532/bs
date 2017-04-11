package com.langoor.app.blueshak.services.model;

import android.util.Log;

import com.langoor.app.blueshak.AppController;

import org.json.JSONObject;

public class UserDetailsModel {

    private final String TAG = "UserDetailsModel";
    private final String
            ID = "id",
            NAME = "name",
            EMAIL = "email",
            PHONE = "phone",
            ADDRESS = "address",
            AVATAR = "avatar",
            GCMID = "gcm_id",
            IMEI = "imei",
            APPVERSION = "app_version",
            APPOS = "app_os",
            FBID = "fb_id",
            ISOTPVERIFIED = "is_otp_verified",
            ISACTIVE = "is_active",
            APPDOWNLOADDATE = "app_download_date",
            LASTLOGIN = "last_login",
            UPDATEDAT = "updated_at",
            CREATEDAT = "created_at";

    String
            id = null,
            name = null,
            email = null,
            phone=null,
            address = null,
            avatar = null,
            gcm_id = null,
            imei = null,
            app_vesion = null,
            app_os = null,
            fb_id = null,
            is_otp_verified = null,
            is_active = null,
            app_download_date = null,
            last_login = null,
            updated_at = null,
            created_at = null;

    public UserDetailsModel(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGcm_id() {
        return gcm_id;
    }

    public void setGcm_id(String gcm_id) {
        this.gcm_id = gcm_id;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getApp_vesion() {
        return app_vesion;
    }

    public void setApp_vesion(String app_vesion) {
        this.app_vesion = app_vesion;
    }

    public String getApp_os() {
        return app_os;
    }

    public void setApp_os(String app_os) {
        this.app_os = app_os;
    }

    public String getFb_id() {
        return fb_id;
    }

    public void setFb_id(String fb_id) {
        this.fb_id = fb_id;
    }

    public String getIs_otp_verified() {
        return is_otp_verified;
    }

    public void setIs_otp_verified(String is_otp_verified) {
        this.is_otp_verified = is_otp_verified;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getApp_download_date() {
        return app_download_date;
    }

    public void setApp_download_date(String app_download_date) {
        this.app_download_date = app_download_date;
    }

    public String getLast_login() {
        return last_login;
    }

    public void setLast_login(String last_login) {
        this.last_login = last_login;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public boolean toObject(String jsonObject){
        try{
            System.out.println("#######jsonObject#######"+jsonObject.toString());
            JSONObject json1 = new JSONObject(jsonObject);
            JSONObject json= json1.getJSONObject("user");
            System.out.println("#######json#######"+json.toString());

            id = json.getString(ID);
            name = json.getString(NAME);
            email = json.getString(EMAIL);
            phone = json.getString(PHONE);
            address = json.getString(ADDRESS);
            avatar = json.getString(AVATAR);
            gcm_id = json.getString(GCMID);
            imei = json.getString(IMEI);
            app_vesion = json.getString(APPVERSION);
            app_os = json.getString(APPOS);
            fb_id = json.getString(FBID);
            is_otp_verified = json.getString(ISOTPVERIFIED);
            is_active = json.getString(ISACTIVE);
            app_download_date = json.getString(APPDOWNLOADDATE);
            last_login = json.getString(LASTLOGIN);
            updated_at = json.getString(UPDATEDAT);
            created_at = json.getString(CREATEDAT);
            return true;
        }catch(Exception ex){
            Log.d(TAG, "Json Exception : " + ex);
           }
        return false;
    }

    @Override
    public String toString(){
        String returnString = null;
        try{
            JSONObject jsonMain = new JSONObject();
            jsonMain.put(ID, id);
            jsonMain.put(NAME, name);
            jsonMain.put(EMAIL, email);
            jsonMain.put(PHONE, phone);
            jsonMain.put(ADDRESS, address);
            jsonMain.put(AVATAR, avatar);
            jsonMain.put(GCMID, gcm_id);
            jsonMain.put(IMEI, imei);
            jsonMain.put(APPVERSION, app_vesion);
            jsonMain.put(APPOS, app_os);
            jsonMain.put(FBID, fb_id);
            jsonMain.put(ISOTPVERIFIED, is_otp_verified);
            jsonMain.put(ISACTIVE, is_active);
            jsonMain.put(APPDOWNLOADDATE, app_download_date);
            jsonMain.put(LASTLOGIN, last_login);
            jsonMain.put(UPDATEDAT, updated_at);
            jsonMain.put(CREATEDAT, created_at);
            returnString = jsonMain.toString();
        }
        catch (Exception ex){Log.d(TAG," To String Exception : "+ex);
           }
        return returnString;
    }
}


