package project226.a000webhostapp.com.demo;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.Manifest;
import android.widget.Toast;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private Boolean mLocationPermissionsGranted = true;
    private static final String TAG = "MainActivity";
    private static final float DEFAULT_ZOOM = 15f;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    public Location currentLocation=null;
    public LatLng otherslocation;
    private static String username="Alekh" ;
    private static String key = "XYZ";
    private static String otherKey ;
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Bundle getargs = getIntent().getExtras();
        //username = getargs.getString("username");
        //Log.d("TTTTTT","k"+username);
        otherKey =getargs.getString("otherKey");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        initMap();
        getDeviceLocation();

    }




    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
       AsyncTask task =new AsyncClass();
       task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,mFusedLocationProviderClient,username,key);

        try {
            if (mLocationPermissionsGranted) {

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location!");
                            currentLocation = (Location) task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM);

                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapsActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    private void initMap() {
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        // Add a marker in Sydney and move the camera

        getDeviceLocation();
        getOtherUserLocation(otherKey);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
    }


    public Marker addMarker(LatLng latLng,String title,float result){

        // Add a marker in Sydney and move the camera
       LatLng sydney = new LatLng(latLng.latitude,latLng.longitude);
        Marker marker =mMap.addMarker(new MarkerOptions().position(sydney).title(title).snippet("Distance="+result));

       // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        return marker;

    }

    public void getOtherUserLocation(final String keyPassed){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String key1 = keyPassed;
               // while (true){
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference().child(key1);
                    // while (true){
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
//                String value = dataSnapshot.getValue().toString()
                            Double latitude =Double.valueOf((Double) dataSnapshot.child("Latitude").getValue());
                            Double longitude =Double.valueOf((Double) dataSnapshot.child("Longitude").getValue());
                            String userName =String.valueOf( dataSnapshot.child("userName").getValue());
                            Log.d("GetUsers", "Value is: "+userName + latitude+":::"+longitude);
                            otherslocation = new LatLng(latitude,longitude);
                            mMap.clear();


                            ///////////////////////////////////////////////////////////////////////////////////
                            Object dataTransfer[] = new Object[5];
                            String url = getDirectionsUrl();
                            GetDirectionsData getDirectionsData = new GetDirectionsData();
                            dataTransfer[0] = mMap;
                            dataTransfer[1] = url;
                            dataTransfer[2] = new LatLng(otherslocation.latitude, otherslocation.longitude);
                            dataTransfer[3] = userName;
                            dataTransfer[4]= new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
                            getDirectionsData.execute(dataTransfer);
                            ///////////////////////////////////////////////////////////////////////////////////////
                            /*if (marker != null)
                                marker.remove();
                            marker =addMarker(otherslocation,userName,distance[0]);
                            Log.d("ReadBothLocation",""+currentLocation+"kkk:"+ otherslocation);*/

                        }
                        private String getDirectionsUrl()
                        {
                            StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
                            googleDirectionsUrl.append("origin="+currentLocation.getLatitude()+","+currentLocation.getLongitude());
                            googleDirectionsUrl.append("&destination="+otherslocation.latitude+","+otherslocation.longitude);
                            googleDirectionsUrl.append("&key="+"AIzaSyBMlQHqPHOeQosA0Yuc9rrzk7wtwDCdUWk");

                            return googleDirectionsUrl.toString();
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w("tag", "Failed to read value.", error.toException());
                        }
                    });

                }
        }).start();

    }

    @Override
    public void onBackPressed() {
        finish();

    }
}
