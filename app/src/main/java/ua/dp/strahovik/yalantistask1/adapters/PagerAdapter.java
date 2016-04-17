package ua.dp.strahovik.yalantistask1.adapters;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


import ua.dp.strahovik.yalantistask1.ListEventFragment;
import ua.dp.strahovik.yalantistask1.R;
import ua.dp.strahovik.yalantistask1.services.EventDao;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private Context mContext;
    private int mNumOfTabs;
    private EventDao mEventDao;
    private FloatingActionButton mFloatingActionButton;

    public PagerAdapter(FragmentManager fm, int NumOfTabs, Context context, EventDao eventDao,
                        FloatingActionButton floatingActionButton) {
        super(fm);
        mNumOfTabs = NumOfTabs;
        mContext = context;
        mEventDao = eventDao;
        mFloatingActionButton = floatingActionButton;
    }

    @Override
    public Fragment getItem(int position) {
        ListEventFragment listEventFragment = new ListEventFragment();
        listEventFragment.setFloatingActionButton(mFloatingActionButton);
        switch (position) {
            case 0:
                listEventFragment.setEventList(mEventDao.getListEventByEventState
                        (mContext.getString(R.string.event_state_in_progress)));
                return listEventFragment;
            case 1:
                listEventFragment.setEventList(mEventDao.getListEventByEventState
                        (mContext.getString(R.string.event_state_done)));
                return listEventFragment;
            case 2:
                listEventFragment.setEventList(mEventDao.getListEventByEventState
                        (mContext.getString(R.string.event_state_waiting)));
                listEventFragment.setIsRecyclerViewBased(false);
                return listEventFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}