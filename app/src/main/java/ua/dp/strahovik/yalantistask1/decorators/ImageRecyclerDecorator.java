/*
Copyright info
*/


package ua.dp.strahovik.yalantistask1.decorators;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import ua.dp.strahovik.yalantistask1.R;


public class ImageRecyclerDecorator extends RecyclerView.ItemDecoration {

    private Context mContext;

    public ImageRecyclerDecorator(Context context) {
        super();
        this.mContext = context; //[Comment] Without "this"
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        parent.getChildLayoutPosition(view);
        outRect.right = mContext.getResources().getDimensionPixelSize(R.dimen.content_main_image_padding);
    }

}
