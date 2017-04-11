package com.langoor.app.blueshak.services.model;

import android.util.Log;

import com.langoor.app.blueshak.AppController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CurrencyListModel implements Serializable {
    private final String TAG = "CurrencyListModel";
    private final String
            CURRENCY_LIST = "currency_list";

    List<CurrencyModel> currency_list = new ArrayList<CurrencyModel>();

    public CurrencyListModel(){}


    public List<CurrencyModel> getCurrency_list() {
        return currency_list;
    }

    public void setCurrency_list(List<CurrencyModel> currency_list) {
        this.currency_list = currency_list;
    }

    public boolean toObject(String jsonObjectString){
        try{
            JSONObject json = new JSONObject(jsonObjectString);
            List<CurrencyModel> categoryModelList = new ArrayList<CurrencyModel>();
            JSONArray jsonArray = json.getJSONArray(CURRENCY_LIST);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                CurrencyModel currencyModel = new CurrencyModel();
                currencyModel.toObject(jsonObject.toString());
                categoryModelList.add(currencyModel);
            }
            currency_list.addAll(categoryModelList); categoryModelList.clear();
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
            List<CurrencyModel> currencyModelList = currency_list;
            for(int i=0;i<currencyModelList.size();i++){
                jsonArray.put(new JSONObject(currencyModelList.get(i).toString()));
            }
            jsonMain.put(CURRENCY_LIST,jsonArray);
            returnString = jsonMain.toString();
        }
        catch (Exception ex){Log.d(TAG," To String Exception : "+ex);
           }
        return returnString;
    }


}
