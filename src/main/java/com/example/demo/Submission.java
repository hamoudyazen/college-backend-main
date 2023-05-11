package com.example.demo;

public class Submission {
    private String submission_id;
    private String submission_description;
    private String submission_assignment_id;
    private String submission_student_id;
    private String answerURL;

    public Submission() {
    }

    public Submission(String submission_id, String submission_description, String submission_student_id, String submission_assignment_id, String answerURL) {
        this.submission_id = submission_id;
        this.submission_description = submission_description;
        this.submission_assignment_id = submission_assignment_id;
        this.answerURL = answerURL;
        this.submission_student_id = submission_student_id;
    }

    public String getSubmission_student_id() {
        return submission_student_id;
    }

    public void setSubmission_student_id(String submission_student_id) {
        this.submission_student_id = submission_student_id;
    }

    public String getSubmission_id() {
        return submission_id;
    }

    public void setSubmission_id(String submission_id) {
        this.submission_id = submission_id;
    }

    public String getSubmission_description() {
        return submission_description;
    }

    public void setSubmission_description(String submission_description) {
        this.submission_description = submission_description;
    }



    public String getSubmission_assignment_id() {
        return submission_assignment_id;
    }

    public void setSubmission_assignment_id(String submission_assignment_id) {
        this.submission_assignment_id = submission_assignment_id;
    }

    public String getAnswerURL() {
        return answerURL;
    }

    public void setAnswerURL(String answerURL) {
        this.answerURL = answerURL;
    }
}