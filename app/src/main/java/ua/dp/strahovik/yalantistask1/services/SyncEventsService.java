package ua.dp.strahovik.yalantistask1.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.util.Log;

import rx.Observer;
import rx.Subscription;
import rx.schedulers.Schedulers;
import ua.dp.strahovik.yalantistask1.R;
import ua.dp.strahovik.yalantistask1.data.DataManager;
import ua.dp.strahovik.yalantistask1.entities.Event;
import ua.dp.strahovik.yalantistask1.util.AndroidComponentUtil;
import ua.dp.strahovik.yalantistask1.util.NetworkUtil;
import ua.dp.strahovik.yalantistask1.view.fragment.ListEventFragment;

public class SyncEventsService extends Service {

    public static final String AMOUNT =
            "ua.dp.strahovik.yalantistask1.services.SyncEventsService.amount";
    private Subscription mSubscription;

    public static Intent getStartIntent(Context context, int amountForEach) {
        Intent intent = new Intent(context, SyncEventsService.class);
        intent.putExtra(AMOUNT, amountForEach);
        return intent;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        if (!NetworkUtil.isNetworkConnected(this)) {
            Log.i(getString(R.string.log_tag), getString(R.string.msg_connection_fail_no_connection));
            AndroidComponentUtil.toggleComponent(this, SyncOnConnectionAvailable.class, true);
            stopSelf(startId);
            return START_NOT_STICKY;
        }


        DataManager dataManager = DataManager.getInstance(getApplicationContext());
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }

        int amount = intent.getIntExtra(AMOUNT, getResources().
                getInteger(R.integer.list_event_activity_default_amount_of_events_to_be_downloaded));

        mSubscription = dataManager.syncAllEvents(this, amount)
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Event>() {
                    @Override
                    public void onCompleted() {
                        Log.i(getString(R.string.log_tag), getString(R.string.msg_sync_successful));
                        ListEventFragment.setIsSynced(true);
                        stopSelf(startId);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(getString(R.string.log_tag), getString(R.string.error_syncing_events_) + e);
                        ListEventFragment.setIsSynced(true);
                        stopSelf(startId);
                    }

                    @Override
                    public void onNext(Event event) {
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


    public static class SyncOnConnectionAvailable extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)
                    && NetworkUtil.isNetworkConnected(context)) {
                Log.i(context.getString(R.string.log_tag), context.getString(R.string.msg_connection_available));
                AndroidComponentUtil.toggleComponent(context, this.getClass(), false);
                context.startService(getStartIntent(context, context.getResources().
                        getInteger(R.integer.list_event_activity_default_amount_of_events_to_be_downloaded)));
            }
        }
    }
}
