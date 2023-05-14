package com.example.demo;
public class CourseSchudle {
    private String courseName;
    private int courseHours;

    public CourseSchudle() {
    }

    public CourseSchudle(String courseName, int courseHours) {
        this.courseName = courseName;
        this.courseHours = courseHours;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCourseHours() {
        return courseHours;
    }

    public void setCourseHours(int courseHours) {
        this.courseHours = courseHours;
    }
}
