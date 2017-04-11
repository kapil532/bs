package com.langoor.app.blueshak.services.model;

import android.util.Log;
import com.langoor.app.blueshak.AppController;
import com.langoor.app.blueshak.global.GlobalVariables;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchModel implements Serializable {
    private final String TAG = "SearchModel";
    public static final String
            LATITUDE = "latitude",
            LONGITUDE = "longitude",
            RANGE = "range",
            PRICE_RANGE = "price_range",
            ZIPCODE = "zip_code",
            TYPE = "type",
            SEARCH="search",
            PAGE="page",
            SORT_BY_RECENT="sortByRecent",
            GARAGE_ITEMS="garage_items",
            ENDING_SOON="ending_soon",
            IS_CURRENT_COUNTRY="is_current_country",
            PRICE_HIGH_TO_LOW="price_h_2_l",
            PRICE_LOW_TO_HIGH="price_l_2_h",
            CURRENT_COUNTRY_CODE="current_country_code",
            CATEGORIES = "categories";

    String
            latitude = null,current_country_code=null;
    String longitude =null;
    String priceRange = "0";
    String zipcode = null;
    String type = null;

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    String search= null;

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    String formatted_address=null;
    String categories = null;
    String location=null;

    int page=1;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    int range = 5;

    boolean
            shipable        = false;
    boolean pickup          = false;
    boolean available       = false;
    boolean ending_soon=false;
    boolean is_current_country=false;

    public boolean isSortByRecent() {
        return sortByRecent;
    }

    public void setSortByRecent(boolean sortByRecent) {
        this.sortByRecent = sortByRecent;
    }

    boolean sorting_enabled=false;
    boolean price_h_2_l=false;
    boolean price_l_2_h=false;
    boolean sortByRecent=false;
    boolean garage_items=false;

    public SearchModel(){}

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

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public boolean isShipable() {
        return shipable;
    }

    public void setShipable(boolean shipable) {
        this.shipable = shipable;
    }

    public boolean isPickup() {
        return pickup;
    }

    public void setPickup(boolean pickup) {
        this.pickup = pickup;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean toObject(String jsonObject){
        try{
            JSONObject json = new JSONObject(jsonObject);
            if(json.has(LATITUDE))latitude = json.getString(LATITUDE);
            if(json.has(LONGITUDE))longitude = json.getString(LONGITUDE);
            if(json.has(ZIPCODE))zipcode = json.getString(ZIPCODE);
            if(json.has(TYPE))type = json.getString(TYPE);
            if(json.has(RANGE))range = json.getInt(RANGE);
            if(json.has(PRICE_RANGE))priceRange = json.getString(PRICE_RANGE);
            if(json.has(CATEGORIES))categories = json.getString(CATEGORIES);
            int temp = 0;
          return true;
        }catch(Exception ex){

            Log.d(TAG, "Json Exception : " + ex);}
        return false;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTAG() {
        return TAG;
    }

    public String getCurrent_country_code() {
        return current_country_code;
    }

    public void setCurrent_country_code(String current_country_code) {
        this.current_country_code = current_country_code;
    }

    public boolean isEnding_soon() {
        return ending_soon;
    }

    public void setEnding_soon(boolean ending_soon) {
        this.ending_soon = ending_soon;
    }

    public boolean is_current_country() {
        return is_current_country;
    }

    public void setIs_current_country(boolean is_current_country) {
        this.is_current_country = is_current_country;
    }

    public boolean isSorting_enabled() {
        return sorting_enabled;
    }


    public boolean isPrice_h_2_l() {
        return price_h_2_l;
    }

    public void setPrice_h_2_l(boolean price_h_2_l) {
        this.price_h_2_l = price_h_2_l;
    }

    public boolean isPrice_l_2_h() {
        return price_l_2_h;
    }

    public void setPrice_l_2_h(boolean price_l_2_h) {
        this.price_l_2_h = price_l_2_h;
    }

    public boolean isGarage_items() {
        return garage_items;
    }

    public void setGarage_items(boolean garage_items) {
        this.garage_items = garage_items;
    }

    @Override
    public String toString(){
        String returnString = null;
        try{
            JSONObject jsonMain = new JSONObject();
            jsonMain.put(TYPE, type);
            jsonMain.put(RANGE, range);
            jsonMain.put(SEARCH, search);
            jsonMain.put(PRICE_RANGE, priceRange);
            jsonMain.put(ZIPCODE, zipcode);
            jsonMain.put(LATITUDE, latitude);
            jsonMain.put(LONGITUDE, longitude);
            jsonMain.put(CATEGORIES, categories);
            jsonMain.put(PAGE, page);

            String temp = "0";
            if(ending_soon){temp = "1";jsonMain.put(ENDING_SOON, temp);}else{temp="0";}temp = "0";
            if(sortByRecent){temp = "1";jsonMain.put(SORT_BY_RECENT, temp);}else{temp="0";}temp = "0";
            if(is_current_country){temp = "1";jsonMain.put(IS_CURRENT_COUNTRY, temp);}else{temp="0";}temp = "0";
            if(is_current_country)jsonMain.put(CURRENT_COUNTRY_CODE,current_country_code);
            if(garage_items){temp = "1";jsonMain.put(GARAGE_ITEMS, temp);}else{temp="0";}temp = "0";
            if(price_h_2_l){temp = "1";jsonMain.put(PRICE_HIGH_TO_LOW, temp);}else{temp="0";}temp = "0";
            if(price_l_2_h){temp = "1";jsonMain.put(PRICE_LOW_TO_HIGH, temp);}else{temp="0";}temp = "0";

            returnString = jsonMain.toString();
        }catch (Exception ex){Log.d(TAG," To String Exception : "+ex);
           }
        return returnString;
    }
}
