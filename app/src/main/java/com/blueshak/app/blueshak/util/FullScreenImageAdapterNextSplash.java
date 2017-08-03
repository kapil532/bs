package com.blueshak.app.blueshak.util;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blueshak.blueshak.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class FullScreenImageAdapterNextSplash extends PagerAdapter {
    String[] titleA={"Find Stuff. Fast.","Sell stuff,for free.","Talk"};
    String subTitleA[]={"Find new sales and stuff close to you.","Easily add your own items or create a sale. It’s Free!","Don’t change this for the meantime. Ertan had in mind to have theme of “Fire Sale”, but the graphics do not match?"};
    int[] displaypictures = {R.drawable.splash_1,R.drawable.splash_2, R.drawable.splash_3};
    private Activity _activity;
    private LayoutInflater inflater;
    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    // constructor
    public FullScreenImageAdapterNextSplash(Activity activity) {
        this._activity = activity;

         options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.placeholder_background)
                .showImageOnFail(R.drawable.placeholder_background)
                .showImageOnLoading(R.drawable.placeholder_background).build();
        //download and display image from url

    }
 
    @Override
    public int getCount() {
        return 3;
    }
 
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }
     
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imgDisplay;
       TextView title;
        TextView titleSub;
  
        inflater = (LayoutInflater) _activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.content_view_splas, container,
                false);

        titleSub = (TextView) viewLayout.findViewById(R.id.subtitle);
        title = (TextView) viewLayout.findViewById(R.id.title);
        imgDisplay = (ImageView) viewLayout.findViewById(R.id.image);
        title.setText(titleA[position]);
        titleSub.setText(subTitleA[position]);
        imgDisplay.setImageResource(displaypictures[position]);

        ((ViewPager) container).addView(viewLayout);
  
        return viewLayout;
    }
     
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);
  
    }
}