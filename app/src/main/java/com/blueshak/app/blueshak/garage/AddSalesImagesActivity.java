package com.blueshak.app.blueshak.garage;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import java.util.concurrent.atomic.AtomicBoolean;

public class AddSalesImagesActivity extends RootActivity implements OnDeletePicture, View.OnDragListener, View.OnLongClickListener {
    public static String TAG = AddSalesImagesActivity.class.getSimpleName();
    ArrayList<CreateImageModel> removed_photos = new ArrayList<CreateImageModel>();

    private static CreateProductModel productModel = null;

    protected static final int REQUEST_CHECK_CAMERA = 115;
    private static final int REQUEST_CODE_CHOOSE = 23;

    private GridLayout mGrid;
    private ScrollView mScrollView;
    private ValueAnimator mAnimator;
    private AtomicBoolean mIsScrolling = new AtomicBoolean(false);
    private int mPosition = 0;
    public ArrayList<CreateImageModel> createImageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sales_images);
        setToolBar(getString(R.string.title_activity_add_images));
        init();
        dragAndDrop(false);
        onOk();
        onToolBarCancel();

        productModel = new CreateProductModel();
    }

    ImageView imageView;

    private void dragAndDrop(boolean isRealBitmap) {
        if (isRealBitmap) {
            mGrid.removeAllViews();
        }
        final LayoutInflater inflater = LayoutInflater.from(this);
        for (mPosition = 0; mPosition < 5; mPosition++) {
            final View itemView = inflater.inflate(R.layout.grid_item, mGrid, false);
            imageView = (ImageView) itemView.findViewById(R.id.img_selection);
            setBitmapImages(CreateItemSaleFragment.objectUploadPhoto.getAvailablePhotos(), imageView, mPosition);
            itemView.setTag(mPosition);
            itemView.setOnLongClickListener(new LongPressListener());
            itemView.setOnClickListener(new onClickListener());
            mGrid.addView(itemView);
        }
    }

    private void init() {

        mScrollView = (ScrollView) findViewById(R.id.layout_grid);
        mGrid = (GridLayout) findViewById(R.id.grid_layout);
        mGrid.setOnDragListener(new DragListener());

    }

    private void onOk() {
        findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removedDataFromArrayListLast(CreateItemSaleFragment.objectUploadPhoto.getAvailablePhotos());
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private void selectImage() {
        if (checkIfAlreadyhavePermission()) {
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
        } else {
            checkCameraPermission();
        }

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


    public void deleteCameraViewfromFile(int position, Boolean isDeleteShow) {
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
        deleteCameraViewfromFile(position, true);
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
            selectImage();
        } else {
            selectImage();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == REQUEST_CODE_CHOOSE) {
                    for (int i = 0; i < Matisse.obtainPathResult(data).size(); i++) {
                        if (isImageEdit) {
                            deleteCameraViewfromFile(iEditablePosition, false);
                        }
                        String imagePatha = System.currentTimeMillis() + "11";
                        Uri uriImage = Matisse.obtainResult(data).get(i);
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriImage);
                            String bitmapa = ImageUtils.savePicture(this, bitmap, "" + imagePatha);
                            CreateImageModel modela = new CreateImageModel();

                            modela.setImage(bitmapa);
                            modela.setDisplay(true);
                            modela.setRealImage(true);
                            modela.setId(i);
                            modela.setImage_order(i);
                            CreateItemSaleFragment.objectUploadPhoto.getAvailablePhotos().add(modela);


                        } catch (IOException e) {
                            BlueShakLog.logError(TAG, "onActivityResult Exception -> " + e.getLocalizedMessage());
                        }
                    }
                    sortArray(CreateItemSaleFragment.objectUploadPhoto.getAvailablePhotos());
                    CreateItemSaleFragment.objectUploadPhoto.setAvailablePhotos(CreateItemSaleFragment.objectUploadPhoto.getAvailablePhotos());
                    dragAndDrop(true);
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
        String decodedImgUri = Uri.fromFile(new File(imageList.get(position).getImage())).toString();
        ImageLoader.getInstance().displayImage(decodedImgUri, imageView, GALLERY);
    }

    private void onToolBarCancel() {
        ((TextView) mToolBarView.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
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
                BlueShakLog.logDebug(TAG, "Drag event started ");
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                // Log.d(LOGCAT, "Drag event entered into "+layoutview.toString());
                BlueShakLog.logDebug(TAG, "Drag event entered into ");
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                BlueShakLog.logDebug(TAG, "Drag event exited from ");
                break;
            case DragEvent.ACTION_DROP:
                BlueShakLog.logDebug(TAG, "Dropped ");
                View viewDrag = (View) dragEvent.getLocalState();
                ViewGroup owner = (ViewGroup) viewDrag.getParent();
                owner.removeView(viewDrag);
                LinearLayout container = (LinearLayout) layoutview;
                container.addView(viewDrag);
                viewDrag.setVisibility(View.VISIBLE);
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                BlueShakLog.logDebug(TAG, "Drag ended ");
                break;
            default:
                break;
        }
        return true;
    }

    int iEditablePosition = 0;

    private void showAlert(String message, final int position) {
        final AlertDialog alertDialog = new AlertDialog(this);
        alertDialog.setCancelable(true);
        alertDialog.setIcon(R.drawable.ic_warning_black_24dp);
        alertDialog.setTitle(this.getResources().getString(R.string.app_name));
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("Edit", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                isImageEdit = true;
                iEditablePosition = position;
                setImage();
            }
        });

        alertDialog.setNegativeButton("Delete", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isImageEdit = false;
                alertDialog.dismiss();
                deleteCameraViewfromFile(position, false);
                dragAndDrop(true);
                //setBitmapImages(CreateItemSaleFragment.objectUploadPhoto.getAvailablePhotos(),imageView,position);
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

    private void deleteDefaultImages() {
        for (int i = 0; i < CreateItemSaleFragment.objectUploadPhoto.getAvailablePhotos().size(); i++) {
            if (!CreateItemSaleFragment.objectUploadPhoto.getAvailablePhotos().get(i).isRealImage()) {
                deleteCameraViewfromFile(i, false);
            }
        }
    }

    static int iLongPosition = 0;

    static class LongPressListener implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View view) {
            iLongPosition = (Integer) view.getTag();
            final ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(data, shadowBuilder, view, 0);
            view.setVisibility(View.INVISIBLE);
            return true;
        }
    }

    class DragListener implements View.OnDragListener {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            final View view = (View) event.getLocalState();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_LOCATION:
                    // do nothing if hovering above own position
                    if (view == v) return true;
                    // get the new list index
                    final int index = calculateNewIndex(event.getX(), event.getY());

                    final Rect rect = new Rect();
                    mScrollView.getHitRect(rect);
                    final int scrollY = mScrollView.getScrollY();

                    if (event.getY() - scrollY > mScrollView.getBottom() - 250) {
                        startScrolling(scrollY, mGrid.getHeight());
                    } else if (event.getY() - scrollY < mScrollView.getTop() + 250) {
                        startScrolling(scrollY, 0);
                    } else {
                        stopScrolling();
                    }
                    BlueShakLog.logDebug(TAG, "DragListener -----> " + index);
                    // remove the view from the old position
                    swapArrayList(CreateItemSaleFragment.objectUploadPhoto.getAvailablePhotos(), index, iLongPosition);
                    mGrid.removeView(view);
                    // and push to the new
                    mGrid.addView(view, index);
                    break;
                case DragEvent.ACTION_DROP:
                    view.setVisibility(View.VISIBLE);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    if (!event.getResult()) {
                        view.setVisibility(View.VISIBLE);
                    }
                    break;
            }
            return true;
        }
    }

    private void startScrolling(int from, int to) {
        if (from != to && mAnimator == null) {
            mIsScrolling.set(true);
            mAnimator = new ValueAnimator();
            mAnimator.setInterpolator(new OvershootInterpolator());
            mAnimator.setDuration(Math.abs(to - from));
            mAnimator.setIntValues(from, to);
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    mScrollView.smoothScrollTo(0, (int) valueAnimator.getAnimatedValue());
                }
            });
            mAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mIsScrolling.set(false);
                    mAnimator = null;
                }
            });
            mAnimator.start();
        }
    }

    private void stopScrolling() {
        if (mAnimator != null) {
            mAnimator.cancel();
        }
    }

    private int calculateNewIndex(float x, float y) {
        // calculate which column to move to
        final float cellWidth = mGrid.getWidth() / mGrid.getColumnCount();
        final int column = (int) (x / cellWidth);

        // calculate which row to move to
        final float cellHeight = mGrid.getHeight() / mGrid.getRowCount();
        final int row = (int) Math.floor(y / cellHeight);

        // the items in the GridLayout is organized as a wrapping list
        // and not as an actual grid, so this is how to get the new index
        int index = row * mGrid.getColumnCount() + column;
        if (index >= mGrid.getChildCount()) {
            index = mGrid.getChildCount() - 1;
        }
        // BlueShakLog.logDebug(TAG, "DragListener calculateNewIndex --------------------> " + index);
        return index;
    }

    boolean isImageEdit = false;

    class onClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int x = (Integer) view.getTag();
            BlueShakLog.logDebug(TAG, "onClickListener mPosition x --------------------> " + x);
            if (CreateItemSaleFragment.objectUploadPhoto.getAvailablePhotos().size() >= 1 &&
                    CreateItemSaleFragment.objectUploadPhoto.getAvailablePhotos().get(x).isRealImage()) {
                showAlert("Do you want edit or delete photo?", x);
            } else {
                isImageEdit = false;
                setImage();
            }
        }
    }

    private void sortArray(ArrayList<CreateImageModel> imageList) {
        Collections.sort(imageList, new Comparator<CreateImageModel>() {
            @Override
            public int compare(CreateImageModel mall1, CreateImageModel mall2) {

                boolean b1 = mall1.isRealImage();
                boolean b2 = mall2.isRealImage();

                return (b1 != b2) ? (b1) ? -1 : 1 : 0;
            }
        });
    }

    private void removedDataFromArrayListLast(ArrayList<CreateImageModel> imageList) {
        if (imageList.size() > 5) {
            for (int i = imageList.size() - 1; i >= 5; i--) {
                imageList.remove(i);
            }
        }
    }

    private void swapArrayList(ArrayList<CreateImageModel> imageList, int i, int j) {
        Collections.swap(imageList, i, j);
    }
}
