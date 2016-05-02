package ua.dp.strahovik.yalantistask1.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DbOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ua.dp.strahovik.yalantistask1.events.db";
    public static final int DATABASE_VERSION = 2;

    public DbOpenHelper(Context appContext) {
        super(appContext, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            db.execSQL("PRAGMA foreign_keys=ON;");
            db.execSQL(Db.EventsTable.CREATE);
            db.execSQL(Db.EventsImagesTable.CREATE);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

}