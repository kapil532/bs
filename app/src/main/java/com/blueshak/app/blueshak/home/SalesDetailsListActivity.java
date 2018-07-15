package com.blueshak.app.blueshak.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blueshak.app.blueshak.base.PresenterCallBack;
import com.blueshak.app.blueshak.global.GlobalVariables;
import com.blueshak.app.blueshak.map.MapViewFragment;
import com.blueshak.app.blueshak.root.RootActivity;
import com.blueshak.app.blueshak.seller.adapter.SalesProductAdapter;
import com.blueshak.app.blueshak.seller.model.Data;
import com.blueshak.app.blueshak.seller.model.SalesDetailsModel;
import com.blueshak.app.blueshak.seller.model.SalesDetailsProduct;
import com.blueshak.app.blueshak.seller.model.SellerData;
import com.blueshak.app.blueshak.seller.presenter.SalesPresenter;
import com.blueshak.app.blueshak.seller.ui.SellerDetailsActivity;
import com.blueshak.blueshak.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lsingh013 on 24/05/18.
 */

public class SalesDetailsListActivity extends RootActivity {

    private TextView tv_listed_date;
    private TextView tv_items_count;
    private TextView tv_sales_description;
    private ImageView seller_icon;
    private TextView tv_seller_name;
    private TextView txt_review;
    private RatingBar rating_bar;
    private FrameLayout seller_map;
    private RecyclerView seller_recycler_view;
    public static String FEATURE_DATA = "feature_data";
    public static String SALES_DATA = "sales_data";
    private FragmentManager fragmentManager;
    private ProgressBar progress_bar;
    private ArrayList<SalesDetailsProduct> detailsProducts = new ArrayList<>();
    private TextView tv_seller_address;
    private SalesDetailsModel salesDetailsResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_details_list_activity);
        init();

        if (getIntent().hasExtra(FEATURE_DATA) && getIntent().getSerializableExtra(FEATURE_DATA) != null) {
            Data featureData = (Data) getIntent().getSerializableExtra(FEATURE_DATA);
            if(featureData!=null){
                getSalesDetails(featureData.getShopId(), featureData.getLatitude(), featureData.getLongitude());
                setToolBar(featureData.getShopName());
                onFeatureSeller(featureData);
            }

        } else if (getIntent().hasExtra(SALES_DATA) && getIntent().getSerializableExtra(SALES_DATA) != null) {
            SellerData featureData = (SellerData) getIntent().getSerializableExtra(SALES_DATA);
            if(featureData!=null){
                getSalesDetails(featureData.getShopId(), featureData.getLatitude(), featureData.getLongitude());
                setToolBar(featureData.getShopName());
                onSeller(featureData);
            }
        }
        onToolBarBack();

    }

    private void onToolBarBack() {
        ((ImageView) mToolBarView.findViewById(R.id.go_back)).setVisibility(View.VISIBLE);
        ((TextView) mToolBarView.findViewById(R.id.cancel)).setVisibility(View.GONE);
        ((ImageView) mToolBarView.findViewById(R.id.go_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private void init() {
        tv_listed_date = (TextView) findViewById(R.id.txt_listed_date);
        tv_items_count = (TextView) findViewById(R.id.txt_items_count);
        tv_sales_description = (TextView) findViewById(R.id.sales_description);
        seller_icon = (ImageView) findViewById(R.id.seller_icon);
        tv_seller_name = (TextView) findViewById(R.id.seller_name);
        txt_review = (TextView) findViewById(R.id.txt_review);
        tv_seller_address = (TextView) findViewById(R.id.seller_address);
        rating_bar = (RatingBar) findViewById(R.id.rating_bar);
        seller_map = (FrameLayout) findViewById(R.id.seller_map);
        seller_recycler_view = (RecyclerView) findViewById(R.id.seller_recycler_view);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        fragmentManager = getSupportFragmentManager();

    }

    private void setValues(SalesDetailsModel salesDetailsModel) {
        if(salesDetailsModel.getSuburb()!=null && !salesDetailsModel.getSuburb().trim().isEmpty()){
            tv_listed_date.setText(salesDetailsModel.getListedDate() + " | " + salesDetailsModel.getSuburb());
        }else{
            tv_listed_date.setText(salesDetailsModel.getListedDate());
        }

        String sItems = "Item";
        int iCount = salesDetailsModel.getSaleItemsCount();
        if (iCount == 0 || iCount == 1) {
            sItems = "Item";
        } else {
            sItems = "Items";
        }
        tv_items_count.setText("" + iCount + " " + sItems);
        tv_sales_description.setText(salesDetailsModel.getDescription());
        tv_seller_name.setText(salesDetailsModel.getSellerName());
        setBitmapImages(seller_icon, salesDetailsModel.getSellerImage());
        String review;
        if (salesDetailsModel.getReviewsCount() == 0 || salesDetailsModel.getReviewsCount() == 1) {
            review = getString(R.string.read_review) + " (" + salesDetailsModel.getReviewsCount() + ")";
        } else {
            review = getString(R.string.read_reviews) + " (" + salesDetailsModel.getReviewsCount() + ")";
        }
        txt_review.setText(review);

        if (salesDetailsModel.getAvgSellerRating() != null) {
            rating_bar.setRating(Float.valueOf(salesDetailsModel.getAvgSellerRating()));
        }
        if(salesDetailsModel.getSaleAddress()!=null && !salesDetailsModel.getSaleAddress().isEmpty()){
            tv_seller_address.setVisibility(View.VISIBLE);
            tv_seller_address.setText(salesDetailsModel.getSaleAddress());
        }else{
            tv_seller_address.setVisibility(View.GONE);
        }

        setMap(salesDetailsModel);
    }

    private void onSeller(final SellerData obj){
        ((RelativeLayout)findViewById(R.id.layout_seller)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SalesDetailsListActivity.this, SellerDetailsActivity.class);
                intent.putExtra(SellerDetailsActivity.SALES_DATA, obj);
                startActivity(intent);
            }
        });
    }

    private void onFeatureSeller(final Data obj){
        ((RelativeLayout)findViewById(R.id.layout_seller)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SalesDetailsListActivity.this, SellerDetailsActivity.class);
                intent.putExtra(SellerDetailsActivity.SALES_FEATURE_DATA, obj);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(salesDetailsResponse!=null){
            setMap(salesDetailsResponse);
        }
    }

    public void setMap(SalesDetailsModel detailsModel) {
        if (fragmentManager != null) {
            if (fragmentManager != null && !this.isFinishing()) {
                Fragment fragment = new MapViewFragment().newInstance(GlobalVariables.TYPE_SHOP, detailsModel, false);
                fragmentManager.beginTransaction().replace(R.id.seller_map, fragment).commitAllowingStateLoss();
            }
        }
    }

    public void showProgressBar() {
        if (progress_bar != null)
            progress_bar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        if (progress_bar != null)
            progress_bar.setVisibility(View.GONE);
    }

    private void getSalesDetails(String shopId, String latitude, String longitude) {
        showProgressBar();
        SalesPresenter salesPresenter = new SalesPresenter();
        salesPresenter.getSalesDetails(this, shopId, latitude, longitude,
                new PresenterCallBack<SalesDetailsModel>() {
                    @Override
                    public void onSuccess(SalesDetailsModel response) {
                        detailsProducts.clear();
                        hideProgressBar();
                        if (response != null) {
                            salesDetailsResponse = response;
                            setValues(salesDetailsResponse);
                            List<SalesDetailsProduct> salesProduct = response.getProducts();
                            detailsProducts.addAll(salesProduct);
                            setAdapterProductList(salesDetailsResponse);
                        }
                    }

                    @Override
                    public void onFailure() {
                        hideProgressBar();

                    }
                });
    }

    private void setAdapterProductList(SalesDetailsModel salesProduct) {

        SalesProductAdapter adapter = new SalesProductAdapter(this, salesProduct);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        seller_recycler_view.setLayoutManager(layoutManager);
        seller_recycler_view.setNestedScrollingEnabled(false);
        seller_recycler_view.setItemAnimator(new DefaultItemAnimator());
        seller_recycler_view.setAdapter(adapter);

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
        ImageLoader.getInstance().displayImage(decodedImgUri, imageView, GALLERY);
    }

}
