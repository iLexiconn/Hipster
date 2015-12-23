package net.ilexiconn.hipster.fragment.main;

import android.graphics.Color;
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
import net.ilexiconn.hipster.fragment.main.tabs.dashboard.AppointmentsTabFragment;
import net.ilexiconn.hipster.fragment.main.tabs.dashboard.RecentGradesTabFragment;
import net.ilexiconn.hipster.pager.HipsterPagerAdapter;
import net.ilexiconn.hipster.util.ConfigUtil;

public class DashboardFragment extends Fragment implements IFragment {
    private Fragment[] tabFragments = new Fragment[]{
            new AppointmentsTabFragment(),
            new RecentGradesTabFragment()
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_dashboard, container, false);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.dashboard_view);
        viewPager.setAdapter(new HipsterPagerAdapter(this));

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.dashboard_tabs);
        tabLayout.setupWithViewPager(viewPager);

        Config config = ConfigUtil.loadConfig(getActivity());
        String color = config.color;
        tabLayout.setBackgroundColor(Color.parseColor(color));
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor(color));

        return view;
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public Fragment[] getFragmentTabs() {
        return tabFragments;
    }
}
