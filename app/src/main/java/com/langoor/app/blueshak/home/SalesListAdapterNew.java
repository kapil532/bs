package com.langoor.app.blueshak.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.langoor.blueshak.R;
import com.langoor.app.blueshak.global.GlobalFunctions;
import com.langoor.app.blueshak.global.GlobalVariables;
import com.langoor.app.blueshak.item.ProductDetail;
import com.langoor.app.blueshak.login.LoginActivity;
import com.langoor.app.blueshak.services.model.ProductModel;
import com.langoor.app.blueshak.services.model.SalesModel;
import com.langoor.app.blueshak.view.CustomSliderTextView;
import com.vlonjatg.progressactivity.ProgressActivity;
import java.util.ArrayList;
import java.util.List;

public class SalesListAdapterNew extends  RecyclerView.Adapter<RecyclerView.ViewHolder> implements
        ViewPagerEx.OnPageChangeListener{

private Context context;
private List<SalesModel> albumList;
    protected boolean showLoader;
    private static final int VIEWTYPE_ITEM = 1;
    private static final int VIEWTYPE_LOADER = 2;

public class MyViewHolder extends RecyclerView.ViewHolder {
    protected TextView name_tv, items_tv, date_tv, distance_tv,sale_time;
    //    protected ImageView image_iv;
    protected ImageView image_iv;
    protected SliderLayout mProductSlider;
    protected ProgressActivity progressActivity;
    protected PagerIndicator mPagerIndicator;
    protected LinearLayout detail_content;
    public MyViewHolder(View view) {
        super(view);
        name_tv = (TextView) view.findViewById(R.id.sale_item_row_list_name_tv);
        items_tv = (TextView) view.findViewById(R.id.sale_item_row_list_items_no_tv);
        date_tv = (TextView) view.findViewById(R.id.sales_item_row_list_date_tv);
        distance_tv = (TextView) view.findViewById(R.id.sales_item_row_list_distance_tv);
        progressActivity = (ProgressActivity) view.findViewById(R.id.products_details_progressActivity);
        mProductSlider = (SliderLayout) view.findViewById(R.id.product_detail_slider);
        sale_time=(TextView)view.findViewById(R.id.sales_item_row_list_time);
        detail_content=(LinearLayout) view.findViewById(R.id.detail_content);



    }
}
    class VHLoader extends RecyclerView.ViewHolder{
        ProgressBar progressBar;
        public VHLoader(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.bottom_progress_bar);
        }
    }

    public SalesListAdapterNew(Context mContext, List<SalesModel> albumList) {
        this.context = mContext;
        this.albumList = albumList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       /* if(viewType == VIEWTYPE_LOADER){
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.*//*custom_bottom_progressbar*//*custom_loading_list_item, parent, false);
                return  new VHLoader(v);
        }else if(viewType == VIEWTYPE_ITEM){
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.sale_list_row_item, parent, false);
            return new MyViewHolder(itemView);
        }
            throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
*/

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sale_list_row_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder view_holder, int position) {
        if(view_holder instanceof MyViewHolder){
            final  MyViewHolder holder=(MyViewHolder)view_holder;
            int grey2=context.getResources().getColor(R.color.grey_2);
            int black=context.getResources().getColor(R.color.black);
            final SalesModel obj = albumList.get(position);
            if(!TextUtils.isEmpty(obj.getName()))
                holder.name_tv.setText(GlobalFunctions.getSentenceFormat(obj.getName()));

      /*  holder.name_tv.setText(obj.getName());*/
            String title="";
            if(!TextUtils.isEmpty(obj.getSale_items_count()))
                if(Integer.parseInt(obj.getSale_items_count())==1)
                    title=obj.getSale_items_count()+" Item";
                else
                    title=obj.getSale_items_count()+" Items";
            else
                title="No Items Available";

            holder.items_tv.setText(title);
            holder.date_tv.setText(obj.getStart_date());
            holder.sale_time.setText(obj.getStart_time()+"-"+obj.getEnd_time());
       /* holder.distance_tv.setText(obj.getDistanceAway()+" "+getContext().getString(R.string.milesAway));*/
            holder.distance_tv.setText(obj.getDistanceAway()/*+" "+context.getString(R.string.milesAway)*/);
            holder.detail_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = ProductDetail.newInstance(context,null,obj,GlobalVariables.TYPE_MY_SALE);
                    context.startActivity(intent);
                }
            });
       /* holder.distance_tv.setVisibility(obj.getDistanceAway()==null?View.GONE:View.VISIBLE);*/
            holder.mProductSlider.removeAllSliders();
            ArrayList<String> displayImageURL = new ArrayList<String>();
            displayImageURL.clear();
            displayImageURL=obj.getImages();
            for(int i=0;i<displayImageURL.size()&&displayImageURL.get(i)!=null;i++){
                CustomSliderTextView textSliderView = new CustomSliderTextView(context);
                // initialize a SliderLayout
                textSliderView
                        .description(1+"")
                        .image(displayImageURL.get(i).toString())
                        .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                 /*   .setScaleType(BaseSliderView.ScaleType.CenterInside);*/
                        .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                            @Override
                            public void onSliderClick(BaseSliderView slider) {
                                Intent intent = ProductDetail.newInstance(context,null,obj,GlobalVariables.TYPE_MY_SALE);
                                context.startActivity(intent);
                            }
                        });
                //add your extra information
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle()
                        .putString("extra", 1 + "");

                holder.mProductSlider.addSlider(textSliderView);
            }

            /*  holder.mProductSlider.setPresetTransformer(SliderLayout.Transformer.ZoomOutSlide);*/
            holder.mProductSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        /*  if(holder.mPagerIndicator!=null){holder.mProductSlider.setCustomIndicator(holder.mPagerIndicator);}
        */  holder.mProductSlider.setCustomAnimation(new DescriptionAnimation());
            holder.mProductSlider.setDuration(4000);
            holder.mProductSlider.addOnPageChangeListener(this);
            holder.mProductSlider.stopAutoCycle();
        }else  if (view_holder instanceof VHLoader) {
            VHLoader loaderViewHolder = (VHLoader)view_holder;
            if (showLoader) {
                loaderViewHolder.progressBar.setVisibility(View.VISIBLE);
            } else {
                loaderViewHolder.progressBar.setVisibility(View.GONE);
            }
            return;
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
    public void showSettingsAlert(){
        final com.langoor.app.blueshak.view.AlertDialog alertDialog = new com.langoor.app.blueshak.view.AlertDialog(context);
        alertDialog.setTitle("Login/Register");
        alertDialog.setMessage("Please Login/Register to continue");
        alertDialog.setPositiveButton("Continue", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent creategarrage = new Intent(context,LoginActivity.class);
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
}
