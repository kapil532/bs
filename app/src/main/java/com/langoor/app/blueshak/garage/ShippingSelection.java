package com.langoor.app.blueshak.garage;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
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
    View local_shipping_l_v,allow_international_shi_l_v,intl_shipping_cost_l_v;
   LinearLayout local_shipping_l,allow_international_shi_l,intl_shipping_cost_l,shibble_l;
    EditText local_shipping_cost;
    EditText intl_shipping_cost_e;
    EditText time_to_deliver_e;
    Switch Shippible_s;
   Switch Shipping_is_free;


    final int SHIPPINGISFREEACTIVE=121;
    final int ALLOWINTERNATIONALACTIVE=141;

    final int SHIPPINGISFREEDEACTIVE=151;
    final int ALLOWINTERNATIONALDEACTIVE=161;
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
        Shipping_is_free.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    hideAndShow(SHIPPINGISFREEACTIVE);
                }
                else
                {
                    hideAndShow(SHIPPINGISFREEDEACTIVE);
                }
            }
        });

        local_shipping_l =(LinearLayout) findViewById(R.id.local_shipping_l);
        local_shipping_l_v =(View) findViewById(R.id.local_shipping_l_v);
        allow_international_shi_l =(LinearLayout) findViewById(R.id.allow_international_shi_l);
        allow_international_shi_l_v =(View) findViewById(R.id.allow_international_shi_l_v);
        intl_shipping_cost_l =(LinearLayout) findViewById(R.id.intl_shipping_cost_l);

        intl_shipping_cost_l_v =(View) findViewById(R.id.intl_shipping_cost_l_v);


        time_to_deliver_e =(EditText) findViewById(R.id.time_to_deliver_e);
        intl_shipping_cost_e =(EditText) findViewById(R.id.intl_shipping_cost_e);
        local_shipping_cost =(EditText) findViewById(R.id.local_shipping_cost);

        allow_international_shi_s =(Switch)findViewById(R.id.allow_international_shi_s);
        shibble_l =(LinearLayout)findViewById(R.id.shibble_l);
        shibble_l.setVisibility(View.GONE);
        Shippible_s =(Switch)findViewById(R.id.Shippible_s);
        Shippible_s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    shibble_l.setVisibility(View.VISIBLE);
                }
                else
                {
                    shibble_l.setVisibility(View.GONE);
                }
            }
        });

        allow_international_shi_s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    hideAndShow(ALLOWINTERNATIONALACTIVE);
                }
                else
                {
                    hideAndShow(ALLOWINTERNATIONALDEACTIVE);
                }
            }
        });

        hideAndShow(SHIPPINGISFREEDEACTIVE);
        hideAndShow(ALLOWINTERNATIONALDEACTIVE);
    }
    public static void closeThisActivity(){
        if(activity!=null)
        {
            activity.finish();
        }
    }


private void hideAndShow(int key)
{
   switch (key)
   {
       case SHIPPINGISFREEACTIVE:
           local_shipping_l.setVisibility(View.VISIBLE);
           allow_international_shi_l.setVisibility(View.VISIBLE);
           local_shipping_l_v.setVisibility(View.VISIBLE);
           allow_international_shi_l_v.setVisibility(View.VISIBLE);
           intl_shipping_cost_l.setVisibility(View.GONE);
           intl_shipping_cost_l_v.setVisibility(View.GONE);
           allow_international_shi_s.setChecked(false);
           break;


       case ALLOWINTERNATIONALACTIVE:
           intl_shipping_cost_l.setVisibility(View.VISIBLE);
           intl_shipping_cost_l_v.setVisibility(View.VISIBLE);

           break;

       case SHIPPINGISFREEDEACTIVE:

           local_shipping_l.setVisibility(View.GONE);
           allow_international_shi_l.setVisibility(View.GONE);
           intl_shipping_cost_l.setVisibility(View.GONE);
           local_shipping_l_v.setVisibility(View.GONE);
           allow_international_shi_l_v.setVisibility(View.GONE);
           intl_shipping_cost_l_v.setVisibility(View.GONE);
           break;


       case ALLOWINTERNATIONALDEACTIVE:

           intl_shipping_cost_l.setVisibility(View.GONE);
           intl_shipping_cost_l_v.setVisibility(View.GONE);
           break;
   }
}


}
