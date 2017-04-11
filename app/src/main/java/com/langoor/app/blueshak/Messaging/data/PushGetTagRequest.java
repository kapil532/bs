package com.langoor.app.blueshak.Messaging.data;


import com.langoor.app.blueshak.Messaging.helper.Constants;

/**
 * Created by atabek on 02/25/2016.
 */
public class PushGetTagRequest {
    public Request request;

    public PushGetTagRequest(String hwid) {
        request = new Request(hwid);
    }

    public class Request {
       /* {
            "request":{
            "application":"DEAD0-BEEF0",
                    "hwid": "device hardware id",
                    "tags": {
                "StringTag": "string value",
                        "IntegerTag": 42,
                        "ListTag": ["string1","string2"],
                "DateTag": "2015-10-02 22:11", //note the time is in UTC
                        "BooleanTag": true,  // valid values are - true, 1, false, 0, null
            }
        }
        }*/

        public String application;
        public String hwid;
        public Request1 tags;
        public Request(String hwid) {
            this.application = Constants.PUSHWHOOSH_APP_CODE;
            this.hwid = hwid;
            //this.tags = new Request1(Username);
                  }
        public class Request1{
            public String Username;
            Request1(String Username){
                this.Username=Username;
            }

        }
    }
}