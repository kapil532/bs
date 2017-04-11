package com.langoor.app.blueshak.bookmarks;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.langoor.blueshak.R;
import com.langoor.app.blueshak.PickLocation;
import com.langoor.app.blueshak.global.GlobalFunctions;
import com.langoor.app.blueshak.global.GlobalVariables;
import com.langoor.app.blueshak.item.ProductDetail;
import com.langoor.app.blueshak.services.ServerResponseInterface;
import com.langoor.app.blueshak.services.ServicesMethodsManager;
import com.langoor.app.blueshak.services.model.ErrorModel;
import com.langoor.app.blueshak.services.model.ProductModel;
import com.langoor.app.blueshak.services.model.StatusModel;
import com.langoor.app.blueshak.util.LocationService;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.io.IOException;
import java.util.List;

public class BoomarkItemListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
        ViewPagerEx.OnPageChangeListener{
    private Context context;
    private static final String TAG = "PickLocation";
    private List<ProductModel> albumList;
    private OnBookMarksDeleted onBookMarksDeleted;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        protected TextView item_price,item_location,item_name;
        private ImageView is_sold;
        protected ImageView image_iv,favarite;
        public MyViewHolder(View view) {
            super(view);
            item_price = (TextView) view.findViewById(R.id.item_price);
            image_iv= (ImageView) view.findViewById(R.id.product_image);
            is_sold= (ImageView) view.findViewById(R.id.is_sold);
            favarite= (ImageView) view.findViewById(R.id.item_favirate);
            item_location= (TextView) view.findViewById(R.id.item_location);
            item_name= (TextView) view.findViewById(R.id.item_name);
        }
    }

    public BoomarkItemListAdapter(Context mContext, List<ProductModel> albumList,OnBookMarksDeleted onBookMarksDeleted) {
        this.context = mContext;
        this.albumList = albumList;
        this.onBookMarksDeleted=onBookMarksDeleted;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.book_mark_item, parent, false);
            return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder view_holder, final int position) {

            try{
                final  MyViewHolder holder=(MyViewHolder)view_holder;
                final ProductModel obj = albumList.get(position);

         /*   int price=0;
            if(!TextUtils.isEmpty(obj.getSalePrice()))
                price=(int)Float.parseFloat(obj.getSalePrice());*/

                holder.item_price.setText(GlobalFunctions.getFormatedAmount(obj.getCurrency(),obj.getSalePrice()));
            /*holder.item_price.setGravity(Gravity.CENTER_HORIZONTAL);*/


                holder.item_name.setText(obj.getName());
           /* holder.item_price.setGravity(Gravity.CENTER_HORIZONTAL);*/

                if(!TextUtils.isEmpty(obj.getCity()))
                    holder.item_location.setText(obj.getCity());
                else
                    holder.item_location.setVisibility(View.GONE);

                if(!obj.isAvailable())
                    holder.is_sold.setImageResource(R.drawable.ic_sold);
                else{
                    if(obj.is_new())
                        holder.is_sold.setImageResource(R.drawable.ic_new);
                }
                holder.image_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = ProductDetail.newInstance(context, obj,null, GlobalVariables.TYPE_MY_BOOKMARKS);
                        context.startActivity(intent);
                    }
                });
                holder.favarite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteBookmark(context,obj.getId(),position);
                    }
                });
                String item_image=obj.getItem_display_Image();
                ImageLoader imageLoader = ImageLoader.getInstance();
                DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                        .cacheOnDisc(true).resetViewBeforeLoading(true)
                        .showImageForEmptyUri(R.drawable.placeholder_background)
                        .showImageOnFail(R.drawable.placeholder_background)
                        .showImageOnLoading(R.drawable.placeholder_background).build();
                //download and display image from url
                imageLoader.displayImage(item_image,holder.image_iv, options);
            }
            catch (NullPointerException e){
                e.printStackTrace();
                Crashlytics.log(e.getMessage());
            }catch (NumberFormatException e) {
                e.printStackTrace();
                Crashlytics.log(e.getMessage());
            }catch (ArrayIndexOutOfBoundsException e){
                e.printStackTrace();
                Crashlytics.log(e.getMessage());
            }catch (Exception e){
                e.printStackTrace();
                Crashlytics.log(e.getMessage());
            }

    }
    @Override
    public int getItemCount() {
        return albumList.size();
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

    private void deleteBookmark(final Context context, final String productID,final  int position){
        /*GlobalFunctions.showProgress(context, "Bookmarking Product...");*/
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.deleteBookmark(context, productID, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                /*GlobalFunctions.hideProgress();*/
                if(arg0 instanceof StatusModel){
                    StatusModel statusModel = (StatusModel) arg0;
                    if(statusModel.isStatus()){
                        Toast.makeText(context, "Bookmark Removed", Toast.LENGTH_SHORT).show();
                        albumList.remove(position);
                        notifyDataSetChanged();
                        if(albumList.size()==0)
                            onBookMarksDeleted.onBookMarksDeleted(position);

                    }
                }else if(arg0 instanceof ErrorModel){
                    ErrorModel errorModel = (ErrorModel) arg0;
                    String msg = errorModel.getError()!=null ? errorModel.getError() : errorModel.getMessage();
                    Log.d(TAG,"OnFailureFromServer"+msg);
                }
            }

            @Override
            public void OnFailureFromServer(String msg) {
                /*GlobalFunctions.hideProgress();*/
                Log.d(TAG,"OnFailureFromServer"+msg);
            }

            @Override
            public void OnError(String msg) {
               /* GlobalFunctions.hideProgress();*/
                Log.d(TAG,"OnFailureFromServer"+msg);
            }
        }, "Add Bookmarks");

    }

}
