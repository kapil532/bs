package com.langoor.app.blueshak.garage;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
import com.langoor.app.blueshak.PickLocation;
import com.langoor.app.blueshak.global.GlobalFunctions;
import com.langoor.app.blueshak.global.GlobalVariables;
import com.langoor.app.blueshak.item.ProductDetail;
import com.langoor.app.blueshak.login.LoginActivity;
import com.langoor.app.blueshak.photos_add.Object_PhotosAdd;
import com.langoor.app.blueshak.photos_add.PhotosAddFragmentMain;
import com.langoor.app.blueshak.services.ServerResponseInterface;
import com.langoor.app.blueshak.services.ServicesMethodsManager;
import com.langoor.app.blueshak.services.model.BookmarkListModel;
import com.langoor.app.blueshak.services.model.CategoryListModel;
import com.langoor.app.blueshak.services.model.CategoryModel;
import com.langoor.app.blueshak.services.model.CreateProductModel;
import com.langoor.app.blueshak.services.model.CreateSalesModel;
import com.langoor.app.blueshak.services.model.ErrorModel;
import com.langoor.app.blueshak.services.model.HomeListModel;
import com.langoor.app.blueshak.services.model.IDModel;
import com.langoor.app.blueshak.services.model.LocationModel;
import com.langoor.app.blueshak.services.model.MyItemListAvailableModel;
import com.langoor.app.blueshak.services.model.MyItemListModel;
import com.langoor.app.blueshak.services.model.PostcodeListModel;
import com.langoor.app.blueshak.services.model.PostcodeModel;
import com.langoor.app.blueshak.services.model.ProductModel;
import com.langoor.app.blueshak.services.model.SalesModel;
import com.langoor.app.blueshak.services.model.StatusModel;
import com.langoor.app.blueshak.services.model.UserModel;
import com.langoor.app.blueshak.util.LocationListener;
import com.langoor.app.blueshak.util.LocationService;
import com.langoor.app.blueshak.view.AlertDialog;
import com.langoor.app.blueshak.view.MultiAutoCompletionView;
import com.langoor.app.blueshak.view_sales.MapFragmentSales;

import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CreateGarageSaleFragment extends Fragment{
    public static final String TAG = "CreateGarageSale";
    public static Context context;
    public static CreateProductModel productModel = new CreateProductModel();
    public static Object_PhotosAdd objectUploadPhoto = new Object_PhotosAdd();
    public static Activity activity;
    public static final String CREATE_GARRAGE_ACTIVITY_BUNDLE_KEY = "CreateGarrageActivityBundleKey";
    public static final String CREATE_GARRAGE_ACTIVITY_TYPE_BUNDLE_KEY = "CreateGarrageActivityTypeBundleKey";
    public static final String CREATE_GARRAGE_LOCATION_BUNDLE_KEY = "CreateItemActivityLocationBundleKey";
    public static final String  FROM_KEY= "from_key";
    public static final String CREATE_GARRAGE_ACTIVITY_SELECT_ITEMS_KEY = "CreateGarrageActivitySelectItemsKey";
    private Toolbar toolbar;
    private Boolean type_edit_sale=false;
    private TextView select_items;
    private EditText name,date,starttime,endtime,description;
    private Button new_item,publish;
    private String namestr,datestr,startstr,endstr,locstr,emailstr,phonestr,type,endDateStr,postalCode,descriptionstr;
    private int from_key;
    private DatePickerDialog toDatePickerDialog;
    private static CreateSalesModel createSalesModel = null;
    private static GlobalFunctions globalFunctions = new GlobalFunctions();
    private static GlobalVariables globalVariables = new GlobalVariables();
    private int tag;
    private GoogleApiClient mGoogleApiClient=null;
    private TextView mAutocompleteTextView;
    private String loading_label="Creating Sale...";
    private RecyclerView recycler_view;
    private MyItemListAdapter adapter;
    private LocationModel locationModel=new LocationModel();
    private List<ProductModel> product_list = new ArrayList<ProductModel>();
    private FragmentManager fragmentManager;
    private List<ProductModel> old_list=new ArrayList<ProductModel>();
    private static boolean item_avaialable=false;
    private LinearLayout add_item_content;
    private ProgressBar progress_bar;
    public static CreateGarageSaleFragment newInstance(Context context,CreateSalesModel sales,String type,int from){
        CreateGarageSaleFragment createGarageSaleFragment = new CreateGarageSaleFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(CREATE_GARRAGE_ACTIVITY_BUNDLE_KEY, sales);
        bundle.putSerializable(CREATE_GARRAGE_ACTIVITY_TYPE_BUNDLE_KEY, type);
        bundle.putInt(FROM_KEY, from);
        createGarageSaleFragment.setArguments(bundle);
        return createGarageSaleFragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
       /* setHasOptionsMenu(true);*/
        context= getActivity();
        activity=getActivity();
        View view = inflater.inflate(R.layout.list_create_garrage, container, false);
        try{
            progress_bar=(ProgressBar)view.findViewById(R.id.progress_bar);
            tag=getArguments().getInt(FROM_KEY,0);
            fragmentManager=getChildFragmentManager();
            name = (EditText) view.findViewById(R.id.creategarrage_name);
            date = (EditText) view.findViewById(R.id.creategarrage_date);
            starttime = (EditText) view.findViewById(R.id.creategarrage_starttime);
            endtime = (EditText) view.findViewById(R.id.creategarrage_endtime);
            description= (EditText) view.findViewById(R.id.creategarrage_description);
            select_items=(TextView)view.findViewById(R.id.select_items);
            recycler_view=(RecyclerView)view.findViewById(R.id.recycler_view);
            adapter = new MyItemListAdapter(activity, product_list,false);
            LinearLayoutManager linearLayoutManagerVertical=new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
            recycler_view.setLayoutManager(linearLayoutManagerVertical);
           /* recyclerView.addItemDecoration(new SpacesItemDecoration(
                    getResources().getDimensionPixelSize(R.dimen.space)));*/
           /* recycler_view.setItemAnimator(new DefaultItemAnimator());*/
            recycler_view.setAdapter(adapter);
            recycler_view.setNestedScrollingEnabled(false);
            recycler_view.addItemDecoration(new SimpleDividerItemDecoration(activity));
            add_item_content=(LinearLayout)view.findViewById(R.id.add_item_content);
            add_item_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=MyItems.newInstance(context,item_avaialable);
                    startActivityForResult(intent,globalVariables.REQUEST_CODE_SELECT_PRODUCT);

                }
            });
            select_items.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=MyItems.newInstance(context,item_avaialable);
                    startActivityForResult(intent,globalVariables.REQUEST_CODE_SELECT_PRODUCT);

                }
            });
            new_item=(Button) view.findViewById(R.id.new_item);
            new_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"Add New Item",Toast.LENGTH_LONG).show();
                }
            });
            publish=(Button) view.findViewById(R.id.publish);
            publish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickProcessing();
                    GlobalFunctions.removeSharedPreferenceKey(context,GlobalVariables.SELECTED_ITEMS_LIST);
                    /*addItems();*/
                }
            });
            mAutocompleteTextView = (TextView) view.findViewById(R.id.autoCompleteTextView);
            mAutocompleteTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= PickLocation.newInstance(context,GlobalVariables.TYPE_GARAGE_SALE,false,locationModel);
                    startActivityForResult(intent,globalVariables.REQUEST_CODE_FILTER_PICK_LOCATION);
                }
            });
            mAutocompleteTextView.setOnEditorActionListener(new DoneOnEditorActionListener());
            from_key = getArguments().getInt(FROM_KEY);
            createSalesModel = (CreateSalesModel) getArguments().getSerializable(CREATE_GARRAGE_ACTIVITY_BUNDLE_KEY);
            if(createSalesModel!=null){setValues();createSalesModel.setRequest_type(GlobalVariables.TYPE_UPDATE_REQUEST);}
            else{createSalesModel=new CreateSalesModel();createSalesModel.setSaleType(type);}
            date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int[] dateInts = GlobalFunctions.convertTexttoDate(date.getText().toString());
                    setDateTimeField(date, dateInts[1],dateInts[1],dateInts[0]);
                    globalFunctions.closeKeyboard(activity);
                }
            });
            starttime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    globalFunctions.closeKeyboard(activity);
                    getTimeDialog("start");
                }
            });

            endtime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getTimeDialog("end");
                    globalFunctions.closeKeyboard(activity);
                }
            });
            is_items_available(context);

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
    public void onResume() {
        super.onResume();
      /* activity.setActionBarTitle("Your title");*/
        ((CreateSaleActivity)getActivity()).setActionBarTitle("Garage Sale");
        uploadImageFragment();
        /*is_items_available(context);*/
    }

    public void Publish() {
        onClickProcessing();
    }

    private void onClickProcessing(){
        namestr = name.getText().toString();
        descriptionstr=description.getText().toString();
        datestr = date.getText().toString();
        startstr = starttime.getText().toString();
        endstr = endtime.getText().toString();
        locstr = mAutocompleteTextView.getText().toString();
        if(createSalesModel.getImages().size()==0){
            Toast.makeText(activity,"Please select a photo",Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(namestr)){
            Toast.makeText(activity,"Please enter the sale name",Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(descriptionstr)){
            Toast.makeText(activity,"Please enter the sale description",Toast.LENGTH_LONG).show();
        }else if(datestr.length()==0){
            Toast.makeText(activity,"Please fill the sale start date",Toast.LENGTH_LONG).show();
        }else if(startstr.length()==0){
            Toast.makeText(activity,"Please fill sale start time",Toast.LENGTH_LONG).show();
        }else if(endstr.length()==0){
            Toast.makeText(activity,"Please fill sale end time",Toast.LENGTH_LONG).show();
        }else if(!isEndTimeGstartTime(startstr,endstr)){
            Toast.makeText(activity,"End time should be minimum 15 minutes greater than start time",Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(locstr)){
            Toast.makeText(activity,"Please enter sale location",Toast.LENGTH_LONG).show();
        }else{
            /*String country=GlobalFunctions.getSharedPreferenceString(context,GlobalVariables.SHARED_PREFERENCE_COUNTRY);
            createSalesModel.setCountry_short(country);
           */ createSalesModel.setName(namestr);
            createSalesModel.setStart_date(datestr);
            createSalesModel.setEnd_date(/*getEndDate(datestr)*/datestr);
            createSalesModel.setStart_time(startstr);
            createSalesModel.setEnd_time(endstr);
            createSalesModel.setAddress(locstr);
            createSalesModel.setSale_address(locstr);
            createSalesModel.setSaleType(GlobalVariables.TYPE_GARAGE);
            createSalesModel.setDescription(descriptionstr);
            createSalesModel.setAddress(mAutocompleteTextView.getText().toString());
            if(!type_edit_sale)
                createSalesModel.setRequest_type(GlobalVariables.TYPE_CREATE_REQUEST);
            if(locationModel!=null){
                if(TextUtils.isEmpty(createSalesModel.getLatitude())){
                    createSalesModel.setLatitude(locationModel.getLatitude()+"");
                    createSalesModel.setLongitude(locationModel.getLongitude()+"");
                }
            }
            JSONArray old_jsonArray = new JSONArray();
            List<ProductModel> old_productModelList = old_list;
            for(int j=0;j<old_list.size();j++){
                old_jsonArray.put(old_list.get(j).getId());
                /*jsonArray.put(new JSONObject(productModelList.get(i).toString()));*/
            }
            Log.d("Create Sale#######", "####old_jsonArray########"+old_jsonArray);
            createSalesModel.setOld_array(old_jsonArray);
           /* createSalesModel.setOld_item_list(old_list);*/
            Log.d("Create Sale#######", "####old_list########"+old_list.size());
            refresh_the_list();
          /*  createSalesModel.setOld_item_list(old_list);*/
          /*  if(type_edit_sale)
                addItems();
            else
                createSalesModel.setItem_list(product_list);*/
            String token = GlobalFunctions.getSharedPreferenceString(context,GlobalVariables.SHARED_PREFERENCE_TOKEN);
            if(token!=null){
                showProgressBar();
                Log.d("Create Sale#######", createSalesModel.toString());
                createSale(context,createSalesModel);
            }else{
                showSettingsAlert();
            }
        }
    }
    private void createSale(final Context context, final CreateSalesModel createSalesModel){
        final String email_verification_error=context.getResources().getString(R.string.ErrorEmailVerification);
      /*  showProgressBar();*/
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.createSale(context, createSalesModel, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
               hideProgressBar();
                if(arg0 instanceof IDModel){
                    IDModel idModel = (IDModel) arg0;
                    createSalesModel.setSaleID(idModel.getId());
                    Toast.makeText(context, "Sale has been Published Successfully",Toast.LENGTH_LONG).show();
                    closeThisActivity();
                }else if(arg0 instanceof ErrorModel){
                    ErrorModel errorModel = (ErrorModel) arg0;
                    String msg = errorModel.getError()!=null ? errorModel.getError() : errorModel.getMessage();
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                }else if(arg0 instanceof StatusModel){
                    Toast.makeText(context, "Sale has been Updated Successfully",Toast.LENGTH_LONG).show();
                    ProductDetail.closeThisActivity();
                    /* new ProductDetail().refreshPage(context);*/
                    SalesModel salesModel=new SalesModel();
                    salesModel.setId(createSalesModel.getSaleID());
                    closeThisActivity();
                    Intent intent = ProductDetail.newInstance(context,null,salesModel,GlobalVariables.TYPE_MY_SALE);
                    context.startActivity(intent);
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
    private void setValues(){
        if(createSalesModel != null){
            loading_label="Updating Sale...";
            publish.setText("Update Sale");
            type_edit_sale=true;
          /*  select_items.setVisibility(View.GONE);*/
            old_list=createSalesModel.getItem_list();
            name.setText(createSalesModel.getName());
            description.setText(createSalesModel.getDescription());
            date.setText(createSalesModel.getStart_date());
            starttime.setText(createSalesModel.getStart_time());
            endtime.setText(createSalesModel.getEnd_time());
            mAutocompleteTextView.setText(createSalesModel.getSale_address());
            createSalesModel.setRequest_type(GlobalVariables.TYPE_UPDATE_REQUEST);
            product_list.clear();
            String label="";
            String data = "";
            product_list=createSalesModel.getItem_list();
            if(product_list.size()==1)
                label="1 Item added";
            else
                label=product_list.size()+" Items added";
            select_items.setText(label);
            for(ProductModel productModel:product_list){
                productModel.setIs_selected(true);
                data=data+productModel.getId()+ ",";

            }
            adapter=new MyItemListAdapter(context,product_list,false);
            recycler_view.setAdapter(adapter);
            synchronized (adapter){adapter.notifyDataSetChanged();}



        }
    }
    public void getTimeDialog(final String cmd){
        Calendar mcurrentTime = Calendar.getInstance();
        final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        final int minute = mcurrentTime.get(Calendar.MINUTE);
        final TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if(cmd.equals("start")) {
                    starttime.setText(String.format("%02d", selectedHour) + ":" + String.format("%02d", selectedMinute));
                    InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(starttime.getWindowToken(), 0);
                }else if(cmd.equals("end")){
                    endtime.setText(String.format("%02d", selectedHour) + ":" + String.format("%02d", selectedMinute));
                    InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(endtime.getWindowToken(), 0);
                }
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }
    private void setDateTimeField(final EditText editText, int year, int month, int day) {

        Calendar newCalendar = Calendar.getInstance();
        day = day==0? newCalendar.get(Calendar.DAY_OF_MONTH) : day;
        month = month==0? newCalendar.get(Calendar.MONTH) : month+1;
        year = year==0? newCalendar.get(Calendar.YEAR) : year;

        toDatePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                editText.setText(globalVariables.dateFormatter.format(newDate.getTime()));
            }

        },year, month, day);
        toDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        toDatePickerDialog.show();
    }

    public String getEndDate(String untildate){
        //String untildate="2011-10-08";//can take any date in current format
        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
        Calendar cal = Calendar.getInstance();
        try{
            cal.setTime( dateFormat.parse(untildate));
        }catch (ParseException e){
            e.printStackTrace();
        }

        cal.add(Calendar.DATE, 1 );
        String convertedDate=dateFormat.format(cal.getTime());
        System.out.println("Date increase by one.."+convertedDate);
        return convertedDate;
    }
    private void uploadImageFragment(){
        objectUploadPhoto.setAvailablePhotos(createSalesModel.getImages());
        Fragment aboutFragment = PhotosAddFragmentMain.newInstance(activity,objectUploadPhoto,GlobalVariables.TYPE_UPDATE_REQUEST);
        getChildFragmentManager().beginTransaction().replace(R.id.container_upload_image, aboutFragment, "uploadImage").commit();
    }
    public static void setImages(){
        if(objectUploadPhoto!=null && createSalesModel!=null){
            createSalesModel.setImages(objectUploadPhoto.getAvailablePhotos());
            createSalesModel.setRemove_images(objectUploadPhoto.getRemoved_photos());
        }

    }
    @Override
    public void onDestroy() {
        super.onDestroy();


    }
    @Override
    public void onPause() {
        super.onPause();

    }


    public static void closeThisActivity(){
        if(activity!=null){
            activity.finish();}
    }
    public void showSettingsAlert(){
        final AlertDialog alertDialog = new AlertDialog(context);
        alertDialog.setMessage("Please Login/Register to continue");
        alertDialog.setPositiveButton("Continue", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent creategarrage = new Intent(context,LoginActivity.class);
                startActivity(creategarrage);
                   closeThisActivity();
            }
        });
        alertDialog.show();
    }
    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
            Log.i(TAG,"on activity result");
            Log.i(TAG,"request code "+resultCode);
            if(resultCode == activity.RESULT_OK){
                Log.i(TAG,"result ok ");
                Log.i(TAG,"request code "+globalVariables.REQUEST_CODE_FILTER_PICK_LOCATION);
                if(requestCode==globalVariables.REQUEST_CODE_FILTER_PICK_LOCATION){
                    LocationModel location_model = (LocationModel) data.getExtras().getSerializable(CREATE_GARRAGE_LOCATION_BUNDLE_KEY);
                    Log.i(TAG,"name pm"+location_model.getFormatted_address());
                    locationModel=location_model;
                    productModel.setLatitude(location_model.getLatitude());
                    productModel.setLongitude(location_model.getLongitude());
                    productModel.setAddress(location_model.getFormatted_address());
                    productModel.setSuburb(location_model.getSubhurb());
                    productModel.setCity(location_model.getCity());
                    mAutocompleteTextView.setText(location_model.getFormatted_address());
                }else if(requestCode==globalVariables.REQUEST_CODE_SELECT_PRODUCT){
                    Log.i(TAG,"result ok ");
                    Log.i(TAG,"request code "+globalVariables.REQUEST_CODE_SELECT_PRODUCT);
                    HomeListModel homeListModel = (HomeListModel) data.getExtras().getSerializable(CREATE_GARRAGE_ACTIVITY_SELECT_ITEMS_KEY);
                    if(homeListModel!=null){
                        if(type_edit_sale){
                           /* for (ProductModel productModel:homeListModel.getItem_list()){
                                for (int i=0;i<product_list.size();i++){
                                    if(!productModel.getId().equalsIgnoreCase(product_list.get(i).getId())){
                                        Log.d(TAG,"###Adding############"+productModel.getId().equalsIgnoreCase(product_list.get(i).getId()));
                                        product_list.add(productModel);
                                        break;
                                    }
                                }
                                *//*  product_list.addAll(homeListModel.getItem_list());*//*
                            }*/
                            product_list.addAll(homeListModel.getItem_list());
                        }else
                            product_list=homeListModel.getItem_list();
                    }
                    adapter=new MyItemListAdapter(context,product_list,false);
                    recycler_view.setAdapter(adapter);
                    synchronized (adapter){adapter.notifyDataSetChanged();}
                    if (adapter.getItemCount() > 1) {
                        // scrolling to bottom of the recycler view
                        recycler_view.getLayoutManager().smoothScrollToPosition(recycler_view, null, adapter.getItemCount() - 1);
                    }
                    if(select_items!=null && product_list.size()>0){
                        String label="";
                        if(product_list.size()==1)
                            label="1 Item added";
                        else
                            label=product_list.size()+" Items added";
                        select_items.setText(label);
                        select_items.setTextColor(context.getResources().getColor(R.color.brand_text_color));
                      /*  Drawable img = getContext().getResources().getDrawable( R.drawable.ic_create_white_24dp );
                        img.setBounds( 0, 0, 60, 60 );
                        select_items.setCompoundDrawables( null, null, img, null );*/
                    }else{
                        select_items.setText("Add Items");
                    }

                }

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

    private void is_items_available(final Context context){
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getAvailableItemsListCount(context,new MyItemListAvailableModel(), new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                if(arg0 instanceof StatusModel){
                    StatusModel statusModel = (StatusModel) arg0;
                }else if(arg0 instanceof ErrorModel){
                    ErrorModel errorModel = (ErrorModel) arg0;
                    String msg = errorModel.getError()!=null ? errorModel.getError() : errorModel.getMessage();
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                }else if(arg0 instanceof MyItemListAvailableModel){
                    MyItemListAvailableModel model = (MyItemListAvailableModel) arg0;
                    if(model.isItem_available()){
                        item_avaialable=true;
                        select_items.setText("Add Items");
                    }else{
                        select_items.setText("Add New Item");
                    }
                }
            }

            @Override
            public void OnFailureFromServer(String msg) {
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void OnError(String msg) {
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            }
        }, "Get My Item List");
    }
    public void refresh_the_list(){
        String data = "";
        List<ProductModel> stList = ((MyItemListAdapter) adapter).getProductList();
        List<ProductModel> selected_List =new ArrayList<ProductModel>();
        selected_List.clear();
        for (int i = 0; i < stList.size(); i++) {
            ProductModel productModel = stList.get(i);
            if (productModel.is_selected() == true) {
                data = data + "\n" + productModel.getName().toString();
                selected_List.add(productModel);
            }
        }
        product_list.clear();
        product_list.addAll(selected_List);
        createSalesModel.setItem_list(product_list);
    }
    public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
        private Drawable mDivider;

        public SimpleDividerItemDecoration(Context context) {
            mDivider = context.getResources().getDrawable(R.drawable.line_divider);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();

                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }
    public void showProgressBar(){
        if(progress_bar!=null)
            progress_bar.setVisibility(View.VISIBLE);
    }
    public void hideProgressBar(){
        if(progress_bar!=null)
            progress_bar.setVisibility(View.GONE);
    }
    private boolean isEndTimeGstartTime(String starttime, String endtime) {
        String pattern = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            Date date1 = sdf.parse(starttime);
            Date date2 = sdf.parse(endtime);
            long difference = date2.getTime() - date1.getTime();
            if(date2.after(date1)) {
                if(difference>=900000)
                    return true;
                else
                    return false;
            } else {
                return false;
            }
        } catch (ParseException e){
            e.printStackTrace();
        }
        return false;
    }

}
