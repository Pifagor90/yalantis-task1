package ua.dp.strahovik.yalantistask1.services;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.facebook.AccessToken;

import rx.Observer;
import rx.Subscription;
import rx.schedulers.Schedulers;
import ua.dp.strahovik.yalantistask1.R;
import ua.dp.strahovik.yalantistask1.data.DataManager;

public class PersistAccessTokenService extends Service {

    public static final String ACCESS_TOKEN =
            "ua.dp.strahovik.yalantistask1.services.PersistProfileService.accessToken";
    private Subscription mSubscription;

    public static Intent getStartIntent(Context context, AccessToken accessToken) {
        Intent intent = new Intent(context, PersistAccessTokenService.class);
        intent.putExtra(ACCESS_TOKEN, accessToken);
        return intent;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        DataManager dataManager = DataManager.getInstance(getApplicationContext());
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        AccessToken accessToken = intent.getParcelableExtra(ACCESS_TOKEN);

        mSubscription = dataManager.syncFbAccessToken(accessToken)
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<AccessToken>() {
                    @Override
                    public void onCompleted() {
                        Log.i(getString(R.string.log_tag), getString(R.string.msg_persisting_fb_access_token_successful));
                        stopSelf(startId);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(getString(R.string.log_tag), getString(R.string.error_persisting_fb_access_token) + e);
                        stopSelf(startId);
                    }

                    @Override
                    public void onNext(AccessToken accessToken) {
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
