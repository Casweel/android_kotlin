package com.example.lab8;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    MyPlace from = MainActivity.placeFrom;
    MyPlace to = MainActivity.placeTo;
    private GoogleMap map;
    PolylineOptions line;
    LocationListener listener;
    LatLngBounds.Builder latLngBuilder;
    LatLng here = new LatLng(55, 37);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragmentFrom = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragmentFrom.getMapAsync(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "To detect your locations app need locate permission.", Toast.LENGTH_SHORT).show();
            return;
        }

        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mFusedLocationClient
                .getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            map.addMarker(new MarkerOptions()
                                    .position(new LatLng(location.getLatitude(), location.getLongitude()))
                                    .title("You are here"));
                            map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Something goes wrong with your location detecting.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        GeoApiContext geoApiContext = new GeoApiContext.Builder()
                .apiKey(getResources().getString(R.string.api_key))
                .build();

        DirectionsResult result = null;
        try {
            result = DirectionsApi.newRequest(geoApiContext)
                    .mode(TravelMode.WALKING)
                    .origin(new com.google.maps.model.LatLng(from.place.getLatLng().latitude, from.place.getLatLng().longitude))
                    .destination(new com.google.maps.model.LatLng(to.place.getLatLng().latitude, to.place.getLatLng().longitude)).await();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        try {
            List<com.google.maps.model.LatLng> path = result.routes[0].overviewPolyline.decodePath();
            line = new PolylineOptions();
            latLngBuilder = new LatLngBounds.Builder();

            for (int i = 0; i < path.size(); i++) {
                line.add(new com.google.android.gms.maps.model.LatLng(path.get(i).lat, path.get(i).lng));
                latLngBuilder.include(new com.google.android.gms.maps.model.LatLng(path.get(i).lat, path.get(i).lng));
            }
            line.width(10f).color(R.color.quantum_black_100);
        }
        catch (Exception e){

        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if(line != null)
        {
            map.addPolyline(line);
            map.moveCamera(CameraUpdateFactory.newLatLng(here));
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Not found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

