package com.langoor.app.blueshak.Messaging.data;

import java.io.Serializable;

/**
 * Created by Lincoln on 07/01/16.
 */
public class ChatRoom implements Serializable {
    String name, lastMessage, timestamp,profileImageUrl,User,product_image;
    int unreadCount=0,bs_id,mMessageReceiverbs_id;
    com.langoor.app.blueshak.Messaging.data.User sender;

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    Boolean is_buyer=false;

    public Boolean is_Buyer() {
        return is_buyer;
    }

    public void is_Buyer(Boolean is_buyer) {
        this.is_buyer = is_buyer;
    }

    public ChatRoom() {

    }

    public ChatRoom(int bs_id, com.langoor.app.blueshak.Messaging.data.User sender, String name, String lastMessage, String timestamp, String profileImageUrl, Boolean is_buyer,String product_image/*, int unreadCount*//*,int mMessageReceiverbs_id, CharlieChatterContact mTargetUser*/ ) {
        this.bs_id = bs_id;
        this.name = name;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
        //this.unreadCount = unreadCount;
        this.profileImageUrl=profileImageUrl;
     /*   this.mMessageReceiverbs_id=mMessageReceiverbs_id;
        this.mTargetUser=mTargetUser;
   */     this.sender=sender;
        this.is_buyer=is_buyer;
        this.product_image=product_image;

    }
    public String getProfileImageUrl() {
        return profileImageUrl;
    }
    public int getbs_id() {
        return bs_id;
    }

    public void setbs_id(int bs_id) {
        this.bs_id = bs_id;
    }

    public com.langoor.app.blueshak.Messaging.data.User getSender() {
        return sender;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    public void setProfileImageUrl(String ProfileImageUrl) {
        this.profileImageUrl = ProfileImageUrl;
    }

    public int getMessageReceiverbs_id() {
        return this.mMessageReceiverbs_id ;
    }

}
