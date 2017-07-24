package com.blueshak.app.blueshak.item;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.blueshak.app.blueshak.util.FullScreenImageAdapter;
import com.blueshak.blueshak.R;
import com.blueshak.app.blueshak.root.RootActivity;
import com.blueshak.app.blueshak.services.model.ImageModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

public class ViewActivity extends RootActivity {
    private static final String TAG = "ImageViewActivty";
    private static Context context;
    private static Activity activity;
    private static final String  PRODUCTDETAIL_BUNDLE_KEY_POSITION = "ProductModelWithDetails";
    private ImageModel imageModel=null;
    private ImageView image;
    private TextView close_button;
    private ImageView go_back;
    ViewPager  viewPager;
    FullScreenImageAdapter myPagerAdapter;
    public static ArrayList<String> displayImageURL = new ArrayList<String>();
    public  static int selectedI=0;
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
            //image=(ImageView)findViewById(R.id.image);
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

          /*  if(getIntent().hasExtra(PRODUCTDETAIL_BUNDLE_KEY_POSITION))
                imageModel=(ImageModel)getIntent().getExtras().getSerializable(PRODUCTDETAIL_BUNDLE_KEY_POSITION);
            if(imageModel!=null)
                setImageOnView();*/
        }catch (Exception e){
            e.printStackTrace();
            Crashlytics.log(e.getMessage());
            Log.d(TAG,e.getMessage());
        }

          viewPager = (ViewPager)findViewById(R.id.pager);
        viewPager.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                viewPager.setCurrentItem(selectedI, true);
            }
        }, 100);
        //viewPager.setCurrentItem(1, false);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
            myPagerAdapter = new FullScreenImageAdapter(this,displayImageURL);
       //  myPagerAdapter.set
           viewPager.setAdapter(myPagerAdapter);

        indicator.setViewPager(viewPager);
        myPagerAdapter.registerDataSetObserver(indicator.getDataSetObserver());
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
