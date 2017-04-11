package com.langoor.app.blueshak.Messaging.data;

import java.io.Serializable;

/**
 * Created by Bryan Yang on 9/18/2015.
 */
public class NewSessionResponse implements Serializable{

    String sessionKey;
    String expiration;

    public String getSessionKey()
    {
        return sessionKey == null ? "": sessionKey;
    }
}
