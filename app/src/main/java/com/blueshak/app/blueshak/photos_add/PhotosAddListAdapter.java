package com.blueshak.app.blueshak.photos_add;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.blueshak.app.blueshak.garage.CreateItemSaleFragment;
import com.blueshak.app.blueshak.garage.Utils;
import com.blueshak.app.blueshak.util.BlueShakLog;
import com.blueshak.blueshak.R;
import com.blueshak.app.blueshak.services.model.CreateImageModel;
import com.blueshak.app.blueshak.view.AlertDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.util.ArrayList;

;

public class PhotosAddListAdapter extends ArrayAdapter<CreateImageModel> {
    private static final String TAG = "HoriListViewAdapter";
    private final ArrayList<CreateImageModel> list;
    private final Activity context;
    private boolean isDeleteAvailable;
    private  OnDeletePicture listener;
    boolean is_edit_product=false;

    public PhotosAddListAdapter(Activity context, ArrayList<CreateImageModel> list, boolean isDeleteAvailable,
                                OnDeletePicture listener,boolean is_edit_product) {
        super(context, R.layout.photos_add_list_row, list);
        this.context = context;
        this.list = list;
        this.isDeleteAvailable = isDeleteAvailable;
        this.listener = listener;
        this.is_edit_product=is_edit_product;
        BlueShakLog.logDebug(TAG, "drag setImageOrderIdArrayList Adapter final ArrayList ->\n "+ list);
    }

    static class ViewHolder {
        protected ImageView cameraImageView, deleteImageView;
        protected RelativeLayout relativeLayout;
        protected ProgressBar progressBar;
    }
    boolean isFirstDelete = false;
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;

        try {
            if(position < 5){
            if (convertView == null) {
                LayoutInflater inflator = context.getLayoutInflater();
                view = inflator.inflate(R.layout.photos_add_list_row, null);
                final ViewHolder viewHolder = new ViewHolder();
                viewHolder.cameraImageView = (ImageView) view.findViewById(R.id.photos_add_list_row_imageView);
                viewHolder.deleteImageView = (ImageView) view.findViewById(R.id.photos_add_list_row_delete_image);
                viewHolder.relativeLayout = (RelativeLayout) view.findViewById(R.id.photos_add_list_relativeLayout);
                viewHolder.progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
                view.setTag(viewHolder);
            } else {
                view = convertView;
            }
            final ViewHolder holder = (ViewHolder) view.getTag();
            holder.cameraImageView.setImageBitmap(null);
            holder.deleteImageView.setOnClickListener(null);
            holder.cameraImageView.setOnClickListener(null);

            final File imgFile = new File(list.get(position).getImage());//new File(list.get(position).getImagePath());

            ImageLoader imageLoader = ImageLoader.getInstance();
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisc(true).resetViewBeforeLoading(true)
                    .showImageForEmptyUri(R.drawable.placeholder_background)
                    .showImageOnFail(R.drawable.placeholder_background)
                    .showImageOnLoading(R.drawable.placeholder_background).build();
            //download and display image from url
            DisplayImageOptions GALLERY = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .considerExifParams(true)
                    .cacheOnDisk(true)
                    .showImageForEmptyUri(R.drawable.placeholder_background)
                    .showImageOnFail(R.drawable.placeholder_background)
                    .showImageOnLoading(R.drawable.placeholder_background).build();
            if(imgFile.exists()) {
                holder.deleteImageView.setVisibility(View.VISIBLE);
                String decodedImgUri = Uri.fromFile(new File(list.get(position).getImage())).toString();
                ImageLoader.getInstance().displayImage(decodedImgUri, holder.cameraImageView,GALLERY);

            } else {
                if(list.get(position).getImage()!=null && list.get(position).getImage().contains("http") ){
                    holder.deleteImageView.setVisibility(View.VISIBLE);
                }else{
                    holder.deleteImageView.setVisibility(View.GONE);
                }
                imageLoader.displayImage(list.get(position).getImage(), holder.cameraImageView, options, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {
                        holder.progressBar.setVisibility(View.VISIBLE);
                        holder.deleteImageView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {
                        holder.progressBar.setVisibility(View.GONE);
                        holder.deleteImageView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                        holder.progressBar.setVisibility(View.GONE);
                        if(list.get(position).getImage()!=null && !list.get(position).getImage().isEmpty()){
                            holder.deleteImageView.setVisibility(View.VISIBLE);
                        }else{
                            holder.deleteImageView.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {
                        holder.progressBar.setVisibility(View.GONE);
                        holder.deleteImageView.setVisibility(View.GONE);
                    }
                });
            }

            holder.cameraImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    listener.onImageClick(position,imgFile,"",list.get(position).isRealImage());
                }
            });

            String className = context.getClass().getName();
            holder.deleteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(Utils.getCountRealImage(list) == 5){
                        listener.ondeleting(position);
                    }else{
                        listener.ondeleting(position-1);
                    }

                }
            });
            }
        }catch (Exception e){
            Log.d(TAG,"Exception :"+e.getMessage());
            e.printStackTrace();
        }/*catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }catch (NullPointerException e){
            e.printStackTrace();
        }catch (NumberFormatException e){
            e.printStackTrace();
        }*/
        return view;
    }
   public boolean  is_base64(String is_base64){
        String someString = "...";
      /*  Base64.Decoder decoder = Base64.decoder;
        try {
            decoder.decode(someString);
        } catch(IllegalArgumentException iae) {
            // That string wasn't valid.
        }*/
    return true;
    }

}
