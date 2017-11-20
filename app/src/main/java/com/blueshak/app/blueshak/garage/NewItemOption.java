package com.blueshak.app.blueshak.garage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.blueshak.app.blueshak.Messaging.helper.Constants;
import com.blueshak.app.blueshak.category.CategoryActivity;
import com.blueshak.app.blueshak.global.GlobalFunctions;
import com.blueshak.app.blueshak.global.GlobalVariables;
import com.blueshak.app.blueshak.root.RootActivity;
import com.blueshak.app.blueshak.services.model.CategoryListModel;
import com.blueshak.app.blueshak.services.model.CategoryModel;
import com.blueshak.app.blueshak.services.model.CreateImageModel;
import com.blueshak.app.blueshak.services.model.CreateProductModel;
import com.blueshak.app.blueshak.services.model.CurrencyListModel;
import com.blueshak.app.blueshak.services.model.CurrencyModel;
import com.blueshak.app.blueshak.services.model.LocationModel;
import com.blueshak.app.blueshak.services.model.SalesModel;
import com.blueshak.blueshak.R;
import com.crashlytics.android.Crashlytics;
import com.mvc.imagepicker.ImagePicker;
import com.mvc.imagepicker.ImageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kapil Katiyar on 11/19/2017.
 */

public class NewItemOption extends RootActivity {

    private ArrayList<String> selectedCategoryString = new ArrayList<String>();
    private ArrayList<String> selectedCategoryIDs = new ArrayList<String>();
    private TextView category,select_sale;
    TextView location_Text;
    private TextView close_button;
    private LinearLayout seporater;
    public static final String CREATE_ITEM_BUNDLE_KEY = "CreateItemSaleFragmentBundleKey";
    public static final String CREATE_ITEM_SALE_BUNDLE_KEY = "CreateItemSelectSaleFragmentBundleKey";
    public static final String CREATE_ITEM_TYPE_BUNDLE_KEY = "CreateItemSaleFragmentTypeBundleKey";
    public static final String CREATE_ITEM_LOCATION_BUNDLE_KEY = "CreateItemActivityLocationBundleKey";
    public static final String CREATE_ITEM_CATEGORY_BUNDLE_KEY = "CreateItemActivityCategoryBundleKey";
    public static final String CREATE_ITEM_CURRENCY_BUNDLE_KEY = "CreateItemActivityCurrencyBundleKey";
    private ImageView go_back;
    private Button save;
    public static CreateProductModel productModel = null;
    String TAG = "NEW ITEM OPTION";
    private static GlobalVariables globalVariables = new GlobalVariables();
    private Switch shippable, nagotiable, is_new_old, is_product_new;
    private LinearLayout category_content, add_to_garage_sale_content, pd_nagotiable_l, shipping_l;
    private Context context;
    private Activity activity;
    private CategoryListModel clm;
    private Boolean type_garage = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_item_option);
        activity =this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        LayoutInflater inflator = LayoutInflater.from(this);
        View v = inflator.inflate(R.layout.action_bar_titlel, null);
        ((TextView) v.findViewById(R.id.title)).setText("New Item - Options");
        toolbar.addView(v);

        close_button = (TextView) v.findViewById(R.id.cancel);
        context = this;
        close_button.setVisibility(View.GONE);
        go_back = (ImageView) findViewById(R.id.go_back);
        go_back.setVisibility(View.VISIBLE);
        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeThisActivity();
            }
        });

        try {
            pd_nagotiable_l = (LinearLayout) findViewById(R.id.pd_nagotiable_l);
            pd_nagotiable_l.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent mIntent = new Intent(context, NegotiableSelection.class);
                    startActivityForResult(mIntent, globalVariables.REQUEST_CODE_NEGOTIABLE);
                }
            });

            shipping_l = (LinearLayout) findViewById(R.id.shipping_l);
            shipping_l.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mIntent = new Intent(context, ShippingSelection.class);
                    startActivityForResult(mIntent, globalVariables.REQUEST_CODE_SHIPPING);
                }
            });
            category = (TextView) findViewById(R.id.pd_category);
            category_content = (LinearLayout) findViewById(R.id.category_content);
            category_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = CategoryActivity.newInstance(context, false, category.getText().toString());
                    startActivityForResult(intent, globalVariables.REQUEST_CODE_SELECT_CATEGORY);
                }
            });
            add_to_garage_sale_content = (LinearLayout) findViewById(R.id.add_to_garage_sale_content);
            add_to_garage_sale_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = GarageSalesList.newInstance(context, "");
                    startActivityForResult(intent, globalVariables.REQUEST_CODE_SELECT_SALE);
                }
            });
            save = (Button) findViewById(R.id.pd_publish);
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickProcessing();
                }
            });
            shippable = (Switch) findViewById(R.id.pd_shippable);
            is_product_new = (Switch) findViewById(R.id.is_product_new);
            nagotiable = (Switch) findViewById(R.id.pd_nagotiable);
            is_new_old = (Switch) findViewById(R.id.is_new_old);
            select_sale = (TextView) findViewById(R.id.select_sale);

            // Log.d("VALUESSS","SHIPPINGCOST"+(CreateProductModel)getArguments().getSerializable(CREATE_ITEM_BUNDLE_KEY));

            if (productModel != null)
            {
                Log.d("VALUESSS", "SHIPPINGCOST NT NULLl" + productModel);
                  setValues();
            } else {
                ShippingSelection.isShippable = false;
                NegotiableSelection.comeFromScreen = false;
                productModel = new CreateProductModel();
            }


            category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = CategoryActivity.newInstance(context, false, category.getText().toString());
                    startActivityForResult(intent, globalVariables.REQUEST_CODE_SELECT_CATEGORY);
                }
            });
        } catch (NullPointerException e) {
            Log.d(TAG, "NullPointerException");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            Log.d(TAG, "NumberFormatException");
            e.printStackTrace();
        } catch (Exception e) {
            Log.d(TAG, "Exception");
            e.printStackTrace();
        }


    }

    //item_negotiable =nagotiable
    boolean item_negotiable = false;
    boolean hide_item = false;
    boolean isShippable = false;
    boolean shipping_foc = false;
    boolean is_intl_shipping = false;


    String intl_shipping_cost = "";
    String time_to_deliver = "";
    String local_shipping_cost = "";

    private void onClickProcessing() {
//        Log.d("STR DESC0","DESCCCCC"+str_desc);
        /*if (item_negotiable)
            productModel.setNegotiable(true);*/

        if (is_new_old.isChecked())
            productModel.setIs_pickup(true);

     /*   if (hide_item)
            productModel.setHide_item_price(true);*/

       /* if (shipping_foc)
            productModel.setShipping_foc(true);*/


        Log.d(TAG, "SHIPPPABLE" + shippable.isChecked() + "--" + is_new_old.isChecked());

        /*if (selectedCategoryIDs.isEmpty()) {
            Toast.makeText(activity, "Please fill the product Category", Toast.LENGTH_LONG).show();
        } */
            /*String country=GlobalFunctions.getSharedPreferenceString(context,GlobalVariables.SHARED_PREFERENCE_COUNTRY);
            productModel.setCountry_short(country);
          */
        productModel.setCategories(selectedCategoryIDs);


       /* if (item_negotiable)
            productModel.setNegotiable(true);
        else
            productModel.setNegotiable(false);
*/

     /*   if (isShippable)
            productModel.setShippable(true);
        else
            productModel.setShippable(false);*/

     /*   if (hide_item)
            productModel.setHide_item_price(true);
        else
            productModel.setHide_item_price(false);*/

      /*  if (shipping_foc)
            productModel.setShipping_foc(true);
        else
            productModel.setShipping_foc(false);*/

       /* if (is_intl_shipping)
            productModel.setIs_intl_shipping(true);
        else
            productModel.setIs_intl_shipping(false);


        if (is_new_old.isChecked())
            productModel.setIs_pickup(true);
        else
            productModel.setIs_pickup(false);


        if (is_product_new.isChecked())
            productModel.setIs_product_new(true);
        else
            productModel.setIs_product_new(false);*/


       /* productModel.setIntl_shipping_cost(intl_shipping_cost);
        productModel.setTime_to_deliver(time_to_deliver);
        productModel.setLocal_shipping_cost(local_shipping_cost);

        String token = GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);*/

        if (clm != null)
            selectedCategoryIDs = clm.getIdsforNames(selectedCategoryString);
    }

    public  void closeThisActivity() {
        Log.d("closeThisActivity", "closeThisActivity the pick location issues#######");
        if (activity != null) {
            activity.finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            Log.i(TAG, "on activity result");
            if (resultCode == activity.RESULT_OK) {



               if (requestCode == globalVariables.REQUEST_CODE_SELECT_CATEGORY) {
                    Log.i(TAG, "REQUEST_CODE_SELECT_CATEGORY " + requestCode);
                    CategoryModel categoryModel = (CategoryModel) data.getExtras().getSerializable(CREATE_ITEM_CATEGORY_BUNDLE_KEY);
                    category.setText(categoryModel.getName());
                    selectedCategoryString.clear();
                    selectedCategoryString.add(categoryModel.getName());
                } else if (requestCode == globalVariables.REQUEST_CODE_SELECT_SALE) {
                    Log.i(TAG, "REQUEST_CODE_SELECT_SALE " + requestCode);
                    SalesModel salesModel = (SalesModel) data.getExtras().getSerializable(CREATE_ITEM_SALE_BUNDLE_KEY);
                    type_garage = true;
                    select_sale.setText(salesModel.getName());
                    productModel.setSale_id(salesModel.getId());
                    productModel.setSaleType(GlobalVariables.TYPE_GARAGE);
                }
                else if (requestCode == globalVariables.REQUEST_CODE_NEGOTIABLE)
                {
                    Log.i(TAG, "REQUEST_CODE_SELECT_CATEGORY " + requestCode);

                    item_negotiable = data.getExtras().getBoolean("item_negotiable");
                    hide_item = data.getExtras().getBoolean("hide_item");
                    Log.d("AAAA", "&&&&&&&&&&" + item_negotiable + "--" + hide_item);

                } else if (requestCode == globalVariables.REQUEST_CODE_SHIPPING)
                {

                    isShippable = ShippingSelection.isShippable;
                    shipping_foc = ShippingSelection.shipping_foc;
                    is_intl_shipping = ShippingSelection.is_intl_shipping;

                    intl_shipping_cost = ShippingSelection.intl_shipping_cost;
                    time_to_deliver = ShippingSelection.time_to_deliver;
                    local_shipping_cost = ShippingSelection.local_shipping_cost_;

                    Log.d("VALUES SET", "SETALLVALUES"
                            + shipping_foc + "" +
                            "---" + intl_shipping_cost + "" +
                            "---" + local_shipping_cost + "" +
                            "---" + is_intl_shipping);

                }
            }
        } catch (NullPointerException e) {
            Log.d(TAG, "NullPointerException");
            e.printStackTrace();
            Crashlytics.log(e.getMessage());
        } catch (NumberFormatException e) {
            Crashlytics.log(e.getMessage());
            Log.d(TAG, "NumberFormatException");
            e.printStackTrace();
        } catch (Exception e) {
            Crashlytics.log(e.getMessage());
            Log.d(TAG, "Exception");
            e.printStackTrace();
        }
    }
    private boolean[] is_checked;

    @Override
    public void onStart() {
        super.onStart();

        new Thread(new Runnable() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        clm = GlobalFunctions.getCategories(activity);
                        if (clm != null)
                            is_checked = new boolean[clm.getCategoryNames().size()];
                    }
                });
            }
        }).start();


    }

    private void setValues() {
        Log.d("VALUESSS", "SHIPPINGCOST VALUES INSIDE");
        if (productModel != null) {

            add_to_garage_sale_content.setVisibility(View.GONE);
            if (productModel.getSaleType() != null) {
                if (productModel.getSaleType().equalsIgnoreCase(GlobalVariables.TYPE_GARAGE)) {
                    type_garage = true;
                }
            }

            //Changes
            isShippable = ShippingSelection.isShippable = productModel.isShippable();
            shipping_foc = ShippingSelection.shipping_foc = productModel.isShipping_foc();
            is_intl_shipping = ShippingSelection.is_intl_shipping = productModel.is_intl_shipping();

            intl_shipping_cost = ShippingSelection.intl_shipping_cost = productModel.getIntl_shipping_cost();
            time_to_deliver = ShippingSelection.time_to_deliver = productModel.getTime_to_deliver();
            local_shipping_cost = ShippingSelection.local_shipping_cost_ = productModel.getLocal_shipping_cost();

            NegotiableSelection.comeFromScreen = true;
            hide_item = NegotiableSelection.bool_hide_item_ = productModel.isHide_item_price();
            item_negotiable = NegotiableSelection.bool_item_negotiable_ = productModel.isNegotiable();
            Log.d("SHIPPINGCOST", "SHIPPINGCOST" + productModel.isNegotiable() + "&&&$$$$" + productModel.isHide_item_price());




            shippable.setChecked(productModel.isShippable() ? true : false);
           /* is_new_old.setChecked(productModel.is_product_new()?true:false);*/
            is_new_old.setChecked(productModel.is_pickup() ? true : false);
            nagotiable.setChecked(productModel.isNegotiable() ? true : false);
            selectedCategoryIDs.clear();
            selectedCategoryIDs.addAll(productModel.getCategories());
            if (clm != null) {
                selectedCategoryString.clear();
                selectedCategoryString.addAll(clm.getNamesforIDs(selectedCategoryIDs));
                setCategory();
            } else {
                clm = GlobalFunctions.getCategories(context);
                selectedCategoryString.clear();
                selectedCategoryString.addAll(clm.getNamesforIDs(selectedCategoryIDs));
                setCategory();
            }
           /* createSaleItem(context,productModel);*/
        }
    }

    private void setCategory() {
        if (selectedCategoryString.size() > 0) {
            String temp = "";
            for (int i = 0; i < selectedCategoryString.size(); i++) {
                temp = i == 0 ? selectedCategoryString.get(0) : temp + ", " + selectedCategoryString.get(i);
            }
            if (category != null) {
                category.setText(temp);
                category.setFocusable(false);
             /*   if(shippable.isChecked())
                    zipcode_actv.requestFocus();*/
            }
        }

    }
}
