package com.langoor.app.blueshak.otp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.hbb20.CountryCodePicker;
import com.langoor.app.blueshak.Messaging.activity.PushActivity;
import com.langoor.app.blueshak.Messaging.helper.NotificationFactory;
import com.langoor.app.blueshak.Messaging.util.CommonUtil;
import com.langoor.app.blueshak.services.model.UserModel;
import com.langoor.blueshak.R;
import com.langoor.app.blueshak.MainActivity;
import com.langoor.app.blueshak.global.GlobalFunctions;
import com.langoor.app.blueshak.global.GlobalVariables;
import com.langoor.app.blueshak.login.LoginActivity;
import com.langoor.app.blueshak.root.RootActivity;
import com.langoor.app.blueshak.services.ServerResponseInterface;
import com.langoor.app.blueshak.services.ServicesMethodsManager;
import com.langoor.app.blueshak.services.model.OTPCheckerModel;
import com.langoor.app.blueshak.services.model.OTPResendModel;
import com.langoor.app.blueshak.services.model.StatusModel;
import com.pushwoosh.PushManager;


public class OTPActivity extends RootActivity {
    private static final String TAG = "OTPActivity";
    private TextView signup_text,otp_get_another,otp_try_again,cancel,titile;
    private OTPCheckerModel otpCheckerModel= new OTPCheckerModel();
    public static final String OTP_BUNDLE_KEY = "otpbundlekey";
	private EditText etEmail,etPhone_number;
	private Button btnSignIn,resend_otp;
	private LinearLayout resend_otp_content,verify_otp_content;
    private static Activity activity;
    private static Context context;
    private ImageView close_button;
    private CountryCodePicker ccp;
    private ProgressBar progress_bar;
    public static Intent newInstance(Context context,OTPCheckerModel otpCheckerModel){
        Intent mIntent = new Intent(context, OTPActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(OTP_BUNDLE_KEY, otpCheckerModel);
        mIntent.putExtras(bundle);
        return mIntent;
    }
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.otp_screen);
        activity = this;
        context=this;
        Intent intent=getIntent();
        verify_otp_content=(LinearLayout)findViewById(R.id.verify_otp_content);
        resend_otp_content=(LinearLayout)findViewById(R.id.resend_otp_content);
        titile=(TextView)findViewById(R.id.titile);
        resend_otp=(Button)findViewById(R.id.resend_otp);
        resend_otp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etPhone_number.getText().length() == 0) {
                    etPhone_number.setError("Please enter a Mobile Number");
                    return;
                }else if(!isValidMobile(etPhone_number.getText().toString())) {
                    etPhone_number.setError("Please enter a valid Mobile Number");
                    return;
                }else{
                    reSendRequestToServer(etPhone_number.getText().toString().trim(),ccp.getSelectedCountryCodeWithPlus());
                }
            }
        });
        etEmail =(EditText)findViewById(R.id.etEmail);
		btnSignIn = (Button)findViewById(R.id.btnSignIn);
        otp_get_another=(TextView) findViewById(R.id.otp_get_another);
        ccp=(CountryCodePicker)findViewById(R.id.isd_code);
        etPhone_number=(EditText)findViewById(R.id.etMobileNUmber);
      /*  disablePhone();*/
        otp_try_again=(TextView) findViewById(R.id.otp_try_again);
        close_button=(ImageView)findViewById(R.id.close_button);
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Update the action bar title with the TypefaceSpan instance
        LayoutInflater inflator = LayoutInflater.from(this);
        View v = inflator.inflate(R.layout.action_bar_titlel_search, null);
        ImageView go_to_filter=(ImageView)v.findViewById(R.id.go_to_filter);
        go_to_filter.setVisibility(View.GONE);
        ImageView back=(ImageView)v.findViewById(R.id.go_back);
        back.setVisibility(View.GONE);
        ((TextView)v.findViewById(R.id.title)).setText("Verification Code");
        toolbar.addView(v);
       /* cancel=(TextView)v.findViewById(R.id.cancel);
        cancel.setVisibility(View.GONE);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeThisActivity();
            }
        });*/
        progress_bar=(ProgressBar)findViewById(R.id.progress_bar);
        etEmail =(EditText)findViewById(R.id.etEmail);
        btnSignIn = (Button)findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etPhone_number.getText().length() == 0) {
                    etPhone_number.setError("Please enter a Mobile Number");
                    return;
                }else if(!isValidMobile(etPhone_number.getText().toString())) {
                    etPhone_number.setError("Please enter a valid Mobile Number");
                    return;
                }else if(TextUtils.isEmpty(etEmail.getText().toString())){
                    etEmail.setError("Please enter a Otp");
                    return;
                }else{
                    sendRequestToServer(activity,etEmail.getText().toString(),etPhone_number.getText().toString());
                }
            }
        });
        close_button=(ImageView)findViewById(R.id.close_button);
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeThisActivity();
            }
        });
      /*  if(email!=null){etEmail.setText(email);}*/
        btnSignIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
                if(etPhone_number.getText().length() == 0) {
                    etPhone_number.setError("Please enter a Mobile Number");
                    return;
                }else if(!isValidMobile(etPhone_number.getText().toString())) {
                    etPhone_number.setError("Please enter a valid Mobile Number");
                    return;
                }else if(TextUtils.isEmpty(etEmail.getText().toString())){
                    etEmail.setError("Please enter a Otp");
                    return;
                }else{
                    sendRequestToServer(activity,etEmail.getText().toString().trim(),etPhone_number.getText().toString().trim());
                }

			}
		});
        if(intent!=null && intent.hasExtra(OTP_BUNDLE_KEY)){
            otpCheckerModel=(OTPCheckerModel) intent.getSerializableExtra(OTP_BUNDLE_KEY);
            if(otpCheckerModel!=null){
                if(otpCheckerModel.getIsd()!=null)
                    ccp.setCountryForPhoneCode(Integer.parseInt(otpCheckerModel.getIsd()));
                etPhone_number.setText(otpCheckerModel.getPhone());
            }

        }
        String otp_get_another1="Didn't receive one? Resend verification code.";

        SpannableString ss = new SpannableString(otp_get_another1);
        ss.setSpan(new MyClickableSpan(),20,otp_get_another1.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        otp_get_another.setText(ss);
        otp_get_another.setMovementMethod(LinkMovementMethod.getInstance());

        String otp_try_another="Entered the wrong Phone number? Edit.";


        SpannableString ss1 = new SpannableString(otp_try_another);

        ss1.setSpan(new MyClickableSpan(),32,otp_try_another.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        otp_try_again.setText(ss1);
        otp_try_again.setMovementMethod(LinkMovementMethod.getInstance());
	}

    class MyClickableSpan extends ClickableSpan { //clickable span
        public void onClick(View textView) {
            if(textView.getId()== R.id.otp_get_another){
                if(etPhone_number.getText().length() == 0) {
                    etPhone_number.setError("Please enter a Mobile Number");
                    return;
                }else if(!isValidMobile(etPhone_number.getText().toString())) {
                    etPhone_number.setError("Please enter a valid Mobile Number");
                    return;
                }else{
                    reSendRequestToServer(etPhone_number.getText().toString().trim(),ccp.getSelectedCountryCodeWithPlus());
                }

            }else if(textView.getId()== R.id.otp_try_again){
                enablePhone();
            }else {
                if(etPhone_number.getText().length() == 0) {
                    etPhone_number.setError("Please enter a Mobile Number");
                    return;
                }else if(!isValidMobile(etPhone_number.getText().toString())) {
                    etPhone_number.setError("Please enter a valid Mobile Number");
                    return;
                }else if(TextUtils.isEmpty(etEmail.getText().toString())){
                    etEmail.setError("Please enter a Otp");
                    return;
               }else{
                    sendRequestToServer(activity,etEmail.getText().toString(),etPhone_number.getText().toString());
                }

            }
        }
        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(activity.getResources().getColor(R.color.brandColor));//set text color
            ds.setUnderlineText(true);
            //ds.setTypeface(Typeface.DEFAULT_BOLD);
        }
    }
	private void sendRequestToServer(Context context, String otp,String phone){
        showProgressBar();
        otpCheckerModel.setPhone(phone);
        otpCheckerModel.setOtp(otp);
        ServicesMethodsManager serverManager = new ServicesMethodsManager();
        serverManager.verifyOTP(context, otpCheckerModel, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                hideProgressBar();
               /* validateOTPSignin((StatusModel) arg0);*/
                UserModel user = (UserModel) arg0;
                validateLogin(user);
            }

            @Override
            public void OnFailureFromServer(String msg) {
                 hideProgressBar();
                Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnError(String msg) {
                 hideProgressBar();
                Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_SHORT).show();
            }
        },"OTP verification");

	}
    private void validateLogin(UserModel user){
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
        registerPushWoosh(context);
		/*Kill the old activity and start the new session when user logged in*/
        MainActivity.closeThisActivity();
        startActivity(new Intent(this,MainActivity.class));
        closeThisActivity();
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

    private void validateOTPSignin(StatusModel statusModel){
        if(statusModel.isStatus()){
         proceedToMainActivity();
        }else{Toast.makeText(getApplicationContext(),"Error on processing OTP, try resending OTP", Toast.LENGTH_SHORT).show();}
    }

    private void validateOTPResend(OTPCheckerModel statusModel){
        if(statusModel.isStatus()){
            resend_otp_content.setVisibility(View.GONE);
            verify_otp_content.setVisibility(View.VISIBLE);
            titile.setText("We have sent you OTP code to verify your phone number");
            GlobalFunctions.setSharedPreferenceString(context,GlobalVariables.OTP_CHECK_MODEL,statusModel.toString());
            Toast.makeText(getApplicationContext(),"OTP Resent Successfully", Toast.LENGTH_SHORT).show();
           if(etEmail!=null){etEmail.setText("");}
        }else{Toast.makeText(getApplicationContext(),"Already Authenticated", Toast.LENGTH_SHORT).show();}
    }


	private void reSendRequestToServer(String phone,String isd) {
        showProgressBar();
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        OTPResendModel resendModel = new OTPResendModel();
        resendModel.setBs_id(otpCheckerModel.getBs_id());
        resendModel.setIsd(isd);
        resendModel.setPhone(phone);
        servicesMethodsManager.resendOTP(getApplicationContext(), resendModel, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                 hideProgressBar();
                 validateOTPResend((OTPCheckerModel)arg0);
            }

            @Override
            public void OnFailureFromServer(String msg) {
                 hideProgressBar();
                Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnError(String msg) {
                 hideProgressBar();
                Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_SHORT).show();
            }
        },"Resend OTP");
	}

    public static void closeThisActivity(){
        if(activity!=null){activity.finish();}
    }
    public void showProgressBar(){
        if(progress_bar!=null)
            progress_bar.setVisibility(View.VISIBLE);
    }
    public void hideProgressBar(){
        if(progress_bar!=null)
            progress_bar.setVisibility(View.GONE);
    }

    public void proceedToMainActivity(){
        MainActivity.closeThisActivity();
        startActivity(new Intent(this,MainActivity.class));
        closeThisActivity();
    }
    private void disablePhone(){
        etPhone_number.setVisibility(View.GONE);
        ccp.setVisibility(View.GONE);
      /*  etPhone_number.setEnabled(false);
        ccp.setEnabled(false);
        ccp.setFocusable(false);
        ccp.setClickable(false);
        ccp.setFocusableInTouchMode(false);
        ccp.setActivated(false);*/
    }
    private void enablePhone(){
       /* etPhone_number.setVisibility(View.VISIBLE);
        ccp.setVisibility(View.VISIBLE);
        etPhone_number.setEnabled(true);
        etPhone_number.setFocusableInTouchMode(true);
        etPhone_number.requestFocus();
        ccp.setEnabled(true);
        ccp.setFocusable(true);
        ccp.setClickable(true);*/
        verify_otp_content.setVisibility(View.GONE);
        resend_otp_content.setVisibility(View.VISIBLE);
        titile.setText("Please fill the phone number to resend otp");
        etPhone_number.requestFocus();
        etPhone_number.setEnabled(true);
        etPhone_number.setFocusableInTouchMode(true);
    }
    private boolean isValidMobile(String phone){
        boolean check=false;
		/*if(phone.length()!=9) {*/
        if(phone.length() < 8 || phone.length() > 12) {
            Log.d(TAG,"################phone.length() < 6 || phone.length() > 15");
            check = false;
            etPhone_number.setError("Please enter a valid Mobile Number");
        }/*else if(!phone.startsWith("+")){
			etMobileNUmber.setError("Please enter a valid 9 digit Mobile Number");
		}*/else if(CommonUtil.getPhone(phone).startsWith("0")){
            Log.d(TAG,"######e(phone).startsWith(\"0\"> 15");
            etPhone_number.setError("Please enter a valid Mobile Number");
        }else {
            check = true;
            etPhone_number.setText(phone);
        }
        return check;
    }
}
