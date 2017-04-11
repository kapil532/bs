package com.langoor.app.blueshak.Messaging.data;


import com.langoor.app.blueshak.Messaging.helper.Constants;

/**
 * Created by atabek on 02/25/2016.
 */
public class PushSenderRequest {
    public Request request;

    public PushSenderRequest(String username, String data) {
        request = new Request(username, data);
    }

    public class Request {
        public String auth;
        public String send_date;
        public String content;
        public String devices_filter;

        public Request(String username, String data) {
            this.auth = Constants.PUSHWHOOSH_API_KEY;
            this.send_date = "now";
            this.content = data;
            this.devices_filter = "A(\"" + Constants.PUSHWHOOSH_APP_CODE + "\") * T(\"user_name\", EQ, \"" + username + "\")";
           // this.devices_filter = "A(\"" + Constants.PUSHWHOOSH_APP_CODE + "\") * T(\"user_name\", EQ, \"" + "38" + "\")";
            //this.devices_filter = "A(\"" + Constants.PUSHWHOOSH_APP_CODE + "\") * T(\"hwid\", EQ, \"" + Constants.PUSHWHOOSH_hwid_KEY_abhinva+ "\")";
        }
    }
}