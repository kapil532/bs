package com.langoor.app.blueshak.Messaging.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.langoor.blueshak.R;
import com.langoor.app.blueshak.AppController;
import com.langoor.app.blueshak.Messaging.adapter.NotificationListAdapter;
import com.langoor.app.blueshak.services.model.NotificationModel;
import java.util.ArrayList;

public class NotificationsFragment extends ListFragment implements SwipeRefreshLayout.OnRefreshListener{
	private ListView myList_apps;
	private Activity activity;
	private TextView no_Chats_label;
	private ArrayList<NotificationModel> notificationModels_list = new ArrayList<NotificationModel>();
	private TextView label;
	private Context context;
	private SwipeRefreshLayout swipeRefreshLayout;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.content_notification, container, false);
		context=getContext();
		activity=getActivity();
		no_Chats_label=(TextView)rootView.findViewById(R.id.no_Chats_label);

	/*	swipeRefreshLayout.setOnRefreshListener(this);
     */ /*  final SwipeRefreshLayout.OnRefreshListener swipeRefreshListner = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "onRefresh called from SwipeRefreshLayout");
              *//*  swipeRefreshLayout.setRefreshing(true);
               *//**//* setThisPage();*//*
            }
        };*/
		/*swipeRefreshLayout.setColorSchemeColors(context.getResources().getColor(R.color.brandColor),
				context.getResources().getColor(R.color.tab_selected),
				context.getResources().getColor(R.color.darkorange),
				context.getResources().getColor(R.color.brandColor));*/
		return rootView;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		notificationModels_list = AppController.getInstance().getPrefManager().getBlueshakNotifications();
		if (notificationModels_list != null) {
			if (!notificationModels_list.isEmpty() && notificationModels_list.size() > 0) {

			/*	myList_apps.setAdapter(new NotificationListAdapter(notificationModels_list, activity));
			*/	no_Chats_label.setVisibility(View.GONE);
				NotificationListAdapter notificationListAdapter=new NotificationListAdapter(notificationModels_list,activity);
				setListAdapter(notificationListAdapter);

			}
		}
	}
	@Override
	public void onRefresh() {

	}
}
