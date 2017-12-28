package com.blueshak.app.blueshak.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blueshak.app.blueshak.Messaging.helper.Constants;
import com.blueshak.app.blueshak.search.SearchActivity;
import com.blueshak.blueshak.R;
import com.blueshak.app.blueshak.MainActivity;
import com.blueshak.app.blueshak.PickLocation;
import com.blueshak.app.blueshak.filter.FilterActivity;
import com.blueshak.app.blueshak.global.GlobalFunctions;
import com.blueshak.app.blueshak.global.GlobalVariables;
import com.blueshak.app.blueshak.services.ServerResponseInterface;
import com.blueshak.app.blueshak.services.ServicesMethodsManager;
import com.blueshak.app.blueshak.services.model.FilterModel;
import com.blueshak.app.blueshak.services.model.HomeListModel;
import com.blueshak.app.blueshak.services.model.LocationModel;
import com.blueshak.app.blueshak.services.model.ProductModel;
import com.blueshak.app.blueshak.services.model.SalesListModel;
import com.blueshak.app.blueshak.services.model.SalesModel;
import com.blueshak.app.blueshak.util.LocationListener;
import com.blueshak.app.blueshak.util.LocationService;

import java.util.ArrayList;
import java.util.List;


public class ItemListFragment extends Fragment implements LocationListener/*,onFilterChange*/ , SwipeRefreshLayout.OnRefreshListener{

    public static  boolean listOrGrid=false;
    public static final String TAG = "ItemListFragment";
    public static final String SALES_LIST_GARAGGE_SALE_MODEL_SERIALIZE="SalesListGarageSaleModelSerialize";
    public static final String SALE_FILTER="filtermodel";
    public static final String LOCATION_MODEL="locationmodel";
    public static final String SALES_LIST_GARAGGE_SALE_MODEL_TYPE="SalesListGarageSaleModelType";
    private Context context;
    private TextView no_sales,results_all;
    private static  Activity activity;
    private LayoutInflater inflater = null;
    private SalesListModel salesListModel=new SalesListModel();
    private HomeListModel itemListModel=new HomeListModel();
    private ItemListAdapter adapter;
    private ArrayList<SalesModel> list = new ArrayList<SalesModel>();
    private ArrayList<ProductModel> product_list = new ArrayList<ProductModel>();
    private GlobalVariables globalVariables=new GlobalVariables();
    private LocationService locServices;
    private boolean location_available;
    private boolean when_open_the_app=false;
    private RecyclerView recyclerView;
    private static FilterModel model=null;
    private  LocationModel locationModel= null;
    private TextView location,filter_tag;
    private ImageView location_arrow,filter;
    public String item_address;
    private String no_items_found;
    private  int last_page=0;
    private String Sydney_latitude="-33.8688196";
    private String Sydney_longitude="151.2092955";
    private  EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;
    int minPriceValue=0, maxPriceValue=GlobalVariables.PRICE_MAX_VALUE, minDistanceValue=0, maxDistanceValue=GlobalVariables.DISTANCE_MAX_VALUE;
    private ProgressBar progress_bar;
    private TextView searchViewResult;
    private ImageView go_to_filter;

    public static ItemListFragment newInstance(SalesListModel salesListModel, String type,FilterModel filterModel,LocationModel locationModel){
        ItemListFragment saleFragment = new ItemListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(SALES_LIST_GARAGGE_SALE_MODEL_SERIALIZE, salesListModel);
        bundle.putSerializable(SALE_FILTER, filterModel);
        bundle.putSerializable(LOCATION_MODEL,locationModel);
        bundle.putString(SALES_LIST_GARAGGE_SALE_MODEL_TYPE, type);
        saleFragment.setArguments(bundle);
        return saleFragment;
    }
    private Toolbar toolbar;
    /* @InjectView(R.id.swipe_container)*/
    private SwipeRefreshLayout swipeRefreshLayout;
   /* LinearLayout header_content;*/
    private Handler handler=new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.sales_list_item_fragment, container, false);
        try{
            progress_bar=(ProgressBar)view.findViewById(R.id.progress_bar);
            context= getActivity();
            activity= getActivity();
            this.inflater = inflater;
            locServices = new LocationService(activity);
            locServices.setListener(this);
            swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
            swipeRefreshLayout.setOnRefreshListener(this);
            results_all=(TextView) view.findViewById(R.id.results_all);
          /*  go_to_filter=(ImageView) view.findViewById(R.id.go_to_filter);*/
          /*  header_content=(LinearLayout)view.findViewById(R.id.header_content);
            header_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addFilter();
                }
            });*/

            Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
            go_to_filter=(ImageView)toolbar.findViewById(R.id.go_to_filter);
            go_to_filter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addFilter();
                }
            });
            searchViewResult=(TextView)view.findViewById(R.id.searchViewResult);
            /*searchViewResult.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SearchActivity.TYPE_SEARCH=GlobalVariables.TYPE_SHOP;
                    startActivity(new Intent(activity, SearchActivity.class));
                }
            });*/
            /*results_all.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final int DRAWABLE_LEFT = 0;
                    final int DRAWABLE_TOP = 1;
                    final int DRAWABLE_RIGHT = 2;
                    final int DRAWABLE_BOTTOM = 3;
                    if(event.getAction() == MotionEvent.ACTION_UP) {
                        if(event.getRawX() >= results_all.getRight() - results_all.getTotalPaddingRight()){
                            Log.d(TAG,"##########onTouch#########");
                            Intent filter_intent=FilterActivity.newInstance(context,locationModel);
                            startActivity(filter_intent);
                            return true;
                        }
                    }
                    return false;
                }
            });*/

            /*final SwipeRefreshLayout.OnRefreshListener swipeRefreshListner = new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    Log.i(TAG, "onRefresh called from SwipeRefreshLayout");
                  *//*  swipeRefreshLayout.setRefreshing(true);
                   *//**//* setThisPage();*//*
                }
            };*/
            no_items_found=context.getResources().getString(R.string.no_items_found);
            swipeRefreshLayout.setColorSchemeColors(context.getResources().getColor(R.color.brandColor),
                    context.getResources().getColor(R.color.tab_selected),
                    context.getResources().getColor(R.color.darkorange),
                    context.getResources().getColor(R.color.brandColor));
            salesListModel = (SalesListModel) getArguments().getSerializable(SALES_LIST_GARAGGE_SALE_MODEL_SERIALIZE);
            locationModel = (LocationModel) getArguments().getSerializable(LOCATION_MODEL);
            model = (FilterModel) getArguments().getSerializable(SALE_FILTER);
            salesListModel = (SalesListModel) getArguments().getSerializable(SALES_LIST_GARAGGE_SALE_MODEL_SERIALIZE);

            if(model==null)
            {
                model=new FilterModel();
                if(locationModel!=null)
                {
                    Log.d(TAG,"######locationModel####"+locationModel.toString());
                    Log.d(TAG,"######getState####"+locationModel.getState());
                    Log.d(TAG,"######getLatitude####"+locationModel.getLatitude());
                    Log.d(TAG,"######getLongitude####"+locationModel.getLongitude());
                    if(locationModel.getCity()!=null&&locationModel.getState()!=null)
                        item_address=locationModel.getCity()+" "+locationModel.getState();
                    else
                        item_address="Sydney"+", Australia";
                    if(!locationModel.getLatitude().equalsIgnoreCase("0.0")&&!locationModel.getLongitude().equalsIgnoreCase("0.0")){
                        model.setLatitude(locationModel.getLatitude()+"");
                        model.setLongitude(locationModel.getLongitude()+"");
                    }else{
                        Log.d(TAG,"#######Setting the Sydney Lat and Lng###################");
                        model.setLatitude(GlobalVariables.Sydney_latitude);
                        model.setLongitude(GlobalVariables.Sydney_longitude);
                    }
                    model.setRange(maxDistanceValue);
                    model.setAvailable(true);
                    model.setPickup(true);
                    model.setShipable(true);
                    model.setType(GlobalVariables.TYPE_SHOP);
                    model.setPriceRange(minPriceValue+","+maxPriceValue);
                    when_open_the_app=true;
                }else{
                    if(!locServices.canGetLocation()){
                        location_available=false;
                       /* locServices.showSettingsAlert();*/
                        model.setLatitude(Sydney_latitude);
                        model.setLongitude(Sydney_longitude);
                        model.setRange(maxDistanceValue);
                        model.setAvailable(true);
                        model.setPickup(true);
                        model.setShipable(true);
                        model.setType(GlobalVariables.TYPE_SHOP);
                        model.setPriceRange(minPriceValue+","+maxPriceValue);
             /*   getItemLists(context,model);*/
                    }else{
                        location_available=true;
                        when_open_the_app=true;
                        Location loc = locServices.getLocation();
                        if(loc!=null){
                            model.setLatitude(loc.getLatitude()+"");
                            model.setLongitude(loc.getLongitude()+"");
                            model.setRange(maxDistanceValue);
                            model.setAvailable(true);
                            model.setPickup(true);
                            model.setShipable(true);
                            model.setType(GlobalVariables.TYPE_SHOP);
                            model.setPriceRange(minPriceValue+","+maxPriceValue);
                    /*getItemLists(context,model);*/
                        }
                    }
                }
                if(!TextUtils.isEmpty(model.getResults_text()))
                    results_all.setText("Results from "+model.getResults_text());
                else
                    results_all.setText(Constants.getTextFromId(context,R.string.item_list_fragment_for_list_result_from_nearest_new));


            }else{
                item_address=model.getLocation();
               /* String category_names=model.getCategory_names();
                if(!TextUtils.isEmpty(category_names)&& category_names!=null)
                    results_all.setText("Results in "+"'"+model.getCategory_names()+"'");
                else
                    results_all.setText("Results in "+"'"+"All"+"'");*/

                if(!TextUtils.isEmpty(model.getResults_text()))
                    results_all.setText("Results from "+model.getResults_text());
                else
                    results_all.setText(Constants.getTextFromId(context,R.string.item_list_fragment_for_list_result_from_nearest_new));

//                results_all.setText(getResources().getString(R.string.item_list_fragment_for_list_result_from_nearest_new));
            }
            location = (TextView)view.findViewById(R.id.location);
            filter_tag = (TextView)view.findViewById(R.id.filter_tag);
            location_arrow=(ImageView)view.findViewById(R.id.location_arrow);
            filter=(ImageView)view.findViewById(R.id.filter);
            toolbar = (Toolbar)getActivity().findViewById(R.id.toolbar);
            no_sales = (TextView) view.findViewById(R.id.no_sales);
            recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
            adapter = new ItemListAdapter(context, product_list);
            StaggeredGridLayoutManager gridLayoutManager =
                    new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.addItemDecoration(new SpacesItemDecoration(
                    getResources().getDimensionPixelSize(R.dimen.space)));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
           /* recyclerView.setOnScrollListener(new MyScrollListener(activity) {
                @Override
                public void onMoved(int distance) {
                    toolbar.setTranslationY(-distance);
                }
            });*/
            endlessRecyclerOnScrollListener=new EndlessRecyclerOnScrollListener(gridLayoutManager) {
                @Override
                public void onLoadMore(int current_page) {
                    Log.d(TAG,"############onLoadMore###############"+current_page+"last_page########"+last_page);
                    if(!(current_page>last_page)){
                      /*  adapter.showLoading(true);*/
                        model.setPage(current_page);
                        getItemLists(context,model);
                    }else{
                        /*adapter.showLoading(false);*/
                    }
                }
            };




            recyclerView.addOnScrollListener(endlessRecyclerOnScrollListener);
            location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=PickLocation.newInstance(context,GlobalVariables.TYPE_HOME,true,null);
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
                    Intent intent=PickLocation.newInstance(context,GlobalVariables.TYPE_HOME,true,null);
                    context.startActivity(intent);
                }
            });
            if(!GlobalFunctions.isNetworkAvailable(context)){
                Toast.makeText(context,"Please check your internet connection",Toast.LENGTH_LONG).show();
            }else{
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setThisPage();
                    }
                });

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
        MainActivity.setTitle(getString(R.string.item),0);
        super.onResume();
    }
    private void setThisPage(){
      /*  if(TextUtils.isEmpty(item_address)&&model.getLongitude()!=null&&model.getLongitude()!=null)
            getAddressFromLatLng(model.getLatitude(),model.getLongitude());
      *//*  else
            setLocation(item_address);*//*
*/
        if(product_list!=null){
            product_list.clear();

        }
        if(when_open_the_app){
            Log.d(TAG,"######when_open_the_app###########");
            if(locServices!=null)
                setCountry(context,locServices.getLatitude(),locServices.getLongitude());
        }else{
            Log.d(TAG,"######from the Filter or Current Location###########");
                if(model!=null){
                    /*String country= GlobalFunctions.getSharedPreferenceString(context,GlobalVariables.SHARED_PREFERENCE_LOCATION_COUNTRY);
                    if(country!=null){
                        model.setCurrent_country_code(country);
                        model.setIs_current_country(true);
                    }*/

                    String filter_string = GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.FILTER_MODEL);
                    Log.d(TAG, "######filter_string######" + filter_string);
                    if (filter_string != null)
                    {
                        Log.d(TAG, "########Filter is NOT null############");
                        detail.toObject(filter_string);
                        if(detail.is_current_country())
                        {
                            String countray= GlobalFunctions.getSharedPreferenceString(context,GlobalVariables.SHARED_PREFERENCE_LOCATION_COUNTRY);
                            model.setCurrent_country_code(countray);
                            model.setIs_current_country(true);
                        }
                        else
                        {
                            model.setIs_current_country(false);
                        }
                    }
                    else {
                        String countray= GlobalFunctions.getSharedPreferenceString(context,GlobalVariables.SHARED_PREFERENCE_LOCATION_COUNTRY);
                        model.setCurrent_country_code(countray);
                        model.setIs_current_country(true);
                    }
                    if(detail!=null)
                    {
                        Log.d("http"," http://dev.blueshak.com/api"+detail.is_current_country());
                    }
                    model.setPage(1);
                    getItemLists(context,model);
                }
        }

    }
    private FilterModel detail = new FilterModel();
    private void setLocation(String item_address){
        Log.d(TAG,"#########item_address##############"+item_address);
        if(TextUtils.isEmpty(item_address))
            item_address ="Pick Location";
        String current_location="";
        if(!item_address.equalsIgnoreCase("Pick Location")){
            current_location="Listing near "+item_address;
            SpannableString ss = new SpannableString(current_location);
            ss.setSpan(new MyClickableSpan(),12,current_location.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            location.setText(ss);
            location.setMovementMethod(LinkMovementMethod.getInstance());
        }else{
            location.setText(item_address);
            location.setTextColor(context.getResources().getColor(R.color.tab_selected));
            location.setGravity(Gravity.CENTER);

        }
    }

    private void setItemListValues(HomeListModel model){
        if(model!=null){
            String str = "";
            itemListModel = model;
            last_page=itemListModel.getLast_page();
            List<ProductModel> productModels = itemListModel.getItem_list();
            System.out.println("#######itemListModel.getItem_list()#########"+itemListModel.getItem_list().size());
            if(productModels!=null){
                if(productModels.size()>0&&recyclerView!=null&&list!=null&&adapter!=null){
                    product_list.addAll(productModels);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            synchronized (adapter){adapter.notifyDataSetChanged();}
                        }
                    });

                }
            }
        }

    }
    private void getItemLists(final Context context, FilterModel filterModel){
    /*    showProgress();*/
        showProgressBar();
        Log.d(TAG, "GetItemLists filterModel :"+filterModel.toString());
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getHomeList(context, filterModel, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                swipeRefreshLayout.setRefreshing(false);
                Log.d(TAG, "onSuccess Response");
                itemListModel = (HomeListModel) arg0;
                String str = itemListModel.toString();
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
                Log.d(TAG, "OnFailureFromServer################"+msg);
              /*  Toast.makeText(context,no_items_found,Toast.LENGTH_LONG).show();*/

            }

            @Override
            public void OnError(String msg) {
                swipeRefreshLayout.setRefreshing(false);
                Log.d(TAG,"#######OnError######"+ msg);
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
        last_page=0;
        setThisPage();
    }
    public void reset(){
        endlessRecyclerOnScrollListener.reset(0, true);
        last_page=0;
    }
    public void showProgress(){
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
            int[] actionBarAttr = new int[] { android.R.attr.actionBarSize };
            TypedArray a = context.obtainStyledAttributes(actionBarAttr);
            toolbarHeight = (int) a.getDimension(0, 0) + 10;
            a.recycle();
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            clipToolbarOffset();
            onMoved(toolbarOffset);

            if((toolbarOffset < toolbarHeight && dy>0) || (toolbarOffset > 0 && dy<0)) {
                toolbarOffset += dy;
            }
        }

        private void clipToolbarOffset() {
            if(toolbarOffset > toolbarHeight) {
                toolbarOffset = toolbarHeight;
            } else if(toolbarOffset < 0) {
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
    private void addMoreItems(final Context context, FilterModel filterModel){
        showProgress();
        Log.d(TAG, "FilterModel :"+filterModel.toString());
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getHomeList(context,filterModel, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                swipeRefreshLayout.setRefreshing(false);
                no_sales.setVisibility(View.GONE);
                Log.d(TAG, "onSuccess Response");
                itemListModel = (HomeListModel)arg0;
                String str = itemListModel.toString();
                setItemListValues(itemListModel);
            }

            @Override
            public void OnFailureFromServer(String msg) {
                swipeRefreshLayout.setRefreshing(false);
                no_sales.setVisibility(View.VISIBLE);
                no_sales.setText(no_items_found);
                Log.d(TAG, "OnFailureFromServer################"+msg);
                /*Toast.makeText(context,no_items_found,Toast.LENGTH_LONG).show();*/

            }

            @Override
            public void OnError(String msg) {
                swipeRefreshLayout.setRefreshing(false);
                Log.d(TAG,"#######OnError######"+ msg);
                no_sales.setVisibility(View.VISIBLE);
                no_sales.setText(no_items_found);
                /*Toast.makeText(context,no_items_found,Toast.LENGTH_LONG).show();*/
            }
        }, "List Sales");

    }

    private void getAddressFromLatLng(final String lat,final String lng){
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getAddress(activity,Double.parseDouble(lat),Double.parseDouble(lng), new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                GlobalFunctions.hideProgress();
                LocationModel locationModel_ =(LocationModel)arg0;
                locationModel=locationModel_;
                locationModel.setLatitude(lat);
                locationModel.setLongitude(lng);
                if(locationModel_ !=null)
                    /*setLocation(locationModel_.getCity()+" "+locationModel_.getState());*/
                    GlobalFunctions.setSharedPreferenceString(context,GlobalVariables.CURRENT_LOCATION,locationModel_.getCity());
            }
            @Override
            public void OnFailureFromServer(String msg) {
            }

            @Override
            public void OnError(String msg) {
            }
        }, "Get AddressFromLatLng");


    }
    public void addFilter(){
        Intent filter_intent=FilterActivity.newInstance(context,locationModel,GlobalVariables.TYPE_ITEMS);
        startActivityForResult(filter_intent,globalVariables.REQUEST_CODE_FILTER_MODEL_LOCATION);
        activity.overridePendingTransition(R.animator.fadeout, R.animator.fadein);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG,"on activity result");
        if(resultCode == activity.RESULT_OK){
            Log.i(TAG,"result ok ");
            Log.i(TAG,"request code "+globalVariables.REQUEST_CODE_FILTER_MODEL_LOCATION);
            if(data.hasExtra(MainActivity.MAIN_ACTIVITY_FILTER_MODEL_SERIALIZE)){
                model=(FilterModel) data.getExtras().getSerializable(MainActivity.MAIN_ACTIVITY_FILTER_MODEL_SERIALIZE);
                if(model!=null){
                    if(model.getCurrent_country_code()==null)
                        model.setIs_current_country(false);
                    MainActivity.filterModel=model;
                    item_address=model.getLocation();
                    String category_names=model.getCategory_names();
                    if(!TextUtils.isEmpty(model.getResults_text()))
                        results_all.setText("Results from "+model.getResults_text());
                    else
                        results_all.setText(Constants.getTextFromId(context,R.string.item_list_fragment_for_list_result_from_nearest_new));


                    /*if(!TextUtils.isEmpty(category_names)&& category_names!=null)
                        results_all.setText("Results in "+"'"+model.getCategory_names()+"'");
                    else
                        results_all.setText("Results in "+"'"+"All"+"'");*/
                    Log.i(TAG,"name pm"+model.getFormatted_address());
                    /*reset the Listener for once the filter is set*/
                    reset();
                    setThisPage();
                }
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
    private void setCountry(final Context context, final Double lattitude,final Double longitude){
        showProgressBar();
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getAddress(activity, lattitude,longitude, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                hideProgressBar();
                GlobalFunctions.closeKeyboard(activity);
                locationModel=(LocationModel)arg0;
                try{
                    locationModel.setLatitude(lattitude.toString());
                    locationModel.setLongitude(longitude.toString());
                    Log.d(TAG,"###########Setting the Current Country SHARED_PREFERENCE_LOCATION_COUNTRY############");
                    if(when_open_the_app){
                        GlobalFunctions.setSharedPreferenceString(context,GlobalVariables.SHARED_PREFERENCE_LOCATION_COUNTRY,locationModel.getCountry_code());
                        model.setCurrent_country_code(locationModel.getCountry_code());
                        model.setIs_current_country(true);
                        getItemLists(context,model);
                        when_open_the_app=false;
                    }else{
                        if(locationModel!=null){
                            GlobalFunctions.setSharedPreferenceString(context,GlobalVariables.SHARED_PREFERENCE_LOCATION_COUNTRY,locationModel.getCountry_code());
                        }
                    }
                }catch (NullPointerException ex){
                    ex.printStackTrace();
                    Log.d(TAG,"Exception: "+ex.getMessage());
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
}

