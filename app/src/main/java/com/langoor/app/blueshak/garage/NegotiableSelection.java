package com.langoor.app.blueshak.garage;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.langoor.app.blueshak.root.RootActivity;
import com.langoor.blueshak.R;

/**
 * Created by Kapil Katiyar on 6/19/2017.
 */

 public class NegotiableSelection extends RootActivity
{
    CardView user_confirm;
    Switch item_negotiable;
    Switch hide_item;
    TextView activity_title,cancel;
   static Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.negotiable_selection);
        activity = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
          /*  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);*/
        LayoutInflater inflator = LayoutInflater.from(this);
        View v = inflator.inflate(R.layout.action_bar_titlel, null);
        activity_title=(TextView)v.findViewById(R.id.title);

        toolbar.addView(v);
        cancel=(TextView)v.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    closeThisActivity();

            }
        });

        user_confirm = (CardView)findViewById(R.id.user_confirm);
        user_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(item_negotiable.isChecked() || hide_item.isChecked())
                {

                }
                else
                {
                    Toast.makeText(NegotiableSelection.this,"Please select one!",Toast.LENGTH_LONG).show();
                }

            }
        });

        item_negotiable =(Switch)findViewById(R.id.item_negotiable);
        hide_item =(Switch)findViewById(R.id.hide_item);
    }
    public static void closeThisActivity(){
        if(activity!=null)
        {
            activity.finish();
        }
    }

}
