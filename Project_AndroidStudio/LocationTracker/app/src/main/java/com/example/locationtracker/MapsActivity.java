package com.example.locationtracker;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.example.locationtracker.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    Location currentLocation;
    FusedLocationProviderClient fusedClient;
    private static final int REQUEST_CODE = 101;
    private String location_message;

    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MapDebug", "onCreate called");

        //setContentView(R.layout.activity_maps);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fusedClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        //        .findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);

    }

    private void getLocation() {
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

        @SuppressLint("MissingPermission") Task<Location> task = fusedClient.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    currentLocation = location;
                    SupportMapFragment supportMapFragment = (SupportMapFragment)
                            getSupportFragmentManager().findFragmentById(R.id.map);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(MapsActivity.this);
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.d("MapDebug", "onMapReady called");

        LatLng latLng = null;

        // if current location is available, call it, and make Toast & Log of the current latLng
        if (currentLocation != null){
            latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            location_message = new String(String.valueOf(currentLocation.getLatitude())) +" "+
                    new String(String.valueOf(currentLocation.getLongitude()));
            Toast.makeText(MapsActivity.this, "<Current Location> " + location_message,
                    Toast.LENGTH_SHORT).show();
            Log.d("LatLng", "LatLng "+ location_message);
        } else {
            // if current location is unavailable, call the default of SF, and make Toast & Log
            latLng = new LatLng(37.7749, -122.4194);
            location_message = new String(String.valueOf(currentLocation.getLatitude())) +" "+
                    new String(String.valueOf(currentLocation.getLongitude()));
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
}