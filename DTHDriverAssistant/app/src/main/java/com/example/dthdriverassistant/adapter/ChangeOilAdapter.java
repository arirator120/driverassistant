package com.example.dthdriverassistant.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
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
import com.example.dthdriverassistant.activity.add.AddChangeOilActivity;
import com.example.dthdriverassistant.model.oil;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ChangeOilAdapter extends RecyclerView.Adapter<ChangeOilAdapter.ViewHolder> {
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference _myRef;

    private List<oil> oilList;
    private Context mContext;

    public ChangeOilAdapter(List<oil> oilList, Context mContext) {
        this.oilList = oilList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ChangeOilAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_history_item,parent,false);
        ChangeOilAdapter.ViewHolder holder = new ChangeOilAdapter.ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChangeOilAdapter.ViewHolder holder, int position) {
        oil o = oilList.get(position);
        if(o==null)
            return;

        if(o.getVehicle().getType().getName().equals("Xe máy"))
            holder.ivCar.setImageResource(R.drawable.ic_motor);
        else{
            holder.ivCar.setImageResource(R.drawable.ic_oto);
        }

        String boldCar = "Xe " +  "<b>" + o.getVehicle().getName() + "</b> ";

        holder.tvNameCar.setText(Html.fromHtml(boldCar));
        holder.tvDate.setText(o.getCalFilled());

        //change theo ngữ cảnh
        holder.tvNamePrice.setText("Tiền nhớt:");
        holder.tvNameDate.setText("Ngày thay:");

        String priceFormat = String.format("%,d",o.getPrice());
        String price = priceFormat.replace(",","."); //thay , thành .
        holder.tvPrice.setText(price);

        holder.layout_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickEdit(o);
            }
        });

        holder.imgBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickDelete(o);
            }
        });


    }

    private void onClickEdit(oil o) {
        Intent i =new Intent(mContext, AddChangeOilActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_change_oil", o);
        i.putExtras(bundle);
        mContext.startActivity(i);
    }

    private void onClickDelete(oil o) {
        new AlertDialog.Builder(mContext)
                .setTitle(mContext.getString(R.string.app_name))
                .setMessage("Bạn có chắc muốn xóa cái này?")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        _myRef = mDatabase.getReference("ChangeOil");
                        _myRef.child(o.getId()).removeValue(); //xóa object trên fb
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
        if(oilList!=null)
            return oilList.size(); //phải cái này để trả về số lượng item
        return 0;

    }

    public void release(){
        mContext=null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView layout_item;
        ImageView ivCar;
        TextView tvNameCar, tvPrice, tvDate, tvNamePrice, tvNameDate;
        ImageButton imgBtnDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivCar = itemView.findViewById(R.id.ivTime);
            tvNameCar = itemView.findViewById(R.id.tvNameCar);

            tvNamePrice = itemView.findViewById(R.id.tvNamePrice);
            tvPrice = itemView.findViewById(R.id.tvPrice);

            tvNameDate = itemView.findViewById(R.id.tvNameDate);
            tvDate = itemView.findViewById(R.id.tvDate);

            imgBtnDelete = itemView.findViewById(R.id.imgBtnDelete);
            layout_item = itemView.findViewById(R.id.layout_item);

        }
    }
}
