package com.example.anany.caretakerdevice;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class EmotionsAdapter extends ArrayAdapter<EmotionsHolder> {

    private Context mContext;
    private List<EmotionsHolder> accountsList;
    String rowid = "";
    Activity activity;
    CardView cardView;


    public EmotionsAdapter(Activity a, Context context, ArrayList<EmotionsHolder> list) {
        super(context, 0, list);
        mContext = context;
        accountsList = list;
        activity = a;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.emotions_item, parent, false);


        EmotionsHolder emotionsHolder = accountsList.get(position);

        final ImageView imageView = listItem.findViewById(R.id.imageView);
        TextView time = listItem.findViewById(R.id.time);
        TextView title = listItem.findViewById(R.id.title);
        TextView emotion1 = listItem.findViewById(R.id.emotion1);
        TextView emotion2 = listItem.findViewById(R.id.emotion2);
        TextView emotion3 = listItem.findViewById(R.id.emotion3);
        // makeToast("Hi " + emotionsHolder.getSize());

        StorageReference mImageRef = FirebaseStorage.getInstance().getReference("Emotions/" + emotionsHolder.getTime() + ".jpg");
        final long ONE_MEGABYTE = 1024 * 1024;
        if (mImageRef.getBytes(ONE_MEGABYTE) == null) {
            imageView.setImageResource(R.drawable.oneuser);

        } else {
            mImageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    DisplayMetrics dm = new DisplayMetrics();

                       /* imageView.setMinimumHeight(dm.heightPixels);
                        imageView.setMinimumWidth(dm.widthPixels);*/
                    imageView.setImageBitmap(bm);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    imageView.setImageResource(R.drawable.oneuser);
                }
            });
        }

        time.setText(emotionsHolder.getTime());
        //Happiness 100.0%
        ArrayList<String> temp = emotionsHolder.getArrayList();
        if (temp.size() == 1) {
            String temporary = temp.get(0);
            String[] parts = temporary.split(" ");
            emotion1.setText(parts[0] + ": " + parts[1]);
        } else if (temp.size() == 2) {
            String temporary = temp.get(0);
            String[] parts = temporary.split(" ");
            emotion1.setText(parts[0] + ": " + parts[1]);
            String temp2 = temp.get(1);
            String[] parts2 = temp2.split(" ");
            emotion2.setText(parts2[0] + ": " + parts2[1]);
        } else {
            String temporary = temp.get(0);
            String[] parts = temporary.split(" ");
            emotion1.setText(parts[0] + ": " + parts[1]);
            String temp2 = temp.get(1);
            String[] parts2 = temp2.split(" ");
            emotion2.setText(parts2[0] + ": " + parts2[1]);
            String temp3 = temp.get(2);
            String[] parts3 = temp3.split(" ");
            emotion3.setText(parts3[0] + ": " + parts3[1]);
        }


        return listItem;
    }

    private void makeToast(String S) {
        Toast.makeText(activity.getApplicationContext(), S, Toast.LENGTH_LONG).show();
    }
}
