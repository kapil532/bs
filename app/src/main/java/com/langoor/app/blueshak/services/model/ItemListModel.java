package com.langoor.app.blueshak.services.model;

import android.util.Log;

import com.langoor.app.blueshak.AppController;
import com.langoor.app.blueshak.photos_add.PhotosAddFragmentMain;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ItemListModel implements Serializable {
    private final String TAG = "ItemListModel";
     private final String
            PRODUCT_LIST = "product_list";

    List<ProductModel> product_list = new ArrayList<ProductModel>();


    public ItemListModel(){}


    public List<ProductModel> getProduct_list() {
        return product_list;
    }

    public void setProduct_list(List<ProductModel> product_list) {
        this.product_list = product_list;
    }

    public boolean toObject(String jsonObjectString){
        try{
            JSONObject json = new JSONObject(jsonObjectString);
            List<ProductModel> salesModelList = new ArrayList<ProductModel>();
            JSONArray jsonArray = json.getJSONArray(PRODUCT_LIST);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ProductModel productModel = new ProductModel();
                productModel.toObject(jsonObject.toString());
                salesModelList.add(productModel);
            }
            product_list.addAll(salesModelList);
            salesModelList.clear();
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
            List<ProductModel> salesModelList = product_list;
            for(int i=0;i<salesModelList.size();i++){
                jsonArray.put(new JSONObject(salesModelList.get(i).toString()));
            }
            jsonMain.put(PRODUCT_LIST,jsonArray);
            returnString = jsonMain.toString();
        }
        catch (Exception ex){Log.d(TAG," To String Exception : "+ex);
           }
        return returnString;
    }

}
