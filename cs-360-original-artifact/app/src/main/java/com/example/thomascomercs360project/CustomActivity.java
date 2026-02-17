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

    private Context context;
    private ArrayList eventId, eventTitle, eventDescription, eventDate, eventTime;
    Activity activity;


    CustomActivity(Activity activity, Context context,
                   ArrayList eventId,
                   ArrayList eventTitle,
                   ArrayList eventDescription,
                   ArrayList eventDate,
                   ArrayList eventTime) {
        this.activity = activity;
        this.context = context;
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.eventDescription = eventDescription;
        this.eventDate = eventDate;
        this.eventTime = eventTime;

    }

    // Fills rows and fills the event table with the data.
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.event_table_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.id.setText(String.valueOf(eventId.get(position)));
        holder.title.setText(String.valueOf(eventTitle.get(position)));
        holder.description.setText(String.valueOf(eventDescription.get(position)));
        holder.date.setText(String.valueOf(eventDate.get(position)));
        holder.time.setText(String.valueOf(eventTime.get(position)));

        holder.layout.setOnClickListener((view) -> {

        });
        holder.itemView.findViewById(R.id.edit_button).setOnClickListener((view -> {
            Intent intent = new Intent(context, UpdateActivity.class);
            intent.putExtra("id", String.valueOf(eventId.get(position)));
            intent.putExtra("title", String.valueOf(eventTitle.get(position)));
            intent.putExtra("description", String.valueOf(eventDescription.get(position)));
            intent.putExtra("date", String.valueOf(eventDate.get(position)));
            intent.putExtra("time", String.valueOf(eventTime.get(position)));
            activity.startActivityForResult(intent, 1);
        }));

    }

    @Override
    public int getItemCount() {
        return eventId.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView id, title, description, date, time;
        LinearLayout layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.event_id);
            title = itemView.findViewById(R.id.event_title);
            description = itemView.findViewById(R.id.event_description);
            date = itemView.findViewById(R.id.date_to_fire);
            time = itemView.findViewById(R.id.time_to_fire);
            layout = itemView.findViewById((R.id.mainLayout));

        }
    }

}
