package ua.dp.strahovik.yalantistask1.adapters;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import ua.dp.strahovik.yalantistask1.R;
import ua.dp.strahovik.yalantistask1.entities.Event;
import ua.dp.strahovik.yalantistask1.listeners.OnEventClickListener;
import ua.dp.strahovik.yalantistask1.util.EventTypeConformityToImage;
import ua.dp.strahovik.yalantistask1.util.TimeUtil;

public class ListEventListViewAdapter extends BaseAdapter {
    private List<Event> mEventList;
    private Context mContext;
    private final LayoutInflater mLayoutInflater;
    private final SimpleDateFormat mSimpleDateFormat;
    private Map<String, Drawable> mEventTypeConformityMap;
    private OnEventClickListener mOnEventClickListener;

    public void setOnEventClickListener(OnEventClickListener onEventClickListener) {
        mOnEventClickListener = onEventClickListener;
    }

    public ListEventListViewAdapter(List<Event> eventList, Context context) {
        mEventList = eventList;
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSimpleDateFormat = new SimpleDateFormat(mContext.getString(R.string.list_event_activity_simple_date_format));
        mEventTypeConformityMap = EventTypeConformityToImage.getConformityMap(mContext);
    }

    static class ViewHolder {
        private ImageView mEventTypeImageView;
        private TextView mEventLikeCount;
        private TextView mEventType;
        private TextView mEventAddress;
        private TextView mEventDate;
        private TextView mEventWeirdDaysCounter;
        private LinearLayout mLinearLayout;

        public ViewHolder(View itemView) {
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
    public int getCount() {
        return mEventList.size();
    }

    @Override
    public Object getItem(int position) {
        return mEventList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.item_list_event, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final Event event = mEventList.get(position);

        holder.mEventTypeImageView.setImageDrawable(mEventTypeConformityMap.get(event.getEventType()));
        holder.mEventLikeCount.setText("" + event.getLikeCounter());
        holder.mEventType.setText(event.getEventType());
        holder.mEventAddress.setText(event.getAddress());
        holder.mEventDate.setText(mSimpleDateFormat.format(event.getCreationDate()));
        long weirdDaysCounter = TimeUtil.getDateDiff(event.getCreationDate(), event.getDeadlineDate(),
                TimeUnit.DAYS);
        holder.mEventWeirdDaysCounter.setText(mContext.getResources().getQuantityString(
                R.plurals.list_event_activity_weird_days_counter, (int) weirdDaysCounter, (int) weirdDaysCounter));
        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnEventClickListener != null) {
                    mOnEventClickListener.onEventClick(event);
                }
            }
        });
        return view;
    }
}
