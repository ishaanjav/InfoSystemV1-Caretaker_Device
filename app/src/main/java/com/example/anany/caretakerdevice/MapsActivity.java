package com.example.anany.caretakerdevice;

import android.Manifest;
import android.app.ApplicationErrorReport;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    Location currentLocation;

    ArrayList<Double> latList = new ArrayList<>();
    ArrayList<Double> lngList = new ArrayList<>();

    MarkerOptions caretaker;

    int times = 0;


    ArrayList<MarkerOptions> mMarkers = new ArrayList<>();

    private LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };
    Boolean mLocationPermissionGranted;
    private Object mLastKnownLocation;
    private Object mFusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        makeToast("Click the back button to return to the previous page.");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getLocationPermission();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Update");
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("Value", "True");
        databaseReference.push().setValue(hashMap);

    }

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            makeToast(Integer.toString(level));
        }
    };

 /*   private void loop() {
        while (times < 3) {
            Runnable dm = new Runnable() {
                @Override
                public void run() {
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = firebaseDatabase.getReference("Update");
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("Value", "True");
                    databaseReference.push().setValue(hashMap);
                    times++;
                    loop();
                }
            };
            Handler h = new Handler();
            h.postDelayed(dm, 5000);
        }

    }
*/
    private void residentLocation(GoogleMap googleMap) {
        final GoogleMap myMap = googleMap;
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference dbref = firebaseDatabase.getReference("Location");
        latList.clear();
        lngList.clear();

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    latList.add(child.child("Latitude").getValue(Double.class));
                    lngList.add(child.child("Longitude").getValue(Double.class));
                }
                if (!latList.isEmpty()) {
                    myMap.clear();
                    double latitude = latList.get(latList.size() - 1);
                    double longitude = lngList.get(lngList.size() - 1);

                    LatLng resident = new LatLng(latitude, longitude);
                    //   mMap.addMarker(new MarkerOptions().position(home).title("My Location"));
                    //   myMap.moveCamera(CameraUpdateFactory.newLatLng(resident));


                    MarkerOptions marker = new MarkerOptions().position(resident).title("Resident's Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                    locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    String locationProvider = LocationManager.GPS_PROVIDER;
                    currentLocation = locationManager.getLastKnownLocation(locationProvider);
                    if (currentLocation != null) {
                        final double lng = currentLocation.getLongitude();
                        final double lat = currentLocation.getLatitude();
                        Log.d("Log", "longtitude=" + lng + ", latitude=" + lat);

                        final LatLng home = new LatLng(lat, lng);
                        caretaker = new MarkerOptions().position(home).title("My Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title("My Location");
                    }

                    LatLng clng = caretaker.getPosition();
                    double carelat = clng.latitude;
                    double carelong = clng.longitude;

                    LatLng rlng = marker.getPosition();
                    double rlat = rlng.latitude;
                    double rlong = rlng.longitude;

                    double x = Math.abs(rlat - carelat);
                    double y = Math.abs(rlong - carelong);

                    x *= x;
                    y *= y;

                    double distance = Math.sqrt(x + y);
                    DecimalFormat df = new DecimalFormat("###.#####");
                    String finaldistance = (df.format(distance));
                    final Double ddistance = Double.parseDouble(finaldistance);

                    if ((ddistance * 69) < 0.05) {
                        makeToast((ddistance * 69 * 5280) + " feet: The resident is close to you.");
                    } else if ((ddistance * 69) < 1.001) {
                        makeToast((ddistance * 69 * 5280) + " feet: The resident is not too close to you.");
                    } else {
                        makeToast((ddistance * 69) + " miles: The resident is not close to you.");
                    }

                    double topy, topx, botx, boty;
                    if (marker.getPosition().latitude > caretaker.getPosition().latitude) {
                        topy = marker.getPosition().latitude;
                        boty = caretaker.getPosition().latitude;
                    } else {
                        topy = caretaker.getPosition().latitude;
                        boty = marker.getPosition().latitude;
                    }

                    if (marker.getPosition().longitude > caretaker.getPosition().longitude) {
                        topx = marker.getPosition().longitude;
                        botx = caretaker.getPosition().longitude;
                    } else {
                        topx = caretaker.getPosition().longitude;
                        botx = marker.getPosition().longitude;
                    }

                    LatLng topleft = new LatLng(topy + 6.3 * distance, topx + 6.3 * distance);
                    LatLng bottomright = new LatLng(boty - 6.3 * distance, botx - 6.3 * distance);

                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    builder.include(marker.getPosition());
                    builder.include(caretaker.getPosition());
                    builder.include(topleft);
                    builder.include(bottomright);

                   /* DatabaseReference databaseReference = firebaseDatabase.getReference("Distances");
                    HashMap<String, Double> hashMap = new HashMap<>();
                    hashMap.put("Distance", ddistance * 69 * 5280);
                    databaseReference.push().setValue(hashMap);*/

                    final LatLngBounds bounds = builder.build();
                    if (times == 0) {
                        Runnable r = new Runnable() {
                            @Override
                            public void run() {
                                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 15);
                                myMap.animateCamera(cu);
                            }
                        };
                        Handler h = new Handler();
                        h.postDelayed(r, 3000);
                    } else {
                        times = 1;
                    }


                    myMap.addMarker(marker);
                    myMap.addMarker(caretaker);

                    mMarkers.add(marker);


                } else {
                    //Empty. Whatever.
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        dbref.addValueEventListener(valueEventListener);
        //      loop();


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        String locationProvider = LocationManager.GPS_PROVIDER;
        currentLocation = locationManager.getLastKnownLocation(locationProvider);
        if (currentLocation != null) {
            final double lng = currentLocation.getLongitude();
            final double lat = currentLocation.getLatitude();
            Log.d("Log", "longtitude=" + lng + ", latitude=" + lat);
            //  makeToast(lat + " " + lng);
            final LatLng home = new LatLng(lat, lng);
            LatLng texas = new LatLng(99.9018, 31.9686);
            //   mMap.addMarker(new MarkerOptions().position(home).title("My Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(texas));

        /*    final LatLng yourLocation = new LatLng(lat, lng);
            Marker melbourne = mMap.addMarker(new MarkerOptions()
                    .position(yourLocation)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title("My Location."));*/

            caretaker = new MarkerOptions().position(home).title("My Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title("My Location");
            mMap.addMarker(caretaker);
//          mMap.addMarker(new MarkerOptions().position(home).title("My Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.circleicon)).title("My Location."));

            Runnable r = new Runnable() {
                @Override
                public void run() {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(home, 16f));
                    Runnable d = new Runnable() {
                        @Override
                        public void run() {
                            mMap.setMinZoomPreference(8.0f);
                        }
                    };
                    Handler m = new Handler();
                    m.postDelayed(d, 2000);
                    /*  mMap.animateCamera(CameraUpdateFactory.newLatLng(home));*/
                }
            };
            Handler h = new Handler();
            h.postDelayed(r, 800);

            residentLocation(googleMap);

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.back) {
            startActivity(new Intent(getApplicationContext(), GPS.class));
        } else if (id == R.id.refresh) {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference("Update");
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("Value", "True");
            databaseReference.push().setValue(hashMap);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Update");
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("Value", "False");
        //makeToast(hashMap.toString());
        databaseReference.push().setValue(hashMap);

        super.onPause();
    }

    private void makeToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }

    }

}