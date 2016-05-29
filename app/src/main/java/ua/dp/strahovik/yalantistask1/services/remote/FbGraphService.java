package ua.dp.strahovik.yalantistask1.services.remote;


import android.os.Bundle;
import android.support.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func0;
import rx.functions.Func1;
import ua.dp.strahovik.yalantistask1.entities.deserializers.FbPhotoNodeHelperDeserializer;
import ua.dp.strahovik.yalantistask1.entities.deserializers.FbPhotosHelperDeserializer;
import ua.dp.strahovik.yalantistask1.entities.deserializers.FbProfileDeserializer;
import ua.dp.strahovik.yalantistask1.entities.helpers.FbPhotoNodeHelper;
import ua.dp.strahovik.yalantistask1.entities.helpers.FbPhotosHelper;


public class FbGraphService {

    public Observable<Profile> getProfile(AccessToken accessToken) {
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,middle_name,link,name");

        final GraphRequest request = new GraphRequest(accessToken, "me", parameters, HttpMethod.GET);
        return Observable.defer(new Func0<Observable<GraphResponse>>() {
            @Override
            public Observable<GraphResponse> call() {
                return Observable.just(request.executeAndWait());
            }
        }).map(new Func1<GraphResponse, Profile>() {
            @Override
            public Profile call(GraphResponse graphResponse) {
                return new GsonBuilder()
                        .registerTypeAdapter(Profile.class, new FbProfileDeserializer())
                        .create()
                        .fromJson(graphResponse.getRawResponse(), Profile.class);
            }
        });
    }

    public Observable<List<String>> getPhotos(final AccessToken accessToken, final int expectedHeight,
                                              final int expectedWidth) {

        final GraphRequest requestPhotosIds = new GraphRequest(accessToken, "me/photos/uploaded", null, HttpMethod.GET);
        return Observable.defer(new Func0<Observable<GraphResponse>>() {
            @Override
            public Observable<GraphResponse> call() {
                return Observable.just(requestPhotosIds.executeAndWait());
            }
        }).map(new Func1<GraphResponse, FbPhotosHelper>() {
            @Override
            public FbPhotosHelper call(GraphResponse graphResponse) {
                return new GsonBuilder()
                        .registerTypeAdapter(FbPhotosHelper.class, new FbPhotosHelperDeserializer())
                        .create()
                        .fromJson(graphResponse.getRawResponse(), FbPhotosHelper.class);
            }
        }).concatMap(new Func1<FbPhotosHelper, Observable<List<String>>>() {
            @Override
            public Observable<List<String>> call(final FbPhotosHelper fbPhotosHelper) {
                return Observable.defer(new Func0<Observable<List<GraphResponse>>>() {
                    @Override
                    public Observable<List<GraphResponse>> call() {
                        return Observable.just(getPhotosGraphRequestBatch(fbPhotosHelper, accessToken)
                                .executeAndWait());
                    }
                }).map(new Func1<List<GraphResponse>, List<String>>() {
                    @Override
                    public List<String> call(List<GraphResponse> graphResponses) {
                        return parseAndMapResponsesToPhotos(graphResponses, expectedHeight, expectedWidth);
                    }
                });
            }
        });
    }

    private List<String> parseAndMapResponsesToPhotos(List<GraphResponse> graphResponses, int expectedHeight, int expectedWidth) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(FbPhotoNodeHelper.class, new FbPhotoNodeHelperDeserializer())
                .create();
        List<FbPhotoNodeHelper> nodeHelpers = new ArrayList<>(graphResponses.size());
        for (GraphResponse graphResponse : graphResponses) {
            nodeHelpers.add(gson.fromJson(graphResponse.getRawResponse(), FbPhotoNodeHelper.class));
        }
        return sourcesBestResolutionChooser(nodeHelpers, expectedHeight, expectedWidth);
    }

    @NonNull
    private GraphRequestBatch getPhotosGraphRequestBatch(FbPhotosHelper fbPhotosHelper, AccessToken accessToken) {
        ArrayList<GraphRequest> requests = new ArrayList<GraphRequest>();
        for (String id : fbPhotosHelper.getIds()) {
            Bundle parameters = new Bundle();
            parameters.putString("fields", "images");
            final GraphRequest request = new GraphRequest(accessToken, id, parameters, HttpMethod.GET);
            request.setParameters(parameters);
            requests.add(request);
        }
        return new GraphRequestBatch(requests);
    }

    /*        returns list of uris of photos where width and height of each photo node
        is bigger or equal than expectedHeight and expectedWidth
    */
    private List<String> sourcesBestResolutionChooser(List<FbPhotoNodeHelper> source, int expectedHeight,
                                                      int expectedWidth) {
        FbPhotoNodeHelper.PlatformImageSource comparablePlatformImageSource;
        int location, bestHeight, bestWidth;
        List<String> photoUriList = new ArrayList<>();
        for (int outerCounter = 0; outerCounter < source.size(); outerCounter++) {
            location = 0;
            bestHeight = source.get(outerCounter).getPlatformImageSources().get(location).getHeight();
            bestWidth = source.get(outerCounter).getPlatformImageSources().get(location).getWidth();
            for (int innerCounter = 0; innerCounter < source.get(outerCounter).getPlatformImageSources().size();
                 innerCounter++) {
                comparablePlatformImageSource = source.get(outerCounter).getPlatformImageSources().get(innerCounter);
                if (comparablePlatformImageSource.getHeight() >= expectedHeight &&
                        comparablePlatformImageSource.getWidth() >= expectedWidth) {
                    if (comparablePlatformImageSource.getHeight() < bestHeight ||
                            comparablePlatformImageSource.getWidth() < bestWidth) {
                        location = innerCounter;
                        bestHeight = comparablePlatformImageSource.getHeight();
                        bestWidth = comparablePlatformImageSource.getWidth();
                    }
                }
            }
            photoUriList.add(source.get(outerCounter).getPlatformImageSources().get(location).getSource());
        }
        return photoUriList;
    }
}
