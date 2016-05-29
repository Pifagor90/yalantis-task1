/*
Copyright info
*/


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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import ua.dp.strahovik.yalantistask1.R;
import ua.dp.strahovik.yalantistask1.adapters.ImageAdapter;
import ua.dp.strahovik.yalantistask1.databinding.ActivitySingleEventInfoBinding;
import ua.dp.strahovik.yalantistask1.databinding.PartialSingleEventInfoContentBinding;
import ua.dp.strahovik.yalantistask1.decorators.ImageRecyclerDecorator;
import ua.dp.strahovik.yalantistask1.entities.Event;
import ua.dp.strahovik.yalantistask1.presenters.SingleEventPresenter;

public class SingleEventInfoActivity extends AppCompatActivity implements SingleEventMvpView {
    public static final String EVENT_ID = "ua.dp.strahovik.yalantistask1.view.activity.SingleEventInfoActivity.event_id";

    private SingleEventPresenter mSingleEventPresenter;
    private RecyclerView mRecyclerView;

    private Button mEventStateButton;

    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private ImageAdapter mImageAdapter;
    private ActivitySingleEventInfoBinding mActivitySingleEventInfoBinding;
    private PartialSingleEventInfoContentBinding mInfoContentBinding;

    public static Intent getStartIntent(Context context, int eventId) {
        Intent intent = new Intent(context, SingleEventInfoActivity.class);
        intent.putExtra(EVENT_ID, eventId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivitySingleEventInfoBinding = DataBindingUtil.setContentView(this, R.layout.activity_single_event_info);
        initWidgets();
        initRecycleView();
        initActionBar();
        setListeners();
        mSingleEventPresenter = new SingleEventPresenter(getApplicationContext());
        mSingleEventPresenter.attachView(this);
        mSingleEventPresenter.loadEventById(getIntent().getIntExtra(EVENT_ID, -1));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSingleEventPresenter.detachView();
    }

    private void initWidgets() {
        mToolbar = mActivitySingleEventInfoBinding.toolbar;
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        mInfoContentBinding = mActivitySingleEventInfoBinding.singleEventInfoContent;

        mRecyclerView = mInfoContentBinding.recyclerView;
        mEventStateButton = mInfoContentBinding.buttonState;
    }

    private void initRecycleView() {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new ImageRecyclerDecorator(this));
        mImageAdapter = new ImageAdapter();
        mImageAdapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getApplicationContext(), getString(R.string.single_event_activity_msg_image_part_1) +
                        position + getString(R.string.single_event_activity_msg_image_part_2), Toast.LENGTH_LONG).show();
            }
        });
        mRecyclerView.setAdapter(mImageAdapter);
    }

    private void initActionBar() {
        if (mActionBar != null) {
            mActionBar.setHomeButtonEnabled(true);
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    private void setListeners() {

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mEventStateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SingleEventInfoActivity.this, R.string.single_event_activity_msg_state_button,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    /*****
     * MVP View methods implementation
     *****/

    public void showEvent(Event event) {
        mInfoContentBinding.setEvent(event);
        mInfoContentBinding.setSimpleDateFormat(new SimpleDateFormat(getString
                (R.string.single_event_activity_simple_date_format)));
        mInfoContentBinding.executePendingBindings();
        mActionBar.setTitle(event.getIdForOutput());
        mImageAdapter.setList(event.getPhotos());
        mImageAdapter.notifyDataSetChanged();
    }

    public void showError() {
        Toast.makeText(this, getString(R.string.error_data_retrieving_exception),
                Toast.LENGTH_LONG).show();
    }

}