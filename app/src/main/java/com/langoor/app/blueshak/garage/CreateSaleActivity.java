package com.langoor.app.blueshak.garage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.*;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.langoor.blueshak.R;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.langoor.app.blueshak.AppController;
import com.langoor.app.blueshak.MainActivity;
import com.langoor.app.blueshak.global.GlobalFunctions;
import com.langoor.app.blueshak.global.GlobalVariables;
import com.langoor.app.blueshak.home.ItemListFragment;
import com.langoor.app.blueshak.root.RootActivity;
import com.langoor.app.blueshak.services.model.CreateProductModel;
import com.langoor.app.blueshak.services.model.CreateSalesModel;
import com.langoor.app.blueshak.services.model.LocationModel;
import com.langoor.app.blueshak.view.AlertDialog;
import com.roughike.bottombar.BottomBar;

import java.util.ArrayList;

public class CreateSaleActivity extends RootActivity {

    private static final String TAG = "CreateSaleActivity";
    public static final String CREATE_GARRAGE_ACTIVITY_GARAGE_BUNDLE_KEY = "CreateGarrageActivityBundleKey";
    public static final String CREATE_GARRAGE_ACTIVITY_ITEM_BUNDLE_KEY = "CreateItemActivityBundleKey";
    public static final String CREATE_GARRAGE_ACTIVITY_LOCATION_BUNDLE_KEY = "CreateItemActivityLocationBundleKey";
    public static final String CREATE_GARRAGE_ACTIVITY_TYPE_BUNDLE_KEY = "CreateGarrageActivityTypeBundleKey";
    private TabLayout tabLayout;
    static Context mainContext;
    public static FragmentManager mainActivityFM;
    static Window mainWindow;
    public static Activity activity = null;
    public static final String FROM_KEY = "from_key";
    private CreateSalesModel createSalesModel = null;
    private int from_key;
    private LocationModel locationModel = new LocationModel();
    private CreateProductModel createProductModel = null;
    private GlobalVariables globalVariables=new GlobalVariables();
    private  GlobalFunctions globalFunctions=new GlobalFunctions();
    private String type;
    private  TextView activity_title,cancel;
    private  ImageView go_back;
    public static Intent newInstance(Context context, CreateSalesModel sales, CreateProductModel createProductModel, LocationModel locationModel, int from,String type) {
        Intent mIntent = new Intent(context, CreateSaleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(CREATE_GARRAGE_ACTIVITY_GARAGE_BUNDLE_KEY, sales);
        bundle.putSerializable(CREATE_GARRAGE_ACTIVITY_ITEM_BUNDLE_KEY, createProductModel);
        bundle.putSerializable(CREATE_GARRAGE_ACTIVITY_LOCATION_BUNDLE_KEY, locationModel);
        bundle.putSerializable(CREATE_GARRAGE_ACTIVITY_TYPE_BUNDLE_KEY, type);
        bundle.putInt(FROM_KEY, from);
        mIntent.putExtras(bundle);
        return mIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      /*  requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    */    setContentView(R.layout.activity_list);
        try{
            activity = this;
            mainContext = this;
            mainActivityFM = getSupportFragmentManager();
            mainWindow = getWindow();
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
          /*  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);*/
            LayoutInflater inflator = LayoutInflater.from(this);
            View v = inflator.inflate(R.layout.action_bar_titlel, null);
            activity_title=(TextView)v.findViewById(R.id.title);

            toolbar.addView(v);
            cancel=(TextView)v.findViewById(R.id.cancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!((Activity)mainContext ).isFinishing()){
                        Log.d(TAG,"isFinishing###################");
                        showExitAlert();
                    }else
                       closeThisActivity();

                }
            });
            if (getIntent().hasExtra(CREATE_GARRAGE_ACTIVITY_GARAGE_BUNDLE_KEY)) {
                createSalesModel = (CreateSalesModel) getIntent().getExtras().getSerializable(CREATE_GARRAGE_ACTIVITY_GARAGE_BUNDLE_KEY);
            }
            if (getIntent().hasExtra(CREATE_GARRAGE_ACTIVITY_ITEM_BUNDLE_KEY)) {
                createProductModel = (CreateProductModel) getIntent().getExtras().getSerializable(CREATE_GARRAGE_ACTIVITY_ITEM_BUNDLE_KEY);
            }
            if (getIntent().hasExtra(CREATE_GARRAGE_ACTIVITY_ITEM_BUNDLE_KEY)) {
                locationModel = (LocationModel) getIntent().getExtras().getSerializable(CREATE_GARRAGE_ACTIVITY_LOCATION_BUNDLE_KEY);
            }
            if (getIntent().hasExtra(FROM_KEY)) {
                from_key = getIntent().getIntExtra(FROM_KEY, 0);
            }
            if (getIntent().hasExtra(CREATE_GARRAGE_ACTIVITY_TYPE_BUNDLE_KEY)) {
                type = getIntent().getStringExtra(CREATE_GARRAGE_ACTIVITY_TYPE_BUNDLE_KEY);
            }

            if(type.equalsIgnoreCase(GlobalVariables.TYPE_ITEM)){
                CreateItemSaleFragment itemListFragment = CreateItemSaleFragment.newInstance(activity, createProductModel,locationModel, GlobalVariables.TYPE_GARAGE, from_key);
                mainActivityFM.beginTransaction().replace(R.id.container, itemListFragment, "").commit();
            }else {
                 CreateGarageSaleFragment garageSaleFragment = CreateGarageSaleFragment.newInstance(activity, createSalesModel, GlobalVariables.TYPE_GARAGE, from_key);
                mainActivityFM.beginTransaction().replace(R.id.container, garageSaleFragment, "").commit();

            }
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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if(!((Activity)mainContext ).isFinishing()){
            Log.d(TAG,"isFinishing###################");
            showExitAlert();
        }else{
            closeThisActivity();
        }

    }

    public static void replaceFragment(Fragment fragment, @NonNull String tag) {
        if (mainActivityFM != null) {
            mainActivityFM.beginTransaction().replace(R.id.container, fragment, tag).commit();
        }
    }

    public static void addFragment(Fragment fragment, @NonNull String tag) {
        if (mainActivityFM != null) {
            mainActivityFM.beginTransaction().add(R.id.container, fragment, tag).addToBackStack(null).commit();
        }
    }

    public void showExitAlert() {
        final AlertDialog alertDialog = new AlertDialog(activity);
        alertDialog.setTitle("Are you sure?");
        alertDialog.setMessage("Are you sure to stop listing this item? The data you've created will be lost.");
        alertDialog.setPositiveButton("Yes", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (from_key == GlobalVariables.TYPE_AC_SIGN_UP)
                    startActivity(new Intent(CreateSaleActivity.this, MainActivity.class));
                else
                  closeThisActivity();
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("No", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();
    }
    public static void closeThisActivity(){
        Log.d(TAG,"closeThisActivity###################");
        if(activity!=null){
            Log.d(TAG,"activity!=null###################");activity.finish();
        }
    }
    @Override
    public void onStop() {
        super.onStop();
    }

    public  void setActionBarTitle(String title) {
        activity_title.setText(title);
    }
}
