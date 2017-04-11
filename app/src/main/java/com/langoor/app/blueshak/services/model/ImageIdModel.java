package com.langoor.app.blueshak.services.model;

import android.util.Log;

import com.langoor.app.blueshak.AppController;

import org.json.JSONObject;

import java.io.Serializable;

public class ImageIdModel implements Serializable {
    private final String TAG = "ImageModel";
    private final String ID = "id";
    private final String LINK = "link";
    private String image=null;
    private int id=0;

    public String getTAG() {
        return TAG;
    }

    public String getImage() {

        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean toObject(String jsonObjectString){
        try {
            JSONObject obj=new JSONObject(jsonObjectString);
            if(obj.has(ID))id=obj.getInt(ID);
            if(obj.has(LINK))image=obj.getString(LINK);
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
