package com.blueshak.app.blueshak.services.model;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MessageConversationModel implements Serializable {
    private final String TAG = "MessageonModel";

    public List<MessageModel> getConversations_messages() {
        return conversations_messages;
    }

    public void setConversations_messages(List<MessageModel> conversations_messages) {
        this.conversations_messages = conversations_messages;
    }

    private final String
                CONVERSATION_MESSAGES = "conversations_messages";

    private List<MessageModel> conversations_messages = new ArrayList<MessageModel>();

    public boolean toObject(Context context,String jsonObjectString){
        try{
            JSONObject json = new JSONObject(jsonObjectString);
            List<MessageModel> productModelArrayList = new ArrayList<MessageModel>();
            MessageModel message = new MessageModel();
            message.toObject(context,jsonObjectString);
            //conversations_messages.add(message);
            JSONArray jsonArray = json.getJSONArray(CONVERSATION_MESSAGES);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                message = new MessageModel();
                message.toObject(context,jsonObject.toString());
                productModelArrayList.add(message);
            }
            conversations_messages.addAll(productModelArrayList);
            productModelArrayList.clear();
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
            JSONArray jsonArray = new JSONArray();
            List<MessageModel> productModelList = conversations_messages;
            for(int i=0;i<productModelList.size();i++){
                jsonArray.put(new JSONObject(productModelList.get(i).toString()));
            }
            jsonMain.put(CONVERSATION_MESSAGES,jsonArray);
            returnString = jsonMain.toString();
        }
        catch (Exception ex){Log.d(TAG," To String Exception : "+ex);
           }
        return returnString;
    }

}
