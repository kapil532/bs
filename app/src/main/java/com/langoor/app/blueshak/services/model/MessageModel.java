package com.langoor.app.blueshak.services.model;

import android.util.Log;

import com.langoor.app.blueshak.AppController;
import com.langoor.app.blueshak.global.GlobalVariables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

public class MessageModel implements Serializable {

    private final String TAG = "MessageModel";
    private final String MESSAGE_TYPE = "message_type";
    private final String MESSAGE = "message";
    private final String IMAGE = "image";
    private final String MESSAGE_ID = "message_id";
    private final String SENDER = "sender";
    private final String CONVERSATION_ID= "conversation_id";
    private final String RECEIVER= "receiver";
    private final String OFFER_PRODUCT_ID = "offered_product_id";
    private final String PRODUCT_IMAGE="product_image";
    private final String PRODUCT_ID="product_id";
    private final String PRODUCT_NAME="product_name";
    private final String DATE="date";

    boolean local_message=false;

    public String getTAG() {
        return TAG;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getOffered_product_id() {
        return offered_product_id;
    }

    public void setOffered_product_id(String offered_product_id) {
        this.offered_product_id = offered_product_id;
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

    public String getContact_image() {
        return contact_image;
    }

    public void setContact_image(String contact_image) {
        this.contact_image = contact_image;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getProduct_details() {
        return product_details;
    }

    public void setProduct_details(String product_details) {
        this.product_details = product_details;
    }

    public boolean is_sent_by_you() {
        return is_sent_by_you;
    }

    public void setIs_sent_by_you(boolean is_sent_by_you) {
        this.is_sent_by_you = is_sent_by_you;
    }

    public ProductModel getProductModel() {
        return productModel;
    }

    public void setProductModel(ProductModel productModel) {
        this.productModel = productModel;
    }

    private final String CONTACT_NAME = "contact_name";
    private final String CONTACT_IMAGE = "contact_image";
    private final String CREATED_AT = "created_at";
    private final String IS_SEND_BY_YOU = "is_sent_by_you";
    private final String PRODUCT_DETAILS= "product_details";

    String message_type=null, message=null,image=null,message_id=null,sender=null,
            receiver=null,offered_product_id=null,product_name=null,
            contact_name=null,
            conversation_id=null,
            contact_image=null,
            created_at=null
            ,product_details=null,  product_image=null;
    boolean is_sent_by_you=false;

    ProductModel productModel=new ProductModel();

    public MessageModel(){}

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public boolean toObject(String jsonObject){
        try{
            JSONObject json = new JSONObject(jsonObject);
            if(json.has(MESSAGE_TYPE)) message_type = json.getString(MESSAGE_TYPE);
            if(json.has(MESSAGE)) message = json.getString(MESSAGE);
            if(json.has(MESSAGE_ID)) message_id = json.getString(MESSAGE_ID);
            if(json.has(IMAGE))image = json.getString(IMAGE);
            if(json.has(CONVERSATION_ID)) conversation_id = json.getString(CONVERSATION_ID);
            if(json.has(SENDER)) sender = json.getString(SENDER);
            if(json.has(RECEIVER))receiver = json.getString(RECEIVER);
            if(json.has(OFFER_PRODUCT_ID)) offered_product_id = json.getString(OFFER_PRODUCT_ID);
            if(json.has(CONTACT_NAME))contact_name = json.getString(CONTACT_NAME);
            if(json.has(CONTACT_IMAGE)) contact_image = json.getString(CONTACT_IMAGE);
            if(json.has(CREATED_AT))created_at = json.getString(CREATED_AT);
            if(isJSONValid(created_at)){
                JSONObject date_obj=json.getJSONObject(CREATED_AT);
                if(date_obj.has(DATE)) created_at = date_obj.getString(DATE);
            }else
                created_at = json.getString(CREATED_AT);


            int temp = 0;
            try{temp = json.getInt(IS_SEND_BY_YOU);}catch(Exception e){temp =0;}if(temp>0){is_sent_by_you=true;}else{is_sent_by_you=false;}temp=0;
            if(json.has(PRODUCT_DETAILS)){
                JSONObject jsonObject1=json.getJSONObject(PRODUCT_DETAILS);
                if(jsonObject1.has(PRODUCT_IMAGE)) product_image = jsonObject1.getString(PRODUCT_IMAGE);
                if(jsonObject1.has(PRODUCT_ID)) offered_product_id = jsonObject1.getString(PRODUCT_ID);
                if(jsonObject1.has(PRODUCT_NAME)) product_name = jsonObject1.getString(PRODUCT_NAME);
            }
            productModel.toObject(product_details);
            return true;
        }catch(Exception ex){
            Log.d(TAG, "Json Exception : " + ex);
           }
        return false;

    }

    public boolean isLocal_message() {
        return local_message;
    }

    public void setLocal_message(boolean local_message) {
        this.local_message = local_message;
    }

    public boolean toObject(CreateMessageModel createMessageModel){
        try{
            message_type=createMessageModel.getMessage_type();
            local_message=true;

            if(message_type.equalsIgnoreCase(GlobalVariables.TYPE_IMAGE))
                image=createMessageModel.getImage();
            else
                message=createMessageModel.getMessage();
            created_at=new Date().toString();
            conversation_id=createMessageModel.getConversation_id();
            receiver=createMessageModel.getSend_to();
            is_sent_by_you=true;
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
            jsonMain.put(MESSAGE_ID, message_id);
            jsonMain.put(IMAGE, image);
            jsonMain.put(CONVERSATION_ID, conversation_id);
            jsonMain.put(SENDER, sender);
            jsonMain.put(RECEIVER, receiver);
            jsonMain.put(OFFER_PRODUCT_ID, offered_product_id);
            jsonMain.put(CONTACT_NAME, contact_name);
            jsonMain.put(CONTACT_IMAGE, contact_image);
            jsonMain.put(CREATED_AT, created_at);
            jsonMain.put(IS_SEND_BY_YOU, is_sent_by_you);
            returnString = jsonMain.toString();
        }catch (Exception ex){Log.d(TAG," To String Exception : "+ex);

        }
        return returnString;
    }
    public boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
           /* // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }*/
            return false;
        }
        return true;
    }
}

