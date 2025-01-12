package com.example.locationtracker;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.example.locationtracker.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    Location currentLocation;
    FusedLocationProviderClient fusedClient;
    private static final int REQUEST_CODE = 101;
    private String location_message;
    private LocationCallback mCallBack;
    private LocationRequest mRequest;
    private ActivityMapsBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MapDebug", "onCreate called");

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // for updates, call back and make toasts
        mCallBack = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult){
                if (locationResult == null){
                    Log.d("CallBack", "Location Result: null");
                    return;
                }
                Log.d("CallBack", "Location Result Received");
                for (Location loc: locationResult.getLocations()){
                    location_message = "lat: " + loc.getLatitude() + " long: " + loc.getLongitude();
                    Toast.makeText(MapsActivity.this, "<Updated Location> " + location_message,
                          Toast.LENGTH_SHORT).show();
                }
            }

            @Override
                public void onLocationAvailability(LocationAvailability locationAvailability) {
                Log.d("CallBack", "locationAvailability is " + locationAvailability.isLocationAvailable());
                super.onLocationAvailability(locationAvailability);
            }
        };

        // now, start the location-tracking methods
        fusedClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation();

    }

    private void getLocation() {
        // check the permission
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        // start from the last location
        Task<Location> task = fusedClient.getLastLocation();

        // if it's successful, show it on the map
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                currentLocation = location;
                SupportMapFragment supportMapFragment = (SupportMapFragment)
                        getSupportFragmentManager().findFragmentById(R.id.map);
                assert supportMapFragment != null;
                supportMapFragment.getMapAsync(MapsActivity.this);
            }
        });

        // location request parameters
        mRequest = new LocationRequest();
        mRequest.setInterval(20000); // every ~20 seconds
        mRequest.setFastestInterval(10000); // fastest in 10 seconds
        mRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // get constant updates
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mRequest);

    }

    public void onMapReady(GoogleMap googleMap) {

        Log.d("MapDebug", "onMapReady called");

        LatLng latLng = null;

        // if current location is available, call it, and make Toast & Log of the current latLng
        if (currentLocation != null){
            latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            location_message = "lat: " + new String(String.valueOf(currentLocation.getLatitude()))+
                    " long: " + new String(String.valueOf(currentLocation.getLongitude()));
            Toast.makeText(MapsActivity.this, "<Current Location> " + location_message,
                    Toast.LENGTH_SHORT).show();
            Log.d("LatLng", "LatLng "+ location_message);
        } else {
            // if current location is unavailable, call the default of SF, and make Toast & Log
            latLng = new LatLng(37.7749, -122.4194);
            location_message = "lat: " + new String(String.valueOf(currentLocation.getLatitude()))+
                    " long: " + new String(String.valueOf(currentLocation.getLongitude()));
            Toast.makeText(MapsActivity.this, "<Default Location> " + location_message,
                    Toast.LENGTH_SHORT).show();
            Log.d("LatLng", "Default: SF");
        }

        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Current Location");
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
        googleMap.addMarker(markerOptions);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                getLocation();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }
    @Override
    protected void onPause() {
        super.onPause();
        fusedClient.removeLocationUpdates(mCallBack);
    }
    @SuppressLint("MissingPermission")
    protected void startLocationUpdates() {
        fusedClient.requestLocationUpdates(mRequest, mCallBack, null);
    }

}