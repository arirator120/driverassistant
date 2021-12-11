package com.example.main.LichSu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EventDatabase extends SQLiteOpenHelper {

    private static final String TAG  = "SQLite";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "LichSu";

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE_EVENTS + "("
            + FeedReaderContract.FeedEntry.COLUMN_EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + FeedReaderContract.FeedEntry.COLUMN_EVENT_NAME + " TEXT NOT NULL,"
            + FeedReaderContract.FeedEntry.COLUMN_DATE + " TEXT, "
            + FeedReaderContract.FeedEntry.COLUMN_PLACE + " TEXT, "
            + FeedReaderContract.FeedEntry.COLUMN_COST + " INTEGER DEFAULT 0,"
            + FeedReaderContract.FeedEntry.COLUMN_DONE + " INTEGER DEFAULT 0)";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.TABLE_EVENTS;

    public  final class FeedReaderContract {
        private FeedReaderContract() {}

        public class FeedEntry implements BaseColumns {
            private static final String TABLE_EVENTS = "events";
            private static final String COLUMN_EVENT_ID = "ID";
            private static final String COLUMN_EVENT_NAME = "NAME";
            private static final String COLUMN_DATE = "DATE";
            private static final String COLUMN_PLACE = "PLACE";
            private static final String COLUMN_COST = "COST";
            private static final String COLUMN_DONE = "DONE";
        }
    }


    public EventDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void createDefaultEvents() {
        int count = this.getEventsCount();
        if (count == 0) {
            Event event1 = new Event("Do Xang","5/5/2021","TPHCM",40000);
            Event event2 = new Event("Thay Nhot", "1/1/2021","LONG AN",50000);
            this.addEvent(event1);
            this.addEvent(event2);
        }
    }

    public void addEvent (Event event){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_EVENT_NAME,event.getTenCV());
        values.put(FeedReaderContract.FeedEntry.COLUMN_DATE, event.getDate());
        values.put(FeedReaderContract.FeedEntry.COLUMN_PLACE, event.getPlace());
        values.put(FeedReaderContract.FeedEntry.COLUMN_COST, event.getVnd());
        values.put(FeedReaderContract.FeedEntry.COLUMN_DONE, event.getCompleted());

        long newRowID = db.insert(FeedReaderContract.FeedEntry.TABLE_EVENTS,null, values);
        db.close();
    }

    public Event getEvent(int id) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(FeedReaderContract.FeedEntry.TABLE_EVENTS, new String[]{
                        FeedReaderContract.FeedEntry.COLUMN_EVENT_ID,
                        FeedReaderContract.FeedEntry.COLUMN_EVENT_NAME,
                        FeedReaderContract.FeedEntry.COLUMN_DATE,
                        FeedReaderContract.FeedEntry .COLUMN_PLACE,
                        FeedReaderContract.FeedEntry.COLUMN_COST,
                        FeedReaderContract.FeedEntry.COLUMN_DONE
        }, FeedReaderContract.FeedEntry.COLUMN_EVENT_ID + "=?",
                new String[]{String.valueOf(id)},null,null,null,null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        Event event = new Event();
        event.setId(cursor.getInt(0));
        event.setTenCV(cursor.getString(1));
        event.setDate(cursor.getString(2));
        event.setPlace(cursor.getString(3));
        event.setVnd(cursor.getInt(4));
        event.setCompleted(cursor.getInt(5) == 1 );
        return event;
    }

    public List<Event> getAllEvents(boolean complete){

        int completeNumber = complete ? 1 : 0;
        List<Event> eventList = new ArrayList<Event>();

        String selectQuery = "SELECT * FROM " + FeedReaderContract.FeedEntry.TABLE_EVENTS;
        selectQuery += " WHERE " + FeedReaderContract.FeedEntry.COLUMN_DONE + " = " + completeNumber;


        SQLiteDatabase db;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        if (cursor.moveToFirst()) {
            do {
                Event event = new Event();
                event.setId(cursor.getInt(0));
                event.setTenCV(cursor.getString(1));
                event.setDate(cursor.getString(2));
                event.setPlace(cursor.getString(3));
                event.setVnd(cursor.getInt(4));
                event.setCompleted(cursor.getInt(5) ==1 );
                eventList.add(event);
            } while (cursor.moveToNext());
        }
        db.close();
        return eventList;
    }

    public int getEventsCount(){

        String countQuery = "SELECT * FROM " + FeedReaderContract.FeedEntry.TABLE_EVENTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery,null);

        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int updateEvent(Event updateNote) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_EVENT_NAME, updateNote.getTenCV());
        values.put(FeedReaderContract.FeedEntry.COLUMN_DATE,updateNote.getDate());
        values.put(FeedReaderContract.FeedEntry.COLUMN_DONE,updateNote.getCompleted());

        return db.update(FeedReaderContract.FeedEntry.TABLE_EVENTS,values, FeedReaderContract.FeedEntry.COLUMN_EVENT_ID + "= ?",
                new String[]{String.valueOf(updateNote.getId())});
    }

    public void deleteEvent(Event event){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FeedReaderContract.FeedEntry.TABLE_EVENTS, FeedReaderContract.FeedEntry.COLUMN_EVENT_ID + "= ?",
                new String[]{String.valueOf(event.getId())});
        db.close();
    }

    public void deleteAllEvents() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("delete from " + FeedReaderContract.FeedEntry.TABLE_EVENTS);
        db.close();
    }

}
