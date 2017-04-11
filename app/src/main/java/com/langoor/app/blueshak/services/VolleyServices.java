package com.langoor.app.blueshak.services;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.langoor.blueshak.R;
import com.langoor.app.blueshak.AppController;
import com.langoor.app.blueshak.global.GlobalFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Spurthi
 * Version 2.0 ,Brighter India Foundation , Bangalore
 * on 1/12/2016.
 */
public class VolleyServices implements Response.Listener<JSONObject>,
        Response.ErrorListener {

    private final static String TAG = "VolleyServices";
    private final int mSocketTimeout = 120000;// 120 seconds
    private RetryPolicy mRetryPolicy;

    private Map<String, String> mParams;
    private String jsonString;
    private ResposeCallBack mCallBack;

    public VolleyServices() {
        // defines max try and max timeout
        mRetryPolicy = new DefaultRetryPolicy(mSocketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

    }

    /**
     *
     * @param callBack
     *            callback for responce
     */
    public void setCallbacks(ResposeCallBack callBack) {
        mCallBack = callBack;
    }

    /**
     *
     * @param params
     *            body of the HTTP request
     */
    public void setBody(Map<String, String> params) {
        mParams = params;
    }

    public void setBody(String jsonObject) {
        jsonString = jsonObject;
    }

    private void makeJsonObjectGetRequest(String url, String tag) {
    }

    private void makeJsonArrayGetRequest(String tag) {
    }

    /**
     *
     * @param url
     *            URL
     * @param tag
     *            as to identify the request in queue
     *
     *
     *            need to be called after calling setBody and setCallbacks
     */
    public void makeJsonPostRequest(final Context context, String url, String tag) {
        JSONObject object = new JSONObject();
        if(mParams!=null){
            object =  new JSONObject(mParams);
        }else{
            try {
                object =  new JSONObject(jsonString);
            } catch (JSONException e) {
                object = new JSONObject();
            }
        }
        Log.d("Make Json Request", object.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST, url,
                object, this, this){
            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers = GlobalFunctions.getHeader(context);
                return headers;
            }


        };
        addToqueue(jsonObjReq, tag);
    }

    public void makeJsonGETRequest(final Context context, String url, String tag) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET, url,
                null, this, this){
            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return GlobalFunctions.getHeader(context);
            }


        };
        addToqueue(jsonObjReq, tag);
    }

    @Override
    public void onResponse(JSONObject arg0) {
        // TODO Auto-generated method stub
        Log.d(TAG,"Response : "+arg0.toString());
        mCallBack.OnSuccess(arg0);
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d(TAG,"Response : "+error.toString());
        Log.d(TAG,"onErrorResponse Response : "+error.toString());

        String file_string ="";
        NetworkResponse response = error.networkResponse;
        if(response != null && response.data != null){
            try{
                file_string = new String(error.networkResponse.data, "UTF-8");

            }catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }
        }
        Log.d(TAG,"#######onErrorResponse Response ############: "+file_string);
        Log.d(TAG, "Registration Error Response: " + file_string);
        if(file_string!=null && !TextUtils.isEmpty(file_string)){

        }
        String messageError ="";
        if(file_string.length() >0){
            try {
                JSONObject json = new JSONObject(file_string);
                if(json.has("errors")){
                    JSONObject err_obj = json.getJSONObject("errors");
                    if(err_obj.has("email")){
                        JSONArray err_arr= err_obj.getJSONArray("email");
                        for(int i=0;i<err_arr.length();i++){
                            messageError=err_arr.getString(i);
                            break;
                        }
                    }
                    if(err_obj.has("password")){
                        JSONArray err_arr= err_obj.getJSONArray("password");
                        for(int i=0;i<err_arr.length();i++){
                            messageError=err_arr.getString(i);
                            break;
                        }

                    } if(err_obj.has("phone")){
                        JSONArray err_arr= err_obj.getJSONArray("phone");
                        for(int i=0;i<err_arr.length();i++){
                            messageError=err_arr.getString(i);
                            break;
                        }

                    }
                }else if(json.has("message")){
                    messageError = json.getString("message");
                }else if(json.has("error")){
                    messageError = json.getString("error");
                }else {
                    messageError="Something went wrong, Please try again";
                }

                if(messageError.equalsIgnoreCase("user_not_found")){
                    messageError="User not found,Please enter the valid Email";
                }

                if(messageError.equalsIgnoreCase("invalid_credentials")){
                    messageError="Invalid credentials";
                }

                mCallBack.OnFailure(messageError);
                return;

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // TODO Auto-generated method stub
        if (error instanceof NetworkError) {
            mCallBack.OnFailure(R.string.NetworkError);
        } else if (error instanceof ServerError) {
            mCallBack.OnFailure(R.string.ServerError);
        } else if (error instanceof AuthFailureError) {
            mCallBack.OnFailure(R.string.AuthFailedError);
        } else if (error instanceof ParseError) {
            mCallBack.OnFailure(R.string.ParseError);
        } else if (error instanceof NoConnectionError) {
            mCallBack.OnFailure(R.string.NoConnectionError);
        } else if (error instanceof TimeoutError) {
            mCallBack.OnFailure(R.string.TimeOutError);
        }else{
            mCallBack.OnFailure(R.string.NetworkError);
        }
    }

    private void addToqueue(JsonObjectRequest jsonObjReq, String tag) {

        jsonObjReq.setRetryPolicy(mRetryPolicy);

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag);

    }

    /**
     *
     * @author Spurthi
     *
     *         interface to get the calllback
     */
    public interface ResposeCallBack {

        /**
         *
         * @param arg0
         *            json object from http call
         */
        public void OnSuccess(JSONObject arg0);

        /**
         *
         * @param cause
         *            R.String.Cause
         */
        public void OnFailure(int cause);

        public void OnFailure(String cause);
    }

}