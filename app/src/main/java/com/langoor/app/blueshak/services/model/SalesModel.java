package com.langoor.app.blueshak.services.model;

import android.util.Log;

import com.langoor.app.blueshak.AppController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SalesModel implements Serializable {
    private final String TAG = "SalesModel";
    private final String
            ID = "sale_id",
            NAME = "sale_name",
            SALE_NAME = "name",
            DESCRIPTION="description",
            LATITUDE = "latitude",
            LONGITUDE = "longitude",
            START_TIME = "start_time",
            END_TIME = "end_time",
            START_DATE = "start_date",
            END_DATE = "end_date",
            POSTCODES = "postcodes",
            SALE_TYPE = "sale_type",
            USER_ID = "user_id",
            IS_SALE_ACTIVE = "is_sale_active",
            SALE_UPDATED_AT = "sale_updated_at",
            SALE_CEATED_AT = "sale_created_at",
            SELLER_NAME = "seller_name",
            DISTANCE_AWAY = "distance_away",
            SELLER_PHONE="seller_phone",
            VIEWS="views",
            SALE_ITEM_COUNT="sale_items_count",
            SALES_IMAGE = "images",
            LINK="link",
            IS_OWN_SALE="is_own_sale",
            SELLER_IMAGE="seller_image",
            AVG_SELLER_RATING ="avg_seller_rating",
            SALE_ADDRESS = "sale_address",
            UTC_START_DATE = "utc_start_date",
            READ_REVIEWS_COUNT = "reviews_count",
            PRODUCTS = "products";

    public String getReviews_count() {
        return reviews_count;
    }

    public void setReviews_count(String reviews_count) {
        this.reviews_count = reviews_count;
    }
    boolean is_selected=false;
    String
            id = null;

    public boolean is_selected() {
        return is_selected;
    }

    public void setIs_selected(boolean is_selected) {
        this.is_selected = is_selected;
    }

    String utc_start_date=null;
    String reviews_count="";
    String name = null;
    String avg_seller_rating = null;

    public boolean is_own_sale() {
        return is_own_sale;
    }

    public void setIs_own_sale(boolean is_own_sale) {
        this.is_own_sale = is_own_sale;
    }

    String latitude = "0";
    String longitude="0";
    String start_time = null;
    String end_time = null;
    String start_date = null;
    String end_date = null;
    String postcodes = null;
    String saleType = null;
    String userID = null;
    String saleUpdatedAt = null;
    String saleCreatedAt = null;
    String sellerName = null;
    String sellerNumber=null;
    String sale_items_count=null;
    String description=null;
    String sale_address=null;
    String seller_image=null;
    ArrayList<ImageIdModel> createImageModels = new ArrayList<ImageIdModel>();
    ArrayList<String> images = new ArrayList<String>();
    public String getViews() {
        return views;
    }

    public ArrayList<ImageIdModel> getCreateImageModels() {
        return createImageModels;
    }

    public void setCreateImageModels(ArrayList<ImageIdModel> createImageModels) {
        this.createImageModels = createImageModels;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public void setViews(String views) {
        this.views = views;
    }

    String views=null;
    String distanceAway = null;

    List<ProductModel> products = new ArrayList<ProductModel>();

    boolean active = false,is_own_sale=false;


    public SalesModel(){}

    public void updateProduct(ProductModel productModel){
        ProductModel productModelLocal = getProduct(productModel.getId());
        if(productModelLocal!=null){
            productModelLocal.setActive(productModel.isActive());
            productModelLocal.setAvailable(productModel.isAvailable());
            productModelLocal.setDescription(productModel.getDescription());
            productModelLocal.setImage(productModel.getImage());
            productModelLocal.setDisplayImage(productModel.isDisplayImage());
            productModelLocal.setName(productModel.getName());
            productModelLocal.setNegotiable(productModel.isNegotiable());
            productModelLocal.setPickup(productModel.isPickup());
            productModelLocal.setProductCategory(productModel.getProductCategory());
            productModelLocal.setProductCreatedAt(productModel.getProductCreatedAt());
            productModelLocal.setProductUpdatedAt(productModel.getProductUpdatedAt());
            productModelLocal.setRetailPrice(productModel.getRetailPrice());
            productModelLocal.setSalePrice(productModel.getSalePrice());
            productModelLocal.setShipable(productModel.isShipable());
        }
    }

    public ProductModel getProduct(String product_id){
        List<ProductModel> productModels = getProducts();
        for(int i=0;i<productModels.size();i++){
            if(product_id.equalsIgnoreCase(productModels.get(i).getId())){
                return productModels.get(i);
            }
        }
        return null;
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

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getPostcodes() {
        return postcodes;
    }

    public void setPostcodes(String postcodes) {
        this.postcodes = postcodes;
    }

    public String getSaleType() {
        return saleType;
    }

    public void setSaleType(String saleType) {
        this.saleType = saleType;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getSaleUpdatedAt() {
        return saleUpdatedAt;
    }

    public void setSaleUpdatedAt(String saleUpdatedAt) {
        this.saleUpdatedAt = saleUpdatedAt;
    }

    public String getSaleCreatedAt() {
        return saleCreatedAt;
    }

    public void setSaleCreatedAt(String saleCreatedAt) {
        this.saleCreatedAt = saleCreatedAt;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getDistanceAway() {
        return distanceAway;
    }

    public void setDistanceAway(String distanceAway) {
        this.distanceAway = distanceAway;
    }

    public List<ProductModel> getProducts() {
        return products;
    }

    public void setProducts(List<ProductModel> products) {
        this.products = products;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getSellerNumber() {
        return sellerNumber;
    }

    public void setSellerNumber(String sellerNumber) {
        this.sellerNumber = sellerNumber;
    }

    public String getSale_items_count() {
        return sale_items_count;
    }

    public void setSale_items_count(String sale_items_count) {
        this.sale_items_count = sale_items_count;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSeller_image() {
        return seller_image;
    }

    public void setSeller_image(String seller_image) {
        this.seller_image = seller_image;
    }

    public String getSale_address() {
        return sale_address;
    }

    public void setSale_address(String sale_address) {
        this.sale_address = sale_address;
    }

    public String getAvg_seller_rating() {
        return avg_seller_rating;
    }

    public void setAvg_seller_rating(String avg_seller_rating) {
        this.avg_seller_rating = avg_seller_rating;
    }

    public String getUtc_start_date() {
        return utc_start_date;
    }

    public void setUtc_start_date(String utc_start_date) {
        this.utc_start_date = utc_start_date;
    }

    public boolean toObject(String jsonObjectString){
        try{
            JSONObject json = new JSONObject(jsonObjectString);
            if(json.has(ID)) id = json.getString(ID);
            if(json.has(NAME)) name = json.getString(NAME);
            if(json.has(SALE_NAME)) name = json.getString(SALE_NAME);
            if(json.has(DESCRIPTION)) description = json.getString(DESCRIPTION);
            if(json.has(LATITUDE))latitude = json.getString(LATITUDE);
            if(json.has(LONGITUDE))longitude = json.getString(LONGITUDE);
            if(json.has(START_TIME))start_time = json.getString(START_TIME);
            if(json.has(END_TIME)) end_time = json.getString(END_TIME);
            if(json.has(START_DATE))  start_date = json.getString(START_DATE);
            if(json.has(END_DATE)) end_date = json.getString(END_DATE);
            if(json.has(POSTCODES))  postcodes = json.getString(POSTCODES);
            if(json.has(SALE_TYPE))   saleType = json.getString(SALE_TYPE);
            if(json.has(USER_ID))  userID = json.getString(USER_ID);
            if(json.has(SELLER_PHONE))sellerNumber=json.getString(SELLER_PHONE);
            if(json.has(SALE_UPDATED_AT)) saleUpdatedAt = json.getString(SALE_UPDATED_AT);
            if(json.has(SALE_CEATED_AT))  saleCreatedAt = json.getString(SALE_CEATED_AT);
            if(json.has(SELLER_NAME))  sellerName = json.getString(SELLER_NAME);
            if(json.has(VIEWS))views = json.getString(VIEWS);
            if(json.has(DISTANCE_AWAY))distanceAway = json.getString(DISTANCE_AWAY);
            if(json.has(SALE_ITEM_COUNT))sale_items_count = json.getString(SALE_ITEM_COUNT);
            if(json.has(SELLER_IMAGE))seller_image = json.getString(SELLER_IMAGE);
            if(json.has(SALE_ADDRESS))sale_address = json.getString(SALE_ADDRESS);
            if(json.has(AVG_SELLER_RATING))avg_seller_rating = json.getString(AVG_SELLER_RATING);
            if(json.has(UTC_START_DATE))utc_start_date = json.getString(UTC_START_DATE);
            if(json.has(READ_REVIEWS_COUNT))reviews_count = json.getString(READ_REVIEWS_COUNT);
            if(json.has(SALES_IMAGE)) {
                JSONArray imageArray = new JSONArray();
                imageArray = json.getJSONArray(SALES_IMAGE);
                createImageModels.clear();
                images.clear();
                for (int i = 0; i < imageArray.length(); i++) {
                    JSONObject obj = (JSONObject) imageArray.get(i);
                    ImageIdModel imageIdModel= new ImageIdModel();
                    imageIdModel.toObject(obj.toString());
                    createImageModels.add(imageIdModel);
                    if (obj.has(LINK)) {
                        images.add(obj.getString(LINK));
                    }
                    /*   images.add(imageArray.getString(i));*/
                }
            }



            int temp = 0;
            try{temp = json.getInt(IS_SALE_ACTIVE);}catch(Exception e){temp =0;}if(temp>0){active=true;}else{active=false;}
            try{temp = json.getInt(IS_OWN_SALE);}catch(Exception e){temp =0;}if(temp>0){is_own_sale=true;}else{is_own_sale=false;}

            List<ProductModel> productModelList = new ArrayList<ProductModel>();
            if(json.has(PRODUCTS)){
                JSONArray jsonArray = json.getJSONArray(PRODUCTS);
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    ProductModel productModel = new ProductModel();
                    productModel.toObject(jsonObject.toString());
                    productModelList.add(productModel);
                }
                products.addAll(productModelList); productModelList.clear();
            }

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
            jsonMain.put(LATITUDE, latitude);
            jsonMain.put(LONGITUDE, longitude);
            jsonMain.put(START_TIME, start_time);
            jsonMain.put(END_TIME, end_time);
            jsonMain.put(START_DATE, start_date);
            jsonMain.put(SALE_TYPE, saleType);
            jsonMain.put(SELLER_PHONE, sellerNumber);
            jsonMain.put(END_DATE, end_date);
            jsonMain.put(POSTCODES, postcodes);
            jsonMain.put(SELLER_NAME, sellerName);
            jsonMain.put(USER_ID, userID);
            jsonMain.put(SALE_UPDATED_AT, saleUpdatedAt);
            jsonMain.put(SALE_CEATED_AT, saleCreatedAt);
            jsonMain.put(SELLER_NAME, sellerName);
            jsonMain.put(DISTANCE_AWAY, distanceAway);
            String temp = "0";
            if(active){temp = "1";}else{temp="0";}jsonMain.put(IS_SALE_ACTIVE, temp);
            JSONArray jsonArray = new JSONArray();
            List<ProductModel> productModelList = products;
            for(int i=0;i<productModelList.size();i++){
                jsonArray.put(new JSONObject(productModelList.get(i).toString()));
            }
            jsonMain.put(PRODUCTS,jsonArray);
            returnString = jsonMain.toString();
        }
        catch (Exception ex){Log.d(TAG," To String Exception : "+ex);
           }
        return returnString;
    }

}
