package com.codepath.apps.restclienttemplate.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by michaunp on 7/3/17.
 */

public class TweetsPagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[] {"Home", "Mentions"};
    private Context context;
    public Fragment homeTimelineFragment = new HomeTimelineFragment();
    public Fragment mentionsTimelineFragment = new MentionsTimelineFragment();
    public TweetsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }
    //return the total number of fragments
    @Override
    public int getCount() {
        return 2;
    }

    //return fragment to use depending on position

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return homeTimelineFragment;
        }
        else if (position == 1) {
            return mentionsTimelineFragment;
        }
        else {
            return null;
        }
    }

    //return title based on item position
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
