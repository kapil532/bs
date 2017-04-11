package com.langoor.app.blueshak.services.model;

import android.nfc.Tag;
import android.util.Log;

import com.langoor.app.blueshak.AppController;
import com.nostra13.universalimageloader.utils.L;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ConversationIdModel implements Serializable {
    private final String TAG = "ConversationIdModel";
    private final String
                PRODUCT_ID = "product_id";
    private final String SELLER_ID = "seller_id",
                        ACTIVE_TAB = "active_tab",
                        CONTACT_NAME = "seller_name",
                        CONTACT_AVATAR= "seller_avatar";
    boolean is_garage=false;

    public boolean is_garage() {
        return is_garage;
    }

    public void setIs_garage(boolean is_garage) {
        this.is_garage = is_garage;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    private final String CONVERSATION_ID= "conversation_id";

    String product_id = null,conversation_id=null,seller_id=null,active_tab=null,contact_name=null,contact_avatar=null;

    public String getProduct_id() {
        return product_id;
    }

    public String getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(String conversation_id) {
        this.conversation_id = conversation_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getActive_tab() {
        return active_tab;
    }

    public void setActive_tab(String active_tab) {
        this.active_tab = active_tab;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getContact_avatar() {
        return contact_avatar;
    }

    public void setContact_avatar(String contact_avatar) {
        this.contact_avatar = contact_avatar;
    }

    public boolean toObject(String jsonObjectString){
        try{
            Log.d(TAG,"#####jsonObjectString###########"+jsonObjectString);

            JSONObject json = new JSONObject(jsonObjectString);
            if(json.has(CONVERSATION_ID)) conversation_id = json.getString(CONVERSATION_ID);
            if(json.has(PRODUCT_ID)) product_id = json.getString(PRODUCT_ID);
            if(json.has(ACTIVE_TAB))  active_tab = json.getString(ACTIVE_TAB);
            if(json.has(CONTACT_NAME)) contact_name = json.getString(CONTACT_NAME);
            if(json.has(CONTACT_AVATAR)) contact_avatar = json.getString(CONTACT_AVATAR);
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
            JSONObject jsonObject=new JSONObject();
            if(is_garage)
                jsonObject.put(SELLER_ID,seller_id);
            else
                jsonObject.put(PRODUCT_ID,product_id);
            returnString = jsonObject.toString();
        }
        catch (Exception ex){Log.d(TAG," To String Exception : "+ex);
           }
        return returnString;
    }

}
