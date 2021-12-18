package com.example.dthdriverassistant;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RemindAdapter  extends RecyclerView.Adapter<RemindAdapter.ViewHolder> {

    private List<Remind> lstRemind;

    public RemindAdapter(List<Remind> lstRemind){this.lstRemind = lstRemind;}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_remind, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Remind r = lstRemind.get(position);
        holder.imgAvatar.setImageResource(r.getAvatar());
        holder.tvAction.setText(r.getAction());
        holder.tvDay.setText(r.getDay());
        holder.tvNote.setText(r.getNote());
    }

    @Override
    public int getItemCount() { return lstRemind.size();}

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        ImageView imgAvatar;
        TextView tvAction, tvDay, tvNote;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            tvAction = itemView.findViewById(R.id.tvAction);
            tvDay = itemView.findViewById(R.id.tvDay);
            tvNote = itemView.findViewById(R.id.tvNote);
            imgAvatar.setOnCreateContextMenuListener(this);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "tvAction.getText() + tvDay.getText() + tvNote.getText()", Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        }
    }
}
