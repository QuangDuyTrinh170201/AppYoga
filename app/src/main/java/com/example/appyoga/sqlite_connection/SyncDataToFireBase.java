package com.example.appyoga.sqlite_connection;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.appyoga.model.Booking;
import com.example.appyoga.model.ClassInstance;
import com.example.appyoga.model.User;
import com.example.appyoga.model.YogaClass;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SyncDataToFireBase {
    private DatabaseHelper dbHelper;
    private DatabaseReference database;

    public SyncDataToFireBase(Context context) {
        dbHelper = new DatabaseHelper(context);
        database = FirebaseDatabase.getInstance().getReference();
    }

    public void syncUsers() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_USERS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_ID));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_NAME));
                @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_EMAIL));
                @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_PASSWORD));
                @SuppressLint("Range") String role = cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_ROLE));

                // Tạo đối tượng User
                User user = new User(id, name, email, password, role);

                // Lưu User lên Firebase
                database.child("users").child(id).setValue(user);

            } while (cursor.moveToNext());
        }

        cursor.close();
    }

    public void syncYogaClasses() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_YOGA_CLASSES, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CLASS_ID));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CLASS_NAME));
                @SuppressLint("Range") String dayOfWeek = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CLASS_DAY_OF_WEEK));
                @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CLASS_TIME));
                @SuppressLint("Range") int capacity = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.CLASS_CAPACITY));
                @SuppressLint("Range") int duration = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.CLASS_DURATION));
                @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.CLASS_PRICE));
                @SuppressLint("Range") String type = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CLASS_TYPE));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CLASS_DESCRIPTION));

                // Tạo đối tượng YogaClass
                YogaClass yogaClass = new YogaClass(id, name, dayOfWeek, time, capacity, duration, price, type, description);

                // Lưu YogaClass lên Firebase
                database.child("yoga_classes").child(id).setValue(yogaClass);

            } while (cursor.moveToNext());
        }

        cursor.close();
    }

    public void syncClassInstances() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_CLASS_INSTANCES, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(DatabaseHelper.INSTANCE_ID));
                @SuppressLint("Range") String yogaClassId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.INSTANCE_YOGA_CLASS_ID));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(DatabaseHelper.INSTANCE_DATE));
                @SuppressLint("Range") String teacher = cursor.getString(cursor.getColumnIndex(DatabaseHelper.INSTANCE_TEACHER));
                @SuppressLint("Range") String comments = cursor.getString(cursor.getColumnIndex(DatabaseHelper.INSTANCE_COMMENTS));

                // Tạo đối tượng ClassInstance
                ClassInstance instance = new ClassInstance(id, yogaClassId, date, teacher, comments);

                // Lưu ClassInstance lên Firebase
                database.child("class_instances").child(id).setValue(instance);

            } while (cursor.moveToNext());
        }

        cursor.close();
    }

    public void syncBookings() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_BOOKINGS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(DatabaseHelper.BOOKING_ID));
                @SuppressLint("Range") String userId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.BOOKING_USER_ID));
                @SuppressLint("Range") String classInstanceId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.BOOKING_CLASS_INSTANCE_ID));
                @SuppressLint("Range") String bookingDate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.BOOKING_DATE));
                @SuppressLint("Range") String status = cursor.getString(cursor.getColumnIndex(DatabaseHelper.BOOKING_STATUS));

                // Tạo đối tượng Booking
                Booking booking = new Booking(id, userId, classInstanceId, bookingDate, status);

                // Lưu Booking lên Firebase
                database.child("bookings").child(id).setValue(booking);

            } while (cursor.moveToNext());
        }

        cursor.close();
    }
}
