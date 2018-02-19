package com.blueshak.app.blueshak.garage.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.blueshak.app.blueshak.base.PresenterCallBack;
import com.blueshak.app.blueshak.garage.CreateItemSaleFragment;
import com.blueshak.app.blueshak.home.model.FeatureItemData;
import com.blueshak.app.blueshak.home.model.FeatureItemsModel;
import com.blueshak.app.blueshak.home.presenter.ItemListPresenter;
import com.blueshak.app.blueshak.paypal.PayPalConfig;
import com.blueshak.app.blueshak.paypal.model.FeatureDataList;
import com.blueshak.app.blueshak.paypal.model.FeaturedOptionModel;
import com.blueshak.app.blueshak.paypal.model.PayPalPaymentModel;
import com.blueshak.app.blueshak.services.model.CreateImageModel;
import com.blueshak.app.blueshak.services.model.FilterModel;
import com.blueshak.app.blueshak.util.BlueShakLog;
import com.blueshak.app.blueshak.util.BlueShakSharedPreferences;
import com.blueshak.app.blueshak.util.DialogUtils;
import com.blueshak.blueshak.R;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;

public class FeatureItemPaymentActivity extends AppCompatActivity {
    private static String TAG = "FeatureItemPaymentActivity";
    //The views
    private Button buttonPay;
    private EditText editTextAmount;

    //Payment Amount
    private String paymentAmount = null;
    private String paymentDescription;
    private String paymentCurrency = "USD";
    private ProgressBar progress_bar;
    private RadioGroup radioGroup;
    private ArrayList<FeatureDataList> featureItemsOptionList = new ArrayList<FeatureDataList>();
    private TextView seller_item_description;
    public static String PRODUCTID = "PRODUCTID";
    public static String FEATURE_FAG = "feature_fag";
    private String mProductId;
    private boolean isFeaturedFlag = false;
    private String optionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature_item_payment);
        init();

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            mProductId = bundle.getString(PRODUCTID);
            isFeaturedFlag = bundle.getBoolean(FEATURE_FAG);
        }

        startPayPalService();
        onSkip();
        onBack();
        onPayPal();
        //onRadioGroup();
        setValues();
        getFeatureListOption(this);
    }

    private void init() {
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        seller_item_description = (TextView)findViewById(R.id.seller_item_description);
    }

    private void onSkip() {
        findViewById(R.id.btn_skip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();

            }
        });
    }

    private void onBack() {
        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();

            }
        });
    }

    private void onPayPal() {
        findViewById(R.id.btn_paypal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paymentAmount != null && !paymentAmount.isEmpty()) {
                    getPayment();
                } else {
                    Toast.makeText(FeatureItemPaymentActivity.this, "Please select payment option", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setValues() {
        ImageView imageView = (ImageView) findViewById(R.id.img_feature);
        TextView textView = (TextView) findViewById(R.id.txt_item_name);
        if (CreateItemSaleFragment.objectUploadPhoto.getAvailablePhotos().size() > 0) {
            setBitmapImages(CreateItemSaleFragment.objectUploadPhoto.getAvailablePhotos(), imageView, 0);
        } else {
            BlueShakLog.logDebug("paymentExample setImage() ", "setImage() else failed");
        }
        if (BlueShakSharedPreferences.getProductName(this) != null) {
            textView.setText(BlueShakSharedPreferences.getProductName(this));
        }

    }

    private void getFeatureListOption(Context context) {
        showProgressBar();
        ItemListPresenter itemListPresenter = new ItemListPresenter();
        itemListPresenter.getFeatureItemPriceOption(context, new PresenterCallBack<FeaturedOptionModel>() {
            @Override
            public void onSuccess(FeaturedOptionModel object) {
                hideProgressBar();
                FeatureDataList[] optionModelList = object.getData();
                seller_item_description.setText(object.getHeader_text());
                for (FeatureDataList featureItem : optionModelList) {
                    featureItemsOptionList.add(featureItem);
                }
                createRadioButton(featureItemsOptionList);
            }

            @Override
            public void onFailure() {
                hideProgressBar();
            }
        });
    }
    RadioButton radioButton;
    private void createRadioButton(final ArrayList<FeatureDataList> featureItemsOptionList){

        for(int i = 0; i< featureItemsOptionList.size();i++){
            paymentCurrency = featureItemsOptionList.get(0).getCurrency();
            radioButton = new RadioButton(this);
            RadioGroup.LayoutParams param = new RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.WRAP_CONTENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT);
            param.leftMargin = 20;
            param.bottomMargin = 10;
            param.topMargin = 10;
            param.rightMargin = 10;
            radioButton.setLayoutParams(param);
            radioButton.setTag(i);
            radioButton.setId(i);
            LinearLayout linearLayout = new LinearLayout(this);
            RadioGroup.LayoutParams paramLLayout = new RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.MATCH_PARENT,2);
            paramLLayout.leftMargin = 100;
            linearLayout.setLayoutParams(paramLLayout);
            linearLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_gray));
            radioGroup.addView(radioButton);
            if(featureItemsOptionList.size()-1 > i){
                radioGroup.addView(linearLayout);
            }
            radioButton.setText(featureItemsOptionList.get(i).getFormatted_text());

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    radioButton = (RadioButton) findViewById(checkedId);
                    paymentAmount = featureItemsOptionList.get(radioButton.getId()).getAmount();
                    paymentDescription = "Payment of " + radioButton.getText();
                    optionId = featureItemsOptionList.get(radioButton.getId()).getId();
                    //Toast.makeText(FeatureItemPaymentActivity.this, paymentDescription+" paymentAmount -> "+paymentAmount, Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void postFeaturePayment(Context context,String optionId, String productId, String payPalId) {
        showProgressBar();
        ItemListPresenter itemListPresenter = new ItemListPresenter();
        itemListPresenter.postFeatureItemPayment(context, new PresenterCallBack<FeaturedOptionModel>() {
            @Override
            public void onSuccess(FeaturedOptionModel object) {
                hideProgressBar();
                showPositiveAlert(FeatureItemPaymentActivity.this,"Payment Successful");
                /*FeatureDataList[] optionModelList = object.getData();
                seller_item_description.setText(object.getHeader_text());
                for (FeatureDataList featureItem : optionModelList) {
                    featureItemsOptionList.add(featureItem);
                }
                createRadioButton(featureItemsOptionList);*/
            }

            @Override
            public void onFailure() {
                hideProgressBar();
                showPositiveAlert(FeatureItemPaymentActivity.this,"Payment failed");

            }
        },optionId,productId,payPalId);
    }

    private void getPayment() {
        //Getting the amount from editText
        //paymentAmount = editTextAmount.getText().toString();
        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(paymentAmount)), paymentCurrency, paymentDescription,
                PayPalPayment.PAYMENT_INTENT_SALE);

        //Creating Paypal Payment activity intent
        Intent intent = new Intent(this, PaymentActivity.class);

        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        //Puting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        //Starting the intent activity for result
        //the request code will be used on the method onActivityResult
        startActivityForResult(intent, PayPalConfig.PAYPAL_REQUEST_CODE);
    }

    //Paypal Configuration Object
    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);

    private void startPayPalService() {
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //If the result is from paypal
        if (requestCode == PayPalConfig.PAYPAL_REQUEST_CODE) {
            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {
                //Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                //if confirmation is not null
                if (confirm != null) {
                    try {
                        //Getting the payment details
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        BlueShakLog.logDebug("paymentExample", paymentDetails);
                        Gson gson = new Gson();
                        PayPalPaymentModel paymentModel = gson.fromJson(paymentDetails.toString(), PayPalPaymentModel.class);

                        if(paymentModel.getResponse().getState().equalsIgnoreCase("approved")){
                            postFeaturePayment(FeatureItemPaymentActivity.this,optionId,mProductId,paymentModel.getResponse().getId());
                        }else{
                            showPositiveAlert(this,"Failed by PayPal, Please try again...");
                        }

                        //Starting a new activity for the payment details and also putting the payment details with intent
                        /*startActivity(new Intent(this, ConfirmationActivity.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", paymentAmount));*/

                    } catch (JSONException e) {
                        BlueShakLog.logError("paymentExample", "an extremely unlikely failure occurred: " + e.getLocalizedMessage());
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                BlueShakLog.logError("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                BlueShakLog.logError("paymentExample",
                        "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }

    private void setBitmapImages(ArrayList<CreateImageModel> imageList, ImageView imageView, int position) {

        BlueShakLog.logDebug(TAG, "setBitmapImages imagelist size -> " + imageList.size());
        DisplayImageOptions GALLERY = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .considerExifParams(true)
                .cacheOnDisk(true)
                .showImageForEmptyUri(R.drawable.placeholder_background)
                .showImageOnFail(R.drawable.placeholder_background)
                .showImageOnLoading(R.drawable.placeholder_background).build();

        setBitmapDecodedImage(position, imageView, GALLERY, imageList);
    }

    private void setBitmapDecodedImage(int position, ImageView imageView, DisplayImageOptions GALLERY,
                                       ArrayList<CreateImageModel> imageList) {
        String decodedImgUri;
        if (imageList.get(position).getImage().contains("http")) {
            decodedImgUri = imageList.get(position).getImage();
        } else {
            decodedImgUri = Uri.fromFile(new File(imageList.get(position).getImage())).toString();
        }
        ImageLoader.getInstance().displayImage(decodedImgUri, imageView, GALLERY, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
                //.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                BlueShakLog.logDebug(TAG, "setBitmapDecodedImage  onLoadingFailed ");
                //imageDelete.setVisibility(View.GONE);
                // progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                BlueShakLog.logDebug(TAG, "setBitmapDecodedImage  onLoadingComplete ");
                /*imageDelete.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);*/
            }

            @Override
            public void onLoadingCancelled(String s, View view) {
                BlueShakLog.logDebug(TAG, "setBitmapDecodedImage  onLoadingCancelled ");
                /*imageDelete.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);*/
            }
        });
    }

    public void showProgressBar() {
        if (progress_bar != null)
            progress_bar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        if (progress_bar != null)
            progress_bar.setVisibility(View.GONE);
    }

    public void showPositiveAlert(final Activity activity,String message) {
        final com.blueshak.app.blueshak.view.AlertDialog alertDialog = new com.blueshak.app.blueshak.view.AlertDialog(activity);
        alertDialog.setTitle(activity.getString(R.string.app_name));
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
               // activity.setResult(activity.RESULT_CANCELED);
               // activity.finish();
            }
        });
        alertDialog.show();
    }
}
