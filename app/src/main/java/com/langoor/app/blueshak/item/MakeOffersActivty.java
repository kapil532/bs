package com.langoor.app.blueshak.item;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.langoor.blueshak.R;
import com.langoor.app.blueshak.Messaging.activity.ChatActivity;
import com.langoor.app.blueshak.Messaging.data.Message;
import com.langoor.app.blueshak.Messaging.data.User;
import com.langoor.app.blueshak.Messaging.manager.MessageManager;
import com.langoor.app.blueshak.global.GlobalFunctions;
import com.langoor.app.blueshak.global.GlobalVariables;
import com.langoor.app.blueshak.root.RootActivity;
import com.langoor.app.blueshak.services.ServerResponseInterface;
import com.langoor.app.blueshak.services.ServicesMethodsManager;
import com.langoor.app.blueshak.services.model.ErrorModel;
import com.langoor.app.blueshak.services.model.MakeOfferModel;
import com.langoor.app.blueshak.services.model.ProductModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.Date;

public class MakeOffersActivty extends RootActivity {
    private static final String TAG = "ImageViewActivty";
    private static Context context;
    private static Activity activity;
    private TextView offer_text,close_button;
    private ImageView product_image,go_back;
    private EditText offer_price;
    private MakeOfferModel makeOfferModel=new MakeOfferModel();
    private Button make_offer;
    private static final String  PRODUCTDETAIL_BUNDLE_KEY_POSITION = "ProductModelWithDetails";
    private static final String  PRODUCTDETAIL_BUNDLE_KEY_FLAG = "ProductModelWithFlag";
    private ProductModel productModel=null;
    private String seller_phone_number=null;
    private String seller_name=null;
    private String seller_profile_image=null;
    private String item_image=null;
    private String seller_user_id=null,item_name=null,item_price=null;
    private User user=new User();
    private ProgressBar progress_bar;
    public static Intent newInstance(Context context, ProductModel product){
        Intent mIntent = new Intent(context,MakeOffersActivty.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(PRODUCTDETAIL_BUNDLE_KEY_POSITION, product);
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
            context=this;
            activity=this;
            LayoutInflater inflator = LayoutInflater.from(this);
            View v = inflator.inflate(R.layout.action_bar_titlel, null);
            ((TextView)v.findViewById(R.id.title)).setText("Make Offer");
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
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);*/
            progress_bar=(ProgressBar)findViewById(R.id.progress_bar);
            offer_price=(EditText)findViewById(R.id.offer_price);

            make_offer=(Button)findViewById(R.id.make_offer_);
            offer_text=(TextView)findViewById(R.id.offer_text);
            product_image=(ImageView)findViewById(R.id.product_image);
            productModel=(ProductModel)getIntent().getExtras().getSerializable(PRODUCTDETAIL_BUNDLE_KEY_POSITION);
            if(productModel!=null)
                setThisPage();
            offer_price.setHint(productModel.getCurrency());
            make_offer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!TextUtils.isEmpty(offer_price.getText().toString()))
                        if(productModel !=null){
                            makeOfferModel.setProduct_id(productModel.getId());
                            makeOfferModel.setOffer(offer_price.getText().toString());
                            makeOffer(makeOfferModel);


                        }
                        else
                            Toast.makeText(context,"Please Enter your offer",Toast.LENGTH_LONG).show();
                }
            });
            make_offer.setOnEditorActionListener(new DoneOnEditorActionListener());
        }catch (Exception e){
            e.printStackTrace();
            Log.d(TAG,e.getMessage());
            Crashlytics.log(e.getMessage());
        }
    }

    public void setThisPage(){
        String image_url="";
        if(productModel.getImage().size()>0)
            image_url=productModel.getImage().get(0);
        item_name =productModel.getName();
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
        user.setProduct_url(image_url);
        user.setProduct_id(productModel.getId());
     /*   user.setActive_tab(GlobalVariables.TYPE_BUYER_TAB);*/
        item_image=image_url;
        offer_text.setText("Enter your offer for "+productModel.getName());
        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.placeholder_background)
                .showImageOnFail(R.drawable.placeholder_background)
                .showImageOnLoading(R.drawable.placeholder_background).build();
        //download and display image from url
        imageLoader.displayImage(item_image,product_image, options);
    }
    private void makeOffer(final MakeOfferModel makeOfferModel){
        final String email_verification_error=context.getResources().getString(R.string.ErrorEmailVerification);
       showProgressBar();
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.makeOffer(context, makeOfferModel, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
              hideProgressBar();
                Log.d(TAG, "onSuccess Response");
                if(arg0 instanceof MakeOfferModel){
                    MakeOfferModel makeOfferModel1 = (MakeOfferModel) arg0;
                    Log.d(TAG,"##########OnSuccessFromServer##############"+makeOfferModel1.toString()+"###############"+makeOfferModel1.getActive_tab()+makeOfferModel1.getContact_avatar()+makeOfferModel1.getConversation_id());
                    user.setConversation_id(makeOfferModel1.getConversation_id());
                    user.setActive_tab(makeOfferModel1.getActive_tab());
                    String offerText=makeOfferModel1.getContact_name()+" has made an offer $"+makeOfferModel.getOffer()+" for "+productModel.getName();
                   /* pushNotification(offerText);*/
                    Intent i= ChatActivity.newInstance(context,user);
                    startActivity(i);
                    finish();
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
                Log.d(TAG, msg);
                if(msg!=null){
                    if(msg.equalsIgnoreCase(email_verification_error))
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
                if(!TextUtils.isEmpty(offer_price.getText().toString()))
                    if(productModel !=null){
                        makeOfferModel.setProduct_id(productModel.getId());
                        makeOfferModel.setOffer(offer_price.getText().toString());
                        makeOffer(makeOfferModel);
                    }
                    else
                        Toast.makeText(context,"Please Enter your offer",Toast.LENGTH_LONG).show();
                return true;
            }
            return false;
        }
    }
    public void pushNotification(String offer){
        Message messageToSend = new Message(
                offer,
                GlobalFunctions.getSingedUser(getApplicationContext()),
                new Date(), "", "",
                user,GlobalVariables.TYPE_TEXT,"");
        messageToSend.setType(GlobalVariables.TYPE_TEXT);
        MessageManager.getInstance(context).sendMessage(messageToSend,user.getBs_id());
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
