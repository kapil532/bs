package com.langoor.app.blueshak.services.model;

import android.util.Log;

import com.langoor.app.blueshak.AppController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchListModel {
    private final String TAG = "SearchListModel";
    private final String
            SEARCH_LIST = "search_list";

    List<ProductModel> productModelList = new ArrayList<ProductModel>();

    public SearchListModel(){}

    public List<ProductModel> getProductsList() {
        return productModelList;
    }

    public void setProductsList(List<ProductModel> productsList) {
        this.productModelList = productsList;
    }

    public boolean toObject(String jsonObjectString){
        try{
            JSONObject json = new JSONObject(jsonObjectString);
            List<ProductModel> productModelListLocal = new ArrayList<ProductModel>();
            JSONArray jsonArray = json.getJSONArray(SEARCH_LIST);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ProductModel productModel = new ProductModel();
                productModel.toObject(jsonObject.toString());
                productModelListLocal.add(productModel);
            }
            productModelList.addAll(productModelListLocal); productModelListLocal.clear();
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
            List<ProductModel> productModels = productModelList;
            for(int i=0;i<productModels.size();i++){
                jsonArray.put(productModels.get(i).toString());
            }
            jsonMain.put(SEARCH_LIST,jsonArray);
            returnString = jsonMain.toString();
        }
        catch (Exception ex){Log.d(TAG," To String Exception : "+ex);
           }
        return returnString;
    }
}
