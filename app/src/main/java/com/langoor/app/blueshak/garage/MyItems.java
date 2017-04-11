package com.langoor.app.blueshak.garage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.langoor.blueshak.R;
import com.langoor.app.blueshak.AppController;
import com.langoor.app.blueshak.global.GlobalFunctions;
import com.langoor.app.blueshak.global.GlobalVariables;
import com.langoor.app.blueshak.home.EndlessRecyclerOnScrollListener;
import com.langoor.app.blueshak.home.ItemListAdapter;
import com.langoor.app.blueshak.home.ItemListFragment;
import com.langoor.app.blueshak.root.RootActivity;
import com.langoor.app.blueshak.services.ServerResponseInterface;
import com.langoor.app.blueshak.services.ServicesMethodsManager;
import com.langoor.app.blueshak.services.model.CreateProductModel;
import com.langoor.app.blueshak.services.model.CreateSalesModel;
import com.langoor.app.blueshak.services.model.ErrorModel;
import com.langoor.app.blueshak.services.model.HomeListModel;
import com.langoor.app.blueshak.services.model.MakeOfferModel;
import com.langoor.app.blueshak.services.model.MyItemListAvailableModel;
import com.langoor.app.blueshak.services.model.MyItemListModel;
import com.langoor.app.blueshak.services.model.ProductModel;
import com.langoor.app.blueshak.services.model.SalesListModel;
import com.langoor.app.blueshak.services.model.SalesModel;
import com.langoor.app.blueshak.services.model.StatusModel;
import com.langoor.app.blueshak.util.LocationListener;
import com.langoor.app.blueshak.util.LocationService;
import java.util.ArrayList;
import java.util.List;


public class MyItems extends RootActivity implements SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = "MyItems";
    private static Context context;
    private TextView no_sales,activity_title,cancel;
    private static Activity activity;
    public static final String MY_ITEMS_ITEMS_AVAILABLE = "MyItemsAvailableBundleKey";
    private RecyclerView recyclerView;
    private List<ProductModel> product_list = new ArrayList<ProductModel>();
    private MyItemListAdapter adapter;
    private Toolbar toolbar;
    private Button done;
    private ImageView go_back;
    private  boolean item_available=false;
    private ProgressBar progress_bar;
    public static Intent newInstance(Context context, boolean item_available){
        Intent mIntent = new Intent(context, MyItems.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(MY_ITEMS_ITEMS_AVAILABLE, item_available);
        mIntent.putExtras(bundle);
        return mIntent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_items);
        try{
            progress_bar=(ProgressBar)findViewById(R.id.progress_bar);
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            context= this;
            activity= this;
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            LayoutInflater inflator = LayoutInflater.from(this);
            View v = inflator.inflate(R.layout.action_bar_titlel, null);
            activity_title=(TextView)v.findViewById(R.id.title);
            activity_title.setText("Add Items");
            cancel=(TextView)v.findViewById(R.id.cancel);
            cancel.setText("New Item");
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveList();
                    Intent i=CreateSaleActivity.newInstance(activity,null,null,null,GlobalVariables.TYPE_HOME,GlobalVariables.TYPE_ITEM);
                    startActivity(i);
                }
            });
            go_back=(ImageView) v.findViewById(R.id.go_back);
            go_back.setVisibility(View.VISIBLE);
            go_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            toolbar.addView(v);
            if(getIntent().hasExtra(MY_ITEMS_ITEMS_AVAILABLE))
               item_available=getIntent().getBooleanExtra(MY_ITEMS_ITEMS_AVAILABLE,false);
            if(!item_available)
                callAddItem();
            no_sales = (TextView) findViewById(R.id.no_sales);
            done=(Button)findViewById(R.id.done);
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addItems();
                }
            });
            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            adapter = new MyItemListAdapter(context, product_list,true);
            LinearLayoutManager linearLayoutManagerVertical =
                    new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManagerVertical);
           /* recyclerView.addItemDecoration(new SpacesItemDecoration(
                    getResources().getDimensionPixelSize(R.dimen.space)));*/
            recyclerView.addItemDecoration(new SimpleDividerItemDecoration(activity));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
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
    public void onRefresh() {
        setThisPage();
    }

    private void setThisPage(){
        getMyItemList(context);
    }

    private void setValues(MyItemListModel model){
        String [] ids=new String[1000];
        if(model!=null){
            String data= GlobalFunctions.getSharedPreferenceString(context,GlobalVariables.SELECTED_ITEMS_LIST);
            if(!TextUtils.isEmpty(data)&&data.contains(",")){
                ids=data.split(",");
            }
            String str = "";
             MyItemListModel myItemListModel = model;
            List<ProductModel> productModels = myItemListModel.getItem_list();
            if(productModels!=null){
                if(productModels.size()>0){
                    product_list.clear();
                    for (ProductModel productModel:productModels){
                        if(data!=null && ids!=null){
                            for (int i=0;i<ids.length;i++){
                                if(ids[i]!=null)
                                    if(Integer.parseInt(productModel.getId())==Integer.parseInt(ids[i])){
                                        productModel.setIs_selected(true);
                                    }
                            }
                        }
                        product_list.add(productModel);
                    }
                    synchronized (adapter){adapter.notifyDataSetChanged();}

                    if(!item_available && product_list.size()>0){
                        product_list.get(0).setIs_selected(true);
                        String selected_item =product_list.get(0).getId()+",";
                        GlobalFunctions.setSharedPreferenceString(context,GlobalVariables.SELECTED_ITEMS_LIST,selected_item);
                        setReturnResult(product_list);
                    }

                }
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
            setThisPage();
    }

    public void saveList(){
        String data = "";
        List<ProductModel> stList = ((MyItemListAdapter) adapter).getProductList();
        List<ProductModel> selected_List =new ArrayList<ProductModel>();
        selected_List.clear();
        for (int i = 0; i < stList.size(); i++) {
            ProductModel productModel = stList.get(i);
            if (productModel.is_selected() == true) {
                /*data = data + "\n" + productModel.getName().toString();*/
                data=data+productModel.getId()+ ",";
                selected_List.add(productModel);
            }
        }

        GlobalFunctions.setSharedPreferenceString(context,GlobalVariables.SELECTED_ITEMS_LIST,data);
        Log.d(TAG,"####Selected ids#########"+data);
    }
    public void addItems(){
        String data = "";
        List<ProductModel> stList = ((MyItemListAdapter) adapter).getProductList();
        List<ProductModel> selected_List =new ArrayList<ProductModel>();
        selected_List.clear();
        for (int i = 0; i < stList.size(); i++) {
            ProductModel productModel = stList.get(i);
            if (productModel.is_selected() == true) {
                /*data = data + "\n" + productModel.getName().toString();*/
                data=data+productModel.getId()+ ",";
                selected_List.add(productModel);
            }
        }
     /*   GlobalFunctions.saveItemsList(context,GlobalVariables.SELECTED_ITEMS_LIST,stList);*/

        GlobalFunctions.setSharedPreferenceString(context,GlobalVariables.SELECTED_ITEMS_LIST,data);
        Log.d(TAG,"####Selected ids#########"+data);
        setReturnResult(selected_List);

    }

    private void setReturnResult(List<ProductModel> list_products){
        HomeListModel homeListModel=new HomeListModel();
        homeListModel.setItem_list(list_products);
        if(homeListModel!=null){
            Bundle bundle = new Bundle();
            bundle.putSerializable(CreateGarageSaleFragment.CREATE_GARRAGE_ACTIVITY_SELECT_ITEMS_KEY,homeListModel);
            Intent result = new Intent(activity,CreateSaleActivity.class);
            result.putExtras(bundle);
            setResult(this.RESULT_OK,result);
        }else{
            setResult(this.RESULT_CANCELED);
        }

        finish();
    }
    private void getMyItemList(final Context context){
        showProgressBar();
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getAvailableItemsList(context, new MyItemListModel(),new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                hideProgressBar();
                if(arg0 instanceof StatusModel){
                    StatusModel statusModel = (StatusModel) arg0;
                    no_sales.setVisibility(View.GONE);
                }else if(arg0 instanceof ErrorModel){
                    ErrorModel errorModel = (ErrorModel) arg0;
                    String msg = errorModel.getError()!=null ? errorModel.getError() : errorModel.getMessage();
                   /* Toast.makeText(context, msg, Toast.LENGTH_LONG).show();*/
                    no_sales.setVisibility(View.GONE);
                }else if(arg0 instanceof MyItemListModel){
                    MyItemListModel model = (MyItemListModel) arg0;
                    setValues(model);
                    done.setVisibility(View.VISIBLE);
                    no_sales.setVisibility(View.GONE);

                }
            }
            @Override
            public void OnFailureFromServer(String msg) {
                hideProgressBar();
               /* Toast.makeText(context, msg, Toast.LENGTH_LONG).show();*/
            }
            @Override
            public void OnError(String msg) {
                hideProgressBar();
                /*Toast.makeText(context, msg, Toast.LENGTH_LONG).show();*/
            }
        }, "GetMyItemList");
    }
    public void callAddItem(){
        Intent i=CreateSaleActivity.newInstance(activity,null,null,null,GlobalVariables.TYPE_HOME,GlobalVariables.TYPE_ITEM);
        startActivity(i);
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
