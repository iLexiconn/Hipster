package net.ilexiconn.hipster.fragment.main;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mikepenz.aboutlibraries.LibsBuilder;
import net.ilexiconn.hipster.R;
import net.ilexiconn.hipster.fragment.IFragment;
import net.ilexiconn.hipster.pager.HipsterPagerAdapter;

public class AboutFragment extends Fragment implements IFragment {
    private Fragment[] tabFragments = new Fragment[]{
            new LibsBuilder()
                    .withAboutIconShown(true)
                    .withAboutVersionShown(true)
                    .withAboutDescription("Je minst favoriete Magister app.")
                    .withAboutAppName("Hipster")
                    .withExcludedLibraries("AboutLibraries")
                    .withLibraries("prettytime", "materialcolorpicker", "preferencefragment")
                    .supportFragment()
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_about, container, false);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.about_view);
        viewPager.setAdapter(new HipsterPagerAdapter(this));

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.about_tabs);
        tabLayout.setupWithViewPager(viewPager);

        /*Config config = ConfigUtil.loadConfig(getActivity());
        int color = config.color;
        tabLayout.setBackgroundColor(color);
        tabLayout.setSelectedTabIndicatorColor(color);*/

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
