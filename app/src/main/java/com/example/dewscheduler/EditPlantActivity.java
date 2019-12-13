package com.example.dewscheduler;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditPlantActivity extends AppCompatActivity {
    private EditText editTextTitle;
    private EditText editTextDescription;
    private NumberPicker numberPickerNumber;

    private int index = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        ActionBar ab = getSupportActionBar();
        if(ab != null)
            ab.setHomeAsUpIndicator(R.drawable.ic_close);

        setTitle(getString(R.string.header_new_plant));

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        numberPickerNumber = findViewById(R.id.number_picker_number);

        numberPickerNumber.setMinValue(1);
        numberPickerNumber.setMaxValue(10);

        Intent intent = getIntent();
        index = intent.getIntExtra("index", -1);
        if(index != -1)
        {
                setTitle(getString(R.string.header_edit_plant));
            editTextTitle.setText(intent.getStringExtra("title"));
            editTextDescription.setText(intent.getStringExtra("description"));
            numberPickerNumber.setValue(intent.getIntExtra("number", 1));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveNote() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        int number = numberPickerNumber.getValue();

        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Пожалуйсте, заполните поле", Toast.LENGTH_SHORT).show();
            return;
        }
        CollectionReference notebookRef = FirebaseFirestore.getInstance()
                .collection("Notebook");
        notebookRef.add(new Note(title, description, number));
        Toast.makeText(this, "Запись добавлена", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK, new Intent().putExtra("index", index));
        finish();
    }
}
