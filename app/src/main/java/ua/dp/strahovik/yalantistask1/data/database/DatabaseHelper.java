package ua.dp.strahovik.yalantistask1.data.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.net.URI;
import java.util.Collection;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import ua.dp.strahovik.yalantistask1.entities.Event;
import ua.dp.strahovik.yalantistask1.entities.helpers.MutableFbAccessToken;


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

    public Observable<Event> persistEvents(final Collection<Event> newEvents) {
        return Observable.create(new Observable.OnSubscribe<Event>() {
            @Override
            public void call(Subscriber<? super Event> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
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

    public Observable<Event> clearAndPersistEvents(final Collection<Event> newEvents) {
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

    public Observable<List<Event>> getEventListByStateWithoutPhotos(final int[] eventStateId) {
        StringBuilder query = new StringBuilder("SELECT * FROM ");
        query.append(Db.EventsTable.TABLE_NAME);
        query.append(" WHERE ");
        int eventStateIdArrayLength = eventStateId.length;
        for (int counter = 0; counter < eventStateIdArrayLength; counter++) {
            query.append(Db.EventsTable.COLUMN_EVENT_STATE_ID);
            query.append(" = '");
            query.append(String.valueOf(eventStateId[counter]));
            query.append("'");
            if (counter < eventStateIdArrayLength - 1) {
                query.append(" OR ");
            }
        }
        return mDb.createQuery(Db.EventsTable.TABLE_NAME, query.toString())
                .mapToList(new Func1<Cursor, Event>() {
                    @Override
                    public Event call(Cursor cursor) {
                        return Db.EventsTable.parseCursor(cursor);
                    }
                });
    }

    public Observable<Event> getEventById(int eventId) {
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

    public Observable<AccessToken> getAccessToken(final Context context) {
        return mDb.createQuery(Db.FbAccessTokenTable.TABLE_NAME, "SELECT * FROM " + Db.FbAccessTokenTable.TABLE_NAME)
                .mapToOne(new Func1<Cursor, AccessToken>() {
                    @Override
                    public AccessToken call(Cursor cursor) {
                        MutableFbAccessToken mutableFbAccessToken = Db.FbAccessTokenTable.parseCursor(cursor, context);
                        Cursor csr = mDb.query("SELECT " + Db.FbAccessTokenPermissionsTable.COLUMN_PERMISSION +
                                 ", " + Db.FbAccessTokenPermissionsTable.COLUMN_IS_DECLINED + " FROM "
                                + Db.FbAccessTokenPermissionsTable.TABLE_NAME + " WHERE " +
                                Db.FbAccessTokenPermissionsTable.COLUMN_TOKEN + " = '" + mutableFbAccessToken.getToken()
                                + "'");
                        return Db.FbAccessTokenPermissionsTable.parseCursor(csr, mutableFbAccessToken).getAccessToken();
                    }
                });
    }

    public Observable<Profile> getProfile() {
        return mDb.createQuery(Db.FbProfileTable.TABLE_NAME, "SELECT * FROM " + Db.FbProfileTable.TABLE_NAME)
                .mapToOne(new Func1<Cursor, Profile>() {
                    @Override
                    public Profile call(Cursor cursor) {
                        return Db.FbProfileTable.parseCursor(cursor);
                    }
                });
    }

    public Observable<AccessToken> persistAccessToken(final AccessToken accessToken) {
        return Observable.create(new Observable.OnSubscribe<AccessToken>() {
            @Override
            public void call(Subscriber<? super AccessToken> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    mDb.delete(Db.FbAccessTokenTable.TABLE_NAME, null);
                    mDb.delete(Db.FbAccessTokenPermissionsTable.TABLE_NAME, null);
                    long result = mDb.insert(Db.FbAccessTokenTable.TABLE_NAME,
                            Db.FbAccessTokenTable.toContentValues(accessToken), SQLiteDatabase.CONFLICT_REPLACE);
                    if (result >= 0) {
                        for (String permission : accessToken.getPermissions()) {
                            mDb.insert(Db.FbAccessTokenPermissionsTable.TABLE_NAME,
                                    Db.FbAccessTokenPermissionsTable.toContentValues(accessToken, permission,
                                            Db.FbAccessTokenPermissionsTable.IS_DECLINED_FALSE),
                                    SQLiteDatabase.CONFLICT_REPLACE);
                        }
                        for (String permission : accessToken.getDeclinedPermissions()) {
                            mDb.insert(Db.FbAccessTokenPermissionsTable.TABLE_NAME,
                                    Db.FbAccessTokenPermissionsTable.toContentValues(accessToken, permission,
                                            Db.FbAccessTokenPermissionsTable.IS_DECLINED_TRUE),
                                    SQLiteDatabase.CONFLICT_REPLACE);
                        }
                        subscriber.onNext(accessToken);
                    }
                    transaction.markSuccessful();
                    subscriber.onCompleted();
                } finally {
                    transaction.end();
                }
            }
        });
    }

    public Observable<Profile> persistProfile(final Profile profile) {
        return Observable.create(new Observable.OnSubscribe<Profile>(){
            @Override
            public void call(Subscriber<? super Profile> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    mDb.delete(Db.FbProfileTable.TABLE_NAME, null);
                    long result = mDb.insert(Db.FbProfileTable.TABLE_NAME,
                            Db.FbProfileTable.toContentValues(profile), SQLiteDatabase.CONFLICT_REPLACE);
                    if (result >= 0) {
                        subscriber.onNext(profile);
                    }
                    transaction.markSuccessful();
                    subscriber.onCompleted();
                } finally {
                    transaction.end();
                }
            }
        });
    }

    public Observable<List<String>> getFbMePhotos() {
        return mDb.createQuery(Db.FbMePhotosTable.TABLE_NAME, "SELECT * FROM " + Db.FbMePhotosTable.TABLE_NAME)
                .mapToList(new Func1<Cursor, String>() {
                    @Override
                    public String call(Cursor cursor) {
                        return Db.FbMePhotosTable.parseCursor(cursor);
                    }
                });
    }

    public Observable<String> persistFbMePhotos(final List<String> photosUriList) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    mDb.delete(Db.FbMePhotosTable.TABLE_NAME, null);
                    for (String uri: photosUriList){
                         long result = mDb.insert(Db.FbMePhotosTable.TABLE_NAME,
                                Db.FbMePhotosTable.toContentValues(uri), SQLiteDatabase.CONFLICT_REPLACE);
                        if (result >= 0) {
                            subscriber.onNext(uri);
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
}

