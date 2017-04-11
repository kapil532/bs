package com.langoor.app.blueshak.forgot_password;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.langoor.blueshak.R;
import com.langoor.app.blueshak.AppController;
import com.langoor.app.blueshak.global.GlobalFunctions;
import com.langoor.app.blueshak.login.LoginActivity;
import com.langoor.app.blueshak.root.RootActivity;
import com.langoor.app.blueshak.services.ServerResponseInterface;
import com.langoor.app.blueshak.services.ServicesMethodsManager;
import com.langoor.app.blueshak.services.model.ForgotPasswordModel;
import com.langoor.app.blueshak.services.model.StatusModel;

public class ForgotPasswordActivity extends RootActivity {
    private TextView cancel;
	private EditText etEmail;
	private Button btnSignIn;
    private static Activity activity;
    private static  Context context;
    private ImageView close_button;
    private ProgressBar progress_bar;
    @Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_screen);
        try{
            activity = this;
            context=this;
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            // Update the action bar title with the TypefaceSpan instance
            LayoutInflater inflator = LayoutInflater.from(this);
            View v = inflator.inflate(R.layout.action_bar_titlel, null);
            ((TextView)v.findViewById(R.id.title)).setText(/*this.getTitle()*/"Reset Password");
            toolbar.addView(v);
            cancel=(TextView)v.findViewById(R.id.cancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeThisActivity();
                }
            });

            progress_bar=(ProgressBar)findViewById(R.id.progress_bar);
            etEmail =(EditText)findViewById(R.id.etEmail);
            btnSignIn = (Button)findViewById(R.id.btnSignIn);
            btnSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    sendRequestToServer(activity,etEmail.getText().toString());
                }
            });
            close_button=(ImageView)findViewById(R.id.close_button);
            close_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeThisActivity();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            Crashlytics.log(e.getMessage());
        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
	private void sendRequestToServer(Context context, String email) {
        if(etEmail.getText().length() == 0) {
            etEmail.setError("Please fill the email");
            return;
        }else  if (!isValidEmail(etEmail.getText().toString())) {
            etEmail.setError("Please Check your Email id!!");
            return;
        }
       showProgressBar();
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        ForgotPasswordModel forgotPasswordModel = new ForgotPasswordModel(email);
        servicesMethodsManager.forgotPassword(context, forgotPasswordModel, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                hideProgressBar();
                validateResult((StatusModel) arg0);
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
        }, "Forgot Password");
	}
    @Override
    public void onResume() {
        super.onResume();
    }
    private void validateResult(StatusModel statusModel){
        if(statusModel.isStatus()){
            Toast.makeText(activity,"Reset password link is sent to your E-mail",Toast.LENGTH_LONG).show();
            closeThisActivity();
        }else{Toast.makeText(getApplicationContext(),"Error on Generating Password, try again latter", Toast.LENGTH_SHORT).show();}
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
}
