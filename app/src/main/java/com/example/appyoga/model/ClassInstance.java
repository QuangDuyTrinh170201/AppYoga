package com.example.appyoga.model;

public class ClassInstance {
    private String id;
    private String yogaClassId;
    private String date;
    private String teacher;
    private String comments;

    // Constructor
    public ClassInstance(String id, String yogaClassId, String date, String teacher, String comments) {
        this.id = id;
        this.yogaClassId = yogaClassId;
        this.date = date;
        this.teacher = teacher;
        this.comments = comments;
    }

    // Getters và Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getYogaClassId() {
        return yogaClassId;
    }

    public void setYogaClassId(String yogaClassId) {
        this.yogaClassId = yogaClassId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}

