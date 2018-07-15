package com.blueshak.app.blueshak.item;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blueshak.app.blueshak.seller.model.SalesDetailsProduct;
import com.crashlytics.android.Crashlytics;
import com.blueshak.blueshak.R;
import com.blueshak.app.blueshak.Messaging.activity.ChatActivity;
import com.blueshak.app.blueshak.Messaging.data.Message;
import com.blueshak.app.blueshak.Messaging.data.User;
import com.blueshak.app.blueshak.Messaging.manager.MessageManager;
import com.blueshak.app.blueshak.global.GlobalFunctions;
import com.blueshak.app.blueshak.global.GlobalVariables;
import com.blueshak.app.blueshak.root.RootActivity;
import com.blueshak.app.blueshak.services.ServerResponseInterface;
import com.blueshak.app.blueshak.services.ServicesMethodsManager;
import com.blueshak.app.blueshak.services.model.ErrorModel;
import com.blueshak.app.blueshak.services.model.MakeOfferModel;
import com.blueshak.app.blueshak.services.model.ProductModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Date;

public class MakeOffersActivty extends RootActivity {
    private static final String TAG = "ImageViewActivty";
    private static Context context;
    private static Activity activity;
    private TextView offer_text, close_button;
    private ImageView product_image, go_back;
    private EditText offer_price;
    private MakeOfferModel makeOfferModel = new MakeOfferModel();
    private Button make_offer;
    private static final String PRODUCTDETAIL_BUNDLE_KEY_POSITION = "ProductModelWithDetails";
    private static final String PRODUCTDETAIL_BUNDLE_KEY_FLAG = "ProductModelWithFlag";
    private ProductModel productModel = null;
    private String seller_phone_number = null;
    private String seller_name = null;
    private String seller_profile_image = null;
    private String item_image = null;
    private String seller_user_id = null, item_name = null, item_price = null;
    private User user = new User();
    private ProgressBar progress_bar;
    private static final String SALES_PRODUCT_DETAILS = "SalesProductDetails";
    private SalesDetailsProduct salesProductModel = null;

    public static Intent newInstance(Context context, ProductModel product) {
        Intent mIntent = new Intent(context, MakeOffersActivty.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(PRODUCTDETAIL_BUNDLE_KEY_POSITION, product);
        mIntent.putExtras(bundle);
        return mIntent;
    }

    public static Intent newInstance(Context context, SalesDetailsProduct saleProduct) {
        Intent mIntent = new Intent(context, MakeOffersActivty.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(SALES_PRODUCT_DETAILS, saleProduct);
        mIntent.putExtras(bundle);
        return mIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_offers_activty);
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            context = this;
            activity = this;
            LayoutInflater inflator = LayoutInflater.from(this);
            View v = inflator.inflate(R.layout.action_bar_titlel, null);
            ((TextView) v.findViewById(R.id.title)).setText("Make Offer");
            toolbar.addView(v);
            close_button = (TextView) v.findViewById(R.id.cancel);
            close_button.setVisibility(View.GONE);
            go_back = (ImageView) findViewById(R.id.go_back);
            go_back.setVisibility(View.VISIBLE);
            go_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
            offer_price = (EditText) findViewById(R.id.offer_price);
            offer_price.addTextChangedListener(new TextWatcher() {
                public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                    String text = arg0.toString();
                    if (text.contains(".") && text.substring(text.indexOf(".") + 1).length() > 2) {
                        offer_price.setText(text.substring(0, text.length() - 1));
                        offer_price.setSelection(offer_price.getText().length());
                    }
                }

                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

                }

                public void afterTextChanged(Editable arg0) {
                }
            });
            make_offer = (Button) findViewById(R.id.make_offer_);
            offer_text = (TextView) findViewById(R.id.offer_text);
            product_image = (ImageView) findViewById(R.id.product_image);
            productModel = (ProductModel) getIntent().getExtras().getSerializable(PRODUCTDETAIL_BUNDLE_KEY_POSITION);
            salesProductModel = (SalesDetailsProduct) getIntent().getExtras().getSerializable(SALES_PRODUCT_DETAILS);
            if (productModel != null){
                setThisPage(true);
                offer_price.setHint(productModel.getCurrency());
            }else if(salesProductModel!=null){
                setThisPage(false);
                offer_price.setHint(salesProductModel.getCurrency());
            }


            make_offer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(offer_price.getText().toString()))
                        if (productModel != null) {
                            makeOfferModel.setProduct_id(productModel.getId());
                            makeOfferModel.setOffer(offer_price.getText().toString());
                            makeOffer(makeOfferModel);
                        } else if (salesProductModel != null) {
                            makeOfferModel.setProduct_id(salesProductModel.getProductId());
                            makeOfferModel.setOffer(offer_price.getText().toString());
                            makeOffer(makeOfferModel);
                        } else {
                            Toast.makeText(context, "Please Enter your offer", Toast.LENGTH_LONG).show();
                        }

                }
            });
            make_offer.setOnEditorActionListener(new DoneOnEditorActionListener());
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
            Crashlytics.log(e.getMessage());
        }
    }

    public void setThisPage(boolean isProduct) {
        String image_url = "";
        if(isProduct){
            if (productModel.getImage().size() > 0)
                image_url = productModel.getImage().get(0);
            item_name = productModel.getName();
            item_price = productModel.getSalePrice();
            seller_name = productModel.getSellerName();
            seller_phone_number = productModel.getSeller_phone();
            seller_profile_image = productModel.getSeller_image();
            seller_user_id = productModel.getSeller_id();
            user.setName(seller_name);
            user.setCurrency(productModel.getCurrency());
            user.setIs_sale(false);
            user.setBs_id(seller_user_id);
            user.setNumber(seller_phone_number);
            user.setProfileImageUrl(seller_profile_image);
            user.setProduct_name(item_name);
            user.setPrice(item_price);
            user.setProduct_url(image_url);
            user.setProduct_id(productModel.getId());

            item_image = image_url;
            offer_text.setText("Enter your offer for " + productModel.getName());
            ImageLoader imageLoader = ImageLoader.getInstance();
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisc(true).resetViewBeforeLoading(true)
                    .showImageForEmptyUri(R.drawable.placeholder_background)
                    .showImageOnFail(R.drawable.placeholder_background)
                    .showImageOnLoading(R.drawable.placeholder_background).build();

            imageLoader.displayImage(item_image, product_image, options);
        }else{
            image_url = salesProductModel.getImage();
            item_name = salesProductModel.getProductName();
            item_price = salesProductModel.getSalePrice();
            //seller_name = salesProductModel.getSellerName();
            //seller_phone_number = salesProductModel.getSeller_phone();
            seller_profile_image = salesProductModel.getImage();
            seller_user_id = salesProductModel.getSaleId();
            user.setName(seller_name);
            user.setCurrency(salesProductModel.getCurrency());
            user.setIs_sale(false);
            user.setBs_id(seller_user_id);
            user.setNumber(seller_phone_number);
            user.setProfileImageUrl(seller_profile_image);
            user.setProduct_name(item_name);
            user.setPrice(item_price);
            user.setProduct_url(image_url);
            user.setProduct_id(salesProductModel.getProductId());

            item_image = image_url;
            offer_text.setText("Enter your offer for " + salesProductModel.getProductName());
            ImageLoader imageLoader = ImageLoader.getInstance();
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisc(true).resetViewBeforeLoading(true)
                    .showImageForEmptyUri(R.drawable.placeholder_background)
                    .showImageOnFail(R.drawable.placeholder_background)
                    .showImageOnLoading(R.drawable.placeholder_background).build();

            imageLoader.displayImage(item_image, product_image, options);
        }

    }

    private void makeOffer(final MakeOfferModel makeOfferModel) {
        final String email_verification_error = context.getResources().getString(R.string.ErrorEmailVerification);
        showProgressBar();
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.makeOffer(context, makeOfferModel, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                hideProgressBar();
                Log.d(TAG, "onSuccess Response");
                if (arg0 instanceof MakeOfferModel) {
                    MakeOfferModel makeOfferModel1 = (MakeOfferModel) arg0;
                    user.setConversation_id(makeOfferModel1.getConversation_id());
                    user.setActive_tab(makeOfferModel1.getActive_tab());
                   /* pushNotification(offerText);*/
                    Intent i = ChatActivity.newInstance(context, user);
                    startActivity(i);
                    finish();
                } else if (arg0 instanceof ErrorModel) {
                    ErrorModel errorModel = (ErrorModel) arg0;
                    String msg = errorModel.getError() != null ? errorModel.getError() : errorModel.getMessage();
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void OnFailureFromServer(String msg) {
                hideProgressBar();
                Log.d(TAG, msg);
                if (msg != null) {
                    if (msg.equalsIgnoreCase(email_verification_error))
                        GlobalFunctions.showEmailVerificatiomAlert(context);
                    else
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void OnError(String msg) {
                hideProgressBar();
                Log.d(TAG, msg);
                if (msg != null) {
                    if (msg.equalsIgnoreCase(email_verification_error))
                        GlobalFunctions.showEmailVerificatiomAlert(context);
                    else
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                }
            }
        }, "Make  Offer");

    }

    class DoneOnEditorActionListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (!TextUtils.isEmpty(offer_price.getText().toString()))
                    if (productModel != null) {
                        makeOfferModel.setProduct_id(productModel.getId());
                        makeOfferModel.setOffer(offer_price.getText().toString());
                        makeOffer(makeOfferModel);
                    } else if(salesProductModel!=null){
                        makeOfferModel.setProduct_id(salesProductModel.getProductId());
                        makeOfferModel.setOffer(offer_price.getText().toString());
                        makeOffer(makeOfferModel);
                    }else{
                        Toast.makeText(context, "Please Enter your offer", Toast.LENGTH_LONG).show();
                    }

                return true;
            }
            return false;
        }
    }

    public void pushNotification(String offer) {
        Message messageToSend = new Message(
                offer,
                GlobalFunctions.getSingedUser(getApplicationContext()),
                new Date(), "", "",
                user, GlobalVariables.TYPE_TEXT, "");
        messageToSend.setType(GlobalVariables.TYPE_TEXT);
        MessageManager.getInstance(context).sendMessage(messageToSend, user.getBs_id());
    }

    public void showProgressBar() {
        if (progress_bar != null)
            progress_bar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        if (progress_bar != null)
            progress_bar.setVisibility(View.GONE);
    }
}
