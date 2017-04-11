package com.langoor.app.blueshak.Messaging.util;

/**
 *    Just like the java.util.concurrent.Callable
 *    but can pass an Object as param when called
 *
 *    Created by Bryan Yang on 9/18/2015.
 */
public interface CallableWithParam {
     void call(Object param) ;
}
