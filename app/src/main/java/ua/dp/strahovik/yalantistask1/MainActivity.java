/*
Copyright info
*/


package ua.dp.strahovik.yalantistask1;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import ua.dp.strahovik.yalantistask1.adapters.ImageAdapter;
import ua.dp.strahovik.yalantistask1.decorators.ImageRecyclerDecorator;
import ua.dp.strahovik.yalantistask1.services.EventDao;
import ua.dp.strahovik.yalantistask1.services.EventDaoMock;
import ua.dp.strahovik.yalantistask1.entities.Event;

public class MainActivity extends AppCompatActivity {
    private EventDao mEventDao = new EventDaoMock(this);
    private Event mEvent;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private Button mEventStateButton;
    private TextView mCreationDateTextView;
    private TextView mRegistrationDateTextView;
    private TextView mDeadlineDateTextView;
    private TextView mResponsibleTextView;
    private TextView mDescriptionTextView;

    private Toolbar mToolbar;
    private ActionBar mActionBar;

    private final String mInitialId = "CET-23475287";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Retrieving data from services*/
        mEvent = mEventDao.getEventById(mInitialId);
        /*End of retrieving data*/

        initWidgets();
        initRecycleView();
        initDataToViews();
        setListeners();
    }

    private void initWidgets() {
        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        mEventStateButton = (Button) findViewById(R.id.button_state);
        mCreationDateTextView = (TextView) findViewById(R.id.created_date);
        mRegistrationDateTextView = (TextView) findViewById(R.id.registred_date);
        mDeadlineDateTextView = (TextView) findViewById(R.id.deadline_date);
        mResponsibleTextView = (TextView) findViewById(R.id.responsible_name);
        mDescriptionTextView = (TextView) findViewById(R.id.description_text);
    }

    /*Generating recycleView*/
    private void initRecycleView() {

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new ImageRecyclerDecorator(this));

        ImageAdapter adapter = new ImageAdapter(mEvent.getPhotos());
        mRecyclerView.setAdapter(adapter);
    }

    /*Initialize all data from Dao to views*/
    private void initDataToViews() {
        if (mActionBar != null) {
            mActionBar.setHomeButtonEnabled(true);
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (mEvent.getEventState().equals(getString(R.string.main_activity_button_in_progress))) {
            mEventStateButton.setText(R.string.main_activity_button_in_progress);
        } else {
            throw new IllegalArgumentException(getString(R.string.main_activity_event_state_illega_arg_exception));
        }

        mActionBar.setTitle(mInitialId);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getString(R.string.main_activity_simple_date_format));
        mCreationDateTextView.setText(simpleDateFormat.format(mEvent.getCreationDate()));
        mRegistrationDateTextView.setText(simpleDateFormat.format(mEvent.getRegistrationDate()));
        mDeadlineDateTextView.setText(simpleDateFormat.format(mEvent.getDeadlineDate()));
        mResponsibleTextView.setText(mEvent.getResponsible().getName());
        mDescriptionTextView.setText(mEvent.getDescription());
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
                Toast.makeText(MainActivity.this, R.string.toast_state_button, Toast.LENGTH_LONG).show();
            }
        });

    }

}