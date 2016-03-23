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

//    TODO: should be chenged for @DagerInject
    private EventDao mEventDao = new EventDaoMock();
    private Event mEvent;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button mEventStateButton;
    private String mInitialId = "CET-23475287";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Retrieving data from services*/
        mEvent = mEventDao.getEventById(mInitialId);
        /*End of retrieving data*/

        initRecycleView();

        initDataToViews();

        setListeners();

    }
    /*Generating recycleView*/
    private void initRecycleView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new ImageRecyclerDecorator());
        ImageAdapter adapter = new ImageAdapter(mEvent.getPhotos());

        recyclerView.setAdapter(adapter);
    }

    /*Initialize all data from Dao to views*/
    private void initDataToViews() {
        mEventStateButton = (Button) findViewById(R.id.button_state);
        if (mEvent.getEventState().equals("In progress")){
            mEventStateButton.setText(R.string.main_activity_button_in_progress);
        } else {
            throw new IllegalArgumentException("Event state cant anything except \"In progress\"");
        }

        TextView menuTextView = (TextView) findViewById(R.id.eventId);
        menuTextView.setText(mInitialId);

        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMMM yyyy");
        TextView creationDateTextView = (TextView) findViewById(R.id.created_date);
        creationDateTextView.setText(simpleDateFormat.format(mEvent.getCreationDate()));

        TextView registrationDateTextView = (TextView) findViewById(R.id.registred_date);
        registrationDateTextView.setText(simpleDateFormat.format(mEvent.getRegistrationDate()));

        TextView deadlineDateTextView = (TextView) findViewById(R.id.deadline_date);
        deadlineDateTextView.setText(simpleDateFormat.format(mEvent.getDeadlineDate()));

        TextView responsibleTextView = (TextView) findViewById(R.id.responsible_name);
        responsibleTextView.setText(mEvent.getResponsible().getName());

        TextView descriptionTextView = (TextView) findViewById(R.id.description_text);
        descriptionTextView.setText(mEvent.getDescription());
    }

    private void setListeners() {

        ImageButton exitButton = (ImageButton) findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new ExitOnClickListener(this));

        mEventStateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Button state was pressed", Toast.LENGTH_LONG).show();
            }
        });

    }

}
