/*
Copyright info
*/


package ua.dp.strahovik.yalantistask1.services;


import ua.dp.strahovik.yalantistask1.entities.Event;

public interface EventDao {
    public Event getEventById(String id); //[Comment] public is default modifier
}
