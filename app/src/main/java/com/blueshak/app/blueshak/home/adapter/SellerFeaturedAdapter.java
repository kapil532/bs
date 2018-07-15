package com.blueshak.app.blueshak.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blueshak.app.blueshak.home.SalesListAdapterNew;
import com.blueshak.app.blueshak.login.LoginActivity;
import com.blueshak.app.blueshak.seller.model.SellerData;
import com.blueshak.app.blueshak.util.BlueShakLog;
import com.blueshak.blueshak.R;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by lsingh013 on 24/05/18.
 */

public class SellerFeaturedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
        ViewPagerEx.OnPageChangeListener {
    private static String TAG = "SalesListAdapterNew";
    private Context context;
    private List<SellerData> sellerDataList;
    protected boolean showLoader;
    private static final int VIEWTYPE_ITEM = 1;
    private static final int VIEWTYPE_LOADER = 2;

    public SellerFeaturedAdapter(Context mContext, List<SellerData> sellerDataList) {
        this.context = mContext;
        this.sellerDataList = sellerDataList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        protected TextView txt_shop_name;
        protected ImageView image_bookmark;


        public MyViewHolder(View view) {
            super(view);




        }
    }

    class VHLoader extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public VHLoader(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.bottom_progress_bar);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sale_list_row_item, parent, false);
        return new SellerFeaturedAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder view_holder, int position) {
        if (view_holder instanceof SalesListAdapterNew.MyViewHolder) {
            try {


            } catch (Resources.NotFoundException e) {
                BlueShakLog.logDebug(TAG, "Error "+e.getLocalizedMessage());
            } catch (Exception e) {
                BlueShakLog.logDebug(TAG, "Error "+e.getLocalizedMessage());
            }

        }
    }

    @Override
    public int getItemCount() {
        return sellerDataList.size();
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    public void showSettingsAlert() {
        final com.blueshak.app.blueshak.view.AlertDialog alertDialog = new com.blueshak.app.blueshak.view.AlertDialog(context);
        alertDialog.setTitle("Login/Register");
        alertDialog.setMessage("Please Login/Register to continue");
        alertDialog.setPositiveButton("Continue", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent creategarrage = new Intent(context, LoginActivity.class);
                context.startActivity(creategarrage);
            }
        });
        alertDialog.show();
    }

    @Override
    public int getItemViewType(int position) {

        // loader can't be at position 0
        // loader can only be at the last position
        if (position != 0 && position == getItemCount() - 1) {
            return VIEWTYPE_LOADER;
        }

        return VIEWTYPE_ITEM;
    }

    public void showLoading(boolean status) {
        showLoader = status;
    }

    private void setBitmapImages(ImageView imageView, String decodedImgUri) {

        DisplayImageOptions GALLERY = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .considerExifParams(true)
                .cacheOnDisk(true)
                .showImageForEmptyUri(R.drawable.placeholder_background)
                .showImageOnFail(R.drawable.placeholder_background)
                .showImageOnLoading(R.drawable.placeholder_background).build();

        setBitmapDecodedImage(imageView, GALLERY, decodedImgUri);
    }

    private void setBitmapDecodedImage(ImageView imageView, DisplayImageOptions GALLERY, String decodedImgUri) {

        ImageLoader.getInstance().displayImage(decodedImgUri, imageView, GALLERY, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                BlueShakLog.logDebug(TAG, "setBitmapDecodedImage  onLoadingFailed ");
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                BlueShakLog.logDebug(TAG, "setBitmapDecodedImage  onLoadingComplete ");
            }

            @Override
            public void onLoadingCancelled(String s, View view) {
                BlueShakLog.logDebug(TAG, "setBitmapDecodedImage  onLoadingCancelled ");
            }
        });
    }
}
