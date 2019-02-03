/*
package com.example.anany.caretakerdevice;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

import static android.support.v4.content.ContextCompat.getSystemService;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.i("MESSAGE RECEIVED:", "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("MESSAGE: ", "Message data payload: " + remoteMessage.getData());
            */
/* if (*//*
*/
/* Check if data needs to be processed by long running job *//*
*/
/* true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }*//*

        }
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("Notification: ", "Message Notification Body: " + remoteMessage.getNotification().getBody());
            Looper.prepare();
            Toast.makeText(getApplicationContext(), "VISITOR SIGNED IN!", Toast.LENGTH_LONG).show();
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference("Latest Login");
            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("Time", currentDateTimeString);

            databaseReference.push().setValue(hashMap).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i("IT WORKEEDDDD", "IT WORKKKEEDDDD");

                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.i("ERRORRRRR", "EROORORORO");
                }
            });


        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }


}
*/
