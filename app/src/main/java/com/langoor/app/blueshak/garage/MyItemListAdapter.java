package com.langoor.app.blueshak.garage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.langoor.blueshak.R;
import com.langoor.app.blueshak.PickLocation;
import com.langoor.app.blueshak.filter.FilterActivity;
import com.langoor.app.blueshak.global.GlobalFunctions;
import com.langoor.app.blueshak.global.GlobalVariables;
import com.langoor.app.blueshak.item.ProductDetail;
import com.langoor.app.blueshak.services.model.CategoryListModel;
import com.langoor.app.blueshak.services.model.CreateProductModel;
import com.langoor.app.blueshak.services.model.ProductModel;
import com.langoor.app.blueshak.util.LocationService;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyItemListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "MyItemListAdapter";
    private Context context;
    private List<ProductModel> albumList;
    public String item_address;
    private boolean item_editable =false;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        protected TextView item_price,item_name;
        protected ImageView item_image;
        public CheckBox chkSelected;
        LinearLayout item_view;
        public MyViewHolder(View view) {
            super(view);
            item_price = (TextView) view.findViewById(R.id.price_tv);
            item_name = (TextView) view.findViewById(R.id.name_tv);
           /* done = (ImageView) view.findViewById(R.id.done);*/
            item_image = (ImageView) view.findViewById(R.id.image_iv);
            item_view=(LinearLayout)view.findViewById(R.id.item_view);
            chkSelected = (CheckBox) view.findViewById(R.id.chkSelected);
        }
    }


    public MyItemListAdapter(Context mContext, List<ProductModel> albumList,boolean item_editable) {
        this.context = mContext;
        this.albumList = albumList;
        this.item_address = GlobalFunctions.getSharedPreferenceString(mContext, GlobalVariables.CURRENT_LOCATION);
        this.item_editable=item_editable;


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_my_item_row, parent, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder view_holder, int position) {
        try{
            final int pos = position;
            if (view_holder instanceof MyViewHolder) {
                final MyViewHolder holder = (MyViewHolder) view_holder;
                 ProductModel obj = albumList.get(position);

              holder.item_price.setText(GlobalFunctions.getFormatedAmount(obj.getCurrency(),obj.getSalePrice()));
              holder.item_name.setText(obj.getName());
                if(!item_editable)
                    holder.chkSelected.setVisibility(View.GONE);
              holder.chkSelected.setChecked(obj.is_selected());
              if(obj.is_selected()){
                  holder.chkSelected.setChecked(obj.is_selected());
                  holder.item_price.setTextColor(context.getResources().getColor(R.color.brandColor));
                  holder.item_name.setTextColor(context.getResources().getColor(R.color.brandColor));
              }else{
                  holder.item_price.setTextColor(context.getResources().getColor(R.color.black));
                  holder.item_name.setTextColor(context.getResources().getColor(R.color.black));
              }
              if(!item_editable)
                  holder.chkSelected.setEnabled(false);

              holder.chkSelected.setTag(obj);

              holder.chkSelected.setOnClickListener(new View.OnClickListener() {
                  public void onClick(View v) {
                      CheckBox cb = (CheckBox) v;
                      ProductModel obj = (ProductModel) cb.getTag();
                      obj.setIs_selected(cb.isChecked());
                      albumList.get(pos).setIs_selected(cb.isChecked());
                      if(cb.isChecked()){
                          holder.item_price.setTextColor(context.getResources().getColor(R.color.brandColor));
                          holder.item_name.setTextColor(context.getResources().getColor(R.color.brandColor));
                      }else{
                          holder.item_price.setTextColor(context.getResources().getColor(R.color.black));
                          holder.item_name.setTextColor(context.getResources().getColor(R.color.black));
                      }
                  }
              });

              List<String> imageURI = null;
              imageURI = obj.getImage();
              String item_image=obj.getItem_display_Image();
              ImageLoader imageLoader = ImageLoader.getInstance();
              DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                      .cacheOnDisc(true).resetViewBeforeLoading(true)
                      .showImageForEmptyUri(R.drawable.placeholder_background)
                      .showImageOnFail(R.drawable.placeholder_background)
                      .showImageOnLoading(R.drawable.placeholder_background).build();
              //download and display image from url
              imageLoader.displayImage(item_image,holder.item_image, options);
            }
        }  catch (NullPointerException e){
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
    // method to access in activity after updating selection
    public List<ProductModel> getProductList() {
        return albumList;
    }


}
