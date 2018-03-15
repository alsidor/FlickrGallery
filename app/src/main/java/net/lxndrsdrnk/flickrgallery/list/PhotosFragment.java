package net.lxndrsdrnk.flickrgallery.list;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.lxndrsdrnk.flickrgallery.FGApplication;
import net.lxndrsdrnk.flickrgallery.R;
import net.lxndrsdrnk.flickrgallery.api.FlickrAPI;
import net.lxndrsdrnk.flickrgallery.api.FlickrResponse;
import net.lxndrsdrnk.flickrgallery.api.Photo;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A fragment representing a list of Flickr Photos.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PhotosFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 3;
    private OnListFragmentInteractionListener mListener;

    private String mSearchValue;

    @BindView(R.id.photosList)
    RecyclerView mPhotosRecyclerView;
    InfinitePhotoRecyclerViewAdapter mPhotosAdapter;
    InfiniteRecyclerViewScrollListener mInfinteScrollAdapter;


    @Inject
    FlickrAPI flickrAPI;

    public PhotosFragment() {
    }

    public static PhotosFragment newInstance(int columnCount) {
        PhotosFragment fragment = new PhotosFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((FGApplication) getActivity().getApplication()).component().inject(this);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_list, container, false);

        ButterKnife.bind(this, view);

        mPhotosAdapter = new InfinitePhotoRecyclerViewAdapter(new ArrayList<Photo>(), mListener);

        GridLayoutManager layoutManager = new CustomGridLayoutManager(view.getContext(), mColumnCount);

        mPhotosRecyclerView.setLayoutManager(layoutManager);
        mPhotosRecyclerView.setAdapter(mPhotosAdapter);

        mInfinteScrollAdapter = new InfiniteRecyclerViewScrollListener(layoutManager){

            @Override
            void requestData(int pageNum, int pageSize) {
                Log.d("SCROLL", "Request pageNum="+pageNum+" pageSize="+pageSize);
                loadData(pageNum, pageSize);
            }
        };

        mPhotosRecyclerView.addOnScrollListener(mInfinteScrollAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadData(1, 50);
    }

    public void setSearchValue(String searchValue){
        this.mSearchValue = searchValue;

        mPhotosAdapter.reset();

        loadData(1, 50);
    }

    protected void loadData(int pageNum, int pageSize){

        final Callback<FlickrResponse> callback = new Callback<FlickrResponse>() {
            @Override
            public void onResponse(Call<FlickrResponse> call, Response<FlickrResponse> response) {
                mPhotosAdapter.appendValues(response.body().photos.photo);
                mInfinteScrollAdapter.notifyDataLoaded();
            }

            @Override
            public void onFailure(Call<FlickrResponse> call, Throwable t) {
                mInfinteScrollAdapter.notifyDataLoaded();
            }
        };


        if( TextUtils.isEmpty(mSearchValue) ) {
            flickrAPI.getRecent(pageNum, pageSize).enqueue(callback);
        } else {
            flickrAPI.search(mSearchValue, pageNum, pageSize).enqueue(callback);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Photo photo);
    }
}
