package com.blueshak.app.blueshak.map;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blueshak.app.blueshak.seller.model.SalesDetailsModel;
import com.blueshak.app.blueshak.seller.model.SalesDetailsProduct;
import com.blueshak.blueshak.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.blueshak.app.blueshak.MainActivity;
import com.blueshak.app.blueshak.filter.FilterActivity;
import com.blueshak.app.blueshak.global.GlobalVariables;
import com.blueshak.app.blueshak.item.ProductDetail;
import com.blueshak.app.blueshak.services.ServerResponseInterface;
import com.blueshak.app.blueshak.services.ServicesMethodsManager;
import com.blueshak.app.blueshak.services.model.FilterModel;
import com.blueshak.app.blueshak.services.model.ProductModel;
import com.blueshak.app.blueshak.services.model.SalesListModelNew;
import com.blueshak.app.blueshak.services.model.SalesModel;
import com.blueshak.app.blueshak.services.model.Shop;
import com.blueshak.app.blueshak.util.LocationListener;
import com.blueshak.app.blueshak.util.LocationService;
import com.blueshak.app.blueshak.view_sales.MapActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.google.android.gms.location.places.*;

import java.util.ArrayList;
import java.util.List;

public class MapViewFragment extends Fragment implements OnMapReadyCallback, LocationListener,GoogleMap.OnMapClickListener {
    public static final String TAG = "MapFragmentSales";
    public static final String MAP_FRAGMENT_SALES_BUNDLE_TYPE_STRING = "MapFragmentSalesBundleTypeString";
    public static final String MAP_FRAGMENT_SALES_PRODUCT_ID_STRING = "MapFragmentProductIdTypeString";
    public static final String MAP_FRAGMENT_SALES_FROM_ACTIVITY_ID_STRING = "MapFragmentFromActivitypeString";
    public static final String SHOP = "shop";
    public static final String SALES_DETAILS = "SalesDetails";
    public FilterModel model = new FilterModel();
    private GlobalVariables globalVariables = new GlobalVariables();
    // Declare a variable for <></>he cluster manager.
    private ClusterManager<MyItem> mClusterManager;
    Activity activity;
    private Context context;
    private GoogleMap map;
    private FloatingActionButton listFAB;
    private SupportMapFragment fragment;
    private SalesListModelNew salesListModel = new SalesListModelNew();
    private String type = GlobalVariables.TYPE_GARAGE;
    TextView sale_header_name, results_all;
    LocationService locServices;
    private String product_id = null;
    private ProductModel productModel = null;
    private Shop shop = null;
    private Double lat;
    private Double lng;
    private String shop_name = "";
    private String icon_name = "";
    private static View view;
    private boolean from_activity = false;
    private ImageView en_large, go_to_filter;
    private LinearLayout header_content;
    int minPriceValue = 0, maxPriceValue = GlobalVariables.PRICE_MAX_VALUE, minDistanceValue = 0, maxDistanceValue = GlobalVariables.DISTANCE_MAX_VALUE;
    private SalesDetailsModel salesDetails = null;

    public static MapViewFragment newInstance(String type, ProductModel product_id, Shop shop, boolean from_activity) {
        MapViewFragment mapFragmentSales = new MapViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(MAP_FRAGMENT_SALES_BUNDLE_TYPE_STRING, type);
        bundle.putSerializable(MAP_FRAGMENT_SALES_PRODUCT_ID_STRING, product_id);
        bundle.putBoolean(MAP_FRAGMENT_SALES_FROM_ACTIVITY_ID_STRING, from_activity);
        bundle.putSerializable(SHOP, shop);
        mapFragmentSales.setArguments(bundle);
        return mapFragmentSales;
    }

    public static MapViewFragment newInstance(String type, SalesDetailsModel detailsModel, boolean from_activity) {
        MapViewFragment mapFragmentSales = new MapViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(MAP_FRAGMENT_SALES_BUNDLE_TYPE_STRING, type);
        bundle.putSerializable(SALES_DETAILS, detailsModel);
        bundle.putBoolean(MAP_FRAGMENT_SALES_FROM_ACTIVITY_ID_STRING, from_activity);
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
        context = getActivity();
        if (view != null)
        {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }


        try {
            view = inflater.inflate(R.layout.map_fragment_item, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */

        }
        try {
            //View view = inflater.inflate(R.layout.map_fragment, container, false);
            en_large = (ImageView) view.findViewById(R.id.en_large);
            go_to_filter = (ImageView) view.findViewById(R.id.go_to_filter);
            go_to_filter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addFilter();
                }
            });
            results_all = (TextView) view.findViewById(R.id.results_all);
            header_content = (LinearLayout) view.findViewById(R.id.header_content);
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

//            listFAB = (FloatingActionButton) view.findViewById(R.id.map_fragment_list_fab);
            sale_header_name = (TextView) view.findViewById(R.id.sale_header_name);

/*
            if (fragment == null) {


            MapFragment mapFragment = (MapFragment)getActivity().getFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            /*if (fragment == null) {
>>>>>>> 4ecf3df750a74d68da740e0779ff673378ff1e4d
                fragment = SupportMapFragment.newInstance();
                fm.beginTransaction().replace(R.id.map, fragment).commit();
                fragment.getMapAsync(this);
            } else {
                fragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
                fragment.getMapAsync(this);
            }*/
            type = (String) getArguments().getSerializable(MAP_FRAGMENT_SALES_BUNDLE_TYPE_STRING);

            if (type.equalsIgnoreCase(GlobalVariables.TYPE_SHOP)) {
                shop = (Shop) getArguments().getSerializable(SHOP);
                productModel = (ProductModel) getArguments().getSerializable(MAP_FRAGMENT_SALES_PRODUCT_ID_STRING);
                salesDetails = (SalesDetailsModel) getArguments().getSerializable(SALES_DETAILS);
                if (shop != null) {
                    shop_name = shop.getName();
                    icon_name = shop_name;
                    lat = Double.parseDouble(shop.getLatitude());
                    lng = Double.parseDouble(shop.getLongitude());
                }
                if (productModel != null) {
                    Log.d(TAG,"PRODUCT"+productModel.getLatitude());
                    icon_name = productModel.getName();
                    shop_name = productModel.getName();
                    lat = Double.parseDouble(productModel.getLatitude());
                    lng = Double.parseDouble(productModel.getLongitude());
                }if(salesDetails!=null){
                    icon_name = salesDetails.getName();
                    shop_name = salesDetails.getName();
                    lat = Double.parseDouble(salesDetails.getLatitude());
                    lng = Double.parseDouble(salesDetails.getLongitude());
                }
            } else {
                Location loc = locServices.getLocation();
                if (loc != null) {
                    model.setLatitude(loc.getLatitude() + "");
                    model.setLongitude(loc.getLongitude() + "");
                    model.setRange(maxDistanceValue);
                    model.setAvailable(true);
                    model.setPickup(true);
                    model.setShipable(true);
                    model.setType(GlobalVariables.TYPE_GARAGE);
                    model.setPriceRange(minPriceValue + "," + maxPriceValue);
                    getLists(getContext(), model);
                    locServices.removeListener();
                }
            }
            MapFragment mapFragment =(MapFragment) getActivity().getFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

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

        return view;
    }

    @Override
    public void onResume() {
        MainActivity.setTitle(getString(R.string.garageSale), 0);
        super.onResume();
    }

    private void getLists(Context context, FilterModel filterModel) {
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
    float camera_focus_zoom = 12.0f;
    public void setUpMap() {
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (!checkPermission())
        {
            requestPermission();
        } else {
            map.setMyLocationEnabled(true);
            //  map.setTrafficEnabled(true);
            map.setIndoorEnabled(true);
//            map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            map.setBuildingsEnabled(true);
//            map.getUiSettings().setZoomControlsEnabled(true);
            map.setOnCameraChangeListener(OnCameraChangeListen);
            map.setOnMapClickListener(this);
           // locServices = new LocationService(activity);
           // lat=locServices.getLatitude();
           // lang=locServices.getLongitude();
          //  map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), camera_focus_zoom));
            if(from_activity || !type.equalsIgnoreCase(GlobalVariables.TYPE_SHOP)){
                map.getUiSettings().setZoomControlsEnabled(true);
            }
            if(type.equalsIgnoreCase(GlobalVariables.TYPE_SHOP)){
            addMarker();
            }else{
              setUpClusterer();
            }
        }
    }
    GoogleMap.OnCameraChangeListener OnCameraChangeListen = new GoogleMap.OnCameraChangeListener() {

        @Override
        public void onCameraChange(CameraPosition cameraPosition)
        {
            /*locationModel.setLatitude(Double.toString(cameraPosition.target.latitude));
            locationModel.setLongitude(Double.toString(cameraPosition.target.longitude));
            lat=cameraPosition.target.latitude;
            lang=cameraPosition.target.longitude;
            getAddressFromLatLng();*/
        }
    };
    final int PERMISSION_REQUEST_CODE = 2002;
    private void requestPermission()
    {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(getActivity(), "GPS permission allows us to access location data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();

        } else
        {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
            if (checkPermission()) {
                map.setMyLocationEnabled(true);
                //  map.setTrafficEnabled(true);
                map.setIndoorEnabled(true);
                map.setBuildingsEnabled(true);
                map.getUiSettings().setZoomControlsEnabled(true);
                map.setOnCameraChangeListener(OnCameraChangeListen);
                map.setOnMapClickListener(this);
                map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(marker.getPosition().latitude,
                                marker.getPosition().longitude));

                        return true;
                    }

                });
            } else {
                requestPermission();
            }
        }
    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {

            return false;

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        setUpMap();

        /*googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        *//*googleMap.setMyLocationEnabled(true);*//*
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
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
            setUpClusterer();*/
    }
    private void addMarker() {
        MarkerOptions options = new MarkerOptions();
        LatLng currentLatLng = new LatLng(lat,lng);
        options.position(currentLatLng);
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.atr));
        Marker mapMarker = map.addMarker(options);
        mapMarker.setAnchor(.5f,.5f);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,GlobalVariables.MAP_ZOOMING_INT));
        map.addCircle(new CircleOptions()
                .center(currentLatLng)
                .radius(6000)
                .strokeColor(getResources().getColor(R.color.fill_color))
                .fillColor(0x220000FF)
                .strokeWidth(3)
        );
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

    @Override
    public void onMapClick(LatLng latLng) {

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
       // Toast.makeText(context,""+model.getLatitude(),Toast.LENGTH_LONG).show();
        if(map!=null) {
            Log.d(TAG, "#######map--" + model.getLatitude());
            {
                drawCircle();
            }
        }
        LatLng currentLatLng = new LatLng(Double.parseDouble(model.getLatitude()),Double.parseDouble(model.getLongitude()));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,8));
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
                        Intent intent = ProductDetail.newInstance(activity,null,clickedClusterItem.getSalesModel(),GlobalVariables.TYPE_MY_SALE);
                        activity.startActivity(intent);
                    }
                });
                window.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = ProductDetail.newInstance(activity,null,clickedClusterItem.getSalesModel(),GlobalVariables.TYPE_MY_SALE);
                        activity.startActivity(intent);

                    }
                });
                if(clickedClusterItem!=null){
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

                }else{
                    System.out.println("The clicked cluster item was nulllll");
                }

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
