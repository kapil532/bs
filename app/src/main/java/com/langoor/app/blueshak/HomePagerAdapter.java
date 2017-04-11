package com.langoor.app.blueshak;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;
import com.langoor.app.blueshak.garage.CreateGarageSaleFragment;
import com.langoor.app.blueshak.garage.CreateItemSaleFragment;
import com.langoor.app.blueshak.global.GlobalVariables;
import com.langoor.app.blueshak.home.GarageSalesListFragment;
import com.langoor.app.blueshak.home.ItemListFragment;
import com.langoor.app.blueshak.services.model.CreateSalesModel;
import com.langoor.app.blueshak.services.model.FilterModel;
import com.langoor.app.blueshak.services.model.LocationModel;
import com.langoor.app.blueshak.services.model.SalesListModel;
import com.langoor.app.blueshak.services.model.SalesListModelNew;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class HomePagerAdapter extends FragmentStatePagerAdapter {
    private final SparseArray<WeakReference<Fragment>> instantiatedFragments = new SparseArray<>();
    private ArrayList<String> mTabHeader;
    Context context;
    CreateSalesModel createSalesModel=null;
    private SalesListModelNew salesListModel = new SalesListModelNew();
    public HomePagerAdapter(FragmentManager fm, ArrayList<String> tabHeader, Context context) {
        super(fm);
        this.mTabHeader = tabHeader;
        this.context=context;
        this.createSalesModel=createSalesModel;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                // return new ItemListFragment();
                return ItemListFragment.newInstance(new SalesListModel(), GlobalVariables.TYPE_MULTIPLE_ITEMS,null,null);

            case 1:
                   /* return new GarageSalesListFragment().newInstance(salesListModel, type);*/
                return GarageSalesListFragment.newInstance(salesListModel, GlobalVariables.TYPE_GARAGE,new
                        FilterModel(),new LocationModel(),false);



            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mTabHeader.size();
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final Fragment fragment = (Fragment) super.instantiateItem(container, position);
        instantiatedFragments.put(position, new WeakReference<>(fragment));
        return fragment;
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        instantiatedFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    @Nullable
    public Fragment getFragment(final int position) {
        final WeakReference<Fragment> wr = instantiatedFragments.get(position);
        if (wr != null) {
            return wr.get();
        } else {
            return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabHeader.get(position);
    }
}
