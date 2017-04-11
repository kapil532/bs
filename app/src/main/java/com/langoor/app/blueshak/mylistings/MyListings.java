package com.langoor.app.blueshak.mylistings;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import com.langoor.blueshak.R;
import com.langoor.app.blueshak.Messaging.adapter.MessagePagerAdapter;
import com.langoor.app.blueshak.global.GlobalFunctions;
import com.langoor.app.blueshak.global.GlobalVariables;
import com.langoor.app.blueshak.home.GarageSalesListFragment;
import com.langoor.app.blueshak.home.ItemListFragment;
import com.langoor.app.blueshak.root.RootActivity;
import com.langoor.app.blueshak.services.model.SalesListModelNew;

import java.util.ArrayList;

public class MyListings extends RootActivity {
    public static final String TAG = "MyListings";
    private ViewPager mViewPager;
    private TabHost mTabHost;
    private String[] tabs = {"Items", "Garage Sales" };
    private Toolbar toolbar;
    private ActionBar actionBar;
    private static Context context;
    private static Activity activity;
    private Window window;
    private static MyListingPagerAdapter adapter;
    private TabLayout tabLayout;
    static GlobalFunctions globalFunctions = new GlobalFunctions();
    static GlobalVariables globalVariables = new GlobalVariables();
    private TextView shop_tile,close_button,tab1,tab2;
    private Button save_profile;
    private ImageView go_back;
    boolean is_tab1_selected=true;
    public static FragmentManager mainActivityFM;
    ItemMyListFragment itemMyListFragment;
    GarageSalesMyListFragment garageSalesListFragment;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
      */  setContentView(R.layout.activity_my_listings);
        activity=this;
        context=this;
        mainActivityFM = getSupportFragmentManager();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        LayoutInflater inflator = LayoutInflater.from(this);
        View v = inflator.inflate(R.layout.action_bar_titlel, null);
        shop_tile=(TextView)v.findViewById(R.id.title);
        shop_tile.setText("My Items");
        toolbar.addView(v);
        close_button=(TextView)v.findViewById(R.id.cancel);
        close_button.setVisibility(View.GONE);
        go_back=(ImageView)findViewById(R.id.go_back);
        go_back.setVisibility(View.VISIBLE);
        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               closeThisActivity();
            }
        });

        itemMyListFragment=new  ItemMyListFragment();
        garageSalesListFragment=GarageSalesMyListFragment.newInstance(new SalesListModelNew(),GlobalVariables.TYPE_GARAGE,null,null,true);
        mainActivityFM.beginTransaction().replace(R.id.container, itemMyListFragment, "").commit();
        tab1=(TextView)findViewById(R.id.tab1);
        tab2=(TextView)findViewById(R.id.tab2);
        tab1.setText("Items");
        tab2.setText("Garage Sales");
        tab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!is_tab1_selected){
                    tab1.setTextColor(context.getResources().getColor(R.color.white));
                    tab1.setBackgroundColor(context.getResources().getColor(R.color.brandColor));
                    tab2.setTextColor(context.getResources().getColor(R.color.brandColor));
                    tab2.setBackgroundColor(context.getResources().getColor(R.color.brand_secondary_color));
                    mainActivityFM.beginTransaction().replace(R.id.container, itemMyListFragment, "").commit();
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
                    mainActivityFM.beginTransaction().replace(R.id.container, garageSalesListFragment, "").commit();
                    is_tab1_selected=false;
                }

            }
        });
        /* ArrayList<String> tabs = new ArrayList<>();
        tabs.add("Items");
        tabs.add("Garage");
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setBackgroundColor(activity.getResources().getColor(R.color.brand_secondary_color));
        tabLayout.setSelectedTabIndicatorColor(activity.getResources().getColor(R.color.brandColor));
        tabLayout.setTabTextColors(activity.getResources().getColor(R.color.white),activity.getResources().getColor(R.color.brandColor));
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new MyListingPagerAdapter(getSupportFragmentManager(), tabs);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);*/
    }
    public static void closeThisActivity(){
        if(activity!=null)
            activity.finish();
    }

}
