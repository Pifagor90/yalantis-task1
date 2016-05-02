package ua.dp.strahovik.yalantistask1.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ua.dp.strahovik.yalantistask1.R;
import ua.dp.strahovik.yalantistask1.view.fragment.ListEventFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private Context mContext;
    private int mNumOfTabs;

    public PagerAdapter(FragmentManager fragmentManager, int NumOfTabs, Context context) {
        super(fragmentManager);
        mNumOfTabs = NumOfTabs;
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        ListEventFragment listEventFragment;
        switch (position) {
            case 0:
                listEventFragment = ListEventFragment.newInstance(mContext.getString(R.string.event_state_in_progress), true);
                return listEventFragment;
            case 1:
                listEventFragment = ListEventFragment.newInstance(mContext.getString(R.string.event_state_done), true);
                return listEventFragment;
            case 2:
                listEventFragment = ListEventFragment.newInstance(mContext.getString(R.string.event_state_waiting), false);
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