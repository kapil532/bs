package com.langoor.app.blueshak.services.model;

import android.util.Log;

import com.langoor.app.blueshak.AppController;

import org.json.JSONObject;

import java.io.Serializable;

public class OTPCheckerModel implements Serializable {
    private final String TAG = "OTPCheckerModel";
    private final String STATUS="status";
    private final String BS_ID="bs_id";
    private final String   ISD ="isd";
    private final String PHONE="phone";
    private final String IS_OTP_VERIFIED="is_otp_verified";
    private final String MESSAGE="message";
    private final String IS_USER_ACTIVATED="is_user_activated";

    public String getBs_id() {
        return bs_id;
    }

    public void setBs_id(String bs_id) {
        this.bs_id = bs_id;
    }

    private final String OTP = "otp";

    String phone=null, otp=null,bs_id=null,message=null,isd=null;
    boolean is_otp_verified=false,is_user_activated=false,status=false;

    public OTPCheckerModel(){}

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean is_otp_verified() {
        return is_otp_verified;
    }

    public boolean is_user_activated() {
        return is_user_activated;
    }

    public String getMessage() {
        return message;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public void setIs_otp_verified(boolean is_otp_verified) {
        this.is_otp_verified = is_otp_verified;
    }

    public void setIs_user_activated(boolean is_user_activated) {
        this.is_user_activated = is_user_activated;
    }

    public OTPCheckerModel(String phone, String otp){

        this.phone = phone;
        this.otp = otp;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOTP() {
        return otp;
    }

    public void setOTP(String otp) {
        this.otp = otp;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getIsd() {
        return isd;
    }

    public void setIsd(String isd) {
        this.isd = isd;
    }

    public boolean toObject(String jsonObject){
        try{

            JSONObject json = new JSONObject(jsonObject);
            phone = json.getString(PHONE);
            bs_id = json.getString(BS_ID);
            if(json.has(ISD))isd=json.getString(ISD);
            message = json.getString(MESSAGE);
            if(json.getString(STATUS).equalsIgnoreCase("success"))status = true;
           /* if(json.has(OTP))otp = json.getString(OTP);*/
            int temp = 0;
            try{temp = json.getInt(IS_OTP_VERIFIED);}catch(Exception e){temp =0;}if(temp>0){is_otp_verified=true;}else{is_otp_verified=false;}temp=0;
            try{temp = json.getInt(IS_USER_ACTIVATED);}catch(Exception e){temp =0;}if(temp>0){is_user_activated=true;}else{is_user_activated=false;}temp=0;
            return true;
        }catch(Exception ex){
            Log.d(TAG, "Json Exception : " + ex);
        }
        return false;
    }

    @Override
    public String toString(){
        String returnString = null;
        try{
            JSONObject jsonMain = new JSONObject();
            jsonMain.put(PHONE, phone);
            jsonMain.put(BS_ID, bs_id);
            jsonMain.put(ISD, isd);
            jsonMain.put(OTP, otp);
            jsonMain.put(MESSAGE, message);
            jsonMain.put(IS_USER_ACTIVATED, is_user_activated);
            jsonMain.put(IS_OTP_VERIFIED, is_otp_verified);
            returnString = jsonMain.toString();
        }
        catch (Exception ex){Log.d(TAG," To String Exception : "+ex);

        }
        return returnString;
    }
}

