package com.example.anany.caretakerdevice;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class EmotionsAnalyzer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Bitmap image;
    ImageView hiddenImage;
    TextView hiddenText, hiddenText2;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotions_analyzer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        hiddenText = findViewById(R.id.hiddenText);
        hiddenText2 = findViewById(R.id.hiddenText2);
        hiddenImage = findViewById(R.id.hiddenimage);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        hiddenImage.setVisibility(View.INVISIBLE);
        hiddenText.setVisibility(View.INVISIBLE);
        hiddenText2.setVisibility(View.INVISIBLE);
        readValues();
        //  startActivity(new Intent(getApplicationContext(), TakePicture.class));

        //IMPORTANT: Read from Emotions path in Firebase to get the picture, date and time, and the 3 strongest emotions.
        //Display this information in a listview.
        //OPTIONAL: Maybe have a filter where they can query for an emotion and returns rows where that emotion was strongest.
    }

    private void readValues() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Emotions");
        listView = findViewById(R.id.listView);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<EmotionsHolder> emotionsHolders = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String time = child.child("Time").getValue().toString();
                    String stringsize = child.child("Size").getValue().toString();
                    ArrayList<String> tempArray = new ArrayList<>();
                    int size = Integer.parseInt(stringsize);
                    for (int i = 0; i < size; i++) {
                        String tempemotion = child.child("Emotion" + i).getValue().toString();
                        tempArray.add(tempemotion);
                    }
                    emotionsHolders.add(new EmotionsHolder(time, size, tempArray));
                }
                if (emotionsHolders.isEmpty()) {
                    makeToast("Whoops! Looks like you don't have any emotions analyzed yet.");
                    hiddenImage.setVisibility(View.VISIBLE);
                    hiddenText.setVisibility(View.VISIBLE);
                    hiddenText2.setVisibility(View.VISIBLE);
                } else {
                    ArrayList<EmotionsHolder> backwards = new ArrayList<>();
                    for (int i = emotionsHolders.size() - 1; i >= 0; i--) {
                        backwards.add(emotionsHolders.get(i));
                    }
                    //    makeToast(emotionsHolders.size() +"");
                    ArrayAdapter arrayAdapter = new EmotionsAdapter(EmotionsAnalyzer.this, getApplicationContext(), backwards);
                    listView.setVisibility(View.VISIBLE);
                    //     makeToast(emotionsHolders.get(0).getArrayList().toString());
                    listView.setAdapter(arrayAdapter);

                    hiddenImage.setVisibility(View.INVISIBLE);
                    hiddenText.setVisibility(View.INVISIBLE);
                    hiddenText2.setVisibility(View.INVISIBLE);
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
        getMenuInflater().inflate(R.menu.emotions_analyzer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.chooseimage) {
            //Make Alert Dialog where they choose between gallery picture or camera. Based on what they choose take it in an intent
            //and pass it to next activity where corresponding thing is automatically launched. After image selected, display the emotions
            //(ALL EMOTIONS) underneath the picture and store it in Firebase along with the date and time.
            makeToast("(Taking the picture from the camera is recommended.)");
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setCancelable(true);
            alertDialogBuilder.setMessage("How do you want to get the picture?");
            alertDialogBuilder.setPositiveButton("Launch the Camera",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            if (ContextCompat.checkSelfPermission(EmotionsAnalyzer.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(EmotionsAnalyzer.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                            } else {
                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(cameraIntent, 1);
                            }
                        }
                    });
            alertDialogBuilder.setNegativeButton("Launch Photo Gallery",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(galleryIntent, 100);
                        }
                    });


            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else if (id == R.id.graph) {
            //Launch activity where it displays graph.
            Intent graphIntent = new Intent(getApplicationContext(), TakePicture.class);
            graphIntent.putExtra("Extra", "Graph");
            startActivity(graphIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            image = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            Intent in1 = new Intent(getApplicationContext(), TakePicture.class);
            in1.putExtra("image", byteArray);
            in1.putExtra("Extra", "n");
            startActivity(in1);
        } else if (requestCode == 100 && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            try {
                image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                makeToast("Got image");
                image = getResizedBitmap(image, 600);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                Intent in1 = new Intent(getApplicationContext(), TakePicture.class);
                in1.putExtra("image", byteArray);
                in1.putExtra("Extra", "Yes");
                startActivity(in1);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(getApplicationContext(), CaretakerAccount.class));
        } else if (id == R.id.nav_events) {
            startActivity(new Intent(getApplicationContext(), EventsLog.class));
        } else if (id == R.id.nav_aboutapp) {
            startActivity(new Intent(getApplicationContext(), AboutApp.class));

        } else if (id == R.id.nav_requests) {
            startActivity(new Intent(getApplicationContext(), PendingRequests.class));
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(getApplicationContext(), RealSettings.class));

        } else if (id == R.id.nav_gps) {
            startActivity(new Intent(this, GPS.class));
        } else if (id == R.id.nav_users) {
            startActivity(new Intent(getApplicationContext(), ExistingUsers.class));
        } else if (id == R.id.emotions) {
            makeToast("Already in the Emotions Analyzer page.");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void makeToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }
}
