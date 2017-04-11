package com.langoor.app.blueshak.Messaging.data;



import com.langoor.app.blueshak.Messaging.util.CommonUtil;

import java.util.Date;


/**
 *    Represents the data received from GCM
 */

public class GcmMessage {

    String senderId;
    String title;
    Date sentAt;
    ITNS itns;

    public GcmMessage()
    {
        // for gson
    }

    public GcmMessage(ITNS itnsObject)
    {
        this.itns = itnsObject;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getSenderFirstName()
    {
        try{
            return itns.data.firstName;
        }catch (Exception e) { }
        
        return "";
    }

    public String getSenderLastName()
    {
        try{
            return itns.data.lastName;
        }catch (Exception e) { }

        return "";
    }

    public String getSenderImageUrl()
    {
        try{
            return itns.imageUrl;
        }catch (Exception e) { }

        return "";
    }
    public String getMessage_imageUrl()
    {
        try{
            return itns.message_imageUrl;
        }catch (Exception e) { }

        return "";
    }
    public String getSenderCc_id()
    {
        try{
            return itns.data.sender_cc_id;
        }catch (Exception e) { }

        return "";
    }
    public String getSenderNumber()
    {
        try{
            return itns.data.Number;
        }catch (Exception e) { }

        return "";
    }

    public String getMessageString() {
        try{
            return itns.data.text;
        }catch (Exception e) {
        }
        return title;
    }
    public String getMessageId() {
        try{
            return itns.message_id;
        }catch (Exception e) {
        }
        return title;
    }
    public String getMessageToken() {
        try{
            return itns.token;
        }catch (Exception e) {
        }
        return title;
    }
    public String getSendBy() {
        try{
            return itns.send_by;
        }catch (Exception e) {
        }
        return title;
    }
    public String getConversationId() {
        try{
            return itns.conversation_id;
        }catch (Exception e) {
        }
        return title;
    }
    public String getActiveTab() {
        try{
            return itns.active_tab;
        }catch (Exception e) {
        }
        return title;
    }
    public String getSendto() {
        try{
            return itns.send_to;
        }catch (Exception e) {
        }
        return title;
    }

    public Date getSendTime()
    {
        return CommonUtil.fromStringToDate(itns.created);
    }

    public ITNS getITNSData()
    {
        return this.itns;
    }

    public boolean isMessage()
    {
        try{
            if(itns.type.equalsIgnoreCase("message"))
                return true;
        }catch (Exception e){ }

        return false;
    }
    public boolean isTextMessage()
    {
        try{
            if(itns.message_type.equalsIgnoreCase("text"))
                return true;
        }catch (Exception e){ }

        return false;
    }
    public String getMessage_type() {
        try{
            return itns.message_type;
        }catch (Exception e) {
        }
        return title;
    }
    public class ITNS {
        public int ui_template_id;
        public String type;
        public String message_type;
        public String created;
        public String expiry;
        public String message_id;
        public String token;
        public ITNSData data;
        public String send_by;
        public String send_to;
        public String conversation_id;
        public String active_tab;
        public String imageUrl;
        public String message_imageUrl;
       // public User sender;
    }

    public class ITNSData{
        public String firstName;
        public String lastName;
        public String text;
        public String imageUrl;
        public String Number;
        public String sender_cc_id;
    }

    
}
