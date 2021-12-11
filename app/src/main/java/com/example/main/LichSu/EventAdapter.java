package com.example.main.LichSu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.main.R;

import java.util.List;


public class EventAdapter extends ArrayAdapter<Event> {

    private Context context;
    private int layout;
    private List<Event> data;
    private EventDatabase mDbHelper;

    public EventAdapter(@NonNull Context context, int resource, @NonNull List<Event> objects,
                        EventDatabase DbHelper) {
        super(context, resource, objects);
        this.context = context;
        this.layout = resource;
        this.data = objects;
        this.mDbHelper = DbHelper;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layout,parent,false);

            holder = new ViewHolder();
            holder.name = convertView.findViewById(R.id.tv_tencv);
            holder.date = convertView.findViewById(R.id.tv_date);
            holder.place = convertView.findViewById(R.id.tv_place);
            holder.cost = convertView.findViewById(R.id.tv_thanhtien);
            holder.eventSwitch = convertView.findViewById(R.id.sw_switch);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Event event = data.get(position);

        holder.name.setText(event.getTenCV());
        holder.date.setText(event.getDate());
        holder.place.setText(event.getPlace());
        holder.cost.setText(new String(String.valueOf(event.getVnd())));

        holder.eventSwitch.setChecked(event.getCompleted());
        holder.eventSwitch.setTag(position);

        holder.eventSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = event.getCompleted();
                event.setCompleted(!checked);
                mDbHelper.updateEvent(event);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    public void setEvents(List<Event> events) {
        this.data = events;
        notifyDataSetChanged();
    }

    public static class ViewHolder {
        TextView name;
        TextView date;
        TextView place;
        TextView cost;
        Switch eventSwitch;
    }
}
