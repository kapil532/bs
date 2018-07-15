package com.blueshak.app.blueshak.seller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.blueshak.app.blueshak.search.SearchListAdapter;
import com.blueshak.app.blueshak.services.model.ProductModel;
import com.blueshak.app.blueshak.services.model.ReviewsRatingsModel;
import com.blueshak.app.blueshak.util.BlueShakLog;
import com.blueshak.app.blueshak.util.LocationService;
import com.blueshak.blueshak.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by lsingh013 on 03/06/18.
 */

public class SellerReviewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = "SearchActivity";
    private Context context;
    private List<ReviewsRatingsModel> ratingsList;
    boolean is_bookmark_needed = false;
    protected boolean showLoader;
    private static final int VIEWTYPE_ITEM = 1;
    private static final int VIEWTYPE_LOADER = 2;

    public SellerReviewsAdapter(Context mContext, List<ReviewsRatingsModel> ratingsList) {
        this.context = mContext;
        this.ratingsList = ratingsList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.seller_reviews_item, parent, false);
        return new SellerReviewsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder view_holder, int position) {
        try {
            if(view_holder instanceof MyViewHolder){
                final MyViewHolder holder = (MyViewHolder)view_holder;
                final ReviewsRatingsModel ratingsModel = ratingsList.get(position);

                holder.tv_seller_name.setText(ratingsModel.getReviewer_name());

                if(ratingsModel.getRating()!=null && !TextUtils.isEmpty(ratingsModel.getRating())){
                    holder.ratingBar.setRating(Float.parseFloat(ratingsModel.getRating()));
                }

                holder.tv_description.setText(ratingsModel.getComment());

                String image = ratingsModel.getseller_image();
                ImageLoader imageLoader = ImageLoader.getInstance();
                DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                        .cacheOnDisc(true).resetViewBeforeLoading(true)
                        .showImageForEmptyUri(R.drawable.placeholder_background)
                        .showImageOnFail(R.drawable.placeholder_background)
                        .showImageOnLoading(R.drawable.placeholder_background).build();
                imageLoader.displayImage(image, holder.img_seller, options);




            }else if(view_holder instanceof VHLoader){
                VHLoader loaderViewHolder = (VHLoader) view_holder;
                if (showLoader) {
                    loaderViewHolder.progressBar.setVisibility(View.VISIBLE);
                } else {
                    loaderViewHolder.progressBar.setVisibility(View.GONE);
                }
                return;
            }


        }catch (Exception e){
            BlueShakLog.logDebug(TAG,"Error -> "+e.getLocalizedMessage());
        }

    }

    @Override
    public int getItemCount() {
        return ratingsList.size();
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

    class VHLoader extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public VHLoader(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.bottom_progress_bar);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private View view_;
        private ImageView img_seller;
        private TextView tv_seller_name;
        private TextView tv_description;
        private RatingBar ratingBar;

        public MyViewHolder(View view) {
            super(view);
            view_ = view;
            img_seller = (ImageView) view.findViewById(R.id.img_seller_review);
            tv_seller_name = (TextView) view.findViewById(R.id.tv_seller_name);
            ratingBar = (RatingBar)view.findViewById(R.id.seller_rating);
            tv_description = (TextView)view.findViewById(R.id.tv_description);

        }
    }

}
