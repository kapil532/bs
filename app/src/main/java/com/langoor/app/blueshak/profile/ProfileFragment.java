package com.langoor.app.blueshak.profile;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.langoor.app.blueshak.Messaging.util.CommonUtil;
import com.langoor.app.blueshak.services.model.VerifyAliasModel;
import com.langoor.blueshak.R;
import com.langoor.app.blueshak.MainActivity;
import com.langoor.app.blueshak.Messaging.adapter.MessagePagerAdapter;
import com.langoor.app.blueshak.PickLocation;
import com.langoor.app.blueshak.bookmarks.BookMarkActivty;
import com.langoor.app.blueshak.global.GlobalFunctions;
import com.langoor.app.blueshak.global.GlobalVariables;
import com.langoor.app.blueshak.helper.RoundedImageView;
import com.langoor.app.blueshak.mylistings.MyListings;
import com.langoor.app.blueshak.register.TermsConditions;
import com.langoor.app.blueshak.reviews_rating.ReviewsRatings;
import com.langoor.app.blueshak.services.ServerResponseInterface;
import com.langoor.app.blueshak.services.ServicesMethodsManager;
import com.langoor.app.blueshak.services.model.LocationModel;
import com.langoor.app.blueshak.services.model.OTPResendModel;
import com.langoor.app.blueshak.services.model.ProfileDetailsModel;
import com.langoor.app.blueshak.services.model.ShopModel;
import com.langoor.app.blueshak.services.model.StatusModel;
;import com.langoor.app.blueshak.shop.ShopActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pushwoosh.PushManager;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


public class ProfileFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
		private String TAG = "ProfileActivity";
		private static Context context;
		private RoundedImageView rounde_image;
		static Activity activity;
		private ProfileDetailsModel profileDetailsModel=new ProfileDetailsModel();
		private TextView edit,name,email_id,phone_no,address,read_reviews;
		private  LinearLayout listing_content;
		private  String user_id;
	    private  boolean mail_verified=false;
	    private  boolean number_verified=false;
		private RatingBar ratingBar;
		private LinearLayout likes_content,contact_support_content,privacy_policy_content,terms_conditions_content,rate_app_content,share_content,logout_content;
		private ShopModel shopModel=null;
	private ProgressBar progress_bar;
	private ImageView go_to_filter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context=getContext();
		activity=getActivity();
		View rootView = inflater.inflate(R.layout.content_profile, container, false);
		progress_bar=(ProgressBar)rootView.findViewById(R.id.progress_bar);
		name = (TextView) rootView.findViewById(R.id.profile_name);
		email_id = (TextView) rootView.findViewById(R.id.email_id);
		phone_no = (TextView) rootView.findViewById(R.id.phone_no);
		address = (TextView) rootView.findViewById(R.id.address);
		Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
		go_to_filter=(ImageView)toolbar.findViewById(R.id.go_to_filter);
		go_to_filter.setVisibility(View.GONE);
		read_reviews = (TextView) rootView.findViewById(R.id.read_reviews);
		read_reviews.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				proceedToReviews();
			}
		});
		read_reviews.setPaintFlags(read_reviews.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		rounde_image=(RoundedImageView)rootView.findViewById(R.id.rounde_image);

		contact_support_content=(LinearLayout)rootView.findViewById(R.id.contact_support_content);
		privacy_policy_content=(LinearLayout)rootView.findViewById(R.id.privacy_policy_content);
		terms_conditions_content=(LinearLayout)rootView.findViewById(R.id.terms_conditions_content);
		ratingBar=(RatingBar)rootView.findViewById(R.id.ratingBar);
		edit=(TextView)rootView.findViewById(R.id.edit);
		edit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=ProfileEditActivity.newInstance(context,profileDetailsModel);
				startActivity(intent);
			}
		});

		likes_content=(LinearLayout)rootView.findViewById(R.id.likes_content);
		likes_content.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(context, BookMarkActivty.class));
			}
		});
		rate_app_content=(LinearLayout)rootView.findViewById(R.id.rate_app_content);
		rate_app_content.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				proceedToRateApp();
			}
		});
		ratingBar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				proceedToRateApp();
			}
		});
		share_content=(LinearLayout)rootView.findViewById(R.id.share_content);
		share_content.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				shareApp();
			}
		});
		logout_content=(LinearLayout)rootView.findViewById(R.id.logout_content);
		logout_content.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!((Activity)context ).isFinishing()){
					showExitAlert();
				}
			}
		});

		final SwipeRefreshLayout.OnRefreshListener swipeRefreshListner = new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				Log.i(TAG, "onRefresh called from SwipeRefreshLayout");
               /* swipeRefreshLayout.setRefreshing(true);*/
				setThisPage();
			}
		};
		/**
		 * Showing Swipe Refresh animation on activity create
		 * As animation won't start on onCreate, post runnable is used
		 */


		contact_support_content.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sendEmail();
			}
		});
		terms_conditions_content.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i= TermsConditions.newInstance(context,GlobalVariables.TYPE_TnC);
				startActivity(i);
			}
		});
		privacy_policy_content.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i= TermsConditions.newInstance(context,GlobalVariables.TYPE_privacy_policy);
				startActivity(i);
			}
		});


		/*profile_content.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
             *//*   Intent i= ShopActivity.newInstance(context,profileDetailsModel,null);
                startActivity(i);*//*
               *//* getShopDetails(context,user_id);*//*
				proceedToShop();
			}
		});*/
		listing_content=(LinearLayout)rootView.findViewById(R.id.listing_content);
		listing_content.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			/*	proceedToShop();*/
				startActivity(new Intent(activity, MyListings.class));
			}
		});

		return rootView;
	}
	@Override
	public void onRefresh() {
		if(!GlobalFunctions.isNetworkAvailable(context)){
			Snackbar.make(name, "Please check your internet connection", Snackbar.LENGTH_LONG)
					.setAction("Retry", new View.OnClickListener() {
						@Override
						@TargetApi(Build.VERSION_CODES.M)
						public void onClick(View v) {

						}
					}).show();
		}else{
			setThisPage();
		}

	}
	@Override
	public void onResume() {
		try {
			((AppCompatActivity) getActivity()).getSupportActionBar().show();
			if (!GlobalFunctions.isNetworkAvailable(context)) {
				Snackbar.make(name, "Please check your internet connection", Snackbar.LENGTH_LONG)
						.setAction("Retry", new View.OnClickListener() {
							@Override
							@TargetApi(Build.VERSION_CODES.M)
							public void onClick(View v) {

							}
						}).show();

			} else
				setThisPage();

		} catch (NullPointerException e){
			Log.d(TAG,"NullPointerException");
			e.printStackTrace();
		}catch (NumberFormatException e) {
			Log.d(TAG,"NumberFormatException");
			e.printStackTrace();
		}catch (Exception e){
			Log.d(TAG,"Exception");
			e.printStackTrace();
		}
		super.onResume();

	}
	public void setThisPage(){
		getUserDetails(context);
	}
	private void getUserDetails(Context context){
		showProgressBar();
		ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
		servicesMethodsManager.getUserDetails(context, new ServerResponseInterface() {
			@Override
			public void OnSuccessFromServer(Object arg0) {
				hideProgressBar();
				Log.d(TAG, "onSuccess Response"+arg0.toString());
				profileDetailsModel = (ProfileDetailsModel) arg0;
				setValues(profileDetailsModel);
			}

			@Override
			public void OnFailureFromServer(String msg) {
				hideProgressBar();

				Log.d(TAG, msg);
			}

			@Override
			public void OnError(String msg) {
				hideProgressBar();
				Log.d(TAG, msg);
			}
		}, "Profile");

	}
/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		this.item = item;
		int id = item.getItemId();
		if (id == R.id.profile_edit) {
			Intent intent=ProfileEditActivity.newInstance(context,profileDetailsModel);
			startActivity(intent);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
*/

	public void setValues(ProfileDetailsModel model){
		if(model!=null){
			user_id=model.getId();
			name.setText(model.getName());
			if(model.getReviews()!=null && !TextUtils.isEmpty(model.getReviews()))
				read_reviews.setText("Read reviews"+"("+model.getReviews_count()+")");
			if(model.getPhone()!=null && !TextUtils.isEmpty(model.getPhone()))
				phone_no.setText("Ph: "+model.getIsd()+" "+model.getPhone());
			else
				phone_no.setVisibility(View.GONE);
			address.setText(model.getAddress());
			email_id.setText(model.getEmail());
			String avatar=model.getAvatar();
			/*ImageLoader imageLoader = ImageLoader.getInstance();
			DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(false)
					.cacheOnDisc(true).resetViewBeforeLoading(true)
					.showImageForEmptyUri(R.drawable.placeholder_background)
					.showImageOnFail(R.drawable.placeholder_background)
					.showImageOnLoading(R.drawable.placeholder_background).build();
			//download and display image from url
			imageLoader.displayImage(avatar,rounde_image,options);
*/			if(!TextUtils.isEmpty(avatar)){
				Picasso.with(activity)
						.load(avatar)
						.placeholder(R.drawable.squareplaceholder)
						.memoryPolicy(MemoryPolicy.NO_CACHE)
						.networkPolicy(NetworkPolicy.NO_CACHE)
						.fit().centerCrop()
						.into(rounde_image);
			}else{
				rounde_image.setImageResource(R.drawable.squareplaceholder);
			}
			    /*if(model.getReviews()!=null)
                setReviewsRatings(model.getReviews());*/
			String rating=profileDetailsModel.getReviews();
			if(rating!=null&&!TextUtils.isEmpty(rating)&&!rating.equalsIgnoreCase("null"))
				ratingBar.setRating(Float.parseFloat(rating));

			GlobalFunctions.setSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_FULLNAME, model.getName());
			GlobalFunctions.setSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_PHONE, model.getPhone());
			GlobalFunctions.setSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_ADDRESS, model.getAddress());
			GlobalFunctions.setSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_AVATAR, model.getAvatar());
			/*getShopDetails(context,user_id);*/
		}
	}
	public void verifyEmail(){
		showProgressBar();
		ServicesMethodsManager serverManager = new ServicesMethodsManager();
		serverManager.verifyEmail(context, new ProfileDetailsModel(),new ServerResponseInterface() {
			@Override
			public void OnSuccessFromServer(Object arg0) {
				hideProgressBar();
				StatusModel statusModel=(StatusModel)arg0;
				Log.d(TAG, "onSuccess Response"+statusModel.toString());
				validateEmail(statusModel);
			}

			@Override
			public void OnFailureFromServer(String msg) {
				hideProgressBar();
				Toast.makeText(activity,msg, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void OnError(String msg) {
				hideProgressBar();
				Toast.makeText(activity,msg, Toast.LENGTH_SHORT).show();
			}
		},"Email verification");
	}
	public void verifyPhoneNumber(){
		showProgressBar();
		ServicesMethodsManager serverManager = new ServicesMethodsManager();
		OTPResendModel resendModel = new OTPResendModel(GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_PHONE));
		serverManager.resendOTP(context, resendModel, new ServerResponseInterface() {
			@Override
			public void OnSuccessFromServer(Object arg0) {
				hideProgressBar();
				validateOTPResend((StatusModel) arg0);
			}

			@Override
			public void OnFailureFromServer(String msg) {
				hideProgressBar();
				Toast.makeText(activity,msg,Toast.LENGTH_SHORT).show();
			}

			@Override
			public void OnError(String msg) {
				hideProgressBar();
				Toast.makeText(activity,msg, Toast.LENGTH_SHORT).show();
			}
		},"OTP verification");
	}
	private void validateOTPResend(StatusModel statusModel){
		if(statusModel.isStatus()){
			Toast.makeText(context,"OTP Send Successfully", Toast.LENGTH_SHORT).show();
		}else{Toast.makeText(context,"Already Authenticated", Toast.LENGTH_SHORT).show();}
	}

	private void validateEmail(StatusModel statusModel){
		if(statusModel.getMessage().equalsIgnoreCase("Verification link is sent to respective E-mail")){
			mail_verified=true;
		}else{
			Toast.makeText(context,"Error on verifying Email", Toast.LENGTH_SHORT).show();}
	}
	public static void logoutAndMove(Context context){
		GlobalFunctions.removeSharedPreferenceKey(context,GlobalVariables.SHARED_PREFERENCE_TOKEN);
		GlobalFunctions.removeSharedPreferenceKey(context,GlobalVariables.SHARED_PREFERENCE_BS_ID);
		GlobalFunctions.removeSharedPreferenceKey(context,GlobalVariables.SHARED_PREFERENCE_USERID);
		/*if(activity!=null)
			activity.finish();*/
		/*CommonUtil.unregisterDevice(context);*/
		PushManager.getInstance(context).unregisterForPushNotifications();
		MainActivity.moveToHome(activity);
	}
	public void getShopDetails(Context context,String selle_user_id){
		ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
		servicesMethodsManager.getShopDetails(context,selle_user_id,new ServerResponseInterface() {
			@Override
			public void OnSuccessFromServer(Object arg0) {
				Log.d(TAG, "onSuccess Response"+arg0.toString());
				shopModel = (ShopModel) arg0;
				Log.d(TAG,"####ShopModel#####"+shopModel.toString());
				Log.d(TAG,"####Profile model#####"+shopModel.getProfileDetailsModel().toString());
				Log.d(TAG,"####Item List#####"+shopModel.getItem_list());
             /*   proceedToShop();*/

			}

			@Override
			public void OnFailureFromServer(String msg) {
				Log.d(TAG, msg);
			}

			@Override
			public void OnError(String msg) {

				Log.d(TAG, msg);
			}
		}, "Shop Model");

	}
	public void proceedToShop() {
		if(shopModel!=null){
			Intent i = ShopActivity.newInstance(context,user_id);
			startActivity(i);
		}else
			getShopDetails(context,user_id);


	}
	public void proceedToReviews() {
		Intent intent= ReviewsRatings.newInstance(activity,null,null,profileDetailsModel,null);
		startActivity(intent);
	}
	public void proceedToRateApp(){
		Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
		Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
		goToMarket.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			context.startActivity(goToMarket);
		} catch (ActivityNotFoundException e) {
			/*UtilityClass.showAlertDialog(context, ERROR, "Couldn't launch the market", null, 0);*/
		}
	}
	public void shareApp(){
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id="+context.getPackageName());
		try {
			context.startActivity(Intent.createChooser(shareIntent, "Share Blueshak using"));
		} catch (ActivityNotFoundException e) {
			/*UtilityClass.showAlertDialog(context, ERROR, "Couldn't launch the market", null, 0);*/
		}

	}
	public void showExitAlert(){
		final com.langoor.app.blueshak.view.AlertDialog alertDialog = new com.langoor.app.blueshak.view.AlertDialog(context);
		alertDialog.setTitle("Are you sure?");
		alertDialog.setPositiveButton("OK", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				logoutAndMove();
				alertDialog.dismiss();
			}
		});

		// on pressing cancel button
		alertDialog.setNegativeButton("Cancel", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.cancel();
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}
	private void logoutAndMove(){
		logoutAndMove(context);
	}
	public void showProgressBar(){
		if(progress_bar!=null)
			progress_bar.setVisibility(View.VISIBLE);
	}
	public void hideProgressBar(){
		if(progress_bar!=null)
			progress_bar.setVisibility(View.GONE);
	}
	protected void sendEmail() {
		String email=GlobalVariables.BLUESHAK_SUPPORT_EMAIL;
		Log.i("Send email", "");
		String[] TO = {email};
		String[] CC = {""};
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setPackage("com.google.android.gm");
		emailIntent.setData(Uri.parse("mailto:"));
		emailIntent.setType("text/plain");
		emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
		emailIntent.putExtra(Intent.EXTRA_CC, CC);
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Blueshak Support");
		emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");
		try {
			startActivity(Intent.createChooser(emailIntent, "Send mail..."));
			/*finish();*/
			Log.i(TAG, "Finished sending email...");
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(activity, "There is no email client installed.", Toast.LENGTH_SHORT).show();
		}
	}


}
