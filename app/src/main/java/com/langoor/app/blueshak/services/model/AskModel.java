package com.langoor.app.blueshak.services.model;

import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.langoor.app.blueshak.AppController;

import org.json.JSONObject;

import java.io.Serializable;

public class AskModel implements Serializable {
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private final String TAG = "AskModel";
    private final String
            ASH_PHONE = "ask_phone";

    private final String
            EMAIL = "email";
    String email=null;

    boolean is_phone_alias_needed=false;

    public boolean is_phone_alias_needed() {
        return is_phone_alias_needed;
    }

    public void setIs_phone_alias_needed(boolean is_phone_alias_needed) {
        this.is_phone_alias_needed = is_phone_alias_needed;
    }

    public boolean toObject(String jsonObjectString){
        try {
            JSONObject obj=new JSONObject(jsonObjectString);
            int temp = 0;
            try{temp = obj.getInt(ASH_PHONE);}catch(Exception e){temp =0;}if(temp>0){is_phone_alias_needed=true;}else{is_phone_alias_needed=false;}temp=0;
            return true;
        }catch (Exception e){
            e.printStackTrace();

            Crashlytics.log(e.getMessage());
        }
        return false;
    }

    @Override
    public String toString(){
        String returnString = null;
        try{
            JSONObject jsonMain = new JSONObject();
            jsonMain.put(EMAIL, email);
            returnString = jsonMain.toString();
        } catch (Exception ex){
            Log.d(TAG," To String Exception : "+ex);}
        return returnString;
    }

}
