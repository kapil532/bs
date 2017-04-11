package com.langoor.app.blueshak.mylistings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.langoor.blueshak.R;
import com.langoor.app.blueshak.AppController;
import com.langoor.app.blueshak.MainActivity;
import com.langoor.app.blueshak.PickLocation;
import com.langoor.app.blueshak.filter.FilterActivity;
import com.langoor.app.blueshak.global.GlobalFunctions;
import com.langoor.app.blueshak.global.GlobalVariables;
import com.langoor.app.blueshak.home.EndlessRecyclerOnScrollListener;
import com.langoor.app.blueshak.home.GarageSalesListFragment;
import com.langoor.app.blueshak.home.ItemListAdapter;
import com.langoor.app.blueshak.search.SearchListAdapter;
import com.langoor.app.blueshak.services.ServerResponseInterface;
import com.langoor.app.blueshak.services.ServicesMethodsManager;
import com.langoor.app.blueshak.services.model.FilterModel;
import com.langoor.app.blueshak.services.model.HomeListModel;
import com.langoor.app.blueshak.services.model.LocationModel;
import com.langoor.app.blueshak.services.model.ProductModel;
import com.langoor.app.blueshak.services.model.SalesListModel;
import com.langoor.app.blueshak.services.model.SalesModel;
import com.langoor.app.blueshak.services.model.SimilarProductsModel;
import com.langoor.app.blueshak.util.LocationListener;
import com.langoor.app.blueshak.util.LocationService;
import java.util.ArrayList;
import java.util.List;


public class ItemMyListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    public static final String TAG = "ItemListFragment";
    private Context context;
    private TextView no_sales;
    private static  Activity activity;
    private SimilarProductsModel similarProductsModel=new SimilarProductsModel();
    private   SearchListAdapter adapter;
    private ArrayList<SalesModel> list = new ArrayList<SalesModel>();
    private ArrayList<ProductModel> product_list = new ArrayList<ProductModel>();
    private RecyclerView recyclerView;
    private String no_items_found;
    private SwipeRefreshLayout swipeRefreshLayout;
    /*LinearLayout header_content;*/
    private Handler handler=new Handler();
    private ProgressBar progress_bar;
    private TextView results_all,searchViewResult;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.sales_list_item_fragment, container, false);
        context= getActivity();
        activity= getActivity();
        progress_bar=(ProgressBar)view.findViewById(R.id.progress_bar);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);
        results_all=(TextView)view.findViewById(R.id.results_all);
        results_all.setVisibility(View.GONE);
        searchViewResult=(TextView)view.findViewById(R.id.searchViewResult);
        searchViewResult.setVisibility(View.GONE);
      /*  header_content=(LinearLayout)view.findViewById(R.id.header_content);*/
        no_sales = (TextView) view.findViewById(R.id.no_sales);
        no_sales.setText("Loading your products...");
     /*   header_content.setVisibility(View.GONE);*/
        no_items_found=context.getResources().getString(R.string.no_items_found);
        swipeRefreshLayout.setColorSchemeColors(context.getResources().getColor(R.color.brandColor),
                context.getResources().getColor(R.color.tab_selected),
                context.getResources().getColor(R.color.darkorange),
                context.getResources().getColor(R.color.brandColor));
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        adapter = new SearchListAdapter(context, product_list,false);
      /*  StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);*/
          /*  StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);*/
        LinearLayoutManager linearLayoutManagerVertical =
                new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManagerVertical);
        /*recyclerView.setLayoutManager(gridLayoutManager);*/
      /*  recyclerView.addItemDecoration(new SpacesItemDecoration(
                getResources().getDimensionPixelSize(R.dimen.space)));*/
         /* recyclerView.addItemDecoration(new SpacesItemDecoration(
                getResources().getDimensionPixelSize(R.dimen.space)));*/
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(activity));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
    }
    @Override
    public void onResume() {
        MainActivity.setTitle(getString(R.string.item),0);
        String user_id=GlobalFunctions.getSharedPreferenceString(context,GlobalVariables.SHARED_PREFERENCE_BS_ID);
        if(user_id!=null && !TextUtils.isEmpty(user_id))
            getSellerProducts(context,user_id);
        super.onResume();
    }
    private void setItemListValues(SimilarProductsModel model){
        if(model!=null){
            product_list.clear();
            String str = "";
            similarProductsModel = model;
            List<ProductModel> productModels = similarProductsModel.getProductsList();
            if(productModels!=null){
                if(productModels.size()>0&&recyclerView!=null&&list!=null&&adapter!=null){
                    no_sales.setVisibility(View.GONE);
                    product_list.addAll(productModels);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            synchronized (adapter){adapter.notifyDataSetChanged();}
                        }
                    });

                }else{
                    no_sales.setVisibility(View.VISIBLE);
                    no_sales.setText("No Items Found");
                }
            }
        }
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        String user_id=GlobalFunctions.getSharedPreferenceString(context,GlobalVariables.SHARED_PREFERENCE_BS_ID);
        if(user_id!=null && !TextUtils.isEmpty(user_id))
            getSellerProducts(context,user_id);
     /*   setThisPage();*/
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

    private void getSellerProducts(Context context,String user_id){
        showProgressBar();
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getSellerProducts(context,user_id,"",new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                hideProgressBar();
                swipeRefreshLayout.setRefreshing(false);
                Log.d(TAG, "getSellerProducts onSuccess Response"+arg0.toString());
                SimilarProductsModel similarProductsModel = (SimilarProductsModel)arg0;
                setItemListValues(similarProductsModel);
                Log.d(TAG, similarProductsModel.toString());
            }

            @Override
            public void OnFailureFromServer(String msg) {
                swipeRefreshLayout.setRefreshing(false);
                no_sales.setVisibility(View.VISIBLE);
                no_sales.setText("No Items Found");
                hideProgressBar();
                Log.d(TAG, msg);
            }

            @Override
            public void OnError(String msg) {
                swipeRefreshLayout.setRefreshing(false);
                no_sales.setVisibility(View.VISIBLE);
                no_sales.setText("No Items Found");

                hideProgressBar();
                Log.d(TAG, msg);
            }
        }, "Similar Products");

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

