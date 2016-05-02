/*
Copyright info
*/


package ua.dp.strahovik.yalantistask1.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import ua.dp.strahovik.yalantistask1.R;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private List<URI> mList;
    private Context mContext;
    private OnItemClickListener mOnEventClickListener;

    public void setList(List<URI> list) {
        mList = list;
    }

    public void setOnItemClickListener(OnItemClickListener onEventClickListener) {
        mOnEventClickListener = onEventClickListener;
    }

    public ImageAdapter() {
        mList = new ArrayList<>();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.recycler_image_view);
        }
    }


    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        View imageView = layoutInflater.inflate(R.layout.item_image, parent, false);

        return new ViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(ImageAdapter.ViewHolder holder, final int position) {
        URI uri = mList.get(position);

        final int pos = position;
        ImageView imageView = holder.mImageView;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnEventClickListener != null) {
                    mOnEventClickListener.onItemClick(pos);
                }
            }
        });
        Picasso.with(mContext)
                .load(String.valueOf(uri))
                .placeholder(R.drawable.image_view_placeholder_deafault)
                .error(R.drawable.image_view_placeholder_error)
                .resizeDimen(R.dimen.content_main_image_width, R.dimen.content_main_image_height)
                .centerCrop()
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
