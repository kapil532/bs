package com.blueshak.app.blueshak;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blueshak.app.blueshak.filter.FilterActivity;
import com.blueshak.app.blueshak.garage.CreateSaleActivity;
import com.blueshak.app.blueshak.global.GlobalFunctions;
import com.blueshak.app.blueshak.global.GlobalVariables;
import com.blueshak.app.blueshak.login.LoginActivity;
import com.blueshak.app.blueshak.profile.ProfileEditActivity;
import com.blueshak.app.blueshak.register.SignUpSCreenActivity;
import com.blueshak.app.blueshak.services.ServerResponseInterface;
import com.blueshak.app.blueshak.services.ServicesMethodsManager;
import com.blueshak.app.blueshak.services.model.LocationModel;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.blueshak.app.blueshak.util.LocationService;
import com.blueshak.app.blueshak.view_sales.LocationAddress;
import com.blueshak.blueshak.R;

/**
 * Created by Kapil Katiyar on 5/15/2017.
 */

public class PickLocationFromMap extends AppCompatActivity implements GoogleMap.OnMapClickListener,OnMapReadyCallback
{
    GoogleMap map;
    private static Activity activity;
    TextView location_Text;
    private TextView close_button;
    private LinearLayout seporater;
    private ImageView go_back;
    public  static  final String FROM="from";
    public  static  final String WORLD_WIDE_BUNDLE_KEY="world_wide_bundle_key";
    public  static  final String LOCATION_MODEL_BUNDLE_KEY="locationmodelbundlekey";
    private  LocationModel locationModel=new LocationModel();
    private  int from=0;
    private ProgressBar progress_bar;
    public static Intent newInstance(Context context, int from, boolean world_wide, LocationModel locationModel){
        Intent mIntent = new Intent(context,PickLocation.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(FROM, from);
        bundle.putBoolean(WORLD_WIDE_BUNDLE_KEY, world_wide);
        bundle.putSerializable(LOCATION_MODEL_BUNDLE_KEY, locationModel);
        mIntent.putExtras(bundle);
        return mIntent;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        activity= this;
        setContentView(R.layout.pick_location_with_map);
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
            /*if (i.hasExtra(FROM)) {
                from = getIntent().getExtras().getInt(FROM);
            }
            if (i.hasExtra(WORLD_WIDE_BUNDLE_KEY)) {
                world_wide = getIntent().getExtras().getBoolean(WORLD_WIDE_BUNDLE_KEY);
            }
*/

            if( from== GlobalVariables.TYPE_ADD_TEMS)
                locationModel=(LocationModel)i.getSerializableExtra(LOCATION_MODEL_BUNDLE_KEY);
            if(locationModel==null)
                locationModel=new LocationModel();
        }


        location_Text =(TextView)findViewById(R.id.location_Text);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.title_fragment);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        setUpMap();

    }
    float camera_focus_zoom = 12.0f;
    LocationService locServices;
    private Double lat,lang;
    public void setUpMap() {

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        if (!checkPermission()) {

            requestPermission();

        } else {


            map.setMyLocationEnabled(true);
            //  map.setTrafficEnabled(true);
            map.setIndoorEnabled(true);
//            map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            map.setBuildingsEnabled(true);
//            map.getUiSettings().setZoomControlsEnabled(true);
            map.setOnCameraChangeListener(OnCameraChangeListen);
            map.setOnMapClickListener(this);
            locServices = new LocationService(activity);
            lat=locServices.getLatitude();
            lang=locServices.getLongitude();
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lang), camera_focus_zoom));
            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                @Override
                public boolean onMarkerClick(Marker marker) {




                    return true;
                }

            });


        }


    }
    GoogleMap.OnCameraChangeListener OnCameraChangeListen = new GoogleMap.OnCameraChangeListener() {

        @Override
        public void onCameraChange(CameraPosition cameraPosition)
        {
            // TODO Auto-generated method stub
            locationModel.setLatitude(Double.toString(cameraPosition.target.latitude));
            locationModel.setLongitude(Double.toString(cameraPosition.target.longitude));
            lat=cameraPosition.target.latitude;
            lang=cameraPosition.target.longitude;
            getAddressFromLatLng();
//            LocationAddress.getAddressFromLocation(cameraPosition.target.latitude, cameraPosition.target.longitude,
//                    getApplicationContext(), new GeocoderHandler());
        }
    };
    final int PERMISSION_REQUEST_CODE = 2002;


    private void requestPermission()
    {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            Toast.makeText(this, "GPS permission allows us to access location data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();

        } else
        {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);


            if (checkPermission()) {


                map.setMyLocationEnabled(true);
                //  map.setTrafficEnabled(true);
                map.setIndoorEnabled(true);
                map.setBuildingsEnabled(true);
                map.getUiSettings().setZoomControlsEnabled(true);
                map.setOnCameraChangeListener(OnCameraChangeListen);
                map.setOnMapClickListener(this);
                map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(marker.getPosition().latitude,
                                marker.getPosition().longitude));

                        return true;
                    }

                });
            } else {
                requestPermission();
            }
        }
    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {

            return false;

        }
    }
    private Context context;
    private boolean proceed=false;
    private void getAddressFromLatLng(){
        showProgressBar();
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getAddress(activity, lat,lang, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                hideProgressBar();
                GlobalFunctions.closeKeyboard(activity);
                locationModel =(LocationModel)arg0;
                if(locationModel !=null)
                {
                    locationModel.setLongitude(Double.toString(lang));
                    locationModel.setLatitude(Double.toString(lat));
                    Log.i("sdfsdf", "Selected: " + locationModel.getFormatted_address()+" Latitude :"+locationModel.getLatitude()+" Longitude :"+locationModel.getLongitude());

                    location_Text.setText(locationModel.getFormatted_address());
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
                else
                {
                    location_Text.setText("Blueshak Garage is not available..");
                }
            }
            @Override
            public void OnFailureFromServer(String msg)
            {
                hideProgressBar();
                location_Text.setText("Blueshak Garage is not available..");
            }

            @Override
            public void OnError(String msg) {
                hideProgressBar();
                location_Text.setText("Blueshak Garage is not available..");
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
    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {


            String locationAddress="";
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    location_Text.setText(locationAddress);
                    break;
                default:
                    locationAddress = null;
            }
//            locationAddressl = locationAddress;
//            search_text.setText(locationAddress);
        }
    }
    public static void closeThisActivity(){
        Log.d("closeThisActivity","closeThisActivity the pick location issues#######");
        if(activity!=null)
        {
            activity.finish();
        }
    }
}
