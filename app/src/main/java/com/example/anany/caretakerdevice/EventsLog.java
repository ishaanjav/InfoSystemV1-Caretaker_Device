package com.example.anany.caretakerdevice;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class EventsLog extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ImageView imageView;
    ListView listView;
    List<Date> listDates;
    ArrayList<Integer> order = new ArrayList<>();
    ArrayList<Events> correctorder;
    ArrayList<Events> events;
    ArrayList<String> times;
    Spinner eventtype, specifiertype;
    RelativeLayout nomatches;
    EditText specifier;
    int timers = 1;
    DatabaseReference databaseReference6;
    Button apply;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference VisitorLogin, WorkerLogin, AccountCreation, PendingApproval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_log);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        correctorder = new ArrayList<>();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        notifyForChange();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        apply = findViewById(R.id.apply);
        nomatches = findViewById(R.id.nomatchfound);
        nomatches.setVisibility(View.INVISIBLE);
        getEntries();
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                correctorder.clear();
                events.clear();
                times.clear();
                getEntries();
            }
        });
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
        timers++;
        notificationManager.notify(timers, mBuilder.build());
        databaseReference102.removeValue();
        notifyForChange();
    }

    private void getEntries() {
        events = new ArrayList<>();
        times = new ArrayList<>();
        listDates = new ArrayList<>();
        events.clear();

        firebaseDatabase = FirebaseDatabase.getInstance();
        VisitorLogin = firebaseDatabase.getReference("/Events Log");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                specifier = findViewById(R.id.specifier);
                specifiertype = findViewById(R.id.specifiertype);
                eventtype = findViewById(R.id.eventtype);
                String seventtype = eventtype.getSelectedItem().toString();
                String sspecifiertype = specifiertype.getSelectedItem().toString();
                String specific = specifier.getText().toString();


                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String stime = child.child("Time").getValue(String.class);
                    String sname = child.child("Name").getValue(String.class);
                    String sphone = child.child("Phone").getValue(String.class);
                    if (sphone == null) {
                        sphone = "";
                    }
                    String semail = child.child("Email").getValue(String.class);
                    if (semail == null) {
                        semail = "";
                    }
                    String susername = child.child("Username").getValue(String.class);
                    if (susername == null) {
                        /* makeToast(sname + " "  + sphone);*/
                        susername = "";
                    }
                    String relation = child.child("Relation").getValue(String.class);
                    if (relation == null) {
                        /* makeToast(sname + " "  + sphone);*/
                        relation = "";
                    }
                    String sdescript = "";
                    String classifier = child.child("Classifier").getValue(String.class);
                    if (classifier.contains("Logout") || classifier.contains("Fail") || classifier.contains("Updat") || classifier.contains("Worker")) {
                        sdescript = "No description available";
                    } else {
                        sdescript = child.child("Description").getValue().toString();
                    }

                    if (sname.contains("Caretaker")) {

                    } else {
                        if (seventtype.equals("All")) {
                            if (sspecifiertype.equals("All")) {
                                times.add(stime);
                                specifier.setText("");
                                events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                            } else if (sspecifiertype.equals("Name")) {
                                if (sname.contains(specific)) {
                                    times.add(stime);
                                    events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                                }
                            } else if (sspecifiertype.equals("Username")) {
                                if (susername.contains(specific)) {
                                    times.add(stime);
                                    events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                                }
                            } else if (sspecifiertype.equals("Relation")) {
                                if (relation.contains(specific)) {
                                    times.add(stime);
                                    events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                                } else if (relation == null) {
                                    makeToast("NULL" + sname);
                                }
                            } else if (sspecifiertype.equals("Phone Number")) {
                                if (sphone == null) {

                                } else if (sphone.contains(specific)) {
                                    times.add(stime);
                                    events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                                }
                            } else if (sspecifiertype.equals("Email Address")) {
                                if (semail == null) {

                                } else if (semail.contains(specific)) {
                                    times.add(stime);
                                    events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                                }
                            }
                        } else if (seventtype.equals("All Logins")) {
                            if (classifier.equals("Worker") || classifier.equals("Visitor")) {
                                if (sspecifiertype.equals("All")) {
                                    times.add(stime);
                                    specifier.setText("");
                                    events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                                } else if (sspecifiertype.equals("Name")) {
                                    if (sname.contains(specific)) {
                                        times.add(stime);
                                        events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                                    }
                                } else if (sspecifiertype.equals("Username")) {
                                    if (susername.contains(specific)) {
                                        times.add(stime);
                                        events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                                    }
                                } else if (sspecifiertype.equals("Relation")) {
                                    if (relation.contains(specific)) {
                                        times.add(stime);
                                        events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                                    } else if (relation == null) {
                                        makeToast("NULL" + sname);
                                    }
                                } else if (sspecifiertype.equals("Phone Number")) {
                                    if (sphone == null) {

                                    } else if (sphone.contains(specific)) {
                                        times.add(stime);
                                        events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                                    }
                                } else if (sspecifiertype.equals("Email Address")) {
                                    if (semail == null) {

                                    } else if (semail.contains(specific)) {
                                        times.add(stime);
                                        events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                                    }
                                }

                            }

                        } else if (seventtype.equals("Pending Requests")) {
                            if (classifier.equals("Pending")) {
                                if (sspecifiertype.equals("All")) {
                                    times.add(stime);
                                    specifier.setText("");
                                    events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                                } else if (sspecifiertype.equals("Name")) {
                                    if (sname.contains(specific)) {
                                        times.add(stime);
                                        events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                                    }
                                } else if (sspecifiertype.equals("Username")) {
                                    if (susername.contains(specific)) {
                                        times.add(stime);
                                        events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                                    }
                                } else if (sspecifiertype.equals("Relation")) {
                                    if (relation.contains(specific)) {
                                        times.add(stime);
                                        events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                                    } else if (relation == null) {
                                        makeToast("NULL" + sname);
                                    }
                                } else if (sspecifiertype.equals("Phone Number")) {
                                    if (sphone == null) {

                                    } else if (sphone.contains(specific)) {
                                        times.add(stime);
                                        events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                                    }
                                } else if (sspecifiertype.equals("Email Address")) {
                                    if (semail == null) {

                                    } else if (semail.contains(specific)) {
                                        times.add(stime);
                                        events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                                    }
                                }

                            }
                        } else if (seventtype.equals("Account Approvals")) {
                            if (classifier.equals("Approved")) {
                                if (sspecifiertype.equals("All")) {
                                    times.add(stime);
                                    specifier.setText("");
                                    events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                                } else if (sspecifiertype.equals("Name")) {
                                    if (sname.contains(specific)) {
                                        times.add(stime);
                                        events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                                    }
                                } else if (sspecifiertype.equals("Username")) {
                                    if (susername.contains(specific)) {
                                        times.add(stime);
                                        events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                                    }
                                } else if (sspecifiertype.equals("Relation")) {
                                    if (relation.contains(specific)) {
                                        times.add(stime);
                                        events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                                    } else if (relation == null) {
                                        makeToast("NULL" + sname);
                                    }
                                } else if (sspecifiertype.equals("Phone Number")) {
                                    if (sphone == null) {

                                    } else if (sphone.contains(specific)) {
                                        times.add(stime);
                                        events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                                    }
                                } else if (sspecifiertype.equals("Email Address")) {
                                    if (semail == null) {

                                    } else if (semail.contains(specific)) {
                                        times.add(stime);
                                        events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                                    }
                                }

                            }
                        } else if (seventtype.equals("Deleted/Declined")) {
                            if (classifier.equals("Deleted") || classifier.equals("Declined")) {
                                if (sspecifiertype.equals("All")) {
                                    times.add(stime);
                                    specifier.setText("");
                                    events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                                } else if (sspecifiertype.equals("Name")) {
                                    if (sname.contains(specific)) {
                                        times.add(stime);
                                        events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                                    }
                                } else if (sspecifiertype.equals("Username")) {
                                    if (susername.contains(specific)) {
                                        times.add(stime);
                                        events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                                    }
                                } else if (sspecifiertype.equals("Relation")) {
                                    if (relation.contains(specific)) {
                                        times.add(stime);
                                        events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                                    } else if (relation == null) {
                                        makeToast("NULL" + sname);
                                    }
                                } else if (sspecifiertype.equals("Phone Number")) {
                                    if (sphone == null) {

                                    } else if (sphone.contains(specific)) {
                                        times.add(stime);
                                        events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                                    }
                                } else if (sspecifiertype.equals("Email Address")) {
                                    if (semail == null) {

                                    } else if (semail.contains(specific)) {
                                        times.add(stime);
                                        events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                                    }
                                }

                            }
                        } else if (seventtype.equals("Updated")) {
                            if (classifier.equals("Updated")) {
                                if (sspecifiertype.equals("All")) {
                                    times.add(stime);
                                    specifier.setText("");
                                    events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                                } else if (sspecifiertype.equals("Name")) {
                                    if (sname.contains(specific)) {
                                        times.add(stime);
                                        events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                                    }
                                } else if (sspecifiertype.equals("Username")) {
                                    if (susername.contains(specific)) {
                                        times.add(stime);
                                        events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                                    }
                                } else if (sspecifiertype.equals("Relation")) {
                                    if (relation.contains(specific)) {
                                        times.add(stime);
                                        events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                                    } else if (relation == null) {
                                        makeToast("NULL" + sname);
                                    }
                                } else if (sspecifiertype.equals("Phone Number")) {
                                    if (sphone == null) {

                                    } else if (sphone.contains(specific)) {
                                        times.add(stime);
                                        events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                                    }
                                } else if (sspecifiertype.equals("Email Address")) {
                                    if (semail == null) {

                                    } else if (semail.contains(specific)) {
                                        times.add(stime);
                                        events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                                    }
                                }

                            }

                        } else if (seventtype.equals("All Logouts")) {
                            if (classifier.equals("Visitor Logout") || classifier.equals("Worker Logout")) {
                                if (sspecifiertype.equals("All")) {
                                    times.add(stime);
                                    specifier.setText("");
                                    events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                                } else if (sspecifiertype.equals("Name")) {
                                    if (sname.contains(specific)) {
                                        times.add(stime);
                                        events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                                    }
                                } else if (sspecifiertype.equals("Username")) {
                                    if (susername.contains(specific)) {
                                        times.add(stime);
                                        events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                                    }
                                } else if (sspecifiertype.equals("Relation")) {
                                    if (relation.contains(specific)) {
                                        times.add(stime);
                                        events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                                    } else if (relation == null) {
                                        makeToast("NULL" + sname);
                                    }
                                } else if (sspecifiertype.equals("Phone Number")) {
                                    if (sphone == null) {

                                    } else if (sphone.contains(specific)) {
                                        times.add(stime);
                                        events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                                    }
                                } else if (sspecifiertype.equals("Email Address")) {
                                    if (semail == null) {

                                    } else if (semail.contains(specific)) {
                                        times.add(stime);
                                        events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                                    }
                                }

                            }
                        } else if (seventtype.equals("Failed Logins")) {
                            if (classifier.equals("Failed")) {
                                if (sspecifiertype.equals("All")) {
                                    times.add(stime);
                                    specifier.setText("");
                                    events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                                } else if (sspecifiertype.equals("Name")) {
                                    if (sname.contains(specific)) {
                                        times.add(stime);
                                        events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                                    }
                                } else if (sspecifiertype.equals("Username")) {
                                    if (susername.contains(specific)) {
                                        times.add(stime);
                                        events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                                    }
                                } else if (sspecifiertype.equals("Relation")) {
                                    if (relation.contains(specific)) {
                                        times.add(stime);
                                        events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                                    } else if (relation == null) {
                                        makeToast("NULL" + sname);
                                    }
                                } else if (sspecifiertype.equals("Phone Number")) {
                                    if (sphone == null) {

                                    } else if (sphone.contains(specific)) {
                                        times.add(stime);
                                        events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                                    }
                                } else if (sspecifiertype.equals("Email Address")) {
                                    if (semail == null) {

                                    } else if (semail.contains(specific)) {
                                        times.add(stime);
                                        events.add(new Events(classifier, sname, stime, sphone, semail, "0", sdescript));
                                    }
                                }

                            }
                        }

                    }

                }

                listView = findViewById(R.id.listview);
                if (events.isEmpty()) {
                    longToast("No results matched your search.");
                    nomatches.setVisibility(View.VISIBLE);
                    ArrayAdapter mAdapter = new LogAdapter(EventsLog.this, getApplicationContext(), events);
                    listView.setAdapter(mAdapter);
                    //Do code to display empty textview saying that "No Results Were Found"

                } else {
                    nomatches.setVisibility(View.INVISIBLE);
                    orderDates();
                    ArrayList<Events> odd = new ArrayList<>();
                    odd.clear();
                    for (int i = 0; i < correctorder.size(); i++) {
                        odd.add(correctorder.get(correctorder.size() - 1 - i));
                    }
                    ArrayAdapter mAdapter = new LogAdapter(EventsLog.this, getApplicationContext(), odd);
                    listView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        VisitorLogin.addValueEventListener(valueEventListener);


        /*VisitorLogin = firebaseDatabase.getReference("/Login/Visitor Login");
        ValueEventListener readvisitor = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String stime = child.child("time").getValue(String.class);
                    String sname = child.child("name").getValue(String.class);
                    String sphone = child.child("phone").getValue(String.class);
                    String semail = child.child("email").getValue(String.class);
                    times.add(stime);
                    events.add(new Events("Visitor", sname, stime, sphone, semail, "0", sdescript));

                    //Oct 14, 2018 8:25:15 PM
                    DateFormat dateFormatter = new SimpleDateFormat("MMM d, yyyy HH:mm:ss aaa");

                   *//* try {
                        listDates.add(dateFormatter.parse(stime));
                    } catch (ParseException ex) {
                        makeToast(ex.toString());
                    }*//*
                }

                WorkerLogin = firebaseDatabase.getReference("/Login/Worker Login");
                ValueEventListener readworker = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            String stime = child.child("Time").getValue(String.class);
                            String sname = child.child("Name").getValue(String.class);
                            String sphone = child.child("Company").getValue(String.class);
                            String semail = child.child("Service").getValue(String.class);
                            times.add(stime);
                            events.add(new Events("Worker", sname, stime, sphone, semail, "0", sdescript));

                            //Oct 14, 2018 8:25:15 PM
                            DateFormat dateFormatter = new SimpleDateFormat("MMM d, yyyy HH:mm:ss aaa");

                           *//* try {
                                listDates.add(dateFormatter.parse(stime));
                            } catch (ParseException ex) {
                                makeToast(ex.toString());
                            }*//*
                        }
                        AccountCreation = firebaseDatabase.getReference("/Create Account");
                        ValueEventListener readcreate = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    String stime = child.child("Created").getValue(String.class);
                                    String sname = child.child("Name").getValue(String.class);
                                    String sphone = child.child("Phone").getValue(String.class);
                                    String semail = child.child("Email").getValue(String.class);
                                    if (sname.contains("Caretaker")) {

                                    } else {
                                        times.add(stime);
                                        events.add(new Events("Existing", sname, stime, sphone, semail, "0", sdescript));
                                    }

                                    //Oct 14, 2018 8:25:15 PM
                                    DateFormat dateFormatter = new SimpleDateFormat("MMM d, yyyy HH:mm:ss aaa");

                                   *//* try {
                                        listDates.add(dateFormatter.parse(stime));
                                    } catch (ParseException ex) {
                                        makeToast(ex.toString());
                                    }*//*
                                }

                                PendingApproval = firebaseDatabase.getReference("/Pending Requests");
                                ValueEventListener readpending = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                                            String stime = child.child("Time").getValue(String.class);
                                            String sname = child.child("Name").getValue(String.class);
                                            String sphone = child.child("Phone").getValue(String.class);
                                            String semail = child.child("Email").getValue(String.class);
                                            times.add(stime);
                                            events.add(new Events("Pending", sname, stime, sphone, semail, "0", sdescript));
                                            //Oct 14, 2018 8:25:15 PM
                                            DateFormat dateFormatter = new SimpleDateFormat("MMM d, yyyy HH:mm:ss aaa");

                                            *//*try {
                                                listDates.add(dateFormatter.parse(stime));
                                            } catch (ParseException ex) {
                                                makeToast(ex.toString());
                                            }
*//*
                                        }

                                        listView = findViewById(R.id.listview);
                                        if (events.isEmpty()) {
                                            makeToast("EMPTY");
                                        } else {
                                            orderDates();
                                            ArrayAdapter mAdapter = new LogAdapter(EventsLog.this, getApplicationContext(), correctorder);
                                            listView.setAdapter(mAdapter);
                                        }
                                      *//*  DateFormat dateFormatter = new SimpleDateFormat("MMM d, yyyy HH:mm:ss aaa");

                                        try {
                                            listDates.add(dateFormatter.parse("Oct 14, 2018 8:25:15 PM"));
                                        } catch (Exception e) {
                                            makeToast(e.toString());
                                        }

                                        Collections.sort(listDates);

                                        makeToast(listDates.toString());*//*

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        makeToast(databaseError.getMessage());
                                    }
                                };
                                PendingApproval.addValueEventListener(readpending);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                makeToast(databaseError.getMessage());

                            }
                        };
                        AccountCreation.addValueEventListener(readcreate);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        makeToast(databaseError.getMessage());

                    }
                };
                WorkerLogin.addValueEventListener(readworker);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        VisitorLogin.addValueEventListener(readvisitor);
*/
    }

    public void orderDates() {

        events = events;
        order.clear();
        ArrayList<String> times = new ArrayList<>();
        ArrayList<Long> yearpoints = new ArrayList<>();
        ArrayList<Long> monthpoints = new ArrayList<>();
        ArrayList<Long> daypoints = new ArrayList<>();
        ArrayList<Long> PMpoints = new ArrayList<>();
        ArrayList<Long> hourpoints = new ArrayList<>();
        ArrayList<Long> minutepoints = new ArrayList<>();
        ArrayList<Long> secondpoints = new ArrayList<>();
        ArrayList<Long> totalpoints = new ArrayList<>();
        ArrayList<Long> originalpoints = new ArrayList<>();
        //correctorder is events     order is integer.
        for (Events e : events) {
            String time = e.getTime();
            String[] parts;

            try {
                parts = time.split("[ ,]+");
                if (parts[2].equals("2018")) {
                    yearpoints.add(10000000000l);
                } else if (parts[2].equals("2019")) {
                    yearpoints.add(20000000000l);
                } else {
                    yearpoints.add(30000000000l);
                }

                monthpoints.add(gradePoints(parts) * 1000l);

                //String[] sdaynum = parts[1].split(",");
                String sdaynum = parts[1];
                int daynum = 0;
                try {
                    daynum = Integer.parseInt(sdaynum);
                } catch (Exception m) {

                }

                daypoints.add(daynum * 1000000l);

                String afternoon = parts[4];
                if (afternoon.equals("PM")) {
                    PMpoints.add(800000l);
                } else {
                    PMpoints.add(500000l);
                }

                String[] numtime = parts[3].split(":");
                int hour = Integer.parseInt(numtime[0]);
                int minutes = Integer.parseInt(numtime[1]);
                int seconds = Integer.parseInt(numtime[2]);

                hourpoints.add(hour * 10000l);
                minutepoints.add(minutes * 100l);
                secondpoints.add(seconds * 1l);

                times.add(time);
            } catch (Exception mt) {

            }


        }

        for (int i = 0; i < hourpoints.size(); i++) {
            totalpoints.add(hourpoints.get(i) + secondpoints.get(i) + minutepoints.get(i) + monthpoints.get(i) + daypoints.get(i) +
                    yearpoints.get(i) + PMpoints.get(i));
            originalpoints.add(totalpoints.get(i));
        }
        Collections.sort(totalpoints);

        for (int i = 0; i < totalpoints.size(); i++) {

            for (int j = 0; j < originalpoints.size(); j++) {
                if (totalpoints.get(i) == originalpoints.get(j)) {
                    order.add(j);
                }
            }

        }
        makeToast(order.size() + " Results Found");
        int ut = 0;
        for (int a : order) {
            //   makeToast(Integer.toString(order.size()));
            Events event1 = events.get(a);

            correctorder.add(new Events(event1.getClassify(), event1.getName(), event1.getTime(), event1.getPhone(), event1.getEmail(), Integer.toString(a), event1.getRelation()/*Long.toString(totalpoints.get(ut))*/));
            ut++;
        }

        //Oct 14, 2018 8:25:15 PM

    }

    private int gradePoints(String[] parts) {
        if (parts[0].equals("Jan")) {
            return 100000;
        } else if (parts[0].equals("Feb")) {
            return 200000;
        } else if (parts[0].equals("Mar")) {
            return 300000;
        } else if (parts[0].equals("Apr")) {
            return 400000;
        } else if (parts[0].equals("May")) {
            return 500000;
        } else if (parts[0].equals("Jun")) {
            return 600000;
        } else if (parts[0].equals("Jul")) {
            return 700000;
        } else if (parts[0].equals("Aug")) {
            return 800000;
        } else if (parts[0].equals("Sep")) {
            return 900000;
        } else if (parts[0].equals("Oct")) {
            return 1000000
                    ;
        } else if (parts[0].equals("Nov")) {
            return 1100000;
        } else if (parts[0].equals("Dec")) {
            return 1200000;
        } else {
            return 0;
        }
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
        getMenuInflater().inflate(R.menu.events_log, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.backtohome) {
            startActivity(new Intent(EventsLog.this, CaretakerAccount.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(EventsLog.this, CaretakerAccount.class));
        } else if (id == R.id.nav_events) {
            makeToast("Already in Events Log");
        } else if (id == R.id.nav_aboutapp) {
            startActivity(new Intent(EventsLog.this, AboutApp.class));

        } else if (id == R.id.nav_requests) {
            startActivity(new Intent(EventsLog.this, PendingRequests.class));
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(EventsLog.this, RealSettings.class));

        } else if (id == R.id.nav_gps) {
            startActivity(new Intent(this, GPS.class));
        } else if (id == R.id.nav_users) {
            startActivity(new Intent(getApplicationContext(), ExistingUsers.class));
        } else if (id == R.id.emotions) {
            startActivity(new Intent(getApplicationContext(), EmotionsAnalyzer.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void makeToast(String s) {
        Toast.makeText(EventsLog.this, s, Toast.LENGTH_SHORT).show();
    }

    private void longToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }
}
