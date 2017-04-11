package com.langoor.app.blueshak.services.model;

import android.util.Log;

import com.langoor.app.blueshak.AppController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SimilarProductsModel {

    private final String TAG = "SimilarProductsModel";
    private final String
            SIMILAR_PRODUCTS = "similar_list";
    private final String
            SELLER_OTHERS_PRODUCTS = "seller_item_list";

    List<ProductModel> productModelList = new ArrayList<ProductModel>();

    public SimilarProductsModel(){}

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
            if(json.has(SIMILAR_PRODUCTS)){
                JSONArray jsonArray = json.getJSONArray(SIMILAR_PRODUCTS);
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    ProductModel productModel = new ProductModel();
                    productModel.toObject(jsonObject.toString());
                    productModelListLocal.add(productModel);
                }
                productModelList.addAll(productModelListLocal); productModelListLocal.clear();
                return true;
            }else if(json.has(SELLER_OTHERS_PRODUCTS)){
                JSONArray jsonArray = json.getJSONArray(SELLER_OTHERS_PRODUCTS);
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    ProductModel productModel = new ProductModel();
                    productModel.toObject(jsonObject.toString());
                    productModelListLocal.add(productModel);
                }
                productModelList.addAll(productModelListLocal); productModelListLocal.clear();
                return true;
            }
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
            List<ProductModel> productModels = productModelList;
            for(int i=0;i<productModels.size();i++){
                jsonArray.put(productModels.get(i).toString());
            }
            jsonMain.put(SIMILAR_PRODUCTS,jsonArray);
            returnString = jsonMain.toString();
        }
        catch (Exception ex){Log.d(TAG," To String Exception : "+ex);
           }
        return returnString;
    }

}
