package ua.dp.strahovik.yalantistask1.data.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.net.URI;
import java.util.Collection;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import ua.dp.strahovik.yalantistask1.entities.Event;


public class DatabaseHelper {

    private final BriteDatabase mDb;
    private final Context mAppContext;
    private static DatabaseHelper mDatabaseHelper;
    private static final Object sMutex = new Object();

    public static DatabaseHelper getInstance(Context appContext) {
        synchronized (sMutex) {
            if (mDatabaseHelper == null) {
                mDatabaseHelper = new DatabaseHelper(appContext);
            }
            return mDatabaseHelper;
        }
    }

    private DatabaseHelper(Context appContext) {
        mDb = SqlBrite.create().wrapDatabaseHelper(new DbOpenHelper(appContext));
        mAppContext = appContext;
    }

    /*
    TODO: Batch insert if below is too slow, tho it will break architecture
    http://stackoverflow.com/questions/3860008/bulk-insertion-on-android-device/32288474#32288474
    */
    public Observable<Event> setEvents(final Collection<Event> newEvents) {
        return Observable.create(new Observable.OnSubscribe<Event>() {
            @Override
            public void call(Subscriber<? super Event> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    mDb.delete(Db.EventsTable.TABLE_NAME, null);
                    mDb.delete(Db.EventsImagesTable.TABLE_NAME, null);
                    for (Event event : newEvents) {
                        long result = mDb.insert(Db.EventsTable.TABLE_NAME,
                                Db.EventsTable.toContentValues(event), SQLiteDatabase.CONFLICT_REPLACE);
                        if (result >= 0) {
                            for (URI image : event.getPhotos()) {
                                mDb.insert(Db.EventsImagesTable.TABLE_NAME,
                                        Db.EventsImagesTable.toContentValues(event.getId(), image),
                                        SQLiteDatabase.CONFLICT_REPLACE);
                            }
                            subscriber.onNext(event);
                        }
                    }
                    transaction.markSuccessful();
                    subscriber.onCompleted();
                } finally {
                    transaction.end();
                }
            }
        });
    }

    public Observable<List<Event>> getEventListByStateWithoutPhotos(final String eventState) {
        return mDb.createQuery(Db.EventsTable.TABLE_NAME, "SELECT * FROM " + Db.EventsTable.TABLE_NAME +
                " WHERE " + Db.EventsTable.COLUMN_EVENT_STATE + " = '" + eventState + "'")
                .mapToList(new Func1<Cursor, Event>() {
                    @Override
                    public Event call(Cursor cursor) {
                        return Db.EventsTable.parseCursor(cursor);
                    }
                });
    }

    public Observable<Event> getEventById(String eventId) {
        return mDb.createQuery(Db.EventsTable.TABLE_NAME, "SELECT * FROM " + Db.EventsTable.TABLE_NAME +
                " WHERE " + Db.EventsTable.COLUMN_ID + " = '" + eventId + "'")
                .mapToOne(new Func1<Cursor, Event>() {
                    @Override
                    public Event call(Cursor cursor) {
                        Event event = Db.EventsTable.parseCursor(cursor);
                        Cursor csr = mDb.query("SELECT " + Db.EventsImagesTable.COLUMN_IMAGE_URL + " FROM "
                                + Db.EventsImagesTable.TABLE_NAME +
                                " WHERE " + Db.EventsImagesTable.COLUMN_EVENT_ID + " = '" + event.getId() + "'");
                        return Db.EventsImagesTable.parseCursor(csr, event, mAppContext);
                    }
                });
    }
}

