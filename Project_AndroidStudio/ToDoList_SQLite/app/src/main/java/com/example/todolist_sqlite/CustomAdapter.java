package com.example.todolist_sqlite;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private ArrayList<TodoItem> mTodoItems;
    private Context mContext;
    private DBHelper mDBHelper;

    public CustomAdapter(ArrayList<TodoItem> mTodoItems, Context mContext) {
        this.mTodoItems = mTodoItems;
        this.mContext = mContext;
        mDBHelper = new DBHelper(mContext);
    }


    @NonNull
    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View holder = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);

        return new ViewHolder(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_title.setText(mTodoItems.get(position).getTitle());
        holder.tv_content.setText(mTodoItems.get(position).getContent());
        holder.tv_writeDate.setText(mTodoItems.get(position).getWriteDate());
    }

    @Override
    public int getItemCount() {
        return mTodoItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_title;
        private TextView tv_content;
        private TextView tv_writeDate;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            tv_title = itemView.findViewById(R.id.tv_title);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_writeDate = itemView.findViewById(R.id.tv_date);

            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view){

                    int curPos = getAdapterPosition(); // current position
                    TodoItem todoItem = mTodoItems.get(curPos); // ArrayList element of the curPos

                    String[] strChoiceItems = {"Complete", "Incomplete", "Edit", "Delete"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Choose Option");
                    builder.setItems(strChoiceItems, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int position) {

                            if (position == 0){
                                // mark complete
                                tv_title.setPaintFlags(tv_title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                tv_content.setPaintFlags(tv_content.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                tv_writeDate.setPaintFlags(tv_writeDate.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            }

                            else if (position == 1){
                                // mark incomplete
                                tv_title.setPaintFlags(tv_title.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                                tv_content.setPaintFlags(tv_content.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                                tv_writeDate.setPaintFlags(tv_writeDate.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                            }

                            else if (position == 2){
                                // edit

                                // pop-up for new to-do
                                Dialog dialog = new Dialog(mContext, android.R.style.Theme_Material_Light_Dialog);
                                dialog.setContentView(R.layout.dialog_edit);
                                EditText et_title = dialog.findViewById(R.id.et_title);
                                EditText et_content = dialog.findViewById(R.id.et_content);
                                Button btn_add_dialog = dialog.findViewById(R.id.btn_add_dialog);

                                // original text
                                et_title.setText(todoItem.getTitle());
                                et_content.setText(todoItem.getContent());

                                // move cursor to the end
                                et_title.setSelection(et_title.getText().length());
                                et_content.setSelection(et_content.getText().length());

                                btn_add_dialog.setOnClickListener(new View.OnClickListener(){

                                    @Override
                                    public void onClick(View view){

                                        // pull the original data
                                        String title = et_title.getText().toString();
                                        String content = et_content.getText().toString();
                                        String currentTime = new SimpleDateFormat("MM-dd-yyyy HH:mm").format(new Date());
                                        String priorTime = todoItem.getWriteDate();

                                        // update the new input data to DB
                                        mDBHelper.UpdateTodo(title, content, currentTime, priorTime);

                                        // update the input to UI
                                        todoItem.setTitle(title);
                                        todoItem.setContent(content);
                                        todoItem.setWriteDate(currentTime);
                                        notifyItemChanged(curPos, todoItem);
                                        dialog.dismiss();
                                        Toast.makeText(mContext, "Edit Completed", Toast.LENGTH_SHORT).show();

                                    }
                                });

                                dialog.show();

                            } else if (position == 3){
                                // delete table
                                String priorTime = todoItem.getWriteDate();
                                mDBHelper.DeleteTodo(priorTime);
                                // delete UI
                                mTodoItems.remove(curPos);
                                notifyItemRemoved(curPos);
                                Toast.makeText(mContext, "Deleted", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.show();

                }

            });
        }
    }

    public void addItem(TodoItem _item){

        mTodoItems.add(0, _item);
        notifyItemInserted(0);

    }
}
