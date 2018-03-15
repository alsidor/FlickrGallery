package net.lxndrsdrnk.flickrgallery.list;

import android.content.Context;
import android.content.SharedPreferences;
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
import net.lxndrsdrnk.flickrgallery.SettingsKeys;
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

    private static final String ARG_SEARCH_VALUE = "search_value";

    private int mColumnCount = 3;
    private int mPageSize = 50;


    private OnListFragmentInteractionListener mListener;

    private String mSearchValue;

    @BindView(R.id.photosList)
    RecyclerView mPhotosRecyclerView;
    InfinitePhotoRecyclerViewAdapter mPhotosAdapter;
    InfiniteRecyclerViewScrollListener mInfinteScrollAdapter;


    @Inject
    FlickrAPI flickrAPI;

    @Inject
    SharedPreferences sharedPreferences;

    public PhotosFragment() {
    }

    public static PhotosFragment newInstance(String searchValue) {
        PhotosFragment fragment = new PhotosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SEARCH_VALUE, searchValue);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((FGApplication) getActivity().getApplication()).component().inject(this);

        if (getArguments() != null) {
            mSearchValue = getArguments().getString(ARG_SEARCH_VALUE);
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

        mInfinteScrollAdapter = new InfiniteRecyclerViewScrollListener(layoutManager, mPageSize){

            @Override
            void requestData(int pageNum, int pageSize) {
                loadData(pageNum, pageSize);
            }
        };

        mPhotosRecyclerView.addOnScrollListener(mInfinteScrollAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadData(1, mPageSize);
    }

    public void setSearchValue(String searchValue){
        this.mSearchValue = searchValue;
        getArguments().putString(ARG_SEARCH_VALUE, mSearchValue);

        mPhotosAdapter.reset();

        loadData(1, mPageSize);
    }

    public void refresh() {
        mPhotosAdapter.reset();

        loadData(1, mPageSize);
    }


    protected void loadData(final int pageNum, int pageSize){

        final Callback<FlickrResponse> callback = new Callback<FlickrResponse>() {
            @Override
            public void onResponse(Call<FlickrResponse> call, Response<FlickrResponse> response) {
                FlickrResponse flickrResponse = response.body();
                mPhotosAdapter.appendValues(flickrResponse.photos.photo);
                mInfinteScrollAdapter.notifyDataLoaded();

                if(pageNum == 1 && !TextUtils.isEmpty(mSearchValue) && !flickrResponse.photos.photo.isEmpty()){
                    sharedPreferences.edit().putString(SettingsKeys.LAST_PHOTO_ID, flickrResponse.photos.photo.get(0).id).commit();
                }
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
        void onPhotoSelected(Photo photo);
    }
}
