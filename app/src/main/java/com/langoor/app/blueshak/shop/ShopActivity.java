package com.langoor.app.blueshak.shop;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.langoor.blueshak.R;

import com.langoor.app.blueshak.global.GlobalFunctions;
import com.langoor.app.blueshak.global.GlobalVariables;
import com.langoor.app.blueshak.helper.RoundedImageView;

import com.langoor.app.blueshak.map.MapViewFragment;
import com.langoor.app.blueshak.profile.ProfileEditActivity;
import com.langoor.app.blueshak.reviews_rating.ReviewsRatings;
import com.langoor.app.blueshak.root.RootActivity;
import com.langoor.app.blueshak.search.SearchListAdapter;
import com.langoor.app.blueshak.services.ServerResponseInterface;
import com.langoor.app.blueshak.services.ServicesMethodsManager;
import com.langoor.app.blueshak.services.model.ErrorModel;
import com.langoor.app.blueshak.services.model.OTPResendModel;
import com.langoor.app.blueshak.services.model.ProductModel;
import com.langoor.app.blueshak.services.model.ProfileDetailsModel;
import com.langoor.app.blueshak.services.model.ReviewsRatingsListModel;
import com.langoor.app.blueshak.services.model.ReviewsRatingsModel;
import com.langoor.app.blueshak.services.model.SalesListModel;
import com.langoor.app.blueshak.services.model.SalesModel;
import com.langoor.app.blueshak.services.model.Shop;
import com.langoor.app.blueshak.services.model.ShopModel;
import com.langoor.app.blueshak.services.model.SimilarProductsModel;
import com.langoor.app.blueshak.services.model.StatusModel;
import com.langoor.app.blueshak.view_sales.MapFragmentSales;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class ShopActivity extends RootActivity {
    String TAG = "ShopActivity";
    private Handler handler=new Handler();
    private Toolbar toolbar;
    public static FragmentManager mainActivityFM;
    static Context mainContext;
    static Window mainWindow;
    private FragmentManager fragmentManager;
    private MenuItem item;
    static GlobalFunctions globalFunctions = new GlobalFunctions();
    static GlobalVariables globalVariables = new GlobalVariables();
    static Context context;
    private  RoundedImageView rounde_image;
    static Activity activity;
    private ProfileDetailsModel profileDetailsModel=new ProfileDetailsModel();
    private TextView shop_tile,close_button,shop_listings,shop_sold,profile_name,shop_location,shop_member_since,shop_response_time,sold_count,shop_listings_count,shop_link;
    private ImageView go_back,rating1,rating2,rating3,rating4,rating5,email_verified,facebook_verified,phone_number_verified,online_status;
    static final String  PRODUCTDETAIL_BUNDLE_KEY_POSITION = "ProductModelWithDetails";
    static final String  PRODUCTDETAIL_BUNDLE_KEY_FLAG = "ProductModelWithFlag";
    static final String  PRODUCTDETAIL_SHOP_BUNDLE_KEY_FLAG = "ShopModelWithFlag";
    private Button copy_link;
    private LinearLayout Reviews_content,sold_content,listing_content;
    private Shop shop=null;
    List<ProductModel> relatedList = new ArrayList<ProductModel>();

    List<ProductModel> sold_items = new ArrayList<ProductModel>();

    private  boolean mail_verified=false;
    private  boolean number_verified=false;

    private FloatingActionButton fab;
    private ShopModel shopModel=null;
    private RecyclerView recyclerView;
    private SearchListAdapter adapter;
    private RatingBar ratingBar1;
    private String user_id;
    public static Intent newInstance(Context context, String user_id){
        Intent mIntent = new Intent(context,ShopActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(PRODUCTDETAIL_SHOP_BUNDLE_KEY_FLAG, user_id);
        mIntent.putExtras(bundle);
        return mIntent;
    }
    private SalesListModel salesListModel=new SalesListModel();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      /*  requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
     */   setContentView(R.layout.activity_shop);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context=this;
        activity=this;
        fragmentManager = getSupportFragmentManager();
        LayoutInflater inflator = LayoutInflater.from(this);
        View v = inflator.inflate(R.layout.action_bar_titlel, null);
        shop_tile=(TextView)v.findViewById(R.id.title);
        toolbar.addView(v);
        close_button=(TextView)v.findViewById(R.id.cancel);
        close_button.setVisibility(View.GONE);
        go_back=(ImageView)findViewById(R.id.go_back);
        go_back.setVisibility(View.VISIBLE);
        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
       /* getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
            actionBar.setTitle("Shop");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.brandColor)));
        }*/
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        ratingBar1=(RatingBar)findViewById(R.id.ratingBar1);
        adapter = new SearchListAdapter(context, relatedList,false);
       /* StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
      */  LinearLayoutManager linearLayoutManagerVertical =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManagerVertical);
      /*  recyclerView.addItemDecoration(new SpacesItemDecoration(
                getResources().getDimensionPixelSize(R.dimen.space)));
    */    recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        rounde_image=(RoundedImageView)findViewById(R.id.rounde_image);
        shop_location=(TextView)findViewById(R.id.shop_location);
        shop_member_since=(TextView)findViewById(R.id.shop_member_since);
        shop_response_time=(TextView)findViewById(R.id.shop_reponse_time);
        shop_listings_count=(TextView)findViewById(R.id.shop_listings_count);
        shop_listings=(TextView)findViewById(R.id.shop_listings);
        shop_sold=(TextView)findViewById(R.id.shop_sold);
        listing_content=(LinearLayout) findViewById(R.id.listing_content);
        listing_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setValues(shopModel.getItem_list());
                shop_listings_count.setTextColor(context.getResources().getColor(R.color.tab_selected));
                shop_listings.setTextColor(context.getResources().getColor(R.color.tab_selected));
                sold_count.setTextColor(context.getResources().getColor(R.color.app_fontBlackColor));
                shop_sold.setTextColor(context.getResources().getColor(R.color.app_fontBlackColor));
            }
        });
        sold_content=(LinearLayout) findViewById(R.id.sold_content);
        sold_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setValues(shopModel.getSold_list());
                sold_count.setTextColor(context.getResources().getColor(R.color.tab_selected));
                shop_sold.setTextColor(context.getResources().getColor(R.color.tab_selected));
                shop_listings_count.setTextColor(context.getResources().getColor(R.color.app_fontBlackColor));
                shop_listings.setTextColor(context.getResources().getColor(R.color.app_fontBlackColor));
            }
        });
        sold_count=(TextView)findViewById(R.id.shop_sold_count);
        profile_name=(TextView)findViewById(R.id.profile_name);
        rating1=(ImageView)findViewById(R.id.rating_image1);
        rating2=(ImageView)findViewById(R.id.rating_image2);
        rating3=(ImageView)findViewById(R.id.rating_image3);
        rating4=(ImageView)findViewById(R.id.rating_image4);
        rating5=(ImageView)findViewById(R.id.rating_image5);
        shop_link=(TextView)findViewById(R.id.shop_link);
        copy_link=(Button)findViewById(R.id.copy_link);
        email_verified=(ImageView)findViewById(R.id.email_verified);
        online_status=(ImageView)findViewById(R.id.online_status);
        facebook_verified=(ImageView)findViewById(R.id.facebook_verified);
        phone_number_verified=(ImageView)findViewById(R.id.phone_number_verified);
        Reviews_content=(LinearLayout)findViewById(R.id.Reviews_content);
        copy_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shop_link_text=shop_link.getText().toString();
                if(!TextUtils.isEmpty(shop_link_text))
                    copyToClipboard(shop_link_text);
            }
        });

        if (getIntent().hasExtra(PRODUCTDETAIL_SHOP_BUNDLE_KEY_FLAG)){
            user_id=getIntent().getExtras().getString(PRODUCTDETAIL_SHOP_BUNDLE_KEY_FLAG);
        }
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= ProfileEditActivity.newInstance(context,profileDetailsModel);
                startActivity(intent);
            }
        });

        email_verified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mail_verified)
                    verifyEmail();
            }
        });
        phone_number_verified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!number_verified)
                    verifyPhoneNumber();
            }
        });
        Reviews_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Toast.makeText(context,"under Development",Toast.LENGTH_LONG).show();*/
                Intent intent= ReviewsRatings.newInstance(activity,null,shopModel,null,null);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.product_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.share) {
            shareProduct();
            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }
    }
    public void setThisPage(){
        String name="";
        if(!TextUtils.isEmpty(profileDetailsModel.getName()))
            name=GlobalFunctions.getSentenceFormat(profileDetailsModel.getName());
        profile_name.setText(name);
        shop_location.setText(profileDetailsModel.getAddress());
        shop_member_since.setText("Member Since: "+profileDetailsModel.getCreated_ago());
        shop_listings_count.setText(profileDetailsModel.getListings());
        sold_count.setText(profileDetailsModel.getSold());
        shop_link.setText(profileDetailsModel.getShop().getShop_link());
        String avatar=profileDetailsModel.getAvatar();
        if(!TextUtils.isEmpty(avatar)){
          /*  imageLoader.DisplayImage(avatar,rounde_image);*/
            Picasso.with(activity)
                    .load(avatar)
                    .placeholder(R.drawable.placeholder_background)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .fit().centerCrop()
                    .into(rounde_image);
        }
        shop=profileDetailsModel.getShop();
        if(TextUtils.isEmpty(shop.getName()))
            shop.setName(name);
     /*   if(!TextUtils.isEmpty(profileDetailsModel.getReviews()))
            setReviewsRatings(profileDetailsModel.getReviews());*/
        String rating=profileDetailsModel.getReviews();
        if(rating!=null&&!TextUtils.isEmpty(rating)&&!rating.equalsIgnoreCase("null"))
            ratingBar1.setRating(Float.parseFloat(rating));
        if(!TextUtils.isEmpty(profileDetailsModel.getFb_id())){facebook_verified.setImageResource(R.drawable.facebook_verified);}else{facebook_verified.setImageResource(R.drawable.facebook_not_verified);}
        if(profileDetailsModel.is_active()){online_status.setVisibility(View.VISIBLE);}
        if(profileDetailsModel.is_email_verify()){mail_verified=true;email_verified.setImageResource(R.drawable.email);}else{email_verified.setImageResource(R.drawable.phone_gray);}
        if(profileDetailsModel.is_otp_verified()){number_verified=true;phone_number_verified.setImageResource(R.drawable.phone_number_verified);} else{phone_number_verified.setImageResource(R.drawable.email_gray);}

       /* if(!GlobalFunctions.isNetworkAvailable(context)){
            Snackbar.make(shop_link, "Please check your internet connection", Snackbar.LENGTH_LONG)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {

                        }
                    }).show();
        }else
            getMySales(context);*/
    }
    @Override
    protected void onResume() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getShopDetails(context,user_id);

            }
        });
        super.onResume();
    }
    private void shareProduct(){
        final String product_="Family and freinds,please follow me on Blueshak everyone.";
        final String product_name="Shop Name :"+profileDetailsModel.getName();
        final String product_description="Shop Description :"+profileDetailsModel.getShop().getDescription();
        String image_url=null;
        if(profileDetailsModel.getImage()!=null)
            if(!TextUtils.isEmpty(profileDetailsModel.getAvatar()))
                image_url="Product Image Url :"+profileDetailsModel.getAvatar();

        final String product_image_url=image_url;
        final String product_location="Shop Location :"+profileDetailsModel.getAddress();
        final String product_Id_for_share="Shop Id :"+profileDetailsModel.getId();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Family and freinds,follow me to sell my products faster."+"\n"+"Product Details");
        shareIntent.putExtra(Intent.EXTRA_TEXT,product_+"\n"+product_name+"\n"+product_Id_for_share+"\n"+
                product_description+"\n"/*+product_duration+"\n"*/+product_image_url+"\n"+product_location
        );
        startActivity(Intent.createChooser(shareIntent, "Share this Shop using"));

    }
    public void setMap(){
        if(fragmentManager!=null){
            if(fragmentManager!=null&&!this.isFinishing()){
                Fragment fragment = new MapViewFragment().newInstance(GlobalVariables.TYPE_SHOP,null,shop,false);
                fragmentManager.beginTransaction().replace(R.id.map,fragment).commit();
               /* Fragment fragment = new MapFragmentSales().newInstance(GlobalVariables.TYPE_SHOP,null,shop,false);
                fragmentManager.beginTransaction().replace(R.id.map,fragment).commit();*/
            }
        }
    }
    public void setSellerProducts(){
        if(fragmentManager!=null){
            if(fragmentManager!=null&&!this.isFinishing()){
            /*    ItemListFragment fragment = new ItemListFragment().newInstance(null,GlobalVariables.TYPE_MULTIPLE_ITEMS,"120");
                fragmentManager.beginTransaction().replace(R.id.seller_products_container,fragment).commit();
     */       }
        }
    }

    private void getSellerProducts(){
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getSellerProducts(context, profileDetailsModel.getId(),"", new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                Log.d(TAG, "getSellerProducts onSuccess Response"+arg0.toString());
                SimilarProductsModel similarProductsModel = (SimilarProductsModel)arg0;
                setSellerProducts(similarProductsModel.getProductsList());
                Log.d(TAG, similarProductsModel.toString());
            }

            @Override
            public void OnFailureFromServer(String msg) {
                Log.d(TAG, msg);
            }

            @Override
            public void OnError(String msg) {
                Log.d(TAG, msg);
            }
        }, "Similar Products");

    }
   /* private void getMySales(final Context context){
        // GlobalFunctions.showProgress(context, "Loading Garage Sales...");
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getMySalesList(context, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                Log.d(TAG, "onSuccess Response");
//             /   GlobalFunctions.hideProgress();
                salesListModel = (SalesListModel)arg0;
                String str = salesListModel.toString();
                setValues(salesListModel);
                Log.d(TAG, str);
                //addItems();
            }

            @Override
            public void OnFailureFromServer(String msg) {
                //  GlobalFunctions.hideProgress();
                Log.d(TAG, msg);
            }

            @Override
            public void OnError(String msg) {
                GlobalFunctions.hideProgress();
                Log.d(TAG, msg);
            }
        }, "List Sales");

    }*/
    public void setSellerProducts(List<ProductModel> products){
        setValues(products);
       /* if(fragmentManager!=null){
            relatedList.clear();
            relatedList.addAll(products);
            try{
                if(relatedList.size()>0){
                    relatedListArray = new HorizontalListArray("My Products",relatedList);
                    Log.d(TAG,"prodFM : "+fragmentManager);
                    Log.d(TAG, "prodFM bool Detaching: " + this.isFinishing());
                    if(fragmentManager!=null&&!this.isFinishing()){
                        Fragment fragment = new HorizontalListItems().newInstance(relatedListArray);
                        fragmentManager.beginTransaction().replace(R.id.seller_products_container,fragment).commit();
                    }
                }
            }catch (Exception ex){Log.d(TAG, "Exception on setting History Data : "+ex);}
        }*/
    }
    private void setValues(SalesListModel model){
        if(model!=null){
            String str = "";
            SalesListModel salesListModel = model;
            ArrayList<ProductModel> product_list=new ArrayList<>();
            List<SalesModel> productModels = salesListModel.getSalesList();
            if(productModels!=null){
                if(productModels.size()>0){
                      for (SalesModel salesModel:productModels){
                            product_list.addAll(salesModel.getProducts());
                      }
                      setSellerProducts(product_list);
                    /*   if(fragmentManager!=null&&!this.isFinishing()){
                        ItemMyListFragment itemListFragment= ItemMyListFragment.newInstance(model);
                        fragmentManager.beginTransaction().replace(R.id.seller_products_container,itemListFragment, "").commit();

                    }*/
                }
            }

        }

    }
    public void copyToClipboard(String copyText) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager)
                    getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(copyText);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager)
                    getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData
                    .newPlainText("Your shop link", copyText);
            clipboard.setPrimaryClip(clip);
        }
        Toast toast = Toast.makeText(getApplicationContext(),
                "Your Shop Link is copied", Toast.LENGTH_SHORT);
       /* toast.setGravity(Gravity.BOTTOM | Gravity.RIGHT, 50, 50);*/
        toast.show();
    }

    public void verifyEmail(){
        GlobalFunctions.showProgress(context, "Verifying Email...");
        ServicesMethodsManager serverManager = new ServicesMethodsManager();
        serverManager.verifyEmail(context, new ProfileDetailsModel(),new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                GlobalFunctions.hideProgress();
                StatusModel statusModel=(StatusModel)arg0;
                Log.d(TAG, "onSuccess Response"+statusModel.toString());
                validateEmail(statusModel);
            }

            @Override
            public void OnFailureFromServer(String msg) {
                GlobalFunctions.hideProgress();
                Toast.makeText(activity,msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnError(String msg) {
                GlobalFunctions.hideProgress();
                Toast.makeText(activity,msg, Toast.LENGTH_SHORT).show();
            }
        },"Email verification");
    }
    public void verifyPhoneNumber(){
        GlobalFunctions.showProgress(context, "Validating OTP...");
        ServicesMethodsManager serverManager = new ServicesMethodsManager();
        OTPResendModel resendModel = new OTPResendModel(GlobalFunctions.getSharedPreferenceString(getApplication(), GlobalVariables.SHARED_PREFERENCE_PHONE));
        serverManager.resendOTP(context, resendModel, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                GlobalFunctions.hideProgress();
                validateOTPResend((StatusModel) arg0);
            }

            @Override
            public void OnFailureFromServer(String msg) {
                GlobalFunctions.hideProgress();
                Toast.makeText(activity,msg,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnError(String msg) {
                GlobalFunctions.hideProgress();
                Toast.makeText(activity,msg, Toast.LENGTH_SHORT).show();
            }
        },"OTP verification");
    }
    private void validateOTPResend(StatusModel statusModel){
        if(statusModel.isStatus()){
            Toast.makeText(getApplicationContext(),"OTP Send Successfully", Toast.LENGTH_SHORT).show();
        }else{Toast.makeText(getApplicationContext(),"Already Authenticated", Toast.LENGTH_SHORT).show();}
    }

    private void validateEmail(StatusModel statusModel){
        if(statusModel.getMessage().equalsIgnoreCase("Verification link is sent to respective E-mail")){
            /*email_verified.setImageResource(R.drawable.email);*/
            mail_verified=true;
        }else{
            Toast.makeText(getApplicationContext(),"Error on verifying Email", Toast.LENGTH_SHORT).show();}
    }

    public void setValues(ShopModel shopModel){
        profileDetailsModel=shopModel.getProfileDetailsModel();
        shop=profileDetailsModel.getShop();

        setSellerProducts(shopModel.getItem_list());
        String name="";
        if(!TextUtils.isEmpty(profileDetailsModel.getName())){
            name=GlobalFunctions.getSentenceFormat(profileDetailsModel.getName());

        }
        shop_tile.setText(name+"'s Shop");
        profile_name.setText(name);
        shop_location.setText(profileDetailsModel.getAddress());
        shop_member_since.setText("Member Since: "+profileDetailsModel.getCreated_ago());
        shop_listings_count.setText(shopModel.getItemsCount()+"");
        if(shopModel.getSold_count()>0)
            sold_count.setText(shopModel.getSold_count()+"");
        else{
            sold_count.setClickable(false);
            sold_content.setClickable(false);
            shop_sold.setClickable(false);
        }
        shop_link.setText(profileDetailsModel.getShop().getShop_link());
        String avatar=profileDetailsModel.getAvatar();
        Log.d(TAG,"avatar in shop####################"+avatar);
        if(!TextUtils.isEmpty(avatar)){
           /* imageLoader.DisplayImage(avatar,rounde_image);*/
            Picasso.with(activity)
                    .load(avatar)
                    .placeholder(R.drawable.placeholder_background)
                    .memoryPolicy(MemoryPolicy.NO_CACHE )
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .fit().centerCrop()
                    .into(rounde_image);
        }

        shop=profileDetailsModel.getShop();
        if(TextUtils.isEmpty(shop.getName()))
            shop.setName(name);
    /*    setReviewsRatings(profileDetailsModel.getReviews());*/
        String rating=profileDetailsModel.getReviews();
        if(rating!=null&&!TextUtils.isEmpty(rating)&&!rating.equalsIgnoreCase("null"))
            ratingBar1.setRating(Float.parseFloat(rating));

        if(!TextUtils.isEmpty(profileDetailsModel.getFb_id())){facebook_verified.setImageResource(R.drawable.facebook_verified);}else{facebook_verified.setImageResource(R.drawable.facebook_not_verified);}
        if(profileDetailsModel.is_active()){online_status.setVisibility(View.VISIBLE);}
        if(profileDetailsModel.is_email_verify()){mail_verified=true;email_verified.setImageResource(R.drawable.email);}else{email_verified.setImageResource(R.drawable.phone_gray);}
        if(profileDetailsModel.is_otp_verified()){number_verified=true;phone_number_verified.setImageResource(R.drawable.phone_number_verified);} else{phone_number_verified.setImageResource(R.drawable.email_gray);}

        facebook_verified.setEnabled(false);
        email_verified.setEnabled(false);
        phone_number_verified.setEnabled(false);
        String id=profileDetailsModel.getId();
        String signed_user_user_id=GlobalFunctions.getSharedPreferenceString(this, GlobalVariables.SHARED_PREFERENCE_USERID);
        if(signed_user_user_id!=null && id!=null){
            if(Integer.parseInt(id) == Integer.parseInt(signed_user_user_id)) {
                fab.setVisibility(View.VISIBLE);
                shop_tile.setText("My Shop");
            }
        }

        setMap();

        globalFunctions.hideProgress();
    }
    private static class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private final int space;
        public SpacesItemDecoration(int space) {
            this.space = space;
        }
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.bottom = 2 * space;
            int pos = parent.getChildAdapterPosition(view);
            outRect.left = space;
            outRect.right = space;
            if (pos < 2)
                outRect.top = 2 * space;
        }
    }
    private void setValues(List<ProductModel> products){
        relatedList.clear();
        relatedList.addAll(products);
        if(relatedList!=null){
            if(relatedList.size()>0&&recyclerView!=null&&relatedList!=null&&adapter!=null){
                refreshList();
            }
        }
    }
    public void refreshList(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                synchronized (adapter){adapter.notifyDataSetChanged();}
            }
        });
    }
    public void getShopDetails(Context context,String selle_user_id){
        GlobalFunctions.showProgress(context,"Loading...");
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getShopDetails(context,selle_user_id,new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                Log.d(TAG, "onSuccess Response"+arg0.toString());
                shopModel = (ShopModel) arg0;
                Log.d(TAG,"####ShopModel#####"+shopModel.toString());
                Log.d(TAG,"####Profile model#####"+shopModel.getProfileDetailsModel().toString());
                Log.d(TAG,"####Item List#####"+shopModel.getItem_list());
                setValues(shopModel);


            }

            @Override
            public void OnFailureFromServer(String msg) {
                globalFunctions.hideProgress();
                Log.d(TAG, msg);
            }

            @Override
            public void OnError(String msg) {
                globalFunctions.hideProgress();
                Log.d(TAG, msg);
            }
        }, "Shop Model");

    }
}
