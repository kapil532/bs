package com.langoor.app.blueshak.garage;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.langoor.app.blueshak.root.RootActivity;
import com.langoor.blueshak.R;

/**
 * Created by Kapil Katiyar on 6/19/2017.
 */

 public class ShippingSelection extends RootActivity
{
    Button pd_publish;
    Switch allow_international_shi_s;
    TextView activity_title,cancel;
   static Activity activity;
   LinearLayout local_shipping_l,allow_international_shi_l,intl_shipping_cost_l;
    EditText local_shipping_cost;
    EditText intl_shipping_cost_e;
    EditText time_to_deliver_e;
   Switch Shipping_is_free;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shipping_selection);
        activity = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        LayoutInflater inflator = LayoutInflater.from(this);
        View v = inflator.inflate(R.layout.action_bar_titlel, null);
        activity_title=(TextView)v.findViewById(R.id.title);
        activity_title.setText("Shipping");
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



            }
        });

        Shipping_is_free =(Switch)findViewById(R.id.Shipping_is_free);
        local_shipping_l =(LinearLayout) findViewById(R.id.local_shipping_l);
        allow_international_shi_l =(LinearLayout) findViewById(R.id.allow_international_shi_l);
        intl_shipping_cost_l =(LinearLayout) findViewById(R.id.intl_shipping_cost_l);
        time_to_deliver_e =(EditText) findViewById(R.id.time_to_deliver_e);
        intl_shipping_cost_e =(EditText) findViewById(R.id.intl_shipping_cost_e);
        local_shipping_cost =(EditText) findViewById(R.id.local_shipping_cost);

        allow_international_shi_s =(Switch)findViewById(R.id.allow_international_shi_s);
    }
    public static void closeThisActivity(){
        if(activity!=null)
        {
            activity.finish();
        }
    }


private void hideAndShow()
{

}


}
