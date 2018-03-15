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

    private GridLayoutManager mLayoutManager;

    public InfiniteRecyclerViewScrollListener(GridLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
        this.mItemsThreshold = mLinesThreshold * layoutManager.getSpanCount();
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

        int totalItemCount = mLayoutManager.getItemCount();
        int lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();

        if( !isPendingData && (lastVisibleItem + mItemsThreshold >= totalItemCount) ){
            int nextPageNum = (totalItemCount / mPageSize) + 1;

            if( totalItemCount % mPageSize == 0 ) {
                requestData(nextPageNum, mPageSize);
            }
            isPendingData = true;
        }
    }

    public void notifyDataChanged(){
        isPendingData = false;
    }

    abstract void requestData(int pageNum, int pageSize);
}
