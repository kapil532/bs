package com.blueshak.app.blueshak.home.presenter;

import android.content.Context;
import android.view.View;

import com.blueshak.app.blueshak.base.PresenterCallBack;
import com.blueshak.app.blueshak.home.model.FeatureItemsModel;
import com.blueshak.app.blueshak.paypal.model.FeaturedOptionModel;
import com.blueshak.app.blueshak.services.ServerResponseInterface;
import com.blueshak.app.blueshak.services.ServicesMethodsManager;
import com.blueshak.app.blueshak.services.model.FilterModel;
import com.blueshak.app.blueshak.services.model.HomeListModel;
import com.blueshak.app.blueshak.util.BlueShakLog;
import com.google.gson.Gson;

/**
 * Created by lal babu on 30/01/18.
 */

public class ItemListPresenter {
    private static Gson gson = new Gson();
    public void getFeatureItemLists(final Context context, FilterModel filterModel, final PresenterCallBack presenterCallBack,String take) {
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getFeatureItemsList(context, filterModel, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                FeatureItemsModel featureItem = gson.fromJson(arg0.toString(), FeatureItemsModel.class);
                presenterCallBack.onSuccess(featureItem);
            }

            @Override
            public void OnFailureFromServer(String msg) {
                presenterCallBack.onFailure();
            }

            @Override
            public void OnError(String msg) {
                presenterCallBack.onFailure();
            }
        }, "Feature Item list",take);

    }

    public void getFeatureItemPriceOption(final Context context, final PresenterCallBack presenterCallBack) {
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getFeatureWeekPriceOption(context, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                FeaturedOptionModel featureItem = gson.fromJson(arg0.toString(), FeaturedOptionModel.class);
                presenterCallBack.onSuccess(featureItem);
            }

            @Override
            public void OnFailureFromServer(String msg){
                presenterCallBack.onFailure();
            }

            @Override
            public void OnError(String msg){
                presenterCallBack.onFailure();
            }
        }, "Feature Item price option");

    }

    public void postFeatureItemPayment(final Context context, final PresenterCallBack presenterCallBack,
                                       String optionId,String prIdoduct,String payPalId) {
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.postFeatureItemPayment(context, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                //FeaturedOptionModel featureItem = gson.fromJson(arg0.toString(), FeaturedOptionModel.class);
                presenterCallBack.onSuccess("");
            }

            @Override
            public void OnFailureFromServer(String msg){
                presenterCallBack.onFailure();
            }

            @Override
            public void OnError(String msg){
                presenterCallBack.onFailure();
            }
        },optionId, prIdoduct,payPalId,"Feature Item price option");

    }
}
