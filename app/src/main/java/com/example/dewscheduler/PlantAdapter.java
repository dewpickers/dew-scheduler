package com.example.dewscheduler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.common.io.Resources;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

import javax.xml.transform.Result;

public class PlantAdapter extends FirestoreRecyclerAdapter<Plant, PlantAdapter.PlantHolder> {

    public PlantAdapter(@NonNull FirestoreRecyclerOptions<Plant> options) {
        super(options);
    }

    private AdapterClickListener clickListener;

    @Override
    protected void onBindViewHolder(@NonNull PlantHolder holder, int position, @NonNull Plant model) {
        holder.textViewTitle.setText(model.getTitle());
        holder.textViewDescription.setText(model.getDescription());
        holder.textViewNumber.setText(model.getWateringDate().format(DateTimeFormatter.ofPattern("dd/LL")));
        holder.textWateringDate.setText(String.format("%d", model.getNumber()));
        holder.barWateringLevel.setRating(model.getWateringLevel());
        holder.imageViewIcon.setImageResource(IconResourceFinder.getIconResIdByIndex(model.getIcon()));
        holder.model = model;
    }

    @NonNull
    @Override
    public PlantHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.plant_item,
                parent, false);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickListener != null)
                    clickListener.OnClickAdapterItem((PlantHolder)v.getTag());
            }
        });
        PlantHolder holder = new PlantHolder(v);
        v.setTag(holder);
        return holder;
    }

    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    public void setOnItemClickListener(AdapterClickListener listener)
    {
        clickListener = listener;
    }

    class PlantHolder extends RecyclerView.ViewHolder {

        Plant model;
        TextView textViewTitle;
        TextView textViewDescription;
        TextView textViewNumber;
        TextView textWateringDate;
        RatingBar barWateringLevel;
        ImageView imageViewIcon;

        public PlantHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textWateringDate = itemView.findViewById(R.id.text_watering_date);
            barWateringLevel = itemView.findViewById(R.id.watering_level_bar);
            textViewNumber = itemView.findViewById(R.id.text_view_number);
            imageViewIcon = itemView.findViewById(R.id.image_view_icon);
        }
    }

    interface AdapterClickListener
    {
        void OnClickAdapterItem(PlantHolder item);
    }
}
