package com.langoor.app.blueshak.map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.langoor.app.blueshak.services.model.SalesListModelNew;
import com.langoor.app.blueshak.services.model.SalesModel;
import com.langoor.app.blueshak.services.model.Shop;
import com.langoor.app.blueshak.util.LocationListener;
import com.langoor.app.blueshak.util.LocationService;
import com.langoor.app.blueshak.view_sales.MapActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class MapViewFragment extends Fragment implements OnMapReadyCallback,LocationListener {
    public static final String TAG = "MapFragmentSales";
    public static final String MAP_FRAGMENT_SALES_BUNDLE_TYPE_STRING = "MapFragmentSalesBundleTypeString";
    public static final String MAP_FRAGMENT_SALES_PRODUCT_ID_STRING = "MapFragmentProductIdTypeString";
    public static final String MAP_FRAGMENT_SALES_FROM_ACTIVITY_ID_STRING = "MapFragmentFromActivitypeString";
    public static final String SHOP = "shop";
    public FilterModel model=new FilterModel();
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
    int minPriceValue=0, maxPriceValue=GlobalVariables.PRICE_MAX_VALUE, minDistanceValue=0, maxDistanceValue=GlobalVariables.DISTANCE_MAX_VALUE;
    public static MapViewFragment newInstance(String type, ProductModel product_id, Shop shop, boolean from_activity ){
        MapViewFragment mapFragmentSales = new MapViewFragment();
        Bundle bundle = new Bundle();
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
            view = inflater.inflate(R.layout.map_fragment_item, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */

        }
        try{
            //View view = inflater.inflate(R.layout.map_fragment, container, false);
            en_large=(ImageView)view.findViewById(R.id.en_large);
            go_to_filter=(ImageView)view.findViewById(R.id.go_to_filter);
            go_to_filter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addFilter();
                }
            });
            results_all=(TextView)view.findViewById(R.id.results_all);
            header_content=(LinearLayout)view.findViewById(R.id.header_content);
            header_content.setVisibility(View.GONE);
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
            if(!from_activity)
                en_large.setVisibility(View.VISIBLE);
            else
                en_large.setVisibility(View.GONE);
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

            if(type.equalsIgnoreCase(GlobalVariables.TYPE_SHOP)){
                shop=(Shop)getArguments().getSerializable(SHOP);
                productModel=(ProductModel) getArguments().getSerializable(MAP_FRAGMENT_SALES_PRODUCT_ID_STRING);
                if(shop!=null){
                    shop_name=shop.getName();
                    icon_name=shop_name;
                    lat=Double.parseDouble(shop.getLatitude());
                    lng=Double.parseDouble(shop.getLongitude());
                }
                if(productModel!=null){
                    icon_name=productModel.getName();
                    shop_name=productModel.getName();
                    lat=Double.parseDouble(productModel.getLatitude());
                    lng=Double.parseDouble(productModel.getLongitude());
                }
            }else{
                Location loc = locServices.getLocation();
                if(loc!=null){
                    model.setLatitude(loc.getLatitude()+"");
                    model.setLongitude(loc.getLongitude()+"");
                    model.setRange(maxDistanceValue);
                    model.setAvailable(true);
                    model.setPickup(true);
                    model.setShipable(true);
                    model.setType(GlobalVariables.TYPE_GARAGE);
                    model.setPriceRange(minPriceValue+","+maxPriceValue);
                    getLists(getContext(),model);
                    locServices.removeListener();
                }
            }
            listFAB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  /*  if(salesListModel.getSalesList().size()>0){
                        MainActivity.replaceFragment(SalesList.newInstance(salesListModel, type,null,false), SalesList.TAG);}
     */           }
            });
           /* sale_header_name.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                   final int DRAWABLE_LEFT = 0;
                    final int DRAWABLE_TOP = 1;
                    final int DRAWABLE_RIGHT = 2;
                    final int DRAWABLE_BOTTOM = 3;
                    final Location loc=locServices.getLocation();
                    if(event.getAction() == MotionEvent.ACTION_UP) {
                        if(event.getRawX() >= (sale_header_name.getRight() - sale_header_name.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            if(salesListModel.getSalesList().size()>0){
                                MainActivity.replaceFragment(SalesList.newInstance(salesListModel, type,null,false), SalesList.TAG);}
                            return true;
                        }
                    }
                    return false;
                }
            });*/
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
        super.onResume();
    }

    private void getLists(Context context, FilterModel filterModel){
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getListDetails(context, filterModel, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                Log.d(TAG, "########onSuccess Response###########");
                salesListModel = (SalesListModelNew) arg0;
                String str = salesListModel.toString();
                Log.d(TAG, str);
                addItems();
            }

            @Override
            public void OnFailureFromServer(String msg) {
                Log.d(TAG, msg);
            }

            @Override
            public void OnError(String msg) {
                Log.d(TAG, msg);
            }
        }, "List Sales");

    }

   /* private void getLists(Context context){
        ServicesMethodsManager servicesMethodsManager = new ServicesMethodsManager();
        servicesMethodsManager.getMySalesList(context, new ServerResponseInterface() {
            @Override
            public void OnSuccessFromServer(Object arg0) {
                Log.d(TAG, "onSuccess Response");
                salesListModel = (SalesListModelNew) arg0;
                String str = salesListModel.toString();
                Log.d(TAG, str);
                addItems();
            }

            @Override
            public void OnFailureFromServer(String msg) {
                Log.d(TAG, msg);
            }

            @Override
            public void OnError(String msg) {
                Log.d(TAG, msg);
            }
        }, "List Sales");

    }*/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        /*googleMap.setMyLocationEnabled(true);*/
        googleMap.setMyLocationEnabled(true);
        googleMap.setTrafficEnabled(true);
        googleMap.setIndoorEnabled(true);
        googleMap.setBuildingsEnabled(true);
        if(from_activity || !type.equalsIgnoreCase(GlobalVariables.TYPE_SHOP))
            googleMap.getUiSettings().setZoomControlsEnabled(true);
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
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,GlobalVariables.MAP_ZOOMING_INT));
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
        List<SalesModel> list = salesListModel.getSalesList();
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
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,8));
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
                iconFactory.setColor(getResources().getColor(R.color.tab_selected));
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

   /* public Bitmap createDrawableFromView(Context context) {
        View marker = ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
                inflate(R.layout.map_popup, your_parent_layout,false);
        TextView tv_location_name = (TextView) marker.findViewById(R.id.tv_location_name);
        TextView tv_event_status = (TextView) marker.findViewById(R.id.tv_event_status);
        TextView tv_event_name = (TextView) marker.findViewById(R.id.tv_event_name);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);
        return bitmap;
    }*/
   private MyItem clickedClusterItem;
    private Cluster<MyItem> clickedCluster;
    private final GoogleMap.InfoWindowAdapter mInfoWindowAdapter = new GoogleMap.InfoWindowAdapter() {
        @Override
        public View getInfoWindow(final Marker marker) {
            View window = null;
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
                    String item_image=displayImageURL.get(0);
                    ImageLoader imageLoader = ImageLoader.getInstance();
                    DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                            .cacheOnDisc(true).resetViewBeforeLoading(true)
                            .showImageForEmptyUri(R.drawable.placeholder_background)
                            .showImageOnFail(R.drawable.placeholder_background)
                            .showImageOnLoading(R.drawable.placeholder_background).build();
                    //download and display image from url
                    imageLoader.displayImage(item_image,sale_image, options);

                  /*  if(!TextUtils.isEmpty(item_image)){
                        Picasso.with(activity).load(item_image).placeholder(R.drawable.placeholder_background).error(R.drawable.placeholder_background).into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                int targetWidth = bitmap.getWidth();
                                double aspectRatio = (double) bitmap.getHeight() / (double) bitmap.getWidth();
                                int targetHeight = (int) (targetWidth * aspectRatio);
                                Bitmap result = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, false);
                               sale_image.setImageBitmap(result);
                                getInfoContents(marker);
                            *//*if (result != bitmap) {
                                // Same bitmap is returned if sizes are the same
                                bitmap.recycle();

                            }*//*
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {
                               sale_image.setImageDrawable(errorDrawable);
                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {
                                sale_image.setImageDrawable(placeHolderDrawable);
                            }
                        });
                    }*/
                }else{
                    System.out.println("The clicked cluster item was nulllll");
                }
              /*  if (clickedCluster != null) {
                    for (Objective item : clickedCluster.getItems()) {
                        // Extract data from each item in the cluster as needed
                        if(item.getRemoteId().equals(clickedClusterItem.getRemoteId())){
                            nameTV.setText(clickedClusterItem.getName());
                        }
                    }
                }*/
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
        Intent filter_intent= FilterActivity.newInstance(context,null,GlobalVariables.TYPE_GARAGE_SALE);
        startActivityForResult(filter_intent,globalVariables.REQUEST_CODE_FILTER_MODEL_LOCATION);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG,"on activity result in MApFrgment######################");
        if(resultCode == activity.RESULT_OK){
            Log.i(TAG,"result ok ");
            Log.i(TAG,"request code "+globalVariables.REQUEST_CODE_FILTER_MODEL_LOCATION);
            if(data.hasExtra(MainActivity.MAIN_ACTIVITY_FILTER_MODEL_SERIALIZE)){
                model=(FilterModel) data.getExtras().getSerializable(MainActivity.MAIN_ACTIVITY_FILTER_MODEL_SERIALIZE);
                if(model!=null){
                    String category_names=model.getCategory_names();
                    if(!TextUtils.isEmpty(category_names)&& category_names!=null)
                        results_all.setText("Results in "+"'"+model.getCategory_names()+"'");
                    else
                        results_all.setText("Results in "+"'"+"All"+"'");
                    Log.i(TAG,"name pm"+model.getFormatted_address());
                    mClusterManager.clearItems();
                    getLists(context,model);
                }
            }
        }
    }
}
