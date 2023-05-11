package com.example.demo;

import java.util.List;

public class Course {

    private String id;
    private int capacity;
    private int credits;
    private String description;
    private int semesterHours;
    private String name;
    private String teacherID;
    private List<String> studentsArray;
    private String semester;

    private String major;

    public Course() {
        // Default constructor required by Firebase
    }

    public Course(String id, int capacity, int credits, String description, int semesterHours, String name, String teacherID, List<String> studentsArray , String semester , String major) {
        this.id = id;
        this.capacity = capacity;
        this.credits = credits;
        this.description = description;
        this.semesterHours = semesterHours;
        this.name = name;
        this.teacherID = teacherID;
        this.semester=semester;
        this.major=major;
        this.studentsArray = studentsArray;
    }


    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(String teacherID) {
        this.teacherID = teacherID;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getSemesterHours() {
        return semesterHours;
    }

    public void setSemesterHours(int semesterHours) {
        this.semesterHours = semesterHours;
    }

    public List<String> getStudentsArray() {
        return studentsArray;
    }

    public void setStudentsArray(List<String> studentsArray) {
        this.studentsArray = studentsArray;
    }
}
