package com.langoor.app.blueshak.photos_add;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.langoor.blueshak.R;
import com.langoor.app.blueshak.AppController;
import com.langoor.app.blueshak.garage.CreateGarageSaleFragment;
import com.langoor.app.blueshak.garage.CreateItemSaleFragment;
import com.langoor.app.blueshak.global.GlobalFunctions;
import com.langoor.app.blueshak.global.GlobalVariables;
import com.langoor.app.blueshak.services.model.CreateImageModel;
import org.lucasr.twowayview.TwoWayView;
import java.io.File;
import java.util.ArrayList;

public class PhotosAddFragmentMain extends Fragment implements OnDeletePicture{

    private static String TAG = "PhotosAddFragmentMain";
    Object_PhotosAdd object_photosAdd = null;
    private static String PHOTOS_ADD_FRAGMENT_MAIN_OBJECT_KEY = "PhotosAddFragmentMainObjectKEY";
    private static String PHOTOS_ADD_FRAGMENT_EDIT_KEY = "PhotosAddFragmentMainObjectEditKEY";
    private static String PHOTOS_ADD_FRAGMENT_MAIN_LISTENER_KEY = "PhotosAddFragmentMainListenerKEY";
    ArrayList<CreateImageModel> removed_photos = new ArrayList<CreateImageModel>();
    Context context;
    Activity activity;
    TextView title_tv ;
    ImageView addIcon;
    ImageView addIcon_one;
    TwoWayView listView;
    PhotosAddListAdapter adapter;
    Uri imageUri;
    //PhotosAddListener listener;
    final int CROP_PIC = 2;
    private Uri picUri;
    GlobalFunctions globalFunctions = new GlobalFunctions();
    GlobalVariables globalVariables = new GlobalVariables();
    String edit_key="";
    protected static final int REQUEST_CHECK_CAMERA = 115;
    protected static final int REQUEST_CHECK_GALLARY = 118;
    public static PhotosAddFragmentMain newInstance(Context context, Object_PhotosAdd obj,String type_Edit){
        PhotosAddFragmentMain photosAddFragmentMain = new PhotosAddFragmentMain();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PHOTOS_ADD_FRAGMENT_MAIN_OBJECT_KEY, obj);
        bundle.putString(PHOTOS_ADD_FRAGMENT_EDIT_KEY, type_Edit);
        // bundle.putSerializable(PHOTOS_ADD_FRAGMENT_MAIN_LISTENER_KEY,listener);
        photosAddFragmentMain.setArguments(bundle);
        return photosAddFragmentMain;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.photos_add_main_fragment,null,false);
        context = getActivity();
        activity=getActivity();
        title_tv = (TextView) view.findViewById(R.id.photos_add_main_fragment_title_textview);
        addIcon = (ImageView) view.findViewById(R.id.photos_add_main_fragment_add_imageView);
        listView = (TwoWayView) view.findViewById(R.id.photos_add_main_fragment_photos_list);
        addIcon_one=(ImageView)view.findViewById(R.id.photos_add_main_fragment_add_imageView_add);

        object_photosAdd = (Object_PhotosAdd) getArguments().getSerializable(PHOTOS_ADD_FRAGMENT_MAIN_OBJECT_KEY);
         edit_key =getArguments().getString(PHOTOS_ADD_FRAGMENT_EDIT_KEY);
        if(edit_key.equalsIgnoreCase(GlobalVariables.TYPE_UPDATE_REQUEST))
            adapter = new PhotosAddListAdapter(getActivity(), object_photosAdd.getAvailablePhotos(),true,this,true);
        else
            adapter = new PhotosAddListAdapter(getActivity(), object_photosAdd.getAvailablePhotos(),true,this,false);

        //listener = (PhotosAddListener) getArguments().getSerializable(PHOTOS_ADD_FRAGMENT_MAIN_LISTENER_KEY);

        listView.setAdapter(adapter);

        addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(object_photosAdd!=null){
                    if(object_photosAdd.getAvailablePhotos().size()>=5){
                        addIcon_one.setVisibility(View.GONE);
                        Toast.makeText(context, "You can not add more than five images", Toast.LENGTH_SHORT).show();
                    }else
                        selectImage();
                }else
                    selectImage();

            }
        });
        addIcon_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(object_photosAdd!=null){
                    if(object_photosAdd.getAvailablePhotos().size()>=5){
                        Toast.makeText(context, "You can not add more than five images", Toast.LENGTH_SHORT).show();
                        addIcon_one.setVisibility(View.GONE);
                    }else
                        selectImage();
                }else
                    selectImage();

            }
        });

        return view ;
    }
    private Dialog alert;

    private void selectImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final CharSequence[] charSequences= new CharSequence[]{"Click a picture from camera", "Choose from gallery"};
        builder.setItems(R.array.select_dialog_items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){
                    if(checkIfAlreadyhavePermission())
                        invokeCamera();
                    else
                        checkCameraPermission();
                }else{
                    if(checkIfReadExternalStorageAlreadyhavePermission())
                        openGallery();
                    else
                        checkReadExternalStoragePermission();
                }
            }
        });
        alert = builder.create();
        alert.show();
    }

    private void setValues(){
        if(context!=null&&object_photosAdd!=null){
            title_tv.setText(context.getString(R.string.app_name));//object_photosAdd.getTitle());
            addIcon.setImageResource(object_photosAdd.getAddIconResID());
        }
    }

    @Override
    public void onResume() {
        setValues();
        if(object_photosAdd.getAvailablePhotos().size()>0){
            addIcon_one.setVisibility(View.VISIBLE);
            addIcon.setVisibility(View.GONE);
        }
        super.onResume();
    }

    private void openGallery(){
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select File"),globalVariables.REQUEST_FOR_SELECTFILE);
    }
    private void invokeCamera(){
        Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        String fileName;
        if(globalFunctions.getProfile(context)!=null)
             fileName = System.currentTimeMillis()+globalFunctions.getProfile(context).getId()+object_photosAdd.getAvailablePhotos().size()+".jpg";
        else
            fileName = System.currentTimeMillis()+"11"+object_photosAdd.getAvailablePhotos().size()+".jpg";

        Log.d(TAG,"##################fileName############# = "+fileName);
        File photoFile = globalFunctions.getImageFilePath(context,fileName);
        Log.d(TAG,"##################photoFile############# = "+photoFile);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
        imageUri = Uri.fromFile(photoFile);    Log.d(TAG,"##################imageUri############# = "+imageUri);
        startActivityForResult(cameraIntent, globalVariables.REQUEST_FOR_CAMERA);
    }


    public void refreshCameraImageList(){
        if(listView!=null&&adapter!=null&&object_photosAdd!=null){
            if(object_photosAdd.getAvailablePhotos().size()>0){
                listView.setVisibility(View.VISIBLE);
                if(object_photosAdd.getAvailablePhotos().size()<5)
                    if(addIcon_one.getVisibility()==View.GONE)
                        addIcon_one.setVisibility(View.VISIBLE);
            }else{
                listView.setVisibility(View.GONE);
            }
            synchronized (adapter){adapter.notifyDataSetChanged();}
            //listener.onRecreate(object_photosAdd);
            setRefreshData();
        }
    }

    public void deleteCameraViewfromFile(Context context, int position){
        try{
            String fileString = object_photosAdd.getAvailablePhotos().get(position).getImage();
            removed_photos.add(object_photosAdd.getAvailablePhotos().get(position));
            File file = new File(fileString);
            if(file.exists()){
                file.delete();
                file.getAbsoluteFile().delete();
                if(file.exists()){
                    context.getApplicationContext().deleteFile(file.getPath());
                }
                if(!file.exists()){
                    Toast.makeText(context, "File Deleted Successfully", Toast.LENGTH_SHORT).show();
                }
            }
            object_photosAdd.getAvailablePhotos().remove(position);
            object_photosAdd.setRemoved_photos(removed_photos);
            refreshCameraImageList();
        }catch(Exception e){
            Log.d(TAG,"Exception on Deleting Image : "+e);

        }

    }

    @Override
    public void onStop() {
        //listener.onRecreate(object_photosAdd);
        setRefreshData();
        super.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode !=getActivity().RESULT_OK) {
            return;
        }
        if(requestCode ==GlobalVariables.REQUEST_FOR_CROP_IMAGE) {
              //   Toast.makeText(context,"Coming inside the Crop Image in activity result",Toast.LENGTH_LONG).show();
              /*  //Create an instance of bundle and get the returned data
                Bundle extras = data.getExtras();
                //get the cropped bitmap from extras
           *//**//**//**//*     Uri thePic = extras.getParcelable("data");
            Uri uri=data
            File imagePath = new File(thePic);
            if(imagePath.exists()){
                globalFunctions.saveImageWithFixedPixels(imagePath.toString());
                CreateImageModel model = new CreateImageModel();
                model.setImage(imagePath.getAbsolutePath().toString());
                model.setDisplay(false);
                object_photosAdd.getAvailablePhotos().add(model);
                refreshCameraImageList();
            }*//**//**//**//**/

     }else if (requestCode == globalVariables.REQUEST_FOR_CAMERA) {
                //File photo = (File) data.getExtras().get("data");
                Uri selectedImageUri = imageUri;//data.getData();
                System.out.println("#############REQUEST_FOR_CAMERA###############");
                picUri=selectedImageUri;
             /*   CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(getActivity());*/
                if(selectedImageUri!=null){
                    String selectedImagePath = globalFunctions.getRealPathFromURI(context, selectedImageUri);
                    Log.d(TAG,"##############Path###### = "+selectedImagePath);
                    File imagePath = new File(selectedImagePath);
                 /*   performCrop(Uri.fromFile(imagePath));*/
                    if(imagePath.exists()){
                        globalFunctions.saveImageWithFixedPixels(imagePath.toString());
                        CreateImageModel model = new CreateImageModel();
                        model.setImage(imagePath.getAbsolutePath().toString());
                        model.setDisplay(false);
                        object_photosAdd.getAvailablePhotos().add(model);
                        refreshCameraImageList();
                    }
                }else{
                    Toast.makeText(context,"Something went wrong..please try again",Toast.LENGTH_LONG).show();
                }
        }else if (requestCode == globalVariables.REQUEST_FOR_SELECTFILE) {
                System.out.println("#############REQUEST_FOR_SELECTFILE###############");
                Uri selectedImageUri = data.getData();
                // Uri selectedImageUri = data.getExtras().getParcelable("data");
                picUri=selectedImageUri;

                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(getActivity(), selectedImageUri, projection, null, null,
                        null);
                Cursor cursor = cursorLoader.loadInBackground();
                if(cursor!=null){
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                    cursor.moveToFirst();
                    String selectedImagePath = cursor.getString(column_index);
                    Log.d(TAG,"Path = "+selectedImagePath);
                    File imagePath = new File(selectedImagePath);
                    Log.d(TAG,"performCrop Path= "+selectedImagePath);
                    Log.d(TAG,"Uri.fromFile(imagePath)= "+Uri.fromFile(imagePath));
                  /*  performCrop(Uri.fromFile(imagePath));*/
                    if(imagePath.exists()){
                        globalFunctions.saveImageWithFixedPixels(imagePath.toString());
                        CreateImageModel model = new CreateImageModel();
                        model.setImage(imagePath.getAbsolutePath().toString());
                        model.setDisplay(false);
                        object_photosAdd.getAvailablePhotos().add(model);
                        refreshCameraImageList();
                    }
                }else {
                    Toast.makeText(context,"Please try again",Toast.LENGTH_LONG).show();
                }
        }
    }

    @Override
    public void ondeleting(int position) {
        deleteCameraViewfromFile(context, position);
    }

    private void setRefreshData(){
       /*call the outer function to refresh list*/
        if(edit_key.equalsIgnoreCase(GlobalVariables.TYPE_CREATE_REQUEST))
            CreateItemSaleFragment.setImages();
        else
            CreateGarageSaleFragment.setImages();

    }
    public String getRealPathFromURI(Context context, Uri contentUri)
    {
        try{
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        catch (Exception e){

            return contentUri.getPath();

        }
    }
    /**
     * this function does the crop operation.
     */
    private void performCrop(Uri ImageUri) {
        // take care of exceptions
        try {
            System.out.println("####com.android.camera.action.CROP###############");
            // call the standard crop action intent (the user device may not
            // support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(ImageUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            cropIntent.putExtra("aspectX", 3);
            cropIntent.putExtra("aspectY", 2);
         /*   cropIntent.putExtra("scale", true);*/
            cropIntent.putExtra("return-data", true);
          /*  Toast.makeText(context,"startActivityForResult cropping",Toast.LENGTH_LONG).show();*/

            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, GlobalVariables.REQUEST_FOR_CROP_IMAGE);




        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {

            Toast toast = Toast
                    .makeText(context, "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    public void checkReadExternalStoragePermission() {
        Log.d(TAG,"checkLocationPermission######################");
        int permissionCheck_location_coarse = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCheck_write_coarse = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(permissionCheck_location_coarse != PackageManager.PERMISSION_GRANTED ||
                permissionCheck_write_coarse!=PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CHECK_GALLARY);
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
    public void checkCameraPermission() {
        int permissionCheck_location_coarse = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCheck_write_coarse = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCheck_camera = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        if(permissionCheck_location_coarse != PackageManager.PERMISSION_GRANTED ||
                permissionCheck_write_coarse!=PackageManager.PERMISSION_GRANTED
                || permissionCheck_camera!=PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CHECK_CAMERA);
        }


    }
    private boolean checkIfAlreadyhavePermission() {
        Log.d(TAG,"checkIfReadExternalStorageAlreadyhavePermission######################");
        int coarse_location = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int camera = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        if (coarse_location == PackageManager.PERMISSION_GRANTED && result == PackageManager.PERMISSION_GRANTED
                && camera == PackageManager.PERMISSION_GRANTED ) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d(TAG,"onRequestPermissionsResult ##########@@@@@@@@@@@@@@@@@@@@@@@@@");
        switch (requestCode) {
            case REQUEST_CHECK_CAMERA   :
                Log.d(TAG,"onRequestPermissionsResult ############REQUEST_CHECK_CAMERA");
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   invokeCamera();
                } else {
                    checkCameraPermission();
                }
                return;
            case REQUEST_CHECK_GALLARY:
                Log.d(TAG,"onRequestPermissionsResult ############REQUEST_CHECK_GALLARY");
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    checkReadExternalStoragePermission();
                }
                return;


            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


}
