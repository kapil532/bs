package com.langoor.app.blueshak.services.model;

import android.util.Log;

import com.langoor.app.blueshak.AppController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BookmarkListModel implements Serializable {
    private final String TAG = "BookmarkListModel";
    private final String
            BOOKMARK_LIST = "bookmarks_list";

    List<ProductModel> productList = new ArrayList<ProductModel>();

    public BookmarkListModel(){}

    public List<ProductModel> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductModel> productList) {
        this.productList = productList;
    }

    public boolean toObject(String jsonObjectString){
        try{
            JSONObject json = new JSONObject(jsonObjectString);
            List<ProductModel> productModelList = new ArrayList<ProductModel>();
            JSONArray jsonArray = json.getJSONArray(BOOKMARK_LIST);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ProductModel productModel = new ProductModel();
                productModel.toObject(jsonObject.toString());
                productModelList.add(productModel);
            }
            productList.addAll(productModelList); productModelList.clear();
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
            List<ProductModel> productModelList = productList;
            for(int i=0;i<productModelList.size();i++){
                jsonArray.put(productModelList.get(i).toString());
            }
            jsonMain.put(BOOKMARK_LIST,jsonArray);
            returnString = jsonMain.toString();
        }
        catch (Exception ex){Log.d(TAG," To String Exception : "+ex);
           }
        return returnString;
    }

}
