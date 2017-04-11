package com.langoor.app.blueshak.item;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Menu;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.DatePicker;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.langoor.app.blueshak.services.model.ImageModel;
import com.langoor.blueshak.R;
import com.langoor.app.blueshak.MainActivity;
import com.langoor.app.blueshak.Messaging.activity.ChatActivity;
import com.langoor.app.blueshak.Messaging.activity.MessageActivity;
import com.langoor.app.blueshak.Messaging.data.User;
import com.langoor.app.blueshak.bookmarks.BoomarkItemListAdapter;
import com.langoor.app.blueshak.garage.CreateSaleActivity;
import com.langoor.app.blueshak.global.GlobalFunctions;
import com.langoor.app.blueshak.global.GlobalVariables;
import com.langoor.app.blueshak.helper.RoundedImageView;
import com.langoor.app.blueshak.login.LoginActivity;
import com.langoor.app.blueshak.map.MapViewFragment;
import com.langoor.app.blueshak.report.ReportListing;
import com.langoor.app.blueshak.reviews_rating.ReviewsRatings;
import com.langoor.app.blueshak.root.RootActivity;
import com.langoor.app.blueshak.search.SearchListAdapter;
import com.langoor.app.blueshak.services.ServerResponseInterface;
import com.langoor.app.blueshak.services.ServicesMethodsManager;
import com.langoor.app.blueshak.services.model.ConversationIdModel;
import com.langoor.app.blueshak.services.model.CreateProductModel;
import com.langoor.app.blueshak.services.model.CreateSalesModel;
import com.langoor.app.blueshak.services.model.HomeListModel;
import com.langoor.app.blueshak.services.model.LocationModel;
import com.langoor.app.blueshak.services.model.BookmarkListModel;
import com.langoor.app.blueshak.services.model.CategoryListModel;
import com.langoor.app.blueshak.services.model.ErrorModel;
import com.langoor.app.blueshak.services.model.MarkSoldModel;
import com.langoor.app.blueshak.services.model.ProductModel;
import com.langoor.app.blueshak.services.model.ReviewsRatingsListModel;
import com.langoor.app.blueshak.services.model.SalesListModel;
import com.langoor.app.blueshak.services.model.SalesModel;
import com.langoor.app.blueshak.services.model.ShopModel;
import com.langoor.app.blueshak.services.model.SimilarProductsModel;
import com.langoor.app.blueshak.services.model.StatusModel;
import com.langoor.app.blueshak.shop.ShopActivity;
import com.langoor.app.blueshak.util.LocationListener;
import com.langoor.app.blueshak.util.LocationService;
import com.langoor.app.blueshak.view.AlertDialog;
import com.langoor.app.blueshak.view.CustomSliderTextView;
import com.langoor.app.blueshak.view_sales.MapActivity;
import com.langoor.app.blueshak.view_sales.MapFragmentSales;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;
import com.vlonjatg.progressactivity.ProgressActivity;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProductDetail extends RootActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener , LocationListener {
    private static final String TAG = "ProductDetail";
    private static final String  PRODUCTDETAIL_BUNDLE_KEY_POSITION = "ProductModelWithDetails";
    private static final String  PRODUCTDETAIL_BUNDLE_KEY_FLAG = "ProductModelWithFlag";
    private static final String  SELL_MODEL = "sellmodel";
    private Toolbar toolbar;
    private ActionBar actionBar;
    private ProgressActivity progressActivity;
    public static ProductModel productModel = new ProductModel();
    private CategoryListModel categoriesModels = null;
    private TextView tab1,tab2,title,similarListing_label,sale_name_tv,sale_items_tv, sale_date_tv,sale_distance_tv,sale_time,close_button, read_reviews,pd_nagotiable,view_offers,image_counts,brand_new,location,pick_up_name,shippable_sale_name,garage_icon_name,item_seller_name,product_name,garage_sale_name,productName_tv, rating_count,product_retail_price, availability_tv, shipable_tv, negotabile_tv, inappropirate_tv, date_tv, category_tv, productDescriptionDetail_tv;
    private ImageView bookmark,go_to_review_ratings;
    private Button report_listing;
    private Button messageButton,make_offer,edit;
    private LinearLayout item_for_sale_listing_tab,listing_tab,item_content,garage_content,pick_up_content,garage_icon_content, shippable_content,profile_content,tap_offer_extra_layout, inappropirate_layout,bottom_bar,category_content;
    private Handler handler=new Handler();
    private ImageView go_back;
    private static boolean active=false;
    private  boolean  self_user=false,is_sale=false;
    private SliderLayout mProductSlider;
    private RoundedImageView seller_image;
    private Context context;
    public static Activity activity;
    public Window window;
    private FragmentManager fragmentManager;
    private String seller_phone_number=null;
    private String seller_name=null;
    private String seller_profile_image=null;
    private String product_image=null;
    private String seller_user_id=null,item_name=null,item_price=null;
    private LocationService locServices;
    private TextView price_tv,view_shop,no_items;
    private List<ProductModel> relatedList = new ArrayList<ProductModel>();
    private  static   SalesModel salesModel=new SalesModel();
    private GlobalFunctions globalFunctions = new GlobalFunctions();
    private User user=new User();
    private FrameLayout frameLayout;
    private   ConversationIdModel conversationIdModel=new ConversationIdModel();
    private double latitude,longitude;
    private RecyclerView recyclerView;
    private RatingBar ratingBar1;
    private SearchListAdapter adapter;
    private List<ProductModel> similar_listing = new ArrayList<ProductModel>();
    private List<ProductModel> seller_other_listing = new ArrayList<ProductModel>();
    private List<ProductModel> sales_listing = new ArrayList<ProductModel>();
    ImageLoader imageLoader;
    DisplayImageOptions options;
    public static boolean is_map=false;
    boolean is_tab1_selected=true;
    private DatePickerDialog toDatePickerDialog;
    private ProgressBar progress_bar;
    private String email_verification_error;
    public static Intent newInstance(Context context, ProductModel product, SalesModel salesModel, int flag){
        Intent mIntent = new Intent(context, ProductDetail.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(PRODUCTDETAIL_BUNDLE_KEY_POSITION, product);
        bundle.putSerializable(SELL_MODEL, salesModel);
        bundle.putInt(PRODUCTDETAIL_BUNDLE_KEY_FLAG, flag);
        mIntent.putExtras(bundle);
        return mIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);
        try{
            context = this;
            activity = this;
            window = getWindow();
            actionBar = getSupportActionBar();
            fragmentManager = getSupportFragmentManager();
            email_verification_error=context.getResources().getString(R.string.ErrorEmailVerification);
            toolbar = (Toolbar) findViewById(R.id.product_detail_tool_bar);
            progress_bar=(ProgressBar)findViewById(R.id.progress_bar);
            setSupportActionBar(toolbar);
            LayoutInflater inflator = LayoutInflater.from(this);
            View v = inflator.inflate(R.layout.action_bar_titlel, null);
            title=(TextView)v.findViewById(R.id.title);
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
            categoriesModels = GlobalFunctions.getCategories(activity);
            locServices = new LocationService(activity);
            locServices.setListener(this);
            if(!locServices.canGetLocation()){/*locServices.showSettingsAlert();*/}else {
                latitude=locServices.getLatitude();longitude=locServices.getLongitude();
            }
            if (getIntent().hasExtra(PRODUCTDETAIL_BUNDLE_KEY_POSITION))
                productModel = (ProductModel) getIntent().getExtras().getSerializable(PRODUCTDETAIL_BUNDLE_KEY_POSITION);
            if (getIntent().hasExtra(SELL_MODEL))
                salesModel = (SalesModel) getIntent().getExtras().getSerializable(SELL_MODEL);

            frameLayout=(FrameLayout)findViewById(R.id.map);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                //enable translucent statusbar via flags
                globalFunctions.setTranslucentStatusFlag(window, true);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //we don't need the translucent flag this is handled by the theme
                globalFunctions.setTranslucentStatusFlag(window, true);
                //set the statusbarcolor transparent to remove the black shadow
                this.getWindow().setStatusBarColor(Color.TRANSPARENT);
            }
            imageLoader = ImageLoader.getInstance();
            options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisc(true).resetViewBeforeLoading(true)
                    .showImageForEmptyUri(R.drawable.placeholder_background)
                    .showImageOnFail(R.drawable.placeholder_background)
                    .showImageOnLoading(R.drawable.placeholder_background).build();

            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            recyclerView.setNestedScrollingEnabled(false);
            ratingBar1=(RatingBar)findViewById(R.id.ratingBar1);

            adapter = new SearchListAdapter(context, relatedList,false);
            LinearLayoutManager linearLayoutManagerVertical =
                    new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManagerVertical);
            recyclerView.addItemDecoration(new SimpleDividerItemDecoration(activity));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
            similarListing_label=(TextView)findViewById(R.id.SimilarListing);
            tab1=(TextView)findViewById(R.id.tab1);
            tab2=(TextView)findViewById(R.id.tab2);
            tab1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!is_tab1_selected){
                        tab1.setTextColor(context.getResources().getColor(R.color.white));
                        tab1.setBackgroundColor(context.getResources().getColor(R.color.brandColor));
                        tab2.setTextColor(context.getResources().getColor(R.color.brandColor));
                        tab2.setBackgroundColor(context.getResources().getColor(R.color.brand_background_color));
                        if(is_sale){
                            setValues(sales_listing);
                        }else {
                            if(similar_listing.size()>0)
                                setValues(similar_listing);
                            else
                                getSimilarProducts();
                        }
                        is_tab1_selected=true;
                    }
                }
            });
            tab2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(is_tab1_selected){
                        tab2.setTextColor(context.getResources().getColor(R.color.white));
                        tab2.setBackgroundColor(context.getResources().getColor(R.color.brandColor));
                        tab1.setTextColor(context.getResources().getColor(R.color.brandColor));
                        tab1.setBackgroundColor(context.getResources().getColor(R.color.brand_background_color));
                        setValues(seller_other_listing);
                       /* if(seller_other_listing.size()>0)
                            setValues(seller_other_listing);
                        else
                            getSellerProducts();*/
                        is_tab1_selected=false;
                    }

                }
            });
            no_items = (TextView) findViewById(R.id.no_items);
            sale_name_tv = (TextView) findViewById(R.id.sale_item_row_list_name_tv);
            sale_items_tv = (TextView) findViewById(R.id.sale_item_row_list_items_no_tv);
            listing_tab=(LinearLayout)findViewById(R.id.listing_tab);
            item_for_sale_listing_tab=(LinearLayout)findViewById(R.id.item_for_sale_listing_tab);
            sale_date_tv = (TextView) findViewById(R.id.sales_item_row_list_date_tv);
            sale_distance_tv = (TextView)findViewById(R.id.sales_item_row_list_distance_tv);
            sale_time=(TextView)findViewById(R.id.sales_item_row_list_time);
            progressActivity = (ProgressActivity) findViewById(R.id.products_details_progressActivity);
            mProductSlider = (SliderLayout) findViewById(R.id.product_detail_slider);
            productName_tv = (TextView) findViewById(R.id.product_detail_name_tv);
            pd_nagotiable = (TextView) findViewById(R.id.pd_nagotiable);
            price_tv = (TextView) findViewById(R.id.product_detail_price_tv);
            date_tv = (TextView) findViewById(R.id.product_detail_date_tv);
            read_reviews= (TextView) findViewById(R.id.read_reviews);
            go_to_review_ratings=(ImageView)findViewById(R.id.go_to_review_ratings);
            go_to_review_ratings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= ReviewsRatings.newInstance(activity,productModel,null,null,salesModel);
                    startActivity(intent);
                }
            });
            read_reviews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= ReviewsRatings.newInstance(activity,productModel,null,null,salesModel);
                    startActivity(intent);
                }
            });
            category_tv = (TextView) findViewById(R.id.product_detail_category_tv);
            productDescriptionDetail_tv = (TextView) findViewById(R.id.product_detail_description_tv);
            shippable_content= (LinearLayout) findViewById(R.id.shippable_content);
            garage_icon_content= (LinearLayout) findViewById(R.id.garage_icon_content);
            pick_up_content= (LinearLayout) findViewById(R.id.pick_up_content);
            bottom_bar=(LinearLayout)findViewById(R.id.bottom_bar);
            bookmark=(ImageView)findViewById(R.id.bookmark_this);
            product_name = (TextView) findViewById(R.id.product_name);
            pick_up_name = (TextView) findViewById(R.id.pick_up_name);
            shippable_sale_name = (TextView) findViewById(R.id.shippable_sale_name);
            garage_icon_name = (TextView) findViewById(R.id.garage_icon_name);
            report_listing= (Button) findViewById(R.id.report_listing);
            /*callButton = (Button) findViewById(R.id.product_detail_call_button);*/
            messageButton = (Button) findViewById(R.id.product_detail_message_button);
            item_seller_name=(TextView)findViewById(R.id.item_seller_name);
            item_seller_name=(TextView)findViewById(R.id.item_seller_name);
            make_offer = (Button) findViewById(R.id.make_offer);
            edit = (Button) findViewById(R.id.edit);
            item_content=(LinearLayout)findViewById(R.id.item_content);
            garage_content=(LinearLayout)findViewById(R.id.garage_content);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!is_sale){
                        show_edit_item_pop_up(v);
                    }else{
                        show_edit_sale_pop_up(v);
                    }
                }
            });
            location=(TextView)findViewById(R.id.location);
            brand_new=(TextView)findViewById(R.id.brand_new);
            image_counts=(TextView)findViewById(R.id.image_counts);
            seller_image=(RoundedImageView)findViewById(R.id.seller_image);
            category_content=(LinearLayout)findViewById(R.id.category_content);
            profile_content=(LinearLayout)findViewById(R.id.profile_content);
            profile_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= ReviewsRatings.newInstance(activity,productModel,null,null,salesModel);
                    startActivity(intent);
                }
            });
            location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i= MapActivity.newInstance(context,GlobalVariables.TYPE_MULTIPLE_ITEMS,productModel);
                    startActivity(i);
                }
            });
            frameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i= MapActivity.newInstance(context,GlobalVariables.TYPE_MULTIPLE_ITEMS,productModel);
                    startActivity(i);
                }
            });
            report_listing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(is_loggedIn()){
                        Intent report_listing;
                        if(is_sale){
                            report_listing= ReportListing.newInstance(context,salesModel.getId(),false);
                            startActivity(report_listing);
                        }else{
                            report_listing= ReportListing.newInstance(context,productModel.getId(),true);
                            startActivity(report_listing);
                        }

                    }else{
                        showSettingsAlert();
                    }

                }
            });
            read_reviews.setPaintFlags(read_reviews.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(productModel!=null){
                            getItemInfo(productModel,context);
                  /*  setThisPage();*/
                        }else if (salesModel!=null){
                            getSaleInfo(salesModel,context);

                        }else{
                            showErrorPage();
                        }
                    }
                });
            messageButton.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                  if(is_loggedIn()){
                        if(!self_user){
                            if(seller_name!=null || seller_user_id!=null){
                                check_conversation_exists();
                            }else{
                                Toast.makeText(activity,"Sorry can't send message",Toast.LENGTH_LONG).show();
                            }
                         }else {
                            if(!is_sale){
                                show_edit_item_pop_up(v);
                            }else{
                                show_edit_sale_pop_up(v);
                            }
                        }
                    }else
                        showSettingsAlert();



                }
            });
            make_offer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(is_loggedIn()){
                        if (!self_user) {
                            Intent intent = MakeOffersActivty.newInstance(context, productModel);
                            startActivity(intent);
                        } else {
                            if (!is_sale) {
                                if (productModel != null)
                                    updateItemStatus(context,productModel.getId());
                            } else {
                                if (salesModel != null)
                                    show_alert(salesModel.getId());
                                    /*updateSaleStatus(context,false,salesModel.getId());*/
                            }
                        }
                    }else
                        showSettingsAlert();
                }
            });
            bookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(is_loggedIn()){
                        if(productModel!=null){
                            if(productModel.is_bookmark())
                                deleteBookmark(context,productModel.getId());
                            else
                                addBookmark(context, productModel.getId());
                        }else
                            Toast.makeText(context,"This product can't be bookmarked",Toast.LENGTH_LONG).show();
                    }else{
                        showSettingsAlert();
                    }
                }
            });
        } catch (NullPointerException e){
            Log.d(TAG,"NullPointerException");
            e.printStackTrace();
        }catch (NumberFormatException e) {
            Log.d(TAG,"NumberFormatException");
            e.printStackTrace();
        }catch (Exception e){
            Log.d(TAG,"Exception");
            e.printStackTrace();
        }

    }
    public void call(String Phone) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:"+Phone));
        startActivity(callIntent);
    }

    public boolean is_loggedIn(){
        boolean is_logged_in=false;
        String token = GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
        if(token!=null){
            return true;
        }else{
            return false;
        }

    }
    public void showSettingsAlert(){
        Intent creategarrage = new Intent(context,LoginActivity.class);
        context.startActivity(creategarrage);
    }
    private void getSimilarProducts(){
      showProgressBar();
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        if(categoriesModels==null)categoriesModels = GlobalFunctions.getCategories(activity);
        String categoryIdsString = "";
        if(categoriesModels!=null){
            categoryIdsString = categoriesModels.getIDStringfromNameString(productModel.getProductCategory());
        }
        Log.d(TAG,"#############categoryIdsString################"+productModel.getProductCategory());
        servicesMethodsManager.getSimilarProducts(context, productModel.getId(),categoryIdsString,Double.toString(latitude),Double.toString(longitude), new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                hideProgressBar();
                Log.d(TAG, "getSimilarProducts onSuccess Response");
                SimilarProductsModel similarProductsModel = (SimilarProductsModel)arg0;
            /*    setSimilarProducts(similarProductsModel.getProductsList(),"Similar products");*/
                setValues(similarProductsModel.getProductsList());
                similar_listing=similarProductsModel.getProductsList();
                Log.d(TAG, similarProductsModel.toString());
            }

            @Override
            public void OnFailureFromServer(String msg) {
                hideProgressBar();
                no_items.setVisibility(View.VISIBLE);
                relatedList.clear();
                refreshList();
                Log.d(TAG, msg);
            }

            @Override
            public void OnError(String msg) {
                hideProgressBar();
                relatedList.clear();
                refreshList();
                no_items.setVisibility(View.VISIBLE);
                Log.d(TAG, msg);
            }
        }, "Similar Products");

    }

    public void setMap(){
        if(fragmentManager!=null){
              if(fragmentManager!=null&&!this.isFinishing()){
                  Fragment fragment = new MapViewFragment().newInstance(GlobalVariables.TYPE_SHOP,productModel,null,false);
                /*  fragmentManager.beginTransaction().replace(R.id.map,fragment).commit();*/
                /*fixing the getting exception “IllegalStateException: Can not perform this action after onSaveInstanceState”*/
                  fragmentManager.beginTransaction().replace(R.id.map,fragment).commitAllowingStateLoss();
              }
        }
    }

    public void restoreToolbar(){
        Log.d(TAG, "Restore Tool Bar");
        if(actionBar!=null){
            Log.d(TAG, "Restore Action Bar not Null");
            actionBar.setTitle("");
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        }

    }

    public static boolean isActive(){
        return active;
    }

    private void toggleInappropirateLayout(){
        if(inappropirate_layout!=null){
            if(inappropirate_layout.getVisibility()==View.VISIBLE){
                inappropirate_layout.setVisibility(View.GONE);
            }else{
                inappropirate_layout.setVisibility(View.VISIBLE);
            }
        }
    }


    /*public void setProductModel(ProductModel product){
        productModel = product;
        setThisPage();
    }*/
    private void getItemInfo(ProductModel productModel,Context context){
       showProgressBar();
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getItemInfo(context, productModel.getId(), new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
              hideProgressBar();
                Log.d(TAG, "GetItemInfo onSuccess Response");
                ProductModel productModel = (ProductModel) arg0;
                setThisPage(productModel);
                Log.d(TAG, productModel.toString());
            }

            @Override
            public void OnFailureFromServer(String msg) {
                Log.d(TAG, msg);
              hideProgressBar();
            }

            @Override
            public void OnError(String msg) {
                Log.d(TAG, msg);
              hideProgressBar();
            }
        }, "GetItemInfo onSuccess Response");

    }
    private void getSaleInfo(SalesModel salesModel,Context context){
        showProgressBar();
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getSaleInfo(context, salesModel.getId(),Double.toString(latitude),Double.toString(longitude), new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
              hideProgressBar();
                Log.d(TAG, "GetItemInfo onSuccess Response");
                 SalesModel salesModel = (SalesModel) arg0;
                Log.d(TAG+"#Sale ####", salesModel.toString());
                setThisPage(salesModel);


                Log.d(TAG, salesModel.toString());
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
        }, "GetItemInfo onSuccess Response");

    }
    private void setThisPage(ProductModel product){
        item_content.setVisibility(View.VISIBLE);
        /*report_Listing_content.setVisibility(View.VISIBLE);*/
        productModel=product;
        String title_name="";
        if(productModel.getName()!=null&&!TextUtils.isEmpty(productModel.getName()))
             title_name=GlobalFunctions.getSentenceFormat(productModel.getName());

        title.setText(title_name);
        product_name.setText(title_name);
        item_name=productModel.getName();
        item_price=productModel.getSalePrice();
        seller_name=productModel.getSellerName();
        seller_phone_number=productModel.getSeller_phone();
        seller_profile_image=productModel.getSeller_image();
        seller_user_id=productModel.getSeller_id();
        user.setName(seller_name);
        user.setCurrency(productModel.getCurrency());
        user.setIs_sale(false);
        user.setBs_id(seller_user_id);
        user.setNumber(seller_phone_number);
        user.setProfileImageUrl(seller_profile_image);
        user.setProduct_name(item_name);
        user.setPrice(item_price);
        user.setProduct_id(productModel.getId());
        user.setProduct_url(product_image);
        user.setActive_tab(GlobalVariables.TYPE_BUYER_TAB);
        String signed_user_user_id=GlobalFunctions.getSharedPreferenceString(this, GlobalVariables.SHARED_PREFERENCE_USERID);
        if(signed_user_user_id!=null && seller_user_id!=null){
            if(Integer.parseInt(seller_user_id) == Integer.parseInt(signed_user_user_id)) {
                self_user=true;
                make_offer.setVisibility(View.GONE);
                make_offer.setVisibility(View.GONE);
                messageButton.setVisibility(View.GONE);
                edit.setVisibility(View.VISIBLE);
            }
        }
        if(!TextUtils.isEmpty(productModel.getName()))
            productName_tv.setText(GlobalFunctions.getSentenceFormat(productModel.getName()));

        if(!TextUtils.isEmpty(productModel.getSellerName()))
            item_seller_name.setText(GlobalFunctions.getSentenceFormat(productModel.getSellerName()));


        read_reviews.setText("Read reviews"+"("+productModel.getReviews_count()+")");
        /*offers.setText("Offers"+"("+productModel.getOffers()+")");*/
        /*productName_tv.setText(productModel.getName());*/
        String avatar=productModel.getSeller_image();
        //download and display image from url
        /*imageLoader.displayImage(avatar,seller_image, options);*/
        if(!TextUtils.isEmpty(avatar)){
            Picasso.with(context)
                    .load(avatar)
                    .placeholder(R.drawable.squareplaceholder)
                    .fit().centerCrop()
                    .into(seller_image);
        }else{
            seller_image.setImageResource(R.drawable.squareplaceholder);
        }
        if(productModel.isAvailable())
            price_tv.setText(GlobalFunctions.getFormatedAmount(productModel.getCurrency(),productModel.getSalePrice()));
        else{
            price_tv.setText("Sold");
            if(!self_user){
                /*messageButton.setEnabled(false);
                messageButton.setAlpha(.5f);*/
                make_offer.setAlpha(.5f);
                make_offer.setClickable(false);
            }
        }
     /*   if(productModel.is_product_new()){
            brand_new.setVisibility(View.VISIBLE);
        }*/
        if(productModel.is_bookmark()){
            bookmark.setImageResource(R.drawable.like_full);
        }else
            bookmark.setImageResource(R.drawable.like_border);
        int grey2=getResources().getColor(R.color.grey_2);
        int black=getResources().getColor(R.color.black);
        if(productModel.is_garage_item())garage_icon_content.setVisibility(View.VISIBLE);
        if(productModel.isShipable())shippable_content.setVisibility(View.VISIBLE);
        if(productModel.isPickup())pick_up_content.setVisibility(View.VISIBLE);
        if(productModel.isNegotiable())pd_nagotiable.setVisibility(View.VISIBLE);
       /* delivery_iv.setImageResource(productModel.isShipable()? R.drawable.shippingon : R.drawable.shippingoff);
        pick_iv.setImageResource(productModel.isPickup()? R.drawable.pickupon : R.drawable.pickupoff);
      */  shippable_sale_name.setTextColor(productModel.isShipable()? black:grey2);
        pick_up_name.setTextColor(productModel.isPickup()?black:grey2);
        garage_icon_name.setTextColor(productModel.is_garage_item()?black:grey2);
        date_tv.setText(/*productModel.getLast_updated()*/productModel.getProductCreatedAt());
        String category=productModel.getProductCategory();
        if(!TextUtils.isEmpty(category) && !productModel.getProductCategory().equalsIgnoreCase("null"))
            category=productModel.getProductCategory();
        else
            category="Electronics and Computers";
        category_tv.setText(category);
        productDescriptionDetail_tv.setText(productModel.getDescription());
        /*If the line are more than show view more or else not needed*/
        if(productDescriptionDetail_tv.getLineCount()>3)
            makeTextViewResizable(productDescriptionDetail_tv, 3, "View More", true);
        setImageOnView(productModel.getImages());
        if(productModel.getCummalative_rating()!=null&&!TextUtils.isEmpty(productModel.getCummalative_rating())){
            ratingBar1.setRating(Float.parseFloat(productModel.getCummalative_rating()));

        }
        /*getAddressFromLatLng(Double.parseDouble(productModel.getLatitude()),Double.parseDouble(productModel.getLongitude()));*/
        location.setText(productModel.getAddress());
        showContent();
        setMap();
        getSellerProducts();
        getSimilarProducts();
    }

    private void shareProduct(){
        final String product_="Family and freinds,please share this product to help me sell faster.";
        final String product_name="Product Name :"+productModel.getName();
        final String product_description="Product Description :"+productModel.getDescription();
        String image_url=null;
        if(productModel.getImage()!=null)
            if(productModel.getImage().size()>0)
                image_url="Product Image Url :"+productModel.getImage().get(0);

        final String product_image_url=image_url;
        final String product_location="Product Location :"+location.getText().toString();
        final String product_Id_for_share="Product Id :"+productModel.getId();
        final String product_duration="Sale Duration :"+productModel.getStartDate()+" to"+productModel.getEndDate();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Family and freinds,please share this "+product_name+
                " to help me sell faster."+"\n"+"Product Details");
        shareIntent.putExtra(Intent.EXTRA_TEXT,product_+"\n"+product_name+"\n"+product_Id_for_share+"\n"+
                product_description+"\n"/*+product_duration+"\n"*/+product_image_url+"\n"+product_location
        );
        startActivity(Intent.createChooser(shareIntent, "Share this product using"));

    }


    private void setImageOnView(ArrayList<String> images){
        final ImageModel imageModel=new ImageModel();
        mProductSlider.removeAllSliders();
        final ArrayList<String> displayImageURL = new ArrayList<String>();
        displayImageURL.clear();
        displayImageURL.addAll(/*productModel.getImage()*/images);
        if(displayImageURL.size()>0){
            product_image=displayImageURL.get(0);
            user.setProduct_url(product_image);
        }
        if(productModel.getImage().size()>1){
            image_counts.setVisibility(View.VISIBLE);
            image_counts.setText("1"+"/"+productModel.getImage().size());
        }

        for(int i=0;i<displayImageURL.size()&&displayImageURL.get(i)!=null;i++){
            final String url=displayImageURL.get(i).toString();
              final  Uri uri=getUriFromUrl(url);
            CustomSliderTextView textSliderView = new CustomSliderTextView(context);
            // initialize a SliderLayout
            textSliderView
                    .description(1+"")
                    .image(displayImageURL.get(i).toString())
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                   /* .setOnSliderClickListener(this);*/
            .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(BaseSliderView slider) {
                    imageModel.setImage(url);
                   /* Intent intent=ImageViewActivty.newInstance(context,productModel);*/
                    Intent intent=ViewActivity.newInstance(context,imageModel);
                    startActivity(intent);
                }
            });
            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", 1 + "");

            mProductSlider.addSlider(textSliderView);
        }

        mProductSlider.setPresetTransformer(SliderLayout.Transformer.ZoomOutSlide);
        mProductSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
      /*  if(mPagerIndicator!=null){mProductSlider.setCustomIndicator(mPagerIndicator);}*/
        mProductSlider.setCustomAnimation(new DescriptionAnimation());
        mProductSlider.setDuration(4000);
        mProductSlider.addOnPageChangeListener(this);
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
            /*shareProduct();*/
            shareApp();
            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }
    }
    private Uri.Builder builder;
    public Uri getUriFromUrl(String thisUrl) {
        try {
            URL url = new URL(thisUrl);
            builder =  new Uri.Builder()
                    .scheme(url.getProtocol())
                    .authority(url.getAuthority())
                    .appendPath(url.getPath());
            return builder.build();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return  null;
        }

    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        if(productModel!=null)
            image_counts.setText(i+1+"/"+productModel.getImage().size());
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onSliderClick(BaseSliderView baseSliderView) {
        mProductSlider.getCurrentPosition();
    }

    private void showLoading(){
        if(progressActivity!=null){
            progressActivity.showLoading();
        }
    }

    private void showContent(){
        if(progressActivity!=null){
            progressActivity.showContent();
        }
    }

    private void showErrorPage(){
        if(progressActivity!=null){
            progressActivity.showError(context.getResources().getDrawable(R.drawable.ic_error), "No Connection",
                    "We could not establish a connection with our servers. Try again when you are connected to the internet.",
                    "Try Again", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ProductDetail productDetail = new ProductDetail();
                            productDetail.setThisPage(productModel);
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


    private void addBookmark(final Context context, String productID){
        /*GlobalFunctions.showProgress(context, "Bookmarking Product...");*/
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.addBookmark(context, productID, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                /*GlobalFunctions.hideProgress();*/
                if(arg0 instanceof StatusModel){
                    StatusModel statusModel = (StatusModel) arg0;
                    if(statusModel.isStatus()){
                        bookmark.setImageResource(R.drawable.like_full);
                        productModel.setIs_bookmark(true);
                        Toast.makeText(context, "Added to bookmarks", Toast.LENGTH_SHORT).show();
                    }
                }else if(arg0 instanceof ErrorModel){
                    ErrorModel errorModel = (ErrorModel) arg0;
                    String msg = errorModel.getError()!=null ? errorModel.getError() : errorModel.getMessage();
                    Log.d(TAG,"OnFailureFromServer"+msg);
                }
            }

            @Override
            public void OnFailureFromServer(String msg) {
                /*GlobalFunctions.hideProgress();*/
                Log.d(TAG,"OnFailureFromServer"+msg);
            }

            @Override
            public void OnError(String msg) {
               /* GlobalFunctions.hideProgress();*/
                Log.d(TAG,"OnFailureFromServer"+msg);
            }
        }, "Add Bookmarks");

    }

    @Override
    protected void onResume() {
        try{
            if(salesModel!=null){
                productModel=new ProductModel();
                productModel.setLongitude(salesModel.getLongitude());
                productModel.setLatitude(salesModel.getLatitude());
                productModel.setName(salesModel.getName());
                productModel.setId(salesModel.getId());
                productModel.setImage(salesModel.getImages());
            }
            if(is_map)
                setMap();

            super.onResume();

        }catch (IllegalStateException e){
            e.printStackTrace();
            Crashlytics.log(e.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            Crashlytics.log(e.getMessage());
        }


    }

    @Override
    protected void onStart() {
        active = true;
        super.onStart();
    }

    @Override
    protected void onStop() {
        active = false;
        mProductSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        finish();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

    public static void closeThisActivity(){
        Log.d(TAG, "Close this Activity" + activity);
        if(activity!=null){
            activity.finish();
        }
    }
    @Override
    public void onProviderEnabled(String provider) {

    }
    @Override
    public void onLocationChanged(Location location) {

    }
    private void getAddressFromLatLng(Double lat,Double lng){
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getAddress(activity, lat,lng, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
              hideProgressBar();
                LocationModel locationModel =(LocationModel)arg0;
               if(locationModel !=null)
                   location.setText(locationModel.getFormatted_address());
            }

            @Override
            public void OnFailureFromServer(String msg) {
            }

            @Override
            public void OnError(String msg) {
            }
        }, "Delete Sale");


    }
    private void setThisPage(SalesModel salesModel){
        garage_content.setVisibility(View.VISIBLE);
        item_content.setVisibility(View.GONE);
       /* report_Listing_content.setVisibility(View.GONE);*/
        listing_tab.setVisibility(View.GONE);
        item_for_sale_listing_tab.setVisibility(View.VISIBLE);
        is_sale=true;
        this.salesModel=salesModel;
        String title_name="";
        if(salesModel.getName()!=null&&!TextUtils.isEmpty(salesModel.getName()))
            title_name=GlobalFunctions.getSentenceFormat(salesModel.getName());

        title.setText(title_name);
        sale_name_tv.setText(title_name);
        String title="";
        if(!TextUtils.isEmpty(salesModel.getSale_items_count()))
            if(Integer.parseInt(salesModel.getSale_items_count())==1)
                title=salesModel.getSale_items_count()+" Item";
            else
                title=salesModel.getSale_items_count()+" Items";
        else
            title="No Items Available";

        sale_items_tv.setText(title);
        sale_date_tv.setText(salesModel.getStart_date());
        sale_time.setText(salesModel.getStart_time()+"-"+salesModel.getEnd_time());
       /* holder.distance_tv.setText(obj.getDistanceAway()+" "+getContext().getString(R.string.milesAway));*/
        sale_distance_tv.setText(salesModel.getDistanceAway()/*+" "+context.getString(R.string.milesAway)*/);
        /*@@@@@@@@@@@@@@@*/


       /* product_name.setText(GlobalFunctions.getSentenceFormat(salesModel.getName()));*/
     /*   offers.setVisibility(View.GONE);*/
        if(salesModel.getProducts().size()>0)
            similarListing_label.setText("Items for sale");
        else
            similarListing_label.setText("No items for sale");
        read_reviews.setText("Read reviews"+"("+salesModel.getReviews_count()+")");
        seller_user_id=salesModel.getUserID();
        seller_name=salesModel.getSellerName();
        seller_phone_number=salesModel.getSellerNumber();
        seller_profile_image=salesModel.getSeller_image();
        user.setIs_sale(true);
        user.setName(seller_name);
        user.setBs_id(seller_user_id);
        user.setNumber(seller_phone_number);
        user.setProfileImageUrl(seller_profile_image);
        user.setProduct_name(salesModel.getName());
        user.setPrice(item_price);
        user.setSale_id(salesModel.getId());
        user.setProduct_url(product_image);
        user.setActive_tab(GlobalVariables.TYPE_BUYER_TAB);
        if(salesModel.is_own_sale()) {
                self_user=true;
                bookmark.setVisibility(View.GONE);
                make_offer.setVisibility(View.GONE);
                messageButton.setVisibility(View.GONE);
                edit.setVisibility(View.VISIBLE);
        }

     /*   if(flag==GlobalVariables.TYPE_MY_BOOKMARKS)
            bookmark.setVisibility(View.GONE);*/
        if(!TextUtils.isEmpty(salesModel.getName()))
            productName_tv.setText(GlobalFunctions.getSentenceFormat(salesModel.getName()));
        if(!TextUtils.isEmpty(salesModel.getSellerName()))
            item_seller_name.setText(GlobalFunctions.getSentenceFormat(salesModel.getSellerName()));
        price_tv.setVisibility(View.GONE);
        category_content.setVisibility(View.GONE);

        String avatar=salesModel.getSeller_image();
        //download and display image from url
      /*  imageLoader.displayImage(avatar,seller_image, options);*/
        if(!TextUtils.isEmpty(avatar)){
            Picasso.with(context)
                    .load(avatar)
                    .placeholder(R.drawable.squareplaceholder)
                    .fit().centerCrop()
                    .into(seller_image);
        }else{
            seller_image.setImageResource(R.drawable.squareplaceholder);
        }
        date_tv.setText(salesModel.getStart_date());
        setImageOnView(salesModel.getImages());
        bookmark.setVisibility(View.GONE);
        if(!salesModel.is_own_sale())
            make_offer.setVisibility(View.GONE);
        productDescriptionDetail_tv.setText(salesModel.getDescription());
        /*If the line are more than show view more or else not needed*/
        if(productDescriptionDetail_tv.getLineCount()>3)
            makeTextViewResizable(productDescriptionDetail_tv, 3, "View More", true);

       /* makeTextViewResizable(productDescriptionDetail_tv, 3, "More", true);*/
        sales_listing=salesModel.getProducts();
        setValues(sales_listing);
        String rating=salesModel.getAvg_seller_rating();
        if(salesModel.getAvg_seller_rating()!=null&&!TextUtils.isEmpty(salesModel.getAvg_seller_rating())&&!rating.equalsIgnoreCase("null")){
            ratingBar1.setRating(Float.parseFloat(salesModel.getAvg_seller_rating()));

        }
        location.setText(salesModel.getSale_address());
      /*  getAddressFromLatLng(Double.parseDouble(salesModel.getLatitude()),Double.parseDouble(salesModel.getLongitude()));*/
        showContent();
        setMap();
    }
    public void updateItemStatus(final Context context,String product_id){
        MarkSoldModel markSoldModel=new MarkSoldModel();
        markSoldModel.setProduct_id(product_id);
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.updateItemStatus(context,markSoldModel,new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                Log.d(TAG, "onSuccess Response");
                if(arg0 instanceof StatusModel){
                    StatusModel statusModel = (StatusModel) arg0;
                    if(statusModel.isStatus()){
                        Toast.makeText(context, "Item has been marked as sold", Toast.LENGTH_LONG).show();
                        closeThisActivity();
                    }
                }else if(arg0 instanceof ErrorModel){
                    ErrorModel errorModel = (ErrorModel) arg0;
                    String msg = errorModel.getError()!=null ? errorModel.getError() : errorModel.getMessage();
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void OnFailureFromServer(String msg) {
                Log.d(TAG, msg);
            }

            @Override
            public void OnError(String msg) {
                Log.d(TAG, msg);
            }
        }, "Review Ratings");
    }
    public void updateSaleStatus(final Context context,boolean is_Available,String sale_id){
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.updateSaleStatus(context,sale_id,is_Available,new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                Log.d(TAG, "onSuccess Response");
                if(arg0 instanceof StatusModel){
                    StatusModel statusModel = (StatusModel) arg0;
                    if(statusModel.isStatus()){
                        Toast.makeText(context, "Sale has been marked as sold", Toast.LENGTH_LONG).show();
                        closeThisActivity();
                    }
                }else if(arg0 instanceof ErrorModel){
                    ErrorModel errorModel = (ErrorModel) arg0;
                    String msg = errorModel.getError()!=null ? errorModel.getError() : errorModel.getMessage();
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void OnFailureFromServer(String msg) {
                Log.d(TAG, msg);
            }

            @Override
            public void OnError(String msg) {
                Log.d(TAG, msg);
            }
        }, "Review Ratings");
    }
    private void deleteSale(final Context context, String saleID){
        showProgressBar();
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.deleteSale(context, saleID, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
              hideProgressBar();
                Log.d(TAG, "onSuccess Response");
                Toast.makeText(context,"Sale has been deleted Successfully",Toast.LENGTH_LONG).show();
                closeThisActivity();       }

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
        }, "Delete Sale");
    }
    private void deleteItem(final Context context, String saleID){
        showProgressBar();
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.deleteItem(context, saleID, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
              hideProgressBar();
                Log.d(TAG, "onSuccess Response");
                Toast.makeText(context,"Item has been deleted Successfully",Toast.LENGTH_LONG).show();
                closeThisActivity();       }

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
        }, "Delete Sale");
    }
    public  void show_alert(final String sale_id){
        final AlertDialog dialog = new AlertDialog(activity);
        dialog.setTitle("Delete Sale");
        dialog.setIcon(R.drawable.ic_warning_black_24dp);
        dialog.setIsCancelable(true);
        dialog.setMessage("Are you sure Delete this Sale?");
        dialog.setPositiveButton("Yes", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSale(activity,sale_id);
                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("Cancel", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void getShopDetails(Context context,String selle_user_id){
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getShopDetails(context,selle_user_id,new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                Log.d(TAG, "onSuccess Response"+arg0.toString());
                ShopModel ShopModel = (ShopModel) arg0;
                Log.d(TAG,"####ShopModel#####"+ShopModel.toString());
                Log.d(TAG,"####Profile model#####"+ShopModel.getProfileDetailsModel().toString());
                Log.d(TAG,"####Item List#####"+ShopModel.getItem_list());
               /* proceedToShop(ShopModel);*/

            }

            @Override
            public void OnFailureFromServer(String msg) {
                Log.d(TAG, msg);
            }

            @Override
            public void OnError(String msg) {

                Log.d(TAG, msg);
            }
        }, "Shop Model");

    }
    public  void proceedToShop() {
        Intent i = ShopActivity.newInstance(context, seller_user_id);
        startActivity(i);

    }
    public void refreshPage(Context context){
        if(isActive()){
            if(is_sale)
                getSaleInfo(salesModel,context);
            else
                getItemInfo(productModel,context);
        }else {
            if(is_sale){
                Intent intent = ProductDetail.newInstance(context,null,salesModel,GlobalVariables.TYPE_MY_SALE);
                context.startActivity(intent);
            }else{
                Intent intent = ProductDetail.newInstance(context,productModel,null,GlobalVariables.TYPE_MY_SALE);
                context.startActivity(intent);
            }



        }


    }
    private void check_conversation_exists(){
        showProgressBar();
        if(is_sale)
            conversationIdModel.setIs_garage(true);
        conversationIdModel.setProduct_id(productModel.getId());
        conversationIdModel.setSeller_id(seller_user_id);
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.check_conversation_exists(context, conversationIdModel,new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                hideProgressBar();
                Log.d(TAG, "onSuccess Response");
                if(arg0 instanceof StatusModel){
                    StatusModel statusModel = (StatusModel) arg0;
                    if(statusModel.isStatus()){
                        Toast.makeText(context, statusModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }else if(arg0 instanceof ErrorModel){
                    ErrorModel errorModel = (ErrorModel) arg0;
                    String msg = errorModel.getError()!=null ? errorModel.getError() : errorModel.getMessage();
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                }else if(arg0 instanceof ConversationIdModel){
                    conversationIdModel=(ConversationIdModel)arg0;
                    Log.d(TAG,"conversationIdModel iddd##############"+conversationIdModel.getConversation_id());
                    user.setConversation_id(conversationIdModel.getConversation_id());
                    user.setActive_tab(conversationIdModel.getActive_tab());
                    String id=conversationIdModel.getProduct_id();
                    user.setProduct_id(conversationIdModel.getProduct_id());
                    Intent i= ChatActivity.newInstance(context,user);
                    startActivity(i);
                }
            }

            @Override
            public void OnFailureFromServer(String msg) {
                hideProgressBar();
                Log.d(TAG, "OnFailureFromServer#############"+msg);
                if(msg!=null){
                    if(msg.equalsIgnoreCase(email_verification_error))
                        GlobalFunctions.showEmailVerificatiomAlert(context);
                    else
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void OnError(String msg) {
                hideProgressBar();
                Log.d(TAG, "OnError############"+msg);
                if(msg!=null){
                    if(msg.equalsIgnoreCase(email_verification_error))
                        GlobalFunctions.showEmailVerificatiomAlert(context);
                    else
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                }
            }
        }, "Review Ratings");

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
                    no_items.setVisibility(View.GONE);
                }else{
                    refreshList();
                    no_items.setVisibility(View.VISIBLE);
                }
            }else{
                refreshList();
                no_items.setVisibility(View.VISIBLE);
            }
    }
    private void deleteBookmark(final Context context, final String productID){
        /*GlobalFunctions.showProgress(context, "Bookmarking Product...");*/
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.deleteBookmark(context, productID, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                /*GlobalFunctions.hideProgress();*/
                if(arg0 instanceof StatusModel){
                    StatusModel statusModel = (StatusModel) arg0;
                    if(statusModel.isStatus()){
                        productModel.setIs_bookmark(false);
                        bookmark.setImageResource(R.drawable.like_border);
                        Toast.makeText(context, "Bookmark Removed", Toast.LENGTH_SHORT).show();
                    }
                }else if(arg0 instanceof ErrorModel){
                    ErrorModel errorModel = (ErrorModel) arg0;
                    String msg = errorModel.getError()!=null ? errorModel.getError() : errorModel.getMessage();
                    Log.d(TAG,"OnFailureFromServer"+msg);
                   /* Toast.makeText(context, msg, Toast.LENGTH_LONG).show();*/
                }
            }


            @Override
            public void OnFailureFromServer(String msg) {
                Log.d(TAG,"OnFailureFromServer"+msg);
                /*GlobalFunctions.hideProgress();*/
               /* Toast.makeText(context, msg, Toast.LENGTH_LONG).show();*/
            }

            @Override
            public void OnError(String msg) {
                Log.d(TAG,"OnFailureFromServer"+msg);
               /* GlobalFunctions.hideProgress();*/
                /*Toast.makeText(context, msg, Toast.LENGTH_LONG).show();*/
            }
        }, "Add Bookmarks");

    }
    public void refreshList(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                synchronized (adapter){adapter.notifyDataSetChanged();}
            }
        });
    }
    private void getSellerProducts(){
        showProgressBar();

        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getSellerProducts(context, seller_user_id, productModel.getId(),new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
             hideProgressBar();
                Log.d(TAG, "getSellerProducts onSuccess Response"+arg0.toString());
                seller_other_listing.clear();
                SimilarProductsModel similarProductsModel = (SimilarProductsModel)arg0;
                seller_other_listing=similarProductsModel.getProductsList();

              /*  setValues(similarProductsModel.getProductsList());*/
                Log.d(TAG, similarProductsModel.toString());
            }

            @Override
            public void OnFailureFromServer(String msg) {
                hideProgressBar();
              /*  no_items.setVisibility(View.VISIBLE);
                relatedList.clear();
                refreshList();*/
               /* GlobalFunctions.hideProgress();*/
                Log.d(TAG, msg);
            }

            @Override
            public void OnError(String msg) {
                hideProgressBar();
              /*  no_items.setVisibility(View.VISIBLE);
                relatedList.clear();
                refreshList();*/

            /*  hideProgressBar();*/
                Log.d(TAG, msg);
            }
        }, "Similar Products");

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
    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {
            if (tv.getTag() == null) {
                tv.setTag(tv.getText());
            }
            ViewTreeObserver vto = tv.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                @SuppressWarnings("deprecation")
                @Override
                public void onGlobalLayout() {
                    ViewTreeObserver obs = tv.getViewTreeObserver();
                    obs.removeGlobalOnLayoutListener(this);
                    if (maxLine == 0) {
                        int lineEndIndex = tv.getLayout().getLineEnd(0);
                        String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                        tv.setText(text);
                        tv.setMovementMethod(LinkMovementMethod.getInstance());
                        tv.setText(
                                addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                        viewMore), BufferType.SPANNABLE);
                    } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                        int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                        String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                        tv.setText(text);
                        tv.setMovementMethod(LinkMovementMethod.getInstance());
                        tv.setText(
                                addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                        viewMore), BufferType.SPANNABLE);
                    } else {
                        int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                        String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                        tv.setText(text);
                        tv.setMovementMethod(LinkMovementMethod.getInstance());
                        tv.setText(
                                addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                        viewMore), BufferType.SPANNABLE);
                    }
                }
            });

    }
    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {


            ssb.setSpan(new MySpannable(true){
                @Override
                public void onClick(View widget) {
                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, -1, "View Less", false);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, 3, "View More", true);
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }
    public static class MySpannable extends ClickableSpan {

        private boolean isUnderline = true;

        /**
         * Constructor
         */
        public MySpannable(boolean isUnderline) {
            this.isUnderline = isUnderline;
        }

        @Override
        public void updateDrawState(TextPaint ds) {

            ds.setUnderlineText(isUnderline);
            ds.setColor(activity.getResources().getColor(R.color.tab_selected));


        }

        @Override
        public void onClick(View widget) {

        }
    }
    public void show_edit_item_pop_up(View v){
        // Open calender Image
        if(activity!=null){
            final PopupMenu popup = new PopupMenu(activity, v);
            popup.getMenuInflater().inflate(R.menu.item_edit, popup.getMenu());
           /* MenuItem sold=popup.fin*/
            Menu popupMenu = popup.getMenu();
            if(!productModel.isAvailable())
                popupMenu.findItem(R.id.sold_item).setVisible(false);
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    if(item.getTitle().toString().equalsIgnoreCase(activity.getResources().getString(R.string.delete_item))){
                        if(GlobalFunctions.is_loggedIn(activity)){
                            final AlertDialog dialog = new AlertDialog(activity);
                            dialog.setTitle("Delete Item");
                            dialog.setIcon(R.drawable.ic_warning_black_24dp);
                            dialog.setIsCancelable(true);
                            dialog.setMessage("Do you really want to delete this item?");
                            dialog.setPositiveButton("Yes", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    deleteItem(activity,productModel.getId());
                                    dialog.dismiss();
                                }
                            });

                            dialog.setNegativeButton("Cancel", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }

                    }else if(item.getTitle().toString().equalsIgnoreCase(activity.getResources().getString(R.string.edit))){
                        CreateProductModel createProductModel = new CreateProductModel();
                        createProductModel.toObject(productModel,GlobalFunctions.getCategories(context));
                      /*  closeThisActivity();*/
                        Intent i= CreateSaleActivity.newInstance(context,null,createProductModel,null,0,GlobalVariables.TYPE_ITEM);
                        startActivity(i);
                    }else if(item.getTitle().toString().equalsIgnoreCase(activity.getResources().getString(R.string.mark_as_sold))){
                        updateItemStatus(context,productModel.getId());
                    }
                    return true;
                }
            });
            if(popup!=null)
                popup.show();
        }

    }
    public void show_edit_sale_pop_up(View v){
        // Open calender Image
        if(activity!=null){
            final PopupMenu popup = new PopupMenu(activity, v);
            popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    if(item.getTitle().toString().equalsIgnoreCase(activity.getResources().getString(R.string.edit_Sale))){
                        CreateSalesModel createSalesModel = new CreateSalesModel();
                        createSalesModel.toObject(salesModel,GlobalFunctions.getCategories(context));
                       /* closeThisActivity();*/
                        Intent i= CreateSaleActivity.newInstance(context,createSalesModel,null,null,0,GlobalVariables.TYPE_GARAGE);
                        startActivity(i);
                    }else if(item.getTitle().toString().equalsIgnoreCase(activity.getResources().getString(R.string.end_sale))){
                        endSale(context,salesModel.getId());
                    }else if(item.getTitle().toString().equalsIgnoreCase(activity.getResources().getString(R.string.delete_sale))){
                        if(GlobalFunctions.is_loggedIn(activity)){
                            final AlertDialog dialog = new AlertDialog(activity);
                            dialog.setTitle("Delete sale");
                            dialog.setIcon(R.drawable.ic_warning_black_24dp);
                            dialog.setIsCancelable(true);
                            dialog.setMessage("Do you really want to delete this sale?");
                            dialog.setPositiveButton("Yes", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                   deleteSale(context,salesModel.getId());
                                    dialog.dismiss();
                                }
                            });

                            dialog.setNegativeButton("Cancel", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }
                    }else if(item.getTitle().toString().equalsIgnoreCase(context.getResources().getString(R.string.reschedule_sale))){
                        final AlertDialog dialog = new AlertDialog(activity);
                        dialog.setTitle("Reschedule Sale");
                        dialog.setIcon(R.drawable.ic_alert);
                        dialog.setIsCancelable(true);
                        dialog.setMessage("Do you really want to reschedule this Sale?");
                        dialog.setPositiveButton("Yes", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int[] dateInts = GlobalFunctions.convertTexttoDate(salesModel.getStart_date());
                                setDateTimeField(dateInts[2],dateInts[1],dateInts[0]);
                                dialog.dismiss();
                            }
                        });

                        dialog.setNegativeButton("Cancel", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                    return true;
                }
            });
            if(popup!=null)
                popup.show();
        }

    }
    private void endSale(final Context context, String saleID){
       showProgressBar();
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.endSale(context, saleID, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
               hideProgressBar();
                Log.d(TAG, "onSuccess Response");
                Toast.makeText(context,"Sale has been closed Successfully",Toast.LENGTH_LONG).show();
                closeThisActivity();
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
        }, "Delete Sale");
    }
    private void setDateTimeField(int year, int month, int day) {
        Calendar newCalendar = Calendar.getInstance();
        day = day==0? newCalendar.get(Calendar.DAY_OF_MONTH) : day;
        month = month==0? newCalendar.get(Calendar.MONTH) : month+1;
        year = year==0? newCalendar.get(Calendar.YEAR) : year;
        toDatePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                String newDateString = GlobalVariables.dateFormatter.format(newDate.getTime());
                changeDate(context, salesModel.getId(), newDateString, salesModel.getStart_time(), salesModel.getEnd_time());
            }

        },year, month, day);

        toDatePickerDialog.show();
    }
    private void changeDate(final Context context, String saleID, String startDate, String startTime, String endTime){
        showProgressBar();
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.rescheduleSale(context, saleID, startDate, startTime, endTime, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
             hideProgressBar();
                Log.d(TAG, "onSuccess Response");
                Toast.makeText(context,"Sale has been rescheduled Successfully",Toast.LENGTH_LONG).show();
                closeThisActivity();

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
        }, "Delete Sale");

    }
    public void showProgressBar(){
        if(progress_bar!=null)
            progress_bar.setVisibility(View.VISIBLE);
    }
    public void hideProgressBar(){
        if(progress_bar!=null)
            progress_bar.setVisibility(View.GONE);
    }
    public void shareApp(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id="+context.getPackageName());
        try {
            context.startActivity(Intent.createChooser(shareIntent, "Share Blueshak using"));
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
			/*UtilityClass.showAlertDialog(context, ERROR, "Couldn't launch the market", null, 0);*/
        }

    }
}
