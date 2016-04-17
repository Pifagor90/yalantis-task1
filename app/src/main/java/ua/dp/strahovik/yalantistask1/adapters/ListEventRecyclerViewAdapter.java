package ua.dp.strahovik.yalantistask1.adapters;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import ua.dp.strahovik.yalantistask1.R;
import ua.dp.strahovik.yalantistask1.SingleEventInfoActivity;
import ua.dp.strahovik.yalantistask1.entities.Event;
import ua.dp.strahovik.yalantistask1.util.EventTypeConformityToImage;
import ua.dp.strahovik.yalantistask1.util.TimeUtil;

public class ListEventRecyclerViewAdapter extends RecyclerView.Adapter<ListEventRecyclerViewAdapter.ViewHolder>{

    private List<Event> mEventList;
    private Context mContext;
    private final SimpleDateFormat mSimpleDateFormat;
    private Map<String, Drawable> mEventTypeConformityMap;


    public ListEventRecyclerViewAdapter(List<Event> eventList, Context context) {
        mEventList = eventList;
        mContext = context;
        mSimpleDateFormat = new SimpleDateFormat(mContext.getString(R.string.list_event_activity_simple_date_format));
        mEventTypeConformityMap = EventTypeConformityToImage.getConformityMap(mContext);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mEventTypeImageView;
        private TextView mEventLikeCount;
        private TextView mEventType;
        private TextView mEventAddress;
        private TextView mEventDate;
        private TextView mEventWeirdDaysCounter;
        private LinearLayout mLinearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            mEventTypeImageView = (ImageView) itemView.findViewById(R.id.item_list_event_image_view_type);
            mEventLikeCount = (TextView) itemView.findViewById(R.id.item_list_event_fb_counter);
            mEventType = (TextView) itemView.findViewById(R.id.item_list_event_text_type);
            mEventAddress = (TextView) itemView.findViewById(R.id.item_list_event_text_address);
            mEventDate = (TextView) itemView.findViewById(R.id.item_list_event_text_date);
            mEventWeirdDaysCounter = (TextView) itemView.findViewById(R.id.item_list_event_text_weird_data);
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.item_list_event_LinearLayout);
        }
    }

    @Override
    public ListEventRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        View itemListEvent = layoutInflater.inflate(R.layout.item_list_event, parent, false);

        return new ViewHolder(itemListEvent);
    }

    @Override
    public void onBindViewHolder(ListEventRecyclerViewAdapter.ViewHolder holder, int position) {
        final Event event = mEventList.get(position);
        holder.mEventTypeImageView.setImageDrawable(mEventTypeConformityMap.get(event.getEventType()));
        holder.mEventLikeCount.setText("" + event.getLikeCounter());
        holder.mEventType.setText(event.getEventType());
        holder.mEventAddress.setText(event.getAddress());
        holder.mEventDate.setText(mSimpleDateFormat.format(event.getCreationDate()));
        long weirdDaysCounter = TimeUtil.getDateDiff(event.getCreationDate(),event.getDeadlineDate(),
                TimeUnit.DAYS);
        holder.mEventWeirdDaysCounter.setText(mContext.getResources().getQuantityString(
                R.plurals.weirdDaysCounter,(int) weirdDaysCounter, (int) weirdDaysCounter));
        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SingleEventInfoActivity.class);
                intent.putExtra("Event id", event.getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mEventList.size();
    }
}