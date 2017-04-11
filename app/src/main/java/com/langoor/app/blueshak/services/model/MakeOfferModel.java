package com.langoor.app.blueshak.services.model;

import android.util.Log;
import com.langoor.app.blueshak.AppController;
import org.json.JSONObject;
import java.io.Serializable;

public class MakeOfferModel implements Serializable {

    private final String TAG = "MakeOfferModel";
    private final String
            PRODUCT_ID = "product_id",
            OFFERED_PRODUCT_ID = "offered_product_id",
            CONVERSATION_ID = "conversation_id",
            ACTIVE_TAB = "active_tab",
            CONTACT_NAME = "contact_name",
            CONTACT_AVATAR= "contact_avatar",
            OFFER = "offer";

    String product_id=null, offer=null,offered_product_id=null,conversation_id=null,active_tab=null,
            contact_name=null,contact_avatar=null;

    public MakeOfferModel(){}

    public MakeOfferModel(String product_id, String offer){
        this.product_id = product_id;
        this.offer = offer;
    }


    public String getPRODUCT_ID() {
        return PRODUCT_ID;
    }

    public String getOffered_product_id() {
        return offered_product_id;
    }

    public void setOffered_product_id(String offered_product_id) {
        this.offered_product_id = offered_product_id;
    }

    public String getActive_tab() {
        return active_tab;
    }

    public void setActive_tab(String active_tab) {
        this.active_tab = active_tab;
    }

    public String getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(String conversation_id) {
        this.conversation_id = conversation_id;
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

    public boolean toObject(String jsonObject){
        try{
            JSONObject json = new JSONObject(jsonObject);
            offered_product_id = json.getString(OFFERED_PRODUCT_ID);
            conversation_id = json.getString(CONVERSATION_ID);
            active_tab = json.getString(ACTIVE_TAB);
            contact_name = json.getString(CONTACT_NAME);
            contact_avatar = json.getString(CONTACT_AVATAR);
            return true;
        }catch(Exception ex){
            Log.d(TAG, "Json Exception : " + ex);
           }
        return false;
    }

    public String getTAG() {
        return TAG;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    @Override
    public String toString(){
        String returnString = null;
        try{
            JSONObject jsonMain = new JSONObject();
            jsonMain.put(PRODUCT_ID, product_id);
            jsonMain.put(OFFER, offer);
            returnString = jsonMain.toString();
        }

        catch (Exception ex){Log.d(TAG," To String Exception : "+ex);

        }
        return returnString;
    }
}

