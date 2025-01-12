package com.example.firebasecalendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private TextView tv_selectedDate;
    private RecyclerView rv_database;
    private Button btn_add;
    private Button btn_logout;
    private String stringDateSelected;
    private ArrayList<ShiftItems> shiftItems;
    private DBHelper mDBHelper;
    private CustomAdapter mAdapter;
    private DatabaseReference databaseReference;
    private String userName = null;
    private String userID = null;
    private String phone = null;


    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDBHelper = new DBHelper();
        databaseReference = mDBHelper.getDatabaseReference();

        // get user info from the front activity
        Intent intent = getIntent();
        if (intent != null){
            userName = intent.getStringExtra("userName");
            //userName = sanitizeString(userName);
            phone = intent.getStringExtra("phone");
            //phone = sanitizeString(phone);
            userID = intent.getStringExtra("userID");
            //userID = sanitizeString(userID);
        }
        setInit();
    }

    private void setInit() {

        calendarView = findViewById(R.id.calendarView);
        rv_database = findViewById(R.id.rv_database);
        btn_add = findViewById(R.id.btn_add);
        btn_logout = findViewById(R.id.btn_logout);
        tv_selectedDate = findViewById(R.id.tv_selectedDate);

        // setting RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv_database.setLayoutManager(layoutManager);

        // set the adapter
        mAdapter = new CustomAdapter(new ArrayList<>(), this);
        rv_database.setAdapter(mAdapter);

        // when "Add Shift" button is clicked,
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShiftDialog shiftDialog = new ShiftDialog(MainActivity.this, mDBHelper,
                        userName, userID, phone);
                shiftDialog.show();
            }
        });

        // when "Log Out" button is clicked,
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to go back to the FrontPageActivity
                Intent intent = new Intent(MainActivity.this, FrontPageActivity.class);

                // start the FrontPageActivity
                startActivity(intent);

                // finish the current MainActivity
                finish();
            }
        });

        // when any date from the calendar is clicked,
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                String selectedDate = Integer.toString(year) + "/" + Integer.toString(month+1) +
                        "/" + Integer.toString(dayOfMonth);
                if (selectedDate != null){
                    tv_selectedDate.setText(selectedDate);
                    Log.d("Date Picker", "Main Calendar date is selected");
                    Log.d("Data Picker", "Now read the DB: " + selectedDate);
                    loadRecentDB(selectedDate);

                } else {
                    Log.d("Date Picker", "Main Date Picker failed to update");
                }
            }
        });
    }

    private void loadRecentDB(String date) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // this method is call to get the realtime updates from the DB.
                ArrayList <ShiftItems> shiftItems = new ArrayList<>();

                DataSnapshot dateSnapshot = snapshot.child(date);
                Log.d("Reading DB", "Looking for Data at " + date);

                if (dateSnapshot.exists()){
                    Log.d("Reading DB", "DataSnap exists on the date. Key: " +
                            dateSnapshot.getKey() + " child count: " + dateSnapshot.getChildrenCount());

                    for (DataSnapshot childSnapshot: dateSnapshot.getChildren()) {
                        try {
                                ShiftItems shiftItem = childSnapshot.getValue(ShiftItems.class);
                                if (shiftItem != null) {
                                    shiftItems.add(shiftItem);
                                    Log.d("Reading DB", "Added: " + shiftItem.getName());
                                } else {
                                    Log.w("Reading DB", "ShiftItem is null");
                                }
                        } catch (DatabaseException e) {
                            Log.e("Reading DB", "Error converting to ShiftItems" + e.getMessage());
                        }
                    }
                }
                updateRecyclerView(shiftItems);
            } // onDateChange

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // handle error
                Log.d("Reading DB", "loadRecentDB cancelled");
            }
        });
    }

    private void updateRecyclerView(ArrayList <ShiftItems> shiftItems){
        if (mAdapter == null){
            Log.d("update RecyclerView", "mAdapter is null");
            mAdapter = new CustomAdapter(shiftItems, this);
            rv_database.setHasFixedSize(true);
            rv_database.setAdapter(mAdapter);
        } else {
            Log.d("update RecyclerView", "mAdapter is working");
            mAdapter.setShiftItems(shiftItems);
            mAdapter.notifyDataSetChanged();
        }
    }

}
