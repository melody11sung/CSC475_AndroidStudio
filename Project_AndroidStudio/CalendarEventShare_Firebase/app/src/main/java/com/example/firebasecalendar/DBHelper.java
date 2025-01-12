package com.example.firebasecalendar;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DBHelper {

    private DatabaseReference databaseReference;

    public DBHelper(DatabaseReference reference) {
        this.databaseReference = reference;
        // databaseReference = FirebaseDatabase.getInstance().getReference("shifts");
        Log.d("DBHelper", "Firebase Reference initialized");
    }

    public DatabaseReference getDatabaseReference(){
        return databaseReference;
    }

    public void DeleteShift(ShiftItems item) {
        if (item != null && item.getRequestDate() != null){
            Query query = databaseReference.child(item.getRequestDate()).child(item.getID());
            query.addListenerForSingleValueEvent(new ValueEventListener(){
                public void onDataChange(DataSnapshot dataSnapshot){
                    dataSnapshot.getRef().removeValue();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void InsertShift(ShiftItems item){
        if (item != null){
            databaseReference.child(item.getRequestDate()).
                    child(item.getID()).setValue(item);
            Log.d("DBHelper", "a ShiftItem is inserted to DB.");
        }
    }

    public ShiftItems getShiftItem(String requestDate, String ID){

        DatabaseReference shiftItemRef = databaseReference.child(requestDate).child(ID);
        final ShiftItems[] shiftItem = {null};

        shiftItemRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    try {
                        shiftItem[0] = snapshot.getValue(ShiftItems.class);
                    } catch (DatabaseException e) {
                        Log.e("DBHelper", "Error converting to ShiftItems: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DBHelper", "Error reading ShiftItem from database: " + error.getMessage());
            }
        });

        return shiftItem[0];
    }


}
