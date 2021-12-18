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
import com.example.dthdriverassistant.activity.add.AddVehicleActivity;
import com.example.dthdriverassistant.model.fuel;
import com.example.dthdriverassistant.model.vehicle;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.ViewHolder> {

    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference _myRef;

    private List<vehicle> vehicleList;
    private Context mContext;

    public VehicleAdapter(List<vehicle> vehicleList, Context mContext) {
        this.vehicleList = vehicleList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public VehicleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_vehicle_item,parent,false);
        VehicleAdapter.ViewHolder holder = new VehicleAdapter.ViewHolder(v);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull VehicleAdapter.ViewHolder holder, int position) {
        vehicle v = vehicleList.get(position);
        if(v==null)
            return;

        if(v.getType().getName().equals("Xe máy"))
            holder.ivCar.setImageResource(R.drawable.ic_motor);
        else{
            holder.ivCar.setImageResource(R.drawable.ic_oto);
        }


        holder.tvNameCar.setText(v.getName());
        holder.tvSign.setText(v.getSign());



        holder.layout_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickEdit(v);
            }
        });

        holder.imgBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickDelete(v);
            }
        });
    }

    private void onClickEdit(vehicle v) {
        Intent i =new Intent(mContext, AddVehicleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_vehicle", v);
        i.putExtras(bundle);
        mContext.startActivity(i);
    }

    private void onClickDelete(vehicle v) {
        new AlertDialog.Builder(mContext)
                .setTitle(mContext.getString(R.string.app_name))
                .setMessage("Bạn có chắc muốn xóa cái này?")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        _myRef = mDatabase.getReference("UsersVehicle");
                        _myRef.child(v.getId()).removeValue(); //xóa object trên fb
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
        if(vehicleList!=null)
            return vehicleList.size(); //phải cái này để trả về số lượng item
        return 0;

    }

    public void release(){
        mContext=null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView layout_item;
        ImageView ivCar;
        TextView tvNameCar, tvSign;
        ImageButton imgBtnDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCar = itemView.findViewById(R.id.ivCar);
            tvNameCar = itemView.findViewById(R.id.tvNameCar);
            tvSign = itemView.findViewById(R.id.tvSign);
            imgBtnDelete = itemView.findViewById(R.id.imgBtnDelete);
            layout_item = itemView.findViewById(R.id.layout_item);

        }
    }


}
