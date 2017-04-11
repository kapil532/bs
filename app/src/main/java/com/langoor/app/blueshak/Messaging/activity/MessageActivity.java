package com.langoor.app.blueshak.Messaging.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.langoor.blueshak.R;
import com.google.gson.Gson;
import com.langoor.app.blueshak.AppController;
import com.langoor.app.blueshak.Messaging.adapter.MessagePagerAdapter;
import com.langoor.app.blueshak.Messaging.data.ChatRoom;
import com.langoor.app.blueshak.Messaging.data.Message;
import com.langoor.app.blueshak.Messaging.data.PushSetTagRequest;
import com.langoor.app.blueshak.Messaging.data.User;
import com.langoor.app.blueshak.Messaging.fragment.NotificationsFragment;
import com.langoor.app.blueshak.Messaging.manager.MessageManager;
import com.langoor.app.blueshak.Messaging.manager.VolleyManager;
import com.langoor.app.blueshak.Messaging.util.CommonUtil;
import com.langoor.app.blueshak.Messaging.util.URLUtil;
import com.langoor.app.blueshak.global.GlobalFunctions;
import com.langoor.app.blueshak.global.GlobalVariables;

/*import com.langoor.app.blueshak.search.SearchActivity;*/
import com.langoor.app.blueshak.report.ReportListing;
import com.langoor.app.blueshak.services.ServerResponseInterface;
import com.langoor.app.blueshak.services.ServicesMethodsManager;
import com.langoor.app.blueshak.services.model.ErrorModel;
import com.langoor.app.blueshak.services.model.StatusModel;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

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