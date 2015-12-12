package net.ilexiconn.hipster.pager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import net.ilexiconn.hipster.R;
import net.ilexiconn.hipster.fragment.main.tabs.timetable.TimetableTabFragment;

public class TimetablePagerAdapter extends FragmentPagerAdapter {
    private String[] strings;
    private Fragment[] fragments;

    public TimetablePagerAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        strings = new String[] {context.getString(R.string.timetable)};
        fragments = new Fragment[] {new TimetableTabFragment()};
    }

    @Override
    public int getCount() {
        return strings.length;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return strings[position];
    }
}