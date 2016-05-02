package ua.dp.strahovik.yalantistask1.presenters;


public interface Presenter <V extends MvpView> {

    void attachView(V mvpView);

    void detachView();
}
