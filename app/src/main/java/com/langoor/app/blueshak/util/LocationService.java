package com.langoor.app.blueshak.util;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
import com.google.android.gms.location.places.Places;
import com.langoor.app.blueshak.MainActivity;
import com.langoor.app.blueshak.global.GlobalVariables;
import com.langoor.app.blueshak.view.AlertDialog;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Manu K R on 21/05/16.
 */
public class LocationService extends Service implements LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    String TAG = "LocationService";

    private Activity mContext = null;

    // Flag for GPS status
    boolean isGPSEnabled = false;

    // Flag for network status
    boolean isNetworkEnabled = false;

    com.langoor.app.blueshak.util.LocationListener listener;

    // Flag for GPS status
    boolean canGetLocation = false;

    Location location; // Location
    double latitude; // Latitude
    double longitude; // Longitude

    GoogleApiClient mGoogleApiClient;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 0; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    public LocationService() {
    }

    public LocationService(final Activity context) {
        this.mContext = context;
        getPermission();
        Log.d(TAG,"##########mGoogleApiClient == null");
        if(isGpsEnabled(context)){
            if (mGoogleApiClient == null) {
                Log.d(TAG,"##########mGoogleApiClient not null");
                mGoogleApiClient = new GoogleApiClient.Builder(context)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(Places.GEO_DATA_API)
                        .addApi(LocationServices.API)
                        .addOnConnectionFailedListener(this)
                        .build();
            }
            getLocation();
        }


    }

    public void setListener(com.langoor.app.blueshak.util.LocationListener listener) {
        this.listener = listener;
    }

    public void removeListener() {
        if (listener != null) {
            this.listener = null;
        }
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);


             locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0,
                    new LocationListener() {
                        @Override
                        public void onStatusChanged(String provider, int status,
                                                    Bundle extras) {
                        }

                        @Override
                        public void onProviderEnabled(String provider) {
                            Toast.makeText(mContext,"GPS is enabled",Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onProviderDisabled(String provider) {
                        }

                        @Override
                        public void onLocationChanged(final Location location) {
                        }
                    });
            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }


    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app.
     * */
    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(LocationService.this);
        }
    }


    /**
     * Function to get latitude
     * */
    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }


    /**
     * Function to get longitude
     * */
    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    /**
     * Function to check GPS/Wi-Fi enabled
     * @return boolean
     * */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }


    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG,"latitude :"+location.getLatitude());
        Log.i(TAG, "location :" + location.getLongitude());
        Log.i(TAG, "altitude :" + location.getAltitude());

        Intent loc = new Intent();
        loc.setAction("biz.paqs.app.mysmartinhaler.location");
        loc.putExtra("from","locationservice");
        loc.putExtra("latitude", location.getLatitude());
        loc.putExtra("longitude",location.getLongitude());
        mContext.sendBroadcast(loc);
        this.location = location;
        if(listener!=null){listener.onLocationChanged(location);}
    }


    @Override
    public void onProviderDisabled(String provider) {
    }


    @Override
    public void onProviderEnabled(String provider) {

        if(listener!=null){listener.onProviderEnabled(provider);}
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("GPS","Connected");
        showSettingsAlert();
        location = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
        this.location = location;
        if(listener!=null){listener.onLocationChanged(location);}
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("GPS","onnection Suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("GPS","onnection Failed");

    }

    @Override
    public void onStart(Intent intent, int startId) {
        if(mGoogleApiClient!=null)mGoogleApiClient.connect();
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        if(mGoogleApiClient!=null)mGoogleApiClient.disconnect();
        super.onDestroy();
    }

    public void showSettingsAlert(){
        settingsrequest();
       /* final AlertDialog alertDialog = new AlertDialog(mContext);
        // Setting Dialog Title
       alertDialog.setTitle("GPS is disabled");
        // Setting Dialog Message*//*
        alertDialog.setMessage("GPS is disabled in your device, Please Enable it to continue.");
        alertDialog.setPositiveButton("Enable GPS", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
                alertDialog.dismiss();
            }
        });

      // on pressing cancel button
        alertDialog.setNegativeButton("Not now", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();*/
    }

    public void settingsrequest()
    {
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
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(mContext, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }
    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
// Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Toast.makeText(mContext,"onActivityResult",Toast.LENGTH_LONG).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        settingsrequest();//keep asking if imp or do whatever
                        break;
                }
                break;
        }
    }*/

    private List<Address> getGeocoder(double lat, double lng){
        Geocoder gcd = new Geocoder(mContext, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(lat, lng, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addresses;
    }
    public String getCityName(){
        List<Address> addresses = getGeocoder(getLatitude(), getLongitude());
        if (addresses.size() > 0)
            return (addresses.get(0).getLocality());
        return null;
    }

    public String getStateName(){
        List<Address> addresses = getGeocoder(getLatitude(), getLongitude());
        if (addresses.size() > 0)
            return (addresses.get(0).getAdminArea());
        return null;
    }

    public String getPostalCode(){
        List<Address> addresses = getGeocoder(getLatitude(), getLongitude());
        if(addresses!=null){
            if (addresses.size() > 0)
                return (addresses.get(0).getPostalCode());
            return null;
        }else
            return null;

    }

    public String getAreaName(){
        List<Address> addresses = getGeocoder(getLatitude(), getLongitude());
        if(addresses!=null){
            if (addresses.size() > 0)
                return (addresses.get(0).getSubLocality());
        }
        return null;
    }

    private void getPermission(){
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, GlobalVariables.PERMISSIONS_REQUEST_PRIMARY);
            }
        }
    }
    private boolean isGpsEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            return  true;
        }else{
            return false;
        }
    }
}