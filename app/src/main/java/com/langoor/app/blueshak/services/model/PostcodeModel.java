package com.langoor.app.blueshak.services.model;

import android.util.Log;

import com.langoor.app.blueshak.AppController;

import org.json.JSONObject;

public class PostcodeModel {
    private final String TAG = "PostcodeModel";
    private final String
            POSTCODE = "postcode",
            ID = "id",
            SUBURB = "suburb";

    String postcode=null, id=null, suburb=null;
    boolean display = false;
    public PostcodeModel(){}

    public PostcodeModel(String postcode, String id, String suburb){
        this.postcode = postcode;
        this.id = id;
        this.suburb = suburb;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }
    public boolean toObject(String jsonObject){
        try{
            JSONObject json = new JSONObject(jsonObject);
            postcode = json.getString(POSTCODE);
            id = json.getString(ID);
            suburb = json.getString(SUBURB);
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
            jsonMain.put(SUBURB, suburb);
            jsonMain.put(POSTCODE, postcode);
            jsonMain.put(ID, id);
            String b = jsonMain.toString().substring(1, jsonMain.toString().length() - 1);
            returnString = b.toString();
        }
        catch (Exception ex){Log.d(TAG," To String Exception : "+ex);
           }
        return returnString;
    }
}
