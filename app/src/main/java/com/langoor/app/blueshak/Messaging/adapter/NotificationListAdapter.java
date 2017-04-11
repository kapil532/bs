package com.langoor.app.blueshak.Messaging.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.langoor.blueshak.R;
import com.langoor.app.blueshak.services.model.NotificationModel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NotificationListAdapter extends BaseAdapter
{

    Context cxt;
    ArrayList<NotificationModel> list;
    private LayoutInflater inflator;

    public NotificationListAdapter(ArrayList<NotificationModel> list, Activity cxt) {
        // TODO Auto-generated constructor stub
        this.list=list;
        this.cxt =cxt;
        inflator = cxt.getLayoutInflater();

    }


    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflator.inflate(R.layout.notification_single_row, null);
            holder = new ViewHolder();
            holder.appName = (TextView) convertView.findViewById(R.id.row_message);
            holder.iconview = (TextView) convertView.findViewById(R.id.row_time);
            convertView.setTag(holder);
            convertView.setTag(R.id.row_message, holder.appName);
            convertView.setTag(R.id.row_time, holder.iconview);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

            holder.appName.setText(list.get(position).getMessage());
            String dateString=list.get(position).getTime();

            Date date=fromStringToDate(dateString);
            if(date!=null && !date.toString().isEmpty() ){
                if(DateUtils.isToday(date.getTime())){
                    dateString=new SimpleDateFormat("hh:mm a").format(date);
                }else{
                    dateString=new SimpleDateFormat("dd/MM/yyyy").format(date);
                }
            }
            holder.iconview.setText(dateString);
            return convertView;
       /* }
        return  null;*/
    }
    public static Date fromStringToDate(String dateString) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
        try {
            return dateFormat.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
    private static class ViewHolder
    {
        TextView appName;
        TextView iconview;
    }

}
