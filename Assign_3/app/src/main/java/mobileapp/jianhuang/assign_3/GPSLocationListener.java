package mobileapp.jianhuang.assign_3;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by jianhuang on 16-02-03.
 */
public class GPSLocationListener implements LocationListener {
    @Override
    public void onLocationChanged(Location loc) {

        String longitude = "Longitude: " + loc.getLongitude();
        String latitude = "Latitude: " + loc.getLatitude();
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider,
                                int status, Bundle extras) {
        // TODO Auto-generated method stub
    }
}