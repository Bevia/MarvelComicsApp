package com.corebaseit.bevia.marvelcomicsreleaseapp.interfaces;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class OnComicsRecycleViewScrollListener extends RecyclerView.OnScrollListener {

    private int previousTotal = 8; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private final static int VISIBLE_THRESHOLD = 12; // The minimum amount of items to have below your current scroll position before loading more.
    int lastVisibleItem, totalItemCount;

    private LinearLayoutManager mLinearLayoutManager;

    public OnComicsRecycleViewScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        totalItemCount = recyclerView.getAdapter().getItemCount();
        lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();

        if (loading) {

            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }

        if (!loading && (totalItemCount <= (lastVisibleItem + VISIBLE_THRESHOLD))) {
            loading = true;
            onLoadMore();

        }
    }

    public abstract void onLoadMore();
}
