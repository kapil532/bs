package com.langoor.app.blueshak.services.model;

import android.util.Log;

import com.langoor.app.blueshak.AppController;

import org.json.JSONObject;

public class RegisterModel {

    private final String TAG = "RegisterModel";
    private final String
            EMAIL = "email",
            ALIAS_NAME = "alias_name",
            LAST_NAME = "last_name",
            COUNTRY = "country",
            PASSWORD = "password",
            NAME = "name",
            PHONE = "phone",
            ISD="isd",
            CITY = "city",
            STATE = "state",
            POSTAL_CODE = "postal_code",
            LATITUDE = "latitude",
            LONGITUDE = "longitude",
            ADDRESS = "address";

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    String country=null,last_name=null,email=null, password = null,alias_name = null, name = null, city = null, state = null, postalCode = null, address = "", phone = "",isd="";
    String latitude = null;
    String longitude=null;
    public RegisterModel(){}

    public String getIsd() {
        return isd;
    }

    public void setIsd(String isd) {
        this.isd = isd;
    }

    public RegisterModel (String email, String password, String name, String phone, String address, String isd){
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.isd=isd;

    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean toObject(String jsonObject){
        try{
            JSONObject json = new JSONObject(jsonObject);
            password = json.getString(PASSWORD);
            name = json.getString(NAME);
            email = json.getString(EMAIL);
            phone = json.getString(PHONE);
            address = json.getString(ADDRESS);
            postalCode = json.getString(POSTAL_CODE);
            city = json.getString(CITY);
            state = json.getString(STATE);
            return true;
        }catch(Exception ex){

            Log.d(TAG, "Json Exception : " + ex);}
        return false;
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

    public String getAlias_name() {
        return alias_name;
    }

    public void setAlias_name(String alias_name) {
        this.alias_name = alias_name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString(){
        String returnString = null;
        try{
            JSONObject jsonMain = new JSONObject();
            jsonMain.put(PASSWORD, password);
            jsonMain.put(NAME, name);
            jsonMain.put(EMAIL, email);
            jsonMain.put(PHONE, phone);
            jsonMain.put(CITY, city);
            jsonMain.put(ISD, isd);
            jsonMain.put(STATE, state);
            jsonMain.put(POSTAL_CODE, postalCode);
            jsonMain.put(ADDRESS, address);
            jsonMain.put(LATITUDE, latitude);
            jsonMain.put(LONGITUDE, longitude);
            jsonMain.put(ALIAS_NAME, alias_name);
            jsonMain.put(COUNTRY, country);
            jsonMain.put(LAST_NAME, last_name);
            returnString = jsonMain.toString();
        } catch (Exception ex){Log.d(TAG," To String Exception : "+ex);
        }
        return returnString;
    }
}
