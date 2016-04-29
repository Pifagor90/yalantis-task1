package ua.dp.strahovik.yalantistask1.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

import ua.dp.strahovik.yalantistask1.R;
import ua.dp.strahovik.yalantistask1.adapters.ListEventListViewAdapter;
import ua.dp.strahovik.yalantistask1.adapters.ListEventRecyclerViewAdapter;
import ua.dp.strahovik.yalantistask1.listeners.OnEventClickListener;
import ua.dp.strahovik.yalantistask1.decorators.ListEventRecyclerDecorator;
import ua.dp.strahovik.yalantistask1.entities.Event;
import ua.dp.strahovik.yalantistask1.view.activity.SingleEventInfoActivity;

//  TODO RENAME names in bundle mb push  em to strings

public class ListEventFragment extends Fragment implements OnEventClickListener {

    private List<Event> mEventList;
    private Context mContext;
    private FloatingActionButton mFloatingActionButton;

    public static ListEventFragment newInstance(List<Event> eventList, boolean isRecyclerViewBased) {
        ListEventFragment listEventFragment = new ListEventFragment();
        Bundle args = new Bundle();
        args.putParcelableArray("Event arr", eventList.toArray(new Event[eventList.size()]));
        args.putBoolean("isRecyclerViewBased", isRecyclerViewBased);
        listEventFragment.setArguments(args);
        return listEventFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = container.getContext();

        Bundle arguments = getArguments();
        Event[] events = (Event[]) arguments.getParcelableArray("Event arr");
        mEventList = Arrays.asList(events != null ? events : new Event[0]);
        boolean isRecyclerViewBased = arguments.getBoolean("isRecyclerViewBased", true);
        mFloatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.fab);

        View view;
        if (isRecyclerViewBased) {
            view = inflater.inflate(R.layout.fragment_list_event_recycler, container, false);
            initRecycleView(view);
        } else {
            view = inflater.inflate(R.layout.fragment_list_event_listview, container, false);
            initListView(view);
        }

        return view;
    }

    private void initListView(View view) {
        ListView listView = (ListView) view.findViewById(R.id.fragment_listView);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    mFloatingActionButton.show();
                } else {
                    mFloatingActionButton.hide();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                NOP;
            }
        });
        ListEventListViewAdapter listEventListViewAdapter = new ListEventListViewAdapter(mEventList, mContext);
        listEventListViewAdapter.setOnEventClickListener(this);
        listView.setAdapter(listEventListViewAdapter);
    }


    private void initRecycleView(View view) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fragment_recyclerView);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mFloatingActionButton.show();
                } else {
                    mFloatingActionButton.hide();
                }
            }
        });
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(mContext,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        ListEventRecyclerViewAdapter adapter = new ListEventRecyclerViewAdapter(mEventList, mContext);
        adapter.setOnEventClickListener(this);
        recyclerView.addItemDecoration(new ListEventRecyclerDecorator(mContext));
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onEventClick(Event event) {
        Intent intent = new Intent(mContext, SingleEventInfoActivity.class);
        intent.putExtra("Event id", event.getId());
        mContext.startActivity(intent);
    }
}
