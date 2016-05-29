package ua.dp.strahovik.yalantistask1.view.activity;


import com.facebook.AccessToken;
import com.facebook.Profile;

import java.util.List;

import ua.dp.strahovik.yalantistask1.presenters.MvpView;

public interface FbProfileMvpView  extends MvpView {

    void showProfile(Profile profile);

    void accessTokenChanged(AccessToken accessToken);

    void showPhotos(List<String> photosUriList);

    void showEmptyPhotosList();

    void showError(Throwable throwable);
}
