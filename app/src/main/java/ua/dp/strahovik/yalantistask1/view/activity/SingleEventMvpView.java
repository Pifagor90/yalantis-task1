package ua.dp.strahovik.yalantistask1.view.activity;


import ua.dp.strahovik.yalantistask1.entities.Event;
import ua.dp.strahovik.yalantistask1.presenters.MvpView;

public interface SingleEventMvpView extends MvpView {
    void showEvent(Event event);

    void showError();
}
