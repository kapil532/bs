package com.langoor.app.blueshak.mylistings;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;
import com.langoor.app.blueshak.Messaging.fragment.BuyerFragment;
import com.langoor.app.blueshak.Messaging.fragment.NotificationsFragment;
import com.langoor.app.blueshak.Messaging.fragment.SellerFragment;
import com.langoor.app.blueshak.global.GlobalVariables;
import com.langoor.app.blueshak.home.GarageSalesListFragment;
import com.langoor.app.blueshak.home.ItemListFragment;
import com.langoor.app.blueshak.services.model.SalesListModel;
import com.langoor.app.blueshak.services.model.SalesListModelNew;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class MyListingPagerAdapter extends FragmentStatePagerAdapter {
    private final SparseArray<WeakReference<Fragment>> instantiatedFragments = new SparseArray<>();
    private ArrayList<String> mTabHeader;
    private BuyerFragment buyerFragment=new BuyerFragment();
    private SellerFragment sellerFragment=new SellerFragment();
    private NotificationsFragment notificationsFragment=new NotificationsFragment();

    public MyListingPagerAdapter(FragmentManager fm, ArrayList<String> tabHeader) {
        super(fm);
        this.mTabHeader = tabHeader;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new ItemMyListFragment();

            case 1:
                return GarageSalesListFragment.newInstance(new SalesListModelNew(),GlobalVariables.TYPE_GARAGE,null,null,true);
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
