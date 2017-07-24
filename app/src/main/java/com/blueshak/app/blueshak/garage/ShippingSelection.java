package com.blueshak.app.blueshak.garage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.blueshak.app.blueshak.root.RootActivity;
import com.blueshak.app.blueshak.util.InputFilterMinMax;
import com.blueshak.blueshak.R;

/**
 * Created by Kapil Katiyar on 6/19/2017.
 */

public class ShippingSelection extends RootActivity {
    Button pd_publish;
    Switch allow_international_shi_s;
    TextView activity_title, cancel;
    static Activity activity;
    View local_shipping_l_v, allow_international_shi_l_v, intl_shipping_cost_l_v;
    LinearLayout local_shipping_l, allow_international_shi_l, intl_shipping_cost_l, shibble_l;
    EditText local_shipping_cost;
    EditText intl_shipping_cost_e;
    EditText time_to_deliver_e;
    Switch Shippible_s;
    Switch Shipping_is_free;

String localShippingCOst;
String intShippingCost;
String timeToDeliver;



    public  static boolean isShippable=false;
    public  static boolean shipping_foc=false;
    public  static boolean is_intl_shipping=false;
    public  static String intl_shipping_cost="";
    public  static String time_to_deliver="";
    public  static String local_shipping_cost_="";


public static String price_default="";
    final int SHIPPINGISFREEACTIVE = 121;
    final int ALLOWINTERNATIONALACTIVE = 141;

    final int SHIPPINGISFREEDEACTIVE = 151;
    final int ALLOWINTERNATIONALDEACTIVE = 161;
    String ss="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shipping_selection);
        activity = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        LayoutInflater inflator = LayoutInflater.from(this);
        View v = inflator.inflate(R.layout.action_bar_titlel, null);
        activity_title = (TextView) v.findViewById(R.id.title);
        activity_title.setText("Shipping");
        toolbar.addView(v);
        cancel = (TextView) v.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                closeThisActivity();

            }
        });

        pd_publish = (Button) findViewById(R.id.pd_publish);
        pd_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmShipping();

            }
        });

        Shipping_is_free = (Switch) findViewById(R.id.Shipping_is_free);
        Shipping_is_free.setChecked(false);
        Shipping_is_free.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    hideAndShow(SHIPPINGISFREEDEACTIVE);
                } else {

                    hideAndShow(SHIPPINGISFREEACTIVE);
                }
            }
        });

        local_shipping_l = (LinearLayout) findViewById(R.id.local_shipping_l);
        local_shipping_l_v = (View) findViewById(R.id.local_shipping_l_v);
        allow_international_shi_l = (LinearLayout) findViewById(R.id.allow_international_shi_l);
        allow_international_shi_l_v = (View) findViewById(R.id.allow_international_shi_l_v);
        intl_shipping_cost_l = (LinearLayout) findViewById(R.id.intl_shipping_cost_l);

        intl_shipping_cost_l_v = (View) findViewById(R.id.intl_shipping_cost_l_v);


        time_to_deliver_e = (EditText) findViewById(R.id.time_to_deliver_e);
        time_to_deliver_e.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "30")});
        time_to_deliver_e.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s) {

                timeToDeliver=""+s.toString();
            }
        });
        intl_shipping_cost_e = (EditText) findViewById(R.id.intl_shipping_cost_e);
        intl_shipping_cost_e.setHint(price_default);
        intl_shipping_cost_e.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                String text = arg0.toString();
                if (text.contains(".") && text.substring(text.indexOf(".") + 1).length() > 2) {
                    intl_shipping_cost_e.setText(text.substring(0, text.length() - 1));
                    intl_shipping_cost_e.setSelection(intl_shipping_cost_e.getText().length());
                }
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            public void afterTextChanged(Editable arg0) {
                intShippingCost=""+arg0.toString();
            }
        });
        local_shipping_cost = (EditText) findViewById(R.id.local_shipping_cost);
        local_shipping_cost.setHint(price_default);
        local_shipping_cost.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                String text = arg0.toString();
                if (text.contains(".") && text.substring(text.indexOf(".") + 1).length() > 2) {
                    local_shipping_cost.setText(text.substring(0, text.length() - 1));
                    local_shipping_cost.setSelection(local_shipping_cost.getText().length());
                }
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            public void afterTextChanged(Editable arg0) {

                localShippingCOst=""+arg0.toString();
            }
        });
        allow_international_shi_s = (Switch) findViewById(R.id.allow_international_shi_s);


        shibble_l = (LinearLayout) findViewById(R.id.shibble_l);
        shibble_l.setVisibility(View.GONE);
        Shippible_s = (Switch) findViewById(R.id.Shippible_s);
        Shippible_s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    shibble_l.setVisibility(View.VISIBLE);
                    hideAndShow(SHIPPINGISFREEACTIVE);
                    hideAndShow(ALLOWINTERNATIONALDEACTIVE);
                    Shipping_is_free.setChecked(false);
                } else {
                    shibble_l.setVisibility(View.GONE);
                }
            }
        });

        allow_international_shi_s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    hideAndShow(ALLOWINTERNATIONALACTIVE);
                } else {
                    hideAndShow(ALLOWINTERNATIONALDEACTIVE);
                }
            }
        });

        hideAndShow(SHIPPINGISFREEACTIVE);
        hideAndShow(ALLOWINTERNATIONALDEACTIVE);


        if(isShippable)
        {

            Shippible_s.setChecked(true);
            hideAndShow(SHIPPINGISFREEACTIVE);
            intl_shipping_cost_e.setText(intl_shipping_cost);
            time_to_deliver_e.setText(time_to_deliver);
            local_shipping_cost.setText(local_shipping_cost_);

            Log.d("LOGSSSS","SHIPPINGCOST ---%%%%^"+shipping_foc);
            if(shipping_foc)
            {
                hideAndShow(SHIPPINGISFREEDEACTIVE);
                Shipping_is_free.setChecked(true);
            }
             if(is_intl_shipping)
             {
                 allow_international_shi_s.setChecked(true);
                 hideAndShow(ALLOWINTERNATIONALACTIVE);
             }
        }




    }


    @Override
    protected void onStop() {
        super.onStop();

      if (!isShippable) {
          isShippable = false;
          shipping_foc = false;
          is_intl_shipping = false;
          intl_shipping_cost = "";
          time_to_deliver = "";
          local_shipping_cost_ = "";
      }
    }

    public static void closeThisActivity() {
        if (activity != null) {
            activity.finish();
        }
    }


    private void hideAndShow(int key) {
        switch (key) {
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

    void confirmShipping()
    {
         if(Shippible_s.isChecked())
         {
             if (!(Shipping_is_free.isChecked()))
             {
                 if (!(local_shipping_cost.getText().length() > 0)) {
                     Toast.makeText(this, "Please Enter Local Shiping Cost!", Toast.LENGTH_LONG).show();
                     return;
                 }
                else if (allow_international_shi_s.isChecked())
                 {
                     if (!(intl_shipping_cost_e.getText().length() > 0))
                     {
                         Toast.makeText(this, "Please Enter Int Shipping Cost!", Toast.LENGTH_LONG).show();
                         return;
                     }
                     else
                     {
                         if (!(time_to_deliver_e.getText().length() > 0)) {
                             Toast.makeText(this, "Please Enter time to deliver!", Toast.LENGTH_LONG).show();
                             return;
                         }
                         else if (!(time_to_deliver_e.getText().length() > 0))
                         {
                             Toast.makeText(this, "Please Enter time to deliver!", Toast.LENGTH_LONG).show();
                         }
                         else
                         {

                             finalSubmit();
//                             Toast.makeText(this, "All Done"+localShippingCOst+"--"+intShippingCost+"--"+timeToDeliver, Toast.LENGTH_LONG).show();
                         }
                     }
                 }

                 else if (!(time_to_deliver_e.getText().length() > 0)) {
                     Toast.makeText(this, "Please Enter time to deliver!", Toast.LENGTH_LONG).show();
                     return;
                 }
                 else if (!(time_to_deliver_e.getText().length() > 0))
                 {
                     Toast.makeText(this, "Please Enter time to deliver!", Toast.LENGTH_LONG).show();
                 }
                 else
                 {

                     finalSubmit();
//                     Toast.makeText(this, "All Done"+localShippingCOst+"--"+intShippingCost+"--"+timeToDeliver, Toast.LENGTH_LONG).show();
                 }
             }
             else if (!(time_to_deliver_e.getText().length() > 0))
             {
                 Toast.makeText(this, "Please Enter time to deliver!", Toast.LENGTH_LONG).show();
             } else
             {

                 finalSubmit();
                // Toast.makeText(this, "All Done"+localShippingCOst+"--"+intShippingCost+"--"+timeToDeliver, Toast.LENGTH_LONG).show();
             }

         }
        else
         {
             finalSubmit();

         }

    }




    void finalSubmit()
    {
        Intent intent = new Intent();

        isShippable=Shippible_s.isChecked();
        shipping_foc=Shipping_is_free.isChecked();
        is_intl_shipping=allow_international_shi_s.isChecked();

        intl_shipping_cost=intl_shipping_cost_e.getText().toString();
        local_shipping_cost_=local_shipping_cost.getText().toString();

        if(shipping_foc)
        {
            intl_shipping_cost="";
            local_shipping_cost_="";

        }

        if(!is_intl_shipping)
        {
            intl_shipping_cost="";
        }
        time_to_deliver=time_to_deliver_e.getText().toString();

        if(isShippable)
        {

        }
        else
        {
            intl_shipping_cost="";
            local_shipping_cost_="";
            time_to_deliver="";
        }


       /* intent.putExtra("isShippable", Shippible_s.isChecked());
        intent.putExtra("shipping_foc", Shipping_is_free.isChecked());
        intent.putExtra("is_intl_shipping ", allow_international_shi_s.isChecked());


        Log.d("SET VALUES","SETALLVALUES" +
                "--"+ intl_shipping_cost_e.getText().toString()+"" + "" +
                "--"+Shipping_is_free.isChecked()+"" +
                "---"+time_to_deliver_e.getText().toString()+"" +
                "--"+ local_shipping_cost.getText().toString()+"" +
                "---"+allow_international_shi_s.isChecked());

        intent.putExtra("intl_shipping_cost ", ""+intl_shipping_cost_e.getText().toString());
        intent.putExtra("time_to_deliver ", ""+time_to_deliver_e.getText().toString());
        intent.putExtra("local_shipping_cost ", ""+local_shipping_cost.getText().toString());*/

        setResult(Activity.RESULT_OK, intent);
        finish();

    }





    public static boolean isNumeric(String str)
    {
        return str.matches("-?\\d+(.\\d+)?");
    }
}
