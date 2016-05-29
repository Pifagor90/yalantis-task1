package ua.dp.strahovik.yalantistask1.presenters;


import android.content.Context;

import com.facebook.AccessToken;
import com.facebook.Profile;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import ua.dp.strahovik.yalantistask1.data.DataManager;
import ua.dp.strahovik.yalantistask1.view.activity.FbProfileMvpView;

public class FbProfilePresenter extends BasePresenter<FbProfileMvpView> {

    private final DataManager mDataManager;
    private Subscription mSubscription;

    public FbProfilePresenter(Context appContext) {
        mDataManager = DataManager.getInstance(appContext);
    }

    @Override
    public void attachView(FbProfileMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    public void loadProfile() {
        checkViewAttached();
        mSubscription = mDataManager.getFbProfile()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Profile>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showError(e);
                    }

                    @Override
                    public void onNext(Profile profile) {
                        getMvpView().showProfile(profile);
                    }
                });
    }

    public void loadAccessToken(Context context) {
        checkViewAttached();
        mSubscription = mDataManager.getAccessToken(context)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<AccessToken>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showError(e);
                    }

                    @Override
                    public void onNext(AccessToken accessToken) {
                        getMvpView().accessTokenChanged(accessToken);
                    }
                });
    }

    public void loadFbMePhotos() {
        checkViewAttached();
        mSubscription = mDataManager.getFbPhotos()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<String>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showError(e);
                    }

                    @Override
                    public void onNext(List<String> photosUriList) {
                        if (photosUriList.isEmpty()) {
                            getMvpView().showEmptyPhotosList();
                        } else {
                            getMvpView().showPhotos(photosUriList);
                        }

                    }
                });
    }
}
