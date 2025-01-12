package com.example.firebasecalendar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FrontPageActivity extends AppCompatActivity {

    private EditText front_name;
    private EditText front_id;
    private EditText front_phone;
    private Button btn_start;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);

        front_name = findViewById(R.id.front_name);
        front_id = findViewById(R.id.front_id);
        front_phone = findViewById(R.id.front_phone);
        btn_start = findViewById(R.id.btn_start);

        btn_start.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                String name = front_name.getText().toString().trim();
                name = sanitizeString(name);
                String id = front_id.getText().toString().trim();
                id = sanitizeString(id);
                String phone = front_phone.getText().toString().trim();
                phone = sanitizeString(phone);

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(id)
                        || TextUtils.isEmpty(phone)) {
                    Toast.makeText(FrontPageActivity.this, "Please fill out the form",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(FrontPageActivity.this, MainActivity.class);
                    intent.putExtra("userName", name);
                    intent.putExtra("userID", id);
                    intent.putExtra("phone", phone);
                    startActivity(intent);
                    Toast.makeText(FrontPageActivity.this, "Name: " + name,
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }

    private String sanitizeString(String input){
        return input.replaceAll("[\\(\\)@#$%^&/*\\{\\}._+=]", "");
    }

}
