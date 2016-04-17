package ua.dp.strahovik.yalantistask1.adapters;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import ua.dp.strahovik.yalantistask1.R;
import ua.dp.strahovik.yalantistask1.SingleEventInfoActivity;
import ua.dp.strahovik.yalantistask1.entities.Event;
import ua.dp.strahovik.yalantistask1.util.EventTypeConformityToImage;
import ua.dp.strahovik.yalantistask1.util.TimeUtil;

public class ListEventListViewAdapter extends BaseAdapter {
    private List<Event> mEventList;
    private Context mContext;
    private final LayoutInflater mLayoutInflater;
    private final SimpleDateFormat mSimpleDateFormat;
    private Map<String, Drawable> mEventTypeConformityMap;

    public ListEventListViewAdapter(List<Event> eventList, Context context) {
        mEventList = eventList;
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSimpleDateFormat = new SimpleDateFormat(mContext.getString(R.string.list_event_activity_simple_date_format));
        mEventTypeConformityMap = EventTypeConformityToImage.getConformityMap(mContext);
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
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.item_list_event, parent, false);
        }
        final Event event = mEventList.get(position);

        ImageView eventTypeImageView = (ImageView) view.findViewById(R.id.item_list_event_image_view_type);
        TextView eventLikeCount = (TextView) view.findViewById(R.id.item_list_event_fb_counter);
        TextView eventType = (TextView) view.findViewById(R.id.item_list_event_text_type);
        TextView eventAddress = (TextView) view.findViewById(R.id.item_list_event_text_address);
        TextView eventDate = (TextView) view.findViewById(R.id.item_list_event_text_date);
        TextView eventWeirdDaysCounter = (TextView) view.findViewById(R.id.item_list_event_text_weird_data);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.item_list_event_LinearLayout);


        eventTypeImageView.setImageDrawable(mEventTypeConformityMap.get(event.getEventType()));
        eventLikeCount.setText(""+ event.getLikeCounter());
        eventType.setText(event.getEventType());
        eventAddress.setText(event.getAddress());
        eventDate.setText(mSimpleDateFormat.format(event.getCreationDate()));
        long weirdDaysCounter = TimeUtil.getDateDiff(event.getCreationDate(), event.getDeadlineDate(),
                TimeUnit.DAYS);
        eventWeirdDaysCounter.setText(Long.toString(weirdDaysCounter) + " " +
                mContext.getString(R.string.days));
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SingleEventInfoActivity.class);
                intent.putExtra("Event id", event.getId());
                mContext.startActivity(intent);
            }
        });
        return view;
    }
}
