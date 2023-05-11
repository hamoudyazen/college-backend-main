package com.example.demo;

public class CourseMaterial {
    private String id;
    private String materialCourseLink;
    private String materialCourseId;
    private String materialCourseName;
    private String materialCourseDescription;

    public CourseMaterial() {
    }

    public CourseMaterial(String id, String materialCourseLink, String materialCourseId, String materialCourseName, String materialCourseDescription) {
        this.id = id;
        this.materialCourseLink = materialCourseLink;
        this.materialCourseId = materialCourseId;
        this.materialCourseName = materialCourseName;
        this.materialCourseDescription = materialCourseDescription;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaterialCourseLink() {
        return materialCourseLink;
    }

    public void setMaterialCourseLink(String materialCourseLink) {
        this.materialCourseLink = materialCourseLink;
    }

    public String getMaterialCourseId() {
        return materialCourseId;
    }

    public void setMaterialCourseId(String materialCourseId) {
        this.materialCourseId = materialCourseId;
    }

    public String getMaterialCourseName() {
        return materialCourseName;
    }

    public void setMaterialCourseName(String materialCourseName) {
        this.materialCourseName = materialCourseName;
    }

    public String getMaterialCourseDescription() {
        return materialCourseDescription;
    }

    public void setMaterialCourseDescription(String materialCourseDescription) {
        this.materialCourseDescription = materialCourseDescription;
    }
}