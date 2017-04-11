package com.langoor.app.blueshak.services.model;

import android.util.Log;

import org.json.JSONObject;

import java.io.Serializable;

public class VerifyAliasModel implements Serializable{
    private final String TAG = "VerifyAliasModel";
    private final String
            IS_EXISTING = "is_existing";
    private final String
            MESSAGE = "message";
    private final String
            ALIAS_NAME = "alias_name";
    boolean alias_available=false;
    String alias_name,message;

    public String getAlias_name() {
        return alias_name;
    }

    public void setAlias_name(String alias_name) {
        this.alias_name = alias_name;
    }

    public boolean isAlias_available() {
        return alias_available;
    }

    public void setAlias_available(boolean alias_available) {
        this.alias_available = alias_available;
    }

    public VerifyAliasModel(){}


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean toObject(String jsonObject){
        try{
            JSONObject json = new JSONObject(jsonObject);
            int temp = 0;
            try{temp = json.getInt(IS_EXISTING);}catch(Exception e){temp =0;}if(temp>0){alias_available=false;}else{alias_available=true;}temp=0;
            if(json.has(MESSAGE))message=json.getString(MESSAGE);
            return true;

        }catch(Exception ex){

            Log.d(TAG, "Json Exception : " + ex);}
        return false;
    }

    @Override
    public String toString(){
        String returnString = null;
        try{
            JSONObject jsonMain = new JSONObject();
            jsonMain.put(ALIAS_NAME, alias_name);
            returnString = jsonMain.toString();
        }catch (Exception ex){Log.d(TAG," To String Exception : "+ex);
        }
        return returnString;
    }
}
