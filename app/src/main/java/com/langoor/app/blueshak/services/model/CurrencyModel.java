package com.langoor.app.blueshak.services.model;

import android.util.Log;

import com.langoor.app.blueshak.AppController;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CurrencyModel implements Serializable {
    private final String TAG = "CurrencyModel";
    private final String
            CURRENCY_ISO = "country_iso",
            CURRENCY = "currency",COUNTRY_CODE="country_code",PHONECODE="phonecode";

    String country_iso=null,
            currency=null,country_code=null,phonecode=null;

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getPhonecode() {
        return phonecode;
    }

    public void setPhonecode(String phonecode) {
        this.phonecode = phonecode;
    }

    boolean is_selected=false;

    public boolean is_selected() {
        return is_selected;
    }

    public void setIs_selected(boolean is_selected) {
        this.is_selected = is_selected;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCountry_iso() {
        return country_iso;
    }

    public void setCountry_iso(String country_iso) {
        this.country_iso = country_iso;
    }

    public boolean toObject(String jsonObject){
        try{
            JSONObject json = new JSONObject(jsonObject);
            country_iso = json.getString(CURRENCY_ISO);
            currency = json.getString(CURRENCY);
            country_code = json.getString(COUNTRY_CODE);
            phonecode = json.getString(PHONECODE);
            Log.d("COUNTRYCODESHORT","COUNTRYCODESHORT#@"+phonecode);
            if(currency.length()>1)
            {
                return true;
            }
            else {
                return false;
            }

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
            Log.d("COUNTRYCODESHORT","COUNTRYCODESHORT#@ss"+country_code);
            jsonMain.put(CURRENCY_ISO, country_iso);
            jsonMain.put(CURRENCY, currency);
            jsonMain.put(COUNTRY_CODE, country_code);
            jsonMain.put(PHONECODE, phonecode);
            returnString = jsonMain.toString();
        }
        catch (Exception ex){Log.d(TAG," To String Exception : "+ex);
           }
        return returnString;
    }
}
