package com.langoor.app.blueshak.util;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

/**
 * Created by NanduYoga on 21/05/16.
 */
public class GeocoderHelper {
    private static final AndroidHttpClient ANDROID_HTTP_CLIENT = AndroidHttpClient.newInstance(GeocoderHelper.class.getName());

    private boolean running = false;

    public void fetchCityName(final Context contex, final Location location) {
        if (running)
            return;

        new AsyncTask<Void, Void, String>() {
            protected void onPreExecute() {
                running = true;
            }

            @Override
            protected String doInBackground(Void... params) {
                String cityName = null;

                if (Geocoder.isPresent()) {
                    try {
                        Geocoder geocoder = new Geocoder(contex, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if (addresses.size() > 0) {
                        /*	StringBuilder str = new StringBuilder();
                            String localityString = addresses.get(0).getLocality();
                        	String city = addresses.get(0).getCountryName();
                        	String region_code = addresses.get(0).getCountryCode();
                        	String zipcode = addresses.get(0).getPostalCode();
                        	str.append(localityString + "");
                        	str.append(city + "" + region_code + "");
                        	str.append(zipcode + ""); */
                            cityName = addresses.get(0).getAddressLine(0).toString() + "--" + addresses.get(0).getAddressLine(1).toString() + "--" + addresses.get(0).getAddressLine(2).toString() + "--" + addresses.get(0).getAddressLine(3).toString();
                            //Log.i("details", addresses.get(0).getAddressLine(0)+"\n"+addresses.get(0).getAddressLine(1)+"\n"+addresses.get(0).getAddressLine(2)+"\n"+addresses.get(0).getAddressLine(3));
                        }
                    } catch (Exception ignored) {
                        // after a while, Geocoder start to trhow "Service not availalbe" exception. really weird since it was working before (same device, same Android version etc..
                    }
                }

                if (cityName != null) { // i.e., Geocoder succeed
                    return cityName;
                } else { // i.e., Geocoder failed
                    return fetchCityNameUsingGoogleMap(location);
                }
            }

            // Geocoder failed :-(
            // Our B Plan : Google Map
            public String fetchCityNameUsingGoogleMap(Location location) {
                String googleMapUrl = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + location.getLatitude() + ","
                        + location.getLongitude() + "&sensor=false&language=fr";
                Log.i("location", "url " + googleMapUrl);
                try {
                    JSONObject googleMapResponse = new JSONObject(ANDROID_HTTP_CLIENT.execute(new HttpGet(googleMapUrl),
                            new BasicResponseHandler()));
                    Log.i("location", "response " + googleMapResponse);
                    // many nested loops.. not great -> use expression instead
                    // loop among all results
                    JSONArray results = (JSONArray) googleMapResponse.get("results");
                    for (int i = 0; i < results.length(); i++) {
                        // loop among all addresses within this result
                        JSONObject result = results.getJSONObject(i);
                        if (result.has("address_components")) {
                            JSONArray addressComponents = result.getJSONArray("address_components");
                            // loop among all address component to find a 'locality' or 'sublocality'
                            for (int j = 0; j < addressComponents.length(); j++) {
                                JSONObject addressComponent = addressComponents.getJSONObject(j);
                                if (result.has("types")) {
                                    JSONArray types = addressComponent.getJSONArray("types");

                                    // search for locality and sublocality
                                    Log.i("address","type details "+types.toString());
                                    String cityName = null;
                                    String state = null;
                                    String country = null;

                                    for (int k = 0; k < 2; k++) {
                                        if ("sublocality_level_2".equals(types.getString(k)) && cityName == null) {
                                            if (addressComponent.has("long_name")) {
                                                cityName = addressComponent.getString("long_name");
                                            } else if (addressComponent.has("short_name")) {
                                                cityName = addressComponent.getString("short_name");
                                            }
                                        }

                                        if ("sublocality".equals(types.getString(k))) {
                                            if (addressComponent.has("long_name")) {
                                                cityName = addressComponent.getString("long_name");
                                            } else if (addressComponent.has("short_name")) {
                                                cityName = addressComponent.getString("short_name");
                                            }
                                        }

                                        if ("locality".equals(types.getString(k)) && state == null) {
                                            if (addressComponent.has("long_name")) {
                                                state = addressComponent.getString("long_name");
                                            } else if (addressComponent.has("short_name")) {
                                                state = addressComponent.getString("short_name");
                                            }
                                        }
                                        if ("administrative_area_level_2".equals(types.getString(k))) {
                                            if (addressComponent.has("long_name")) {
                                                state = addressComponent.getString("long_name");
                                            } else if (addressComponent.has("short_name")) {
                                                state = addressComponent.getString("short_name");
                                            }
                                        }

                                        if ("country".equals(types.getString(k)) && country == null) {
                                            if (addressComponent.has("long_name")) {
                                                country = addressComponent.getString("long_name");
                                            } else if (addressComponent.has("short_name")) {
                                                country = addressComponent.getString("short_name");
                                            }
                                        }

                                        if(addressComponent.has("formatted_address"))
                                            Log.i("address","formatted address "+addressComponent.getString("formatted_address"));
                                    }

                                    if (cityName != null) {
                                        String address = cityName;
                                        if(state != null)
                                            address = address+"--"+state;
                                        if(country != null)
                                            address = address+"--"+country;

                                        Log.i("location", "Address " + address);
                                        return address;
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(String cityName) {
                running = false;
                if (cityName != null) {
                    Log.i("GeocoderHelper", cityName);
                    Intent intent = new Intent();
                    intent.setAction("biz.paqs.app.mysmartinhaler.land");
                    intent.putExtra("from","location");
                    contex.sendBroadcast(intent);
                }
            }

            ;
        }.execute();
    }


    public String fetchCityNameUsingGoogleMaps(Location location) {
        String googleMapUrl = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + location.getLatitude() + ","
                + location.getLongitude() + "&sensor=false&language=fr";

        try {
            JSONObject googleMapResponse = new JSONObject(ANDROID_HTTP_CLIENT.execute(new HttpGet(googleMapUrl),
                    new BasicResponseHandler()));

            // many nested loops.. not great -> use expression instead
            // loop among all results
            JSONArray results = (JSONArray) googleMapResponse.get("results");
            Log.i("address","result "+results.toString());

            boolean flag = true;
            int i=0;
            while(i<results.length() && flag){
                JSONObject result = results.getJSONObject(i);
                if(result.has("formatted_address")) {
                    String address = result.getString("formatted_address").replace(", ","--");
                    Log.i("address", "formatted address " + address);
                    flag = false;
                    return address;
                }
            }
            /*for (int i = 0; i < results.length(); i++) {
                // loop among all addresses within this result
                JSONObject result = results.getJSONObject(i);
                *//**
             * code to be replaced.
             *//*
                if(result.has("formatted_address"))
                    Log.i("address","formatted address "+result.getString("formatted_address"));
            }*/
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        return null;
    }
}

