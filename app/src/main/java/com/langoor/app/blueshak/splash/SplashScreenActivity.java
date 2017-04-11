package com.langoor.app.blueshak.splash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import com.langoor.app.blueshak.otp.OTPActivity;
import com.langoor.app.blueshak.services.model.OTPCheckerModel;
import com.langoor.blueshak.R;
import com.langoor.app.blueshak.MainActivity;
import com.langoor.app.blueshak.global.GlobalFunctions;
import com.langoor.app.blueshak.global.GlobalVariables;
import com.langoor.app.blueshak.root.RootActivity;
import com.pushwoosh.PushManager;

public class SplashScreenActivity extends RootActivity {
	private final int SPLASH_SHOW_TIME = 1000;
	private static Activity activity;
	private static Context context;
	private static final String LTAG = "PushwooshNotification";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity=this;
		context=this;
		setContentView(R.layout.activity_splash_screen);
		new BackgroundSplashTask().execute();

	}
	/**
	 * Async Task: can be used to load DB, images during which the splash screen
	 * is shown to user
	 */
	private class BackgroundSplashTask extends AsyncTask<Void, Void , Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				Thread.sleep(SPLASH_SHOW_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			int is_first_time=GlobalFunctions.getSharedPreferenceInt(activity, GlobalVariables.SHARED_PREFERENCE_FIRST_TIME);
			if(is_first_time==0) {
				try{
					if(PushManager.getInstance(context)!=null){
						Log.d(LTAG,"");
						PushManager.getInstance(context).unregisterForPushNotifications();
					}
				}
				catch (Exception e){
					e.printStackTrace();
					Log.d(LTAG,"");
				}
				Intent i = new Intent(SplashScreenActivity.this,NextSplashScreen.class);
				startActivity(i);
				closeThisActivity();
			}else{
				OTPCheckerModel otpCheckerModel =new OTPCheckerModel();
				String filter_string=GlobalFunctions.getSharedPreferenceString(context,GlobalVariables.OTP_CHECK_MODEL);
				int otp_verified=GlobalFunctions.getSharedPreferenceInt(activity, GlobalVariables.SHARED_PREFERENCE_IS_OTP_VERIFIED);
				if(filter_string!=null){
					otpCheckerModel.toObject(filter_string);
					if(otp_verified==0){
						startActivity(OTPActivity.newInstance(context,otpCheckerModel));
						closeThisActivity();
					}else{
						proceedToMainActivity();
					}
				}else{
					proceedToMainActivity();
				}
			}
		}
	}
	public void proceedToMainActivity(){
		Intent i = new Intent(SplashScreenActivity.this,MainActivity.class);
		startActivity(i);
		closeThisActivity();
	}

	public static void closeThisActivity(){
		if(activity!=null){activity.finish();}
	}

}