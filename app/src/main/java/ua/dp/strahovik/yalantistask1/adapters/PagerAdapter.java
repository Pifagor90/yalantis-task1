package ua.dp.strahovik.yalantistask1.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.os.ResultReceiver;

import java.util.HashMap;
import java.util.Map;

import ua.dp.strahovik.yalantistask1.R;
import ua.dp.strahovik.yalantistask1.view.fragment.ListEventFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;
    private Map<int[], Boolean> mLoadEventsState;
    private int[] mInProgressArr;
    private int[] mDoneArr;
    private int[] mPendingArr;
    private ResultReceiver mReceiver;


    public PagerAdapter(Context context, FragmentManager fragmentManager, int NumOfTabs,
                        ResultReceiver receiver) {
        super(fragmentManager);
        mNumOfTabs = NumOfTabs;
        mLoadEventsState = new HashMap<>(NumOfTabs);
        mInProgressArr = context.getResources().
                getIntArray(R.array.list_event_activity_pager_adapter_in_progress);
        mDoneArr = context.getResources().
                getIntArray(R.array.list_event_activity_pager_adapter_done);
        mPendingArr = context.getResources().
                getIntArray(R.array.list_event_activity_pager_adapter_pending);

        mLoadEventsState.put(mInProgressArr, false);
        mLoadEventsState.put(mDoneArr, false);
        mLoadEventsState.put(mPendingArr, false);
        mReceiver = receiver;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return initNewFragment(mInProgressArr);
            case 1:
                return initNewFragment(mDoneArr);
            case 2:
                return initNewFragment(mPendingArr);
            default:
                return null;
        }
    }

    private ListEventFragment initNewFragment(int[] requestArray) {
        return ListEventFragment.newInstance(requestArray, mLoadEventsState.get(requestArray),
                mReceiver);
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    public void accept(int[] request, boolean isLoading) {
        mLoadEventsState.put(request, isLoading);
    }
}