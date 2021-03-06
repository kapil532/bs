package com.blueshak.app.blueshak.services.model;

import android.util.Log;

import com.blueshak.app.blueshak.global.GlobalVariables;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductModel implements Serializable {
    private final String TAG = "ProductModel";
    private final String
            CURRENCY = "currency";
    private final String SALE_ID = "sale_id";
    private final String SALE_NAME = "sale_name";
    private final String LATITUDE = "latitude";
    private final String LONGITUDE = "longitude";
    private final String START_TIME = "start_time";
    private final String END_TIME = "end_time";
    private final String START_DATE = "start_date";
    private final String END_DATE = "end_date";
    private final String POSTALCODES = "postcodes";
    private final String SALE_TYPE = "sale_type";
    private final String IS_SALE_ACTIVE = "is_sale_active";
    private final String SALE_UPDATED_AT = "sale_updated_at";
    private final String SALE_CREATED_AT = "sale_created_at";
    private final String ID = "product_id";
    private final String NAME = "product_name";
    private final String SALE_PRICE = "sale_price";
    private final String RETAIL_PRICE = "retail_price";
    private final String DESCRIPTION = "description";
    private final String IS_SHIPABLE = "is_shippable";
    private final String IS_PICKUP = "is_pickup";
    private final String IS_AVAILABLE = "is_available";
    private final String IS_NEGOTIABLE = "is_negotiable";
    private final String IS_ACTIVE = "is_active";
    private final String PRODUCT_UPDATED_AT = "product_updated_at";
    private final String PRODUCT_CREATED_AT = "product_created_at";
    private final String PRODUCT_IMAGE = "images";
    private final String PRODUCT_IMAGES = "product_images";
    private final String IMAGE = "image";
    private final String IS_DISPLAY_IMAGE = "is_display_image";
    private final String PRODUCT_CATEGORY = "product_category";
    private final String SHOP_NAME = "shop_or_sale_name";
    private final String SELLER_NAME = "seller_name";
    private final String SELLER_PHONE = "seller_phone";
    private final String SELLER_IMAGE = "seller_image";
    private final String SELLER_ID = "seller_user_id";
    private final String USER_ID = "user_id";
    private final String VIEWS = "num_views";
    private final String OFFERS = "num_offers";
    private final String CUMULATIVE_RATING = "avg_seller_rating";
    private final String IS_BOOKAMARK = "is_bookmarked";
    private final String REVIEWS_COUNT = "num_reviews";
    private final String LAST_UPDATED = "last_updated";
    private final String IS_PRODUCT_NEW = "is_product_new";
    private final String IS_FEATURE_PRODUCT = "is_featured";
    private final String LINK = "link";
    private final String ADDRESS = "product_address";
    private final String IS_NEW_FLAG = "is_new_flag";
    private List<ProductModel> horizontalList;

    public List<ProductModel> getHorizontalList() {
        return horizontalList;
    }

    public void setHorizontalList(List<ProductModel> horizontalList) {
        this.horizontalList = horizontalList;
    }

    public String getShipping_delivery_date() {
        return shipping_delivery_date;
    }

    public void setShipping_delivery_date(String shipping_delivery_date) {
        this.shipping_delivery_date = shipping_delivery_date;
    }

    public String getSHIPPING_DELIVERY_DATE() {
        return SHIPPING_DELIVERY_DATE;
    }

    private final String CITY = "city";
    private final String SHIPPING_DELIVERY_DATE = "shipping_delivery_date";


    public String getLOCAL_SHIPPING_COST() {
        return LOCAL_SHIPPING_COST;
    }

    public String getIS_INTL_SHIPPING() {
        return IS_INTL_SHIPPING;
    }

    public String getINTL_SHIPPING_COST() {
        return INTL_SHIPPING_COST;
    }

    public String getTIME_TO_DELIVER() {
        return TIME_TO_DELIVER;
    }

    public String getHIDE_ITEM_PRICE() {
        return HIDE_ITEM_PRICE;
    }

    public String getSHIPPING_FOC() {
        return SHIPPING_FOC;
    }

    private final String SUBHURB = "suburb";
    private final String IS_NEW = "is_new";

    //changesss bool
    private final String SHIPPING_FOC = "shipping_foc";
    private final String IS_INTL_SHIPPING = "is_intl_shipping";

    private final String HIDE_ITEM_PRICE = "hide_item_price";


    //string
    private final String INTL_SHIPPING_COST = "intl_shipping_cost";
    private final String LOCAL_SHIPPING_COST = "local_shipping_cost";
    private final String TIME_TO_DELIVER = "time_to_deliver";
    //Changes

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public boolean is_new() {
        return is_new;
    }

    public void setIs_new(boolean is_new) {
        this.is_new = is_new;
    }

    /*  "is_new": 1,
                "sale_name": "Kamanu40",

                "views": 24,*/
    String
            saleID = null;
    String currency = null;
    String startTime = null;
    String endTime = null;
    String saleName = null;
    String startDate = null;
    String endDate = null;
    String postalcodes = null;
    String saleType = null;
    String latitude = "0";
    String longitude = "0";
    String id = null;
    String name = null;
    String salePrice = null;
    String retailPrice = null;
    String description = null;
    String productUpdatedAt = null;
    String productCreatedAt = null;
    String saleCreatedAt = null;
    String image = null;
    String productCategory = null;
    String sellerName = null;
    String seller_phone = null;
    String seller_id = null;
    String user_id = null;
    String views = null;
    String cummalative_rating = null;
    String reviews_count = null;
    String last_updated = null;
    String item_display_Image = null;
    String shop_or_sale_name = null;
    String seller_image = null;
    String address = null;
    String city = null;
    String subhrub = null;
    String local_shipping_cost = null;
    String shipping_delivery_date = null;

    public String getIntl_shipping_cost() {
        return intl_shipping_cost;
    }

    public void setIntl_shipping_cost(String intl_shipping_cost) {
        this.intl_shipping_cost = intl_shipping_cost;
    }

    String intl_shipping_cost = null;

    public boolean isHide_item_price() {
        return hide_item_price;
    }

    public void setHide_item_price(boolean hide_item_price) {
        this.hide_item_price = hide_item_price;
    }

    public String getLocal_shipping_cost() {
        return local_shipping_cost;
    }

    public void setLocal_shipping_cost(String local_shipping_cost) {
        this.local_shipping_cost = local_shipping_cost;
    }

    public String getTime_to_deliver() {
        return time_to_deliver;
    }

    public void setTime_to_deliver(String time_to_deliver) {
        this.time_to_deliver = time_to_deliver;
    }

    public boolean isShipping_foc() {
        return shipping_foc;
    }

    public void setShipping_foc(boolean shipping_foc) {
        this.shipping_foc = shipping_foc;
    }

    public boolean is_intl_shipping() {
        return is_intl_shipping;
    }

    public void setIs_intl_shipping(boolean is_intl_shipping) {
        this.is_intl_shipping = is_intl_shipping;
    }


    String time_to_deliver = null;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSeller_image() {
        return seller_image;
    }

    public void setSeller_image(String seller_image) {
        this.seller_image = seller_image;
    }

    String offers = null;

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getSeller_phone() {
        return seller_phone;
    }

    public void setSeller_phone(String seller_phone) {
        this.seller_phone = seller_phone;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


    public String getTAG() {
        return TAG;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    boolean
            shipable = false;
    boolean pickup = false;
    boolean available = false;
    boolean negotiable = false;
    boolean active = false;
    boolean sale_active = false;
    boolean is_garage_item = false;


    boolean shipping_foc = false;
    boolean is_intl_shipping = false;
    boolean hide_item_price = false;


    public boolean is_garage_item() {
        if (saleType != null && saleType.equalsIgnoreCase(GlobalVariables.TYPE_GARAGE))
            return true;
        else
            return false;
    }

    public void setIs_garage_item(boolean is_garage_item) {
        this.is_garage_item = is_garage_item;
    }

    public boolean is_selected() {
        return is_selected;
    }

    public void setIs_selected(boolean is_selected) {
        this.is_selected = is_selected;
    }

    boolean is_new = false;
    boolean is_bookmark = false;
    boolean is_product_new = false;
    boolean is_selected = false;
    boolean displayImage = false;
    boolean is_featured = false;

    public boolean isIs_featured() {
        return is_featured;
    }

    public void setIs_featured(boolean is_featured) {
        this.is_featured = is_featured;
    }

    ArrayList<String> images = new ArrayList<String>();
    ArrayList<ImageIdModel> createImageModels = new ArrayList<ImageIdModel>();

    public ArrayList<ImageIdModel> getCreateImageModels() {
        return createImageModels;
    }

    public void setCreateImageModels(ArrayList<ImageIdModel> createImageModels) {
        this.createImageModels = createImageModels;
    }

    public ProductModel() {
    }

    public String getSaleID() {
        return saleID;
    }

    public void setSaleID(String saleID) {
        this.saleID = saleID;
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

    public String getProductUpdatedAt() {
        return productUpdatedAt;
    }

    public void setProductUpdatedAt(String productUpdatedAt) {
        this.productUpdatedAt = productUpdatedAt;
    }

    public String getProductCreatedAt() {
        return productCreatedAt;
    }

    public void setProductCreatedAt(String productCreatedAt) {
        this.productCreatedAt = productCreatedAt;
    }

    public ArrayList<String> getImage() {
        return images;
    }

    public void setImage(ArrayList<String> image) {
        this.images = image;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
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

    public boolean isNew() {
        return is_new;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isNegotiable() {
        return negotiable;
    }

    public void setNegotiable(boolean negotiable) {
        this.negotiable = negotiable;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isDisplayImage() {
        return displayImage;
    }

    public void setDisplayImage(boolean displayImage) {
        this.displayImage = displayImage;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getSaleName() {
        return saleName;
    }

    public void setSaleName(String saleName) {
        this.saleName = saleName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPostalcodes() {
        return postalcodes;
    }

    public void setPostalcodes(String postalcodes) {
        this.postalcodes = postalcodes;
    }

    public String getSaleType() {
        return saleType;
    }

    public void setSaleType(String saleType) {
        this.saleType = saleType;
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

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public boolean isSale_active() {
        return sale_active;
    }

    public void setSale_active(boolean sale_active) {
        this.sale_active = sale_active;
    }

    public String getCummalative_rating() {
        return cummalative_rating;
    }

    public void setCummalative_rating(String cummalative_rating) {
        this.cummalative_rating = cummalative_rating;
    }

    public String getOffers() {
        return offers;
    }

    public void setOffers(String offers) {
        this.offers = offers;
    }

    public boolean is_bookmark() {
        return is_bookmark;
    }

    public void setIs_bookmark(boolean is_bookmark) {
        this.is_bookmark = is_bookmark;
    }

    public String getReviews_count() {
        return reviews_count;
    }

    public void setReviews_count(String reviews_count) {
        this.reviews_count = reviews_count;
    }

    public String getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(String last_updated) {
        this.last_updated = last_updated;
    }

    public boolean is_product_new() {
        return is_product_new;
    }

    public void setIs_product_new(boolean is_product_new) {
        this.is_product_new = is_product_new;
    }

    public String getItem_display_Image() {
        return item_display_Image;
    }

    public void setItem_display_Image(String item_display_Image) {
        this.item_display_Image = item_display_Image;
    }

    public String getShop_or_sale_name() {
        return shop_or_sale_name;
    }

    public void setShop_or_sale_name(String shop_or_sale_name) {
        this.shop_or_sale_name = shop_or_sale_name;
    }

    public String getSALE_ID() {
        return SALE_ID;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSubhrub() {
        return subhrub;
    }

    public void setSubhrub(String subhrub) {
        this.subhrub = subhrub;
    }

    public String getSaleCreatedAt() {
        return saleCreatedAt;
    }

    public void setSaleCreatedAt(String saleCreatedAt) {
        this.saleCreatedAt = saleCreatedAt;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public boolean toObject(String jsonObject) {

        Log.d("VALUESSS", "SHIPPINGCOST--" + jsonObject.toString());
        try {
            JSONObject json = new JSONObject(jsonObject);
            if (json.has(SALE_ID)) saleID = json.getString(SALE_ID);
            if (json.has(ID)) id = json.getString(ID);
            if (json.has(NAME)) name = json.getString(NAME);
            if (json.has(SALE_PRICE)) salePrice = json.getString(SALE_PRICE);
            if (json.has(RETAIL_PRICE)) retailPrice = json.getString(RETAIL_PRICE);
            if (json.has(DESCRIPTION)) description = json.getString(DESCRIPTION);
            if (json.has(START_TIME)) startTime = json.getString(START_TIME);
            if (json.has(END_TIME)) endTime = json.getString(END_TIME);
            if (json.has(SALE_NAME)) saleName = json.getString(SALE_NAME);
            if (json.has(START_DATE)) startDate = json.getString(START_DATE);
            if (json.has(END_DATE)) endDate = json.getString(END_DATE);
            if (json.has(POSTALCODES)) postalcodes = json.getString(POSTALCODES);
            if (json.has(SALE_TYPE)) saleType = json.getString(SALE_TYPE);
            if (json.has(LATITUDE)) latitude = json.getString(LATITUDE);
            if (json.has(LONGITUDE)) longitude = json.getString(LONGITUDE);
            if (json.has(SELLER_NAME)) sellerName = json.getString(SELLER_NAME);
            if (json.has(SELLER_PHONE)) seller_phone = json.getString(SELLER_PHONE);
            if (json.has(USER_ID)) user_id = json.getString(USER_ID);
            if (json.has(SELLER_ID)) seller_id = json.getString(SELLER_ID);
            if (json.has(PRODUCT_UPDATED_AT)) productUpdatedAt = json.getString(PRODUCT_UPDATED_AT);
            if (json.has(PRODUCT_CREATED_AT)) productCreatedAt = json.getString(PRODUCT_CREATED_AT);
            if (json.has(SALE_CREATED_AT)) saleCreatedAt = json.getString(SALE_CREATED_AT);
            if (json.has(SHIPPING_DELIVERY_DATE))
                shipping_delivery_date = json.getString(SHIPPING_DELIVERY_DATE);

            //changes
            if (json.has(LOCAL_SHIPPING_COST))
                local_shipping_cost = json.getString(LOCAL_SHIPPING_COST);
            if (json.has(INTL_SHIPPING_COST)) {
                intl_shipping_cost = json.getString(INTL_SHIPPING_COST);
                Log.d("VALUESSS", "SHIPPINGCOST-ssss-" + intl_shipping_cost);
            }
            if (json.has(TIME_TO_DELIVER)) time_to_deliver = json.getString(TIME_TO_DELIVER);
//changes


            if (json.has(SALE_UPDATED_AT)) productUpdatedAt = json.getString(SALE_UPDATED_AT);
            if (json.has(VIEWS)) views = json.getString(VIEWS);
            if (json.has(OFFERS)) offers = json.getString(OFFERS);
            if (json.has(CUMULATIVE_RATING)) cummalative_rating = json.getString(CUMULATIVE_RATING);
            if (json.has(REVIEWS_COUNT)) reviews_count = json.getString(REVIEWS_COUNT);
            if (json.has(LAST_UPDATED)) last_updated = json.getString(LAST_UPDATED);
            if (json.has(IMAGE)) {
                item_display_Image = json.getString(IMAGE);
            }
            if (json.has(SHOP_NAME)) {
                shop_or_sale_name = json.getString(SHOP_NAME);
            }
            if (json.has(SELLER_IMAGE)) {
                seller_image = json.getString(SELLER_IMAGE);
            }
            if (json.has(ADDRESS)) {
                address = json.getString(ADDRESS);
            }
            if (json.has(CITY)) {
                city = json.getString(CITY);
            }
            if (json.has(SUBHURB)) {
                subhrub = json.getString(SUBHURB);
            }

            if (json.has(CURRENCY)) {
                currency = json.getString(CURRENCY);
            }


            if (json.has(PRODUCT_IMAGE)) {
                JSONArray imageArray = new JSONArray();
                imageArray = json.getJSONArray(PRODUCT_IMAGE);
                images.clear();
                createImageModels.clear();
                for (int i = 0; i < imageArray.length(); i++) {
                    JSONObject obj = (JSONObject) imageArray.get(i);
                    ImageIdModel imageIdModel = new ImageIdModel();
                    imageIdModel.toObject(obj.toString());
                    createImageModels.add(imageIdModel);
                    if (obj.has(LINK)) {
                        images.add(obj.getString(LINK));
                    }
                    /*   images.add(imageArray.getString(i));*/
                }

            }
            if (json.has(PRODUCT_IMAGES)) {
                JSONArray imageArray = new JSONArray();
                imageArray = json.getJSONArray(PRODUCT_IMAGES);
                images.clear();
                for (int i = 0; i < imageArray.length(); i++) {
                    images.add(imageArray.getString(i));
                }
            }
           /* if(json.has(PRODUCT_IMAGES)) {
                JSONArray imageArray = new JSONArray();
                imageArray = json.getJSONArray(PRODUCT_IMAGES);
                images.clear();
                for (int i = 0; i < imageArray.length(); i++) {
                    images.add(imageArray.getString(i));
                }
            }*/


            if (json.has(PRODUCT_CATEGORY)) productCategory = json.getString(PRODUCT_CATEGORY);
            int temp = 0;
            try {
                temp = json.getInt(IS_SHIPABLE);
            } catch (Exception e) {
                temp = 0;
            }
            if (temp > 0) {
                shipable = true;
            } else {
                shipable = false;
            }
            temp = 0;
            try {
                temp = json.getInt(IS_PICKUP);
            } catch (Exception e) {
                temp = 0;
            }
            if (temp > 0) {
                pickup = true;
            } else {
                pickup = false;
            }
            temp = 0;
            try {
                temp = json.getInt(IS_AVAILABLE);
            } catch (Exception e) {
                temp = 0;
            }
            if (temp > 0) {
                available = true;
            } else {
                available = false;
            }
            temp = 0;
            try {
                temp = json.getInt(IS_ACTIVE);
            } catch (Exception e) {
                temp = 0;
            }
            if (temp > 0) {
                active = true;
            } else {
                active = false;
            }
            temp = 0;
            try {
                temp = json.getInt(IS_NEGOTIABLE);
            } catch (Exception e) {
                temp = 0;
            }
            if (temp > 0) {
                negotiable = true;
            } else {
                negotiable = false;
            }
            temp = 0;
            try {
                temp = json.getInt(IS_NEW);
            } catch (Exception e) {
                temp = 0;
            }
            if (temp > 0) {
                is_new = true;
            } else {
                is_new = false;
            }
            temp = 0;
            try {
                temp = json.getInt(IS_BOOKAMARK);
            } catch (Exception e) {
                temp = 0;
            }
            if (temp > 0) {
                is_bookmark = true;
            } else {
                is_bookmark = false;
            }
            temp = 0;
            try {
                temp = json.getInt(IS_PRODUCT_NEW);
            } catch (Exception e) {
                temp = 0;
            }
            if (temp > 0) {
                is_product_new = true;
            } else {
                is_product_new = false;
            }

            temp = 0;
            try {
                temp = json.getInt(IS_FEATURE_PRODUCT);
            } catch (Exception e) {
                temp = 0;
            }
            if (temp > 0) {
                is_featured = true;
            } else {
                is_featured = false;
            }

            temp = 0;
            try {
                temp = json.getInt(IS_NEW_FLAG);
            } catch (Exception e) {
                temp = 0;
            }
            if (temp > 0) {
                is_new = true;
            } else {
                is_new = false;
            }
            temp = 0;


            //changes
            try {
                temp = json.getInt(IS_INTL_SHIPPING);
            } catch (Exception e) {
                temp = 0;
            }
            if (temp > 0) {
                is_intl_shipping = true;
            } else {
                is_intl_shipping = false;
            }
            temp = 0;
            try {
                temp = json.getInt(SHIPPING_FOC);
            } catch (Exception e) {
                temp = 0;
            }
            if (temp > 0) {
                shipping_foc = true;
            } else {
                shipping_foc = false;
            }
            temp = 0;

            try {
                temp = json.getInt(HIDE_ITEM_PRICE);

            } catch (Exception e) {
                temp = 0;
            }
            if (temp > 0) {
                hide_item_price = true;
            } else {
                hide_item_price = false;
            }
            temp = 0;
            //ffff
            if (json.has(IS_DISPLAY_IMAGE)) try {
                temp = json.getInt(IS_DISPLAY_IMAGE);
            } catch (Exception e) {
                temp = 0;
            }
            if (temp > 0) {
                displayImage = true;
            } else {
                displayImage = false;
            }
            if (json.has(IS_SALE_ACTIVE)) try {
                temp = json.getInt(IS_SALE_ACTIVE);
            } catch (Exception e) {
                temp = 0;
            }
            if (temp > 0) {
                sale_active = true;
            } else {
                sale_active = false;
            }
            return true;
        } catch (Exception ex) {

            Log.d(TAG, "Json Exception : " + ex);
        }
        return false;
    }

    @Override
    public String toString() {
        String returnString = null;
        Log.d("VALUESSS", "SHIPPINGCOST 0000");
        try {

            JSONObject jsonMain = new JSONObject();
            jsonMain.put(SALE_ID, saleID);
            jsonMain.put(ID, id);
            jsonMain.put(CURRENCY, currency);
            jsonMain.put(NAME, name);
            jsonMain.put(START_TIME, name);
            jsonMain.put(END_TIME, name);
            jsonMain.put(START_DATE, name);
            jsonMain.put(END_DATE, name);
            jsonMain.put(POSTALCODES, name);
            jsonMain.put(SALE_TYPE, name);
            jsonMain.put(SALE_NAME, name);
            jsonMain.put(SELLER_NAME, name);
            jsonMain.put(LATITUDE, name);
            jsonMain.put(LONGITUDE, name);
            jsonMain.put(SALE_PRICE, salePrice);
            jsonMain.put(RETAIL_PRICE, retailPrice);
            jsonMain.put(DESCRIPTION, description);
            jsonMain.put(PRODUCT_UPDATED_AT, productUpdatedAt);
            jsonMain.put(PRODUCT_CREATED_AT, productCreatedAt);
            jsonMain.put(PRODUCT_CATEGORY, productCategory);


            jsonMain.put(SHIPPING_FOC, shipping_foc);
            jsonMain.put(LOCAL_SHIPPING_COST, local_shipping_cost);
            jsonMain.put(IS_INTL_SHIPPING, is_intl_shipping);
            jsonMain.put(INTL_SHIPPING_COST, intl_shipping_cost);
            jsonMain.put(TIME_TO_DELIVER, time_to_deliver);
            jsonMain.put(HIDE_ITEM_PRICE, hide_item_price);

            JSONArray imageArray = new JSONArray();
            if (images != null) {
                for (int i = 0; i < images.size(); i++) {
                    imageArray.put(images.get(i));
                }
            }
            jsonMain.put(PRODUCT_IMAGE, imageArray);
            String temp = "0";
            if (shipable) {
                temp = "1";
            } else {
                temp = "0";
            }
            jsonMain.put(IS_SHIPABLE, temp);
            temp = "0";
            if (pickup) {
                temp = "1";
            } else {
                temp = "0";
            }
            jsonMain.put(IS_PICKUP, temp);
            temp = "0";
            if (available) {
                temp = "1";
            } else {
                temp = "0";
            }
            jsonMain.put(IS_AVAILABLE, temp);
            temp = "0";
            if (active) {
                temp = "1";
            } else {
                temp = "0";
            }
            jsonMain.put(IS_ACTIVE, temp);
            temp = "0";
            if (negotiable) {
                temp = "1";
            } else {
                temp = "0";
            }
            jsonMain.put(IS_NEGOTIABLE, temp);
            temp = "0";
            if (displayImage) {
                temp = "1";
            } else {
                temp = "0";
            }
            jsonMain.put(IS_DISPLAY_IMAGE, temp);
            temp = "0";


            if (sale_active) {
                temp = "1";
            } else {
                temp = "0";
            }
            jsonMain.put(IS_SALE_ACTIVE, temp);
            returnString = jsonMain.toString();
        } catch (Exception ex) {
            Log.d(TAG, " To String Exception : " + ex);
        }
        return returnString;
    }
}
