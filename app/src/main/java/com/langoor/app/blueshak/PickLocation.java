package com.langoor.app.blueshak;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.awareness.state.BeaconState;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.langoor.app.blueshak.login.LoginActivity;
import com.langoor.app.blueshak.register.SignUpSCreenActivity;
import com.langoor.app.blueshak.search.SearchActivity;
import com.langoor.blueshak.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.langoor.app.blueshak.filter.FilterActivity;
import com.langoor.app.blueshak.garage.CreateSaleActivity;
import com.langoor.app.blueshak.garage.PlaceArrayAdapter;
import com.langoor.app.blueshak.global.GlobalFunctions;
import com.langoor.app.blueshak.global.GlobalVariables;
import com.langoor.app.blueshak.profile.ProfileEditActivity;
import com.langoor.app.blueshak.root.RootActivity;
import com.langoor.app.blueshak.services.ServerResponseInterface;
import com.langoor.app.blueshak.services.ServicesMethodsManager;
import com.langoor.app.blueshak.services.model.LocationModel;
import com.langoor.app.blueshak.util.LocationListener;
import com.langoor.app.blueshak.util.LocationService;

import static com.google.android.gms.location.places.AutocompleteFilter.TYPE_FILTER_CITIES;

public class PickLocation extends RootActivity implements LocationListener, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks{
    private GoogleApiClient mGoogleApiClient;
    private Context context;
    private Button save;
    private static Activity activity;
    private Button current_location;
    private Double lat,lng;
    private static LatLngBounds BOUNDS_MOUNTAIN_VIEW ;
    private  LocationService locServices=null; private AutoCompleteTextView mAutocompleteTextView;
    private final String TAG = "PickLocation";
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private  LocationModel locationModel=new LocationModel();
    public  static  final String FROM="from";
    public  static  final String WORLD_WIDE_BUNDLE_KEY="world_wide_bundle_key";
    public  static  final String LOCATION_MODEL_BUNDLE_KEY="locationmodelbundlekey";
    private  int from=0;
    static GlobalVariables globalVariables = new GlobalVariables();
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private boolean proceed=false;
    private TextView close_button;
    private LinearLayout seporater;
    private ImageView go_back;
    private ProgressBar progress_bar;
    private boolean world_wide=false;
    private AutocompleteFilter  typeFilter=null;
    private EditText street_number;
    private String country;
    String address;
    public static Intent newInstance(Context context,int from,boolean world_wide,LocationModel locationModel){
        Intent mIntent = new Intent(context,PickLocation.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(FROM, from);
        bundle.putBoolean(WORLD_WIDE_BUNDLE_KEY, world_wide);
        bundle.putSerializable(LOCATION_MODEL_BUNDLE_KEY, locationModel);
        mIntent.putExtras(bundle);
        return mIntent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_location);
        try{
            context=this;
            activity=this;
            progress_bar=(ProgressBar)findViewById(R.id.progress_bar);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            LayoutInflater inflator = LayoutInflater.from(this);
            View v = inflator.inflate(R.layout.action_bar_titlel, null);
            ((TextView)v.findViewById(R.id.title)).setText("Pick Location");
            toolbar.addView(v);
            close_button=(TextView)v.findViewById(R.id.cancel);
            seporater=(LinearLayout)findViewById(R.id.seporater);
            close_button.setVisibility(View.GONE);
            go_back=(ImageView)findViewById(R.id.go_back);
            go_back.setVisibility(View.VISIBLE);
            go_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   closeThisActivity();
                }
            });
            Intent i=getIntent();
            if(i!=null) {
                if (i.hasExtra(FROM)) {
                    from = getIntent().getExtras().getInt(FROM);
                }
                if (i.hasExtra(WORLD_WIDE_BUNDLE_KEY)) {
                    world_wide = getIntent().getExtras().getBoolean(WORLD_WIDE_BUNDLE_KEY);
                }
                if(from==GlobalVariables.TYPE_GARAGE_SALE || from==GlobalVariables.TYPE_ADD_TEMS)
                    locationModel=(LocationModel)i.getSerializableExtra(LOCATION_MODEL_BUNDLE_KEY);
                    if(locationModel==null)
                        locationModel=new LocationModel();
            }
            street_number=(EditText)findViewById(R.id.street_number);
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(LocationServices.API)
                 /*   .enableAutoManage(getActivity(),GOOGLE_API_CLIENT_ID, this)*/
                    .addConnectionCallbacks(this)
                    .build();
            if(checkIfAlreadyhavePermission()){
                Log.d(TAG,"checkIfAlreadyhavePermission is dere#########");
                if(isGpsEnabled()){
                    Log.d(TAG,"isGpsEnabled is dere#########");
                    locServices = new LocationService(activity);
                    lat=locServices.getLatitude();lng=locServices.getLongitude();
                    Log.d(TAG,"##############isGpsEnabled lat: "+lat+"Lng :"+lng);
                    double radiusDegrees = 1.0;
                    LatLng northEast = new LatLng(lat+ radiusDegrees,lng + radiusDegrees);
                    LatLng southWest = new LatLng(lat-radiusDegrees,lng-radiusDegrees);
                    BOUNDS_MOUNTAIN_VIEW = LatLngBounds.builder()
                            .include(northEast)
                            .include(southWest)
                            .build();
                    locServices.setListener(this);
                }else {
                    Log.d(TAG,"isGpsEnabled not enabled dere#########");
                    settingsrequest();
                }
            }else{
                Log.d(TAG,"checkLocationPermission not dere########");
                checkLocationPermission();
            }
            current_location=(Button)findViewById(R.id.current_location);
            current_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    proceed=true;
                    /*if(!locServices.canGetLocation()){
                        settingsrequest();
                        locationModel.setLongitude(Bangalore_longitude);
                        locationModel.setLatitude(Bangalore_latitude);
                        locationModel.setFormatted_address(Bangalore_address);
                        locationModel.setCity(Bangalore_address);
                        locationModel.setState(Bangalore_address);
                        locationModel.setSubhurb(Bangalore_address);
                        Intent i=MainActivity.newInstance(context,locationModel,null);
                        startActivity(i);
                    }else{*/
                    if(locServices!=null){
                        lat=locServices.getLatitude();lng=locServices.getLongitude();
                        getAddressFromLatLng();
                    }

                   /* }*/


                }
            });
            mAutocompleteTextView = (AutoCompleteTextView)findViewById(R.id
                    .autoCompleteTextView);
            mAutocompleteTextView.setThreshold(3);
            mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
            /*if(GlobalFunctions.is_loggedIn(context) && (from==GlobalVariables.TYPE_ADD_TEMS || from==GlobalVariables.TYPE_GARAGE_SALE)){
                country=GlobalFunctions.getSharedPreferenceString(context,GlobalVariables.SHARED_PREFERENCE_COUNTRY);
            }else{
                country=GlobalFunctions.getSharedPreferenceString(context,GlobalVariables.SHARED_PREFERENCE_LOCATION_COUNTRY);
            }*/
            country=GlobalFunctions.getSharedPreferenceString(context,GlobalVariables.SHARED_PREFERENCE_LOCATION_COUNTRY);

            if(country==null){
                if(locServices!=null) {
                    lat = locServices.getLatitude();
                    lng = locServices.getLongitude();
                    setCountry(context,lat,lng);
                }
            }
            if(world_wide){
                Log.d(TAG,"world_wide###################");
                 if(from==GlobalVariables.TYPE_FILTER_ACTIVITY){
                     Log.d(TAG,"#########world_wide from Filter Activity##########");
                    typeFilter = new AutocompleteFilter.Builder()
                            .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                            .build();
                 }
                mPlaceArrayAdapter = new PlaceArrayAdapter(activity, android.R.layout.simple_list_item_1,
                        BOUNDS_MOUNTAIN_VIEW,typeFilter);

            }else{
                Log.d(TAG,"Country###################"+country);
                if(from==GlobalVariables.TYPE_FILTER_ACTIVITY){
                    typeFilter = new AutocompleteFilter.Builder()
                            .setCountry(country)
                            .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                            .build();
                }else{
                    typeFilter = new AutocompleteFilter.Builder()
                            .setCountry(country)
                            .build();
                }
                mPlaceArrayAdapter = new PlaceArrayAdapter(activity, android.R.layout.simple_list_item_1,
                        BOUNDS_MOUNTAIN_VIEW, typeFilter);


            }
            mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);

            save=(Button) findViewById(R.id.save);
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(from==GlobalVariables.TYPE_GARAGE_SALE)
                        if(!TextUtils.isEmpty(street_number.getText().toString())){
                            locationModel.setStree_number(street_number.getText().toString().trim());
                            address=street_number.getText().toString().trim()+"/"+mAutocompleteTextView.getText().toString().trim();
                        }else
                            address=mAutocompleteTextView.getText().toString().trim();
                    else
                        address=mAutocompleteTextView.getText().toString().trim();

                    if(!TextUtils.isEmpty(mAutocompleteTextView.getText().toString().trim())){
                        /*setCity(mAutocompleteTextView.getText().toString());*/
                        locationModel.setFormatted_address(address);
                        locationModel.setAutocomplete_address(mAutocompleteTextView.getText().toString().trim());
                        GlobalFunctions.setSharedPreferenceString(activity,GlobalVariables.CURRENT_LOCATION,mAutocompleteTextView.getText().toString());
                        Log.i(TAG, "Selected: " + locationModel.getFormatted_address()+" Latitude :"+locationModel.getLatitude()+" Longitude :"+locationModel.getLongitude());
                        if(from==GlobalVariables.TYPE_FILTER_ACTIVITY || from==GlobalVariables.TYPE_ADD_TEMS || from==GlobalVariables.TYPE_MY_PROFILE || from==GlobalVariables.TYPE_SIGN_UP||from==GlobalVariables.TYPE_LOGIN || from==GlobalVariables.TYPE_GARAGE_SALE){
                            setReturnResult(locationModel);
                        }else{
                            closeThisActivity();
                            Intent i=MainActivity.newInstance(context,locationModel,null);
                            startActivity(i);

                        }
                    }else
                        mAutocompleteTextView.setError("Please enter location");
                }
            });

            if(from==GlobalVariables.TYPE_FILTER_ACTIVITY ){
                seporater.setVisibility(View.GONE);
                current_location.setVisibility(View.GONE);
                mAutocompleteTextView.setImeOptions(EditorInfo.IME_ACTION_DONE);
            }else if(from==GlobalVariables.TYPE_GARAGE_SALE){
                seporater.setVisibility(View.GONE);
                current_location.setVisibility(View.GONE);
                street_number.setVisibility(View.VISIBLE);
                if(locationModel!=null){
                    street_number.setText(locationModel.getStree_number()!=null?locationModel.getStree_number():"");
                    mAutocompleteTextView.setText(locationModel.getAutocomplete_address());

                }
                mAutocompleteTextView.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                street_number.setImeOptions(EditorInfo.IME_ACTION_DONE);
            }else if(from==GlobalVariables.TYPE_ADD_TEMS){
                seporater.setVisibility(View.GONE);
                current_location.setVisibility(View.GONE);
                if(locationModel!=null){
                    mAutocompleteTextView.setText(locationModel.getAutocomplete_address());
                }
                mAutocompleteTextView.setImeOptions(EditorInfo.IME_ACTION_DONE);
            }else{
                mAutocompleteTextView.setImeOptions(EditorInfo.IME_ACTION_DONE);
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
    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null){
            mGoogleApiClient.connect();

        }

    }
    public void setCity(String Address){
        String [] arr=new String[10];
        if(!TextUtils.isEmpty(Address)){
            if(Address.contains(",")){
                arr=Address.split(",");
                if(arr.length>=1){
                    locationModel.setSubhurb(arr[arr.length-2]);
                    locationModel.setState(arr[arr.length-2]);
                    locationModel.setCity(arr[arr.length-1]);
                }else {
                    locationModel.setSubhurb(Address);
                    locationModel.setState(Address);
                    locationModel.setCity(Address);
                }
            }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
       /* mGoogleApiClient.stopAutoManage(getActivity());*/
        if (mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
    }

    @Override
    public void onPause() {
        super.onPause();
      /*  mGoogleApiClient.stopAutoManage(getActivity());*/
        if (mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
    }
    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG,"onConnected##############");
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
    }
    @Override
    public void onProviderEnabled(String provider) {

    }
    @Override
    public void onLocationChanged(Location loc) {
        /*if(loc!=null){
            locationModel.setLatitude(Double.toString(loc.getLatitude()));
            locationModel.setLongitude(Double.toString(loc.getLongitude()));
          locServices.removeListener();
        }*/

    }
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(TAG, "Fetching details for ID: " + item.placeId);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();
            if(proceed)
                proceed=false;
            lat=place.getLatLng().latitude;lng=place.getLatLng().longitude;
            getAddressFromLatLng();
            locationModel.setLatitude(Double.toString(place.getLatLng().latitude));
            locationModel.setLongitude(Double.toString(place.getLatLng().longitude));
        }
    };

    private void getAddressFromLatLng(){
        showProgressBar();
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getAddress(activity, lat,lng, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                hideProgressBar();
                GlobalFunctions.closeKeyboard(activity);
                 locationModel =(LocationModel)arg0;
                 if(locationModel !=null){
                     locationModel.setLongitude(Double.toString(lng));
                     locationModel.setLatitude(Double.toString(lat));
                     Log.i(TAG, "Selected: " + locationModel.getFormatted_address()+" Latitude :"+locationModel.getLatitude()+" Longitude :"+locationModel.getLongitude());
                     GlobalFunctions.setSharedPreferenceString(activity,GlobalVariables.CURRENT_LOCATION, locationModel.getFormatted_address());
                     if(proceed){
                         if(from==GlobalVariables.TYPE_FILTER_ACTIVITY || from==GlobalVariables.TYPE_ADD_TEMS || from==GlobalVariables.TYPE_MY_PROFILE || from==GlobalVariables.TYPE_SIGN_UP|| from==GlobalVariables.TYPE_LOGIN){
                             setReturnResult(locationModel);
                         }else{
                             Intent i=MainActivity.newInstance(context,locationModel,null);
                             startActivity(i);
                             closeThisActivity();
                            /* closeThisActivity();*/
                         }
                     }

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
    private void setReturnResult(LocationModel locationModel){
        if(locationModel!=null){
            Bundle bundle = new Bundle();Intent result;
            if(from==GlobalVariables.TYPE_ADD_TEMS || from==GlobalVariables.TYPE_GARAGE_SALE){
                result = new Intent(activity,CreateSaleActivity.class);
                bundle.putSerializable(CreateSaleActivity.CREATE_GARRAGE_ACTIVITY_LOCATION_BUNDLE_KEY,locationModel);
            }else if(from==GlobalVariables.TYPE_MY_PROFILE){
                result = new Intent(activity,ProfileEditActivity.class);
                bundle.putSerializable(ProfileEditActivity.PROFILE_ACTIVITY_LOCATION_BUNDLE_KEY,locationModel);
            }else if(from==GlobalVariables.TYPE_SIGN_UP){
                result = new Intent(activity,SignUpSCreenActivity.class);
                bundle.putSerializable(SignUpSCreenActivity.BUNDLE_KEY_SIGN_UP_MODEL_SERIALIZABLE,locationModel);
            }else if(from==GlobalVariables.TYPE_LOGIN){
                result = new Intent(activity,LoginActivity.class);
                bundle.putSerializable(LoginActivity.BUNDLE_KEY_LOGIN_MODEL_SERIALIZABLE,locationModel);
            }else{
                result = new Intent(activity,FilterActivity.class);
                bundle.putSerializable(FilterActivity.BUNDLE_KEY_FILTER_MODEL_SERIALIZABLE,locationModel);
            }
            result.putExtras(bundle);
            setResult(this.RESULT_OK,result);
            closeThisActivity();
        }else{
            setResult(this.RESULT_CANCELED);
            closeThisActivity();
        }

    }
    public void settingsrequest()
    {
        Log.d(TAG,"settingsrequest");
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        Log.d(TAG,"##########Gps Enabled##############");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.d(TAG,"##########GpRESOLUTION_REQUIRED##############");
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.d(TAG,"##########SETTINGS_CHANGE_UNAVAILABLE##############");
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.d(TAG,"onActivityResult");
                        Log.d(TAG,"onActivityResult");
                        locServices=new LocationService(activity);
                        if(mGoogleApiClient!=null)
                            mGoogleApiClient.disconnect();

                        mGoogleApiClient = new GoogleApiClient.Builder(context)
                                .addApi(Places.GEO_DATA_API)
                                .addApi(LocationServices.API)
             /*   .enableAutoManage(getActivity(),GOOGLE_API_CLIENT_ID, this)*/
                                .addConnectionCallbacks(this)
                                .build();
                        mGoogleApiClient.connect();
                        lat=locServices.getLatitude();lng=locServices.getLongitude();
                        Log.d(TAG,"##############onActivityResult lat: "+lat+"Lng :"+lng);
                        setCountry(context,lat,lng);
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(context,context.getResources().getString(R.string.location_update_request),Toast.LENGTH_LONG).show();
                        settingsrequest();//keep asking if imp or do whatever
                        break;
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 145: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(isGpsEnabled()){
                        Log.d(TAG,"isGpsEnabled in permission check");
                        // permission was granted, yay! Do the
                        // contacts-related task you need to do.
                        locServices = new LocationService(activity);
                        if(mGoogleApiClient!=null)
                            mGoogleApiClient.disconnect();
                        mGoogleApiClient = new GoogleApiClient.Builder(context)
                                .addApi(Places.GEO_DATA_API)
                                .addApi(LocationServices.API)
                                /*.enableAutoManage(getActivity(),GOOGLE_API_CLIENT_ID, this)*/
                                .addConnectionCallbacks(this)
                                .build();
                        mGoogleApiClient.connect();
                        lat=locServices.getLatitude();lng=locServices.getLongitude();
                        setCountry(context,lat,lng);
                        double radiusDegrees = 1.0;
                        LatLng northEast = new LatLng(lat+ radiusDegrees,lng + radiusDegrees);
                        LatLng southWest = new LatLng(lat-radiusDegrees,lng-radiusDegrees);
                        BOUNDS_MOUNTAIN_VIEW = LatLngBounds.builder()
                                .include(northEast)
                                .include(southWest)
                                .build();
                        locServices.setListener(this);
                    }else{
                        Log.d(TAG,"Gps is not ENabled");
                        if(mGoogleApiClient!=null)
                            mGoogleApiClient.disconnect();
                        mGoogleApiClient = new GoogleApiClient.Builder(context)
                                .addApi(Places.GEO_DATA_API)
                                .addApi(LocationServices.API)
                                /*.enableAutoManage(getActivity(),GOOGLE_API_CLIENT_ID, this)*/
                                .addConnectionCallbacks(this)
                                .build();
                        mGoogleApiClient.connect();
                        settingsrequest();
                    }


                } else {
                    checkLocationPermission();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        if(mGoogleApiClient!=null)
            mGoogleApiClient.disconnect();
    }
    public void checkLocationPermission() {
        Log.d(TAG,"checkLocationPermission######################");
        int permissionCheck_location_coarse = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);
        if(permissionCheck_location_coarse != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION}, 145);
        }

    }
    private boolean checkIfAlreadyhavePermission() {
        int coarse_location = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (coarse_location == PackageManager.PERMISSION_GRANTED && result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    private boolean isGpsEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            return  true;
        }else{
           return false;
        }
    }
    public void getLocationInstance(){
        if(checkIfAlreadyhavePermission()){
            Log.d(TAG,"checkIfAlreadyhavePermission is dere#########");
            if(isGpsEnabled()){
                Log.d(TAG,"isGpsEnabled is dere#########");
                locServices = new LocationService(activity);
                lat=locServices.getLatitude();lng=locServices.getLongitude();
                double radiusDegrees = 1.0;
                LatLng northEast = new LatLng(lat+ radiusDegrees,lng + radiusDegrees);
                LatLng southWest = new LatLng(lat-radiusDegrees,lng-radiusDegrees);
                BOUNDS_MOUNTAIN_VIEW = LatLngBounds.builder()
                        .include(northEast)
                        .include(southWest)
                        .build();
                locServices.setListener(this);
                if(mGoogleApiClient!=null)
                    mGoogleApiClient.disconnect();
                mGoogleApiClient = new GoogleApiClient.Builder(context)
                        .addApi(Places.GEO_DATA_API)
                        .addApi(LocationServices.API)
                                /*.enableAutoManage(getActivity(),GOOGLE_API_CLIENT_ID, this)*/
                        .addConnectionCallbacks(this)
                        .build();
                mGoogleApiClient.connect();

            }else {
                Log.d(TAG,"isGpsEnabled not enabled dere#########");
                settingsrequest();
            }
        }else{
            Log.d(TAG,"checkLocationPermission not dere########");
            checkLocationPermission();
        }
    }
    public void accessPermissions(Activity activity) {
        int permissionCheck_getAccounts = ContextCompat.checkSelfPermission(activity, Manifest.permission.GET_ACCOUNTS);
        int permissionCheck_location_coarse = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionCheck_location_fine = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionCheck_vibrate = ContextCompat.checkSelfPermission(activity, Manifest.permission.VIBRATE);
        int permissionCheck_lockwake = ContextCompat.checkSelfPermission(activity, Manifest.permission.WAKE_LOCK);
        int permissionCheck_internet = ContextCompat.checkSelfPermission(activity, Manifest.permission.INTERNET);
        int permissionCheck_Access_internet = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_NETWORK_STATE);
        int permissionCheck_Access_wifi = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_WIFI_STATE);
        int permissionCheck_External_storage = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCheck_Internal_storage = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        int permissionCheck_cam = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);

        if (permissionCheck_lockwake != PackageManager.PERMISSION_GRANTED || permissionCheck_Access_wifi != PackageManager.PERMISSION_GRANTED || permissionCheck_getAccounts != PackageManager.PERMISSION_GRANTED || permissionCheck_vibrate != PackageManager.PERMISSION_GRANTED || permissionCheck_internet != PackageManager.PERMISSION_GRANTED || permissionCheck_Access_internet != PackageManager.PERMISSION_GRANTED || permissionCheck_External_storage != PackageManager.PERMISSION_GRANTED || permissionCheck_cam != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WAKE_LOCK) && ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_WIFI_STATE) && ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.GET_ACCOUNTS) && ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.VIBRATE) && ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.INTERNET) && ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_NETWORK_STATE) && ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WAKE_LOCK, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.VIBRATE, Manifest.permission.GET_ACCOUNTS, Manifest.permission.CAMERA}, globalVariables.PERMISSIONS_REQUEST_CALENDER);
            }
        }

        if (permissionCheck_internet != PackageManager.PERMISSION_GRANTED || permissionCheck_Access_internet != PackageManager.PERMISSION_GRANTED || permissionCheck_External_storage != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.INTERNET) && ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_NETWORK_STATE) && ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, globalVariables.PERMISSIONS_REQUEST_PRIMARY);
            }
        }


        if(permissionCheck_cam != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {

            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, globalVariables.PERMISSIONS_REQUEST_CAMERA);
            }
        }
        if(permissionCheck_Internal_storage != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, globalVariables.PERMISSIONS_REQUEST_CAMERA);
            }
        }
        if(permissionCheck_location_coarse != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, globalVariables.PERMISSIONS_REQUEST_CAMERA);
            }
        }
        if(permissionCheck_location_fine != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, globalVariables.PERMISSIONS_REQUEST_CAMERA);
            }
        }
        if(permissionCheck_Internal_storage != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 145);
            }
        }
        GlobalFunctions.setSharedPreferenceInt(activity, GlobalVariables.SHARED_PREFERENCE_PERMISSION,1);
      /*  Toast.makeText(context,"Please use current location",Toast.LENGTH_LONG).show();*/
    }
    public static void closeThisActivity(){
        Log.d("closeThisActivity","closeThisActivity the pick location issues#######");
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
    private void setCountry(final Context context, Double lattitude, Double longitude){
        showProgressBar();
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getAddress(activity, lattitude,longitude, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                hideProgressBar();
                GlobalFunctions.closeKeyboard(activity);
                locationModel=(LocationModel)arg0;
                Log.d(TAG,"###########Setting the Current Country SHARED_PREFERENCE_LOCATION_COUNTRY############");
                if(locationModel!=null){
                    country=locationModel.getCountry_code();
                  GlobalFunctions.setSharedPreferenceString(context,GlobalVariables.SHARED_PREFERENCE_LOCATION_COUNTRY,locationModel.getCountry_code());
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
}
