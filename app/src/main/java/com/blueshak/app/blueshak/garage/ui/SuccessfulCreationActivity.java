package com.blueshak.app.blueshak.garage.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.blueshak.app.blueshak.garage.CreateItemSaleFragment;
import com.blueshak.app.blueshak.garage.CreateSaleActivity;
import com.blueshak.app.blueshak.global.GlobalVariables;
import com.blueshak.app.blueshak.services.model.CreateImageModel;
import com.blueshak.app.blueshak.util.BlueShakLog;
import com.blueshak.blueshak.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.util.ArrayList;

public class SuccessfulCreationActivity extends AppCompatActivity {

    private static String TAG = "SuccessfulCreationActivity";
    public static final int SUCCESS_FEATURE = 50;
    public static String PRODUCTID = "PRODUCTID";
    public static String FEATURE_FAG = "feature_fag";
    public static String FEATURE_FIRST_IMAGE = "Feature_First_Image";
    private String mProductId;
    private boolean isFeaturedFlag = false;
    private Button button;
    private String featureFirstImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successfull_creation);
        init();
        onNext();
        onAnotherItem();
        Bundle bundle = getIntent().getExtras();

        if(bundle!=null){
            mProductId = bundle.getString(PRODUCTID);
            isFeaturedFlag = bundle.getBoolean(FEATURE_FAG);
            featureFirstImage = bundle.getString(FEATURE_FIRST_IMAGE);
        }

        if(isFeaturedFlag){
            button.setText(getString(R.string.done));
        }

    }

    private void init(){
        button = (Button)findViewById(R.id.btn_next);
    }

    private void onNext(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isFeaturedFlag){
                    Intent intent = new Intent(SuccessfulCreationActivity.this,FeatureItemPaymentActivity.class);
                    intent.putExtra(FeatureItemPaymentActivity.PRODUCTID,mProductId);
                    intent.putExtra(FeatureItemPaymentActivity.FEATURE_FAG,isFeaturedFlag);
                    intent.putExtra(FeatureItemPaymentActivity.FEATURE_FIRST_IMAGE,featureFirstImage);
                    startActivityForResult(intent,SUCCESS_FEATURE);
                }else{
                    finish();
                    closeCreateSaleActivity();
                }
            }
        });
    }

    private void onAnotherItem(){
        findViewById(R.id.txt_another_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setResult(RESULT_CANCELED);
                Intent i = CreateSaleActivity.newInstance(SuccessfulCreationActivity.this, null,
                        null, null, GlobalVariables.TYPE_HOME, GlobalVariables.TYPE_ITEM);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if(requestCode == SUCCESS_FEATURE){

            }

        }else{
            finish();
            closeCreateSaleActivity();
        }

    }

    private void closeCreateSaleActivity(){
        if(CreateSaleActivity.activity!=null){
            CreateSaleActivity.activity.finish();
        }
    }

}
