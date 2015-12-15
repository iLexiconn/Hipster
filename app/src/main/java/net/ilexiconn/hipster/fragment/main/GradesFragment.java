package net.ilexiconn.hipster.fragment.main;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import net.ilexiconn.hipster.R;
import net.ilexiconn.hipster.config.Config;
import net.ilexiconn.hipster.fragment.IFragment;
import net.ilexiconn.hipster.fragment.ITabFragment;
import net.ilexiconn.hipster.fragment.main.tabs.grades.GradesTabFragment;
import net.ilexiconn.hipster.pager.HipsterPagerAdapter;
import net.ilexiconn.hipster.util.ConfigUtil;

public class GradesFragment extends Fragment implements IFragment {
    private ITabFragment[] tabFragments = new ITabFragment[]{
            new GradesTabFragment()
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_grades, container, false);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.grades_view);
        viewPager.setAdapter(new HipsterPagerAdapter(this));

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.grades_tabs);
        tabLayout.setupWithViewPager(viewPager);

        Config config = ConfigUtil.loadConfig(getActivity());
        int color = config.color;
        tabLayout.setBackgroundColor(color);
        tabLayout.setSelectedTabIndicatorColor(color);

        return view;
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public ITabFragment[] getFragmentTabs() {
        return tabFragments;
    }
}
