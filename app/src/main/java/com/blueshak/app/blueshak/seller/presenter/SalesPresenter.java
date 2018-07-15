package com.blueshak.app.blueshak.seller.presenter;

import android.content.Context;

import com.blueshak.app.blueshak.base.PresenterCallBack;
import com.blueshak.app.blueshak.home.model.FeatureItemsModel;
import com.blueshak.app.blueshak.seller.model.Data;
import com.blueshak.app.blueshak.seller.model.SalesDetailsModel;
import com.blueshak.app.blueshak.services.ServerResponseInterface;
import com.blueshak.app.blueshak.services.ServicesMethodsManager;
import com.blueshak.app.blueshak.services.model.FilterModel;
import com.google.gson.Gson;

/**
 * Created by lsingh013 on 26/05/18.
 */

public class SalesPresenter {
    private static Gson gson = new Gson();
    public void getSalesDetails(final Context context, String shop_id,String latitude,String longitude, final PresenterCallBack presenterCallBack) {
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getSalesDetails(context, shop_id,latitude,longitude, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                SalesDetailsModel salesDetails = gson.fromJson(arg0.toString(), SalesDetailsModel.class);
                presenterCallBack.onSuccess(salesDetails);
            }

            @Override
            public void OnFailureFromServer(String msg) {
                presenterCallBack.onFailure();
            }

            @Override
            public void OnError(String msg) {
                presenterCallBack.onFailure();
            }
        }, "Sales Details");

    }
}
