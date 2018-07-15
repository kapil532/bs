package com.blueshak.app.blueshak.seller.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blueshak.app.blueshak.Messaging.helper.Constants;
import com.blueshak.app.blueshak.PickLocationFromMap;
import com.blueshak.app.blueshak.garage.ui.FeatureItemPaymentActivity;
import com.blueshak.app.blueshak.garage.ui.SuccessfulCreationActivity;
import com.blueshak.app.blueshak.global.GlobalFunctions;
import com.blueshak.app.blueshak.global.GlobalVariables;
import com.blueshak.app.blueshak.item.ProductDetail;
import com.blueshak.app.blueshak.login.LoginActivity;
import com.blueshak.app.blueshak.root.RootActivity;
import com.blueshak.app.blueshak.seller.model.SalesEditModel;
import com.blueshak.app.blueshak.seller.model.SellerData;
import com.blueshak.app.blueshak.services.ServerResponseInterface;
import com.blueshak.app.blueshak.services.ServicesMethodsManager;
import com.blueshak.app.blueshak.services.model.CreateSalesModel;
import com.blueshak.app.blueshak.services.model.ErrorModel;
import com.blueshak.app.blueshak.services.model.HomeListModel;
import com.blueshak.app.blueshak.services.model.IDModel;
import com.blueshak.app.blueshak.services.model.LocationModel;
import com.blueshak.app.blueshak.services.model.ProductModel;
import com.blueshak.app.blueshak.services.model.SalesModel;
import com.blueshak.app.blueshak.services.model.StatusModel;
import com.blueshak.app.blueshak.view.AlertDialog;
import com.blueshak.blueshak.R;

import org.json.JSONArray;

import java.util.List;

public class EditOwnSalesActivity extends RootActivity {

    public static String EDIT_SALES_MODEL = "edit_sales_model";
    private TextView mTvSaleName;
    private TextView mTvSaleDescription;
    private TextView mTvSaleAddress;
    private ProgressBar mProgressBar;
    private SellerData sellerData;
    private String mSaleName;
    private String mDescription;
    private String mAddress;
    private SalesEditModel createSalesModel = new SalesEditModel();
    private static GlobalVariables globalVariables = new GlobalVariables();
    private LocationModel locationModel = new LocationModel();
    public static final String CREATE_GARRAGE_LOCATION_BUNDLE_KEY = "CreateItemActivityLocationBundleKey";
    public static final int SUCCESS_FEATURE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_own_sales_activity);
        setToolBar(getString(R.string.edit_seller));
        init();
        if (getIntent().hasExtra(EDIT_SALES_MODEL) && getIntent().getSerializableExtra(EDIT_SALES_MODEL) != null) {
            sellerData = (SellerData) getIntent().getSerializableExtra(EDIT_SALES_MODEL);
            if (sellerData != null) {
                setValues(sellerData);
            }
        }

        onPublish();
        onToolBarAction();
        onAddress();
    }

    private void init() {
        mTvSaleName = (TextView) findViewById(R.id.tv_sale_name);
        mTvSaleDescription = (TextView) findViewById(R.id.tv_sale_description);
        mTvSaleAddress = (TextView) findViewById(R.id.tv_sale_address);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    private void setValues(SellerData sellerData) {

        if (sellerData.getSellerName() != null) {
            mTvSaleName.setText(sellerData.getSellerName());
        }
        if (sellerData.getDescription() != null) {
            mTvSaleDescription.setText(sellerData.getDescription());
        }
        if (sellerData.getSaleAddress() != null) {
            mTvSaleAddress.setText(sellerData.getSaleAddress());
        }
    }

    private void onAddress(){
        mTvSaleAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = PickLocationFromMap.newInstance(EditOwnSalesActivity.this, GlobalVariables.TYPE_GARAGE_SALE, false, locationModel);
                startActivityForResult(intent, globalVariables.REQUEST_CODE_FILTER_PICK_LOCATION);
            }
        });
    }

    private void onPublish() {
        Button btnPublish = (Button) findViewById(R.id.btn_publish);
        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setModelValues();
            }
        });
    }

    private void setModelValues() {
        try {
            mSaleName = Constants.parseTo(mTvSaleName.getText().toString());
        } catch (Exception eee) {
            mSaleName = mTvSaleName.getText().toString();
        }
        try {
            mDescription = Constants.parseTo(mTvSaleDescription.getText().toString());
        } catch (Exception e) {
            mDescription = mTvSaleDescription.getText().toString();
        }
        mAddress = mTvSaleAddress.getText().toString();

        if (TextUtils.isEmpty(mSaleName)) {
            Toast.makeText(this, "Please enter the sale name", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(mDescription)) {
            Toast.makeText(this, "Please enter the sale description", Toast.LENGTH_LONG).show();
        }
        if (TextUtils.isEmpty(mAddress)) {
            Toast.makeText(this, "Please enter sale location", Toast.LENGTH_LONG).show();
        } else {

            createSalesModel.setName(mSaleName);
            createSalesModel.setSaleAddress(mAddress);
            createSalesModel.setDescription(mDescription);

            if (locationModel != null) {
                if (TextUtils.isEmpty(createSalesModel.getLatitude())) {
                    createSalesModel.setLatitude(locationModel.getLatitude() + "");
                    createSalesModel.setLongitude(locationModel.getLongitude() + "");
                }
            }
            String token = GlobalFunctions.getSharedPreferenceString(this, GlobalVariables.SHARED_PREFERENCE_TOKEN);
            if (token != null) {
                showProgressBar(mProgressBar);
                publishSales(this, createSalesModel);
            } else {
                showSettingsAlert();
            }
        }
    }

    private void publishSales(final Context context, final SalesEditModel createSalesModel) {
        final String email_verification_error = context.getResources().getString(R.string.ErrorEmailVerification);
        //showProgressBar(mProgressBar);
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.publishOwnSales(context, createSalesModel, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                hideProgressBar(mProgressBar);
                if (arg0 instanceof StatusModel) {
                    if(sellerData!=null && sellerData.getIsFeatured().equalsIgnoreCase("0")){
                        Intent intent = new Intent(EditOwnSalesActivity.this,FeatureItemPaymentActivity.class);
                        intent.putExtra(FeatureItemPaymentActivity.PRODUCTID,sellerData.getSellerId());//product Id
                        intent.putExtra(FeatureItemPaymentActivity.FEATURE_FAG,false);
                        intent.putExtra(FeatureItemPaymentActivity.FEATURE_FIRST_IMAGE,sellerData.getSellerImage());
                        startActivityForResult(intent,SUCCESS_FEATURE);
                    }else{
                        Toast.makeText(context, "Sale has been Published Successfully", Toast.LENGTH_LONG).show();
                        finish();
                    }

                }
            }

            @Override
            public void OnFailureFromServer(String msg) {
                hideProgressBar(mProgressBar);

                if (msg != null) {
                    if (msg.equalsIgnoreCase(email_verification_error))
                        GlobalFunctions.showEmailVerificatiomAlert(context);
                    else
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void OnError(String msg) {
                hideProgressBar(mProgressBar);

                if (msg != null) {
                    if (msg.equalsIgnoreCase(email_verification_error))
                        GlobalFunctions.showEmailVerificatiomAlert(context);
                    else
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                }
            }
        }, "Publish Sale");

    }


    private void onToolBarAction() {
        ((ImageView) mToolBarView.findViewById(R.id.go_back)).setVisibility(View.VISIBLE);
        ((ImageView) mToolBarView.findViewById(R.id.img_chat)).setVisibility(View.GONE);
        TextView cancel = (TextView) mToolBarView.findViewById(R.id.cancel);
        cancel.setVisibility(View.GONE);
        ((ImageView) mToolBarView.findViewById(R.id.go_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == globalVariables.REQUEST_CODE_FILTER_PICK_LOCATION) {
                    LocationModel location_model = (LocationModel) data.getExtras().getSerializable(CREATE_GARRAGE_LOCATION_BUNDLE_KEY);
                    Log.i(TAG, "name pm" + location_model.getFormatted_address());
                    locationModel = location_model;
                   /* productModel.setLatitude(location_model.getLatitude());
                    productModel.setLongitude(location_model.getLongitude());
                    productModel.setAddress(location_model.getFormatted_address_for_map());
                    productModel.setSuburb(location_model.getSubhurb());
                    productModel.setCity(location_model.getCity());*/
                    mTvSaleAddress.setText(location_model.getFormatted_address());
                }

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




    public void showSettingsAlert() {
        final AlertDialog alertDialog = new AlertDialog(this);
        alertDialog.setMessage("Please Login/Register to continue");
        alertDialog.setPositiveButton("Continue", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent creategarrage = new Intent(EditOwnSalesActivity.this, LoginActivity.class);
                startActivity(creategarrage);
                finish();
                //closeThisActivity();
            }
        });
        alertDialog.show();
    }
}
