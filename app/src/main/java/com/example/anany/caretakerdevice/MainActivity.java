package com.example.anany.caretakerdevice;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText username, password;
    ImageView toggle;
    Boolean passwordshowing = false;
    Button login;
    int numoftries = 0;
    TextView mainerror;
    Boolean dismiss = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseDatabase firebaseDatabase1 = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference1 = firebaseDatabase1.getReference("/Logged In");
        ValueEventListener check = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> loggedin = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    loggedin.add(child.child("State").getValue(String.class));
                }
                if (loggedin.isEmpty()) {
                    bindViews();
                    listeners();
                } else {
                    String finalstate = loggedin.get(loggedin.size() - 1);
                    if (finalstate.equals("true")) {
                        startActivity(new Intent(MainActivity.this, CaretakerAccount.class));
                    } else {
                        bindViews();
                        listeners();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference1.addValueEventListener(check);

        bindViews();
        listeners();

    }

    private void listeners() {

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference("/Create Account");

                ValueEventListener changeListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final ArrayList<String> usernames;
                        final ArrayList<String> passwords;
                        final ArrayList<String> names = new ArrayList<>();
                        usernames = new ArrayList<>();
                        passwords = new ArrayList<>();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            String getusername = child.child("Username").getValue(String.class);
                            usernames.add(getusername);
                            final String getpassword = child.child("Password").getValue(String.class);
                            passwords.add(getpassword);
                            names.add(child.child("Name").getValue(String.class));

                        }
                        String username1 = username.getText().toString();
                        String password1 = password.getText().toString();
                        if (numoftries < 3) {
                            if (username1.isEmpty() || password1.isEmpty()) {
                                mainerror.setText("You have not filled in both the username and password.");
                                numoftries = numoftries;
                            } else {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference dbref = database.getReference("Login/Visitor Login");

                                int j = 0;
                                String[] array = new String[usernames.size()];
                                for (int i = 0; i < usernames.size(); i++) {
                                    array[i] = usernames.get(i);
                                }

                                Boolean b = false;
                                for (int i = 0; i < usernames.size(); i++) {
                                    // makeToast(Integer.toString(j));
                                    if (array[i].equals(username1)) {
                                        b = true;
                                        break;
                                    } else {
                                        j++;
                                        b = false;
                                    }
                                }
                                if (b == false) {
                                    numoftries++;
                                    if ((3 - numoftries) == 1) {
                                        mainerror.setText("You have entered the wrong username and password. You have " + (3 - numoftries) + " try left.");

                                    } else if ((3 - numoftries) == 0) {
                                        mainerror.setText("Login has been disabled for 15 seconds due to 3 unsuccessful attempts.");
                                        numoftries++;
                                        disableLogin();
                                    } else {
                                        mainerror.setText("You have entered the wrong username and password. You have " + (3 - numoftries) + " tries left.");
                                    }
                                } else {
                                    if (!passwords.get(j).equals(password1)) {
                                        numoftries++;
                                        if ((3 - numoftries) == 1) {
                                            mainerror.setText("You have entered the wrong username and password. You have " + (3 - numoftries) + " try left.");

                                        } else if ((3 - numoftries) == 0) {
                                            mainerror.setText("Login has been disabled.");
                                            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                                                    .setAction("Action", null).show();
                                            numoftries++;
                                            disableLogin();
                                        } else {
                                            mainerror.setText("You have entered the wrong username and password. You have " + (3 - numoftries) + " tries left.");
                                        }
                                    } else {
                                        mainerror.setText("");
                                        numoftries = 0;
                                        mainerror.setText("");
                                        numoftries = 0;
                                        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                                        Map<String, String> values = new HashMap<>();
                                        values.put("Time", currentDateTimeString);
                                        values.put("Password", password1);
                                        values.put("Username", username1);
                                        values.put("Name", names.get(j));
                                        values.put("Device", "Caretaker Device");
                                        values.put("Phone", "8482482353");

                                        if (names.get(j).contains("Caretaker ")) {
                                            FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                                            DatabaseReference dbref1 = database.getReference("Logged In");

                                            Map<String, String> state = new HashMap<>();
                                            state.put("State", "true");
                                            dbref1.push().setValue(state, new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                                                }
                                            });

                                            dbref.push().setValue(values, new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                                    if (databaseError == null) {
                                                        makeToast("Successful Login");
                                                        startActivity(new Intent(MainActivity.this, CaretakerAccount.class));

                                                    } else {
                                                        makeToast("Error with logging in.");
                                                    }
                                                }
                                            });


                                        } else {
                                            FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                                            DatabaseReference dbref1 = database.getReference("Logged In");

                                            Map<String, String> state = new HashMap<>();
                                            state.put("State", "false");
                                            dbref1.push().setValue(state, new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                                                }
                                            });

                                            dbref.push().setValue(values, new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                                    if (databaseError == null) {
                                                        makeToast("Logging In...");
                                                        makeAlertBuilder();
                                                    } else {
                                                        makeToast("Error with logging in.");
                                                    }
                                                }
                                            });
                                        }


                                    }
                                }
                            }


                      /*  Query query = FirebaseDatabase.getInstance().getReference("/Login/Visitor Login").orderByChild("username");
                        query.addListenerForSingleValueEvent(valueEventListener);*/
                    /*    for (DataSnapshot child : dataSnapshot.getChildren()) {
                            makeToast(child.getKey());
                            makeToast(child.getValue(String.class));
                        }*/
                        } else {
                            makeToast("Login has been disabled.");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        makeToast("Error. Possible error due to WiFi fluctuation.");
                    }
                };
                databaseReference.addValueEventListener(changeListener);

            }
        });

        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (passwordshowing == false) {

                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    toggle.setImageResource(R.drawable.hidepassword);
                    passwordshowing = true;
                    password.setSelection(password.getText().toString().length());
                } else if (passwordshowing == true) {
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    toggle.setImageResource(R.drawable.showpassword);
                    passwordshowing = false;
                    password.setSelection(password.getText().toString().length());
                }
            }
        });
    }

    private void disableLogin() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                numoftries = 0;
                mainerror.setText("No longer disabled. You may try to login again.");
                listeners();
            }
        };

        Handler h = new Handler();
        h.postDelayed(r, 15000); //CHANGE TO 15 sECONDS ONCE EVERYTHING IS SUCCESSFUL

    }

    private void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void makeAlertBuilder() {
        final android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
        builder1.setTitle("Successful Sign In!");
        builder1.setMessage("Do you want to edit your account info?\n(Automatically closes in 10 seconds)");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        dismiss = true;
                    }
                });

        builder1.setNegativeButton(
                "Yes, edit info.",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss = true;
                        startActivity(new Intent(MainActivity.this, EditAccount.class));
                    }
                });

        final android.support.v7.app.AlertDialog alert11 = builder1.create();
        alert11.show();
        runit(alert11);


    }

    private void runit(final AlertDialog a) {
        new CountDownTimer(11000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                a.setMessage("Do you want to edit your account info?\n(Automatically closes in " + (millisUntilFinished / 1000) + " seconds)");
            }

            @Override
            public void onFinish() {
                a.cancel();
            }
        }.start();
    }

    private void bindViews() {
        username = findViewById(R.id.useredit);
        password = findViewById(R.id.passedit);
        toggle = findViewById(R.id.toggle);
        login = findViewById(R.id.signin);
        mainerror = findViewById(R.id.errortext);

    }


}
