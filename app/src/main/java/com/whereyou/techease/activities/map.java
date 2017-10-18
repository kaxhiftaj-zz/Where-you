package com.whereyou.techease.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Button;

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

public class map extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    Place place;
    LatLng latLng;
    DatabaseReference databaseReference;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

//        Firebase.setAndroidContext(this);
//        databaseReference= FirebaseDatabase.getInstance().getReference();

        //initializing firebae
     //   auth=FirebaseAuth.getInstance();

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
                // TODO: Get info about the selected place.
                Log.i("Place", "Place: " + place.getName());
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
//                AttributesHolder attributesHolder=new AttributesHolder(latLng);
//                databaseReference.child("location").setValue(attributesHolder);
                MyMethod(mMap,place.getLatLng());

                Log.i("Searched", "Place: " + place.getName());
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
    public void MyMethod(GoogleMap googleMap, LatLng latLng) {
        mMap=googleMap;
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(map.this);
                builder.setTitle("Dialog Box");
                builder.setMessage("This is the plain text...!");
                builder.setCancelable(true);
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                       builder.setCancelable(true);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                                    builder.setCancelable(true);
                    }
                });
               builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       builder.setCancelable(true);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                nbutton.setTextColor(Color.MAGENTA);
                Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                pbutton.setTextColor(Color.MAGENTA);
                Button cButton=alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL);
                cButton.setTextColor(Color.MAGENTA);
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
