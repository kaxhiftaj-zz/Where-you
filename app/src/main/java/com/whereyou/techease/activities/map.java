package com.whereyou.techease.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.whereyou.techease.R;
import com.whereyou.techease.controllers.CustomDialogClass;
import com.whereyou.techease.utils.Configuration;

import java.io.IOException;
import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

public class map extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    Place place;
    LatLng latLng;
    public static String loclat = "";
    public static String loclong = "";
    public static String curlat = "";
    public static String curlong = "";
    public static String token = "";
    public static String locname = "";

    String api_token;
    double lat,lon ;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        sharedPreferences = this.getSharedPreferences(Configuration.MY_PREF, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        api_token = sharedPreferences.getString("api_token","");
        Log.d("api_token" , api_token);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        SmartLocation.with(map.this).location()
                .start(new OnLocationUpdatedListener() {

                    @Override
                    public void onLocationUpdated(Location location) {
                        lat = location.getLatitude();
                        lon = location.getLongitude();
                        Log.d("Location : ", "" + lat + " " + lon);
                        Geocoder geoCoder = new Geocoder(map.this, Locale.getDefault());
                        StringBuilder builder = new StringBuilder();
                        try {
                            List<Address> address = geoCoder.getFromLocation(lat, lon, 1);
                            int maxLines = address.get(0).getMaxAddressLineIndex();
                            for (int i = 0; i < maxLines; i++) {
                                String addressStr = address.get(0).getAddressLine(i);
                                String city = address.get(0).getLocality();
                                String state = address.get(0).getAdminArea();
                                String country = address.get(0).getCountryName();
                                String postalCode = address.get(0).getPostalCode();
                                String knownName = address.get(0).getFeatureName();
                                String subadmin = address.get(0).getSubLocality();
                                Log.d("zma city 2", "city " + city + "\nstate " + state + "\n country " +
                                        country + "\n postal code " + postalCode + "\nknow name " + knownName + "get sub admin area" + subadmin);
                                builder.append(addressStr);
                                builder.append(" ");
                            }

                        } catch (IOException e) {
                        } catch (NullPointerException e) {
                        }

                    }
                });


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Obtain supportAutocomplete fragment
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                latLng=place.getLatLng();
                MyMethod(mMap,latLng);

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("Error", "An error occurred: " + status);
            }
        });
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                 place = PlaceAutocomplete.getPlace(this, data);
                latLng = place.getLatLng();

                MyMethod(mMap,place.getLatLng());


            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("RuntimeError", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
                Log.d("Zma result cancel",String.valueOf(RESULT_CANCELED));
            }
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

    }
    public void MyMethod(GoogleMap googleMap, final LatLng latLng) {
        mMap=googleMap;
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                CustomDialogClass cdd=new CustomDialogClass(map.this);

                loclat = String.valueOf(latLng.latitude);
                loclong = String.valueOf(latLng.longitude);
                locname = String.valueOf(place.getName());
                curlat = String.valueOf(lat) ;
                curlong = String.valueOf(lon);
                Toast.makeText(map.this, curlong, Toast.LENGTH_SHORT).show();
                token = api_token;

                cdd.show();
                return false;
            }

        });

        LatLng location = latLng;
        Log.d("zma loc",String.valueOf(location));
        mMap.addMarker(new MarkerOptions().position(location));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity( new Intent(map.this,MainActivity.class));
        finish();
    }
}
