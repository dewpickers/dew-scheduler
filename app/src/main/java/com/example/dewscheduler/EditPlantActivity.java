package com.example.dewscheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

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
        menuInflater.inflate(R.menu.new_plant_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                savePlant();
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
        builder.setItems(R.array.plant_types, this);
        builder.setNegativeButton(R.string.action_cancel, this);
        builder.show();
    }

    private void savePlant() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        int number = numberPickerNumber.getValue();

        // check input for correctness
        if (title.trim().isEmpty()) {
            Toast.makeText(this, R.string.warn_fill_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        Plant nPlant = new Plant(title, description, number, iconIndex, LocalDateTime.now());

        // prepare notification
        Notification.Builder builder = new Notification.Builder(this, "add_plant");
        builder.setContentTitle("It is time to water!");
        builder.setContentText(title);
        builder.setSmallIcon(IconResourceFinder.getIconResIdByIndex(iconIndex));
        Notification notification = builder.build();

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = nPlant.getWateringDate().toInstant(ZoneOffset.MAX).toEpochMilli();
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);

        // send data to db
        CollectionReference plantsRef = FirebaseFirestore.getInstance()
                .collection("plants");
        plantsRef.add(nPlant);

        // get confirmation for successful plant editing
        Toast.makeText(this, R.string.save_plant, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, nPlant.getWateringDate().toString(), Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK, new Intent().putExtra("index", index));

        finish();
    }

    @Override
    public void onClick(DialogInterface dialog, int which)
    {
        iconIndex = which;
        imageButtonIcon.setImageResource(IconResourceFinder.getIconResIdByIndex(iconIndex));
    }

    public void onWaterButtonClick(View view)
    {

        Toast.makeText(this, "Flower is watered", Toast.LENGTH_SHORT).show();
    }
}
