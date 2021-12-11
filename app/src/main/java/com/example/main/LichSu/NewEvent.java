package com.example.main.LichSu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.main.R;

import java.util.Calendar;

import static com.example.main.LichSu.LichsuActivity.INTENT_EDIT_EVENT_ACTION;
import static com.example.main.LichSu.LichsuActivity.INTENT_EVENT_ACTION;
import static com.example.main.LichSu.LichsuActivity.INTENT_EVENT_ID;
import static com.example.main.LichSu.LichsuActivity.INTENT_NEW_EVENT_ACTION;


public class NewEvent extends AppCompatActivity {

    private EditText etName;
    private TextView etNameError;
    private EditText etDate;
    private EditText etPlace;
    private EditText etCost;
    private EventDatabase mDbHelper;
    boolean isNew = false;
    private Event selectedEvent;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);

        mDbHelper = new EventDatabase(this);

        etName = findViewById(R.id.et_tencv);
        etNameError = findViewById(R.id.tv_name_error);
        etDate = findViewById(R.id.et_date);
        etPlace = findViewById(R.id.et_place);
        etCost = findViewById(R.id.et_thanhtien);

        etNameError.setVisibility(View.INVISIBLE);

        Intent callerIntent = getIntent();
        Bundle callerBunle = callerIntent.getExtras();

        String action = callerBunle.getString(INTENT_EVENT_ACTION,INTENT_NEW_EVENT_ACTION);

         if (action.equals(INTENT_EDIT_EVENT_ACTION)) {
             isNew = false;
             int eventId = callerBunle.getInt(INTENT_EVENT_ID);
             selectedEvent = mDbHelper.getEvent(eventId);
             populateUI(selectedEvent);
         } else {
             isNew = true;
         }

         etDate.setOnTouchListener(new View.OnTouchListener() {
             @Override
             public boolean onTouch(View v, MotionEvent event) {
                 if (MotionEvent.ACTION_UP == event.getAction()){
                     showDateSelectionDialog();
                 }
                 return true;
             }
         });
    }

    private void populateUI (Event selectedEvent){
        etName.setText(selectedEvent.getTenCV());
        etDate.setText(selectedEvent.getDate());
        etPlace.setText(selectedEvent.getPlace());
        etCost.setText(selectedEvent.getVnd());
    }
    private void showDateSelectionDialog(){
        Calendar c = Calendar.getInstance();
        int startYear = c.get(Calendar.YEAR);
        int startMonth = c.get(Calendar.MONTH);
        int startDay = c .get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                StringBuilder stringBuilder = new StringBuilder(dayOfMonth).append(dayOfMonth).append("/").append(month)
                        .append("/").append(year);
                etDate.setText(stringBuilder.toString());
            }
        },startYear,startMonth,startDay);
        datePickerDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.create_option_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.miSave){
            Intent intent = new Intent();
            String eventName = etName.getText().toString();
            String eventDate = etDate.getText().toString();
            String eventPlace= etPlace.getText().toString();
            Integer eventCost = Integer.parseInt(etCost.getText().toString());

            if (eventName.trim().isEmpty()){
                etNameError.setText("Tên không được để trống");
                etNameError.setVisibility(View.VISIBLE);
            } else {
                if (isNew) {
                    Event newEvent = new Event(eventName,eventDate,eventPlace,eventCost);
                    mDbHelper.addEvent(newEvent);
                } else {
                    selectedEvent.setTenCV(eventName);
                    selectedEvent.setDate(eventDate);
                    selectedEvent.setPlace(eventPlace);
                    selectedEvent.setVnd(eventCost);

                    mDbHelper.updateEvent(selectedEvent);
                }
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
