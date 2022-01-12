package com.example.dthdriverassistant.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dthdriverassistant.R;
import com.example.dthdriverassistant.activity.add.AddReminderActivity;
import com.example.dthdriverassistant.model.help;
import com.example.dthdriverassistant.model.remind;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class HelpAdapter extends  RecyclerView.Adapter<HelpAdapter.ViewHolder> {

    private List<help> helpList;
    private Context mContext;

    public HelpAdapter(List<help> helpList, Context mContext) {
        this.helpList = helpList;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_help_item,parent,false);
        HelpAdapter.ViewHolder holder = new HelpAdapter.ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        help h = helpList.get(position);
        if(h == null)
            return;

        holder.tvProvince.setText(h.getName());
        holder.ivCallHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iCall = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+ h.getPhone()));
                mContext.startActivity(iCall);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(helpList != null)
            return helpList.size(); //phải cái này để trả về số lượng item
        return 0;
    }

    public void release(){
        mContext=null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView layout_itemHelp;
        ImageView ivCallHelp;
        TextView tvProvince;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_itemHelp = itemView.findViewById(R.id.layout_itemHelp);
            ivCallHelp = itemView.findViewById(R.id.ivCallHelp);
            tvProvince = itemView.findViewById(R.id.tvProvince);
        }
    }
}
