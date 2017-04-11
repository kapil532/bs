package com.langoor.app.blueshak.services.model;

import android.util.Log;

import com.langoor.app.blueshak.AppController;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SalesListModel implements Serializable {
    private final String TAG = "SalesListModel";
    private final String
            GARAGE_SALES_LIST = "garage_sales_list";
    private final String
            MULTIPLE_SALES_LIST = "multiple_sales_list";
    private final String
            SALES_LIST = "sales_list";

    List<SalesModel> salesList = new ArrayList<SalesModel>();
    List<SalesModel> garage_sales_list = new ArrayList<SalesModel>();
    List<SalesModel> multiple_sales_list = new ArrayList<SalesModel>();

    public List<SalesModel> getMultiple_sales_list() {
        return multiple_sales_list;
    }

    public void setMultiple_sales_list(List<SalesModel> multiple_sales_list) {
        this.multiple_sales_list = multiple_sales_list;
    }

    public List<SalesModel> getGarage_sales_list() {
        return garage_sales_list;
    }

    public void setGarage_sales_list(List<SalesModel> garage_sales_list) {
        this.garage_sales_list = garage_sales_list;
    }

    public SalesListModel(){}

    public List<SalesModel> getSalesList() {
        return salesList;
    }

    public void setSalesList(List<SalesModel> salesList) {
        this.salesList = salesList;
    }

    public boolean toObject(String jsonObjectString){
        try{
            JSONObject json = new JSONObject(jsonObjectString);
            List<SalesModel> salesModelList = new ArrayList<SalesModel>();
            JSONArray jsonArray = json.getJSONArray(SALES_LIST);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                SalesModel salesModel = new SalesModel();
                salesModel.toObject(jsonObject.toString());
                salesModelList.add(salesModel);
            }
            for(SalesModel salesModel:salesModelList){
                if(salesModel.getSaleType().equalsIgnoreCase("multiple_item"))
                    multiple_sales_list.add(salesModel);
                else
                    garage_sales_list.add(salesModel);

            }
            salesList.addAll(salesModelList);
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
            List<SalesModel> salesModelList = salesList;
            for(int i=0;i<salesModelList.size();i++){
                jsonArray.put(new JSONObject(salesModelList.get(i).toString()));
            }
            jsonMain.put(SALES_LIST,jsonArray);
            returnString = jsonMain.toString();
        }
        catch (Exception ex){Log.d(TAG," To String Exception : "+ex);
           }
        return returnString;
    }

}
