package com.blueshak.app.blueshak.filter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.blueshak.app.blueshak.services.ServerResponseInterface;
import com.blueshak.app.blueshak.services.ServicesMethodsManager;
import com.blueshak.blueshak.R;
import com.blueshak.app.blueshak.MainActivity;
import com.blueshak.app.blueshak.PickLocation;
import com.blueshak.app.blueshak.category.CategoryActivity;
import com.blueshak.app.blueshak.category.CategoryListAdapter;
import com.blueshak.app.blueshak.category.OnSelected;
import com.blueshak.app.blueshak.global.GlobalFunctions;
import com.blueshak.app.blueshak.global.GlobalVariables;
import com.blueshak.app.blueshak.root.RootActivity;
import com.blueshak.app.blueshak.search.SearchActivity;
import com.blueshak.app.blueshak.services.model.CategoryListModel;
import com.blueshak.app.blueshak.services.model.CategoryModel;
import com.blueshak.app.blueshak.services.model.FilterModel;
import com.blueshak.app.blueshak.services.model.LocationModel;
import com.blueshak.app.blueshak.util.LocationListener;
import com.blueshak.app.blueshak.util.LocationService;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class FilterActivity extends RootActivity implements OnSelected, LocationListener {
    private static final String TAG = "FilterActivity";
    private static Context context;
    private static Activity activity;
    public static final String BUNDLE_KEY_FILTER_MODEL_SERIALIZABLE = "FilterKeyFilterModelSerializeable";
    public static final String BUNDLE_KEY_LOCATION_MODEL_SERIALIZABLE = "FilterKeyLocationModelSerializeable";
    public static final String CREATE_ITEM_CATEGORY_BUNDLE_KEY = "CreateItemActivityCategoryBundleKey";
    /*public RangeBar priceRangeBar;*/
    private SeekBar distanceRangeBar;
    private AutoCompleteTextView places_actv;
    public ImageView go_back, ic_check;
    private RelativeLayout all_categories_content;
    private TextView all_categories, close_button/*,priceMinRange_tv, priceMaxRange_tv*/, distanceMinRange_tv, distanceMaxRaange_tv, location;
    /*RadioButton shippable_rb, pickupable_rb, available_rb, sold_rb;*/
    int minPriceValue = 0, maxPriceValue = 0, minDistanceValue =3, maxDistanceValue = 0;
    private LocationModel locationModel = null;
    private int from = 0;
    private Button applyButton;
    private static GlobalVariables globalVariables = new GlobalVariables();
    public static final String FROM = "from";
    boolean[] is_checked;
    private Switch search_radius, nearest_first, sort_by_recent, ending_soon, only_garage_items,/*sorting*/
            current_country, h_to_l, l_to_h,nly_new_items_s,nly_negotiable_item_s;
    private FilterModel detail = new FilterModel();
    private CategoryListModel categoryListModel = null;
    private ArrayList<String> selectedCategoryString = new ArrayList<String>();
    private LinearLayout location_content, nearest_content, distance_content, sorting_main_content,
            sorting_content, only_garage_item_content, ending_soon_content,
            h_to_l_content, l_to_h_content,negotial_l,only_new_item_l;
    private List<CategoryModel> category_list = new ArrayList<CategoryModel>();
    private String type = GlobalVariables.TYPE_SHOP;
    private CategoryListModel clm;
    private RecyclerView recyclerView;
    private CategoryListAdapter adapter;
    boolean is_all = false;
    private TextView title_tv;
    private LocationService locServices;
    TextView category;
    public static String categoryText = "Category - All";
    RelativeLayout category_layout;
    private ArrayList<String> stringArrayList = new ArrayList<String>();
    private HashSet<String> hashSet = new HashSet<String>();
    private LinearLayout layoutFreeItems;
    private Switch switchOnlyFreeItem;
    private TextView mTvNearestFirst;
    private TextView mTvNewlyListed;


    public static Intent newInstance(Context context, LocationModel locationModel, int from) {
        System.out.println("########newInstance#######" + Integer.toString(from));
        Intent mIntent = new Intent(context, FilterActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_KEY_LOCATION_MODEL_SERIALIZABLE, locationModel);
        bundle.putInt(FROM, from);
        mIntent.putExtras(bundle);
        return mIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        context = this;
        activity = this;
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            LayoutInflater inflator = LayoutInflater.from(this);
            View v = inflator.inflate(R.layout.action_bar_titlel, null);
            title_tv = ((TextView) v.findViewById(R.id.title));
            toolbar.addView(v);
            close_button = (TextView) v.findViewById(R.id.cancel);

            close_button.setText("Reset");
            close_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetValues();
                }
            });
            go_back = (ImageView) findViewById(R.id.go_back);
            go_back.setVisibility(View.VISIBLE);
            go_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            locServices = new LocationService(activity);
            locServices.setListener(this);
            /*priceRangeBar = (RangeBar) findViewById(R.id.price_range_bar);*/

            nearest_content = (LinearLayout) findViewById(R.id.nearest_content);

            ending_soon_content = (LinearLayout) findViewById(R.id.ending_soon_content);
            h_to_l_content = (LinearLayout) findViewById(R.id.h_to_l_content);
            l_to_h_content = (LinearLayout) findViewById(R.id.l_to_h_content);
            sorting_main_content = (LinearLayout) findViewById(R.id.sorting_main_content);
            only_garage_item_content = (LinearLayout) findViewById(R.id.only_garage_item_content);
            negotial_l = (LinearLayout) findViewById(R.id.negotial_l);
            only_new_item_l = (LinearLayout) findViewById(R.id.only_new_item_l);
            layoutFreeItems = (LinearLayout)findViewById(R.id.layout_only_free_items);

            mTvNearestFirst = (TextView)findViewById(R.id.tv_nearest_first);
            mTvNewlyListed = (TextView)findViewById(R.id.tv_newly_listed);


            sorting_content = (LinearLayout) findViewById(R.id.sorting_content);
            all_categories_content = (RelativeLayout) findViewById(R.id.all_categories_content);
            only_garage_items = (Switch) findViewById(R.id.nly_garage_sale_items);
            nearest_first = (Switch) findViewById(R.id.nearest_first);


            nly_new_items_s = (Switch) findViewById(R.id.nly_new_items_s);
            switchOnlyFreeItem = (Switch) findViewById(R.id.switch_free_item);

            nly_negotiable_item_s = (Switch) findViewById(R.id.nly_negotiable_item_s);
  /*  sorting=(Switch)findViewById(R.id.sorting);*/
            current_country = (Switch) findViewById(R.id.current_country);
            h_to_l = (Switch) findViewById(R.id.h_to_l);
            l_to_h = (Switch) findViewById(R.id.l_to_h);
            ending_soon = (Switch) findViewById(R.id.ending_soon);
            ending_soon.setVisibility(View.GONE);
            sort_by_recent = (Switch) findViewById(R.id.sort_by_recent);
            category_layout = (RelativeLayout) findViewById(R.id.category_layout);
            category_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = CategoryActivity.newInstance(context, false, category.getText().toString());
                    startActivityForResult(intent, globalVariables.REQUEST_CODE_SELECT_CATEGORY);
                }
            });
            category = (TextView) findViewById(R.id.pd_category);
            category.setText(categoryText);
            category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = CategoryActivity.newInstance(context, false, category.getText().toString());
                    startActivityForResult(intent, globalVariables.REQUEST_CODE_SELECT_CATEGORY);
                }
            });
            all_categories = (TextView) findViewById(R.id.all_categories);
            ic_check = (ImageView) findViewById(R.id.ic_check);
            all_categories_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!is_all) {
                        is_all = true;
                        all_categories.setTextColor(context.getResources().getColor(R.color.brandColor));
                        ic_check.setVisibility(View.VISIBLE);
                        setAll(is_all);
                    } else {
                        is_all = false;
                        all_categories.setTextColor(context.getResources().getColor(R.color.brand_text_color));
                        ic_check.setVisibility(View.GONE);
                        setAll(is_all);
                    }

                }
            });


            distanceRangeBar = (SeekBar) findViewById(R.id.distance_range_bar);
            distance_content = (LinearLayout) findViewById(R.id.distance_content);
            search_radius = (Switch) findViewById(R.id.search_radius);

          /*  priceMinRange_tv = (TextView) findViewById(R.id.price_start_tv);
            priceMaxRange_tv = (TextView) findViewById(R.id.price_end_tv);*/
            distanceMinRange_tv = (TextView) findViewById(R.id.distance_start_tv);
            distanceMaxRaange_tv = (TextView) findViewById(R.id.distance_end_tv);
            location = (TextView) findViewById(R.id.location);
            applyButton = (Button) findViewById(R.id.applyButton);
            /*##############################*/
            if (clm == null) {
                clm = GlobalFunctions.getCategories(activity);
                if (clm != null) {
                    is_checked = new boolean[clm.getCategoryNames().size()];
                  /*  CategoryModel categoryModel=new CategoryModel();
                    categoryModel.setId("0");
                    categoryModel.setName("All");*/
                    category_list = clm.getCategoryList();

                }
            }
            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            recyclerView.setNestedScrollingEnabled(false);
            adapter = new CategoryListAdapter(context, category_list, true, this);
            LinearLayoutManager linearLayoutManagerVertical =
                    new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManagerVertical);
           /* recyclerView.addItemDecoration(new SpacesItemDecoration(
                    getResources().getDimensionPixelSize(R.dimen.space)));*/
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
            recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));

            location_content = (LinearLayout) findViewById(R.id.location_content);
            location_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    boolean world_wide = true;
                    if (current_country.isChecked())
                        world_wide = false;
                    else
                        world_wide = true;
                    Intent intent = PickLocation.newInstance(context, GlobalVariables.TYPE_FILTER_ACTIVITY, world_wide, null);
                    startActivityForResult(intent, globalVariables.REQUEST_CODE_FILTER_PICK_LOCATION);
                }
            });
            search_radius.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        distance_content.setVisibility(View.VISIBLE);
                    } else {
                        distance_content.setVisibility(View.GONE);
                    }
                }
            });

            ending_soon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                      /*  if (!search_radius.isChecked()) {
                            search_radius.setChecked(true);
                            setDistanceRangeBar(50);
                            distance_content.setVisibility(View.VISIBLE);
                        }*/
                        if (sort_by_recent.isChecked()) {
                            sort_by_recent.setChecked(false);
                        }
                        if (nearest_first.isChecked()) {
                            nearest_first.setChecked(false);
                        }

                    } else {
                        if (!sort_by_recent.isChecked() && !nearest_first.isChecked()) {
                            nearest_first.setChecked(true);
                        }
                    }
                }
            });
            h_to_l.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                       /* if (!search_radius.isChecked()) {
                            search_radius.setChecked(true);
                            setDistanceRangeBar(50);
                            distance_content.setVisibility(View.VISIBLE);
                        }*/
                        if (sort_by_recent.isChecked()) {
                            sort_by_recent.setChecked(false);
                        }
                        if (l_to_h.isChecked()) {
                            l_to_h.setChecked(false);
                        }
                        if (nearest_first.isChecked()) {
                            nearest_first.setChecked(false);
                        }
                    } else {
                        if (!l_to_h.isChecked() && !nearest_first.isChecked() && !sort_by_recent.isChecked()) {
                            nearest_first.setChecked(true);
                        }
                    }


                }
            });
            l_to_h.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        /*if (!search_radius.isChecked()) {
                            search_radius.setChecked(true);
                            setDistanceRangeBar(50);
                            distance_content.setVisibility(View.VISIBLE);
                        }*/
                        if (sort_by_recent.isChecked()) {
                            sort_by_recent.setChecked(false);
                        }
                        if (h_to_l.isChecked()) {
                            h_to_l.setChecked(false);
                        }
                        if (nearest_first.isChecked()) {
                            nearest_first.setChecked(false);
                        }
                    } else {
                        if (!nearest_first.isChecked() && !h_to_l.isChecked() && !sort_by_recent.isChecked()) {
                            nearest_first.setChecked(true);
                        }
                    }
                }
            });

            nearest_first.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (from == GlobalVariables.TYPE_GARAGE_SALE) {
                            if (sort_by_recent.isChecked()) {
                                sort_by_recent.setChecked(false);
                            }
                            if (ending_soon.isChecked()) {
                                ending_soon.setChecked(false);
                            }
                        } else {
                            if (l_to_h.isChecked()) {
                                l_to_h.setChecked(false);
                            }
                            if (h_to_l.isChecked()) {
                                h_to_l.setChecked(false);
                            }
                            if (sort_by_recent.isChecked()) {
                                sort_by_recent.setChecked(false);
                            }

                        }
                    } else {
                        if (from == GlobalVariables.TYPE_GARAGE_SALE) {
                            if (!sort_by_recent.isChecked() && !ending_soon.isChecked()) {
                                nearest_first.setChecked(true);
                            }
                        } else {
                            if (!l_to_h.isChecked() && !h_to_l.isChecked() && !sort_by_recent.isChecked()) {
                                nearest_first.setChecked(true);
                            }
                        }
                    }
                }
            });
            sort_by_recent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        /*if (!search_radius.isChecked()) {
                            search_radius.setChecked(true);
                            setDistanceRangeBar(50);
                            distance_content.setVisibility(View.VISIBLE);
                        }*/
                        if (from == GlobalVariables.TYPE_GARAGE_SALE) {
                            if (ending_soon.isChecked()) {
                                ending_soon.setChecked(false);
                            }
                            if (nearest_first.isChecked()) {
                                nearest_first.setChecked(false);
                            }
                        } else {
                            if (l_to_h.isChecked()) {
                                l_to_h.setChecked(false);
                            }
                            if (h_to_l.isChecked()) {
                                h_to_l.setChecked(false);
                            }
                            if (nearest_first.isChecked()) {
                                nearest_first.setChecked(false);
                            }
                        }


                    } else {
                        if (from == GlobalVariables.TYPE_GARAGE_SALE)
                        {
                            if (!sort_by_recent.isChecked() && !ending_soon.isChecked()) {
                                nearest_first.setChecked(true);
                            }
                        } else {
                            if (!l_to_h.isChecked() && !h_to_l.isChecked() && !nearest_first.isChecked()) {
                                nearest_first.setChecked(true);
                            }
                        }

                    }
                }
            });

            /* sorting.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     //is chkIos checked?
                     if (((Switch) v).isChecked()) {
                         sorting_content.setVisibility(View.VISIBLE);
                     }else{
                         sorting_content.setVisibility(View.GONE);
                     }
                 }
             });*/
            if (getIntent().hasExtra(BUNDLE_KEY_LOCATION_MODEL_SERIALIZABLE)) {
                locationModel = (LocationModel) getIntent().getExtras().getSerializable(BUNDLE_KEY_LOCATION_MODEL_SERIALIZABLE);
            }
            if (getIntent().hasExtra(FROM)) {
                from = getIntent().getExtras().getInt(FROM);
                System.out.println("###FROM#########" + Integer.toString(from));
                if (from == GlobalVariables.TYPE_GARAGE_SALE)
                    setupGarageFilter();
                else if (from == GlobalVariables.TYPE_ITEMS || from == globalVariables.TYPE_SEARCH)
                    setupSearchFilter();

            }
            String filter_string = GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.FILTER_MODEL);
            Log.d(TAG, "######filter_string######" + filter_string);
            if (filter_string != null) {
                Log.d(TAG, "########Filter is NOT null############");
                detail.toObject(filter_string);
                setThisPage();
            } else {
                Log.d(TAG, "########Filter is null,Setting the Location#############");
                resetValues();
                if (locationModel != null) {
                    detail.setLatitude(locationModel.getLatitude());
                    detail.setLongitude(locationModel.getLongitude());
                    detail.setLocation(locationModel.getCity() + " " + locationModel.getState());
                    detail.setFormatted_address(locationModel.getFormatted_address());
                    location.setText(locationModel.getFormatted_address());
                }

            }
            distanceRangeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    minDistanceValue = progress;
                    int MIN = GlobalVariables.DISTANCE_MIN_VALUE;
                    if (progress < MIN) {
                        distanceMaxRaange_tv.setText(minDistanceValue + " Km");
                    } else {
                        minDistanceValue = progress;
                    }
                    if (minDistanceValue <=3) {
                        distanceMaxRaange_tv.setText(3 + " Km");
                    } else {
                        distanceMaxRaange_tv.setText(minDistanceValue + " Kms");
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {


                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }

            });

            applyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickFunction();
                }
            });


        } catch (NullPointerException e) {
            Log.d(TAG, "NullPointerException");
            e.printStackTrace();
            Crashlytics.log(e.getMessage());
        } catch (NumberFormatException e) {
            Log.d(TAG, "NumberFormatException");
            e.printStackTrace();
            Crashlytics.log(e.getMessage());
        } catch (Exception e) {
            Log.d(TAG, "Exception");
            e.printStackTrace();
            Crashlytics.log(e.getMessage());
        }

    }

    public void resetValues() {
        Log.d(TAG, "Resetting the values");
        setDistanceRangeBar(GlobalVariables.DISTANCE_MIN_VALUE);
        GlobalFunctions.removeSharedPreferenceKey(context, GlobalVariables.FILTER_MODEL);
        is_all = true;
        categoryText = "Category - All";
        category.setText(categoryText);
        nly_negotiable_item_s.setChecked(false);
        nly_new_items_s.setChecked(false);
        switchOnlyFreeItem.setChecked(false);

        all_categories.setTextColor(context.getResources().getColor(R.color.brandColor));
        ic_check.setVisibility(View.VISIBLE);
        if (search_radius.isChecked()) {
            distance_content.setVisibility(View.GONE);
            search_radius.setChecked(false);
        }
        if (sort_by_recent.isChecked())
            sort_by_recent.setChecked(false);
        sort_by_recent.setChecked(false);
        ending_soon.setChecked(false);
        if (only_garage_items.isChecked())
            only_garage_items.setChecked(false);
        if (!current_country.isChecked())
            current_country.setChecked(true);
        if (h_to_l.isChecked())
            h_to_l.setChecked(false);
        if (l_to_h.isChecked())
            l_to_h.setChecked(false);
        nearest_first.setChecked(true);
        setAll(is_all);

        if (locServices != null && locServices.canGetLocation()) {
            resetLocation(locServices.getLatitude(), locServices.getLongitude());
        }
    }

    private void setThisPage() {
        if (detail != null) {
            if (detail.isDistance_enabled()) {
                search_radius.setChecked(true);
                distance_content.setVisibility(View.VISIBLE);
            } else {
                distance_content.setVisibility(View.GONE);
                search_radius.setChecked(false);
            }
            if (detail.is_current_country())
                current_country.setChecked(true);
            else
                current_country.setChecked(false);


            if (detail.isNew_items())
                nly_new_items_s.setChecked(true);
            else
                nly_new_items_s.setChecked(false);

            if (detail.isFreeItems()){
                switchOnlyFreeItem.setChecked(true);
            }else{
                switchOnlyFreeItem.setChecked(false);
            }


            if (detail.isNegotiable_items())
                nly_negotiable_item_s.setChecked(true);
            else
                nly_negotiable_item_s.setChecked(false);

          /*  if(detail.isSortByRecent())
                sort_by_recent.setChecked(true);
            else
                sort_by_recent.setChecked(false);*/
            if (from == GlobalVariables.TYPE_GARAGE_SALE) {
               /* if(detail.isSorting_enabled()){
                    sorting.setChecked(true);
                    sorting_content.setVisibility(View.VISIBLE);
                }else
                    sorting.setChecked(false);*/
              /*  if (detail.isEnding_soon())
                    ending_soon.setChecked(true);
                else
                    ending_soon.setChecked(false);
                if (detail.isSortByRecent())
                    sort_by_recent.setChecked(true);
                else
                    sort_by_recent.setChecked(false);*/
                if (detail.isEnding_soon()) {
                    nearest_first.setChecked(false);
                    ending_soon.setChecked(true);

                } else if (detail.isSortByRecent_garage()) {
                    nearest_first.setChecked(false);
                    sort_by_recent.setChecked(true);
                } else {
                    nearest_first.setChecked(true);
                }

            } else if (from == GlobalVariables.TYPE_SEARCH || from == GlobalVariables.TYPE_ITEMS) {
                if (detail.isGarage_items())
                    only_garage_items.setChecked(true);
                else
                    only_garage_items.setChecked(false);

                if (detail.isSortByRecent()) {
                    sort_by_recent.setChecked(true);
                    nearest_first.setChecked(false);
                } else if (detail.isPrice_h_2_l()) {
                    nearest_first.setChecked(false);
                    h_to_l.setChecked(true);
                } else if (detail.isPrice_l_2_h()) {
                    nearest_first.setChecked(false);
                    l_to_h.setChecked(true);
                } else
                    nearest_first.setChecked(true);

            }
            location.setText(detail.getFormatted_address());
            minDistanceValue = detail.getRange();
            setDistanceRangeBar(detail.getRange());
            if (!TextUtils.isEmpty(detail.getCategories()) && detail.getCategories() != null) {
                String[] ids = detail.getCategories().split(",");
                for (CategoryModel categoryModel : category_list) {
                    if (ids != null && !TextUtils.isEmpty(ids.toString())) {
                        for (int i = 0; i < ids.length; i++) {
                            if (ids[i] != null && !TextUtils.isEmpty(ids[i]))
                                if (Integer.parseInt(categoryModel.getId()) == Integer.parseInt(ids[i])) {
                                    categoryModel.setIs_selected(true);
                                }
                        }
                    }
                }
                synchronized (adapter) {
                    adapter.notifyDataSetChanged();
                }
            } else {
                is_all = true;
                all_categories.setTextColor(context.getResources().getColor(R.color.brandColor));
                ic_check.setVisibility(View.VISIBLE);
                setAll(is_all);
            }
          /*  setCategory();*/
        }
    }

    private void setDistanceRangeBar(int val) {
        distanceRangeBar.setProgress(val);
        if (val == 0 || val == 1|| val == 2|| val == 3) {
            distanceMaxRaange_tv.setText(3 + " Km");
        } else {
            distanceMaxRaange_tv.setText(val + " Kms");
        }
    }

    private void setPlacesAutoComplete(String lati, String lngi) {
        if (lati != null && lngi != null) {
            try {
                places_actv.setText(GlobalFunctions.getLocationNames(activity, Double.parseDouble(lati), Double.parseDouble(lngi)));
            } catch (Exception ex) {
                Log.d(TAG, "Double error catch");
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        onBackSaveValues();
    }

    private void onClickFunction() {
        stringArrayList.clear();
        hashSet.clear();
        String country = null;
        if (maxPriceValue == 0)
            maxPriceValue = GlobalVariables.PRICE_MAX_VALUE;
        detail.setRange(minDistanceValue);
        detail.setPriceRange(minPriceValue + "," + maxPriceValue);
        detail.setType(type);
        if (current_country.isChecked()) {
            detail.setIs_current_country(true);
            country = GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_LOCATION_COUNTRY);
            detail.setCurrent_country_code(country);
        } else
            detail.setIs_current_country(false);

        if (search_radius.isChecked())
            detail.setDistance_enabled(true);
        else
            detail.setDistance_enabled(false);

        if (from == GlobalVariables.TYPE_GARAGE_SALE) {

            if(nearest_first.isChecked()){
                if (from == GlobalVariables.TYPE_GARAGE_SALE){
                    if(!noDuplicateEntry(getString(R.string.item_list_fragment_for_list_result_from_nearest_seller))){
                        stringArrayList.add(getString(R.string.item_list_fragment_for_list_result_from_nearest_seller));
                    }
                }else{
                    if(!noDuplicateEntry(getString(R.string.nearest_first))){
                        stringArrayList.add(getString(R.string.nearest_first));
                    }
                }

            }
            if (sort_by_recent.isChecked()) {
                if (from == GlobalVariables.TYPE_GARAGE_SALE){
                    detail.setResults_text(getString(R.string.filter_newest_seller));
                    if(!noDuplicateEntry(getString(R.string.filter_newest_seller))){
                        stringArrayList.add(getString(R.string.filter_newest_seller));
                    }
                }else{
                    detail.setResults_text(getString(R.string.newly_listed));
                    if(!noDuplicateEntry(getString(R.string.newly_listed))){
                        stringArrayList.add(getString(R.string.newly_listed));
                    }
                }

                detail.setSortByRecent_garage(true);
            } else
                detail.setSortByRecent_garage(false);

            if (ending_soon.isChecked()) {
                detail.setResults_text(getString(R.string.ending_first));
                if(!noDuplicateEntry(getString(R.string.ending_first))){
                    stringArrayList.add(getString(R.string.ending_first));
                }
                detail.setEnding_soon(true);
            } else
                detail.setEnding_soon(false);

            if (nly_new_items_s.isChecked()){
                if(!noDuplicateEntry(getString(R.string.only_new_items))){
                    stringArrayList.add(getString(R.string.only_new_items));
                }
                detail.setNew_items(true);
            } else
                detail.setNew_items(false);

            if (switchOnlyFreeItem.isChecked()){
                if(!noDuplicateEntry(getString(R.string.only_free_items))){
                    stringArrayList.add(getString(R.string.only_free_items));
                }
                detail.setFreeItems(true);
            } else{
                detail.setFreeItems(false);
            }

            if (nly_negotiable_item_s.isChecked()){
                if(!noDuplicateEntry(getString(R.string.only_negotiable_items))){
                    stringArrayList.add(getString(R.string.only_negotiable_items));
                }
                detail.setNegotiable_items(true);
            }else
                detail.setNegotiable_items(false);

        } else if (from == GlobalVariables.TYPE_SEARCH || from == GlobalVariables.TYPE_ITEMS) {

            if(nearest_first.isChecked()){
                if (from == GlobalVariables.TYPE_GARAGE_SALE){
                    if(!noDuplicateEntry(getString(R.string.item_list_fragment_for_list_result_from_nearest_seller))){
                        stringArrayList.add(getString(R.string.item_list_fragment_for_list_result_from_nearest_seller));
                    }
                }else{
                    if(!noDuplicateEntry(getString(R.string.nearest_first))){
                        stringArrayList.add(getString(R.string.nearest_first));
                    }
                }
            }
            if (sort_by_recent.isChecked()) {
                if (from == GlobalVariables.TYPE_GARAGE_SALE){
                    detail.setResults_text(getString(R.string.filter_newest_seller));
                    if(!noDuplicateEntry(getString(R.string.filter_newest_seller))){
                        stringArrayList.add(getString(R.string.filter_newest_seller));
                    }
                }else{
                    detail.setResults_text(getString(R.string.newly_listed));
                    if(!noDuplicateEntry(getString(R.string.newly_listed))){
                        stringArrayList.add(getString(R.string.newly_listed));
                    }
                }
                detail.setSortByRecent(true);
            } else
                detail.setSortByRecent(false);

            if (h_to_l.isChecked()) {
                detail.setPrice_h_2_l(true);
                if(!noDuplicateEntry(getString(R.string.highest_to_lowest_price))){
                    stringArrayList.add(getString(R.string.highest_to_lowest_price));
                }
                detail.setResults_text(getString(R.string.highest_to_lowest_price));
            } else
                detail.setPrice_h_2_l(false);
            if (l_to_h.isChecked()) {
                if(!noDuplicateEntry(getString(R.string.lowest_to_highest_price))){
                    stringArrayList.add(getString(R.string.lowest_to_highest_price));
                }
                detail.setResults_text(getString(R.string.lowest_to_highest_price));
                detail.setPrice_l_2_h(true);
            } else
                detail.setPrice_l_2_h(false);

            stringArrayList.add(categoryText);

            if(search_radius.isChecked()){
                stringArrayList.add(distanceMaxRaange_tv.getText().toString());
            }

            if (nly_new_items_s.isChecked()){
                if(!noDuplicateEntry(getString(R.string.only_new_items))){
                    stringArrayList.add(getString(R.string.only_new_items));
                }
                detail.setNew_items(true);
            } else
                detail.setNew_items(false);

            if (switchOnlyFreeItem.isChecked()){
                if(!noDuplicateEntry(getString(R.string.only_free_items))){
                    stringArrayList.add(getString(R.string.only_free_items));
                }
                detail.setFreeItems(true);
            } else{
                detail.setFreeItems(false);
            }

            if (nly_negotiable_item_s.isChecked()){
                if(!noDuplicateEntry(getString(R.string.only_negotiable_items))){
                    stringArrayList.add(getString(R.string.only_negotiable_items));
                }
                detail.setNegotiable_items(true);
            }else
                detail.setNegotiable_items(false);

            if (only_garage_items.isChecked()){
                if(!noDuplicateEntry(getString(R.string.only_sales_items))){
                    stringArrayList.add(getString(R.string.only_sales_items));
                }
                detail.setGarage_items(true);
            }else
                detail.setGarage_items(false);
        }

        detail.setSorting_enabled(true);

       /* hashSet.addAll(stringArrayList);
        stringArrayList.clear();
        stringArrayList.addAll(hashSet);*/
        detail.setArrayListFilterResult(stringArrayList);

        GlobalFunctions.setSharedPreferenceString(context, GlobalVariables.FILTER_MODEL, detail.toString());

        if (detail.getLatitude() == null || detail.getLongitude() == null)
        {
            Toast.makeText(context, "Please select city/suburb", Toast.LENGTH_LONG).show();
        } else {
            setReturnResult(detail);
        }
    }

  void  onBackSaveValues(){
      stringArrayList.clear();
      hashSet.clear();
        String country = null;
        if (maxPriceValue == 0)
            maxPriceValue = GlobalVariables.PRICE_MAX_VALUE;

        detail.setRange(minDistanceValue);
        detail.setPriceRange(minPriceValue + "," + maxPriceValue);
        detail.setType(type);
        if (current_country.isChecked()) {
            detail.setIs_current_country(true);
            country = GlobalFunctions.getSharedPreferenceString(context, GlobalVariables.SHARED_PREFERENCE_LOCATION_COUNTRY);
            detail.setCurrent_country_code(country);
        } else
            detail.setIs_current_country(false);

        if (search_radius.isChecked())
            detail.setDistance_enabled(true);
        else
            detail.setDistance_enabled(false);


        if (from == GlobalVariables.TYPE_GARAGE_SALE) {
            if(nearest_first.isChecked()){

                if (from == GlobalVariables.TYPE_GARAGE_SALE){
                    if(!noDuplicateEntry(getString(R.string.item_list_fragment_for_list_result_from_nearest_seller))){
                        stringArrayList.add(getString(R.string.item_list_fragment_for_list_result_from_nearest_seller));
                    }
                }else{
                    if(!noDuplicateEntry(getString(R.string.nearest_first))){
                        stringArrayList.add(getString(R.string.nearest_first));
                    }
                }

            }
            if (sort_by_recent.isChecked()) {
                if (from == GlobalVariables.TYPE_GARAGE_SALE){
                    detail.setResults_text(getString(R.string.filter_newest_seller));
                    if(!noDuplicateEntry(getString(R.string.filter_newest_seller))){
                        stringArrayList.add(getString(R.string.filter_newest_seller));
                    }
                }else{
                    detail.setResults_text(getString(R.string.newly_listed));
                    if(!noDuplicateEntry(getString(R.string.newly_listed))){
                        stringArrayList.add(getString(R.string.newly_listed));
                    }
                }
                detail.setSortByRecent_garage(true);
            } else{
                detail.setSortByRecent_garage(false);
            }
            if (ending_soon.isChecked()) {
                detail.setResults_text(getString(R.string.ending_first));
                if(!noDuplicateEntry(getString(R.string.ending_first))){
                    stringArrayList.add(getString(R.string.ending_first));
                }
                detail.setEnding_soon(true);
            } else
                detail.setEnding_soon(false);
            if (nly_new_items_s.isChecked()){
                if(!noDuplicateEntry(getString(R.string.only_new_items))){
                    stringArrayList.add(getString(R.string.only_new_items));
                }
                detail.setNew_items(true);
            }else
                detail.setNew_items(false);

            if (switchOnlyFreeItem.isChecked()){
                if(!noDuplicateEntry(getString(R.string.only_free_items))){
                    stringArrayList.add(getString(R.string.only_free_items));
                }
                detail.setFreeItems(true);
            } else{
                detail.setFreeItems(false);
            }

            if (nly_negotiable_item_s.isChecked()){
                if(!noDuplicateEntry(getString(R.string.only_negotiable_items))){
                    stringArrayList.add(getString(R.string.only_negotiable_items));
                }
                detail.setNegotiable_items(true);
            } else
                detail.setNegotiable_items(false);
        } else if (from == GlobalVariables.TYPE_SEARCH || from == GlobalVariables.TYPE_ITEMS) {

            if(nearest_first.isChecked()){

                if (from == GlobalVariables.TYPE_GARAGE_SALE){
                    if(!noDuplicateEntry(getString(R.string.item_list_fragment_for_list_result_from_nearest_seller))){
                        stringArrayList.add(getString(R.string.item_list_fragment_for_list_result_from_nearest_seller));
                    }
                }else{
                    if(!noDuplicateEntry(getString(R.string.nearest_first))){
                        stringArrayList.add(getString(R.string.nearest_first));
                    }
                }

            }
            if (sort_by_recent.isChecked()) {

                if (from == GlobalVariables.TYPE_GARAGE_SALE){
                    detail.setResults_text(getString(R.string.filter_newest_seller));
                    if(!noDuplicateEntry(getString(R.string.filter_newest_seller))){
                        stringArrayList.add(getString(R.string.filter_newest_seller));
                    }
                }else{
                    detail.setResults_text(getString(R.string.newly_listed));
                    if(!noDuplicateEntry(getString(R.string.newly_listed))){
                        stringArrayList.add(getString(R.string.newly_listed));
                    }
                }
                detail.setSortByRecent(true);
            } else
                detail.setSortByRecent(false);

            if (h_to_l.isChecked()) {
                detail.setPrice_h_2_l(true);
                detail.setResults_text(getString(R.string.highest_to_lowest_price));
                if(!noDuplicateEntry(getString(R.string.highest_to_lowest_price))){
                    stringArrayList.add(getString(R.string.highest_to_lowest_price));
                }
            } else
                detail.setPrice_h_2_l(false);
            if (l_to_h.isChecked()) {
                stringArrayList.add(getString(R.string.lowest_to_highest_price));
                if(!noDuplicateEntry(getString(R.string.lowest_to_highest_price))){
                    stringArrayList.add(getString(R.string.lowest_to_highest_price));
                }
                detail.setPrice_l_2_h(true);
            } else
                detail.setPrice_l_2_h(false);

            stringArrayList.add(categoryText);

            if(search_radius.isChecked()){
                stringArrayList.add(distanceMaxRaange_tv.getText().toString());
            }

            if (nly_new_items_s.isChecked()){
                if(!noDuplicateEntry(getString(R.string.only_new_items))){
                    stringArrayList.add(getString(R.string.only_new_items));
                }
                detail.setNew_items(true);
            }else
                detail.setNew_items(false);

            if (switchOnlyFreeItem.isChecked()){
                if(!noDuplicateEntry(getString(R.string.only_free_items))){
                    stringArrayList.add(getString(R.string.only_free_items));
                }
                detail.setFreeItems(true);
            } else{
                detail.setFreeItems(false);
            }

            if (nly_negotiable_item_s.isChecked()){
                if(!noDuplicateEntry(getString(R.string.only_negotiable_items))){
                    stringArrayList.add(getString(R.string.only_negotiable_items));
                }
                detail.setNegotiable_items(true);
            } else
                detail.setNegotiable_items(false);

            if (only_garage_items.isChecked()){
                if(!noDuplicateEntry(getString(R.string.only_sales_items))){
                    stringArrayList.add(getString(R.string.only_sales_items));
                }
                detail.setGarage_items(true);
            }else
                detail.setGarage_items(false);

        }
       /* hashSet.addAll(stringArrayList);
        stringArrayList.clear();
        stringArrayList.addAll(hashSet);*/
        detail.setArrayListFilterResult(stringArrayList);

        detail.setSorting_enabled(true);
        GlobalFunctions.setSharedPreferenceString(context, GlobalVariables.FILTER_MODEL, detail.toString());

    }

    private boolean noDuplicateEntry(String value){
      if(stringArrayList!=null){
          return stringArrayList.contains(value);
      }else {
          return false;
      }
    }

    @Override
    public void onStart() {
        super.onStart();
        categoryListModel = GlobalFunctions.getCategories(activity);
        if (categoryListModel != null)
            is_checked = new boolean[categoryListModel.getCategoryNames().size()];

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == activity.RESULT_OK) {
                if (requestCode == globalVariables.REQUEST_CODE_FILTER_PICK_LOCATION) {
                    LocationModel location_model = (LocationModel) data.getExtras().getSerializable(BUNDLE_KEY_FILTER_MODEL_SERIALIZABLE);
                    detail.setLatitude(location_model.getLatitude());
                    detail.setLongitude(location_model.getLongitude());
                    detail.setLocation(location_model.getCity() + " " + location_model.getState());
                    detail.setFormatted_address(location_model.getFormatted_address());
                    location.setText(location_model.getFormatted_address());
                } else if (requestCode == globalVariables.REQUEST_CODE_SELECT_CATEGORY) {

                    CategoryModel categoryModel = (CategoryModel) data.getExtras().getSerializable(CREATE_ITEM_CATEGORY_BUNDLE_KEY);

                    category.setText(categoryModel.getName());
                    selectedCategoryString.clear();
                    selectedCategoryString.add(categoryModel.getName());
                    all_categories.setTextColor(context.getResources().getColor(R.color.brand_text_color));
                    ic_check.setVisibility(View.GONE);
                    detail.setCategories(categoryModel.getId());
                    if (categoryModel.getName().equalsIgnoreCase("All")) {
                        categoryText = "Category - " + categoryModel.getName();
                        category.setText("Category - " + categoryModel.getName());
                        //detail.setCategory_names("Category - "+categoryModel.getName());
                    } else {

                        categoryText = categoryModel.getName();
                        category.setText(categoryModel.getName());
                    }
                    detail.setCategory_names(categoryModel.getName());
                }
            }
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

    public static void closeThisActivity() {
        if (activity != null) {
            activity.finish();
        }
    }

    public void showExitAlert() {
        final com.blueshak.app.blueshak.view.AlertDialog alertDialog = new com.blueshak.app.blueshak.view.AlertDialog(activity);
        alertDialog.setTitle("Are you sure?");
        alertDialog.setMessage("Filter changes will not reflect");
        alertDialog.setPositiveButton("Yes", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeThisActivity();
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

    @Override
    public void onBackPressed() {
        if (!((Activity) context).isFinishing()) {
            showExitAlert();
        }
    }

    private void setReturnResult(FilterModel filterModel) {
        if (filterModel != null) {
            Log.d(TAG, "##########setReturnResult#########" + filterModel.toString());
            Bundle bundle = new Bundle();
            Intent result;
            if (from == GlobalVariables.TYPE_SEARCH) {
                result = new Intent(activity, SearchActivity.class);
                bundle.putSerializable(SearchActivity.MAIN_ACTIVITY_FILTER_MODEL_SERIALIZE, filterModel);
            } else {
                result = new Intent(activity, MainActivity.class);
                bundle.putSerializable(MainActivity.MAIN_ACTIVITY_FILTER_MODEL_SERIALIZE, filterModel);
            }
            result.putExtras(bundle);
            setResult(this.RESULT_OK, result);
        } else {
            setResult(this.RESULT_CANCELED);
        }
        closeThisActivity();
    }

    public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
        private Drawable mDivider;

        public SimpleDividerItemDecoration(Context context) {
            mDivider = context.getResources().getDrawable(R.drawable.line_divider);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();
            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }

    @Override
    public void onSelected(int position) {
        if (is_all) {
            is_all = false;
            all_categories.setTextColor(context.getResources().getColor(R.color.brand_text_color));
            ic_check.setVisibility(View.GONE);
        }

        Log.d(TAG, "onSelected###############" + category_list.get(position).getName());
        all_categories.setTextColor(context.getResources().getColor(R.color.brand_text_color));
        ic_check.setVisibility(View.GONE);
        detail.setCategories(category_list.get(position).getId() + "");
        detail.setCategory_names(category_list.get(position).getName());


        /* setReturnResult(category_list.get(position));*/

    }

    public void setSelectedCategories() {
        String data = "";
        List<CategoryModel> stList = ((CategoryListAdapter) adapter).getProductList();
        List<CategoryModel> selected_List = new ArrayList<CategoryModel>();
        selected_List.clear();

        for (int i = 0; i < stList.size(); i++) {
            CategoryModel productModel = stList.get(i);
            if (productModel.is_selected() == true) {
                /*data = data + "\n" + productModel.getName().toString();*/
                data = data + productModel.getId() + ",";
                selected_List.add(productModel);
            }
        }
        CategoryModel categoryModel = new CategoryModel();
        categoryModel.setSelectedCategoryString(selected_List);
        Log.d(TAG, "####Selected ids#########" + data);

        selectedCategoryString.clear();
        for (int i = 0; i < categoryModel.getSelectedCategoryString().size(); i++) {
            selectedCategoryString.add(categoryModel.getSelectedCategoryString().get(i).getName());
        }
        if (selectedCategoryString.size() > 0) {
            String temp = "";
            for (int i = 0; i < selectedCategoryString.size(); i++) {
                temp = i == 0 ? selectedCategoryString.get(0) : temp + ", " + selectedCategoryString.get(i);
            }
            String categories = temp;
           /* detail.setCategories(categories==null?"":categoryListModel.getIDStringfromNameString(categories));*/
            detail.setCategories(data);
            detail.setCategory_names(categories);
        }

    }

    public void setAll(boolean all_select) {
        for (CategoryModel categoryModel : category_list) {
            if (categoryModel.is_selected())
                categoryModel.setIs_selected(false);
            /*if(all_select)
                categoryModel.setIs_selected(true);
            else
                categoryModel.setIs_selected(false);*/
        }
        synchronized (adapter) {
            adapter.notifyDataSetChanged();
        }
        detail.setCategories("");

    }

    public void setupGarageFilter() {
        title_tv.setText("Sale Filter");
        mTvNearestFirst.setText(getString(R.string.item_list_fragment_for_list_result_from_nearest_seller));
        mTvNewlyListed.setText(getString(R.string.filter_newest_seller));
        sorting_main_content.setVisibility(View.VISIBLE);
        sorting_content.setVisibility(View.VISIBLE);
        nearest_content.setVisibility(View.VISIBLE);
        h_to_l_content.setVisibility(View.GONE);
        l_to_h_content.setVisibility(View.GONE);
        ending_soon_content.setVisibility(View.VISIBLE);


    }

    public void setupSearchFilter() {
        if (from == GlobalVariables.TYPE_SEARCH) {
            title_tv.setText("Search Filter");
        } else if (from == GlobalVariables.TYPE_ITEMS) {
            title_tv.setText("Item Filter");
        }

        mTvNearestFirst.setText(getString(R.string.nearest_first));
        mTvNewlyListed.setText(getString(R.string.filter_newly_listed_item));

        only_garage_item_content.setVisibility(View.VISIBLE);
        negotial_l.setVisibility(View.VISIBLE);
        only_new_item_l.setVisibility(View.VISIBLE);
        only_garage_items.setVisibility(View.VISIBLE);
        sorting_main_content.setVisibility(View.VISIBLE);
        sorting_content.setVisibility(View.VISIBLE);
        nearest_content.setVisibility(View.VISIBLE);
        h_to_l_content.setVisibility(View.VISIBLE);
        l_to_h_content.setVisibility(View.VISIBLE);
        layoutFreeItems.setVisibility(View.VISIBLE);

        ending_soon_content.setVisibility(View.GONE);

       /* most_recent_content.setVisibility(View.VISIBLE);*/

    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    private void resetLocation(final double lat, final double lng) {
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getAddress(activity, lat, lng, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                Log.d(TAG, "##########Resetting the Location#########");
                GlobalFunctions.hideProgress();
                try {
                    LocationModel locationModel_ = (LocationModel) arg0;
                    locationModel = locationModel_;
                    if (locationModel != null) {
                        locationModel.setLatitude(Double.toString(lat));
                        locationModel.setLongitude(Double.toString(lng));
                        detail.setLatitude(locationModel.getLatitude());
                        detail.setLongitude(locationModel.getLongitude());
                        detail.setLocation(locationModel.getCity() + " " + locationModel.getState());
                        detail.setFormatted_address(locationModel.getFormatted_address());
                        location.setText(locationModel.getFormatted_address());
                    }
                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                    Log.d(TAG, "Exception :" + ex.getStackTrace());
                }
            }

            @Override
            public void OnFailureFromServer(String msg) {
            }

            @Override
            public void OnError(String msg) {
            }
        }, "Get AddressFromLatLng");


    }
}
