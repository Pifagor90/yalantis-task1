package ua.dp.strahovik.yalantistask1.view.activity;


import java.util.List;

import ua.dp.strahovik.yalantistask1.entities.Event;
import ua.dp.strahovik.yalantistask1.presenters.MvpView;

public interface ListEventMvpView extends MvpView {
    void showEventList(List<Event> eventList);

    void showEventListEmpty();

    void showError(Throwable e);
}
