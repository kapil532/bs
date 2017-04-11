package com.langoor.app.blueshak.services.model;

import android.util.Log;

import com.langoor.app.blueshak.AppController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionListModel {
    private final String TAG = "SubscriptionListModel";
    private final String
            SUBSCRIPTION_ACTIVE = "active_subscription",
            SUBSCRIPTION_LIST = "subscription_list";

    private SubscriptionActiveModel subscriptionActiveModel = new SubscriptionActiveModel();
    List<SubscriptionModel> subscriptionList = new ArrayList<SubscriptionModel>();

    public SubscriptionListModel(){}

    public SubscriptionActiveModel getSubscriptionActiveModel() {
        return subscriptionActiveModel;
    }

    public void setSubscriptionActiveModel(SubscriptionActiveModel subscriptionActiveModel) {
        this.subscriptionActiveModel = subscriptionActiveModel;
    }

    public List<SubscriptionModel> getSubscriptionList() {
        return subscriptionList;
    }

    public void setSubscriptionList(List<SubscriptionModel> subscriptionList) {
        this.subscriptionList = subscriptionList;
    }

    public boolean toObject(String jsonObjectString){
        try{
            JSONObject json = new JSONObject(jsonObjectString);
            List<SubscriptionModel> subscriptionModels = new ArrayList<SubscriptionModel>();

            try{
                JSONObject obj = json.getJSONObject(SUBSCRIPTION_ACTIVE);
                SubscriptionActiveModel tempModel = new SubscriptionActiveModel();
                tempModel.toObject(obj.toString());
                subscriptionActiveModel = tempModel;
            }catch(Exception jsonEx){
                subscriptionActiveModel = new SubscriptionActiveModel();
            }
            JSONArray subscriptionListArray = json.getJSONArray(SUBSCRIPTION_LIST);
            for(int i=0;i<subscriptionListArray.length();i++){
                JSONObject jsonObject = subscriptionListArray.getJSONObject(i);
                SubscriptionModel subscriptionModel = new SubscriptionModel();
                subscriptionModel.toObject(jsonObject.toString());
                subscriptionModels.add(subscriptionModel);
            }
            subscriptionList.addAll(subscriptionModels); subscriptionModels.clear();
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
            List<SubscriptionModel> subscriptionModels = subscriptionList;
            for(int i=0;i<subscriptionModels.size();i++){
                jsonArray.put(subscriptionModels.get(i).toString());
            }
            jsonMain.put(SUBSCRIPTION_ACTIVE,subscriptionActiveModel.toString());
            jsonMain.put(SUBSCRIPTION_LIST,jsonArray);
            returnString = jsonMain.toString();
        }
        catch (Exception ex){Log.d(TAG," To String Exception : "+ex);
           }
        return returnString;
    }
}
