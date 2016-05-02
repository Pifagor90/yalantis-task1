package ua.dp.strahovik.yalantistask1.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import rx.Observer;
import rx.Subscription;
import rx.schedulers.Schedulers;
import ua.dp.strahovik.yalantistask1.data.DataManager;
import ua.dp.strahovik.yalantistask1.entities.Event;

public class EventGenerationService extends Service {

    private Subscription mSubscription;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, EventGenerationService.class);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        DataManager dataManager = DataManager.getInstance(getApplicationContext());
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        mSubscription = dataManager.generateEvents()
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Event>() {
                    @Override
                    public void onCompleted() {
                        stopSelf(startId);
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopSelf(startId);

                    }

                    @Override
                    public void onNext(Event ribot) {
                    }
                });

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mSubscription != null) mSubscription.unsubscribe();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
