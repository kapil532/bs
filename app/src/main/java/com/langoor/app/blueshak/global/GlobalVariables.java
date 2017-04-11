package com.langoor.app.blueshak.global;

import java.text.SimpleDateFormat;

public class GlobalVariables {
    public static final String SHARED_PREFERENCE_PROFILE =  "SharedPreferenceProfile";
    public static final int REQUEST_FOR_CAMERA = 222;
    public static final int REQUEST_FOR_CROP_IMAGE = 225;
    public static final int REQUEST_FOR_SELECTFILE = 223;
    public static final int REQUEST_FOR_GOOGLE = 224;
    public static final String SHARED_PREFERENCE_TOKEN = "TOKEN";
    public static final String SHARED_PREFERENCE_KEY = "Blueshake_private_key";
    public static final String SHARED_PREFERENCE_EMAIL =  "SharedPreferenceEMAIL";
    public static final String SHARED_PREFERENCE_COUNTRY =  "SharedPreferencecountry";
    public static final String SHARED_PREFERENCE_LOCATION_COUNTRY =  "SharedPreferenceLocationcountry";
    public static final String SHARED_PREFERENCE_FULLNAME =  "SharedPreferenceFullName";
    public static final String SHARED_PREFERENCE_USERID =  "SharedPreferenceUserID";
    public static final String SHARED_PREFERENCE_USERSTATUS =  "SharedPreferenceUserStatus";
    public static final String SHARED_PREFERENCE_MOBILE =  "SharedPreferenceUserMobile";
    public static final String SHARED_PREFERENCE_PHONE =  "SharedPreferenceUserPhone";
    public static final String SHARED_PREFERENCE_ADDRESS =  "SharedPreferenceUserAddress";
    public static final String SHARED_PREFERENCE_CATEGORIES =  "SharedPreferenceCategories";
    public static final String SHARED_PREFERENCE_POSTALCODES =  "SharedPreferencePostalCodes";
    public static final String SHARED_PREFERENCE_CURRENCIES =  "SharedPreferenceCurrencies";
    public static final String SHARED_PREFERENCE_FIRST_TIME =  "SharedPreferenceFirstTime";
    public static final String SHARED_PREFERENCE_IS_REGISTRATION_FINISHED =  "SharedPreferenceRegistrationFinished";
    public static final String SHARED_PREFERENCE_IS_OTP_VERIFIED =  "SharedPreferenceIS_OTP_verified";
    public static final String SHARED_PREFERENCE_BS_ID =  "bsid";
    public static final String SHARED_PREFERENCE_AVATAR =  "avatar";
    public static final String SHARED_PREFERENCE_PERMISSION ="SharedPreferencePermissionCheck";
    public static final String SHARED_PREFERENCE_CURRENCY = "currency";
    public static final String MESSAGE_UNREAD_COUNT = "messagesunreadcount";
    public static final String NOTIFICATIONS_UNREAD_COUNT = "notificationsunreadcount";
    public static final String BOOKMARKS_UNREAD_COUNT = "bookmarksunreadcount";
    public static final String HEADER_CONTENT_TYPE =  "Content-Type";
    public static final String HEADER_TOKEN =  "token";
    public static final String HEADER_CONTENT_TYPE_VALUE =  "application/json; charset=utf-8";
    public static final String CURRENCY_NOTATION = "$";
    public static final String FREE = "Free";
    public static final String CURRENT_LOCATION = "current_location";
    public static final String FILTER_MODEL = "filtermodel";
    public static final String OTP_CHECK_MODEL = "otpcheckmodel";
    public static final String LATITUDE = "lattitude";
    public static final String LONGITUDE = "lattitude";
    public static final String SELECTED_ITEMS_LIST = "selcted_items_list";
    public static final String SELECTED_old_LIST = "selcted_old_items_list";
    public static final int IMAGE_WIDTH = 640;
    public static final int IMAGE_HEIGHT = 360;
    public static final int PERMISSIONS_REQUEST_CAMERA = 151;
    public static final int PERMISSIONS_REQUEST_LOCATION_CHECK= 145;
    public static final int PERMISSIONS_REQUEST_CALENDER = 152;
    public static final int PERMISSIONS_REQUEST_PRIMARY = 153;
    public static final int REQUEST_CODE_ADD_PRODUCT = 154;
    public static final int REQUEST_CODE_UPDATE_PRODUCT = 155;
    public static final int REQUEST_CODE_FILTER_PICK_LOCATION = 157;
    public static final int REQUEST_CODE_SELECT_PRODUCT = 160;
    public static final int REQUEST_CODE_FILTER_MODEL_LOCATION = 161;
    public static final int REQUEST_CODE_SELECT_CATEGORY= 162;
    public static final int REQUEST_CODE_SELECT_SALE= 163;
    public static final int REQUEST_CODE_SELECT_CURRENCY= 164;


    public static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    public static final String
            TYPE_GARAGE = "garage",
            TYPE_ITEM = "item",
            TYPE_SHOP = "shop",
            TYPE_MULTIPLE_ITEMS = "shop";

    public static final String
            TYPE_TEXT = "text",
            TYPE_OFFER= "offer",
            TYPE_IMAGE = "image";
    public static final String
            TYPE_SELLER_TAB = "seller",
                TYPE_INBOX="INBOX",
            TYPE_BUYER_TAB= "buyer";

    public static final String
            TYPE_CREATE_REQUEST = "create",
            TYPE_PUBLISH_REQUEST = "publish",
            TYPE_UPDATE_REQUEST = "update";

    public static final String
            TYPE_FILTER = "filter";
    public static final int
            PRICE_MIN_VALUE = 0,
            PRICE_MAX_VALUE = 999500,
            PRICE_OFFSET_VALUE = 500;

    public static final int
            DISTANCE_MIN_VALUE = 5,
            DISTANCE_MAX_VALUE = 250,
            DISTANCE_SEARCH_MAX_VALUE = 50000,
            DISTANCE_OFFSET_VALUE = 1;


    public static final int
            TYPE_HOME                       = 0,
            TYPE_NOTIFICATION               = 1,
            TYPE_MY_BOOKMARKS               = 2,
            TYPE_MESSAGE                    = 3,
            TYPE_MY_SALE                    = 4,
            TYPE_GARAGE_SALE        = 5,
            TYPE_ITEMS = 6,
            TYPE_MY_PROFILE                 = 7,
            TYPE_UPGRADE_SUBBSCRIPTION      = 8,
            TYPE_VIEW_TUTORIAL              = 9,
            TYPE_SUPPORT                    = 10,
            TYPE_REPORT_A_BUG               = 11,
            TYPE_SIGNOUT                    = 12,
            TYPE_MY_Multiple_SALE            =13,
            TYPE_MY_GARAGE_SALE              =14,
            TYPE_REVIEW_RATINGS              =15,
            TYPE_SEARCH                      =16,
            TYPE_SIGN_UP                     =17,
            TYPE_ADD_TEMS                    =18,
            TYPE_SIMILAR_PRODUCT             =19,
            TYPE_AC_SIGN_UP                  =20,
            TYPE_FILTER_ACTIVITY             =21,
            TYPE_PICK_LOCATION                =22,
            TYPE_ITEM_ACTIVITY             =23,
            TYPE_LOGIN                    =24,
            TYPE_TnC             =25,
            TYPE_privacy_policy             =26;


    public static String Sydney_latitude="-33.8688196";
    public static String Sydney_longitude="151.2092955";
    public static String BLUESHAK_SUPPORT_EMAIL="help@blueshak.com";
    public static final int MAP_ZOOMING_INT = 8;
}
