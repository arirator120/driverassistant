package com.example.main.LichSu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.app.AlertDialog.Builder;
import android.widget.Switch;
import android.widget.Toast;

import com.example.main.LichSu.Event;
import com.example.main.LichSu.EventAdapter;
import com.example.main.LichSu.EventDatabase;
import com.example.main.LichSu.NewEvent;
import com.example.main.R;

import java.util.List;

public class LichsuActivity extends AppCompatActivity {

    public static final String INTENT_EVENT_ACTION = "INTENT_EVENT_ACTION";
    public static final String INTENT_EVENT_ID = "EVENT_ID";

    public static final String INTENT_NEW_EVENT_ACTION = "NEW";
    public static final String INTENT_EDIT_EVENT_ACTION = "EDIT";

    private ListView mListView;
    private EventAdapter mAdapter;
    private List<Event> mData;
    private EventDatabase mDbHelper;
    private Switch switchEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lichsu);

        mListView = findViewById(R.id.listView);

        mDbHelper = new EventDatabase(this);

        mDbHelper.createDefaultEvents();
        registerForContextMenu(mListView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu,menu);

        MenuItem menuItem = menu.findItem(R.id.miSwitch);
        menuItem.setActionView(R.layout.actionbar_switch);

        switchEvent = menuItem.getActionView().findViewById(R.id.sw_event);
        switchEvent.setChecked(false);

        switchEvent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                showAllEventsFromDatabase();
            }
        });
        showAllEventsFromDatabase();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.miDeleteAll) {
            showConfirmDialog();
        } else if (item.getItemId() == R.id.miCreate){
            openCreateNewEvent();
        }
        if (item.getItemId() == R.id.item_delete) {
            onContextItemSelected(item);
        } else if (item.getItemId() == R.id.item_edit) {
            onContextItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAllEventsFromDatabase(){
        mData = mDbHelper.getAllEvents(switchEvent.isChecked());

        mAdapter = new EventAdapter(this, R.layout.row_event, mData, mDbHelper);
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void openCreateNewEvent() {

        Intent newEventIntent = new Intent(LichsuActivity.this, NewEvent.class);
        newEventIntent.putExtra(INTENT_EVENT_ACTION,INTENT_NEW_EVENT_ACTION);
        startActivityForResult(newEventIntent,100);
    }

    private void showConfirmDialog() {
        Builder builder = new Builder(this);
        builder.setTitle("Confirm");
        builder.setMessage("Bạn có muốn xóa tất cả không?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDbHelper.deleteAllEvents();
                showAllEventsFromDatabase();
                Toast.makeText(LichsuActivity.this, "Đã xóa tất cả!", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog confirmDialog = builder.create();
        confirmDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && requestCode == Activity.RESULT_OK) {
            showAllEventsFromDatabase();
        }
    }
}