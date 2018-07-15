package com.blueshak.app.blueshak.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blueshak.app.blueshak.Messaging.helper.Constants;
import com.blueshak.app.blueshak.global.GlobalFunctions;
import com.blueshak.app.blueshak.global.GlobalVariables;
import com.blueshak.app.blueshak.home.FeatureItemLoadMore;
import com.blueshak.app.blueshak.home.ItemListFragmentForList;
import com.blueshak.app.blueshak.home.model.FeatureItemData;
import com.blueshak.app.blueshak.item.ProductDetail;
import com.blueshak.app.blueshak.login.LoginActivity;
import com.blueshak.app.blueshak.services.ServerResponseInterface;
import com.blueshak.app.blueshak.services.ServicesMethodsManager;
import com.blueshak.app.blueshak.services.model.ErrorModel;
import com.blueshak.app.blueshak.services.model.FilterModel;
import com.blueshak.app.blueshak.services.model.ProductModel;
import com.blueshak.app.blueshak.services.model.StatusModel;
import com.blueshak.app.blueshak.util.BlueShakLog;
import com.blueshak.blueshak.R;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by lsingh013 on 29/01/18.
 */

public class HorizontalItemListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
        ViewPagerEx.OnPageChangeListener {
    public static final String TAG = "ItemListAdapter";
    private Context context;
    private List<FeatureItemData> featureItemList;
    protected boolean showLoader;
    private static final int VIEWTYPE_ITEM = 1;
    private static final int VIEWTYPE_LOADER = 2;
    public String item_address;
    private FilterModel filterModel;
    private FeatureItemLoadMore itemLoadMore;


    public HorizontalItemListAdapter(Context mContext, List<FeatureItemData> featureItemList, FilterModel filterModel,
                                     FeatureItemLoadMore itemLoadMore) {
        this.context = mContext;
        this.featureItemList = featureItemList;
        this.filterModel = filterModel;
        this.itemLoadMore = itemLoadMore;
        this.item_address = GlobalFunctions.getSharedPreferenceString(mContext, GlobalVariables.CURRENT_LOCATION);
     /*   imgLoader = new ImageLoader(mContext);*/


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.horizantal_items_list, parent, false);
        return new HorizontalItemListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder view_holder, final int position) {
        try {
            if (view_holder instanceof HorizontalItemListAdapter.MyViewHolder) {

                final HorizontalItemListAdapter.MyViewHolder holder = (HorizontalItemListAdapter.MyViewHolder) view_holder;
                final FeatureItemData featureData = featureItemList.get(position);

                if(position == 0){
                    holder.item_name.setText("BlueShak Shop");
                    loadImage(Constants.BLUESHAK_SHOP_IMAGE,holder.image_iv);
                    holder.txt_feature_price.setVisibility(View.GONE);

                }else{
                    if(position == ItemListFragmentForList.iFeatureListSize-1
                            && ItemListFragmentForList.iFeature_last_page >= ItemListFragmentForList.iFeature_current_page){
                        //BlueShakLog.logDebug(TAG,"onLoadMore Adapter feature position INSIDE----->"+position+"  iFeatureListSize -->");
                       // itemLoadMore.onLoadMoreFeatureItems(ItemListFragmentForList.iFeature_current_page+1);
                    }

                    holder.item_name.setText(featureData.getProduct_name());
                    holder.txt_feature_price.setVisibility(View.VISIBLE);
                    if (featureData.getHide_item_price().equalsIgnoreCase("1")) {
                        holder.txt_feature_price.setText("Negotiable");
                    } else {
                        holder.txt_feature_price.setText(GlobalFunctions.getFormatedAmount(featureData.getCurrency(), featureData.getSale_price()));
                    }

                    if (featureData.getIs_product_new().equalsIgnoreCase("1")
                            && featureData.getIs_featured().equalsIgnoreCase("1")) {
                        holder.is_sold.setVisibility(View.GONE);
                        holder.img_feature.setVisibility(View.VISIBLE);
                        holder.img_feature.setImageResource(R.drawable.icon_new_feature_small);
                    }else if (featureData.getIs_available().equalsIgnoreCase("0")
                            && featureData.getIs_featured().equalsIgnoreCase("1")) {
                        holder.is_sold.setVisibility(View.VISIBLE);
                        holder.img_feature.setVisibility(View.GONE);
                        holder.is_sold.setImageResource(R.drawable.ic_sold);
                    }else if(featureData.getIs_featured().equalsIgnoreCase("1")){
                        holder.is_sold.setVisibility(View.GONE);
                        holder.img_feature.setVisibility(View.VISIBLE);
                        holder.img_feature.setImageResource(R.drawable.icon_feature_small);
                    }else{
                        holder.is_sold.setVisibility(View.GONE);
                        holder.img_feature.setVisibility(View.GONE);
                    }


                    String item_image = featureData.getImage();
                    loadImage(item_image,holder.image_iv);
                }

                holder.container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(position == 0){
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.BLUESHAK_SHOP_WEB_LINK));
                            context.startActivity(browserIntent);
                        }else{
                            Intent intent = ProductDetail.newInstance(context, null, null,featureData.getProduct_id(),
                                    GlobalVariables.TYPE_MY_SALE);
                            context.startActivity(intent);
                        }

                    }
                });

            } else if (view_holder instanceof HorizontalItemListAdapter.VHLoader) {
                HorizontalItemListAdapter.VHLoader loaderViewHolder = (HorizontalItemListAdapter.VHLoader) view_holder;
                BlueShakLog.logDebug(TAG,"onLoadMore Adapter feature position ELSE ----->");
                if (showLoader) {
                    loaderViewHolder.progressBar.setVisibility(View.VISIBLE);
                } else {
                    loaderViewHolder.progressBar.setVisibility(View.GONE);
                }
                return;
            }

        } catch (NullPointerException e) {
            Log.d(TAG, "NullPointerException");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            Log.d(TAG, "NumberFormatException");
            e.printStackTrace();
        } catch (Exception e) {
            Log.d(TAG, "Exception");
            e.printStackTrace();
        }
    }

    private void loadImage(String imageUrl, ImageView imageView){
        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.placeholder_background)
                .showImageOnFail(R.drawable.placeholder_background)
                .showImageOnLoading(R.drawable.placeholder_background).build();
        //download and display image from url
        imageLoader.displayImage(imageUrl, imageView, options);
    }

    @Override
    public int getItemCount() {
        if(featureItemList!=null){
            return featureItemList.size();
        }else{
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        protected TextView txt_feature_price, item_name;
        protected ImageView image_iv, img_feature;
        private ImageView is_sold;
        public View container;

        public MyViewHolder(View view) {
            super(view);
            container = view;
            txt_feature_price = (TextView) view.findViewById(R.id.txt_feature_price);
            image_iv = (ImageView) view.findViewById(R.id.product_image);
            img_feature = (ImageView) view.findViewById(R.id.img_feature);
            item_name = (TextView) view.findViewById(R.id.item_name);
            is_sold = (ImageView)view.findViewById(R.id.is_sold);

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
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    class MyClickableSpan extends ClickableSpan {
        public void onClick(View textView) {

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(context.getResources().getColor(R.color.tab_selected));//set text color

        }
    }

    public void showSettingsAlert() {
        Intent creategarrage = new Intent(context, LoginActivity.class);
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
