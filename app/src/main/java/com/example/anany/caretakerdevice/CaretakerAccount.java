package com.example.anany.caretakerdevice;

import android.Manifest;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CaretakerAccount extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ImageView imageView;
    RelativeLayout getinflated;
    ArrayList<String> phone;

    int times = 1;
    String phones, email, name, description, relation, classifier2, time;

    DatabaseReference databaseReference6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caretaker_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getinflated = findViewById(R.id.inflaterholder);

        //  getinflated.removeAllViews();
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(CaretakerAccount.this,
                    new String[]{Manifest.permission.SEND_SMS},
                    105);
        }

        imageView = findViewById(R.id.imageView);

        final FirebaseDatabase database2 = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database2.getReference("Latest Login");
        //These DatabaseReferences are used to check the paths to see whether or not to make Alert Dialog if they are empty.
        final DatabaseReference databaseReference1 = database2.getReference("Latest Create");
        final DatabaseReference databaseReference2 = database2.getReference("Wrong Login");

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> times = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    times.add(child.child("Time").getValue(String.class));
                }
                if (times.isEmpty()) {
                    //break;
                } else {
                    DatabaseReference dbref = database2.getReference("Events Log");
                    ValueEventListener valueEventListener1 = new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot child : dataSnapshot.getChildren()) {

                                classifier2 = child.child("Classifier").getValue(String.class);

                                if (classifier2.equals("Visitor")) {
                                    String tphone = child.child("Phone").getValue(String.class);
                                    if (tphone == null) {
                                        phones = "";

                                    } else {
                                        phones = (tphone);
                                    }
                                    String tname = child.child("Name").getValue(String.class);
                                    String temail = child.child("Email").getValue(String.class);
                                    if (temail == null) {
                                        email = ("");
                                    } else {
                                        email = (temail);

                                    }
                                    String tdescript = child.child("Description").getValue(String.class);
                                    if (tdescript == null) {
                                        description = ("");
                                    } else {
                                        description = (tdescript);
                                    }
                                    String trelation = child.child("Relation").getValue(String.class);
                                    if (trelation == null) {
                                        relation = ("");
                                    } else {
                                        relation = (trelation);
                                    }
                                    name = (tname);
                                }

                            }
                            databaseReference.removeValue();
                            //    makeToast(name);
                            makeAlertDialog(name, phones, email, relation, description, databaseReference, classifier2);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    };
                    dbref.addValueEventListener(valueEventListener1);


                    //Read from Visitor Logins. Get the latest login. Display in custom Alert Dialog using method above with parameters.
                    //Delete everything in Latest Login
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.addValueEventListener(valueEventListener);

        ValueEventListener valueEventListener1 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> times = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    times.add(child.child("Time").getValue(String.class));
                }
                if (times.isEmpty()) {
                    //break;
                } else {
                    DatabaseReference dbref = database2.getReference("Events Log");
                    ValueEventListener valueEventListener1 = new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot child : dataSnapshot.getChildren()) {

                                classifier2 = child.child("Classifier").getValue(String.class);

                                if (classifier2.equals("Pending")) {
                                    String tphone = child.child("Phone").getValue(String.class);
                                    if (tphone == null) {
                                        phones = "";

                                    } else {
                                        phones = (tphone);
                                    }
                                    String tname = child.child("Name").getValue(String.class);
                                    String temail = child.child("Email").getValue(String.class);
                                    if (temail == null) {
                                        email = ("");
                                    } else {
                                        email = (temail);

                                    }
                                    String tdescript = child.child("Description").getValue(String.class);
                                    if (tdescript == null) {
                                        description = ("");
                                    } else {
                                        description = (tdescript);
                                    }
                                    String trelation = child.child("Relation").getValue(String.class);
                                    if (trelation == null) {
                                        relation = ("");
                                    } else {
                                        relation = (trelation);
                                    }
                                    name = (tname);
                                }

                            }
                            //    makeToast(name);

                            makeAlertPending(name, phones, email, relation, description, databaseReference1, classifier2);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    };
                    dbref.addValueEventListener(valueEventListener1);


                    //Read from Visitor Logins. Get the latest login. Display in custom Alert Dialog using method above with parameters.
                    //Delete everything in Latest Login
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        databaseReference1.addValueEventListener(valueEventListener1);

        ValueEventListener valueEventListener3 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> times = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    times.add(child.child("Time").getValue(String.class));
                }
                if (times.isEmpty()) {
                    //break;
                } else {
                    DatabaseReference dbref = database2.getReference("Events Log");
                    ValueEventListener valueEventListener1 = new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot child : dataSnapshot.getChildren()) {

                                classifier2 = child.child("Classifier").getValue(String.class);

                                if (classifier2.equals("Failed")) {
                                    name = child.child("Name").getValue(String.class);
                                    time = child.child("Time").getValue(String.class);
                                }

                            }
                            //    makeToast(name);

                            makeFailedDialog(name, time, databaseReference2, classifier2);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    };
                    dbref.addValueEventListener(valueEventListener1);


                    //Read from Visitor Logins. Get the latest login. Display in custom Alert Dialog using method above with parameters.
                    //Delete everything in Latest Login
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference2.addValueEventListener(valueEventListener3);

        //   databaseReference.removeValue();

        createNotificationChannel();
        //  notifyForChange();
        FirebaseDatabase firebaseDatabase3 = FirebaseDatabase.getInstance();
        final DatabaseReference myref = firebaseDatabase3.getReference("NWrong Login");
        databaseReference6 = firebaseDatabase3.getReference("Notify Login");

        ValueEventListener valueEventListener6 = new ValueEventListener() {
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
                    //        notifyForChange();
                } else {
                    //  makeToast(value.toString());
                    makeNotification(databaseReference6, value.get(0));

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        databaseReference6.addValueEventListener(valueEventListener6);

        ValueEventListener valueEventListener334 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> value = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    value.add(child.child("Time").getValue(String.class));
                    String st = child.child("Name").getValue(String.class);
                    //   makeToast(st);
                }

                if (value.isEmpty()) {
                    //   makeToast("NULL " + value);
                    //        notifyForChange();
                } else {
                    //  makeToast(value.toString());
                    warnNotification(myref, value.get(0));

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        myref.addValueEventListener(valueEventListener334);


        circle(); //Unimportant methods for notifications.
        listeners();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        // recoverData();

    }


    private void notifyForChange() {

        Runnable r = new Runnable() {
            @Override
            public void run() {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference6 = firebaseDatabase.getReference("Notify Login");

                ValueEventListener valueEventListener6 = new ValueEventListener() {
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
                            //makeNotification(databaseReference6, value.get(0));

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };

                databaseReference6.addValueEventListener(valueEventListener6);

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
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        times = 0;
        notificationManager.notify(times, mBuilder.build());
        databaseReference102.removeValue();
        //   notifyForChange();
    }

    private void makeAlertDialog(String name, String phone, String email, String relation, final String description, final DatabaseReference databaseReference, String classifier) {
        //Take the parameters and put them in Alert Dialog.
        //Tried changing it to CaretakerAccount.this
        final Dialog alert = new Dialog(CaretakerAccount.this);
        alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alert.setContentView(R.layout.visitorsignedin);
        alert.setCancelable(true);

        Button moreinfo = alert.findViewById(R.id.getinfo);
        TextView title = alert.findViewById(R.id.title);
        if (classifier.equals("Pending")) {
            title.setText("Account Pending Approval!");
        } else if (classifier.equals("Visitor")) {
            title.setText("A Visitor Signed In!");
        }

        MediaPlayer ring = MediaPlayer.create(CaretakerAccount.this, R.raw.doorbelltone);
        ring.start();

        TextView sname = alert.findViewById(R.id.namer);
        TextView sphone = alert.findViewById(R.id.phone);
        TextView semail = alert.findViewById(R.id.email);
        TextView srelation = alert.findViewById(R.id.relation);
        final StorageReference mImageRef =
                FirebaseStorage.getInstance().getReference("Visitors/" + name + ".jpg");

        final long ONE_MEGABYTE = 1024 * 1024;
        final ImageView imageView2 = alert.findViewById(R.id.image);

        if (mImageRef.getBytes(ONE_MEGABYTE) == null) {
            ImageView imageView = alert.findViewById(R.id.image);
            imageView.setImageResource(R.drawable.oneuser);
        } else {
            mImageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    DisplayMetrics dm = new DisplayMetrics();

                       /* imageView.setMinimumHeight(dm.heightPixels);
                        imageView.setMinimumWidth(dm.widthPixels);*/
                    imageView2.setImageBitmap(bm);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    imageView2.setImageResource(R.drawable.oneuser);
                }
            });
        }

        sname.setText(name);
        if (phone.isEmpty()) {
            sphone.setText("No Phone #");
        } else {
            phone = "(" + phone.substring(0, 3) + ") - " + phone.substring(3, 6) + " - " + phone.substring(6, 10);
            sphone.setText("Phone: " + phone);

        }

        if (email.isEmpty()) {
            semail.setText("No Email Address");
        } else {
            semail.setText("Email: " + email);
        }

        srelation.setText("Relation: " + relation);

        Button cancel = alert.findViewById(R.id.cancel_action);

        moreinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                longToast(description);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // alert.dismiss();
                alert.cancel();
                //alert.hide();
                databaseReference.removeValue();
            }
        });
        alert.show();
    }


    private void warnNotification(DatabaseReference databaseReference102, String value) {
        //  makeToast("IN MAKE NOTIFICATION!");
        Intent intent = new Intent(getApplicationContext(), CaretakerAccount.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "Channel")
                .setSmallIcon(R.drawable.notification)
                .setContentTitle("A Visitor is at the Front Door!")
                .setContentText("A person is at your front door and they failed to log in.")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("A person is at your front door and they failed to log in."))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        times = 0;
        notificationManager.notify(times, mBuilder.build());
        databaseReference102.removeValue();
        //   notifyForChange();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel";
            String description = "description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("Channel", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    private void makeFailedDialog(String name, final String time, final DatabaseReference databaseReference10, String classifier2) {
        final Dialog alert = new Dialog(this);
        alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alert.setContentView(R.layout.alertfailedlogin);
        alert.setCancelable(true);

        TextView title = alert.findViewById(R.id.title);

        title.setText("Be Aware!");

        TextView sphone = alert.findViewById(R.id.phone);
        String[] split = time.split(" ");
        String date = split[0] + " " + split[1] + " " + split[2];
        String numtime = split[3] + " " + split[4];

        sphone.setText("The visitor tried entering in this username: " + name + "\non: " + date + "\nat: " + numtime + ".");

        Button cancel = alert.findViewById(R.id.cancel_action);
        MediaPlayer ring = MediaPlayer.create(CaretakerAccount.this, R.raw.danger);
        ring.start();
        Button warn = alert.findViewById(R.id.warn);
        Button view = alert.findViewById(R.id.existingusers);
//Oct 28, 2018 8:24:19 PM"

        warn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] split = time.split(" ");
                String num = split[3] + " " + split[4];
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage("8482482353", null, "Suspicious activity was recently reported near the front door at " + num + ".", null, null);
                makeToast("Sent warning message.");
                alert.dismiss();
                databaseReference10.removeValue();

            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference10.removeValue();
                startActivity(new Intent(getApplicationContext(), ExistingUsers.class));
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
                databaseReference10.removeValue();
            }
        });
        alert.show();

    }

    private void makeAlertPending(String name, String phone, String email, String relation, final String description, final DatabaseReference databaseReference4, String classifier) {
        //Take the parameters and put them in Alert Dialog.
        final Dialog alert = new Dialog(this);
        alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alert.setContentView(R.layout.alertaccountcreated);
        alert.setCancelable(true);

        Button moreinfo = alert.findViewById(R.id.getinfo);
        Button approve = alert.findViewById(R.id.approveaction);
        TextView title = alert.findViewById(R.id.title);

        title.setText("New Account\nPending Approval!");
        MediaPlayer ring = MediaPlayer.create(CaretakerAccount.this, R.raw.createaccount);
        ring.start();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                MediaPlayer ring2 = MediaPlayer.create(CaretakerAccount.this, R.raw.createaccount);
                ring2.start();
            }
        };
        Handler h = new Handler();
        h.postDelayed(r, 1000);

        TextView sname = alert.findViewById(R.id.namer);
        TextView sphone = alert.findViewById(R.id.phone);
        TextView semail = alert.findViewById(R.id.email);
        TextView srelation = alert.findViewById(R.id.relation);
        final StorageReference mImageRef =
                FirebaseStorage.getInstance().getReference("Visitors/" + name + ".jpg");

        final long ONE_MEGABYTE = 1024 * 1024;
        final ImageView imageView2 = alert.findViewById(R.id.image);

        if (mImageRef.getBytes(ONE_MEGABYTE) == null) {
            ImageView imageView = alert.findViewById(R.id.image);
            imageView.setImageResource(R.drawable.oneuser);
        } else {
            mImageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    DisplayMetrics dm = new DisplayMetrics();

                       /* imageView.setMinimumHeight(dm.heightPixels);
                        imageView.setMinimumWidth(dm.widthPixels);*/
                    imageView2.setImageBitmap(bm);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    imageView2.setImageResource(R.drawable.oneuser);
                }
            });
        }

        sname.setText(name);
        if (phone.isEmpty()) {
            sphone.setText("No Phone #");
        } else {
            phone = "(" + phone.substring(0, 3) + ") - " + phone.substring(3, 6) + " - " + phone.substring(6, 10);
            sphone.setText("Phone: " + phone);

        }

        if (email.isEmpty()) {
            semail.setText("No Email Address");
        } else {
            semail.setText("Email: " + email);
        }

        srelation.setText("Relation: " + relation);

        Button cancel = alert.findViewById(R.id.cancel_action);

        moreinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                longToast(description);
            }
        });

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PendingRequests.class));
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
                databaseReference4.removeValue();
            }
        });
        alert.show();

    }

    private long lastTouchDown;
    private static int CLICK_ACTION_THRESHHOLD = 200;

    private void recoverData() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Create Account");

        HashMap<String, String> values = new HashMap<>();
        values.put("Approved", "Oct 7, 2018 9:47:55 PM");
        values.put("Created", "Oct 7, 2018 2:56:45 PM");
        values.put("Description", "Imma yo cuzin from willy do hilly whos house. ");
        values.put("Email", "@hdbjdbd.com");
        values.put("Name", "Subject 8");
        values.put("Password", "jkjkjkj");
        values.put("Phone", "7328558777");
        values.put("Relation", "Family");
        values.put("Username", "billywilly");

        databaseReference.push().setValue(values);

        HashMap<String, String> values2 = new HashMap<>();
        values2.put("Approved", "Oct 7, 2018 10:02:39 PM");
        values2.put("Created", "Oct 7, 2018 5:14:29 PM");
        values2.put("Description", "Nitpick. Gvgvhvh h. J jv J jb");
        values2.put("Email", "j@jdmx.com");
        values2.put("Name", "Hello Hi");
        values2.put("Password", "hdhdhd");
        values2.put("Phone", "");
        values2.put("Relation", "Friend"
        );
        values2.put("Username", "hidhdndj");

        databaseReference.push().setValue(values2);

        HashMap<String, String> values3 = new HashMap<>();
        values3.put("Approved", "Oct 14, 2018 8:42:20 PM");
        values3.put("Created", "Oct 14, 2018 8:25:15 PM");
        values3.put("Description", "You use me to drink water. I am green and purple. ");
        values3.put("Email", "");
        values3.put("Name", "Water Cup");
        values3.put("Password", "waterr");
        values3.put("Phone", "5454548487");
        values3.put("Relation", "Other");
        values3.put("Username", "helllf");
        databaseReference.push().setValue(values3);

        HashMap<String, String> values4 = new HashMap<>();
        values4.put("Approved", "Oct 21, 2018 9:00:24 PM");
        values4.put("Created", "Oct 21, 2018 8:45:46 PM");
        values4.put("Description", "I am here for business.  His. ");
        values4.put("Email", "mvkvj@jjmd.com");
        values4.put("Name", "jdjdnfjf Fnkfk");
        values4.put("Password", "hhhhhh");
        values4.put("Phone", "5767667676");
        values4.put("Relation", "Business");
        values4.put("Username", "jdjfjfi");
        databaseReference.push().setValue(values4);

        HashMap<String, String> values5 = new HashMap<>();
        values5.put("Approved", "Oct 21, 2018 9:20:25 PM");
        values5.put("Created", "Oct 21, 2018 9:19:19 PM");
        values5.put("Description", "Imma yo friend from though Schopenhauer. N");
        values5.put("Email", "");
        values5.put("Name", "Newbue Hekk");
        values5.put("Password", "hfjjjj");
        values5.put("Phone", "5787875566");
        values5.put("Relation", "Friend");
        values5.put("Username", "fhfjfj");
        databaseReference.push().setValue(values5);

        HashMap<String, String> values6 = new HashMap<>();
        values6.put("Name", "Caretaker Ishaan Javali");
        values6.put("Password", "caretaker");
        values6.put("Phone", "(848) - 248 - 2353");
        values6.put("Relation", "I have been with the resident for 10 years now,...");
        values6.put("Username", "ishaanjav");

        databaseReference.push().setValue(values6);


    }

    private void listeners() {
        TextView settingspage = findViewById(R.id.settingspage);
        TextView aboutpage = findViewById(R.id.aboutapppage);
        TextView logpage = findViewById(R.id.logpage);
        TextView existingpage = findViewById(R.id.existingpage);
        TextView pendingpage = findViewById(R.id.pendingpage);
        TextView gpspage = findViewById(R.id.gpspage);
        TextView emotionspage = findViewById(R.id.emotionspage);


        TextView settingsdescription = findViewById(R.id.settingsdescription);
        TextView aboutdescription = findViewById(R.id.aboutdescription);
        TextView logdescription = findViewById(R.id.logdescription);
        TextView existingdescription = findViewById(R.id.existingdescription);
        TextView pendingdescription = findViewById(R.id.pendingdescription);
        TextView gpsdecription = findViewById(R.id.gpsdescription);
        TextView emotiondescription = findViewById(R.id.emotionssdescription);


        settingspage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastTouchDown = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (System.currentTimeMillis() - lastTouchDown < CLICK_ACTION_THRESHHOLD) {
                            startActivity(new Intent(CaretakerAccount.this, RealSettings.class));
                        }
                        break;
                }
                return true;
                //recoverData();

            }
        });
        aboutpage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastTouchDown = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (System.currentTimeMillis() - lastTouchDown < CLICK_ACTION_THRESHHOLD) {
                            startActivity(new Intent(CaretakerAccount.this, AboutApp.class));
                        }
                        break;
                }
                return true;
                //recoverData();

            }
        });
        logpage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastTouchDown = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (System.currentTimeMillis() - lastTouchDown < CLICK_ACTION_THRESHHOLD) {
                            startActivity(new Intent(CaretakerAccount.this, EventsLog.class));
                        }
                        break;
                }
                return true;
                //recoverData();

            }
        });
        existingpage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastTouchDown = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (System.currentTimeMillis() - lastTouchDown < CLICK_ACTION_THRESHHOLD) {
                            startActivity(new Intent(CaretakerAccount.this, ExistingUsers.class));
                        }
                        break;
                }
                return true;
                //recoverData();


            }
        });
        pendingpage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastTouchDown = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (System.currentTimeMillis() - lastTouchDown < CLICK_ACTION_THRESHHOLD) {
                            startActivity(new Intent(CaretakerAccount.this, PendingRequests.class));
                        }
                        break;
                }
                return true;
                //recoverData();

            }
        });
        settingsdescription.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastTouchDown = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (System.currentTimeMillis() - lastTouchDown < CLICK_ACTION_THRESHHOLD) {
                            startActivity(new Intent(CaretakerAccount.this, RealSettings.class));
                        }
                        break;
                }
                return true;
                //recoverData();

            }
        });
        aboutdescription.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastTouchDown = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (System.currentTimeMillis() - lastTouchDown < CLICK_ACTION_THRESHHOLD) {
                            startActivity(new Intent(CaretakerAccount.this, AboutApp.class));
                        }
                        break;
                }
                return true;
                //recoverData();

            }
        });
        logdescription.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastTouchDown = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (System.currentTimeMillis() - lastTouchDown < CLICK_ACTION_THRESHHOLD) {
                            startActivity(new Intent(CaretakerAccount.this, EventsLog.class));
                        }
                        break;
                }
                return true;
                //recoverData();

            }
        });
        existingdescription.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastTouchDown = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (System.currentTimeMillis() - lastTouchDown < CLICK_ACTION_THRESHHOLD) {
                            startActivity(new Intent(CaretakerAccount.this, ExistingUsers.class));
                        }
                        break;
                }
                return true;
                //recoverData();

            }
        });
        pendingdescription.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastTouchDown = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (System.currentTimeMillis() - lastTouchDown < CLICK_ACTION_THRESHHOLD) {
                            startActivity(new Intent(CaretakerAccount.this, PendingRequests.class));
                        }
                        break;
                }
                return true;
                //recoverData();

            }
        });

        gpspage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastTouchDown = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (System.currentTimeMillis() - lastTouchDown < CLICK_ACTION_THRESHHOLD) {
                            startActivity(new Intent(CaretakerAccount.this, GPS.class));
                        }
                        break;
                }
                return true;
                //recoverData();

            }
        });
        emotionspage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastTouchDown = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (System.currentTimeMillis() - lastTouchDown < CLICK_ACTION_THRESHHOLD) {
                            startActivity(new Intent(CaretakerAccount.this, EmotionsAnalyzer.class));
                        }
                        break;
                }
                return true;
                //recoverData();

            }
        });
        emotiondescription.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastTouchDown = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (System.currentTimeMillis() - lastTouchDown < CLICK_ACTION_THRESHHOLD) {
                            startActivity(new Intent(CaretakerAccount.this, EmotionsAnalyzer.class));
                        }
                        break;
                }
                return true;
                //recoverData();

            }
        });
        gpsdecription.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastTouchDown = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (System.currentTimeMillis() - lastTouchDown < CLICK_ACTION_THRESHHOLD) {
                            startActivity(new Intent(CaretakerAccount.this, GPS.class));
                        }
                        break;
                }
                return true;
                //recoverData();

            }
        });

    }

    private void circle() {
        //getinflated.removeAllViews();
        latestpendingapproval();

    }

    private void latestpendingapproval() {
        final ArrayList<PendingAccounts> pendingAccounts = new ArrayList<>();
        pendingAccounts.clear();
        getinflated.setVisibility(View.VISIBLE);
        Animation nim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slowfadein);
        getinflated.startAnimation(nim);

        final FirebaseDatabase database2 = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database2.getReference("Pending Requests");
        final View inflatedLayout = getLayoutInflater().inflate(R.layout.latestpendingapproval, null, false);
        //  getinflated.removeAllViews();
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //        getinflated.removeAllViews();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String sname = child.child("Name").getValue(String.class);
                    String spassword = child.child("Password").getValue(String.class);
                    String susername = child.child("Username").getValue(String.class);
                    String sphone = child.child("Phone").getValue(String.class);
                    String semail = child.child("Email").getValue(String.class);
                    String sdescription = child.child("Description").getValue(String.class);
                    String srelation = child.child("Relation").getValue(String.class);
                    String stime = child.child("Time").getValue(String.class);
                    pendingAccounts.add(new PendingAccounts(sname, susername, spassword, sdescription, sphone, semail, srelation, stime, "UNKNWON"));
                }
                //   getinflated.addView(inflatedLayout);

                if (pendingAccounts.isEmpty()) {

                } else {
                    TextView name = /*(TextView) inflatedLayout.*/findViewById(R.id.name);
                    TextView time = /*(TextView) inflatedLayout.*/findViewById(R.id.time);
                    TextView contact = /*(TextView) inflatedLayout.*/findViewById(R.id.contact);
                    TextView relation = /*(TextView) inflatedLayout.*/findViewById(R.id.relation);
                    TextView title = findViewById(R.id.title2);
                    final Button description =/* (Button) inflatedLayout.*/findViewById(R.id.button);

               /* TextView name = (TextView) inflatedLayout.findViewById(R.id.name);
                TextView time = (TextView) inflatedLayout.findViewById(R.id.time);
                TextView contact = (TextView) inflatedLayout.findViewById(R.id.contact);
                TextView relation = (TextView) inflatedLayout.findViewById(R.id.relation);
                Button description = (Button) inflatedLayout.findViewById(R.id.button);*/
                    name.setText("");
                    contact.setText("");
                    title.setText("Latest Account Pending Approval");
                    relation.setText("");
                    time.setText("");

                    name.setText("Name: " + "" + pendingAccounts.get(pendingAccounts.size() - 1).getName());
                    time.setText("Requested on: " + "" + pendingAccounts.get(pendingAccounts.size() - 1).getTime());
                    relation.setText("Relation: " + pendingAccounts.get(pendingAccounts.size() - 1).getRelations());
                    String phone = pendingAccounts.get(pendingAccounts.size() - 1).getPhone_number();
                    if (phone.isEmpty()) {
                        contact.setText("Email: " + pendingAccounts.get(pendingAccounts.size() - 1).getEmail());
                    } else {
                        phone = "(" + phone.substring(0, 3) + ")-" + phone.substring(3, 6) + "-" + phone.substring(6, 10);
                        contact.setText("Phone: " + phone);
                    }

                    description.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            makeToast(pendingAccounts.get(pendingAccounts.size() - 1).getDescription());
                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        databaseReference.addValueEventListener(valueEventListener);

        Runnable r = new Runnable() {
            @Override
            public void run() {
                Animation nim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slowfadeout);
                getinflated.startAnimation(nim);

                Runnable d = new Runnable() {
                    @Override
                    public void run() {
                        getinflated.setVisibility(View.INVISIBLE);
                        inflatedLayout.setVisibility(View.INVISIBLE);
                        //   getinflated.removeAllViews();
                        latestlogin();
                    }
                };
                Handler ht = new Handler();
                ht.postDelayed(d, 3000);


            }
        };
        Handler h = new Handler();
        h.postDelayed(r, 6000);


    }

    private void latestlogin() {
        //  getinflated.removeAllViews();
        final FirebaseDatabase database2 = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database2.getReference("Login/Visitor Login");
        final View inflatedLayout = getLayoutInflater().inflate(R.layout.latestlogin, null, false);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> name = new ArrayList<>();
                ArrayList<String> password = new ArrayList<>();
                ArrayList<String> username = new ArrayList<>();
                ArrayList<String> time = new ArrayList<>();
                ArrayList<String> device = new ArrayList<>();
                phone = new ArrayList<>();
                ArrayList<String> email = new ArrayList<>();
                TextView title = findViewById(R.id.title2);

                ArrayList<String> relation = new ArrayList<>();
                final ArrayList<String> description = new ArrayList<>();


                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String sname = child.child("Name").getValue(String.class);
                    if (sname == null) {
                        sname = child.child("name").getValue(String.class);
                    }
                    String spassword = child.child("Password").getValue(String.class);
                    String susername = child.child("Username").getValue(String.class);
                    String sdevice = child.child("Device").getValue(String.class);
                    String stime = child.child("Time").getValue(String.class);
                    String sphone = child.child("Phone").getValue(String.class);
                    String semail = child.child("Email").getValue(String.class);
                    String sdescription = child.child("Description").getValue(String.class);
                    String srelation = child.child("Relation").getValue(String.class);

                    if (sname.contains("Caretaker")) {

                    } else {
                        name.add(sname);
                        password.add(spassword);
                        time.add(stime);
                        device.add(sdevice);
                        phone.add(sphone);
                        email.add(semail);
                        username.add(susername);
                        description.add(sdescription);
                        relation.add(srelation);
                    }
                }
                getinflated.setVisibility(View.VISIBLE);
                Animation nim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slowfadein);
                getinflated.startAnimation(nim);
                //    getinflated.addView(inflatedLayout);
                if (phone.isEmpty()) {

                } else {

                    TextView personname = /*(TextView) inflatedLayout.*/findViewById(R.id.name);
                    TextView persontime = /*(TextView) inflatedLayout.*/findViewById(R.id.time);
                    TextView contact = /*(TextView) inflatedLayout.*/findViewById(R.id.contact);
                    TextView relationperson = /*(TextView) inflatedLayout.*/findViewById(R.id.relation);
                    final Button descriptionpress =/* (Button) inflatedLayout.*/findViewById(R.id.button);
                    personname.setText("");
                    contact.setText("");
                    relationperson.setText("");
                    title.setText("Latest Login");
                    persontime.setText("");

                    personname.setText("Name: " + "" + name.get(name.size() - 1));
                    persontime.setText("Logged In: " + "" + time.get(time.size() - 1));
                    relationperson.setText("Relation: " + relation.get(relation.size() - 1));
                    String personphone = phone.get(phone.size() - 1);
                    if (personphone.isEmpty()) {
                        if (email.isEmpty()) {

                        } else {
                            contact.setText("Email: " + email.get(email.size() - 1));
                        }
                    } else {
                        personphone = "(" + personphone.substring(0, 3) + ")-" + personphone.substring(3, 6) + "-" + personphone.substring(6, 10);
                        contact.setText("Phone: " + personphone);
                    }

                    descriptionpress.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            makeToast(description.get(description.size() - 1));
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        databaseReference.addValueEventListener(valueEventListener);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Animation nim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slowfadeout);
                getinflated.startAnimation(nim);

                Runnable d = new Runnable() {
                    @Override
                    public void run() {
                        getinflated.setVisibility(View.INVISIBLE);
                        inflatedLayout.setVisibility(View.INVISIBLE);
                        //   getinflated.removeAllViews();
                        secondlatestlogin();
                    }
                };
                Handler ht = new Handler();
                ht.postDelayed(d, 3000);


            }
        };
        Handler h = new Handler();
        h.postDelayed(r, 6000);

    }

    private void secondlatestlogin() {
        final FirebaseDatabase database2 = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database2.getReference("Login/Visitor Login");
        final View inflatedLayout = getLayoutInflater().inflate(R.layout.latestlogin, null, false);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> name = new ArrayList<>();
                ArrayList<String> password = new ArrayList<>();
                ArrayList<String> username = new ArrayList<>();
                ArrayList<String> time = new ArrayList<>();
                ArrayList<String> device = new ArrayList<>();
                ArrayList<String> phone = new ArrayList<>();
                ArrayList<String> email = new ArrayList<>();
                TextView title = findViewById(R.id.title2);

                ArrayList<String> relation = new ArrayList<>();
                final ArrayList<String> description = new ArrayList<>();


                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String sname = child.child("Name").getValue(String.class);
                    if (sname == null) {
                        sname = child.child("name").getValue(String.class);
                    }
                    String spassword = child.child("Password").getValue(String.class);
                    String susername = child.child("Username").getValue(String.class);
                    String sdevice = child.child("Device").getValue(String.class);
                    String stime = child.child("Time").getValue(String.class);
                    String sphone = child.child("Phone").getValue(String.class);
                    String semail = child.child("Email").getValue(String.class);
                    String sdescription = child.child("Description").getValue(String.class);
                    String srelation = child.child("Relation").getValue(String.class);


                    if (sname.contains("Caretaker")) {

                    } else {
                        name.add(sname);
                        password.add(spassword);
                        time.add(stime);
                        device.add(sdevice);
                        phone.add(sphone);
                        email.add(semail);
                        username.add(susername);
                        description.add(sdescription);
                        relation.add(srelation);
                    }

                }
                getinflated.setVisibility(View.VISIBLE);
                Animation nim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slowfadein);
                getinflated.startAnimation(nim);
                //    getinflated.addView(inflatedLayout);
                if (name.isEmpty()) {
                    title.setText("No Logins");
                } else {
                    if (name.size() > 1) {
                        TextView personname = /*(TextView) inflatedLayout.*/findViewById(R.id.name);
                        TextView persontime = /*(TextView) inflatedLayout.*/findViewById(R.id.time);
                        TextView contact = /*(TextView) inflatedLayout.*/findViewById(R.id.contact);
                        TextView relationperson = /*(TextView) inflatedLayout.*/findViewById(R.id.relation);
                        final Button descriptionpress =/* (Button) inflatedLayout.*/findViewById(R.id.button);
                        personname.setText("");
                        contact.setText("");
                        relationperson.setText("");
                        title.setText("Second Latest Login");
                        persontime.setText("");

                        personname.setText("Name: " + "" + name.get(name.size() - 2));
                        persontime.setText("Logged In: " + "" + time.get(time.size() - 2));
                        relationperson.setText("Relation: " + relation.get(relation.size() - 2));
                        String personphone = phone.get(phone.size() - 2);

                        if (personphone == null || personphone.length() < 10) {
                            contact.setText("Email: " + email.get(email.size() - 2));
                        } else {
                            personphone = "(" + personphone.substring(0, 3) + ")-" + personphone.substring(3, 6) + "-" + personphone.substring(6, 10);
                            contact.setText("Phone: " + personphone);
                        }

                        descriptionpress.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                makeToast(description.get(description.size() - 1));
                            }
                        });


                    } else {
                        title.setText("Only 1 Login.");

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        databaseReference.addValueEventListener(valueEventListener);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Animation nim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slowfadeout);
                getinflated.startAnimation(nim);

                Runnable d = new Runnable() {
                    @Override
                    public void run() {
                        getinflated.setVisibility(View.INVISIBLE);
                        inflatedLayout.setVisibility(View.INVISIBLE);
                        //   getinflated.removeAllViews();
                        latestcreation();
                    }
                };
                Handler ht = new Handler();
                ht.postDelayed(d, 3000);


            }
        };
        Handler h = new Handler();
        h.postDelayed(r, 6000);

    }

    private void latestcreation() {
        final FirebaseDatabase database2 = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database2.getReference("Create Account");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> name = new ArrayList<>();
                ArrayList<String> password = new ArrayList<>();
                ArrayList<String> username = new ArrayList<>();
                ArrayList<String> time = new ArrayList<>();
                ArrayList<String> device = new ArrayList<>();
                ArrayList<String> phone = new ArrayList<>();
                ArrayList<String> email = new ArrayList<>();
                TextView title = findViewById(R.id.title2);
                title.setText("Latest Account Approved");

                ArrayList<String> relation = new ArrayList<>();
                final ArrayList<String> description = new ArrayList<>();


                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String sname = child.child("Name").getValue(String.class);
                    String spassword = child.child("Password").getValue(String.class);
                    String susername = child.child("Username").getValue(String.class);
                    String sdevice = child.child("device").getValue(String.class);
                    String stime = child.child("Approved").getValue(String.class);
                    String sphone = child.child("Phone").getValue(String.class);
                    String semail = child.child("Email").getValue(String.class);
                    String sdescription = child.child("Description").getValue(String.class);
                    String srelation = child.child("Relation").getValue(String.class);

                    if (sname.contains("Caretaker")) {
                        continue;
                    } else {
                        name.add(sname);
                        password.add(spassword);
                        time.add(stime);
                        device.add(sdevice);
                        phone.add(sphone);
                        email.add(semail);
                        username.add(susername);
                        description.add(sdescription);
                        relation.add(srelation);
                    }

                }
                getinflated.setVisibility(View.VISIBLE);
                Animation nim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slowfadein);
                getinflated.startAnimation(nim);

                if (name.isEmpty()) {

                } else {

                    TextView personname = /*(TextView) inflatedLayout.*/findViewById(R.id.name);
                    TextView persontime = /*(TextView) inflatedLayout.*/findViewById(R.id.time);
                    TextView contact = /*(TextView) inflatedLayout.*/findViewById(R.id.contact);
                    TextView relationperson = /*(TextView) inflatedLayout.*/findViewById(R.id.relation);
                    final Button descriptionpress =/* (Button) inflatedLayout.*/findViewById(R.id.button);
                    personname.setText("");
                    contact.setText("");
                    relationperson.setText("");
                    title.setText("Latest Account Creation");
                    persontime.setText("");

                    personname.setText("Name: " + "" + name.get(name.size() - 1));
                    persontime.setText("Approved on: " + "" + time.get(time.size() - 1));
                    relationperson.setText("Relation: " + relation.get(relation.size() - 1));
                    String personphone = phone.get(phone.size() - 1);
                    if (personphone.isEmpty()) {
                        contact.setText("Email: " + email.get(email.size() - 1));
                    } else {
                        personphone = "(" + personphone.substring(0, 3) + ")-" + personphone.substring(3, 6) + "-" + personphone.substring(6, 10);
                        contact.setText("Phone: " + personphone);
                    }

                    descriptionpress.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            makeToast(description.get(description.size() - 1));
                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };


        databaseReference.addValueEventListener(valueEventListener);

        Runnable r = new Runnable() {
            @Override
            public void run() {
                Animation nim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slowfadeout);
                getinflated.startAnimation(nim);

                Runnable d = new Runnable() {
                    @Override
                    public void run() {
                        getinflated.setVisibility(View.INVISIBLE);
                        //   getinflated.removeAllViews();
                        circle();
                    }
                };
                Handler ht = new Handler();
                ht.postDelayed(d, 3000);


            }
        };
        Handler h = new Handler();
        h.postDelayed(r, 6000);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.caretaker_account, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.signoutmenu) {
            //Crashlytics.getInstance().crash();
            Crashlytics.setString("Caretaker Device", "Logging Out");

            makeToast("Signing out...");
            FirebaseDatabase database1 = FirebaseDatabase.getInstance();
            DatabaseReference dbref1 = database1.getReference("Logged In");

            Map<String, String> state = new HashMap<>();
            state.put("State", "false");
            dbref1.push().setValue(state, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(CaretakerAccount.this, MainActivity.class));
                        }
                    };

                    Handler h = new Handler();
                    h.postDelayed(r, 1700);
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }

    /*public class Messaging extends FirebaseMessagingService {
        @Override
        public void onMessageReceived(RemoteMessage remoteMessage) {

            makeToast(remoteMessage.getData().toString());
        }


    }*/

    private void makeToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private void longToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
//        getinflated.removeAllViews();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_events) {
            startActivity(new Intent(CaretakerAccount.this, EventsLog.class));
        } else if (id == R.id.nav_aboutapp) {
            startActivity(new Intent(CaretakerAccount.this, AboutApp.class));

        } else if (id == R.id.nav_requests) {
            startActivity(new Intent(CaretakerAccount.this, PendingRequests.class));

        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(CaretakerAccount.this, RealSettings.class));

        } else if (id == R.id.nav_gps) {
            String token = FirebaseInstanceId.getInstance().getId();
            TextView edt = findViewById(R.id.existingpage);
            startActivity(new Intent(CaretakerAccount.this, GPS.class));
           /* edt.setText(token);
            makeToast(token);*/
        } else if (id == R.id.nav_users) {
            startActivity(new Intent(getApplicationContext(), ExistingUsers.class));
        } else if (id == R.id.emotions) {
            startActivity(new Intent(getApplicationContext(), EmotionsAnalyzer.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
