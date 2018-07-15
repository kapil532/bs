package com.blueshak.app.blueshak.home;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v4.app.*;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blueshak.app.blueshak.Messaging.activity.ChatActivity;
import com.blueshak.app.blueshak.Messaging.data.User;
import com.blueshak.app.blueshak.garage.CreateGarageSaleFragment;
import com.blueshak.app.blueshak.garage.CreateSaleActivity;
import com.blueshak.app.blueshak.seller.model.SellerData;
import com.blueshak.app.blueshak.seller.model.SellerLikeModel;
import com.blueshak.app.blueshak.seller.ui.EditOwnSalesActivity;
import com.blueshak.app.blueshak.seller.ui.SellerDetailsActivity;
import com.blueshak.app.blueshak.services.ServerResponseInterface;
import com.blueshak.app.blueshak.services.ServicesMethodsManager;
import com.blueshak.app.blueshak.services.model.ConversationIdModel;
import com.blueshak.app.blueshak.services.model.ErrorModel;
import com.blueshak.app.blueshak.services.model.StatusModel;
import com.blueshak.app.blueshak.util.BlueShakLog;
import com.blueshak.app.blueshak.util.BlueShakSharedPreferences;
import com.blueshak.app.blueshak.util.Utilities;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.blueshak.blueshak.R;
import com.blueshak.app.blueshak.global.GlobalFunctions;
import com.blueshak.app.blueshak.global.GlobalVariables;
import com.blueshak.app.blueshak.login.LoginActivity;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.vlonjatg.progressactivity.ProgressActivity;

import java.util.List;

public class SalesListAdapterNew extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
        ViewPagerEx.OnPageChangeListener {

    private static String TAG = "SalesListAdapterNew";
    private Context context;
    private List<SellerData> sellerDataList;
    protected boolean showLoader;
    private static final int VIEWTYPE_ITEM = 1;
    private static final int VIEWTYPE_LOADER = 2;
    private int likeCount;
    private int likedCounts;
    private boolean isSelfUser = false;
    private User user = new User();
    private ConversationIdModel conversationIdModel = new ConversationIdModel();

    private OnSellerLikeListener likeListener;
    private boolean isCheck = false;
    private String sText;

    public SalesListAdapterNew(Context mContext, List<SellerData> sellerDataList, OnSellerLikeListener likeListener) {
        this.context = mContext;
        this.sellerDataList = sellerDataList;
        this.likeListener = likeListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        protected TextView txt_shop_name;
        protected ImageView image_bookmark;
        protected ProgressActivity progressActivity;
        protected PagerIndicator mPagerIndicator;
        protected LinearLayout detail_content;

        private LinearLayout layout_two;
        private LinearLayout layout_four;
        private LinearLayout layout_six;
        private LinearLayout layout_asymmetric_main;

        private ImageView img_two_1;
        private ImageView img_two_2;
        private View empty_two_2;

        private ImageView img_four_1;
        private ImageView img_four_2;
        private ImageView img_four_3;
        private ImageView img_four_4;
        private View empty_four_4;
        private TextView tv_more;

        private ImageView img_six_1;
        private ImageView img_six_2;
        private ImageView img_six_3;
        private ImageView img_six_4;
        private ImageView img_six_5;
        private ImageView seller_image;
        private TextView seller_name;
        private ImageView image_feature;
        private TextView txt_review;
        private RatingBar rating_bar;
        private TextView txt_last_date;
        private ImageView image_more;
        private TextView txt_address;
        private TextView txt_description;
        private TextView txt_like;
        private TextView txt_chat;
        private RelativeLayout layout_seller;

        public MyViewHolder(View view) {
            super(view);

            seller_name = (TextView) view.findViewById(R.id.seller_name);
            seller_image = (ImageView) view.findViewById(R.id.seller_image);
            image_feature = (ImageView) view.findViewById(R.id.image_feature);
            txt_review = (TextView) view.findViewById(R.id.txt_review);
            rating_bar = (RatingBar) view.findViewById(R.id.rating_bar);
            txt_last_date = (TextView) view.findViewById(R.id.txt_last_date);

            txt_shop_name = (TextView) view.findViewById(R.id.txt_shop_name);
            image_bookmark = (ImageView) view.findViewById(R.id.image_bookmark);
            image_more = (ImageView) view.findViewById(R.id.image_more);
            txt_address = (TextView) view.findViewById(R.id.txt_address);
            txt_description = (TextView) view.findViewById(R.id.txt_description);


            detail_content = (LinearLayout) view.findViewById(R.id.detail_content);

            layout_two = (LinearLayout) view.findViewById(R.id.layout_two);
            layout_four = (LinearLayout) view.findViewById(R.id.layout_four);
            layout_six = (LinearLayout) view.findViewById(R.id.layout_six);
            layout_asymmetric_main = (LinearLayout) view.findViewById(R.id.layout_asymmetric_main);

            img_two_1 = (ImageView) view.findViewById(R.id.img_two_1);
            img_two_2 = (ImageView) view.findViewById(R.id.img_two_2);
            empty_two_2 = (View) view.findViewById(R.id.empty_two_2);

            img_four_1 = (ImageView) view.findViewById(R.id.img_four_1);
            img_four_2 = (ImageView) view.findViewById(R.id.img_four_2);
            img_four_3 = (ImageView) view.findViewById(R.id.img_four_3);
            img_four_4 = (ImageView) view.findViewById(R.id.img_four_4);
            empty_four_4 = (View) view.findViewById(R.id.empty_four_4);

            img_six_1 = (ImageView) view.findViewById(R.id.img_six_1);
            img_six_2 = (ImageView) view.findViewById(R.id.img_six_2);
            img_six_3 = (ImageView) view.findViewById(R.id.img_six_3);
            img_six_4 = (ImageView) view.findViewById(R.id.img_six_4);
            img_six_5 = (ImageView) view.findViewById(R.id.img_six_5);
            tv_more = (TextView) view.findViewById(R.id.tv_more);
            txt_like = (TextView) view.findViewById(R.id.txt_like);
            txt_chat = (TextView) view.findViewById(R.id.txt_chat);
            layout_seller = (RelativeLayout)view.findViewById(R.id.layout_seller);
        }
    }

    class VHLoader extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public VHLoader(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.bottom_progress_bar);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sale_list_row_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder view_holder, int position) {
        if (view_holder instanceof MyViewHolder) {
            try {
                final MyViewHolder holder = (MyViewHolder) view_holder;
                int grey2 = context.getResources().getColor(R.color.grey_2);
                int black = context.getResources().getColor(R.color.black);
                final SellerData obj = sellerDataList.get(position);

                if (obj.getSellerName() != null) {
                    holder.seller_name.setText(obj.getSellerName());
                }
                setBitmapImages(holder.seller_image, obj.getSellerImage());

                if (obj.getIsFeatured() != null && obj.getIsFeatured().equalsIgnoreCase("1")) {
                    holder.image_feature.setVisibility(View.VISIBLE);
                } else {
                    holder.image_feature.setVisibility(View.GONE);
                }
                String review;
                if (obj.getReviewsCount() == 0 || obj.getReviewsCount() == 1) {
                    review = context.getString(R.string.read_review) + " (" + obj.getReviewsCount() + ")";
                } else {
                    review = context.getString(R.string.read_reviews) + " (" + obj.getReviewsCount() + ")";
                }
                holder.txt_review.setText(review);

                if (obj.getAvgSellerRating() != null) {
                    holder.rating_bar.setRating(Float.valueOf(obj.getAvgSellerRating()));
                }

                if (obj.getListedDate() != null) {
                    holder.txt_last_date.setText(context.getString(R.string.listed) + " " + obj.getListedDate());
                }

                if (!TextUtils.isEmpty(obj.getShopName())) {
                    holder.txt_shop_name.setText(GlobalFunctions.getSentenceFormat(obj.getShopName()));
                }

                String itemName;
                int iTemCount = obj.getShopItemsCount();
                if (iTemCount == 0 || iTemCount == 1) {
                    itemName = iTemCount + " item";
                } else {
                    itemName = iTemCount + " items";
                }

                if (obj.getSaleAddress() != null && !obj.getSaleAddress().isEmpty()
                        && !obj.getSaleAddress().equalsIgnoreCase("null")) {
                    holder.txt_address.setText(itemName + " | " + obj.getSaleAddress());
                } else {
                    holder.txt_address.setText(itemName);
                }

                if (obj.getDescription() != null && !obj.getDescription().trim().isEmpty()
                        && !obj.getDescription().equalsIgnoreCase("null")) {
                    holder.txt_description.setVisibility(View.VISIBLE);

                    /*sText = "Description efugiewf efewf vhv fuberf rufber veruyfv fuerfbhfef fefb fhrbf fhr fherf rufbr furebf furebf " +
                            "fubr ufer fberu hs fhjfh jfrjf jvbjv vjdfv fjbvf jrjbv vjbjvh vjbv Description efugiewf efewf vhv fuberf rufber veruyfv fuerfbhfef fefb fhrbf fhr fherf rufbr furebf fur" +
                            "Description efugiewf efewf vhv fuberf rufber veruyfv fuerfbhfef fefb fhrbf fhr fherf rufbr furebf fur" +
                            "Description efugiewf efewf vhv fuberf rufber veruyfv " +
                            "fuerfbhfef fefb fhrbf fhr fherf rufbr furebf fur ";*///obj.getDescription();
                    sText = obj.getDescription();
                   // holder.txt_description.setText(sText);
                   // Utilities.makeTextViewResizable(holder.txt_description, 4, ".. More", true);

                    if (sText.length() > 180) {
                        String sText1 = sText.substring(0,180)+"..";
                        holder.txt_description.setText(Html.fromHtml(sText1+"<font color='blue'>More</font>"));
                    }else{
                        holder.txt_description.setText(sText);
                    }

                    holder.txt_description.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(sText.length() > 180){
                                if (!isCheck) {
                                    String sText2 = sText.substring(0,sText.length());
                                    holder.txt_description.setText(Html.fromHtml(sText2+"<font color='blue'>Less</font>"));
                                    isCheck = true;
                                } else {
                                    String sText3 = sText.substring(0,180)+"..";
                                    holder.txt_description.setText(Html.fromHtml(sText3+"<font color='blue'>More</font>"));
                                    isCheck = false;
                                }
                            }

                        }
                    });


                } else {
                    holder.txt_description.setVisibility(View.GONE);
                }

                holder.layout_seller.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       /* Intent intent = ReviewsRatings.newInstance(context, null, obj);
                        context.startActivity(intent);*/
                        Intent intent = new Intent(context, SellerDetailsActivity.class);
                        intent.putExtra(SellerDetailsActivity.SALES_DATA, obj);
                        context.startActivity(intent);
                    }
                });


                int imageSize = obj.getImages().size();
                setDynamicImages(holder, imageSize, obj);

                if (obj.getIsBookmarked() == 1) {
                    holder.image_bookmark.setBackgroundResource(R.drawable.like_full);
                } else {
                    holder.image_bookmark.setBackgroundResource(R.drawable.like_border);
                }

                likeCount = obj.getLikesCount();
                holder.txt_like.setText(" (" + likeCount + ") " + getLikes(likeCount));
                if (obj.getIsUserLikedSeller() == 1) {
                    holder.txt_like.setTextColor(ContextCompat.getColor(context, R.color.brandColor));
                    holder.txt_like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.liked_icon, 0, 0, 0);
                } else {
                    holder.txt_like.setTextColor(ContextCompat.getColor(context, R.color.black));
                    holder.txt_like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.unlike_icon, 0, 0, 0);
                }

                String signed_user_user_id = GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_USERID);
                if (signed_user_user_id != null && obj.getSellerId() != null) {
                    if (Integer.parseInt(obj.getSellerId()) == Integer.parseInt(signed_user_user_id)) {
                        isSelfUser = true;
                        BlueShakSharedPreferences.setSelfUser(context,true);
                        holder.image_more.setVisibility(View.VISIBLE);
                        holder.txt_chat.setVisibility(View.GONE);
                    } else {
                        isSelfUser = false;
                        BlueShakSharedPreferences.setSelfUser(context,false);
                        holder.image_more.setVisibility(View.GONE);
                        holder.txt_chat.setVisibility(View.VISIBLE);
                    }
                }


                holder.layout_asymmetric_main.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, SalesDetailsListActivity.class);
                        intent.putExtra(SalesDetailsListActivity.SALES_DATA, obj);
                        context.startActivity(intent);
                        //Intent intent = ProductDetail.newInstance(context, null, obj, GlobalVariables.TYPE_MY_SALE);
                        //context.startActivity(intent);
                    }
                });

                onChat(holder, obj);
                onLike(holder, obj);
                onBookmark(holder, obj);
                onEdit(holder, obj);

            } catch (Resources.NotFoundException e) {
                BlueShakLog.logDebug(TAG, "Error " + e.getLocalizedMessage());
            } catch (Exception e) {
                BlueShakLog.logDebug(TAG, "Error " + e.getLocalizedMessage());
            }

        } else if (view_holder instanceof VHLoader) {
            VHLoader loaderViewHolder = (VHLoader) view_holder;
            if (showLoader) {
                loaderViewHolder.progressBar.setVisibility(View.VISIBLE);
            } else {
                loaderViewHolder.progressBar.setVisibility(View.GONE);
            }
            return;
        }
    }

    private void setDynamicImages(MyViewHolder holder, int imageSize, SellerData obj) {
        if (imageSize > 0 && imageSize <= 2) {
            holder.layout_two.setVisibility(View.VISIBLE);
            holder.layout_four.setVisibility(View.GONE);
            holder.layout_six.setVisibility(View.GONE);
            if (imageSize == 1) {
                holder.img_two_1.setVisibility(View.VISIBLE);
                holder.img_two_2.setVisibility(View.GONE);
                holder.empty_two_2.setVisibility(View.GONE);
                setBitmapImages(holder.img_two_1, obj.getImages().get(0).getLink());
            } else {
                holder.img_two_1.setVisibility(View.VISIBLE);
                holder.img_two_2.setVisibility(View.VISIBLE);
                holder.empty_two_2.setVisibility(View.VISIBLE);
                setBitmapImages(holder.img_two_1, obj.getImages().get(0).getLink());
                setBitmapImages(holder.img_two_2, obj.getImages().get(1).getLink());
            }

        } else if (imageSize == 3 || imageSize == 4) {
            holder.layout_two.setVisibility(View.GONE);
            holder.layout_four.setVisibility(View.VISIBLE);
            holder.layout_six.setVisibility(View.GONE);
            if (imageSize == 3) {
                holder.img_four_1.setVisibility(View.VISIBLE);
                holder.img_four_2.setVisibility(View.VISIBLE);
                holder.img_four_3.setVisibility(View.VISIBLE);
                holder.img_four_4.setVisibility(View.GONE);
                holder.empty_four_4.setVisibility(View.GONE);
                setBitmapImages(holder.img_four_1, obj.getImages().get(0).getLink());
                setBitmapImages(holder.img_four_2, obj.getImages().get(1).getLink());
                setBitmapImages(holder.img_four_3, obj.getImages().get(2).getLink());

            } else {
                holder.img_four_1.setVisibility(View.VISIBLE);
                holder.img_four_2.setVisibility(View.VISIBLE);
                holder.img_four_3.setVisibility(View.VISIBLE);
                holder.img_four_4.setVisibility(View.VISIBLE);
                holder.empty_four_4.setVisibility(View.VISIBLE);
                setBitmapImages(holder.img_four_1, obj.getImages().get(0).getLink());
                setBitmapImages(holder.img_four_2, obj.getImages().get(1).getLink());
                setBitmapImages(holder.img_four_3, obj.getImages().get(2).getLink());
                setBitmapImages(holder.img_four_4, obj.getImages().get(3).getLink());
            }
        } else if (imageSize == 5 || imageSize > 5) {
            holder.layout_two.setVisibility(View.GONE);
            holder.layout_four.setVisibility(View.GONE);
            holder.layout_six.setVisibility(View.VISIBLE);
            if (imageSize == 5) {
                holder.tv_more.setVisibility(View.GONE);

            } else {
                holder.tv_more.setVisibility(View.VISIBLE);
                holder.tv_more.setText("+" + (imageSize - 4));
            }
            setBitmapImages(holder.img_six_1, obj.getImages().get(0).getLink());
            setBitmapImages(holder.img_six_2, obj.getImages().get(1).getLink());
            setBitmapImages(holder.img_six_3, obj.getImages().get(2).getLink());
            setBitmapImages(holder.img_six_4, obj.getImages().get(3).getLink());
            setBitmapImages(holder.img_six_5, obj.getImages().get(4).getLink());
        }
    }

    private void onEdit(MyViewHolder holder,final SellerData obj) {
        holder.image_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_loggedIn()) {
                   /* CreateGarageSaleFragment.newInstance(context, obj);
                    Intent result = new Intent(context,CreateGarageSaleFragment.class);
                    context.startActivity(result);*/
                   Intent intent = new Intent(context, EditOwnSalesActivity.class);
                   intent.putExtra(EditOwnSalesActivity.EDIT_SALES_MODEL,obj);
                   context.startActivity(intent);

                } else{
                    showSettingsAlert();
                }

            }
        });
    }


    private void onLike(final MyViewHolder holder, final SellerData obj) {
        holder.txt_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //likeListener.onSellerLike(obj.getShopId(),obj.getSellerId());
                if (!isLikeClicked) {
                    isLikeClicked = true;
                    postSellerLike(context, holder, obj);
                }
            }
        });
    }


    private void onChat(MyViewHolder holder,final SellerData obj) {
        holder.txt_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_loggedIn()) {
                    if (!isSelfUser) {
                        if (obj.getSellerName() != null || obj.getSellerId() != null) {
                            check_conversation_exists(obj);
                        } else {
                            Toast.makeText(context, "Sorry can't send message", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        //show_edit_sale_pop_up(v);
                    }
                } else
                    showSettingsAlert();
            }
        });
    }

    private void onBookmark(final MyViewHolder holder, final SellerData obj) {
        holder.image_bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isBookmarkClicked) {
                    isBookmarkClicked = true;
                    postSellerBookmark(context, holder, obj);
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

    @Override
    public int getItemCount() {
        return sellerDataList.size();
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

    private boolean isLikeClicked = false;

    private void postSellerLike(final Context context, final MyViewHolder holder, final SellerData data) {
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.postLikeSeller(context, data.getShopId(), data.getSellerId(), new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                isLikeClicked = false;
                if (arg0 != null) {
                    SellerLikeModel sellerLike = new Gson().fromJson(arg0.toString(), SellerLikeModel.class);
                    if (sellerLike.getSuccess() != null && !sellerLike.getSuccess().isEmpty()
                            && !sellerLike.getSuccess().equalsIgnoreCase("null")) {
                        likedCounts = likeCount;
                        if (!sellerLike.getSuccess().contains("Un")) {
                            likedCounts = likedCounts + 1;
                            if (likedCounts < 0) {
                                likedCounts = 0;
                            }
                            holder.txt_like.setText(" (" + likedCounts + ") " + getLikes(likedCounts));
                            holder.txt_like.setTextColor(ContextCompat.getColor(context, R.color.brandColor));
                            holder.txt_like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.liked_icon, 0, 0, 0);
                        } else {
                            likedCounts = likedCounts - 1;
                            if (likedCounts < 0) {
                                likedCounts = 0;
                            }
                            holder.txt_like.setText(" (" + likedCounts + ") " + getLikes(likedCounts));
                            holder.txt_like.setTextColor(ContextCompat.getColor(context, R.color.black));
                            holder.txt_like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.unlike_icon, 0, 0, 0);
                        }
                        //Toast.makeText(context, "Message -> " + sellerLike.getSuccess(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void OnFailureFromServer(String msg) {
                isLikeClicked = false;
                /*GlobalFunctions.hideProgress();*/
               /* Toast.makeText(context, msg, Toast.LENGTH_LONG).show();*/
            }

            @Override
            public void OnError(String msg) {
                isLikeClicked = false;
               /* GlobalFunctions.hideProgress();*/
             /*   Toast.makeText(context, msg, Toast.LENGTH_LONG).show();*/
            }
        }, "Seller Like");

    }

    private String getLikes(int iCount) {
        String like;
        if (iCount == 0 || iCount == 1) {
            like = "Like";
        } else {
            like = "Likes";
        }
        return like;
    }

    private boolean isBookmarkClicked = false;

    private void postSellerBookmark(final Context context, final MyViewHolder holder, final SellerData data) {
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.postBookmarkSeller(context, data.getShopId(), new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                isBookmarkClicked = false;
                if (arg0 != null) {
                    SellerLikeModel sellerLike = new Gson().fromJson(arg0.toString(), SellerLikeModel.class);
                    String message = sellerLike.getSuccess();
                    if (message != null && !message.isEmpty()
                            && !message.equalsIgnoreCase("null")) {

                        if (message.contains("Bookmark removed")) {
                            holder.image_bookmark.setBackgroundResource(R.drawable.like_border);
                        } else {
                            holder.image_bookmark.setBackgroundResource(R.drawable.like_full);
                        }
                        //Toast.makeText(context, "Message -> " + sellerLike.getSuccess(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void OnFailureFromServer(String msg) {
                isBookmarkClicked = false;

            }

            @Override
            public void OnError(String msg) {
                isBookmarkClicked = false;

            }
        }, "Seller Bookmark");

    }

    private void check_conversation_exists(SellerData product) {
        //showProgressBar();
        user.setName(product.getSellerName());
        user.setIs_sale(false);
        user.setBs_id(product.getSellerId());
        user.setProfileImageUrl(product.getSellerImage());
        user.setProduct_name(product.getSellerName());
        user.setProduct_url(product.getSellerImage());
        user.setProduct_id(product.getUserId());
        conversationIdModel.setIs_garage(true);
        conversationIdModel.setProduct_id(product.getUserId());
        conversationIdModel.setSeller_id(product.getSellerId());
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

    private void setBitmapImages(ImageView imageView, String decodedImgUri) {

        DisplayImageOptions GALLERY = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .considerExifParams(true)
                .cacheOnDisk(true)
                .showImageForEmptyUri(R.drawable.placeholder_background)
                .showImageOnFail(R.drawable.placeholder_background)
                .showImageOnLoading(R.drawable.placeholder_background).build();

        setBitmapDecodedImage(imageView, GALLERY, decodedImgUri);
    }

    private void setBitmapDecodedImage(ImageView imageView, DisplayImageOptions GALLERY, String decodedImgUri) {

        ImageLoader.getInstance().displayImage(decodedImgUri, imageView, GALLERY, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                BlueShakLog.logDebug(TAG, "setBitmapDecodedImage  onLoadingFailed ");
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                BlueShakLog.logDebug(TAG, "setBitmapDecodedImage  onLoadingComplete ");
            }

            @Override
            public void onLoadingCancelled(String s, View view) {
                BlueShakLog.logDebug(TAG, "setBitmapDecodedImage  onLoadingCancelled ");
            }
        });
    }

    public interface OnSellerLikeListener {
        void onSellerLike(String shop_id, String seller_id);
    }
}
