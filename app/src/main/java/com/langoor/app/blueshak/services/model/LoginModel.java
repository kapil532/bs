package com.langoor.app.blueshak.services.model;

import android.util.Log;

import com.langoor.app.blueshak.AppController;

import org.json.JSONObject;

public class LoginModel {

    private final String TAG = "LoginModel";
    private final String
            EMAIL = "email",
            COUNTRY = "country",
            PASSWORD = "password";

    String email=null, password=null,country=null;

    public LoginModel(){}

    public LoginModel(String email, String password){
        this.email = email;
        this.password = password;
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

    public boolean toObject(String jsonObject){
        try{
            JSONObject json = new JSONObject(jsonObject);
            email = json.getString(EMAIL);
            password = json.getString(PASSWORD);
            return true;
        }catch(Exception ex){Log.d(TAG, "Json Exception : " + ex);
           }
        return false;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString(){
        String returnString = null;
        try{

            JSONObject jsonMain = new JSONObject();
            jsonMain.put(EMAIL, email);
            jsonMain.put(PASSWORD, password);
            jsonMain.put(COUNTRY, country);
            returnString = jsonMain.toString();
        }
        catch (Exception ex){Log.d(TAG," To String Exception : "+ex);
           }
        return returnString;
    }
}
