package com.langoor.app.blueshak.services.model;

import android.util.Log;
import com.langoor.app.blueshak.AppController;
import org.json.JSONObject;
import java.io.Serializable;

public class ContactModel implements Serializable {
    private final String TAG = "ContactModel";
    private final String
            CONVERSATION_ID= "conversation_id";
    private final String SELLER_ID= "seller_id";
    private final String BUYER_ID = "buyer_id";
    private final String CONTACT_NAME = "contact_name";

    public boolean is_read() {
        return is_read;
    }

    public void setIs_read(boolean is_read) {
        this.is_read = is_read;
    }

    private final String CONTACT_IMAGE = "contact_image";
    private final String CREATED_AT = "created_at";
    private final String MESSAGE_TYPE = "message_type";
    private final String MESSAGE= "message";
    private final String ACTIVE_PRODUCT_ID= "active_product_id";
    private final String IMAGE="image";
    private final String PRODUCT_IMAGE="product_image";
    private final String PRODUCT_DETAILS="product_details";
    private final String IS_READ="is_read";
    private final String MSG_DATE= "msg_date";
    boolean is_read=false;
    String message_type=null;
    String message=null;
    String image=null;
    String product_image=null;
    String seller_id=null;
    String buyer_id=null;
    String active_product_id=null;
    String contact_name=null;
    String conversation_id=null;
    String contact_image=null;
    String created_at=null;

    public String getMsg_date() {
        return msg_date;
    }

    public void setMsg_date(String msg_date) {
        this.msg_date = msg_date;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(String buyer_id) {
        this.buyer_id = buyer_id;
    }

    public String getActive_product_id() {
        return active_product_id;
    }

    public void setActive_product_id(String active_product_id) {
        this.active_product_id = active_product_id;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(String conversation_id) {
        this.conversation_id = conversation_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getContact_image() {
        return contact_image;
    }

    public void setContact_image(String contact_image) {
        this.contact_image = contact_image;
    }

    String msg_date=null;

    public boolean toObject(String jsonObject){
        try{
            JSONObject json = new JSONObject(jsonObject);
            if(json.has(MESSAGE_TYPE)) message_type = json.getString(MESSAGE_TYPE);
            if(json.has(MESSAGE)) message = json.getString(MESSAGE);
            if(json.has(IMAGE))image = json.getString(IMAGE);
            if(json.has(CONVERSATION_ID)) conversation_id = json.getString(CONVERSATION_ID);
            if(json.has(CONTACT_NAME))contact_name = json.getString(CONTACT_NAME);
            if(json.has(CONTACT_IMAGE)) contact_image = json.getString(CONTACT_IMAGE);
            if(json.has(CREATED_AT)) created_at = json.getString(CREATED_AT);
            if(json.has(MSG_DATE)) msg_date = json.getString(MSG_DATE);
            if(json.has(SELLER_ID)) seller_id = json.getString(SELLER_ID);
            if(json.has(BUYER_ID)) buyer_id = json.getString(BUYER_ID);
            if(json.has(ACTIVE_PRODUCT_ID)) active_product_id = json.getString(ACTIVE_PRODUCT_ID);
            if(json.has(PRODUCT_DETAILS)){
                JSONObject jsonObject1=json.getJSONObject(PRODUCT_DETAILS);
                if(jsonObject1.has(PRODUCT_IMAGE)) product_image = jsonObject1.getString(PRODUCT_IMAGE);
            }
            int temp = 0;
            try{temp = json.getInt(IS_READ);}catch(Exception e){temp =0;}if(temp>0){is_read=true;}else{is_read=false;}temp=0;

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
            jsonMain.put(MESSAGE_TYPE, message_type);
            jsonMain.put(MESSAGE, message);
            jsonMain.put(IMAGE, image);
            jsonMain.put(CONVERSATION_ID, conversation_id);
            jsonMain.put(CONTACT_NAME, contact_name);
            jsonMain.put(CONTACT_IMAGE, contact_image);
            jsonMain.put(CREATED_AT, created_at);
            jsonMain.put(MSG_DATE, msg_date);
            jsonMain.put(BUYER_ID, buyer_id);
            jsonMain.put(SELLER_ID, seller_id);

            returnString = jsonMain.toString();
        }catch (Exception ex){Log.d(TAG," To String Exception : "+ex);

        }
        return returnString;
    }
}

