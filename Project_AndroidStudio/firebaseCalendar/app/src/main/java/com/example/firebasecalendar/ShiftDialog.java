package com.example.firebasecalendar;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;

import java.util.Locale;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class ShiftDialog extends Dialog {

    private EditText et_duty;
    private TextView tv_date;
    private Button btn_save;
    private Button btn_cancel;
    private DatabaseReference databaseReference;
    private CustomAdapter mAdapter;
    private DBHelper dbHelper;
    private String userID;
    private String userName;
    private String phone;

    @SuppressLint("MissingInflatedId")
    public ShiftDialog(@NonNull Context context, DBHelper dbHelper,
                       String userName, String userID, String phone){
        super(context, android.R.style.Theme_Material_Light_Dialog);
        this.dbHelper = dbHelper;
        this.userName = userName;
        this.userID = userID;
        this.phone = phone;

        setContentView(R.layout.dialog_add);

        et_duty = findViewById(R.id.et_duty);
        tv_date = findViewById(R.id.tv_date);
        btn_save = findViewById(R.id.btn_save);
        btn_cancel = findViewById(R.id.btn_cancel);

        setupDatePicker();

        // when "Save Duty" button is clicked;
        btn_save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // get the inputs
                String duty = et_duty.getText().toString().trim();
                String requestDate = tv_date.getText().toString();
                String currentDate = new SimpleDateFormat("yyyy/MM/dd, HH:mm").format(new Date());

                // insert the input data to DB
                if (!duty.isEmpty() && !requestDate.isEmpty()) {
                    ShiftItems shiftItem = new ShiftItems(userID, userName, duty, requestDate, currentDate, phone);

                    // insert the input to DB
                    dbHelper.InsertShift(shiftItem);

                    // insert the input to UI

                    // log and toast
                    Log.d("Save Shift", "shiftItem is saved in DB");
                    dismiss();
                    Toast.makeText(getContext(), "Successfully Saved", Toast.LENGTH_SHORT).show();

                } else { // if name, duty or date is null
                    Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    Log.d("Save Shift", "missing fields");
                }
            }
        });

        // when "cancel" button is clicked,
        btn_cancel.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                dismiss();
                Toast.makeText(getContext(), "Canceled", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setupDatePicker() {

        CalendarView calenderView = findViewById(R.id.calendarView_dialog);
        calenderView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                String selectedDate = Integer.toString(year) + "/" + Integer.toString(month+1) +
                        "/" + Integer.toString(dayOfMonth);
                if (selectedDate != null){
                    tv_date.setText(selectedDate);
                    Log.d("Dialog Date Picker", "tv_date is changed");
                } else {
                    Log.d("Dialog Date Picker", "tv_date failed to update");
                }


            }
        });
    }

}
