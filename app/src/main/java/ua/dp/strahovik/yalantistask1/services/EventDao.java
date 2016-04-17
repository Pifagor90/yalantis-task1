/*
Copyright info
*/


package ua.dp.strahovik.yalantistask1.services;


import java.util.List;

import ua.dp.strahovik.yalantistask1.entities.Event;

public interface EventDao {
    Event getEventById(String id);


    List<Event> getListEventByEventState(String eventState);
}
