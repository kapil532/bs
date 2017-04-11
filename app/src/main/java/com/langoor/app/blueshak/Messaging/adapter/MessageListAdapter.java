package com.langoor.app.blueshak.Messaging.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.nfc.Tag;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.langoor.blueshak.R;
import com.langoor.app.blueshak.Messaging.data.Message;
import com.langoor.app.blueshak.Messaging.helper.CircleImageView;
import com.langoor.app.blueshak.global.GlobalFunctions;
import com.langoor.app.blueshak.global.GlobalVariables;
import com.langoor.app.blueshak.helper.RoundedImageView;
import com.langoor.app.blueshak.item.ImageViewActivty;
import com.langoor.app.blueshak.item.ProductDetail;
import com.langoor.app.blueshak.item.ViewActivity;
import com.langoor.app.blueshak.services.ServerResponseInterface;
import com.langoor.app.blueshak.services.ServicesMethodsManager;
import com.langoor.app.blueshak.services.model.ErrorModel;
import com.langoor.app.blueshak.services.model.ImageModel;
import com.langoor.app.blueshak.services.model.MessageModel;
import com.langoor.app.blueshak.services.model.ProductModel;
import com.langoor.app.blueshak.services.model.StatusModel;
import com.langoor.app.blueshak.util.LocationService;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class MessageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "ContactsListAdapter";
    private Context context;
    private final int VIEWTYPE_MESSAGE_SENT = 0;
    private final int VIEWTYPE_MESSAGE_RECEIVED = 1;
    private final int VIEWTYPE_MESSAGE_SENT_IMAGE = 3;
    private final int VIEWTYPE_MESSAGE_RECEIVED_IMAGE = 4;
    private final int VIEWTYPE_MESSAGE_RECEIVED_OFFER_MESSAGE = 5;
    private final int VIEWTYPE_MESSAGE_SEND_OFFER_MESSAGE = 6;
    private final int VIEWTYPE_GROUP_MEMBER = 2;
    private static String today;
    private List<MessageModel> messages =new ArrayList<MessageModel>();
    private ProductModel productModel=new ProductModel();
    private ImageModel imageModel=new ImageModel();
    public class SenderHolder extends RecyclerView.ViewHolder {
        TextView messageBody;
        TextView messagTimestamp;
        RoundedImageView profilePic;
        public SenderHolder(View view) {
            super(view);
            messageBody = (TextView) view.findViewById(R.id.message_body);
            messagTimestamp = (TextView)view.findViewById(R.id.message_timestamp);
            profilePic = (RoundedImageView)view.findViewById(R.id.profile_pic);
        }
    }
    public class ReceiverHolder extends RecyclerView.ViewHolder {
        TextView messageBody;
        TextView messagTimestamp;
        RoundedImageView profilePic;
        TextView group_member;
        public ReceiverHolder(View view) {
            super(view);
            messageBody = (TextView) view.findViewById(R.id.message_body);
            messagTimestamp = (TextView) view.findViewById(R.id.message_timestamp);
            profilePic = (RoundedImageView)view.findViewById(R.id.profile_pic);
        }
    }
    public class ReceiverImageHolder extends RecyclerView.ViewHolder {
        ImageView messageBody;
        TextView messagTimestamp;
        RoundedImageView profilePic;
        TextView group_member;
        public ReceiverImageHolder(View view) {
            super(view);
            messageBody = (ImageView) view.findViewById(R.id.message_body);
            messagTimestamp = (TextView) view.findViewById(R.id.message_timestamp);
            profilePic = (RoundedImageView)view.findViewById(R.id.profile_pic);
        }
    }
    public class SenderImageHolder extends RecyclerView.ViewHolder {
        ImageView messageBody;
        TextView messagTimestamp;
        RoundedImageView profilePic;
        TextView group_member;
        public SenderImageHolder(View view) {
            super(view);
            messageBody = (ImageView) view.findViewById(R.id.message_body);
            messagTimestamp = (TextView) view.findViewById(R.id.message_timestamp);
            profilePic = (RoundedImageView)view.findViewById(R.id.profile_pic);
        }
    }
    public class SenderOfferHolder extends RecyclerView.ViewHolder {
        ImageView product_image;
        TextView messagTimestamp,product_name,messageBody;
        RoundedImageView profilePic;
        TextView group_member;
        View product_view;
        public SenderOfferHolder(View view) {
            super(view);
            product_view=view;
            messageBody = (TextView) view.findViewById(R.id.message_body);
            messagTimestamp = (TextView) view.findViewById(R.id.message_timestamp);
            profilePic = (RoundedImageView)view.findViewById(R.id.profile_pic);
            product_image = (ImageView)view.findViewById(R.id.product_image);
            product_name = (TextView) view.findViewById(R.id.product_name);
        }
    }
    public class ReceiverOfferHolder extends RecyclerView.ViewHolder {
        ImageView product_image;
        TextView messageBody, messagTimestamp,product_name;
        RoundedImageView profilePic;
        TextView group_member;
        View product_view;
        public ReceiverOfferHolder(View view) {
            super(view);
            product_view=view;
            messageBody = (TextView) view.findViewById(R.id.message_body);
            messagTimestamp = (TextView) view.findViewById(R.id.message_timestamp);
            profilePic = (RoundedImageView)view.findViewById(R.id.profile_pic);
            product_image = (ImageView)view.findViewById(R.id.product_image);
            product_name = (TextView) view.findViewById(R.id.product_name);
        }
    }
    public MessageListAdapter(Context mContext, List<MessageModel> albumList) {
        this.messages =albumList;
        this.context = mContext;
        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

    }
    @Override
    public int getItemViewType(int position){
        MessageModel messageModel=messages.get(position);
        if(messageModel.getMessage_type().equalsIgnoreCase(GlobalVariables.TYPE_TEXT)){
            if(messageModel.is_sent_by_you())
                return VIEWTYPE_MESSAGE_SENT;
            else
                return VIEWTYPE_MESSAGE_RECEIVED;
        }else if(messageModel.getMessage_type().equalsIgnoreCase(GlobalVariables.TYPE_OFFER)){
            if(messageModel.is_sent_by_you())
                return VIEWTYPE_MESSAGE_SEND_OFFER_MESSAGE;
            else
                return VIEWTYPE_MESSAGE_RECEIVED_OFFER_MESSAGE;
        }else{
            if(messageModel.is_sent_by_you())
                return VIEWTYPE_MESSAGE_SENT_IMAGE;
            else
                return VIEWTYPE_MESSAGE_RECEIVED_IMAGE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            if (viewType == VIEWTYPE_MESSAGE_SENT) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_listviewcell_sent_message, parent, false);
                return new SenderHolder(v);
            } else if (viewType == VIEWTYPE_MESSAGE_RECEIVED) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.message_listviewcell_received_message, parent, false);
                return new ReceiverHolder(itemView);
            } else if (viewType == VIEWTYPE_MESSAGE_RECEIVED_IMAGE) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.message_listviewcell_received_image, parent, false);
                return new ReceiverImageHolder(itemView);
            } else if (viewType == VIEWTYPE_MESSAGE_SENT_IMAGE) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.message_listviewcell_sent_image, parent, false);
                return new SenderImageHolder(itemView);
            } else if (viewType == VIEWTYPE_MESSAGE_RECEIVED_OFFER_MESSAGE) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.message_listviewcell_received_offer_message, parent, false);
                return new ReceiverOfferHolder(itemView);
            } else if (viewType == VIEWTYPE_MESSAGE_SEND_OFFER_MESSAGE) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.message_listviewcell_send_offer_message, parent, false);
                return new SenderOfferHolder(itemView);
            } else
                throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
        }catch (NullPointerException e){
            e.printStackTrace();
            throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");

        }
     }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder view_holder, final int position) {
        try{
            MessageModel messageModel = messages.get(position);
            ImageLoader imageLoader = ImageLoader.getInstance();
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisc(true).resetViewBeforeLoading(true)
                    .showImageForEmptyUri(R.drawable.placeholder_background)
                    .showImageOnFail(R.drawable.placeholder_background)
                    .showImageOnLoading(R.drawable.placeholder_background).build();

            DisplayImageOptions options_rounded = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .displayer(new RoundedBitmapDisplayer(25)) // default
                    .cacheOnDisc(true).resetViewBeforeLoading(true)
                    .showImageForEmptyUri(R.drawable.placeholder_background)
                    .showImageOnFail(R.drawable.placeholder_background)
                    .showImageOnLoading(R.drawable.placeholder_background).build();
            String time_stamp="";
            if(!messageModel.isLocal_message())
                time_stamp=getTimeStamp(messageModel.getCreated_at());
            else
                time_stamp="Just now";

            String chat_user_image=GlobalFunctions.getSharedPreferenceString(context,GlobalVariables.SHARED_PREFERENCE_AVATAR);

            if(view_holder instanceof SenderHolder){
                SenderHolder senderHolder=(SenderHolder)view_holder;
                senderHolder.messagTimestamp.setText(time_stamp);
                senderHolder.messageBody.setText(messageModel.getMessage());
                       //download and display image from url
               /* imageLoader.displayImage(chat_user_image,senderHolder.profilePic, options);*/
                if(!TextUtils.isEmpty(chat_user_image)&&chat_user_image!=null){
                    Picasso.with(context)
                            .load(chat_user_image)
                            .placeholder(R.drawable.squareplaceholder)
                            .fit().centerCrop()
                            .into(senderHolder.profilePic);
                }else{
                    senderHolder.profilePic.setImageResource(R.drawable.squareplaceholder);
                }
            }else if(view_holder instanceof ReceiverHolder){
                ReceiverHolder receiverHolder=(ReceiverHolder)view_holder;
                receiverHolder.messagTimestamp.setText(time_stamp);
                receiverHolder.messageBody.setText(messageModel.getMessage());
                String image=messageModel.getContact_image();
                //download and display image from url
               /* imageLoader.displayImage(image,receiverHolder.profilePic, options);*/
                if(!TextUtils.isEmpty(image)){
                    Picasso.with(context)
                            .load(image)
                            .placeholder(R.drawable.squareplaceholder)
                            .fit().centerCrop()
                            .into(receiverHolder.profilePic);
                }else{
                    receiverHolder.profilePic.setImageResource(R.drawable.squareplaceholder);
                }
            }else if(view_holder instanceof ReceiverImageHolder){
                ReceiverImageHolder receiverHolder=(ReceiverImageHolder)view_holder;
                receiverHolder.messagTimestamp.setText(time_stamp);
                String message_body=messageModel.getImage();
                //download and display image from url
                imageLoader.displayImage(message_body,receiverHolder.messageBody, options_rounded);
                String image=messageModel.getContact_image();
                imageModel.setImage(message_body);
                if(!TextUtils.isEmpty(image)){
                    Picasso.with(context)
                            .load(image)
                            .placeholder(R.drawable.squareplaceholder)
                            .fit().centerCrop()
                            .into(receiverHolder.profilePic);
                }else{
                    receiverHolder.profilePic.setImageResource(R.drawable.squareplaceholder);
                }
                receiverHolder.messageBody.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent= ViewActivity.newInstance(context,imageModel);
                        context.startActivity(intent);
                    }
                });
            }else if(view_holder instanceof SenderImageHolder){
                SenderImageHolder receiverHolder=(SenderImageHolder)view_holder;
                receiverHolder.messagTimestamp.setText(time_stamp);
                String message_body=messageModel.getImage();
                imageModel.setImage(message_body);
                if(!messageModel.isLocal_message()){
                    //download and display image from url
                    imageLoader.displayImage(message_body,receiverHolder.messageBody, options_rounded);
                }else {
                    Bitmap image_bitMap= GlobalFunctions.getBitmapFromBase64(message_body);
                    receiverHolder.messageBody.setImageBitmap(image_bitMap);
                }
                receiverHolder.messageBody.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent= ViewActivity.newInstance(context,imageModel);
                        context.startActivity(intent);
                    }
                });
                if(!TextUtils.isEmpty(chat_user_image)&&chat_user_image!=null){
                    Picasso.with(context)
                            .load(chat_user_image)
                            .placeholder(R.drawable.squareplaceholder)
                            .fit().centerCrop()
                            .into(receiverHolder.profilePic);
                }else{
                    receiverHolder.profilePic.setImageResource(R.drawable.squareplaceholder);
                }
            }else if(view_holder instanceof SenderOfferHolder){
                SenderOfferHolder senderHolder=(SenderOfferHolder)view_holder;
                senderHolder.messagTimestamp.setText(time_stamp);
                senderHolder.messageBody.setText(messageModel.getMessage());
                if(!TextUtils.isEmpty(chat_user_image)&&chat_user_image!=null){
                    Picasso.with(context)
                            .load(chat_user_image)
                            .placeholder(R.drawable.squareplaceholder)
                            .fit().centerCrop()
                            .into(senderHolder.profilePic);
                }else{
                    senderHolder.profilePic.setImageResource(R.drawable.squareplaceholder);
                }
                senderHolder.product_name.setText(messageModel.getProduct_name());
                String product_image=messageModel.getProduct_image();
                //download and display image from url
                imageLoader.displayImage(product_image,senderHolder.product_image, options);
                productModel.setId(messageModel.getOffered_product_id());
                productModel.setName(messageModel.getProduct_name());
                senderHolder.product_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = ProductDetail.newInstance(context,productModel,null,GlobalVariables.TYPE_MY_SALE);
                        context.startActivity(intent);
                    }
                });
            }else if(view_holder instanceof ReceiverOfferHolder){
                ReceiverOfferHolder senderHolder=(ReceiverOfferHolder)view_holder;
                senderHolder.messagTimestamp.setText(time_stamp);
                senderHolder.messageBody.setText(messageModel.getMessage());
                //download and display image from url
               /* imageLoader.displayImage(chat_user_image,senderHolder.profilePic, options);*/
                String image=messageModel.getContact_image();
                if(!TextUtils.isEmpty(image)&&image!=null){
                    Picasso.with(context)
                            .load(image)
                            .placeholder(R.drawable.squareplaceholder)
                            .fit().centerCrop()
                            .into(senderHolder.profilePic);
                }else{
                    senderHolder.profilePic.setImageResource(R.drawable.squareplaceholder);
                }
                senderHolder.product_name.setText(messageModel.getProduct_name());
                String product_image=messageModel.getProduct_image();
                //download and display image from url
                imageLoader.displayImage(product_image,senderHolder.product_image, options);
                productModel.setId(messageModel.getOffered_product_id());
                productModel.setName(messageModel.getProduct_name());
                senderHolder.product_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = ProductDetail.newInstance(context,productModel,null,GlobalVariables.TYPE_MY_SALE);
                        context.startActivity(intent);
                    }
                });
            }

        }  catch (NullPointerException e){
            e.printStackTrace();
            Crashlytics.log(e.getMessage());
        }catch (NumberFormatException e) {
            e.printStackTrace();
            Crashlytics.log(e.getMessage());
        }catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
            Crashlytics.log(e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            Crashlytics.log(e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void insertMessage(MessageModel newMessage){
        this.messages.add(this.messages.size(),newMessage);
        notifyDataSetChanged();

    }
    public static String getTimeStamp(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     /*   format.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));*/
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        String timestamp = "";
        today = today.length() < 2 ? "0" + today : today;
        try {
            Date date = format.parse(dateStr);
            SimpleDateFormat todayFormat = new SimpleDateFormat("dd");
            String dateToday = todayFormat.format(date);
            format = dateToday.equals(today) ? new SimpleDateFormat("hh:mm a") : new SimpleDateFormat("dd LLL, hh:mm a");
            String date1 = format.format(date);
            timestamp = date1.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timestamp;
    }
}
