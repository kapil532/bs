package com.langoor.app.blueshak.register;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.hardware.camera2.params.Face;
import android.location.Location;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.hbb20.CountryCodePicker;
import com.langoor.app.blueshak.Messaging.activity.PushActivity;
import com.langoor.app.blueshak.Messaging.helper.NotificationFactory;
import com.langoor.app.blueshak.PickLocation;
import com.langoor.app.blueshak.otp.OTPActivity;
import com.langoor.app.blueshak.services.model.CategoryModel;
import com.langoor.app.blueshak.services.model.ErrorModel;
import com.langoor.app.blueshak.services.model.FacebookRegisterModel;
import com.langoor.app.blueshak.services.model.OTPCheckerModel;
import com.langoor.app.blueshak.services.model.VerifyAliasModel;
import com.langoor.blueshak.R;
import com.langoor.app.blueshak.AppController;
import com.langoor.app.blueshak.MainActivity;
import com.langoor.app.blueshak.Messaging.util.CommonUtil;
import com.langoor.app.blueshak.global.GlobalFunctions;
import com.langoor.app.blueshak.global.GlobalVariables;
import com.langoor.app.blueshak.root.RootActivity;
import com.langoor.app.blueshak.services.ServerResponseInterface;
import com.langoor.app.blueshak.services.ServicesMethodsManager;
import com.langoor.app.blueshak.services.model.LocationModel;
import com.langoor.app.blueshak.services.model.RegisterModel;
import com.langoor.app.blueshak.services.model.UserModel;
import com.langoor.app.blueshak.util.LocationListener;
import com.langoor.app.blueshak.util.LocationService;
import com.pushwoosh.PushManager;


public class SignUpSCreenActivity extends RootActivity implements LocationListener, GoogleAccountsFragment.OnNewsItemSelectedListener{
	private Button go_to_sing_in;
	public static final String BUNDLE_KEY_SIGN_UP_MODEL_SERIALIZABLE = "SignUpKeyLocationModelSerializeable";
	private static final String TAG = "SignUpSCreenActivity";
	private  static Context context;
	private EditText etUserName,userId,last_name;
	private EditText etEmail;
	private EditText etMobileNUmber/*isd_code*/;
	private TextView location;
	private EditText etCOnfPass;
	private TextView loginPageLink,cancel;
	private TextView signUpText;
	private Button btnSignUp;
	private static Activity activity;
	private CheckBox terms_conditions;
	private TextView termsOfAgreement;
	public static FragmentManager mainActivityFM;
	private LocationModel locationModel=new LocationModel();
	/**
	 * Id to identity READ_CONTACTS permission request.
	 */
	private static final int REQUEST_READ_CONTACTS = 99;
	private String address;
	private  LinearLayout location_content;
	private LocationService locServices;
	private RegisterModel registerModel = new RegisterModel();
	private String formatted_address;
	private ImageView close_button;
	private ProgressBar progress_bar;
	private  GlobalVariables globalVariables= new GlobalVariables();
	private CountryCodePicker ccp;
	private VerifyAliasModel verifyAliasModel=new VerifyAliasModel();
	/*private IntlPhoneInput phoneInputView;*/
	public static final String FB_MODEL_KEY = "fbbundlekey";
	private  boolean is_fb_login=false;
	private FacebookRegisterModel facebookRegisterModel=new FacebookRegisterModel();
	private TextView title_tv;
	public static Intent newInstance(Context context,FacebookRegisterModel facebookRegisterModel){
		Intent mIntent = new Intent(context, SignUpSCreenActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(FB_MODEL_KEY, facebookRegisterModel);
		mIntent.putExtras(bundle);
		return mIntent;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup_screen);
		try{
			activity = this;
			context=this;
			progress_bar=(ProgressBar)findViewById(R.id.progress_bar);
			locServices = new LocationService(activity);
			locServices.setListener(this);
			if(!locServices.canGetLocation()){
				Intent intent= PickLocation.newInstance(context,GlobalVariables.TYPE_SIGN_UP,true,null);
				startActivityForResult(intent,globalVariables.REQUEST_CODE_FILTER_PICK_LOCATION);
			}
			Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
			setSupportActionBar(toolbar);
			LayoutInflater inflator = LayoutInflater.from(this);
			View v = inflator.inflate(R.layout.action_bar_titlel, null);
			title_tv=((TextView)v.findViewById(R.id.title));
			((TextView)v.findViewById(R.id.title)).setText(/*this.getTitle()*/"Sign Up");
			toolbar.addView(v);
			cancel=(TextView)v.findViewById(R.id.cancel);
			cancel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					closeThisActivity();

				}
			});
			mainActivityFM = getSupportFragmentManager();
			etUserName =(EditText)findViewById(R.id.etUserName);
			last_name =(EditText)findViewById(R.id.last_name);
			userId =(EditText)findViewById(R.id.userid);
			etEmail =(EditText)findViewById(R.id.etEmail);
			terms_conditions=(CheckBox)findViewById(R.id.checkBox);
			ccp = (CountryCodePicker) findViewById(R.id.ccp);
			ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
				@Override
				public void onCountrySelected() {
			/*		Toast.makeText(context, "Updated " + ccp.getSelectedCountryName()+ccp.getSelectedCountryCode(), Toast.LENGTH_SHORT).show();
			*/	}
			});
			/*IntlPhoneInput phoneInputView = (IntlPhoneInput) findViewById(R.id.my_phone_input);*/
		/*mayRequestContacts();*/
			etMobileNUmber =(EditText)findViewById(R.id.etMobileNUmber);
			location =(TextView)findViewById(R.id.location);
			etCOnfPass =(EditText)findViewById(R.id.etCOnfPass);
			btnSignUp =(Button)findViewById(R.id.btnSignUp);
			/*isd_code=(EditText)findViewById(R.id.isd_code);*/
			loginPageLink=(TextView)findViewById(R.id.login_page_link);
			termsOfAgreement=(TextView)findViewById(R.id.termsOfAgreement);
			termsOfAgreement.setPaintFlags(termsOfAgreement.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

			termsOfAgreement.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i=TermsConditions.newInstance(context,GlobalVariables.TYPE_TnC);
					startActivity(i);
				}
			});
			go_to_sing_in = (Button)findViewById(R.id.go_to_sing_in);
			go_to_sing_in.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					closeThisActivity();
				}
			});
			getAddressFromLatLng(activity);
			close_button=(ImageView)findViewById(R.id.close_button);
			close_button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
			btnSignUp.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(is_fb_login)
						registerfbData();
					else
						sendThaData();
				}
			});


			location_content=(LinearLayout)findViewById(R.id.location_content);
			location_content.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent= PickLocation.newInstance(context,GlobalVariables.TYPE_SIGN_UP,true,null);
					startActivityForResult(intent,globalVariables.REQUEST_CODE_FILTER_PICK_LOCATION);
				}
			});
			Intent intent=getIntent();
			if(intent!=null && intent.hasExtra(FB_MODEL_KEY)){
				facebookRegisterModel=(FacebookRegisterModel)intent.getSerializableExtra(FB_MODEL_KEY);
				is_fb_login=true;
				setupFbLogin(facebookRegisterModel);
			}else{
				if(checkIfContactsAccountsPermission())
					showGmailAccountsAvailable();
				/*else
					checkContactsAccountsPermission();*/
			}
		}catch (Exception e){
			e.printStackTrace();
			Log.d(TAG,e.getMessage());
			Crashlytics.log(e.getMessage());
		}
	}

	void sendThaData(){
		String myInternationalNumber;
		if(etUserName.getText().length()==0) {
			etUserName.setError("Please fill the First Name");
			return;
		}else if(TextUtils.isDigitsOnly(etUserName.getText().toString())){
			etUserName.setError("Please enter valid First Name");
			return;
		}else if(last_name.getText().length()==0) {
			last_name.setError("Please fill the Last Name");
			return;
		}else if(TextUtils.isDigitsOnly(last_name.getText().toString())){
			last_name.setError("Please enter valid Last Name");
			return;
		}else if(etEmail.getText().length() == 0) {
			etEmail.setError("Please fill the email");
			return;
		}else  if (!isValidEmail(etEmail.getText().toString())) {
			etEmail.setError("Please Check your Email id!!");
			return;
		}else if(etCOnfPass.getText().length() ==0) {
			etCOnfPass.setError("Please fill the password");
			return;
		}else if(etCOnfPass.getText().length()<5) {
			etCOnfPass.setError("The password must be at least 5 characters");
			return;
		}/*else if(phoneInputView.isValid()) {
			myInternationalNumber = phoneInputView.getNumber();
		}*//*else if(isd_code.getText().length() == 0) {
			isd_code.setError("Please fill the ISD code");
			return;
		}else if(!isd_code.getText().toString().startsWith("+")){
			isd_code.setError("Please enter a valid ISD code");
		}else if(isd_code.getText().toString().startsWith("0")){
			isd_code.setError("Please enter a valid ISD code");
		}*/else if(etMobileNUmber.getText().length() == 0) {
			etMobileNUmber.setError("Please enter a valid Mobile Number");
			return;
		}/*else if(!isValidInteger(etMobileNUmber.getText().toString())) {
			Log.d(TAG,"###############!isValidInteger(etMobileNUmber.getText()");
			etMobileNUmber.setError("Please enter a valid Mobile Number");
			return;
		}*/else if(!isValidMobile(etMobileNUmber.getText().toString())) {
			Log.d(TAG,"###############!sValidMobile(etMobileNUmb(etMobileNUmber.getText()");
			etMobileNUmber.setError("Please enter a valid Mobile Number");
			return;
		}else if(userId.getText().length()==0) {
			userId.setError("Please fill the User Name");
			return;
		}else if(TextUtils.isDigitsOnly(userId.getText().toString())){
			userId.setError("Please enter valid User Name");
			return;
		}else if(!terms_conditions.isChecked()){
			terms_conditions.setError("Please Agree for Terms and Conditions");
			return;
		}else{
			if(GlobalFunctions.isNetworkAvailable(this)){
				verifyUserId(userId.getText().toString());
			}else{
				Snackbar.make(etEmail, "Please check your internet connection", Snackbar.LENGTH_LONG)
						.setAction("Retry", new View.OnClickListener() {
							@Override
							@TargetApi(Build.VERSION_CODES.M)
							public void onClick(View v) {
								verifyUserId(userId.getText().toString());
							}
						}).show();

			}
		}


	}
	public static Boolean isValidInteger(String value) {
		try {
			Integer val = Integer.valueOf(value);
			return val != null;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public final static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}
	private boolean isValidMobile(String phone){
		boolean check=false;
		/*if(phone.length()!=9) {*/
		if(phone.length() < 8 || phone.length() > 12) {
			Log.d(TAG,"################phone.length() < 6 || phone.length() > 15");
			check = false;
			etMobileNUmber.setError("Please enter a valid Mobile Number");
		}/*else if(!phone.startsWith("+")){
			etMobileNUmber.setError("Please enter a valid 9 digit Mobile Number");
		}*/else if(phone.startsWith("0")){
			Log.d(TAG,"######e(phone).startsWith(\"0\"> 15");
			etMobileNUmber.setError("Please enter a valid Mobile Number");
		}else {
			check = true;
			etMobileNUmber.setText(phone);
		}
		return check;
	}


	private void registerData(Context context,String country,String alias_name, String email, String password, String name, String phone,final String isd,String last_name){
		showProgressBar();
		registerModel.setEmail(email);
        registerModel.setPassword(password);
        registerModel.setName(name);
        registerModel.setPhone(phone);
		registerModel.setIsd(isd);
		registerModel.setAlias_name(alias_name);
		registerModel.setCountry(country);
		registerModel.setLast_name(last_name);

		ServicesMethodsManager serverManager = new ServicesMethodsManager();
		serverManager.registerUser(this, registerModel, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
				hideProgressBar();
				OTPCheckerModel otpCheckerModel=(OTPCheckerModel)arg0;
				/*otpCheckerModel.setIsd(isd);*/
				verifyOtp(otpCheckerModel);
				/*UserModel user = (UserModel) arg0;
                validateLogin(user);*/
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
	private void verifyOtp(OTPCheckerModel otpCheckerModel){
		if(!otpCheckerModel.is_otp_verified()){
			GlobalFunctions.setSharedPreferenceInt(context,GlobalVariables.SHARED_PREFERENCE_IS_REGISTRATION_FINISHED,1);
			GlobalFunctions.setSharedPreferenceString(context,GlobalVariables.OTP_CHECK_MODEL,otpCheckerModel.toString());
			startActivity(OTPActivity.newInstance(context,otpCheckerModel));
			closeThisActivity();
		}else{
			MainActivity.closeThisActivity();
			startActivity(new Intent(this,MainActivity.class));
			closeThisActivity();
		}
	}
    private void validateLogin(UserModel user){
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
		registerPushWoosh(context);
		/*Kill the old activity and start the new session when user logged in*/
		MainActivity.closeThisActivity();
		startActivity(new Intent(this,MainActivity.class));
		closeThisActivity();
	}

    public void closeThisActivity(){
        if(activity!=null){activity.finish();}
    }

	@Override
	public void onLocationChanged(Location location) {
		if(location!=null){
			locServices.removeListener();
		}else{
			Toast.makeText(activity, "Fetching Location", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	public void setAddress(String adress){
		this.address=adress;
	}
	public String getAddress(){
		return this.address;
	}
	private void getAddressFromLatLng(Context context){
		//GlobalFunctions.showProgress(context, "Fetching address...");
		Log.d(TAG,"############getAddressFromLatLng#############");
		ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
		servicesMethodsManager.getAddress(context, locServices.getLatitude(),locServices.getLongitude(), new ServerResponseInterface() {
			@Override
			public void OnSuccessFromServer(Object arg0) {
				//GlobalFunctions.hideProgress();
				Log.d(TAG, "onSuccess Response");
				try{
					locationModel =(LocationModel)arg0;
					if(locServices!=null){
						locationModel.setLatitude(Double.toString(locServices.getLatitude()));
						locationModel.setLongitude(Double.toString(locServices.getLongitude()));
					}
					Log.d(TAG,"############LocationModel#############"+locationModel.toString());
					registerModel.setLatitude(locationModel.getLatitude());
					registerModel.setLongitude(locationModel.getLongitude());
					registerModel.setAddress(locationModel.getFormatted_address());
					registerModel.setState(locationModel.getState());
					registerModel.setCity(locationModel.getCity());
					registerModel.setPostalCode(locationModel.getPostal_code());
					facebookRegisterModel.setLatitude(locationModel.getLatitude());
					facebookRegisterModel.setLongitude(locationModel.getLongitude());
					facebookRegisterModel.setAddress(locationModel.getFormatted_address());
					facebookRegisterModel.setState(locationModel.getState());
					facebookRegisterModel.setCity(locationModel.getCity());
					facebookRegisterModel.setPostalCode(locationModel.getPostal_code());
					location.setText(locationModel.getFormatted_address());
					setAddress(locationModel);
				} catch (NullPointerException ex){
					ex.printStackTrace();
					Log.d(TAG,ex.getMessage());
				}catch (Exception e){
					e.printStackTrace();
					Log.d(TAG,e.getMessage());
				}

			}

			@Override
			public void OnFailureFromServer(String msg) {
				//GlobalFunctions.hideProgress();
				Log.d(TAG, msg);
			}
			@Override
			public void OnError(String msg) {
		hideProgressBar();
				Log.d(TAG, msg);
			}
		}, "Delete Sale");
	}
	public void setAddress(LocationModel locationModel){
		setAddress(locationModel.getFormatted_address());
		location.setText(locationModel.getFormatted_address());
	}
	//Override the method here
	@Override
	public void onNewsItemPicked(Item position){
		etUserName.setText(position.getKey());
		etEmail.setText(position.getValue());
		etMobileNUmber.setText(position.getPhoneNumber());

	}
	@Override
	public void onProviderEnabled(String provider) {

	}
	public void showGmailAccountsAvailable(){
		Log.d(TAG,"showGmailAccountsAvailable");
		try {
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
				ft.add(new GoogleAccountsFragment(), GoogleAccountsFragment.TAG);
				ft.commit();
		}catch(Exception e) {
				e.printStackTrace();
		}

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
				Log.d(TAG,"onRequestPermissionsResult");
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					Log.d(TAG,"onRequestPermissionsResult");
					/*etEmail.setText(AppController.getEmail(this));
					etMobileNUmber.setText(AppController.getMobileNumber(this));*/
					showGmailAccountsAvailable();
				} else {
					/*checkContactsAccountsPermission();*/
					if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.GET_ACCOUNTS) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
						checkContactsAccountsPermission();
					}else {
						Snackbar snackbar = Snackbar.make(etCOnfPass, getResources().getString(R.string.contact_permission_text), Snackbar.LENGTH_LONG);
						snackbar.setAction(getResources().getString(R.string.settings), new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								if (activity
										== null) {
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
			// other 'case' lines to check for other
			// permissions this app might request
		}
	}
	private boolean checkIfContactsAccountsPermission() {
		Log.d(TAG,"checkIfContactsAccountsPermission");
		int coarse_location = ContextCompat.checkSelfPermission(activity, Manifest.permission.GET_ACCOUNTS);
		int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS);
		if (coarse_location == PackageManager.PERMISSION_GRANTED && result == PackageManager.PERMISSION_GRANTED) {
			return true;
		} else {
			return false;
		}
	}
	public void showProgressBar(){
		if(progress_bar!=null)
			progress_bar.setVisibility(View.VISIBLE);
	}
	public void hideProgressBar(){
		if(progress_bar!=null)
			progress_bar.setVisibility(View.GONE);
	}
	public static  void registerPushWoosh(Context context){
		Log.d(TAG,"OnSuccess registerPushWoosh#############################");
		if(GlobalFunctions.is_loggedIn(context)){
			//Register receivers for push notifications
			/*registerReceivers();*/

			final PushManager pushManager = PushManager.getInstance(context);

			//Start push manager, this will count app open for Pushwoosh stats as well
			try {
				pushManager.onStartup(context);
			} catch (Exception e) {
				Log.e(TAG, e.getLocalizedMessage());
			}

			//Register for push!
			pushManager.registerForPushNotifications();


			//once u register setTag
			PushActivity.setTags(context);

			//getTags(context);

			//disable the notification builder
			pushManager.setNotificationFactory(new NotificationFactory());

		}

	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(TAG,"on activity result");
		try {
			if(resultCode == activity.RESULT_OK){
				Log.i(TAG,"result ok ");
				if(requestCode == globalVariables.REQUEST_CODE_FILTER_PICK_LOCATION){
					Log.i(TAG,"request code "+globalVariables.REQUEST_CODE_FILTER_PICK_LOCATION);
					locationModel = (LocationModel) data.getExtras().getSerializable(BUNDLE_KEY_SIGN_UP_MODEL_SERIALIZABLE);
					Log.i(TAG,"name pm"+locationModel.getFormatted_address());
					registerModel.setLatitude(locationModel.getLatitude());
					registerModel.setLongitude(locationModel.getLongitude());
					registerModel.setAddress(locationModel.getFormatted_address());
					registerModel.setState(locationModel.getState());
					registerModel.setCity(locationModel.getCity());
					registerModel.setPostalCode(locationModel.getPostal_code());
					location.setText(locationModel.getFormatted_address());
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
	private void verifyUserId(final String alias_name){
		showProgressBar();
		verifyAliasModel.setAlias_name(alias_name);
		//GlobalFunctions.showProgress(context, "Fetching address...");
		ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
		servicesMethodsManager.isUserIdValid(context,verifyAliasModel, new ServerResponseInterface() {
			@Override
			public void OnSuccessFromServer(Object arg0) {
				//GlobalFunctions.hideProgress();
				Log.d(TAG, "onSuccess Response");
				hideProgressBar();
				verifyAliasModel=(VerifyAliasModel)arg0;
				if(verifyAliasModel.isAlias_available()){
					if(is_fb_login)
						registerFb();
					else
						registerData(context,ccp.getSelectedCountryNameCode(),alias_name,etEmail.getText().toString(), etCOnfPass.getText().toString(), etUserName.getText().toString(), etMobileNUmber.getText().toString(),ccp.getSelectedCountryCodeWithPlus(),last_name.getText().toString());
				}else{
					Toast.makeText(context,verifyAliasModel.getMessage(),Toast.LENGTH_LONG).show();
					userId.setError(verifyAliasModel.getMessage());
				}
			}
			@Override
			public void OnFailureFromServer(String msg) {
				//GlobalFunctions.hideProgress();
				Log.d(TAG, msg);
				hideProgressBar();
				Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
			}
			@Override
			public void OnError(String msg) {
				hideProgressBar();
				Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
				hideProgressBar();
				Log.d(TAG, msg);
			}
		}, "Delete Sale");
	}
	private void registerfbData(){
		if(etMobileNUmber.getText().length() == 0) {
			etMobileNUmber.setError("Please enter a valid Mobile Number");
			return;
		}else if(!isValidMobile(etMobileNUmber.getText().toString())) {
			Log.d(TAG,"###############!sValidMobile(etMobileNUmb(etMobileNUmber.getText()");
			etMobileNUmber.setError("Please enter a valid Mobile Number");
			return;
		}else if(userId.getText().length()==0) {
			userId.setError("Please fill the User Name");
			return;
		}else if(TextUtils.isDigitsOnly(userId.getText().toString())){
			userId.setError("Please enter valid User Name");
			return;
		}else if(!terms_conditions.isChecked()){
			terms_conditions.setError("Please Agree for Terms and Conditions");
			return;
		}else{
			if(GlobalFunctions.isNetworkAvailable(this)){
				verifyUserId(userId.getText().toString());
			}else{
				Snackbar.make(etEmail, "Please check your internet connection", Snackbar.LENGTH_LONG)
						.setAction("Retry", new View.OnClickListener() {
							@Override
							@TargetApi(Build.VERSION_CODES.M)
							public void onClick(View v) {
								verifyUserId(userId.getText().toString());
							}
						}).show();

			}
		}

	}
	public void registerFb(){
		facebookRegisterModel.setAlias_name(userId.getText().toString());
		facebookRegisterModel.setPhone(etMobileNUmber.getText().toString());
		facebookRegisterModel.setIsd(ccp.getSelectedCountryCodeWithPlus());
		facebookRegisterModel.setCountry(ccp.getSelectedCountryNameCode());
		showProgressBar();
		ServicesMethodsManager serverManager = new ServicesMethodsManager();
		serverManager.registerFacebookUser(this,facebookRegisterModel, new ServerResponseInterface() {
			@Override
			public void OnSuccessFromServer(Object arg0) {
				hideProgressBar();
				Log.d(TAG, "Object String:"+arg0.toString());
				if(arg0 instanceof OTPCheckerModel){
					OTPCheckerModel otpCheckerModel=(OTPCheckerModel)arg0;
					otpCheckerModel.setIsd(facebookRegisterModel.getIsd());
					verifyOtp(otpCheckerModel);
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
		GlobalFunctions.setSharedPreferenceString(this, GlobalVariables.SHARED_PREFERENCE_COUNTRY, user.getCountry());
		GlobalFunctions.setSharedPreferenceInt(context,GlobalVariables.SHARED_PREFERENCE_IS_OTP_VERIFIED,1);
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
	public void setupFbLogin(FacebookRegisterModel facebookRegisterModel){
		title_tv.setText("Facebook Sign In");
		etCOnfPass.setVisibility(View.GONE);
		etEmail.setVisibility(View.GONE);
		last_name.setVisibility(View.GONE);
		etUserName.setVisibility(View.GONE);
		btnSignUp.setText("Continue");
	}
}
