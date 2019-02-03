package com.example.anany.caretakerdevice;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class GPS extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Button viewmap = findViewById(R.id.viewmap);
        viewmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
            }
        });

    /*    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Update");
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("Value", "True");
        // makeToast(hashMap.toString());

        databaseReference.push().setValue(hashMap);*/

    }

   /* @Override
    protected void onPause() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Update");
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("Value", "False");
        //makeToast(hashMap.toString());
        databaseReference.push().setValue(hashMap);

        super.onPause();
    }*/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            startActivity(new Intent(getApplicationContext(), CaretakerAccount.class));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.g, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.next) {
            startActivity(new Intent(getApplicationContext(), MapsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            startActivity(new Intent(this, CaretakerAccount.class));
        } else if (id == R.id.nav_events) {
            startActivity(new Intent(this, EventsLog.class));
        } else if (id == R.id.nav_aboutapp) {
            startActivity(new Intent(this, AboutApp.class));

        } else if (id == R.id.nav_requests) {
            startActivity(new Intent(this, PendingRequests.class));

        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, RealSettings.class));

        } else if (id == R.id.nav_gps) {
            makeToast("Already in the GPS Page. Click the button to view the map.");

        } else if (id == R.id.nav_users) {
            startActivity(new Intent(getApplicationContext(), ExistingUsers.class));
        }else if(id == R.id.emotions){
            startActivity(new Intent(getApplicationContext(), EmotionsAnalyzer.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void makeToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
