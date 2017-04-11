package com.langoor.app.blueshak.search;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.langoor.blueshak.R;
import com.langoor.app.blueshak.MainActivity;
import com.langoor.app.blueshak.PickLocation;
import com.langoor.app.blueshak.bookmarks.BoomarkItemListAdapter;
import com.langoor.app.blueshak.filter.FilterActivity;
import com.langoor.app.blueshak.global.GlobalFunctions;
import com.langoor.app.blueshak.global.GlobalVariables;
import com.langoor.app.blueshak.home.EndlessRecyclerOnScrollListener;
import com.langoor.app.blueshak.home.EndlessRecyclerOnScrollListenerLinearView;
import com.langoor.app.blueshak.home.ItemListAdapter;
import com.langoor.app.blueshak.item.ProductDetail;
import com.langoor.app.blueshak.root.RootActivity;
import com.langoor.app.blueshak.services.ServerResponseInterface;
import com.langoor.app.blueshak.services.ServicesMethodsManager;
import com.langoor.app.blueshak.services.model.ErrorModel;
import com.langoor.app.blueshak.services.model.FilterModel;
import com.langoor.app.blueshak.services.model.HomeListModel;
import com.langoor.app.blueshak.services.model.ProductModel;
import com.langoor.app.blueshak.services.model.SearchListModel;
import com.langoor.app.blueshak.services.model.SearchModel;
import com.langoor.app.blueshak.services.model.StatusModel;
import com.langoor.app.blueshak.util.LocationListener;
import com.langoor.app.blueshak.util.LocationService;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends RootActivity implements LocationListener,SwipeRefreshLayout.OnRefreshListener{
    public static final String TAG = "SearchActivity";
    private SearchListAdapter adapter;
    private HomeListModel searchListModel=new HomeListModel();
    private ArrayList<ProductModel> product_list = new ArrayList<ProductModel>();
    private LocationService locServices;
    private static  Context context;
    private static Activity activity;
    private EditText searchViewResult;
    private TextView no_search,results_all,cancel;
    private String lng;
    private String lat;
    private Toolbar toolbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private String search_query=null;
    private int last_page=1;
    private boolean hide_keyBoard=false;
    public static final String MAIN_ACTIVITY_FILTER_MODEL_SERIALIZE="filterModelSerialize";
    private GlobalVariables globalVariables=new GlobalVariables();
    private FilterModel model=null;
    private SearchModel searchModel=new SearchModel();
    private LinearLayout header_content;
    private ImageView go_to_filter,go_back;
    private ProgressBar progress_bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        try{
            context=this;
            activity=this;
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            locServices = new LocationService(this);
            locServices.setListener(this);
            no_search=(TextView)findViewById(R.id.no_search);
            swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_container);
            swipeRefreshLayout.setOnRefreshListener(this);
         /*   final SwipeRefreshLayout.OnRefreshListener swipeRefreshListner = new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    Log.i(TAG, "onRefresh called from SwipeRefreshLayout");
                   *//* swipeRefreshLayout.setRefreshing(true);*//*
                    setThisPage();
                }
            };*/
            /**
             * Showing Swipe Refresh animation on activity create
             * As animation won't start on onCreate, post runnable is used
             */
            progress_bar=(ProgressBar)findViewById(R.id.progress_bar);
            swipeRefreshLayout.setColorSchemeColors(context.getResources().getColor(R.color.brandColor),
                    context.getResources().getColor(R.color.tab_selected),
                    context.getResources().getColor(R.color.darkorange),
                    context.getResources().getColor(R.color.brandColor));
           /* swipeRefreshLayout.setRefreshing(true);*/
          /*  searchViewResult=(EditText) findViewById(R.id.searchViewResult);*/
            header_content=(LinearLayout)findViewById(R.id.header_content);
            header_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addFilter();
                }
            });
           /* go_to_filter=(ImageView)findViewById(R.id.go_to_filter);*/


            results_all=(TextView)findViewById(R.id.results_all);
            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            adapter = new SearchListAdapter(context, product_list,true);
           /* StaggeredGridLayoutManager gridLayoutManager =
                    new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);*/
            LinearLayoutManager linearLayoutManagerVertical =
                    new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManagerVertical);
            /*recyclerView.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.space)));*/

             /* recyclerView.addItemDecoration(new SpacesItemDecoration(
                    getResources().getDimensionPixelSize(R.dimen.space)));*/
            recyclerView.addItemDecoration(new SimpleDividerItemDecoration(activity));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListenerLinearView(
                    linearLayoutManagerVertical) {
                @Override
                public void onLoadMore(int current_page) {
                    Log.d(TAG,"onLoadMore##############");
                    if(!(current_page>last_page)){
                        adapter.showLoading(true);
                        Log.d(TAG,"current_page##############"+current_page);
                        /*model.setPage(current_page);*/
                        getDetails(context,searchViewResult.getText().toString().trim(),current_page);

                    }else
                        adapter.showLoading(false);
                }
            });
            recyclerView.setAdapter(adapter);
            recyclerView.setOnScrollListener(new MyScrollListener(activity) {
                @Override
                public void onMoved(int distance) {
                    toolbar.setTranslationY(-distance);
                }
            });
          /*  LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            View v = inflator.inflate(R.layout.actionbar_search, null);
            searchViewResult=(EditText)v.findViewById(R.id.searchViewResult);
             v.setLayoutParams(layoutParams);*/
            LayoutInflater inflator = LayoutInflater.from(this);
            View v = inflator.inflate(R.layout.action_bar_titlel_search, null);
            ((TextView)v.findViewById(R.id.title)).setText("Search");
            toolbar.addView(v);

            go_back=(ImageView)findViewById(R.id.go_back);
            go_back.setVisibility(View.VISIBLE);
            go_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            go_to_filter=(ImageView)findViewById(R.id.go_to_filter);
            go_back.setVisibility(View.VISIBLE);
            go_to_filter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addFilter();
                }
            });
            searchViewResult=(EditText) findViewById(R.id.searchViewResult);
            searchViewResult.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
            searchViewResult.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        hide_keyBoard=true;
                        doSearch();
                        return true;
                    }
                    return false;
                }
            });
            searchViewResult.addTextChangedListener(new TextChangedListener<EditText>(searchViewResult) {
                @Override
                public void onTextChanged(EditText target, Editable s) {
                    if(target.getText().toString().length()>2){
                        if(hide_keyBoard)
                            hide_keyBoard=false;
                        doSearch();
                    }

                    //Do stuff
                }
            });


            GlobalFunctions.showKeyboard(activity);
           /* searchViewResult.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!locServices.canGetLocation()){
                        startActivity(new Intent(activity,PickLocation.class));
                    }else {
                        doSearch();
                    }
                }
            });*/

        /*    toolbar.addView(v);*/
            searchViewResult.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final int DRAWABLE_LEFT = 0;
                    final int DRAWABLE_TOP = 1;
                    final int DRAWABLE_RIGHT = 2;
                    final int DRAWABLE_BOTTOM = 3;

                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (event.getRawX() >= (searchViewResult.getRight() - searchViewResult.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            hide_keyBoard=true;
                            doSearch();
                            return true;
                        }
                    }
                    return false;
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

    }
    public void showProgress(){
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                        swipeRefreshLayout.setRefreshing(true);
                                    }
                                }
        );
    }
    @Override
    public void onResume() {

        super.onResume();
    }
    public void doSearch() {
        final Location loc=locServices.getLocation();
        lat=Double.toString(loc.getLatitude());
        lng=Double.toString(loc.getLongitude());
        if(loc!=null){
            setDefaultFilter();
            if(GlobalFunctions.isNetworkAvailable(context)){
                search_query=searchViewResult.getText().toString().trim();
                if(!TextUtils.isEmpty(search_query)){
                    getDetails(context,searchViewResult.getText().toString().trim(),1);
                }/*else
                    Toast.makeText(context,"Please enter product name",Toast.LENGTH_LONG).show();*/
            }else {
                Snackbar.make(searchViewResult, "Please check your internet connection", Snackbar.LENGTH_LONG)
                        .setAction("Retry", new View.OnClickListener() {
                            @Override
                            @TargetApi(Build.VERSION_CODES.M)
                            public void onClick(View v) {
                                getDetails(context,searchViewResult.getText().toString(),1);
                            }
                        }).show();
            }


        }
    }
    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        /*if(!GlobalFunctions.isNetworkAvailable(context)){
            Snackbar.make(no_search, "Please check your internet connection", Snackbar.LENGTH_LONG)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {

                        }
                    }).show();
        }else{
            last_page=1;
            setThisPage();
        }*/



    }

    private void setValues(HomeListModel model){
        if(model!=null){
            searchListModel = model;
            last_page=model.getLast_page();
            List<ProductModel> productModels = searchListModel.getItem_list();
            if(productModels!=null){
                if(productModels.size()>0&&recyclerView!=null&&product_list!=null&&adapter!=null){
                    no_search.setVisibility(View.GONE);
                    results_all.setVisibility(View.VISIBLE);
                   /* product_list.clear();*/
                    product_list.addAll(productModels);
                    synchronized (adapter){adapter.notifyDataSetChanged();}
                }
            }
        }

    }

    public void setThisPage(){ swipeRefreshLayout.setRefreshing(true);
        getDetails(context,search_query,1);
    }

    private void getDetails(final Context context,String searchText,final int page){
        searchModel.setSearch(searchText);
        searchModel.setPage(page);
      showProgressBar();
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getSearchDetails(context,searchModel,new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                hideProgressBar();
                swipeRefreshLayout.setRefreshing(false);
                if(arg0 instanceof StatusModel){
                    if(hide_keyBoard)
                        GlobalFunctions.closeKeyboard(activity);
                    no_search.setVisibility(View.VISIBLE);
                    no_search.setText("No items found for your search");
                    StatusModel statusModel = (StatusModel) arg0;
                    refreshView();
                    /*Toast.makeText(context, "No items found your search", Toast.LENGTH_SHORT).show();*/
                }else if(arg0 instanceof ErrorModel){
                    if(hide_keyBoard)
                        GlobalFunctions.closeKeyboard(activity);
                    no_search.setVisibility(View.VISIBLE);
                    no_search.setText("No items found your search");
                    ErrorModel errorModel = (ErrorModel) arg0;
                    refreshView();
                    String msg = errorModel.getError()!=null ? errorModel.getError() : errorModel.getMessage();
                 /*   Toast.makeText(context, "No items found for your search", Toast.LENGTH_SHORT).show();*/
                }else if(arg0 instanceof HomeListModel){
                    if(hide_keyBoard)
                        GlobalFunctions.closeKeyboard(activity);
                    if(page==1&&product_list!=null)
                        product_list.clear();
                    HomeListModel model = (HomeListModel) arg0;
                    setValues(model);
                }
            }

            @Override
            public void OnFailureFromServer(String msg) {
                hideProgressBar();
                if(hide_keyBoard)
                    GlobalFunctions.closeKeyboard(activity);
                swipeRefreshLayout.setRefreshing(false);
                no_search.setVisibility(View.VISIBLE);
                refreshView();
                /*Toast.makeText(context, "No items found for your search", Toast.LENGTH_SHORT).show();*/
            }

            @Override
            public void OnError(String msg) {
                hideProgressBar();
                swipeRefreshLayout.setRefreshing(false);
                if(hide_keyBoard)
                    GlobalFunctions.closeKeyboard(activity);
                refreshView();
               /* Toast.makeText(context, "No items found for your search", Toast.LENGTH_SHORT).show();*/
                no_search.setVisibility(View.VISIBLE);

            }
        }, "Get Search List");

    }
    @Override
    public void onLocationChanged(Location loc) {
        if(loc!=null){
            lat=loc.getLatitude()+"";
            lng=loc.getLongitude()+"";
            locServices.removeListener();
        }else{
            Toast.makeText(this, "Fetching Location", Toast.LENGTH_SHORT).show();
        }
        GlobalFunctions.closeKeyboard(this);
    }
    @Override
    public void onProviderEnabled(String provider) {
    }
    public void refreshView(){

        if(product_list!=null)
            product_list.clear();
        synchronized (adapter){adapter.notifyDataSetChanged();}
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
    public abstract class TextChangedListener<T> implements TextWatcher {
        private T target;

        public TextChangedListener(T target) {
            this.target = target;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            this.onTextChanged(target, s);
        }

        public abstract void onTextChanged(T target, Editable s);
    }
    public void addFilter(){
        Intent filter_intent= FilterActivity.newInstance(context,null,GlobalVariables.TYPE_SEARCH);
        startActivityForResult(filter_intent,globalVariables.REQUEST_CODE_FILTER_MODEL_LOCATION);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG,"on activity result");
        if(resultCode == activity.RESULT_OK){
            Log.i(TAG,"result ok ");
            Log.i(TAG,"request code "+globalVariables.REQUEST_CODE_FILTER_MODEL_LOCATION);
            if(data.hasExtra(MAIN_ACTIVITY_FILTER_MODEL_SERIALIZE)){
                model=(FilterModel) data.getExtras().getSerializable(MAIN_ACTIVITY_FILTER_MODEL_SERIALIZE);
                if(model!=null){
                    String item_address=model.getLocation();
                    String category_names=model.getCategory_names();
                   /* if(!TextUtils.isEmpty(category_names)&& category_names!=null)
                        results_all.setText("Results in "+"'"+model.getCategory_names()+"'");
                    else
                        results_all.setText("Results in "+"'"+"All"+"'");
                */
                    results_all.setVisibility(View.VISIBLE);
                    if(!TextUtils.isEmpty(model.getResults_text()))
                        results_all.setText("Results in "+model.getResults_text());
                    else
                        results_all.setText("Results from nearest first");

                    Log.i(TAG,"name pm"+model.getFormatted_address());
                    searchModel.setLongitude(model.getLongitude());
                    searchModel.setLatitude(model.getLatitude());
                    searchModel.setPriceRange(model.getPriceRange());
                    searchModel.setRange(model.getRange());
                    searchModel.setLocation(model.getLocation());
                    searchModel.setCategories(model.getCategories());
                    if(model.is_current_country()){
                        searchModel.setIs_current_country(true);
                        searchModel.setCurrent_country_code(model.getCurrent_country_code());
                    }
                    if(model.isPrice_l_2_h())
                        searchModel.setPrice_l_2_h(true);
                    else
                        searchModel.setPrice_l_2_h(false);
                    if(model.isPrice_h_2_l())
                        searchModel.setPrice_h_2_l(true);
                    else
                        searchModel.setPrice_h_2_l(false);
                    if(model.isSortByRecent())
                        searchModel.setSortByRecent(true);
                    else
                        searchModel.setSortByRecent(false);
                    if(model.isGarage_items())
                        searchModel.setGarage_items(true);
                    else
                        searchModel.setGarage_items(false);

                    if(search_query!=null&&!TextUtils.isEmpty(search_query))
                        getDetails(context,search_query,1);

                }
            }
        }
    }
    public void setDefaultFilter(){
        searchModel.setType(GlobalVariables.TYPE_SHOP);
        searchModel.setLatitude(lat);
        searchModel.setLongitude(lng);
        searchModel.setPage(1);
        searchModel.setRange(GlobalVariables.DISTANCE_SEARCH_MAX_VALUE);
        searchModel.setPriceRange(GlobalVariables.PRICE_MIN_VALUE+","+GlobalVariables.PRICE_MAX_VALUE);
    }

    public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
        private Drawable mDivider;

        public SimpleDividerItemDecoration(Context context) {
            mDivider = context.getResources().getDrawable(R.drawable.line_divider);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();

                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
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
}
