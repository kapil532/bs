package com.blueshak.app.blueshak.paypal.model;

/**
 * Created by lsingh013 on 19/02/18.
 */

public class FeaturedOptionModel {

    private String status_code;

    private FeatureDataList[] data;

    private String header_text;

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public FeatureDataList[] getData() {
        return data;
    }

    public void setData(FeatureDataList[] data) {
        this.data = data;
    }

    public String getHeader_text() {
        return header_text;
    }

    public void setHeader_text(String header_text) {
        this.header_text = header_text;
    }

    @Override
    public String toString() {
        return "ClassPojo [status_code = " + status_code + ", data = " + data + ", header_text = " + header_text + "]";
    }
}
