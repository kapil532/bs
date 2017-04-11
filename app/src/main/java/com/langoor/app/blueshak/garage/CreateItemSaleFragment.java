package com.langoor.app.blueshak.garage;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.langoor.app.blueshak.currency.CurrencyActivity;
import com.langoor.app.blueshak.services.model.CurrencyModel;
import com.langoor.blueshak.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.langoor.app.blueshak.AppController;
import com.langoor.app.blueshak.MainActivity;
import com.langoor.app.blueshak.PickLocation;
import com.langoor.app.blueshak.category.CategoryActivity;
import com.langoor.app.blueshak.global.GlobalFunctions;
import com.langoor.app.blueshak.global.GlobalVariables;
import com.langoor.app.blueshak.item.ProductDetail;
import com.langoor.app.blueshak.login.LoginActivity;
import com.langoor.app.blueshak.photos_add.Object_PhotosAdd;
import com.langoor.app.blueshak.photos_add.PhotosAddFragmentMain;
import com.langoor.app.blueshak.services.ServerResponseInterface;
import com.langoor.app.blueshak.services.ServicesMethodsManager;
import com.langoor.app.blueshak.services.model.CategoryListModel;
import com.langoor.app.blueshak.services.model.CategoryModel;
import com.langoor.app.blueshak.services.model.CreateProductModel;
import com.langoor.app.blueshak.services.model.ErrorModel;
import com.langoor.app.blueshak.services.model.IDModel;
import com.langoor.app.blueshak.services.model.LocationModel;
import com.langoor.app.blueshak.services.model.PostcodeListModel;
import com.langoor.app.blueshak.services.model.PostcodeModel;
import com.langoor.app.blueshak.services.model.ProductModel;
import com.langoor.app.blueshak.services.model.SalesModel;
import com.langoor.app.blueshak.services.model.StatusModel;
import com.langoor.app.blueshak.util.LocationListener;
import com.langoor.app.blueshak.util.LocationService;
import com.langoor.app.blueshak.view.MultiAutoCompletionView;
import com.tokenautocomplete.TokenCompleteTextView;
import java.util.ArrayList;
import java.util.List;

public class CreateItemSaleFragment extends Fragment  implements TokenCompleteTextView.TokenListener{

    String TAG = "CreateItemSaleFragment";
    String[] ary;
    public static final String CREATE_ITEM_BUNDLE_KEY = "CreateItemSaleFragmentBundleKey";
    public static final String CREATE_ITEM_SALE_BUNDLE_KEY = "CreateItemSelectSaleFragmentBundleKey";
    public static final String CREATE_ITEM_TYPE_BUNDLE_KEY = "CreateItemSaleFragmentTypeBundleKey";
    public static final String CREATE_ITEM_LOCATION_BUNDLE_KEY = "CreateItemActivityLocationBundleKey";
    public static final String CREATE_ITEM_CATEGORY_BUNDLE_KEY = "CreateItemActivityCategoryBundleKey";
    public static final String CREATE_ITEM_CURRENCY_BUNDLE_KEY = "CreateItemActivityCurrencyBundleKey";
    public static final String  FROM_KEY= "from_key";
    private Toolbar toolbar;
    private TextView category;
    private EditText name,description,saleprice,address;
    private Switch shippable,nagotiable,is_new_old;
    private Button save;
    private boolean[] is_checked;
    static Activity activity;
    private static final int GOOGLE_API_CLIENT_ID = 1;
    private CategoryListModel clm;
    private List<CategoryModel> list_cm = new ArrayList<CategoryModel>();
    private ArrayList<String> selectedCategoryString = new ArrayList<String>();
    private ArrayList<String> selectedCategoryIDs = new ArrayList<String>();
    private static Object_PhotosAdd objectUploadPhoto = new Object_PhotosAdd();
    private static GlobalFunctions globalFunctions = new GlobalFunctions();
    private static GlobalVariables globalVariables = new GlobalVariables();
    private static CreateProductModel productModel = null;
    private TextView mAutocompleteTextView,select_sale,pd_salepricetype;
    private String str_name,str_desc,str_sp,locstr,descriptionstr,catogarystr,postalCode;
    boolean isAvailable = true;
    private static ArrayList<PostcodeModel> autoSuggesstionsList = new ArrayList<PostcodeModel>();
    private static ArrayList<PostcodeModel> selectedAutoSuggesstionsList = new ArrayList<PostcodeModel>();
    private ArrayAdapter<PostcodeModel> autoCompletionAdapter = null;
    /*Zipcodes not needed*/
   /* private MultiAutoCompletionView zipcode_actv;*/
    private int from_key;
    private Context context;
    private LocationModel locationModel=new LocationModel();
    private View view;
    private Boolean type_edit_item=false;
    private Boolean type_garage=false;
    private String loading_label="Publishing Item...";
    private LinearLayout category_content,add_to_garage_sale_content;
    private ProgressBar progress_bar;
    public static CreateItemSaleFragment newInstance(Context context, CreateProductModel sales, LocationModel locationModel,String type, int from){
        CreateItemSaleFragment createItemSaleFragment = new CreateItemSaleFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(CREATE_ITEM_BUNDLE_KEY, sales);
        bundle.putSerializable(CREATE_ITEM_TYPE_BUNDLE_KEY, type);
        bundle.putSerializable(CREATE_ITEM_LOCATION_BUNDLE_KEY, locationModel);
        bundle.putInt(FROM_KEY, from);
        createItemSaleFragment.setArguments(bundle);
        return createItemSaleFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
       /* setHasOptionsMenu(true);*/
        context= getActivity();
        activity=getActivity();
        view = inflater.inflate(R.layout.item_productdetails_new, container, false);
        try{
            progress_bar=(ProgressBar)view.findViewById(R.id.progress_bar);
            name = (EditText) view.findViewById(R.id.pd_name);
            description = (EditText)view.findViewById(R.id.pd_description);
            saleprice = (EditText) view.findViewById(R.id.pd_saleprice);
            category = (TextView) view.findViewById(R.id.pd_category);
            pd_salepricetype = (TextView) view.findViewById(R.id.pd_salepricetype);
            String currency=GlobalFunctions.getSharedPreferenceString(context,GlobalVariables.SHARED_PREFERENCE_CURRENCY);
            pd_salepricetype.setText(currency);
            if(currency!=null)
                saleprice.setHint("Price in "+currency);
            pd_salepricetype.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= CurrencyActivity.newInstance(context,pd_salepricetype.getText().toString());
                    startActivityForResult(intent,globalVariables.REQUEST_CODE_SELECT_CURRENCY);

                }
            });
            mAutocompleteTextView = (TextView) view.findViewById(R.id
                    .pick_location);
            mAutocompleteTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= PickLocation.newInstance(context,GlobalVariables.TYPE_ADD_TEMS,false,locationModel);
                    startActivityForResult(intent,globalVariables.REQUEST_CODE_FILTER_PICK_LOCATION);
                }
            });
            category_content=(LinearLayout)view.findViewById(R.id.category_content);
            category_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= CategoryActivity.newInstance(context,false,category.getText().toString());
                    startActivityForResult(intent,globalVariables.REQUEST_CODE_SELECT_CATEGORY);
                }
            });
            add_to_garage_sale_content=(LinearLayout)view.findViewById(R.id.add_to_garage_sale_content);
            add_to_garage_sale_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= GarageSalesList.newInstance(context,"");
                    startActivityForResult(intent,globalVariables.REQUEST_CODE_SELECT_SALE);
                }
            });
            save = (Button)view.findViewById(R.id.pd_publish);
            select_sale=(TextView)view.findViewById(R.id.select_sale);
            mAutocompleteTextView.setOnEditorActionListener(new DoneOnEditorActionListener());
            shippable = (Switch)view.findViewById(R.id.pd_shippable);
            nagotiable = (Switch)view.findViewById(R.id.pd_nagotiable);
            is_new_old= (Switch)view.findViewById(R.id.is_new_old);
            productModel = (CreateProductModel) getArguments().getSerializable(CREATE_ITEM_BUNDLE_KEY);
            locationModel=(LocationModel)getArguments().getSerializable(CREATE_ITEM_LOCATION_BUNDLE_KEY);
            if(productModel!=null){
                    setValues();
            }else{productModel=new CreateProductModel();}
            from_key = getArguments().getInt(FROM_KEY);

            category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= CategoryActivity.newInstance(context,false,category.getText().toString());
                    startActivityForResult(intent,globalVariables.REQUEST_CODE_SELECT_CATEGORY);
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
    return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        clm = GlobalFunctions.getCategories(activity);
                        if(clm!=null)
                            is_checked = new boolean[clm.getCategoryNames().size()];
                    }
                });
            }
        }).start();


    }
    @Override
    public void onDestroy() {
        super.onDestroy();

    }
    public void setLocation(LocationModel locationModel){
        this.locationModel=locationModel;
        Toast.makeText(context,"setLocation",Toast.LENGTH_LONG).show();
        if(mAutocompleteTextView!=null)
            mAutocompleteTextView.setText(locationModel.getFormatted_address());
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    private void onClickProcessing(){
        if(nagotiable.isChecked())
            productModel.setNegotiable(true);
        if(is_new_old.isChecked())
            productModel.setIs_pickup(true);

        postalCode = null;
        for(int  i=0;i<selectedAutoSuggesstionsList.size();i++){
            postalCode = postalCode==null?selectedAutoSuggesstionsList.get(i).getId():postalCode+","+selectedAutoSuggesstionsList.get(i).getId();
        }
        str_name = name.getText().toString();
        str_desc = description.getText().toString();
        str_sp = saleprice.getText().toString();
        String currency=pd_salepricetype.getText().toString();
        if(clm!=null)
            selectedCategoryIDs = clm.getIdsforNames(selectedCategoryString);
        if(productModel.getImages().size()==0){
            Toast.makeText(activity,"Please select a photo",Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(str_name)){
            Toast.makeText(activity,"Please enter the product name",Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(currency)){
            Toast.makeText(activity,"Please select the currency",Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(str_sp)){
            Toast.makeText(activity,"Please enter the product price",Toast.LENGTH_LONG).show();
        }else if(str_sp.length()>9){
            Toast.makeText(activity,"Please enter the valid product price",Toast.LENGTH_LONG).show();
        }else if(selectedCategoryIDs.isEmpty()){
            Toast.makeText(activity,"Please fill the product Category",Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(str_desc)){
            Toast.makeText(activity,"Please enter the product description",Toast.LENGTH_LONG).show();
        }/*else if(shippable.isChecked() && TextUtils.isEmpty(mAutocompleteTextView.getText().toString())){
            *//*zipcode_actv.setError("Please enter the product zip code");*//*
            Toast.makeText(activity,"Please enter the product zip code",Toast.LENGTH_LONG).show();
        }*/else if(TextUtils.isEmpty(mAutocompleteTextView.getText().toString())){
            Toast.makeText(activity,"Please fill the product location",Toast.LENGTH_LONG).show();
        }else if(!shippable.isChecked() && !is_new_old.isChecked()){
            Toast.makeText(activity,"Item should be either shippable or pick up",Toast.LENGTH_LONG).show();
        }else{
            /*String country=GlobalFunctions.getSharedPreferenceString(context,GlobalVariables.SHARED_PREFERENCE_COUNTRY);
            productModel.setCountry_short(country);
          */  productModel.setAddress(mAutocompleteTextView.getText().toString());
            productModel.setName(str_name);
            productModel.setDescription(str_desc);
            productModel.setSalePrice(str_sp);
            productModel.setCurrency(currency);
            productModel.setCategories(selectedCategoryIDs);
            productModel.setPostcodes(postalCode);
            if(type_garage)
                productModel.setSaleType(GlobalVariables.TYPE_GARAGE);
            else
                productModel.setSaleType(GlobalVariables.TYPE_ITEM);

            if(type_edit_item)
                productModel.setRequest_type(GlobalVariables.TYPE_UPDATE_REQUEST);
            else
                productModel.setRequest_type(GlobalVariables.TYPE_CREATE_REQUEST);

            if(locationModel!=null){
                if(TextUtils.isEmpty(productModel.getLatitude())){
                    productModel.setLatitude(locationModel.getLatitude()+"");
                    productModel.setLongitude(locationModel.getLongitude()+"");
                }
            }
            if(nagotiable.isChecked())
                productModel.setNegotiable(true);
            else
                productModel.setNegotiable(false);
            if(shippable.isChecked())
                productModel.setShippable(true);
            else
                productModel.setShippable(false);
            if(is_new_old.isChecked())
                productModel.setIs_pickup(true);
            else
                productModel.setIs_pickup(false);

            String token = GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
            if(token!=null){
                showProgressBar();
                createSaleItem(context,productModel);
            }else{
                showSettingsAlert();
            }

        }
    }
    public static void setImages(){
        if(objectUploadPhoto!=null && productModel!=null){
            productModel.setImages(objectUploadPhoto.getAvailablePhotos());
            productModel.setRemove_images(objectUploadPhoto.getRemoved_photos());
        }

    }
    private void createSaleItem(final Context context, final CreateProductModel createProductModel){
        final String email_verification_error=context.getResources().getString(R.string.ErrorEmailVerification);
       /* showProgressBar();*/
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.createSaleItem(context, createProductModel, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                hideProgressBar();
                if(arg0 instanceof IDModel){
                    IDModel idModel = (IDModel) arg0;
                    createProductModel.setProduct_id(idModel.getId());
                    Toast.makeText(context, "Item has been listed Successfully", Toast.LENGTH_LONG).show();
                    closeThisActivity();
                }else if(arg0 instanceof ErrorModel){
                    ErrorModel errorModel = (ErrorModel) arg0;
                    String msg = errorModel.getError()!=null ? errorModel.getError() : errorModel.getMessage();
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                }else if (arg0 instanceof StatusModel){
                    hideProgressBar();
                    StatusModel statusModel=(StatusModel)arg0;
                    if(statusModel.isStatus()){
                        Toast.makeText(context,"Item has been updated Successfully.", Toast.LENGTH_LONG).show();
                        ProductDetail.closeThisActivity();
                        ProductModel productModel=new ProductModel();
                        productModel.setId(createProductModel.getProduct_id());
                        closeThisActivity();
                        Intent intent = ProductDetail.newInstance(context,productModel,null,GlobalVariables.TYPE_MY_SALE);
                        context.startActivity(intent);
                    }
                  }
            }

            @Override
            public void OnFailureFromServer(String msg) {
                hideProgressBar();
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
                if(msg!=null){
                    if(msg.equalsIgnoreCase(email_verification_error))
                        GlobalFunctions.showEmailVerificatiomAlert(context);
                    else
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                }
            }
        }, "Create Sale");

    }
    @Override
    public void onResume() {
        super.onResume();

        uploadImageFragment();
        ((CreateSaleActivity)getActivity()).setActionBarTitle("New Item");

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickProcessing();
            }
        });

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    if(productModel!=null){
                        if(productModel.getImages().size()!=0||
                                !TextUtils.isEmpty(name.getText().toString())||
                                !TextUtils.isEmpty(description.getText().toString())||
                                !TextUtils.isEmpty(saleprice.getText().toString())||
                                !selectedCategoryIDs.isEmpty()||
                                !TextUtils.isEmpty(mAutocompleteTextView.getText().toString())
                                ){
                            showExitAlert();
                        }else
                            activity.finish();
                    }else
                        activity.finish();


                    return true;

                }

                return false;
            }
        });

    }

    private void uploadImageFragment(){
        objectUploadPhoto.setAvailablePhotos(productModel.getImages());
        Fragment aboutFragment = PhotosAddFragmentMain.newInstance(activity,objectUploadPhoto,GlobalVariables.TYPE_CREATE_REQUEST);
        getChildFragmentManager().beginTransaction().replace(R.id.container_upload_image, aboutFragment, "uploadImage").commit();
    }

    private void setValues(){
        if(productModel!=null){
            loading_label="Updating Item...";
            save.setText("Update");
            type_edit_item=true;
            add_to_garage_sale_content.setVisibility(View.GONE);
            if(productModel.getSaleType()!=null){
                if(productModel.getSaleType().equalsIgnoreCase(GlobalVariables.TYPE_GARAGE)){
                    type_garage=true;
                }
            }
            name.setText(productModel.getName());
            description.setText(productModel.getDescription());
            saleprice.setText(productModel.getSalePrice());
            pd_salepricetype.setText(productModel.getCurrency());
            shippable.setChecked(productModel.isShippable()?true:false);
           /* is_new_old.setChecked(productModel.is_product_new()?true:false);*/
            is_new_old.setChecked(productModel.is_pickup()?true:false);
            nagotiable.setChecked(productModel.isNegotiable()?true:false);
            if(productModel.getAddress()!=null && !TextUtils.isEmpty( productModel.getAddress()))
                mAutocompleteTextView.setText(productModel.getAddress());
            else
                getAddressFromLatLng(Double.parseDouble(productModel.getLatitude()),Double.parseDouble(productModel.getLongitude()));
            selectedCategoryIDs.clear();
            selectedCategoryIDs.addAll(productModel.getCategories());
            if(clm!=null){
                selectedCategoryString.clear();
                selectedCategoryString.addAll(clm.getNamesforIDs(selectedCategoryIDs));
                setCategory();
            }else{
                clm=GlobalFunctions.getCategories(context);
                selectedCategoryString.clear();
                selectedCategoryString.addAll(clm.getNamesforIDs(selectedCategoryIDs));
                setCategory();
            }
           /* createSaleItem(context,productModel);*/
        }
    }
    private void setCategory()
    {
        if(selectedCategoryString.size()>0){
            String temp = "";
            for(int i=0;i<selectedCategoryString.size();i++){
                temp = i==0? selectedCategoryString.get(0): temp+", "+selectedCategoryString.get(i);
            }
            if(category!=null){category.setText(temp);
                category.setFocusable(false);
             /*   if(shippable.isChecked())
                    zipcode_actv.requestFocus();*/
            }
        }

    }

    @Override
    public void onTokenAdded(Object obj) {
        PostcodeModel model = (PostcodeModel) obj;
        selectedAutoSuggesstionsList.add(model);
    }

    @Override
    public void onTokenRemoved(Object obj) {

        PostcodeModel model = (PostcodeModel) obj;
        for(int i=0;i<selectedAutoSuggesstionsList.size();i++){
            if(selectedAutoSuggesstionsList.get(i).getId().equalsIgnoreCase(model.getId())){
                selectedAutoSuggesstionsList.remove(i);break;
            }
        }
    }


    public void showExitAlert(){
        final com.langoor.app.blueshak.view.AlertDialog alertDialog = new com.langoor.app.blueshak.view.AlertDialog(activity);
        alertDialog.setTitle("Are you sure?");
        alertDialog.setMessage("Are you sure to stop listing this item? The data you've created will be lost.");
        alertDialog.setPositiveButton("Yes", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(from_key==GlobalVariables.TYPE_AC_SIGN_UP)
                    startActivity(new Intent(getActivity(),MainActivity.class));
                else
                    getActivity().finish();
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("No", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
    public void showSuccesAlert(String title,String message){
        final com.langoor.app.blueshak.view.AlertDialog alertDialog = new com.langoor.app.blueshak.view.AlertDialog(activity);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("Add more items", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent intent=new Intent(activity,CreateSaleActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*/
                Intent i=CreateSaleActivity.newInstance(activity,null,null,null,GlobalVariables.TYPE_HOME,GlobalVariables.TYPE_ITEM);
                startActivity(i);
                alertDialog.dismiss();
                closeThisActivity();

            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Continue shopping", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent intent=new Intent(activity,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*/
                alertDialog.dismiss();
                closeThisActivity();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
    public void showSettingsAlert(){
        final com.langoor.app.blueshak.view.AlertDialog alertDialog = new com.langoor.app.blueshak.view.AlertDialog(context);
        alertDialog.setMessage("Please Login/Register to continue");
        alertDialog.setPositiveButton("Continue", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent creategarrage = new Intent(activity,LoginActivity.class);
                startActivity(creategarrage);
                closeThisActivity();
            }
        });
        alertDialog.show();
    }
    public static void closeThisActivity(){
        if(activity!=null){activity.finish();}
    }


    @Override
    public void onStop() {
        super.onStop();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
            Log.i(TAG,"on activity result");
            if(resultCode == activity.RESULT_OK){
                Log.i(TAG,"result ok ");
                Log.i(TAG,"request code "+requestCode);
                if(requestCode==globalVariables.REQUEST_CODE_FILTER_PICK_LOCATION){
                    Log.i(TAG,"REQUEST_CODE_FILTER_PICK_LOCATION "+requestCode);
                    LocationModel location_model = (LocationModel) data.getExtras().getSerializable(CREATE_ITEM_LOCATION_BUNDLE_KEY);
                    Log.i(TAG,"name pm"+location_model.getFormatted_address());
                    locationModel=location_model;
                    productModel.setLatitude(location_model.getLatitude());
                    productModel.setLongitude(location_model.getLongitude());
                    productModel.setAddress(location_model.getFormatted_address());
                    productModel.setSuburb(location_model.getSubhurb());
                    productModel.setCity(location_model.getCity());
                    mAutocompleteTextView.setText(location_model.getFormatted_address());
                }else if(requestCode==globalVariables.REQUEST_CODE_SELECT_CATEGORY){
                    Log.i(TAG,"REQUEST_CODE_SELECT_CATEGORY "+requestCode);
                    CategoryModel categoryModel = (CategoryModel) data.getExtras().getSerializable(CREATE_ITEM_CATEGORY_BUNDLE_KEY);
                    category.setText(categoryModel.getName());
                    selectedCategoryString.clear();
                    selectedCategoryString.add(categoryModel.getName());
                }else if(requestCode==globalVariables.REQUEST_CODE_SELECT_SALE){
                    Log.i(TAG,"REQUEST_CODE_SELECT_SALE "+requestCode);
                    SalesModel salesModel = (SalesModel) data.getExtras().getSerializable(CREATE_ITEM_SALE_BUNDLE_KEY);
                    type_garage=true;
                    select_sale.setText(salesModel.getName());
                    productModel.setSale_id(salesModel.getId());
                    productModel.setSaleType(GlobalVariables.TYPE_GARAGE);
                }else if(requestCode==globalVariables.REQUEST_CODE_SELECT_CURRENCY){
                    Log.i(TAG,"REQUEST_CODE_SELECT_CATEGORY "+requestCode);
                    CurrencyModel currencyModel = (CurrencyModel) data.getExtras().getSerializable(CREATE_ITEM_CURRENCY_BUNDLE_KEY);
                    pd_salepricetype.setText(currencyModel.getCurrency());
                    productModel.setCurrency(currencyModel.getCurrency());
                    saleprice.setHint("Price in "+currencyModel.getCurrency());
                    GlobalFunctions.setSharedPreferenceString(context,GlobalVariables.SHARED_PREFERENCE_CURRENCY,currencyModel.getCurrency());
                 /*   select_sale.setText(salesModel.getName());
                    productModel.setSale_id(salesModel.getId());*/
                }
            }
        } catch (NullPointerException e){
            Log.d(TAG,"NullPointerException");
            e.printStackTrace();
            Crashlytics.log(e.getMessage());
        }catch (NumberFormatException e) {
            Crashlytics.log(e.getMessage());
            Log.d(TAG,"NumberFormatException");
            e.printStackTrace();
        }catch (Exception e){
            Crashlytics.log(e.getMessage());
            Log.d(TAG,"Exception");
            e.printStackTrace();
        }
    }
     class DoneOnEditorActionListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onClickProcessing();
                return true;
            }
            return false;
        }
    }
    private void getAddressFromLatLng(Double lat,Double lng){
       showProgressBar();
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getAddress(activity, lat,lng, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
            hideProgressBar();
                GlobalFunctions.closeKeyboard(activity);
                locationModel =(LocationModel)arg0;
                if(locationModel !=null){
                    mAutocompleteTextView.setText(locationModel.getFormatted_address());
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
        }, "Fetching Current Location");


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

