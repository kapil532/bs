package com.langoor.app.blueshak.item;

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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.daimajia.slider.library.SliderLayout;
import com.langoor.blueshak.R;
import com.langoor.app.blueshak.root.RootActivity;
import com.langoor.app.blueshak.services.model.ImageModel;
import com.langoor.app.blueshak.services.model.ProductModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

public class ViewActivity extends RootActivity {
    private static final String TAG = "ImageViewActivty";
    private static Context context;
    private static Activity activity;
    private static final String  PRODUCTDETAIL_BUNDLE_KEY_POSITION = "ProductModelWithDetails";
    private ImageModel imageModel=null;
    private ImageView image;
    private TextView close_button;
    private ImageView go_back;

    public static Intent newInstance(Context context, ImageModel product){
        Intent mIntent = new Intent(context, ViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(PRODUCTDETAIL_BUNDLE_KEY_POSITION, product);
        mIntent.putExtras(bundle);
        return mIntent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        try{
            context=this;
            activity=this;
            image=(ImageView)findViewById(R.id.image);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            LayoutInflater inflator = LayoutInflater.from(this);
            View v = inflator.inflate(R.layout.action_bar_titlel, null);
            ((TextView)v.findViewById(R.id.title)).setText("Image");
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

            if(getIntent().hasExtra(PRODUCTDETAIL_BUNDLE_KEY_POSITION))
                imageModel=(ImageModel)getIntent().getExtras().getSerializable(PRODUCTDETAIL_BUNDLE_KEY_POSITION);
            if(imageModel!=null)
                setImageOnView();
        }catch (Exception e){
            e.printStackTrace();
            Crashlytics.log(e.getMessage());
            Log.d(TAG,e.getMessage());
        }


    }
    private void setImageOnView(){
        String image_=imageModel.getImage();
        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.placeholder_background)
                .showImageOnFail(R.drawable.placeholder_background)
                .showImageOnLoading(R.drawable.placeholder_background).build();
        //download and display image from url
        imageLoader.displayImage(image_,image, options);
    }
}
