package com.langoor.app.blueshak.services.model;

import android.util.Log;

import com.langoor.app.blueshak.AppController;

import org.json.JSONObject;

public class ErrorModel {
    private final String TAG = "ErrorModel";
    private final String
            ERROR = "error",
            MESSAGE = "message";

    String error=null, message = null;

    public ErrorModel(){}

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean toObject(String jsonObject){
        try{
            JSONObject json = new JSONObject(jsonObject);
            if(json.has(ERROR))error = json.getString(ERROR);else error = null;
            if(json.has(MESSAGE))message = json.getString(MESSAGE);else message = null;
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
            if(error!=null)jsonMain.put(ERROR, error);
            if(message!=null)jsonMain.put(MESSAGE, message);
            returnString = jsonMain.toString();
        }
        catch (Exception ex){Log.d(TAG," To String Exception : "+ex);
           }
        return returnString;

    }
}
