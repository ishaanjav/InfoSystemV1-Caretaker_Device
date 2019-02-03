package com.example.anany.caretakerdevice;

public class Events {
    private String classify;
    private String name;
    private String time;
    private String phone;
    private String email;
    private String points;
    private String relation;


    public Events(String classify, String name, String time, String phone, String email, String points, String relation) {
        this.classify = classify;
        this.name = name;
        this.time = time;
        this.phone = phone;
        this.email = email;
        this.points = points;
        this.relation = relation;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClassify() {
        return classify;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }
}
