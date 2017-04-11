package com.langoor.app.blueshak.currency;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.langoor.app.blueshak.category.CategoryListAdapter;
import com.langoor.app.blueshak.category.OnSelected;
import com.langoor.app.blueshak.filter.FilterActivity;
import com.langoor.app.blueshak.garage.CreateItemSaleFragment;
import com.langoor.app.blueshak.garage.CreateSaleActivity;
import com.langoor.app.blueshak.global.GlobalFunctions;
import com.langoor.app.blueshak.root.RootActivity;
import com.langoor.app.blueshak.services.model.CategoryListModel;
import com.langoor.app.blueshak.services.model.CategoryModel;
import com.langoor.app.blueshak.services.model.CreateSalesModel;
import com.langoor.app.blueshak.services.model.CurrencyListModel;
import com.langoor.app.blueshak.services.model.CurrencyModel;
import com.langoor.app.blueshak.services.model.ProductModel;
import com.langoor.blueshak.R;

import java.util.ArrayList;
import java.util.List;

public class CurrencyActivity extends RootActivity  implements OnSelected {
    private static final String TAG = "CategoryActivity";
    private static Context context;
    private TextView no_sales;
    private static Activity activity;
    public static final String SALE_DETAILS_KEY = "SaleDetailsKey";
    public static final String MY_ITEMS_ITEMS_AVAILABLE = "MyItemsAvailableBundleKey";
    public static final String CURRENCY = "currency";
    /*private SwipeRefreshLayout swipeRefreshLayout;*/
    private RecyclerView recyclerView;
    private List<CurrencyModel> product_list = new ArrayList<CurrencyModel>();
    private CurrencyListAdapter adapter;
    private TextView close_button;
    private ImageView go_back;
    private CurrencyListModel clm;
    private  String category=null;
    public static Intent newInstance(Context context, String category){
        Intent mIntent = new Intent(context, CurrencyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(CURRENCY, category);
        mIntent.putExtras(bundle);
        return mIntent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);
        try{
            context= this;
            activity= this;

            if(getIntent().hasExtra(CURRENCY))
               category=getIntent().getStringExtra(CURRENCY);
           Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
           setSupportActionBar(toolbar);
           setSupportActionBar(toolbar);
           LayoutInflater inflator = LayoutInflater.from(this);
           View v = inflator.inflate(R.layout.action_bar_titlel, null);
           ((TextView)v.findViewById(R.id.title)).setText("Currencies");
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
           no_sales = (TextView) findViewById(R.id.no_sales);

            if(clm==null) {
               clm = GlobalFunctions.getCurrencies(activity);
               if (clm != null) {
                   product_list=clm.getCurrency_list();
               }
           }
           recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
           if(category!=null&&!TextUtils.isEmpty(category)){
               for(int i=0;i<product_list.size();i++){
                   if(category!=null&&category.equalsIgnoreCase(product_list.get(i).getCurrency()))
                       product_list.get(i).setIs_selected(true);

               }
           }
           adapter = new CurrencyListAdapter(context,product_list,this);
           LinearLayoutManager linearLayoutManagerVertical =
                   new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
           recyclerView.setLayoutManager(linearLayoutManagerVertical);
       /* recyclerView.addItemDecoration(new SpacesItemDecoration(
                getResources().getDimensionPixelSize(R.dimen.space)));*/
           recyclerView.setItemAnimator(new DefaultItemAnimator());
           recyclerView.setAdapter(adapter);
           recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
       }catch (Exception e){
            Log.d(TAG,e.getMessage());
            e.printStackTrace();
            Crashlytics.log(e.getMessage());
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        clm = GlobalFunctions.getCurrencies(activity);
                    }
                });
            }
        }).start();


    }
    private void setReturnResult(CurrencyModel categoryModel){
        if(categoryModel!=null){
            Bundle bundle = new Bundle();
            Intent result;
            bundle.putSerializable(CreateItemSaleFragment.CREATE_ITEM_CURRENCY_BUNDLE_KEY,categoryModel);
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
        Log.d(TAG,"onSelected###############"+product_list.get(position).getCurrency());
        setReturnResult(product_list.get(position));

    }
}
