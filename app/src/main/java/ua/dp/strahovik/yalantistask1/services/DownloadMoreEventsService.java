package ua.dp.strahovik.yalantistask1.services;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.os.ResultReceiver;
import android.util.Log;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import rx.Observer;
import rx.Subscription;
import rx.schedulers.Schedulers;
import ua.dp.strahovik.yalantistask1.R;
import ua.dp.strahovik.yalantistask1.data.DataManager;
import ua.dp.strahovik.yalantistask1.entities.Event;
import ua.dp.strahovik.yalantistask1.view.activity.ListEventActivity;

public class DownloadMoreEventsService extends Service {

    public static final String EVENT_STATE_ID =
            "ua.dp.strahovik.yalantistask1.services.DownloadMoreEventsService.event_state_id";
    public static final String OFFSET =
            "ua.dp.strahovik.yalantistask1.services.DownloadMoreEventsService.offset";
    public static final String AMOUNT =
            "ua.dp.strahovik.yalantistask1.services.DownloadMoreEventsService.amount";
    public static final String RECEIVER =
            "ua.dp.strahovik.yalantistask1.services.DownloadMoreEventsService.receiver";
    private Map<int[], Subscription> mSubscriptions = new HashMap<>();

    public static Intent getStartIntent(Context context, int[] eventStateId, int offset, int amount,
                                        ResultReceiver receiver) {
        Intent intent = new Intent(context, DownloadMoreEventsService.class);
        intent.putExtra(EVENT_STATE_ID, eventStateId);
        intent.putExtra(OFFSET, offset);
        intent.putExtra(AMOUNT, amount);
        intent.putExtra(RECEIVER, receiver);
        return intent;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {

        final int[] eventStateId = intent.getIntArrayExtra(EVENT_STATE_ID);
        int offset = intent.getIntExtra(OFFSET, 0);
        int amount = intent.getIntExtra(AMOUNT, getResources().getInteger(
                R.integer.list_event_activity_default_amount_of_events_to_be_downloaded));
        final ResultReceiver receiver = intent.getParcelableExtra(RECEIVER);

        Subscription subscription = mSubscriptions.get(eventStateId);
        DataManager dataManager = DataManager.getInstance(getApplicationContext());
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        final Bundle bundle = new Bundle();
        bundle.putIntArray(ListEventActivity.FragmentLoadingStateResultReceiver.ARRAY_REQUEST, eventStateId);
        bundle.putBoolean(ListEventActivity.FragmentLoadingStateResultReceiver.IS_LOADING, false);

        mSubscriptions.put(eventStateId, dataManager.downloadEvents(eventStateId, offset, amount)
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Event>() {
                    @Override
                    public void onCompleted() {
                        Log.i(getString(R.string.log_tag), getString(R.string.msg_download_events_successful)
                                + Arrays.toString(eventStateId));
                        receiver.send(0, bundle);
                        mSubscriptions.remove(eventStateId);
                        stopSelf(startId);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(getString(R.string.log_tag), getString(R.string.error_downloading_events)
                                + Arrays.toString(eventStateId) + " " + e);
                        receiver.send(0, bundle);
                        mSubscriptions.remove(eventStateId);
                        stopSelf(startId);
                    }

                    @Override
                    public void onNext(Event event) {
                    }
                }));

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        for (Map.Entry<int[], Subscription> entry : mSubscriptions.entrySet()) {
            if (entry.getValue() != null) entry.getValue().unsubscribe();
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}