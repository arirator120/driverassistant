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
import com.example.dthdriverassistant.model.fuel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class FuelAdapter extends RecyclerView.Adapter<FuelAdapter.ViewHolder> {
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference _myRef;

    private List<fuel> fuelList;
    private Context mContext;

    public FuelAdapter(List<fuel> fuelList, Context mContext) {
        this.fuelList = fuelList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public FuelAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_history_item,parent,false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FuelAdapter.ViewHolder holder, int position) {
        fuel f = fuelList.get(position);
        if(f==null)
            return;

        if(f.getVehicle().getType().getName().equals("Xe máy"))
            holder.ivCar.setImageResource(R.drawable.ic_motor);
        else{
            holder.ivCar.setImageResource(R.drawable.ic_oto);
        }

        holder.tvNameCar.setText("Xe " + f.getVehicle().getName());
        holder.tvDate.setText(f.getCalFilled());

//        DecimalFormat formatter = new DecimalFormat("#,##,###,####");
        String priceFormat = String.format("%,d",f.getPrice());
        String price = priceFormat.replace(",","."); //thay , thành .
        holder.tvPrice.setText(price);

        holder.layout_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickEdit(f);
            }
        });

        holder.imgBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickDelete(f);
            }
        });
    }

    private void onClickEdit(fuel f) {
        Intent i =new Intent(mContext, AddFuelActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_refuel", f);
        i.putExtras(bundle);
        mContext.startActivity(i);
    }

    private void onClickDelete(fuel f) {
        new AlertDialog.Builder(mContext)
                .setTitle(mContext.getString(R.string.app_name))
                .setMessage("Bạn có chắc muốn xóa cái này?")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        _myRef = mDatabase.getReference("Refuel");
                        _myRef.child(f.getId()).removeValue(); //xóa object trên fb
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
        if(fuelList!=null)
            return fuelList.size(); //phải cái này để trả về số lượng item
        return 0;

    }

    public void release(){
        mContext=null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView layout_item;
        ImageView ivCar;
        TextView tvNameCar, tvPrice,tvDate;
        ImageButton imgBtnDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCar = itemView.findViewById(R.id.ivTime);
            tvNameCar = itemView.findViewById(R.id.tvNameCar);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvDate = itemView.findViewById(R.id.tvDate);
            imgBtnDelete = itemView.findViewById(R.id.imgBtnDelete);
            layout_item = itemView.findViewById(R.id.layout_item);

        }
    }
}
