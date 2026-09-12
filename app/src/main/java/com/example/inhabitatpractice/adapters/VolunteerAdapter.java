package com.example.inhabitatpractice.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inhabitatpractice.R;
import com.example.inhabitatpractice.models.Volunteer;

import java.util.ArrayList;

public class VolunteerAdapter extends RecyclerView.Adapter<VolunteerAdapter.VolunteerViewHolder> {
    public final ArrayList<Volunteer> volunteers;

    public VolunteerAdapter(ArrayList<Volunteer> volunteers) {
        this.volunteers = volunteers;
    }

    @NonNull
    @Override
    public VolunteerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.volunteer_list, parent, false);
        return new VolunteerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VolunteerViewHolder holder, int position) {
        Volunteer volunteer = volunteers.get(position);
        holder.name.setText(volunteer.name);
        holder.username.setText("@" + volunteer.username);
    }

    @Override
    public int getItemCount() {
        return volunteers.size();
    }

    static class VolunteerViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView username;

        VolunteerViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.volunteerName);
            username = itemView.findViewById(R.id.volunteerUsername);
        }
    }
}
