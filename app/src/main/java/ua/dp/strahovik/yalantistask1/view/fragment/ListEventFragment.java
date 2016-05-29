package ua.dp.strahovik.yalantistask1.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.os.ResultReceiver;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.dp.strahovik.yalantistask1.R;
import ua.dp.strahovik.yalantistask1.adapters.ListEventRecyclerViewAdapter;
import ua.dp.strahovik.yalantistask1.decorators.ListEventRecyclerDecorator;
import ua.dp.strahovik.yalantistask1.entities.Event;
import ua.dp.strahovik.yalantistask1.listeners.OnEventClickListener;
import ua.dp.strahovik.yalantistask1.presenters.ListEventPresenter;
import ua.dp.strahovik.yalantistask1.services.DownloadMoreEventsService;
import ua.dp.strahovik.yalantistask1.view.activity.ListEventActivity;
import ua.dp.strahovik.yalantistask1.view.activity.ListEventMvpView;
import ua.dp.strahovik.yalantistask1.view.activity.SingleEventInfoActivity;


public class ListEventFragment extends Fragment implements OnEventClickListener, ListEventMvpView {

    private Context mContext;
    public static final String EVENT_STATE_IDS =
            "ua.dp.strahovik.yalantistask1.view.fragment.ListEventFragment.event_state_ids";
    public static final String IS_LOADING =
            "ua.dp.strahovik.yalantistask1.view.fragment.ListEventFragment.is_loading";
    public static final String RECEIVER =
            "ua.dp.strahovik.yalantistask1.view.fragment.ListEventFragment.receiver";
    @BindView(R.id.fab) FloatingActionButton mFloatingActionButton;
    private ListEventPresenter mListEventPresenter;
    private ListEventRecyclerViewAdapter mListEventRecyclerViewAdapter;
    private ResultReceiver mReceiver;


    private static AtomicBoolean sIsSynced = new AtomicBoolean(false);
    private boolean mIsLoading;
    private int firstVisibleItem, visibleItemCount, totalItemCount;
    private int[] mEventStateId;

    public static ListEventFragment newInstance(int[] eventState, Boolean isLoading,
                                                ResultReceiver receiver) {
        ListEventFragment listEventFragment = new ListEventFragment();
        Bundle args = new Bundle();
        args.putIntArray(EVENT_STATE_IDS, eventState);
        args.putBoolean(IS_LOADING, isLoading);
        args.putParcelable(RECEIVER, receiver);
        listEventFragment.setArguments(args);
        return listEventFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = container.getContext();

        Bundle arguments = getArguments();
        mEventStateId = arguments.getIntArray(EVENT_STATE_IDS);
        mIsLoading = arguments.getBoolean(IS_LOADING, false);
        mReceiver = arguments.getParcelable(RECEIVER);
        ButterKnife.bind(this, getActivity());

        View view = inflater.inflate(R.layout.fragment_list_event_recycler, container, false);
        initRecycleView(view);

        mListEventPresenter = new ListEventPresenter(mContext.getApplicationContext());
        mListEventPresenter.attachView(this);
        mListEventPresenter.loadEventListByEventStateWithoutPhotos(mEventStateId);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mListEventPresenter.detachView();
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
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (sIsSynced.get()) {
                    if (!mIsLoading) {
                        if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                            visibleItemCount = linearLayoutManager.getChildCount();
                            totalItemCount = linearLayoutManager.getItemCount();
                            if (totalItemCount <= visibleItemCount) {
                                startDownloadMoreEventsServiceAndNotifyLoading();
                            }
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    if (sIsSynced.get()) {
                        if (!mIsLoading) {
                            visibleItemCount = linearLayoutManager.getChildCount();
                            totalItemCount = linearLayoutManager.getItemCount();
                            if (totalItemCount > visibleItemCount) {
                                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                                if ((visibleItemCount + firstVisibleItem) >= totalItemCount) {
                                    startDownloadMoreEventsServiceAndNotifyLoading();
                                }
                            }
                        }
                    }
                }
            }
        });
        mListEventRecyclerViewAdapter = new ListEventRecyclerViewAdapter(mContext);
        mListEventRecyclerViewAdapter.setOnEventClickListener(this);
        recyclerView.addItemDecoration(new ListEventRecyclerDecorator(mContext));
        recyclerView.setAdapter(mListEventRecyclerViewAdapter);
    }

    private void startDownloadMoreEventsServiceAndNotifyLoading() {
//        some anim through progressBar may be added here
        Bundle bundle = new Bundle();
        bundle.putIntArray(ListEventActivity.FragmentLoadingStateResultReceiver.ARRAY_REQUEST, mEventStateId);
        bundle.putBoolean(ListEventActivity.FragmentLoadingStateResultReceiver.IS_LOADING, true);
        mReceiver.send(0, bundle);
        mContext.startService(DownloadMoreEventsService.getStartIntent(
                mContext, mEventStateId, totalItemCount, mContext.getResources().
                        getInteger(R.integer.list_event_activity_default_amount_of_events_to_be_downloaded), mReceiver));
    }


    @Override
    public void onEventClick(Event event) {
        mContext.startActivity(SingleEventInfoActivity.getStartIntent(mContext, event.getId()));
    }

    public static void setIsSynced(boolean isSynced) {
        sIsSynced.set(isSynced);
    }

    public int[] getEventStateId() {
        return mEventStateId;
    }

    public void setIsLoading(boolean isLoading) {
        mIsLoading = isLoading;
    }
    /*****
     * MVP View methods implementation
     *****/
    @Override
    public void showEventList(List<Event> eventList) {
        mListEventRecyclerViewAdapter.setEventList(eventList);
        mListEventRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void showEventListEmpty() {
        mListEventRecyclerViewAdapter.setEventList(Collections.<Event>emptyList());
        mListEventRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(Throwable e) {
        Log.e(mContext.getString(R.string.log_tag), e.toString());
        Toast.makeText(mContext, getString(R.string.error_data_retrieving_exception) + e,
                Toast.LENGTH_LONG).show();
    }
}
