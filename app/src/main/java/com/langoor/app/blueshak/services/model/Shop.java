package com.langoor.app.blueshak.services.model;

import android.util.Log;
import com.langoor.app.blueshak.AppController;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.Serializable;

public class Shop implements Serializable {
    private final String TAG = "Shop";
    private final String
            SHOP_NAME = "name",
            SHOP_DESCRIPTION="description",
            SHOP_LINK="shop_link",
            LATITUDE = "latitude",
            LONGITUDE = "longitude",
            RESPONSE_TIME="response_time";

    String shop_name=null,
           shop_description=null,
           shop_link=null,
            latitude = "0",
            longitude="0",
            response_time=null;


    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public boolean toObject(String jsonObject){
        try{
            Log.d(TAG, "Shop Details : " + jsonObject);
            JSONObject json = new JSONObject(jsonObject);
            shop_name = json.getString(SHOP_NAME);
            shop_description = json.getString(SHOP_DESCRIPTION);
            shop_link = json.getString(SHOP_LINK);
            response_time = json.getString(RESPONSE_TIME);
            latitude = json.getString(LATITUDE);
            longitude = json.getString(LONGITUDE);
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
            jsonMain.put(SHOP_NAME, shop_name);
            jsonMain.put(SHOP_DESCRIPTION,shop_description);
            jsonMain.put(SHOP_LINK, shop_link);
            jsonMain.put(RESPONSE_TIME, response_time);
            returnString = jsonMain.toString();
            Log.d(TAG," To returnString################## : "+returnString);
        }
        catch (Exception ex){Log.d(TAG," To String Exception : "+ex);

        }
        return returnString;
    }


    public String getName() {
        return shop_name;
    }

    public void setName(String name) {
        this.shop_name = name;
    }

    public String getDescription() {
        return shop_description;
    }

    public void setDescription(String description) {
        this.shop_description = description;
    }

    public String getShop_link() {
        return shop_link;
    }

    public void setShop_link(String shop_link) {
        this.shop_link = shop_link;
    }

    public String getResponse_time() {
        return response_time;
    }

    public void setResponse_time(String response_time) {
        this.response_time = response_time;
    }
}
