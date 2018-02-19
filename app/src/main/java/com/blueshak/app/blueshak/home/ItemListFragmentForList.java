package com.blueshak.app.blueshak.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Dimension;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blueshak.app.blueshak.MainActivity;
import com.blueshak.app.blueshak.Messaging.helper.Constants;
import com.blueshak.app.blueshak.PickLocation;
import com.blueshak.app.blueshak.base.PresenterCallBack;
import com.blueshak.app.blueshak.filter.FilterActivity;
import com.blueshak.app.blueshak.global.GlobalFunctions;
import com.blueshak.app.blueshak.global.GlobalVariables;
import com.blueshak.app.blueshak.home.model.FeatureItemData;
import com.blueshak.app.blueshak.home.model.FeatureItemsModel;
import com.blueshak.app.blueshak.home.presenter.ItemListPresenter;
import com.blueshak.app.blueshak.search.SearchActivity;
import com.blueshak.app.blueshak.services.ServerResponseInterface;
import com.blueshak.app.blueshak.services.ServicesMethodsManager;
import com.blueshak.app.blueshak.services.model.FilterModel;
import com.blueshak.app.blueshak.services.model.HomeListModel;
import com.blueshak.app.blueshak.services.model.LocationModel;
import com.blueshak.app.blueshak.services.model.ProductModel;
import com.blueshak.app.blueshak.services.model.SalesListModel;
import com.blueshak.app.blueshak.services.model.SalesModel;
import com.blueshak.app.blueshak.text.MyTextViewMediumBold;
import com.blueshak.app.blueshak.util.BlueShakLog;
import com.blueshak.app.blueshak.util.LocationListener;
import com.blueshak.app.blueshak.util.LocationService;
import com.blueshak.blueshak.R;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;


public class ItemListFragmentForList extends Fragment implements LocationListener/*,onFilterChange*/,
        SwipeRefreshLayout.OnRefreshListener,FeatureItemLoadMore {

    public static boolean listOrGrid = false;
    public static final String TAG = "ItemListFragment";
    public static final String SALES_LIST_GARAGGE_SALE_MODEL_SERIALIZE = "SalesListGarageSaleModelSerialize";
    public static final String SALE_FILTER = "filtermodel";
    public static final String LOCATION_MODEL = "locationmodel";
    public static final String SALES_LIST_GARAGGE_SALE_MODEL_TYPE = "SalesListGarageSaleModelType";
    private Context context;
    private TextView no_sales, results_all;
    private static Activity activity;
    private LayoutInflater inflater = null;
    private SalesListModel salesListModel = new SalesListModel();
    private HomeListModel itemListModel = new HomeListModel();
    private ItemListAdapterForList adapter;
    private ArrayList<SalesModel> list = new ArrayList<SalesModel>();
    private ArrayList<ProductModel> product_list = new ArrayList<ProductModel>();
    private GlobalVariables globalVariables = new GlobalVariables();
    private LocationService locServices;
    private boolean location_available;
    private boolean when_open_the_app = false;
    private RecyclerView recyclerView;
    private static FilterModel model = null;
    private LocationModel locationModel = null;
    private TextView location, filter_tag;
    private ImageView location_arrow, filter;
    public String item_address;
    private String no_items_found;
    private int last_page = 0;
    private String Sydney_latitude = "-33.8688196";
    private String Sydney_longitude = "151.2092955";
    private EndlessRecyclerOnScrollListenerForList endlessRecyclerOnScrollListener;
    int minPriceValue = 0, maxPriceValue = GlobalVariables.PRICE_MAX_VALUE, minDistanceValue = 0, maxDistanceValue = GlobalVariables.DISTANCE_MAX_VALUE;
    private ProgressBar progress_bar;
    private TextView searchViewResult;
    private ImageView go_to_filter;
    private ArrayList<FeatureItemData> featureItemsList = new ArrayList<FeatureItemData>();
    private LinearLayoutManager gridLayoutManager;
    public static int iTake = 3;
    //private FlexboxLayout flexboxLayout;
    private LinearLayout layoutFilterHorizontal;

    public static ItemListFragmentForList newInstance(SalesListModel salesListModel, String type, FilterModel filterModel, LocationModel locationModel) {
        ItemListFragmentForList saleFragment = new ItemListFragmentForList();
        Bundle bundle = new Bundle();
        bundle.putSerializable(SALES_LIST_GARAGGE_SALE_MODEL_SERIALIZE, salesListModel);
        bundle.putSerializable(SALE_FILTER, filterModel);
        bundle.putSerializable(LOCATION_MODEL, locationModel);
        bundle.putString(SALES_LIST_GARAGGE_SALE_MODEL_TYPE, type);
        saleFragment.setArguments(bundle);
        return saleFragment;
    }

    private Toolbar toolbar;
    /* @InjectView(R.id.swipe_container)*/
    private SwipeRefreshLayout swipeRefreshLayout;
    /* LinearLayout header_content;*/
    private Handler handler = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.sales_list_item_fragment, container, false);
        try {
            progress_bar = (ProgressBar) view.findViewById(R.id.progress_bar);
            context = getActivity();
            activity = getActivity();
            this.inflater = inflater;
            locServices = new LocationService(activity);
            locServices.setListener(this);
            swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
            swipeRefreshLayout.setOnRefreshListener(this);

            //results_all = (TextView) view.findViewById(R.id.results_all);
            layoutFilterHorizontal = (LinearLayout) view.findViewById(R.id.layout_filter_horizontal);

            Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
            go_to_filter = (ImageView) toolbar.findViewById(R.id.go_to_filter);

            go_to_filter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addFilter();
                }
            });
            searchViewResult = (TextView) toolbar.findViewById(R.id.searchViewResult);

            no_items_found = context.getResources().getString(R.string.no_items_found);
            swipeRefreshLayout.setColorSchemeColors(context.getResources().getColor(R.color.brandColor),
                    context.getResources().getColor(R.color.tab_selected),
                    context.getResources().getColor(R.color.darkorange),
                    context.getResources().getColor(R.color.brandColor));
            salesListModel = (SalesListModel) getArguments().getSerializable(SALES_LIST_GARAGGE_SALE_MODEL_SERIALIZE);
            locationModel = (LocationModel) getArguments().getSerializable(LOCATION_MODEL);
            model = (FilterModel) getArguments().getSerializable(SALE_FILTER);
            salesListModel = (SalesListModel) getArguments().getSerializable(SALES_LIST_GARAGGE_SALE_MODEL_SERIALIZE);

            if (model == null) {
                model = new FilterModel();
                if (locationModel != null) {

                    if (locationModel.getCity() != null && locationModel.getState() != null)
                        item_address = locationModel.getCity() + " " + locationModel.getState();
                    else
                        item_address = "Sydney" + ", Australia";

                    if (!locationModel.getLatitude().equalsIgnoreCase("0.0") && !locationModel.getLongitude().equalsIgnoreCase("0.0")) {
                        model.setLatitude(locationModel.getLatitude() + "");
                        model.setLongitude(locationModel.getLongitude() + "");
                    } else {
                        Log.d(TAG, "#######Setting the Sydney Lat and Lng###################");
                        model.setLatitude(GlobalVariables.Sydney_latitude);
                        model.setLongitude(GlobalVariables.Sydney_longitude);
                    }

                    model.setRange(maxDistanceValue);
                    model.setAvailable(true);
                    model.setPickup(true);
                    model.setShipable(true);
                    model.setType(GlobalVariables.TYPE_SHOP);
                    model.setPriceRange(minPriceValue + "," + maxPriceValue);
                    when_open_the_app = true;
                } else {
                    if (!locServices.canGetLocation()) {
                        location_available = false;
                       /* locServices.showSettingsAlert();*/
                        model.setLatitude(Sydney_latitude);
                        model.setLongitude(Sydney_longitude);
                        model.setRange(maxDistanceValue);
                        model.setAvailable(true);
                        model.setPickup(true);
                        model.setShipable(true);
                        model.setType(GlobalVariables.TYPE_SHOP);
                        model.setPriceRange(minPriceValue + "," + maxPriceValue);
             /*   getItemLists(context,model);*/
                    } else {
                        location_available = true;
                        when_open_the_app = true;
                        Location loc = locServices.getLocation();
                        if (loc != null) {
                            model.setLatitude(loc.getLatitude() + "");
                            model.setLongitude(loc.getLongitude() + "");
                            model.setRange(maxDistanceValue);
                            model.setAvailable(true);
                            model.setPickup(true);
                            model.setShipable(true);
                            model.setType(GlobalVariables.TYPE_SHOP);
                            model.setPriceRange(minPriceValue + "," + maxPriceValue);
                    /*getItemLists(context,model);*/
                        }
                    }
                }
                if(model.getArrayListFilterResult()!=null){
                    setFilterView(model.getArrayListFilterResult());
                    BlueShakLog.logDebug("Result from ","getArrayListFilterResult  -> "+model.getArrayListFilterResult());
                    BlueShakLog.logDebug("Result from ","getArrayListFilterResult  size -> "+model.getArrayListFilterResult().size());
                }else {
                    setFilterView(defaultFilteredList());
                }
                /*if (!TextUtils.isEmpty(model.getResults_text())) {
                    Log.d("VALUESS", "VALUESSS-->3  " + model.getResults_text());
                    results_all.setText("Results from " + model.getResults_text());
                } else {
                    Log.d("VALUESS", "VALUESSS-->4  " + model.getResults_text());
                    results_all.setText(Constants.getTextFromId(context, R.string.item_list_fragment_for_list_result_from_nearest_new));
                }*/
            } else {
                item_address = model.getLocation();

                if(model.getArrayListFilterResult()!=null){
                    setFilterView(model.getArrayListFilterResult());
                    BlueShakLog.logDebug("Result from ","getArrayListFilterResult 1 -> "+model.getArrayListFilterResult());
                    BlueShakLog.logDebug("Result from ","getArrayListFilterResult 1 size -> "+model.getArrayListFilterResult().size());
                }else {
                    setFilterView(defaultFilteredList());
                }

               /* if (!TextUtils.isEmpty(model.getResults_text())) {
                    Log.d("VALUESS", "VALUESSS-->1  " + model.getResults_text());
                    results_all.setText("Results from " + model.getResults_text());
                } else {
                    Log.d("VALUESS", "VALUESSS-->2  " + model.getResults_text());
                    results_all.setText(Constants.getTextFromId(context, R.string.item_list_fragment_for_list_result_from_nearest_new));
                }*/
//                results_all.setText(getResources().getString(R.string.item_list_fragment_for_list_result_from_nearest_new));
            }
            location = (TextView) view.findViewById(R.id.location);
            filter_tag = (TextView) view.findViewById(R.id.filter_tag);
            location_arrow = (ImageView) view.findViewById(R.id.location_arrow);
            filter = (ImageView) view.findViewById(R.id.filter);
            toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
            no_sales = (TextView) view.findViewById(R.id.no_sales);


            recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
            setAdapterItemList(product_list,featureItemsList);

           /* recyclerView.setOnScrollListener(new MyScrollListener(activity) {
                @Override
                public void onMoved(int distance) {
                    toolbar.setTranslationY(-distance);
                }
            });*/
            endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListenerForList(gridLayoutManager) {
                @Override
                public void onLoadMore(int current_page) {
                    if (!(current_page > last_page)) {
                      /*  adapter.showLoading(true);*/
                        model.setPage(current_page);
                        getItemLists(context, model);
                    } else {
                        /*adapter.showLoading(false);*/
                    }
                }
            };
            recyclerView.addOnScrollListener(endlessRecyclerOnScrollListener);

            location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = PickLocation.newInstance(context, GlobalVariables.TYPE_HOME, true, null);
                    context.startActivity(intent);
                }
            });
            filter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*context.startActivity(new Intent(context, FilterActivity.class));*/
                    addFilter();
                }
            });
            filter_tag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addFilter();
                }
            });

            location_arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = PickLocation.newInstance(context, GlobalVariables.TYPE_HOME, true, null);
                    context.startActivity(intent);
                }
            });
            if (!GlobalFunctions.isNetworkAvailable(context)) {
                Toast.makeText(context, getResources().getString(R.string.please_check_ur_internet), Toast.LENGTH_LONG).show();
            } else {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setThisPage();
                    }
                });

            }
        } catch (NullPointerException e) {
            Log.d(TAG, "NullPointerException");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            Log.d(TAG, "NumberFormatException");
            e.printStackTrace();
        } catch (Exception e) {
            Log.d(TAG, "Exception");
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
     /*   endlessRecyclerOnScrollListener.reset(0, true);
        last_page=0;*/
        MainActivity.setTitle(getString(R.string.item), 0);
        super.onResume();
    }

    private void setThisPage() {
      /*  if(TextUtils.isEmpty(item_address)&&model.getLongitude()!=null&&model.getLongitude()!=null)
            getAddressFromLatLng(model.getLatitude(),model.getLongitude());
      *//*  else
            setLocation(item_address);*//*
*/
        if (product_list != null) {
            product_list.clear();

        }
        if (when_open_the_app) {
            Log.d(TAG, "######when_open_the_app###########");
            if (locServices != null){
                getFeatureList(context, model,String.valueOf(iTake));
                setCountry(context, locServices.getLatitude(), locServices.getLongitude());
            }

        } else {
            Log.d(TAG, "######from the Filter or Current Location###########");
            if (model != null) {
                String country = GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_LOCATION_COUNTRY);
                  /* if(model.getCurrent_country_code().length()>0)
                   {
                   }
                   else
                   {
                       if (country != null) {
                           model.setCurrent_country_code(country);
                           model.setIs_current_country(true);
                       }

                   }*/

                String filter_string = GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.FILTER_MODEL);
                Log.d(TAG, "######filter_string######" + filter_string);
                if (filter_string != null) {
                    Log.d(TAG, "########Filter is NOT null############");
                    detail.toObject(filter_string);
                    if (detail.is_current_country()) {
                        String countray = GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_LOCATION_COUNTRY);
                        model.setCurrent_country_code(countray);
                        model.setIs_current_country(true);
                    } else {
                        model.setIs_current_country(false);
                    }
                } else {
                    String countray = GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_LOCATION_COUNTRY);
                    model.setCurrent_country_code(countray);
                    model.setIs_current_country(true);
                }
                if (detail != null) {
                    Log.d("http", " http://dev.blueshak.com/api" + detail.is_current_country());
                }
                model.setPage(1);
               // getFeatureList(context, model);
                getItemLists(context, model);
            }
        }

    }

    private FilterModel detail = new FilterModel();

    private void setLocation(String item_address) {
        Log.d(TAG, "#########item_address##############" + item_address);
        if (TextUtils.isEmpty(item_address))
            item_address = "Pick Location";
        String current_location = "";
        if (!item_address.equalsIgnoreCase("Pick Location")) {
            current_location = "Listing near " + item_address;
            SpannableString ss = new SpannableString(current_location);
            ss.setSpan(new MyClickableSpan(), 12, current_location.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            location.setText(ss);
            location.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            location.setText(item_address);
            location.setTextColor(context.getResources().getColor(R.color.tab_selected));
            location.setGravity(Gravity.CENTER);

        }
    }

    private void setItemListValues(HomeListModel model) {
        if (model != null) {
            String str = "";
            itemListModel = model;
            last_page = itemListModel.getLast_page();
            List<ProductModel> productModels = itemListModel.getItem_list();
            if (productModels != null) {
                if (productModels.size() > 0 && recyclerView != null && list != null && adapter != null) {
                   /* ProductModel productModel = new ProductModel();
                    productModel.setHorizontalList(productModels);
                    product_list.add(0, productModel);*/
                    product_list.addAll(productModels);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            synchronized (adapter) {
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });

                }
            }
        }

    }

    private void getItemLists(final Context context, FilterModel filterModel) {
    /*    showProgress();*/
        showProgressBar();
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getHomeList(context, filterModel, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                swipeRefreshLayout.setRefreshing(false);
                itemListModel = (HomeListModel) arg0;
                String str = itemListModel.toString();
                BlueShakLog.logDebug(TAG, "getItemLists onSuccess -> " + str);
                setItemListValues(itemListModel);
                no_sales.setVisibility(View.GONE);
                hideProgressBar();
            }

            @Override
            public void OnFailureFromServer(String msg) {
                swipeRefreshLayout.setRefreshing(false);
                no_sales.setVisibility(View.VISIBLE);
                no_sales.setText(no_items_found);
                hideProgressBar();
                BlueShakLog.logError(TAG, "getItemLists OnFailureFromServer -> " + msg);
              /*  Toast.makeText(context,no_items_found,Toast.LENGTH_LONG).show();*/

            }

            @Override
            public void OnError(String msg) {
                swipeRefreshLayout.setRefreshing(false);
                BlueShakLog.logError(TAG, "getItemLists OnError -> " + msg);
                no_sales.setVisibility(View.VISIBLE);
                no_sales.setText(no_items_found);
                hideProgressBar();
                /*Toast.makeText(context,no_items_found,Toast.LENGTH_LONG).show();*/
            }
        }, "List Sales");

    }

    @Override
    public void onRefresh() {
        endlessRecyclerOnScrollListener.reset(0, true);
        swipeRefreshLayout.setRefreshing(false);
        last_page = 0;
        setThisPage();
    }

    public void reset() {
        endlessRecyclerOnScrollListener.reset(0, true);
        last_page = 0;
    }

    public void showProgress() {
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                    }
                                }
        );
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    public abstract class MyScrollListener extends RecyclerView.OnScrollListener {
        private int toolbarOffset = 0;
        private int toolbarHeight;

        public MyScrollListener(Context context) {
            int[] actionBarAttr = new int[]{android.R.attr.actionBarSize};
            TypedArray a = context.obtainStyledAttributes(actionBarAttr);
            toolbarHeight = (int) a.getDimension(0, 0) + 10;
            a.recycle();
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            clipToolbarOffset();
            onMoved(toolbarOffset);

            if ((toolbarOffset < toolbarHeight && dy > 0) || (toolbarOffset > 0 && dy < 0)) {
                toolbarOffset += dy;
            }
        }

        private void clipToolbarOffset() {
            if (toolbarOffset > toolbarHeight) {
                toolbarOffset = toolbarHeight;
            } else if (toolbarOffset < 0) {
                toolbarOffset = 0;
            }
        }

        public abstract void onMoved(int distance);
    }

    private static class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private final int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.bottom = 2 * space;
            int pos = parent.getChildAdapterPosition(view);
            outRect.left = space;
            outRect.right = space;
            if (pos < 2)
                outRect.top = 2 * space;
        }
    }

    class MyClickableSpan extends ClickableSpan { //clickable span
        public void onClick(View textView) {

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(context.getResources().getColor(R.color.tab_selected));//set text color
          /*  ds.setUnderlineText(true);*/
            //ds.setStyle(Typeface.BOLD);
          /*  ds.setTypeface(Typeface.DEFAULT_BOLD);*/
        }
    }

    private void getAddressFromLatLng(final String lat, final String lng) {
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getAddress(activity, Double.parseDouble(lat), Double.parseDouble(lng), new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                GlobalFunctions.hideProgress();
                LocationModel locationModel_ = (LocationModel) arg0;
                locationModel = locationModel_;
                locationModel.setLatitude(lat);
                locationModel.setLongitude(lng);
                if (locationModel_ != null)
                    /*setLocation(locationModel_.getCity()+" "+locationModel_.getState());*/
                    GlobalFunctions.setSharedPreferenceString(context, GlobalVariables.CURRENT_LOCATION, locationModel_.getCity());
            }

            @Override
            public void OnFailureFromServer(String msg) {
            }

            @Override
            public void OnError(String msg) {
            }
        }, "Get AddressFromLatLng");


    }

    public void addFilter() {
        Intent filter_intent = FilterActivity.newInstance(context, locationModel, GlobalVariables.TYPE_ITEMS);
        startActivityForResult(filter_intent, globalVariables.REQUEST_CODE_FILTER_MODEL_LOCATION);
        activity.overridePendingTransition(R.animator.fadeout, R.animator.fadein);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "on activity result");
        if (resultCode == activity.RESULT_OK) {
            Log.i(TAG, "result ok ");
            Log.i(TAG, "request code " + globalVariables.REQUEST_CODE_FILTER_MODEL_LOCATION);
            if (data.hasExtra(MainActivity.MAIN_ACTIVITY_FILTER_MODEL_SERIALIZE)) {
                model = (FilterModel) data.getExtras().getSerializable(MainActivity.MAIN_ACTIVITY_FILTER_MODEL_SERIALIZE);
                if (model != null) {
                    if (model.getCurrent_country_code() == null)
                        model.setIs_current_country(false);
                    MainActivity.filterModel = model;
                    item_address = model.getLocation();
                    String category_names = model.getCategory_names();

                    if(model.getArrayListFilterResult()!=null){
                        setFilterView(model.getArrayListFilterResult());
                        BlueShakLog.logDebug("Result from ","getArrayListFilterResult onActivityResult -> "+model.getArrayListFilterResult());
                        BlueShakLog.logDebug("Result from ","getArrayListFilterResult onActivityResult size -> "+model.getArrayListFilterResult().size());
                    }else {
                        setFilterView(defaultFilteredList());
                    }

                    /*if (!TextUtils.isEmpty(model.getResults_text())) {
                        results_all.setText(Constants.getTextFromId(context, R.string.item_list_fragment_for_list_result_in) + " " + model.getResults_text());
                    } else {
                        results_all.setText(Constants.getTextFromId(context, R.string.item_list_fragment_for_list_result_from_nearest_new));
                    }*/

                    /*if(!TextUtils.isEmpty(category_names)&& category_names!=null)
                        results_all.setText("Results in "+"'"+model.getCategory_names()+"'");
                    else
                        results_all.setText("Results in "+"'"+"All"+"'");*/
                    Log.i(TAG, "name pm" + model.getFormatted_address());
                    /*reset the Listener for once the filter is set*/
                    reset();
                    setThisPage();
                }
            }
        }
    }

    public void showProgressBar() {
        if (progress_bar != null)
            progress_bar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        if (progress_bar != null)
            progress_bar.setVisibility(View.GONE);
    }

    private void setCountry(final Context context, final Double lattitude, final Double longitude) {
        showProgressBar();
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getAddress(activity, lattitude, longitude, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                hideProgressBar();
                GlobalFunctions.closeKeyboard(activity);
                locationModel = (LocationModel) arg0;
                try {
                    locationModel.setLatitude(lattitude.toString());
                    locationModel.setLongitude(longitude.toString());
                    Log.d(TAG, "###########Setting the Current Country SHARED_PREFERENCE_LOCATION_COUNTRY############");
                    if (when_open_the_app) {
                        if(locationModel!=null && locationModel.getCountry_code()!=null && !locationModel.getCountry_code().isEmpty()){
                            GlobalFunctions.setSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_LOCATION_COUNTRY, locationModel.getCountry_code());
                        }
                        model.setCurrent_country_code(locationModel.getCountry_code());
                        model.setIs_current_country(true);
                        getItemLists(context, model);
                        when_open_the_app = false;
                    } else {
                        if (locationModel != null) {
                            if(locationModel.getCountry_code()!=null && !locationModel.getCountry_code().isEmpty()){
                                GlobalFunctions.setSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_LOCATION_COUNTRY, locationModel.getCountry_code());
                            }
                        }
                    }
                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                    Log.d(TAG, "Exception: " + ex.getMessage());
                }
            }

            @Override
            public void OnFailureFromServer(String msg) {
                hideProgressBar();
            }

            @Override
            public void OnError(String msg) {
                hideProgressBar();
            }
        }, "Fetching Current Location");
    }

    private void getFeatureList(Context context,FilterModel filterModel,String take){
        ItemListPresenter itemListPresenter = new ItemListPresenter();
        itemListPresenter.getFeatureItemLists(context, filterModel, new PresenterCallBack<FeatureItemsModel>() {
            @Override
            public void onSuccess(FeatureItemsModel object) {
                featureItemsList.clear();
                FeatureItemData[] featureItemList =  object.getData();
                for (FeatureItemData featureItem : featureItemList) {
                    featureItemsList.add(featureItem);
                }
                setAdapterItemList(product_list,featureItemsList);
                BlueShakLog.logDebug(TAG,"getFeatureList Size --> "+featureItemsList.size());
            }

            @Override
            public void onFailure() {

            }
        },take);
    }

    private void setAdapterItemList(ArrayList<ProductModel> product_list,ArrayList<FeatureItemData> featureItemsList){
        adapter = new ItemListAdapterForList(context, product_list,featureItemsList,this);
        gridLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(gridLayoutManager);
        //recyclerView.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.space)));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onLoadMoreItems(int position) {
        iTake = iTake + 5;
        BlueShakLog.logDebug(TAG,"onLoadMoreItems iTake ------>"+iTake);
        getFeatureList(context, model,String.valueOf(iTake));
    }

    private void setFilterView(ArrayList<String> arrayList){
          if(layoutFilterHorizontal!=null){
              layoutFilterHorizontal.removeAllViews();
          }
        for(int i = 0; i < arrayList.size(); i++){

            com.blueshak.app.blueshak.text.MyTextViewMediumBold textViewMediumBold = new MyTextViewMediumBold(getContext());
            textViewMediumBold.setBackgroundResource(R.drawable.rectangle_nearest);
            textViewMediumBold.setText(arrayList.get(i));
            textViewMediumBold.setPadding(20,15,20,15);
            textViewMediumBold.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
            textViewMediumBold.setTextSize(16);
            FlexboxLayout.LayoutParams paramsFilterText = new FlexboxLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            //paramsFilterText.topMargin = 10;
            //paramsFilterText.leftMargin = 5;
            paramsFilterText.rightMargin = 15;
            textViewMediumBold.setLayoutParams(paramsFilterText);

            layoutFilterHorizontal.addView(textViewMediumBold);

        }

    }
    private ArrayList<String> defaultFilteredList(){
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(getString(R.string.item_list_fragment_for_list_result_from_nearest_new));
        return arrayList;
    }
}

