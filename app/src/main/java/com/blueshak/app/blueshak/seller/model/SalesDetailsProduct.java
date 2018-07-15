package com.blueshak.app.blueshak.seller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by lsingh013 on 26/05/18.
 */

public class SalesDetailsProduct implements Serializable {

    @SerializedName("sale_id")
    @Expose
    private String saleId;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("sale_price")
    @Expose
    private String salePrice;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("is_shippable")
    @Expose
    private String isShippable;
    @SerializedName("is_pickup")
    @Expose
    private String isPickup;
    @SerializedName("is_available")
    @Expose
    private String isAvailable;
    @SerializedName("is_negotiable")
    @Expose
    private String isNegotiable;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("suburb")
    @Expose
    private String suburb;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("product_image_id")
    @Expose
    private String productImageId;
    @SerializedName("accepts_paypal")
    @Expose
    private String acceptsPaypal;
    @SerializedName("is_featured")
    @Expose
    private String isFeatured;
    @SerializedName("is_bookmarked")
    @Expose
    private Integer isBookmarked;

    public String getSaleId() {
        return saleId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getIsShippable() {
        return isShippable;
    }

    public void setIsShippable(String isShippable) {
        this.isShippable = isShippable;
    }

    public String getIsPickup() {
        return isPickup;
    }

    public void setIsPickup(String isPickup) {
        this.isPickup = isPickup;
    }

    public String getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(String isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getIsNegotiable() {
        return isNegotiable;
    }

    public void setIsNegotiable(String isNegotiable) {
        this.isNegotiable = isNegotiable;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProductImageId() {
        return productImageId;
    }

    public void setProductImageId(String productImageId) {
        this.productImageId = productImageId;
    }

    public String getAcceptsPaypal() {
        return acceptsPaypal;
    }

    public void setAcceptsPaypal(String acceptsPaypal) {
        this.acceptsPaypal = acceptsPaypal;
    }

    public String getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(String isFeatured) {
        this.isFeatured = isFeatured;
    }

    public Integer getIsBookmarked() {
        return isBookmarked;
    }

    public void setIsBookmarked(Integer isBookmarked) {
        this.isBookmarked = isBookmarked;
    }
}
