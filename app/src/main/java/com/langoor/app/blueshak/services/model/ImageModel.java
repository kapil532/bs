package com.langoor.app.blueshak.services.model;

import android.util.Log;

import com.langoor.app.blueshak.AppController;

import org.json.JSONObject;

import java.io.Serializable;

public class ImageModel implements Serializable {
    private final String TAG = "ImageModel";
    private String image=null;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean toObject(String jsonObjectString){
        try {
            JSONObject obj=new JSONObject(jsonObjectString);

            return true;
        }catch (Exception e){
            e.printStackTrace();

        }
        return false;
    }

    @Override
    public String toString(){
        String returnString = null;
        try{


        }
        catch (Exception ex){

            Log.d(TAG," To String Exception : "+ex);}
        return returnString;
    }

}
