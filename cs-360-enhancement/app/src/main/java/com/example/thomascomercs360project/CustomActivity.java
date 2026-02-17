package com.example.thomascomercs360project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomActivity extends RecyclerView.Adapter<CustomActivity.MyViewHolder> {

    private final Context context;
    private final ArrayList<String> eventId, eventTitle, eventDescription, eventDate, eventTime;
    private final Activity activity;

    CustomActivity(Activity activity, Context context,
                   ArrayList<String> eventId,
                   ArrayList<String> eventTitle,
                   ArrayList<String> eventDescription,
                   ArrayList<String> eventDate,
                   ArrayList<String> eventTime) {
        this.activity = activity;
        this.context = context;
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.eventDescription = eventDescription;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.event_table_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.id.setText(eventId.get(position));
        holder.title.setText(eventTitle.get(position));
        holder.description.setText(eventDescription.get(position));
        holder.date.setText(eventDate.get(position));
        holder.time.setText(eventTime.get(position));

        holder.itemView.findViewById(R.id.edit_button).setOnClickListener(view -> {
            Intent intent = new Intent(context, UpdateActivity.class);
            intent.putExtra("id", eventId.get(position));
            intent.putExtra("title", eventTitle.get(position));
            intent.putExtra("description", eventDescription.get(position));
            intent.putExtra("date", eventDate.get(position));
            intent.putExtra("time", eventTime.get(position));
            activity.startActivityForResult(intent, 1);
        });
    }

    @Override
    public int getItemCount() {
        return eventId.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView id, title, description, date, time;
        LinearLayout layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.event_id);
            title = itemView.findViewById(R.id.event_title);
            description = itemView.findViewById(R.id.event_description);
            date = itemView.findViewById(R.id.date_to_fire);
            time = itemView.findViewById(R.id.time_to_fire);
            layout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
