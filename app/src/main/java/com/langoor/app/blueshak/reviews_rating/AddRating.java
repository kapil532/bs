package com.langoor.app.blueshak.reviews_rating;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.langoor.blueshak.R;
import com.langoor.app.blueshak.global.GlobalFunctions;
import com.langoor.app.blueshak.global.GlobalVariables;
import com.langoor.app.blueshak.login.LoginActivity;
import com.langoor.app.blueshak.root.RootActivity;
import com.langoor.app.blueshak.services.ServerResponseInterface;
import com.langoor.app.blueshak.services.ServicesMethodsManager;
import com.langoor.app.blueshak.services.model.ErrorModel;
import com.langoor.app.blueshak.services.model.ProductModel;
import com.langoor.app.blueshak.services.model.ReviewsRatingsListModel;
import com.langoor.app.blueshak.services.model.ReviewsRatingsModel;
import com.langoor.app.blueshak.services.model.ShopModel;
import com.langoor.app.blueshak.services.model.StatusModel;

public class AddRating extends RootActivity {
    private static final String TAG = "ReviewsRatings";
    static final String  REVIEWS_RATINGS_IS_GARAGE = "ReviewsRatingsIsGarage";
    static final String  REVIEWS_RATINGS_SELLER_USER_ID = "ReviewsRatingsModelSellerId";
    static Context context;
    static Activity activity;
    static final String  PRODUCTDETAIL_BUNDLE_KEY_POSITION = "ProductModelWithDetails";
    static final String  PRODUCTDETAIL_BUNDLE_KEY_FLAG = "ProductModelWithFlag";
    static final String  SELL_MODEL = "sellmodel";
    public static ProductModel productModel = new ProductModel();
    public RatingBar ratingbar1;
    public Button add_review;
    private  boolean is_garage=false;
    private  String user_id;
    private EditText review;
    private TextView close_button;
    private ProgressBar progress_bar;
    public static Intent newInstance(Context context,boolean is_garage,String user_id){
        Intent mIntent = new Intent(context, AddRating.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(REVIEWS_RATINGS_IS_GARAGE,is_garage);
        bundle.putString(REVIEWS_RATINGS_SELLER_USER_ID,user_id);
        mIntent.putExtras(bundle);
        return mIntent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
      */  setContentView(R.layout.activity_add_rating);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        review=(EditText)findViewById(R.id.review);
        ratingbar1=(RatingBar)findViewById(R.id.ratingBar1);
        add_review=(Button)findViewById(R.id.add_review);
        add_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rating=String.valueOf(ratingbar1.getRating());
                String review_=review.getText().toString();
                if(!TextUtils.isEmpty(review_))
                    saveReviewsRatings(rating,review_);
                  /*  Toast.makeText(getApplicationContext(), rating, Toast.LENGTH_LONG).show();*/
            }
        });
        setSupportActionBar(toolbar);
        context=this;
        activity=this;
        LayoutInflater inflator = LayoutInflater.from(this);
        View v = inflator.inflate(R.layout.action_bar_titlel, null);
        close_button=(TextView)v.findViewById(R.id.cancel);
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeThisActivity();
            }
        });
        ((TextView)v.findViewById(R.id.title)).setText("Review");
        toolbar.addView(v);
        progress_bar=(ProgressBar)findViewById(R.id.progress_bar);
        if(getIntent().hasExtra(REVIEWS_RATINGS_IS_GARAGE))
            is_garage=getIntent().getExtras().getBoolean(REVIEWS_RATINGS_IS_GARAGE);
        if(getIntent().hasExtra(REVIEWS_RATINGS_SELLER_USER_ID))
            user_id =  getIntent().getExtras().getString(REVIEWS_RATINGS_SELLER_USER_ID);
    }
    public void
    saveReviewsRatings(String rating,String comment){
        showProgressBar();
        ReviewsRatingsModel reviewsRatingsModel=new ReviewsRatingsModel();
        reviewsRatingsModel.setTitle("Review");
        reviewsRatingsModel.setComment(comment);
        reviewsRatingsModel.setIs_garage_review(is_garage);
        reviewsRatingsModel.setReviewer_user_id(user_id);
        reviewsRatingsModel.setRating(rating);
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.setReviewsRatings(context,reviewsRatingsModel,new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
               hideProgressBar();
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
                hideProgressBar();
                Log.d(TAG, msg);
            }

            @Override
            public void OnError(String msg) {
                hideProgressBar();
                Log.d(TAG, msg);
            }
        }, "Similar Products");

    }
    public void showSettingsAlert(){
        Intent creategarrage = new Intent(activity,LoginActivity.class);
        startActivity(creategarrage);
        closeThisActivity();
    }
    public static void closeThisActivity(){
        if(activity!=null){activity.finish();}
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
