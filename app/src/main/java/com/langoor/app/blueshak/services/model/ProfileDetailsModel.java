package com.langoor.app.blueshak.services.model;

import android.util.Log;

import com.langoor.app.blueshak.AppController;

import org.json.JSONObject;

import java.io.Serializable;

public class ProfileDetailsModel implements Serializable {

    private final String TAG = "UserDetails";
    private final String
            ID = "id",
            NAME = "name",
            LAST_NAME = "last_name",
            EMAIL = "email",
            PHONE = "phone",
            ALIAS_NAME= "alias_name",
            ISD ="isd",
            ADDRESS = "address",
            AVATAR = "avatar",
            GCMID = "gcm_id",
            IMEI = "imei",
            APPVERSION = "app_version",
            APPOS = "app_os",
            FBID = "fb_id",
            ISACTIVE = "is_active",
            APPDOWNLOADDATE = "app_download_date",
            LASTLOGIN = "last_login",
            UPDATEDAT = "updated_at",
            LISTINGS="listings",
            LIKES="likes",
            SOLD="sold",
            BUYING="buying",
            REVIEWS="reviews",
            LATITUDE="latitude",
            LONGITUDE="longitude",
            TOKEN="token",
            SHOP="shop",
            SHOP_NAME = "shop_name",
            SHOP_DESCRIPTION="shop_description",
            SHOP_LINK="shop_link",
            POSTAL_CODE="postal_code",
            IS_OTP_VERIFIED="is_otp_verified",
            IS_EMAIL_VERIFIED="is_email_verify",
            CREATED_AGO="created_ago",
            REVIEWS_COUNT="reviews_count",
            COUNTRY="country",
            CREATEDAT = "created_at";
/*
    String email=null,fb_id=null, token = null, name = null, address = "", phone = "", id, city, postalCode, state, image;
*/
    boolean is_active=false,
            is_banned=false,
            is_otp_verified=false,
            is_email_verify=false;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    String alias_name=null,country=null;

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    String last_name=null;
    String reviews_count="";
    String shop_name=null;
    String shop_description=null;
    String shop_link=null;
    String id = null;
    String name = null;
    String email = null;
    String isd = null;
    String phone=null;
    String token = null;
    String address =null;
    String longitude=null;
    String latitude=null;
    String postal_code=null;
    String city=null;
    String state=null;
    String avatar=null;
    String updated_at=null;
    String created_at=null;
    String listings=null;
    String likes=null;
    String sold=null;
    String buying=null;
    String fb_id=null;
    String created_ago=null;
    String reviews=null;

    String image = null;

    Shop shop=new Shop();
    public ProfileDetailsModel(){}

    public void setImage(String bitmap) {
        this.image = bitmap;
    }

    public String getTAG() {
        return TAG;
    }

    public boolean is_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public boolean is_banned() {
        return is_banned;
    }

    public void setIs_banned(boolean is_banned) {
        this.is_banned = is_banned;
    }

    public boolean is_email_verify() {
        return is_email_verify;
    }

    public void setIs_email_verify(boolean is_email_verify) {
        this.is_email_verify = is_email_verify;
    }

    public boolean is_otp_verified() {
        return is_otp_verified;
    }

    public void setIs_otp_verified(boolean is_otp_verified) {
        this.is_otp_verified = is_otp_verified;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getListings() {
        return listings;
    }

    public void setListings(String listings) {
        this.listings = listings;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getSold() {
        return sold;
    }

    public void setSold(String sold) {
        this.sold = sold;
    }

    public String getBuying() {
        return buying;
    }

    public void setBuying(String buying) {
        this.buying = buying;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public String getImage() {
        return image;
    }

    public String getFb_id() {
        return fb_id;
    }

    public void setFb_id(String fb_id) {
        this.fb_id = fb_id;
    }

    public String getCreated_ago() {
        return created_ago;
    }

    public void setCreated_ago(String created_ago) {
        this.created_ago = created_ago;
    }

    public String getReviews_count() {
        return reviews_count;
    }

    public void setReviews_count(String reviews_count) {
        this.reviews_count = reviews_count;
    }

    public String getAlias_name() {
        return alias_name;
    }

    public void setAlias_name(String alias_name) {
        this.alias_name = alias_name;
    }

    public boolean toObject(String jsonObject){

        try{
            System.out.println("######jsonObject#######"+jsonObject);
            JSONObject jsonObject1=new JSONObject(jsonObject);
            JSONObject json = jsonObject1.getJSONObject("user");
            if(json.has(ID)) id = json.getString(ID);
            if(json.has(NAME)) name = json.getString(NAME);
            if(json.has(EMAIL)) email = json.getString(EMAIL);
            if(json.has(PHONE)) phone = json.getString(PHONE);
            if(json.has(ISD)) isd = json.getString(ISD);
            if(json.has(ADDRESS)) address = json.getString(ADDRESS);
            if(json.has(LATITUDE)) latitude = json.getString(LATITUDE);
            if(json.has(LONGITUDE)) longitude = json.getString(LONGITUDE);
            if(json.has(POSTAL_CODE)) postal_code = json.getString(POSTAL_CODE);
            if(json.has(LISTINGS)) listings = json.getString(LISTINGS);
            if(json.has(REVIEWS)) reviews = json.getString(REVIEWS);
            if(json.has(AVATAR)) avatar = json.getString(AVATAR);
            if(json.has(TOKEN)) token = json.getString(TOKEN);
            if(json.has(UPDATEDAT))updated_at = json.getString(UPDATEDAT);
            if(json.has(CREATEDAT)) created_at = json.getString(CREATEDAT);
            if(json.has(LISTINGS))  listings = json.getString(LISTINGS);
            if(json.has(LIKES)) likes = json.getString(LIKES);
            if(json.has(SOLD)) sold = json.getString(SOLD);

            if(json.has(BUYING)) buying = json.getString(BUYING);
            if(json.has(FBID)) fb_id = json.getString(FBID);
            if(json.has(CREATED_AGO)) created_ago = json.getString(CREATED_AGO);
            if(json.has(REVIEWS_COUNT)) reviews_count = json.getString(REVIEWS_COUNT);
            if(json.has(ALIAS_NAME)) alias_name = json.getString(ALIAS_NAME);
            if(json.has(LAST_NAME)) last_name = json.getString(LAST_NAME);
            if(json.has(COUNTRY)) country = json.getString(COUNTRY);
            if(json.has(SHOP)){

                JSONObject shop_obj=json.getJSONObject(SHOP);
                shop.toObject(shop_obj.toString());

            }
            int temp = 0;
            try{temp = json.getInt(ISACTIVE);}catch(Exception e){temp =0;}if(temp>0){is_active=true;}else{is_active=false;}temp=0;
            try{temp = json.getInt(IS_OTP_VERIFIED);}catch(Exception e){temp =0;}if(temp>0){is_otp_verified=true;}else{is_otp_verified=false;}temp=0;
            try{temp = json.getInt(IS_EMAIL_VERIFIED);}catch(Exception e){temp =0;}if(temp>0){is_email_verify=true;}else{is_email_verify=false;}temp=0;
            return true;
        }catch(Exception ex){
            Log.d(TAG, "Json Exception : " + ex);
           }
        return false;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_description() {
        return shop_description;
    }

    public void setShop_description(String shop_description) {
        this.shop_description = shop_description;
    }

    public String getShop_link() {
        return shop_link;
    }

    public void setShop_link(String shop_link) {
        this.shop_link = shop_link;
    }

    public String getIsd() {
        return isd;
    }

    public void setIsd(String isd) {
        this.isd = isd;
    }

    @Override
    public String toString(){
        String returnString = null;
        try{
            JSONObject jsonMain = new JSONObject();
            jsonMain.put(ID, id);
            jsonMain.put(NAME, name);
            jsonMain.put(EMAIL, email);
            jsonMain.put(ALIAS_NAME, alias_name);
            jsonMain.put(PHONE, phone);
            jsonMain.put(ISD, isd);
            jsonMain.put(ADDRESS, address);
            jsonMain.put(SHOP_NAME, shop_name);
            jsonMain.put(SHOP_DESCRIPTION, shop_description);
            jsonMain.put(LATITUDE, latitude);
            jsonMain.put(LONGITUDE, longitude);
            jsonMain.put(LAST_NAME, last_name);
            jsonMain.put(COUNTRY, country);
            jsonMain.put("base64_image", image);
            returnString = jsonMain.toString();
        }
        catch (Exception ex){Log.d(TAG," To String Exception : "+ex);

        }
        return returnString;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}



