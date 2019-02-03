package com.example.anany.caretakerdevice;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ExistingUsers extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DatabaseReference dref;
    ListView listview;
    List<String> list = new ArrayList<>();
    Button button;
    CardView hidden;
    DatabaseReference databaseReference6;
    int times = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing_users);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        hidden = findViewById(R.id.hidden);
        hidden.setVisibility(View.INVISIBLE);
        buttonClicked();
        notifyForChange();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void notifyForChange() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference6 = firebaseDatabase.getReference("Notify Login");

                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<String> value = new ArrayList<>();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            value.add(child.child("Name").getValue(String.class));
                            String st = child.child("Name").getValue(String.class);
                            //   makeToast(st);
                        }

                        if (value.isEmpty()) {
                            //   makeToast("NULL " + value);
                            notifyForChange();
                        } else {
                            //  makeToast(value.toString());
                            makeNotification(databaseReference6, value.get(0));

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };

                databaseReference6.addValueEventListener(valueEventListener);

                //Write the code to read from database path and check if it not empty.
                //If it is not empty. Get the data at the path and make a notification from it by passing the info into a notification method.
                // Also, in that notification method, delete the databaseReference. Then, call the notifyForChange() method.
                //If it is empty, call this method again.


            }
        };

        Handler h = new Handler();
        h.postDelayed(r, 5000);

    }

    private void makeNotification(DatabaseReference databaseReference102, String value) {
        //  makeToast("IN MAKE NOTIFICATION!");
        Intent intent = new Intent(getApplicationContext(), CaretakerAccount.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "Channel")
                .setSmallIcon(R.drawable.notification)
                .setContentTitle("A Visitor Logged In!")
                .setContentText(value + " just logged in! They are at the front door.")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(value + " just logged in! They are at the front door."))
                .setContentIntent(pendingIntent)
                .setAutoCancel(false)
                .setPriority(NotificationCompat.PRIORITY_MAX);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        times++;
        notificationManager.notify(times, mBuilder.build());
        databaseReference102.removeValue();
        notifyForChange();
    }

    private void buttonClicked() {
        listview = findViewById(R.id.listview);
        readToListView();
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(ExistingUsers.this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    105);
        } else {
            readToListView();
        }
    }

    private void readToListView() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference("/Create Account");
        listview = findViewById(R.id.listview);
        dref = FirebaseDatabase.getInstance().getReference();


        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> name = new ArrayList<>();
                ArrayList<String> password = new ArrayList<>();
                ArrayList<String> username = new ArrayList<>();
                ArrayList<String> phone = new ArrayList<>();
                ArrayList<String> email = new ArrayList<>();
                ArrayList<String> description = new ArrayList<>();
                ArrayList<String> relation = new ArrayList<>();
                ArrayList<String> times = new ArrayList<>();


                ArrayList<ExistingListClass> arrayList = new ArrayList<>();

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String sname = child.child("Name").getValue(String.class);
                    String spassword = child.child("Password").getValue(String.class);
                    String susername = child.child("Username").getValue(String.class);
                    String sphone = child.child("Phone").getValue(String.class);
                    String semail = child.child("Email").getValue(String.class);
                    String sdescription = child.child("Description").getValue(String.class);
                    String srelation = child.child("Relation").getValue(String.class);
                    String stime = child.child("Approved").getValue(String.class);

                    if (sname.contains("Caretaker")) {

                    } else {
                        name.add(sname);
                        username.add(susername);
                        password.add(spassword);
                        phone.add(sphone);
                        email.add(semail);
                        description.add(sdescription);
                        relation.add(srelation);
                        times.add(stime);
                        arrayList.add(new ExistingListClass(sname, susername, spassword, sdescription, sphone, semail, srelation, stime));

                    }

                }

                ArrayList<ExistingListClass> arrayList2 = new ArrayList<>();

                for (int i = 0; i < arrayList.size(); i++) {
                    arrayList2.add(arrayList.get(arrayList.size() - 1 - i));
                }

                ArrayAdapter mAdapter = new UsersAdapter(ExistingUsers.this, getApplicationContext(), arrayList2);
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        makeToast("Entry clicked");
                        ExistingListClass entry = (ExistingListClass) parent.getItemAtPosition(position);
                        makeToast(entry.getName());
                    }
                });
                listview.setAdapter(mAdapter);
                if (name.isEmpty()) {
                    hidden.setVisibility(View.VISIBLE);
                } else {
                    hidden.setVisibility(View.INVISIBLE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.addValueEventListener(valueEventListener);

    }


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
        getMenuInflater().inflate(R.menu.existing_users, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.gohome) {
            startActivity(new Intent(ExistingUsers.this, CaretakerAccount.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(ExistingUsers.this, CaretakerAccount.class));
        } else if (id == R.id.nav_events) {
            startActivity(new Intent(ExistingUsers.this, EventsLog.class));
        } else if (id == R.id.nav_aboutapp) {
            startActivity(new Intent(ExistingUsers.this, AboutApp.class));

        } else if (id == R.id.nav_requests) {
            startActivity(new Intent(ExistingUsers.this, PendingRequests.class));

        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(ExistingUsers.this, RealSettings.class));

        } else if (id == R.id.nav_gps) {
            startActivity(new Intent(this, GPS.class));

        } else if (id == R.id.nav_users) {
            makeToast("Already in Existing Users.");
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
