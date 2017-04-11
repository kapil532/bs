package com.langoor.app.blueshak.services.model;

import android.util.Log;

import com.langoor.app.blueshak.AppController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CategoryListModel {
    private final String TAG = "CategoryListModel";
    private final String
            CATEGORIES = "categories";

    List<CategoryModel> categoryList = new ArrayList<CategoryModel>();

    public CategoryListModel(){}

    public List<CategoryModel> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<CategoryModel> categoryList) {
        this.categoryList = categoryList;
    }

    public ArrayList<String> getCategoryNames(){
        ArrayList<String> catList = new ArrayList<String>();
        if(categoryList!=null&&categoryList.size()>0){
            for(int i=0; i<categoryList.size(); i++){
                String name = categoryList.get(i).getName();
                catList.add(name);
            }
        }
        return catList;
    }

    public String getIDStringfromNameString(String nameString){
        String idString = "";
        if(nameString!=null){
            String[] IDs = nameString.split(",");
            for(int i=0;i<IDs.length;i++){
                String id = getIdFromName(IDs[i].trim());
                idString = idString.equalsIgnoreCase("")?(id!=null?id:""):(id!=null?idString+","+id:"");
            }
        }
        return idString;
    }
    public ArrayList<String> getIdsforNames(ArrayList<String> names){
        ArrayList<String> ids = new ArrayList<String>();
        if(names!=null&&names.size()>0){
            for(int i=0; i<names.size();i++){
                String id = getIdFromName(names.get(i));
                if(id!=null){ids.add(id);}
            }
        }
        return ids;
    }

    public String getIdFromName(String categoryName){
        if(categoryList!=null&&categoryList.size()>0){
            for(int i=0; i<categoryList.size(); i++){
                String name = categoryList.get(i).getName();
                if(name.equalsIgnoreCase(categoryName)){return categoryList.get(i).getId();}
            }
        }
        return null;
    }

    public ArrayList<String> getNamesforIDs(ArrayList<String> IDs){
        ArrayList<String> names = new ArrayList<String>();
        if(IDs!=null&&IDs.size()>0){
            for(int i=0; i<IDs.size();i++){
                String id = getNameFromID(IDs.get(i));
                if(id!=null){names.add(id);}
            }
        }
        return names;
    }

    public String getNameFromID(String categoryID){
        if(categoryList!=null&&categoryList.size()>0){
            for(int i=0; i<categoryList.size(); i++){
                String name = categoryList.get(i).getId();
                if(name.equalsIgnoreCase(categoryID)){return categoryList.get(i).getName();}
            }
        }
        return null;
    }

    public boolean toObject(String jsonObjectString){
        try{
            JSONObject json = new JSONObject(jsonObjectString);
            List<CategoryModel> categoryModelList = new ArrayList<CategoryModel>();
            JSONArray jsonArray = json.getJSONArray(CATEGORIES);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                CategoryModel categoryModel = new CategoryModel();
                categoryModel.toObject(jsonObject.toString());
                categoryModelList.add(categoryModel);
            }
            categoryList.addAll(categoryModelList); categoryModelList.clear();
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
            List<CategoryModel> categoryModelList = categoryList;
            for(int i=0;i<categoryModelList.size();i++){
                jsonArray.put(new JSONObject(categoryModelList.get(i).toString()));
            }
            jsonMain.put(CATEGORIES,jsonArray);
            returnString = jsonMain.toString();
        }
        catch (Exception ex){Log.d(TAG," To String Exception : "+ex);
           }
        return returnString;
    }
    public int getIndexforCategoryID(String id){
        if(categoryList!=null&&categoryList.size()>0){
            for(int i=0; i<categoryList.size(); i++){
                String name = categoryList.get(i).getId();
                if(name.equalsIgnoreCase(id)){
                    return i;
                }
            }
        }
        return -1;
    }

}
