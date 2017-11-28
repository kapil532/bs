package com.blueshak.app.blueshak.garage;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blueshak.app.blueshak.photos_add.OnDeletePicture;
import com.blueshak.app.blueshak.root.RootActivity;
import com.blueshak.app.blueshak.services.model.CreateImageModel;
import com.blueshak.app.blueshak.services.model.CreateProductModel;
import com.blueshak.app.blueshak.util.BlueShakLog;
import com.blueshak.app.blueshak.view.AlertDialog;
import com.blueshak.blueshak.R;
import com.crashlytics.android.Crashlytics;
import com.mvc.imagepicker.ImagePicker;
import com.mvc.imagepicker.ImageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AddSalesImagesActivity extends RootActivity implements OnDeletePicture,View.OnDragListener,View.OnLongClickListener {

    ArrayList<CreateImageModel> removed_photos = new ArrayList<CreateImageModel>();

    private static CreateProductModel productModel = null;

    protected static final int REQUEST_CHECK_CAMERA = 115;
    private static final int REQUEST_CODE_CHOOSE = 23;

    private ImageView img_selection_one;
    private ImageView img_selection_two;
    private ImageView img_selection_three;
    private ImageView img_selection_four;
    private ImageView img_selection_five;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sales_images);
        setToolBar(getString(R.string.title_activity_add_images));
        init();
        onOk();
        onToolBarCancel();

        productModel = new CreateProductModel();
    }

    private void init() {
        img_selection_one = findViewById(R.id.img_selection_one);
        img_selection_two = findViewById(R.id.img_selection_two);
        img_selection_three = findViewById(R.id.img_selection_three);
        img_selection_four = findViewById(R.id.img_selection_four);
        img_selection_five = findViewById(R.id.img_selection_five);

        findViewById(R.id.img_selection_one).setOnLongClickListener(this);
        findViewById(R.id.img_selection_two).setOnLongClickListener(this);
        findViewById(R.id.img_selection_three).setOnLongClickListener(this);
        findViewById(R.id.img_selection_four).setOnLongClickListener(this);
        findViewById(R.id.img_selection_five).setOnLongClickListener(this);

        findViewById(R.id.layout_drag_one).setOnDragListener(this);
        findViewById(R.id.layout_drag_two).setOnDragListener(this);
        setBitmapImages(CreateItemSaleFragment.objectUploadPhoto.getAvailablePhotos());

        img_selection_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //img_selection_one.setImageBitmap(null);
                //deleteCameraViewfromFile(0);
                if(CreateItemSaleFragment.objectUploadPhoto.getAvailablePhotos().size() >=1 &&
                        CreateItemSaleFragment.objectUploadPhoto.getAvailablePhotos().get(0).isRealImage()){
                    showAlert("Do you want edit or delete photo?",0);
                }else{
                    setImage();
                }

            }
        });
        img_selection_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CreateItemSaleFragment.objectUploadPhoto.getAvailablePhotos().size() >=2 &&
                        CreateItemSaleFragment.objectUploadPhoto.getAvailablePhotos().get(1).isRealImage()){
                    showAlert("Do you want edit or delete photo?",1);
                }else{
                    setImage();
                }
            }
        });
        img_selection_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CreateItemSaleFragment.objectUploadPhoto.getAvailablePhotos().size() >=3 &&
                        CreateItemSaleFragment.objectUploadPhoto.getAvailablePhotos().get(2).isRealImage()){
                    showAlert("Do you want edit or delete photo?",2);
                }else{
                    setImage();
                }
            }
        });
        img_selection_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CreateItemSaleFragment.objectUploadPhoto.getAvailablePhotos().size() >=4 &&
                        CreateItemSaleFragment.objectUploadPhoto.getAvailablePhotos().get(3).isRealImage()){
                    showAlert("Do you want edit or delete photo?",3);
                }else{
                    setImage();
                }
            }
        });
        img_selection_five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CreateItemSaleFragment.objectUploadPhoto.getAvailablePhotos().size() >=5 &&
                CreateItemSaleFragment.objectUploadPhoto.getAvailablePhotos().get(4).isRealImage()){
                    showAlert("Do you want edit or delete photo?",4);
                }else{
                    setImage();
                }
            }
        });
    }

    private void onOk() {
        findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             setResult(RESULT_OK);
             finish();
            }
        });
    }

    private void selectImage() {

        if (checkIfAlreadyhavePermission()) {
//            RxPermissions rxPermissions = new RxPermissions(activity);
//            rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            Matisse.from(this)
                    .choose(MimeType.ofImage(), true)
                    .countable(true)
                    .capture(true)
                    .captureStrategy(
                            new com.zhihu.matisse.internal.entity.CaptureStrategy(true, "com.blueshak.fileprovider"))
                    .maxSelectable(5)
                    .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                    .gridExpectedSize(
                            getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                    .thumbnailScale(0.85f)
                    .imageEngine(new GlideEngine())
                    .forResult(REQUEST_CODE_CHOOSE);
        }
        //ImagePicker.pickImage(CreateItemSaleFragment.this, "Select your image");
        else
            checkCameraPermission();
    }

    public void checkCameraPermission() {
        int permissionCheck_location_coarse = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCheck_write_coarse = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCheck_camera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
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
        int coarse_location = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int camera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (coarse_location == PackageManager.PERMISSION_GRANTED && result == PackageManager.PERMISSION_GRANTED
                && camera == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult ##########@@@@@@@@@@@@@@@@@@@@@@@@@");
        switch (requestCode) {
            case REQUEST_CHECK_CAMERA:
                Log.d(TAG, "onRequestPermissionsResult ############REQUEST_CHECK_CAMERA");
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    selectImage();
                } else {
                    checkCameraPermission();
                }
                return;

        }
    }


    public void deleteCameraViewfromFile(int position,Boolean isDeleteShow) {
        try {
            String fileString = CreateItemSaleFragment.objectUploadPhoto.getAvailablePhotos().get(position).getImage();
            removed_photos.add(CreateItemSaleFragment.objectUploadPhoto.getAvailablePhotos().get(position));
            File file = new File(fileString);
            if (file.exists()) {
                file.delete();
                file.getAbsoluteFile().delete();
                if (file.exists()) {
                    getApplicationContext().deleteFile(file.getPath());
                }
                if (!file.exists() && isDeleteShow) {
                    Toast.makeText(this, "File Deleted Successfully", Toast.LENGTH_SHORT).show();
                }
            }
            CreateItemSaleFragment.objectUploadPhoto.getAvailablePhotos().remove(position);
            CreateItemSaleFragment.objectUploadPhoto.setRemoved_photos(removed_photos);
            // refreshCameraImageList();
        } catch (Exception e) {
            Log.d(TAG, "Exception on Deleting Image : " + e);

        }

    }

    public static void setImages() {
        if (CreateItemSaleFragment.objectUploadPhoto != null && productModel != null) {
            productModel.setImages(CreateItemSaleFragment.objectUploadPhoto.getAvailablePhotos());
            productModel.setRemove_images(CreateItemSaleFragment.objectUploadPhoto.getRemoved_photos());
        }

    }

    @Override
    public void ondeleting(int position) {
        deleteCameraViewfromFile(position,true);
    }

    @Override
    public void onImageClick(int position, File file, String name, Boolean isDefault) {

    }

    private void setRefreshData() {
       /*call the outer function to refresh list*/
         setImages();

    }

    private void setImage() {
        if (CreateItemSaleFragment.objectUploadPhoto != null) {
            /*if (objectUploadPhoto.getAvailablePhotos().size() >= 5) {
                //addIcon_one.setVisibility(View.GONE);
                Toast.makeText(this, "You can not add more than five images", Toast.LENGTH_SHORT).show();
            } else {
                selectImage();
            }*/
            selectImage();
        } else {
            selectImage();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {

            if (resultCode == RESULT_OK) {
                try {
                    String imagePatha = System.currentTimeMillis() + "11";
                    Bitmap bit_ = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);
                    String bitmap = ImageUtils.savePicture(this, bit_, imagePatha);
                    // String bitmap = ImagePicker.getImagePathFromResult(getActivity(),requestCode,resultCode,data);
                    BlueShakLog.logDebug(TAG, "onActivityResult IMAGE PATH -> " + bitmap);
                    CreateImageModel modela = new CreateImageModel();
                    modela.setImage(bitmap);
                    modela.setDisplay(false);
                    CreateItemSaleFragment.objectUploadPhoto.getAvailablePhotos().add(modela);
                    // refreshCameraImageList();

                } catch (Exception e) {
                    BlueShakLog.logError(TAG, "onActivityResult Exception -> " + e.getLocalizedMessage());
                }
                if (requestCode == REQUEST_CODE_CHOOSE) {
                    //objectUploadPhoto.getAvailablePhotos().clear();
                    for (int i = 0; i < Matisse.obtainPathResult(data).size(); i++) {
                        if(CreateItemSaleFragment.objectUploadPhoto.getAvailablePhotos().size() >= 5){
                            deleteCameraViewfromFile(0,false);
                        }
                        String imagePatha = System.currentTimeMillis() + "11";
                        Uri uriImage = Matisse.obtainResult(data).get(i);
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriImage);
                            String bitmapa = ImageUtils.savePicture(this, bitmap, "" + imagePatha);
                            CreateImageModel modela = new CreateImageModel();

                            modela.setImage(bitmapa);
                            modela.setDisplay(false);
                            modela.setRealImage(true);
                            CreateItemSaleFragment.objectUploadPhoto.getAvailablePhotos().add(modela);



                        } catch (IOException e) {
                            BlueShakLog.logError(TAG, "onActivityResult Exception -> " + e.getLocalizedMessage());
                        }
                        //refreshCameraImageList();
                        setRefreshData();
                    }
                    CreateItemSaleFragment.objectUploadPhoto.setAvailablePhotos(CreateItemSaleFragment.objectUploadPhoto.getAvailablePhotos());
                    setBitmapImages(CreateItemSaleFragment.objectUploadPhoto.getAvailablePhotos());
                    //Toast.makeText(this, "" + Matisse.obtainResult(data).toString() + "--" + Matisse.obtainPathResult(data), Toast.LENGTH_LONG).show();
                }
            }
        } catch (NullPointerException e) {
            BlueShakLog.logError(TAG, "onActivityResult Exception -> " + e.getLocalizedMessage());
            Crashlytics.log(e.getMessage());
        } catch (NumberFormatException e) {
            Crashlytics.log(e.getMessage());
            BlueShakLog.logError(TAG, "onActivityResult Exception -> " + e.getLocalizedMessage());
        } catch (Exception e) {
            Crashlytics.log(e.getMessage());
            BlueShakLog.logError(TAG, "onActivityResult Exception -> " + e.getLocalizedMessage());
        }
    }

    private void setBitmapImages(ArrayList<CreateImageModel> imageList) {

        BlueShakLog.logDebug(TAG, "setBitmapImages imagelist size -> " + imageList.size());

        DisplayImageOptions GALLERY = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .considerExifParams(true)
                .cacheOnDisk(true)
                .showImageForEmptyUri(R.drawable.placeholder_background)
                .showImageOnFail(R.drawable.placeholder_background)
                .showImageOnLoading(R.drawable.placeholder_background).build();


        Collections.sort(imageList, new Comparator<CreateImageModel>(){
            @Override
            public int compare(CreateImageModel mall1, CreateImageModel mall2){

                boolean b1 = mall1.isRealImage();
                boolean b2 = mall2.isRealImage();

                return (b1 != b2) ? (b1) ? -1 : 1 : 0;
            }
        });

        if(imageList.size() == 5){
            setBitmapDecodedImage(0,img_selection_one,GALLERY,imageList);
            setBitmapDecodedImage(1,img_selection_two,GALLERY,imageList);
            setBitmapDecodedImage(2,img_selection_three,GALLERY,imageList);
            setBitmapDecodedImage(3,img_selection_four,GALLERY,imageList);
            setBitmapDecodedImage(4,img_selection_five,GALLERY,imageList);
        }else if(imageList.size() == 4){
            setBitmapDecodedImage(0,img_selection_one,GALLERY,imageList);
            setBitmapDecodedImage(1,img_selection_two,GALLERY,imageList);
            setBitmapDecodedImage(2,img_selection_three,GALLERY,imageList);
            setBitmapDecodedImage(3,img_selection_four,GALLERY,imageList);
            img_selection_five.setImageResource(R.drawable.placeholder_background);
        }else if(imageList.size() == 3){
            setBitmapDecodedImage(0,img_selection_one,GALLERY,imageList);
            setBitmapDecodedImage(1,img_selection_two,GALLERY,imageList);
            setBitmapDecodedImage(2,img_selection_three,GALLERY,imageList);
            img_selection_four.setImageResource(R.drawable.placeholder_background);
            img_selection_five.setImageResource(R.drawable.placeholder_background);
        }else if(imageList.size() == 2){
            setBitmapDecodedImage(0,img_selection_one,GALLERY,imageList);
            setBitmapDecodedImage(1,img_selection_two,GALLERY,imageList);
            img_selection_three.setImageResource(R.drawable.placeholder_background);
            img_selection_four.setImageResource(R.drawable.placeholder_background);
            img_selection_five.setImageResource(R.drawable.placeholder_background);
        }else if(imageList.size() == 1){
            setBitmapDecodedImage(0,img_selection_one,GALLERY,imageList);
            img_selection_two.setImageResource(R.drawable.placeholder_background);
            img_selection_three.setImageResource(R.drawable.placeholder_background);
            img_selection_four.setImageResource(R.drawable.placeholder_background);
            img_selection_five.setImageResource(R.drawable.placeholder_background);
        }else {
            img_selection_one.setImageResource(R.drawable.placeholder_background);
            img_selection_two.setImageResource(R.drawable.placeholder_background);
            img_selection_three.setImageResource(R.drawable.placeholder_background);
            img_selection_four.setImageResource(R.drawable.placeholder_background);
            img_selection_five.setImageResource(R.drawable.placeholder_background);
        }

    }

    private void setBitmapDecodedImage(int position,ImageView imageView,DisplayImageOptions GALLERY,
                                       ArrayList<CreateImageModel> imageList){
        String decodedImgUri = Uri.fromFile(new File(imageList.get(position).getImage())).toString();
        ImageLoader.getInstance().displayImage(decodedImgUri, imageView, GALLERY);
    }

  private void onToolBarCancel(){
      ((TextView)mToolBarView.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              setResult(RESULT_CANCELED);
              finish();
          }
      });
  }

    @Override
    public boolean onDrag(View layoutview, DragEvent dragEvent) {
        int action = dragEvent.getAction();
        switch (action) {
            case DragEvent.ACTION_DRAG_STARTED:
                BlueShakLog.logDebug(TAG,"Drag event started ");
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
               // Log.d(LOGCAT, "Drag event entered into "+layoutview.toString());
                BlueShakLog.logDebug(TAG,"Drag event entered into ");
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                BlueShakLog.logDebug(TAG,"Drag event exited from ");
                break;
            case DragEvent.ACTION_DROP:
                BlueShakLog.logDebug(TAG,"Dropped ");
                View viewDrag = (View) dragEvent.getLocalState();
                ViewGroup owner = (ViewGroup) viewDrag.getParent();
                owner.removeView(viewDrag);
                LinearLayout container = (LinearLayout) layoutview;
                container.addView(viewDrag);
                viewDrag.setVisibility(View.VISIBLE);
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                BlueShakLog.logDebug(TAG,"Drag ended ");
                break;
            default:
                break;
        }
        return true;
    }



    /*@Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(null, shadowBuilder, view, 0);
            view.setVisibility(View.INVISIBLE);
            return true;
        }
        else {
            return false;
        }
    }*/

    private void showAlert(String message,final int position){
        final AlertDialog alertDialog = new AlertDialog(this);
        alertDialog.setCancelable(true);
        alertDialog.setIcon(R.drawable.ic_warning_black_24dp);
        alertDialog.setTitle(this.getResources().getString(R.string.app_name));
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("Edit", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                setImage();
            }
        });

        alertDialog.setNegativeButton("Delete", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                deleteCameraViewfromFile(position,false);
                setBitmapImages(CreateItemSaleFragment.objectUploadPhoto.getAvailablePhotos());
            }
        });

        alertDialog.show();
    }

    @Override
    public boolean onLongClick(View view) {
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
        view.startDrag(null, shadowBuilder, view, 0);
        view.setVisibility(View.INVISIBLE);
        return true;
    }

    private void deleteDefaultImages(){
        for(int i = 0; i < CreateItemSaleFragment.objectUploadPhoto.getAvailablePhotos().size(); i++){
            if(!CreateItemSaleFragment.objectUploadPhoto.getAvailablePhotos().get(i).isRealImage()){
                deleteCameraViewfromFile(i,false);
            }
        }
    }
}
