package net.ilexiconn.hipster.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import net.ilexiconn.hipster.fragment.Fragments;
import net.ilexiconn.hipster.fragment.IFragment;
import net.ilexiconn.hipster.fragment.ITabFragment;

public class HipsterPagerAdapter extends FragmentPagerAdapter {
    private String[] strings;
    private Fragment[] fragments;

    public HipsterPagerAdapter(IFragment fragment) {
        super(fragment.getFragment().getChildFragmentManager());
        Fragment[] tabFragments = fragment.getFragmentTabs();
        strings = new String[tabFragments.length];
        for (int i = 0; i < strings.length; i++) {
            if (tabFragments[i] instanceof ITabFragment) {
                strings[i] = fragment.getFragment().getActivity().getString(((ITabFragment) tabFragments[i]).getTitle());
            } else {
                strings[i] = Fragments.getFragmentFromInstance(fragment).name();
            }
        }
        fragments = new Fragment[tabFragments.length];
        System.arraycopy(tabFragments, 0, fragments, 0, fragments.length);
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