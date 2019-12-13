package com.example.dewscheduler;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditPlantActivity extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnClickListener
{
    private EditText editTextTitle;
    private EditText editTextDescription;
    private NumberPicker numberPickerNumber;
    private ImageButton imageButtonIcon;
    private int iconIndex = 0;

    private int index = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_plant);

        ActionBar ab = getSupportActionBar();
        if(ab != null)
            ab.setHomeAsUpIndicator(R.drawable.ic_close);

        setTitle(getString(R.string.header_new_plant));

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        numberPickerNumber = findViewById(R.id.number_picker_number);

        numberPickerNumber.setMinValue(1);
        numberPickerNumber.setMaxValue(10);

        imageButtonIcon = findViewById(R.id.image_button_icon);
        imageButtonIcon.setOnClickListener(this);

        Intent intent = getIntent();
        index = intent.getIntExtra("index", -1);
        if(index != -1)
        {
            setTitle(getString(R.string.header_edit_plant));
            editTextTitle.setText(intent.getStringExtra("title"));
            editTextDescription.setText(intent.getStringExtra("description"));
            numberPickerNumber.setValue(intent.getIntExtra("number", 1));
        }
        iconIndex = intent.getIntExtra("icon", 0);
        imageButtonIcon.setImageResource(IconResourceFinder.getIconResIdByIndex(iconIndex));
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

    @Override
    public void onClick(View v)
    {
        if(v.getId() == R.id.image_button_icon)
        {
            showIconSelect();
        }
    }

    private void showIconSelect()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.action_select_icon);
        String[] plantVariants = new String[] { "Растение 0", "Растение 1", "Растение 2", "Растение 3" };
        builder.setItems(plantVariants, this);
        builder.setNegativeButton(R.string.action_cancel, this);
        builder.show();
    }

    private void saveNote() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        int number = numberPickerNumber.getValue();

        if (title.trim().isEmpty()) {
            Toast.makeText(this, "Пожалуйсте, заполните поле", Toast.LENGTH_SHORT).show();
            return;
        }
        CollectionReference notebookRef = FirebaseFirestore.getInstance()
                .collection("Notebook");
        notebookRef.add(new Note(title, description, number, iconIndex));
        Toast.makeText(this, "Запись добавлена", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK, new Intent().putExtra("index", index));
        finish();
    }

    @Override
    public void onClick(DialogInterface dialog, int which)
    {
        iconIndex = which;
        imageButtonIcon.setImageResource(IconResourceFinder.getIconResIdByIndex(iconIndex));
    }
}
