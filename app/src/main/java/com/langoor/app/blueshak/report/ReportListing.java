package com.langoor.app.blueshak.report;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.langoor.blueshak.R;
import com.langoor.app.blueshak.global.GlobalFunctions;
import com.langoor.app.blueshak.root.RootActivity;
import com.langoor.app.blueshak.services.ServerResponseInterface;
import com.langoor.app.blueshak.services.ServicesMethodsManager;
import com.langoor.app.blueshak.services.model.ErrorModel;
import com.langoor.app.blueshak.services.model.MarkInApproModel;
import com.langoor.app.blueshak.services.model.ProductModel;
import com.langoor.app.blueshak.services.model.SalesModel;
import com.langoor.app.blueshak.services.model.StatusModel;

public class ReportListing extends RootActivity {
    private static final String TAG = "ReportListing";
    private static Context context;
    private static Activity activity;
    private static final String  PRODUCTDETAIL_BUNDLE_KEY_POSITION = "ProductModelWithDetails";
    private static final String IS_ITEM = "IS_ITEM";
    private RadioButton radioButton,radioButton1,radioButton2,radioButton3,radioButton4,radioButton5,radioButton6
            ,   radioButton7,radioButton8,radioButton9;
    private TextView close_button;
    private ImageView go_back;
    private EditText pd_description;
    private String id;
    private boolean is_item;
    private MarkInApproModel markInApproModel= new MarkInApproModel();
    private ProgressBar progress_bar;
    public static Intent newInstance(Context context,String product,boolean is_item){
        Intent mIntent = new Intent(context, ReportListing.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(PRODUCTDETAIL_BUNDLE_KEY_POSITION, product);
        bundle.putSerializable(IS_ITEM,is_item);
        mIntent.putExtras(bundle);
        return mIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_listing);
        try{
            context=this;
            activity=this;
            progress_bar=(ProgressBar)findViewById(R.id.progress_bar);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            LayoutInflater inflator = LayoutInflater.from(this);
            View v = inflator.inflate(R.layout.action_bar_titlel, null);
            ((TextView)v.findViewById(R.id.title)).setText("Report Listing");
            toolbar.addView(v);
            close_button=(TextView)v.findViewById(R.id.cancel);
            close_button.setText("Save");
            close_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getReportText();
                }
            });
            pd_description=(EditText)findViewById(R.id.pd_description);
            go_back=(ImageView)findViewById(R.id.go_back);
            go_back.setVisibility(View.VISIBLE);
            go_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            radioButton=(RadioButton)findViewById(R.id.radioButton);
            radioButton1=(RadioButton)findViewById(R.id.radioButton1);
            radioButton2=(RadioButton)findViewById(R.id.radioButton2);
            radioButton3=(RadioButton)findViewById(R.id.radioButton3);
            radioButton4=(RadioButton)findViewById(R.id.radioButton4);
            radioButton5=(RadioButton)findViewById(R.id.radioButton5);
            radioButton6=(RadioButton)findViewById(R.id.radioButton6);
            radioButton7=(RadioButton)findViewById(R.id.radioButton7);
            radioButton8=(RadioButton)findViewById(R.id.radioButton8);
            radioButton9=(RadioButton)findViewById(R.id.radioButton9);
            if (getIntent().hasExtra(PRODUCTDETAIL_BUNDLE_KEY_POSITION))
                id = getIntent().getExtras().getString(PRODUCTDETAIL_BUNDLE_KEY_POSITION);
            if (getIntent().hasExtra(IS_ITEM))
                is_item = getIntent().getExtras().getBoolean(IS_ITEM);
            if(is_item){
                    Log.d(TAG,"is_item="+id);
                    markInApproModel.setIs_item(true);
                    markInApproModel.setProduct_id(id);
            }else {
                    Log.d(TAG,"is_Sale="+id);
                    markInApproModel.setSale_id(id);
                    markInApproModel.setIs_item(false);
            }
        }catch (Exception e){
            e.printStackTrace();
            Crashlytics.log(e.getMessage());
        }
    }
    private void getReportText() {
         if(radioButton.isChecked()){
            reportListing(radioButton.getText().toString());
        }else if(radioButton1.isChecked()){
             reportListing(radioButton1.getText().toString());
        }else if(radioButton2.isChecked()){
             reportListing(radioButton2.getText().toString());
        }else if(radioButton3.isChecked()){
             reportListing(radioButton3.getText().toString());
        }else if(radioButton4.isChecked()){
             reportListing(radioButton4.getText().toString());
        }else if(radioButton5.isChecked()){
             reportListing(radioButton5.getText().toString());
        }else if(radioButton6.isChecked()){
             reportListing(radioButton6.getText().toString());
        }else if(radioButton7.isChecked()){
             reportListing(radioButton7.getText().toString());
        }else if(radioButton8.isChecked()){
             reportListing(radioButton8.getText().toString());
        }else if(radioButton9.isChecked()){
             String text=pd_description.getText().toString();
             if(!TextUtils.isEmpty(text))
                reportListing(text);
             else
                 Toast.makeText(context,"Please mention the reason for reporting",Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(context,"Please select reason for reporting",Toast.LENGTH_LONG).show();
        }

    }
    private void reportListing(String content){
        markInApproModel.setContent(content);
      showProgressBar();
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.markInappropirate(context,markInApproModel, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
              hideProgressBar();
                Log.d(TAG, "onSuccess Response");
                if(arg0 instanceof StatusModel){
                    StatusModel statusModel = (StatusModel) arg0;
                    if(statusModel.isStatus()){
                            Toast.makeText(context, context.getResources().getString(R.string.marked_inappropriate_success_message), Toast.LENGTH_LONG).show();
                        finish();
                    }
                }else if(arg0 instanceof ErrorModel){
                     hideProgressBar();
                    ErrorModel errorModel = (ErrorModel) arg0;
                    String msg = errorModel.getError()!=null ? errorModel.getError() : errorModel.getMessage();
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                }
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
        }, "Similar Products");

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
