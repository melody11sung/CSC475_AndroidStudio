package com.example.todolist_sqlite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rv_todo;
    private FloatingActionButton btn_write;
    private ArrayList<TodoItem> todoItems;
    private DBHelper mDBHelper;
    private CustomAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setInit();

    }

    private void setInit() {

        mDBHelper = new DBHelper(this);
        rv_todo = findViewById(R.id.rv_todo);
        btn_write = findViewById(R.id.btn_write);
        todoItems = new ArrayList<>();

        // load recent DB
        loadRecentDB();

        // when write(add) button is clicked from the main page
        btn_write.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                // pop-up for new to-do
                Dialog dialog;

                dialog = new Dialog(MainActivity.this, android.R.style.Theme_Material_Light_Dialog);
                dialog.setContentView(R.layout.dialog_edit);

                EditText et_title = dialog.findViewById(R.id.et_title);
                EditText et_content = dialog.findViewById(R.id.et_content);
                Button btn_edit_dialog = dialog.findViewById(R.id.btn_add_dialog);

                // when add button is clicked from the dialog page with new inputs
                btn_edit_dialog.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view){
                        // save current time as a String variable
                        String currentTime = new SimpleDateFormat("MM-dd-yyyy HH:mm").format(new Date());

                        // insert the input data to DB
                        mDBHelper.InsertTodo(et_title.getText().toString(), et_content.getText().toString(), currentTime);

                        // insert the input to UI
                        TodoItem item = new TodoItem();
                        item.setTitle(et_title.getText().toString());
                        item.setContent(et_content.getText().toString());
                        item.setWriteDate(currentTime);

                        mAdapter.addItem(item);
                        rv_todo.smoothScrollToPosition(0);
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, "Added to the list", Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.show();  // dialog is done
            } // end onClick

        }); // end setOnClickListener
    }

    private void loadRecentDB() {

        // bring the DB
        todoItems = mDBHelper.getTodoList();
        if(mAdapter == null){
            mAdapter = new CustomAdapter(todoItems, this);
            rv_todo.setHasFixedSize(true);
            rv_todo.setAdapter(mAdapter);
        }
    }
}