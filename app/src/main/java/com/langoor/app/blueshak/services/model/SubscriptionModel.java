package com.langoor.app.blueshak.services.model;

import android.util.Log;

import com.langoor.app.blueshak.AppController;

import org.json.JSONObject;

public class SubscriptionModel {
    private final String TAG = "SubscriptionModel";
    private final String
            NAME = "name",
            PRICE= "price",
            DESCRIPTION= "description",
            WEEKS = "no_weeks",
            ITEMS = "no_items",
            ID = "subscriptions_id";

    String id=null, name = null, price = null, description = null;
    int weeks = 0, items = 0;

    public SubscriptionModel(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getWeeks() {
        return weeks;
    }

    public void setWeeks(int weeks) {
        this.weeks = weeks;
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
    }

    public boolean toObject(String jsonObject){
        try{
            JSONObject json = new JSONObject(jsonObject);
            id = json.getString(ID);
            name = json.getString(NAME);
            price = json.getString(PRICE);
            description = json.getString(DESCRIPTION);
            weeks = json.getInt(WEEKS);
            items = json.getInt(ITEMS);
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
            jsonMain.put(ID, id);
            jsonMain.put(NAME, name);
            jsonMain.put(PRICE, price);
            jsonMain.put(DESCRIPTION, description);
            jsonMain.put(WEEKS, weeks);
            jsonMain.put(ITEMS, items);
            returnString = jsonMain.toString();
        }
        catch (Exception ex){Log.d(TAG," To String Exception : "+ex);
           }
        return returnString;
    }
}
