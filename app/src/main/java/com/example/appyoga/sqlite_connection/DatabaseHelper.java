package com.example.appyoga.sqlite_connection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.appyoga.model.Booking;
import com.example.appyoga.model.YogaClass;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Tên và phiên bản cơ sở dữ liệu
    private static final String DATABASE_NAME = "yoga_app.db";
    private static final int DATABASE_VERSION = 1;

    // Các bảng trong cơ sở dữ liệu
    public static final String TABLE_USERS = "Users";
    public static final String TABLE_YOGA_CLASSES = "YogaClasses";
    public static final String TABLE_CLASS_INSTANCES = "ClassInstances";
    public static final String TABLE_BOOKINGS = "Bookings";

    // Các cột trong bảng Users
    public static final String USER_ID = "id";
    public static final String USER_NAME = "name";
    public static final String USER_EMAIL = "email";
    public static final String USER_PASSWORD = "password";
    public static final String USER_ROLE = "role";

    // Các cột trong bảng YogaClasses
    public static final String CLASS_ID = "id";
    public static final String CLASS_NAME = "name";
    public static final String CLASS_DAY_OF_WEEK = "day_of_week";
    public static final String CLASS_TIME = "time";
    public static final String CLASS_CAPACITY = "capacity";
    public static final String CLASS_DURATION = "duration";
    public static final String CLASS_PRICE = "price";
    public static final String CLASS_TYPE = "type";
    public static final String CLASS_DESCRIPTION = "description";

    // Các cột trong bảng ClassInstances
    public static final String INSTANCE_ID = "id";
    public static final String INSTANCE_YOGA_CLASS_ID = "yoga_class_id";
    public static final String INSTANCE_DATE = "date";
    public static final String INSTANCE_TEACHER = "teacher";
    public static final String INSTANCE_COMMENTS = "comments";

    // Các cột trong bảng Bookings
    public static final String BOOKING_ID = "id";
    public static final String BOOKING_USER_ID = "user_id";
    public static final String BOOKING_CLASS_INSTANCE_ID = "class_instance_id";
    public static final String BOOKING_DATE = "booking_date";
    public static final String BOOKING_STATUS = "status";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng Users
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USER_NAME + " TEXT, " +
                USER_EMAIL + " TEXT UNIQUE, " +
                USER_PASSWORD + " TEXT, " +
                USER_ROLE + " TEXT);";
        db.execSQL(createUsersTable);

        // Tạo bảng YogaClasses
        String createYogaClassesTable = "CREATE TABLE " + TABLE_YOGA_CLASSES + " (" +
                CLASS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CLASS_NAME + " TEXT, " +
                CLASS_DAY_OF_WEEK + " TEXT, " +
                CLASS_TIME + " TEXT, " +
                CLASS_CAPACITY + " INTEGER, " +
                CLASS_DURATION + " INTEGER, " +
                CLASS_PRICE + " REAL, " +
                CLASS_TYPE + " TEXT, " +
                CLASS_DESCRIPTION + " TEXT);";
        db.execSQL(createYogaClassesTable);

        // Tạo bảng ClassInstances
        String createClassInstancesTable = "CREATE TABLE " + TABLE_CLASS_INSTANCES + " (" +
                INSTANCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                INSTANCE_YOGA_CLASS_ID + " INTEGER, " +
                INSTANCE_DATE + " TEXT, " +
                INSTANCE_TEACHER + " TEXT, " +
                INSTANCE_COMMENTS + " TEXT, " +
                "FOREIGN KEY(" + INSTANCE_YOGA_CLASS_ID + ") REFERENCES " + TABLE_YOGA_CLASSES + "(" + CLASS_ID + "));";
        db.execSQL(createClassInstancesTable);

        // Tạo bảng Bookings
        String createBookingsTable = "CREATE TABLE " + TABLE_BOOKINGS + " (" +
                BOOKING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BOOKING_USER_ID + " INTEGER, " +
                BOOKING_CLASS_INSTANCE_ID + " INTEGER, " +
                BOOKING_DATE + " TEXT, " +
                BOOKING_STATUS + " TEXT, " +
                "FOREIGN KEY(" + BOOKING_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + USER_ID + "), " +
                "FOREIGN KEY(" + BOOKING_CLASS_INSTANCE_ID + ") REFERENCES " + TABLE_CLASS_INSTANCES + "(" + INSTANCE_ID + "));";
        db.execSQL(createBookingsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa bảng cũ và tạo lại bảng mới
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_YOGA_CLASSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLASS_INSTANCES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);
        onCreate(db);
    }

    // Các phương thức để thêm, sửa, xóa dữ liệu trong SQLite có thể được triển khai ở đây

    //CRUD Yoga Class

    // Hiển thị YogaClass
    public List<YogaClass> getAllYogaClasses() {
        List<YogaClass> yogaClasses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_YOGA_CLASSES;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                YogaClass yogaClass = new YogaClass(
                        cursor.getString(cursor.getColumnIndexOrThrow(CLASS_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(CLASS_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(CLASS_DAY_OF_WEEK)),
                        cursor.getString(cursor.getColumnIndexOrThrow(CLASS_TIME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(CLASS_CAPACITY)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(CLASS_DURATION)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(CLASS_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(CLASS_TYPE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(CLASS_DESCRIPTION))
                );
                yogaClasses.add(yogaClass);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return yogaClasses;
    }

    // Thêm YogaClass
    public long addYogaClass(YogaClass yogaClass) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CLASS_NAME, yogaClass.getName());
        values.put(CLASS_DAY_OF_WEEK, yogaClass.getDayOfWeek());
        values.put(CLASS_TIME, yogaClass.getTime());
        values.put(CLASS_CAPACITY, yogaClass.getCapacity());
        values.put(CLASS_DURATION, yogaClass.getDuration());
        values.put(CLASS_PRICE, yogaClass.getPrice());
        values.put(CLASS_TYPE, yogaClass.getType());
        values.put(CLASS_DESCRIPTION, yogaClass.getDescription());

        long id = db.insert(TABLE_YOGA_CLASSES, null, values);
        db.close();
        return id;
    }

    // Xóa YogaClass
    public int deleteYogaClass(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_YOGA_CLASSES, CLASS_ID + " = ?", new String[]{id});
        db.close();
        return rows;
    }

    // Sửa YogaClass
    public int updateYogaClass(YogaClass yogaClass) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CLASS_NAME, yogaClass.getName());
        values.put(CLASS_DAY_OF_WEEK, yogaClass.getDayOfWeek());
        values.put(CLASS_TIME, yogaClass.getTime());
        values.put(CLASS_CAPACITY, yogaClass.getCapacity());
        values.put(CLASS_DURATION, yogaClass.getDuration());
        values.put(CLASS_PRICE, yogaClass.getPrice());
        values.put(CLASS_TYPE, yogaClass.getType());
        values.put(CLASS_DESCRIPTION, yogaClass.getDescription());

        int rows = db.update(TABLE_YOGA_CLASSES, values, CLASS_ID + " = ?", new String[]{yogaClass.getId()});
        db.close();
        return rows;
    }

    //CRUD Booking

    // Thêm Booking
    public long addBooking(Booking booking) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BOOKING_USER_ID, booking.getUserId());
        values.put(BOOKING_CLASS_INSTANCE_ID, booking.getClassInstanceId());
        values.put(BOOKING_DATE, booking.getBookingDate());
        values.put(BOOKING_STATUS, booking.getStatus());

        long id = db.insert(TABLE_BOOKINGS, null, values);
        db.close();
        return id;
    }

    // Xóa Booking
    public int deleteBooking(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_BOOKINGS, BOOKING_ID + " = ?", new String[]{id});
        db.close();
        return rows;
    }

    // Sửa Booking
    public int updateBooking(Booking booking) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BOOKING_USER_ID, booking.getUserId());
        values.put(BOOKING_CLASS_INSTANCE_ID, booking.getClassInstanceId());
        values.put(BOOKING_DATE, booking.getBookingDate());
        values.put(BOOKING_STATUS, booking.getStatus());

        int rows = db.update(TABLE_BOOKINGS, values, BOOKING_ID + " = ?", new String[]{booking.getId()});
        db.close();
        return rows;
    }


}

