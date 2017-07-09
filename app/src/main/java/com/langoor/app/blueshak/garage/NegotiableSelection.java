package com.langoor.app.blueshak.garage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
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
    Button pd_publish;
    Switch item_negotiable;
    Switch hide_item;
    TextView activity_title,cancel;
   static Activity activity;

    public static boolean bool_item_negotiable_=false;
    public static boolean bool_hide_item_=false;
    public static boolean comeFromScreen=false;
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
        activity_title.setText("Negotiable");
        toolbar.addView(v);
        cancel=(TextView)v.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    closeThisActivity();

            }
        });

        pd_publish = (Button)findViewById(R.id.pd_publish);
        pd_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // if(item_negotiable.isChecked() || hide_item.isChecked())
                {
                    Intent intent = new Intent();
                    intent.putExtra("item_negotiable", item_negotiable.isChecked());
                    intent.putExtra("hide_item", hide_item.isChecked());
                    setResult(Activity.RESULT_OK, intent);
                    comeFromScreen=true;
                    bool_item_negotiable_=item_negotiable.isChecked();
                    bool_hide_item_=hide_item.isChecked();
                    finish();
                }
//                else
//                {
//                    Toast.makeText(NegotiableSelection.this,"Please select one!",Toast.LENGTH_LONG).show();
//                }

            }
        });

        item_negotiable =(Switch)findViewById(R.id.item_negotiable);
        item_negotiable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked)
                {
                    switchButton(hide_item_);
                }
            }
        });

        hide_item =(Switch)findViewById(R.id.hide_item);
        hide_item.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {
                    switchButton(item_negotiable_);
                }
            }
        });

        if(bool_item_negotiable_)
        {
            item_negotiable.setChecked(true);
        }
        else
        {
            switchButton(hide_item_);
        }
        if(bool_hide_item_)
        {
            hide_item.setChecked(true);

            switchButton(item_negotiable_);
        }

    }
    public static void closeThisActivity(){
        if(activity!=null)
        {
            activity.finish();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if(!comeFromScreen)
        {
            bool_item_negotiable_=false;
            bool_hide_item_=false;
        }
    }

    final int item_negotiable_=1002;
final int hide_item_=1003;
    private void switchButton(int key)
    {

        switch (key)
        {
            case item_negotiable_:
            item_negotiable.setChecked(true);

            break;

            case hide_item_:
                hide_item.setChecked(false);

                break;
        }

    }





}
