/*
Copyright info
*/


package ua.dp.strahovik.yalantistask1.services;


import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import ua.dp.strahovik.yalantistask1.R;
import ua.dp.strahovik.yalantistask1.entities.Event;

public class EventFactory implements Callable<List<Event>> {

    private Context mContext;

    private final String[] mUriAsString;
    private final String[] mEventType;
    private final String[] mStreets;
    private final String[] mEventState;
    private final int mNumberOfEventsToBeGenerated;
    private final int mRandomCoefficient;
    private final int lengthOfGeneratedIdFromEventStateToSubstring;


    public EventFactory(Context context) {
        mContext = context;
        mUriAsString = mContext.getResources().getStringArray(R.array.data_generation_image_uris);
        mStreets = mContext.getResources().getStringArray(R.array.data_generation_event_streets);
        mEventType = mContext.getResources().getStringArray(R.array.event_types);
        mEventState = mContext.getResources().getStringArray(R.array.event_states);
        mNumberOfEventsToBeGenerated = mContext.getResources().getInteger(R.integer.data_generation_number_of_events_to_be_generated);
        mRandomCoefficient = mContext.getResources().getInteger(R.integer.data_generation_random_coefficient);
        lengthOfGeneratedIdFromEventStateToSubstring = mContext.getResources().getInteger(R.integer.
                data_generation_length_of_generated_id_from_event_state_to_substring);
    }

    private Event eventFactory(String id, double random) {
        Event event = new Event();
        event.setId(id);
        event.setCreationDate(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(5)));
        event.setDeadlineDate(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)));
        event.setRegistrationDate(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(4)));
        event.setDescription(mContext.getString(R.string.data_generation_event_description));
        event.setResponsible(CompanyFactory.getCompanyByName(mContext.getString(R.string.data_generation_company_name)));
        event.setEventState(mEventState[(int) (mEventState.length * random)]);

        List<URI> list = new ArrayList<URI>();
        try {
            for (String element : mUriAsString) {
                list.add(new URI(element));
            }
        } catch (URISyntaxException e) {
            Log.e(mContext.getString(R.string.log_tag), mContext.getString(R.string.error_uri_exception) + e);
        }

        event.setPhotos(list);

        event.setLikeCounter((int) (random * mRandomCoefficient));
        event.setAddress((int) (random * mRandomCoefficient) + " " + mStreets[(int) (mStreets.length * random)]);
        event.setEventType(mEventType[(int) (mEventType.length * random)]);

        return event;
    }

    private Event eventFactory(String id, double random, String eventState) {
        Event event = eventFactory(id, random);
        event.setEventState(eventState);
        return event;
    }

    public Observable<List<Event>> getEventList() {
        ExecutorService pool = Executors.newSingleThreadExecutor();
        Future<List<Event>> future = pool.submit(this);
        pool.shutdown();
        return Observable.from(future);
    }

    @Override
    public List<Event> call(){
        List<Event> list = new ArrayList<>();
        for (String eventState : mEventState) {
            list.addAll(generateEventList(eventState));
        }
        return list;
    }

    @NonNull
    private List<Event> generateEventList(String eventState) {
        List<Event> list = new ArrayList<Event>();
        final String startId = eventState.substring(0, lengthOfGeneratedIdFromEventStateToSubstring) + "-";
        for (int counter = 0; counter < mNumberOfEventsToBeGenerated; counter++) {
            list.add(eventFactory(startId + counter, Math.random(), eventState));
        }

        return list;
    }


}
