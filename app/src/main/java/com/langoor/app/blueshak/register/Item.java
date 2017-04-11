package com.langoor.app.blueshak.register;

import java.io.Serializable;

/**
 * Created by admin on 7/20/2016.
 */
public class Item implements Serializable {

    private String key;
    private String value;
    private String phoneNumber;

    public Item(String key, String value,String phoneNumber) {
        this.key = key;
        this.value = value;
        this.phoneNumber=phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}