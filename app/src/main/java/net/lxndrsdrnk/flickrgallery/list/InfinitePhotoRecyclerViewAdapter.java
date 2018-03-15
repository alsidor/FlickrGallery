package net.lxndrsdrnk.flickrgallery.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import net.lxndrsdrnk.flickrgallery.R;
import net.lxndrsdrnk.flickrgallery.api.Photo;

import java.util.List;



public class InfinitePhotoRecyclerViewAdapter extends RecyclerView.Adapter<InfinitePhotoRecyclerViewAdapter.ViewHolder> {

    private final List<Photo> mValues;
    private final PhotosFragment.OnListFragmentInteractionListener mListener;

    public InfinitePhotoRecyclerViewAdapter(List<Photo> items, PhotosFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.photo_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mPhoto = mValues.get(position);

        Picasso.get().load(holder.mPhoto.url_s)
//                .error(R.drawable.placeholder)
//                .placeholder(R.drawable.placeholder)
                .into(holder.mPhotoImageView);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onPhotoSelected(holder.mPhoto);
                }
            }
        });
    }

    public void appendValues(List<Photo> newValues){
        int positionStart = mValues.size();
        mValues.addAll(newValues);
        notifyItemRangeChanged(positionStart, newValues.size());
    }

    public void reset(){
        mValues.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mPhotoImageView;
        public Photo mPhoto;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mPhotoImageView = (ImageView) mView;
        }
    }
}
