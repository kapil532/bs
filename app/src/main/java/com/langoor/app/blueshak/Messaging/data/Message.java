package com.langoor.app.blueshak.Messaging.data;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.langoor.app.blueshak.Messaging.util.CommonUtil;

import java.util.Date;


/**
 * Message object send/receive through AppGrid Push Notification Service
 * Created by Manu on 22/9/15.
 */

public class Message {
    public String getMessage_image_url() {
        return message_image_url;
    }

    public void setMessage_image_url(String message_image_url) {
        this.message_image_url = message_image_url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    String content,mMessageReceiverName,mMessageReceiverProfileUrl,type,message_image_url;
    JsonObject data;
    String id;
    User mTargetUser;

    int mMessageReceiverCc_id;

    transient User sender; // Not part of the Json Object
    transient Date sendTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public Message(){

    }

    public String getContent()
    {
        return this.content;
    }
    public Message(String id, String message, Date createdAt, User sender/* CharlieChatterContact mTargetUser*/, int mMessageReceiverCc_id) {
        this.id = id;
        this.content = message;
        this.sendTime = createdAt;
        this.sender = sender;
      /*  this.mTargetUser=mTargetUser;*/
        this.mMessageReceiverCc_id=mMessageReceiverCc_id;
    }


        //testing
        public Message(String messageString, User sender, Date sentAt, String token, String message_id , User mTargetUser,String message_type,String image_url){
            this.content = messageString;
            this.data = new JsonObject();
            JsonObject itns = new JsonObject();
            itns.addProperty("ui_template_id",1);
            itns.addProperty("type","message");
            itns.addProperty("message_type",message_type);
            itns.addProperty("message_image_url",image_url);
            itns.addProperty("created", CommonUtil.fromDateToString(sentAt));
            itns.addProperty("expiry", CommonUtil.fromDateToString(new Date(sentAt.getTime() + 10*60*1000)));
            itns.addProperty("message_id",message_id);
            itns.addProperty("token",token);
            itns.addProperty("send_by",sender.getBs_id());
            itns.addProperty("conversation_id",mTargetUser.getConversation_id());
            itns.addProperty("active_tab",mTargetUser.getActive_tab());
           /* itns.addProperty("send_to",mTargetUser.getCc_id());*/
            itns.addProperty("imageUrl",sender.getProfileImageUrl());

            JsonObject embedData = new JsonObject();
            embedData.addProperty("firstName", sender.getName().trim());
            embedData.addProperty("lastName", sender.getName());
            embedData.addProperty("text", messageString);
            embedData.addProperty("imageUrl", sender.getProfileImageUrl().toString());
            embedData.addProperty("Number", sender.getNumber());
            embedData.addProperty("sender_cc_id",sender.getBs_id());
            itns.add("data", embedData);
            String itnsString = (new Gson()).toJson(itns);
            data.addProperty("itns", itnsString);

            this.sender = sender;
            this.sendTime = sentAt;
            this.mTargetUser=mTargetUser;
        }

    public Message (String messageString, User sender, Date sentAt, String token, String message_id){
        this.content = messageString;
        this.data = new JsonObject();
        JsonObject itns = new JsonObject();
        itns.addProperty("ui_template_id",1);
        itns.addProperty("type","message");
        itns.addProperty("message_type",type);
        itns.addProperty("message_image_url",message_image_url);
        itns.addProperty("created", CommonUtil.fromDateToString(sentAt));
        itns.addProperty("expiry", CommonUtil.fromDateToString(new Date(sentAt.getTime() + 10 * 60 * 1000)));
        itns.addProperty("message_id",message_id);
        itns.addProperty("token",token);
        itns.addProperty("send_by", sender.getBs_id());
        itns.addProperty("imageUrl",sender.getProfileImageUrl());
        JsonObject embedData = new JsonObject();
        embedData.addProperty("firstName", sender.getName());
        embedData.addProperty("lastName", sender.getName());
        embedData.addProperty("text", messageString);
        embedData.addProperty("imageUrl", sender.getProfileImageUrl().toString());
        //added by for message authentication
        embedData.addProperty("Number", sender.getNumber());
        embedData.addProperty("sender_cc_id",sender.getBs_id());
        itns.add("data", embedData);
        String itnsString = (new Gson()).toJson(itns);
        data.addProperty("itns", itnsString);
        this.sender = sender;
        this.sendTime = sentAt;
    }

        public String getMessageString()
    {
        return this.content;
    }

        public User getSender()
    {
        return this.sender;
    }
        public int getMessageReceiverCc_id()
    {
        return this.mMessageReceiverCc_id;
    }
        public String getMessageReceiverName()
    {
        return this.mMessageReceiverName;
    }
        public String getMessageReceiverProfileUrl()
    {
        return this.mMessageReceiverProfileUrl;
    }
        public Date getSendTime()
    {
        return this.sendTime;
    }

        public JsonObject getData() {
        return data;
    }

   public User getTargetUser()
   {
       return this.mTargetUser;
   }

}
