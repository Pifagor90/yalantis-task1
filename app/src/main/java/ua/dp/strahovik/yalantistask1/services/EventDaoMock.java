/*
Copyright info
*/


package ua.dp.strahovik.yalantistask1.services;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;

import ua.dp.strahovik.yalantistask1.entities.Event;

public class EventDaoMock implements EventDao {

    private static CompanyDao sCompanyDao = new CompanyDaoMock(); //[Comment] Why is it static?

    private static Event eventFactory (String id){
        Event event = new Event();
        event.setId(id);
        event.setCreationDate(new Date(System.currentTimeMillis() - (5 * 1000 * 60 * 60 * 24))); //[Comment] Magic numbers
        event.setDeadlineDate(new Date(System.currentTimeMillis() - (1 * 1000 * 60 * 60 * 24))); //[Comment] Magic numbers
        event.setRegistrationDate(new Date(System.currentTimeMillis() - (4 * 1000 * 60 * 60 * 24))); //[Comment] Magic numbers
        event.setDescription("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt" +
                " ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi" +
                " ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum " +
                "dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia" +
                " deserunt mollit anim id est laborum."); //[Comment] Hardcode
        event.setResponsible(sCompanyDao.getCompanyByName("Dnipr MVK"));//[Comment] Hardcode
        event.setEventState("In progress"); //[Comment] Hardcode

        ArrayList<URI> list = new ArrayList<URI>(); //[Comment] Use abstraction instead of realization.
        try {
            list.add(new URI("http://i.imgur.com/JnogEyO.jpg")); //[Comment] Hardcode
            list.add(new URI("http://i.imgur.com/45M76q0.jpg")); //[Comment] Hardcode
            list.add(new URI("http://i.imgur.com/OB5xinf.jpg")); //[Comment] Hardcode
            list.add(new URI("http://i.imgur.com/HkdMadu.png")); //[Comment] Hardcode
        }  catch (URISyntaxException NOP) {} //[Comment] What will happen if catch will work

        event.setPhotos(list);

        return event;
    }

    @Override
    public Event getEventById(String id) {
        return eventFactory(id);
    }
}
