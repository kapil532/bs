package com.blueshak.app.blueshak.Messaging.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.blueshak.blueshak.R;
import com.blueshak.app.blueshak.Messaging.adapter.MessagePagerAdapter;
import com.blueshak.app.blueshak.global.GlobalFunctions;
import com.blueshak.app.blueshak.global.GlobalVariables;

/*import com.langoor.app.blueshak.search.SearchActivity;*/

public class MessageActivity extends PushActivity/* implements  MessageManager.MessageListenerOne*/{

    public static final String TAG = "Message";
    private ViewPager mViewPager;
    private TabHost mTabHost;
    private String[] tabs = { "All", "Buyer", "Seller" };
    private Toolbar toolbar;
    private ActionBar actionBar;
    private static  Context context;
    private Activity activity;
    private Window window;
    private static  MessagePagerAdapter adapter;
    private TabLayout tabLayout;
    static GlobalFunctions globalFunctions = new GlobalFunctions();
    static GlobalVariables globalVariables = new GlobalVariables();
    private TextView close_button;
    private ImageView go_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message2);
        context=this;
        activity=this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        LayoutInflater inflator = LayoutInflater.from(this);
        View v = inflator.inflate(R.layout.action_bar_titlel, null);
        ((TextView)v.findViewById(R.id.title)).setText("Offers");
        toolbar.addView(v);
        close_button=(TextView)v.findViewById(R.id.cancel);
        close_button.setVisibility(View.GONE);
        go_back=(ImageView)findViewById(R.id.go_back);
        go_back.setVisibility(View.VISIBLE);
        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
       /* ArrayList<String> tabs = new ArrayList<>();
        tabs.add("Buyer");
        tabs.add("Seller");
		*//*tabs.add("Notifications");*//*
        tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setBackgroundColor(activity.getResources().getColor(R.color.brand_secondary_color));
        tabLayout.setSelectedTabIndicatorColor(activity.getResources().getColor(R.color.brandColor));
        tabLayout.setTabTextColors(activity.getResources().getColor(R.color.white),activity.getResources().getColor(R.color.brandColor));
		*//*mBottomBar.setTypeFace("Raleway-Bold.ttf");*//*


        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new MessagePagerAdapter(getSupportFragmentManager(), tabs);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
*/
       /* MessageManager.getInstance(this).setMessageListenerOne(this);*/
       /* setTags(context);*/
    }
    public void refresh(){
        if(adapter!=null){
            Log.d(TAG,"#########refresh notifyDataSetChanged");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });

        }

    }
   /* @Override
    public void onReceiveMessageOne(Message receivedMessage) {
        updateRow(receivedMessage,this);
    }*/

    }