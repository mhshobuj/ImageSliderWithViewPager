package com.example.slideimagewithviewpager;


import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;

/**
 * @Md Mehedi Hasan on 05/29/22
 **/

public class GPSTracker extends Service implements LocationListener {

    private SettingsClient mSettingsClient;
    private LocationSettingsRequest mLocationSettingsRequest;

    // flag for GPS status
    boolean isGPSEnabled = false;
    // flag for network status
    boolean isNetworkEnabled = false;
    // flag for GPS status
    boolean canGetLocation = false;

    Location location; // location
    double latitude, longitude;

    // Declaring a Location Manager
    protected LocationManager locationManager;
    private LocationRequest locationRequest;

    private final Context mContext;
    private String device_id;

    public GPSTracker(Context context) {
        this.mContext = context;
        mSettingsClient = LocationServices.getSettingsClient(context);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        mLocationSettingsRequest = builder.build();
        builder.setAlwaysShow(true);

        getLocation();
    }

    @SuppressLint("MissingPermission")
    public Location getLocation() {
        try {

            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {

                Toast.makeText(getApplicationContext(), "Network or GPS in not available", Toast.LENGTH_SHORT).show();

            } else if (isNetworkEnabled && isGPSEnabled) {
                location = null;
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
                //interval
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {

                        latitude = location.getLatitude();
                        longitude = location.getLongitude();

                        Log.e("LocationGPS", "Lat/Long: " + latitude + longitude);
                        this.canGetLocation = true;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    /**
     * Function to get latitude
     */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }
        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }
        // return longitude
        return longitude;
    }

    /**
     * Function to getIMEI
     */
    @SuppressLint("HardwareIds")
    public String getDevice_id() {
        device_id = Build.ID;
        // return latitude
        return device_id;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }
}
