package com.blueshak.app.blueshak.seller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lsingh013 on 20/05/18.
 */

public class SellerData implements Serializable{

    @SerializedName("shop_id")
    @Expose
    private String shopId;
    @SerializedName("shop_name")
    @Expose
    private String shopName;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("seller_id")
    @Expose
    private String sellerId;
    @SerializedName("sale_address")
    @Expose
    private String saleAddress;
    @SerializedName("seller_name")
    @Expose
    private String sellerName;
    @SerializedName("seller_image")
    @Expose
    private String sellerImage;
    @SerializedName("is_featured")
    @Expose
    private String isFeatured;
    @SerializedName("distance_away")
    @Expose
    private String distanceAway;
    @SerializedName("listed_date")
    @Expose
    private String listedDate;
    @SerializedName("is_bookmarked")
    @Expose
    private Integer isBookmarked;
    @SerializedName("shop_items_count")
    @Expose
    private Integer shopItemsCount;
    @SerializedName("images")
    @Expose
    private List<SellerImage> images = null;
    @SerializedName("reviews_count")
    @Expose
    private Integer reviewsCount;
    @SerializedName("avg_seller_rating")
    @Expose
    private Integer avgSellerRating;
    @SerializedName("likes_count")
    @Expose
    private Integer likesCount;
    @SerializedName("is_user_liked_seller")
    @Expose
    private Integer isUserLikedSeller;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
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

    public String getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(String isFeatured) {
        this.isFeatured = isFeatured;
    }

    public String getDistanceAway() {
        return distanceAway;
    }

    public void setDistanceAway(String distanceAway) {
        this.distanceAway = distanceAway;
    }

    public String getListedDate() {
        return listedDate;
    }

    public void setListedDate(String listedDate) {
        this.listedDate = listedDate;
    }

    public Integer getIsBookmarked() {
        return isBookmarked;
    }

    public void setIsBookmarked(Integer isBookmarked) {
        this.isBookmarked = isBookmarked;
    }

    public Integer getShopItemsCount() {
        return shopItemsCount;
    }

    public void setShopItemsCount(Integer shopItemsCount) {
        this.shopItemsCount = shopItemsCount;
    }

    public List<SellerImage> getImages() {
        return images;
    }

    public void setImages(List<SellerImage> images) {
        this.images = images;
    }

    public Integer getReviewsCount() {
        return reviewsCount;
    }

    public void setReviewsCount(Integer reviewsCount) {
        this.reviewsCount = reviewsCount;
    }

    public Integer getAvgSellerRating() {
        return avgSellerRating;
    }

    public void setAvgSellerRating(Integer avgSellerRating) {
        this.avgSellerRating = avgSellerRating;
    }

    public Integer getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public Integer getIsUserLikedSeller() {
        return isUserLikedSeller;
    }

    public void setIsUserLikedSeller(Integer isUserLikedSeller) {
        this.isUserLikedSeller = isUserLikedSeller;
    }
}
