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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import ua.dp.strahovik.yalantistask1.R;
import ua.dp.strahovik.yalantistask1.entities.Event;

public class EventDaoMock implements EventDao {
/*
    Todo: if its consuming too much resources rework everything to return j.u.c.Future or through
    asyncTask or through mainThread --> service --> contentProvider
 */

    private static Map<String, List<Event>> sCache = new HashMap<>(3);
    private Context mContext;
    private CompanyDaoMock mCompanyDaoMock = new CompanyDaoMock();

    private final String[] mUriAsString;
    private final String[] mEventType;
    private final String[] mStreets;
    private final String[] mEventState;
    private final int mNumberOfEventsToBeGenerated;
    private final int mRandomCoefficient;
    private final int lengthOfGeneratedIdFromEventStateToSubstring;


    public EventDaoMock(Context context) {
        mContext = context;
        mUriAsString = mContext.getResources().getStringArray(R.array.EventDaoMock_URI_array);
        mStreets = mContext.getResources().getStringArray(R.array.EventStreet_array);
        mEventType = mContext.getResources().getStringArray(R.array.EventType_array);
        mEventState = mContext.getResources().getStringArray(R.array.EventState_array);
        mNumberOfEventsToBeGenerated = mContext.getResources().getInteger(R.integer.numberOfEventsToBeGenerated);
        mRandomCoefficient = mContext.getResources().getInteger(R.integer.randomCoefficient);
        lengthOfGeneratedIdFromEventStateToSubstring = mContext.getResources().getInteger(R.integer.
                lengthOfGeneratedIdFromEventStateToSubstring);
    }

    private Event eventFactory(String id, double random) {
        Event event = new Event();
        event.setId(id);
        event.setCreationDate(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(5)));
        event.setDeadlineDate(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)));
        event.setRegistrationDate(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(4)));
        event.setDescription(mContext.getString(R.string.EventDaoMock_description));
        event.setResponsible(mCompanyDaoMock.getCompanyByName(mContext.getString(R.string.EventDaoMock_company_name)));
        event.setEventState(mEventState[(int) (mEventState.length * random)]);

        List<URI> list = new ArrayList<URI>();
        try {
            for (String element : mUriAsString) {
                list.add(new URI(element));
            }
        } catch (URISyntaxException e) {
            Log.e(mContext.getString(R.string.Log_tag), mContext.getString(R.string.EventDaoMock_URI_exception) + e);
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

    @Override
    public Event getEventById(String id) {
        if (sCache.isEmpty()) {
            return eventFactory(id, Math.random());
        } else {
            for (Map.Entry<String, List<Event>> entry : sCache.entrySet()) {
                for (Event event : entry.getValue()) {
                    if (event.getId().equals(id)) {
                        return event;
                    }
                }
            }
        }
        return eventFactory(id, Math.random());
    }

    @Override
    public List<Event> getListEventByEventState(String eventState) {
        List<Event> list;
        if (sCache.isEmpty() || !sCache.containsKey(eventState)) {
            list = generateEventList(eventState);
        } else {
            list = sCache.get(eventState);
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
        sCache.put(eventState, list);
        return list;
    }
}
