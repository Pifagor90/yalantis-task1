/*
Copyright info
*/


package ua.dp.strahovik.yalantistask1.decorators;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import ua.dp.strahovik.yalantistask1.R;


public class ListEventRecyclerDecorator extends RecyclerView.ItemDecoration {

    private Context mContext;

    public ListEventRecyclerDecorator(Context context) {
        super();
        mContext = context;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        parent.getChildLayoutPosition(view);
        outRect.bottom = mContext.getResources().getDimensionPixelSize(R.dimen.list_event_recycler);
    }

}
