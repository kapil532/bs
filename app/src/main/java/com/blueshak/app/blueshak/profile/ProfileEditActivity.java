package com.blueshak.app.blueshak.profile;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.blueshak.app.blueshak.garage.CreateItemSaleFragment;
import com.blueshak.app.blueshak.garage.GifSizeFilter;
import com.blueshak.app.blueshak.garage.Utils;
import com.blueshak.app.blueshak.services.model.CreateImageModel;
import com.blueshak.app.blueshak.util.BlueShakLog;
import com.hbb20.CountryCodePicker;
import com.blueshak.app.blueshak.otp.OTPActivity;
import com.blueshak.app.blueshak.services.model.OTPCheckerModel;
import com.blueshak.app.blueshak.services.model.VerifyAliasModel;
import com.blueshak.blueshak.R;
import com.blueshak.app.blueshak.MainActivity;
import com.blueshak.app.blueshak.Messaging.util.CommonUtil;
import com.blueshak.app.blueshak.PickLocation;
import com.blueshak.app.blueshak.global.GlobalFunctions;
import com.blueshak.app.blueshak.global.GlobalVariables;
import com.blueshak.app.blueshak.helper.RoundedImageView;
import com.blueshak.app.blueshak.root.RootActivity;
import com.blueshak.app.blueshak.services.ServerResponseInterface;
import com.blueshak.app.blueshak.services.ServicesMethodsManager;
import com.blueshak.app.blueshak.services.model.ForgotPasswordModel;
import com.blueshak.app.blueshak.services.model.LocationModel;
import com.blueshak.app.blueshak.services.model.OTPResendModel;
import com.blueshak.app.blueshak.services.model.ProfileDetailsModel;
import com.blueshak.app.blueshak.services.model.Shop;
import com.blueshak.app.blueshak.services.model.StatusModel;
import com.mvc.imagepicker.ImagePicker;
import com.mvc.imagepicker.ImageUtils;
import com.pushwoosh.PushManager;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.ui.adapter.AlbumMediaAdapter;
import com.zhihu.matisse.ui.MatisseActivity;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ProfileEditActivity extends RootActivity {
    String TAG = "ProfileEditActivity";
    LinearLayout phone_verify_content, email_veirification_content, pass_content;
    RelativeLayout profile_photo_content;
    private boolean mail_verified = false;
    private Uri fileUri;
    private RoundedImageView rounde_image;
    private EditText edit_fullname, edit_mobile, last_name, alias_name;
    private TextView email_verified, shop_tile, close_button, phone_number_verified;
    private TextView edit_address, profile_photo, shop_link;
    private EditText edit_email, shop_name, about_my_shop;
    private Button save_profile;
    private EditText edit_std;
    private TextView location;
    private ImageView go_back;
    static GlobalFunctions globalFunctions = new GlobalFunctions();
    static GlobalVariables globalVariables = new GlobalVariables();
    private static final int REQUEST_CAMERA = 11;
    private static final int PICK_PHOTO_FOR_AVATAR = 12;
    private static final int REQUEST_CODE_CHOOSE = 23;
    private Dialog alert;
    String email;
    private int mImageCount = 1;

    public String getImage_bmp() {
        return image_bmp;
    }

    public void setImage_bmp(String image_bmp) {
        this.image_bmp = image_bmp;
    }

    private String image_bmp;
    static Context context;
    static Activity activity;
    static final String PRODUCTDETAIL_BUNDLE_KEY_POSITION = "ProductModelWithDetails";
    static final String PRODUCTDETAIL_BUNDLE_KEY_FLAG = "ProductModelWithFlag";
    private Switch fb_link;
    CountryCodePicker ccp;
    ProfileDetailsModel profileDetailsModel = new ProfileDetailsModel();
    public static final String PROFILE_ACTIVITY_LOCATION_BUNDLE_KEY = "ProfileLocationBundleKey";
    protected static final int REQUEST_CHECK_CAMERA = 112;
    protected static final int REQUEST_CHECK_GALLARY = 113;
    private ProgressBar progress_bar;

    public static Intent newInstance(Context context, ProfileDetailsModel product) {
        Intent mIntent = new Intent(context, ProfileEditActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(PRODUCTDETAIL_BUNDLE_KEY_POSITION, product);
        mIntent.putExtras(bundle);
        return mIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
      /*  requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
     */
        setContentView(R.layout.activity_profile_edit);
        ImagePicker.setMinQuality(600, 600);
        try {
            progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            LayoutInflater inflator = LayoutInflater.from(this);
            View v = inflator.inflate(R.layout.action_bar_titlel, null);
            shop_tile = (TextView) v.findViewById(R.id.title);
            shop_tile.setText("Edit Profile");
            toolbar.addView(v);
            close_button = (TextView) v.findViewById(R.id.cancel);
            save_profile = (Button) findViewById(R.id.save_profile);
            ccp = (CountryCodePicker) findViewById(R.id.isd_code);
            save_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (edit_fullname.getText().length() == 0) {
                        edit_fullname.setError("Please fill the Name");
                        return;
                    } else if (TextUtils.isDigitsOnly(edit_fullname.getText().toString())) {
                        edit_fullname.setError("Please enter valid Name");
                        return;
                    }
                    if (last_name.getText().length() == 0) {
                        last_name.setError("Please fill the Last Name");
                        return;
                    } else if (TextUtils.isDigitsOnly(last_name.getText().toString())) {
                        last_name.setError("Please enter valid Last Name");
                        return;
                    }
                    if (alias_name.getText().length() == 0) {
                        alias_name.setError("Please fill the User Name");
                        return;
                    } else if (TextUtils.isDigitsOnly(alias_name.getText().toString())) {
                        alias_name.setError("Please enter valid User Name");
                        return;
                    } else if (edit_email.getText().length() == 0) {
                        edit_email.setError("Please fill the email");
                        return;
                    } else if (!isValidEmail(edit_email.getText().toString())) {
                        edit_email.setError("Please Check your Email id!!");
                        return;
                    }/*else if(isd_code.getText().length() == 0) {
                        isd_code.setError("Please fill the ISD code");
                        return;
                    }else if(!isd_code.getText().toString().startsWith("+")){
                        isd_code.setError("Please enter a valid ISD code");
                    }else if(isd_code.getText().toString().startsWith("0")){
                        isd_code.setError("Please enter a valid ISD code");
                    }*/ else if (edit_mobile.getText().length() == 0) {
                        edit_mobile.setError("Please enter a valid Mobile Number");
                        return;
                    } else if (!isValidMobile(edit_mobile.getText().toString())) {
                        edit_mobile.setError("Please enter a valid Mobile Number");
                        return;
                    }/*else if(edit_address.getText().length() == 0) {
                        edit_address.setError("Please enter a valid address");
                        return;
                    }*/ else {
                        verifyUserId(alias_name.getText().toString().trim());
                    }
                }
            });
            close_button.setVisibility(View.GONE);
            go_back = (ImageView) findViewById(R.id.go_back);
            go_back.setVisibility(View.VISIBLE);
            go_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            /*ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setTitle("Edit Profile");
                actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.brandColor)));
            }*/
            context = this;
            activity = this;
            rounde_image = (RoundedImageView) findViewById(R.id.rounde_image);
            edit_fullname = (EditText) findViewById(R.id.edit_fullname);
            last_name = (EditText) findViewById(R.id.last_name);
            alias_name = (EditText) findViewById(R.id.alias_name);
            edit_email = (EditText) findViewById(R.id.edit_email);
            location = (TextView) findViewById(R.id.location);
            edit_mobile = (EditText) findViewById(R.id.edit_mobile);
            edit_address = (TextView) findViewById(R.id.location);
            edit_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    proceedToLocation();
                }
            });
            shop_link = (TextView) findViewById(R.id.shop_link);
            shop_name = (EditText) findViewById(R.id.shop_name);
            about_my_shop = (EditText) findViewById(R.id.about_my_shop);
            email_verified = (TextView) findViewById(R.id.email_verified);
            phone_number_verified = (TextView) findViewById(R.id.phone_number_verified);
            fb_link = (Switch) findViewById(R.id.fb_link);
            fb_link.setClickable(false);
            phone_verify_content = (LinearLayout) findViewById(R.id.phone_verify_content);
            phone_verify_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!profileDetailsModel.is_otp_verified()) {
                        verifyPhoneNumber();
                    } else
                        Toast.makeText(context, "Your phone number is already verified", Toast.LENGTH_SHORT).show();

                }
            });
            email_veirification_content = (LinearLayout) findViewById(R.id.email_veirification_content);
            email_veirification_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!profileDetailsModel.is_email_verify()) {
                        verifyEmail();
                    } else
                        Toast.makeText(context, "Your e-mail is already verified", Toast.LENGTH_SHORT).show();

                }
            });
            pass_content = (LinearLayout) findViewById(R.id.pass_content);
            pass_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showExitAlert();
                }
            });
           /* location_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  *//*  startActivity(new Intent(activity, PickLocation.class));*//*
                    proceedToLocation();
                }
            });*/
            profile_photo_content = (RelativeLayout) findViewById(R.id.profile_photo_content);
            profile_photo_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    showAlert();
                    selectImage(mImageCount);
                }
            });
            profileDetailsModel = (ProfileDetailsModel) getIntent().getExtras().getSerializable(PRODUCTDETAIL_BUNDLE_KEY_POSITION);
            if (profileDetailsModel != null)
                setThisPage();


            rounde_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    //sendThaData();
//                    showAlert();
                    selectImage(mImageCount);
                }
            });
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

   /* private void selectImage() {

        if (checkIfAlreadyhavePermission())
            selectImageNew();
//            ImagePicker.pickImage(this, "Select your image");
        else
            checkCameraPermission();
    }*/

    private void selectImage(int imageCount) {
        MatisseActivity.isProfilePic = true;
        if (checkIfAlreadyhavePermission()) {
            Matisse.from(this)
                    .choose(MimeType.ofImage(), true)
                    .countable(true)
                    .capture(true)
                    .captureStrategy(
                            new com.zhihu.matisse.internal.entity.CaptureStrategy(true, "com.blueshak.fileprovider"))
                    .maxSelectable(imageCount)
                    .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                    .gridExpectedSize(
                            getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                    .thumbnailScale(0.85f)
                    .imageEngine(new GlideEngine())
                    .forResult(REQUEST_CODE_CHOOSE);
        } else {
            checkCameraPermission();
        }

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;

    private void selectImageNew() {
        try {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Take Photo", "Choose From Gallery"};
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(activity);
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            AlbumMediaAdapter.isCameraClick = true;
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, PICK_IMAGE_CAMERA);
                        } else if (options[item].equals("Choose From Gallery")) {
                            AlbumMediaAdapter.isCameraClick = false;
                            dialog.dismiss();
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                        } /*else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }*/
                    }
                });
                builder.show();
            } else
                Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void setThisPage() {
        email = profileDetailsModel.getEmail();
        edit_fullname.setText(profileDetailsModel.getName());
        edit_email.setText(profileDetailsModel.getEmail());
        edit_address.setText(profileDetailsModel.getAddress());
        edit_mobile.setText(profileDetailsModel.getPhone());
        alias_name.setText(profileDetailsModel.getAlias_name());
        last_name.setText(profileDetailsModel.getLast_name());
        ccp.setCountryForPhoneCode(Integer.parseInt(profileDetailsModel.getIsd()));
        String avatar = profileDetailsModel.getAvatar();
      /*  ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(false)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.placeholder_background)
                .showImageOnFail(R.drawable.placeholder_background)
                .showImageOnLoading(R.drawable.placeholder_background).build();
        //download and display image from url
        imageLoader.displayImage(avatar,rounde_image,options);
*/
        if (!TextUtils.isEmpty(avatar)) {
            Picasso.with(activity)
                    .load(avatar)
                    .placeholder(R.drawable.squareplaceholder)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .fit().centerCrop()
                    .into(rounde_image);
        } else {
            rounde_image.setImageResource(R.drawable.squareplaceholder);
        }
        Shop shop = profileDetailsModel.getShop();
        if (shop != null) {
            shop_name.setText(shop.getName());
            shop_link.setText(shop.getShop_link());
            about_my_shop.setText(shop.getDescription());
        }
        if (profileDetailsModel.is_email_verify())
            email_verified.setText("Verified");
        else
            email_verified.setText("Not Verified");

        if (profileDetailsModel.is_otp_verified())
            phone_number_verified.setText("Verified");
        else
            phone_number_verified.setText("Not Verified");

        if (!TextUtils.isEmpty(profileDetailsModel.getFb_id()))
            fb_link.setChecked(true);
    }

    private void sendRequestToServer(final Context context) {
        showProgressBar();
        final String fullname = edit_fullname.getText().toString();
        final String last_name_ = last_name.getText().toString();
        final String alias = alias_name.getText().toString();
        final String email = edit_email.getText().toString();
        final String mobile = edit_mobile.getText().toString();
        final String isd = ccp.getSelectedCountryCodeWithPlus();
        final String address = edit_address.getText().toString();
        final String shp_name = shop_name.getText().toString();
        final String shop_description = about_my_shop.getText().toString();
        final String shp_link = shop_link.getText().toString();
        String country = GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_COUNTRY);
        final String bmp = getImage_bmp();
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        ProfileDetailsModel profileDetailsModel = new ProfileDetailsModel();
        profileDetailsModel.setName(fullname);
        profileDetailsModel.setEmail(email);
        profileDetailsModel.setAlias_name(alias);
        profileDetailsModel.setLast_name(last_name_);
        profileDetailsModel.setPhone(mobile);
        profileDetailsModel.setIsd(isd);
        profileDetailsModel.setAddress(address);
        profileDetailsModel.setImage(bmp);
        profileDetailsModel.setCountry(country);
        profileDetailsModel.setShop_description(shop_description);
        profileDetailsModel.setShop_name(shp_name);

    /*    Shop shop=new Shop();
        shop.setName(shp_name);
        shop.setDescription(shop_description);
        shop.setShop_link(shp_link);
        profileDetailsModel.setShop(shop);*/

        servicesMethodsManager.updateProfile(context, profileDetailsModel, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                hideProgressBar();
                ProfileDetailsModel model = (ProfileDetailsModel) arg0;
                update(model);

            }

            @Override
            public void OnFailureFromServer(String msg) {
                hideProgressBar();
                Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void OnError(String msg) {
                hideProgressBar();
                Toast.makeText(activity, "msg", Toast.LENGTH_LONG).show();
            }
        }, "Profile update");

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_CHOOSE) {
                if(data!=null && Matisse.obtainPathResult(data)!=null
                        && Matisse.obtainPathResult(data).size() > 0){
                    String imagePatha = System.currentTimeMillis() + "11";
                    Uri uriImage = Matisse.obtainResult(data).get(0);
                    try {
                        String imagePath;
                        Bitmap bitmap = null, rotedBitmap = null;
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriImage);
                        rotedBitmap = Utils.setRotatedBitmap(this, Utils.getResizedBitmap(bitmap, bitmap.getHeight() / 2, bitmap.getWidth() / 2), uriImage);
                        // imagePath = ImageUtils.savePicture(this, rotedBitmap, "" + imagePatha);

                        String base64_image = CommonUtil.encodeToBase64(rotedBitmap, Bitmap.CompressFormat.JPEG, 100);

                        if (base64_image != null) {
                            setImage_bmp(base64_image);
                        }
                        if (rotedBitmap != null) {
                            Picasso.with(activity)
                                    .load(getImageUri(activity, rotedBitmap))
                                    .placeholder(R.drawable.squareplaceholder)
                                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                                    .networkPolicy(NetworkPolicy.NO_CACHE)
                                    .fit().centerCrop()
                                    .into(rounde_image);
//                    rounde_image.setImageBitmap(bmp);
                        } else {
                            Toast.makeText(context, "Your profile pic is empty can't update the your profile", Toast.LENGTH_LONG).show();
                        }
                        if (bitmap != null) {
                            bitmap.recycle();
                            bitmap = null;
                        }
                        if (rotedBitmap != null) {
                            rotedBitmap.recycle();
                            rotedBitmap = null;
                        }


                    } catch (IOException e) {
                        BlueShakLog.logError(TAG, "onActivityResult Exception -> " + e.getLocalizedMessage());
                    }

                }
            }
        }

        /*if (requestCode == PICK_IMAGE_CAMERA) {
            try {

                try {
                    Bitmap rotedBitmap = null,bitmap = null;
                    bitmap = (Bitmap) data.getExtras().get("data");
                    Uri selectedImage = Utils.getImageUri(this,bitmap);
                    rotedBitmap = Utils.setRotatedBitmap(this, Utils.getResizedBitmap(bitmap,
                            bitmap.getHeight(),bitmap.getWidth()), selectedImage);

                    String base64_image= CommonUtil.encodeToBase64(rotedBitmap, Bitmap.CompressFormat.JPEG,100);
                    setImage_bmp(base64_image);
                    // String bitmap = ImagePicker.getImagePathFromResult(getActivity(),requestCode,resultCode,data);
                    if(!TextUtils.isEmpty(base64_image)){

                        Picasso.with(activity)
                                .load(getImageUri(activity,rotedBitmap))
                                .placeholder(R.drawable.squareplaceholder)
                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .networkPolicy(NetworkPolicy.NO_CACHE)
                                .fit().centerCrop()
                                .into(rounde_image);
//                    rounde_image.setImageBitmap(bmp);
                    }else{
                        Toast.makeText(context,"Your profile pic is empty can't update the your profile",Toast.LENGTH_LONG).show();
                    }
                    if (bitmap != null) {
                        bitmap.recycle();
                        bitmap = null;
                    }
                    if (rotedBitmap != null) {
                        rotedBitmap.recycle();
                        rotedBitmap = null;
                    }
                } catch (Exception e) {

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_GALLERY) {
            Uri selectedImage = data.getData();
            try {
                try {

                    Bitmap bitmap = null, rotedBitmap = null;
                    String imagePatha = "profile";
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    rotedBitmap = Utils.setRotatedBitmap(this, Utils.getResizedBitmap(bitmap,
                            bitmap.getHeight()/2,bitmap.getWidth()/2), selectedImage);
                    String base64_image= CommonUtil.encodeToBase64(rotedBitmap, Bitmap.CompressFormat.JPEG,100);
                    setImage_bmp(base64_image);
                    // String bitmap = ImagePicker.getImagePathFromResult(getActivity(),requestCode,resultCode,data);
                    if(!TextUtils.isEmpty(base64_image)){

                        Picasso.with(activity)
                                .load(getImageUri(activity,rotedBitmap))
                                .placeholder(R.drawable.squareplaceholder)
                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .networkPolicy(NetworkPolicy.NO_CACHE)
                                .fit().centerCrop()
                                .into(rounde_image);
//                    rounde_image.setImageBitmap(bmp);
                    }else{
                        Toast.makeText(context,"Your profile pic is empty can't update the your profile",Toast.LENGTH_LONG).show();
                    }

                    if (bitmap != null) {
                        bitmap.recycle();
                        bitmap = null;
                    }
                    if (rotedBitmap != null) {
                        rotedBitmap.recycle();
                        rotedBitmap = null;
                    }

                } catch (Exception e) {

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
  /*  @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK)
        {
            Log.d("VALUESSS","VALUESSS===" +
                    ">>"+data);
            if (data == null)
            {
                return;
            }


            try {
                String imagePatha = "profile";
                Bitmap bit_ = ImagePicker.getImageFromResult(activity, requestCode, resultCode, data);
               // String bitmap = ImageUtils.savePicture(activity, bit_, imagePatha);
                String base64_image= CommonUtil.encodeToBase64(bit_, Bitmap.CompressFormat.JPEG,100);
                setImage_bmp(base64_image);
                // String bitmap = ImagePicker.getImagePathFromResult(getActivity(),requestCode,resultCode,data);
                if(!TextUtils.isEmpty(base64_image)){

                    Picasso.with(activity)
                            .load(getImageUri(activity,bit_))
                            .placeholder(R.drawable.squareplaceholder)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .fit().centerCrop()
                            .into(rounde_image);
//                    rounde_image.setImageBitmap(bmp);
                }else{
                    Toast.makeText(context,"Your profile pic is empty can't update the your profile",Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {

            }
           *//* try {
                InputStream inputStream = context.getContentResolver().openInputStream(data.getData());
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);
                String base64_image= CommonUtil.encodeToBase64(bmp, Bitmap.CompressFormat.JPEG,100);
                setImage_bmp(base64_image);

              *//**//*  if(bmp!=null)
                    bmp.recycle();
*//**//*
                if(!TextUtils.isEmpty(base64_image)){

                Picasso.with(activity)
                            .load(getImageUri(activity,bmp))
                            .placeholder(R.drawable.squareplaceholder)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .fit().centerCrop()
                            .into(rounde_image);
//                    rounde_image.setImageBitmap(bmp);
                }else{
                    Toast.makeText(context,"Your profile pic is empty can't update the your profile",Toast.LENGTH_LONG).show();
                }
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }*//*
        }
        else if(requestCode == REQUEST_CAMERA){

            onCaptureImageResult(data);
        }else  if (requestCode == globalVariables.REQUEST_CODE_FILTER_PICK_LOCATION) {
            if(data!=null){
                if(data.hasExtra(PROFILE_ACTIVITY_LOCATION_BUNDLE_KEY)){
                    LocationModel location_model = (LocationModel) data.getExtras().getSerializable(PROFILE_ACTIVITY_LOCATION_BUNDLE_KEY);
                    Log.i(TAG, "name pm" + location_model.getFormatted_address());
                    edit_address.setText(location_model.getFormatted_address());
                }
            }

        }
    }
*/

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void onCaptureImageResult(Intent data) {
        if (data != null) {
            if (data.hasExtra("data")) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                String base64_image = CommonUtil.encodeToBase64(thumbnail, Bitmap.CompressFormat.JPEG, 100);
                Picasso.with(activity)
                        .load(getImageUri(activity, thumbnail))
                        .placeholder(R.drawable.squareplaceholder)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .fit().centerCrop()
                        .into(rounde_image);
//                rounde_image.setImageBitmap(thumbnail);


      /*  if(thumbnail!=null)
           thumbnail.recycle();
      */
                setImage_bmp(base64_image);
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
            }
        }

    }

    private static void logoutAndMove(Context context) {
        GlobalFunctions.removeSharedPreferenceKey(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
        GlobalFunctions.removeSharedPreferenceKey(context, GlobalVariables.SHARED_PREFERENCE_BS_ID);
        GlobalFunctions.removeSharedPreferenceKey(context, GlobalVariables.SHARED_PREFERENCE_USERID);
     /*   CommonUtil.unregisterDevice(context);*/
        PushManager.getInstance(context).unregisterForPushNotifications();
        activity.finish();
        MainActivity.is_reset = true;
      /*  Intent j = new Intent(activity,LoginActivity.class);
        context.startActivity(j);*/
    }

    public void checkCameraPermission() {
        int permissionCheck_location_coarse = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCheck_write_coarse = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCheck_camera = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        if (permissionCheck_location_coarse != PackageManager.PERMISSION_GRANTED ||
                permissionCheck_write_coarse != PackageManager.PERMISSION_GRANTED
                || permissionCheck_camera != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CHECK_CAMERA);
            }
        }


    }

    private boolean checkIfAlreadyhavePermission() {
        Log.d(TAG, "checkIfReadExternalStorageAlreadyhavePermission######################");
        int coarse_location = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int camera = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        if (coarse_location == PackageManager.PERMISSION_GRANTED && result == PackageManager.PERMISSION_GRANTED
                && camera == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        {
            switch (requestCode) {
                case REQUEST_CHECK_CAMERA:
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        selectImage(mImageCount);
                    } else {
                        checkCameraPermission();
                    }
                    return;

            }
        }

        /*
        Log.d(TAG,"onRequestPermissionsResult##############");
        switch (requestCode) {
            case REQUEST_CHECK_CAMERA:
                {
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
        }*/
    }

    public void checkReadExternalStoragePermission() {
        Log.d(TAG, "checkReadExternalStoragePermission######################");
        int permissionCheck_location_coarse = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCheck_write_coarse = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck_location_coarse != PackageManager.PERMISSION_GRANTED &&
                permissionCheck_write_coarse != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CHECK_GALLARY);
        }

    }

    private boolean checkIfReadExternalStorageAlreadyhavePermission() {
        Log.d(TAG, "checkIfReadExternalStorageAlreadyhavePermission######################");
        int coarse_location = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (coarse_location == PackageManager.PERMISSION_GRANTED && result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public void verifyEmail() {
        showProgressBar();
        ServicesMethodsManager serverManager = new ServicesMethodsManager();
        serverManager.verifyEmail(context, new ProfileDetailsModel(), new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                hideProgressBar();
                StatusModel statusModel = (StatusModel) arg0;
                Log.d(TAG, "onSuccess Response" + statusModel.toString());
                validateEmail(statusModel);
            }

            @Override
            public void OnFailureFromServer(String msg) {
                hideProgressBar();
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnError(String msg) {
                hideProgressBar();
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            }
        }, "Email verification");
    }

    public void verifyPhoneNumber() {
        showProgressBar();
        ServicesMethodsManager serverManager = new ServicesMethodsManager();
        OTPResendModel resendModel = new OTPResendModel(GlobalFunctions.getSharedPreferenceString(getApplication(), GlobalVariables.SHARED_PREFERENCE_PHONE));
        serverManager.resendOTP(context, resendModel, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                hideProgressBar();
                validateOTPResend((StatusModel) arg0);
            }

            @Override
            public void OnFailureFromServer(String msg) {
                hideProgressBar();
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnError(String msg) {
                hideProgressBar();
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            }
        }, "OTP verification");
    }

    private void validateOTPResend(StatusModel statusModel) {
        if (statusModel.isStatus()) {
            Toast.makeText(getApplicationContext(), "OTP Send Successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Already Authenticated", Toast.LENGTH_SHORT).show();
        }
    }

    private void validateEmail(StatusModel statusModel) {
        if (statusModel.getMessage().equalsIgnoreCase("Verification link is sent to respective E-mail")) {
            mail_verified = true;
        } else {
            Toast.makeText(getApplicationContext(), "Error on verifying Email", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetPassword() {
        showProgressBar();
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        ForgotPasswordModel forgotPasswordModel = new ForgotPasswordModel(email);
        servicesMethodsManager.forgotPassword(context, forgotPasswordModel, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                hideProgressBar();
                validateResult((StatusModel) arg0);
            }

            @Override
            public void OnFailureFromServer(String msg) {
                hideProgressBar();
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnError(String msg) {
                hideProgressBar();
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        }, "Forgot Password");
    }

    private void validateResult(StatusModel statusModel) {
        if (statusModel.isStatus()) {
            Toast.makeText(activity, "an email has been sent to change your password", Toast.LENGTH_LONG).show();
            logoutAndMove(context);
            closeThisActivity();
        } else {
            Toast.makeText(getApplicationContext(), "Error on Generating Password, try again later", Toast.LENGTH_SHORT).show();
        }
    }

    public static void closeThisActivity() {
        if (activity != null) {
            activity.finish();
        }
    }

    public void showExitAlert() {
        final com.blueshak.app.blueshak.view.AlertDialog alertDialog = new com.blueshak.app.blueshak.view.AlertDialog(context);
        alertDialog.setTitle("BlueShak");
        alertDialog.setMessage("You are sure you want to reset your password? You will be logged out and will receive a new password in your email.");
        alertDialog.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                resetPassword();
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public void proceedToLocation() {
        Intent intent = PickLocation.newInstance(context, GlobalVariables.TYPE_MY_PROFILE, true, null);
        startActivityForResult(intent, globalVariables.REQUEST_CODE_FILTER_PICK_LOCATION);
    }

    public void showProgressBar() {
        if (progress_bar != null)
            progress_bar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        if (progress_bar != null)
            progress_bar.setVisibility(View.GONE);
    }

    private boolean isValidMobile(String phone) {
        boolean check = false;
        /*if(phone.length()!=9) {*/
        if (phone.length() < 8 || phone.length() > 12) {
            Log.d(TAG, "################phone.length() < 6 || phone.length() > 15");
            check = false;
            edit_mobile.setError("Please enter a valid Mobile Number");
        }/*else if(!phone.startsWith("+")){
            etMobileNUmber.setError("Please enter a valid 9 digit Mobile Number");
		}*/ else if (phone.startsWith("00")) {
            Log.d(TAG, "######e(phone).startsWith(\"0\"> 15");
            edit_mobile.setError("Please enter a valid Mobile Number");
        } else {
            check = true;
            edit_mobile.setText(phone);
        }
        return check;
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public void update(ProfileDetailsModel model) {
        GlobalFunctions.setSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_FULLNAME, model.getName());
        GlobalFunctions.setSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_PHONE, model.getPhone());
        GlobalFunctions.setSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_ADDRESS, model.getAddress());
        GlobalFunctions.setSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_AVATAR, model.getAvatar());
        if (model.is_otp_verified()) {
            Toast.makeText(context, "Your profile has been updated Successfully", Toast.LENGTH_LONG).show();
            finish();
        } else {
            OTPCheckerModel otpCheckerModel = new OTPCheckerModel();
            otpCheckerModel.setPhone(model.getPhone());
            otpCheckerModel.setBs_id(model.getId());
            otpCheckerModel.setIsd(model.getIsd());
            GlobalFunctions.setSharedPreferenceString(context, GlobalVariables.OTP_CHECK_MODEL, otpCheckerModel.toString());
            GlobalFunctions.removeSharedPreferenceKey(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
            GlobalFunctions.removeSharedPreferenceKey(context, GlobalVariables.SHARED_PREFERENCE_BS_ID);
            GlobalFunctions.removeSharedPreferenceKey(context, GlobalVariables.SHARED_PREFERENCE_USERID);
            GlobalFunctions.removeSharedPreferenceKey(context, GlobalVariables.SHARED_PREFERENCE_IS_OTP_VERIFIED);
           /* PushManager.getInstance(context).unregisterForPushNotifications();*/
            Intent intent = OTPActivity.newInstance(context, otpCheckerModel);
            startActivity(intent);
            finish();
        }

    }

    private void verifyUserId(final String aliasname) {
        showProgressBar();
        VerifyAliasModel verifyAliasModel = new VerifyAliasModel();
        verifyAliasModel.setAlias_name(aliasname);
        //GlobalFunctions.showProgress(context, "Fetching address...");
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.isUserIdValid(context, verifyAliasModel, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                //GlobalFunctions.hideProgress();
                Log.d(TAG, "onSuccess Response");
                hideProgressBar();
                VerifyAliasModel verifyAliasModel = (VerifyAliasModel) arg0;
                if (verifyAliasModel.isAlias_available()) {
                    Log.d(TAG, "##############onSuccess Response########" + verifyAliasModel.getMessage());
                    sendRequestToServer(context);
                } else {
                    Toast.makeText(context, verifyAliasModel.getMessage(), Toast.LENGTH_LONG).show();
                    alias_name.setError(verifyAliasModel.getMessage());
                }
            }

            @Override
            public void OnFailureFromServer(String msg) {
                //GlobalFunctions.hideProgress();
                Log.d(TAG, msg);
                hideProgressBar();
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void OnError(String msg) {
                hideProgressBar();
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                hideProgressBar();
                Log.d(TAG, msg);
            }
        }, "Delete Sale");
    }

}
