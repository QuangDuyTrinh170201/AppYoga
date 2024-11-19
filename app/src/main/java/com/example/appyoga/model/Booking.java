package com.example.appyoga.model;

public class Booking {
    private String id;
    private String userId;
    private String classInstanceId;
    private String bookingDate;
    private String status;

    // Constructor
    public Booking(String id, String userId, String classInstanceId, String bookingDate, String status) {
        this.id = id;
        this.userId = userId;
        this.classInstanceId = classInstanceId;
        this.bookingDate = bookingDate;
        this.status = status;
    }

    // Getters và Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getClassInstanceId() {
        return classInstanceId;
    }

    public void setClassInstanceId(String classInstanceId) {
        this.classInstanceId = classInstanceId;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

