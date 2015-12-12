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
import net.ilexiconn.hipster.pager.GradesPagerAdapter;
import net.ilexiconn.hipster.util.ConfigUtil;

public class GradesFragment extends Fragment {
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TabLayout tabLayout;
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_main_grades, container, false);

            ViewPager viewPager = (ViewPager) view.findViewById(R.id.grades_view);
            viewPager.setAdapter(new GradesPagerAdapter(getActivity().getSupportFragmentManager(), getActivity()));

            tabLayout = (TabLayout) view.findViewById(R.id.grades_tabs);
            tabLayout.setupWithViewPager(viewPager);
        } else {
            tabLayout = (TabLayout) view.findViewById(R.id.grades_tabs);
        }

        Config config = ConfigUtil.loadConfig(getActivity());
        int color = config.color;
        tabLayout.setBackgroundColor(color);
        tabLayout.setSelectedTabIndicatorColor(color);

        return view;
    }
}
