package com.langoor.app.blueshak.reviews_rating;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.langoor.blueshak.R;
import com.langoor.app.blueshak.helper.RoundedImageView;
import com.langoor.app.blueshak.services.model.ReviewsRatingsModel;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import java.util.List;

public class ReviewsRatingListAdapter extends ArrayAdapter<ReviewsRatingsModel> {

    private final List<ReviewsRatingsModel> list;
    private final LayoutInflater layoutInflater;

    public ReviewsRatingListAdapter(LayoutInflater layoutInflater, List<ReviewsRatingsModel> list) {
        super(layoutInflater.getContext(), R.layout.listviewcell_review_rate_item, list);
        this.layoutInflater = layoutInflater;
        this.list = list;
    }

    static class ViewHolder {
        protected TextView name_tv,item_description;
        protected ImageView rate1,rate2,rate3,rate4,rate5;
        protected RoundedImageView seller_image;
        private RatingBar ratingBar1;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ReviewsRatingsModel obj = list.get(position);
        View view = null;
        try{
            if (convertView == null) {
                view = layoutInflater.inflate(R.layout.listviewcell_review_rate_item, null);
                final ViewHolder viewHolder = new ViewHolder();
                viewHolder.name_tv = (TextView) view.findViewById(R.id.product_detail_name_tv);
                viewHolder.item_description = (TextView) view.findViewById(R.id.item_description);
                viewHolder.rate1= (ImageView) view.findViewById(R.id.rating_image1);
                viewHolder.rate2= (ImageView) view.findViewById(R.id.rating_image2);

                viewHolder.rate3= (ImageView) view.findViewById(R.id.rating_image3);
                viewHolder.rate4= (ImageView) view.findViewById(R.id.rating_image4);
                viewHolder.rate5= (ImageView) view.findViewById(R.id.rating_image5);
                viewHolder.seller_image= (RoundedImageView) view.findViewById(R.id.seller_image);
                viewHolder.ratingBar1= (RatingBar) view.findViewById(R.id.ratingBar1);

                view.setTag(viewHolder);
            } else {
                view = convertView;
            }
            final ViewHolder holder = (ViewHolder) view.getTag();
            holder.name_tv.setText(obj.getReviewer_name());
            holder.item_description.setText(obj.getComment());
            String avatar=obj.getseller_image();
            if(!TextUtils.isEmpty(avatar)){
                Picasso.with(getContext())
                        .load(avatar)
                        .placeholder(R.drawable.squareplaceholder)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .fit().centerCrop()
                        .into(holder.seller_image);
            }
            if(obj.getRating()!=null&&!TextUtils.isEmpty(obj.getRating()))
                holder.ratingBar1.setRating(Float.parseFloat(obj.getRating()));
            Double rating_double=Double.parseDouble(obj.getRating());
            holder.ratingBar1.setRating(Float.parseFloat(obj.getRating()));
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
        return view;
    }
}
