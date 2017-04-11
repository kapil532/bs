package com.langoor.app.blueshak.Messaging.data;


import com.langoor.app.blueshak.Messaging.helper.Constants;

/**
 * Created by atabek on 02/25/2016.
 */
public class PushUnRegisterDeviceRequest {

    public Request request;
    public PushUnRegisterDeviceRequest(String hwid) {
        request = new Request(hwid);
    }
    public class Request {
        public String application;
        public String hwid;
        public Request(String hwid) {
            this.application = Constants.PUSHWHOOSH_APP_CODE;
            this.hwid = hwid;
        }
    }
}