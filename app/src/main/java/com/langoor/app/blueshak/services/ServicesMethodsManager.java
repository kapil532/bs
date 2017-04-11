package com.langoor.app.blueshak.services;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.langoor.app.blueshak.services.model.AskModel;
import com.langoor.app.blueshak.services.model.CurrencyListModel;
import com.langoor.app.blueshak.services.model.VerifyAliasModel;
import com.langoor.blueshak.R;
import com.langoor.app.blueshak.global.GlobalFunctions;
import com.langoor.app.blueshak.global.GlobalVariables;
import com.langoor.app.blueshak.services.model.ContactsListModel;
import com.langoor.app.blueshak.services.model.ConversationIdModel;
import com.langoor.app.blueshak.services.model.CreateMessageModel;
import com.langoor.app.blueshak.services.model.HomeListModel;
import com.langoor.app.blueshak.services.model.LocationModel;
import com.langoor.app.blueshak.services.model.BookmarkListModel;
import com.langoor.app.blueshak.services.model.CategoryListModel;
import com.langoor.app.blueshak.services.model.CreateImageModel;
import com.langoor.app.blueshak.services.model.CreateProductModel;
import com.langoor.app.blueshak.services.model.CreateSalesModel;
import com.langoor.app.blueshak.services.model.ErrorModel;
import com.langoor.app.blueshak.services.model.FacebookRegisterModel;
import com.langoor.app.blueshak.services.model.FilterModel;
import com.langoor.app.blueshak.services.model.ForgotPasswordModel;
import com.langoor.app.blueshak.services.model.IDModel;
import com.langoor.app.blueshak.services.model.ItemListModel;
import com.langoor.app.blueshak.services.model.LoginModel;
import com.langoor.app.blueshak.services.model.MakeOfferModel;
import com.langoor.app.blueshak.services.model.MarkInApproModel;
import com.langoor.app.blueshak.services.model.MarkSoldModel;
import com.langoor.app.blueshak.services.model.MessageConversationModel;
import com.langoor.app.blueshak.services.model.MessageModel;
import com.langoor.app.blueshak.services.model.MyItemListAvailableModel;
import com.langoor.app.blueshak.services.model.MyItemListModel;
import com.langoor.app.blueshak.services.model.OTPCheckerModel;
import com.langoor.app.blueshak.services.model.OTPResendModel;
import com.langoor.app.blueshak.services.model.PostcodeListModel;
import com.langoor.app.blueshak.services.model.ProductModel;
import com.langoor.app.blueshak.services.model.ProfileDetailsModel;
import com.langoor.app.blueshak.services.model.RegisterModel;
import com.langoor.app.blueshak.services.model.ReviewsRatingsListModel;
import com.langoor.app.blueshak.services.model.ReviewsRatingsModel;
import com.langoor.app.blueshak.services.model.SalesListModel;
import com.langoor.app.blueshak.services.model.SalesListModelNew;
import com.langoor.app.blueshak.services.model.SalesModel;
import com.langoor.app.blueshak.services.model.SearchListModel;
import com.langoor.app.blueshak.services.model.SearchModel;
import com.langoor.app.blueshak.services.model.ShopModel;
import com.langoor.app.blueshak.services.model.SimilarProductsModel;
import com.langoor.app.blueshak.services.model.StatusModel;
import com.langoor.app.blueshak.services.model.SubscriptionListModel;
import com.langoor.app.blueshak.services.model.UserModel;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class ServicesMethodsManager {

    private ServerResponseInterface mUiCallBack;

    private void setCallbacks(ServerResponseInterface mCallBack) {
        mUiCallBack = mCallBack;
    }

    private void postData(final Context context, final Object obj, String URL, String TAG) {
        if (!(obj instanceof LoginModel) && !(obj instanceof RegisterModel) && !(obj instanceof FacebookRegisterModel)) {
            String token = GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
            if (token != null) {
                URL += "?" + "token=" + token;
            }
        }

        Log.d(TAG, "Object String:" + obj.toString());
        Log.d(TAG, "URL String:" + URL);
        VolleyServices request = new VolleyServices();
        request.setCallbacks(new VolleyServices.ResposeCallBack() {
            @Override
            public void OnSuccess(JSONObject arg0) {
                parseResponse(context, obj, arg0);
            }
            @Override
            public void OnFailure(String cause) {
                mUiCallBack.OnFailureFromServer(cause);
            }
            @Override
            public void OnFailure(int cause) {
                mUiCallBack.OnFailureFromServer(context.getString(cause));
            }
        });
        request.setBody(obj.toString());
        request.makeJsonPostRequest(context, URL, TAG);
    }

    private void postData(final Context context, final Object obj, String URL, String params, String TAG) {
        if (params != null) {
            if (!params.equalsIgnoreCase("")) {
                URL += "?" + params;
            }
        }
        /*if(!(obj instanceof LoginModel) && !(obj instanceof RegisterModel)){
            String token =  GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
            if(token!=null){URL += "?"+"token="+ token;}
        }*/

        VolleyServices request = new VolleyServices();
        request.setCallbacks(new VolleyServices.ResposeCallBack() {
            @Override
            public void OnSuccess(JSONObject arg0) {
                System.out.println("####arg0#######" + arg0.toString());
                parseResponse(context, obj, arg0);
            }
            @Override
            public void OnFailure(String cause) {
                System.out.println("##context.getString(cause)######" + cause);
                mUiCallBack.OnFailureFromServer(cause);
            }
            @Override
            public void OnFailure(int cause) {
                System.out.println("##context.getString(cause)######" + context.getString(cause));
                mUiCallBack.OnFailureFromServer(context.getString(cause));
            }
        });
        System.out.println("##URL#"+URL);
        request.setBody(obj.toString());
        request.makeJsonPostRequest(context, URL, TAG);
    }

    private void getData(final Context context, final Object obj, String URL, String params, String TAG) {
        if (params != null) {
            if (!params.equalsIgnoreCase("")) {
                URL += "?" + params;
            }
        }
        Log.d(TAG,URL);
        VolleyServices request = new VolleyServices();
        request.setCallbacks(new VolleyServices.ResposeCallBack() {
            @Override
            public void OnSuccess(JSONObject arg0) {
                if (obj instanceof CategoryListModel) {
                    GlobalFunctions.setSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_CATEGORIES, arg0.toString());
                } else if (obj instanceof PostcodeListModel) {
                    GlobalFunctions.setSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_POSTALCODES, arg0.toString());
                } else if (obj instanceof CurrencyListModel) {
                    GlobalFunctions.setSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_CURRENCIES, arg0.toString());
                }
                parseResponse(context, obj, arg0);
            }
            @Override
            public void OnFailure(String cause) {
                Log.d("OnFailure","coming in side the OnFailure ########String cause##############");
                mUiCallBack.OnFailureFromServer(cause);
            }
            @Override
            public void OnFailure(int cause) {
                Log.d("OnFailure","coming in side the OnFailure #########int cause#############");
                mUiCallBack.OnFailureFromServer(context.getString(cause));
            }
        });
        if (obj != null) {
            request.setBody(obj.toString());
        }
        request.makeJsonGETRequest(context, URL.trim(), TAG);
    }

    private void parseResponse(Context context, Object obj, JSONObject resp) {
        if (obj instanceof LoginModel || obj instanceof OTPCheckerModel) {
            UserModel user = new UserModel();
            if (resp.has("error")) {
                ErrorModel errorModel = new ErrorModel();
                if (errorModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(errorModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            }else if (user.toObject(resp.toString())) {
                mUiCallBack.OnSuccessFromServer(user);
            } else {
                mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
            }
        }else if ( obj instanceof RegisterModel || obj instanceof FacebookRegisterModel || obj instanceof OTPResendModel) {
            OTPCheckerModel otpCheckerModel = new OTPCheckerModel();
            UserModel userModel=new UserModel();
            if (resp.has("error")) {
                ErrorModel errorModel = new ErrorModel();
                if (errorModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(errorModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            }else if (otpCheckerModel.toObject(resp.toString())) {
                mUiCallBack.OnSuccessFromServer(otpCheckerModel);
            } else {
                if(userModel.toObject(resp.toString())){
                    mUiCallBack.OnSuccessFromServer(userModel);
                }else{
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            }
        }else if ( obj instanceof AskModel) {
            AskModel askModel=new AskModel();
            if (resp.has("error")) {
                ErrorModel errorModel = new ErrorModel();
                if (errorModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(errorModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            }/*else if (user.toObject(resp.toString())) {
                mUiCallBack.OnSuccessFromServer(user);
            }*/ else {
                if (askModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(askModel);
                }else{
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            }
        }  else if (obj instanceof ForgotPasswordModel/* || obj instanceof OTPResendModel || obj instanceof OTPCheckerModel */|| obj instanceof StatusModel) {
            if (resp.has("error")) {
                ErrorModel errorModel = new ErrorModel();
                if (errorModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(errorModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            } else {
                StatusModel statusModel = new StatusModel();
                if (statusModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(statusModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            }

        } else if (obj instanceof ProfileDetailsModel) {
            ProfileDetailsModel user = new ProfileDetailsModel();
            if (user.toObject(resp.toString())) {
                mUiCallBack.OnSuccessFromServer(user);
            } else {
                StatusModel statusModel = new StatusModel();
                if (statusModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(statusModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            }
        } else if (obj instanceof MarkSoldModel) {
            if (resp.has("status")) {
                StatusModel statusModel = new StatusModel();
                if (statusModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(statusModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            }
        }else if (obj instanceof ShopModel) {
            ShopModel shopModel = new ShopModel();
            if (shopModel.toObject(resp.toString())) {
                mUiCallBack.OnSuccessFromServer(shopModel);
            } else {
                StatusModel statusModel = new StatusModel();
                if (statusModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(statusModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            }
        } else if (obj instanceof SalesListModel) {
            SalesListModel salesListModel = new SalesListModel();
            if (salesListModel.toObject(resp.toString())) {
                mUiCallBack.OnSuccessFromServer(salesListModel);
            } else {
                mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
            }
        } else if (obj instanceof ItemListModel) {
            ItemListModel itemListModel = new ItemListModel();
            if (itemListModel.toObject(resp.toString())) {
                mUiCallBack.OnSuccessFromServer(itemListModel);
            } else {
                mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
            }
        } else if (obj instanceof ProductModel) {
            ProductModel productModel = new ProductModel();
            if (productModel.toObject(resp.toString())) {
                mUiCallBack.OnSuccessFromServer(productModel);
            } else {
                mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
            }
        } else if (obj instanceof HomeListModel) {
            HomeListModel homeListModel = new HomeListModel();
            if (homeListModel.toObject(resp.toString())) {
                mUiCallBack.OnSuccessFromServer(homeListModel);
            } else {
                mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
            }
        } else if (obj instanceof SearchModel) {
            HomeListModel homeListModel = new HomeListModel();
            if (homeListModel.toObject(resp.toString())) {
                mUiCallBack.OnSuccessFromServer(homeListModel);
            } else {
                mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
            }
        } else if (obj instanceof SalesListModelNew) {
            SalesListModelNew salesListModelNew = new SalesListModelNew();
            if (salesListModelNew.toObject(resp.toString())) {
                mUiCallBack.OnSuccessFromServer(salesListModelNew);
            } else {
                mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
            }
        } else if (obj instanceof SalesModel) {
            if (resp.has("id")) {
                IDModel idModel = new IDModel();
                if (idModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(idModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            } else if (resp.has("error")) {
                ErrorModel errorModel = new ErrorModel();
                if (errorModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(errorModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            } else if (resp.has("message")) {
                ErrorModel messagemodel = new ErrorModel();
                if (messagemodel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(messagemodel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            }else if (resp.has("sale_type")) {
                SalesModel salesModel = new SalesModel();
                if (salesModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(salesModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            }
            else {
                StatusModel statusModel = new StatusModel();
                if (statusModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(statusModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            }
        } else if (obj instanceof CreateProductModel) {
            if (resp.has("id")) {
                IDModel idModel = new IDModel();
                if (idModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(idModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            } else if (resp.has("error")) {
                ErrorModel errorModel = new ErrorModel();
                if (errorModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(errorModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            } else if (resp.has("message")) {
                ErrorModel messagemodel = new ErrorModel();
                if (messagemodel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(messagemodel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            } else {
                StatusModel statusModel = new StatusModel();
                if (statusModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(statusModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            }
        } else if (obj instanceof SearchListModel) {
            SearchListModel searchListModel = new SearchListModel();
            if (searchListModel.toObject(resp.toString())) {
                mUiCallBack.OnSuccessFromServer(searchListModel);
            } else {
                mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
            }
        } else if (obj instanceof CategoryListModel) {
            CategoryListModel categoryListModel = new CategoryListModel();
            if (categoryListModel.toObject(resp.toString())) {
                mUiCallBack.OnSuccessFromServer(categoryListModel);
            } else {
                mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
            }
        } else if (obj instanceof PostcodeListModel) {
            PostcodeListModel listModel = new PostcodeListModel();
            if (listModel.toObject(resp.toString())) {
                mUiCallBack.OnSuccessFromServer(listModel);
            } else {
                mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
            }
        }else if (obj instanceof CurrencyListModel) {
            CurrencyListModel currencyListModel = new CurrencyListModel();
            if (currencyListModel.toObject(resp.toString())) {
                mUiCallBack.OnSuccessFromServer(currencyListModel);
            } else {
                mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
            }
        }  else if (obj instanceof SubscriptionListModel) {
            SubscriptionListModel listModel = new SubscriptionListModel();
            if (listModel.toObject(resp.toString())) {
                mUiCallBack.OnSuccessFromServer(listModel);
            } else {
                mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
            }
        } else if (obj instanceof CreateSalesModel) {
            if (resp.has("id")) {
                IDModel idModel = new IDModel();
                if (idModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(idModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            } else if (resp.has("error")) {
                ErrorModel errorModel = new ErrorModel();
                if (errorModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(errorModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            } else if (resp.has("message")) {
                ErrorModel messagemodel = new ErrorModel();
                if (messagemodel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(messagemodel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            } else {
                StatusModel statusModel = new StatusModel();
                if (statusModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(statusModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            }
        } else if (obj instanceof BookmarkListModel) {
            if (resp.has("error")) {
                ErrorModel errorModel = new ErrorModel();
                if (errorModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(errorModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            } else if (resp.has("status")) {
                StatusModel statusModel = new StatusModel();
                if (statusModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(statusModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            } else {
                BookmarkListModel bookMarksList = new BookmarkListModel();
                if (bookMarksList.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(bookMarksList);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            }
        } else if (obj instanceof MarkInApproModel) {
            if (resp.has("error")) {
                ErrorModel errorModel = new ErrorModel();
                if (errorModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(errorModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            } else if (resp.has("status")) {
                StatusModel statusModel = new StatusModel();
                if (statusModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(statusModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            } else {
                BookmarkListModel bookMarksList = new BookmarkListModel();
                if (bookMarksList.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(bookMarksList);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            }
        } else if (obj instanceof ReviewsRatingsListModel) {
            if (resp.has("error")) {
                ErrorModel errorModel = new ErrorModel();
                if (errorModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(errorModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            } else if (resp.has("status")) {
                StatusModel statusModel = new StatusModel();
                if (statusModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(statusModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            } else {
                ReviewsRatingsListModel reviewsRatingsList = new ReviewsRatingsListModel();
                if (reviewsRatingsList.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(reviewsRatingsList);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            }
        } else if (obj instanceof SimilarProductsModel) {
            if (resp.has("error")) {
                ErrorModel errorModel = new ErrorModel();
                if (errorModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(errorModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            } else if (resp.has("status")) {
                StatusModel statusModel = new StatusModel();
                if (statusModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(statusModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            } else {
                SimilarProductsModel similarProductsModel = new SimilarProductsModel();
                if (similarProductsModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(similarProductsModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            }
        } else if (obj instanceof LocationModel) {
            LocationModel locationModel = new LocationModel();
            if (locationModel.toObject(resp.toString())) {
                Log.d("LocationModel","#########Setting the County code######"+locationModel.getCountry_code());
          /*      GlobalFunctions.setSharedPreferenceString(context,GlobalVariables.SHARED_PREFERENCE_LOCATION_COUNTRY,locationModel.getCountry_code());
         */       mUiCallBack.OnSuccessFromServer(locationModel);
            } else {
                mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
            }

        } else if (obj instanceof ReviewsRatingsModel) {
            if (resp.has("error")) {
                ErrorModel errorModel = new ErrorModel();
                if (errorModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(errorModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            } else if (resp.has("status")) {
                StatusModel statusModel = new StatusModel();
                if (statusModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(statusModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            }
        } else if (obj instanceof MakeOfferModel) {
            if (resp.has("error")) {
                ErrorModel errorModel = new ErrorModel();
                if (errorModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(errorModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            } else {
                MakeOfferModel makeOfferModel = new MakeOfferModel();
                if (makeOfferModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(makeOfferModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
                /*StatusModel statusModel = new StatusModel();
                if (statusModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(statusModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }*/
            }

        } else if (obj instanceof MyItemListModel) {
            if (resp.has("error")) {
                ErrorModel errorModel = new ErrorModel();
                if (errorModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(errorModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            } else if (resp.has("status")) {
                StatusModel statusModel = new StatusModel();
                if (statusModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(statusModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            } else {
                MyItemListModel myItemListModel = new MyItemListModel();
                if (myItemListModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(myItemListModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            }
        } else if (obj instanceof MyItemListAvailableModel) {
            if (resp.has("error")) {
                ErrorModel errorModel = new ErrorModel();
                if (errorModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(errorModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            } else if (resp.has("status")) {
                StatusModel statusModel = new StatusModel();
                if (statusModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(statusModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            } else {
                MyItemListAvailableModel myItemListAvailableModel = new MyItemListAvailableModel();
                if (myItemListAvailableModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(myItemListAvailableModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            }
        } else if (obj instanceof ContactsListModel) {
            if (resp.has("error")) {
                ErrorModel errorModel = new ErrorModel();
                if (errorModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(errorModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            } else if (resp.has("status")) {
                StatusModel statusModel = new StatusModel();
                if (statusModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(statusModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            } else {
                ContactsListModel contactsListModel = new ContactsListModel();
                if (contactsListModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(contactsListModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            }
        }else if (obj instanceof MessageConversationModel) {
            if (resp.has("error")) {
                ErrorModel errorModel = new ErrorModel();
                if (errorModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(errorModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            } else if (resp.has("status")) {
                StatusModel statusModel = new StatusModel();
                if (statusModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(statusModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            } else {
                MessageConversationModel messageConversationModel = new MessageConversationModel();
                if (messageConversationModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(messageConversationModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            }
        }else if (obj instanceof CreateMessageModel) {
            if (resp.has("id")) {
                IDModel idModel = new IDModel();
                if (idModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(idModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            } else if (resp.has("error")) {
                ErrorModel errorModel = new ErrorModel();
                if (errorModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(errorModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            } else if (resp.has("message_id")) {
                MessageModel messagemodel = new MessageModel();
                if (messagemodel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(messagemodel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            } else {
                StatusModel statusModel = new StatusModel();
                if (statusModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(statusModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            }
        }else if (obj instanceof ConversationIdModel) {
            if (resp.has("id")) {
                IDModel idModel = new IDModel();
                if (idModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(idModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            } else if (resp.has("error")) {
                ErrorModel errorModel = new ErrorModel();
                if (errorModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(errorModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            } else if (resp.has("message")) {
                ErrorModel messagemodel = new ErrorModel();
                if (messagemodel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(messagemodel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            } else {
                ConversationIdModel ConversationIdModel = new ConversationIdModel();
                if (ConversationIdModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(ConversationIdModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            }
        }else if (obj instanceof VerifyAliasModel) {
           if (resp.has("error")) {
                ErrorModel errorModel = new ErrorModel();
                if (errorModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(errorModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            } else{
               VerifyAliasModel verifyAliasModel = new VerifyAliasModel();
                if (verifyAliasModel.toObject(resp.toString())) {
                    mUiCallBack.OnSuccessFromServer(verifyAliasModel);
                } else {
                    mUiCallBack.OnError(context.getString(R.string.ErrorResponseData));
                }
            }
        }
    }

    public void login(Context context, LoginModel loginModel, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        postData(context, loginModel, ServerConstants.URL_UserLogin, TAG);
    }

    public void registerUser(Context context, RegisterModel registerModel, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        postData(context, registerModel, ServerConstants.URL_UserRegistration, TAG);
    }

    public void registerFacebookUser(Context context, FacebookRegisterModel registerModel, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        postData(context, registerModel, ServerConstants.URL_UserFbRegistration, TAG);
    }

    public void forgotPassword(Context context, ForgotPasswordModel forgotPasswordModel, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        postData(context, forgotPasswordModel, ServerConstants.URL_ForgotPassword, TAG);
    }

    public void resendOTP(Context context, OTPResendModel otpResendModel, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        postData(context, otpResendModel, ServerConstants.URL_ResendOTP, TAG);
    }

    public void verifyOTP(Context context, OTPCheckerModel otpCheckerModel, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        postData(context, otpCheckerModel, ServerConstants.URL_VerifyOTP, TAG);
    }
    public void verifyEmail(Context context,ProfileDetailsModel profileDetailsModel, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        postData(context, profileDetailsModel, ServerConstants.URL_VerifyEmail, TAG);
    }

    public void updateProfile(Context context, ProfileDetailsModel profileDetailsModel, ServerResponseInterface mCallInterface, String TAG) {
        Log.i(TAG, "mobile " + profileDetailsModel.getPhone());
        setCallbacks(mCallInterface);
        postData(context, profileDetailsModel, ServerConstants.URL_updateUserDetails, TAG);
    }

    public void getUserDetails(Context context, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String param = "token=" + GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
        getData(context, new ProfileDetailsModel(), ServerConstants.URL_getUserDetails, param, TAG);
    }
    public void getShopDetails(Context context,String user_id, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String param = "token=" + GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
        param += "&user_id=" + user_id;
        getData(context, new ShopModel(), ServerConstants.URL_getShopDetails, param, TAG);
    }

    public void getSimilarProducts(Context context, String productID, String category,String latitude,String longitude,ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String param = "token=" + GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
        param += "&product_id=" + productID;
        param += "&category=" + category;
        param += "&latitude=" + latitude;
        param += "&longitude=" + longitude;
        Log.d(TAG, "#########param ######" + param);
        getData(context, new SimilarProductsModel(), ServerConstants.URL_getSimilarProducts, param, TAG);
    }
    public void getSellerProducts(Context context, String seller_id,String product_id,ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String param = "token=" + GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
        param += "&user_id="+seller_id;
        param += "&current_product_id=" + product_id;
        Log.d(TAG, "#########param ######" + param+"######URL_getSellerProducts######"+ServerConstants.URL_getSellerProducts);
        getData(context, new SimilarProductsModel(), ServerConstants.URL_getSellerProducts, param, TAG);
    }
    public void getListDetails(Context context, FilterModel filterModel, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String param = "token=" + GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
        if(!TextUtils.isEmpty(filterModel.getLatitude()) && !TextUtils.isEmpty(filterModel.getLongitude())){
            param += "&" + FilterModel.LATITUDE + "=" + filterModel.getLatitude();
            param += "&" + FilterModel.LONGITUDE + "=" + filterModel.getLongitude();
        }
        if(filterModel.isDistance_enabled())
            param += "&" + FilterModel.RANGE + "=" + filterModel.getRange();
        param += "&" + FilterModel.PAGE + "=" + filterModel.getPage();
        param += "&" + FilterModel.TYPE + "=" + filterModel.getType();
       /* param += "&" + FilterModel.PRICE_RANGE + "=" + filterModel.getPriceRange();*/
       /* param += "&" + FilterModel.IS_SHIPABLE + "=" + (filterModel.isShipable() ? 1 : 0);*/
        if(filterModel.isSortByRecent_garage())
            param += "&" + FilterModel.SORT_BY_RECENT + "=" + (filterModel.isSortByRecent_garage() ? 1 : 0);
        if(filterModel.isEnding_soon())
            param += "&" + FilterModel.ENDING_SOON + "=" + (filterModel.isEnding_soon() ? 1 : 0);
        if(filterModel.is_current_country()){
            param += "&" + FilterModel.CURRENT_COUNTRY_CODE + "=" +  filterModel.getCurrent_country_code();
            param += "&" + FilterModel.IS_CURRENT_COUNTRY + "=" + (filterModel.is_current_country() ? 1 : 0);
        }

        param += "&" + FilterModel.GARAGE_ITEMS + "="+1;
        /*  param += "&" + FilterModel.IS_PICKUP + "=" + (filterModel.isPickup() ? 1 : 0);
        param += "&" + FilterModel.IS_AVAILABLE + "=" + (filterModel.isAvailable() ? 1 : 0);*/
        if(!TextUtils.isEmpty(filterModel.getCategories()))
            param += "&" + FilterModel.CATEGORIES + "=" + filterModel.getCategories();
        /*param += "&" + FilterModel.ZIPCODE + "=" + filterModel.getZipcode();*/
        getData(context, /*new SalesListModel()*/new SalesListModelNew(), ServerConstants.URL_getListSales, param, TAG);
    }
    public  void getItemListDetails(Context context, FilterModel filterModel, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String param = "token=" + GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
        if(!TextUtils.isEmpty(filterModel.getLatitude()) && !TextUtils.isEmpty(filterModel.getLongitude())){
            param += "&" + FilterModel.LATITUDE + "=" + filterModel.getLatitude();
            param += "&" + FilterModel.LONGITUDE + "=" + filterModel.getLongitude();
        }
        if(filterModel.isDistance_enabled())
            param += "&" + FilterModel.RANGE + "=" + filterModel.getRange();
        param += "&" + FilterModel.PAGE + "=" + filterModel.getPage();
        param += "&" + FilterModel.TYPE + "=" + filterModel.getType();
    /*    param += "&" + FilterModel.PRICE_RANGE + "=" + filterModel.getPriceRange();*/
        param += "&" + FilterModel.SORT_BY_RECENT + "=" + (filterModel.isSortByRecent() ? 1 : 0);
       /* param += "&" + FilterModel.IS_SHIPABLE + "=" + (filterModel.isShipable() ? 1 : 0);
        param += "&" + FilterModel.IS_PICKUP + "=" + (filterModel.isPickup() ? 1 : 0);
        param += "&" + FilterModel.IS_AVAILABLE + "=" + (filterModel.isAvailable() ? 1 : 0);*/
        if(!TextUtils.isEmpty(filterModel.getCategories()))
            param += "&" + FilterModel.CATEGORIES + "=" + filterModel.getCategories();
       /* param += "&" + FilterModel.ZIPCODE + "=" + filterModel.getZipcode();*/
        getData(context, new ItemListModel(), ServerConstants.URL_getItemList, param, TAG);
    }
    public  void getHomeList(Context context, FilterModel filterModel, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String param="";/*= "token=" + GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);*/
        if(!TextUtils.isEmpty(filterModel.getLatitude()) && !TextUtils.isEmpty(filterModel.getLongitude())){
            param += "&" + FilterModel.LATITUDE + "=" + filterModel.getLatitude();
            param += "&" + FilterModel.LONGITUDE + "=" + filterModel.getLongitude();
        }
        if(filterModel.isDistance_enabled())
            param += "&" + FilterModel.RANGE + "=" +filterModel.getRange();
        param += "&" + FilterModel.PAGE + "=" + filterModel.getPage();
        param += "&" + FilterModel.TYPE + "=" + filterModel.getType();
        /*param += "&" + FilterModel.PRICE_RANGE + "=" + filterModel.getPriceRange();*/
        if(filterModel.isSortByRecent())
            param += "&" + FilterModel.SORT_BY_RECENT + "=" + (filterModel.isSortByRecent() ? 1 : 0);
        if(filterModel.isGarage_items())
            param += "&" + FilterModel.GARAGE_ITEMS + "=" + (filterModel.isGarage_items() ? 1 : 0);
        if(filterModel.is_current_country()){
            param += "&" + FilterModel.CURRENT_COUNTRY_CODE + "=" +  filterModel.getCurrent_country_code();
            param += "&" + FilterModel.IS_CURRENT_COUNTRY + "=" + (filterModel.is_current_country() ? 1 : 0);
        }
        if(filterModel.isPrice_l_2_h())
            param += "&" + FilterModel.PRICE_LOW_TO_HIGH + "=" + (filterModel.isPrice_l_2_h() ? 1 : 0);

        if(filterModel.isPrice_h_2_l())
            param += "&" + FilterModel.PRICE_HIGH_TO_LOW + "=" + (filterModel.isPrice_h_2_l() ? 1 : 0);

    /*    param += "&" + FilterModel.IS_SHIPABLE + "=" + (filterModel.isShipable() ? 1 : 0);
        param += "&" + FilterModel.IS_PICKUP + "=" + (filterModel.isPickup() ? 1 : 0);
        param += "&" + FilterModel.IS_AVAILABLE + "=" + (filterModel.isAvailable() ? 1 : 0);*/
        if(!TextUtils.isEmpty(filterModel.getCategories()))
            param += "&" + FilterModel.CATEGORIES + "=" + filterModel.getCategories();
       /* param += "&" + FilterModel.ZIPCODE + "=" + filterModel.getZipcode();*/
        getData(context, new HomeListModel(), ServerConstants.URL_getItemList, param, TAG);
    }

    public void getMySalesList(Context context,String latitude,String longitude, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String param = "token=" + GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
        param += "&latitude=" + latitude;
        param += "&longitude=" + longitude;
        getData(context,new SalesListModelNew(), ServerConstants.URL_getListMySales, param, TAG);
    }

    public void getBookmarksList(Context context, String page,ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String param = "token=" + GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
        param += "&page="+page;
        getData(context, new HomeListModel(), ServerConstants.URL_getBookmarkList, param, TAG);
    }

    public void deleteBookmark(Context context, String productID, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String param = "token=" + GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
        param += "&product_id=" + productID;
        postData(context, new StatusModel(), ServerConstants.URL_deleteBookmark, param, TAG);
    }

    public void addBookmark(Context context, String productID, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String param = "token=" + GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
        param += "&product_id=" + productID;
        param += "&content=" + "application/json";
        postData(context, new StatusModel(), ServerConstants.URL_addBookmark, param, TAG);
    }

    public void reportABug(Context context, String content, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String param = "token=" + GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
        param += "&content=\"" + content + "\"";
        param += "&device=" + GlobalFunctions.getDevice();
        postData(context, new StatusModel(), ServerConstants.URL_reportABug, param, TAG);
    }

    public void markInappropirate(Context context, MarkInApproModel markInApproModel,ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String param = "token=" + GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
        if(markInApproModel.is_item())
            postData(context, markInApproModel, ServerConstants.URL_markInappropirate, param, TAG);
        else
            postData(context, markInApproModel, ServerConstants.URL_markSaleInappropirate, param, TAG);
    }


    public void getCategoriesList(Context context, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String param = "token=" + GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
        getData(context, new CategoryListModel(), ServerConstants.URL_getCategoryList, null, TAG);
    }
    public void getCurrencies(Context context, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String param = "token=" + GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
        getData(context, new CurrencyListModel(), ServerConstants.URL_getCurrencies, param, TAG);
    }
    public void getPostCodesList(Context context, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
       String param = "token=" + GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
        getData(context, new PostcodeListModel(), ServerConstants.URL_getPostCodesList, null, TAG);
    }

    public void getSubscriptionList(Context context, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String param = "token=" + GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
        getData(context, new SubscriptionListModel(), ServerConstants.URL_getSubscriptionList, param, TAG);
    }

    public void getSearchDetails(Context context, SearchModel searchModel, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String param = "token=" + GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
      /*  param += "&search=" + searchString;
        param += "&latitude=" + latitude;
        param += "&longitude=" + longitude;
        param += "&type="+type;
        param += "&page="+page;
        param += "&range="+range;*/
        postData(context, searchModel, ServerConstants.URL_getSearchItem, param, TAG);
    }

/*    public void createSale(Context context, CreateSalesModel salesModel, ServerResponseInterface mCallInterface, String TAG) {
        for (int i = 0; i < salesModel.getProducts().size(); i++) {
            ArrayList<CreateImageModel> images = salesModel.getProducts().get(i).getImages();
            for (int j = 0; j < images.size(); j++) {
                String base64 = GlobalFunctions.getBase64fromPath(new File(images.get(j).getImage()));
                CreateImageModel model = new CreateImageModel();
                model.setImage(base64);
                if (j == 0) {
                    model.setDisplay(true);
                } else {
                    model.setDisplay(false);
                }
                images.remove(j);
                images.add(j, model);
            }
            salesModel.getProducts().get(i).setImages(images);
        }
        Log.d("Create Sale", salesModel.toString());
        setCallbacks(mCallInterface);
        postData(context, salesModel, ServerConstants.URL_CreateSale, TAG);
    }*/
    public void createSale(Context context, CreateSalesModel salesModel, ServerResponseInterface mCallInterface, String TAG) {
            ArrayList<CreateImageModel> images = salesModel.getImages();
                if(images.size()>0){
                    for (int j = 0; j < images.size(); j++) {
                        if(images.get(j).is_new_image()){
                            String base64 = GlobalFunctions.getBase64fromPath(new File(images.get(j).getImage()));
                            if(base64.length()>0){
                                CreateImageModel model = new CreateImageModel();
                                model.setImage(base64);
                                if (j == 0) {
                                    model.setDisplay(true);
                                } else {
                                    model.setDisplay(false);
                                }
                                images.remove(j);
                                images.add(j, model);
                            }else{
                                images.remove(images.get(j));
                            }
                        }
                    }
                }
        salesModel.setImages(images);
        Log.d("Create Sale", salesModel.toString());
        setCallbacks(mCallInterface);
        if(salesModel.getRequest_type().equalsIgnoreCase(GlobalVariables.TYPE_CREATE_REQUEST) ||
                salesModel.getRequest_type().equalsIgnoreCase(GlobalVariables.TYPE_UPDATE_REQUEST)    )
            postData(context, salesModel, ServerConstants.URL_CreateSale, TAG);
        else if(salesModel.getRequest_type().equalsIgnoreCase(GlobalVariables.TYPE_PUBLISH_REQUEST))
            postData(context, salesModel, ServerConstants.URL_PublishSale, TAG);

    }
    public void updateSale(Context context, CreateSalesModel salesModel, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        postData(context, salesModel, ServerConstants.URL_UpdateSale, TAG);
    }
    public void createSaleItem(Context context, CreateProductModel createProductModel, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
       // for (int i = 0; i < salesModel.getProducts().size(); i++) {
            ArrayList<CreateImageModel> images = createProductModel.getImages();
            if(images.size()>0){
                for (int j = 0; j < images.size(); j++) {
                    if(images.get(j).is_new_image()){
                        String base64 = GlobalFunctions.getBase64fromPath(new File(images.get(j).getImage()));
                        if(base64.length()>0){
                            CreateImageModel model = new CreateImageModel();
                            model.setImage(base64);
                            if (j == 0) {
                                model.setDisplay(true);
                            } else {
                                model.setDisplay(false);
                            }
                            images.remove(j);
                            images.add(j, model);
                        }else
                            images.remove(j);
                    }/*else
                        images.remove(j);*/
                }
            }
            createProductModel.setImages(images);
        //}
        postData(context, createProductModel, ServerConstants.URL_CreateSaleItem, TAG);
    }

    public void updateSaleStatus(Context context, String saleID, boolean isAvailable, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String param = "token=" + GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
        param += "&sale_id=" + saleID;
        String status = "0";
        if (!isAvailable) {
            status = "1";
        }
        param += "&status=" + status;
        postData(context, new StatusModel(), ServerConstants.URL_UpdateSaleStatus, param, TAG);
    }
    public void updateItemStatus(Context context, MarkSoldModel markSoldModel, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String param = "token=" + GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
       /* param += "&product_id=" + product_id;
        String status = "0";
        if (!isAvailable) {
            status = "1";
        }
        param += "&status=" + status;*/
        postData(context,markSoldModel, ServerConstants.URL_MARK_ITEM_SOLD, param, TAG);
    }
    public void deleteSale(Context context, String saleID, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String param = "token=" + GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
        param += "&sale_id=" + saleID;
        postData(context, new StatusModel(), ServerConstants.URL_DeleteSale, param, TAG);
    }

    public void isShippingAvailable(Context context, String saleID, String postalCode, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String param = "token=" + GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
        param += "&sale_id=" + saleID;
        param += "&postcode=" + postalCode;
        postData(context, new StatusModel(), ServerConstants.URL_checkShipping, param, TAG);
    }

    public void deleteSaleItem(Context context, String saleID, String productID, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String param = "token=" + GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
        param += "&sale_id=" + saleID;
        param += "&product_id=" + productID;
        postData(context, new StatusModel(), ServerConstants.URL_DeleteSaleItem, param, TAG);
    }
    public void deleteItem(Context context,String productID, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String param = "token=" + GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
        param += "&product_id=" + productID;
        postData(context, new StatusModel(), ServerConstants.URL_DeleteItem, param, TAG);
    }

    public void rescheduleSale(Context context, String saleID, String startDate, String startTime, String endTime, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String param = "token=" + GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
        param += "&sale_id=" + saleID;
        param += "&type=schedule";
        param += "&start_date=" + startDate;
        param += "&start_time=" + startTime;
        param += "&end_time=" + endTime;
        postData(context, new StatusModel(), ServerConstants.URL_scheduleSale, param, TAG);
    }

    public void endSale(Context context, String saleID, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String param = "token=" + GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
        param += "&sale_id=" + saleID;
        param += "&type=end";
        postData(context, new StatusModel(), ServerConstants.URL_scheduleSale, param, TAG);
    }

    public void moveSaletoMultipleItem(Context context, String saleID, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String param = "token=" + GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
        param += "&sale_id=" + saleID;
        param += "&type=move";
        postData(context, new StatusModel(), ServerConstants.URL_scheduleSale, param, TAG);
    }
    public void getReviewsRatings(Context context, String saleID, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String param = "token=" + GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
        param += "&seller_id=" + saleID;
        getData(context, new ReviewsRatingsListModel(), ServerConstants.URL_GetSellerRatings, param, TAG);
    }
    public void getItemInfo(Context context, String product_id, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String param = "token=" + GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
        param += "&product_id=" + product_id;
        getData(context, new ProductModel(), ServerConstants.URL_GET_ITEM_DETAILS, param, TAG);
    }
    public void getSaleInfo(Context context, String sale_id,String latitude,String longitude, ServerResponseInterface mCallInterface, String TAG) {
       setCallbacks(mCallInterface);
        String param = "token=" + GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
        param += "&sale_id=" + sale_id;
        param += "&latitude=" + latitude;
        param += "&longitude=" + longitude;
        getData(context, new SalesModel(), ServerConstants.URL_VIEW_SALE, param, TAG);
    }

  /*  public void setReviewsRatings(Context context, String seller_ID, String title, String comment, boolean is_garrage_review, int rating, String productID, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String param = "token=" + GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
        param += "&seller_id=" + seller_ID;
        param += "&title=" + title;
        param += "&comment=" + comment;
        param += "&is_garage_review=" + (is_garrage_review ?Integer.toString( 1) :Integer.toString(0));
        param += "&rating=" + Integer.toString(rating);


        postData(context,new StatusModel(), ServerConstants.URL_SaveReviewRating, param, TAG);
      //  postData(context,new StatusModel(), ServerConstants.URL_SaveReviewRating, param, TAG);
    }*/
    public void setReviewsRatings(Context context, ReviewsRatingsModel reviewsRatingsModel, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
      /*  String param = "token=" + GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
        param += "&seller_id=" + seller_ID;
        param += "&title=" + title;
        param += "&comment=" + comment;
        param += "&is_garage_review=" + (is_garrage_review ?Integer.toString( 1) :Integer.toString(0));
        param += "&rating=" + Integer.toString(rating);
*/

        postData(context,reviewsRatingsModel, ServerConstants.URL_SaveReviewRating, TAG);
        //  postData(context,new StatusModel(), ServerConstants.URL_SaveReviewRating, param, TAG);
    }
    public void messageToAdmin(Context context, String message, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String param = "token=" + GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
        param += "&content=\"" + message + "\"";
      //  param += "&device=" + GlobalFunctions.getDevice();
        postData(context, new StatusModel(), ServerConstants.URL_Send_message_to_admin, param, TAG);
    }

    public void getAddress(Context context, Double lat, Double lng, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String param ="latlng="+lat+","+lng;
        param +="&sensor=true";
        getData(context, new LocationModel(), ServerConstants.URL_GET_ADDRESS, param, TAG);

       /* http://maps.google.com/maps/api/geocode/json?latlng="+lat+","+lng+"&sensor=true*/

    }
    public void ask_for_phone_number(Context context,AskModel askModel, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
      /*  String param ="email="+email;*/
        postData(context, askModel, ServerConstants.URL_UserFb_ask_phoneRegistration, TAG);

    }
    public void makeOffer(Context context, MakeOfferModel makeOfferModel, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        postData(context, makeOfferModel, ServerConstants.URL_MAKE_OFFER, TAG);
    }
    public void getAvailableItemsList(Context context, MyItemListModel myItemListModel, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String param = "token=" + GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
        getData(context, myItemListModel, ServerConstants.URL_GET_AVAILABLE_ITEMS_LIST, param, TAG);
    }
    public void getAvailableItemsListCount(Context context, MyItemListAvailableModel myItemListAvailableModel, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String param = "token=" + GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
        getData(context, myItemListAvailableModel, ServerConstants.URL_GET_AVAILABLE_ITEMS_COUNT, param, TAG);
    }
    public void getConversationContacts(Context context, ContactsListModel contactsListModel,String active_tab, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String param = "token=" + GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
        param += "&active_tab=" + active_tab;
        getData(context, contactsListModel, ServerConstants.URL_get_conversation_contacts, param, TAG);
    }
    public void getConversationMessages(Context context, MessageConversationModel messageConversationModel, String conversation_id,String active_tab, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String param = "token=" + GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
        param += "&conversation_id=" + conversation_id;
        param += "&active_tab=" + active_tab;
        getData(context, messageConversationModel, ServerConstants.URL_get_conversation_messages, param, TAG);
    }
    public void sendMessage(Context context, CreateMessageModel createMessageModel, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        postData(context,createMessageModel,ServerConstants.URL_send_message, TAG);
    }

    public void check_conversation_exists(Context context, ConversationIdModel conversationIdModel, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        postData(context,conversationIdModel,ServerConstants.URL_check_conversation_exists, TAG);
    }
    public void isUserIdValid(Context context, VerifyAliasModel verifyAliasModel, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String param = "token=" + GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
       /* getData(context, verifyAliasModel, ServerConstants.URL_verify_alias, param, TAG);*/
        postData(context,verifyAliasModel,ServerConstants.URL_verify_alias, TAG);

    }
    public void delete_conversation(Context context, String active_tab, String conversation_id, ServerResponseInterface mCallInterface, String TAG) {
        setCallbacks(mCallInterface);
        String param = "token=" + GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
        param += "&active_tab=" + active_tab;
        param += "&conversation_id=" + conversation_id;
        postData(context, new StatusModel(), ServerConstants.URL_delete_conversation, param, TAG);
    }
}