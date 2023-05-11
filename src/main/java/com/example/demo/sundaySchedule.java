package com.example.demo;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.google.cloud.firestore.QuerySnapshot;

public class sundaySchedule {
    private String id;
    private String teacherId;
    private String scheduleMajor;
    private List<String> sundayArray;

    public sundaySchedule() {
    }

    public sundaySchedule(String id, String teacherId, String courserId, String scheduleMajor, List<String>  sundayArray) {
        this.id = id;
        this.teacherId = teacherId;
        this.scheduleMajor = scheduleMajor;
        this.sundayArray = sundayArray;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }



    public String getScheduleMajor() {
        return scheduleMajor;
    }

    public void setScheduleMajor(String scheduleMajor) {
        this.scheduleMajor = scheduleMajor;
    }

    public List<String> getSundayArray() {
        return sundayArray;
    }

    public void setSundayArray(List<String> sundayArray) {
        this.sundayArray = sundayArray;
    }
}