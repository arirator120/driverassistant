package com.example.dthdriverassistant.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dthdriverassistant.R;
import com.example.dthdriverassistant.activity.add.AddFuelActivity;
import com.example.dthdriverassistant.activity.add.AddReminderActivity;
import com.example.dthdriverassistant.model.fuel;
import com.example.dthdriverassistant.model.remind;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class RemindAdapter extends  RecyclerView.Adapter<RemindAdapter.ViewHolder> {
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference _myRef;

    private List<remind> remindList;
    private Context mContext;

    public RemindAdapter(List<remind> remindList, Context mContext) {
        this.remindList = remindList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_remind_item,parent,false);
        RemindAdapter.ViewHolder holder = new RemindAdapter.ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        remind r = remindList.get(position);
        if(r == null)
            return;

        if(r.getVehicle().getType().getName().equals("Xe máy"))
            holder.ivTime.setImageResource(R.drawable.ic_time);
        else{
            holder.ivTime.setImageResource(R.drawable.ic_time);
        }

        holder.tvNameXe.setText("Xe " + r.getVehicle().getName());
        holder.tvAction.setText(r.getTvAction());
        holder.tvDay.setText(r.getTvDay());
        holder.tvNote.setText(r.getTvNote());

        holder.layout_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickEdit(r);
            }
        });

        holder.imgBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickDelete(r);
            }
        });
    }


    private void onClickEdit(remind r) {
        Intent i =new Intent(mContext, AddReminderActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_remind", r);
        i.putExtras(bundle);
        mContext.startActivity(i);
    }

    private void onClickDelete(remind r) {
        new AlertDialog.Builder(mContext)
                .setTitle(mContext.getString(R.string.app_name))
                .setMessage("Bạn có chắc muốn xóa cái này?")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        _myRef = mDatabase.getReference("Reminders");
                        _myRef.child(r.getId()).removeValue(); //xóa object trên fb
                        Toast.makeText(mContext, "Xóa thành công!!", Toast.LENGTH_LONG).show();
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("Dừng", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    @Override
    public int getItemCount() {
        if(remindList != null)
            return remindList.size(); //phải cái này để trả về số lượng item
        return 0;

    }

    public void release(){
        mContext=null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView layout_item;
        ImageView ivTime;
        TextView tvNameXe, tvAction, tvDay, tvNote;
        ImageButton imgBtnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivTime = itemView.findViewById(R.id.ivTime);
            tvAction = itemView.findViewById(R.id.tvAction);
            tvDay = itemView.findViewById(R.id.tvDay);
            tvNote = itemView.findViewById(R.id.tvNote);
            tvNameXe = itemView.findViewById(R.id.tvNameXe);
            imgBtnDelete = itemView.findViewById(R.id.imgBtnDelete);
            layout_item = itemView.findViewById(R.id.layout_item);

        }
    }
}
