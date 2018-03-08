package com.blueshak.app.blueshak.home;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blueshak.app.blueshak.base.PresenterCallBack;
import com.blueshak.app.blueshak.home.adapter.HorizontalItemListAdapter;
import com.blueshak.app.blueshak.home.model.FeatureItemData;
import com.blueshak.app.blueshak.home.model.FeatureItemsModel;
import com.blueshak.app.blueshak.home.presenter.ItemListPresenter;
import com.blueshak.app.blueshak.services.model.FilterModel;
import com.blueshak.app.blueshak.util.BlueShakLog;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/*import com.langoor.app.blueshak.ImageCashing.ImageLoader;*/


public class ItemListAdapterForList extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
        ViewPagerEx.OnPageChangeListener {
    public static final String TAG = "ItemListAdapter";
    private Context context;
    private List<ProductModel> albumList;
    public ArrayList<FeatureItemData> featureItemList;
    protected boolean showLoader;
    private static final int VIEWTYPE_ITEM = 1;
    private static final int VIEWTYPE_LOADER = 2;
    public String item_address;
    private EndlessRecyclerOnScrollListenerForList endlessRecyclerOnScrollListener;
    private FeatureItemLoadMore iLoadMore;
    private Handler handler = new Handler();
    private FilterModel filterModel;
    private HorizontalItemListAdapter itemListDataAdapter;
   // public static boolean isFeatureItemLoaded = false;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        protected TextView item_price, item_name, item_location, shipping_type;
        protected ImageView image_iv, favarite, is_sold, is_garage, shippable, pick_up;
        public View container;
        public RecyclerView horizontalRecyclerView;
        private TextView txt_feature_items;
        private TextView txt_seller_items;
        private LinearLayout feature_below_line;
        private LinearLayout feature_line;
        private LinearLayout seller_line;
        private ImageView is_featured;

        public MyViewHolder(View view) {
            super(view);
            container = view;
            item_price = (TextView) view.findViewById(R.id.item_price);
            image_iv = (ImageView) view.findViewById(R.id.product_image);
            is_sold = (ImageView) view.findViewById(R.id.is_sold);
            is_featured = (ImageView) view.findViewById(R.id.is_featured);
            is_garage = (ImageView) view.findViewById(R.id.is_garage_item);
            favarite = (ImageView) view.findViewById(R.id.item_favirate);
            item_name = (TextView) view.findViewById(R.id.item_name);
            shippable = (ImageView) view.findViewById(R.id.shippable);
            pick_up = (ImageView) view.findViewById(R.id.pick_up);
            item_location = (TextView) view.findViewById(R.id.item_location);
            //change this value
            shipping_type = (TextView) view.findViewById(R.id.shipping_type);
            horizontalRecyclerView = (RecyclerView)view.findViewById(R.id.horizontal_recycler_view);
            txt_feature_items = (TextView)view.findViewById(R.id.txt_feature_items);
            txt_seller_items = (TextView)view.findViewById(R.id.txt_seller_items);
            feature_below_line = (LinearLayout)view.findViewById(R.id.feature_below_line);
            feature_line = (LinearLayout)view.findViewById(R.id.feature_line);
            seller_line = (LinearLayout)view.findViewById(R.id.seller_line);

        }
    }

    class VHLoader extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public VHLoader(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.bottom_progress_bar);
        }
    }

    public ItemListAdapterForList(Context mContext,FilterModel filterModel, List<ProductModel> albumList,
                                  FeatureItemLoadMore iLoadMore) {
        this.context = mContext;
        this.albumList = albumList;
        //this.featureItemList = featureItemList;
        this.item_address = GlobalFunctions.getSharedPreferenceString(mContext, GlobalVariables.CURRENT_LOCATION);
        this.iLoadMore = iLoadMore;
        this.filterModel = filterModel;
     /*   imgLoader = new ImageLoader(mContext);*/


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      /*  if(viewType == VIEWTYPE_LOADER){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.*//*custom_bottom_progressbar*//*custom_loading_list_item, parent, false);
            return  new VHLoader(v);
        }else if(viewType == VIEWTYPE_ITEM){
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_row_item_new, parent, false);
            return new MyViewHolder(itemView);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
*/
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_row_item_new_for_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder view_holder, int position) {
        try {
            if (view_holder instanceof MyViewHolder) {
                final MyViewHolder holder = (MyViewHolder) view_holder;
              /*  final ProductModel obj = albumList.get(position-1);*/
                final ProductModel obj = albumList.get(position);

               /* if(position == ItemListFragmentForList.iListSize
                        && ItemListFragmentForList.last_page >= ItemListFragmentForList.iCurrentPage){
                    BlueShakLog.logDebug(TAG,"onLoadMore position "+position);
                    iLoadMore.onLoadMoreItems(ItemListFragmentForList.iCurrentPage+1);
                }*/

                holder.item_name.setText(obj.getName());

                if (obj.isShipping_foc()) {
                    holder.shipping_type.setText("Free Shipping");
                } else if (Float.parseFloat(obj.getLocal_shipping_cost()) == 0.00) {
                    holder.shipping_type.setText(" ");
                } else {
                    holder.shipping_type.setText("+" + GlobalFunctions.getFormatedAmount(obj.getCurrency(), obj.getLocal_shipping_cost()));
                }
                if (obj.isHide_item_price()) {

                    holder.item_price.setText("Negotiable");
                } else {

                    holder.item_price.setText(GlobalFunctions.getFormatedAmount(obj.getCurrency(), obj.getSalePrice()));
                }

                if (!TextUtils.isEmpty(obj.getAddress()))
                    holder.item_location.setText(obj.getAddress());
                else
                    holder.item_location.setVisibility(View.GONE);

                if (obj.is_bookmark())
                    holder.favarite.setImageResource(R.drawable.like_full);
                else
                    holder.favarite.setImageResource(R.drawable.like_border);
                /*if(obj.is_new())
                    holder.new_flag_image.setVisibility(View.VISIBLE);
                else
                    holder.new_flag_image.setVisibility(View.GONE);
                */
                if (obj.isPickup()) holder.pick_up.setVisibility(View.VISIBLE);
                if (obj.isShipable()) holder.shippable.setVisibility(View.VISIBLE);


                if (obj.is_new()) {
                    holder.is_sold.setVisibility(View.VISIBLE);
                    holder.is_featured.setVisibility(View.GONE);
                    holder.is_sold.setImageResource(R.drawable.ic_new);
                }else if (!obj.isAvailable() && obj.isIs_featured()) {
                    holder.is_sold.setVisibility(View.VISIBLE);
                    holder.is_featured.setVisibility(View.GONE);
                    holder.is_sold.setImageResource(R.drawable.ic_sold);
                }else if(!obj.isAvailable()){
                    holder.is_sold.setVisibility(View.VISIBLE);
                    holder.is_featured.setVisibility(View.GONE);
                    holder.is_sold.setImageResource(R.drawable.ic_sold);
                }else if(obj.is_product_new() && obj.isIs_featured()){
                    holder.is_sold.setVisibility(View.GONE);
                    holder.is_featured.setVisibility(View.VISIBLE);
                    holder.is_featured.setImageResource(R.drawable.icon_new_feature);
                }else if(obj.isIs_featured()){
                    holder.is_sold.setVisibility(View.GONE);
                    holder.is_featured.setVisibility(View.VISIBLE);
                    holder.is_featured.setImageResource(R.drawable.icon_feature);
                }else{
                    holder.is_sold.setVisibility(View.GONE);
                    holder.is_featured.setVisibility(View.GONE);
                }

               /* if (obj.is_garage_item())
                    holder.is_garage.setVisibility(View.VISIBLE);
                else
                    holder.is_garage.setVisibility(View.GONE);*/

                holder.container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                     /*  getItemInfo(obj.getId());*/
                        Intent intent = ProductDetail.newInstance(context, obj, null, GlobalVariables.TYPE_MY_SALE);
                        context.startActivity(intent);

                    }
                });
                holder.favarite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (GlobalFunctions.is_loggedIn(context)) {
                            if (obj.is_bookmark()) {
                                holder.favarite.setImageResource(R.drawable.like_border);
                                deleteBookmark(context, obj.getId());
                                obj.setIs_bookmark(false);
                            } else {
                                holder.favarite.setImageResource(R.drawable.like_full);
                                addBookmark(context, obj.getId());
                                obj.setIs_bookmark(true);
                            }
                        } else
                            showSettingsAlert();


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
            } else if (view_holder instanceof VHLoader) {
                VHLoader loaderViewHolder = (VHLoader) view_holder;
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

    private void setVisible(MyViewHolder holder){
        holder.horizontalRecyclerView.setVisibility(View.GONE);
        holder.txt_feature_items.setVisibility(View.GONE);
        holder.txt_seller_items.setVisibility(View.GONE);
        holder.feature_below_line.setVisibility(View.GONE);
        holder.feature_line.setVisibility(View.GONE);
        holder.seller_line.setVisibility(View.GONE);
    }



    @Override
    public int getItemCount() {
        if(albumList!=null){
            return albumList.size();
        }else{
            return 0;
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

    //    need to override this method
    /*@Override
    public int getItemViewType(int position) {
        if(isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }*/

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    class MyClickableSpan extends ClickableSpan { //clickable span
        public void onClick(View textView) {

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(context.getResources().getColor(R.color.tab_selected));//set text color
          /*  ds.setUnderlineText(true);*/
            //ds.setStyle(Typeface.BOLD);
          /*  ds.setTypeface(Typeface.DEFAULT_BOLD);*/
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
       /* if (position != 0 && position == getItemCount() - 1) {
            return VIEWTYPE_LOADER;
        }

        return VIEWTYPE_ITEM;*/

        return position;
    }

    public void showLoading(boolean status) {
        showLoader = status;
    }


}
