package com.langoor.app.blueshak.Messaging.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.langoor.app.blueshak.global.GlobalFunctions;
import com.langoor.app.blueshak.services.ServerResponseInterface;
import com.langoor.app.blueshak.services.ServicesMethodsManager;
import com.langoor.app.blueshak.view.AlertDialog;
import com.langoor.blueshak.R;
import com.langoor.app.blueshak.Messaging.activity.ChatActivity;
import com.langoor.app.blueshak.Messaging.data.User;
import com.langoor.app.blueshak.global.GlobalVariables;
import com.langoor.app.blueshak.helper.RoundedImageView;
import com.langoor.app.blueshak.services.model.ContactModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.squareup.picasso.Picasso;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class ContactsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "ContactsListAdapter";
    private Context context;
    private List<ContactModel> albumList;
    List<ContactModel> itemsPendingRemoval;
    private Handler handler = new Handler(); // hanlder for running delayed runnables
    HashMap<ContactModel, Runnable> pendingRunnables = new HashMap<>(); // map of items to pending runnables, so we can cancel a removal if need be
    private static final int PENDING_REMOVAL_TIMEOUT = 3000; // 3sec
    public String active_tab;
    private static String today;
    boolean undoOn; // is undo on, you can turn it on from the toolbar menu
    public class MyViewHolder extends RecyclerView.ViewHolder {
        protected TextView lastTextMessage,/*TimeStamp,unreadCount,*/name,last_text_message_not_read;
        protected RoundedImageView profilePic;
        protected ImageView item_image,/*product_image,*/image_type;
        public CheckBox chkSelected;
        public RadioButton is_read;

        LinearLayout item_view;
        protected  View view;
        public MyViewHolder(View convertView) {
            super(convertView);
            view=convertView;
            profilePic = (RoundedImageView) convertView.findViewById(R.id.profile_pic);
            is_read=(RadioButton)convertView.findViewById(R.id.is_read);
        /*    product_image = (ImageView) convertView.findViewById(R.id.product_image);*/
            lastTextMessage=(TextView)convertView.findViewById(R.id.last_text_message);
            last_text_message_not_read=(TextView)convertView.findViewById(R.id.last_text_message_not_read);
         /*   TimeStamp=(TextView)convertView.findViewById(R.id.message_timestamp);*/
            /*unreadCount=(TextView)convertView.findViewById(R.id.unread_count);*/
            name = (TextView) convertView.findViewById(R.id.contact_name);
            image_type = (ImageView) convertView.findViewById(R.id.image_type);
        }
    }

    public ContactsListAdapter(Context mContext, List<ContactModel> albumList, String active_tab) {
        this.context = mContext;
        this.albumList = albumList;
        this.active_tab=active_tab;
        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        itemsPendingRemoval = new ArrayList<ContactModel>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_chat_list_listview_item, parent, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder view_holder, int position) {
        try{
            final int pos = position;
            if(view_holder instanceof MyViewHolder){
                MyViewHolder myViewHolder=(MyViewHolder)view_holder;
                final ContactModel contactModel=albumList.get(pos);
              /*  myViewHolder.TimeStamp.setText(getTimeStamp(contactModel.getCreated_at()));*/
                if(contactModel.getMessage_type().equalsIgnoreCase(GlobalVariables.TYPE_IMAGE)){
                   /* myViewHolder.lastTextMessage.setText("Photo");*/
                    myViewHolder.image_type.setVisibility(View.VISIBLE);
                    myViewHolder.lastTextMessage.setVisibility(View.GONE);
                    myViewHolder.last_text_message_not_read.setVisibility(View.GONE);

                    if(!contactModel.is_read()){
                        myViewHolder.is_read.setChecked(true);
                    }
                }else{
                    myViewHolder.lastTextMessage.setText(contactModel.getMessage());
                    myViewHolder.last_text_message_not_read.setText(contactModel.getMessage());
                    if(myViewHolder.image_type.getVisibility()==View.VISIBLE)
                        myViewHolder.image_type.setVisibility(View.GONE);
                    if(!contactModel.is_read()){
                        myViewHolder.lastTextMessage.setVisibility(View.GONE);
                        myViewHolder.last_text_message_not_read.setVisibility(View.VISIBLE);
                        myViewHolder.is_read.setChecked(true);
                    }else{
                        myViewHolder.lastTextMessage.setVisibility(View.VISIBLE);
                    }
                }
                myViewHolder.name.setText(contactModel.getContact_name());
                String profilePic=contactModel.getContact_image();
             /*   ImageLoader imageLoader = ImageLoader.getInstance();
                DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                        .cacheOnDisc(true).resetViewBeforeLoading(true)
                        .showImageForEmptyUri(R.drawable.squareplaceholder)
                        .showImageOnFail(R.drawable.squareplaceholder)
                        .imageScaleType(ImageScaleType.NONE_SAFE)
                        .showImageOnLoading(R.drawable.squareplaceholder).build();
                //download and display image from url
                imageLoader.displayImage(profilePic,myViewHolder.profilePic, options);*/
                /* String product_url=contactModel.getProduct_image();
                        //download and display image from url
                imageLoader.displayImage(product_url,myViewHolder.product_image, options);*/
                if(!TextUtils.isEmpty(profilePic)){
                    Picasso.with(context)
                            .load(profilePic)
                            .placeholder(R.drawable.squareplaceholder)
                            .fit().centerCrop()
                            .into(myViewHolder.profilePic);
                }else{
                    myViewHolder.profilePic.setImageResource(R.drawable.squareplaceholder);
                }
                ((MyViewHolder) view_holder).view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        User user=new User();
                        user.setName(contactModel.getContact_name());
                        if(active_tab.equalsIgnoreCase(GlobalVariables.TYPE_SELLER_TAB))
                            user.setBs_id(contactModel.getSeller_id());
                        else
                            user.setBs_id(contactModel.getBuyer_id());
                        /*user.setBs_id(contactModel.getSeller_id());*/
                        user.setProfileImageUrl(contactModel.getContact_image());
                        user.setProduct_id(contactModel.getActive_product_id());
                        user.setActive_tab(active_tab);
                        user.setConversation_id(contactModel.getConversation_id());
                        Intent intent= ChatActivity.newInstance(context,user);
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
        return albumList.size();
    }
    // method to access in activity after updating selection
    public List<ContactModel> getProductList() {
        return albumList;
    }

    public static String getTimeStamp(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        /*format.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));*/
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


    public void setUndoOn(boolean undoOn) {
        this.undoOn = undoOn;
    }

    public boolean isUndoOn() {
        return undoOn;
    }

    public void pendingRemoval(int position) {
        final ContactModel item = albumList.get(position);
        if (!itemsPendingRemoval.contains(item)) {
            itemsPendingRemoval.add(item);
            // this will redraw row in "undo" state
            notifyItemChanged(position);
            // let's create, store and post a runnable to remove the item
            Runnable pendingRemovalRunnable = new Runnable() {
                @Override
                public void run() {
                    remove(albumList.indexOf(item));
                }
            };
            handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
            pendingRunnables.put(item, pendingRemovalRunnable);
        }
    }

    public void remove(int position) {
       /* ContactModel item = albumList.get(position);
        if (itemsPendingRemoval.contains(item)) {
            itemsPendingRemoval.remove(item);
        }
        if (albumList.contains(item)) {
            albumList.remove(position);
            notifyItemRemoved(position);
        }*/
        askUser(position);
    }

    public boolean isPendingRemoval(int position) {
        ContactModel item = albumList.get(position);
        return itemsPendingRemoval.contains(item);
    }
  public void askUser(final int position){
      final AlertDialog dialog = new AlertDialog(context);
      dialog.setTitle("Hide Conversation?");
      dialog.setIcon(R.drawable.ic_alert);
      dialog.setIsCancelable(true);
      dialog.setMessage("Do you really want to hide this Conversation?");
      dialog.setPositiveButton("Yes", new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              delete_conversation(context,active_tab,albumList.get(position).getConversation_id(),position);
              dialog.dismiss();
          }
      });

      dialog.setNegativeButton("No", new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              // this will redraw row in "undo" state
              notifyItemChanged(position);
              dialog.dismiss();
          }
      });
      dialog.show();
  }
    private void delete_conversation(final Context context, String active_tab,String conversation_id,final int position){
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.delete_conversation(context, active_tab,conversation_id, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                Log.d(TAG, "onSuccess Response");
                Toast.makeText(context,"Conversation has been hidden Successfully",Toast.LENGTH_LONG).show();
                ContactModel item = albumList.get(position);
                if (itemsPendingRemoval.contains(item)) {
                    itemsPendingRemoval.remove(item);
                }
                if (albumList.contains(item)) {
                    albumList.remove(position);
                    notifyItemRemoved(position);
                }
            }
            @Override
            public void OnFailureFromServer(String msg) {
                Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
                // this will redraw row in "undo" state
                notifyItemChanged(position);
                Log.d(TAG, msg);
            }
            @Override
            public void OnError(String msg) {
                Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
                // this will redraw row in "undo" state
                notifyItemChanged(position);
                Log.d(TAG, msg);
            }
        }, "Delete Conversation");
    }
}
