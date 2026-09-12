package com.example.inhabitatpractice.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inhabitatpractice.R;
import com.example.inhabitatpractice.admin.EventDetailsActivity;
import com.example.inhabitatpractice.models.Event;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventAdapterViewHolder> {

    public Context context;
    public ArrayList<Event> list;
    private final boolean volunteerView;
    private final long userId;

    public EventAdapter(Context context, ArrayList<Event> list) {
        this(context, list, false, 0);
    }

    public EventAdapter(Context context, ArrayList<Event> list, boolean volunteerView, long userId) {
        this.context = context;
        this.list = list;
        this.volunteerView = volunteerView;
        this.userId = userId;
    }

    @NonNull
    @Override
    public EventAdapter.EventAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.events_list, parent, false);
        return new EventAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.EventAdapterViewHolder holder, int position) {
        Event event = list.get(position);
        holder.image.setImageResource(R.drawable.default_1);
        holder.name.setText(event.name);
        holder.description.setText(event.description);
        holder.date.setText("Date: " + event.date);
        holder.plant.setText("Plant: " + event.plant);
        holder.status.setText("Status: Ongoing");
        holder.volunteers.setText("Volunteers: " + event.volunteersJoined + "/" + event.volunteersNeeded);
        holder.container.setOnClickListener(view -> openEventDetails(event));

    }

    private void openEventDetails(Event event) {
        Intent intent = new Intent(context, volunteerView
                ? com.example.inhabitatpractice.volunteer.EventDetailsActivity.class
                : EventDetailsActivity.class);
        intent.putExtra("event_id", event.id);
        intent.putExtra("user_id", userId);
        intent.putExtra("plant", event.plant);
        intent.putExtra("name", event.name);
        intent.putExtra("description", event.description);
        intent.putExtra("date", event.date);
        intent.putExtra("volunteers_needed", event.volunteersNeeded);
        intent.putExtra("volunteers_joined", event.volunteersJoined);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class EventAdapterViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        TextView description;
        TextView date;
        TextView plant;
        TextView status;
        TextView volunteers;
        View container;

        public EventAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.eventImage);
            name = itemView.findViewById(R.id.eventName);
            description = itemView.findViewById(R.id.eventDescription);
            date = itemView.findViewById(R.id.eventDate);
            plant = itemView.findViewById(R.id.eventPlant);
            status = itemView.findViewById(R.id.eventStatus);
            volunteers = itemView.findViewById(R.id.eventVolunteers);
            container = itemView.findViewById(R.id.container);
        }
    }
}
