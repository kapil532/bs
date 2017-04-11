package com.langoor.app.blueshak.Messaging.data;

import java.io.Serializable;

public class User implements Serializable {
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    String bs_id=null,email=null,name=null,profileImageUrl=null,number=null,product_url=null,product_name=null,
            price=null,currency=null,product_id=null,active_tab=null,conversation_id=null,sale_id=null;

    public boolean is_sale() {
        return is_sale;
    }

    public void setIs_sale(boolean is_sale) {
        this.is_sale = is_sale;
    }

    public String getSale_id() {
        return sale_id;
    }

    public void setSale_id(String sale_id) {
        this.sale_id = sale_id;
    }

    boolean is_sale=false;

    public String getActive_tab() {
        return active_tab;
    }

    public String getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(String conversation_id) {
        this.conversation_id = conversation_id;
    }

    public void setActive_tab(String active_tab) {
        this.active_tab = active_tab;
    }

    public User() {
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public User(String name, String email, String number, String bs_id, String profileImageUrl){
        this.name = name;
        this.email = email;
        this.number=number;
        this.bs_id=bs_id;
        this.profileImageUrl=profileImageUrl;

    }

    public String getProduct_url() {
        return product_url;
    }

    public void setProduct_url(String product_url) {
        this.product_url = product_url;
    }

    public User(String name, String email, String number, String bs_id, String profileImageUrl, String product_url,String product_name,String price){
        this.name = name;
        this.email = email;
        this.number=number;
        this.bs_id=bs_id;
        this.profileImageUrl=profileImageUrl;
        this.product_url=product_url;
        this.product_name=product_name;
        this.price=price;


    }

    public String getBs_id() {
        return bs_id;
    }

    public void setBs_id(String bs_id) {
        this.bs_id = bs_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
