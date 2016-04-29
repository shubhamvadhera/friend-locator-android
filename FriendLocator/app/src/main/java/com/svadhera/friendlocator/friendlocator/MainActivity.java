package com.svadhera.friendlocator.friendlocator;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.content.ClipboardManager;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final String USER_PREFS = "com.svadhera.friendlocator.friendlocator.USER_PREFERENCE_FILE_KEY";

    private static String userKey;
    SharedPreferences user_sp;

    private static final String TAG = MainActivity.class.getSimpleName();

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;

    static final int PERMISSION_ACCESS_FINE_LOCATION = 1;
    boolean FINE_LOCATION_PERMISSION_GRANTED = false;

    TextView textViewLocationData;
    TextView textViewKeyDisplay;
    Button buttonRefresh;
    Button btnCopyKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToActivityFriends();
            }
        });
        fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_people_white_48dp));
        textViewLocationData = (TextView) findViewById(R.id.textViewLocationData);
        textViewKeyDisplay = (TextView) findViewById(R.id.tvKeyDisplay);
        buttonRefresh = (Button) findViewById(R.id.buttonRefresh);
        btnCopyKey = (Button) findViewById(R.id.btnCopyKey);
        // Create an instance of GoogleAPIClient.
        buildGoogleApiClient();

        user_sp = getSharedPreferences(USER_PREFS, 0);

        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayLocation();
            }
        });

        btnCopyKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("userKey", textViewKeyDisplay.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getBaseContext(), "Key copied !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) mGoogleApiClient.connect();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    private void displayLocation() {
        //if (FINE_LOCATION_PERMISSION_GRANTED) return;
        //if (Build.VERSION.SDK_INT < 23) return;
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if ( permissionCheck != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_ACCESS_FINE_LOCATION);
        //if (FINE_LOCATION_PERMISSION_GRANTED)
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();

            textViewLocationData.setText(latitude + ", " + longitude);
            updateServers(latitude, longitude);

        } else {
            textViewLocationData
                    .setText("Couldn't get the location. Make sure location is enabled on the device");
        }
    }

    /*private void checkPermissions () {
        if (FINE_LOCATION_PERMISSION_GRANTED) return;
        if (Build.VERSION.SDK_INT < 23) return;
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if ( permissionCheck == PackageManager.PERMISSION_GRANTED) return;
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_ACCESS_FINE_LOCATION);
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    FINE_LOCATION_PERMISSION_GRANTED = true;
                    displayLocation();
                } else {
                    FINE_LOCATION_PERMISSION_GRANTED = false;
                }
                return;
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        displayLocation();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    public void goToActivityFriends () {
        Intent intent = new Intent(this, Friends.class);
        startActivity(intent);
    }

    private void checkIsRegistered (double lat, double lng) {
        if (user_sp.contains("default")) {
            userKey = user_sp.getString("default", "Error fetching key");
            textViewKeyDisplay.setText(userKey);
            new HTTPPutAsyncTask().execute(lat, lng);
            return;
        };
        new HTTPPostAsyncTask().execute(lat, lng);
    }

    private void updateServers (double lat, double lng) {
        checkIsRegistered(lat, lng);
    }

    class HTTPPostAsyncTask extends AsyncTask<Double, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("Sending POST to network");
        }

        protected String doInBackground(Double... params) {
            String resp = HttpHelper.postLocation(params[0], params[1]);
            userKey = JsonHelper.getId(resp);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println("Network POST complete");
            SharedPreferences.Editor editor = user_sp.edit();
            editor.putString("default", userKey);
            editor.commit();
            textViewKeyDisplay.setText(userKey);
        }
    }

    class HTTPPutAsyncTask extends AsyncTask<Double, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("Sending PUT to network");
        }

        protected String doInBackground(Double... params) {
            //String resp =
            HttpHelper.updateLocation(userKey, params[0], params[1]);
            //userKey = JsonHelper.getId(resp);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println("Network PUT complete");
            //SharedPreferences.Editor editor = user_sp.edit();
            //editor.putString("default", userKey);
            //editor.commit();
            //textViewKeyDisplay.setText(userKey);
        }
    }

}
