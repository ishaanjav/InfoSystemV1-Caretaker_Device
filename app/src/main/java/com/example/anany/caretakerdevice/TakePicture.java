package com.example.anany.caretakerdevice;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/*import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;*/
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.face.contract.FaceRectangle;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TakePicture extends AppCompatActivity {

    Bitmap image;
    ImageView imageView;
    TextView emotion1, emotion2, emotion3, emotion4, emotion5, age, title, frequencytitle;

    FaceServiceClient faceServiceClient;
    TreeMap<Integer, String> rank;
    String extra = "";
    ArrayList<Double> arrayList;

    TextView name1, name2, name3, name4, name5, name6, name7, fre1, fre2, fre3, fre4, fre5, fre6, fre7;

    HashMap<String, Integer> hashMap = new HashMap<>();


    Face[] people;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture);
        initializeViews();

        frequencytitle = findViewById(R.id.frequency);
        faceServiceClient = new FaceServiceRestClient("<YOUR ENDPOINT HERE>", "<YOUR API KEY HERE>");

        Intent receiveIntent = getIntent();
        extra = receiveIntent.getStringExtra("Extra");
        if (extra.equals("Yes")) {
            frequencytitle.setVisibility(View.INVISIBLE);
            byte[] byteArray = receiveIntent.getByteArrayExtra("image");
            image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            invisible();
            imageView.setRotation(270);
            imageView.setImageBitmap(image);
            detectandFrame(image);
        } else if (extra.equals("Graph")) {
            title = findViewById(R.id.title);
            title.setVisibility(View.INVISIBLE);
            visible();
            frequencytitle.setVisibility(View.VISIBLE);
            readData();
        } else {
            byte[] byteArray = receiveIntent.getByteArrayExtra("image");
            image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            imageView.setImageBitmap(image);
            frequencytitle.setVisibility(View.INVISIBLE);
            invisible();
            detectandFrame(image);
        }


    }

    private void readData() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Emotions");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hashMap.put("Happiness", 0);
                hashMap.put("Anger", 0);
                hashMap.put("Disgust", 0);
                hashMap.put("Sadness", 0);
                hashMap.put("Fear", 0);
                hashMap.put("Neutral", 0);
                hashMap.put("Surprise", 0);

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String emotion0 = child.child("Emotion0").getValue().toString();
                    String emotion = emotion0.split(" ")[0];
                    if (emotion.equals("Happiness")) {
                        hashMap.put("Happiness", hashMap.get("Happiness") + 1);
                    } else if (emotion.equals("Anger")) {
                        hashMap.put("Anger", hashMap.get("Anger") + 1);
                    } else if (emotion.equals("Disgust")) {
                        hashMap.put("Disgust", hashMap.get("Disgust") + 1);
                    } else if (emotion.equals("Sadness")) {
                        hashMap.put("Sadness", hashMap.get("Sadness") + 1);
                    } else if (emotion.equals("Fear")) {
                        hashMap.put("Fear", hashMap.get("Fear") + 1);
                    } else if (emotion.equals("Neutral")) {
                        hashMap.put("Neutral", hashMap.get("Neutral") + 1);
                    } else if (emotion.equals("Surprise")) {
                        hashMap.put("Surprise", hashMap.get("Surprise") + 1);
                    }
                }
                createGraph(hashMap.get("Happiness"), hashMap.get("Anger"), hashMap.get("Disgust"), hashMap.get("Sadness"), hashMap.get("Fear"), hashMap.get("Neutral"), hashMap.get("Surprise"));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        databaseReference.addValueEventListener(valueEventListener);

    }

    private void createGraph(int happy, int anger, int disgust, int sad, int fear, int neutral, int surprise) {
        GraphView graph = findViewById(R.id.graph);

        HashMap<String, Integer> values = new HashMap<>();
        values.put("Happiness", happy);
        values.put("Anger", anger);
        values.put("Disgust", disgust);
        values.put("Sadness", sad);
        values.put("Fear", fear);
        values.put("Neutral", neutral);
        values.put("Surprise", surprise);

        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[]{
                new DataPoint(1, happy),
                new DataPoint(2, neutral),
                new DataPoint(3, surprise),
                new DataPoint(4, sad),
                new DataPoint(5, anger),
                new DataPoint(6, fear),
                new DataPoint(7, disgust),
                new DataPoint(8, 0)
        });
        graph.addSeries(series);

// styling
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX() * 255 / 4, (int) Math.abs(data.getY() * 255 / 6), 100);
            }
        });

        ValueDependentColor<DataPoint> valueDependentColor;
        valueDependentColor = new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                if (data.getX() == 1) {
                    return getResources().getColor(R.color.happy);
                } else if (data.getX() == 2) {
                    return getResources().getColor(R.color.neutral);
                } else if (data.getX() == 3) {
                    return getResources().getColor(R.color.surprise);
                } else if (data.getX() == 4) {
                    return getResources().getColor(R.color.sad);
                } else if (data.getX() == 5) {
                    return getResources().getColor(R.color.angry);
                } else if (data.getX() == 6) {
                    return getResources().getColor(R.color.fear);
                } else if (data.getX() == 7) {
                    return getResources().getColor(R.color.disgust);
                }
                return 0;
            }
        };
        series.setSpacing(0);

// draw values on top
        series.setDrawValuesOnTop(false);
        series.setValueDependentColor(valueDependentColor);
        series.setValuesOnTopColor(Color.TRANSPARENT);
        displayLegend(values);
    }

    private void displayLegend(HashMap<String, Integer> valuesorig) {
        TreeMap<String, Integer> values = sortMapByValue(valuesorig);
        ArrayList<Integer> frequencies = new ArrayList<>();
        ArrayList<String> emotions = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : values.entrySet()) {
            frequencies.add(entry.getValue());
            emotions.add(entry.getKey());
        }
        //  name1.setText(values.toString());
        name1.setText(emotions.get(0) + " -");
        name2.setText(emotions.get(1) + " -");
        name3.setText(emotions.get(2) + " -");
        name4.setText(emotions.get(3) + " -");
        name5.setText(emotions.get(4) + " -");
        name6.setText(emotions.get(5) + " -");
        name7.setText(emotions.get(6) + " -");

        fre1.setText(frequencies.get(0) + " time(s)");
        fre2.setText(frequencies.get(1) + " time(s)");
        fre3.setText(frequencies.get(2) + " time(s)");
        fre4.setText(frequencies.get(3) + " time(s)");
        fre5.setText(frequencies.get(4) + " time(s)");
        fre6.setText(frequencies.get(5) + " time(s)");
        fre7.setText(frequencies.get(6) + " time(s)");

        setColor(name1, fre1);
        setColor(name2, fre2);
        setColor(name3, fre3);
        setColor(name4, fre4);
        setColor(name5, fre5);
        setColor(name6, fre6);
        setColor(name7, fre7);

    }

    private void setColor(TextView txt, TextView txt1) {
        String emotion = txt.getText().toString();
        HashMap<String, Integer> temp = new HashMap<>();
        temp.put("Happiness -", R.color.happy);
        temp.put("Anger -", R.color.angry);
        temp.put("Disgust -", R.color.disgust);
        temp.put("Sadness -", R.color.sad);
        temp.put("Fear -", R.color.fear);
        temp.put("Neutral -", R.color.neutral);
        temp.put("Surprise -", R.color.surprise);

        for (Map.Entry<String, Integer> entry : temp.entrySet()) {
            String emot = entry.getKey();
            int color = entry.getValue();
            if (emot.equals(emotion)) {
                txt.setTextColor(getResources().getColor(color));
                txt1.setTextColor(getResources().getColor(color));
                break;
            }
        }

    }

    public TreeMap<String, Integer> sortMapByValue(HashMap<String, Integer> map) {
        Comparator<String> comparator = new ValueComparator(map);
        //TreeMap is a map sorted by its keys.
        //The comparator is used to sort the TreeMap by keys.
        TreeMap<String, Integer> result = new TreeMap<String, Integer>(comparator);
        result.putAll(map);
        return result;
    }

    class ValueComparator implements Comparator<String> {

        HashMap<String, Integer> map = new HashMap<String, Integer>();

        public ValueComparator(HashMap<String, Integer> map) {
            this.map.putAll(map);
        }

        @Override
        public int compare(String s1, String s2) {
            if (map.get(s1) >= map.get(s2)) {
                return -1;
            } else {
                return 1;
            }
        }
    }


    private void visible() {
        name1 = findViewById(R.id.name1);
        name2 = findViewById(R.id.name2);
        name3 = findViewById(R.id.name3);
        name4 = findViewById(R.id.name4);
        name5 = findViewById(R.id.name5);
        name6 = findViewById(R.id.name6);
        name7 = findViewById(R.id.name7);
        fre1 = findViewById(R.id.fre1);
        fre2 = findViewById(R.id.fre2);
        fre3 = findViewById(R.id.fre3);
        fre4 = findViewById(R.id.fre4);
        fre5 = findViewById(R.id.fre5);
        fre6 = findViewById(R.id.fre6);
        fre7 = findViewById(R.id.fre7);

        name1.setVisibility(View.VISIBLE);
        name2.setVisibility(View.VISIBLE);
        name3.setVisibility(View.VISIBLE);
        name4.setVisibility(View.VISIBLE);
        name5.setVisibility(View.VISIBLE);
        name6.setVisibility(View.VISIBLE);
        name7.setVisibility(View.VISIBLE);
        fre1.setVisibility(View.VISIBLE);
        fre2.setVisibility(View.VISIBLE);
        fre3.setVisibility(View.VISIBLE);
        fre4.setVisibility(View.VISIBLE);
        fre6.setVisibility(View.VISIBLE);
        fre5.setVisibility(View.VISIBLE);
        fre7.setVisibility(View.VISIBLE);
    }

    private void invisible() {
        name1 = findViewById(R.id.name1);
        name2 = findViewById(R.id.name2);
        name3 = findViewById(R.id.name3);
        name4 = findViewById(R.id.name4);
        name5 = findViewById(R.id.name5);
        name6 = findViewById(R.id.name6);
        name7 = findViewById(R.id.name7);
        fre1 = findViewById(R.id.fre1);
        fre2 = findViewById(R.id.fre2);
        fre3 = findViewById(R.id.fre3);
        fre4 = findViewById(R.id.fre4);
        fre5 = findViewById(R.id.fre5);
        fre6 = findViewById(R.id.fre6);
        fre7 = findViewById(R.id.fre7);

        name1.setVisibility(View.INVISIBLE);
        name2.setVisibility(View.INVISIBLE);
        name3.setVisibility(View.INVISIBLE);
        name4.setVisibility(View.INVISIBLE);
        name5.setVisibility(View.INVISIBLE);
        name6.setVisibility(View.INVISIBLE);
        name7.setVisibility(View.INVISIBLE);
        fre1.setVisibility(View.INVISIBLE);
        fre2.setVisibility(View.INVISIBLE);
        fre3.setVisibility(View.INVISIBLE);
        fre4.setVisibility(View.INVISIBLE);
        fre6.setVisibility(View.INVISIBLE);
        fre5.setVisibility(View.INVISIBLE);
        fre7.setVisibility(View.INVISIBLE);
    }

    private void detectandFrame(final Bitmap mBitmap) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        final ByteArrayInputStream inputStream = new ByteArrayInputStream((outputStream.toByteArray()));

        AsyncTask<InputStream, String, Face[]> detectTask = new AsyncTask<InputStream, String, Face[]>() {
            ProgressDialog pd = new ProgressDialog(getApplicationContext());

            @Override
            protected Face[] doInBackground(InputStream... inputStreams) {

                publishProgress("Detecting...");
                FaceServiceClient.FaceAttributeType[] faceAttr = new FaceServiceClient.FaceAttributeType[]{
                        FaceServiceClient.FaceAttributeType.Age,
                        FaceServiceClient.FaceAttributeType.Emotion
                };


                try {
                    Face[] result = faceServiceClient.detect(inputStreams[0],
                            true,
                            false,
                            faceAttr);

                    if (result == null) {
                        publishProgress("Detection failed. Nothing detected.");
                    }

                    publishProgress(String.format("Detection Finished. %d face(s) detected", result.length));
                    return result;
                } catch (Exception e) {
                    publishProgress("Detection Failed: " + e.getMessage());
                    return null;
                }
            }


            @Override
            protected void onPreExecute() {
                //    pd.show();
            }

            @Override
            protected void onProgressUpdate(String... values) {
                //   pd.setMessage(values[0]);
            }

            @Override
            protected void onPostExecute(Face[] faces) {
                //   pd.dismiss();
                if (faces == null) {
                    makeToast("No faces detected.");
                } else {
                    people = faces;
                    if (people.length == 1) {
                        makeToast("Person detected");
                        displayResults(people[0]);
                    } else if (people.length == 0) {
                        makeToast("No faces detected.");
                    } else {
                        makeToast("Please consider retaking the picture otherwise only the largest face will be analyzed.");
                    }
                }
                imageView.setImageBitmap(drawFaceRectangleOnBitmap(image, people, "Bob"));

            }
        };
        detectTask.execute(inputStream);
    }

    private void displayResults(Face person) {
        TreeMap<Double, String> treeMap = new TreeMap<>();
        treeMap.put(person.faceAttributes.emotion.happiness, "Happiness");
        treeMap.put(person.faceAttributes.emotion.anger, "Anger");
        treeMap.put(person.faceAttributes.emotion.disgust, "Disgust");
        treeMap.put(person.faceAttributes.emotion.sadness, "Sadness");
        treeMap.put(person.faceAttributes.emotion.neutral, "Neutral");
        treeMap.put(person.faceAttributes.emotion.surprise, "Surprise");
        treeMap.put(person.faceAttributes.emotion.fear, "Fear");

        arrayList = new ArrayList<>();
        rank = new TreeMap<>();

        int counter = 0;
        String t = "";

        for (Map.Entry<Double, String> entry : treeMap.entrySet()) {
            String key = entry.getValue();
            Double value = entry.getKey();
            rank.put(counter, key);
            t += key;
            counter++;
            arrayList.add(value);
        }

        age.setText("Estimated age: " + person.faceAttributes.age);
        String emotions = "";
        emotion1.setText(rank.size() + " " + treeMap.size() + " " + arrayList.size() + "\n " + t);
        for (int i = rank.size() - 1; i >= 0; i--) {
            emotions += rank.get(i) + ": " + 100 * arrayList.get(i) + "%\n";
        }
        emotion1.setText(emotions);


    /*    emotion1.setText(rank.get(rank.size() - 1) + ": " + 100 * arrayList.get(rank.size() - 1) + "%");
        emotion2.setText(rank.get(rank.size() - 2) + ": " + 100 * arrayList.get(rank.size() - 2) + "%");
        emotion3.setText(rank.get(rank.size() - 3) + ": " + 100 * arrayList.get(rank.size() - 3) + "%");
        emotion4.setText(rank.get(rank.size() - 4) + ": " + 100 * arrayList.get(rank.size() - 4) + "%");
        emotion5.setText(rank.get(rank.size() - 5) + ": " + 100 * arrayList.get(rank.size() - 5) + "%");*/


    }

    private Bitmap drawFaceRectangleOnBitmap(Bitmap imageBitmap, Face[] faces, String name) {
        Bitmap changedBitmap = imageBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(changedBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);

        int strokeWidth = 3;
        paint.setStrokeWidth(strokeWidth);

        if (faces != null) {
            int length = faces.length;
            for (Face face : faces) {
                FaceRectangle faceRectangle = face.faceRectangle;
                canvas.drawRect(faceRectangle.left,
                        faceRectangle.top,
                        faceRectangle.left + faceRectangle.width,
                        faceRectangle.top + faceRectangle.height,
                        paint);
            }
        }
        return changedBitmap;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.backtohome) {
            if (extra.equals("Graph")) {
                startActivity(new Intent(getApplicationContext(), EmotionsAnalyzer.class));
            } else {
                if (rank.isEmpty()) {
                    makeToast("No emotion data collected. Please retake or choose the picture again.");
                } else {
                    makeToast("Saving information.");
                    saveInformation();
                }
            }
            //Save information and image in Firebase: image, date, top 3 emotions.
        }
        if (id == R.id.backbutton) {
            startActivity(new Intent(getApplicationContext(), EmotionsAnalyzer.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveInformation() {
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Emotions");
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("Time", currentDateTimeString);

        int count = 0;
        for (int i = rank.size() - 1; i >= 0; i--) {
            DecimalFormat df2 = new DecimalFormat(".##");
            hashMap.put("Emotion" + count, rank.get(i) + " " + df2.format(100 * arrayList.get(i)) + "%");
            count++;
        }
        hashMap.put("Size", Integer.toString(rank.size()));
        databaseReference.push().setValue(hashMap);

        doUpload(image, currentDateTimeString);


    }

    private void doUpload(Bitmap bitmap, String s) {
        String path = "Emotions/" + s + ".jpg";
        StorageReference storageReference2 = FirebaseStorage.getInstance().getReference(path);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data2 = baos.toByteArray();

        UploadTask uploadTask = storageReference2.putBytes(data2);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                makeToast("Exception " + exception.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                makeToast("Picture saved.");
                startActivity(new Intent(getApplicationContext(), EmotionsAnalyzer.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.picturemenu, menu);
        return true;
    }


    private void initializeViews() {
        imageView = findViewById(R.id.imageView);
        emotion1 = findViewById(R.id.emotion1);
        emotion2 = findViewById(R.id.emotion2);
        emotion3 = findViewById(R.id.emotion3);
        emotion4 = findViewById(R.id.emotion4);
        age = findViewById(R.id.age);
        emotion5 = findViewById(R.id.emotion5);
    }

    private void makeToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

}
