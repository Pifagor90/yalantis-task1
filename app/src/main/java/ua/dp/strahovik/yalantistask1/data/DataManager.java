package ua.dp.strahovik.yalantistask1.data;


import android.content.Context;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import ua.dp.strahovik.yalantistask1.data.database.DatabaseHelper;
import ua.dp.strahovik.yalantistask1.entities.Event;
import ua.dp.strahovik.yalantistask1.services.EventFactory;

public class DataManager {

    private final EventFactory mEventFactory;
    private final DatabaseHelper mDatabaseHelper;
    private static DataManager mDataManager;
    private static final Object sMutex = new Object();

    public static DataManager getInstance(Context appContext){
        synchronized (sMutex){
            if (mDataManager == null){
                mDataManager = new DataManager(appContext);
            }
            return mDataManager;
        }
    }

    private DataManager(Context appContext) {
        mDatabaseHelper = DatabaseHelper.getInstance(appContext);
        mEventFactory = new EventFactory(appContext);
    }


    public Observable<Event> generateEvents() {
        return mEventFactory.getEventList()
                .concatMap(new Func1<List<Event>, Observable<Event>>() {
                    @Override
                    public Observable<Event> call(List<Event> events) {
                        return mDatabaseHelper.setEvents(events);
                    }
                });
    }


    public Observable<Event> getEventById(String eventId) {
        return mDatabaseHelper.getEventById(eventId).distinct();
    }


    public Observable<List<Event>> getEventByEventStateWithoutPhotos(String eventState) {
        return mDatabaseHelper.getEventListByStateWithoutPhotos(eventState).distinct();
    }
}
