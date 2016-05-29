package ua.dp.strahovik.yalantistask1.data;


import android.content.Context;

import com.facebook.AccessToken;
import com.facebook.Profile;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func3;
import ua.dp.strahovik.yalantistask1.R;
import ua.dp.strahovik.yalantistask1.data.database.DatabaseHelper;
import ua.dp.strahovik.yalantistask1.entities.Event;
import ua.dp.strahovik.yalantistask1.services.remote.EventService;
import ua.dp.strahovik.yalantistask1.services.remote.FbGraphService;

public class DataManager {

    private final EventService mEventService;
    private final DatabaseHelper mDatabaseHelper;
    private static DataManager mDataManager;
    private static final Object sMutex = new Object();
    private final FbGraphService mFbGraphService;

    public static DataManager getInstance(Context appContext) {
        synchronized (sMutex) {
            if (mDataManager == null) {
                mDataManager = new DataManager(appContext);
            }
            return mDataManager;
        }
    }

    private DataManager(Context appContext) {
        mDatabaseHelper = DatabaseHelper.getInstance(appContext);
        mEventService = EventService.Factory.newEventService(appContext);
        mFbGraphService = new FbGraphService();
    }

    public Observable<Event> syncAllEvents(Context context, int amount) {
        Observable<List<Event>> inProgress = mEventService.getEvents(context.getString(R.string.list_event_activity_event_service_request_in_progress), "0", String.valueOf(amount));
        Observable<List<Event>> done = mEventService.getEvents(context.getString(R.string.list_event_activity_event_service_request_done), "0", String.valueOf(amount));
        Observable<List<Event>> pending = mEventService.getEvents(context.getString(R.string.list_event_activity_event_service_request_pending), "0", String.valueOf(amount));
        return Observable
                .zip(inProgress, done, pending, new Func3<List<Event>, List<Event>, List<Event>, List<Event>>() {
                    @Override
                    public List<Event> call(List<Event> eventList, List<Event> eventList2, List<Event> eventList3) {
                        eventList.addAll(eventList2);
                        eventList.addAll(eventList3);
                        return eventList;
                    }
                })
                .concatMap(new Func1<List<Event>, Observable<Event>>() {
                    @Override
                    public Observable<Event> call(List<Event> events) {
                        return mDatabaseHelper.clearAndPersistEvents(events);
                    }
                });
    }

    public Observable<Event> downloadEvents(int[] eventStateId, int offset, int amount) {
        StringBuilder stateQuery = new StringBuilder();
        int eventStateIdArrayLength = eventStateId.length;
        for (int counter = 0; counter < eventStateIdArrayLength; counter++) {
            stateQuery.append(eventStateId[counter]);
            if (counter < eventStateIdArrayLength - 1) {
                stateQuery.append(",");
            }
        }
        return mEventService.getEvents(stateQuery.toString(), String.valueOf(offset), String.valueOf(amount))
                .concatMap(new Func1<List<Event>, Observable<Event>>() {
                    @Override
                    public Observable<Event> call(List<Event> events) {
                        return mDatabaseHelper.persistEvents(events);
                    }
                });
    }


    public Observable<Event> getEventById(int eventId) {
        return mDatabaseHelper.getEventById(eventId).distinct();
    }


    public Observable<List<Event>> getEventByEventStateWithoutPhotos(int[] eventStateId) {
        return mDatabaseHelper.getEventListByStateWithoutPhotos(eventStateId).distinct();
    }


    public Observable<Profile> getFbProfile() {
        return mDatabaseHelper.getProfile().distinct();
    }

    public Observable<Profile> syncFbProfile(AccessToken accessToken) {
        return mFbGraphService.getProfile(accessToken)
                .concatMap(new Func1<Profile, Observable<Profile>>() {
                    @Override
                    public Observable<Profile> call(Profile profile) {
                        return mDatabaseHelper.persistProfile(profile);
                    }
                });
    }

    public Observable<AccessToken> getAccessToken(Context context) {
        return mDatabaseHelper.getAccessToken(context).distinct();
    }

    public Observable<AccessToken> syncFbAccessToken(AccessToken accessToken) {
        return Observable.just(accessToken)
                .concatMap(new Func1<AccessToken, Observable<AccessToken>>() {
                    @Override
                    public Observable<AccessToken> call(AccessToken accessToken) {
                        return mDatabaseHelper.persistAccessToken(accessToken);
                    }
                });
    }

    public Observable<List<String>> getFbPhotos() {
        return mDatabaseHelper.getFbMePhotos().distinct();
    }

    public Observable<String> syncFbPhotos(AccessToken accessToken, int expectedHeight,
                                           int expectedWidth) {
        return mFbGraphService.getPhotos(accessToken, expectedHeight, expectedWidth)
                .concatMap(new Func1<List<String>, Observable<String>>() {
                    @Override
                    public Observable<String> call(List<String> photosUriList) {
                        return mDatabaseHelper.persistFbMePhotos(photosUriList);
                    }
                });
    }
}
