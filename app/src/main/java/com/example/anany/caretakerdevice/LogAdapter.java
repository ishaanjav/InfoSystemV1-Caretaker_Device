package com.example.anany.caretakerdevice;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LogAdapter extends ArrayAdapter<Events> implements GestureDetector.OnDoubleTapListener {

    private Context mContext;
    Activity activity;
    private List<Events> accountsList = new ArrayList<>();

    String rowid = "";
    StorageReference mImageRef;

    public LogAdapter(Activity a, Context context, ArrayList<Events> list) {
        super(context, 0, list);
        mContext = context;
        activity = a;
        accountsList = list;
    }

    int CLICK_ACTION_THRESHOLD = 200;
    private long lastTouchDown;
    private static int CLICK_ACTION_THRESHHOLD = 200;


    float startX = 0;
    float startY = 0;

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.eventlogrow, parent, false);

       /* listItem.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {

            }
        })*/


        final Events events = accountsList.get(position);
        String name = events.getName();
        String classifier = events.getClassify();
        String phone = events.getPhone();
        String email = events.getEmail();
        String time = events.getTime();

        RelativeLayout relativeLayout = listItem.findViewById(R.id.rel);
        TextView stime = listItem.findViewById(R.id.time);
        TextView sname = listItem.findViewById(R.id.name);
        TextView semail = listItem.findViewById(R.id.email);
        TextView sphone = listItem.findViewById(R.id.phone);


        listItem.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

            }
        });

        listItem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastTouchDown = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (System.currentTimeMillis() - lastTouchDown < CLICK_ACTION_THRESHHOLD) {
                            makeToast(events.getRelation());
                        }
                        break;
                }
                return true;

                //  onDoubleTap(event);
               /* }
                if (event.getAction() == MotionEvent.ACTION_HOVER_ENTER) {
                    makeToast(events.getRelation());

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //  makeToast(events.getRelation());

                } else if (event.getAction() == MotionEvent.ACTION_SCROLL) {
                    //   makeToast(events.getRelation());

                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    // makeToast(events.getRelation());

                } else if (event.getAction() == MotionEvent.AXIS_SCROLL) {
                    //   makeToast(events.getRelation());

                } else if (event.getAction() == MotionEvent.AXIS_VSCROLL) {
                    //    makeToast(events.getRelation());

                } else {

                }*/

                //  return false;
            }
        });

        if (time == null)

        {

        } else

        {
            String[] broken = time.split(" ");
            String line1 = "", line2 = "", line3 = "";
            Date now = new Date();
            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            String[] currentbroken = currentDateTimeString.split(" ");
            SimpleDateFormat simpleDateformat = new SimpleDateFormat("E"); // the day of the week abbreviated

            int origmonth = 0;
            int currentmonth = 0;
            try {
                String[] b1 = broken[1].split(",");
                String[] b2 = currentbroken[1].split(",");
                origmonth = Integer.parseInt(b1[0]);
                currentmonth = Integer.parseInt(b2[0]);
            } catch (Exception e) {

            }


            if (currentbroken[0].equals(broken[0]) && currentbroken[1].equals(broken[1]) && currentbroken[2].equals(broken[2])) {
                for (int i = 0; i < 3; i++) {
                    line1 += broken[i] + " ";
                }

                for (int i = 3; i < broken.length; i++) {
                    line2 += broken[i] + " ";
                }
                stime.setText("Today,\n" + line1 + "\n" + line2);

            } else if (currentbroken[0].equals(broken[0]) && (currentmonth - origmonth) == 1 && currentbroken[2].equals(broken[2])) {
                for (int i = 0; i < 3; i++) {
                    line1 += broken[i] + " ";
                }

                for (int i = 3; i < broken.length; i++) {
                    line2 += broken[i] + " ";
                }
                stime.setText("Yesterday,\n" + line1 + "\n" + line2);
            } else {
                // makeToast(simpleDateformat.format(now));
                //Oct 14, 2018 8:25:15 PM"

                int monthnum = abbreviationToNum(broken[0]);
                String[] b1 = broken[1].split(",");
                LocalDate localDate = LocalDate.of(Integer.parseInt(broken[2]), monthnum, Integer.parseInt(b1[0]));

                //Getting the day of week for a given date
                java.time.DayOfWeek dayOfWeek = localDate.getDayOfWeek();
                // makeToast(dayOfWeek.toString());
                for (int i = 0; i < 3; i++) {
                    line1 += broken[i] + " ";
                }

                for (int i = 3; i < broken.length; i++) {
                    line2 += broken[i] + " ";
                }

                stime.setText(dayOfWeek.toString() + ",\n" + line1 + "\n" + line2);

            }


        }

        if (email == null || email.isEmpty())

        {
            semail.setText("No email");
        } else

        {
            semail.setText(email);
        }

        if (phone == null || phone.isEmpty())

        {
            sphone.setText("No phone number");
        } else

        {
            char ch = phone.charAt(0);
            if (Character.isDigit(ch)) {
                sphone.setText("(" + phone.substring(0, 3) + ") - " + phone.substring(3, 6) + " - " + phone.substring(6, 10));
            } else {
                sphone.setText(phone);
            }
        }

        if (name.contains("Caretaker"))

        {
            sname.setText("You");
            sphone.setText("(848) - 248 - 2353");

        } else

        {
            sname.setText(name);
        }

        //  stime.setTextColor(activity.getResources().getColor(R.color.cursorcolor));

        TextView description = listItem.findViewById(R.id.description);

        if (classifier.equals("Visitor"))

        {
            description.setText("Visitor Login");
            description.setTextColor(activity.getResources().getColor(R.color.lo25));
            sname.setTextColor(activity.getResources().getColor(R.color.menubar));
            relativeLayout.setBackgroundColor(activity.getResources().getColor(R.color.faintgreen));

            // sphone.setTextColor(activity.getResources().getColor(R.color.green));
            // semail.setTextColor(activity.getResources().getColor(R.color.green));

        } else if (classifier.equals("Pending"))

        {
            description.setText("Pending");
            sname.setTextColor(activity.getResources().getColor(R.color.mainbar));
            description.setTextColor(activity.getResources().getColor(R.color.darkblue));
            relativeLayout.setBackgroundColor(activity.getResources().getColor(R.color.faintblue));
            //   sphone.setTextColor(activity.getResources().getColor(R.color.container));
            // semail.setTextColor(activity.getResources().getColor(R.color.container));

        } else if (classifier.equals("Worker"))

        {
            description.setText("Worker Login");
            sname.setTextColor(activity.getResources().getColor(R.color.purple));
            description.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
            relativeLayout.setBackgroundColor(activity.getResources().getColor(R.color.faintpurple));

            // sphone.setTextColor(activity.getResources().getColor(R.color.purplelight));
            // semail.setTextColor(activity.getResources().getColor(R.color.purplelight));

        } else if (classifier.equals("Approved"))

        {
            description.setText("Approved");
            description.setTextColor(activity.getResources().getColor(R.color.darkred));
            relativeLayout.setBackgroundColor(activity.getResources().getColor(R.color.faintorange));

            // sphone.setTextColor(activity.getResources().getColor(R.color.lightred3));
            // semail.setTextColor(activity.getResources().getColor(R.color.lightred3));
            sname.setTextColor(activity.getResources().getColor(R.color.lightred));
        } else if (classifier.equals("Declined"))

        {
            description.setText("Declined");
            description.setTextColor(activity.getResources().getColor(R.color.congratulationsdark));
            relativeLayout.setBackgroundColor(activity.getResources().getColor(R.color.faintred));

            // sphone.setTextColor(activity.getResources().getColor(R.color.lightred3));
            // semail.setTextColor(activity.getResources().getColor(R.color.lightred3));
            sname.setTextColor(activity.getResources().getColor(R.color.congratulationslight));
        } else if (classifier.equals("Deleted"))

        {
            description.setText("Deleted");
            description.setTextColor(activity.getResources().getColor(R.color.darkyellow));
            relativeLayout.setBackgroundColor(activity.getResources().getColor(R.color.faintyellow));

            // sphone.setTextColor(activity.getResources().getColor(R.color.lightred3));
            // semail.setTextColor(activity.getResources().getColor(R.color.lightred3));
            sname.setTextColor(activity.getResources().getColor(R.color.yellow));
        } else if (classifier.equals("Updated"))

        {
            description.setText("Updated");
            description.setTextColor(activity.getResources().getColor(R.color.pink));
            relativeLayout.setBackgroundColor(activity.getResources().getColor(R.color.lightpink));

            // sphone.setTextColor(activity.getResources().getColor(R.color.lightred3));
            // semail.setTextColor(activity.getResources().getColor(R.color.lightred3));
            sname.setTextColor(activity.getResources().getColor(R.color.mediumpink));
        } else if (classifier.equals("Failed"))

        {
            description.setText("Failed Login");
            description.setTextColor(activity.getResources().getColor(R.color.darkgrey));
            relativeLayout.setBackgroundColor(activity.getResources().getColor(R.color.faintgrey));

            // sphone.setTextColor(activity.getResources().getColor(R.color.lightred3));
            // semail.setTextColor(activity.getResources().getColor(R.color.lightred3));
            sname.setTextColor(activity.getResources().getColor(R.color.grey));
            sname.setText("Username:\n" + events.getName());
        } else if (classifier.equals("Visitor Logout"))

        {
            description.setText("Visitor Logout");
            description.setTextColor(activity.getResources().getColor(R.color.darkbrown));
            relativeLayout.setBackgroundColor(activity.getResources().getColor(R.color.lightbrown));

            sname.setTextColor(activity.getResources().getColor(R.color.brown));
            //   sname.setText("Username:\n" + events.getName());
        } else if (classifier.equals("Worker Logout"))

        {
            description.setText("Worker Logout");
            description.setTextColor(activity.getResources().getColor(R.color.brown));
            relativeLayout.setBackgroundColor(activity.getResources().getColor(R.color.lightbrown));

            sname.setTextColor(activity.getResources().getColor(R.color.darkbrown));
            //   sname.setText("Username:\n" + events.getName());
        }

        //    semail.setText(events.getPoints());
        return listItem;
    }

    private int abbreviationToNum(String s) {
        if (s.equals("Jan")) {
            return 1;
        } else if (s.equals("Feb")) {
            return 2;

        } else if (s.equals("Mar")) {
            return 3;

        } else if (s.equals("Apr")) {
            return 4;

        } else if (s.equals("May")) {
            return 5;

        } else if (s.equals("Jun")) {
            return 6;

        } else if (s.equals("Jul")) {
            return 7;

        } else if (s.equals("Aug")) {
            return 8;

        } else if (s.equals("Sep")) {
            return 9;

        } else if (s.equals("Oct")) {
            return 10;

        } else if (s.equals("Nov")) {
            return 11;

        } else if (s.equals("Dec")) {
            return 12;

        }
        return 0;

    }

    private boolean isAClick(float startX, float endX, float startY, float endY) {
        float differenceX = Math.abs(startX - endX);
        float differenceY = Math.abs(startY - endY);
        return !(differenceX > CLICK_ACTION_THRESHOLD/* =5 */ || differenceY > CLICK_ACTION_THRESHOLD);
    }

    private void makeToast(String s) {
        Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        makeToast("DOuble");
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }
}
