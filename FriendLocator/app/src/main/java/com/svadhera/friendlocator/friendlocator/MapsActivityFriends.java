package com.svadhera.friendlocator.friendlocator;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapsActivityFriends extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    public static final String FRIENDS_PREFS = "com.svadhera.friendlocator.friendlocator.FRIENDS_PREFERENCE_FILE_KEY";
    //public static final String FRIENDS_LOC_PREFS = "com.svadhera.friendlocator.friendlocator.FRIENDS_LOC_PREFERENCE_FILE_KEY";
    SharedPreferences friends_sp;
    SharedPreferences friends_loc_sp;

    Button btnMapRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_activity_friends);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        friends_sp = getSharedPreferences(FRIENDS_PREFS, 0);
        //friends_loc_sp = getSharedPreferences(FRIENDS_LOC_PREFS, 0);
        btnMapRefresh = (Button) findViewById(R.id.btnMapRefresh);
        btnMapRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshAll();
            }
        });
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

        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
        refreshAll();

    }

    private void refreshAll () {
        Toast.makeText(this, "Fetching latest data ...", Toast.LENGTH_SHORT).show();
        List<String> friendList = new ArrayList<>();
        List<String> keyList = new ArrayList<>();
        Map<String,?> keys = friends_sp.getAll();
        for(Map.Entry<String,?> entry : keys.entrySet()){
            if (entry.getValue() instanceof String) {
                keyList.add(entry.getKey());
                friendList.add(entry.getValue().toString());
            }
        }
        int index = 0;
        for (String key : keyList) {
            try {
                String location = new HTTPGetAsyncTask().execute(key).get();
                if (!location.equals("error")) {
                    Toast.makeText(this, "Fetching " + ++index + " ...", Toast.LENGTH_SHORT).show();
                    double lat = JsonHelper.getLat(location);
                    double lng = JsonHelper.getLng(location);
                    LatLng latlng = new LatLng(lat, lng);
                    mMap.addMarker(new MarkerOptions().position(latlng).title(friendList.get(index - 1)));
                    //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Toast.makeText(this, "Database refreshed !", Toast.LENGTH_SHORT).show();
    }

    class HTTPGetAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("Fetching from network");
        }

        protected String doInBackground(String... params) {
            String location = HttpHelper.getLocation(params[0]);
            if (!location.equals("error")) {
                return location;
            }
            return "error";
        }

        @Override
        protected void onPostExecute(String n) {
            super.onPostExecute(n);
            System.out.println("Network fetch complete");
        }
    }
}
