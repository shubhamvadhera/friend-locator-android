package com.svadhera.friendlocator.friendlocator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Friends extends AppCompatActivity {

    public static final String FRIENDS_PREFS = "com.svadhera.friendlocator.friendlocator.FRIENDS_PREFERENCE_FILE_KEY";
    SharedPreferences friends_sp;

    /*TextView tvFriend1Lat;
    TextView tvFriend1Lng;*/
    /*double friend1Lat;
    double friend1Lng;*/
    //List<String> friend_list = new ArrayList<String>();
    private ListView listView;
    Button btnAddFriend;
    Button btnRemFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToActivityFriendsMap();
            }
        });
        fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_place_white_48dp));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = (ListView) findViewById(R.id.listView);
        friends_sp = getSharedPreferences(FRIENDS_PREFS, 0);
        btnAddFriend = (Button) findViewById(R.id.btnAddFriend);
        btnRemFriend = (Button) findViewById(R.id.btnRemoveFriend);
        //populateListView();
        //tvFriend1Lat = (TextView) findViewById(R.id.tvFriend1Lat);
        //tvFriend1Lng = (TextView) findViewById(R.id.tvFriend1Lng);
        /*new HTTPAsyncTask().execute("5706bcf94c8918056aea0817");*/
        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivityAddFriends();
            }
        });
        btnRemFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivityRemoveFriends();
            }
        });
        //tvFriend1.setText(friend1Location);
    }

    @Override
    protected void onStart() {
        super.onStart();
        populateListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateListView();
    }

    /*class HTTPGetAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("Fetching from network");
        }

        protected String doInBackground(String... params) {
            String location = HttpHelper.getLocation(params[0]);
            //friend1Lat = JsonHelper.getLat(location);
            //friend1Lng = JsonHelper.getLng(location);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println("Network fetch complete");
            refreshTextViews();
        }
    }*/

    private void refreshTextViews () {
        //tvFriend1Lat.setText(String.valueOf(friend1Lat));
        //tvFriend1Lng.setText(String.valueOf(friend1Lng));
    }

    public void goToActivityFriendsMap () {
        Intent intent = new Intent(this, MapsActivityFriends.class);
        startActivity(intent);
    }

    public void goToActivityAddFriends () {
        Intent intent = new Intent(this, AddFriendsActivity.class);
        startActivity(intent);
    }

    public void goToActivityRemoveFriends () {
        Intent intent = new Intent(this, RemoveFriendsActivity.class);
        startActivity(intent);
    }

    private void populateListView () {
        List<String> friendList = new ArrayList<> ();
        Map<String,?> keys = friends_sp.getAll();
        for(Map.Entry<String,?> entry : keys.entrySet()){
            if (entry.getValue() instanceof String) {
                friendList.add((String) entry.getValue());
            }
        }
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.friends_listview, friendList);
        listView.setAdapter(adapter);
    }

}
