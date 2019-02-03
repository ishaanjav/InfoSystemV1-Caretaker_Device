package com.example.anany.caretakerdevice;

public class Upload {
    private String name;
    private String imageuri;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageuri() {
        return imageuri;
    }

    public void setImageuri(String imageuri) {
        this.imageuri = imageuri;
    }


    public Upload() {

    }

    public Upload(String name, String imageurl) {
        this.name = name;
        imageuri = imageurl;


    }


}
