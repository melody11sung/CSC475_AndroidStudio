package com.example.todolist_sqlite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DV_VERSION = 1;
    private static final String DB_NAME = "minhyun.db";
    public DBHelper(@Nullable Context context){
        super(context, DB_NAME, null, DV_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){

        // When database is created, make this table
        db.execSQL("CREATE TABLE IF NOT EXISTS TodoList (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT NOT NULL, content TEXT NOT NULL, writeDate TEXT NOT NULL)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

        onCreate(db);
    }

    // select (to search and read)
    public ArrayList<TodoItem> getTodoList(){
        ArrayList <TodoItem> todoItems = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM TodoList ORDER BY writeDate DESC", null);
        if (cursor.getCount() != 0){
            // if db is not empty
            while (cursor.moveToNext()){
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex("title"));
                @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndex("content"));
                @SuppressLint("Range") String writeDate = cursor.getString(cursor.getColumnIndex("writeDate"));

                TodoItem todoItem = new TodoItem();
                todoItem.setId(id);
                todoItem.setTitle(title);
                todoItem.setContent(content);
                todoItem.setWriteDate(writeDate);
                // add the todoItem to the ArrayList
                todoItems.add(todoItem);
            }
        }
        cursor.close();

        return todoItems;
    }



    // insert (to write)
    public void InsertTodo(String _title, String _content, String _writeDate){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO TodoList (title, content, writeDate) " +
                "VALUES('" + _title + "', '" + _content + "', '" + _writeDate + "' );");
    }

    // update (to write)
    public void UpdateTodo(String _title, String _content, String _writeDate, String _priorDate){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE TodoList SET title = '"+_title+"', content = '"+_content+"', writeDate = '"+_writeDate+"' " +
                "WHERE writeDate = '" + _priorDate + "'  ");
    }

    // delete
    public void DeleteTodo(String _priorDate){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM TodoList WHERE writeDate = '" + _priorDate +"'");
    }


}
