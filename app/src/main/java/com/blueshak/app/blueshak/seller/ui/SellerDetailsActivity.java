package com.blueshak.app.blueshak.seller.ui;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blueshak.app.blueshak.Messaging.activity.ChatActivity;
import com.blueshak.app.blueshak.Messaging.data.User;
import com.blueshak.app.blueshak.global.GlobalFunctions;
import com.blueshak.app.blueshak.global.GlobalVariables;
import com.blueshak.app.blueshak.login.LoginActivity;
import com.blueshak.app.blueshak.root.RootActivity;
import com.blueshak.app.blueshak.search.SearchListAdapter;
import com.blueshak.app.blueshak.seller.adapter.SellerReviewsAdapter;
import com.blueshak.app.blueshak.seller.model.Data;
import com.blueshak.app.blueshak.seller.model.SellerData;
import com.blueshak.app.blueshak.services.ServerResponseInterface;
import com.blueshak.app.blueshak.services.ServicesMethodsManager;
import com.blueshak.app.blueshak.services.model.CategoryListModel;
import com.blueshak.app.blueshak.services.model.ConversationIdModel;
import com.blueshak.app.blueshak.services.model.ErrorModel;
import com.blueshak.app.blueshak.services.model.ProductModel;
import com.blueshak.app.blueshak.services.model.ReviewsRatingsListModel;
import com.blueshak.app.blueshak.services.model.ReviewsRatingsModel;
import com.blueshak.app.blueshak.services.model.SimilarProductsModel;
import com.blueshak.app.blueshak.services.model.StatusModel;
import com.blueshak.app.blueshak.util.BlueShakLog;
import com.blueshak.app.blueshak.util.LocationListener;
import com.blueshak.app.blueshak.util.LocationService;
import com.blueshak.app.blueshak.util.SimpleDividerItemDecoration;
import com.blueshak.app.blueshak.util.Utilities;
import com.blueshak.blueshak.R;

import java.util.ArrayList;
import java.util.List;

public class SellerDetailsActivity extends RootActivity implements LocationListener {

    private static String TAG = SellerDetailsActivity.class.getSimpleName();
    public static String SALES_DATA = "sales_data";
    public static String SALES_FEATURE_DATA = "sales_feature_data";


    private ImageView img_seller_bg;
    private ImageView img_seller_pic;
    private TextView tv_seller_name;
    private TextView tv_member;
    private TextView tv_item_sold;
    private TextView tv_rating;
    private RatingBar rating_bar;
    private ProgressBar progress_bar;
    private TextView tv_seller_listing;
    private TextView tv_seller_review;
    private RecyclerView listening_recycler_view;
    private TextView no_items;
    private CategoryListModel categoriesModels = null;
    private LocationService locServices;
    private double latitude, longitude;

    private List<ProductModel> sellerListingList = new ArrayList<ProductModel>();
    private List<ReviewsRatingsModel> sellerReviewList = new ArrayList<ReviewsRatingsModel>();
    private SellerData sellerData = null;
    private Data dataModel = null;
    private boolean isSimilarListing = true;
    private ConversationIdModel conversationIdModel = new ConversationIdModel();
    private User user = new User();
    private ReviewsRatingsListModel reviewsRatingsModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_details_activity);
        setToolBar(getString(R.string.seller_details));
        init();
        getLocation();
        if (getIntent().hasExtra(SALES_DATA) && getIntent().getSerializableExtra(SALES_DATA) != null) {
            sellerData = (SellerData) getIntent().getSerializableExtra(SALES_DATA);
            setSellerValues(sellerData);
            getSellerListingProducts(sellerData.getSellerId(), sellerData.getUserId());
            getReviewRatings(sellerData.getSellerId());
            onToolBarAction(sellerData.getSellerName(), sellerData.getSellerId());
            listingTabSelection(sellerData.getSellerId(), sellerData.getUserId());

        } else if (getIntent().hasExtra(SALES_FEATURE_DATA) && getIntent().getSerializableExtra(SALES_FEATURE_DATA) != null) {
            dataModel = (Data) getIntent().getSerializableExtra(SALES_FEATURE_DATA);
            setFeatureSellerValues(dataModel);
            getSellerListingProducts(dataModel.getSellerId(), dataModel.getUserId());
            getReviewRatings(dataModel.getSellerId());
            onToolBarAction(dataModel.getSellerName(), dataModel.getSellerId());
            listingTabSelection(dataModel.getSellerId(), dataModel.getUserId());
        }

    }

    private void init() {
        img_seller_bg = (ImageView) findViewById(R.id.img_seller_bg);
        img_seller_pic = (ImageView) findViewById(R.id.img_seller_pic);
        tv_seller_name = (TextView) findViewById(R.id.tv_seller_name);
        tv_member = (TextView) findViewById(R.id.tv_member);
        tv_item_sold = (TextView) findViewById(R.id.tv_item_sold);
        tv_rating = (TextView) findViewById(R.id.tv_rating);
        rating_bar = (RatingBar) findViewById(R.id.rating_bar);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        tv_seller_listing = (TextView) findViewById(R.id.tv_seller_listing);
        tv_seller_review = (TextView) findViewById(R.id.tv_seller_review);
        listening_recycler_view = (RecyclerView) findViewById(R.id.listening_recycler_view);
        no_items = (TextView) findViewById(R.id.no_items);

        categoriesModels = GlobalFunctions.getCategories(this);
    }

    private void onToolBarAction(final String sellerName, final String sellerId) {
        ((ImageView) mToolBarView.findViewById(R.id.go_back)).setVisibility(View.VISIBLE);
        ((ImageView) mToolBarView.findViewById(R.id.img_chat)).setVisibility(View.VISIBLE);
        ((TextView) mToolBarView.findViewById(R.id.cancel)).setVisibility(View.GONE);
        ((ImageView) mToolBarView.findViewById(R.id.go_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        ((ImageView) mToolBarView.findViewById(R.id.img_chat)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_loggedIn()) {
                    if (!isSelfUser(sellerId)) {
                        if (sellerName != null || sellerId != null) {
                            check_conversation_exists(SellerDetailsActivity.this);
                        } else {
                            Toast.makeText(SellerDetailsActivity.this, "Sorry can't send message", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        //show_edit_sale_pop_up(v);
                    }
                } else {
                    showSettingsAlert(SellerDetailsActivity.this);
                }
            }
        });
    }

    private void setSellerValues(SellerData sellerData) {

        Utilities.setBitmapImages(img_seller_pic, sellerData.getSellerImage());

        if (sellerData.getSellerName() != null) {
            tv_seller_name.setText(sellerData.getSellerName());
        }

        if (sellerData.getListedDate() != null && !sellerData.getListedDate().trim().isEmpty()) {
            tv_member.setVisibility(View.VISIBLE);
            tv_member.setText(getString(R.string.member_since) + " " + sellerData.getListedDate());
        } else {
            tv_member.setVisibility(View.GONE);
        }

        if (sellerData.getShopItemsCount() != null) {
            tv_item_sold.setVisibility(View.VISIBLE);
            tv_item_sold.setText(getString(R.string.item_sold) + " " + sellerData.getShopItemsCount());
        } else {
            tv_item_sold.setVisibility(View.GONE);
        }

        if (sellerData.getAvgSellerRating() != null) {
            rating_bar.setRating(Float.valueOf(sellerData.getAvgSellerRating()));
        }


    }

    private void setFeatureSellerValues(Data sellerData) {

        Utilities.setBitmapImages(img_seller_pic, sellerData.getSellerImage());

        if (sellerData.getSellerName() != null) {
            tv_seller_name.setText(sellerData.getSellerName());
        }

        if (sellerData.getListedDate() != null && !sellerData.getListedDate().trim().isEmpty()) {
            tv_member.setVisibility(View.VISIBLE);
            tv_member.setText(getString(R.string.member_since) + " " + sellerData.getListedDate());
        } else {
            tv_member.setVisibility(View.GONE);
        }

        if (sellerData.getShopItemsCount() != null) {
            tv_item_sold.setVisibility(View.VISIBLE);
            tv_item_sold.setText(getString(R.string.item_sold) + " " + sellerData.getShopItemsCount());
        } else {
            tv_item_sold.setVisibility(View.GONE);
        }

        if (sellerData.getAvgSellerRating() != null) {
            rating_bar.setRating(Float.valueOf(sellerData.getAvgSellerRating()));
        }


    }

    private void getLocation() {
        locServices = new LocationService(this);
        locServices.setListener(this);
        if (!locServices.canGetLocation()) {/*locServices.showSettingsAlert();*/} else {
            latitude = locServices.getLatitude();
            longitude = locServices.getLongitude();
        }
    }

    private void listingTabSelection(final String sellerId, final String userId) {
        tv_seller_listing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSimilarListing) {
                    isSimilarListing = true;
                    tv_seller_listing.setTextColor(getResources().getColor(R.color.white));
                    tv_seller_listing.setBackgroundColor(getResources().getColor(R.color.brandColor));
                    tv_seller_review.setTextColor(getResources().getColor(R.color.brandColor));
                    tv_seller_review.setBackgroundColor(getResources().getColor(R.color.brand_background_color));

                    if (sellerListingList != null && sellerListingList.size() > 0) {
                        listening_recycler_view.setVisibility(View.VISIBLE);
                        no_items.setVisibility(View.GONE);
                        setListingAdapter(sellerListingList);
                    } else {
                        getSellerListingProducts(sellerId, userId);
                    }


                }
            }
        });
        tv_seller_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSimilarListing) {
                    isSimilarListing = false;
                    tv_seller_review.setTextColor(getResources().getColor(R.color.white));
                    tv_seller_review.setBackgroundColor(getResources().getColor(R.color.brandColor));
                    tv_seller_listing.setTextColor(getResources().getColor(R.color.brandColor));
                    tv_seller_listing.setBackgroundColor(getResources().getColor(R.color.brand_background_color));

                    if (sellerReviewList != null && sellerReviewList.size() > 0) {
                        listening_recycler_view.setVisibility(View.VISIBLE);
                        no_items.setVisibility(View.GONE);
                        setReviewAdapter(sellerReviewList);
                    } else {
                        getReviewRatings(sellerId);
                    }

                }

            }
        });
    }

    private void getSellerListingProducts(String sellerId, final String productId) {
        showProgressBar(progress_bar);
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getSellerProducts(this, sellerId, productId,
                new ServerResponseInterface() {
                    @Override
                    public void OnSuccessFromServer(Object arg0) {
                        hideProgressBar(progress_bar);
                        sellerListingList.clear();
                        SimilarProductsModel similarProductsModel = (SimilarProductsModel) arg0;
                        sellerListingList = similarProductsModel.getProductsList();
                        if (sellerListingList != null && sellerListingList.size() > 0) {
                            listening_recycler_view.setVisibility(View.VISIBLE);
                            no_items.setVisibility(View.GONE);
                            setListingAdapter(sellerListingList);
                        } else {
                            listening_recycler_view.setVisibility(View.GONE);
                            no_items.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void OnFailureFromServer(String msg) {
                        hideProgressBar(progress_bar);
                        listening_recycler_view.setVisibility(View.GONE);
                        no_items.setVisibility(View.VISIBLE);
                        BlueShakLog.logDebug(TAG, "OnFailureFromServer -> " + msg);
                    }

                    @Override
                    public void OnError(String msg) {
                        hideProgressBar(progress_bar);
                        listening_recycler_view.setVisibility(View.GONE);
                        no_items.setVisibility(View.VISIBLE);
                        BlueShakLog.logDebug(TAG, "OnError -> " + msg);
                    }
                }, "Similar Products");

    }

    private void getReviewRatings(String sellerId) {
        showProgressBar(progress_bar);
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getReviewsRatings(this, sellerId, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                hideProgressBar(progress_bar);
                BlueShakLog.logDebug(TAG, "onSuccess Response");
                if (arg0 instanceof StatusModel) {
                    StatusModel statusModel = (StatusModel) arg0;
                    if (statusModel.isStatus()) {
                        Toast.makeText(SellerDetailsActivity.this, statusModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    listening_recycler_view.setVisibility(View.GONE);
                    no_items.setVisibility(View.VISIBLE);
                } else if (arg0 instanceof ErrorModel) {
                    hideProgressBar(progress_bar);
                    ErrorModel errorModel = (ErrorModel) arg0;
                    String msg = errorModel.getError() != null ? errorModel.getError() : errorModel.getMessage();
                    Toast.makeText(SellerDetailsActivity.this, msg, Toast.LENGTH_LONG).show();
                    listening_recycler_view.setVisibility(View.GONE);
                    no_items.setVisibility(View.VISIBLE);
                } else if (arg0 instanceof ReviewsRatingsListModel) {
                    no_items.setVisibility(View.GONE);
                    hideProgressBar(progress_bar);
                    reviewsRatingsModel = (ReviewsRatingsListModel) arg0;
                    sellerReviewList = reviewsRatingsModel.getReviews_list();
                    if (sellerReviewList != null && sellerReviewList.size() > 0) {
                        listening_recycler_view.setVisibility(View.VISIBLE);
                        no_items.setVisibility(View.GONE);
                        setReviewAdapter(sellerReviewList);
                    } else {
                        listening_recycler_view.setVisibility(View.GONE);
                        no_items.setVisibility(View.VISIBLE);
                    }

                }
            }

            @Override
            public void OnFailureFromServer(String msg) {
                hideProgressBar(progress_bar);
                listening_recycler_view.setVisibility(View.GONE);
                no_items.setVisibility(View.VISIBLE);
                BlueShakLog.logDebug(TAG, msg);
            }

            @Override
            public void OnError(String msg) {
                hideProgressBar(progress_bar);
                listening_recycler_view.setVisibility(View.GONE);
                no_items.setVisibility(View.VISIBLE);
                BlueShakLog.logDebug(TAG, msg);
            }
        }, "Review Ratings");

    }

    private void setListingAdapter(List<ProductModel> listingList) {
        SearchListAdapter adapter = new SearchListAdapter(this, listingList, false);
        LinearLayoutManager linearLayoutManagerVertical =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listening_recycler_view.setLayoutManager(linearLayoutManagerVertical);
        listening_recycler_view.setNestedScrollingEnabled(false);
        listening_recycler_view.addItemDecoration(new SimpleDividerItemDecoration(this));
        listening_recycler_view.setItemAnimator(new DefaultItemAnimator());
        listening_recycler_view.setAdapter(adapter);
    }

    private void setReviewAdapter(List<ReviewsRatingsModel> sellerReviewList) {
        if (!isSimilarListing) {
            SellerReviewsAdapter adapter = new SellerReviewsAdapter(this, sellerReviewList);
            LinearLayoutManager linearLayoutManagerVertical =
                    new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            listening_recycler_view.setLayoutManager(linearLayoutManagerVertical);
            listening_recycler_view.setNestedScrollingEnabled(false);
            listening_recycler_view.addItemDecoration(new SimpleDividerItemDecoration(this));
            listening_recycler_view.setItemAnimator(new DefaultItemAnimator());
            listening_recycler_view.setAdapter(adapter);
        }

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onProviderEnabled(String location) {

    }

    public boolean is_loggedIn() {
        String token = GlobalFunctions.getSharedPreferenceString(SellerDetailsActivity.this, GlobalVariables.SHARED_PREFERENCE_TOKEN);
        if (token != null) {
            return true;
        } else {
            return false;
        }

    }

    private boolean isSelfUser(String sellerId) {
        boolean isSelf = false;
        String signed_user_user_id = GlobalFunctions.getSharedPreferenceString(SellerDetailsActivity.this, GlobalVariables.SHARED_PREFERENCE_USERID);
        if (signed_user_user_id != null && sellerId != null) {
            if (Integer.parseInt(sellerId) == Integer.parseInt(signed_user_user_id)) {
                isSelf = true;
            } else {
                isSelf = false;
            }
        }
        return isSelf;

    }

    private void check_conversation_exists(final Context context) {
        //showProgressBar();
        user.setIs_sale(false);
        conversationIdModel.setIs_garage(true);
        if (sellerData != null) {
            user.setName(sellerData.getSellerName());
            user.setBs_id(sellerData.getSellerId());
            user.setProfileImageUrl(sellerData.getSellerImage());
            user.setProduct_name(sellerData.getSellerName());
            user.setProduct_url(sellerData.getSellerImage());
            user.setProduct_id(sellerData.getUserId());
            conversationIdModel.setProduct_id(sellerData.getUserId());
            conversationIdModel.setSeller_id(sellerData.getSellerId());
        } else {
            user.setName(dataModel.getSellerName());
            user.setBs_id(dataModel.getSellerId());
            user.setProfileImageUrl(dataModel.getSellerImage());
            user.setProduct_name(dataModel.getSellerName());
            user.setProduct_url(dataModel.getSellerImage());
            user.setProduct_id(dataModel.getUserId());
            conversationIdModel.setProduct_id(dataModel.getUserId());
            conversationIdModel.setSeller_id(dataModel.getSellerId());
        }

        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.check_conversation_exists(context, conversationIdModel, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                // hideProgressBar();
                Log.d(TAG, "onSuccess Response");
                if (arg0 instanceof StatusModel) {
                    StatusModel statusModel = (StatusModel) arg0;
                    if (statusModel.isStatus()) {
                        Toast.makeText(context, statusModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (arg0 instanceof ErrorModel) {
                    ErrorModel errorModel = (ErrorModel) arg0;
                    String msg = errorModel.getError() != null ? errorModel.getError() : errorModel.getMessage();
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                } else if (arg0 instanceof ConversationIdModel) {
                    conversationIdModel = (ConversationIdModel) arg0;
                    user.setConversation_id(conversationIdModel.getConversation_id());
                    user.setActive_tab(conversationIdModel.getActive_tab());
                    String id = conversationIdModel.getProduct_id();
                    user.setProduct_id(conversationIdModel.getProduct_id());
                    Intent i = ChatActivity.newInstance(context, user);
                    context.startActivity(i);
                }
            }

            @Override
            public void OnFailureFromServer(String msg) {
                //hideProgressBar();
                if (msg != null) {
                    if (msg.equalsIgnoreCase(context.getResources().getString(R.string.ErrorEmailVerification)))
                        GlobalFunctions.showEmailVerificatiomAlert(context);
                    else
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void OnError(String msg) {
                // hideProgressBar();
                if (msg != null) {
                    if (msg.equalsIgnoreCase(context.getResources().getString(R.string.ErrorEmailVerification)))
                        GlobalFunctions.showEmailVerificatiomAlert(context);
                    else
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                }
            }
        }, "Review Ratings");

    }

    public void showSettingsAlert(final Context context) {
        final com.blueshak.app.blueshak.view.AlertDialog alertDialog = new com.blueshak.app.blueshak.view.AlertDialog(context);
        alertDialog.setTitle("Login/Register");
        alertDialog.setMessage("Please Login/Register to continue");
        alertDialog.setPositiveButton("Continue", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent creategarrage = new Intent(context, LoginActivity.class);
                context.startActivity(creategarrage);
            }
        });
        alertDialog.show();
    }
}
