package com.langoor.app.blueshak.Messaging.util;

/**
 * Created by Bryan Yang on 9/18/2015.
 */
public class URLUtil {

    public final static String APPGRID_BASE_URL = "https://appgrid-api.cloud.accedo.tv/";
    public final static String PUSHWHOOSH_CREATE_MESSAGE = "https://cp.pushwoosh.com/json/1.3/createTargetedMessage";
    public final static String PUSHWHOOSH_SET_TAG= "https://cp.pushwoosh.com/json/1.3/setTags";
    public final static String PUSHWHOOSH_GET_TAG= "https://cp.pushwoosh.com/json/1.3/getTags";
    public final static String PUSHWHOOSH_UN_REGISTERE_DEVICE= "https://cp.pushwoosh.com/json/1.3/unregisterDevice";
    public static final String ACTION_REGISTER = "tv.accedo.itnsconnect.REGISTER";
    public static final String EXTRA_STATUS = "status";
    public static final int STATUS_SUCCESS = 1;
    public static final int STATUS_FAILED = 0;


    public static String getNewSessionUrl()
    {
        return APPGRID_BASE_URL + "session";
    }

    public static String getDeviceRegistrationUrl()
    {
        return APPGRID_BASE_URL + "plugin/nativepush/registration";
    }

    public static String getSendGCMMessageUrl(String targetUuid)
    {
        return APPGRID_BASE_URL + "plugin/nativepush/notification/" + targetUuid;                                  
    }

    public static String getLogOutUrl(String hardwareID)
    {
        return APPGRID_BASE_URL + "plugin/nativepush/registration/" + hardwareID;                                  
    }

}
