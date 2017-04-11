package com.langoor.app.blueshak.services.model;

import android.util.Log;
import com.langoor.app.blueshak.AppController;
import com.langoor.app.blueshak.global.GlobalVariables;
import org.json.JSONObject;
import java.io.Serializable;

public class FilterModel implements Serializable {
    private final String TAG = "FilterModel";
    public static final String
            LATITUDE = "latitude",
            LONGITUDE = "longitude",
            RANGE = "range",
            PRICE_RANGE = "price_range",
           /* ZIPCODE = "zip_code",*/
            TYPE = "type",
            PAGE="page",
            IS_SHIPABLE = "is_shippable",
            IS_PICKUP = "is_pickupable",
            IS_AVAILABLE = "is_available",
            LOCATION="location",
            DISTANCE_ENABLED="distance_enabled",
                   SORTING_ENABLED="sorting_enabled",
            FORMATTED_ADDRESS="formatted_address",
            SORT_BY_RECENT="sortByRecent",
                   SORT_BY_RECENT_GARAGE="sortByRecentGarage",
                   GARAGE_ITEMS="garage_items",
                   ENDING_SOON="ending_soon",
                   IS_CURRENT_COUNTRY="is_current_country",
                   PRICE_HIGH_TO_LOW="price_h_2_l",
                   PRICE_LOW_TO_HIGH="price_l_2_h",
                   CURRENT_COUNTRY_CODE="current_country_code",
            CATEGORIES = "categories";

    String latitude =/* GlobalVariables.Sydney_latitude*/null;
    String longitude = /* GlobalVariables.Sydney_longitude*/null;

    public String getResults_text() {
        return results_text;
    }

    public void setResults_text(String results_text) {
        this.results_text = results_text;
    }

    String priceRange = GlobalVariables.PRICE_MIN_VALUE+","+GlobalVariables.PRICE_MAX_VALUE;
    String zipcode = null;
    String type = null;
    String current_country_code = null,results_text=null;

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    String formatted_address=null;
    String categories = null;
    String category_names= null;
    String location=null;

    public String getCategory_names() {
        return category_names;
    }

    public void setCategory_names(String category_names) {
        this.category_names = category_names;
    }

    int page=1;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    int range =0;

    public boolean isDistance_enabled() {
        return distance_enabled;
    }

    public void setDistance_enabled(boolean distance_enabled) {
        this.distance_enabled = distance_enabled;
    }

    boolean

            shipable        = false,
            pickup          = false,
            distance_enabled        = false,
            sortByRecent=false,
            sortByRecent_garage=false,
            available       = false,
            ending_soon=false,
            is_current_country=false,
            sorting_enabled=false,
            price_h_2_l=false,
            price_l_2_h=false,
            garage_items=false;

    public boolean isPrice_l_2_h() {
        return price_l_2_h;
    }

    public void setPrice_l_2_h(boolean price_l_2_h) {
        this.price_l_2_h = price_l_2_h;
    }

    public boolean isPrice_h_2_l() {
        return price_h_2_l;
    }

    public void setPrice_h_2_l(boolean price_h_2_l) {
        this.price_h_2_l = price_h_2_l;
    }

    public FilterModel(){}

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

    public boolean isGarage_items() {
        return garage_items;
    }

    public void setGarage_items(boolean garage_items) {
        this.garage_items = garage_items;
    }

    public boolean toObject(String jsonObject){
        try{
            JSONObject json = new JSONObject(jsonObject);
            if(json.has(LATITUDE))latitude = json.getString(LATITUDE);
            if(json.has(LONGITUDE))longitude = json.getString(LONGITUDE);

           /* if(json.has(ZIPCODE))zipcode = json.getString(ZIPCODE);*/
            if(json.has(TYPE))type = json.getString(TYPE);
            if(json.has(RANGE))range = json.getInt(RANGE);
            if(json.has(FORMATTED_ADDRESS))formatted_address = json.getString(FORMATTED_ADDRESS);
            if(json.has(PRICE_RANGE))priceRange = json.getString(PRICE_RANGE);
            if(json.has(CATEGORIES))categories = json.getString(CATEGORIES);
            if(json.has(CURRENT_COUNTRY_CODE))current_country_code = json.getString(CURRENT_COUNTRY_CODE);
            int temp = 0;
            try{temp = json.getInt(SORT_BY_RECENT_GARAGE);}catch(Exception e){temp =0;}if(temp>0){sortByRecent_garage=true;}else{sortByRecent_garage=false;}temp=0;
            try{temp = json.getInt(SORT_BY_RECENT);}catch(Exception e){temp =0;}if(temp>0){sortByRecent=true;}else{sortByRecent=false;}temp=0;
            try{temp = json.getInt(IS_SHIPABLE);}catch(Exception e){temp =0;}if(temp>0){shipable=true;}else{shipable=false;}temp=0;
            try{temp = json.getInt(IS_PICKUP);}catch(Exception e){temp =0;}if(temp>0){pickup=true;}else{pickup=false;}temp=0;
            try{temp = json.getInt(IS_AVAILABLE);}catch(Exception e){temp =0;}if(temp>0){available=true;}else{available=false;}temp=0;
            try{temp = json.getInt(DISTANCE_ENABLED);}catch(Exception e){temp =0;}if(temp>0){distance_enabled=true;}else{distance_enabled=false;}temp=0;
            try{temp = json.getInt(ENDING_SOON);}catch(Exception e){temp =0;}if(temp>0){ending_soon=true;}else{ending_soon=false;}temp=0;
            try{temp = json.getInt(IS_CURRENT_COUNTRY);}catch(Exception e){temp =0;}if(temp>0){is_current_country=true;}else{is_current_country=false;}temp=0;
            try{temp = json.getInt(SORTING_ENABLED);}catch(Exception e){temp =0;}if(temp>0){sorting_enabled=true;}else{sorting_enabled=false;}temp=0;
            try{temp = json.getInt(GARAGE_ITEMS);}catch(Exception e){temp =0;}if(temp>0){garage_items=true;}else{garage_items=false;}temp=0;
            try{temp = json.getInt(PRICE_HIGH_TO_LOW);}catch(Exception e){temp =0;}if(temp>0){price_h_2_l=true;}else{price_h_2_l=false;}temp=0;
            try{temp = json.getInt(PRICE_LOW_TO_HIGH);}catch(Exception e){temp =0;}if(temp>0){price_l_2_h=true;}else{price_l_2_h=false;}temp=0;

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

    public boolean isSortByRecent() {
        return sortByRecent;
    }

    public void setSortByRecent(boolean sortByRecent) {
        this.sortByRecent = sortByRecent;
    }

    public boolean isSorting_enabled() {
        return sorting_enabled;
    }

    public void setSorting_enabled(boolean sorting_enabled) {
        this.sorting_enabled = sorting_enabled;
    }

    public boolean isSortByRecent_garage() {
        return sortByRecent_garage;
    }

    public void setSortByRecent_garage(boolean sortByRecent_garage) {
        this.sortByRecent_garage = sortByRecent_garage;
    }

    @Override
    public String toString(){
        String returnString = null;
        try{
            JSONObject jsonMain = new JSONObject();
            jsonMain.put(TYPE, type);
            jsonMain.put(RANGE, range);
            jsonMain.put(PRICE_RANGE, priceRange);
            /*jsonMain.put(ZIPCODE, zipcode);*/
            jsonMain.put(LATITUDE, latitude);
            jsonMain.put(LONGITUDE, longitude);
            jsonMain.put(CATEGORIES, categories);

            jsonMain.put(FORMATTED_ADDRESS, formatted_address);
            jsonMain.put(PAGE, page);
            jsonMain.put(CURRENT_COUNTRY_CODE, current_country_code);
            String temp = "0";
            if(sortByRecent_garage){temp = "1";}else{temp="0";}jsonMain.put(SORT_BY_RECENT_GARAGE, temp);temp = "0";
            if(sortByRecent){temp = "1";}else{temp="0";}jsonMain.put(SORT_BY_RECENT, temp);temp = "0";
            if(distance_enabled){temp = "1";}else{temp="0";}jsonMain.put(DISTANCE_ENABLED, temp);temp = "0";
            if(shipable){temp = "1";}else{temp="0";}jsonMain.put(IS_SHIPABLE, temp);temp = "0";
            if(pickup){temp = "1";}else{temp="0";}jsonMain.put(IS_PICKUP, temp);temp = "0";
            if(available){temp = "1";}else{temp="0";}jsonMain.put(IS_AVAILABLE, temp);temp = "0";
            if(ending_soon){temp = "1";}else{temp="0";}jsonMain.put(ENDING_SOON, temp);temp = "0";
            if(is_current_country){temp = "1";}else{temp="0";}jsonMain.put(IS_CURRENT_COUNTRY, temp);temp = "0";
            if(sorting_enabled){temp = "1";}else{temp="0";}jsonMain.put(SORTING_ENABLED, temp);temp = "0";
            if(garage_items){temp = "1";}else{temp="0";}jsonMain.put(GARAGE_ITEMS, temp);temp = "0";
            if(price_h_2_l){temp = "1";}else{temp="0";}jsonMain.put(PRICE_HIGH_TO_LOW, temp);temp = "0";
            if(price_l_2_h){temp = "1";}else{temp="0";}jsonMain.put(PRICE_LOW_TO_HIGH, temp);temp = "0";
            returnString = jsonMain.toString();
        }
        catch (Exception ex){Log.d(TAG," To String Exception : "+ex);
           }
        return returnString;
    }
}
