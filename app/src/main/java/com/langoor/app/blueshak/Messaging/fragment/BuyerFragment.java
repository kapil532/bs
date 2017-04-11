package com.langoor.app.blueshak.Messaging.fragment;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.langoor.blueshak.R;
import com.langoor.app.blueshak.Messaging.adapter.ContactsListAdapter;
import com.langoor.app.blueshak.Messaging.data.Message;
import com.langoor.app.blueshak.Messaging.manager.MessageManager;
import com.langoor.app.blueshak.global.GlobalFunctions;
import com.langoor.app.blueshak.global.GlobalVariables;
import com.langoor.app.blueshak.login.LoginActivity;
import com.langoor.app.blueshak.services.ServerResponseInterface;
import com.langoor.app.blueshak.services.ServicesMethodsManager;
import com.langoor.app.blueshak.services.model.ContactModel;
import com.langoor.app.blueshak.services.model.ContactsListModel;
import com.langoor.app.blueshak.services.model.ErrorModel;
import com.langoor.app.blueshak.services.model.StatusModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class BuyerFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,MessageManager.MessageListenerOne{
	private String TAG = "BuyerFragmentold";
	private TextView label;
	private SwipeRefreshLayout swipeRefreshLayout;
	private RecyclerView recyclerView;
	private Context context;
	private ContactsListAdapter adapter;
	private List<ContactModel> contact_list = new ArrayList<ContactModel>();
	private ContactsListModel contactModel=new ContactsListModel();
	private Handler handler=new Handler();
	private static Activity activity;
	private ProgressBar progress_bar;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context=getContext();
		activity=getActivity();
		View rootView = inflater.inflate(R.layout.message_buyer_fragment_new, container, false);
		progress_bar=(ProgressBar)rootView.findViewById(R.id.progress_bar);
		label=(TextView)rootView.findViewById(R.id.no_recent_chats);
		recyclerView=(RecyclerView)rootView.findViewById(R.id.recycler_view);
		adapter = new ContactsListAdapter(context, contact_list,GlobalVariables.TYPE_BUYER_TAB);
		LinearLayoutManager linearLayoutManagerVertical =
				new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
		recyclerView.setLayoutManager(linearLayoutManagerVertical);
		recyclerView.setAdapter(adapter);
		setUpItemTouchHelper();
		setUpAnimationDecoratorHelper();
		adapter.setUndoOn(false);
		recyclerView.addItemDecoration(new SimpleDividerItemDecoration(activity));
		MessageManager.getInstance(getActivity()).setMessageListenerOne(this);
	/*	recyclerView.addOnItemTouchListener(
				new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
					@Override public void onItemClick(View view, int position) {
						Log.d(TAG,"position####"+position);
						Toast.makeText(context,"Clicked On "+contact_list.get(position).getContact_name(),Toast.LENGTH_LONG).show();
						// do whatever
					}
				})
		);*/
		swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
		swipeRefreshLayout.setOnRefreshListener(this);
		final SwipeRefreshLayout.OnRefreshListener swipeRefreshListner = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "onRefresh called from SwipeRefreshLayout");

            }
        };
		swipeRefreshLayout.setColorSchemeColors(context.getResources().getColor(R.color.brandColor),
				context.getResources().getColor(R.color.tab_selected),
				context.getResources().getColor(R.color.darkorange),
				context.getResources().getColor(R.color.brandColor));

		return rootView;
	}
	@Override
	public void onResume() {
		try{
			if(GlobalFunctions.isNetworkAvailable(context)){
				if(GlobalFunctions.is_loggedIn(context))
					getConversationContacts(context);
				else
					showSettingsAlert();
			}else {
				label.setVisibility(View.VISIBLE);
					Snackbar.make(label, "Please check your internet connection", Snackbar.LENGTH_LONG)
							.setAction("Retry", new View.OnClickListener() {
								@Override
								@TargetApi(Build.VERSION_CODES.M)
								public void onClick(View v) {
									if(GlobalFunctions.is_loggedIn(context))
										getConversationContacts(context);
									else
										showSettingsAlert();
								}
							}).show();
			}
			super.onResume();
		} catch (NullPointerException e){
			Log.d(TAG,"NullPointerException");
			e.printStackTrace();
		}catch (NumberFormatException e) {
			Log.d(TAG,"NumberFormatException");
			e.printStackTrace();
		}catch (Exception e){
			Log.d(TAG,"Exception");
			e.printStackTrace();
		}
	}

	@Override
	public void onRefresh() {
		if(GlobalFunctions.is_loggedIn(context))
			getConversationContacts(context);
		else
			showSettingsAlert();
	}
	private void getConversationContacts(final Context context){
		showProgressBar();
		ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
		servicesMethodsManager.getConversationContacts(context,new ContactsListModel(),GlobalVariables.TYPE_BUYER_TAB, new ServerResponseInterface() {
			@Override
			public void OnSuccessFromServer(Object arg0) {
				hideProgressBar();
				if(arg0 instanceof StatusModel){
					swipeRefreshLayout.setRefreshing(false);
					StatusModel statusModel = (StatusModel) arg0;
					label.setVisibility(View.VISIBLE);
				}else if(arg0 instanceof ErrorModel){
					swipeRefreshLayout.setRefreshing(false);
					ErrorModel errorModel = (ErrorModel) arg0;
					String msg = errorModel.getError()!=null ? errorModel.getError() : errorModel.getMessage();
					label.setVisibility(View.VISIBLE);

				}else if(arg0 instanceof ContactsListModel){
					swipeRefreshLayout.setRefreshing(false);
					label.setVisibility(View.GONE);
					ContactsListModel model = (ContactsListModel) arg0;
					setValues(model);
					Log.d(TAG,"ContactsListModel###########"+model.toString());
				}
			}

			@Override
			public void OnFailureFromServer(String msg) {
				label.setVisibility(View.VISIBLE);
				hideProgressBar();
				Log.d(TAG,"OnFailureFromServer"+msg);
			}

			@Override
			public void OnError(String msg) {
				hideProgressBar();
				label.setVisibility(View.VISIBLE);
				swipeRefreshLayout.setRefreshing(false);
				Log.d(TAG,"OnFailureFromServer"+msg);

			}
		}, "Get Bookmarks");

	}
	private void setValues(ContactsListModel model){
		if(model!=null){
			contactModel = model;
			List<ContactModel> contactModels = contactModel.getConversations_contacts();
			if(contactModels!=null){
				if(contactModels.size()>0&&recyclerView!=null&&contact_list!=null&&adapter!=null){
					contact_list.clear();
					contact_list.addAll(contactModels);
					refreshList();
				}else
					label.setVisibility(View.VISIBLE);
			}else
				label.setVisibility(View.VISIBLE);
		}else
			label.setVisibility(View.VISIBLE);

	}
	public void showProgress(){
		swipeRefreshLayout.post(new Runnable() {
									@Override
									public void run() {
										swipeRefreshLayout.setRefreshing(true);

										swipeRefreshLayout.setRefreshing(true);
									}
								}
		);
	}
	@Override
	public void onReceiveMessageOne(Message receivedMessage) {
		if(GlobalFunctions.is_loggedIn(context))
			getConversationContacts(context);
		else
			showSettingsAlert();
	}
	public void refreshList(){
		handler.post(new Runnable() {
			@Override
			public void run() {
				synchronized (adapter){adapter.notifyDataSetChanged();}
			}
		});
	}
	public void showSettingsAlert(){
		Intent creategarrage = new Intent(activity,LoginActivity.class);
		startActivity(creategarrage);
	}
	public static void closeThisActivity(){
		if(activity!=null){activity.finish();}
	}
	public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
		private Drawable mDivider;

		public SimpleDividerItemDecoration(Context context) {
			mDivider = context.getResources().getDrawable(R.drawable.line_divider);
		}

		@Override
		public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
			int left = parent.getPaddingLeft();
			int right = parent.getWidth() - parent.getPaddingRight();

			int childCount = parent.getChildCount();
			for (int i = 0; i < childCount; i++) {
				View child = parent.getChildAt(i);

				RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

				int top = child.getBottom() + params.bottomMargin;
				int bottom = top + mDivider.getIntrinsicHeight();

				mDivider.setBounds(left, top, right, bottom);
				mDivider.draw(c);
			}
		}
	}
	public void showProgressBar(){
		if(progress_bar!=null)
			progress_bar.setVisibility(View.VISIBLE);
	}
	public void hideProgressBar(){
		if(progress_bar!=null)
			progress_bar.setVisibility(View.GONE);
	}
	/**
	 * This is the standard support library way of implementing "swipe to delete" feature. You can do custom drawing in onChildDraw method
	 * but whatever you draw will disappear once the swipe is over, and while the items are animating to their new position the recycler view
	 * background will be visible. That is rarely an desired effect.
	 */
	private void setUpItemTouchHelper() {

		ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

			// we want to cache these and not allocate anything repeatedly in the onChildDraw method
			Drawable background;
			Drawable xMark;
			int xMarkMargin;
			boolean initiated;

			private void init() {
				background = new ColorDrawable(context.getResources().getColor(R.color.brandColor));
				xMark = ContextCompat.getDrawable(context, R.drawable.ic_clear_black_24dp);
				xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
				xMarkMargin = (int) context.getResources().getDimension(R.dimen.ic_clear_margin);
				initiated = true;
			}

			// not important, we don't want drag & drop
			@Override
			public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
				return false;
			}

			@Override
			public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
				int position = viewHolder.getAdapterPosition();
				 adapter = (ContactsListAdapter)recyclerView.getAdapter();
				if (adapter.isUndoOn() && adapter.isPendingRemoval(position)) {
					return 0;
				}
				return super.getSwipeDirs(recyclerView, viewHolder);
			}

			@Override
			public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
				int swipedPosition = viewHolder.getAdapterPosition();
				 adapter = (ContactsListAdapter) recyclerView.getAdapter();
				boolean undoOn = adapter.isUndoOn();
				if (undoOn) {
					adapter.pendingRemoval(swipedPosition);
				} else {
					adapter.remove(swipedPosition);
				}
			}

			@Override
			public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
				View itemView = viewHolder.itemView;

				// not sure why, but this method get's called for viewholder that are already swiped away
				if (viewHolder.getAdapterPosition() == -1) {
					// not interested in those
					return;
				}

				if (!initiated) {
					init();
				}

				// draw red background
				background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
				background.draw(c);

				// draw x mark
				int itemHeight = itemView.getBottom() - itemView.getTop();
				int intrinsicWidth = xMark.getIntrinsicWidth();
				int intrinsicHeight = xMark.getIntrinsicWidth();

				int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
				int xMarkRight = itemView.getRight() - xMarkMargin;
				int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
				int xMarkBottom = xMarkTop + intrinsicHeight;
				xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

				xMark.draw(c);

				super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
			}

		};
		ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
		mItemTouchHelper.attachToRecyclerView(recyclerView);
	}

	/**
	 * We're gonna setup another ItemDecorator that will draw the red background in the empty space while the items are animating to thier new positions
	 * after an item is removed.
	 */
	private void setUpAnimationDecoratorHelper() {
		recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

			// we want to cache this and not allocate anything repeatedly in the onDraw method
			Drawable background;
			boolean initiated;

			private void init() {
				background = new ColorDrawable(context.getResources().getColor(R.color.brandColor));
				initiated = true;
			}

			@Override
			public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

				if (!initiated) {
					init();
				}

				// only if animation is in progress
				if (parent.getItemAnimator().isRunning()) {

					// some items might be animating down and some items might be animating up to close the gap left by the removed item
					// this is not exclusive, both movement can be happening at the same time
					// to reproduce this leave just enough items so the first one and the last one would be just a little off screen
					// then remove one from the middle

					// find first child with translationY > 0
					// and last one with translationY < 0
					// we're after a rect that is not covered in recycler-view views at this point in time
					View lastViewComingDown = null;
					View firstViewComingUp = null;

					// this is fixed
					int left = 0;
					int right = parent.getWidth();

					// this we need to find out
					int top = 0;
					int bottom = 0;

					// find relevant translating views
					int childCount = parent.getLayoutManager().getChildCount();
					for (int i = 0; i < childCount; i++) {
						View child = parent.getLayoutManager().getChildAt(i);
						if (child.getTranslationY() < 0) {
							// view is coming down
							lastViewComingDown = child;
						} else if (child.getTranslationY() > 0) {
							// view is coming up
							if (firstViewComingUp == null) {
								firstViewComingUp = child;
							}
						}
					}

					if (lastViewComingDown != null && firstViewComingUp != null) {
						// views are coming down AND going up to fill the void
						top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
						bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
					} else if (lastViewComingDown != null) {
						// views are going down to fill the void
						top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
						bottom = lastViewComingDown.getBottom();
					} else if (firstViewComingUp != null) {
						// views are coming up to fill the void
						top = firstViewComingUp.getTop();
						bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
					}

					background.setBounds(left, top, right, bottom);
					background.draw(c);

				}
				super.onDraw(c, parent, state);
			}

		});
	}




}
