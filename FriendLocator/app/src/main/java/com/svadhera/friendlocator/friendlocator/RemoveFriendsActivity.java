package com.svadhera.friendlocator.friendlocator;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Map;

public class RemoveFriendsActivity extends AppCompatActivity {

    public static final String FRIENDS_PREFS = "com.svadhera.friendlocator.friendlocator.FRIENDS_PREFERENCE_FILE_KEY";

    SharedPreferences friends_sp;

    EditText eTRemNameEntered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_friends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeFriend();
            }
        });
        fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_remove_circle_outline_white_48dp));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        friends_sp = getSharedPreferences(FRIENDS_PREFS, 0);
        eTRemNameEntered = (EditText) findViewById(R.id.eTRemNameEntered);
    }

    private void removeFriend() {
        String name = eTRemNameEntered.getText().toString();
        if (name.isEmpty()) {
            Toast.makeText(this, "Enter name of friend to remove !", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean foundName = false;
        String removeKey = "";
        Map<String,?> keys = friends_sp.getAll();
        for(Map.Entry<String,?> entry : keys.entrySet()){
            if (entry.getValue() instanceof String) {
                if(entry.getValue().toString().equals(name)) {
                    removeKey = entry.getKey();
                    foundName = true;
                }
            }
        }
        if (foundName) {
            SharedPreferences.Editor editor = friends_sp.edit();
            editor.remove(removeKey);
            editor.commit();
            Toast.makeText(this, "Friend removed from your list!", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "Name not found! Please check name entered.", Toast.LENGTH_SHORT).show();
    }

}
