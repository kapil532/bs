package com.langoor.app.blueshak.services.model;

import android.util.Log;

import com.langoor.app.blueshak.AppController;
import com.langoor.app.blueshak.global.GlobalVariables;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CreateSalesModel implements Serializable {
    private final String TAG = "SalesModel";
    private final String
            STATUS = "sale_status";
    private final String NAME = "name";
    private final String SALESID = "sale_id";
    private final String LATITUDE = "latitude";
    private final String LONGITUDE = "longitude";
    private final String START_TIME = "start_time";
    private final String END_TIME = "end_time";
    private final String START_DATE = "start_date";
    private final String END_DATE = "end_date";
    private final String POSTCODES = "postcodes";
    private final String SALE_TYPE = "sale_type";
    private final String EMAIL = "email";
    private final String PHONE = "phone";
    private final String DESCRIPTION="description";
    private final String REQUEST_TYPE="request_type";
    private final String IMAGE = "images";

    public String getCountry_short() {
        return country_short;
    }

    public void setCountry_short(String country_short) {
        this.country_short = country_short;
    }

    private final String NEW_IMAGE = "new_images";
    private final String SALE_ADDRESS = "sale_address";
    private final String OLD_PRODUCTS = "old_products";
    private final String REMOVE_IMAGES = "remove_images";
    private final String PRODUCTS = "products";
    private final String COUNTRY_SHORT="country_short";

    public String getRequest_type() {
        return request_type;
    }

    public void setRequest_type(String request_type) {
        this.request_type = request_type;
    }

    String name = null;
    String saleID = null;
    String latitude = null;

    public String getSale_address() {
        return sale_address;
    }

    public void setSale_address(String sale_address) {
        this.sale_address = sale_address;
    }

    String longitude=null;
    String start_time = null;
    String email = null;
    String phone = null;
    String end_time = null;
    String country_short=null;

    public ArrayList<CreateImageModel> getRemove_images() {
        return remove_images;
    }

    public void setRemove_images(ArrayList<CreateImageModel> remove_images) {
        this.remove_images = remove_images;
    }

    String start_date = null;
    String sale_address=null;
    String request_type=null;
    ArrayList<CreateImageModel> images = new ArrayList<CreateImageModel>();
    ArrayList<CreateImageModel> remove_images = new ArrayList<CreateImageModel>();
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    String end_date = null;
    String description = null;
    ArrayList<String> sale_images = new ArrayList<String>();

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    String postcodes = null;
    String address=null;
    String saleType = null;

    List<CreateProductModel> products = new ArrayList<CreateProductModel>();

    public List<ProductModel> getItem_list() {
        return item_list;
    }

    public void setItem_list(List<ProductModel> item_list) {
        this.item_list = item_list;
    }

    List<ProductModel> item_list = new ArrayList<ProductModel>();

    List<ProductModel> old_item_list = new ArrayList<ProductModel>();
    static  JSONArray old_array=new JSONArray();

    public CreateSalesModel(){}

    public List<ProductModel> getOld_item_list() {
        return old_item_list;
    }

    public JSONArray getOld_array() {
        return old_array;
    }

    public void setOld_array(JSONArray old_array) {
        this.old_array = old_array;
    }

    public void setOld_item_list(List<ProductModel> old_item_list) {
        this.old_item_list = old_item_list;
    }

    public String getSaleID() {
        return saleID;
    }

    public void setSaleID(String saleID) {
        this.saleID = saleID;
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

    public List<CreateProductModel> getProducts() {
        return products;
    }

    public void setProducts(List<CreateProductModel> products) {
        this.products = products;
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

    public boolean toObject(String jsonObjectString){
        try{
            JSONObject json = new JSONObject(jsonObjectString);
            name = json.getString(NAME);
            if(json.has(SALESID))saleID = json.getString(SALESID);
            email = json.getString(EMAIL);
            phone = json.getString(PHONE);
            latitude = json.getString(LATITUDE);
            longitude = json.getString(LONGITUDE);
            start_time = json.getString(START_TIME);
            end_time = json.getString(END_TIME);
            start_date = json.getString(START_DATE);
            end_date = json.getString(END_DATE);
            postcodes = json.getString(POSTCODES);
            saleType = json.getString(SALE_TYPE);
            List<CreateProductModel> productModelList = new ArrayList<CreateProductModel>();
            JSONArray jsonArray = json.getJSONArray(PRODUCTS);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                CreateProductModel productModel = new CreateProductModel();
                productModel.toObject(jsonObject.toString());
                productModelList.add(productModel);
            }
            products.addAll(productModelList); productModelList.clear();
            return true;
        }catch(Exception ex){

            Log.d(TAG, "Json Exception : " + ex);}
        return false;
    }

    public boolean toObject(SalesModel salesModel, CategoryListModel categoryListModel){
        try{
            name = salesModel.getName();
            saleID = salesModel.getId();
            description=salesModel.getDescription();
            /*email = salesModel.getEma;
            phone = json.getString(PHONE);*/
            latitude = salesModel.getLatitude();
            longitude = salesModel.getLongitude();
            start_time = salesModel.getStart_time();
            end_time = salesModel.getEnd_time();
            start_date = salesModel.getUtc_start_date();
            end_date = salesModel.getEnd_date();
            postcodes = salesModel.getPostcodes();
            saleType = salesModel.getSaleType();
           /* sale_images=salesModel.getImages();*/
            sale_address=salesModel.getSale_address();

          /*  List<String> imageArray = salesModel.getImages();
            for(int i=0;i<imageArray.size();i++){
                CreateImageModel model = new CreateImageModel();
                model.setImage(imageArray.get(i));
                model.setDisplay(i==0?true:false);
                images.add(model);
            }*/
            List<ImageIdModel> createImageModels = salesModel.getCreateImageModels();
            for(int i=0;i<createImageModels.size();i++){
                CreateImageModel model = new CreateImageModel();
                model.setImage(createImageModels.get(i).getImage());
                model.setId(createImageModels.get(i).getId());
                model.setDisplay(i==0?true:false);
                images.add(model);
            }
            List<CreateProductModel> productModelList = new ArrayList<CreateProductModel>();
            List<ProductModel> productModels = salesModel.getProducts();
            item_list=salesModel.getProducts();
          /*  old_item_list=salesModel.getProducts();*/
            for(int i=0;i<productModels.size();i++){
                CreateProductModel productModel = new CreateProductModel();
                productModel.toObject(productModels.get(i),categoryListModel);
                productModelList.add(productModel);
            }
            products.addAll(productModelList);
            productModelList.clear();
            return true;
        }catch(Exception ex){

            Log.d(TAG, "Sales Object Exception : " + ex);}
        return false;
    }

    public ArrayList<CreateImageModel> getImages() {
        return images;
    }

    public void setImages(ArrayList<CreateImageModel> images) {
        this.images = images;
    }

    @Override
    public String toString(){
        String returnString = null;
        try{
            JSONObject jsonMain = new JSONObject();
            jsonMain.put(NAME, name);
            jsonMain.put(SALESID, saleID);
            jsonMain.put(DESCRIPTION,description);
            jsonMain.put(REQUEST_TYPE,request_type);
            jsonMain.put(EMAIL, email);
            jsonMain.put(PHONE, phone);
            jsonMain.put(SALE_TYPE, saleType);
            jsonMain.put(LATITUDE, latitude);
            jsonMain.put(LONGITUDE, longitude);
            jsonMain.put(START_TIME, start_time);
            jsonMain.put(END_TIME, end_time);
            jsonMain.put(START_DATE, start_date);
            jsonMain.put(END_DATE, end_date);
            jsonMain.put(POSTCODES, postcodes);
            jsonMain.put(SALE_ADDRESS, sale_address);
            jsonMain.put(SALE_ADDRESS, address);
          /*  jsonMain.put(COUNTRY_SHORT, country_short);*/
            JSONArray jsonArray = new JSONArray();
            List<ProductModel> productModelList = item_list;
            for(int i=0;i<productModelList.size();i++){
                jsonArray.put(productModelList.get(i).getId());
                /*jsonArray.put(new JSONObject(productModelList.get(i).toString()));*/
            }
            jsonMain.put(PRODUCTS,jsonArray);
            JSONArray imageArray = new JSONArray();
            for(int i=0;i<images.size();i++){
                CreateImageModel model = images.get(i);
                imageArray.put(new JSONObject(model.toString()));
            }
           /* JSONArray old_jsonArray = new JSONArray();
            List<ProductModel> old_productModelList = old_item_list;
            for(int j=0;j<old_productModelList.size();j++){
                old_jsonArray.put(old_productModelList.get(j).getId());
                *//*jsonArray.put(new JSONObject(productModelList.get(i).toString()));*//*
            }*/
            if(request_type.equalsIgnoreCase(GlobalVariables.TYPE_UPDATE_REQUEST)){
                jsonMain.put(OLD_PRODUCTS,/*old_jsonArray*/old_array);
             /*   jsonMain.put(REMOVE_IMAGES,remove_images);
                jsonMain.put(NEW_IMAGE, imageArray);*/
                JSONArray removed_arry = new JSONArray();
                Log.d(TAG,"remove_images : "+remove_images);
                for(int i=0;i<remove_images.size();i++){
                    CreateImageModel model1 = remove_images.get(i);
                    removed_arry.put(model1.getId());
                }
                jsonMain.put(REMOVE_IMAGES,removed_arry);
                Log.d(TAG,"new_imageArray : "+images);
                JSONArray new_imageArray = new JSONArray();
                for(int i=0;i<images.size();i++){
                    CreateImageModel model = images.get(i);
                    Log.d(TAG,"################## new_imageArray : "+model.toString());
                    if(model.is_new_image())
                        new_imageArray.put(new JSONObject(model.toString()));
                }
                Log.d(TAG,"################## final new_imageArray : "+new_imageArray);
                jsonMain.put(NEW_IMAGE,new_imageArray);
            }else
                jsonMain.put(IMAGE, imageArray);
            if(request_type.equalsIgnoreCase(GlobalVariables.TYPE_CREATE_REQUEST) || request_type.equalsIgnoreCase(GlobalVariables.TYPE_UPDATE_REQUEST) )
                returnString = jsonMain.toString();
            else if(request_type.equalsIgnoreCase(GlobalVariables.TYPE_PUBLISH_REQUEST)){
                JSONObject publishJsonMain = new JSONObject();
                publishJsonMain.put(SALESID, saleID);
                publishJsonMain.put(STATUS,GlobalVariables.TYPE_PUBLISH_REQUEST);
                returnString = publishJsonMain.toString();
            }

            Log.d(TAG, "######Updating old_item_list: ###########" + old_item_list.size());
            Log.d(TAG, "######Updating item_list: ###########" + item_list.size());
        }
        catch (Exception ex){Log.d(TAG," To String Exception : "+ex);
           }
        return returnString;
    }

}
