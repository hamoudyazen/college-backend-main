package com.example.demo;

import java.util.Arrays;

public class PublicSchedule {
    private String id;
    private String majorName;
    private String sunday;
    private String monday;
    private String tuesday;
    private String wednesday;
    private String thursday;
    private String friday;

    public PublicSchedule() {}

    public PublicSchedule(String id, String majorName, CourseSchudle[] sunday, CourseSchudle[] monday, CourseSchudle[] tuesday, CourseSchudle[] wednesday, CourseSchudle[] thursday, CourseSchudle[] friday) {
        this.id = id;
        this.majorName = majorName;
        this.sunday = Arrays.toString(sunday);
        this.monday = Arrays.toString(monday);
        this.tuesday = Arrays.toString(tuesday);
        this.wednesday = Arrays.toString(wednesday);
        this.thursday = Arrays.toString(thursday);
        this.friday = Arrays.toString(friday);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    public String getSunday() {
        return sunday;
    }

    public void setSunday(String sunday) {
        this.sunday = sunday;
    }

    public String getMonday() {
        return monday;
    }

    public void setMonday(String monday) {
        this.monday = monday;
    }

    public String getTuesday() {
        return tuesday;
    }

    public void setTuesday(String tuesday) {
        this.tuesday = tuesday;
    }

    public String getWednesday() {
        return wednesday;
    }

    public void setWednesday(String wednesday) {
        this.wednesday = wednesday;
    }

    public String getThursday() {
        return thursday;
    }

    public void setThursday(String thursday) {
        this.thursday = thursday;
    }

    public String getFriday() {
        return friday;
    }

    public void setFriday(String friday) {
        this.friday = friday;
    }
}
