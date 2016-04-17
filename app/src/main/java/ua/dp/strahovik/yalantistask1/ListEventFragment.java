package ua.dp.strahovik.yalantistask1;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import ua.dp.strahovik.yalantistask1.adapters.ListEventListViewAdapter;
import ua.dp.strahovik.yalantistask1.adapters.ListEventRecyclerViewAdapter;
import ua.dp.strahovik.yalantistask1.decorators.ImageRecyclerDecorator;
import ua.dp.strahovik.yalantistask1.decorators.ListEventRecyclerDecorator;
import ua.dp.strahovik.yalantistask1.entities.Event;


public class ListEventFragment extends Fragment {

    private List<Event> mEventList;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context mContext;
    private boolean isRecyclerViewBased = true;
    private ListView mListView;
    private FloatingActionButton mFloatingActionButton;

    public ListEventFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = container.getContext();
        View view;
        if (isRecyclerViewBased){
            view = inflater.inflate(R.layout.fragment_list_event_recycler, container, false);
            initRecycleView(view);
        } else {
            view = inflater.inflate(R.layout.fragment_list_event_listview, container, false);
            initListView(view);
        }
        
        return view;
    }

    private void initListView(View view) {
        mListView = (ListView) view.findViewById(R.id.fragment_listView);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE){
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

        mListView.setAdapter(listEventListViewAdapter);
    }

    private void initRecycleView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_recyclerView);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    mFloatingActionButton.show();
                } else {
                    mFloatingActionButton.hide();
                }
            }
        });
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        ListEventRecyclerViewAdapter adapter = new ListEventRecyclerViewAdapter(mEventList, mContext);
        mRecyclerView.addItemDecoration(new ListEventRecyclerDecorator(mContext));
        mRecyclerView.setAdapter(adapter);
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


    public void setEventList(List<Event> eventList) {
        mEventList = eventList;
    }


    public void setIsRecyclerViewBased(boolean isRecyclerViewBased) {
        this.isRecyclerViewBased = isRecyclerViewBased;
    }

    public void setFloatingActionButton(FloatingActionButton floatingActionButton) {
        mFloatingActionButton = floatingActionButton;
    }
}
