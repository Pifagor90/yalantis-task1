/*
Copyright info
*/


package ua.dp.strahovik.yalantistask1.services;


import android.content.Context;
import android.util.Log;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ua.dp.strahovik.yalantistask1.R;
import ua.dp.strahovik.yalantistask1.entities.Event;

public class EventDaoMock implements EventDao {

    private Context mContext;
    private CompanyDaoMock mCompanyDaoMock = new CompanyDaoMock();

    public EventDaoMock(Context context) {
        mContext = context;
    }

    private Event eventFactory (String id){
        Event event = new Event();
        event.setId(id);
        final int dayInMillis = 1000 * 60 * 60 * 24; //Not sure if this has to be transferred to R.integers
        event.setCreationDate(new Date(System.currentTimeMillis() - (5 * dayInMillis)));
        event.setDeadlineDate(new Date(System.currentTimeMillis() - (1 * dayInMillis)));
        event.setRegistrationDate(new Date(System.currentTimeMillis() - (4 * dayInMillis)));
        event.setDescription(mContext.getString(R.string.EventDaoMock_description));
        event.setResponsible(mCompanyDaoMock.getCompanyByName(mContext.getString(R.string.EventDaoMock_company_name)));
        event.setEventState(mContext.getString(R.string.main_activity_button_in_progress));

        List<URI> list = new ArrayList<>();
        try {
/*            TODO: unfortunately it does not work. find out why
            list.addAll(Arrays.<URI>asList(mContext.getResources().getStringArray(R.string.EventDaoMock_URI));
            */
            list.add(new URI(mContext.getString(R.string.EventDaoMock_URI_1)));
            list.add(new URI(mContext.getString(R.string.EventDaoMock_URI_2)));
            list.add(new URI(mContext.getString(R.string.EventDaoMock_URI_3)));
            list.add(new URI(mContext.getString(R.string.EventDaoMock_URI_4)));
        }  catch (URISyntaxException e) {
            Log.e(mContext.getString(R.string.Log_tag),mContext.getString(R.string.EventDaoMock_URI_exception) + e);
        }

        event.setPhotos(list);

        return event;
    }

    @Override
    public Event getEventById(String id) {
        return eventFactory(id);
    }
}
