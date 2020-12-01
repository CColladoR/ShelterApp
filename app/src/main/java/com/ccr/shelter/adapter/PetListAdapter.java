package com.ccr.shelter.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PetListAdapter extends RecyclerView.Adapter<PetListAdapter.PetViewHolder> implements Filterable {

    class PetViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameView;
        private final TextView breedView;
        private final ImageView genderView;
        private final TextView weightView;
        private final ImageView imageView;

        private PetViewHolder(View itemView) {
            super(itemView);

            nameView = itemView.findViewById(R.id.name);
            breedView = itemView.findViewById(R.id.breed);
            genderView = itemView.findViewById(R.id.gender);
            weightView = itemView.findViewById(R.id.weight);
            imageView = itemView.findViewById(R.id.image);
        }

        public String getName() {
            return nameView.getText().toString();
        }

    }

        @Override
        public Filter getFilter() {
            return null;
        }

        @NonNull
        @Override
        public PetListAdapter.PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = mInflater.inflate(R.layout.item_list, parent, false);
            return new PetViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull PetListAdapter.PetViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }

