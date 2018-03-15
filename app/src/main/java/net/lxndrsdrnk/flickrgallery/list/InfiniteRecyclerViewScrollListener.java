package net.lxndrsdrnk.flickrgallery.list;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by alsidor on 15/03/2018.
 */

public abstract class InfiniteRecyclerViewScrollListener extends RecyclerView.OnScrollListener {


    private int mLinesThreshold = 5;
    private int mItemsThreshold = mLinesThreshold;

    private int mPageSize = 50;

    private boolean isPendingData = false;
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
            int nextPageNum = (totalItemCount / mPageSize) + 1;

            requestData(nextPageNum, mPageSize);
            isPendingData = true;
        }
    }

    public void setHaveMoreData(boolean haveMoreData) {
        this.mHaveMoreData = haveMoreData;
    }

    public void notifyDataLoaded(){
        isPendingData = false;
    }

    abstract void requestData(int pageNum, int pageSize);

}
