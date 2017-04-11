package com.langoor.app.blueshak.search;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.langoor.blueshak.R;
import com.langoor.app.blueshak.global.GlobalFunctions;
import com.langoor.app.blueshak.global.GlobalVariables;
import com.langoor.app.blueshak.item.ProductDetail;
import com.langoor.app.blueshak.login.LoginActivity;
import com.langoor.app.blueshak.services.ServerResponseInterface;
import com.langoor.app.blueshak.services.ServicesMethodsManager;
import com.langoor.app.blueshak.services.model.ErrorModel;
import com.langoor.app.blueshak.services.model.ProductModel;
import com.langoor.app.blueshak.services.model.StatusModel;
import com.langoor.app.blueshak.util.LocationService;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import java.util.List;


public class SearchListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
        ViewPagerEx.OnPageChangeListener{
    public static final String TAG = "SearchActivity";
    private Context context;
    private List<ProductModel> albumList;
    public String item_address;
    private LocationService locServices;
    boolean is_bookmark_needed=false;
    protected boolean showLoader;
    private static final int VIEWTYPE_ITEM = 1;
    private static final int VIEWTYPE_LOADER = 2;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        protected TextView item_price,item_location,item_name;
        private ImageView is_sold,shippable,pick_up;
        protected ImageView image_iv,favarite;
        LinearLayout linearLayout;
        View view_;
        public MyViewHolder(View view) {
            super(view);
            view_=view;
            item_price = (TextView) view.findViewById(R.id.item_price);
            image_iv= (ImageView) view.findViewById(R.id.product_image);
            shippable= (ImageView) view.findViewById(R.id.shippable);
            pick_up= (ImageView) view.findViewById(R.id.pick_up);
            is_sold= (ImageView) view.findViewById(R.id.is_sold);
            favarite= (ImageView) view.findViewById(R.id.item_favirate);
            item_location= (TextView) view.findViewById(R.id.item_location);
            item_name= (TextView) view.findViewById(R.id.item_name);
        }
    }
    class VHLoader extends RecyclerView.ViewHolder{
        ProgressBar progressBar;
        public VHLoader(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.bottom_progress_bar);
        }
    }

    public SearchListAdapter(Context mContext, List<ProductModel> albumList,boolean is_bookmark_needed) {
        this.context = mContext;
        this.albumList = albumList;
        this.item_address=GlobalFunctions.getSharedPreferenceString(mContext,GlobalVariables.CURRENT_LOCATION);
        this.is_bookmark_needed=is_bookmark_needed;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_item_list_row_item, parent, false);
            return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder view_holder, int position) {
        try{
            if(view_holder instanceof MyViewHolder){
                final  MyViewHolder holder=(MyViewHolder)view_holder;
                final ProductModel obj = albumList.get(position);
                if(is_bookmark_needed)
                    holder.favarite.setVisibility(View.VISIBLE);
              /*  int price=0;
                if(!TextUtils.isEmpty(obj.getSalePrice()))
                    price=(int)Float.parseFloat(obj.getSalePrice());*/

                holder.item_price.setText(GlobalFunctions.getFormatedAmount(obj.getCurrency(),obj.getSalePrice()));
                holder.item_name.setText(obj.getName());
               /* holder.item_price.setGravity(Gravity.CENTER_HORIZONTAL);*/

                if(!TextUtils.isEmpty(obj.getCity()))
                    holder.item_location.setText(obj.getCity());
                else
                    holder.item_location.setVisibility(View.GONE);


                if(obj.is_bookmark())
                    holder.favarite.setImageResource(R.drawable.like_full);

                if(!obj.isAvailable())
                    holder.is_sold.setImageResource(R.drawable.ic_sold);
                else{
                    if(obj.is_new())
                        holder.is_sold.setImageResource(R.drawable.ic_new);
                }
                if(obj.isPickup())holder.pick_up.setVisibility(View.VISIBLE);
                if(obj.isShipable())holder.shippable.setVisibility(View.VISIBLE);

           /* if(obj.is_new())
                    holder.new_flag_image.setVisibility(View.VISIBLE);
                else
                    holder.new_flag_image.setVisibility(View.GONE);*/

                holder.image_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = ProductDetail.newInstance(context, obj,null, GlobalVariables.TYPE_MY_SALE);
                        context.startActivity(intent);
                    }
                });
                holder.view_.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = ProductDetail.newInstance(context, obj,null, GlobalVariables.TYPE_MY_SALE);
                        context.startActivity(intent);
                    }
                });
                holder.favarite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(GlobalFunctions.is_loggedIn(context)){
                            if(obj.is_bookmark()){
                                holder.favarite.setImageResource(R.drawable.like_border);
                                deleteBookmark(context,obj.getId());
                                obj.setIs_bookmark(false);
                            }else{
                                holder.favarite.setImageResource(R.drawable.like_full);
                                addBookmark(context,obj.getId());
                                obj.setIs_bookmark(true);
                            }
                        }else
                            showSettingsAlert();


                    }
                });
                String image=obj.getItem_display_Image();
                ImageLoader imageLoader = ImageLoader.getInstance();
                DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                        .cacheOnDisc(true).resetViewBeforeLoading(true)
                        .showImageForEmptyUri(R.drawable.placeholder_background)
                        .showImageOnFail(R.drawable.placeholder_background)
                        .showImageOnLoading(R.drawable.placeholder_background).build();
                //download and display image from url
                imageLoader.displayImage(image,holder.image_iv, options);
            }else  if (view_holder instanceof VHLoader) {
                VHLoader loaderViewHolder = (VHLoader)view_holder;
                if (showLoader) {
                    loaderViewHolder.progressBar.setVisibility(View.VISIBLE);
                } else {
                    loaderViewHolder.progressBar.setVisibility(View.GONE);
                }
                return;
            }

        } catch (NullPointerException e){
            Log.d(TAG,"NullPointerException");
            e.printStackTrace();
        }catch (NumberFormatException e) {
            Log.d(TAG,"NumberFormatException");
            e.printStackTrace();
        }catch (Exception e){
            Log.d(TAG,"Exception");
            e.printStackTrace();
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

    private void deleteBookmark(final Context context, String productID){
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
    private void addBookmark(final Context context, String productID){
        /*GlobalFunctions.showProgress(context, "Bookmarking Product...");*/
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.addBookmark(context, productID, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                /*GlobalFunctions.hideProgress();*/
                if(arg0 instanceof StatusModel){
                    StatusModel statusModel = (StatusModel) arg0;
                    if(statusModel.isStatus()){

                        Toast.makeText(context, "Added to bookmarks", Toast.LENGTH_SHORT).show();
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
    public void showSettingsAlert(){
        Intent creategarrage =new Intent(context,LoginActivity.class);
        context.startActivity(creategarrage);

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
}
