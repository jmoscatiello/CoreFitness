package com.polymorphicstudios.fragment;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PageAdapter_Tabs extends FragmentPagerAdapter {
	
	private List<Fragment> fragments;
	private String[] tabTitles;

	public PageAdapter_Tabs(FragmentManager fm, List<Fragment> fragments, String[] tabTitles) {
		super(fm);
		this.fragments = fragments;
		this.tabTitles = tabTitles;
	}
	
	public PageAdapter_Tabs(FragmentManager fm, List<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int position) {
		return this.fragments.get(position);
	}

	@Override
	public int getCount() {
		return this.fragments.size();
	}
	
	@Override
    public CharSequence getPageTitle(int position) {
		return tabTitles[position];
    }

	

}
