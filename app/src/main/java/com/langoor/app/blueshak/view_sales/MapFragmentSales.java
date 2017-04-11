package com.langoor.app.blueshak.view_sales;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolygonOptions;
import com.langoor.app.blueshak.global.GlobalFunctions;
import com.langoor.app.blueshak.search.SearchActivity;
import com.langoor.app.blueshak.services.model.LocationModel;
import com.langoor.blueshak.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.langoor.app.blueshak.AppController;
import com.langoor.app.blueshak.MainActivity;
import com.langoor.app.blueshak.filter.FilterActivity;
import com.langoor.app.blueshak.global.GlobalVariables;
import com.langoor.app.blueshak.item.ProductDetail;
import com.langoor.app.blueshak.services.ServerResponseInterface;
import com.langoor.app.blueshak.services.ServicesMethodsManager;
import com.langoor.app.blueshak.services.model.FilterModel;
import com.langoor.app.blueshak.services.model.ProductModel;
import com.langoor.app.blueshak.services.model.SalesListModel;
import com.langoor.app.blueshak.services.model.SalesListModelNew;
import com.langoor.app.blueshak.services.model.SalesModel;
import com.langoor.app.blueshak.services.model.Shop;
import com.langoor.app.blueshak.util.LocationListener;
import com.langoor.app.blueshak.util.LocationService;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class MapFragmentSales extends Fragment implements OnMapReadyCallback,LocationListener {
    public static final String TAG = "MapFragmentSales";
    public static final String MAP_FRAGMENT_SALES_BUNDLE_TYPE_STRING = "MapFragmentSalesBundleTypeString";
    public static final String MAP_FRAGMENT_SALES_PRODUCT_ID_STRING = "MapFragmentProductIdTypeString";
    public static final String MAP_FRAGMENT_SALES_FROM_ACTIVITY_ID_STRING = "MapFragmentFromActivitypeString";
    public static final String SHOP = "shop";
    public FilterModel model=null;
    private GlobalVariables globalVariables=new GlobalVariables();
    // Declare a variable for <></>he cluster manager.
    private ClusterManager<MyItem> mClusterManager;
    Activity activity;
    private Context context;
    private GoogleMap map;
    private FloatingActionButton listFAB;
    private SupportMapFragment fragment;
    private SalesListModelNew salesListModel = new SalesListModelNew();
    private String type = GlobalVariables.TYPE_GARAGE;
    TextView sale_header_name,results_all;
    LocationService locServices;
    private String product_id=null;
    private ProductModel productModel=null;
    private Shop shop=null;
    private Double lat;
    private  Double lng;
    private  String shop_name="";
    private  String icon_name="";
    private static View view;
    private  boolean from_activity=false;
    private ImageView en_large,go_to_filter;
    private LinearLayout header_content;
    private ProgressBar progress_bar;
    List<SalesModel> list=new ArrayList<SalesModel>();
    private int last_page=1,current_page=1;
    private int minPriceValue=0, maxPriceValue=GlobalVariables.PRICE_MAX_VALUE, minDistanceValue=0, maxDistanceValue=GlobalVariables.DISTANCE_MAX_VALUE;
    public static final String SALE_FILTER="Garagefiltermodel";
    public static final String SALE_LOCATION_MODEL_BUNDLE_KEY="salelocationbundlekey";
    private TextView searchViewResult;
    private LocationModel locationModel=new LocationModel();

    public static MapFragmentSales newInstance(LocationModel locationModel,String type,ProductModel product_id,Shop shop,boolean from_activity ,FilterModel filterModel){
        MapFragmentSales mapFragmentSales = new MapFragmentSales();
        Bundle bundle = new Bundle();
        bundle.putSerializable(SALE_LOCATION_MODEL_BUNDLE_KEY,locationModel);
        bundle.putSerializable(SALE_FILTER, filterModel);
        bundle.putString(MAP_FRAGMENT_SALES_BUNDLE_TYPE_STRING, type);
        bundle.putSerializable(MAP_FRAGMENT_SALES_PRODUCT_ID_STRING, product_id);
        bundle.putBoolean(MAP_FRAGMENT_SALES_FROM_ACTIVITY_ID_STRING, from_activity);
        bundle.putSerializable(SHOP, shop);
        mapFragmentSales.setArguments(bundle);
        return mapFragmentSales;
    }
   /* @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = getActivity();
        context=getContext();
    }*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
         activity = getActivity();
        context=getActivity();
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.map_fragment, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */

        }
        try{
            //View view = inflater.inflate(R.layout.map_fragment, container, false);
            progress_bar=(ProgressBar)view.findViewById(R.id.progress_bar);
            en_large=(ImageView)view.findViewById(R.id.en_large);
            en_large.setVisibility(View.GONE);
            searchViewResult=(TextView)view.findViewById(R.id.searchViewResult);
            searchViewResult.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(activity, SearchActivity.class));
                }
            });
            Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
            go_to_filter=(ImageView)toolbar.findViewById(R.id.go_to_filter);
            go_to_filter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addFilter();
                }
            });
            /*go_to_filter=(ImageView)view.findViewById(R.id.go_to_filter);
            go_to_filter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addFilter();
                }
            });*/
            results_all=(TextView)view.findViewById(R.id.results_all);
            header_content=(LinearLayout)view.findViewById(R.id.header_content);
            header_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addFilter();
                }
            });
            locServices = new LocationService(activity);
            locServices.setListener(this);
            final FragmentManager fm = getChildFragmentManager();
            fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
            listFAB = (FloatingActionButton) view.findViewById(R.id.map_fragment_list_fab);
            sale_header_name= (TextView) view.findViewById(R.id.sale_header_name);

            if (fragment == null) {
                fragment = SupportMapFragment.newInstance();
                fm.beginTransaction().replace(R.id.map, fragment).commit();
                fragment.getMapAsync(this);
            }else{
                fragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
                fragment.getMapAsync(this);
            }
            from_activity = getArguments().getBoolean(MAP_FRAGMENT_SALES_FROM_ACTIVITY_ID_STRING);
            type = getArguments().getString(MAP_FRAGMENT_SALES_BUNDLE_TYPE_STRING);
            model = (FilterModel) getArguments().getSerializable(SALE_FILTER);
            locationModel=(LocationModel)getArguments().getSerializable(SALE_LOCATION_MODEL_BUNDLE_KEY);
            en_large.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!from_activity){
                        ProductModel productModel=new ProductModel();
                        productModel.setName(shop_name);
                        productModel.setLongitude(Double.toString(lng));
                        productModel.setLatitude(Double.toString(lat));
                        Intent i= MapActivity.newInstance(activity,GlobalVariables.TYPE_SHOP,productModel);
                        startActivity(i);
                    }
                }
            });

            if(model==null){
                Log.d(TAG,"@#################model==null");
                    Location loc = locServices.getLocation();
                    if(loc!=null){
                        model=new FilterModel();
                        Log.d(TAG,"@#################loc!=null");
                        model.setLatitude(loc.getLatitude()+"");
                        model.setLongitude(loc.getLongitude()+"");
                        model.setRange(maxDistanceValue);
                        model.setAvailable(true);
                        model.setPickup(true);
                        model.setShipable(true);
                        model.setType(GlobalVariables.TYPE_GARAGE);
                        model.setPriceRange(minPriceValue+","+maxPriceValue);
                        String country= GlobalFunctions.getSharedPreferenceString(context,GlobalVariables.SHARED_PREFERENCE_LOCATION_COUNTRY);
                        if(country!=null){
                            model.setCurrent_country_code(country);
                            model.setIs_current_country(true);
                        }
                        model.setPage(1);
                        lat=Double.parseDouble(model.getLatitude());
                        lng=Double.parseDouble(model.getLongitude());
                        cleaMap();
                        getLists(getContext(),model);
                        locServices.removeListener();
                    }
            }else{
                lat=Double.parseDouble(model.getLatitude());
                lng=Double.parseDouble(model.getLongitude());
                Log.d(TAG,"@#############model!!!!!!!!!!!!!!!11=null");
                cleaMap();
                model.setPage(1);
                getLists(getContext(),model);
            }

        } catch (NullPointerException e){
            Log.d(TAG,"NullPointerException");
            e.printStackTrace();
        }catch (NumberFormatException e) {
            Log.d(TAG,"NumberFormatException");
            e.printStackTrace();
        }catch (Exception e){
            Log.d(TAG,"Exception");
            e.printStackTrace();
        }
        return view;
    }
    @Override
    public void onResume() {
        MainActivity.setTitle(getString(R.string.garageSale),0);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        super.onResume();
    }

    private void getLists(final Context context, FilterModel filterModel){
        showProgressBar();
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getListDetails(context, filterModel, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                Log.d(TAG, "########onSuccess Response###########");
                salesListModel = (SalesListModelNew) arg0;
                String str = salesListModel.toString();
                current_page=salesListModel.getCurrent_page();
                last_page=salesListModel.getLast_page();
                Log.d(TAG, "#########current_page##########"+current_page);
                List<SalesModel> local_list=salesListModel.getSalesList();
                list.addAll(local_list);
                local_list.clear();
                if(!(current_page>=last_page)){
                    current_page++;
                    model.setPage(current_page);
                    getLists(context,model);
                }else{
                    Log.d(TAG, "#########All pages done##########");
                    hideProgressBar();
                    addItems();
                    current_page=1;
                    last_page=1;
                  }
            }

            @Override
            public void OnFailureFromServer(String msg) {
                Log.d(TAG, "######OnFailureFromServer######");
                Log.d(TAG, msg);
                cleaMap();
                hideProgressBar();
            }

            @Override
            public void OnError(String msg) {
                Log.d(TAG, "#######OnError###########");
                Log.d(TAG, msg);
                cleaMap();
                hideProgressBar();
            }
        }, "List Sales");

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        /*googleMap.setMyLocationEnabled(true);*/
      /*  googleMap.setMyLocationEnabled(true);*/
        googleMap.setTrafficEnabled(true);
        googleMap.setIndoorEnabled(true);
        googleMap.setBuildingsEnabled(true);
        /*if(from_activity))
            googleMap.getUiSettings().setZoomControlsEnabled(true);*/
        map = googleMap;

        if(type.equalsIgnoreCase(GlobalVariables.TYPE_SHOP))
            addMarker();
        else
            setUpClusterer();
    }
    private void addMarker() {
        MarkerOptions options = new MarkerOptions();
        /*MarkerOptions marker = new MarkerOptions().position(currentLatLng).title("resturan")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.profile_pic));
        */

        // following four lines requires 'Google Maps Android API Utility Library'
        // https://developers.google.com/maps/documentation/android/utility/
        // I have used this to display the time as title for location markers
        // you can safely comment the following four lines but for this info
        IconGenerator iconFactory = new IconGenerator(activity);
        iconFactory.setStyle(IconGenerator.STYLE_PURPLE);
        iconFactory.setColor(getResources().getColor(R.color.tab_selected));
     /*   options.icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(icon_name)));*/
        /*options.icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(icon_name)));*/
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_orange_small));
        options.anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());
        LatLng currentLatLng = new LatLng(lat,lng);
        options.position(currentLatLng);
        Marker mapMarker = map.addMarker(options);
        mapMarker.setTitle(shop_name);
        Log.d(TAG, "Marker added.............................");
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,0));
        Log.d(TAG, "Zoom done.............................");

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(!from_activity){
                    ProductModel productModel=new ProductModel();
                    productModel.setName(shop_name);
                    productModel.setLongitude(Double.toString(lng));
                    productModel.setLatitude(Double.toString(lat));
                    Intent i= MapActivity.newInstance(activity,GlobalVariables.TYPE_SHOP,productModel);
                    startActivity(i);
                }
                return false;
            }
        });
    }
    @Override
    public void onLocationChanged(Location location) {
       /* if(!type.equalsIgnoreCase(GlobalVariables.TYPE_MY_SALE+"")){
            Location loc = locServices.getLocation();
            if(loc!=null){
                FilterModel model = new FilterModel();
                model.setLatitude(loc.getLatitude()+"");
                model.setLongitude(loc.getLongitude()+"");
                model.setRange(10);
                model.setAvailable(true);
                model.setPickup(true);
                model.setShipable(true);
                model.setType(type);
                model.setPriceRange("0,10000");
                getLists(getContext(),model);
                locServices.removeListener();
                setUpClusterer();
            }
        }*/
    }

    public class MyItem implements ClusterItem {
        private SalesModel salesModel;

        public MyItem(SalesModel salesModel) {
            this.salesModel = salesModel;
        }

        public SalesModel getSalesModel() {
            return salesModel;
        }

        public void setSalesModel(SalesModel salesModel) {
            this.salesModel = salesModel;
        }

        @Override
        public LatLng getPosition() {
            return new LatLng(Double.parseDouble(salesModel.getLatitude()), Double.parseDouble(salesModel.getLongitude()));
        }
    }

    private void setUpClusterer() {
        if(locServices.getLocation()!=null)map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(locServices.getLatitude(), locServices.getLongitude()), 10));
        mClusterManager = new ClusterManager<MyItem>(getActivity(), map);
        /*mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(new MarkerInfoWindowAdapter());
        mClusterManager.getClusterMarkerCollection().setOnInfoWindowAdapter(new ClusterInfoWindow());*/
        mClusterManager.setRenderer(new OwnIconRendered(getActivity().getApplicationContext(), map, mClusterManager));
        map.setOnCameraChangeListener(mClusterManager);
        map.setOnMarkerClickListener(mClusterManager);

        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                mClusterManager.onCameraChange(map.getCameraPosition());
            }
        });
/*        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>() {
            @Override
            public boolean onClusterItemClick(MyItem myItem) {
              *//*  MainActivity.addFragment(SaleFragment.newInstance(myItem.getSalesModel(),type), "salesFragment");*//*
                *//*ProductModel productModel=new ProductModel();
                productModel.setName(shop_name);
                productModel.setLongitude(Double.toString(lat));
                productModel.setLatitude(Double.toString(lng));
                Intent i= MapActivity.newInstance(activity,GlobalVariables.TYPE_MULTIPLE_ITEMS,productModel);
                startActivity(i);*//*
                Intent intent = ProductDetail.newInstance(activity,null,myItem.getSalesModel(),GlobalVariables.TYPE_MY_SALE);
                activity.startActivity(intent);
                return true;
            }
        });*/
        /*Testing*/  map.setInfoWindowAdapter(mClusterManager.getMarkerManager());
        mClusterManager.getClusterMarkerCollection().setOnInfoWindowAdapter(mInfoWindowAdapter);
        mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(mInfoWindowAdapter);

        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MyItem>() {
            @Override
            public boolean onClusterClick(Cluster<MyItem> cluster) {
                clickedCluster = cluster; // remember for use later in the Adapter
                return false;
            }
        });
        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>() {
            @Override
            public boolean onClusterItemClick(MyItem item) {
                clickedClusterItem = item;
                return false;
            }
        });

        //add the onClickInfoWindowListener
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = ProductDetail.newInstance(activity,null,clickedClusterItem.getSalesModel(),GlobalVariables.TYPE_MY_SALE);
                activity.startActivity(intent);
            }
        });
    }

    private void addItems() {
      /*  List<SalesModel> list *//*list= salesListModel.getSalesList();*/
        Log.d(TAG, "#########Total Item loaded##########"+list.size());
        for (int i = 0; i < list.size(); i++) {
            SalesModel item = list.get(i);
            Double lat = Double.parseDouble(item.getLatitude());
            Double lng = Double.parseDouble(item.getLongitude());
            MyItem offsetItem = new MyItem(item);
            if(product_id!=null){
                for (ProductModel productModel:item.getProducts()) {
                    if(product_id.equals(productModel.getId())){
                        item.setName(productModel.getName());
                        item.setLongitude(productModel.getLongitude());
                        item.setLatitude(productModel.getLatitude());
                        item.setId(productModel.getId());
                        MyItem offsetItem1 = new MyItem(item);
                        if(mClusterManager!=null)mClusterManager.addItem(offsetItem1);
                        break;
                    }
                }
            }else{
                if(mClusterManager!=null)mClusterManager.addItem(offsetItem);
            }

        }
        if(mClusterManager!=null)mClusterManager.cluster();
        LatLng currentLatLng = new LatLng(Double.parseDouble(model.getLatitude()),Double.parseDouble(model.getLongitude()));
       if(map!=null){
           if(model.is_current_country() || model.isDistance_enabled()){
               drawCircle();
           }
           map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,7));
       }

    }
    class OwnIconRendered extends DefaultClusterRenderer<MyItem> {

        public OwnIconRendered(Context context, GoogleMap map,
                               ClusterManager<MyItem> clusterManager) {
            super(context, map, clusterManager);
        }

       @Override
        protected boolean shouldRenderAsCluster(Cluster<MyItem> cluster) {
            //start clustering if at least 2 items overlap
            int maxSize = 100;
          /*  if(map.getCameraPosition().zoom<=0){maxSize=20;}*/
            return cluster.getSize() > maxSize;
        }

        @Override
        protected void onBeforeClusterItemRendered(MyItem item, MarkerOptions markerOptions) {
            if(getActivity()!=null){
             /*   IconGenerator icnGenerator = new IconGenerator(getActivity());
                icnGenerator.setColor(getResources().getColor(R.color.brandColor));
                icnGenerator.setTextAppearance(R.style.iconGenText);
                Bitmap iconBitmap = icnGenerator.makeIcon(item.getSalesModel().getName());
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(iconBitmap));*/


                IconGenerator iconFactory = new IconGenerator(activity);
                iconFactory.setStyle(IconGenerator.STYLE_PURPLE);
                iconFactory.setColor(context.getResources().getColor(R.color.tab_selected));
                /*options.icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(icon_name)));*/
                /*options.icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(icon_name)));*/
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_orange_small));
                markerOptions.anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());
            /*    markerOptions.title("My Shop");*/

                //markerOptions.snippet(item.getSnippet());
                //markerOptions.title(item.getTitle());
            }

            super.onBeforeClusterItemRendered(item, markerOptions);
        }

    }
    @Override
    public void onProviderEnabled(String provider) {

    }
    private MyItem clickedClusterItem;
    private Cluster<MyItem> clickedCluster;
    private final GoogleMap.InfoWindowAdapter mInfoWindowAdapter = new GoogleMap.InfoWindowAdapter() {
        @Override
        public View getInfoWindow(final Marker marker) {
            View window = null;
            try{
                if(getActivity()!=null&&isAdded()){
                    window = activity.getLayoutInflater().inflate(R.layout.map_overlay, null);
                    final TextView sale_name = (TextView) window.findViewById(R.id.sale_name);
                    final TextView sale_items = (TextView) window.findViewById(R.id.sale_items);
                    final ImageView sale_image = (ImageView) window.findViewById(R.id.sale_image);
                    final ImageView go_to_sale_info = (ImageView) window.findViewById(R.id.go_to_sale_page);
                    go_to_sale_info.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d(TAG,"########onClick###################");
                            Intent intent = ProductDetail.newInstance(activity,null,clickedClusterItem.getSalesModel(),GlobalVariables.TYPE_MY_SALE);
                            activity.startActivity(intent);
                        }
                    });
                    window.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d(TAG,"########onClick###################");
                            Intent intent = ProductDetail.newInstance(activity,null,clickedClusterItem.getSalesModel(),GlobalVariables.TYPE_MY_SALE);
                            activity.startActivity(intent);

                        }
                    });
                    if(clickedClusterItem!=null){
                        System.out.println("You clicked this: "+clickedClusterItem.getSalesModel().getName());
                        SalesModel salesModel=clickedClusterItem.getSalesModel();
                        sale_name.setText(salesModel.getName());
                        String title="";
                        if(!TextUtils.isEmpty(salesModel.getSale_items_count()))
                            if(Integer.parseInt(salesModel.getSale_items_count())==1)
                                title=salesModel.getSale_items_count()+" Item";
                            else
                                title=salesModel.getSale_items_count()+" Items";
                        else
                            title="No Items Available";

                        sale_items.setText(title);
                        ArrayList<String> displayImageURL = new ArrayList<String>();
                        displayImageURL.clear();
                        displayImageURL=salesModel.getImages();
                        String item_image="";
                        if(displayImageURL.size()>0)
                             item_image=displayImageURL.get(0);
                        ImageLoader imageLoader = ImageLoader.getInstance();
                        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                                .cacheOnDisc(true).resetViewBeforeLoading(true)
                                .showImageForEmptyUri(R.drawable.placeholder_background)
                                .showImageOnFail(R.drawable.placeholder_background)
                                .showImageOnLoading(R.drawable.placeholder_background).build();
                        //download and display image from url
                   /* imageLoader.displayImage(item_image,sale_image, options);*/
                        // Load image, decode it to Bitmap and display Bitmap in ImageView (or any other view
//  which implements ImageAware interface)
                        imageLoader.displayImage(item_image, sale_image, options, new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {

                            }
                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                            }
                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                getInfoContents(marker);
                                sale_image.setImageBitmap(loadedImage);

                            }
                            @Override
                            public void onLoadingCancelled(String imageUri, View view) {

                            }
                        }, new ImageLoadingProgressListener() {
                            @Override
                            public void onProgressUpdate(String imageUri, View view, int current, int total) {

                            }
                        });

                    }else{
                        System.out.println("The clicked cluster item was nulllll");
                    }
                }
            }catch (ArrayIndexOutOfBoundsException e){
                e.printStackTrace();
                Crashlytics.log(e.getMessage());
                Log.d(TAG,e.getMessage());
            }catch (NullPointerException e){
                e.printStackTrace();
                e.printStackTrace();
                Crashlytics.log(e.getMessage());
                Log.d(TAG,e.getMessage());
            }
            return window;
        }

        @Override
        public View getInfoContents(Marker marker) {
            if (marker != null && marker.isInfoWindowShown()) {
                marker.hideInfoWindow();
                marker.showInfoWindow();
            }
            return null;
        }
    };
    public void addFilter(){
        Intent filter_intent= FilterActivity.newInstance(context,locationModel,GlobalVariables.TYPE_GARAGE_SALE);
        startActivityForResult(filter_intent,globalVariables.REQUEST_CODE_FILTER_MODEL_LOCATION);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
            Log.i(TAG,"on activity result in MApFrgment######################");
            if(resultCode == activity.RESULT_OK){
                Log.i(TAG,"result ok ");
                Log.i(TAG,"request code "+globalVariables.REQUEST_CODE_FILTER_MODEL_LOCATION);
                if(data.hasExtra(MainActivity.MAIN_ACTIVITY_FILTER_MODEL_SERIALIZE)){
                    model=(FilterModel) data.getExtras().getSerializable(MainActivity.MAIN_ACTIVITY_FILTER_MODEL_SERIALIZE);
                    if(model!=null){
                        MainActivity.filterModel=model;
                        lat=Double.parseDouble(model.getLatitude());
                        lng=Double.parseDouble(model.getLongitude());
                        String category_names=model.getCategory_names();
                   /* if(!TextUtils.isEmpty(category_names)&& category_names!=null)
                        results_all.setText("Results in "+"'"+model.getCategory_names()+"'");
                    else
                        results_all.setText("Results in "+"'"+"All"+"'");*/
                        if(!TextUtils.isEmpty(model.getResults_text()))
                            results_all.setText("Results in "+model.getResults_text());
                        else
                            results_all.setText("Results from nearest first");

                        Log.i(TAG,"name pm"+model.getFormatted_address());
                        cleaMap();
                        model.setPage(1);
                        getLists(context,model);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.d(TAG,e.getMessage());
        }

    }
    public void cleaMap(){
        current_page=1;
        last_page=1;
        if(list!=null)
            list.clear();
        if(mClusterManager!=null&&map!=null){
            map.clear();
            mClusterManager.clearItems();
        }
    }
    public void showProgressBar(){
        if(progress_bar!=null)
            progress_bar.setVisibility(View.VISIBLE);
    }
    public void hideProgressBar(){
        if(progress_bar!=null)
            progress_bar.setVisibility(View.GONE);
    }
    private void drawBounds (LatLngBounds bounds, int color) {
        Log.d(TAG,"#########drawBounds##############");
        PolygonOptions polygonOptions =  new PolygonOptions()
                .add(new LatLng(bounds.northeast.latitude, bounds.northeast.longitude))
                .add(new LatLng(bounds.southwest.latitude, bounds.northeast.longitude))
                .add(new LatLng(bounds.southwest.latitude, bounds.southwest.longitude))
                .add(new LatLng(bounds.northeast.latitude, bounds.southwest.longitude))
                .strokeColor(context.getResources().getColor(R.color.tab_selected))
                .strokeWidth(3)
                .fillColor(context.getResources().getColor(R.color.brand_secondary_trasnparent_color));

        map.addPolygon(polygonOptions);
    }
    private void drawCircle(){
        try{
            int range;
            if(model.isDistance_enabled()){
                range=model.getRange()*1000;
            }else{
                range=250000;
            }
            CircleOptions circleOptions = new CircleOptions()
                    .center(new LatLng(lat, lng))
                    .fillColor(context.getResources().getColor(R.color.brand_secondary_trasnparent_color_1))
                    .strokeColor(context.getResources().getColor(R.color.tab_selected))
                    .strokeWidth(2)
                    .radius(range).clickable(true);; // In meters

            map.addCircle(circleOptions);
        }catch (NullPointerException e){
            e.printStackTrace();
            Log.d(TAG,e.getMessage());
        }

    }
}
