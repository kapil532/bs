package com.blueshak.app.blueshak;

import android.Manifest;
import android.app.Activity;
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
import android.widget.TextView;
import android.widget.Toast;

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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity= this;
        setContentView(R.layout.pick_location_with_map);
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
        public void onCameraChange(CameraPosition cameraPosition) {
            // TODO Auto-generated method stub
            LocationAddress.getAddressFromLocation(cameraPosition.target.latitude, cameraPosition.target.longitude,
                    getApplicationContext(), new GeocoderHandler());
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
