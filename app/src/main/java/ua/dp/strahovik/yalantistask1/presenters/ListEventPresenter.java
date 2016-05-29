package ua.dp.strahovik.yalantistask1.presenters;


import android.content.Context;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import ua.dp.strahovik.yalantistask1.data.DataManager;
import ua.dp.strahovik.yalantistask1.entities.Event;
import ua.dp.strahovik.yalantistask1.view.activity.ListEventMvpView;

public class ListEventPresenter extends BasePresenter<ListEventMvpView> {

    private final DataManager mDataManager;
    private Subscription mSubscription;

    public ListEventPresenter(Context appContext) {
        mDataManager = DataManager.getInstance(appContext);
    }

    @Override
    public void attachView(ListEventMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    public void loadEventListByEventStateWithoutPhotos(int[] eventStateId) {
        checkViewAttached();
        mSubscription = mDataManager.getEventByEventStateWithoutPhotos(eventStateId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Event>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showError(e);
                    }

                    @Override
                    public void onNext(List<Event> eventList) {
                        if (eventList.isEmpty()) {
                            getMvpView().showEventListEmpty();
                        } else {
                            getMvpView().showEventList(eventList);
                        }
                    }
                });
    }
}
