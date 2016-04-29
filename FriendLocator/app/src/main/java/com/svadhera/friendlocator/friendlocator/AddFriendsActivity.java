package com.svadhera.friendlocator.friendlocator;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.ArraySet;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Set;

public class AddFriendsActivity extends AppCompatActivity {

    public static final String FRIENDS_PREFS = "com.svadhera.friendlocator.friendlocator.FRIENDS_PREFERENCE_FILE_KEY";
    //public static final String FRIENDS_LOC_PREFS = "com.svadhera.friendlocator.friendlocator.FRIENDS_LOC_PREFERENCE_FILE_KEY";
    SharedPreferences friends_sp;
    //SharedPreferences friends_loc_sp;

    EditText eTKeyEntered;
    EditText eTNameEntered;

    double tempLat;
    double tempLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveFriend();
            }
        });
        fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_save_white_48dp));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        friends_sp = getSharedPreferences(FRIENDS_PREFS, 0);
        //friends_loc_sp = getSharedPreferences(FRIENDS_LOC_PREFS, 0);
        eTKeyEntered = (EditText) findViewById(R.id.eTKeyEntered);
        eTNameEntered = (EditText) findViewById(R.id.eTNameEntered);
    }

    private void saveFriend() {
        String key = eTKeyEntered.getText().toString();
        String name = eTNameEntered.getText().toString();
        if (key.isEmpty() || name.isEmpty()) {
            Toast.makeText(this, "Both key and name are mandatory.Please try again !", Toast.LENGTH_SHORT).show();
            return;
        }

        if (friends_sp.contains(key)) {
            String s = friends_sp.getString(key, "Error fetching key");
            Toast.makeText(this, "Friend with same key already saved  - " + s, Toast.LENGTH_SHORT).show();
            return;
        };

        boolean keyExists = false;
        try {
            keyExists = new HTTPGetAsyncTask().execute(key).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (keyExists) {
            SharedPreferences.Editor editor = friends_sp.edit();
            editor.putString(key, name);
            editor.commit();
            //editor = friends_loc_sp.edit();
            //Set<String> locationSet = new ArraySet<>(2);
            //locationSet.add(String.valueOf(tempLat));
            //locationSet.add(String.valueOf(tempLng));
            //editor.putStringSet(key,locationSet);
            //editor.commit();
            Toast.makeText(this, "Friend " + name + " has been saved !", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Network error or invalid key !", Toast.LENGTH_SHORT).show();
        }
    }

    class HTTPGetAsyncTask extends AsyncTask<String, String, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("Fetching from network");
        }

        protected Boolean doInBackground(String... params) {
            return HttpHelper.checkKey(params[0]);
            /*if (!location.equals("error")) {
                tempLat = JsonHelper.getLat(location);
                tempLng = JsonHelper.getLng(location);
                return true;
            }
            return false;*/
        }

        @Override
        protected void onPostExecute(Boolean flag) {
            super.onPostExecute(flag);
            System.out.println("Network fetch complete");
        }
    }

}
