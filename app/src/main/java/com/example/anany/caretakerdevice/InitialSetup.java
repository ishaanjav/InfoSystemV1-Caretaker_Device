package com.example.anany.caretakerdevice;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InitialSetup extends AppCompatActivity {

    TextView welcome, aboutapp, appdescription, initialsetuptitle, spinnerdescription;
    Button gotosetup;
    RelativeLayout title;
    int buttonclicked = 0;
    CardView cd, infoholder, cmt, image, colorhcnage;

    TextView happiness;

    //FirebaseVisionImage visionImage;
    //FirebaseVisionFaceDetectorOptions options;

    Button picture;
    EditText phone;
    Spinner spinner;
    private StorageReference storageReference;


    Bitmap bitmap;
    CardView realholder;
    TextView choosetitle;
    RelativeLayout inflaterholder;
    ImageView togglepassword;
    boolean passwordshowing = false;
    private Uri uri;
    FirebaseStorage firebaseStorage;

    StorageReference storageRef;
    StorageReference imageRef;

    EditText username, password, name, description;

    CardView holder;
    ArrayList<String> values;

    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.initial_setup);
        firebaseDatabase = FirebaseDatabase.getInstance();
        values = new ArrayList<>();

        //
    //    startActivity(new Intent(this, CaretakerAccount.class));
        //

        databaseReference = firebaseDatabase.getReference("/CComplete");

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    values.add(child.child("State").getValue(String.class));

                    if (values.isEmpty()) {
                        getSupportActionBar().setTitle("Initial Setup");
                        makeToast("Nothing there.");
                        addAnimations();
                    } else {
                       // makeToast(values.toString());
                        startActivity(new Intent(InitialSetup.this, MainActivity.class));
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.addValueEventListener(valueEventListener);

        firebaseStorage = FirebaseStorage.getInstance();

        storageReference = firebaseStorage.getReference();
        bindViews();
        picture.setVisibility(View.INVISIBLE);
        phone.setVisibility(View.INVISIBLE);

        choosetitle.setVisibility(View.INVISIBLE);
        //spinner.setVisibility(View.INVISIBLE);
        initialsetuptitle.setVisibility(View.INVISIBLE);
        holder.setVisibility(View.INVISIBLE);
        SpannableStringBuilder str = new SpannableStringBuilder("Welcome to the Information\n               System App");
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 35, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        welcome.setText(str);
        invisibility();

        happiness = findViewById(R.id.happiness);
        addAnimations();


    }

    private void invisibility() {
        title.setVisibility(View.INVISIBLE);
        welcome.setVisibility(View.INVISIBLE);
        aboutapp.setVisibility(View.INVISIBLE);
        appdescription.setVisibility(View.INVISIBLE);
        //spinner.setVisibility(View.INVISIBLE);
        spinnerdescription.setVisibility(View.INVISIBLE);

        colorhcnage.setVisibility(View.INVISIBLE);
        image.setBackgroundResource(R.color.lightorange);
        cd.setVisibility(View.INVISIBLE);
        image.setVisibility(View.INVISIBLE);
        cmt = findViewById(R.id.changeback);
        choosetitle.setVisibility(View.INVISIBLE);

        cmt.setBackgroundResource(R.drawable.bettergradient);
        infoholder.setVisibility(View.INVISIBLE);

    }

    private void addAnimations() {
        // makeToast(values.toString());
        title.setVisibility(View.VISIBLE);

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setStartOffset(1000);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(2000);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setStartOffset(1000);
        fadeOut.setDuration(4000);

        AnimationSet animation = new AnimationSet(false); //change to false
        animation.addAnimation(fadeIn);
        AnimationSet welcomeani = new AnimationSet(false);
        welcomeani.addAnimation(fadeOut);

        title.setAnimation(animation);


        Runnable r = new Runnable() {
            @Override
            public void run() {
                Animation fadeIn = new AlphaAnimation(0, 1);
                fadeIn.setStartOffset(1000);
                fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
                fadeIn.setDuration(3000);
                Animation nim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
                AnimationSet animation = new AnimationSet(false); //change to false
                animation.addAnimation(fadeIn);
                welcome.setVisibility(View.VISIBLE);

                welcome.setAnimation(animation);
                displayimage();

            }
        };

        Handler h = new Handler();
        h.postDelayed(r, 2500);


    }

    private void displayimage() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Animation nim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
                AnimationSet dd = new AnimationSet(false);
                dd.addAnimation(nim);
                image.setVisibility(View.VISIBLE);
                colorhcnage.setVisibility(View.VISIBLE);
                image.setAnimation(nim);
                aboutappanimation();
            }
        };
        Handler h = new Handler();
        h.postDelayed(r, 1500);
    }

    private void aboutappanimation() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Animation nim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slidefromright);
                AnimationSet dd = new AnimationSet(false);
                dd.addAnimation(nim);

                infoholder.setVisibility(View.VISIBLE);
                aboutapp.setVisibility(View.VISIBLE);
                aboutapp.startAnimation(nim);
                descriptionAnimation();
                /*welcome.setAnimation(animation);*/
            }
        };
        Handler h = new Handler();
        h.postDelayed(r, 2000);

    }

    private void descriptionAnimation() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Animation nim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
                AnimationSet dd = new AnimationSet(false);
                dd.addAnimation(nim);

                String text2 = "      The purpose of this app is to serve as an information system for senior citizens with disabilities and diseases, particularly Alzheimer's. This app functions by requiring visitors to create an account by entering information about their relation to the resident as well as a description of themselves in addition to other details. READ MORE";
                Spannable spannable = new SpannableString(text2);

                spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.READMORE)), 337, 346, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableStringBuilder str = new SpannableStringBuilder(text2);
                str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 337, 346, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                Spannable spannable1 = new SpannableString(text2);
                spannable.setSpan(new UnderlineSpan(), 337, 346, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


                appdescription.setText(spannable, TextView.BufferType.SPANNABLE);
                appdescription.setVisibility(View.VISIBLE);
                appdescription.startAnimation(nim);
                buttonAnimation();
                /*welcome.setAnimation(animation);*/
            }
        };
        Handler h = new Handler();
        h.postDelayed(r, 2300);

    }

    private void buttonAnimation() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Animation nim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoominspecial);
                AnimationSet dd = new AnimationSet(false);
                dd.addAnimation(nim);

                gotosetup.setVisibility(View.VISIBLE);
                cd.setVisibility(View.VISIBLE);
                gotosetup.startAnimation(nim);
                listeners();
                /*welcome.setAnimation(animation);*/
            }
        };
        Handler h = new Handler();
        h.postDelayed(r, 1700);
    }

    private void zoominanimation(final View v, int i) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Animation nim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
                AnimationSet dd = new AnimationSet(false);
                dd.addAnimation(nim);


                v.startAnimation(dd);

                /*welcome.setAnimation(animation);*/
            }
        };
        Handler h = new Handler();
        h.postDelayed(r, i);
    }

    private void fadeinanimation(final View v, int i) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Animation nim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.realfadein);
                AnimationSet dd = new AnimationSet(false);
                dd.addAnimation(nim);


                v.startAnimation(dd);

                /*welcome.setAnimation(animation);*/
            }
        };
        Handler h = new Handler();
        h.postDelayed(r, i);
    }

    private void fadeoutanimation(final View v, int i) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Animation nim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);
                AnimationSet dd = new AnimationSet(false);
                dd.addAnimation(nim);


                v.startAnimation(dd);

                listeners();
                /*welcome.setAnimation(animation);*/
            }
        };
        Handler h = new Handler();
        h.postDelayed(r, i);
    }

    private void zoomoutanimation(final View v, int i) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Animation nim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomout);
                AnimationSet dd = new AnimationSet(false);
                dd.addAnimation(nim);


                v.startAnimation(dd);
                v.setVisibility(View.INVISIBLE);
                listeners();
                /*welcome.setAnimation(animation);*/
            }
        };
        Handler h = new Handler();
        h.postDelayed(r, i);
    }

    private void secondpageanimations() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Animation nim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
                AnimationSet dd = new AnimationSet(false);
                dd.addAnimation(nim);
                Animation nim4 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slidefromright);
                AnimationSet xd = new AnimationSet(false);
                dd.addAnimation(nim);
                Animation nim2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.realfadein);
                AnimationSet dm = new AnimationSet(false);
                dm.addAnimation(nim);

                initialsetuptitle.setVisibility(View.VISIBLE);
                holder.setVisibility(View.VISIBLE);
                spinnerdescription.setVisibility(View.VISIBLE);
                choosetitle.setVisibility(View.VISIBLE);

                Toast.makeText(getApplicationContext(), "This is the Initial Setup where you will setup your device.", Toast.LENGTH_LONG).show();
                initialsetuptitle.startAnimation(nim4);
                choosetitle.startAnimation(nim);
                holder.startAnimation(nim2);
                spinnerdescription.startAnimation(nim);
                gotosetup.setText("Continue with Setup");

            }
        };
        Handler h = new Handler();
        h.postDelayed(r, 3000);
    }

    private void listeners() {
        phoneListener();

        togglepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String spassword = password.getText().toString();

                if (passwordshowing == false) {
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    togglepassword.setImageResource(R.drawable.hidepassword);
                    passwordshowing = true;
                    password.setSelection(password.getText().toString().length());
                } else if (passwordshowing == true) {
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    togglepassword.setImageResource(R.drawable.showpassword);
                    passwordshowing = false;
                    password.setSelection(password.getText().toString().length());
                }
            }
        });

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(InitialSetup.this,
                                new String[]{Manifest.permission.CAMERA},
                                101);

                    } else {

                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, 1);
                    }
                } catch (ActivityNotFoundException anfe) {
                    //display an error message
                    String errorMessage = "Whoops - your device doesn't support capturing images!";
                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();

                }
            }
        });

        gotosetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buttonclicked == 0) {
                    fadeoutanimation(title, 0);

                    zoomoutanimation(image, 100);
                    zoomoutanimation(welcome, 300);
                    fadeoutanimation(infoholder, 500);
                    title.setVisibility(View.INVISIBLE);
                    infoholder.setVisibility(View.INVISIBLE);

                    secondpageanimations();
                    buttonclicked++;
                } else if (buttonclicked == 1) {
                    String username1 = username.getText().toString();
                    String password1 = password.getText().toString();
                    String description1 = description.getText().toString();
                    String fullname = name.getText().toString();

                    if (fullname.isEmpty() || description1.length() < 15 || password1.length() < 5 || username1.length() < 5) {
                        Toast.makeText(InitialSetup.this, "Either some of your information is empty, or it is too short.", Toast.LENGTH_SHORT).show();
                    } else {
                        username.setVisibility(View.INVISIBLE);
                        password.setVisibility(View.INVISIBLE);
                        description.setVisibility(View.INVISIBLE);
                        name.setVisibility(View.INVISIBLE);
                        togglepassword.setVisibility(View.INVISIBLE);
                        phone.setVisibility(View.VISIBLE);
                        picture.setVisibility(View.VISIBLE);
                        buttonclicked++;
                        gotosetup.setText("Create Account");
                    }


                } else {
                    if (buttonclicked < 4) {
                        if (buttonclicked == 2) {
                            for (int i = 0; i < 2; i++) {
                                Toast.makeText(getApplicationContext(), "If you are certain with these settings, click the button 2 more times. These settings can be changed later.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        buttonclicked++;
                    } else {
                        String phoneno = phone.getText().toString();

                        if (phoneno.length() != 18) {
                            Toast.makeText(InitialSetup.this, "You have not entered a valid phone number", Toast.LENGTH_SHORT).show();
                        } else {
                            makeAlertBuilder();
                        }


                    }
                }
            }
        });

        appdescription.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(InitialSetup.this, R.style.AlertDialogStyle);

                alertDialog.setTitle("About This App");
                alertDialog.setMessage("          The purpose of this app is to serve as an information system for senior citizens with disabilities and diseases, particularly Alzheimer's. This app functions by requiring visitors to create an account by entering information about their relation to the resident as well as a description of themselves in addition to other details. Once the account has been approved by the caretaker of the resident, visitors can use their account to sign in to the Visitor Device positioned near the front door of the house.\n\n          When a visitor signs in, the resident and caretaker will get notified of their arrival and they will also get the information about the visitor, helping the resident with Alzheimer's remember and know who who is visiting them. Furthermore, the caretaker can use this app to monitor the visits as well as ensure that the resident has not left the house. Essentially, this app can ensure the safety of the resident as well as inform them about who is visiting. It also gives the caretaker the ability to make sure that the visitors entering the house are safe.");

                alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
//CONVER THIS TO ALERTDIALOF OR SOMETHING TO SETCONTENTVIEW TO XML FILE. FOLLOW STACK OVERFLOW ARTICLE.
                alertDialog.show();


                return false;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            bitmap = (Bitmap) data.getExtras().get("data");

            String image = BitMapToString(bitmap);
            //   uri = Uri.parse(image);

           /* options =
                    new FirebaseVisionFaceDetectorOptions.Builder()
                            .setModeType(FirebaseVisionFaceDetectorOptions.ACCURATE_MODE)
                            .setLandmarkType(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                            .setClassificationType(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                            .setMinFaceSize(0.15f)
                            .setTrackingEnabled(true)
                            .build();

            FirebaseVisionFaceDetector detector;

            detector = FirebaseVision.getInstance()
                    .getVisionFaceDetector(options);
            FirebaseVisionImage image2 = FirebaseVisionImage.fromBitmap(bitmap);
            Task<List<FirebaseVisionFace>> result =
                    detector.detectInImage(image2)
                            .addOnSuccessListener(
                                    new OnSuccessListener<List<FirebaseVisionFace>>() {
                                        @Override
                                        public void onSuccess(List<FirebaseVisionFace> faces) {
                                            for (FirebaseVisionFace face : faces) {
                                                float smileProb = 0f;
                                                if (face.getSmilingProbability() != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                                                    smileProb = face.getSmilingProbability() * 100;
                                                }
                                                if (smileProb > 52) {
                                                    happiness.setText("You looked happy in that picture!");
                                                } else if (smileProb < 20) {
                                                    happiness.setText("You didn't look too happy in that picture! You should consider\n retaking it.");
                                                } else {

                                                }


                                            }
                                        }
                                    })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            makeToast("IT FAILED!");
                                            makeToast(e.getMessage());
                                            makeToast(e.getMessage());
                                            makeToast(e.getMessage());
                                        }
                                    });
*/

            doUpload(bitmap, storageReference);

        }

    }

    private void doUpload(Bitmap bitmap, StorageReference storageReference2) {
        String s = "Caretaker/" + name.getText().toString() + ".jpg";
        storageReference2 = FirebaseStorage.getInstance().getReference(s);

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
            }
        });
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public void getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);

        makeToast(path);

    }


    public void uploadFile() {

        if (uri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
            fileReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {


                        }
                    }, 4000);
                    makeToast("Image upload successful");
                    Upload upload = new Upload("Caretaker Picture", taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());

                    String uploadId = databaseReference.push().getKey();
                    databaseReference.child(uploadId).setValue(upload);


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    makeToast(e.getMessage());
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                }
            });

        } else {
            makeToast("URI NULL");
        }

    }

    public String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    private void phoneListener() {
        phone.addTextChangedListener(new TextWatcher() {
            private int previousLength;
            private boolean backSpace;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                previousLength = phone.getText().length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (phone.getText().length() > 18) {
                    String s = phone.getText().toString();
                    phone.setText(s.substring(0, 18));
                    phone.setSelection(phone.getText().length());
                    Toast.makeText(getApplicationContext(), "Phone number can't be more than 10 digits long", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int s = phone.getText().length();
                backSpace = previousLength > s;

                if (backSpace) {
                    int currentLengt = phone.getText().length();
                    String current = phone.getText().toString();
                    if (currentLengt != 7 && currentLengt != 8 && currentLengt != 14 && currentLengt != 13 && currentLengt != 12 && currentLengt != 1) {

                    } else if (currentLengt == 1) {
                        phone.setText("");
                    } else if (currentLengt == 8 || currentLengt == 7) {
                        phone.setText(current.substring(0, 5));
                        phone.setSelection(phone.getText().length());
                    } else if (currentLengt == 14 || currentLengt == 13 || currentLengt == 12) {
                        String hello = phone.getText().toString();
                        String hi = hello.substring(0, 11);

                        phone.setText(hi);
                        phone.setSelection(phone.getText().length());
                    }
                } else {
                    int currentLength = phone.getText().length();
                    String t = phone.getText().toString();
                    if (currentLength == 1) {
                        phone.setText("(" + phone.getText());
                        phone.setSelection(phone.getText().length());

                    } else if (currentLength == 4) {
                        phone.setText(phone.getText() + ") - ");
                        phone.setSelection(phone.getText().length());

                    } else if (currentLength == 5) {
                        phone.setText(t.substring(0, 4) + ") - " + t.substring(4, 5));
                        phone.setSelection(phone.getText().length());
                    } else if (currentLength == 6) {
                        phone.setText(t.substring(0, 5) + " - " + t.substring(5, 6));
                        phone.setSelection(phone.getText().length());
                    } else if (currentLength == 11) {
                        phone.setText(phone.getText() + " - ");
                        phone.setSelection(phone.getText().length());
                    } else if (currentLength == 12) {
                        phone.setText(t.substring(0, 11) + " - " + t.substring(11, 12));
                        phone.setSelection(phone.getText().length());
                    }

                }

            }
        });

    }

    private void makeAlertBuilder() {
        final Dialog alert = new Dialog(this);
        alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alert.setContentView(R.layout.alertdialog_w_edittext);
        alert.setCancelable(true);
        Button cancel = alert.findViewById(R.id.cancel_action);
        Button ok = alert.findViewById(R.id.Enter);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
                FirebaseDatabase fd = FirebaseDatabase.getInstance();
                DatabaseReference dbref = fd.getReference("/CComplete");
                HashMap<String, String> values2 = new HashMap<>();
                values2.put("State", "Complete");
                dbref.push().setValue(values2, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            //  startActivity(new Intent(InitialSetup.this, MainActivity.class));

                        } else {
                            Toast.makeText(InitialSetup.this, "Possible error with saving changes.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String susername = username.getText().toString();
                String spassword = password.getText().toString();
                String sdescription = description.getText().toString();
                String fullname = name.getText().toString();
                String phonenumber = phone.getText().toString();
                String phonereal = "";
                for (int i = 0; i < phonenumber.length(); i++) {
                    if (Character.isDigit(phonenumber.charAt(i))) {
                        phonereal += Character.toString(phonenumber.charAt(i));
                    } else {
                        continue;
                    }
                }
                fullname = "Caretaker " + fullname;

                FirebaseDatabase fd = FirebaseDatabase.getInstance();
                DatabaseReference dbref = fd.getReference("/CComplete");
                HashMap<String, String> values2 = new HashMap<>();
                values2.put("State", "Complete");
                dbref.push().setValue(values2, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            //  startActivity(new Intent(InitialSetup.this, MainActivity.class));

                        } else {
                           Toast.makeText(InitialSetup.this, "Possible error with saving changes.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference("/Create Account");
                Map<String, String> values = new HashMap<>();
                values.put("Relation", sdescription);
                values.put("Password", spassword);
                values.put("Username", susername);
                values.put("Phone", phonereal);
                values.put("Name", fullname);
                values.put("Phone", "8482482353");

                if (ContextCompat.checkSelfPermission(InitialSetup.this,
                        Manifest.permission.SEND_SMS)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(InitialSetup.this,
                            new String[]{Manifest.permission.SEND_SMS},
                            100);
                } else {

                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage("8482482353", null, "Hello! Welcome to the Information System App.\nYour username is: " + susername + "\nYour password is: " + spassword, null, null);
                }
                databaseReference.push().setValue(values, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            Toast.makeText(InitialSetup.this, "Congratulations! You have successfully made your account.", Toast.LENGTH_SHORT).show();

                            Runnable d = new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(InitialSetup.this, "Thank you for using this app.", Toast.LENGTH_SHORT).show();
                                }
                            };

                            Handler ht = new Handler();
                            ht.postDelayed(d, 2100);


                            Runnable r = new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(InitialSetup.this, MainActivity.class));
                                }
                            };
                            Handler h = new Handler();

                            h.postDelayed(r, 6200);

                        } else {
                            Toast.makeText(InitialSetup.this, "Error with creating account. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });

        alert.show();
    }


    private void bindViews() {
        title = findViewById(R.id.card);
        welcome = findViewById(R.id.welcome);
        aboutapp = findViewById(R.id.aboutapp);
        appdescription = findViewById(R.id.appdescription);
        gotosetup = findViewById(R.id.InitialSetup);
        cd = findViewById(R.id.cardView);
        colorhcnage = findViewById(R.id.colorchange);
        infoholder = findViewById(R.id.holder);
        image = findViewById(R.id.image);
        holder = findViewById(R.id.infoholder);
        choosetitle = findViewById(R.id.choosetitile);
        spinnerdescription = findViewById(R.id.choosetitile);
        initialsetuptitle = findViewById(R.id.initialsetuptitle);
        inflaterholder = findViewById(R.id.inflaterholder);

        picture = findViewById(R.id.takepicture);
        phone = findViewById(R.id.phone);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        togglepassword = findViewById(R.id.toggle);
        description = findViewById(R.id.years);
        name = findViewById(R.id.name);

    }

    private void makeToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }


}
