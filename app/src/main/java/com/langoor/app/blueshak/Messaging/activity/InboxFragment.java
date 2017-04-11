package com.langoor.app.blueshak.Messaging.activity;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.langoor.blueshak.R;
import com.langoor.app.blueshak.Messaging.adapter.MessagePagerAdapter;
import com.langoor.app.blueshak.Messaging.fragment.BuyerFragment;
import com.langoor.app.blueshak.Messaging.fragment.SellerFragment;

import java.util.ArrayList;

public class InboxFragment extends Fragment{
	public static final String TAG = "Message";
	private ViewPager mViewPager;
	private TabHost mTabHost;
	private String[] tabs = { "All", "Buyer", "Seller" };
	private Toolbar toolbar;
	private ActionBar actionBar;
	private static  Context context;
	private Activity activity;
	private Window window;
	private static MessagePagerAdapter adapter;
	private TabLayout tabLayout;
	private BuyerFragment buyerFragment;
	private SellerFragment sellerFragment;
	private TextView tab1,tab2;
	public static FragmentManager mainActivityFM;
	boolean is_tab1_selected=true;
	private ImageView go_to_filter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context=getContext();
		activity=getActivity();
		View rootView = inflater.inflate(R.layout.activity_message2, container, false);
		Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
		go_to_filter=(ImageView)toolbar.findViewById(R.id.go_to_filter);
		go_to_filter.setVisibility(View.GONE);
		buyerFragment=new BuyerFragment();
		sellerFragment=new SellerFragment();
		mainActivityFM = getChildFragmentManager();
		mainActivityFM.beginTransaction().replace(R.id.container, buyerFragment, "").commit();
		tab1=(TextView)rootView.findViewById(R.id.tab1);
		tab2=(TextView)rootView.findViewById(R.id.tab2);
		tab1.setText("Buyer");
		tab2.setText("Seller");
		tab1.setOnClickListener(new View.OnClickListener() {
			@Override

			public void onClick(View v) {
				if(!is_tab1_selected){
					tab1.setTextColor(context.getResources().getColor(R.color.white));
					tab1.setBackgroundColor(context.getResources().getColor(R.color.brandColor));
					tab2.setTextColor(context.getResources().getColor(R.color.brandColor));
					tab2.setBackgroundColor(context.getResources().getColor(R.color.brand_secondary_color));
					mainActivityFM.beginTransaction().replace(R.id.container, buyerFragment, "").commit();
					is_tab1_selected=true;

				}
			}
		});
		tab2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(is_tab1_selected){
					tab2.setTextColor(context.getResources().getColor(R.color.white));
					tab2.setBackgroundColor(context.getResources().getColor(R.color.brandColor));
					tab1.setTextColor(context.getResources().getColor(R.color.brandColor));
					tab1.setBackgroundColor(context.getResources().getColor(R.color.brand_secondary_color));
					mainActivityFM.beginTransaction().replace(R.id.container, sellerFragment, "").commit();
					is_tab1_selected=false;
				}

			}
		});
		return rootView;
	}
	@Override
	public void onResume() {

		super.onResume();
	}
}
