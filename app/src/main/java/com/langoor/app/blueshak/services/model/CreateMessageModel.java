package com.langoor.app.blueshak.services.model;

import android.util.Log;
import com.langoor.app.blueshak.AppController;
import com.langoor.app.blueshak.global.GlobalVariables;
import org.json.JSONObject;
import java.io.Serializable;

public class CreateMessageModel implements Serializable {
    private final String TAG = "MessageModel";
    private final String
            MESSAGE_TYPE = "message_type",
            MESSAGE = "message",
            IMAGE = "base64_image",
            MESSAGE_ID = "message_id",
            PRODUCT_ID="product_id",
            SEND_TO = "send_to",
            CONVERSATION_ID= "conversation_id";

    String message_type=null, message=null,image=null,message_id=null,send_to=null,
            product_id=null, conversation_id=null;

    ProductModel productModel=new ProductModel();

    public CreateMessageModel(){}

    public boolean toObject(String jsonObject){
        try{
            JSONObject json = new JSONObject(jsonObject);
            if(json.has(MESSAGE_TYPE)) message_type = json.getString(MESSAGE_TYPE);
            if(json.has(MESSAGE)) message = json.getString(MESSAGE);
            if(json.has(MESSAGE_ID)) message_id = json.getString(MESSAGE_ID);
            if(json.has(IMAGE))image = json.getString(IMAGE);
            if(json.has(CONVERSATION_ID)) conversation_id = json.getString(CONVERSATION_ID);
            return true;
        }catch(Exception ex){
            Log.d(TAG, "Json Exception : " + ex);
           }
        return false;
    }


    public String getTAG() {
        return TAG;
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

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getSend_to() {
        return send_to;
    }

    public void setSend_to(String send_to) {
        this.send_to = send_to;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(String conversation_id) {
        this.conversation_id = conversation_id;
    }

    public ProductModel getProductModel() {
        return productModel;
    }

    public void setProductModel(ProductModel productModel) {
        this.productModel = productModel;
    }

    @Override
    public String toString(){

        String returnString = null;
        try{
            JSONObject jsonMain = new JSONObject();
            jsonMain.put(MESSAGE_TYPE, message_type);
            jsonMain.put(SEND_TO, send_to);
            jsonMain.put(CONVERSATION_ID, conversation_id);
            jsonMain.put(PRODUCT_ID,product_id);
            if(message_type.equalsIgnoreCase(GlobalVariables.TYPE_IMAGE))
                jsonMain.put(IMAGE, image);
            else
                jsonMain.put(MESSAGE, message);
            returnString = jsonMain.toString();
        }catch (Exception ex){Log.d(TAG," To String Exception : "+ex);

        }
        return returnString;
    }
}

