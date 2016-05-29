package ua.dp.strahovik.yalantistask1.util;

import com.facebook.AccessToken;

public class PersistProfileOnConnectionAvailableParams {

    private static final Object sMutex = new Object();
    AccessToken accessToken;

    private static PersistProfileOnConnectionAvailableParams instance;

    public static PersistProfileOnConnectionAvailableParams getInstance() {
        synchronized (sMutex) {
            if (instance == null) {
                instance = new PersistProfileOnConnectionAvailableParams();
            }
            return instance;
        }
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
    }
}
