package com.blueshak.app.blueshak.util;

import java.util.ArrayList;
 
import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.blueshak.blueshak.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class FullScreenImageAdapter extends PagerAdapter {
 
    private Activity _activity;
    private ArrayList<String> _imagePaths;
    private LayoutInflater inflater;
    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    // constructor
    public FullScreenImageAdapter(Activity activity, ArrayList<String> imagePaths) {
        this._activity = activity;
        this._imagePaths = imagePaths;

         options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.placeholder_background)
                .showImageOnFail(R.drawable.placeholder_background)
                .showImageOnLoading(R.drawable.placeholder_background).build();
        //download and display image from url

    }
 
    @Override
    public int getCount() {
        return this._imagePaths.size();
    }
 
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }
     
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imgDisplay;

  
        inflater = (LayoutInflater) _activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.content_view, container,
                false);
  
        imgDisplay = (ImageView) viewLayout.findViewById(R.id.image);

     /*   BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(_imagePaths.get(position), options);
        imgDisplay.setImageBitmap(bitmap);*/
        ImageLoader.getInstance().displayImage(_imagePaths.get(position).toString(),imgDisplay,  options,null);
        // close button click event

        ((ViewPager) container).addView(viewLayout);
  
        return viewLayout;
    }
     
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);
  
    }
}