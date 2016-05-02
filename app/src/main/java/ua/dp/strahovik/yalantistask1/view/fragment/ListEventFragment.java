package ua.dp.strahovik.yalantistask1.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import ua.dp.strahovik.yalantistask1.R;
import ua.dp.strahovik.yalantistask1.adapters.ListEventListViewAdapter;
import ua.dp.strahovik.yalantistask1.adapters.ListEventRecyclerViewAdapter;
import ua.dp.strahovik.yalantistask1.decorators.ListEventRecyclerDecorator;
import ua.dp.strahovik.yalantistask1.entities.Event;
import ua.dp.strahovik.yalantistask1.listeners.OnEventClickListener;
import ua.dp.strahovik.yalantistask1.presenters.ListEventPresenter;
import ua.dp.strahovik.yalantistask1.view.activity.ListEventMvpView;
import ua.dp.strahovik.yalantistask1.view.activity.SingleEventInfoActivity;


public class ListEventFragment extends Fragment implements OnEventClickListener, ListEventMvpView {

    private Context mContext;
    public static final String EVENT_STATE =
            "ua.dp.strahovik.yalantistask1.view.fragment.ListEventFragment.event_state";
    public static final String IS_RECYCLER_VIEW_BASED =
            "ua.dp.strahovik.yalantistask1.view.fragment.ListEventFragment.is_recycler_view_based";
    private FloatingActionButton mFloatingActionButton;
    private boolean mIsRecyclerViewBased;
    private ListEventPresenter mListEventPresenter;
    private ListEventRecyclerViewAdapter mListEventRecyclerViewAdapter;
    private ListEventListViewAdapter mListEventListViewAdapter;

    public static ListEventFragment newInstance(String eventState, boolean isRecyclerViewBased) {
        ListEventFragment listEventFragment = new ListEventFragment();
        Bundle args = new Bundle();
        args.putString(EVENT_STATE, eventState);
        args.putBoolean(IS_RECYCLER_VIEW_BASED, isRecyclerViewBased);
        listEventFragment.setArguments(args);
        return listEventFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = container.getContext();

        Bundle arguments = getArguments();
        String eventState = arguments.getString(EVENT_STATE);
        mIsRecyclerViewBased = arguments.getBoolean(IS_RECYCLER_VIEW_BASED, true);
        mFloatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.fab);

        View view;
        if (mIsRecyclerViewBased) {
            view = inflater.inflate(R.layout.fragment_list_event_recycler, container, false);
            initRecycleView(view);
        } else {
            view = inflater.inflate(R.layout.fragment_list_event_listview, container, false);
            initListView(view);
        }

        mListEventPresenter = new ListEventPresenter(mContext.getApplicationContext());
        mListEventPresenter.attachView(this);
        mListEventPresenter.loadEventListByEventStateWithoutPhotos(eventState);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mListEventPresenter.detachView();
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
        mListEventListViewAdapter = new ListEventListViewAdapter(mContext);
        mListEventListViewAdapter.setOnEventClickListener(this);
        listView.setAdapter(mListEventListViewAdapter);
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
        mListEventRecyclerViewAdapter = new ListEventRecyclerViewAdapter(mContext);
        mListEventRecyclerViewAdapter.setOnEventClickListener(this);
        recyclerView.addItemDecoration(new ListEventRecyclerDecorator(mContext));
        recyclerView.setAdapter(mListEventRecyclerViewAdapter);
    }


    @Override
    public void onEventClick(Event event) {
        mContext.startActivity(SingleEventInfoActivity.getStartIntent(mContext, event.getId()));
    }

    /*****
     * MVP View methods implementation
     *****/
    @Override
    public void showEventList(List<Event> eventList) {
        if (mIsRecyclerViewBased) {
            mListEventRecyclerViewAdapter.setEventList(eventList);
            mListEventRecyclerViewAdapter.notifyDataSetChanged();
        } else {
            mListEventListViewAdapter.setEventList(eventList);
            mListEventListViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showEventListEmpty() {
        if (mIsRecyclerViewBased) {
            mListEventRecyclerViewAdapter.setEventList(Collections.<Event>emptyList());
            mListEventRecyclerViewAdapter.notifyDataSetChanged();
        } else {
            mListEventListViewAdapter.setEventList(Collections.<Event>emptyList());
            mListEventListViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showError(Throwable e) {
        Log.e(mContext.getString(R.string.log_tag), e.toString());
        Toast.makeText(mContext, getString(R.string.error_data_retrieving_exception) + e,
                Toast.LENGTH_LONG).show();
    }
}
