package ua.dp.strahovik.yalantistask1.presenters;


import android.content.Context;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import ua.dp.strahovik.yalantistask1.data.DataManager;
import ua.dp.strahovik.yalantistask1.entities.Event;
import ua.dp.strahovik.yalantistask1.view.activity.SingleEventMvpView;

public class SingleEventPresenter extends BasePresenter<SingleEventMvpView> {

    private final DataManager mDataManager;
    private Subscription mSubscription;

    public SingleEventPresenter(Context appContext) {
        mDataManager = DataManager.getInstance(appContext);
    }

    @Override
    public void attachView(SingleEventMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    public void loadEventById(int eventId) {
        checkViewAttached();
        mSubscription = mDataManager.getEventById(eventId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Event>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showError();
                    }

                    @Override
                    public void onNext(Event event) {
                        getMvpView().showEvent(event);
                    }
                });
    }

}