package com.langoor.app.blueshak.services.model;

import android.util.Log;

import com.langoor.app.blueshak.AppController;

import org.json.JSONObject;

import java.io.Serializable;


public class ReviewsRatingsModel implements Serializable {
    private final String TAG = "ReviewsRatingsModel";
    private final String
            TITLE = "title";
    private final String COMMENT = "comment";
    private final String SALE_NAME = "sale_name";
    private final String RATING = "rating";
    private final String REVIEWER_NAME = "reviewer_name";



    private final String REVIEWER_USER_ID = "seller_id";
    private final String SELLER_ID="seller_id";
    private final String IS_GARAGE_REVIEW ="is_garage_review";
    private final String SELLER_IMAGE = "seller_image";

   /* &seller_id=44&title=Review&comment=yyuug&is_garage_review=0&rating=4*/
    public ReviewsRatingsModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getSale_name() {
        return sale_name;
    }

    public void setSale_name(String sale_name) {
        this.sale_name = sale_name;
    }

    public String getReviewer_name() {
        return reviewer_name;
    }

    public void setReviewer_name(String reviewer_name) {
        this.reviewer_name = reviewer_name;
    }

    public String getseller_image() {
        return seller_image;
    }

    public void setseller_image(String seller_image) {
        this.seller_image = seller_image;
    }

    public String getReviewer_user_id() {
        return reviewer_user_id;
    }

    public void setReviewer_user_id(String reviewer_user_id) {
        this.reviewer_user_id = reviewer_user_id;
    }

    String
            title = null,
            comment = null,
            sale_name = null,
            rating = null,
            reviewer_name = null,
            seller_image = null,

    reviewer_user_id = null;
    boolean is_garage_review =false;

    public boolean is_garage_review() {
        return is_garage_review;
    }

    public void setIs_garage_review(boolean is_garage_review) {
        this.is_garage_review = is_garage_review;
    }

    public boolean toObject(String jsonObjectString) {

        try {
            JSONObject json = new JSONObject(jsonObjectString);
            if (json.has(TITLE)) title = json.getString(TITLE);
            if (json.has(COMMENT)) comment = json.getString(COMMENT);
            if (json.has(SALE_NAME)) sale_name = json.getString(SALE_NAME);
            if (json.has(RATING)) rating = json.getString(RATING);
            if (json.has(REVIEWER_NAME)) reviewer_name = json.getString(REVIEWER_NAME);
            if (json.has(REVIEWER_USER_ID)) reviewer_user_id = json.getString(REVIEWER_USER_ID);
            if (json.has(SELLER_IMAGE)) seller_image = json.getString(SELLER_IMAGE);
            return true;
        } catch (Exception ex) {


            Log.d(TAG, " To String Exception : " + ex);
        }
        return false;
    }

    @Override
    public String toString() {
        String returnString = null;
        try {
            JSONObject jsonMain = new JSONObject();
            jsonMain.put(TITLE, title);
            jsonMain.put(COMMENT, comment);
            jsonMain.put(REVIEWER_USER_ID, reviewer_user_id);
            jsonMain.put(IS_GARAGE_REVIEW,is_garage_review);
            jsonMain.put(RATING, rating);
            returnString = jsonMain.toString();
        } catch (Exception ex) {
            Log.d(TAG, " To String Exception : " + ex);

        }
        return returnString;
    }
}