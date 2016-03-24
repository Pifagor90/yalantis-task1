/*
Copyright info
*/


package ua.dp.strahovik.yalantistask1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import ua.dp.strahovik.yalantistask1.adapters.ImageAdapter;
import ua.dp.strahovik.yalantistask1.decorators.ImageRecyclerDecorator;
import ua.dp.strahovik.yalantistask1.services.EventDao;
import ua.dp.strahovik.yalantistask1.services.EventDaoMock;
import ua.dp.strahovik.yalantistask1.entities.Event;
import ua.dp.strahovik.yalantistask1.listeners.ExitOnClickListener;

public class MainActivity extends AppCompatActivity {
    //[Comment] Wrong toolbar and status bar colors
    //[Comment] Wrong back button resource
    //[Comemnt] Title is too bold
    //    TODO: should be changed for @DagerInject
    //[Comment] Dagger is cool think but we don't use it now.
    //[Comment] Wrong top padding
    private EventDao mEventDao = new EventDaoMock();
    private Event mEvent;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private Button mEventStateButton;
    private TextView mMenuTextView;
    private TextView mCreationDateTextView;
    private TextView mRegistrationDateTextView;
    private TextView mDeadlineDateTextView;
    private TextView mResponsibleTextView;
    private TextView mDescriptionTextView;

    private ImageButton mExitButton;

    private String mInitialId = "CET-23475287"; //[Comment] I think it should be final


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

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        mEventStateButton = (Button) findViewById(R.id.button_state);
        mMenuTextView = (TextView) findViewById(R.id.eventId);
        mCreationDateTextView = (TextView) findViewById(R.id.created_date);
        mRegistrationDateTextView = (TextView) findViewById(R.id.registred_date);
        mDeadlineDateTextView = (TextView) findViewById(R.id.deadline_date);
        mResponsibleTextView = (TextView) findViewById(R.id.responsible_name);
        mDescriptionTextView = (TextView) findViewById(R.id.description_text);

        mExitButton = (ImageButton) findViewById(R.id.exitButton);
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

        if (mEvent.getEventState().equals("In progress")) { //[Comment] Hardcode
            mEventStateButton.setText(R.string.main_activity_button_in_progress);
        } else {
            throw new IllegalArgumentException("Event state cant anything except \"In progress\""); //[Comment] Hardcode
        }

        mMenuTextView.setText(mInitialId);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMMM yyyy"); //[Comment] Hardcode
        mCreationDateTextView.setText(simpleDateFormat.format(mEvent.getCreationDate()));
        mRegistrationDateTextView.setText(simpleDateFormat.format(mEvent.getRegistrationDate()));
        mDeadlineDateTextView.setText(simpleDateFormat.format(mEvent.getDeadlineDate()));
        mResponsibleTextView.setText(mEvent.getResponsible().getName());
        mDescriptionTextView.setText(mEvent.getDescription());
    }

    private void setListeners() {

        mExitButton.setOnClickListener(new ExitOnClickListener(this));
        mEventStateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Button state was pressed", Toast.LENGTH_LONG).show(); //[Comment] Hardcode
            }
        });

    }

}