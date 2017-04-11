package com.langoor.app.blueshak.home;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

public abstract class EndlessRecyclerOnScrollListenerLinearView extends RecyclerView.OnScrollListener {
	public static String TAG = EndlessRecyclerOnScrollListenerLinearView.class
			.getSimpleName();
	private int previousTotal = 0;
	private boolean loading = true;
	private int visibleThreshold = 5;
	int firstVisibleItem, visibleItemCount, totalItemCount;
	private int current_page = 1;
	private LinearLayoutManager mLinearLayoutManager;
	RecyclerViewPositionHelper mRecyclerViewHelper;
	public EndlessRecyclerOnScrollListenerLinearView(
			LinearLayoutManager linearLayoutManager) {
		this.mLinearLayoutManager = linearLayoutManager;
	}

	@Override
	public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
		super.onScrolled(recyclerView, dx, dy);
		mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(recyclerView);
		visibleItemCount = recyclerView.getChildCount();
		totalItemCount = mRecyclerViewHelper.getItemCount();
		firstVisibleItem = mRecyclerViewHelper.findFirstVisibleItemPosition();
		/*visibleItemCount = recyclerView.getChildCount();
		totalItemCount = mLinearLayoutManager.getItemCount();
		firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

	*/	if (loading) {
			if (totalItemCount > previousTotal) {
				loading = false;
				previousTotal = totalItemCount;
			}
		}
		if (!loading
				&& (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
			// End has been reached

			// Do something
			current_page++;

			onLoadMore(current_page);

			loading = true;
		}
	}

	public abstract void onLoadMore(int current_page);
	public void reset(int previousTotal, boolean loading) {
		this.previousTotal = previousTotal;
		this.loading = loading;
/*		this.totalItemCount=0;
		this.visibleItemCount=0;
		this.firstVisibleItem=0;*/
		this.current_page=1;

	}
}