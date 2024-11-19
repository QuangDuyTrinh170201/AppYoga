package com.example.appyoga;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appyoga.model.YogaClass;
import com.example.appyoga.sqlite_connection.DatabaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

public class AddYogaActivity extends AppCompatActivity {

    EditText edtClassName, edtCapacity, edtPrice;
    Spinner spnDayOfWeek, spnType;
    ImageButton btnAddNew;

    Button btnPickDuration;
    TextView tvDuration;

    DatabaseHelper dbHelper;
    DatabaseReference firebaseDatabase;
    EditText etTimePicker;

    EditText edtDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_yoga);

        etTimePicker = findViewById(R.id.etTimePicker);
        edtClassName = findViewById(R.id.edtClassName);
        edtCapacity = findViewById(R.id.edtCapacity);
        edtPrice = findViewById(R.id.edtPrice);
        spnDayOfWeek = findViewById(R.id.spnDayOfWeek);
        spnType = findViewById(R.id.spnType);
        btnPickDuration = findViewById(R.id.btnPickDuration);
        tvDuration = findViewById(R.id.tvDuration);
        btnAddNew = findViewById(R.id.btnAddNew);
        edtDescription = findViewById(R.id.edtDescription);

        // Sự kiện nhấn vào EditText
        etTimePicker.setOnClickListener(v -> {
            // Lấy giờ phút hiện tại
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            // Hiển thị TimePickerDialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    (TimePicker view, int selectedHour, int selectedMinute) -> {
                        // Cập nhật EditText với thời gian đã chọn
                        String selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute);
                        etTimePicker.setText(selectedTime);
                    }, hour, minute, true); // true: 24 giờ, false: 12 giờ
            timePickerDialog.show();
        });


        btnPickDuration.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    (view, hourOfDay, minute) -> {
                        String duration = hourOfDay + " giờ " + minute + " phút";
                        tvDuration.setText("Thời lượng: " + duration);
                    }, 0, 0, true); // Mặc định từ 0:00
            timePickerDialog.show();
        });

        dbHelper = new DatabaseHelper(this);
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        // Xử lý sự kiện khi nhấn nút "Add New"
        btnAddNew.setOnClickListener(v -> {
            addYogaClass();
        });
    }

    private void addYogaClass() {
        // Lấy dữ liệu từ các trường nhập liệu
        String className = edtClassName.getText().toString().trim();
        String capacityStr = edtCapacity.getText().toString().trim();
        String priceStr = edtPrice.getText().toString().trim();
        String time = etTimePicker.getText().toString().trim();
        String duration = tvDuration.getText().toString().replace("Thời lượng: ", "").trim();
        String dayOfWeek = spnDayOfWeek.getSelectedItem().toString();
        String type = spnType.getSelectedItem().toString();
        String description = edtDescription.getText().toString().trim();

        // Kiểm tra dữ liệu nhập vào
        if (className.isEmpty() || capacityStr.isEmpty() || priceStr.isEmpty() || time.isEmpty() || duration.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        int capacity = Integer.parseInt(capacityStr);
        double price = Double.parseDouble(priceStr);

        // Tạo đối tượng YogaClass
        YogaClass yogaClass = new YogaClass(
                null, // SQLite sẽ tự động tạo ID
                className,
                dayOfWeek,
                time,
                capacity,
                parseDurationToMinutes(duration),
                price,
                type,
                description
        );

        // Thêm vào SQLite
        long id = dbHelper.addYogaClass(yogaClass);
        if (id != -1) {
            // Đồng bộ lên Firebase
            String firebaseId = firebaseDatabase.child("yoga_classes").push().getKey();
            yogaClass.setId(firebaseId); // Cập nhật ID từ Firebase
            firebaseDatabase.child("yoga_classes").child(firebaseId).setValue(yogaClass)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Đã thêm lớp Yoga thành công!", Toast.LENGTH_SHORT).show();
                            finish(); // Quay về màn hình trước
                        } else {
                            Toast.makeText(this, "Đồng bộ Firebase thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Thêm vào SQLite thất bại!", Toast.LENGTH_SHORT).show();
        }
    }


    // Chuyển đổi thời lượng từ chuỗi "x giờ y phút" sang số phút
    private int parseDurationToMinutes(String duration) {
        String[] parts = duration.split(" ");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[2]);
        return hours * 60 + minutes;
    }
}