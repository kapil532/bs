package com.blueshak.app.blueshak.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.blueshak.app.blueshak.global.GlobalFunctions;
import com.blueshak.app.blueshak.global.GlobalVariables;
import com.blueshak.app.blueshak.home.FeatureItemLoadMore;
import com.blueshak.app.blueshak.home.ItemListFragmentForList;
import com.blueshak.app.blueshak.item.ProductDetail;
import com.blueshak.app.blueshak.login.LoginActivity;
import com.blueshak.app.blueshak.seller.model.Data;
import com.blueshak.app.blueshak.seller.model.SellerFeatured;
import com.blueshak.app.blueshak.services.model.FilterModel;
import com.blueshak.app.blueshak.util.BlueShakLog;
import com.blueshak.blueshak.R;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lsingh013 on 13/05/18.
 */

public class SellerHorizontalItemListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
        ViewPagerEx.OnPageChangeListener {
    public static final String TAG = "SellerItemListAdapter";
    private Context context;
    private List<Data> sellerFeatureItemList;
    protected boolean showLoader;
    private static final int VIEWTYPE_ITEM = 1;
    private static final int VIEWTYPE_LOADER = 2;
    public String item_address;
    private FilterModel filterModel;
    private FeatureItemLoadMore itemLoadMore;

    public SellerHorizontalItemListAdapter(Context mContext, List<Data> sellerFeatureItemList, FilterModel filterModel,
                                           FeatureItemLoadMore itemLoadMore) {
        this.context = mContext;
        this.sellerFeatureItemList = sellerFeatureItemList;
        this.filterModel = filterModel;
        this.itemLoadMore = itemLoadMore;
        this.item_address = GlobalFunctions.getSharedPreferenceString(mContext, GlobalVariables.CURRENT_LOCATION);
     /*   imgLoader = new ImageLoader(mContext);*/


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.seller_horizontal_item_list, parent, false);
        return new SellerHorizontalItemListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder view_holder, final int position) {
        try {
            if (view_holder instanceof SellerHorizontalItemListAdapter.MyViewHolder) {

                final SellerHorizontalItemListAdapter.MyViewHolder holder = (SellerHorizontalItemListAdapter.MyViewHolder) view_holder;
                final Data featureData = sellerFeatureItemList.get(position);

                if (position == ItemListFragmentForList.iFeatureListSize - 1
                        && ItemListFragmentForList.iFeature_last_page >= ItemListFragmentForList.iFeature_current_page) {
                    //BlueShakLog.logDebug(TAG,"onLoadMore Adapter feature position INSIDE----->"+position+"  iFeatureListSize -->");
                    itemLoadMore.onLoadMoreFeatureItems(ItemListFragmentForList.iFeature_current_page + 1);
                }

                holder.item_name.setText(featureData.getSellerName());

                if(featureData.getAvgSellerRating()!=null){
                    holder.rating_bar_feature.setRating(Float.valueOf(featureData.getAvgSellerRating()));
                }

                setBitmapImages(holder.feature_seller_image, featureData.getSellerImage());

                String itemName;
                int iTemCount = featureData.getShopItemsCount();
                if(iTemCount == 0 || iTemCount == 1){
                    itemName = iTemCount+" item";
                }else{
                    itemName = iTemCount+" items";
                }

                if(featureData.getSaleAddress()!=null && !featureData.getSaleAddress().isEmpty()
                        && !featureData.getSaleAddress().equalsIgnoreCase("null")){
                    holder.txt_feature.setText(itemName +" | "+featureData.getSaleAddress());
                }else{
                    holder.txt_feature.setText(itemName);
                }

                if (featureData.getIsFeatured().equalsIgnoreCase("1")) {
                    holder.is_sold.setVisibility(View.GONE);
                    holder.img_feature.setVisibility(View.VISIBLE);
                    holder.img_feature.setImageResource(R.drawable.icon_feature_small);
                } else {
                    holder.is_sold.setVisibility(View.GONE);
                    holder.img_feature.setVisibility(View.GONE);
                }

                holder.container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = ProductDetail.newInstance(context, null, null,
                                sellerFeatureItemList.get(position).getSellerId(),
                                GlobalVariables.TYPE_MY_SALE);
                        context.startActivity(intent);

                    }
                });

                /*String item_image = featureData.getImage();
                ImageLoader imageLoader = ImageLoader.getInstance();
                DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                        .cacheOnDisc(true).resetViewBeforeLoading(true)
                        .showImageForEmptyUri(R.drawable.placeholder_background)
                        .showImageOnFail(R.drawable.placeholder_background)
                        .showImageOnLoading(R.drawable.placeholder_background).build();
                //download and display image from url
                imageLoader.displayImage(item_image, holder.image_iv, options);
                */

                /*ArrayList<String> displayImageURL = new ArrayList<String>();
                displayImageURL.clear();
                displayImageURL = featureData.getImages().get(position).getLink();*/

                int imageSize = featureData.getImages().size();
                BlueShakLog.logDebug(TAG, "Position SellerHorizontalItemListAdapter --> " + position + "  imageSize --> " + imageSize);
                if (imageSize > 0 && imageSize <= 2) {
                    holder.layout_two.setVisibility(View.VISIBLE);
                    holder.layout_four.setVisibility(View.GONE);
                    holder.layout_six.setVisibility(View.GONE);
                    if (imageSize == 1) {
                        holder.img_two_1.setVisibility(View.VISIBLE);
                        holder.img_two_2.setVisibility(View.GONE);
                        holder.empty_two_2.setVisibility(View.GONE);
                        setBitmapImages(holder.img_two_1, featureData.getImages().get(0).getLink());
                    } else {
                        holder.img_two_1.setVisibility(View.VISIBLE);
                        holder.img_two_2.setVisibility(View.VISIBLE);
                        holder.empty_two_2.setVisibility(View.VISIBLE);
                        setBitmapImages(holder.img_two_1, featureData.getImages().get(0).getLink());
                        setBitmapImages(holder.img_two_2, featureData.getImages().get(1).getLink());
                    }

                } else if (imageSize == 3 || imageSize == 4) {
                    holder.layout_two.setVisibility(View.GONE);
                    holder.layout_four.setVisibility(View.VISIBLE);
                    holder.layout_six.setVisibility(View.GONE);
                    if (imageSize == 3) {
                        holder.img_four_1.setVisibility(View.VISIBLE);
                        holder.img_four_2.setVisibility(View.VISIBLE);
                        holder.img_four_3.setVisibility(View.VISIBLE);
                        holder.img_four_4.setVisibility(View.GONE);
                        holder.empty_four_4.setVisibility(View.GONE);
                        setBitmapImages(holder.img_four_1, featureData.getImages().get(0).getLink());
                        setBitmapImages(holder.img_four_2, featureData.getImages().get(1).getLink());
                        setBitmapImages(holder.img_four_3, featureData.getImages().get(2).getLink());

                    } else {
                        holder.img_four_1.setVisibility(View.VISIBLE);
                        holder.img_four_2.setVisibility(View.VISIBLE);
                        holder.img_four_3.setVisibility(View.VISIBLE);
                        holder.img_four_4.setVisibility(View.VISIBLE);
                        holder.empty_four_4.setVisibility(View.VISIBLE);
                        setBitmapImages(holder.img_four_1, featureData.getImages().get(0).getLink());
                        setBitmapImages(holder.img_four_2, featureData.getImages().get(1).getLink());
                        setBitmapImages(holder.img_four_3, featureData.getImages().get(2).getLink());
                        setBitmapImages(holder.img_four_4, featureData.getImages().get(3).getLink());
                    }
                } else if (imageSize == 5 || imageSize > 5) {
                    holder.layout_two.setVisibility(View.GONE);
                    holder.layout_four.setVisibility(View.GONE);
                    holder.layout_six.setVisibility(View.VISIBLE);
                    if (imageSize == 5) {
                        holder.tv_more.setVisibility(View.GONE);

                    } else {
                        holder.tv_more.setVisibility(View.VISIBLE);
                        holder.tv_more.setText("+" + (imageSize - 4));
                    }
                    setBitmapImages(holder.img_six_1, featureData.getImages().get(0).getLink());
                    setBitmapImages(holder.img_six_2, featureData.getImages().get(1).getLink());
                    setBitmapImages(holder.img_six_3, featureData.getImages().get(2).getLink());
                    setBitmapImages(holder.img_six_4, featureData.getImages().get(3).getLink());
                    setBitmapImages(holder.img_six_5, featureData.getImages().get(4).getLink());
                }


            } else if (view_holder instanceof HorizontalItemListAdapter.VHLoader) {
                HorizontalItemListAdapter.VHLoader loaderViewHolder = (HorizontalItemListAdapter.VHLoader) view_holder;
                BlueShakLog.logDebug(TAG, "onLoadMore Adapter feature position ELSE ----->");
                if (showLoader) {
                    loaderViewHolder.progressBar.setVisibility(View.VISIBLE);
                } else {
                    loaderViewHolder.progressBar.setVisibility(View.GONE);
                }
                return;
            }

        } catch (NullPointerException e) {
            Log.d(TAG, "NullPointerException");
        } catch (NumberFormatException e) {
            Log.d(TAG, "NumberFormatException");
        } catch (Exception e) {
            Log.d(TAG, "Exception");
        }
    }

    @Override
    public int getItemCount() {
        if (sellerFeatureItemList != null) {
            return sellerFeatureItemList.size();
        } else {
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        protected TextView txt_feature, item_name;
        protected ImageView image_iv, img_feature;
        private ImageView is_sold;
        public View container;

        private LinearLayout layout_two;
        private LinearLayout layout_four;
        private LinearLayout layout_six;
        private LinearLayout layout_asymmetric_main;

        private ImageView img_two_1;
        private ImageView img_two_2;
        private View empty_two_2;

        private ImageView img_four_1;
        private ImageView img_four_2;
        private ImageView img_four_3;
        private ImageView img_four_4;
        private View empty_four_4;

        private ImageView img_six_1;
        private ImageView img_six_2;
        private ImageView img_six_3;
        private ImageView img_six_4;
        private ImageView img_six_5;
        private TextView tv_more;
        private ImageView feature_seller_image;
        //private TextView txt_feature_count;
        private RatingBar rating_bar_feature;

        public MyViewHolder(View view) {
            super(view);
            container = view;
            txt_feature = (TextView) view.findViewById(R.id.txt_feature);
            image_iv = (ImageView) view.findViewById(R.id.product_image);
            img_feature = (ImageView) view.findViewById(R.id.img_feature);
            item_name = (TextView) view.findViewById(R.id.item_name);
            is_sold = (ImageView) view.findViewById(R.id.is_sold);

            layout_two = (LinearLayout) view.findViewById(R.id.layout_two);
            layout_four = (LinearLayout) view.findViewById(R.id.layout_four);
            layout_six = (LinearLayout) view.findViewById(R.id.layout_six);
            layout_asymmetric_main = (LinearLayout) view.findViewById(R.id.layout_asymmetric_main);

            img_two_1 = (ImageView) view.findViewById(R.id.img_two_1);
            img_two_2 = (ImageView) view.findViewById(R.id.img_two_2);
            empty_two_2 = (View) view.findViewById(R.id.empty_two_2);

            img_four_1 = (ImageView) view.findViewById(R.id.img_four_1);
            img_four_2 = (ImageView) view.findViewById(R.id.img_four_2);
            img_four_3 = (ImageView) view.findViewById(R.id.img_four_3);
            img_four_4 = (ImageView) view.findViewById(R.id.img_four_4);
            empty_four_4 = (View) view.findViewById(R.id.empty_four_4);

            img_six_1 = (ImageView) view.findViewById(R.id.img_six_1);
            img_six_2 = (ImageView) view.findViewById(R.id.img_six_2);
            img_six_3 = (ImageView) view.findViewById(R.id.img_six_3);
            img_six_4 = (ImageView) view.findViewById(R.id.img_six_4);
            img_six_5 = (ImageView) view.findViewById(R.id.img_six_5);
            tv_more = (TextView) view.findViewById(R.id.tv_more);
            feature_seller_image = (ImageView)view.findViewById(R.id.feature_seller_image);
            //txt_feature_count = (TextView)view.findViewById(R.id.txt_feature_count);
            rating_bar_feature = (RatingBar)view.findViewById(R.id.rating_bar_feature);

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
