package com.example.anany.caretakerdevice;

import java.util.ArrayList;

public class EmotionsHolder {
    private String time;
    private int size;
    private ArrayList<String> arrayList;

    public EmotionsHolder(String time, int size, ArrayList<String> arrayList) {
        this.time = time;
        this.size = size;
        this.arrayList = arrayList;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public ArrayList<String> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<String> arrayList) {
        this.arrayList = arrayList;
    }
}
