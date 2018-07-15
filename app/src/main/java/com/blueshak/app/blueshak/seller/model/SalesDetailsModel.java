package com.blueshak.app.blueshak.seller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lsingh013 on 26/05/18.
 */

public class SalesDetailsModel implements Serializable{

    @SerializedName("sale_id")
    @Expose
    private String saleId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("utc_start_time")
    @Expose
    private String utcStartTime;
    @SerializedName("utc_end_time")
    @Expose
    private String utcEndTime;
    @SerializedName("utc_start_date")
    @Expose
    private String utcStartDate;
    @SerializedName("postcodes")
    @Expose
    private String postcodes;
    @SerializedName("sale_type")
    @Expose
    private String saleType;
    @SerializedName("suburb")
    @Expose
    private String suburb;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("seller_id")
    @Expose
    private String sellerId;
    @SerializedName("sale_address")
    @Expose
    private String saleAddress;
    @SerializedName("is_sale_active")
    @Expose
    private String isSaleActive;
    @SerializedName("sale_created_at")
    @Expose
    private String saleCreatedAt;
    @SerializedName("seller_name")
    @Expose
    private String sellerName;
    @SerializedName("seller_image")
    @Expose
    private String sellerImage;
    @SerializedName("sale_state")
    @Expose
    private String saleState;
    @SerializedName("distance_away")
    @Expose
    private String distanceAway;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("end_time")
    @Expose
    private String endTime;
    @SerializedName("listed_date")
    @Expose
    private String listedDate;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("is_own_sale")
    @Expose
    private Integer isOwnSale;
    @SerializedName("reviews_count")
    @Expose
    private Integer reviewsCount;
    @SerializedName("avg_seller_rating")
    @Expose
    private String avgSellerRating;
    @SerializedName("sale_items_count")
    @Expose
    private Integer saleItemsCount;
    @SerializedName("is_bookmarked")
    @Expose
    private Integer isBookmarked;
    @SerializedName("products")
    @Expose
    private List<SalesDetailsProduct> products = null;
    @SerializedName("images")
    @Expose
    private List<SalesDetailsImages> images = null;

    public String getSaleId() {
        return saleId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getUtcStartTime() {
        return utcStartTime;
    }

    public void setUtcStartTime(String utcStartTime) {
        this.utcStartTime = utcStartTime;
    }

    public String getUtcEndTime() {
        return utcEndTime;
    }

    public void setUtcEndTime(String utcEndTime) {
        this.utcEndTime = utcEndTime;
    }

    public String getUtcStartDate() {
        return utcStartDate;
    }

    public void setUtcStartDate(String utcStartDate) {
        this.utcStartDate = utcStartDate;
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

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSaleAddress() {
        return saleAddress;
    }

    public void setSaleAddress(String saleAddress) {
        this.saleAddress = saleAddress;
    }

    public String getIsSaleActive() {
        return isSaleActive;
    }

    public void setIsSaleActive(String isSaleActive) {
        this.isSaleActive = isSaleActive;
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

    public String getSellerImage() {
        return sellerImage;
    }

    public void setSellerImage(String sellerImage) {
        this.sellerImage = sellerImage;
    }

    public String getSaleState() {
        return saleState;
    }

    public void setSaleState(String saleState) {
        this.saleState = saleState;
    }

    public String getDistanceAway() {
        return distanceAway;
    }

    public void setDistanceAway(String distanceAway) {
        this.distanceAway = distanceAway;
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

    public String getListedDate() {
        return listedDate;
    }

    public void setListedDate(String listedDate) {
        this.listedDate = listedDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Integer getIsOwnSale() {
        return isOwnSale;
    }

    public void setIsOwnSale(Integer isOwnSale) {
        this.isOwnSale = isOwnSale;
    }

    public Integer getReviewsCount() {
        return reviewsCount;
    }

    public void setReviewsCount(Integer reviewsCount) {
        this.reviewsCount = reviewsCount;
    }

    public String getAvgSellerRating() {
        return avgSellerRating;
    }

    public void setAvgSellerRating(String avgSellerRating) {
        this.avgSellerRating = avgSellerRating;
    }

    public Integer getSaleItemsCount() {
        return saleItemsCount;
    }

    public void setSaleItemsCount(Integer saleItemsCount) {
        this.saleItemsCount = saleItemsCount;
    }

    public Integer getIsBookmarked() {
        return isBookmarked;
    }

    public void setIsBookmarked(Integer isBookmarked) {
        this.isBookmarked = isBookmarked;
    }

    public List<SalesDetailsProduct> getProducts() {
        return products;
    }

    public void setProducts(List<SalesDetailsProduct> products) {
        this.products = products;
    }

    public List<SalesDetailsImages> getImages() {
        return images;
    }

    public void setImages(List<SalesDetailsImages> images) {
        this.images = images;
    }

}
