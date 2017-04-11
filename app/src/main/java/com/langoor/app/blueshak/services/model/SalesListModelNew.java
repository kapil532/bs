package com.langoor.app.blueshak.services.model;

import android.util.Log;

import com.langoor.app.blueshak.AppController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SalesListModelNew implements Serializable {
    private final String TAG = "SalesListModelNew";
    private final String
            TOTAL = "total",
            PER_PAGE = "per_page",
            CURRENT_PAGE = "current_page",
            LAST_PAGE = "last_page",
            NEXT_PAGE_URL = "next_page_url",
            PREV_PAGE_URL = "prev_page_url",
            FROM = "from",
            TO = "to",
            DATA = "data";
    String next_page_url = null,prev_page_url = null;
    int total=0,from = 0,to = 0,last_page = 0,per_page=0,current_page;
    List<SalesModel> salesList = new ArrayList<SalesModel>();


    public String getNext_page_url() {
        return next_page_url;
    }

    public void setNext_page_url(String next_page_url) {
        this.next_page_url = next_page_url;
    }

    public String getPrev_page_url() {
        return prev_page_url;
    }

    public void setPrev_page_url(String prev_page_url) {
        this.prev_page_url = prev_page_url;
    }

    public List<SalesModel> getSalesList() {
        return salesList;
    }

    public void setSalesList(List<SalesModel> salesList) {
        this.salesList = salesList;
    }



    public String getTAG() {
        return TAG;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }

    public int getPer_page() {
        return per_page;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public boolean toObject(String jsonObjectString){
        try{
            JSONObject json = new JSONObject(jsonObjectString);
            if(json.has(TOTAL))total=json.getInt(TOTAL);
            if(json.has(PER_PAGE))per_page=json.getInt(PER_PAGE);
            if(json.has(CURRENT_PAGE))current_page=json.getInt(CURRENT_PAGE);
            if(json.has(LAST_PAGE))last_page=json.getInt(LAST_PAGE);
            if(json.has(NEXT_PAGE_URL))next_page_url=json.getString(NEXT_PAGE_URL);
            if(json.has(PREV_PAGE_URL))prev_page_url=json.getString(PREV_PAGE_URL);
            if(json.has(FROM))from=json.getInt(FROM);
            if(json.has(TO))to=json.getInt(TO);

            List<SalesModel> productModelArrayList = new ArrayList<SalesModel>();
            JSONArray jsonArray = json.getJSONArray(DATA);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                SalesModel salesModel = new SalesModel();
                salesModel.toObject(jsonObject.toString());
                productModelArrayList.add(salesModel);
            }
            salesList.addAll(productModelArrayList);
            productModelArrayList.clear();
            return true;

        }catch(Exception ex){
            Log.d(TAG, "Json Exception : " + ex);
           }
        return false;
    }

    @Override
    public String toString(){
        String returnString = null;
        try{
            JSONObject jsonMain = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            List<SalesModel> productModelList = salesList;
            for(int i=0;i<productModelList.size();i++){
                jsonArray.put(new JSONObject(productModelList.get(i).toString()));
            }
            jsonMain.put(DATA,jsonArray);
            returnString = jsonMain.toString();
        }
        catch (Exception ex){Log.d(TAG," To String Exception : "+ex);
           }
        return returnString;
    }

}
