package com.langoor.app.blueshak.services.model;

import android.util.Log;

import com.langoor.app.blueshak.AppController;

import org.json.JSONObject;

import java.io.Serializable;

public class MyItemListAvailableModel implements Serializable{
    private final String TAG = "MyItemAvailableModel";
    private final String
            ITEMS_COUNT = "items_count";

    boolean item_available=false;

    public MyItemListAvailableModel(){}


    public boolean isItem_available() {
        return item_available;
    }

    public void setItem_available(boolean item_available) {
        this.item_available = item_available;
    }

    public boolean toObject(String jsonObject){
        try{
            JSONObject json = new JSONObject(jsonObject);
            int temp = 0;
            try{temp = json.getInt(ITEMS_COUNT);}catch(Exception e){temp =0;}if(temp>0){item_available=true;}else{item_available=false;}temp=0;
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
            if(ITEMS_COUNT!=null)jsonMain.put(ITEMS_COUNT, item_available);
            returnString = jsonMain.toString();
        }
        catch (Exception ex){Log.d(TAG," To String Exception : "+ex);
           }
        return returnString;
    }
}
