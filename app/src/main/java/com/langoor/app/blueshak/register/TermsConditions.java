package com.langoor.app.blueshak.register;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.langoor.app.blueshak.root.RootActivity;
import com.langoor.blueshak.R;
import com.langoor.app.blueshak.global.GlobalVariables;
import com.langoor.app.blueshak.services.ServerConstants;
import com.langoor.app.blueshak.services.model.ProductModel;
import com.langoor.app.blueshak.support.WebViewFragment;

public class TermsConditions extends RootActivity {
    public static FragmentManager mainActivityFM;
    static final String  PRODUCTDETAIL_BUNDLE_KEY_POSITION = "ProductModelWithDetails";
    private TextView close_button,activity_title;
    private ImageView go_back;
    public static Intent newInstance(Context context, int product){
        Intent mIntent = new Intent(context,TermsConditions.class);
        Bundle bundle = new Bundle();
        bundle.putInt(PRODUCTDETAIL_BUNDLE_KEY_POSITION, product);
        mIntent.putExtras(bundle);
        return mIntent;
    }
    private String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);
        try{
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            LayoutInflater inflator = LayoutInflater.from(this);
            View v = inflator.inflate(R.layout.action_bar_titlel, null);
            activity_title=(TextView)v.findViewById(R.id.title);
            toolbar.addView(v);
            close_button=(TextView)v.findViewById(R.id.cancel);
            close_button.setVisibility(View.GONE);
            go_back=(ImageView)findViewById(R.id.go_back);
            go_back.setVisibility(View.VISIBLE);
            go_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            mainActivityFM=getSupportFragmentManager();
            if(getIntent()!=null){
                if(getIntent().getExtras().getInt(PRODUCTDETAIL_BUNDLE_KEY_POSITION)== GlobalVariables.TYPE_TnC){
                    title="Terms & Conditions";
                    TermsConditions.replaceFragment(WebViewFragment.newInstance(this, ServerConstants.URL_Blueshak_Support_Url),WebViewFragment.TAG);
                }else if(getIntent().getExtras().getInt(PRODUCTDETAIL_BUNDLE_KEY_POSITION)== GlobalVariables.TYPE_SUPPORT){
                    title="Help Center";
                    TermsConditions.replaceFragment(WebViewFragment.newInstance(this, ServerConstants.URL_Blueshak_Support_Url),WebViewFragment.TAG);
                }else if(getIntent().getExtras().getInt(PRODUCTDETAIL_BUNDLE_KEY_POSITION)== GlobalVariables.TYPE_privacy_policy){
                    title="Privacy Policy";
                    TermsConditions.replaceFragment(WebViewFragment.newInstance(this, ServerConstants.URL_privacy_policy),WebViewFragment.TAG);
                }
            }
            activity_title.setText(title);
        }catch (Exception e){
            e.printStackTrace();
            Crashlytics.log(e.getMessage());
        }

    }
    public static void replaceFragment(Fragment fragment, @NonNull String tag){
        if(mainActivityFM!=null){mainActivityFM.beginTransaction().replace(R.id.terms_conditions_frame,fragment, tag).commit();}
    }
    public  void setActionBarTitle(String title) {
        activity_title.setText(title);
    }
}
