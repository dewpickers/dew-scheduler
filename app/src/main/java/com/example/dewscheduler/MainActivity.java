package com.example.dewscheduler;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity implements PlantAdapter.AdapterClickListener, View.OnClickListener, DialogInterface.OnClickListener, DialogInterface.OnCancelListener
{
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference plantsRef = db.collection("plants");
    private PlantAdapter adapter;
    private int plantToDelete = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton buttonAddNote = findViewById(R.id.button_add_note);
        buttonAddNote.setOnClickListener(this);
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        Query query = plantsRef.orderBy("number", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Plant> options = new FirestoreRecyclerOptions.Builder<Plant>()
                .setQuery(query, Plant.class)
                .build();
        adapter = new PlantAdapter(options);
        adapter.setOnItemClickListener(this);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                plantToDelete = viewHolder.getAdapterPosition();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                String name = viewHolder instanceof PlantAdapter.PlantHolder ? ((PlantAdapter.PlantHolder)viewHolder).textViewTitle.getText().toString() : "<Error>";
                builder.setMessage(String.format(getString(R.string.sure_to_delete), name))
                    .setPositiveButton(R.string.answer_yes, MainActivity.this)
                    .setNegativeButton(R.string.answer_no, MainActivity.this)
                    .setOnCancelListener(MainActivity.this)
                    .show();
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void OnClickAdapterItem(PlantAdapter.PlantHolder item)
    {
        Intent intent = new Intent(this, EditPlantActivity.class)
            .putExtra("index", item.getAdapterPosition())
            .putExtra("title", item.model.getTitle())
            .putExtra("description", item.model.getDescription())
            .putExtra("number", item.model.getNumber())
            .putExtra("icon", item.model.getIcon());
        startActivityForResult(intent, 0);
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId() == R.id.button_add_note)
            startActivity(new Intent(MainActivity.this, EditPlantActivity.class));
    }

    @Override
    public void onClick(DialogInterface dialog, int which)
    {
        if(which == DialogInterface.BUTTON_POSITIVE)
            adapter.deleteItem(plantToDelete);
        else
            adapter.notifyDataSetChanged();
    }

    @Override
    public void onCancel(DialogInterface dialog)
    {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            plantToDelete = data.getIntExtra("index", -1);
        }
    }
}
