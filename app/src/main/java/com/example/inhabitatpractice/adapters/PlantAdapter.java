package com.example.inhabitatpractice.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inhabitatpractice.R;
import com.example.inhabitatpractice.models.Plant;

import java.util.List;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.PlantViewHolder> {
    private final List<Plant> plants;

    public PlantAdapter(List<Plant> plants) {
        this.plants = plants;
    }

    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.plant_list, parent, false);
        return new PlantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {
        Plant plant = plants.get(position);
        holder.image.setImageResource(R.drawable.default_1);
        holder.name.setText(plant.getName());
        holder.description.setText(plant.getDescription());
    }

    @Override
    public int getItemCount() {
        return plants.size();
    }

    static class PlantViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        TextView description;

        PlantViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.plantImage);
            name = itemView.findViewById(R.id.plantName);
            description = itemView.findViewById(R.id.plantDescription);
        }
    }
}
