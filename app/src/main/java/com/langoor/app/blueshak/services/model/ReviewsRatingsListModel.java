package com.langoor.app.blueshak.services.model;

import android.util.Log;

import com.langoor.app.blueshak.AppController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReviewsRatingsListModel implements Serializable {
    private final String TAG = "ReviewsRatingsListModel";
    private final String
            REVIEWS_RATINGS_LIST = "reviews_list";
    private final String
            CUMULATIVE_RATING = "cummalative_rating";


    String cummalative_rating = null;

    List<ReviewsRatingsModel> reviews_list = new ArrayList<ReviewsRatingsModel>();

    public ReviewsRatingsListModel(){}

    public List<ReviewsRatingsModel> getReviews_list() {
        return reviews_list;
    }

    public void setReviews_list(List<ReviewsRatingsModel> reviews_list) {
        this.reviews_list = reviews_list;
    }

    public String getCummalative_rating() {
        return cummalative_rating;
    }

    public void setCummalative_rating(String cummalative_rating) {
        this.cummalative_rating = cummalative_rating;
    }

    public boolean toObject(String jsonObjectString){
        try{
            JSONObject json = new JSONObject(jsonObjectString);
            if (json.has(CUMULATIVE_RATING)) cummalative_rating = json.getString(CUMULATIVE_RATING);
            List<ReviewsRatingsModel> productModelList = new ArrayList<ReviewsRatingsModel>();
            JSONArray jsonArray = json.getJSONArray(REVIEWS_RATINGS_LIST);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ReviewsRatingsModel productModel = new ReviewsRatingsModel();
                productModel.toObject(jsonObject.toString());
                productModelList.add(productModel);
            }
            reviews_list.addAll(productModelList); productModelList.clear();
            return true;
        }catch(Exception ex){

            Log.d(TAG, "Json Exception : " + ex);}
        return false;
    }

    @Override
    public String toString(){
        String returnString = null;
        try{
            JSONObject jsonMain = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            List<ReviewsRatingsModel> productModelList = reviews_list;
            for(int i=0;i<productModelList.size();i++){
                jsonArray.put(productModelList.get(i).toString());
            }
            jsonMain.put(REVIEWS_RATINGS_LIST,jsonArray);
            returnString = jsonMain.toString();
        }
        catch (Exception ex){Log.d(TAG," To String Exception : "+ex);

        }
        return returnString;
    }

}
