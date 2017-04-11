package com.langoor.app.blueshak.photos_add;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.langoor.blueshak.R;
import com.langoor.app.blueshak.global.GlobalFunctions;
import com.langoor.app.blueshak.global.GlobalVariables;
import com.langoor.app.blueshak.services.model.CreateImageModel;
import com.langoor.app.blueshak.view.AlertDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
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

    public PhotosAddListAdapter(Activity context, ArrayList<CreateImageModel> list, boolean isDeleteAvailable, OnDeletePicture listener,boolean is_edit_product) {
        super(context, R.layout.photos_add_list_row, list);
        this.context = context;
        this.list = list;
        this.isDeleteAvailable = isDeleteAvailable;
        this.listener = listener;
        this.is_edit_product=is_edit_product;
    }

    static class ViewHolder {
        protected ImageView cameraImageView, deleteImageView;
        protected RelativeLayout relativeLayout;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;
        try {
            if (convertView == null) {
                LayoutInflater inflator = context.getLayoutInflater();
                view = inflator.inflate(R.layout.photos_add_list_row, null);
                final ViewHolder viewHolder = new ViewHolder();
                viewHolder.cameraImageView = (ImageView) view.findViewById(R.id.photos_add_list_row_imageView);
                viewHolder.deleteImageView = (ImageView) view.findViewById(R.id.photos_add_list_row_delete_image);
                viewHolder.relativeLayout = (RelativeLayout) view.findViewById(R.id.photos_add_list_relativeLayout);
                view.setTag(viewHolder);
            } else {
                view = convertView;
            }
            final ViewHolder holder = (ViewHolder) view.getTag();
            holder.cameraImageView.setImageBitmap(null);
            holder.deleteImageView.setOnClickListener(null);
            holder.cameraImageView.setOnClickListener(null);
            if (is_edit_product) {

            } else {

            }
            final File imgFile = new File(list.get(position).getImage());//new File(list.get(position).getImagePath());
           /* Log.d(TAG, imgFile.getPath() + " is available");*/
            if (!isDeleteAvailable) {
                holder.deleteImageView.setVisibility(View.GONE);
            }
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
                String decodedImgUri = Uri.fromFile(new File(list.get(position).getImage())).toString();
                ImageLoader.getInstance().displayImage(decodedImgUri, holder.cameraImageView,GALLERY);
         //Bitmap myBitmap = BitmapFactory.decodeFile(list.get(position).getAbsolutePath());
                //Bitmap myBitmap = globalFunctions.getScaledBitmap(list.get(position).getAbsolutePath(), globalVariables.IMAGE_THUMBNAIL_SIZE);
                //holder.cameraImageView.setImageBitmap(myBitmap);
                Uri uri = Uri.fromFile(new File(list.get(position).getImage()));
               /* Picasso.Builder builder = new Picasso.Builder(context);
                Picasso.with(context).invalidate(uri);
                Picasso.with(context).load(uri).resize(100,100).centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_STORE).into(holder.cameraImageView,
                        new Callback()
                        {
                            @Override
                            public void onSuccess()
                            {
                                Log.d(TAG,"Picasso onSuccess############");
                                //Dimiss progress dialog here
                            }

                            @Override
                            public void onError()
                            {
                                Log.d(TAG,"Picasso onError############");
                                //And here
                            }
                        });
                    Log.d(TAG, "Image set");*/

/*
                builder.listener(new Picasso.Listener()
                {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
                    {
                        exception.printStackTrace();
                        Log.d(TAG,"onImageLoadFailed############ :"+exception.getMessage());
                        exception.printStackTrace();
                    }
                });
                builder.build().load(uri).resize(100, 100).centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_STORE).into(holder.cameraImageView);
        */    /*String uri = fileName.getUri().toString();
              String decodedUri = Uri.decode(uri);
                ImageLoader.getInstance().displayImage(decodedUri, imageView);*/
            } else {
                imageLoader.displayImage(list.get(position).getImage(), holder.cameraImageView, options);
            }

            holder.cameraImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse("file://" + imgFile.getPath()), "image/*");
                    Log.d(TAG, "Path = " + "file://" + imgFile.getPath());
                /*context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("file:/"+imgFile.getPath())));*/
                    context.startActivity(intent);
                }
            });

            String className = context.getClass().getName();
            Log.d(TAG, "ClassName" + className);
            holder.deleteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final AlertDialog alertDialog = new AlertDialog(context);
                    alertDialog.setCancelable(true);
                    alertDialog.setIcon(R.drawable.ic_warning_black_24dp);
                    alertDialog.setTitle(context.getResources().getString(R.string.app_name));
                    alertDialog.setMessage("Are you sure you want to delete this item image");
                    alertDialog.setPositiveButton("yes", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                            listener.ondeleting(position);
                        }
                    });

                    alertDialog.setNegativeButton("cancel", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });

                    alertDialog.show();
                }
            });
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
