package com.langoor.app.blueshak.reviews_rating;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.langoor.blueshak.R;
import com.langoor.app.blueshak.global.GlobalFunctions;
import com.langoor.app.blueshak.global.GlobalVariables;
import com.langoor.app.blueshak.helper.RoundedImageView;
import com.langoor.app.blueshak.login.LoginActivity;
import com.langoor.app.blueshak.root.RootActivity;
import com.langoor.app.blueshak.services.ServerResponseInterface;
import com.langoor.app.blueshak.services.ServicesMethodsManager;
import com.langoor.app.blueshak.services.model.ErrorModel;
import com.langoor.app.blueshak.services.model.HomeListModel;
import com.langoor.app.blueshak.services.model.ProductModel;
import com.langoor.app.blueshak.services.model.ProfileDetailsModel;
import com.langoor.app.blueshak.services.model.ReviewsRatingsListModel;
import com.langoor.app.blueshak.services.model.ReviewsRatingsModel;
import com.langoor.app.blueshak.services.model.SalesModel;
import com.langoor.app.blueshak.services.model.ShopModel;
import com.langoor.app.blueshak.services.model.StatusModel;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.vlonjatg.progressactivity.ProgressActivity;
import java.util.ArrayList;
import java.util.List;

import java.util.logging.LogRecord;

public class ReviewsRatings extends RootActivity {
    private static final String TAG = "ReviewsRatings";
    static final String  REVIEWS_RATINGS_PRODUVT_MODEL_ID = "ReviewsRatingsWithDetails";
    static final String  REVIEWS_RATINGS_PRODUVT_SHOP_MODEL_ID = "ReviewsRatingsShopModelWithDetails";
    static final String  REVIEWS_RATINGS_PRODUVT_SALE_MODEL_ID = "ReviewsRatingsSaleModelWithDetails";
    static final String  REVIEWS_RATINGS_PRODUVT_PROFILE_MODEL_ID = "ReviewsRatingsProfileModelWithDetails";
    private TextView seller_name;
    private ListView reviews_rating_list_view;
    private Button sumbit_ratings;
    private ProgressActivity progressActivity;
    private Context context;
    private TextView no_sales,close_button;
    public static Activity activity;
    public Window window;
    private List<ReviewsRatingsModel> relatedList = new ArrayList<ReviewsRatingsModel>();
    private ProductModel productModel=null;
    private ShopModel shopModel=null;
    private SalesModel salesModel=null;
    private ProfileDetailsModel profileDetailsModel=null;
    private ReviewsRatingListAdapter adapter;
    private String seller_id;
    private ReviewsRatingsListModel reviewsRatingsModel=new ReviewsRatingsListModel();
    public static FragmentManager mainActivityFM;
    private LayoutInflater inflater ;
    private FrameLayout  drawer;
    private RoundedImageView seller_image;
    private boolean signed_user=false;
    private RatingBar ratingBar1;
    private Handler handler=new Handler();
    private ImageView go_back;
    private CardView user_commu_content;
    private ProgressBar progress_bar;
    public static Intent newInstance(Context context, ProductModel productModel, ShopModel shopModel, ProfileDetailsModel profileDetailsModel, SalesModel salesModel){
        Intent mIntent = new Intent(context, ReviewsRatings.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(REVIEWS_RATINGS_PRODUVT_MODEL_ID,productModel);
        bundle.putSerializable(REVIEWS_RATINGS_PRODUVT_SHOP_MODEL_ID,shopModel);
        bundle.putSerializable(REVIEWS_RATINGS_PRODUVT_PROFILE_MODEL_ID,profileDetailsModel);
        bundle.putSerializable(REVIEWS_RATINGS_PRODUVT_SALE_MODEL_ID,salesModel);
        mIntent.putExtras(bundle);
        return mIntent;
    }
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews_ratings);
        try{
            activity=this;
            window=getWindow();
            context=this;
            progress_bar=(ProgressBar)findViewById(R.id.progress_bar);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            user_commu_content=(CardView)findViewById(R.id.user_commu_content);
            LayoutInflater inflator = LayoutInflater.from(this);
            View v = inflator.inflate(R.layout.action_bar_titlel, null);
            close_button=(TextView)v.findViewById(R.id.cancel);
            close_button.setVisibility(View.GONE);
            ((TextView)v.findViewById(R.id.title)).setText("Reviews");
            toolbar.addView(v);
            go_back=(ImageView)v.findViewById(R.id.go_back);
            go_back.setVisibility(View.VISIBLE);
            go_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            mainActivityFM=getSupportFragmentManager();
            progressActivity = (ProgressActivity) findViewById(R.id.products_details_progressActivity);
            sumbit_ratings = (Button) findViewById(R.id.sumbit_ratings);
            ratingBar1=(RatingBar)findViewById(R.id.ratingBar1);
            seller_name=(TextView)findViewById(R.id.seller_name);
            no_sales = (TextView) findViewById(R.id.no_sales);
            reviews_rating_list_view=(ListView)findViewById(R.id.reviews_rating_list_view);
            seller_image=(RoundedImageView)findViewById(R.id.rounde_image);
            if(getIntent().hasExtra(REVIEWS_RATINGS_PRODUVT_MODEL_ID))
                productModel = (ProductModel) getIntent().getExtras().getSerializable(REVIEWS_RATINGS_PRODUVT_MODEL_ID);
            if(getIntent().hasExtra(REVIEWS_RATINGS_PRODUVT_SHOP_MODEL_ID))
                shopModel = (ShopModel) getIntent().getExtras().getSerializable(REVIEWS_RATINGS_PRODUVT_SHOP_MODEL_ID);
            if(getIntent().hasExtra(REVIEWS_RATINGS_PRODUVT_PROFILE_MODEL_ID))
                profileDetailsModel = (ProfileDetailsModel) getIntent().getExtras().getSerializable(REVIEWS_RATINGS_PRODUVT_PROFILE_MODEL_ID);
            if(getIntent().hasExtra(REVIEWS_RATINGS_PRODUVT_SALE_MODEL_ID))
                salesModel = (SalesModel) getIntent().getExtras().getSerializable(REVIEWS_RATINGS_PRODUVT_SALE_MODEL_ID);

            LayoutInflater factory = LayoutInflater.from(this);
            adapter = new ReviewsRatingListAdapter(factory,relatedList);
            reviews_rating_list_view.setAdapter(adapter);

            if(reviewsRatingsModel!=null){
                setThisPage();
            }else{
                showErrorPage();
            }
            sumbit_ratings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(GlobalFunctions.is_loggedIn(context)){
                        boolean is_Garage=false;
                        if((productModel!=null)){
                            if(productModel.getSaleType()!=null){
                                is_Garage=productModel.getSaleType().equalsIgnoreCase(GlobalVariables.TYPE_GARAGE)?true:false;
                            }
                        }
                        Intent intent=AddRating.newInstance(context,is_Garage,seller_id);
                        startActivity(intent);
                    }else {
                        showSettingsAlert();
                    }

                }
            });
            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            drawer = (FrameLayout) inflater.inflate(R.layout.review_activity_main, null); // "null" is important.
        }catch (Exception e){
            e.printStackTrace();
            Crashlytics.log(e.getMessage());
        }
    }
    @Override
    public void onResume() {
        if(!GlobalFunctions.isNetworkAvailable(context)){
            Snackbar.make(no_sales, "Please check your internet connection", Snackbar.LENGTH_LONG)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {

                        }
                    }).show();
        }else{
            getReviewRatings(seller_id);

        }
        super.onResume();
    }
    private void setThisPage(){
        String avatar="";
        String sellerName="";
        if(productModel!=null){
            if(!TextUtils.isEmpty(productModel.getSellerName())){
                seller_id=productModel.getSeller_id();
                sellerName=productModel.getSellerName();
                avatar=productModel.getSeller_image();
            }else{
                if(salesModel!=null){
                    seller_id=salesModel.getUserID();
                    sellerName=salesModel.getSellerName();
                    avatar=salesModel.getSeller_image();
                }
            }
        }else if(shopModel!=null){
            seller_id=shopModel.getProfileDetailsModel().getId();
            sellerName=shopModel.getProfileDetailsModel().getName();
            avatar=shopModel.getProfileDetailsModel().getAvatar();
        }else if(profileDetailsModel!=null){
            seller_id=profileDetailsModel.getId();
            sellerName=profileDetailsModel.getName();
            avatar=profileDetailsModel.getAvatar();
        }
        String signed_user_user_id=GlobalFunctions.getSharedPreferenceString(this, GlobalVariables.SHARED_PREFERENCE_USERID);
        if(signed_user_user_id!=null && seller_id!=null){
            if(Integer.parseInt(seller_id) == Integer.parseInt(signed_user_user_id)) {
                signed_user=true;
                user_commu_content.setVisibility(View.GONE);

            }
        }
        seller_name.setText(sellerName);
        if(!TextUtils.isEmpty(avatar)){
            Picasso.with(activity)
                    .load(avatar)
                    .placeholder(R.drawable.squareplaceholder)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .fit().centerCrop()
                    .into(seller_image);
        }
        String temp="";
       /* if(reviewsRatingsModel!=null){
           *//* setReviewsRatings(reviewsRatingsModel);*//*
            if(reviewsRatingsModel.getCummalative_rating()!=null&&!TextUtils.isEmpty(reviewsRatingsModel.getCummalative_rating())&&!reviewsRatingsModel.getCummalative_rating().equalsIgnoreCase("null"))
                ratingBar1.setRating(Float.parseFloat(reviewsRatingsModel.getCummalative_rating()));
        }*/

    }
    private void showErrorPage(){
        if(progressActivity!=null){
            progressActivity.showError(context.getResources().getDrawable(R.drawable.ic_error), "No Connection",
                    "We could not establish a connection with our servers. Try again when you are connected to the internet.",
                    "Try Again", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ReviewsRatings reviewsRatings = new ReviewsRatings();
                            reviewsRatings.setThisPage();
                        }
                    });
        }
    }

    private void showEmptyPage(){
        if(progressActivity!=null){
            progressActivity.showEmpty(context.getResources().getDrawable(R.drawable.ic_error), "Empty Shopping Cart",
                    "Please add things in the cart to continue.");
        }
    }

    public void saveReviewsRatings(int rating,String comment){
        GlobalFunctions.showProgress(context, "Rating ...");
        boolean is_Garage=false;
            if(productModel.getSaleType()!=null){
                is_Garage=productModel.getSaleType().equalsIgnoreCase(GlobalVariables.TYPE_GARAGE)?true:false;
            }
        ReviewsRatingsModel reviewsRatingsModel=new ReviewsRatingsModel();
        reviewsRatingsModel.setTitle("Review");
        reviewsRatingsModel.setComment(comment);
        reviewsRatingsModel.setIs_garage_review(is_Garage);
        reviewsRatingsModel.setReviewer_user_id(seller_id);
        reviewsRatingsModel.setRating(Integer.toString(rating));
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.setReviewsRatings(context,reviewsRatingsModel,new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                GlobalFunctions.hideProgress();
                Log.d(TAG, "onSuccess Response");
                if(arg0 instanceof StatusModel){
                    StatusModel statusModel = (StatusModel) arg0;
                    if(statusModel.isStatus()){
                       /*startActivity(new Intent(ReviewsRatings.this,ReviewThankYou.class));*/
                        Toast.makeText(context,"Thank you for your rating",Toast.LENGTH_LONG).show();
                        finish();
                    }
                }else if(arg0 instanceof ErrorModel){
                    ErrorModel errorModel = (ErrorModel) arg0;
                    String msg = errorModel.getError()!=null ? errorModel.getError() : errorModel.getMessage();
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void OnFailureFromServer(String msg) {
                GlobalFunctions.hideProgress();
                Log.d(TAG, msg);
            }

            @Override
            public void OnError(String msg) {
                GlobalFunctions.hideProgress();
                Log.d(TAG, msg);
            }
        }, "Similar Products");
    }

    private void getReviewRatings(String user_id){
        showProgressBar();
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getReviewsRatings(context,user_id, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                hideProgressBar();
                Log.d(TAG, "onSuccess Response");
                if(arg0 instanceof StatusModel){
                    StatusModel statusModel = (StatusModel) arg0;
                    if(statusModel.isStatus()){
                        Toast.makeText(context, statusModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    no_sales.setVisibility(View.VISIBLE);
                }else if(arg0 instanceof ErrorModel){
                    hideProgressBar();
                    ErrorModel errorModel = (ErrorModel) arg0;
                    String msg = errorModel.getError()!=null ? errorModel.getError() : errorModel.getMessage();
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                    no_sales.setVisibility(View.VISIBLE);
                }else if(arg0 instanceof ReviewsRatingsListModel){
                    hideProgressBar();
                    no_sales.setVisibility(View.GONE);
                    reviewsRatingsModel = (ReviewsRatingsListModel)arg0;
                    setValues();
                 /*   proceedToReviews();*/
                   /* setReviewsRatings(reviewsRatingsModel);*/
                    Log.d(TAG, reviewsRatingsModel.toString());       }
            }

            @Override
            public void OnFailureFromServer(String msg) {
                hideProgressBar();
                Log.d(TAG, msg);
                no_sales.setVisibility(View.VISIBLE);
            }

            @Override
            public void OnError(String msg) {
                hideProgressBar();
                Log.d(TAG, msg);
                no_sales.setVisibility(View.VISIBLE);
            }
        }, "Review Ratings");

    }
    private void setValues(){
        if(reviewsRatingsModel!=null){
            if(reviewsRatingsModel.getCummalative_rating()!=null&&!TextUtils.isEmpty(reviewsRatingsModel.getCummalative_rating())&&!reviewsRatingsModel.getCummalative_rating().equalsIgnoreCase("null"))
                ratingBar1.setRating(Float.parseFloat(reviewsRatingsModel.getCummalative_rating()));
            List<ReviewsRatingsModel> reviews_list = reviewsRatingsModel.getReviews_list();
            if(reviews_list!=null){
                if(reviews_list.size()>0&&reviews_rating_list_view!=null&&reviews_list!=null&&adapter!=null){
                    relatedList.clear();
                    relatedList.addAll(reviews_list);
                    refreshList();
                }else
                    no_sales.setVisibility(View.VISIBLE);
            }
        }else
            no_sales.setVisibility(View.VISIBLE);

    }
    public void refreshList(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                synchronized (adapter){adapter.notifyDataSetChanged();}
            }
        });
    }
    public void showSettingsAlert(){
      /*  final com.langoor.app.blueshak.view.AlertDialog alertDialog = new com.langoor.app.blueshak.view.AlertDialog(context);
        alertDialog.setMessage("Please Login/Register to continue");
        alertDialog.setTitle("Login/Register");
        alertDialog.setPositiveButton("Continue", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent creategarrage = LoginActivity.newInstance(activity,null,null);
                startActivity(creategarrage);
                closeThisActivity();
            }
        });
        alertDialog.show();*/
        Intent creategarrage =new Intent(activity,LoginActivity.class);
        startActivity(creategarrage);


    }
    public static void closeThisActivity(){
        if(activity!=null){activity.finish();}
    }
    public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
        private Drawable mDivider;

        public SimpleDividerItemDecoration(Context context) {
            mDivider = context.getResources().getDrawable(R.drawable.line_divider);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();

                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }
    public void showProgressBar(){
        if(progress_bar!=null)
            progress_bar.setVisibility(View.VISIBLE);
    }
    public void hideProgressBar(){
        if(progress_bar!=null)
            progress_bar.setVisibility(View.GONE);
    }
}
