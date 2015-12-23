package net.ilexiconn.hipster.fragment.main;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.machinarius.preferencefragment.PreferenceFragment;
import net.ilexiconn.hipster.R;
import net.ilexiconn.hipster.config.Config;
import net.ilexiconn.hipster.fragment.IFragment;
import net.ilexiconn.hipster.fragment.main.tabs.settings.SettingsTabFragment;
import net.ilexiconn.hipster.pager.HipsterPagerAdapter;
import net.ilexiconn.hipster.util.ConfigUtil;

public class SettingsFragment extends PreferenceFragment implements IFragment {
    private Fragment[] tabFragments = new Fragment[]{
            new SettingsTabFragment()
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_settings, container, false);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.settings_view);
        viewPager.setAdapter(new HipsterPagerAdapter(this));

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.settings_tabs);
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
