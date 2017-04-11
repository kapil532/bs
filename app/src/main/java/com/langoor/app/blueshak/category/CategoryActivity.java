package com.langoor.app.blueshak.category;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.langoor.blueshak.R;
import com.langoor.app.blueshak.bookmarks.OnBookMarksDeleted;
import com.langoor.app.blueshak.filter.FilterActivity;
import com.langoor.app.blueshak.garage.CreateGarageSaleFragment;
import com.langoor.app.blueshak.garage.CreateItemSaleFragment;
import com.langoor.app.blueshak.garage.CreateSaleActivity;
import com.langoor.app.blueshak.garage.MyItems;
import com.langoor.app.blueshak.global.GlobalFunctions;
import com.langoor.app.blueshak.global.GlobalVariables;
import com.langoor.app.blueshak.home.ItemListFragment;
import com.langoor.app.blueshak.root.RootActivity;
import com.langoor.app.blueshak.services.model.CategoryListModel;
import com.langoor.app.blueshak.services.model.CategoryModel;
import com.langoor.app.blueshak.services.model.CreateSalesModel;
import com.langoor.app.blueshak.services.model.HomeListModel;
import com.langoor.app.blueshak.services.model.ProductModel;
import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends RootActivity implements OnSelected {
    private static final String TAG = "CategoryActivity";
    private static Context context;
    private TextView no_sales;
    private static Activity activity;
    public static final String MY_ITEMS_ITEMS_AVAILABLE = "MyItemsAvailableBundleKey";
    public static final String CATEGORY = "category";
    private RecyclerView recyclerView;
    private List<CategoryModel> product_list = new ArrayList<CategoryModel>();
    private CategoryListAdapter adapter;
    private Button done;
    private TextView close_button;
    private ImageView go_back;
    private CategoryListModel clm;
    private boolean is_multiple_selection=false;
    private  String category=null;
    public static Intent newInstance(Context context, boolean is_multiple_selection,String category){
        Intent mIntent = new Intent(context, CategoryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(MY_ITEMS_ITEMS_AVAILABLE, is_multiple_selection);
        bundle.putString(CATEGORY, category);
        mIntent.putExtras(bundle);
        return mIntent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        try{
            context= this;
            activity= this;
            if(getIntent().hasExtra(MY_ITEMS_ITEMS_AVAILABLE))
                is_multiple_selection=getIntent().getBooleanExtra(MY_ITEMS_ITEMS_AVAILABLE,false);
            if(getIntent().hasExtra(CATEGORY))
                category=getIntent().getStringExtra(CATEGORY);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            setSupportActionBar(toolbar);
            LayoutInflater inflator = LayoutInflater.from(this);
            View v = inflator.inflate(R.layout.action_bar_titlel, null);
            ((TextView)v.findViewById(R.id.title)).setText("Category");
            toolbar.addView(v);
            close_button=(TextView)v.findViewById(R.id.cancel);
            close_button.setText("Done");
            if(!is_multiple_selection)
                close_button.setVisibility(View.GONE);
            close_button.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    addItems();
                                                }
                                            }
            );
            go_back=(ImageView)findViewById(R.id.go_back);
            go_back.setVisibility(View.VISIBLE);
            go_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            no_sales = (TextView) findViewById(R.id.no_sales);
            done=(Button)findViewById(R.id.done);
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addItems();
                }
            });

            if(clm==null) {
                clm = GlobalFunctions.getCategories(activity);
                if (clm != null) {

                    product_list=clm.getCategoryList();
                }
            }
            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            if(category!=null&&!TextUtils.isEmpty(category)){
                for(int i=0;i<product_list.size();i++){
                    if(category!=null&&category.equalsIgnoreCase(product_list.get(i).getName()))
                        product_list.get(i).setIs_selected(true);

                }
            }
            adapter = new CategoryListAdapter(context,product_list,is_multiple_selection,this);
            LinearLayoutManager linearLayoutManagerVertical =
                    new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManagerVertical);
       /* recyclerView.addItemDecoration(new SpacesItemDecoration(
                getResources().getDimensionPixelSize(R.dimen.space)));*/
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
            recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));

        }catch (Exception e){
            e.printStackTrace();
            Crashlytics.log(e.getMessage());
            Log.d(TAG,e.getMessage());
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
                        clm = GlobalFunctions.getCategories(activity);
                    }
                });
            }
        }).start();


    }
    public void addItems(){
        String data = "";
        List<CategoryModel> stList = ((CategoryListAdapter) adapter).getProductList();
        List<CategoryModel> selected_List =new ArrayList<CategoryModel>();
        selected_List.clear();

        for (int i = 0; i < stList.size(); i++) {
            CategoryModel productModel = stList.get(i);
            if (productModel.is_selected() == true) {
                /*data = data + "\n" + productModel.getName().toString();*/
                data=data+productModel.getId()+ ",";
                selected_List.add(productModel);
            }
        }
        CategoryModel categoryModel=new CategoryModel();
        categoryModel.setSelectedCategoryString(selected_List);
        Log.d(TAG,"####Selected ids#########"+data);
        setReturnResult(categoryModel);

    }

    private void setReturnResult(CategoryModel categoryModel){
        if(categoryModel!=null){
            Bundle bundle = new Bundle();
            Intent result;
            if(is_multiple_selection){
                bundle.putSerializable(FilterActivity.CREATE_ITEM_CATEGORY_BUNDLE_KEY,categoryModel);
                result = new Intent(activity,FilterActivity.class);
            }else {
                bundle.putSerializable(CreateItemSaleFragment.CREATE_ITEM_CATEGORY_BUNDLE_KEY,categoryModel);
                result = new Intent(activity,CreateSaleActivity.class);
            }
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
        Log.d(TAG,"onSelected###############"+product_list.get(position).getName());
        setReturnResult(product_list.get(position));

    }
}
