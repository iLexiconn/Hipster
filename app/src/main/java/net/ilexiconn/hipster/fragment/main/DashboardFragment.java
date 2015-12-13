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
import net.ilexiconn.hipster.fragment.main.tabs.dashboard.AppointmentsTabFragment;
import net.ilexiconn.hipster.fragment.main.tabs.dashboard.RecentGradesTabFragment;
import net.ilexiconn.hipster.pager.HipsterPagerAdapter;
import net.ilexiconn.hipster.util.ConfigUtil;

public class DashboardFragment extends Fragment implements IFragment {
    private View view;
    private ITabFragment[] tabFragments = new ITabFragment[]{
            new AppointmentsTabFragment(),
            new RecentGradesTabFragment()
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TabLayout tabLayout;

        view = inflater.inflate(R.layout.fragment_main_dashboard, container, false);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.dashboard_view);
        viewPager.setAdapter(new HipsterPagerAdapter(this));

        tabLayout = (TabLayout) view.findViewById(R.id.dashboard_tabs);
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
