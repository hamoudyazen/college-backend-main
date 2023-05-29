package com.example.demo;

import java.util.List;

public class Major {
    private String id;
    private String majorName;
    private Schedule schedule;
    private List<String> coursesList;

    public Major() {
    }

    public Major(String id, String majorName, Schedule schedule, List<String> coursesList) {
        this.id = id;
        this.majorName = majorName;
        this.schedule = schedule;
        this.coursesList = coursesList;
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


    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public List<String> getCoursesList() {
        return coursesList;
    }

    public void setCoursesList(List<String> coursesList) {
        this.coursesList = coursesList;
    }

    public static class Schedule {
        private List<CourseInsideMajor> sunday;
        private List<CourseInsideMajor> monday;
        private List<CourseInsideMajor> tuesday;
        private List<CourseInsideMajor> wednesday;
        private List<CourseInsideMajor> thursday;
        private List<CourseInsideMajor> friday;

        public Schedule() {
        }

        public List<CourseInsideMajor> getSunday() {
            return sunday;
        }

        public void setSunday(List<CourseInsideMajor> sunday) {
            this.sunday = sunday;
        }

        public List<CourseInsideMajor> getMonday() {
            return monday;
        }

        public void setMonday(List<CourseInsideMajor> monday) {
            this.monday = monday;
        }

        public List<CourseInsideMajor> getTuesday() {
            return tuesday;
        }

        public void setTuesday(List<CourseInsideMajor> tuesday) {
            this.tuesday = tuesday;
        }

        public List<CourseInsideMajor> getWednesday() {
            return wednesday;
        }

        public void setWednesday(List<CourseInsideMajor> wednesday) {
            this.wednesday = wednesday;
        }

        public List<CourseInsideMajor> getThursday() {
            return thursday;
        }

        public void setThursday(List<CourseInsideMajor> thursday) {
            this.thursday = thursday;
        }

        public List<CourseInsideMajor> getFriday() {
            return friday;
        }

        public void setFriday(List<CourseInsideMajor> friday) {
            this.friday = friday;
        }
    }

    public static class CourseInsideMajor {
        private String courseId;
        private String timeSlot;
        private String title;
        private String description;
        private String room;

        public CourseInsideMajor() {
        }

        public CourseInsideMajor(String courseId, String timeSlot , String title, String description, String room) {
            this.courseId = courseId;
            this.timeSlot = timeSlot;
            this.title=title;
            this.description=description;
            this.room=room;

        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCourseId() {
            return courseId;
        }

        public void setCourseId(String courseId) {
            this.courseId = courseId;
        }

        public String getTimeSlot() {
            return timeSlot;
        }

        public void setTimeSlot(String timeSlot) {
            this.timeSlot = timeSlot;
        }

        public String getRoom() {
            return room;
        }

        public void setRoom(String room) {
            this.room = room;
        }
    }

}
