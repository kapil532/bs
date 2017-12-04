package com.blueshak.app.blueshak.garage;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import com.blueshak.app.blueshak.services.model.CreateImageModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Created by lsingh013 on 03/12/17.
 */

public class Utils {
    static String  TAG = "Utils";
    public static void removedDataFromArrayListLast(ArrayList<CreateImageModel> imageList) {
        if (imageList.size() > 5) {
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
                return (int) (mall1.getId() - mall2.getId());
            }
        });
    }

    public static void getAddressFromLatLong(Context context, double LATITUDE, double LONGITUDE) {
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null && addresses.size() > 0) {
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
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

    public static Bitmap setRotatedBitmap(Bitmap bitmap, String selectedImagePath){
         ExifInterface exifObject = null;
        try {
            exifObject = new ExifInterface(selectedImagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exifObject.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        Bitmap imageRotate = rotateBitmap(bitmap,orientation);
        return imageRotate;
        //imageView.setImageBitmap(imageRotate);
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
}
