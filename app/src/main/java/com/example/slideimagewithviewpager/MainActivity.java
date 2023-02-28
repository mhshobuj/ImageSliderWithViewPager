package com.example.slideimagewithviewpager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    AppCompatButton btnScan, btnLocation;
    ViewPager viewPager;
    ArrayList<String> images = new ArrayList<>();
    ViewPagerAdapter adapter;
    WormDotsIndicator wormDotsIndicator;

    // GPSTracker class
    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnScan = findViewById(R.id.btnScan);
        btnLocation = findViewById(R.id.btnLocation);
        viewPager = findViewById(R.id.viewPager);
        wormDotsIndicator = (WormDotsIndicator) findViewById(R.id.worm_dots_indicator);

        images.add("https://images.pexels.com/photos/6898857/pexels-photo-6898857.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1");
        images.add("https://images.pexels.com/photos/6897769/pexels-photo-6897769.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1");
        images.add("https://images.pexels.com/photos/6898855/pexels-photo-6898855.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1");
        images.add("https://images.pexels.com/photos/6897682/pexels-photo-6897682.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1");
        images.add("https://images.pexels.com/photos/7258495/pexels-photo-7258495.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1");
        images.add("https://images.pexels.com/photos/5358943/pexels-photo-5358943.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1");

        adapter = new ViewPagerAdapter(this, images);
        viewPager.setPadding(100, 0, 100, 0);
        viewPager.setAdapter(adapter);

        wormDotsIndicator.attachTo(viewPager);

        btnScan.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, BarCodeScannerActivity.class))
        );

        IntentFilter filter = new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION);
        filter.addAction(Intent.ACTION_PROVIDER_CHANGED);
        registerReceiver(gpsSwitchStateReceiver, filter);

        if (isLocationEnabled(this)) {
            Toast.makeText(this, "Location On", Toast.LENGTH_SHORT).show();
        } else {
            // Handle Location turned OFF
            Toast.makeText(this, "Location off", Toast.LENGTH_SHORT).show();
            showSettingsAlert(this);
        }

        btnLocation.setOnClickListener(v -> {
            // create class object
            gps = new GPSTracker(MainActivity.this);
            // check if GPS enable
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: "
                    + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        });

    }

    private BroadcastReceiver gpsSwitchStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (LocationManager.PROVIDERS_CHANGED_ACTION.equals(intent.getAction())) {

                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (isGpsEnabled || isNetworkEnabled) {
                    // Handle Location turned ON
                    Toast.makeText(context, "Location On", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle Location turned OFF
                    Toast.makeText(context, "Location off", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(gpsSwitchStateReceiver);
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    public void showSettingsAlert(Context context){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}