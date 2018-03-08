package com.blueshak.app.blueshak.home.model;

/**
 * Created by lalbabu on 30/01/18.
 */

public class FeatureItemsModel {

    private String to;

    private String next_page_url;

    private int last_page;

    private String total;

    private String per_page;

    private FeatureItemData[] data;

    private String from;

    private String prev_page_url;

    private int current_page;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getNext_page_url() {
        return next_page_url;
    }

    public void setNext_page_url(String next_page_url) {
        this.next_page_url = next_page_url;
    }

    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPer_page() {
        return per_page;
    }

    public void setPer_page(String per_page) {
        this.per_page = per_page;
    }

    public FeatureItemData[] getData() {
        return data;
    }

    public void setData(FeatureItemData[] data) {
        this.data = data;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getPrev_page_url() {
        return prev_page_url;
    }

    public void setPrev_page_url(String prev_page_url) {
        this.prev_page_url = prev_page_url;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    @Override
    public String toString() {
        return "ClassPojo [to = " + to + ", next_page_url = " + next_page_url + ", feature_last_page = " + last_page + ", total = "
                + total + ", per_page = " + per_page + ", data = " + data + ", from = " + from + ", prev_page_url = "
                + prev_page_url + ", current_page = " + current_page + "]";
    }
}
