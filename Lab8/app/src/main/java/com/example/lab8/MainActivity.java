package com.example.lab8;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMapFrom;
    private GoogleMap mMapTo;

    public static MyPlace placeFrom;
    public static MyPlace placeTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Places.initialize(this, getResources().getString(R.string.api_key));
        final PlacesClient placesClient = Places.createClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }

        SupportMapFragment mapFragmentFrom = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFrom);
        mapFragmentFrom.getMapAsync(this);

        AutocompleteSupportFragment autocompleteFragmentFrom = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.from);
        autocompleteFragmentFrom.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.VIEWPORT));

        SupportMapFragment mapFragmentTo = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapTo);
        mapFragmentTo.getMapAsync(this);

        AutocompleteSupportFragment autocompleteFragmentTo = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.to);
        autocompleteFragmentTo.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.VIEWPORT));

        autocompleteFragmentFrom.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                mMapFrom.clear();
                mMapFrom.addMarker(new MarkerOptions()
                        .position(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude))
                        .title("From"));
                placeFrom = new MyPlace(place);
                mMapFrom.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude)));
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(getApplicationContext(), "Something goes wrong.", Toast.LENGTH_SHORT).show();
            }
        });

        autocompleteFragmentTo.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                mMapTo.clear();
                mMapTo.addMarker(new MarkerOptions()
                        .position(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude))
                        .title("From"));
                placeTo = new MyPlace(place);
                mMapTo.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude)));
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(getApplicationContext(), "Something goes wrong.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (mMapFrom == null)
            mMapFrom = googleMap;
        else
            mMapTo = googleMap;
    }

    public void confirm(View view) {
        if (placeFrom != null && placeTo != null) {
            setContentView(R.layout.activity_map);
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Some fields are empty.", Toast.LENGTH_SHORT).show();
        }
    }
}
