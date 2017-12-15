package org.levraievangile.View.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Maranatha on 15/12/2017.
 */

public class DownloadPagerAdapter  extends FragmentPagerAdapter {

    private List<Fragment> fragments;
    private List<String> titres;

    public DownloadPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public DownloadPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    public DownloadPagerAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titres) {
        super(fm);
        this.fragments = fragments;
        this.titres = titres;
    }

    @Override
    public Fragment getItem(int arg0) {
        return this.fragments.get(arg0);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(titres != null && titres.size() > 0)
            return titres.get(position);
        else
            return super.getPageTitle(position);
    }
}