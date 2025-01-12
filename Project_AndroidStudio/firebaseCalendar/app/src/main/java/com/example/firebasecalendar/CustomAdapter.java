package com.example.firebasecalendar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.app.AlertDialog;
import java.util.ArrayList;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class CustomAdapter extends RecyclerView.Adapter <CustomAdapter.ViewHolder> {

    private ArrayList <ShiftItems> mShiftItems;
    private Context mContext;
    private DBHelper mDBHelper;

    public CustomAdapter(ArrayList<ShiftItems> shiftItems, Context context) {
        Log.d("Custom Adapter", "CustomAdapter Initiated");

        if (shiftItems != null){
            this.mShiftItems = new ArrayList<>(shiftItems);
        } else {
            this.mShiftItems = new ArrayList<>();
            Log.d("Custom Adapter", "mShiftItems is null");
        }
        this.mContext = context;
        mDBHelper = new DBHelper();
    }

    public void setShiftItems(ArrayList<ShiftItems> shiftItems) {
        this.mShiftItems = shiftItems;
        Log.d("Custom Adapter", "setShiftItems done");
    }

    @NonNull
    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.db_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.ViewHolder holder, int position) {
        ShiftItems shiftItem = mShiftItems.get(position);

        String name = shiftItem.getName();
        String shift = shiftItem.getShift();
        String phone = shiftItem.getPhone();

        holder.tv_name.setText(name);
        holder.tv_duty.setText(shift);
        holder.tv_phone.setText(phone);
    }

    @Override
    public int getItemCount() {
        return mShiftItems.size();
    }

    public void addItem(ShiftItems _item){
        mShiftItems.add(0, _item);
        notifyItemInserted(0);
        Log.d("Custom Adapter", "addItem done");
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_name;
        private TextView tv_duty;
        private TextView tv_phone;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_name);
            tv_duty = itemView.findViewById(R.id.tv_duty);
            tv_phone = itemView.findViewById(R.id.tv_phone);

            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {

                    int curPos = getAdapterPosition();
                    ShiftItems shiftItem = mShiftItems.get(curPos);
                    Log.d("Custom Adapter", "ViewHolder Clicked, curPos: " + Integer.toString(curPos));

                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Choose Option");
                    CharSequence options[] = new CharSequence[]{
                            "Delete", "Send Text"
                    };
                    builder.setItems(options, new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialog, int position) {

                            if (position == 0){
                                // option delete
                                mDBHelper.DeleteShift(shiftItem);
                                mShiftItems.remove(curPos);
                                notifyItemRemoved(curPos);
                                Toast.makeText(mContext, "Deleted", Toast.LENGTH_SHORT).show();
                            }

                            if (position == 1){
                                // option notification
                                sendNotification(shiftItem);
                            }

                        }
                    });
                    builder.show();
                }
            });
        }

        public void sendNotification(ShiftItems item){

            String phone = item.getPhone();
            String requestDate = item.getRequestDate();
            String name = item.getName();
            String shift = item.getShift();

            if (phone == null){
                Toast.makeText(mContext, "Phone number not available", Toast.LENGTH_SHORT).show();
            } else {
                Intent smsIntent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", phone, null));
                smsIntent.putExtra("sms_body", "Hi "+name+"! This is a kind reminder of your "+
                        shift+" shift at "+requestDate+". Please be at work 10 minutes prior to your" +
                        " start time. Thank you!");
                // Start the messaging app
                itemView.getContext().startActivities(new Intent[]{smsIntent});
            }
        }

    } // end class
}
