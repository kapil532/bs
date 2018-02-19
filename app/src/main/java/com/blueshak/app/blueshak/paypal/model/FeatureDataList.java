package com.blueshak.app.blueshak.paypal.model;

/**
 * Created by lsingh013 on 19/02/18.
 */

public class FeatureDataList {
    private String amount;

    private String id;

    private String updated_at;

    private String name;

    private String created_at;

    private String duration_weeks;

    private String formatted_text;

    private String currency;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getDuration_weeks() {
        return duration_weeks;
    }

    public void setDuration_weeks(String duration_weeks) {
        this.duration_weeks = duration_weeks;
    }

    public String getFormatted_text() {
        return formatted_text;
    }

    public void setFormatted_text(String formatted_text) {
        this.formatted_text = formatted_text;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "ClassPojo [amount = " + amount + ", id = " + id + ", updated_at = " + updated_at + ", name = " + name + ", created_at = " + created_at + ", duration_weeks = " + duration_weeks + ", formatted_text = " + formatted_text + ", currency = " + currency + "]";
    }
}
