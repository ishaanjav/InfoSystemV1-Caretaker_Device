package com.example.anany.caretakerdevice;

import android.app.Application;

public class MyApplication extends Application {
    public static String isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = "resumed";
    }

    public static void activityPaused() {
        activityVisible = "paused";
    }
    public static void activityStopped() {
        activityVisible = "stopped";
    }
    public static void activityDestroyed() {
        activityVisible = "destroyed";
    }


    private static String activityVisible;
}
