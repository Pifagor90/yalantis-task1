package ua.dp.strahovik.yalantistask1.services;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.util.Log;

import com.facebook.AccessToken;

import rx.Observer;
import rx.Subscription;
import rx.schedulers.Schedulers;
import ua.dp.strahovik.yalantistask1.R;
import ua.dp.strahovik.yalantistask1.data.DataManager;
import ua.dp.strahovik.yalantistask1.util.AndroidComponentUtil;
import ua.dp.strahovik.yalantistask1.util.NetworkUtil;
import ua.dp.strahovik.yalantistask1.util.PersistFbMePhotosOnConnectionAvailableParams;

public class PersistFbMePhotosService extends Service {

    public static final String ACCESS_TOKEN =
            "ua.dp.strahovik.yalantistask1.services.PersistFbMePhotosService.accessToken";
    public static final String EXPECTED_HEIGHT =
            "ua.dp.strahovik.yalantistask1.services.PersistFbMePhotosService.expectedHeight";
    public static final String EXPECTED_WIDTH =
            "ua.dp.strahovik.yalantistask1.services.PersistFbMePhotosService.expectedWidth";
    private Subscription mSubscription;

    public static Intent getStartIntent(Context context, AccessToken accessToken, int expectedHeight,
                                        int expectedWidth) {
        Intent intent = new Intent(context, PersistFbMePhotosService.class);
        intent.putExtra(ACCESS_TOKEN, accessToken);
        intent.putExtra(EXPECTED_HEIGHT, expectedHeight);
        intent.putExtra(EXPECTED_WIDTH, expectedWidth);
        return intent;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {

        AccessToken accessToken = intent.getParcelableExtra(ACCESS_TOKEN);
        int expectedHeight = intent.getIntExtra(EXPECTED_HEIGHT, 0);
        int expectedWidth = intent.getIntExtra(EXPECTED_WIDTH, 0);

        if (!NetworkUtil.isNetworkConnected(this)) {
            Log.i(getString(R.string.log_tag), getString(R.string.msg_connection_fail_no_connection));
            AndroidComponentUtil.toggleComponent(this, PersistFbMePhotosOnConnectionAvailable.class, true);
            PersistFbMePhotosOnConnectionAvailableParams params = PersistFbMePhotosOnConnectionAvailableParams.getInstance();
            params.setAccessToken(accessToken);
            params.setExpectedHeight(expectedHeight);
            params.setExpectedWidth(expectedWidth);
            stopSelf(startId);
            return START_NOT_STICKY;
        }

        DataManager dataManager = DataManager.getInstance(getApplicationContext());
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }

        mSubscription = dataManager.syncFbPhotos(accessToken, expectedHeight, expectedWidth)
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        Log.i(getString(R.string.log_tag), getString(R.string.msg_persisting_fb_me_photos_successful));
                        stopSelf(startId);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(getString(R.string.log_tag), getString(R.string.error_persisting_fb_me_photos) + e);
                        stopSelf(startId);
                    }

                    @Override
                    public void onNext(String uri) {
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

    public static class PersistFbMePhotosOnConnectionAvailable extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)
                    && NetworkUtil.isNetworkConnected(context)) {
                Log.i(context.getString(R.string.log_tag), context.getString(R.string.msg_connection_available));
                AndroidComponentUtil.toggleComponent(context, this.getClass(), false);
                PersistFbMePhotosOnConnectionAvailableParams params = PersistFbMePhotosOnConnectionAvailableParams.getInstance();
                context.startService(getStartIntent(context, params.getAccessToken(), params.getExpectedHeight(),
                        params.getExpectedWidth()));
            }
        }
    }
}
