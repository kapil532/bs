package com.langoor.app.blueshak.services.model;

import android.util.Log;
import com.langoor.app.blueshak.AppController;
import org.json.JSONObject;
import java.io.Serializable;

public class FacebookRegisterModel implements Serializable{

    private final String TAG = "FacebookRegisterModel";
    private final String
            ID = "bs_id",
            IMAGE = "avatar",
            ALIAS_NAME = "alias_name",
            LAST_NAME = "last_name",
            COUNTRY = "country",
            CITY = "city",
            POSTAL_CODE = "postal_code",
            STATE = "state",
            EMAIL = "email",
            TOKEN = "token",
            NAME = "name",
            PHONE = "phone",
            IS_ACTIVE="is_active",
            IS_BANNED="is_banned",
            FACEBOOK_ID="fb_id",
            ADDRESS = "address",
            ASH_PHONE = "ask_phone",
            LATITUDE = "latitude",
            LONGITUDE = "longitude",
            ISD="isd",
            PASSWORD = "password";

    String
            id = null;
    String token=null;
    String avatar = null;
    String gcm_id = null;
    String imei = null;
    String app_vesion = null;
    String app_os = null;
    String is_otp_verified = null;
    String is_active = null;
    String app_download_date = null;
    String last_login = null;
    String updated_at = null;
    String created_at = null;
    String latitude = null;
    String country=null,last_name=null,isd=null;

    public String getTAG() {
        return TAG;
    }

    public String getAlias_name() {
        return alias_name;
    }

    public void setAlias_name(String alias_name) {
        this.alias_name = alias_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    String alias_name = null;

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    String longitude=null;

    int ask_phone=0;

    public int getAsk_phone() {
        return ask_phone;
    }

    public void setAsk_phone(int ask_phone) {
        this.ask_phone = ask_phone;
    }

    public String getFb_id() {
        return fb_id;
    }

    public void setFb_id(String fb_id) {
        this.fb_id = fb_id;
    }



    String email=null,fb_id=null,password = null, name = null, city = null, state = null, postalCode = null, address = "", phone = "";

    public FacebookRegisterModel(){}

    public FacebookRegisterModel(String email, String name, String phone, String address,String fb_id){
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.fb_id=fb_id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public boolean toObject(String jsonObject){
        System.out.println(TAG+"#######toObject######"+jsonObject.toString());
        try{
            JSONObject json = new JSONObject(jsonObject);
            if(json.has(ASH_PHONE))ask_phone =json.getInt(ASH_PHONE);
            if(json.has(PASSWORD))password = json.getString(PASSWORD);
            if(json.has(NAME))name = json.getString(NAME);
            if(json.has(EMAIL))email = json.getString(EMAIL);
            if(json.has(PHONE))phone = json.getString(PHONE);
            if(json.has(ADDRESS)) address = json.getString(ADDRESS);
            if(json.has(POSTAL_CODE))postalCode = json.getString(POSTAL_CODE);
            if(json.has(CITY))city =json.getString(CITY);
            if(json.has(STATE))state = json.getString(STATE);

            if(json.has(ID))id = json.getString(ID);
             if(json.has(IMAGE))avatar = json.getString(IMAGE);
            if(json.has(TOKEN))token = json.getString(TOKEN);


            return true;
        }catch(Exception ex){

            Log.d(TAG, "Json Exception : " + ex);}
        return false;
    }

    public String getIsd() {
        return isd;
    }

    public void setIsd(String isd) {
        this.isd = isd;
    }

    @Override
    public String toString(){
        String returnString = null;
        try{
            JSONObject jsonMain = new JSONObject();
            jsonMain.put(NAME, name);
            jsonMain.put(EMAIL, email);
            jsonMain.put(PHONE, phone);
            jsonMain.put(FACEBOOK_ID,fb_id);
            jsonMain.put(ADDRESS, address);
            jsonMain.put(POSTAL_CODE, postalCode);
            jsonMain.put(CITY, city);
            jsonMain.put(STATE, state);
            jsonMain.put(LATITUDE, latitude);
            jsonMain.put(LONGITUDE, longitude);
            jsonMain.put(ALIAS_NAME, alias_name);
            jsonMain.put(COUNTRY, country);
            jsonMain.put(LAST_NAME, last_name);
            jsonMain.put(ISD, isd);
            returnString = jsonMain.toString();
        }
        catch (Exception ex){Log.d(TAG," To String Exception : "+ex);
           }
        return returnString;
    }
}
