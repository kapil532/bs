package com.langoor.app.blueshak.services;

/**
 * Created by Spurthi
 * Version 2.0 ,Brighter India Foundation , Bangalore
 * on 1/12/2016.
 */
public interface ServerResponseInterface {

    public void OnSuccessFromServer(Object arg0);

    public void OnFailureFromServer(String msg);

    public void OnError(String msg);

}
