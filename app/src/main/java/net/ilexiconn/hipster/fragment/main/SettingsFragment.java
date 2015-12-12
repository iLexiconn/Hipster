package net.ilexiconn.hipster.fragment.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.machinarius.preferencefragment.PreferenceFragment;
import net.ilexiconn.hipster.R;
import net.ilexiconn.hipster.pager.SettingsPagerAdapter;

public class SettingsFragment extends PreferenceFragment {
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TabLayout tabLayout;
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_main_settings, container, false);

            ViewPager viewPager = (ViewPager) view.findViewById(R.id.settings_view);
            viewPager.setAdapter(new SettingsPagerAdapter(getActivity().getSupportFragmentManager(), getActivity()));

            tabLayout = (TabLayout) view.findViewById(R.id.settings_tabs);
            tabLayout.setupWithViewPager(viewPager);
        } else {
            tabLayout = (TabLayout) view.findViewById(R.id.settings_tabs);
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int color = preferences.getInt("color", -16738597);
        tabLayout.setBackgroundColor(color);
        tabLayout.setSelectedTabIndicatorColor(color);

        return view;
    }
}
