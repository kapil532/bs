package com.langoor.app.blueshak.services.model;

import android.util.Log;

import com.langoor.app.blueshak.AppController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PostcodeListModel {
    private final String TAG = "PostcodeListModel";
    private final String
            POSTCODES_LIST = "postcodes_list";

    List<PostcodeModel> postcodeModelList = new ArrayList<PostcodeModel>();

    public PostcodeListModel(){}

    public List<PostcodeModel> getPostcodeModelList() {
        return postcodeModelList;
    }

    public void setPostcodeModelList(List<PostcodeModel> postcodeModelList) {
        this.postcodeModelList = postcodeModelList;
    }

    public String getIdFromSuburb(String suburb){
        if(postcodeModelList!=null&&postcodeModelList.size()>0){
            for(int i=0; i<postcodeModelList.size(); i++){
                String name = postcodeModelList.get(i).getSuburb();
                if(name.equalsIgnoreCase(suburb)){return postcodeModelList.get(i).getId();}
            }
        }
        return null;
    }

    public String getIdFromPostcode(String postcode){
        if(postcodeModelList!=null&&postcodeModelList.size()>0){
            for(int i=0; i<postcodeModelList.size(); i++){
                String name = postcodeModelList.get(i).getPostcode();
                if(name.equalsIgnoreCase(postcode)){return postcodeModelList.get(i).getId();}
            }
        }
        return null;
    }

    public String getPostcodeFromSuburb(String suburb){
        if(postcodeModelList!=null&&postcodeModelList.size()>0){
            for(int i=0; i<postcodeModelList.size(); i++){
                String name = postcodeModelList.get(i).getSuburb();
                if(name.equalsIgnoreCase(suburb)){return postcodeModelList.get(i).getPostcode();}
            }
        }
        return null;
    }

    public void setDisplayFlag(boolean isDisplay){
        if(postcodeModelList!=null&&postcodeModelList.size()>0){
            for(int i=0; i<postcodeModelList.size(); i++){
                postcodeModelList.get(i).setDisplay(isDisplay);
            }
        }
    }

    public boolean toObject(String jsonObjectString){
        try{
            JSONObject json = new JSONObject(jsonObjectString);
            List<PostcodeModel> postcodeModels = new ArrayList<PostcodeModel>();
            JSONArray jsonArray = json.getJSONArray(POSTCODES_LIST);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                PostcodeModel postcodeModel = new PostcodeModel();
                postcodeModel.toObject(jsonObject.toString());
                postcodeModels.add(postcodeModel);

            }
            postcodeModelList.addAll(postcodeModels); postcodeModels.clear();
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
            List<PostcodeModel> postcodeModels = postcodeModelList;
            for(int i=0;i<postcodeModels.size();i++){
                jsonArray.put(postcodeModels.get(i).toString());
            }
            jsonMain.put(POSTCODES_LIST,jsonArray);
            returnString = jsonMain.toString();
        }
        catch (Exception ex){Log.d(TAG," To String Exception : "+ex);
           }
        return returnString;
    }
}
