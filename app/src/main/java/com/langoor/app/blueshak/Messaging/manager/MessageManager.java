package com.langoor.app.blueshak.Messaging.manager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.langoor.blueshak.R;
import com.google.gson.Gson;
import com.langoor.app.blueshak.AppController;
import com.langoor.app.blueshak.Messaging.activity.ChatActivity;
import com.langoor.app.blueshak.Messaging.activity.MessageActivity;
import com.langoor.app.blueshak.Messaging.data.ChatRoom;
import com.langoor.app.blueshak.Messaging.data.GcmMessage;
import com.langoor.app.blueshak.Messaging.data.Message;
import com.langoor.app.blueshak.Messaging.data.PushSenderRequest;
import com.langoor.app.blueshak.Messaging.data.User;
import com.langoor.app.blueshak.Messaging.helper.Constants;
import com.langoor.app.blueshak.Messaging.util.CommonUtil;
import com.langoor.app.blueshak.Messaging.util.URLUtil;
import com.langoor.app.blueshak.global.GlobalFunctions;
import com.langoor.app.blueshak.global.GlobalVariables;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by Bryan Yang on 9/21/2015.
 */
public class MessageManager {

    /*
         Implement this interface to update UI
     */

    boolean  isValidMessage=true;
    public interface MessageListener
    {
        void onReceiveMessage(Message receivedMessage);
        void onSendMessageResult(boolean isSuccess, String extraInfo);
    }
    public interface MessageListenerOne
    {
        void onReceiveMessageOne(Message receivedMessage);
        //public void onSendMessageResult(boolean isSuccess, String extraInfo);
    }
    public interface OnNotificationReceived
    {
        void onNotificationReceived(User receivedMessage);
        //public void onSendMessageResult(boolean isSuccess, String extraInfo);
    }


    private static MessageManager mInstance;
    private static Context mCtx;
    private MessageListener mMessageListener;
    private MessageListenerOne mMessageListenerOne;
    private OnNotificationReceived onNotificationReceived;
    GcmMessageBroadcastReceiver mGCMReceiver;

    private MessageManager(Context context)
    {
        mCtx = context.getApplicationContext();
    }
    public void setMessageListenerOne(MessageListenerOne listener)
    {
        this.mMessageListenerOne = listener;
    }

    public static synchronized MessageManager getInstance(Context context)
    {
        if (mInstance == null)
            mInstance = new MessageManager(context);

        return mInstance;
    }

    public void registerGCMBroadcastReceiver()
    {
        if(mGCMReceiver == null)
            mGCMReceiver = new GcmMessageBroadcastReceiver();

        IntentFilter intentFilter = new IntentFilter(Constants.BROADCAST_ACTION_NOTIFICATION);
        mCtx.registerReceiver(mGCMReceiver, intentFilter);
    }

    public void unregisterGCMBroadcastReceiver()
    {
        if((mGCMReceiver != null) && (mCtx != null))
            mCtx.unregisterReceiver(mGCMReceiver);
    }


    public OnNotificationReceived getOnNotificationReceived() {
        return onNotificationReceived;
    }

    public void setOnNotificationReceived(OnNotificationReceived onNotificationReceived) {
        this.onNotificationReceived = onNotificationReceived;
    }

    public void setMessageListener(MessageListener listener)
    {

        this.mMessageListener = listener;
    }

    public void sendMessage(Message messageToSend, String sender_id)
    {
        if(mMessageListenerOne!=null)
            mMessageListenerOne.onReceiveMessageOne(messageToSend);
        //sendMessageToGCM(messageToSend);
        sendMessageToPushWoosh(messageToSend,sender_id);

    }


    private void sendMessageToPushWoosh(Message messageToSend,String cc_id) {
        String text = messageToSend.getMessageString();
        //String toUser = mTargetUser.getName().toLowerCase();
        String toUser=cc_id;
        Gson gson = new Gson();
        //Message message = new Message(text, UserManager.getInstance(mCtx).getSignedInUser(), new Date());
        String data = gson.toJson(messageToSend);
        //new PushSenderRequest()
        String request = gson.toJson(new PushSenderRequest(toUser, data));
        System.out.println("########Message request########"+request.toString());
        JSONObject jsonRequest;
        JsonObjectRequest jsonObjectRequest = null;
        try {
            jsonRequest = new JSONObject(request);
            jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                URLUtil.PUSHWHOOSH_CREATE_MESSAGE,
                jsonRequest,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject res = response;
                        System.out.println("########Message Sent########"+response.toString());
                        if(mMessageListener != null)
                            mMessageListener.onSendMessageResult(true, "Message Sent");


                    }
                }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(mMessageListener != null)
                        mMessageListener.onSendMessageResult(false, "Failed to send message");
                    String file_string ="";
                    NetworkResponse response = error.networkResponse;
                    if(response != null && response.data != null)
                    {
                        try
                        {
                            file_string = new String(error.networkResponse.data, "UTF-8");
                            JSONObject errorObj= new JSONObject(file_string);
                            String error_res;

                            if(errorObj.has("error")){
                                error_res  =errorObj.getString("error");
                            }else{
                                error_res="";
                            }
                           // Log.d("##########SS######", "SSS---S" + file_string);

                        }
                        catch (UnsupportedEncodingException e)
                        {
                        } catch (JSONException e)
                        {
                        }
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObjectRequest != null) {
            VolleyManager.getInstance(mCtx).addToRequestQueue(jsonObjectRequest);
        }
    }

  /*  private void sendMessageToGCM(Message messageToSend)
    {
        Gson gson = new Gson();
       // String targetUuid = UserManager.getInstance(mCtx).getTargetUser().getCcid();

        //i have changed here
        String targetUuid =UserManager.getInstance(mCtx).getTargetUser().getCc_id();

        ITNSJSONObjectRequest jsObjRequest = new ITNSJSONObjectRequest
                (Request.Method.POST, URLUtil.getSendGCMMessageUrl(targetUuid), DeviceRegistrationResponse.class, false, gson.toJson(messageToSend), new Response.Listener<DeviceRegistrationResponse>() {
                    @Override
                    public void onResponse(DeviceRegistrationResponse response)
                    {
                        onSendGCMMessageResponse(true);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onSendGCMMessageResponse(false);
                    }
                });

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("X-Session", UserManager.getInstance(mCtx).getCurrentSessionKey());
        jsObjRequest.addHeaders(headers);
        jsObjRequest.setContentType("application/json");
        VolleyManager.getInstance(mCtx).addToRequestQueue(jsObjRequest);
    }
*/
    private void onSendGCMMessageResponse(boolean isSuccess)
    {
        if(mMessageListener != null)
            mMessageListener.onSendMessageResult(isSuccess, isSuccess? "" : "Failed to send message to GCM");
    }




    private class GcmMessageBroadcastReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (intent == null) return;

            if (Constants.BROADCAST_ACTION_NOTIFICATION.equals(intent.getAction()))
            {


                final Bundle extras = intent.getExtras();
                if (extras.keySet().contains(Constants.EXTENDED_DATA_MESSAGE))
                {
                    final String msgString = extras.getString(Constants.EXTENDED_DATA_MESSAGE);



                    final Gson gson = new Gson();
                    GcmMessage gcmMessage = gson.fromJson(msgString, GcmMessage.class);

                    if(gcmMessage != null)
                    {
                        User sender = new User(gcmMessage.getSenderFirstName(),null,gcmMessage.getSenderNumber(),
                                gcmMessage.getSenderCc_id(), gcmMessage.getSenderImageUrl());
                        // Message receivedMessage = new Message(gcmMessage.getMessageString(), sender, gcmMessage.getSendTime());
                        Message receivedMessage = new Message(gcmMessage.getMessageString(), sender, gcmMessage.getSendTime(),gcmMessage.getMessageToken(),gcmMessage.getMessageId());
                       // Message messageToSend = new Message(mInputMessage.getText().toString(), UserManager.getInstance(getApplicationContext()).getSignedInUser(), new Date(),msg_token,msg_id,UserManager.getInstance(getApplicationContext()).getTargetUser());

                               /*Before showing the message in app and before sending to Tv make sure that message is authenticated */
                       // doMessageAuthentication(gcmMessage, receivedMessage,);

                            /* sendMessageToSamsungTV(gcmMessage);

                            if(gcmMessage.isMessage() && (mMessageListener != null))
                                mMessageListener.onReceiveMessage(receivedMessage);*/


                    }

                    // stop propagate to the rest of receivers
                    abortBroadcast();                    
                }
            }
        }

    }
    public void doMessageAuthentication(final GcmMessage gcmMessage,final Message receivedMessage,final Context ctx,User sender){
        AppController.BaseActivityLifeCycleListener baseActivityLifeCycleListener=  new AppController.BaseActivityLifeCycleListener();
        Gson sender_=new Gson();
        String sender_data=sender_.toJson(sender);
        if(gcmMessage.isMessage() && (mMessageListener != null))
            mMessageListener.onReceiveMessage(receivedMessage);

        if(baseActivityLifeCycleListener.isChatActivityVisible()){
            Log.d("isChatActivityVisible","isChatActivityVisible");
            User TargetUser= UserManager.getInstance(ctx).getTargetUser();
            if(TargetUser!=null){
                if(!(Integer.parseInt(receivedMessage.getSender().getBs_id()) == Integer.parseInt(UserManager.getInstance(ctx).getTargetUser().getBs_id()))){
                    Log.d("isChatActivityVisible","Different so user build the Notification");
                    sendNotification(sender,receivedMessage.getSender().getName(), receivedMessage.getMessageString(),receivedMessage.getType());
                }
            }
        }else if(baseActivityLifeCycleListener.isRecentChatsActivityActivityVisible()){
            Log.d("isRecentChatsA","isRecentChatsActivityActivityVisible");
            if(mMessageListenerOne!=null)
                mMessageListenerOne.onReceiveMessageOne(receivedMessage);
            sendNotification(sender,receivedMessage.getSender().getName(), receivedMessage.getMessageString(),receivedMessage.getType());
        }else{
            Log.d("Nothing is visible","Build the Notification");
            sendNotification(sender,receivedMessage.getSender().getName(), receivedMessage.getMessageString(),receivedMessage.getType());
        }
    }

    public void notify(Context mCtx,User sender){
        Message message=new Message();
        message.setSender(sender);
        if((mMessageListener != null))
            mMessageListener.onReceiveMessage(message);
        if(onNotificationReceived!=null)
            onNotificationReceived.onNotificationReceived(sender);

      /*  if(mMessageListenerOne!=null)
            mMessageListenerOne.onReceiveMessageOne(message);*/

    }

    /**
     * Send simple notification using the NotificationCompat API.
     */
    public void sendNotification(User user,String Title,String Text,String type) {

        //UserManager.getInstance(getApplicationContext()).isFromNotification(true);
        // Use NotificationCompat.Builder to set up our notification.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mCtx);

        //icon appears in device notification bar and right hand corner of notification
        builder.setSmallIcon(R.drawable.ic_app_icon);

        Intent intent= ChatActivity.newInstance(mCtx,user);

       /* Intent intent = new Intent(mCtx, ChatActivity.class);
        intent.putExtra("Notification",user);*/

        PendingIntent pendingIntent = PendingIntent.getActivity(mCtx, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
        // Set the intent that will fire when the user taps the notification.
        builder.setContentIntent(pendingIntent);

        // Large icon appears on the left of the notification
        builder.setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.drawable.ic_app_icon));

        // Content title, which appears in large type at the top of the notification
        builder.setContentTitle(Title);

        // Content text, which appears in smaller text below the title
        if(type.equalsIgnoreCase(GlobalVariables.TYPE_IMAGE))
            builder.setContentText("Image");
        else
            builder.setContentText(Text);


        NotificationManager notificationManager = (NotificationManager) mCtx.getSystemService(mCtx.NOTIFICATION_SERVICE);
        Notification notification=builder.build();
        notification.defaults = Notification.DEFAULT_SOUND
                | Notification.DEFAULT_VIBRATE;
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        // Will display the notification in the notification bar
        notificationManager.notify(Constants.NOTIFICATION_ID, notification);
    }
}
