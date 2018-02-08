package com.blueshak.app.blueshak.garage.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successfull_creation);
        onNext();
        onAnotherItem();
    }

    private void onNext(){
        findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuccessfulCreationActivity.this,FeatureItemPaymentActivity.class);
                startActivityForResult(intent,SUCCESS_FEATURE);
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
