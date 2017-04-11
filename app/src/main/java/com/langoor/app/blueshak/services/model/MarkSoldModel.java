package com.langoor.app.blueshak.services.model;

import android.util.Log;

import com.langoor.app.blueshak.AppController;

import org.json.JSONObject;

import java.io.Serializable;

public class MarkSoldModel implements Serializable {
    private final String TAG = "MarkSoldModel";
    private final String
                PRODUCT_ID = "product_id";
    private final String
            TYPE = "type";
    public String product_id;

    public boolean toObject(String jsonObjectString){
        try{
            Log.d(TAG,"#####jsonObjectString###########"+jsonObjectString);
            JSONObject json = new JSONObject(jsonObjectString);
            return true;
        }catch(Exception ex){
            Log.d(TAG, "Json Exception : " + ex);
           }
        return false;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    @Override
    public String toString(){
        String returnString = null;
        try{
            JSONObject jsonObject=new JSONObject();
            jsonObject.put(TYPE,"sold");
            jsonObject.put(PRODUCT_ID,product_id);
            returnString = jsonObject.toString();
        }catch (Exception ex){Log.d(TAG," To String Exception : "+ex);
           }
        return returnString;
    }

}
