package net.lxndrsdrnk.flickrgallery.list;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Analyze RecyclerView and LayoutManger state and request new data in advance
 * Created by alsidor on 15/03/2018.
 */

public abstract class InfiniteRecyclerViewScrollListener extends RecyclerView.OnScrollListener {


    /**
     * Number of lines treshold to start loading next portion of data
     */
    private int mLinesThreshold = 5;
    private int mItemsThreshold = mLinesThreshold;

    private int mPageSize = 50;

    /**
     * True when the data was requested
     */
    private boolean isPendingData = false;
    /**
     * False when the end of endless data reached
     */
    private boolean mHaveMoreData = true;

    private GridLayoutManager mLayoutManager;

    public InfiniteRecyclerViewScrollListener(GridLayoutManager layoutManager, int pageSize) {
        this.mLayoutManager = layoutManager;
        this.mItemsThreshold = mLinesThreshold * layoutManager.getSpanCount();
        this.mPageSize = pageSize;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

        int totalItemCount = mLayoutManager.getItemCount();
        int lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();

        if( mHaveMoreData && !isPendingData && (lastVisibleItem + mItemsThreshold >= totalItemCount) ){
            //We are close to bottom, time to load next portion

            int nextPageNum = (totalItemCount / mPageSize) + 1;

            requestData(nextPageNum, mPageSize);
            isPendingData = true;
        }

        if( mHaveMoreData && totalItemCount == lastVisibleItem + 1){
            //We are at the bottom, but data didn't arrived yet
            onDataHunger();
        }
    }

    public void setHaveMoreData(boolean haveMoreData) {
        this.mHaveMoreData = haveMoreData;
    }

    public void notifyDataLoaded(){
        isPendingData = false;
    }

    abstract void requestData(int pageNum, int pageSize);

    abstract void onDataHunger();

}
