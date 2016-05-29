package ua.dp.strahovik.yalantistask1.util;


import com.facebook.AccessToken;

public class PersistFbMePhotosOnConnectionAvailableParams {
    private static final Object sMutex = new Object();
    AccessToken accessToken;
    int expectedHeight, expectedWidth;

    private static PersistFbMePhotosOnConnectionAvailableParams instance;

    public static PersistFbMePhotosOnConnectionAvailableParams getInstance() {
        synchronized (sMutex) {
            if (instance == null) {
                instance = new PersistFbMePhotosOnConnectionAvailableParams();
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

    public int getExpectedHeight() {
        return expectedHeight;
    }

    public void setExpectedHeight(int expectedHeight) {
        this.expectedHeight = expectedHeight;
    }

    public int getExpectedWidth() {
        return expectedWidth;
    }

    public void setExpectedWidth(int expectedWidth) {
        this.expectedWidth = expectedWidth;
    }
}
