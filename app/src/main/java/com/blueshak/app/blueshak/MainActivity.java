package com.blueshak.app.blueshak;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.blueshak.app.blueshak.Messaging.data.User;
import com.blueshak.app.blueshak.Messaging.manager.MessageManager;
import com.blueshak.app.blueshak.home.ItemListFragmentForList;
import com.blueshak.blueshak.R;
import com.blueshak.app.blueshak.Messaging.activity.InboxFragment;
import com.blueshak.app.blueshak.Messaging.activity.PushActivity;
import com.blueshak.app.blueshak.garage.*;
import com.blueshak.app.blueshak.global.GlobalFunctions;
import com.blueshak.app.blueshak.global.GlobalVariables;
import com.blueshak.app.blueshak.home.GarageSalesListFragment;
import com.blueshak.app.blueshak.home.ItemListFragment;
import com.blueshak.app.blueshak.item.ProductDetail;
import com.blueshak.app.blueshak.login.LoginActivity;
import com.blueshak.app.blueshak.profile.ProfileFragment;
import com.blueshak.app.blueshak.search.SearchActivity;
import com.blueshak.app.blueshak.services.ServerResponseInterface;
import com.blueshak.app.blueshak.services.ServicesMethodsManager;
import com.blueshak.app.blueshak.services.model.FilterModel;
import com.blueshak.app.blueshak.services.model.LocationModel;
import com.blueshak.app.blueshak.services.model.SalesListModel;
import com.blueshak.app.blueshak.services.model.SalesListModelNew;
import com.blueshak.app.blueshak.util.LocationListener;
import com.blueshak.app.blueshak.util.LocationService;
import com.blueshak.app.blueshak.view_sales.MapFragmentSales;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarBadge;
import com.roughike.bottombar.OnMenuTabClickListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends PushActivity implements LocationListener, MessageManager.OnNotificationReceived {
    private static BottomBar mBottomBar;
    private static final String TAG = "MainActivity";
    static Context mainContext;
    public static FragmentManager mainActivityFM;
    static Window mainWindow;
    static String mTitle;
    static int mResourceID;
    static Menu menu;
    static TextView toolbar_title;
    LocationService locServices;
    public static Activity activity = null;
    private SalesListModelNew salesListModel = new SalesListModelNew();
    String current_location;
    private SearchView mSearchView;
    public static final String MAIN_ACTIVITY_LOCATION_MODEL_SERIALIZE = "locationModelSerialize";
    public static final String MAIN_ACTIVITY_FILTER_MODEL_SERIALIZE = "filterModelSerialize";
    public static final String MAIN_ACTIVITY_FILTER_MODEL_SERIALIZE_FOR_SALE = "filterModelSerializeSale";
    private static LocationModel locationModel = null;
    public static FilterModel filterModel = null;
    public static FilterModel filterModelForSale = null;
    private ImageView go_to_search, grid,logo_title;
    TextView searchViewResult;
    boolean is_map = false;
    public static boolean is_reset = false;
    public static boolean is_active = false;
    public static int unread_count = 0;
    public static BottomBarBadge badge;
    private AppController.BaseActivityLifeCycleListener baseActivityLifeCycleListener;

    public static Intent newInstance(Context context, LocationModel locationModel, FilterModel filterModel) {
        Intent mIntent = new Intent(context, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(MAIN_ACTIVITY_LOCATION_MODEL_SERIALIZE, locationModel);
        bundle.putSerializable(MAIN_ACTIVITY_FILTER_MODEL_SERIALIZE, filterModel);
        mIntent.putExtras(bundle);
        return mIntent;
    }

    boolean is_list = false;
    int activeTab = 0;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LocationModel.ctx=this;
       //hashKey();
        try {
            activity = this;
            mainContext = this;
            mainActivityFM = getSupportFragmentManager();
            mainWindow = getWindow();
            ProductDetail.is_map = true;
            MessageManager.getInstance(mainContext).setOnNotificationReceived(this);
            baseActivityLifeCycleListener = new AppController.BaseActivityLifeCycleListener();

            Intent i = getIntent();
            if (i != null) {
                if (i.hasExtra(MAIN_ACTIVITY_LOCATION_MODEL_SERIALIZE)) {
                    locationModel = (LocationModel) getIntent().getExtras().getSerializable(MAIN_ACTIVITY_LOCATION_MODEL_SERIALIZE);
                }
                if (i.hasExtra(MAIN_ACTIVITY_FILTER_MODEL_SERIALIZE)) {
                    filterModel = (FilterModel) getIntent().getExtras().getSerializable(MAIN_ACTIVITY_FILTER_MODEL_SERIALIZE);
                }
            }

            locServices = new LocationService(activity);
            if (!locServices.canGetLocation()) {
                startActivity(new Intent(this, PickLocation.class));
                closeThisActivity();
            } else {
                if (GlobalFunctions.getSharedPreferenceString(activity, GlobalVariables.CURRENT_LOCATION) == null)
                    getAddressFromLatLng();

            }
            locServices.setListener(this);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            View v = inflator.inflate(R.layout.actionbar_custom_view, null);
            go_to_search = (ImageView) v.findViewById(R.id.go_to_filter);
            logo_title = (ImageView) v.findViewById(R.id.logo_title);
            grid = (ImageView) v.findViewById(R.id.grid);
            searchViewResult =(TextView)v.findViewById(R.id.searchViewResult);
            grid.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (activeTab) {
                                case 0:
                                    Log.d("STATE","STATE"+is_list);
                                    if(!is_list)
                                    {
                                        is_list = true;
                                    }
                                    else
                                    {
                                        is_list =false;
                                    }
                                    if (!is_list) {
                                        ItemListFragmentForList itemListFragment = ItemListFragmentForList.newInstance(new SalesListModel(), GlobalVariables.TYPE_MULTIPLE_ITEMS, filterModel, locationModel);
                                        mainActivityFM.beginTransaction().replace(R.id.container, itemListFragment, "").commit();
                                       // is_list = true;
                                        grid.setImageResource(R.drawable.ic_grid);
                                    } else {
                                        ItemListFragment itemListFragment = ItemListFragment.newInstance(new SalesListModel(), GlobalVariables.TYPE_MULTIPLE_ITEMS, filterModel, locationModel);
                                        mainActivityFM.beginTransaction().replace(R.id.container, itemListFragment, "").commit();
                                       // is_list = false;
                                        grid.setImageResource(R.drawable.ic_list);
                                    }
                                    break;
                                case 1:

                                    if(!is_map)
                                    {
                                        is_map = true;
                                    }
                                    else
                                    {
                                        is_map =false;
                                    }

                                    if (is_map) {

                                        Fragment fragment = new MapFragmentSales().newInstance(locationModel, GlobalVariables.TYPE_GARAGE, null, null, false, filterModelForSale);
                                        mainActivityFM.beginTransaction().replace(R.id.container, fragment, "").commit();
                                        grid.setImageResource(R.drawable.ic_grid);
                                       // is_map=false;
                                    } else {
                                        GarageSalesListFragment garageSalesListFragment = GarageSalesListFragment.newInstance(salesListModel, GlobalVariables.TYPE_GARAGE, filterModel, locationModel, false);
                                        mainActivityFM.beginTransaction().replace(R.id.container, garageSalesListFragment, "").commit();
                                        grid.setImageResource(R.drawable.pin_white);
                                      //  is_map=true;
                                    }
                                    break;
                                case 2:
                                    break;
                                case 3:
                                    break;
                                case 4:
                                    break;
                            }
                        }
                    });
            go_to_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(activity, SearchActivity.class));
                }
            });
            v.setLayoutParams(layoutParams);
            toolbar.addView(v);

            mBottomBar = BottomBar.attach(this, savedInstanceState);
            // Disable the left bar on tablets and behave exactly the same on mobile and tablets instead.
            mBottomBar.noTabletGoodness();
            // Show all titles even when there's more than three tabs.
            mBottomBar.useFixedMode();
            mBottomBar.noNavBarGoodness();
       /* mBottomBar.setBackgroundColor(getResources().getColor(R.color.white));*/
            mBottomBar.setItems(R.menu.bottombar_menu);
           /* mBottomBar.makeBadgeForTabAt(3,mainContext.getResources().getColor(R.color.brandColor),2);*/
            mBottomBar.setBackgroundColor(mainContext.getResources().getColor(R.color.bottom_bar_background_color));
            mBottomBar.setActiveTabColor(mainContext.getResources().getColor(R.color.brandColor));
           /* View menuItemRefresh = mBottomBar.findViewById(R.id.list);
            menuItemRefresh.setBackgroundResource(R.drawable.ic_bt_add3x);*/
            // Use custom typeface that's located at the "/src/main/assets" directory. If using with
            // custom text appearance, set the text appearance first.
            mBottomBar.setTypeFace("Raleway-Medium.ttf");
            mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
                @Override
                public void onMenuTabSelected(@IdRes int menuItemId) {
                    switch (menuItemId)
                    {
                        case R.id.home:
                            activeTab=0;
                            topbarToShowOrNot(0);
                            grid.setVisibility(View.VISIBLE);
                            go_to_search.setVisibility(View.VISIBLE);
                            Log.d("STATE","STATE--->"+is_list);
                            if (!is_list)
                            {
                                ItemListFragmentForList itemListFragment = ItemListFragmentForList.newInstance(new SalesListModel(), GlobalVariables.TYPE_MULTIPLE_ITEMS, filterModel, locationModel);
                                mainActivityFM.beginTransaction().replace(R.id.container, itemListFragment, "").commit();
                           // is_list = false;
                                grid.setImageResource(R.drawable.ic_grid);
                            } else {
                                ItemListFragment itemListFragment = ItemListFragment.newInstance(new SalesListModel(), GlobalVariables.TYPE_MULTIPLE_ITEMS, filterModel, locationModel);
                                mainActivityFM.beginTransaction().replace(R.id.container, itemListFragment, "").commit();
                             //  is_list = true;
                                grid.setImageResource(R.drawable.ic_list);
                            }
                            break;
                        case R.id.garage:
                            activeTab=1;
                            topbarToShowOrNot(0);
                            grid.setVisibility(View.VISIBLE);
                            go_to_search.setVisibility(View.VISIBLE);
                            if (is_map) {

                                Fragment fragment = new MapFragmentSales().newInstance(locationModel, GlobalVariables.TYPE_GARAGE, null, null, false, filterModelForSale);
                                mainActivityFM.beginTransaction().replace(R.id.container, fragment, "").commit();
                                grid.setImageResource(R.drawable.ic_grid);
                             //  is_map=true;
                            } else {
                                GarageSalesListFragment garageSalesListFragment = GarageSalesListFragment.newInstance(salesListModel, GlobalVariables.TYPE_GARAGE, filterModelForSale, locationModel, false);
                                mainActivityFM.beginTransaction().replace(R.id.container, garageSalesListFragment, "").commit();
                                grid.setImageResource(R.drawable.pin_white);
                         // is_map=false;
                            }
                            break;
                        case R.id.list:
                            activeTab=2;
                            // for tab hidden just comment below 2 lines
                           //grid.setVisibility(View.GONE);
                          // go_to_search.setVisibility(View.VISIBLE);
                            View menuItemView = findViewById(R.id.list);
                     /*   menuItemView.setBackground(R.drawable.bt_add);*/
                            if (!((Activity) mainContext).isFinishing()) {
                                show_popUp(menuItemView);
                            }
                            break;
                        case R.id.message:
                            activeTab=3;
                            topbarToShowOrNot(1);
                            grid.setVisibility(View.GONE);
                            go_to_search.setVisibility(View.VISIBLE);
                            if (!GlobalFunctions.is_loggedIn(activity))
                                showSettingsAlert();
                            else {
                                unread_count = 0;
                                InboxFragment inboxFragment = new InboxFragment();
                                mainActivityFM.beginTransaction().replace(R.id.container, inboxFragment, "").commit();
                            }
                            break;
                        case R.id.profile:
                            activeTab=4;
                            topbarToShowOrNot(1);
                            grid.setVisibility(View.GONE);
                            go_to_search.setVisibility(View.GONE);
                            if (GlobalFunctions.is_loggedIn(activity)) {
                                ProfileFragment profileFragment = new ProfileFragment();
                                mainActivityFM.beginTransaction().replace(R.id.container, profileFragment, "").commit();
                            } else {
                                showSettingsAlert();
                            }
                            break;
                    }
                }

                @Override
                public void onMenuTabReSelected(@IdRes int menuItemId) {
                    switch (menuItemId) {
                        case R.id.home:
                            activeTab=0;
                            topbarToShowOrNot(0);
                            Log.d("STATE","STATE###"+is_list);
                            grid.setVisibility(View.VISIBLE);
                      /*  ItemListFragment itemListFragment= ItemListFragment.newInstance(new SalesListModel(), GlobalVariables.TYPE_MULTIPLE_ITEMS,filterModel,locationModel);
                        mainActivityFM.beginTransaction().replace(R.id.container, itemListFragment, "").commit();
                */
                            break
                                    ;
                        case R.id.garage:
                            activeTab=1;
                            topbarToShowOrNot(0);
                            grid.setVisibility(View.VISIBLE);
                        /*GarageSalesListFragment garageSalesListFragment= GarageSalesListFragment.newInstance(salesListModel, GlobalVariables.TYPE_GARAGE,filterModel,locationModel);
                        mainActivityFM.beginTransaction().replace(R.id.container, garageSalesListFragment, "").commit();
               */
                            break;
                        case R.id.list:
                            activeTab=2;
                           // grid.setVisibility(View.GONE);
                            View menuItemView = findViewById(R.id.list);
                            if (!((Activity) mainContext).isFinishing()) {
                                show_popUp(menuItemView);
                            }
                            break;
                        case R.id.message:
                            topbarToShowOrNot(1);
                            grid.setVisibility(View.GONE);
                            go_to_search.setVisibility(View.VISIBLE);
                            if (!GlobalFunctions.is_loggedIn(activity))
                                showSettingsAlert();
                            else {
                                unread_count = 0;
                                InboxFragment inboxFragment = new InboxFragment();
                                mainActivityFM.beginTransaction().replace(R.id.container, inboxFragment, "").commit();
                            }
                            break;
                        case R.id.profile:
                            topbarToShowOrNot(1);
                            grid.setVisibility(View.GONE);
                            if (!GlobalFunctions.is_loggedIn(activity))
                                showSettingsAlert();
                            break;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
    }
    void topbarToShowOrNot(int key)
    {

        switch (key)
        {
            case 0:
                searchViewResult.setVisibility(View.VISIBLE);
                logo_title.setVisibility(View.GONE);
                break;

            case 1:
                searchViewResult.setVisibility(View.GONE);
                logo_title.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            is_active = true;
            if (GlobalFunctions.isNetworkAvailable(this)) {
                if (GlobalFunctions.getCategories(this) == null) {
                    GlobalFunctions.saveCategories(activity);
                }
                if (GlobalFunctions.getCurrencies(this) == null) {
                    GlobalFunctions.saveCurrencies(activity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onResume() {
        is_active = true;
        Log.d(TAG, "OnResume");
        if (is_reset) {
            moveToHome(activity);
        }
        super.onResume();
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

    public static void setTitle(String title, int resourceID) {
        mTitle = title;
        mResourceID = resourceID;
      /*  restoreToolbar();*/
    }


    public void setAddress(String item_address) {
        this.current_location = item_address;
        GlobalFunctions.setSharedPreferenceString(activity, GlobalVariables.CURRENT_LOCATION, item_address);

    }

    public String getAddress() {
        return this.current_location;
    }

    private void getAddressFromLatLng() {
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getAddress(activity, locServices.getLatitude(), locServices.getLongitude(), new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                GlobalFunctions.hideProgress();
                LocationModel locationModel = (LocationModel) arg0;
                if (locationModel != null) {
                    setAddress(locationModel.getFormatted_address());
                    Log.d(TAG, "###########Setting the Current Country SHARED_PREFERENCE_LOCATION_COUNTRY############"+locationModel.getFormatted_address());
                    GlobalFunctions.setSharedPreferenceString(mainContext, GlobalVariables.SHARED_PREFERENCE_LOCATION_COUNTRY, locationModel.getCountry_code());
                }

            }

            @Override
            public void OnFailureFromServer(String msg) {
            }

            @Override
            public void OnError(String msg) {
            }
        }, "Delete Sale");


    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            if (GlobalFunctions.getSharedPreferenceString(activity, GlobalVariables.CURRENT_LOCATION) == null)
                getAddressFromLatLng();
        }

    }

    @Override
    public void onProviderEnabled(String provider) {

    }
     /*   @Override
        public void onBackPressed() {
            showExitAlert();
          *//*  super.onBackPressed();*//*
        }
    */

    public static void closeThisActivity() {
        if (activity != null) {
            activity.finish();
        }
    }

    public void showSettingsAlert() {
        Intent creategarrage = new Intent(activity, LoginActivity.class);
        startActivity(creategarrage);
    }

    public void show_popUp(View v) {
        // Open calender Image
        if (activity != null) {
            final PopupMenu popup = new PopupMenu(activity, v);
            popup.getMenuInflater().inflate(R.menu.listing_menu_option, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getTitle().toString().equalsIgnoreCase(activity.getResources().getString(R.string.list_item))) {
                        if (GlobalFunctions.is_loggedIn(activity)) {
                            Intent i = CreateSaleActivity.newInstance(activity, null, null, null, GlobalVariables.TYPE_HOME, GlobalVariables.TYPE_ITEM);
                            startActivity(i);
                          /*  setDeFaultTabAsHome();*/
                        } else
                            showSettingsAlert();
                    } else if (item.getTitle().toString().equalsIgnoreCase(activity.getResources().getString(R.string.create_sale))) {
                        if (GlobalFunctions.is_loggedIn(activity)) {
                            Intent i = CreateSaleActivity.newInstance(activity, null, null, null, GlobalVariables.TYPE_HOME, GlobalVariables.TYPE_GARAGE);
                            startActivity(i);
                          /*  setDeFaultTabAsHome();*/
                        } else
                            showSettingsAlert();
                    }/*else {
                    popup.dismiss();
                }*/
                    return true;
                }
            });
            if (popup != null)
                popup.show();
        }

    }

    public void setDeFaultTabAsHome() {
        if (mBottomBar != null) {
            if (mBottomBar.getCurrentTabPosition() != 0) {
                Log.d(TAG, "getCurrentTabPosition########" + mBottomBar.getCurrentTabPosition());

                mBottomBar.selectTabAtPosition(0, false);
            }


        }
    }

    @Override
    public void onPause() {
        super.onPause();
        /*setDeFaultTabAsHome();*/
    }

    @Override
    public void onStop() {
        super.onStop();
        /*setDeFaultTabAsHome();*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    public static void moveToHome(Activity activity) {
        if (!activity.isFinishing()) {
            is_reset = false;
            FragmentTransaction ft = mainActivityFM.beginTransaction();
            ItemListFragment itemListFragment = ItemListFragment.newInstance(new SalesListModel(), GlobalVariables.TYPE_MULTIPLE_ITEMS, filterModel, locationModel);
            mainActivityFM.beginTransaction().replace(R.id.container, itemListFragment, "").commit();
            ft.commitAllowingStateLoss();
            mBottomBar.setDefaultTabPosition(0);
        }

    }

    @Override
    public void onNotificationReceived(User receivedMessage) {
        /*don't show the badge if the conversation happening*/
        if (!baseActivityLifeCycleListener.isChatActivityVisible())
        {
            if (GlobalFunctions.is_loggedIn(mainContext))
            {
                unread_count++;
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run() {
                        /*Whether we created new badge or its already existed one, update the count now.*/
                        if (badge != null) {
                            badge.setCount(unread_count);
                            badge.show();
                        } else {
                            badge = mBottomBar.makeBadgeForTabAt(3, mainContext.getResources().getColor(R.color.red), unread_count);
                        }
                    }
                });

            }
        }


    }



    private void hashKey()
    {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", "VALUESS"+GlobalFunctions.getSharedPreferenceString(this, GlobalVariables.SHARED_PREFERENCE_COUNTRY));
                Log.d("KeyHash:", "VALUESS"+GlobalFunctions.getSharedPreferenceString(this, GlobalVariables.SHARED_PREFERENCE_LOCATION_COUNTRY));
                Log.d("KeyHash:", "VALUESS"+GlobalFunctions.getSharedPreferenceString(this, GlobalVariables.SHARED_PREFERENCE_PHONE));
                Log.d("KeyHash:", "VALUESS"+GlobalFunctions.getSharedPreferenceString(this, GlobalVariables.SHARED_PREFERENCE_POSTALCODES));
                Log.d("KeyHash:", "VALUESS"+GlobalFunctions.getSharedPreferenceString(this, GlobalVariables.SHARED_PREFERENCE_CURRENCIES));
                Log.d("KeyHash:", "VALUESS"+GlobalFunctions.getSharedPreferenceString(this, GlobalVariables.SHARED_PREFERENCE_CURRENCY));
            }
        }
        catch (PackageManager.NameNotFoundException e) {

        }
        catch (NoSuchAlgorithmException e) {

        }
        //getCurrency();
    }

    /*private  void getCurrency()
    {
        Locale locale= getResources().getConfiguration().locale;

        Currency currency=Currency.getInstance(locale);
        String symbol = currency.getSymbol();
        Log.d("SYMOBOLS ","CURRENCYCURENY"+symbol);
    }*/

   /* private  void getISDD()
    {
        PhoneNumberUtils phoneUtil = PhoneNumberUtils.getInstance();
        try {
            // phone must begin with '+'
            PhoneNumber numberProto = phoneUtil.parse(phone, "");
            int countryCode = numberProto.getCountryCode();
        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e.toString());
        }
    }*/
}
