package com.langoor.app.blueshak.services.model;

import android.util.Log;
import com.langoor.app.blueshak.AppController;
import com.langoor.app.blueshak.global.GlobalVariables;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.Serializable;

public class LocationModel implements Serializable {
    private final String TAG = "LocationModel";
    public static final String
            CITY="city",
            SUBHURB="subhurb",
            COUNTRY_CODE="country_code",
            STREET_NUMBER="street_number",
            AUTOCOMPLETE_ADDRESS="autocomplete_address",
            STATE="state",
            COUNTRY="country",
            POSTAL_CODE="postal_code",
            LATITUDE = "latitude",
            LONGITUDE = "longitude";
    private final String
            RESULTS = "results";

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSubhurb() {
        return subhurb;
    }

    public void setSubhurb(String subhurb) {
        this.subhurb = subhurb;
    }


    private final String
            ADDRESS_COMPONENTS = "address_components";
    private final String
            FORMATTED_ADDRESS = "formatted_address";

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    String results=null, formatted_address=null,address_components=null;

    public String getStree_number() {
        return stree_number;
    }

    public String getAutocomplete_address() {
        return autocomplete_address;
    }

    public void setAutocomplete_address(String autocomplete_address) {
        this.autocomplete_address = autocomplete_address;
    }

    public void setStree_number(String stree_number) {
        this.stree_number = stree_number;
    }

    String city=null;
    String subhurb=null;
    String state=null;String country=null,country_code=null,stree_number=null,autocomplete_address=null;
    String postal_code=null;

    String latitude =/* GlobalVariables.Sydney_latitude*/null;
    String longitude = /* GlobalVariables.Sydney_longitude*/null;
    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public String getAddress_components() {
        return address_components;
    }

    public void setAddress_components(String address_components) {
        this.address_components = address_components;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public boolean toObject(String jsonObjectString){
        try {
            JSONObject obj=new JSONObject(jsonObjectString);
            if(obj.has("results")){
                JSONArray arr=(JSONArray)obj.getJSONArray(RESULTS);
                for(int i=0;i<arr.length();i++){
                    JSONObject obj_=(JSONObject)arr.getJSONObject(0);
                    if(obj_.has("formatted_address")){
                        formatted_address=obj_.getString(FORMATTED_ADDRESS);
                    }
                    JSONArray address_components=(JSONArray)obj_.getJSONArray("address_components");
                    for(int j=0;j<address_components.length();j++){
                        JSONObject address_obj=(JSONObject)address_components.get(j);
                        JSONArray type_arr=(JSONArray)address_obj.getJSONArray("types");
                        if(type_arr.get(0).equals("locality")){
                            if(address_obj.has("long_name")){
                                city=address_obj.getString("long_name");
                            }
                        }
                        if(type_arr.get(0).equals("administrative_area_level_2")){
                            if(address_obj.has("long_name")){
                                subhurb=address_obj.getString("long_name");
                            }
                        }
                        if(type_arr.get(0).equals("administrative_area_level_1")){
                            if(address_obj.has("long_name")){
                                state=address_obj.getString("long_name");
                            }
                        }
                        if(type_arr.get(0).equals("country")){
                            if(address_obj.has("long_name")){
                                country=address_obj.getString("long_name");
                            }
                            if(address_obj.has("short_name")){
                                country_code=address_obj.getString("short_name");
                            }
                        }
                        if(type_arr.get(0).equals("postal_code")){
                            if(address_obj.has("long_name")){
                                postal_code=address_obj.getString("long_name");
                            }
                        }


                    }
                }
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();

        }
        return false;
    }

    @Override
    public String toString(){
        String returnString = null;
        try {
            JSONObject jsonMain = new JSONObject();
            jsonMain.put(LATITUDE, latitude);
            jsonMain.put(LONGITUDE, longitude);
            jsonMain.put(CITY, city);
            jsonMain.put(COUNTRY, country);
            jsonMain.put(COUNTRY_CODE, country_code);
            jsonMain.put(STATE, state);
            jsonMain.put(POSTAL_CODE, postal_code);
            jsonMain.put(FORMATTED_ADDRESS, formatted_address);
            jsonMain.put(AUTOCOMPLETE_ADDRESS, autocomplete_address);
            returnString = jsonMain.toString();
        }catch (Exception e){
            Log.d(TAG," To String Exception : "+e);
        }
        return returnString;

    }

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
 /* if(obj_.has(ADDRESS_COMPONENTS)){
                        JSONArray address_components=(JSONArray)obj_.getJSONArray(ADDRESS_COMPONENTS);
                        for(int j=0;i<address_components.length();j++){
                            JSONObject comp=(JSONObject)address_components.getJSONObject(j);
                            JSONArray types_arr=(JSONArray)comp.getJSONArray("types");
                            if(comp.has("types")){
                                if(types_arr.get(0).toString().equalsIgnoreCase("street_number")){
                                    String street_number=comp.getString("street_number");
                                    continue;
                                }
                                if(types_arr.getString(0).toString().equalsIgnoreCase("postal_code")){
                                    String administrative_area_level_1=comp.getString("administrative_area_level_1");
                                    continue;
                                }
                                if(types_arr.getString(0).toString().equalsIgnoreCase("locality")){
                                    String locality=comp.getString("locality");
                                     continue;
                                }
                                if(types_arr.getString(0).toString().equalsIgnoreCase("country")){
                                    String country=comp.getString("country");
                                    continue;
                                }
                                continue;
                            }
                        }
                    }*/
}
