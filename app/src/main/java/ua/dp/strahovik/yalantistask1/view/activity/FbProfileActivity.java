package ua.dp.strahovik.yalantistask1.view.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.Profile;

import java.util.Collections;
import java.util.List;

import ua.dp.strahovik.yalantistask1.R;
import ua.dp.strahovik.yalantistask1.adapters.FbPhotosAdapter;
import ua.dp.strahovik.yalantistask1.databinding.ActivityFbProfileBinding;
import ua.dp.strahovik.yalantistask1.databinding.PartialFbProfileContentBinding;
import ua.dp.strahovik.yalantistask1.decorators.ImageRecyclerDecorator;
import ua.dp.strahovik.yalantistask1.presenters.FbProfilePresenter;
import ua.dp.strahovik.yalantistask1.services.PersistFbMePhotosService;
import ua.dp.strahovik.yalantistask1.services.PersistProfileService;

public class FbProfileActivity extends AppCompatActivity implements FbProfileMvpView {


    private ActionBar mActionBar;
    private PartialFbProfileContentBinding mFbProfileContentBinding;
    private FbProfilePresenter mFbProfilePresenter;
    private FbPhotosAdapter mFbPhotosAdapter;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityFbProfileBinding activityFbProfileBinding = DataBindingUtil.setContentView(this, R.layout.activity_fb_profile);
        FacebookSdk.sdkInitialize(getApplicationContext());
        mFbProfileContentBinding = activityFbProfileBinding.activityFbProfileContent;
        mToolbar = activityFbProfileBinding.toolbar;
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        initActionBar();
        initRecycleView();
        mFbProfilePresenter = new FbProfilePresenter(getApplicationContext());
        mFbProfilePresenter.attachView(this);
        mFbProfilePresenter.loadProfile();
        mFbProfilePresenter.loadFbMePhotos();
        mFbProfilePresenter.loadAccessToken(getApplicationContext());


    }

    private void initRecycleView() {
        RecyclerView recyclerView = mFbProfileContentBinding.recyclerView;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new ImageRecyclerDecorator(this));
        mFbPhotosAdapter = new FbPhotosAdapter();
        recyclerView.setAdapter(mFbPhotosAdapter);
    }

    public static Intent getStartIntent(Context context) {
        return new Intent(context, FbProfileActivity.class);
    }

    private void initActionBar() {
        if (mActionBar != null) {
            mActionBar.setHomeButtonEnabled(true);
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mFbProfilePresenter.detachView();
    }

    /*****
     * MVP View methods implementation
     *****/

    @Override
    public void accessTokenChanged(AccessToken accessToken) {
        startService(PersistProfileService.getStartIntent(this, accessToken));
        startService(PersistFbMePhotosService.getStartIntent(this, accessToken, R.dimen.content_main_image_height, R.dimen.content_main_image_width));
    }

    @Override
    public void showProfile(Profile profile) {
        mFbProfileContentBinding.setProfile(profile);
        mFbProfileContentBinding.executePendingBindings();
    }

    @Override
    public void showPhotos(List<String> photosUriList) {
        mFbPhotosAdapter.setList(photosUriList);
        mFbPhotosAdapter.notifyDataSetChanged();
    }

    public void showEmptyPhotosList(){
        mFbPhotosAdapter.setList(Collections.<String>emptyList());
        mFbPhotosAdapter.notifyDataSetChanged();
    }


    @Override
    public void showError(Throwable throwable) {
        Log.e(getString(R.string.log_tag), throwable.toString());
        Toast.makeText(this, getString(R.string.error_data_retrieving_exception) + throwable,
                Toast.LENGTH_LONG).show();
    }
}
