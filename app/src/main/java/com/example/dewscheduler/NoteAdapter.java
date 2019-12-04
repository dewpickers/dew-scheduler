package com.example.dewscheduler;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.NoteHolder> {

    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteHolder holder, int position, @NonNull Note model) {
        holder.textViewTitle.setText(model.getTitle());
        holder.textViewDescription.setText(model.getDescription());
        holder.textViewNumber.setText(String.valueOf(model.getNumber()));
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item,
                parent, false);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, NewNoteActivity.class);
                NoteHolder holder = (NoteHolder)v.getTag();
                intent.putExtra("title", holder.textViewTitle.getText());
                intent.putExtra("description", holder.textViewDescription.getText());
                intent.putExtra("number", Integer.valueOf(holder.textViewNumber.getText().toString()));
                context.startActivity(intent);
            }
        });
        NoteHolder holder = new NoteHolder(v);
        v.setTag(holder);
        return holder;
    }

    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();

    }

    class NoteHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewDescription;
        TextView textViewNumber;

        public NoteHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewNumber = itemView.findViewById(R.id.text_view_number);
        }
    }
}
