package com.langoor.app.blueshak.Messaging.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.langoor.app.blueshak.MainActivity;
import com.langoor.app.blueshak.reviews_rating.ReviewsRatings;
import com.langoor.app.blueshak.services.model.ProfileDetailsModel;
import com.langoor.blueshak.R;
import com.google.gson.Gson;
import com.langoor.app.blueshak.AppController;
import com.langoor.app.blueshak.Messaging.adapter.MessageListAdapter;
import com.langoor.app.blueshak.Messaging.data.ChatRoom;
import com.langoor.app.blueshak.Messaging.data.Message;
import com.langoor.app.blueshak.Messaging.data.User;
import com.langoor.app.blueshak.Messaging.manager.MessageManager;

import com.langoor.app.blueshak.Messaging.manager.UserManager;
import com.langoor.app.blueshak.Messaging.util.CommonUtil;
import com.langoor.app.blueshak.PickLocation;
import com.langoor.app.blueshak.bookmarks.BoomarkItemListAdapter;
import com.langoor.app.blueshak.global.GlobalFunctions;
import com.langoor.app.blueshak.global.GlobalVariables;
import com.langoor.app.blueshak.item.ProductDetail;
import com.langoor.app.blueshak.login.LoginActivity;
import com.langoor.app.blueshak.report.ReportListing;
import com.langoor.app.blueshak.root.RootActivity;
import com.langoor.app.blueshak.services.ServerResponseInterface;
import com.langoor.app.blueshak.services.ServicesMethodsManager;
import com.langoor.app.blueshak.services.model.BookmarkListModel;
import com.langoor.app.blueshak.services.model.ContactsListModel;
import com.langoor.app.blueshak.services.model.CreateMessageModel;
import com.langoor.app.blueshak.services.model.ErrorModel;
import com.langoor.app.blueshak.services.model.MessageConversationModel;
import com.langoor.app.blueshak.services.model.MessageModel;
import com.langoor.app.blueshak.services.model.ProductModel;
import com.langoor.app.blueshak.services.model.SalesModel;
import com.langoor.app.blueshak.services.model.StatusModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ChatActivity extends RootActivity implements  MessageManager.MessageListener,View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "ChatActivity";
    private static Context context;
    private static Activity activity;
    private static final String CHAT_ACTIVITY_CONVERSATION_BUNDLE_KEY_POSITION = "ChatRoomModelBundleKey";
    private static final String CHAT_ACTIVITY_USER_BUNDLE_KEY_POSITION = "UserModelBundleKey";
    private String conversation_id = null;
    private MessageConversationModel messageConversationModel = new MessageConversationModel();
    private ArrayList<MessageModel> messages = new ArrayList<MessageModel>();
    private RecyclerView recyclerView;
    private MessageListAdapter adapter;
    private Toolbar toolbar;
    private TextView no_messages, product_name, product_price,close_button;
    private CreateMessageModel createMessageModel = new CreateMessageModel();
    private EditText mInputMessage;
    private User user = null;
    private ImageView product_image, camera,go_back;
    private String active_tab;
    private CardView product_content;
    private Button send;
    private Dialog alert;
    private static final int REQUEST_CAMERA = 11;
    private static final int PICK_PHOTO_FOR_AVATAR = 12;
    private ProductModel productModel = new ProductModel();
    private SalesModel salesModel = new SalesModel();
    private SwipeRefreshLayout swipeRefreshLayout;
    private Handler handler=new Handler();
    protected static final int REQUEST_CHECK_CAMERA = 211;
    protected static final int REQUEST_CHECK_GALLARY = 213;
    private String id=null;
    private ProgressBar progress_bar;
    private ProfileDetailsModel profileDetailsModel=new ProfileDetailsModel();
    private boolean hide_report=false;
    public static Intent newInstance(Context context, User user) {
        Intent mIntent = new Intent(context, ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(CHAT_ACTIVITY_USER_BUNDLE_KEY_POSITION, user);
        mIntent.putExtras(bundle);
        return mIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_activty);
        try{
            context = this;
            activity = this;
            progress_bar=(ProgressBar)findViewById(R.id.progress_bar);
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            MessageManager.getInstance(this).setMessageListener(this);
            swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
            swipeRefreshLayout.setOnRefreshListener(this);
            final SwipeRefreshLayout.OnRefreshListener swipeRefreshListner = new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    Log.i(TAG, "onRefresh called from SwipeRefreshLayout");

                }
            };
            swipeRefreshLayout.setColorSchemeColors(context.getResources().getColor(R.color.brandColor),
                    context.getResources().getColor(R.color.tab_selected),
                    context.getResources().getColor(R.color.darkorange),
                    context.getResources().getColor(R.color.brandColor));

            product_content = (CardView) findViewById(R.id.product_content);
            product_name = (TextView) findViewById(R.id.product_name);
            product_price = (TextView) findViewById(R.id.product_price);
            product_image = (ImageView) findViewById(R.id.product_image);
            no_messages = (TextView) findViewById(R.id.empty);
            camera = (ImageView) findViewById(R.id.camera);
            camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAlert("");
                }
            });
            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            send = (Button) findViewById(R.id.send);
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SendMessage();
                }
            });
            mInputMessage = (EditText) findViewById(R.id.input);
            adapter = new MessageListAdapter(context, messages);
            LinearLayoutManager linearLayoutManagerVertical =
                    new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            linearLayoutManagerVertical.setStackFromEnd(true);
           /* linearLayoutManagerVertical.setReverseLayout(true);*/
            recyclerView.setLayoutManager(linearLayoutManagerVertical);
            recyclerView.addItemDecoration(new SpacesItemDecoration(
                    getResources().getDimensionPixelSize(R.dimen.space)));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);

            if(getIntent().hasExtra(CHAT_ACTIVITY_USER_BUNDLE_KEY_POSITION))
                user = (User) getIntent().getExtras().getSerializable(CHAT_ACTIVITY_USER_BUNDLE_KEY_POSITION);

            LayoutInflater inflator = LayoutInflater.from(this);
            View v = inflator.inflate(R.layout.action_bar_titlel, null);
            ((TextView)v.findViewById(R.id.title)).setText(user.getName());
            toolbar.addView(v);
            toolbar.setOverflowIcon(ContextCompat.getDrawable(context,R.drawable.ic_more_horiz_black_24dp));
            close_button=(TextView)v.findViewById(R.id.cancel);
            close_button.setVisibility(View.GONE);
            close_button.setText("Report");
            close_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent report_listing= ReportListing.newInstance(context,id,user.is_sale()?false:true);
                    startActivity(report_listing);
                }
            });

            go_back=(ImageView)findViewById(R.id.go_back);
            go_back.setVisibility(View.VISIBLE);
            go_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 /*   finish();*/
                    if(MainActivity.is_active)
                        closeThisActivity();
                    else{
                        closeThisActivity();
                        startActivity(new Intent(activity,MainActivity.class));
                    }
                }
            });
            mInputMessage.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                  /*  camera.setVisibility(View.GONE);*/
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                   /* camera.setVisibility(View.GONE);*/
                    if (s.length() == 0 || s.toString().isEmpty())
                        mInputMessage.setVisibility(View.VISIBLE);
                }
            });
            mInputMessage.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    v.setFocusable(true);
                    v.setFocusableInTouchMode(true);
                    return false;
                }
            });
              product_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = ProductDetail.newInstance(context, productModel, null, GlobalVariables.TYPE_MY_SALE);
                    context.startActivity(intent);
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

    private void setThisPage() {
        UserManager.getInstance(this).setTargetUser(user);
        String avatar = user.getProduct_url();
        String item_name = user.getProduct_name();
        String item_price = user.getPrice();
        String item_currency = user.getCurrency();
        profileDetailsModel.setName(user.getName());
        profileDetailsModel.setAvatar(user.getProfileImageUrl());
        profileDetailsModel.setId(user.getBs_id());
        productModel.setId(user.getProduct_id());
        if(user.is_sale())
            id=user.getSale_id();
        else
            id=user.getProduct_id();
        try {
            if (id != null)
                if (Integer.parseInt(id) == 0){
                    hide_report=true;
                    close_button.setVisibility(View.GONE);
                }

        }catch (NumberFormatException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(item_name) || TextUtils.isEmpty(item_price) || TextUtils.isEmpty(avatar))
            product_content.setVisibility(View.GONE);
        else {
            ImageLoader imageLoader = ImageLoader.getInstance();
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisc(true).resetViewBeforeLoading(true)
                    .showImageForEmptyUri(R.drawable.placeholder_background)
                    .showImageOnFail(R.drawable.placeholder_background)
                    .showImageOnLoading(R.drawable.placeholder_background).build();
            //download and display image from url
            imageLoader.displayImage(avatar,product_image, options);

            /*if (!TextUtils.isEmpty(avatar)) {
                Picasso.with(activity)
                        .load(avatar)
                        .placeholder(R.drawable.placeholder_background)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .fit().centerCrop()
                        .into(product_image);
            }*/
            product_name.setText(item_name);
            product_price.setText(GlobalFunctions.getFormatedAmount(item_currency,item_price));

        }
        active_tab = user.getActive_tab();
        conversation_id = user.getConversation_id();
        if(GlobalFunctions.is_loggedIn(context)){
            getConversationMessages(context, conversation_id, active_tab);
            if (adapter.getItemCount() > 1) {
                // scrolling to bottom of the recycler view
                recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, adapter.getItemCount() - 1);
            }
        }else
            showSettingsAlert();

    }

    private void getConversationMessages(final Context context, String conversation_id, String active_tab) {
       showProgressBar();
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getConversationMessages(context, new MessageConversationModel(), conversation_id, active_tab, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                hideProgressBar();
                swipeRefreshLayout.setRefreshing(false);
                if (arg0 instanceof StatusModel) {
                    StatusModel statusModel = (StatusModel) arg0;
                    no_messages.setVisibility(View.VISIBLE);

                } else if (arg0 instanceof ErrorModel) {
                    ErrorModel errorModel = (ErrorModel) arg0;
                    String msg = errorModel.getError() != null ? errorModel.getError() : errorModel.getMessage();
                    no_messages.setVisibility(View.VISIBLE);

                } else if (arg0 instanceof MessageConversationModel) {
                    MessageConversationModel model = (MessageConversationModel) arg0;
                    Log.d(TAG, "ContactsListModel###########" + model.toString());
                    setValues(model);
                }
            }

            @Override
            public void OnFailureFromServer(String msg) {
                hideProgressBar();
                swipeRefreshLayout.setRefreshing(false);
                Log.d(TAG, "OnFailureFromServer########" + msg);
                no_messages.setVisibility(View.VISIBLE);

            }

            @Override
            public void OnError(String msg) {
                hideProgressBar();
                swipeRefreshLayout.setRefreshing(false);
                Log.d(TAG, "OnFailureFromServer########" + msg);
                no_messages.setVisibility(View.VISIBLE);

            }
        }, "Get Bookmarks");

    }

    private void    setValues(MessageConversationModel model) {
        if (model != null) {
            messageConversationModel = model;
            List<MessageModel> messageModels = model.getConversations_messages();
            if (messageModels != null) {
                if (messageModels.size() > 0 && recyclerView != null && messageModels != null && adapter != null) {
                    messages.clear();
                    messages.addAll(messageModels);
                    Collections.reverse(messages);
                    refreshList();
                    if (adapter.getItemCount() > 1) {
                        recyclerView.getLayoutManager().scrollToPosition(adapter.getItemCount() - 1);
                    }
                }else
                    no_messages.setVisibility(View.VISIBLE);
            }
        }else
            no_messages.setVisibility(View.VISIBLE);

    }

    public void createMessage(final  String message) {
        showProgressBar();
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.sendMessage(context, createMessageModel, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                hideProgressBar();
                /*GlobalFunctions.hideProgress();*/
                if (arg0 instanceof StatusModel) {
                    StatusModel statusModel = (StatusModel) arg0;
                } else if (arg0 instanceof MessageModel) {
                    Log.d(TAG,"OnSuccessFromServer");
                    hideProgressBar();
                    MessageModel messageModel=(MessageModel)arg0;
                    messageModel.setIs_sent_by_you(true);
                    adapter.insertMessage(messageModel);
                    if (adapter.getItemCount() > 1) {
                        recyclerView.getLayoutManager().scrollToPosition(adapter.getItemCount() - 1);
                    }
                    /* sendPushwooshMessage(messageModel,message);*/
                }else if (arg0 instanceof ErrorModel) {
                    ErrorModel errorModel = (ErrorModel) arg0;
                    String msg = errorModel.getError() != null ? errorModel.getError() : errorModel.getMessage();

                }
            }

            @Override
            public void OnFailureFromServer(String msg) {
                hideProgressBar();

            }

            @Override
            public void OnError(String msg) {
                hideProgressBar();

            }
        }, "Add Bookmarks");


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

    @Override
    public void onClick(View viewClicked) {
        switch (viewClicked.getId()) {
            case R.id.send:
                SendMessage();
                break;
          /*  case R.id.camera:
                showAlert("");
                break;*/

        }
    }

    private void SendMessage() {
        String message="";
        if (TextUtils.isEmpty(mInputMessage.getText().toString().trim()))
            return;
        message=mInputMessage.getText().toString().trim();
        if (GlobalFunctions.isNetworkAvailable(this)) {
            createMessageModel.setConversation_id(conversation_id);
            createMessageModel.setMessage_type(GlobalVariables.TYPE_TEXT);
            createMessageModel.setMessage(message);
            createMessageModel.setSend_to(user.getBs_id());
            createMessageModel.setProduct_id(user.getProduct_id());
            createMessage(message);
          /*  MessageModel messageModel = new MessageModel();
            messageModel.toObject(createMessageModel);
            adapter.insertMessage(messageModel);*/
            /*Don't hide the keyboard once the message has been sent*/
          /*  final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(mInputMessage.getWindowToken(), 0);
         */
            mInputMessage.setText("");
            no_messages.setVisibility(View.GONE);
            if (adapter.getItemCount() > 1) {
                // scrolling to bottom of the recycler view
                recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, adapter.getItemCount() - 1);
            }
        } else {
            Snackbar.make(no_messages, "Please check your internet connection", Snackbar.LENGTH_LONG)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {

                        }
                    }).show();
        }

    }

    private void SendImage(String base_64) {
        if (GlobalFunctions.isNetworkAvailable(this)) {
            showProgressBar();
            createMessageModel.setConversation_id(conversation_id);
            createMessageModel.setMessage_type(GlobalVariables.TYPE_IMAGE);
            createMessageModel.setImage(base_64);
            createMessageModel.setSend_to(user.getBs_id());
            createMessageModel.setProduct_id(user.getProduct_id());
            createMessage("Image");
          /*  MessageModel messageModel = new MessageModel();
            messageModel.toObject(createMessageModel);
            adapter.insertMessage(messageModel);*/
           /* final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(mInputMessage.getWindowToken(), 0);
          */
            mInputMessage.setText("");
            no_messages.setVisibility(View.GONE);
            if (adapter.getItemCount() > 1) {
                // scrolling to bottom of the recycler view
                recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, adapter.getItemCount() - 1);
            }
        } else {
            Snackbar.make(no_messages, "Please check your internet connection", Snackbar.LENGTH_LONG)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            createMessage("Image");
                        }
                    }).show();
        }

    }

    void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final CharSequence[] charSequences = new CharSequence[]{"Click a picture from camera", "Choose from gallery"};
        builder.setItems(R.array.select_dialog_items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){
                    if(checkIfAlreadyhavePermission())
                        cameraIntent();
                    else
                        checkCameraPermission();
                }else{
                    if(checkIfReadExternalStorageAlreadyhavePermission())
                        pickImage();
                    else
                        checkReadExternalStoragePermission();

                }
            }
        });
        alert = builder.create();
        alert.show();

    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    return;
                }
                try {
                    InputStream inputStream = getApplicationContext().getContentResolver().openInputStream(data.getData());
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                    Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);
                 /*   camera.setVisibility(View.VISIBLE);*/
                   /* setProfile(bmp);
                    toolbarImage.setImageBitmap(bmp);
                 */
                    String base64_image = CommonUtil.encodeToBase64(bmp, Bitmap.CompressFormat.JPEG, 100);

                    if (!TextUtils.isEmpty(base64_image)) {
                        SendImage(base64_image);
                        // setProfileImage(base64_image);
                    } else {
                        Toast.makeText(this, "Your profile pic is empty can't update the your profile", Toast.LENGTH_LONG).show();
                    }
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_CAMERA) {

                onCaptureImageResult(data);
            }
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

    private void onCaptureImageResult(Intent data) {
        /*camera.setVisibility(View.VISIBLE);*/
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
       /* ivImage.setImageBitmap(thumbnail);*/
        SendImage(CommonUtil.BitMapToString(thumbnail));
    }
    @Override
    public void onResume() {
        try{
            Log.d(TAG, "OnResume");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (user != null) {
                        setThisPage();
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

    super.onResume();
    }

    @Override
    public void onRefresh() {
        if(GlobalFunctions.is_loggedIn(context)){
            getConversationMessages(context, conversation_id, active_tab);
        }else
            showSettingsAlert();

    }

    @Override
    public void onReceiveMessage(final Message receivedMessage) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (adapter != null && Integer.parseInt(receivedMessage.getSender().getBs_id()) == Integer.parseInt(UserManager.getInstance(getApplicationContext()).getTargetUser().getBs_id())
                        &&Integer.parseInt(receivedMessage.getSender().getConversation_id()) == Integer.parseInt(UserManager.getInstance(getApplicationContext()).getTargetUser().getConversation_id())) {
                    if(GlobalFunctions.is_loggedIn(context)){
                        getConversationMessages(context,receivedMessage.getSender().getConversation_id(),
                                receivedMessage.getSender().getActive_tab());
                    }else
                        showSettingsAlert();
                }
            }
        });

    }
    @Override
    public void onSendMessageResult(boolean isSuccess, String extraInfo)
    {
        if(!isSuccess)
            Toast.makeText(this, "Unable to send message", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "Message sent", Toast.LENGTH_SHORT).show();
    }
    public void showProgress(){
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                    }
                                }
        );
    }
    public void sendPushwooshMessage(MessageModel messageModel,String Message){
        Message messageToSend;
        messageModel.setIs_sent_by_you(true);
        adapter.insertMessage(messageModel);
        GlobalFunctions.hideProgress();
        if(messageModel.getMessage_type().equalsIgnoreCase(GlobalVariables.TYPE_IMAGE)){
             messageToSend = new Message(
                    Message,
                    GlobalFunctions.getSingedUser(getApplicationContext()),
                    new Date(), "", "",
                    user,GlobalVariables.TYPE_IMAGE,messageModel.getImage());

        }else{
             messageToSend = new Message(
                    Message,
                    GlobalFunctions.getSingedUser(getApplicationContext()),
                    new Date(), "", "",
                    user,GlobalVariables.TYPE_TEXT,"");
            messageToSend.setType(GlobalVariables.TYPE_TEXT);
        }
        MessageManager.getInstance(context).sendMessage(messageToSend, user.getBs_id());
        if (adapter.getItemCount() > 1) {
            recyclerView.getLayoutManager().scrollToPosition(adapter.getItemCount() - 1);
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
    public void checkCameraPermission() {
        Log.d(TAG,"checkCameraPermission######################");
        int permissionCheck_location_coarse = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        if(permissionCheck_location_coarse != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, REQUEST_CHECK_CAMERA);
        }

    }
    private boolean checkIfAlreadyhavePermission() {
        int coarse_location = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (coarse_location == PackageManager.PERMISSION_GRANTED && result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d(TAG,"onRequestPermissionsResult##############");
        switch (requestCode) {
            case REQUEST_CHECK_CAMERA:{
                Log.d(TAG,"onRequestPermissionsResult##############REQUEST_CAMERA");
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    cameraIntent();
                } else {
                    checkCameraPermission();
                }
                return;
            }

            case REQUEST_CHECK_GALLARY:{
                Log.d(TAG,"onRequestPermissionsResult##############REQUEST_CHECK_GALLARY");
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImage();
                } else {
                    checkReadExternalStoragePermission();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    public void checkReadExternalStoragePermission() {
        Log.d(TAG,"checkReadExternalStoragePermission######################");
        int permissionCheck_location_coarse = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCheck_write_coarse = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(permissionCheck_location_coarse != PackageManager.PERMISSION_GRANTED &&
                permissionCheck_write_coarse != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CHECK_GALLARY);
        }

    }
    private boolean checkIfReadExternalStorageAlreadyhavePermission() {
        Log.d(TAG,"checkIfReadExternalStorageAlreadyhavePermission######################");
        int coarse_location = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (coarse_location == PackageManager.PERMISSION_GRANTED && result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    public void showSettingsAlert(){
        final com.langoor.app.blueshak.view.AlertDialog alertDialog = new com.langoor.app.blueshak.view.AlertDialog(context);
        alertDialog.setMessage("Please Login/Register to continue");
        alertDialog.setTitle("Login/Register");
        alertDialog.setPositiveButton("Continue", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent creategarrage =new Intent(activity,LoginActivity.class);
                startActivity(creategarrage);
                closeThisActivity();
            }
        });
        alertDialog.show();
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
    @Override
    public void onBackPressed() {
        if(MainActivity.is_active)
            closeThisActivity();
        else{
            closeThisActivity();
            startActivity(new Intent(activity,MainActivity.class));
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.message_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.message_menu, menu);
        if(hide_report)
            menu.findItem(R.id.report).setVisible(false);


        // setOptionsMenuVisiblity(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.write_review) {
           proceedToReviews();
            return true;
        }else if(item.getItemId() == R.id.report) {
            Intent report_listing= ReportListing.newInstance(context,id,user.is_sale()?false:true);
            startActivity(report_listing);
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }
    public void proceedToReviews() {
        Intent intent= ReviewsRatings.newInstance(activity,null,null,profileDetailsModel,null);
        startActivity(intent);
    }
}