package com.langoor.app.blueshak.services.model;

import android.util.Log;
import com.langoor.app.blueshak.AppController;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ContactsListModel implements Serializable {
    public List<ContactModel> getConversations_contacts() {
        return conversations_contacts;
    }

    public void setConversations_contacts(List<ContactModel> conversations_contacts) {
        this.conversations_contacts = conversations_contacts;
    }

    private final String TAG = "ContactsListModel";
    private final String CONVERSATION_CONTACTS = "conversations_contacts";
    private List<ContactModel> conversations_contacts = new ArrayList<ContactModel>();

    public boolean toObject(String jsonObjectString){
        try{
            JSONObject json = new JSONObject(jsonObjectString);
            List<ContactModel> productModelArrayList = new ArrayList<ContactModel>();
            JSONArray jsonArray = json.getJSONArray(CONVERSATION_CONTACTS);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ContactModel contactModel = new ContactModel();
                contactModel.toObject(jsonObject.toString());
                productModelArrayList.add(contactModel);
            }
            conversations_contacts.addAll(productModelArrayList);
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
            List<ContactModel> productModelList = conversations_contacts;
            for(int i=0;i<productModelList.size();i++){
                jsonArray.put(new JSONObject(productModelList.get(i).toString()));
            }
            jsonMain.put(CONVERSATION_CONTACTS,jsonArray);
            returnString = jsonMain.toString();
        }catch (Exception ex){Log.d(TAG," To String Exception : "+ex);

        }
        return returnString;
    }

}
