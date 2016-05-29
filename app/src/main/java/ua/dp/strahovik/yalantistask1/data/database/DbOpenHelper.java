package ua.dp.strahovik.yalantistask1.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DbOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ua.dp.strahovik.yalantistask1.events.db";
    public static final int DATABASE_VERSION = 10;

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
            db.execSQL(Db.FbAccessTokenTable.CREATE);
            db.execSQL(Db.FbProfileTable.CREATE);
            db.execSQL(Db.FbAccessTokenPermissionsTable.CREATE);
            db.execSQL(Db.FbMePhotosTable.CREATE);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.beginTransaction();
        try {
            db.execSQL("DROP TABLE IF EXISTS " + Db.EventsTable.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + Db.EventsImagesTable.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + Db.FbAccessTokenTable.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + Db.FbProfileTable.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + Db.FbAccessTokenPermissionsTable.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + Db.FbMePhotosTable.TABLE_NAME);
            db.execSQL("PRAGMA foreign_keys=ON;");
            db.execSQL(Db.EventsTable.CREATE);
            db.execSQL(Db.EventsImagesTable.CREATE);
            db.execSQL(Db.FbAccessTokenTable.CREATE);
            db.execSQL(Db.FbProfileTable.CREATE);
            db.execSQL(Db.FbAccessTokenPermissionsTable.CREATE);
            db.execSQL(Db.FbMePhotosTable.CREATE);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

}