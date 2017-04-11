package com.langoor.app.blueshak.services.model;

import android.util.Log;

import com.langoor.app.blueshak.AppController;

import org.json.JSONObject;

import java.io.Serializable;

public class UserModel implements Serializable{
    private final String TAG = "UserModel";
    private final String
            ID = "bs_id",
            IMAGE = "avatar",
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
            ASH_PHONE = "ask_phone",
            FACEBOOK_ID="fb_id",
            ADDRESS = "address";
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


    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    public int getIs_banned() {
        return is_banned;
    }

    public void setIs_banned(int is_banned) {
        this.is_banned = is_banned;
    }

    String email=null,fb_id=null, token = null, name = null,country=null, address = "", phone = "", id, city, postalCode, state, image;
    int is_active=1,is_banned=0;
    public UserModel(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean toObject(String jsonObject){
        try{
            JSONObject json = new JSONObject(jsonObject);
            if(json.has(ASH_PHONE))ask_phone =json.getInt(ASH_PHONE);
            if(json.has(ID)) id = json.getString(ID);
            if(json.has(CITY))   city = json.getString(CITY);
            if(json.has(POSTAL_CODE))  postalCode = json.getString(POSTAL_CODE);
            if(json.has(STATE))   state = json.getString(STATE);
            if(json.has(IMAGE))image = json.getString(IMAGE);
            if(json.has(IS_ACTIVE))is_active=json.getInt(IS_ACTIVE);
            if(json.has(IS_BANNED))is_banned=json.getInt(IS_BANNED);
            if(json.has(TOKEN))token = json.getString(TOKEN);
            if(json.has(NAME))name = json.getString(NAME);
            if(json.has(EMAIL))email = json.getString(EMAIL);
            if(json.has(PHONE))phone = json.getString(PHONE);
            if(json.has(ADDRESS))address = json.getString(ADDRESS);
            if(json.has(COUNTRY))country = json.getString(COUNTRY);
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
            jsonMain.put(POSTAL_CODE, postalCode);
            jsonMain.put(STATE, state);
            jsonMain.put(IMAGE, image);
            jsonMain.put(CITY, city);
            jsonMain.put(TOKEN, token);
            jsonMain.put(NAME, name);
            jsonMain.put(EMAIL, email);
            jsonMain.put(PHONE, phone);
            jsonMain.put(ADDRESS, address);
            returnString = jsonMain.toString();
        }
        catch (Exception ex){Log.d(TAG," To String Exception : "+ex);
           }
        return returnString;
    }
}
