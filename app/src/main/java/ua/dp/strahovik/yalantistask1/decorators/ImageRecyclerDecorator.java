/*
Copyright info
*/


package ua.dp.strahovik.yalantistask1.decorators;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import ua.dp.strahovik.yalantistask1.R;


public class ImageRecyclerDecorator extends RecyclerView.ItemDecoration {

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.right = 32;
    }

}
