/*
Copyright info
*/


package ua.dp.strahovik.yalantistask1.decorators;

import android.app.Activity;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import ua.dp.strahovik.yalantistask1.R;


public class ImageRecyclerDecorator extends RecyclerView.ItemDecoration {

    private Activity mActivity;

    public ImageRecyclerDecorator(Activity activity) {
        super();
        this.mActivity = activity;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        parent.getChildLayoutPosition(view);
        outRect.right = mActivity.getResources().getDimensionPixelSize(R.dimen.content_main_image_padding);
    }

}
