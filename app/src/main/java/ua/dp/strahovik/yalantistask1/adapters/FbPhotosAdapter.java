package ua.dp.strahovik.yalantistask1.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.dp.strahovik.yalantistask1.R;

public class FbPhotosAdapter extends RecyclerView.Adapter<FbPhotosAdapter.ViewHolder> {

    private List<String> mList;
    private Context mContext;

    public FbPhotosAdapter() {
        mList = new ArrayList<>();
    }

    public void setList(List<String> list) {
        mList = list;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recycler_image_view)
        ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public FbPhotosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        View imageView = layoutInflater.inflate(R.layout.item_image, parent, false);

        return new ViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(FbPhotosAdapter.ViewHolder holder, final int position) {
        Picasso.with(mContext)
                .load(mList.get(position))
                .placeholder(R.drawable.image_view_placeholder_deafault)
                .error(R.drawable.image_view_placeholder_error)
                .resizeDimen(R.dimen.content_main_image_width, R.dimen.content_main_image_height)
                .centerCrop()
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
