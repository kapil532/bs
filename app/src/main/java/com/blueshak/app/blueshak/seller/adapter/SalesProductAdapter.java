package com.blueshak.app.blueshak.seller.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blueshak.app.blueshak.Messaging.activity.ChatActivity;
import com.blueshak.app.blueshak.Messaging.data.User;
import com.blueshak.app.blueshak.garage.CreateSaleActivity;
import com.blueshak.app.blueshak.global.GlobalFunctions;
import com.blueshak.app.blueshak.global.GlobalVariables;
import com.blueshak.app.blueshak.item.MakeOffersActivty;
import com.blueshak.app.blueshak.login.LoginActivity;
import com.blueshak.app.blueshak.search.SearchListAdapter;
import com.blueshak.app.blueshak.seller.model.SalesDetailsModel;
import com.blueshak.app.blueshak.seller.model.SalesDetailsProduct;
import com.blueshak.app.blueshak.services.ServerResponseInterface;
import com.blueshak.app.blueshak.services.ServicesMethodsManager;
import com.blueshak.app.blueshak.services.model.ConversationIdModel;
import com.blueshak.app.blueshak.services.model.CreateSalesModel;
import com.blueshak.app.blueshak.services.model.ErrorModel;
import com.blueshak.app.blueshak.services.model.StatusModel;
import com.blueshak.app.blueshak.util.BlueShakLog;
import com.blueshak.app.blueshak.util.BlueShakSharedPreferences;
import com.blueshak.app.blueshak.view.AlertDialog;
import com.blueshak.blueshak.R;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by lal on 26/05/18.
 */

public class SalesProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
        ViewPagerEx.OnPageChangeListener {

    private static String TAG = "SalesProductAdapter";
    private Context context;
    private List<SalesDetailsProduct> salesProductList;
    protected boolean showLoader;
    private static final int VIEWTYPE_ITEM = 1;
    private static final int VIEWTYPE_LOADER = 2;;
    private SalesDetailsModel salesProductModel;
    private ConversationIdModel conversationIdModel = new ConversationIdModel();
    private User user = new User();


    public SalesProductAdapter(Context mContext, SalesDetailsModel salesProductModel) {
        this.context = mContext;
        salesProductList = salesProductModel.getProducts();
        this.salesProductModel = salesProductModel;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_item_no;
        private TextView txt_items_name;
        private TextView txt_items_price;
        private ImageView image_bookmark;
        private ImageView img_sales_product;
        private Button btn_message;
        private Button btn_make_offer;
        private Button btn_edit;
        private ProgressBar img_progressbar;

        public MyViewHolder(View view) {
            super(view);

            txt_item_no = (TextView) view.findViewById(R.id.txt_item_no);
            txt_items_name = (TextView) view.findViewById(R.id.txt_items_name);
            txt_items_price = (TextView) view.findViewById(R.id.txt_items_price);
            image_bookmark = (ImageView) view.findViewById(R.id.image_bookmark);
            img_sales_product = (ImageView) view.findViewById(R.id.img_sales_product);
            btn_message = (Button) view.findViewById(R.id.btn_message);
            btn_make_offer = (Button) view.findViewById(R.id.btn_make_offer);
            btn_edit = (Button) view.findViewById(R.id.btn_edit);
            img_progressbar = (ProgressBar) view.findViewById(R.id.img_progressbar);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sales_details_product_items, parent, false);
        return new SalesProductAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder view_holder, int position) {

        try {
            if (view_holder instanceof SalesProductAdapter.MyViewHolder) {

                final MyViewHolder holder = (MyViewHolder) view_holder;
                final SalesDetailsProduct detailsProduct = salesProductList.get(position);
                int iCount = position + 1;

                holder.txt_item_no.setText("Item " + iCount + ": ");
                holder.txt_items_name.setText(detailsProduct.getProductName());

                if (detailsProduct.getSalePrice() != null) {
                    holder.txt_items_price.setVisibility(View.VISIBLE);
                    holder.txt_items_price.setText(" - " + detailsProduct.getCurrency() + " " + detailsProduct.getSalePrice());
                } else {
                    holder.txt_items_price.setVisibility(View.GONE);
                }

                if (detailsProduct.getIsBookmarked() == 1) {
                    holder.image_bookmark.setBackgroundResource(R.drawable.like_full);
                } else {
                    holder.image_bookmark.setBackgroundResource(R.drawable.like_border);
                }

                if (detailsProduct.getImage() != null) {
                    BlueShakLog.logDebug(TAG, "setBitmapDecodedImage  iCount " + iCount);
                    setBitmapImages(holder, detailsProduct.getImage());
                }

                onMakeOffer(holder, detailsProduct);
                onMessage(holder, detailsProduct);
                onBookmark(holder, detailsProduct);

            } else if (view_holder instanceof VHLoader) {
                VHLoader loaderViewHolder = (VHLoader) view_holder;
                if (showLoader) {
                    loaderViewHolder.progressBar.setVisibility(View.VISIBLE);
                } else {
                    loaderViewHolder.progressBar.setVisibility(View.GONE);
                }
                return;
            }
        } catch (Resources.NotFoundException e) {
            BlueShakLog.logDebug(TAG, "Error " + e.getLocalizedMessage());
        } catch (Exception e) {
            BlueShakLog.logDebug(TAG, "Error " + e.getLocalizedMessage());
        }
    }

    class VHLoader extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public VHLoader(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.bottom_progress_bar);
        }
    }

    private void onMakeOffer(MyViewHolder holder,final SalesDetailsProduct product) {
        holder.btn_make_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_loggedIn()) {
                    if (!isSelfUser()) {
                        Intent intent = MakeOffersActivty.newInstance(context, product);
                        context.startActivity(intent);
                    } else {
                        show_alert(product.getSaleId());
                    }
                } else
                    showSettingsAlert();
            }
        });
    }

    private void onMessage(MyViewHolder holder,final SalesDetailsProduct product) {
        holder.btn_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_loggedIn()) {
                    if (!isSelfUser()) {
                        if (salesProductModel.getSellerName() != null || salesProductModel.getSellerId() != null) {
                            check_conversation_exists(product);
                        } else {
                            Toast.makeText(context, "Sorry can't send message", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        //show_edit_sale_pop_up(v);
                    }
                } else{
                    showSettingsAlert();
                }

            }
        });
    }

    public boolean is_loggedIn() {
        String token = GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
        if (token != null) {
            return true;
        } else {
            return false;
        }

    }

    private boolean isSelfUser(){
        boolean isSelf = false;
        String signed_user_user_id = GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_USERID);
        if (signed_user_user_id != null && salesProductModel.getSellerId() != null) {
            if (Integer.parseInt(salesProductModel.getSellerId()) == Integer.parseInt(signed_user_user_id)) {
                isSelf = true;
            } else {
                isSelf = false;
            }
        }
        return isSelf;

    }

    /*public void show_edit_sale_pop_up(View v) {
        // Open calender Image
        if (context != null) {
            final PopupMenu popup = new PopupMenu(context, v);
            popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getTitle().toString().equalsIgnoreCase(context.getResources().getString(R.string.edit_Sale))) {
                        CreateSalesModel createSalesModel = new CreateSalesModel();
                        createSalesModel.toObject(salesModel, GlobalFunctions.getCategories(context));
                       *//* closeThisActivity();*//*
                        Intent i = CreateSaleActivity.newInstance(context, createSalesModel, null, null, 0, GlobalVariables.TYPE_GARAGE);
                        context.startActivity(i);
                    } else if (item.getTitle().toString().equalsIgnoreCase(context.getResources().getString(R.string.end_sale))) {
                        endSale(context, salesModel.getId());
                    } else if (item.getTitle().toString().equalsIgnoreCase(context.getResources().getString(R.string.delete_sale))) {
                        if (GlobalFunctions.is_loggedIn(context)) {
                            final AlertDialog dialog = new AlertDialog(context);
                            dialog.setTitle("Delete sale");
                            dialog.setIcon(R.drawable.ic_warning_black_24dp);
                            dialog.setIsCancelable(true);
                            dialog.setMessage("Do you really want to delete this sale?");
                            dialog.setPositiveButton("Yes", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    deleteSale(context, salesModel.getId());
                                    dialog.dismiss();
                                }
                            });

                            dialog.setNegativeButton("Cancel", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }
                    } else if (item.getTitle().toString().equalsIgnoreCase(context.getResources().getString(R.string.reschedule_sale))) {
                        final AlertDialog dialog = new AlertDialog(context);
                        dialog.setTitle("Reschedule Sale");
                        dialog.setIcon(R.drawable.ic_alert);
                        dialog.setIsCancelable(true);
                        dialog.setMessage("Do you really want to reschedule this Sale?");
                        dialog.setPositiveButton("Yes", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int[] dateInts = GlobalFunctions.convertTexttoDate(salesModel.getStart_date());
                                setDateTimeField(dateInts[2], dateInts[1], dateInts[0]);
                                dialog.dismiss();
                            }
                        });

                        dialog.setNegativeButton("Cancel", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                    return true;
                }
            });
            if (popup != null)
                popup.show();
        }

    }
    */

    public void show_alert(final String sale_id) {
        final AlertDialog dialog = new AlertDialog(context);
        dialog.setTitle("Delete Sale");
        dialog.setIcon(R.drawable.ic_warning_black_24dp);
        dialog.setIsCancelable(true);
        dialog.setMessage("Are you sure Delete this Sale?");
        dialog.setPositiveButton("Yes", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSale(context, sale_id);
                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("Cancel", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void deleteSale(final Context context, String saleID) {
        //showProgressBar();
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.deleteSale(context, saleID, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
               // hideProgressBar();
                Log.d(TAG, "onSuccess Response");
                Toast.makeText(context, "Sale has been deleted Successfully", Toast.LENGTH_LONG).show();
               // closeThisActivity();
            }

            @Override
            public void OnFailureFromServer(String msg) {
               // hideProgressBar();
                Log.d(TAG, msg);
            }

            @Override
            public void OnError(String msg) {
               // hideProgressBar();
                Log.d(TAG, msg);
            }
        }, "Delete Sale");
    }


    private void onBookmark(final MyViewHolder holder, final SalesDetailsProduct product) {
        holder.image_bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalFunctions.is_loggedIn(context)) {
                    if (product.getIsBookmarked() == 1) {
                        deleteBookmark(context, holder, product);
                    } else {
                        addBookmark(context, holder, product);
                    }
                } else {
                    showSettingsAlert();
                }
            }
        });
    }


    private void addBookmark(final Context context, final MyViewHolder holder, final SalesDetailsProduct product) {

        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.addBookmark(context, product.getProductId(), new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                if (arg0 instanceof StatusModel) {
                    StatusModel statusModel = (StatusModel) arg0;
                    if (statusModel.isStatus()) {
                        holder.image_bookmark.setImageResource(R.drawable.like_full);
                        product.setIsBookmarked(1);
                        Toast.makeText(context, "Added to bookmarks", Toast.LENGTH_SHORT).show();
                    }
                } else if (arg0 instanceof ErrorModel) {
                    ErrorModel errorModel = (ErrorModel) arg0;
                    String msg = errorModel.getError() != null ? errorModel.getError() : errorModel.getMessage();
                   /* Toast.makeText(context, msg, Toast.LENGTH_LONG).show();*/
                }
            }

            @Override
            public void OnFailureFromServer(String msg) {
                BlueShakLog.logDebug(TAG, "OnFailureFromServer");
            }

            @Override
            public void OnError(String msg) {
                BlueShakLog.logDebug(TAG, "OnError");
            }
        }, "Add Bookmarks");

    }

    private void deleteBookmark(final Context context, final MyViewHolder holder, final SalesDetailsProduct product) {

        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.deleteBookmark(context, product.getProductId(), new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {

                if (arg0 instanceof StatusModel) {
                    StatusModel statusModel = (StatusModel) arg0;
                    if (statusModel.isStatus()) {
                        holder.image_bookmark.setImageResource(R.drawable.like_border);
                        product.setIsBookmarked(0);
                        Toast.makeText(context, "Bookmark Removed", Toast.LENGTH_SHORT).show();
                    }
                } else if (arg0 instanceof ErrorModel) {
                    ErrorModel errorModel = (ErrorModel) arg0;
                    String msg = errorModel.getError() != null ? errorModel.getError() : errorModel.getMessage();
                    Log.d(TAG, "OnFailureFromServer" + msg);
                }
            }

            @Override
            public void OnFailureFromServer(String msg) {
                /*GlobalFunctions.hideProgress();*/
                Log.d(TAG, "OnFailureFromServer" + msg);
            }

            @Override
            public void OnError(String msg) {
               /* GlobalFunctions.hideProgress();*/
                Log.d(TAG, "OnFailureFromServer" + msg);
            }
        }, "Add Bookmarks");
    }


    @Override
    public int getItemCount() {
        return salesProductList.size();
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    public void showSettingsAlert() {
        final com.blueshak.app.blueshak.view.AlertDialog alertDialog = new com.blueshak.app.blueshak.view.AlertDialog(context);
        alertDialog.setTitle("Login/Register");
        alertDialog.setMessage("Please Login/Register to continue");
        alertDialog.setPositiveButton("Continue", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent creategarrage = new Intent(context, LoginActivity.class);
                context.startActivity(creategarrage);
            }
        });
        alertDialog.show();
    }

    @Override
    public int getItemViewType(int position) {

        // loader can't be at position 0
        // loader can only be at the last position
        if (position != 0 && position == getItemCount() - 1) {
            return VIEWTYPE_LOADER;
        }

        return VIEWTYPE_ITEM;
    }

    private void check_conversation_exists(SalesDetailsProduct product) {
        //showProgressBar();
        user.setName(salesProductModel.getSellerName());
        user.setCurrency(product.getCurrency());
        user.setIs_sale(false);
        user.setBs_id(salesProductModel.getSellerId());
        user.setProfileImageUrl(salesProductModel.getSellerImage());
        user.setProduct_name(product.getProductName());
        user.setPrice(product.getSalePrice());
        user.setProduct_url(product.getImage());
        user.setProduct_id(product.getProductId());
        BlueShakSharedPreferences.setChatProductName(context,product.getProductName());
        BlueShakSharedPreferences.setChatProductCurrency(context,product.getCurrency());
        BlueShakSharedPreferences.setChatProductPrice(context,product.getSalePrice());
        conversationIdModel.setIs_garage(true);
        conversationIdModel.setProduct_id(product.getProductId());
        conversationIdModel.setSeller_id(salesProductModel.getSellerId());
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.check_conversation_exists(context, conversationIdModel, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
               // hideProgressBar();
                Log.d(TAG, "onSuccess Response");
                if (arg0 instanceof StatusModel) {
                    StatusModel statusModel = (StatusModel) arg0;
                    if (statusModel.isStatus()) {
                        Toast.makeText(context, statusModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (arg0 instanceof ErrorModel) {
                    ErrorModel errorModel = (ErrorModel) arg0;
                    String msg = errorModel.getError() != null ? errorModel.getError() : errorModel.getMessage();
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                } else if (arg0 instanceof ConversationIdModel) {
                    conversationIdModel = (ConversationIdModel) arg0;
                    user.setConversation_id(conversationIdModel.getConversation_id());
                    user.setActive_tab(conversationIdModel.getActive_tab());
                    String id = conversationIdModel.getProduct_id();
                    user.setProduct_id(conversationIdModel.getProduct_id());
                    Intent i = ChatActivity.newInstance(context, user);
                    context.startActivity(i);
                }
            }

            @Override
            public void OnFailureFromServer(String msg) {
                //hideProgressBar();
                Log.d(TAG, "OnFailureFromServer#############" + msg);
                if (msg != null) {
                    if (msg.equalsIgnoreCase(context.getResources().getString(R.string.ErrorEmailVerification)))
                        GlobalFunctions.showEmailVerificatiomAlert(context);
                    else
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void OnError(String msg) {
               // hideProgressBar();
                if (msg != null) {
                    if (msg.equalsIgnoreCase(context.getResources().getString(R.string.ErrorEmailVerification)))
                        GlobalFunctions.showEmailVerificatiomAlert(context);
                    else
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                }
            }
        }, "Review Ratings");

    }

    public void showLoading(boolean status) {
        showLoader = status;
    }

    private void setBitmapImages(MyViewHolder holder, String decodedImgUri) {

        DisplayImageOptions GALLERY = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .considerExifParams(true)
                .cacheOnDisk(true)
                .showImageForEmptyUri(R.drawable.placeholder_background)
                .showImageOnFail(R.drawable.placeholder_background)
                .showImageOnLoading(R.drawable.placeholder_background).build();

        setBitmapDecodedImage(holder, GALLERY, decodedImgUri);
    }

    private void setBitmapDecodedImage(final MyViewHolder holder, DisplayImageOptions GALLERY, String decodedImgUri) {

        ImageLoader.getInstance().displayImage(decodedImgUri, holder.img_sales_product, GALLERY, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
                holder.img_progressbar.setVisibility(View.GONE);

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                holder.img_progressbar.setVisibility(View.GONE);
                BlueShakLog.logDebug(TAG, "setBitmapDecodedImage  onLoadingFailed ");
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                holder.img_progressbar.setVisibility(View.GONE);
                BlueShakLog.logDebug(TAG, "setBitmapDecodedImage  onLoadingComplete ");
            }

            @Override
            public void onLoadingCancelled(String s, View view) {
                holder.img_progressbar.setVisibility(View.GONE);
                BlueShakLog.logDebug(TAG, "setBitmapDecodedImage  onLoadingCancelled ");
            }
        });
    }
}
