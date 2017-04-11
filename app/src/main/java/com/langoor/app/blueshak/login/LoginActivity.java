package com.langoor.app.blueshak.login;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.langoor.app.blueshak.PickLocation;
import com.langoor.app.blueshak.otp.OTPActivity;
import com.langoor.app.blueshak.services.model.AskModel;
import com.langoor.app.blueshak.services.model.OTPCheckerModel;
import com.langoor.blueshak.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.langoor.app.blueshak.AppController;
import com.langoor.app.blueshak.MainActivity;
import com.langoor.app.blueshak.Messaging.data.PostData;
import com.langoor.app.blueshak.forgot_password.ForgotPasswordActivity;
import com.langoor.app.blueshak.garage.CreateGarageSaleFragment;
import com.langoor.app.blueshak.garage.CreateItemSaleFragment;
import com.langoor.app.blueshak.garage.CreateSaleActivity;
import com.langoor.app.blueshak.global.GlobalFunctions;
import com.langoor.app.blueshak.global.GlobalVariables;
import com.langoor.app.blueshak.register.SignUpSCreenActivity;
import com.langoor.app.blueshak.report.ReportListing;
import com.langoor.app.blueshak.root.RootActivity;
import com.langoor.app.blueshak.services.ServerConstants;
import com.langoor.app.blueshak.services.ServerResponseInterface;
import com.langoor.app.blueshak.services.ServicesMethodsManager;
import com.langoor.app.blueshak.services.model.CreateProductModel;
import com.langoor.app.blueshak.services.model.CreateSalesModel;
import com.langoor.app.blueshak.services.model.ErrorModel;
import com.langoor.app.blueshak.services.model.FacebookRegisterModel;
import com.langoor.app.blueshak.services.model.LocationModel;
import com.langoor.app.blueshak.services.model.LoginModel;
import com.langoor.app.blueshak.services.model.UserModel;
import com.langoor.app.blueshak.splash.NextSplashScreen;
import com.langoor.app.blueshak.util.LocationListener;
import com.langoor.app.blueshak.util.LocationService;
import com.nostra13.universalimageloader.utils.L;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends RootActivity implements View.OnClickListener,LocationListener {
	private EditText mUsername;
	public static final String BUNDLE_KEY_LOGIN_MODEL_SERIALIZABLE = "LoginKeyLocationModelSerializeable";
	private Button mLoginBtn;
	private Button facebookLoginBtn;
	private EditText etCOnfPass;
	private TextView mForgotpass,cancel;
	private Button mSignup;
	private ProgressDialog progressDialog;
	private static Activity activity;
	private CallbackManager callbackManager;
	private LoginButton loginButton;
	private static final int REQUEST_READ_CONTACTS = 88;
	private static final String TAG = "SigninActivity";
	private static FacebookRegisterModel facebookregisterModel = new FacebookRegisterModel();
	private  static  Context context;
	private static ImageView close_button;
	private static LocationService locServices;
	private ProgressBar progress_bar;
	private GlobalVariables globalVariables=new GlobalVariables();
	private LocationModel locationModel=new LocationModel();
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		/*if the phone number is changed and user navigate to Otp Activty*/
		OTPCheckerModel otpCheckerModel =new OTPCheckerModel();
		String filter_string=GlobalFunctions.getSharedPreferenceString(this,GlobalVariables.OTP_CHECK_MODEL);
		int otp_verified=GlobalFunctions.getSharedPreferenceInt(this, GlobalVariables.SHARED_PREFERENCE_IS_OTP_VERIFIED);
		if(filter_string!=null){
			otpCheckerModel.toObject(filter_string);
			if(otp_verified==0){
				startActivity(OTPActivity.newInstance(context,otpCheckerModel));
				finish();
			}
		}
		int googlePlayServiceStatus = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if(googlePlayServiceStatus != ConnectionResult.SUCCESS)
		{
			if(googlePlayServiceStatus == ConnectionResult.SERVICE_MISSING ||
					googlePlayServiceStatus == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED ||
					googlePlayServiceStatus == ConnectionResult.SERVICE_DISABLED )
				GooglePlayServicesUtil.getErrorDialog(googlePlayServiceStatus, this, 0).show();

			return;
		}
		setContentView(R.layout.activity_login_screen);
		try{
			activity=this;
			context=this;
			FacebookSdk.sdkInitialize(context);
			locServices = new LocationService(activity);
			locServices.setListener(this);
			if(!locServices.canGetLocation()){
				Intent intent= PickLocation.newInstance(context,GlobalVariables.TYPE_LOGIN,true,null);
				startActivityForResult(intent,globalVariables.REQUEST_CODE_FILTER_PICK_LOCATION);
			}else{
				getAddressFromLatLng(context);
			}
			Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
			setSupportActionBar(toolbar);
			// Update the action bar title with the TypefaceSpan instance
			LayoutInflater inflator = LayoutInflater.from(this);
			View v = inflator.inflate(R.layout.action_bar_titlel, null);
			((TextView)v.findViewById(R.id.title)).setText(/*this.getTitle()*/"Sign In");
			toolbar.addView(v);
			cancel=(TextView)v.findViewById(R.id.cancel);
			cancel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					closeThisActivity();
				}
			});
			disconnectFromFacebook();

			progress_bar=(ProgressBar)findViewById(R.id.progress_bar);
			mUsername = (EditText)findViewById(R.id.username);
			mLoginBtn = (Button)findViewById(R.id.login);
			facebookLoginBtn = (Button)findViewById(R.id.face_book_login);
			etCOnfPass=(EditText)findViewById(R.id.password);
			mForgotpass=(TextView)findViewById(R.id.forgot_password);
			mForgotpass.setPaintFlags(mForgotpass.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
			context=this;
			if(!checkIfContactsAccountsPermission())
				checkContactsAccountsPermission();
			mForgotpass.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
					startActivity(i);

				}
			});

			mSignup=(Button)findViewById(R.id.sign_up);
			mSignup.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent=new Intent(activity,SignUpSCreenActivity.class);
					startActivity(intent);
				}
			});

			close_button=(ImageView)findViewById(R.id.close_button);
			close_button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					closeThisActivity();
				}
			});
			activity = this;
			mLoginBtn.setOnClickListener(this);
		}catch (Exception e){
			e.printStackTrace();
			Crashlytics.log(e.getMessage());
			Log.d(TAG,e.getMessage());
		}

	}

	@Override
	public void onClick(View viewClicked)
	{
		switch (viewClicked.getId())
		{
			case R.id.login:
				sendRequestToServer();
				break;
		}
	}

	private void sendRequestToServer() {
		if(mUsername.getText().length() == 0)
		{
			mUsername.setError("Please fill the email");
			return;
		}else  if(!isValidEmail(mUsername.getText().toString().trim())){
			mUsername.setError("Please Check your Email id!!");
			return;
		}else if(etCOnfPass.getText().length() ==0){
			etCOnfPass.setError("Please fill the password");
			return;
		}else {
			if(GlobalFunctions.isNetworkAvailable(this)){
				loginUser(this, mUsername.getText().toString().trim(), etCOnfPass.getText().toString().trim());
			}else{
				Snackbar.make(mUsername, "Please check your internet connection", Snackbar.LENGTH_LONG)
						.setAction("Retry", new View.OnClickListener() {
							@Override
							@TargetApi(Build.VERSION_CODES.M)
							public void onClick(View v) {
								sendRequestToServer();
							}
						}).show();
			}
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		closeThisActivity();
	}

	private void loginUser(Context context, String email, String password){
		String country=GlobalFunctions.getSharedPreferenceString(context,GlobalVariables.SHARED_PREFERENCE_COUNTRY);
		if(country==null){
			/*Needed when device data is cleared*/
			country= context.getResources().getConfiguration().locale.getCountry();
		}
		showProgressBar();
		LoginModel loginModel = new LoginModel();
		loginModel.setEmail(email);
		loginModel.setPassword(password);
		loginModel.setCountry(country);
		ServicesMethodsManager serverManager = new ServicesMethodsManager();
		serverManager.login(activity, loginModel, new ServerResponseInterface() {
			@Override
			public void OnSuccessFromServer(Object arg0) {
			hideProgressBar();
				UserModel user = (UserModel) arg0;
				validateLogin(user);
			}

			@Override
			public void OnFailureFromServer(String msg) {
				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
				hideProgressBar();
			}

			@Override
			public void OnError(String msg) {
				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
				hideProgressBar();
			}
		}, "login Data");
	}



	public final static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}

	public void closeThisActivity(){
		if(activity!=null){activity.finish();}
	}



	@Override
	protected void onResume() {
		super.onResume();
		try{
			callbackManager= CallbackManager.Factory.create();
			loginButton= (LoginButton)findViewById(R.id.login_button);
			loginButton.setReadPermissions("public_profile", "email","user_friends");
			facebookLoginBtn= (Button) findViewById(R.id.face_book_login);
			facebookLoginBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					progressDialog = new ProgressDialog(LoginActivity.this);
					progressDialog.setMessage("Loading...");
				/*progressDialog.show();*/

					loginButton.performClick();

					loginButton.setPressed(true);

					loginButton.invalidate();

					loginButton.registerCallback(callbackManager, mCallBack);

					loginButton.setPressed(false);

					loginButton.invalidate();

				/*boolean sessionExpired;
				if(AccessToken.getCurrentAccessToken()!=null){
					Log.d(TAG,"getCurrentAccessTokenis not null");
					sessionExpired = AccessToken.getCurrentAccessToken().isExpired();
				}else{
					sessionExpired = true;
				}
				if(sessionExpired){
					Log.d(TAG,"getCurrentAccessTokenis sessionExpired");
					LoginManager.getInstance().logOut();
				}*/
				}
			});
		}catch (NullPointerException e){
			e.printStackTrace();
			Crashlytics.log(e.getMessage());
			Log.d(TAG,e.getMessage());
		}catch (Exception e){
			e.printStackTrace();
			Crashlytics.log(e.getMessage());
			Log.d(TAG,e.getMessage());
		}

	}

	/*@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);
	}*/

	private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
		@Override
		public void onSuccess(LoginResult loginResult) {
			progressDialog.dismiss();
			final String email=null;
			GraphRequest request = GraphRequest.newMeRequest(
					loginResult.getAccessToken(),
					new GraphRequest.GraphJSONObjectCallback() {
						@Override
						public void onCompleted(
								JSONObject object,
								GraphResponse response) {
							Log.e("fb response: ", response + "");
							try {
								 String id= object.getString("id").toString();
								 String email = object.getString("email").toString();
								 String first_name = object.getString("first_name").toString();
								 String last_name = object.getString("last_name").toString();
								 String gender = object.getString("gender").toString();
								 facebookregisterModel.setEmail(email);
								 facebookregisterModel.setName(first_name);
								 facebookregisterModel.setLast_name(last_name);
								 facebookregisterModel.setFb_id(id);
								/* registerfbData();*/
								fbUserExist();
							}catch (Exception e){
								e.printStackTrace();
							}
						}
					});

			Bundle parameters = new Bundle();
			/*parameters.putString("fields", "id,name,email,gender, birthday");*/
			parameters.putString("fields", "id,first_name,last_name,email,gender, birthday");
			request.setParameters(parameters);
			request.executeAsync();


		}

		@Override
		public void onCancel() {

			System.out.println("#######onCancel#########");
			progressDialog.dismiss();
		}

		@Override
		public void onError(FacebookException e) {
			System.out.println("#######onError#########");
		/*	Log.e("######onError####: " , e.e.printStackTrace()());*/
			e.printStackTrace();
			progressDialog.dismiss();
		}
	};
	public void fetchFaceBookProfilePic(){
		new AsyncTask<Void,Void,Void>(){
			@Override
			protected Void doInBackground(Void... params) {
				URL imageURL = null;
				try {
					imageURL = new URL("https://graph.facebook.com/" + "1435767690067161" + "/picture?type=large");
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				try {
					Bitmap bitmap  = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());

				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void aVoid) {
				super.onPostExecute(aVoid);
				//  profileImage.setImageBitmap(bitmap);
			}
		}.execute();

	}

	private void validateLogin(UserModel user){
		Log.d(TAG,"validateLogin"+user.toString());
		GlobalFunctions.setSharedPreferenceString(this, GlobalVariables.SHARED_PREFERENCE_COUNTRY, user.getCountry());
		GlobalFunctions.setSharedPreferenceString(this, GlobalVariables.SHARED_PREFERENCE_EMAIL, user.getEmail());
		GlobalFunctions.setSharedPreferenceString(this, GlobalVariables.SHARED_PREFERENCE_FULLNAME, user.getName());
		GlobalFunctions.setSharedPreferenceString(this, GlobalVariables.SHARED_PREFERENCE_PHONE, user.getPhone());
		GlobalFunctions.setSharedPreferenceString(this, GlobalVariables.SHARED_PREFERENCE_ADDRESS, user.getAddress());
		GlobalFunctions.setSharedPreferenceString(this, GlobalVariables.SHARED_PREFERENCE_TOKEN, user.getToken());
		GlobalFunctions.setSharedPreferenceString(this, GlobalVariables.SHARED_PREFERENCE_AVATAR, user.getImage());
		GlobalFunctions.setSharedPreferenceString(this, GlobalVariables.SHARED_PREFERENCE_BS_ID, user.getId());
		GlobalFunctions.setSharedPreferenceString(this, GlobalVariables.SHARED_PREFERENCE_USERID, user.getId());
		GlobalFunctions.setProfile(this, user);
		/*Kill the old activity and start the new session when user logged in*/
		SignUpSCreenActivity.registerPushWoosh(context);
		MainActivity.closeThisActivity();
		startActivity(new Intent(this,MainActivity.class));
		closeThisActivity();
	}

	public void checkContactsAccountsPermission() {
		Log.d(TAG,"checkContactsAccountsPermission");
		int permissionCheck_contacts_coarse = ContextCompat.checkSelfPermission(activity, Manifest.permission.GET_ACCOUNTS);
		int permissionCheck_accounts_coarse = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS);
		if(permissionCheck_contacts_coarse != PackageManager.PERMISSION_GRANTED &&
				permissionCheck_accounts_coarse != PackageManager.PERMISSION_GRANTED ) {
			ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.GET_ACCOUNTS,Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACTS);
		}

	}
	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		Log.d(TAG,"onRequestPermissionsResult");
		switch (requestCode) {
			case REQUEST_READ_CONTACTS:{
				Log.d(TAG,"REQUEST_READ_CONTACTS");
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				} else {
					/*checkContactsAccountsPermission();*/
					if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.GET_ACCOUNTS) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
						checkContactsAccountsPermission();
					}else {
						Snackbar snackbar = Snackbar.make(etCOnfPass, getResources().getString(R.string.contact_permission_text), Snackbar.LENGTH_LONG);
						snackbar.setAction(getResources().getString(R.string.settings), new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								if (activity == null) {
									return;
								}
								Intent intent = new Intent();
								intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
								Uri uri = Uri.fromParts("package",activity.getPackageName(), null);
								intent.setData(uri);
								activity.startActivity(intent);
							}
						});
						snackbar.show();
						//                            //proceed with logic by disabling the related features or quit the app.
					}
				}
				return;
			}
		}
	}
	private boolean checkIfContactsAccountsPermission() {
		int coarse_location = ContextCompat.checkSelfPermission(activity, Manifest.permission.GET_ACCOUNTS);
		int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS);
		if (coarse_location == PackageManager.PERMISSION_GRANTED && result == PackageManager.PERMISSION_GRANTED) {
			return true;
		} else {
			return false;
		}
	}
	public void disconnectFromFacebook() {
		Log.d(TAG,"disconnectFromFacebook");
		LoginManager.getInstance().logOut();
	}

	private void registerfbData(){
		showProgressBar();
		String country=GlobalFunctions.getSharedPreferenceString(context,GlobalVariables.SHARED_PREFERENCE_COUNTRY);
		if(country!=null){
			facebookregisterModel.setCountry(country);
		}else{
			facebookregisterModel.setCountry("IN");
		}
		ServicesMethodsManager serverManager = new ServicesMethodsManager();
		serverManager.registerFacebookUser(this, facebookregisterModel, new ServerResponseInterface() {
			@Override
			public void OnSuccessFromServer(Object arg0) {
				hideProgressBar();
				Log.d(TAG, "Object String:"+arg0.toString());
				if(arg0 instanceof UserModel){
					UserModel user = (UserModel) arg0;
					validateFbLogin(user);
				}else if(arg0 instanceof ErrorModel){
					ErrorModel errorModel=(ErrorModel)arg0;
					Toast.makeText(context,errorModel.getError(),Toast.LENGTH_SHORT).show();
				}

			}
			@Override
			public void OnFailureFromServer(String msg) {
				hideProgressBar();
				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void OnError(String msg) {
				hideProgressBar();
				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
			}
		}, "Register User");
	}
	private void validateFbLogin(UserModel user){
		GlobalFunctions.setSharedPreferenceInt(context,GlobalVariables.SHARED_PREFERENCE_IS_OTP_VERIFIED,1);
		GlobalFunctions.setSharedPreferenceString(this, GlobalVariables.SHARED_PREFERENCE_COUNTRY, user.getCountry());
		GlobalFunctions.setSharedPreferenceString(this, GlobalVariables.SHARED_PREFERENCE_EMAIL, user.getEmail());
		GlobalFunctions.setSharedPreferenceString(this, GlobalVariables.SHARED_PREFERENCE_FULLNAME, user.getName());
		GlobalFunctions.setSharedPreferenceString(this, GlobalVariables.SHARED_PREFERENCE_PHONE, user.getPhone());
		GlobalFunctions.setSharedPreferenceString(this, GlobalVariables.SHARED_PREFERENCE_ADDRESS, user.getAddress());
		GlobalFunctions.setSharedPreferenceString(this, GlobalVariables.SHARED_PREFERENCE_TOKEN, user.getToken());
		GlobalFunctions.setSharedPreferenceString(this, GlobalVariables.SHARED_PREFERENCE_AVATAR, user.getImage());
		GlobalFunctions.setSharedPreferenceString(this, GlobalVariables.SHARED_PREFERENCE_BS_ID, user.getId());
		GlobalFunctions.setSharedPreferenceString(this, GlobalVariables.SHARED_PREFERENCE_USERID, user.getId());
		GlobalFunctions.setProfile(this, user);
		/*Kill the old activity and start the new session when user logged in*/
		SignUpSCreenActivity.registerPushWoosh(context);
		MainActivity.closeThisActivity();
		closeThisActivity();
		startActivity(new Intent(this,MainActivity.class));

	}
	@Override
	public void onLocationChanged(Location location) {
		if(location!=null){
			locServices.removeListener();
		}else{
			Toast.makeText(activity, "Fetching Location", Toast.LENGTH_SHORT).show();
		}
	}
	String address;
	public void setAddress(String adress){
		this.address=adress;
	}
	public String getAddress(){
		return this.address;
	}
	public void setAddress(LocationModel locationModel){
		setAddress(locationModel.getFormatted_address());
	}
	private void getAddressFromLatLng(Context context){
		ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
		servicesMethodsManager.getAddress(context, locServices.getLatitude(),locServices.getLongitude(), new ServerResponseInterface() {
			@Override
			public void OnSuccessFromServer(Object arg0) {
				//hideProgressBar();
				Log.d(TAG, "onSuccess Response");
				locationModel =(LocationModel)arg0;
				setAddress(locationModel);
				try {
					locationModel = (LocationModel) arg0;
					if (locServices != null) {
						locationModel.setLatitude(Double.toString(locServices.getLatitude()));
						locationModel.setLongitude(Double.toString(locServices.getLongitude()));
					}
					Log.d(TAG, "############LocationModel#############" + locationModel.toString());
					facebookregisterModel.setLatitude(locationModel.getLatitude());
					facebookregisterModel.setLongitude(locationModel.getLongitude());
					facebookregisterModel.setLatitude(locationModel.getLatitude());
					facebookregisterModel.setLongitude(locationModel.getLongitude());
					facebookregisterModel.setAddress(locationModel.getFormatted_address());
					facebookregisterModel.setState(locationModel.getState());
					facebookregisterModel.setCity(locationModel.getCity());
					facebookregisterModel.setPostalCode(locationModel.getPostal_code());
				}catch (NullPointerException ex){
					ex.printStackTrace();
					Log.d(TAG,ex.getMessage());
				}catch (Exception ex){
					ex.printStackTrace();
					Log.d(TAG, ex.getMessage());
				}
			}

			@Override
			public void OnFailureFromServer(String msg) {
				//hideProgressBar();
				Log.d(TAG, msg);
			}

			@Override
			public void OnError(String msg) {
				hideProgressBar();
				Log.d(TAG, msg);
			}
		}, "Delete Sale");
	}
	@Override
	public void onProviderEnabled(String provider) {

	}
	public void showProgressBar(){
		if(progress_bar!=null)
			progress_bar.setVisibility(View.VISIBLE);
	}
	public void hideProgressBar(){
		if(progress_bar!=null)
			progress_bar.setVisibility(View.GONE);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(TAG,"on activity result");
		try {
			if(resultCode == activity.RESULT_OK){
				Log.i(TAG,"result ok ");
				if(requestCode == globalVariables.REQUEST_CODE_FILTER_PICK_LOCATION){
					Log.i(TAG,"request code "+globalVariables.REQUEST_CODE_FILTER_PICK_LOCATION);
					locationModel = (LocationModel) data.getExtras().getSerializable(BUNDLE_KEY_LOGIN_MODEL_SERIALIZABLE);
					Log.i(TAG,"name pm"+locationModel.getFormatted_address());
					facebookregisterModel.setLatitude(locationModel.getLatitude());
					facebookregisterModel.setLongitude(locationModel.getLongitude());
					facebookregisterModel.setAddress(locationModel.getFormatted_address());
					facebookregisterModel.setState(locationModel.getState());
					facebookregisterModel.setCity(locationModel.getCity());
					facebookregisterModel.setPostalCode(locationModel.getPostal_code());
					/*location.setText(locationModel.getFormatted_address());*/
				}else{
					callbackManager.onActivityResult(requestCode, resultCode, data);
				}
			}
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
	}
	public void fbUserExist(){
		showProgressBar();
		AskModel askModel=new AskModel();
		askModel.setEmail(facebookregisterModel.getEmail());
		ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
		servicesMethodsManager.ask_for_phone_number(context,askModel, new ServerResponseInterface() {
			@Override
			public void OnSuccessFromServer(Object arg0) {
				hideProgressBar();
				Log.d(TAG, "onSuccess Response");
				if(arg0 instanceof AskModel){
					AskModel askModel1=(AskModel)arg0;
					if(askModel1.is_phone_alias_needed()){
						startActivity(SignUpSCreenActivity.newInstance(context,facebookregisterModel));
						closeThisActivity();
					}else{
						registerfbData();
					}
				}/*else if(arg0 instanceof UserModel){
					validateFbLogin((UserModel)arg0);
				}*/
			}
			@Override
			public void OnFailureFromServer(String msg) {
				hideProgressBar();
				Log.d(TAG, msg);
				Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
			}

			@Override
			public void OnError(String msg) {
				Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
				hideProgressBar();
				Log.d(TAG, msg);
			}
		}, "Delete Sale");
	}
}
