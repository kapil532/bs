package com.blueshak.app.blueshak.garage;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.blueshak.app.blueshak.Messaging.helper.Constants;
import com.blueshak.app.blueshak.PickLocationFromMap;
import com.blueshak.app.blueshak.util.BlueShakLog;
import com.crashlytics.android.Crashlytics;
import com.blueshak.app.blueshak.currency.CurrencyActivity;
import com.blueshak.app.blueshak.photos_add.OnDeletePicture;
import com.blueshak.app.blueshak.photos_add.PhotosAddListAdapter;
import com.blueshak.app.blueshak.services.model.CreateImageModel;
import com.blueshak.app.blueshak.services.model.CurrencyListModel;
import com.blueshak.app.blueshak.services.model.CurrencyModel;
import com.blueshak.app.blueshak.services.model.ProfileDetailsModel;
import com.blueshak.blueshak.R;
import com.blueshak.app.blueshak.MainActivity;
import com.blueshak.app.blueshak.category.CategoryActivity;
import com.blueshak.app.blueshak.global.GlobalFunctions;
import com.blueshak.app.blueshak.global.GlobalVariables;
import com.blueshak.app.blueshak.item.ProductDetail;
import com.blueshak.app.blueshak.login.LoginActivity;
import com.blueshak.app.blueshak.photos_add.Object_PhotosAdd;
import com.blueshak.app.blueshak.services.ServerResponseInterface;
import com.blueshak.app.blueshak.services.ServicesMethodsManager;
import com.blueshak.app.blueshak.services.model.CategoryListModel;
import com.blueshak.app.blueshak.services.model.CategoryModel;
import com.blueshak.app.blueshak.services.model.CreateProductModel;
import com.blueshak.app.blueshak.services.model.ErrorModel;
import com.blueshak.app.blueshak.services.model.IDModel;
import com.blueshak.app.blueshak.services.model.LocationModel;
import com.blueshak.app.blueshak.services.model.PostcodeModel;
import com.blueshak.app.blueshak.services.model.ProductModel;
import com.blueshak.app.blueshak.services.model.SalesModel;
import com.blueshak.app.blueshak.services.model.StatusModel;
import com.mvc.imagepicker.ImagePicker;
import com.mvc.imagepicker.ImageUtils;
import com.tokenautocomplete.TokenCompleteTextView;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;

import org.lucasr.twowayview.TwoWayView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateItemSaleFragment extends Fragment implements TokenCompleteTextView.TokenListener,
        OnDeletePicture {

    String TAG = "CreateItemSaleFragment";
    String[] ary;
    public static final String CREATE_ITEM_BUNDLE_KEY = "CreateItemSaleFragmentBundleKey";
    public static final String CREATE_ITEM_SALE_BUNDLE_KEY = "CreateItemSelectSaleFragmentBundleKey";
    public static final String CREATE_ITEM_TYPE_BUNDLE_KEY = "CreateItemSaleFragmentTypeBundleKey";
    public static final String CREATE_ITEM_LOCATION_BUNDLE_KEY = "CreateItemActivityLocationBundleKey";
    public static final String CREATE_ITEM_CATEGORY_BUNDLE_KEY = "CreateItemActivityCategoryBundleKey";
    public static final String CREATE_ITEM_CURRENCY_BUNDLE_KEY = "CreateItemActivityCurrencyBundleKey";
    public static final String FROM_KEY = "from_key";
    private Toolbar toolbar;
    private TextView category;
    private EditText name, description, saleprice, address;
    private Switch shippable, nagotiable, is_new_old, is_product_new;
    private Button save;
    private boolean[] is_checked;
    static Activity activity;
    private static final int GOOGLE_API_CLIENT_ID = 1;
    private CategoryListModel clm;
    private List<CategoryModel> list_cm = new ArrayList<CategoryModel>();
    private ArrayList<String> selectedCategoryString = new ArrayList<String>();
    private ArrayList<String> selectedCategoryIDs = new ArrayList<String>();
    public static Object_PhotosAdd objectUploadPhoto = new Object_PhotosAdd();
    private static GlobalFunctions globalFunctions = new GlobalFunctions();
    private static GlobalVariables globalVariables = new GlobalVariables();
    private static CreateProductModel productModel = null;
    private TextView mAutocompleteTextView, select_sale, pd_salepricetype;
    private String str_name, str_desc, str_sp, locstr, descriptionstr, catogarystr, postalCode;
    boolean isAvailable = true;
    private static ArrayList<PostcodeModel> autoSuggesstionsList = new ArrayList<PostcodeModel>();
    private static ArrayList<PostcodeModel> selectedAutoSuggesstionsList = new ArrayList<PostcodeModel>();
    private ArrayAdapter<PostcodeModel> autoCompletionAdapter = null;
    /*Zipcodes not needed*/
   /* private MultiAutoCompletionView zipcode_actv;*/
    private int from_key;
    private Context context;
    private LocationModel locationModel = new LocationModel();
    private View view;
    private Boolean type_edit_item = false;
    private Boolean type_garage = false;
    private String loading_label = "Publishing Item...";
    private LinearLayout category_content, add_to_garage_sale_content, pd_nagotiable_l, shipping_l;
    private ProgressBar progress_bar;
    protected static final int REQUEST_ADD_IMAGES = 20;

    public static CreateItemSaleFragment newInstance(Context context, CreateProductModel sales, LocationModel locationModel, String type, int from)
    {
        CreateItemSaleFragment createItemSaleFragment = new CreateItemSaleFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(CREATE_ITEM_BUNDLE_KEY, sales);
        bundle.putSerializable(CREATE_ITEM_TYPE_BUNDLE_KEY, type);
        bundle.putSerializable(CREATE_ITEM_LOCATION_BUNDLE_KEY, locationModel);
        bundle.putInt(FROM_KEY, from);
        createItemSaleFragment.setArguments(bundle);
        return createItemSaleFragment;
    }

    String currency;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
       /* setHasOptionsMenu(true);*/
        context = getActivity();
        activity = getActivity();
        objectUploadPhoto = new Object_PhotosAdd();
        stdCode = GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_LOCATION_COUNTRY);

        Log.d("stdCode", "stdCode" + stdCode);
        view = inflater.inflate(R.layout.item_productdetails_new, container, false);
        NegotiableSelection.bool_hide_item_ = false;
        NegotiableSelection.bool_item_negotiable_ = false;
        getUserDetailsPro(getActivity());
        try {
            progress_bar = (ProgressBar) view.findViewById(R.id.progress_bar);
            name = (EditText) view.findViewById(R.id.pd_name);
            description = (EditText) view.findViewById(R.id.pd_description);
            saleprice = (EditText) view.findViewById(R.id.pd_saleprice);
            saleprice.addTextChangedListener(new TextWatcher() {
                public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                    String text = arg0.toString();


                    if (text.contains(".") && text.substring(text.indexOf(".") + 1).length() > 2) {
                        saleprice.setText(text.substring(0, text.length() - 1));
                        saleprice.setSelection(saleprice.getText().length());
                    }
                }

                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

                }

                public void afterTextChanged(Editable arg0) {
                }
            });
            category = (TextView) view.findViewById(R.id.pd_category);
            pd_salepricetype = (TextView) view.findViewById(R.id.pd_salepricetype);
            currency = GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_CURRENCY);

            //Log.d("STD CODEDC","STDCODE"+currency);


            if (currency != null)
                saleprice.setHint("Price in " + currency);
            pd_salepricetype.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = CurrencyActivity.newInstance(context, pd_salepricetype.getText().toString());
                    startActivityForResult(intent, globalVariables.REQUEST_CODE_SELECT_CURRENCY);

                }
            });


            mAutocompleteTextView = (TextView) view.findViewById(R.id
                    .pick_location);
            mAutocompleteTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = PickLocationFromMap.newInstance(context, GlobalVariables.TYPE_ADD_TEMS, false, locationModel);
                    startActivityForResult(intent, globalVariables.REQUEST_CODE_FILTER_PICK_LOCATION);
                }
            });
            category_content = (LinearLayout) view.findViewById(R.id.category_content_option);
            pd_nagotiable_l = (LinearLayout) view.findViewById(R.id.pd_nagotiable_l);
            pd_nagotiable_l.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent mIntent = new Intent(context, NegotiableSelection.class);
                    startActivityForResult(mIntent, globalVariables.REQUEST_CODE_NEGOTIABLE);
                }
            });

            shipping_l = (LinearLayout) view.findViewById(R.id.shipping_l);
            shipping_l.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mIntent = new Intent(context, ShippingSelection.class);
                    startActivityForResult(mIntent, globalVariables.REQUEST_CODE_SHIPPING);
                }
            });
            category_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent newItemOption = new Intent(context,NewItemOption.class);
                    startActivityForResult(newItemOption, globalVariables.REQUEST_CODE_NEWITEM);
//                    Intent intent = CategoryActivity.newInstance(context, false, category.getText().toString());
//                    startActivityForResult(intent, globalVariables.REQUEST_CODE_SELECT_CATEGORY);
                }
            });
            add_to_garage_sale_content = (LinearLayout) view.findViewById(R.id.add_to_garage_sale_content);
            add_to_garage_sale_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = GarageSalesList.newInstance(context, "");
                    startActivityForResult(intent, globalVariables.REQUEST_CODE_SELECT_SALE);
                }
            });
            save = (Button) view.findViewById(R.id.pd_publish);
            select_sale = (TextView) view.findViewById(R.id.select_sale);
            mAutocompleteTextView.setOnEditorActionListener(new DoneOnEditorActionListener());
            shippable = (Switch) view.findViewById(R.id.pd_shippable);
            is_product_new = (Switch) view.findViewById(R.id.is_product_new);
            nagotiable = (Switch) view.findViewById(R.id.pd_nagotiable);
            is_new_old = (Switch) view.findViewById(R.id.is_new_old);

            // Log.d("VALUESSS","SHIPPINGCOST"+(CreateProductModel)getArguments().getSerializable(CREATE_ITEM_BUNDLE_KEY));
            productModel = (CreateProductModel) getArguments().getSerializable(CREATE_ITEM_BUNDLE_KEY);
            NewItemOption.productModel=productModel;
            locationModel = (LocationModel) getArguments().getSerializable(CREATE_ITEM_LOCATION_BUNDLE_KEY);
            if (productModel != null) {
                Log.d("VALUESSS", "SHIPPINGCOST NT NULLl" + productModel);
                setValues();
            } else {
                ShippingSelection.isShippable = false;
                NegotiableSelection.comeFromScreen = false;
                productModel = new CreateProductModel();
            }
            from_key = getArguments().getInt(FROM_KEY);

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
        generateCurre();
        return view;
    }

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

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public void setLocation(LocationModel locationModel) {
        this.locationModel = locationModel;
        Toast.makeText(context, "setLocation", Toast.LENGTH_LONG).show();
        if (mAutocompleteTextView != null)
            mAutocompleteTextView.setText(locationModel.getFormatted_address());
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    //Changes 6/25/2017
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
//        str_desc= emojiParser.emojiText(description.getText().toString());
//        str_desc = description.getText().toString();
        // description.getText().toString();
        Log.d("STR DESC0", "DESCCCCC" + productModel);
//        Log.d("STR DESC0","DESCCCCC"+str_desc);

        selectedCategoryIDs= NewItemOption.selectedCategoryIDs;


        if (item_negotiable)
            productModel.setNegotiable(true);

//        if (is_new_old.isChecked())
            productModel.setIs_pickup(NewItemOption.is_new_old_);

        if (hide_item)
            productModel.setHide_item_price(true);

        if (shipping_foc)
            productModel.setShipping_foc(true);


        Log.d(TAG, "SHIPPPABLE" + shippable.isChecked() + "--" + is_new_old.isChecked());
        postalCode = null;
        for (int i = 0; i < selectedAutoSuggesstionsList.size(); i++)
        {
            postalCode = postalCode == null ? selectedAutoSuggesstionsList.get(i).getId() : postalCode + "," + selectedAutoSuggesstionsList.get(i).getId();
        }
        try {
            str_name = Constants.parseTo(name.getText().toString());
        } catch (Exception e) {
            str_name = name.getText().toString();
        }
        try {
            str_desc = Constants.parseTo(description.getText().toString());
        } catch (Exception e) {
            str_desc = description.getText().toString();
        }

        str_sp = saleprice.getText().toString();
        String currency = pd_salepricetype.getText().toString();
//        if (clm != null)
//            selectedCategoryIDs = clm.getIdsforNames(selectedCategoryString);
        if (productModel.getImages().size() == 0) {
            Toast.makeText(activity, "Please select a photo", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(str_name)) {
            Toast.makeText(activity, "Please enter the product name", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(currency)) {
            Toast.makeText(activity, "Please select the currency", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(str_sp)) {
            Toast.makeText(activity, "Please enter the product price", Toast.LENGTH_LONG).show();
        } else if (str_sp.length() > 9) {
            Toast.makeText(activity, "Please enter the valid product price", Toast.LENGTH_LONG).show();
        } else if (selectedCategoryIDs.isEmpty()) {
            Toast.makeText(activity, "Please fill the product Category", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(str_desc)) {
            Toast.makeText(activity, "Please enter the product description", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(mAutocompleteTextView.getText().toString())) {
            Toast.makeText(activity, "Please fill the product location", Toast.LENGTH_LONG).show();
        }/* else if (!isShippable && !is_new_old.isChecked()) {
            Toast.makeText(activity, "Item should be either shippable or pick up", Toast.LENGTH_LONG).show();
        } */ else {
            /*String country=GlobalFunctions.getSharedPreferenceString(context,GlobalVariables.SHARED_PREFERENCE_COUNTRY);
            productModel.setCountry_short(country);
          */
            productModel.setAddress(mAutocompleteTextView.getText().toString());
            productModel.setName(str_name);
            productModel.setDescription(str_desc);
            productModel.setSalePrice(str_sp);
            productModel.setCurrency(currency);
            productModel.setCategories(selectedCategoryIDs);
            productModel.setPostcodes(postalCode);
            if (type_garage)
                productModel.setSaleType(GlobalVariables.TYPE_GARAGE);
            else
                productModel.setSaleType(GlobalVariables.TYPE_ITEM);

            if (type_edit_item)
                productModel.setRequest_type(GlobalVariables.TYPE_UPDATE_REQUEST);
            else
                productModel.setRequest_type(GlobalVariables.TYPE_CREATE_REQUEST);

            if (locationModel != null) {
                if (TextUtils.isEmpty(productModel.getLatitude())) {
                    productModel.setLatitude(locationModel.getLatitude() + "");
                    productModel.setLongitude(locationModel.getLongitude() + "");
                }
            }
            if (item_negotiable)
                productModel.setNegotiable(true);
            else
                productModel.setNegotiable(false);


            if (isShippable)
                productModel.setShippable(true);
            else
                productModel.setShippable(false);

            if (hide_item)
                productModel.setHide_item_price(true);
            else
                productModel.setHide_item_price(false);

            if (shipping_foc)
                productModel.setShipping_foc(true);
            else
                productModel.setShipping_foc(false);

            if (is_intl_shipping)
                productModel.setIs_intl_shipping(true);
            else
                productModel.setIs_intl_shipping(false);


            //if (is_new_old.isChecked())
                productModel.setIs_pickup(NewItemOption.is_new_old_);
          /*  else
                productModel.setIs_pickup(false);*/


            //if (is_product_new.isChecked())
                productModel.setIs_product_new(NewItemOption.is_product_new_);
//            else
//                productModel.setIs_product_new(false);


            productModel.setIntl_shipping_cost(intl_shipping_cost);
            productModel.setTime_to_deliver(time_to_deliver);
            productModel.setLocal_shipping_cost(local_shipping_cost);

            String token = GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_TOKEN);
            if (token != null) {
                showProgressBar();

                createSaleItem(context, productModel);
            } else {
                showSettingsAlert();
            }

        }
    }

    public static void setImages() {
        if (objectUploadPhoto != null && productModel != null) {
            productModel.setImages(objectUploadPhoto.getAvailablePhotos());
            productModel.setRemove_images(objectUploadPhoto.getRemoved_photos());
        }

    }

    private void createSaleItem(final Context context, final CreateProductModel createProductModel) {
        final String email_verification_error = context.getResources().getString(R.string.ErrorEmailVerification);
       /* showProgressBar();*/
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.createSaleItem(context, createProductModel, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                hideProgressBar();
                if (arg0 instanceof IDModel) {
                    IDModel idModel = (IDModel) arg0;
                    createProductModel.setProduct_id(idModel.getId());
                    Toast.makeText(context, "Item has been listed Successfully", Toast.LENGTH_LONG).show();
                    closeThisActivity();
                } else if (arg0 instanceof ErrorModel) {
                    ErrorModel errorModel = (ErrorModel) arg0;
                    String msg = errorModel.getError() != null ? errorModel.getError() : errorModel.getMessage();
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                } else if (arg0 instanceof StatusModel) {
                    hideProgressBar();
                    StatusModel statusModel = (StatusModel) arg0;
                    if (statusModel.isStatus()) {
                        Toast.makeText(context, "Item has been updated Successfully.", Toast.LENGTH_LONG).show();
                        ProductDetail.closeThisActivity();
                        ProductModel productModel = new ProductModel();
                        productModel.setId(createProductModel.getProduct_id());
                        closeThisActivity();
                        Intent intent = ProductDetail.newInstance(context, productModel, null, GlobalVariables.TYPE_MY_SALE);
                        context.startActivity(intent);
                    }
                }
            }

            @Override
            public void OnFailureFromServer(String msg) {
                hideProgressBar();
                if (msg != null) {
                    if (msg.equalsIgnoreCase(email_verification_error))
                        GlobalFunctions.showEmailVerificatiomAlert(context);
                    else
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void OnError(String msg) {
                hideProgressBar();
                if (msg != null) {
                    if (msg.equalsIgnoreCase(email_verification_error))
                        GlobalFunctions.showEmailVerificatiomAlert(context);
                    else
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                }
            }
        }, "Create Sale");

    }

    @Override
    public void onResume() {
        super.onResume();


        setValuesA();
        //   Log.d("IMAGES","GETPHOTOTS"+objectUploadPhoto.getAvailablePhotos().size());
        try {
            if (objectUploadPhoto.getAvailablePhotos().size() > 0) {
                addIcon_one.setVisibility(View.GONE);
                addIcon.setVisibility(View.GONE);
            }
        } catch (Exception e) {

        }

        uploadImageFragment();
        ((CreateSaleActivity) getActivity()).setActionBarTitle("New Item");

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickProcessing();
            }
        });

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    if (productModel != null) {
                        if (productModel.getImages().size() != 0 ||
                                !TextUtils.isEmpty(name.getText().toString()) ||
                                !TextUtils.isEmpty(description.getText().toString()) ||
                                !TextUtils.isEmpty(saleprice.getText().toString()) ||
                                !selectedCategoryIDs.isEmpty() ||
                                !TextUtils.isEmpty(mAutocompleteTextView.getText().toString())
                                ) {
                            showExitAlert();
                        } else
                            activity.finish();
                    } else
                        activity.finish();


                    return true;

                }

                return false;
            }
        });

    }

    private void uploadImageFragment()

    {
        objectUploadPhoto.setAvailablePhotos(productModel.getImages());
        scrollImageForTheItems();

    }


    private void setValues() {
        Log.d("VALUESSS", "SHIPPINGCOST VALUES INSIDE");
        if (productModel != null) {
            loading_label = "Updating Item...";
            save.setText("Update");
            type_edit_item = true;
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

            try {
                name.setText(Constants.getConv(productModel.getName()));
                description.setText(Constants.getConv(productModel.getDescription()));
            } catch (Exception ee) {
                name.setText(productModel.getName());
                description.setText(productModel.getDescription());
            }


            Log.d("VALUESSS", "SHIPPINGCOST" + productModel.getLocal_shipping_cost() + "--" + productModel.getDescription());
            saleprice.setText(productModel.getSalePrice());
            pd_salepricetype.setText(productModel.getCurrency());
            shippable.setChecked(productModel.isShippable() ? true : false);
           /* is_new_old.setChecked(productModel.is_product_new()?true:false);*/
            is_new_old.setChecked(productModel.is_pickup() ? true : false);
            nagotiable.setChecked(productModel.isNegotiable() ? true : false);


            if (productModel.getAddress() != null && !TextUtils.isEmpty(productModel.getAddress()))
                mAutocompleteTextView.setText(productModel.getAddress());
            else
                getAddressFromLatLng(Double.parseDouble(productModel.getLatitude()), Double.parseDouble(productModel.getLongitude()));
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

    @Override
    public void onTokenAdded(Object obj) {
        PostcodeModel model = (PostcodeModel) obj;
        selectedAutoSuggesstionsList.add(model);
    }

    @Override
    public void onTokenRemoved(Object obj) {

        PostcodeModel model = (PostcodeModel) obj;
        for (int i = 0; i < selectedAutoSuggesstionsList.size(); i++) {
            if (selectedAutoSuggesstionsList.get(i).getId().equalsIgnoreCase(model.getId())) {
                selectedAutoSuggesstionsList.remove(i);
                break;
            }
        }
    }


    public void showExitAlert()
    {
        final com.blueshak.app.blueshak.view.AlertDialog alertDialog = new com.blueshak.app.blueshak.view.AlertDialog(activity);
        alertDialog.setTitle("Are you sure?");
        alertDialog.setMessage("Are you sure to stop listing this item? The data you've created will be lost.");
        alertDialog.setPositiveButton("Yes", new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {


                if (from_key == GlobalVariables.TYPE_AC_SIGN_UP)
                {
                    startActivity(new Intent(getActivity(), MainActivity.class));
                } else {
                    getActivity().finish();
                }


            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("No", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public void showSuccesAlert(String title, String message) {
        final com.blueshak.app.blueshak.view.AlertDialog alertDialog = new com.blueshak.app.blueshak.view.AlertDialog(activity);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("Add more items", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent intent=new Intent(activity,CreateSaleActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*/
                Intent i = CreateSaleActivity.newInstance(activity, null, null, null, GlobalVariables.TYPE_HOME, GlobalVariables.TYPE_ITEM);
                startActivity(i);


                alertDialog.dismiss();
                closeThisActivity();

            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Continue shopping", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent intent=new Intent(activity,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*/
                alertDialog.dismiss();
                closeThisActivity();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public void showSettingsAlert() {
        final com.blueshak.app.blueshak.view.AlertDialog alertDialog = new com.blueshak.app.blueshak.view.AlertDialog(context);
        alertDialog.setMessage("Please Login/Register to continue");
        alertDialog.setPositiveButton("Continue", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent creategarrage = new Intent(activity, LoginActivity.class);
                startActivity(creategarrage);
                closeThisActivity();
            }
        });
        alertDialog.show();
    }

    public static void closeThisActivity() {
        if (activity != null) {
            activity.finish();
        }
        objectUploadPhoto = new Object_PhotosAdd();
    }


    @Override
    public void onStop() {
        //
        super.onStop();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            Log.i(TAG, "on activity result");
            if (resultCode == activity.RESULT_OK)
            {
                /*try {
                    String imagePatha = System.currentTimeMillis() + "11";
                    Bitmap bit_ = ImagePicker.getImageFromResult(getActivity(), requestCode, resultCode, data);
                    String bitmap = ImageUtils.savePicture(getActivity(), bit_, imagePatha);
                    // String bitmap = ImagePicker.getImagePathFromResult(getActivity(),requestCode,resultCode,data);
                    Log.d("IMAGE PATH", "IMAGE PATH" + bitmap);

                    CreateImageModel modela = new CreateImageModel();
                    modela.setImage(bitmap);
                    modela.setDisplay(false);
                    objectUploadPhoto.getAvailablePhotos().add(modela);
                    refreshCameraImageList();


                } catch (Exception e) {
                    BlueShakLog.logDebug(TAG,"onActivityResult Exception e -> "+e.getLocalizedMessage());
                }*/
                Log.i(TAG, "result ok ");
                Log.i(TAG, "request code " + requestCode);
                if (requestCode == globalVariables.REQUEST_CODE_FILTER_PICK_LOCATION) {
                    Log.i(TAG, "REQUEST_CODE_FILTER_PICK_LOCATION " + requestCode);
                    LocationModel location_model = (LocationModel) data.getExtras().getSerializable(CREATE_ITEM_LOCATION_BUNDLE_KEY);
                    Log.i(TAG, "name pm" + location_model.getFormatted_address());
                    locationModel = location_model;
                    productModel.setLatitude(location_model.getLatitude());
                    productModel.setLongitude(location_model.getLongitude());
                    productModel.setAddress(location_model.getFormatted_address_for_map());
                    productModel.setSuburb(location_model.getSubhurb());
                    productModel.setCity(location_model.getCity());
                    mAutocompleteTextView.setText(location_model.getFormatted_address());
                } else if (requestCode == globalVariables.REQUEST_CODE_SELECT_CATEGORY) {
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
                } else if (requestCode == globalVariables.REQUEST_CODE_SELECT_CURRENCY) {
                    Log.i(TAG, "REQUEST_CODE_SELECT_CATEGORY " + requestCode);
                    CurrencyModel currencyModel = (CurrencyModel) data.getExtras().getSerializable(CREATE_ITEM_CURRENCY_BUNDLE_KEY);
                    pd_salepricetype.setText(currencyModel.getCurrency());
                    productModel.setCurrency(currencyModel.getCurrency());
                    saleprice.setHint("Price in " + currencyModel.getCurrency());
                    GlobalFunctions.setSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_CURRENCY, currencyModel.getCurrency());
                 /*   select_sale.setText(salesModel.getName());
                    productModel.setSale_id(salesModel.getId());*/
                } else if (requestCode == globalVariables.REQUEST_CODE_NEGOTIABLE) {
                    Log.i(TAG, "REQUEST_CODE_SELECT_CATEGORY " + requestCode);

                    item_negotiable = data.getExtras().getBoolean("item_negotiable");
                    hide_item = data.getExtras().getBoolean("hide_item");
                    Log.d("AAAA", "&&&&&&&&&&" + item_negotiable + "--" + hide_item);

                } else if (requestCode == globalVariables.REQUEST_CODE_SHIPPING) {

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
                else if(requestCode == globalVariables.REQUEST_CODE_NEWITEM)
                {


                    try
                    {
                        item_negotiable = data.getExtras().getBoolean("item_negotiable");
                        hide_item = data.getExtras().getBoolean("hide_item");
                    }
                    catch (Exception e)
                    {
                        item_negotiable=false;
                        hide_item=false;
                    }
                    isShippable = ShippingSelection.isShippable;
                    shipping_foc = ShippingSelection.shipping_foc;
                    is_intl_shipping = ShippingSelection.is_intl_shipping;

                    intl_shipping_cost = ShippingSelection.intl_shipping_cost;
                    time_to_deliver = ShippingSelection.time_to_deliver;
                    local_shipping_cost = ShippingSelection.local_shipping_cost_;



                }
                else if(requestCode == REQUEST_CODE_CHOOSE)
                {
                    BlueShakLog.logDebug(TAG, "onActivityResult REQUEST_CODE_CHOOSE " );
                    for(int i=0;i<Matisse.obtainPathResult(data).size();i++)
                    { String imagePatha = System.currentTimeMillis() + "11";

                        Uri uriImage= Matisse.obtainResult(data).get(i);
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),uriImage);
                            String bitmapa = ImageUtils.savePicture(getActivity(), bitmap, ""+imagePatha);
                            CreateImageModel modela = new CreateImageModel();

                            modela.setImage(bitmapa);
                            modela.setDisplay(false);
                            objectUploadPhoto.getAvailablePhotos().add(modela);


                        }
                        catch (IOException e)
                        {

                        }
                        refreshCameraImageList();
                    }
                    Toast.makeText(getActivity(),""+Matisse.obtainResult(data).toString()+"--"+Matisse.obtainPathResult(data),Toast.LENGTH_LONG).show();
                }else if(requestCode == REQUEST_ADD_IMAGES){
                    BlueShakLog.logDebug(TAG, "onActivityResult REQUEST_ADD_IMAGES " );
                    setImagesAdapter();
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

    class DoneOnEditorActionListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
        {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onClickProcessing();
                return true;
            }
            return false;
        }
    }

    private void getAddressFromLatLng(Double lat, Double lng) {
        showProgressBar();
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getAddress(activity, lat, lng, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                hideProgressBar();
                GlobalFunctions.closeKeyboard(activity);
                locationModel = (LocationModel) arg0;
                if (locationModel != null) {
                    mAutocompleteTextView.setText(locationModel.getFormatted_address());
                }
            }

            @Override
            public void OnFailureFromServer(String msg) {
                hideProgressBar();
            }

            @Override
            public void OnError(String msg) {
                hideProgressBar();
            }
        }, "Fetching Current Location");


    }

    public void showProgressBar() {
        if (progress_bar != null)
            progress_bar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        if (progress_bar != null)
            progress_bar.setVisibility(View.GONE);
    }

    private ProfileDetailsModel profileDetailsModelp = new ProfileDetailsModel();

    private void getUserDetailsPro(Context context) {
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getUserDetails(context, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                Log.d(TAG, "onSuccess Response" + arg0.toString());
                profileDetailsModelp = (ProfileDetailsModel) arg0;
                setValuesPRO(profileDetailsModelp);
            }

            @Override
            public void OnFailureFromServer(String msg) {

                Log.d(TAG, msg);
            }

            @Override
            public void OnError(String msg) {
                Log.d(TAG, msg);
            }
        }, "Profile");

    }

    String stdCode = "";

    public void setValuesPRO(ProfileDetailsModel model) {


        if (model != null) {
            if (model.getPhone() != null && !TextUtils.isEmpty(model.getPhone())) {

//                stdCode=""+ model.getIsd();
//                Log.d("STD CODEDC","STDCODE"+stdCode);
                if (currency != null) {
                    pd_salepricetype.setText(currency);
                    if (currency != null)
                        saleprice.setHint("Price in " + currency);
                    pd_salepricetype.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = CurrencyActivity.newInstance(context, pd_salepricetype.getText().toString());
                            startActivityForResult(intent, globalVariables.REQUEST_CODE_SELECT_CURRENCY);

                        }
                    });
                } else {


                    if (currency != null)
                        saleprice.setHint("Price in " + currency);
                    pd_salepricetype.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = CurrencyActivity.newInstance(context, pd_salepricetype.getText().toString());
                            startActivityForResult(intent, globalVariables.REQUEST_CODE_SELECT_CURRENCY);

                        }
                    });
                    //pd_salepricetype.setText(stdCode);
                }
            }

        }
    }

    private CurrencyListModel clma;
    private List<CurrencyModel> product_list = new ArrayList<CurrencyModel>();

    private void generateCurre() {
        if (clma == null) {
            clma = GlobalFunctions.getCurrencies(activity);
            if (clma != null) {
                product_list = clma.getCurrency_list();
                for (int i = 0; i < product_list.size(); i++) {
                    Log.d("stdCode", "stdCode1122" + stdCode + "---" + product_list.get(i).getCountry_code());
                  try {
                      if (stdCode.equalsIgnoreCase(product_list.get(i).getCountry_code())) {
                          stdCode = product_list.get(i).getCurrency();
                          Log.d("stdCode", "stdCode11" + stdCode);
                          GlobalFunctions.setSharedPreferenceString(getActivity(), GlobalVariables.SHARED_PREFERENCE_USER_CURRENCY, stdCode);
                          ShippingSelection.price_default = stdCode;
                          pd_salepricetype.setText(stdCode);
                          return;
                      }

                  }
                  catch (Exception e)
                  {

                  }
                }

            }
        }

    }


    ArrayList<CreateImageModel> removed_photos = new ArrayList<CreateImageModel>();
    TextView title_tv;
    ImageView addIcon;
    ImageView addIcon_one;
    TwoWayView listView;
    PhotosAddListAdapter adapter;
    protected static final int REQUEST_CHECK_CAMERA = 115;
    protected static final int REQUEST_CHECK_GALLARY = 118;

    void scrollImageForTheItems() {
        ImagePicker.setMinQuality(600, 600);
//        imageutill= new Imageutils(getActivity(),this,true);
        title_tv = (TextView) view.findViewById(R.id.photos_add_main_fragment_title_textview);
        addIcon = (ImageView) view.findViewById(R.id.photos_add_main_fragment_add_imageView);
        listView = (TwoWayView) view.findViewById(R.id.photos_add_main_fragment_photos_list);
        addIcon_one = (ImageView) view.findViewById(R.id.photos_add_main_fragment_add_imageView_add);

        String bitmapa = "https://www.blueshak.co.in/search"; //dummy
        int listSize = objectUploadPhoto.getAvailablePhotos().size();
        if( listSize >= 0 && listSize != 5){
            for(int i = 0; i < 5-listSize;i++){
                CreateImageModel modela = new CreateImageModel();
                modela.setImage(bitmapa);
                modela.setDisplay(false);
                modela.setRealImage(false);
                objectUploadPhoto.getAvailablePhotos().add(modela);
                objectUploadPhoto.setAvailablePhotos(objectUploadPhoto.getAvailablePhotos());
            }
        }
        if (type_edit_item) {
            adapter = new PhotosAddListAdapter(getActivity(), objectUploadPhoto.getAvailablePhotos(),
                    true, this, true);
            addIcon_one.setVisibility(View.GONE);
            addIcon.setVisibility(View.GONE);
        }
        setImagesAdapter();

        addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startAddImageActivity();

                /*if (objectUploadPhoto != null) {
                    if (objectUploadPhoto.getAvailablePhotos().size() >= 5) {
                        addIcon_one.setVisibility(View.GONE);
                        Toast.makeText(context, "You can not add more than five images", Toast.LENGTH_SHORT).show();
                    } else
                        selectImage();
                } else
                    selectImage();*/

            }
        });
        addIcon_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startAddImageActivity();

                /*if (objectUploadPhoto != null) {
                    if (objectUploadPhoto.getAvailablePhotos().size() >= 5) {
                        Toast.makeText(context, "You can not add more than five images", Toast.LENGTH_SHORT).show();
                        addIcon_one.setVisibility(View.GONE);
                    } else
                        selectImage();
                } else
                    selectImage();*/

            }
        });

    }

    private static final int REQUEST_CODE_CHOOSE = 23;
    private void selectImage() {

        if (checkIfAlreadyhavePermission())
        {
//            RxPermissions rxPermissions = new RxPermissions(activity);
//            rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
           Matisse.from(this)
                    .choose(MimeType.ofImage(), false)
                    .countable(true)
                    .capture(true)
                    .captureStrategy(
                            new com.zhihu.matisse.internal.entity.CaptureStrategy(true, "com.blueshak.fileprovider"))
                    .maxSelectable(5)
                    .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                    .gridExpectedSize(
                            getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                    .thumbnailScale(0.85f)
                    .imageEngine(new GlideEngine())
                    .forResult(REQUEST_CODE_CHOOSE);
        }
            //ImagePicker.pickImage(CreateItemSaleFragment.this, "Select your image");
        else
            checkCameraPermission();
    }

    public void checkCameraPermission() {
        int permissionCheck_location_coarse = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCheck_write_coarse = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCheck_camera = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        if (permissionCheck_location_coarse != PackageManager.PERMISSION_GRANTED ||
                permissionCheck_write_coarse != PackageManager.PERMISSION_GRANTED
                || permissionCheck_camera != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CHECK_CAMERA);
        }


    }

    private boolean checkIfAlreadyhavePermission() {
        Log.d(TAG, "checkIfReadExternalStorageAlreadyhavePermission######################");
        int coarse_location = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int camera = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        if (coarse_location == PackageManager.PERMISSION_GRANTED && result == PackageManager.PERMISSION_GRANTED
                && camera == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult ##########@@@@@@@@@@@@@@@@@@@@@@@@@");
        switch (requestCode) {
            case REQUEST_CHECK_CAMERA:
                Log.d(TAG, "onRequestPermissionsResult ############REQUEST_CHECK_CAMERA");
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    selectImage();
                } else {
                    checkCameraPermission();
                }
                return;

        }
    }


    private void setValuesA() {
        try {
            if (context != null && objectUploadPhoto != null) {
                title_tv.setText(context.getString(R.string.app_name));//object_photosAdd.getTitle());
                addIcon.setImageResource(objectUploadPhoto.getAddIconResID());
            }
        } catch (Exception e) {

        }
    }

    public void refreshCameraImageList() {

        Log.d("IMAGES", "GETPHOTOTS" + objectUploadPhoto.getAvailablePhotos().size());
        if (listView != null && adapter != null && objectUploadPhoto != null) {
            if (objectUploadPhoto.getAvailablePhotos().size() > 0) {
                listView.setVisibility(View.VISIBLE);
                if (objectUploadPhoto.getAvailablePhotos().size() < 5)
                    if (addIcon_one.getVisibility() == View.GONE)
                        addIcon_one.setVisibility(View.GONE);
            } else {
                listView.setVisibility(View.GONE);
            }
            synchronized (adapter) {
                adapter.notifyDataSetChanged();
            }
            //listener.onRecreate(object_photosAdd);
            setRefreshData();
        }
    }

    public void deleteCameraViewfromFile(Context context, int position) {
        try {
            String fileString = objectUploadPhoto.getAvailablePhotos().get(position).getImage();
            removed_photos.add(objectUploadPhoto.getAvailablePhotos().get(position));
            File file = new File(fileString);
            if (file.exists()) {
                file.delete();
                file.getAbsoluteFile().delete();
                if (file.exists()) {
                    context.getApplicationContext().deleteFile(file.getPath());
                }
                if (!file.exists()) {
                    Toast.makeText(context, "File Deleted Successfully", Toast.LENGTH_SHORT).show();
                }
            }
            objectUploadPhoto.getAvailablePhotos().remove(position);
            objectUploadPhoto.setRemoved_photos(removed_photos);
            refreshCameraImageList();
        } catch (Exception e) {
            Log.d(TAG, "Exception on Deleting Image : " + e);

        }

    }

    @Override
    public void ondeleting(int position) {
        deleteCameraViewfromFile(context, position);
    }

    @Override
    public void onImageClick(int position, File file, String name, Boolean isDefault) {
        startAddImageActivity();
    }

    private void setRefreshData() {
       /*call the outer function to refresh list*/
        setImages();

    }

    private void startAddImageActivity(){
        //objectUploadPhoto.getAvailablePhotos().clear();
        Intent intent = new Intent(getActivity(),AddSalesImagesActivity.class);
        startActivityForResult(intent,REQUEST_ADD_IMAGES);
    }

    private void setImagesAdapter(){
        adapter = new PhotosAddListAdapter(getActivity(), objectUploadPhoto.getAvailablePhotos(), true, this, false);
        listView.setAdapter(adapter);
    }

}

