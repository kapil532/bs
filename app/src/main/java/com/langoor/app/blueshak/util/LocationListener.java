package com.langoor.app.blueshak.util;

import android.location.Location;

public interface LocationListener {
    void onLocationChanged(Location location);
    void onProviderEnabled(String location);
}
