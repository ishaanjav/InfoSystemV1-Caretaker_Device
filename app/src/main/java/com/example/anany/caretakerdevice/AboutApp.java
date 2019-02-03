package com.example.anany.caretakerdevice;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AboutApp extends AppCompatActivity {

    CardView cardView, card;
    TextView thanks;
    int times = 1;
    DatabaseReference databaseReference6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        cardView = findViewById(R.id.cardView);
        card = findViewById(R.id.card);
        thanks = findViewById(R.id.thanks);
        // cardView.setBackgroundColor(getResources().getColor(R.color.green));
        card.setBackgroundColor(getResources().getColor(R.color.green));
        thanks.setVisibility(View.INVISIBLE);
        notifyForChange();

        Runnable r = new Runnable() {
            @Override
            public void run() {
                thanks.setVisibility(View.VISIBLE);
                Animation nim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slidefrombottom);

                thanks.setAnimation(nim);
                disappear();
            }
        };
        Handler h = new Handler();
        h.postDelayed(r, 1300);

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

    private void disappear() {
        Runnable d = new Runnable() {
            @Override
            public void run() {
                Animation nim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slidetobottom);
                thanks.setAnimation(nim);

                thanks.setVisibility(View.INVISIBLE);
            }
        };

        Handler ht = new Handler();
        ht.postDelayed(d, 4000);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.aboutappmenu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.backbutton) {

            startActivity(new Intent(AboutApp.this, CaretakerAccount.class));
        }

        return super.onOptionsItemSelected(item);
    }

}
