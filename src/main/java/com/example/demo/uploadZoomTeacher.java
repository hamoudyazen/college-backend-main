package com.example.demo;

public class uploadZoomTeacher {
    private String id;
    private String courseName;
    private String courseId;
    private String zoomLink;

    public uploadZoomTeacher() {
    }

    public uploadZoomTeacher(String id, String courseName, String courseId, String zoomLink) {
        this.id = id;
        this.courseName = courseName;
        this.courseId = courseId;
        this.zoomLink = zoomLink;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getZoomLink() {
        return zoomLink;
    }

    public void setZoomLink(String zoomLink) {
        this.zoomLink = zoomLink;
    }
}
