package ua.dp.strahovik.yalantistask1.adapters;


import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ua.dp.strahovik.yalantistask1.R;
import ua.dp.strahovik.yalantistask1.databinding.ItemListEventBinding;
import ua.dp.strahovik.yalantistask1.entities.Event;
import ua.dp.strahovik.yalantistask1.listeners.OnEventClickListener;
import ua.dp.strahovik.yalantistask1.util.TimeUtil;

public class ListEventRecyclerViewAdapter extends RecyclerView.Adapter<ListEventRecyclerViewAdapter.ViewHolder> {

    private List<Event> mEventList;
    private Context mContext;
    private final SimpleDateFormat mSimpleDateFormat;
    private OnEventClickListener mOnEventClickListener;

    public void setEventList(List<Event> eventList) {
        mEventList = eventList;
    }

    public void setOnEventClickListener(OnEventClickListener onEventClickListener) {
        mOnEventClickListener = onEventClickListener;
    }

    public ListEventRecyclerViewAdapter(Context context) {
        mEventList = new ArrayList<>();
        mContext = context;
        mSimpleDateFormat = new SimpleDateFormat(mContext.getString(R.string.list_event_activity_simple_date_format));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemListEventBinding mItemListEventBinding;


        public ViewHolder(View view) {
            super(view);
            mItemListEventBinding = DataBindingUtil.bind(view);
        }
    }

    @Override
    public ListEventRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        ItemListEventBinding itemListEventBinding = ItemListEventBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(itemListEventBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(ListEventRecyclerViewAdapter.ViewHolder holder, int position) {
        final Event event = mEventList.get(position);
        ItemListEventBinding itemListEventBinding = holder.mItemListEventBinding;
        itemListEventBinding.setSimpleDateFormat(mSimpleDateFormat);
        itemListEventBinding.setEvent(event);
        itemListEventBinding.itemListEventLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnEventClickListener != null) {
                    mOnEventClickListener.onEventClick(event);
                }
            }
        });
        itemListEventBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mEventList.size();
    }

    @BindingAdapter({"bind:creationDate", "bind:deadlineDate"})
    public static void setWeirdDaysCounter(TextView view, Date creationDate, Date deadlineDate) {
        long weirdDaysCounter = TimeUtil.getDateDiff(creationDate, deadlineDate, TimeUnit.DAYS);
        view.setText(view.getContext().getResources().getQuantityString(
                R.plurals.list_event_activity_weird_days_counter, (int) weirdDaysCounter,
                (int) weirdDaysCounter));
    }
}
