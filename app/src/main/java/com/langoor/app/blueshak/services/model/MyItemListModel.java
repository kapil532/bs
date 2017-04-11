package com.langoor.app.blueshak.services.model;

import android.util.Log;

import com.langoor.app.blueshak.AppController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyItemListModel implements Serializable {
    private final String TAG = "MyItemListModel";
    private final String
            ITEM_LIST = "items_list";

    List<ProductModel> item_list = new ArrayList<ProductModel>();

     public List<ProductModel> getItem_list() {
        return item_list;
    }

    public void setItem_list(List<ProductModel> item_list) {
        this.item_list = item_list;
    }

    public String getTAG() {
        return TAG;
    }


    public boolean toObject(String jsonObjectString){
        try{
            JSONObject json = new JSONObject(jsonObjectString);
            List<ProductModel> productModelArrayList = new ArrayList<ProductModel>();
            JSONArray jsonArray = json.getJSONArray(ITEM_LIST);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ProductModel productModel = new ProductModel();
                productModel.toObject(jsonObject.toString());
                productModelArrayList.add(productModel);
            }
            item_list.addAll(productModelArrayList);
            productModelArrayList.clear();
            return true;

        }catch(Exception ex){
            Log.d(TAG, "Json Exception : " + ex);
           }
        return false;
    }

    public String getITEM_LIST() {
        return ITEM_LIST;
    }

    @Override
    public String toString(){
        String returnString = null;
        try{
            JSONObject jsonMain = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            List<ProductModel> productModelList = item_list;
            for(int i=0;i<productModelList.size();i++){
                jsonArray.put(new JSONObject(productModelList.get(i).toString()));
            }
            jsonMain.put(ITEM_LIST,jsonArray);
            returnString = jsonMain.toString();
        }
        catch (Exception ex){Log.d(TAG," To String Exception : "+ex);
           }
        return returnString;
    }

}
