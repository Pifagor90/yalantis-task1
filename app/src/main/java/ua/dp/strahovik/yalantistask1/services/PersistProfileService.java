package ua.dp.strahovik.yalantistask1.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.Profile;

import rx.Observer;
import rx.Subscription;
import rx.schedulers.Schedulers;
import ua.dp.strahovik.yalantistask1.R;
import ua.dp.strahovik.yalantistask1.data.DataManager;
import ua.dp.strahovik.yalantistask1.util.AndroidComponentUtil;
import ua.dp.strahovik.yalantistask1.util.NetworkUtil;
import ua.dp.strahovik.yalantistask1.util.PersistProfileOnConnectionAvailableParams;

public class PersistProfileService extends Service {

    public static final String ACCESS_TOKEN =
            "ua.dp.strahovik.yalantistask1.services.PersistProfileService.accessToken";
    private Subscription mSubscription;

    public static Intent getStartIntent(Context context, AccessToken accessToken) {
        Intent intent = new Intent(context, PersistProfileService.class);
        intent.putExtra(ACCESS_TOKEN, accessToken);
        return intent;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        AccessToken accessToken = intent.getParcelableExtra(ACCESS_TOKEN);

        if (!NetworkUtil.isNetworkConnected(this)) {
            Log.i(getString(R.string.log_tag), getString(R.string.msg_connection_fail_no_connection));
            AndroidComponentUtil.toggleComponent(this, PersistProfileOnConnectionAvailable.class, true);
            PersistProfileOnConnectionAvailableParams params = PersistProfileOnConnectionAvailableParams.getInstance();
            params.setAccessToken(accessToken);
            stopSelf(startId);
            return START_NOT_STICKY;
        }


        DataManager dataManager = DataManager.getInstance(getApplicationContext());
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }

        mSubscription = dataManager.syncFbProfile(accessToken)
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Profile>() {
                    @Override
                    public void onCompleted() {
                        Log.i(getString(R.string.log_tag), getString(R.string.msg_persisting_fb_profile_successful));
                        stopSelf(startId);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(getString(R.string.log_tag), getString(R.string.error_persisting_fb_profile) + e);
                        stopSelf(startId);
                    }

                    @Override
                    public void onNext(Profile profile) {
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

    public static class PersistProfileOnConnectionAvailable extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)
                    && NetworkUtil.isNetworkConnected(context)) {
                Log.i(context.getString(R.string.log_tag), context.getString(R.string.msg_connection_available));
                AndroidComponentUtil.toggleComponent(context, this.getClass(), false);
                PersistProfileOnConnectionAvailableParams params = PersistProfileOnConnectionAvailableParams.getInstance();
                context.startService(getStartIntent(context, params.getAccessToken()));
            }
        }
    }
}
