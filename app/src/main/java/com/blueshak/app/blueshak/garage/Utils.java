package com.blueshak.app.blueshak.garage;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import com.blueshak.app.blueshak.services.model.CreateImageModel;
import com.blueshak.app.blueshak.util.BlueShakLog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import static com.zhihu.matisse.internal.utils.PathUtils.getPath;

/**
 * Created by lsingh013 on 03/12/17.
 */

public class Utils {
    static String  TAG = "Utils -->> ";
    public static void removedDataFromArrayListLast(ArrayList<CreateImageModel> imageList) {
        if (imageList!=null && imageList.size() > 5) {
            for (int i = imageList.size() - 1; i >= 5; i--) {
                imageList.remove(i);
            }
        }
    }

    public static void sortArray(ArrayList<CreateImageModel> imageList) {
        Collections.sort(imageList, new Comparator<CreateImageModel>() {
            @Override
            public int compare(CreateImageModel mall1, CreateImageModel mall2) {

                boolean b1 = mall1.isDisplay();
                boolean b2 = mall2.isDisplay();

                return (b1 != b2) ? (b1) ? -1 : 1 : 0;
            }
        });
    }

    public static void sortArrayBasedId(ArrayList<CreateImageModel> imageList) {
        Collections.sort(imageList, new Comparator<CreateImageModel>() {
            @Override
            public int compare(CreateImageModel mall1, CreateImageModel mall2) {
                return mall1.getImage_order() - mall2.getImage_order();
            }
        });
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                //matrix.setRotate(90);
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                //matrix.setRotate(90);
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap setRotatedBitmap(Context context,Bitmap bitmap, Uri selectedImagePath){
         ExifInterface exifObject = null;
        try {
            InputStream input = context.getContentResolver().openInputStream(selectedImagePath);
            if (Build.VERSION.SDK_INT > 23){
                exifObject = new ExifInterface(input);
            }else{
                exifObject = new ExifInterface(selectedImagePath.getPath());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exifObject.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        BlueShakLog.logDebug(TAG," orientation -> "+orientation);
        Bitmap imageRotate = rotateBitmap(bitmap,orientation);
        return imageRotate;
        //imageView.setImageBitmap(imageRotate);
    }

    public static Bitmap rotateBitmapOrientation(Context context, Bitmap imageAsync, Uri contentUri) throws IOException {
        String path = getFilePath(context, contentUri);
        ExifInterface exif = new ExifInterface(path);
        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
        // Rotate Bitmap
        Matrix matrix = new Matrix();
        // Create and configure BitmapFactory
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        matrix.setRotate(rotationAngle, (float) imageAsync.getWidth() / 2, (float) imageAsync.getHeight() / 2);
        imageAsync = Bitmap.createBitmap(imageAsync, 0, 0, imageAsync.getWidth(), imageAsync.getHeight(), matrix, true);
        return imageAsync;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static String getFilePath(Context context, Uri contentUri) {
        String wholeID = "";
        try {
            wholeID = DocumentsContract.getDocumentId(contentUri);
        } catch (Exception e) {
            return getPath(context, contentUri);
        }
        String id = wholeID.split(":")[1];
        String[] column = {MediaStore.Images.Media.DATA};

        String sel = MediaStore.Images.Media._ID + "=?";
        Cursor cursor = context.getContentResolver().
                query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{id}, null);

        String filePath = "";
        int columnIndex = cursor.getColumnIndex(column[0]);
        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);

        return resizedBitmap;
    }


    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    public static int getDefaultImageCount(ArrayList<CreateImageModel> availablePhotos){
        int iCount = 0;
        for(int i = 0; i < availablePhotos.size(); i++){
            if(!availablePhotos.get(i).isRealImage()){
                iCount = iCount+1;
            }
        }
        BlueShakLog.logDebug(TAG,"Count - > "+iCount);
        return iCount;
    }
    public static int getCountRealImage(ArrayList<CreateImageModel> availablePhotos){
        int iCount = 0;
        for(int i = 0; i < availablePhotos.size(); i++){
            if(availablePhotos.get(i).isRealImage()){
                iCount = iCount+1;
            }
        }
        BlueShakLog.logDebug(TAG,"Count - > "+iCount);
        return iCount;
    }
    public static ArrayList<CreateImageModel> getFilteredArrayList(ArrayList<CreateImageModel> availablePhotos){
        ArrayList<CreateImageModel> availablePh = new ArrayList<>();
        boolean isEmpty = false;
        for(int i = 0; i < availablePhotos.size(); i++){
            CreateImageModel modela = new CreateImageModel();
            if(availablePhotos.get(i).isDisplay()){
                modela.setImage(availablePhotos.get(i).getImage());
                modela.setId(availablePhotos.get(i).getId());
                modela.setDisplay(availablePhotos.get(i).isDisplay());
                modela.setRealImage(availablePhotos.get(i).isRealImage());
                modela.setImage_order(availablePhotos.get(i).getImage_order());
                availablePh.add(modela);
            }else{
                if(!isEmpty){
                    isEmpty = true;
                    modela.setImage(availablePhotos.get(i).getImage());
                    modela.setId(availablePhotos.get(i).getId());
                    modela.setDisplay(availablePhotos.get(i).isDisplay());
                    modela.setRealImage(availablePhotos.get(i).isRealImage());
                    modela.setImage_order(availablePhotos.get(i).getImage_order());
                    availablePh.add(modela);
                }
            }
        }
        sortArrayFirstEmpty(availablePh);
        return availablePh;
    }

    public static void sortArrayFirstEmpty(ArrayList<CreateImageModel> imageList) {
        Collections.sort(imageList, new Comparator<CreateImageModel>() {
            @Override
            public int compare(CreateImageModel mall1, CreateImageModel mall2) {

                boolean b1 = mall1.isDisplay();
                boolean b2 = mall2.isDisplay();

                return (b1 != b2) ? (b2) ? -1 : 1 : 0;
            }
        });
    }

    public static void setEditTextMaxLength(EditText edt_text) {
        InputFilter[] filterArray = new InputFilter[1];
        if(edt_text.getText().toString().contains(".")){
            filterArray[0] = new InputFilter.LengthFilter(11);
        }else{
            filterArray[0] = new InputFilter.LengthFilter(8);
        }
        edt_text.setFilters(filterArray);
    }

    public static void editTextWatcher(final EditText edt_text){
        edt_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                setEditTextMaxLength(edt_text);
            }
        });
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "profile", null);
        return Uri.parse(path);
    }
}
