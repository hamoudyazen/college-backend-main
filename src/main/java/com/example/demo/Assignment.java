package com.example.demo;

import com.google.cloud.Timestamp;

import java.util.List;

public class Assignment {

    private String name;
    private String description;
    private String course_id;
    private String material_link;
    private String assignment_id;
    private String deadline;
    private List<String> studentUploads;


    public Assignment() {
        // Default constructor required by Firebase
    }

    public Assignment( String id ,String name, String description, String course_id, String material_link, String assignment_id, String deadline , List<String> studentUploads ) {
        this.name = name;
        this.description = description;
        this.course_id = course_id;
        this.material_link = material_link;
        this.assignment_id = assignment_id;
        this.deadline = deadline;
        this.studentUploads = studentUploads;
    }

    public List<String> getStudentUploads() {
        return studentUploads;
    }

    public void setStudentUploads(List<String> studentUploads) {
        this.studentUploads = studentUploads;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getMaterial_link() {
        return material_link;
    }

    public void setMaterial_link(String material_link) {
        this.material_link = material_link;
    }

    public String getAssignment_id() {
        return assignment_id;
    }

    public void setAssignment_id(String assignment_id) {
        this.assignment_id = assignment_id;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
}
