package ua.dp.strahovik.yalantistask1.data.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;

import ua.dp.strahovik.yalantistask1.R;
import ua.dp.strahovik.yalantistask1.entities.Company;
import ua.dp.strahovik.yalantistask1.entities.Event;

public class Db {

    public Db() {
    }

    public abstract static class EventsTable {
        public static final String TABLE_NAME = "events";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_EVENT_STATE = "event_state";
        public static final String COLUMN_CREATION_DATE = "creation_date";
        public static final String COLUMN_REGISTRATION_DATE = "registration_date";
        public static final String COLUMN_DEADLINE_DATE = "deadline_date";
        public static final String COLUMN_RESPONSIBLE_COMPANY = "responsible_company";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_EVENT_TYPE = "event_type";
        public static final String COLUMN_LIKES = "likes";
        public static final String COLUMN_ADDRESS = "address";

        public static final String CREATE = (
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_ID + " TEXT PRIMARY KEY, " +
                        COLUMN_EVENT_STATE + " TEXT, " +
                        COLUMN_CREATION_DATE + " INTEGER NOT NULL, " +
                        COLUMN_REGISTRATION_DATE + " INTEGER, " +
                        COLUMN_DEADLINE_DATE + " INTEGER, " +
                        COLUMN_RESPONSIBLE_COMPANY + " TEXT, " +
                        COLUMN_DESCRIPTION + " TEXT, " +
                        COLUMN_EVENT_TYPE + " TEXT, " +
                        COLUMN_LIKES + " TEXT, " +
                        COLUMN_ADDRESS + " TEXT" +
                        " ); "
        );

        public static ContentValues toContentValues(Event event) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ID, event.getId());
            values.put(COLUMN_EVENT_STATE, event.getEventState());
            values.put(COLUMN_CREATION_DATE, event.getCreationDate().getTime());
            values.put(COLUMN_REGISTRATION_DATE, event.getRegistrationDate().getTime());
            values.put(COLUMN_DEADLINE_DATE, event.getDeadlineDate().getTime());
            values.put(COLUMN_RESPONSIBLE_COMPANY, event.getResponsible().getName());
            values.put(COLUMN_DESCRIPTION, event.getDescription());
            values.put(COLUMN_EVENT_TYPE, event.getEventType());
            values.put(COLUMN_LIKES, event.getLikeCounter());
            values.put(COLUMN_ADDRESS, event.getAddress());
            return values;
        }

        public static Event parseCursor(Cursor cursor) {
            Event event = new Event();
            event.setId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            event.setEventState(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_STATE)));
            event.setCreationDate(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_CREATION_DATE))));
            event.setRegistrationDate(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_REGISTRATION_DATE))));
            event.setDeadlineDate(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_DEADLINE_DATE))));
            event.setResponsible(new Company(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RESPONSIBLE_COMPANY))));
            event.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
            event.setEventType(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_TYPE)));
            event.setLikeCounter(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LIKES)));
            event.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS)));
            return event;
        }
    }

    public abstract static class EventsImagesTable {
        public static final String TABLE_NAME = "events_images";

        public static final String COLUMN_EVENT_ID = "event_id";
        public static final String COLUMN_IMAGE_URL = "image_url";

        public static final String CREATE = (
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_EVENT_ID + " TEXT, " +
                        COLUMN_IMAGE_URL + " TEXT, " +
                        "FOREIGN KEY(" + COLUMN_EVENT_ID + ") REFERENCES " + EventsTable.TABLE_NAME +
                        "(" + EventsTable.COLUMN_ID + ")" + " ON DELETE CASCADE" + " ON UPDATE CASCADE" +
                        " ); "
        );

        public static ContentValues toContentValues(String id, URI uri) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_EVENT_ID, id);
            values.put(COLUMN_IMAGE_URL, uri.toString());
            return values;
        }

        public static Event parseCursor(Cursor cursor, Event event, Context context) {
            ArrayList<URI> photos = new ArrayList<>();
            while (cursor.moveToNext()) {
                try {
                    photos.add(new URI(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL))));
                } catch (URISyntaxException e) {
                    Log.e(context.getString(R.string.log_tag),
                            context.getString(R.string.error_uri_exception) + e);
                }
            }
            event.setPhotos(photos);
            return event;
        }
    }
}