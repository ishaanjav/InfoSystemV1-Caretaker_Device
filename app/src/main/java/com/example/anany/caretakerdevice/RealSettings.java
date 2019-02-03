package com.example.anany.caretakerdevice;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class RealSettings extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String phone, description, username, password, name;
    EditText editphone, editdescription, editpassword, editname, editusername;
    String rowid;

    int times = 1;
    DatabaseReference databaseReference6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        notifyForChange();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        bindViews();
        loadinfo();
    }

    private void bindViews() {
        editdescription = findViewById(R.id.editdescription);
        editname = findViewById(R.id.editname);
        editphone = findViewById(R.id.phoneedit);
        editusername = findViewById(R.id.usernameedit);
        editpassword = findViewById(R.id.passwordedit);
    }

    private void loadinfo() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("/Create Account");

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String tempname = child.child("Name").getValue().toString();
                    if (tempname.contains("Caretaker")) {
                        name = tempname.substring(10, tempname.length());
                        rowid = child.getKey();
                        phone = child.child("Phone").getValue().toString();
                        description = child.child("Relation").getValue().toString();
                        username = child.child("Username").getValue().toString();
                        password = child.child("Password").getValue().toString();
                        editname.setText(name);
                        editdescription.setText(description);
                        editpassword.setText(password);
                        editusername.setText(username);
                        editphone.setText(phone);

                    } else {
                        continue;
                    }

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
        getMenuInflater().inflate(R.menu.real_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.save) {
            String path = "Create Account/" + rowid;
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            final DatabaseReference dbref = database.getReference(path);
            dbref.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });

            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference("/Create Account");

            HashMap<String, String> values = new HashMap<>();
            values.put("Name", "Caretaker " + editname.getText().toString());
            String finalusername = editusername.getText().toString();
            String finalphone = editphone.getText().toString();
            String finalrelation = editdescription.getText().toString();
            String finalpassword = editpassword.getText().toString();

            values.put("Username", finalusername);
            values.put("Password", finalpassword);
            values.put("Phone", finalphone);
            values.put("Relation", finalrelation);


            databaseReference.push().setValue(values, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    if (databaseError == null) {
                        makeToast("Updated Info.");
                    } else {
                        makeToast("Error saving Information.");
                    }
                }
            });


            startActivity(new Intent(getApplicationContext(), CaretakerAccount.class));
        }

        return super.onOptionsItemSelected(item);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(getApplicationContext(), CaretakerAccount.class));
        } else if (id == R.id.nav_events) {
            startActivity(new Intent(RealSettings.this, EventsLog.class));
        } else if (id == R.id.nav_aboutapp) {
            startActivity(new Intent(RealSettings.this, AboutApp.class));

        } else if (id == R.id.nav_requests) {
            startActivity(new Intent(RealSettings.this, PendingRequests.class));
        } else if (id == R.id.nav_settings) {
            makeToast("Already in Settings");

        } else if (id == R.id.nav_gps) {
            startActivity(new Intent(this, GPS.class));

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
