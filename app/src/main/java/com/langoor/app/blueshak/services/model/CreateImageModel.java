package com.langoor.app.blueshak.services.model;

import android.util.Log;

import com.langoor.app.blueshak.AppController;

import org.json.JSONObject;

import java.io.Serializable;

public class CreateImageModel implements Serializable{

    private final String TAG = "CreateImageModel";
    private final String
            IMAGE = "base64_image",
            DISPLAY_FLAG = "is_display_image";

    String image=null;
    boolean display=false;

    public boolean is_new_image() {
        if(id==0)
            return true;
        else
            return false;
    }
    public void setIs_new_image(boolean is_new_image) {
        this.is_new_image = is_new_image;
    }

    boolean is_new_image=false;
    int id=0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CreateImageModel(){}

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

    public boolean toObject(String jsonObject){
        try{
            JSONObject json = new JSONObject(jsonObject);
            image = json.getString(IMAGE);
            display = json.getBoolean(DISPLAY_FLAG);
            return true;
        }catch(Exception ex){Log.d(TAG, "Json Exception : " + ex);
           }
        return false;
    }

    @Override
    public String toString(){
        String returnString = null;
        try{
            JSONObject jsonMain = new JSONObject();
            jsonMain.put(IMAGE, image);
            jsonMain.put(DISPLAY_FLAG, display);
            jsonMain.put("id", id);
            returnString = jsonMain.toString();
        }
        catch (Exception ex){Log.d(TAG," To String Exception : "+ex);

        }
        return returnString;
    }
}
