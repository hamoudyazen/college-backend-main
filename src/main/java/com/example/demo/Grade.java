package com.example.demo;

import java.util.Date;

public class Grade {
    private String id;
    private String studentEmail;
    private String courseId;
    private String courseName;
    private Date date;
    private int grade;

    public Grade() {
    }

    public Grade(String id, String studentEmail, String courseId, String courseName, int grade,Date date) {
        this.id = id;
        this.studentEmail = studentEmail;
        this.courseId = courseId;
        this.courseName = courseName;
        this.grade = grade;
        this.date = new Date();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
