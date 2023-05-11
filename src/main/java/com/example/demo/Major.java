package com.example.demo;

import java.util.List;

public class Major {
    private String id;
    private String majorName;
    private List<String> courses;

    public Major() {}

    public Major(String id, String majorName, List<String> courses) {
        this.id = id;
        this.majorName = majorName;
        this.courses = courses;
    }

    // Getters and setters
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

    public List<String> getCourses() {
        return courses;
    }

    public void setCourses(List<String> courses) {
        this.courses = courses;
    }
}
