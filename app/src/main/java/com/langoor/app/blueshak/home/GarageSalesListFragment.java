package com.langoor.app.blueshak.home;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
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
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.langoor.app.blueshak.search.SearchActivity;
import com.langoor.blueshak.R;
import com.langoor.app.blueshak.AppController;
import com.langoor.app.blueshak.MainActivity;
import com.langoor.app.blueshak.PickLocation;
import com.langoor.app.blueshak.filter.FilterActivity;
import com.langoor.app.blueshak.global.GlobalFunctions;
import com.langoor.app.blueshak.global.GlobalVariables;
import com.langoor.app.blueshak.services.ServerResponseInterface;
import com.langoor.app.blueshak.services.ServicesMethodsManager;
import com.langoor.app.blueshak.services.model.FilterModel;
import com.langoor.app.blueshak.services.model.LocationModel;
import com.langoor.app.blueshak.services.model.SalesListModel;
import com.langoor.app.blueshak.services.model.SalesListModelNew;
import com.langoor.app.blueshak.services.model.SalesModel;
import com.langoor.app.blueshak.util.LocationListener;
import com.langoor.app.blueshak.util.LocationService;
import com.langoor.app.blueshak.view_sales.MapFragmentSales;
import java.util.ArrayList;
import java.util.List;

public class GarageSalesListFragment  extends Fragment implements LocationListener/*,onFilterChange*/  , SwipeRefreshLayout.OnRefreshListener{

    public static final String TAG = "GarageSalesListFragment";
    public static final String SALES_LIST_GARAGGE_SALE_MODEL_SERIALIZE="GarageSalesListGarageSaleModelSerialize";
    public static final String SALE_FILTER="Garagefiltermodel";
    public static final String SALES_LIST_GARAGGE_SALE_MODEL_TYPE="GarageSalesListGarageSaleModelType";
    public static final String SALES_MY_LIST_GARAGGE_SALE_MODEL_TYPE="GarageSalesMyListGarageSaleModelType";
    public static final String LOCATION_MODEL="Garagelocationmodel";
    private GlobalVariables globalVariables=new GlobalVariables();
    private Handler handler=new Handler();
    private Context context;
    private TextView no_sales;
    private String no_sales_text="No Sales found near you";
    private Activity activity;
    private LayoutInflater inflater = null;
    private SalesListModelNew salesListModel=new SalesListModelNew();
    private SalesListAdapterNew adapter;
    private TextView sale_header_name,results_all;
    private String header_name;
    private ArrayList<SalesModel> sales_list = new ArrayList<SalesModel>();
    private String type = GlobalVariables.TYPE_GARAGE;
    private LocationService locServices;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private  LocationModel locationModel=new LocationModel();
    FilterModel model = new FilterModel();
    private SwipeRefreshLayout swipeRefreshLayout;
    public String item_address;
    private boolean location_available;
    private String Sydney_latitude="-33.8688196";
    private String Sydney_longitude="151.2092955";
    private TextView location,filter_tag;
    private ImageView location_arrow,filter,go_to_filter;
    private  int last_page=1;
   /* private LinearLayout header_content;*/
    int minPriceValue=0, maxPriceValue=GlobalVariables.PRICE_MAX_VALUE, minDistanceValue=0, maxDistanceValue=GlobalVariables.DISTANCE_MAX_VALUE;
    private ProgressBar progress_bar;
    private TextView searchViewResult;
    private EndlessRecyclerOnScrollListenerLinearView endlessRecyclerOnScrollListenerLinearView;
    public static GarageSalesListFragment newInstance(SalesListModelNew salesListModel, String type,FilterModel filterModel,LocationModel locationModel,boolean list_my_sales){
        GarageSalesListFragment garageSalesListFragment = new GarageSalesListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(SALES_LIST_GARAGGE_SALE_MODEL_SERIALIZE, salesListModel);
        bundle.putSerializable(SALE_FILTER, filterModel);
        bundle.putSerializable(LOCATION_MODEL,locationModel);
        bundle.putString(SALES_LIST_GARAGGE_SALE_MODEL_TYPE, type);
        bundle.putBoolean(SALES_MY_LIST_GARAGGE_SALE_MODEL_TYPE, list_my_sales);
        garageSalesListFragment.setArguments(bundle);
        return garageSalesListFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.sales_list_item_fragment, container, false);
        try{
            progress_bar=(ProgressBar)view.findViewById(R.id.progress_bar);
            context= getActivity();
            activity= getActivity();
            searchViewResult=(TextView)view.findViewById(R.id.searchViewResult);
            searchViewResult.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(activity, SearchActivity.class));
                }
            });
            this.inflater = inflater;
            toolbar = (Toolbar)getActivity().findViewById(R.id.toolbar);
            swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
            swipeRefreshLayout.setOnRefreshListener(this);
            swipeRefreshLayout.setColorSchemeColors(context.getResources().getColor(R.color.brandColor),
                    context.getResources().getColor(R.color.tab_selected),
                    context.getResources().getColor(R.color.darkorange),
                    context.getResources().getColor(R.color.brandColor));
            salesListModel = (SalesListModelNew) getArguments().getSerializable(SALES_LIST_GARAGGE_SALE_MODEL_SERIALIZE);
            type = getArguments().getString(SALES_LIST_GARAGGE_SALE_MODEL_TYPE);
            location = (TextView)view.findViewById(R.id.location);
            results_all=(TextView) view.findViewById(R.id.results_all);
            filter_tag = (TextView)view.findViewById(R.id.filter_tag);
            location_arrow=(ImageView)view.findViewById(R.id.location_arrow);
            filter=(ImageView)view.findViewById(R.id.filter);
            no_sales = (TextView) view.findViewById(R.id.no_sales);
            sale_header_name= (TextView) view.findViewById(R.id.sale_header_name);
           /* header_content=(LinearLayout)view.findViewById(R.id.header_content);
            header_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addFilter();
                }
            });*/
            locServices = new LocationService(activity);
            locServices.setListener(this);
            recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
            adapter = new SalesListAdapterNew(context, sales_list);
            LinearLayoutManager linearLayoutManagerVertical =
                    new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
            recyclerView.addItemDecoration(new SpacesItemDecoration(
                    getResources().getDimensionPixelSize(R.dimen.space)));
            recyclerView.setLayoutManager(linearLayoutManagerVertical);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
        /*    recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListenerLinearView(
                    linearLayoutManagerVertical) {
                @Override
                public void onLoadMore(int current_page) {
                    Log.d(TAG,"####onLoadMore#####"+current_page);
                    if(!(current_page>last_page)){
                        Log.d(TAG,"#####addOnScrollListener############"+last_page);
                        adapter.showLoading(true);
                        model.setPage(current_page);
                        getLists(context,model);
                    }else{
                        adapter.showLoading(false);
                    }
                }
            });*/
            endlessRecyclerOnScrollListenerLinearView=new EndlessRecyclerOnScrollListenerLinearView(linearLayoutManagerVertical) {
                @Override
                public void onLoadMore(int current_page) {
                    Log.d(TAG,"####onLoadMore#####"+current_page);
                    if(!(current_page>last_page)){
                        Log.d(TAG,"#####addOnScrollListener############"+last_page);
                        adapter.showLoading(true);
                        model.setPage(current_page);
                        getLists(context,model);
                    }else{
                        adapter.showLoading(false);
                    }
                }
            };
           /* recyclerView.setOnScrollListener(new MyScrollListener(activity) {
                @Override
                public void onMoved(int distance) {
                    toolbar.setTranslationY(-distance);
                }
            });*/
            salesListModel = (SalesListModelNew) getArguments().getSerializable(SALES_LIST_GARAGGE_SALE_MODEL_SERIALIZE);
            locationModel = (LocationModel) getArguments().getSerializable(LOCATION_MODEL);
            model = (FilterModel) getArguments().getSerializable(SALE_FILTER);
            salesListModel = (SalesListModelNew) getArguments().getSerializable(SALES_LIST_GARAGGE_SALE_MODEL_SERIALIZE);
            if(model==null){
                model=new FilterModel();
                if(locationModel!=null){
                    item_address=locationModel.getCity()+" "+locationModel.getState();
                    model.setLatitude(locationModel.getLatitude()+"");
                    model.setLongitude(locationModel.getLongitude()+"");
                    model.setRange(maxDistanceValue);
                    model.setAvailable(true);
                    model.setPickup(true);
                    model.setShipable(true);
                    model.setType(GlobalVariables.TYPE_SHOP);
                    model.setPriceRange(minPriceValue+","+maxPriceValue);
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
                        Log.d(TAG,"###############Garage Sale Lis First TIme##################");
                        String country= GlobalFunctions.getSharedPreferenceString(context,GlobalVariables.SHARED_PREFERENCE_LOCATION_COUNTRY);
                        model.setCurrent_country_code(country);
                        model.setIs_current_country(true);
                        location_available=true;
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
            }else{
                item_address=model.getLocation();
              /*  String category_names=model.getCategory_names();
                if(!TextUtils.isEmpty(category_names)&& category_names!=null)
                    results_all.setText("Results in "+"'"+model.getCategory_names()+"'");
                else
                    results_all.setText("Results in "+"'"+"All"+"'");*/
                results_all.setText("Results from nearest first");
            }

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setThisPage();
                }
            });
           /* go_to_filter=(ImageView) view.findViewById(R.id.go_to_filter);
            go_to_filter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addFilter();
                }
            });
            location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= PickLocation.newInstance(context,GlobalVariables.TYPE_HOME,true,null);
                    context.startActivity(intent);
                }
            });*/
            Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);


            filter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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

            go_to_filter=(ImageView)toolbar.findViewById(R.id.go_to_filter);
            go_to_filter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addFilter();
                }
            });

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
    public void onRefresh() {
        /*swipeRefreshLayout.setRefreshing(false);*/
        swipeRefreshLayout.setRefreshing(false);
        endlessRecyclerOnScrollListenerLinearView.reset(0, true);
        last_page=0;
        setThisPage();
        /*setThisPage();*/
    }
    private void setThisPage(){
        if(TextUtils.isEmpty(item_address)&&model.getLongitude()!=null&&model.getLongitude()!=null)
            getAddressFromLatLng(model.getLatitude(),model.getLongitude());
        else
            setLocation(item_address);

        if(sales_list!=null){
            sales_list.clear();
            refreshList();
         /*   synchronized (adapter){adapter.notifyDataSetChanged();}*/
        }
        if(model!=null){
            model.setPage(1);
            getLists(context,model);
        }

    }
    @Override
    public void onResume() {

        super.onResume();
    }
    private void setValues(SalesListModelNew model){
        if(model!=null){
            String str = "";
            salesListModel = model;
            last_page=model.getLast_page();
            List<SalesModel> productModels = salesListModel.getSalesList();
            if(productModels!=null){
                if(productModels.size()>0&&recyclerView!=null&&sales_list!=null&&adapter!=null){
                    if(productModels.size()==1)
                        str = productModels.size() + " Garage Sale you";
                    else
                        str = productModels.size() + " Garage Sales near you";

                    sale_header_name.setText(str);

                    no_sales.setVisibility(View.GONE);
                 /*   sales_list.clear();*/
                    sales_list.addAll(productModels);
                    Log.d(TAG,"productModels########"+productModels.size()+"sales_list#########"+sales_list.size());
                    refreshList();
                    // setHeader(inflater);
                } else{   no_sales.setVisibility(View.VISIBLE);
                    no_sales.setText("No Sales found");

                    sale_header_name.setText("No Sales found near you");

                    sale_header_name.setVisibility(View.GONE);
                }
            } else{
                no_sales.setVisibility(View.VISIBLE);
                no_sales.setText("No Sales found");
                sale_header_name.setText("No Sales found near you");

                sale_header_name.setVisibility(View.GONE);
            }
        }

    }

    private void deleteList(int position){
        if(position>=0&&recyclerView!=null&&sales_list!=null&&adapter!=null){
            if(position+1<=sales_list.size()){
                sales_list.remove(position);
               /* synchronized (adapter){adapter.notifyDataSetChanged();}*/
                refreshList();
            }
        }
    }

    private void getLists(Context context, FilterModel filterModel){
        showProgressBar();
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getListDetails(context, filterModel, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                hideProgressBar();
                swipeRefreshLayout.setRefreshing(false);
                Log.d(TAG, "onSuccess Response");
                salesListModel = (SalesListModelNew) arg0;
                String str = salesListModel.toString();
                Log.d(TAG, str);
                setValues(salesListModel);
            }

            @Override
            public void OnFailureFromServer(String msg) {
                hideProgressBar();
                Log.d(TAG, msg);
                swipeRefreshLayout.setRefreshing(false);
                sale_header_name.setVisibility(View.GONE);
                no_sales.setVisibility(View.VISIBLE);
                sale_header_name.setText("No Sales found near you");
                no_sales.setText(no_sales_text);
            }

            @Override
            public void OnError(String msg) {
                hideProgressBar();
                Log.d(TAG, msg);
                swipeRefreshLayout.setRefreshing(false);
                sale_header_name.setVisibility(View.GONE);
                no_sales.setVisibility(View.VISIBLE);
                sale_header_name.setText("No Sales found near you");
                no_sales.setText(no_sales_text);
            }
        }, "List Sales");

    }

    @Override
    public void onLocationChanged(Location location) {
        if(!type.equalsIgnoreCase(GlobalVariables.TYPE_GARAGE+"")){
            Location loc = locServices.getLocation();
            if(loc!=null){
                FilterModel model = new FilterModel();
                model.setLatitude(loc.getLatitude()+"");
                model.setLongitude(loc.getLongitude()+"");
                model.setRange(10);
                model.setAvailable(true);
                model.setPickup(true);
                model.setShipable(true);
                model.setType(type);
                model.setPriceRange("0,10000");
                getLists(getContext(),model);
                locServices.removeListener();
            }
        }else {
            locServices.removeListener();
        }
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
    @Override
    public void onProviderEnabled(String provider) {

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
    private void getAddressFromLatLng(final String lat,final String lng){
        showProgressBar();
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getAddress(activity,Double.parseDouble(lat),Double.parseDouble(lng), new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                hideProgressBar();
                GlobalFunctions.hideProgress();
                LocationModel locationModel_ =(LocationModel)arg0;
                locationModel=locationModel_;
                locationModel.setLatitude(lat);
                locationModel.setLongitude(lng);
                if(locationModel_ !=null)
                    setLocation(locationModel_.getCity()+" "+locationModel_.getState());
                GlobalFunctions.setSharedPreferenceString(context,GlobalVariables.CURRENT_LOCATION,locationModel_.getCity());
            }
            @Override
            public void OnFailureFromServer(String msg) {
                hideProgressBar();
            }

            @Override
            public void OnError(String msg) {
                hideProgressBar();
            }
        }, "Get AddressFromLatLng");


    }
    private void setLocation(String item_address){
        if(TextUtils.isEmpty(item_address))
            item_address ="Pick Location";
        String current_location="";
        if(!item_address.equalsIgnoreCase("Pick Location")){
            current_location="Listing near "+item_address;
            SpannableString ss = new SpannableString(current_location);
            ss.setSpan(new MyClickableSpan(),12,current_location.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            location.setText(ss);
            location.setMovementMethod(LinkMovementMethod.getInstance());
        }else{
            location.setText(item_address);
            location.setTextColor(context.getResources().getColor(R.color.tab_selected));
            location.setGravity(Gravity.CENTER);

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
    public void refreshList(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                synchronized (adapter){adapter.notifyDataSetChanged();}
            }
        });
    }
        public void addFilter(){
            Intent filter_intent=FilterActivity.newInstance(context,locationModel,GlobalVariables.TYPE_GARAGE_SALE);
            startActivityForResult(filter_intent,globalVariables.REQUEST_CODE_FILTER_MODEL_LOCATION);
        }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
            Log.i(TAG,"on activity result");
            if(resultCode == activity.RESULT_OK){
                Log.i(TAG,"result ok ");
                Log.i(TAG,"request code "+globalVariables.REQUEST_CODE_FILTER_MODEL_LOCATION);
                if(data.hasExtra(MainActivity.MAIN_ACTIVITY_FILTER_MODEL_SERIALIZE)){
                    model=(FilterModel) data.getExtras().getSerializable(MainActivity.MAIN_ACTIVITY_FILTER_MODEL_SERIALIZE);
                    if(model!=null){
                        MainActivity.filterModel=model;
                        item_address=model.getLocation();
                        String category_names=model.getCategory_names();
                     /*   if(!TextUtils.isEmpty(category_names)&& category_names!=null)
                            results_all.setText("Results in "+"'"+model.getCategory_names()+"'");
                        else
                            results_all.setText("Results in "+"'"+"All"+"'");*/
                        if(!TextUtils.isEmpty(model.getResults_text()))
                            results_all.setText("Results in "+model.getResults_text());
                        else
                            results_all.setText("Results from nearest first");

                        Log.i(TAG,"name pm"+model.getFormatted_address());
                        reset();
                        setThisPage();
                    }
                }
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
    public void reset(){
        Log.d(TAG,"sales_list############"+sales_list.size());
        sales_list.clear();
        refreshList();
        Log.d(TAG,"refreshList############"+sales_list.size());
        endlessRecyclerOnScrollListenerLinearView.reset(0, true);
        last_page=0;
    }
    private void getMyLists(Context context){
        showProgressBar();
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getMySalesList(context,Double.toString(locServices.getLatitude()),Double.toString(locServices.getLongitude()) ,new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                hideProgressBar();
                Log.d(TAG, "onSuccess Response");
                salesListModel = (SalesListModelNew)arg0;
                String str = salesListModel.toString();
                Log.d(TAG, str);
                setValues(salesListModel);
            }
            @Override
            public void OnFailureFromServer(String msg) {
                hideProgressBar();
                sales_list.clear();
                refreshList();
                no_sales.setVisibility(View.VISIBLE);
                no_sales.setText("No Sales found near you");
                Log.d(TAG, msg);
            }

            @Override
            public void OnError(String msg) {
                hideProgressBar();
                sales_list.clear();
                refreshList();
                no_sales.setVisibility(View.VISIBLE);
                no_sales.setText("No Sales found near you");
                Log.d(TAG, msg);
            }
        }, "CreateSaleActivity Sales");

    }
    public void showProgressBar(){
        if(progress_bar!=null)
            progress_bar.setVisibility(View.VISIBLE);
    }
    public void hideProgressBar(){
        if(progress_bar!=null)
            progress_bar.setVisibility(View.GONE);
    }
}

