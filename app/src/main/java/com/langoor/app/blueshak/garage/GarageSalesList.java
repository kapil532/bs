package com.langoor.app.blueshak.garage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.langoor.app.blueshak.category.CategoryListAdapter;
import com.langoor.app.blueshak.category.OnSelected;
import com.langoor.app.blueshak.filter.FilterActivity;
import com.langoor.app.blueshak.global.GlobalVariables;
import com.langoor.app.blueshak.root.RootActivity;
import com.langoor.app.blueshak.services.ServerResponseInterface;
import com.langoor.app.blueshak.services.ServicesMethodsManager;
import com.langoor.app.blueshak.services.model.CategoryListModel;
import com.langoor.app.blueshak.services.model.CategoryModel;
import com.langoor.app.blueshak.services.model.CreateSalesModel;
import com.langoor.app.blueshak.services.model.FilterModel;
import com.langoor.app.blueshak.services.model.ProductModel;
import com.langoor.app.blueshak.services.model.SalesListModelNew;
import com.langoor.app.blueshak.services.model.SalesModel;
import com.langoor.app.blueshak.util.LocationListener;
import com.langoor.app.blueshak.util.LocationService;
import com.langoor.blueshak.R;

import java.util.ArrayList;
import java.util.List;

public class GarageSalesList extends RootActivity implements OnSelected, LocationListener {
    private static final String TAG = "GarageSalesList";
    private static Context context;
    private TextView no_sales;
    private static Activity activity;
    public static final String CATEGORY = "category";
    private RecyclerView recyclerView;
    private List<SalesModel> sales_list = new ArrayList<SalesModel>();
    private SalesListAdapter adapter;
    private TextView close_button;
    private ImageView go_back;
    private Handler handler=new Handler();
    private LocationService locServices;
    private SalesListModelNew salesListModelNew=new SalesListModelNew();
    private ProgressBar progress_bar;
    public static Intent newInstance(Context context, String sale){
        Intent mIntent = new Intent(context, GarageSalesList.class);
        Bundle bundle = new Bundle();
        bundle.putString(CATEGORY, sale);
        mIntent.putExtras(bundle);
        return mIntent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage_sales_list);
        try{
            activity=this;
            context=this;
            progress_bar=(ProgressBar)findViewById(R.id.progress_bar);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            setSupportActionBar(toolbar);
            locServices = new LocationService(activity);
            locServices.setListener(this);
            LayoutInflater inflator = LayoutInflater.from(this);
            View v = inflator.inflate(R.layout.action_bar_titlel, null);
            ((TextView)v.findViewById(R.id.title)).setText("Select a Sale");
            toolbar.addView(v);
            close_button=(TextView)v.findViewById(R.id.cancel);
            close_button.setText("Done");
            close_button.setVisibility(View.GONE);
            go_back=(ImageView)findViewById(R.id.go_back);
            go_back.setVisibility(View.VISIBLE);
            go_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            no_sales=(TextView)findViewById(R.id.no_sales);
            adapter = new SalesListAdapter(context,sales_list,this);
            LinearLayoutManager linearLayoutManagerVertical =
                    new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManagerVertical);
       /* recyclerView.addItemDecoration(new SpacesItemDecoration(
                getResources().getDimensionPixelSize(R.dimen.space)));*/
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
            recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
            getMyLists(context);
        }catch (Exception e){
            e.printStackTrace();
            Crashlytics.log(e.getMessage());
            Log.d(TAG,e.getMessage());
        }

    }
    private void setReturnResult(SalesModel salesModel){
        if(salesModel!=null){
            Bundle bundle = new Bundle();
            Intent result;
            bundle.putSerializable(CreateItemSaleFragment.CREATE_ITEM_SALE_BUNDLE_KEY,salesModel);
            result = new Intent(activity,CreateSaleActivity.class);
            result.putExtras(bundle);
            setResult(this.RESULT_OK,result);
        }else{
            setResult(this.RESULT_CANCELED);
        }

        finish();
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
    @Override
    public void onSelected(int position) {
        Log.d(TAG,"onSelected###############"+sales_list.get(position).getName());
        setReturnResult(sales_list.get(position));

    }
    private void getMyLists(Context context){
        showProgressBar();
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getMySalesList(context,Double.toString(locServices.getLatitude()),Double.toString(locServices.getLongitude()) ,new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                hideProgressBar();
                Log.d(TAG, "onSuccess Response");
                salesListModelNew = (SalesListModelNew)arg0;
                String str = salesListModelNew.toString();
                Log.d(TAG, str);
                setValues(salesListModelNew);
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
    @Override
    public void onLocationChanged(Location location) {

    }
    @Override
    public void onProviderEnabled(String provider) {

    }
    private void setValues(SalesListModelNew model){
        if(model!=null){
            String str = "";
            salesListModelNew = model;
            List<SalesModel> productModels = salesListModelNew.getSalesList();
            if(productModels!=null){
                if(productModels.size()>0&&recyclerView!=null&&sales_list!=null&&adapter!=null){
                    no_sales.setVisibility(View.GONE);
                    sales_list.addAll(productModels);
                    refreshList();
                } else{
                    no_sales.setVisibility(View.VISIBLE);
                    no_sales.setText("No Sales found");
                }
            } else{
                no_sales.setVisibility(View.VISIBLE);
                no_sales.setText("No Sales found");

            }
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
    public void showProgressBar(){
        if(progress_bar!=null)
            progress_bar.setVisibility(View.VISIBLE);
    }
    public void hideProgressBar(){
        if(progress_bar!=null)
            progress_bar.setVisibility(View.GONE);
    }
}
