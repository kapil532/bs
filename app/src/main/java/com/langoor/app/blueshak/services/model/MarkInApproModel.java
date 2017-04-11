package com.langoor.app.blueshak.services.model;

import android.util.Log;

import com.langoor.app.blueshak.AppController;

import org.json.JSONObject;

import java.io.Serializable;

public class MarkInApproModel implements Serializable {
    private final String TAG = "MarkInApproModel";
    private final String
                PRODUCT_ID = "product_id",
                CONTENT = "content",
                SALE_ID = "sale_id";
    private final String
            TYPE = "type";
    public String product_id;
    public String sale_id;
    public String content;

    public String getTAG() {
        return TAG;
    }

    public boolean is_item() {
        return is_item;
    }

    public void setIs_item(boolean is_item) {
        this.is_item = is_item;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSale_id() {
        return sale_id;
    }

    public void setSale_id(String sale_id) {
        this.sale_id = sale_id;
    }

    boolean is_item=false;

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
            if(is_item)
                jsonObject.put(PRODUCT_ID,product_id);
            else
                jsonObject.put(SALE_ID,sale_id);

            jsonObject.put(CONTENT,content);
            returnString = jsonObject.toString();
        }catch (Exception ex){Log.d(TAG," To String Exception : "+ex);
           }
        return returnString;
    }

}
