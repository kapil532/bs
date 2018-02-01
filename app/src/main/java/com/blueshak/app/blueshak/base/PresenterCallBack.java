package com.blueshak.app.blueshak.base;

/**
 * Created by lsingh013 on 30/01/18.
 */

public interface PresenterCallBack<T> {
    void onSuccess(T response);
    void onFailure();
}
