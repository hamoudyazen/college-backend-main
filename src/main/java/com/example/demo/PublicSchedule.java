package com.example.demo;

import java.util.ArrayList;
public class PublicSchedule {
    private String id;
    private String majorName;
    private CourseSchudle[] sunday;
    private CourseSchudle[] monday;
    private CourseSchudle[] tuesday;
    private CourseSchudle[] wednesday;
    private CourseSchudle[] thursday;
    private CourseSchudle[] friday;

    public PublicSchedule() {}

    public PublicSchedule(String id, String majorName, CourseSchudle[] sunday, CourseSchudle[] monday, CourseSchudle[] tuesday, CourseSchudle[] wednesday, CourseSchudle[] thursday, CourseSchudle[] friday) {
        this.id = id;
        this.majorName = majorName;
        this.sunday = sunday;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
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

    public CourseSchudle[] getSunday() {
        return sunday;
    }

    public void setSunday(CourseSchudle[] sunday) {
        this.sunday = sunday;
    }

    public CourseSchudle[] getMonday() {
        return monday;
    }

    public void setMonday(CourseSchudle[] monday) {
        this.monday = monday;
    }

    public CourseSchudle[] getTuesday() {
        return tuesday;
    }

    public void setTuesday(CourseSchudle[] tuesday) {
        this.tuesday = tuesday;
    }

    public CourseSchudle[] getWednesday() {
        return wednesday;
    }

    public void setWednesday(CourseSchudle[] wednesday) {
        this.wednesday = wednesday;
    }

    public CourseSchudle[] getThursday() {
        return thursday;
    }

    public void setThursday(CourseSchudle[] thursday) {
        this.thursday = thursday;
    }

    public CourseSchudle[] getFriday() {
        return friday;
    }

    public void setFriday(CourseSchudle[] friday) {
        this.friday = friday;
    }
}
