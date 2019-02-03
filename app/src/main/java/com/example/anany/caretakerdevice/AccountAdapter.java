package com.example.anany.caretakerdevice;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Movie;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.telephony.SmsManager;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AccountAdapter extends ArrayAdapter<PendingAccounts> {

    private Context mContext;
    Activity activity;
    private List<PendingAccounts> accountsList = new ArrayList<>();
    String rowid = "";
    StorageReference mImageRef;

    public AccountAdapter(Activity a, Context context, ArrayList<PendingAccounts> list) {
        super(context, 0, list);
        mContext = context;
        activity = a;
        accountsList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);

        final PendingAccounts accounts = accountsList.get(position);
        final TextView username = listItem.findViewById(R.id.accountusername);
        username.setText(accounts.getUsername());

        final ImageView imageView = listItem.findViewById(R.id.userpicture);
        StorageReference mImageRef =
                FirebaseStorage.getInstance().getReference(accounts.getImagelocation());
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

       /* final ImageView imageView = listItem.findViewById(R.id.userpicture);
       mImageRef =
                FirebaseStorage.getInstance().getReference(accounts.getImagelocation());
        final long ONE_MEGABYTE = 1024 * 1024;
        if (mImageRef == null) {
            imageView.setImageResource(R.drawable.oneuser);
        } else {
            mImageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    DisplayMetrics dm = new DisplayMetrics();

                        imageView.setMinimumHeight(dm.heightPixels);
                        imageView.setMinimumWidth(dm.widthPixels);
                    imageView.setImageBitmap(bm);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    makeToast("Didn't work");
                }
            });
        }*/


        TextView contactstitle = listItem.findViewById(R.id.contactstitle);


        SpannableString content = new SpannableString("Contact Info");
        content.setSpan(new UnderlineSpan(), 0, 12, 0);
        contactstitle.setText(content);
        TextView email = listItem.findViewById(R.id.accountemail);

        if (accounts.getEmail().isEmpty()) {
            email.setText("No email was given.");
        } else {
            email.setText(accounts.getEmail());
        }

        final TextView phone = listItem.findViewById(R.id.accountphone);

        if (accounts.getPhone_number().isEmpty()) {
            phone.setText("No phone # was given.");
        } else {
            String sphone = accounts.getPhone_number();
            String finalphone = "(" + sphone.substring(0, 3) + ") - " + sphone.substring(3, 6) + " - " + sphone.substring(6, 10);
            phone.setText(finalphone);
        }
        final String createdtime = accounts.getTime();
        TextView password = listItem.findViewById(R.id.accountpassword);
        password.setText(accounts.getPassword());

        TextView name = listItem.findViewById(R.id.accountname);
        name.setText(accounts.getName() + ":   " + accounts.getRelations());

        Button approve = listItem.findViewById(R.id.approve);
        Button decline = listItem.findViewById(R.id.decline);


        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database2 = FirebaseDatabase.getInstance();
                final DatabaseReference databaseReference = database2.getReference("Pending Requests");


                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            String username = child.child("Name").getValue(String.class);

                            if (username.equals(accounts.getName())) {
                                rowid = child.getKey();
                                String path = "Pending Requests/" + rowid;
                                FirebaseDatabase database = FirebaseDatabase.getInstance();

                                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                DatabaseReference databaseReference1 = firebaseDatabase.getReference("Create Account");
                                DatabaseReference databaseReference2 = firebaseDatabase.getReference("Events Log");
                                String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                                MediaPlayer ring2 = MediaPlayer.create(activity, R.raw.createaccount);
                                ring2.start();

                                HashMap<String, String> values = new HashMap<>();
                                values.put("Name", accounts.getName());
                                values.put("Username", accounts.getUsername());
                                values.put("Password", accounts.getPassword());
                                values.put("Phone", accounts.getPhone_number());
                                values.put("Email", accounts.getEmail());
                                values.put("Email", accounts.getEmail());
                                values.put("Relation", accounts.getRelations());
                                values.put("Description", accounts.getDescription());
                                values.put("Created", createdtime);
                                values.put("Approved", currentDateTimeString);

                                HashMap<String, String> values2 = new HashMap<>();
                                values2.put("Name", accounts.getName());
                                values2.put("Username", accounts.getUsername());
                                values2.put("Password", accounts.getPassword());
                                values2.put("Phone", accounts.getPhone_number());
                                values2.put("Email", accounts.getEmail());
                                values2.put("Relation", accounts.getRelations());
                                values2.put("Description", accounts.getDescription());
                                values2.put("Created", createdtime);
                                values2.put("Time", currentDateTimeString);
                                values2.put("Classifier", "Approved");

                                databaseReference2.push().setValue(values2);

                                databaseReference1.push().setValue(values, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                        if (databaseError == null) {
                                            makeToast("Approved Account.");
                                        } else {
                                            makeToast("Error saving Information.");
                                        }
                                    }
                                });

                                final DatabaseReference dbref = database.getReference(path);
                                dbref.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        if (phone.getText().toString().isEmpty()) {
                                            String[] TO = {"ishaanjav@gmail.com"};
                                            String[] CC = {""};
                                            Intent emailIntent = new Intent(Intent.ACTION_SEND);

                                            emailIntent.setData(Uri.parse("mailto:"));
                                            emailIntent.setType("text/plain");
                                            emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                                            emailIntent.putExtra(Intent.EXTRA_CC, CC);
                                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Information System Account");
                                            emailIntent.putExtra(Intent.EXTRA_TEXT, "Your account has been approved.");

                                          /*  startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                                            finish();*/
                                        } else {
                                            SmsManager smsManager = SmsManager.getDefault();
                                            smsManager.sendTextMessage("8482482353", null, "Your account for the Information System was approved.", null, null);
                                        }

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
                                break;
                            } else {
                                continue;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        makeToast(databaseError.getMessage());
                    }
                };

                databaseReference.addValueEventListener(valueEventListener);


            }
        });
        ImageView sendemail = listItem.findViewById(R.id.emailimage);
        sendemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] TO = {"ishaanjav@gmail.com"};
                String[] CC = {""};
                Intent emailIntent = new Intent(Intent.ACTION_SEND);

                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_CC, CC);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");

                try {
                    activity.startActivity(Intent.createChooser(emailIntent, "Send mail..."));

                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(activity.getApplicationContext(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageView phonecall = listItem.findViewById(R.id.phoneimage);
        phonecall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    return;
                } else {
                    makeToast("ALL GOOD!");
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:8482482353"));

                    activity.startActivity(callIntent);
                }
            }
        });
        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database2 = FirebaseDatabase.getInstance();
                final DatabaseReference databaseReference = database2.getReference("Pending Requests");


                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            String username = child.child("Name").getValue(String.class);

                            if (username.equals(accounts.getName())) {
                                rowid = child.getKey();
                                String path = "Pending Requests/" + rowid;
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference databaseReference2 = database.getReference("Events Log");
                                String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

                                HashMap<String, String> values2 = new HashMap<>();
                                values2.put("Name", accounts.getName());
                                values2.put("Username", accounts.getUsername());
                                values2.put("Password", accounts.getPassword());
                                values2.put("Phone", accounts.getPhone_number());
                                values2.put("Email", accounts.getEmail());
                                values2.put("Relation", accounts.getRelations());
                                values2.put("Description", accounts.getDescription());
                                values2.put("Created", createdtime);
                                values2.put("Time", currentDateTimeString);
                                values2.put("Classifier", "Declined");

                                databaseReference2.push().setValue(values2);

                                final DatabaseReference dbref = database.getReference(path);
                                dbref.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        makeToast("Deleted Account.");
                                        if (phone.getText().toString().isEmpty()) {
                                            String[] TO = {"ishaanjav@gmail.com"};
                                            String[] CC = {""};
                                            Intent emailIntent = new Intent(Intent.ACTION_SEND);

                                            emailIntent.setData(Uri.parse("mailto:"));
                                            emailIntent.setType("text/plain");
                                            emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                                            emailIntent.putExtra(Intent.EXTRA_CC, CC);
                                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Information System Account");
                                            emailIntent.putExtra(Intent.EXTRA_TEXT, "Your account has been declined.");

                                          /*  startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                                            finish();*/
                                        } else {
                                            SmsManager smsManager = SmsManager.getDefault();
                                            smsManager.sendTextMessage("8482482353", null, "Your account for the Information System was declined.", null, null);
                                        }

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        makeToast("Failed to Delete Account.");
                                    }
                                });
                                break;
                            } else {
                                continue;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        makeToast(databaseError.getMessage());
                    }
                };

                databaseReference.addValueEventListener(valueEventListener);


            }
        });


        Button button = listItem.findViewById(R.id.button);
        button.setClickable(true);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), accounts.getDescription(), Toast.LENGTH_LONG).show();

            }
        });


        return listItem;
    }

    private void makeToast(String S) {
        Toast.makeText(activity.getApplicationContext(), S, Toast.LENGTH_SHORT).show();
    }


}
