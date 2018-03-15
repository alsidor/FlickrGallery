package net.lxndrsdrnk.flickrgallery;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.lxndrsdrnk.flickrgallery.PhotosFragment.OnListFragmentInteractionListener;
import net.lxndrsdrnk.flickrgallery.api.Photo;

import java.util.List;



public class MyPhotoRecyclerViewAdapter extends RecyclerView.Adapter<MyPhotoRecyclerViewAdapter.ViewHolder> {

    private final List<Photo> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyPhotoRecyclerViewAdapter(List<Photo> items, OnListFragmentInteractionListener listener) {
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

//        Picasso.get().load(holder.mPhoto.)
//                .error(R.drawable.placeholder)
//                .placeholder(R.drawable.placeholder)
//                .into(feedListRowHolder.thumbnail);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mPhoto);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mAltTextView;
        public final ImageView mPhotoImageView;
        public Photo mPhoto;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mAltTextView = view.findViewById(R.id.altTextView);
            mPhotoImageView = view.findViewById(R.id.photoImageView);
        }
    }
}
