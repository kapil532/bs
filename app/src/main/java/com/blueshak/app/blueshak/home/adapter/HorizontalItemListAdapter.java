package com.blueshak.app.blueshak.home.adapter;

import android.content.Context;
import android.content.Intent;
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

import com.blueshak.app.blueshak.global.GlobalFunctions;
import com.blueshak.app.blueshak.global.GlobalVariables;
import com.blueshak.app.blueshak.item.ProductDetail;
import com.blueshak.app.blueshak.login.LoginActivity;
import com.blueshak.app.blueshak.services.ServerResponseInterface;
import com.blueshak.app.blueshak.services.ServicesMethodsManager;
import com.blueshak.app.blueshak.services.model.ErrorModel;
import com.blueshak.app.blueshak.services.model.ProductModel;
import com.blueshak.app.blueshak.services.model.StatusModel;
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
    private List<ProductModel> albumList;
    protected boolean showLoader;
    private static final int VIEWTYPE_ITEM = 1;
    private static final int VIEWTYPE_LOADER = 2;
    public String item_address;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        protected TextView item_price, item_name;
        protected ImageView image_iv, is_sold;
        public View container;

        public MyViewHolder(View view) {
            super(view);
            container = view;
            item_price = (TextView) view.findViewById(R.id.item_price);
            image_iv = (ImageView) view.findViewById(R.id.product_image);
            is_sold = (ImageView) view.findViewById(R.id.is_sold);
            item_name = (TextView) view.findViewById(R.id.item_name);

        }
    }

    class VHLoader extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public VHLoader(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.bottom_progress_bar);
        }
    }

    public HorizontalItemListAdapter(Context mContext, List<ProductModel> albumList) {
        this.context = mContext;
        this.albumList = albumList;
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
    public void onBindViewHolder(RecyclerView.ViewHolder view_holder, int position) {
        try {
            if (view_holder instanceof HorizontalItemListAdapter.MyViewHolder) {
                final HorizontalItemListAdapter.MyViewHolder holder = (HorizontalItemListAdapter.MyViewHolder) view_holder;
                final ProductModel obj = albumList.get(position);
                holder.item_name.setText(obj.getName());
                if (obj.isHide_item_price()) {
                    holder.item_price.setText("Negotiable");
                } else {
                    holder.item_price.setText(GlobalFunctions.getFormatedAmount(obj.getCurrency(), obj.getSalePrice()));
                }
                if (!obj.isAvailable()) {
                    holder.is_sold.setVisibility(View.VISIBLE);
                    holder.is_sold.setImageResource(R.drawable.ic_sold);
                } else {
                    holder.is_sold.setVisibility(View.INVISIBLE);
                }

                if (obj.is_new()) {
                    holder.is_sold.setVisibility(View.VISIBLE);
                    holder.is_sold.setImageResource(R.drawable.ic_new);
                }


                holder.container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                     /*  getItemInfo(obj.getId());*/
                        Intent intent = ProductDetail.newInstance(context, obj, null, GlobalVariables.TYPE_MY_SALE);
                        context.startActivity(intent);

                    }
                });

                String item_image = obj.getItem_display_Image();
                ImageLoader imageLoader = ImageLoader.getInstance();
                DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                        .cacheOnDisc(true).resetViewBeforeLoading(true)
                        .showImageForEmptyUri(R.drawable.placeholder_background)
                        .showImageOnFail(R.drawable.placeholder_background)
                        .showImageOnLoading(R.drawable.placeholder_background).build();
                //download and display image from url
                imageLoader.displayImage(item_image, holder.image_iv, options);
            } else if (view_holder instanceof HorizontalItemListAdapter.VHLoader) {
                HorizontalItemListAdapter.VHLoader loaderViewHolder = (HorizontalItemListAdapter.VHLoader) view_holder;
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

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    class MyClickableSpan extends ClickableSpan { //clickable span
        public void onClick(View textView) {

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(context.getResources().getColor(R.color.tab_selected));//set text color

        }
    }

    private void addBookmark(final Context context, String productID) {
        /*GlobalFunctions.showProgress(context, "Bookmarking Product...");*/
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.addBookmark(context, productID, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                /*GlobalFunctions.hideProgress();*/
                if (arg0 instanceof StatusModel) {
                    StatusModel statusModel = (StatusModel) arg0;
                    if (statusModel.isStatus()) {

                        Toast.makeText(context, "Added to bookmarks", Toast.LENGTH_SHORT).show();
                    }
                } else if (arg0 instanceof ErrorModel) {
                    ErrorModel errorModel = (ErrorModel) arg0;
                    String msg = errorModel.getError() != null ? errorModel.getError() : errorModel.getMessage();
                   /* Toast.makeText(context, msg, Toast.LENGTH_LONG).show();*/
                }
            }

            @Override
            public void OnFailureFromServer(String msg) {
                /*GlobalFunctions.hideProgress();*/
               /* Toast.makeText(context, msg, Toast.LENGTH_LONG).show();*/
            }

            @Override
            public void OnError(String msg) {
               /* GlobalFunctions.hideProgress();*/
             /*   Toast.makeText(context, msg, Toast.LENGTH_LONG).show();*/
            }
        }, "Add Bookmarks");

    }

    private void deleteBookmark(final Context context, String productID) {
        /*GlobalFunctions.showProgress(context, "Bookmarking Product...");*/
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.deleteBookmark(context, productID, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                /*GlobalFunctions.hideProgress();*/
                if (arg0 instanceof StatusModel) {
                    StatusModel statusModel = (StatusModel) arg0;
                    if (statusModel.isStatus()) {
                        Toast.makeText(context, "Bookmark Removed", Toast.LENGTH_SHORT).show();
                    }
                } else if (arg0 instanceof ErrorModel) {
                    ErrorModel errorModel = (ErrorModel) arg0;
                    String msg = errorModel.getError() != null ? errorModel.getError() : errorModel.getMessage();
                    /*Toast.makeText(context, msg, Toast.LENGTH_LONG).show();*/
                }
            }

            @Override
            public void OnFailureFromServer(String msg) {
                /*GlobalFunctions.hideProgress();*/
               /* Toast.makeText(context, msg, Toast.LENGTH_LONG).show();*/
            }

            @Override
            public void OnError(String msg) {
               /* GlobalFunctions.hideProgress();*/
                /*Toast.makeText(context, msg, Toast.LENGTH_LONG).show();*/
            }
        }, "Add Bookmarks");

    }

    public void add(List<ProductModel> items) {
        int previousDataSize = this.albumList.size();
        this.albumList.addAll(items);
        notifyItemRangeInserted(previousDataSize, items.size());
    }

    private void getItemInfo(String product_id) {
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getItemInfo(context, product_id, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                ProductModel productModel = (ProductModel) arg0;
                Intent intent = ProductDetail.newInstance(context, productModel, null, GlobalVariables.TYPE_MY_SALE);
                context.startActivity(intent);
            }

            @Override
            public void OnFailureFromServer(String msg) {

            }

            @Override
            public void OnError(String msg) {

            }
        }, "GetItemInfo onSuccess Response");

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
