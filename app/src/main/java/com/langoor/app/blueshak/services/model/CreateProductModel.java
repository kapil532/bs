package com.langoor.app.blueshak.services.model;

import android.text.TextUtils;
import android.util.Log;

import com.langoor.app.blueshak.AppController;
import com.langoor.app.blueshak.global.GlobalVariables;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateProductModel implements Serializable {
    private final String TAG = "CreateProductModel";
    private final String
            NAME = "product_name";
    private final String SALE_PRICE = "sale_price";

    public boolean is_pickup() {
        return is_pickup;
    }

    public void setIs_pickup(boolean is_pickup) {
        this.is_pickup = is_pickup;
    }

    private final String DESCRIPTION = "description";

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    private final String IS_SHIPPABLE = "is_shippable";
    private final String IS_NEGOTIABLE = "is_negotiable";
    private final String SALE_TYPE = "sale_type";
    private final String IMAGE = "images";
    private final String SALESID = "sale_id";
    private final String LATITUDE = "latitude";
    private final String LONGITUDE = "longitude";
    private final String ADDRESS = "address";
    private final String POSTCODES = "postcodes";
    private final String CATEGORIES = "categories";
    private final String IS_PRODUCT_NEW="is_product_new";
    private final String SUBHURB="suburb";
    private final String CITY="city";
    private final String IS_PICKUP="is_pickup";
    private final String REQUEST_TYPE="request_type";
    private final String REMOVE_IMAGES = "remove_images";
    private final String  NEW_IMAGE = "new_images";
    private final String  SALE_ID = "sale_id";
    private final String  CURRENCY = "currency";
    private final String  COUNTRY_SHORT = "country_short";


    /*@@@@@@@@@@@@@@@@@@added by me@@@@@@@@@@@@@@@@*/
    private final String PRODUCT_ID="product_id";
    String product_id = null;
    String sale_id = null;
    /*3333333333333333333*/

    public String getSale_id() {
        return sale_id;
    }

    public void setSale_id(String sale_id) {
        this.sale_id = sale_id;
    }
    String currency = null;
    String latitude = null;
    String longitude=null;
    String postcodes = null;
    String name = null;
    String salePrice = null;
    String retailPrice=null;
    String description = null;
    String request_type=null;

    public String getCountry_short() {
        return country_short;
    }

    public void setCountry_short(String country_short) {
        this.country_short = country_short;
    }

    String saleID = null;
    String suburb = null;
    String city = null,country_short=null;

    public String getCategory_string() {
        return category_string;
    }

    public void setCategory_string(String category_string) {
        this.category_string = category_string;
    }

    String saleType = null;
    String category_string = null;
    public String getSaleType() {
        return saleType;
    }

    public void setSaleType(String saleType) {
        this.saleType = saleType;
    }

    public String getSaleID() {
        return saleID;
    }

    public String getRequest_type() {
        return request_type;
    }

    public void setRequest_type(String request_type) {
        this.request_type = request_type;
    }
    ArrayList<CreateImageModel> remove_images = new ArrayList<CreateImageModel>();

    public ArrayList<CreateImageModel> getRemove_images() {
        return remove_images;
    }

    public void setRemove_images(ArrayList<CreateImageModel> remove_images) {
        this.remove_images = remove_images;
    }

    public void setSaleID(String saleID) {
        this.saleID = saleID;

    }

    String address=null;

    ArrayList<CreateImageModel> images = new ArrayList<CreateImageModel>();
    ArrayList<String> categories = new ArrayList<String>();

    boolean
            shippable        = false,
            is_product_new=false,
            is_pickup=false,
            negotiable      = false;

    int id = -1;

    public String getTAG() {
        return TAG;
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


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }



    public CreateProductModel(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(String retailPrice) {
        this.retailPrice = retailPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<CreateImageModel> getImages() {
        return images;
    }

    public void setImages(ArrayList<CreateImageModel> images) {
        this.images = images;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public boolean isShippable() {
        return shippable;
    }

    public void setShippable(boolean shippable) {
        this.shippable = shippable;
    }


    public boolean isNegotiable() {
        return negotiable;
    }

    public void setNegotiable(boolean negotiable) {
        this.negotiable = negotiable;
    }

    public boolean toObject(String jsonObject){
        try{
            JSONObject json = new JSONObject(jsonObject);
            name = json.getString(NAME);
            salePrice = json.getString(SALE_PRICE);
            description = json.getString(DESCRIPTION);
            JSONArray imageArray = json.getJSONArray(IMAGE);
            for(int i=0;i<imageArray.length();i++){
                JSONObject imageObj = imageArray.getJSONObject(i);
                CreateImageModel model = new CreateImageModel();
                model.toObject(imageObj.toString());
                images.add(model);
            }
            JSONArray categoryArray = json.getJSONArray(CATEGORIES);
            for(int i=0;i<categoryArray.length();i++){
                categories.add(categoryArray.getString(i));
            }
            shippable = json.getBoolean(IS_SHIPPABLE);
            negotiable = json.getBoolean(IS_NEGOTIABLE);
            is_product_new = json.getBoolean(IS_PRODUCT_NEW);
            if(json.has(ADDRESS)) address=json.getString(ADDRESS);
            if(json.has(LATITUDE)) latitude=json.getString(LATITUDE);
            if(json.has(LONGITUDE)) longitude=json.getString(LONGITUDE);
            return true;
        }catch(Exception ex){
            Log.d(TAG, "Json Exception : " + ex);
           }
        return false;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public boolean is_product_new() {
        return is_product_new;
    }

    public void setIs_product_new(boolean is_product_new) {
        this.is_product_new = is_product_new;
    }

    public boolean toObject(ProductModel productModel, CategoryListModel categoryListModel){
        try{
            product_id=productModel.getId();
            name = productModel.getName();
            salePrice = productModel.getSalePrice();
            retailPrice = productModel.getRetailPrice();
            description = productModel.getDescription();
            product_id=productModel.getId();
            address=productModel.getAddress();
            latitude=productModel.getLatitude();
            longitude=productModel.getLongitude();
            currency=productModel.getCurrency();
            saleType=productModel.getSaleType();
            sale_id=productModel.getSaleID();
           /* List<String> imageArray = productModel.getImage();
            for(int i=0;i<imageArray.size();i++){
                CreateImageModel model = new CreateImageModel();
                model.setImage(imageArray.get(i));
                model.setDisplay(i==0?true:false);
                images.add(model);
            }*/
            List<ImageIdModel> createImageModels = productModel.getCreateImageModels();
            for(int i=0;i<createImageModels.size();i++){
                CreateImageModel model = new CreateImageModel();
                model.setImage(createImageModels.get(i).getImage());
                model.setId(createImageModels.get(i).getId());
                model.setDisplay(i==0?true:false);
                images.add(model);
            }

            String cat = productModel.getProductCategory();
            ArrayList<String> myList = new ArrayList<String>(Arrays.asList(cat.split(",")));
            categories.clear();
            categories.addAll(categoryListModel.getIdsforNames(myList));

            shippable = productModel.isShipable();
            is_pickup=productModel.isPickup();
            negotiable = productModel.isNegotiable();
            return true;
        }catch(Exception ex){
            Log.d(TAG, "Product Model Exception : " + ex);
           }
        return false;
    }

    public String getPostcodes() {
        return postcodes;
    }

    public void setPostcodes(String postcodes) {
        this.postcodes = postcodes;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString(){
        String returnString = null;
        try{
            JSONObject jsonMain = new JSONObject();
            jsonMain.put(NAME, name);
            jsonMain.put(REQUEST_TYPE,request_type);
            jsonMain.put(SALE_PRICE, salePrice);
            jsonMain.put(IS_PRODUCT_NEW, is_product_new);
            jsonMain.put(DESCRIPTION, description);
            jsonMain.put(ADDRESS, address);
            jsonMain.put(SUBHURB, suburb);
            jsonMain.put(CITY,city);
            jsonMain.put(LONGITUDE, longitude);
            jsonMain.put(LATITUDE, latitude);
            if(saleType.equalsIgnoreCase(GlobalVariables.TYPE_GARAGE))
                jsonMain.put(SALESID, saleID);
            /*added for updating sale*/
            jsonMain.put(PRODUCT_ID, product_id);
            jsonMain.put(SALE_ID, sale_id);
            JSONArray categoryArray = new JSONArray();
            for(int i=0;i<categories.size();i++){
                categoryArray.put(categories.get(i));
            }
            jsonMain.put(CATEGORIES, categoryArray);

            jsonMain.put(IS_SHIPPABLE, shippable);
            jsonMain.put(IS_PICKUP, is_pickup);
            jsonMain.put(IS_NEGOTIABLE, negotiable);
            jsonMain.put(IS_PRODUCT_NEW, is_product_new);
            jsonMain.put(IS_NEGOTIABLE, negotiable);
            jsonMain.put(CURRENCY, currency);
            jsonMain.put(POSTCODES, postcodes);
         /*   jsonMain.put(COUNTRY_SHORT, country_short);*/

            jsonMain.put(SALE_TYPE, saleType);

            JSONArray imageArray = new JSONArray();
            for(int i=0;i<images.size();i++){
                CreateImageModel model = images.get(i);
                imageArray.put(new JSONObject(model.toString()));
            }
            if(request_type.equalsIgnoreCase(GlobalVariables.TYPE_UPDATE_REQUEST)){
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
            returnString = jsonMain.toString();
            Log.d(TAG," CreProduct request : "+returnString);
        }
        catch (Exception ex){Log.d(TAG," To String Exception : "+ex);
           }
        return returnString;
    }
}
