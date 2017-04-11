package com.langoor.app.blueshak.Messaging.util;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.text.format.DateUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.langoor.blueshak.R;
import com.google.gson.Gson;
import com.langoor.app.blueshak.Messaging.data.ChatRoom;
import com.langoor.app.blueshak.Messaging.data.Message;
import com.langoor.app.blueshak.Messaging.data.PushUnRegisterDeviceRequest;
import com.langoor.app.blueshak.Messaging.manager.VolleyManager;
import com.langoor.app.blueshak.global.GlobalFunctions;
import com.langoor.app.blueshak.services.model.NotificationModel;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


/**
 * Created by Bryan Yang on 9/18/2015.
 */
public class CommonUtil {

        public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static String parentLink = "http://52.34.5.124/api/";
    //	http://52.63.211.171/api
    private static String acname;
    private static String email;
    public  static Boolean debugable;
    //public static final String DATE_FORMAT_FOR_MESSAGE="yyyy-MM-dd HH:mm:ss.SSSZ";
    public static final String DATE_FORMAT_FOR_MESSAGE="yyyy-MM-dd HH:mm:ss";

    public static String getMD5(String input) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance(MD5);
            digest.update(input.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            return input.hashCode() + "";
        }
    }

    public static String fromDateToString(Date date) {
        if(date != null && !date.toString().isEmpty()){
            DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_FOR_MESSAGE, Locale.getDefault());
            return dateFormat.format(date);
        }
        return null;
    }

    public static Date fromStringToDate(String dateString) {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_FOR_MESSAGE, Locale.getDefault());
        try {
            return dateFormat.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
        public static Boolean isToday(Date mDate){
            if (DateUtils.isToday(mDate.getTime())) {
                return  true;
            } else {
                return false;
            }
        }
    public static boolean isListEmpty(List list) {
        return (list == null || list.isEmpty());
    }

    public static String getContactNameByPhoneNumber(Context context, String phoneNumber) {
        Uri personUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cur = context.getContentResolver().query(personUri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cur.moveToFirst()) {
            int nameIdx = cur.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
            String name = cur.getString(nameIdx);
            cur.close();
            return name;
        }
        return phoneNumber;
    }

    public static Bitmap retrieveContactPhoto(Context context, String number) {
        ContentResolver contentResolver = context.getContentResolver();
        String contactId = null;
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));

        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID};

        Cursor cursor =
                contentResolver.query(
                        uri,
                        projection,
                        null,
                        null,
                        null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));
            }
            cursor.close();
        }
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        Bitmap photo = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.placeholder_background);
        if (photo != null) {
            photo.recycle();
            photo = null;
        }

        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(),
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactId)));

            if (inputStream != null) {
                photo = BitmapFactory.decodeStream(inputStream);

            }

            assert inputStream != null;
            if (inputStream != null)
                inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return photo;
    }



    public static String getContactName(final String phoneNumber, Context ctx) {
        Uri uri;
        String[] projection;
        Uri mBaseUri = Contacts.Phones.CONTENT_FILTER_URL;
        projection = new String[]{Contacts.People.NAME};
        try {
            Class<?> c = Class.forName("android.provider.ContactsContract$PhoneLookup");
            mBaseUri = (Uri) c.getField("CONTENT_FILTER_URI").get(mBaseUri);
            projection = new String[]{"display_name"};
        } catch (Exception e) {
            e.printStackTrace();
        }


        uri = Uri.withAppendedPath(mBaseUri, Uri.encode(phoneNumber));
        Cursor cursor = ctx.getContentResolver().query(uri, projection, null, null, null);

        String contactName = "";

        if (cursor.moveToFirst()) {
            contactName = cursor.getString(0);
        }

        cursor.close();
        cursor = null;

        return contactName;
    }

    public static String getPhoneFormate(String userPhone) {
        String isd_code = "";
        if (userPhone.contains(" ")) {
            userPhone = userPhone.replaceAll("\\s", "");
        }
        StringBuffer usb = new StringBuffer();
        if (userPhone.trim().startsWith("+")) {
            isd_code = userPhone.substring(0, 3);
            userPhone = userPhone.substring(3);
        } else {

        }
        usb.append(isd_code);
        usb.append(" ");
        usb.append(userPhone);

        return usb.toString();

    }

    public static String getPhoneIsd(String userPhone) {
        String isd_code = "";
        if (userPhone.contains(" ")) {
            userPhone = userPhone.replaceAll("\\s", "");
        }
        if (userPhone.trim().startsWith("+")) {
            isd_code = userPhone.substring(0, 3);
            userPhone = userPhone.substring(3);
        }

        return isd_code;

    }

    public static String getPhone(String userPhone) {
        String isd_code = "";
        if (userPhone.contains(" ")) {
            userPhone = userPhone.replaceAll("\\s", "");
        }
        if (userPhone.trim().startsWith("+")) {
            isd_code = userPhone.substring(0, 3);
            userPhone = userPhone.substring(3);
        }
        return userPhone;

    }


   /* public static void sortAlphabetcally(*//*ArrayList<User>*//*ArrayList<CharlieChatterContact> list) {
        Collections.sort(list, new Comparator<CharlieChatterContact>() {
            *//* This comparator will sort AppDetail objects alphabetically. *//*
            @Override
            public int compare(CharlieChatterContact a1, CharlieChatterContact a2) {
                // String implements Comparable
                return (a1.getFirstName()).compareTo(a2.getFirstName());
            }
        });
    }*/

   /* public static void sortDates(ArrayList<Message> list) {
        Collections.sort(list, new Comparator<Message>() {
            *//* This comparator will sort AppDetail objects alphabetically. *//*
            @Override
            public int compare(Message a1, Message a2) {
                // String implements Comparable
                return (a1.getSendTime()).compareTo(a2.getSendTime());
            }
        });
    }*/
  /*  public static void sortRecentChats(ArrayList<ChatRoom> list) {
        Collections.sort(list,Collections.reverseOrder(new Comparator<ChatRoom>() {
            @Override
            public int compare(ChatRoom a1, ChatRoom a2) {
                // String implements Comparable
                System.out.println("############a2.getTimestamp()############"+a2.getTimestamp());
                return (a1.getTimestamp()).compareTo(a2.getTimestamp());
            }
        }));
    }
*/
  /*  Collections.sort(datestring, new Comparator<String>() {
        DateFormat f = new SimpleDateFormat("dd/MM/yyyy '@'hh:mm a");
        @Override
        public int compare(String o1, String o2) {
            try {
                return f.parse(o1).compareTo(f.parse(o2));
            } catch (ParseException e) {
                throw new IllegalArgumentException(e);
            }
        }
    });*/

    public static void picassoLoader(Context context, ImageView imageView, String url) {
        //Log.d("PICASSO", "loading image");
        Picasso.with(context)
                .load(url)
                        //.resize(30,30)
                .placeholder(R.drawable.ic_app_icon)
                .error(R.drawable.ic_app_icon)
                .fit()
                .centerCrop()
                .into(imageView);
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static String getlastName(String input) {
        String[] namr_arr = null;
        if (input.contains(" ")) {
            namr_arr = input.split(" ");
            return namr_arr[1];
        }
        return "";

    }

   /* public static ArrayList<CharlieChatterContact> removeDuplicates(ArrayList<CharlieChatterContact> allEvents) {
        //  ArrayList<CharlieChatterContact> allEvents = // fill with your events.
        ArrayList<CharlieChatterContact> noRepeat = new ArrayList<CharlieChatterContact>();

        for (CharlieChatterContact event : allEvents) {
            boolean isFound = false;
            // check if the event name exists in noRepeat
            for (CharlieChatterContact e : noRepeat) {
                if (e.getFirstName().equals(event.getFirstName()))
                    isFound = true;
            }

            if (!isFound) noRepeat.add(event);
        }
    return noRepeat;
    }*/
   /* public static ArrayList<CharlieChatterContact> removeRegisterPhone(ArrayList<CharlieChatterContact> allEvents,String phone) {
        //  ArrayList<CharlieChatterContact> allEvents = // fill with your events.
        ArrayList<CharlieChatterContact> noRepeat = new ArrayList<CharlieChatterContact>();

        for (CharlieChatterContact event : allEvents) {
            boolean isFound = false;
            // check if the event name exists in noRepeat
            for (CharlieChatterContact e : noRepeat) {
                if (e.getNumber().equals(phone))
                    isFound = true;
            }

            if (isFound) noRepeat.add(event);
        }
        return noRepeat;
    }*/

    public static  String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
    /**
     * @param encodedString
     * @return bitmap (from given string)
     */
    public static Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }
    static  Bitmap bmp=null;
    static String email11;
    static  String name111;
    public static void  SetBitmap(Bitmap encodedString){
       bmp=encodedString;

    }
    public static void  SetEmail(String email){
        email11=email;
    }
    public static void  SetName(String name){
        name111=name;
    }
    public static String  getName(){
        return name111;
    }
    public static String   getEmail(){
        return  email11;
    }

    public static Bitmap getBitMap(){
       return bmp;
    }

    public static String getCurrentTimezoneOffset() {
        TimeZone tz = TimeZone.getDefault();
        Calendar cal = GregorianCalendar.getInstance(tz);
        int offsetInMillis = tz.getOffset(cal.getTimeInMillis());
        String offset = String.format("%02d:%02d", Math.abs(offsetInMillis / 3600000), Math.abs((offsetInMillis / 60000) % 60));
        offset = "GMT"+(offsetInMillis >= 0 ? "+" : "-") + offset;
        return offset;
    }
  /*  public static Bitmap LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            drawableToBitmap(new PictureDrawable(d));
            return d;
        } catch (Exception e) {
            return null;
        }
    }*/
    public static  Bitmap drawableToBitmap(PictureDrawable pd) {
        Bitmap bm = Bitmap.createBitmap(pd.getIntrinsicWidth(), pd.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        canvas.drawPicture(pd.getPicture());
        return bm;
    }
    public static String DateTime(Date date) {
        SimpleDateFormat dateformat = new SimpleDateFormat(DATE_FORMAT);
        TimeZone tz = TimeZone.getTimeZone("UTC"); //Will return your device current time zone
        getCurrentTimezoneOffset();
      //  formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        dateformat.setTimeZone(tz); //Set the time zone to your simple date formatter
        return dateformat.format(date);
    }
    public static  String getDateCurrentTimeZone(long timestamp) {
        try{
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(timestamp * 1000);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            Date currenTimeZone = calendar.getTime();
            return sdf.format(currenTimeZone);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    private String getDate(String OurDate)
    {
        try
        {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = formatter.parse(OurDate);

            SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy HH:mm"); //this format changeable
            dateFormatter.setTimeZone(TimeZone.getDefault());
            OurDate = dateFormatter.format(value);

            //Log.d("OurDate", OurDate);
        }
        catch (Exception e)
        {
            OurDate = "00-00-0000 00:00";
        }
        return OurDate;
    }

    public static long currentTimeMillisUTC() {
        return Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                .getTimeInMillis();
    }

    public static long toLocal(long timestamp) {
        Calendar cal = Calendar.getInstance();
        int offset = cal.getTimeZone().getOffset(timestamp);
        return timestamp - offset;
    }
    public String getTimestamp(){
        // prints "Sep 6, 2009 9:03:20 PM"
        DateFormat dateTimeInstance = SimpleDateFormat.getDateTimeInstance();
        System.out.println(dateTimeInstance.format(Calendar.getInstance().getTime()));

        // prints "Sep 6, 2009"
        DateFormat dateInstance = SimpleDateFormat.getDateInstance();
        System.out.println(dateInstance.format(Calendar.getInstance().getTime()));

        // prints "9:03:20 PM"
        DateFormat timeInstance = SimpleDateFormat.getTimeInstance();
        System.out.println(timeInstance.format(Calendar.getInstance().getTime())+","+dateInstance.format(Calendar.getInstance().getTime()));

        return timeInstance.format(Calendar.getInstance().getTime())+" "+dateInstance.format(Calendar.getInstance().getTime());

    }
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            e.printStackTrace();
            return null;
        }
    }
    public static void sortNotifications(ArrayList<NotificationModel> list) {

        Collections.sort(list,Collections.reverseOrder(new Comparator<NotificationModel>() {
            @Override
            public int compare(NotificationModel a1, NotificationModel a2) {
                // String implements Comparable
                return (a1.getTime()).compareTo(a2.getTime());
               /*// System.out.println("######(a1.getTimestamp()).compareTo(a2.getTimestamp()##########"+(a1.getTime()).compareTo(a2.getTime()));
                if(!TextUtils.isEmpty(a1.getTime()) || !TextUtils.isEmpty(a2.getTime()))
                    return (a1.getTime()).compareTo(a2.getTime());
                else
                    return 0;*/



            }
        }));
    }
    public static void sortRecentChats(ArrayList<ChatRoom> list) {

        Collections.sort(list,Collections.reverseOrder(new Comparator<ChatRoom>() {
            @Override
            public int compare(ChatRoom a1, ChatRoom a2) {
                // String implements Comparable
                return (a1.getTimestamp()).compareTo(a2.getTimestamp());
            /*  //  System.out.println("######(a1.getTimestamp()).compareTo(a2.getTimestamp()##########"+(a1.getTimestamp()).compareTo(a2.getTimestamp()));
                if(!TextUtils.isEmpty(a1.getTimestamp()) || !TextUtils.isEmpty(a2.getTimestamp()))
                    return (a1.getTimestamp()).compareTo(a2.getTimestamp());
                else
                    return 0;*/



            }
        }));
    }
    public static void sortDates(ArrayList<Message> list) {
        Collections.sort(list, new Comparator<Message>() {
            /* This comparator will sort AppDetail objects alphabetically. */
            @Override
            public int compare(Message a1, Message a2) {
                // String implements Comparable
         return (a1.getSendTime()).compareTo(a2.getSendTime());

               /* System.out.println("######(a1.getTimestamp()).compareTo(a2.getTimestamp()##########"+(a1.getSendTime()).compareTo(a2.getSendTime()));
                if(!(TextUtils.isEmpty(a1.getSendTime())) || !TextUtils.isEmpty(a2.getSendTime().toString())){
                    if(!TextUtils.isEmpty(a1.getSendTime().toString()) || !TextUtils.isEmpty(a2.getSendTime().toString()))
                        return (a1.getSendTime()).compareTo(a2.getSendTime());
                }else{
                    return 0;
                }

                return 0;*/

            }
        });
    }
    public static  void unregisterDevice(Context ctx) {
        Log.d("unregisterDevice", "unregisterDevice");
        Gson gson = new Gson();
        String request = gson.toJson(new PushUnRegisterDeviceRequest(GlobalFunctions.getDeviceUUID(ctx)));
        JSONObject jsonRequest;
        JsonObjectRequest jsonObjectRequest = null;
        try {
            jsonRequest = new JSONObject(request);
            jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    URLUtil.PUSHWHOOSH_UN_REGISTERE_DEVICE,
                    jsonRequest,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject res = response;


                                Log.d("RESPONSE", "RESPONS" + response.toString());


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                    String file_string = "";
                    //  Toast.makeText(ctx,error.toString(),Toast.LENGTH_LONG).show();

                    NetworkResponse response = error.networkResponse;
                    if (response != null && response.data != null) {
                        try {
                            file_string = new String(error.networkResponse.data, "UTF-8");
                            JSONObject errorObj = new JSONObject(file_string);
                            String error_res;

                            if (errorObj.has("error")) {
                                error_res = errorObj.getString("error");
                            } else {
                                error_res = "";
                            }

                                Log.d("RESPONSE", "RESPONS" + file_string);
                        } catch (UnsupportedEncodingException e) {
                        } catch (JSONException e) {
                        }
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObjectRequest != null) {
            VolleyManager.getInstance(ctx).addToRequestQueue(jsonObjectRequest);
        }
    }
}
