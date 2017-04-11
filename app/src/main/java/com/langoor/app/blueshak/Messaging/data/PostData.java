package com.langoor.app.blueshak.Messaging.data;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.langoor.app.blueshak.AppController;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class PostData {


	private static ProgressDialog ringProgressDialog;
	PostCommentResponseListener  mPostCommentResponse ;






	public interface PostCommentResponseListener {
		public void requestStarted();
		public void requestCompleted(String message);
		public void requestEndedWithError(VolleyError error);
	}


	public interface PostCommentJsonResponseListener {
		public void requestStarted();
		public void requestCompleted(JSONObject message);
		public void requestEndedWithError(VolleyError error);
	}



	public interface PostCommentJsonStringResponseListener
	{
		public void requestStarted();
		public void requestCompleted(JSONObject message);
		public void requestEndedWithError(String message);
	}

	public static void call(Context ctx ,JSONObject jsonobject_one,String url,final PostCommentJsonResponseListener mPostCommentResponse)
	{

		Log.d("SSSD","SSSS URL"+url+"--"+jsonobject_one.toString());

		mPostCommentResponse.requestStarted();

		ringProgressDialog	= ProgressDialog.show(ctx, "", "Please wait ...", true);
		ringProgressDialog.setCancelable(true);
		ringProgressDialog.show();
		JsonObjectRequest jsonObjReq = new JsonObjectRequest
				(
						Request.Method.POST,url,
						jsonobject_one,
						new Response.Listener<JSONObject>()
						{
							@Override
							public void onResponse(JSONObject response) 
							{
								mPostCommentResponse.requestCompleted(response);

							}
						}, new Response.ErrorListener()
						{

							@Override
							public void onErrorResponse(VolleyError error)
							{
								try {
									String file_string = new String(error.networkResponse.data, "UTF-8");

									Log.d("SS","SSS---S"+file_string);
								} catch (UnsupportedEncodingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								Log.d("SS","SSS---S"+error.networkResponse.data);
								/* for(int i = 0; i < error.networkResponse.data.length; i++)
								    {
								         file_string += (char)error.networkResponse.data[i];
								    }
								 Log.d("RESPONSE","RESPNO"+file_string);*/
								mPostCommentResponse.requestEndedWithError(error);
							}
						}) 
		{


			/**
			 * Passing some request headers
			 * */
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("Content-Type", "application/json; charset=utf-8");
				return headers;
			}


		};


		int socketTimeout = 30000;//30 seconds - change to what you want
		RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		jsonObjReq.setRetryPolicy(policy);
		AppController.getInstance().addToRequestQueue(jsonObjReq);

	}


	public static void callString(Context ctx ,JSONObject jsonobject_one,String url,final PostCommentJsonStringResponseListener mPostCommentResponse)
	{

		Log.d("SSSD","SSSS URL"+url+"--"+jsonobject_one.toString());

		mPostCommentResponse.requestStarted();

		ringProgressDialog	= ProgressDialog.show(ctx, "", "Please wait ...", true);
		ringProgressDialog.setCancelable(true);
		ringProgressDialog.show();
		JsonObjectRequest jsonObjReq = new JsonObjectRequest
				(
						Request.Method.POST,url,
						jsonobject_one,
						new Response.Listener<JSONObject>()
						{
							@Override
							public void onResponse(JSONObject response) 
							{
								mPostCommentResponse.requestCompleted(response);
								ringProgressDialog.cancel();

							}
						}, new Response.ErrorListener()
						{

							@Override
							public void onErrorResponse(VolleyError error)
							{
								String file_string ="";

								NetworkResponse response = error.networkResponse;
								if(response != null && response.data != null)
								{
									try 
									{
										file_string = new String(error.networkResponse.data, "UTF-8");
										Log.d("SS","SSS---S"+file_string);
									}
									catch (UnsupportedEncodingException e) 
									{
									}
								}
								mPostCommentResponse.requestEndedWithError(file_string);
								ringProgressDialog.cancel();
							}
						}) 
		{


			/**
			 * Passing some request headers
			 * */
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("Content-Type", "application/json; charset=utf-8");
				return headers;
			}


		};


		int socketTimeout = 30000;//30 seconds - change to what you want
		RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		jsonObjReq.setRetryPolicy(policy);
		AppController.getInstance().addToRequestQueue(jsonObjReq);

	}

	public static void getData(Context context,final String url,final PostCommentResponseListener	mPostCommentResponse){


		//mPostCommentResponse.requestStarted();
		//	    RequestQueue queue = Volley.newRequestQueue(context);

		StringRequest sr = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
			//	ringProgressDialog.cancel();
				//mPostCommentResponse.requestCompleted(response);
				//	            queue.
				Log.d("RESPOSNE","RESPONSE"+response);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				//ringProgressDialog.cancel();
				Log.d("VolleyError","RESPONSE"+error);

				String file_string ="";

				NetworkResponse response = error.networkResponse;
				if(response != null && response.data != null)
				{
					try
					{
						file_string = new String(error.networkResponse.data, "UTF-8");
						Log.d("SS","SSS---S"+file_string);
						Log.d("VolleyError","file_string"+file_string);
					}
					catch (UnsupportedEncodingException e)
					{
					}
				}
				//mPostCommentResponse.requestEndedWithError(error);
			}
		}){};

		sr.setRetryPolicy(new DefaultRetryPolicy(
				2000, 
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		AppController.getInstance().addToRequestQueue(sr);
	}


	public static void postDataParams(Context cxt ,String uri,final HashMap<String, String> parama,final PostCommentResponseListener  mPostCommentResponse)
	{

		mPostCommentResponse.requestStarted();
		ringProgressDialog	= ProgressDialog.show(cxt, "", "Please wait ...", true);
		ringProgressDialog.setCancelable(true);
		ringProgressDialog.show();

		StringRequest sr = new StringRequest(Request.Method.POST,uri, new Response.Listener<String>() {



			@Override
			public void onResponse(String response) 
			{
				ringProgressDialog.cancel();
				mPostCommentResponse.requestCompleted(response);
			}
		},
		new Response.ErrorListener()
		{

			@Override
			public void onErrorResponse(VolleyError error) {
				ringProgressDialog.cancel();
				mPostCommentResponse.requestEndedWithError(error);
			}
		})
		{
			@Override
			protected Map<String,String> getParams()
			{
				return parama;
			}

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError
			{
				Map<String,String> params = new HashMap<String, String>();
				params.put("Content-Type","application/x-www-form-urlencoded");
				return params;
			}
		};

		AppController.getInstance().addToRequestQueue(sr);
	}







}

