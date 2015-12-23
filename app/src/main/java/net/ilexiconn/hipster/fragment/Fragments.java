package net.ilexiconn.hipster.fragment;

import android.support.annotation.StringRes;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import net.ilexiconn.hipster.R;
import net.ilexiconn.hipster.fragment.main.*;

public enum Fragments {
    DASHBOARD(R.string.dashboard, new DashboardFragment(), R.drawable.ic_class_black_24dp),
    GRADES(R.string.grades, new GradesFragment(), R.drawable.ic_done_all_black_24dp),
    TIMETABLE(R.string.timetable, new TimetableFragment(), R.drawable.ic_watch_later_black_24dp),
    SETTINGS(R.string.settings, new SettingsFragment(), R.drawable.ic_settings_black_24dp),
    ABOUT(R.string.about, new AboutFragment(), R.drawable.ic_build_black_24dp);

    private IDrawerItem<?> drawerItem;
    private IFragment fragment;
    private int icon;

    Fragments(@StringRes int name, IFragment fragment, int icon) {
        this.drawerItem = new PrimaryDrawerItem().withName(name).withIcon(icon).withIconTintingEnabled(true);
        this.fragment = fragment;
        this.icon = icon;
    }

    public static Fragments getFragmentFromItem(IDrawerItem<?> drawerItem) {
        for (Fragments fragments : values()) {
            if (fragments.drawerItem == drawerItem) {
                return fragments;
            }
        }
        return null;
    }

    public static IDrawerItem<?> getItemFromFragment(IFragment fragment) {
        for (Fragments fragments : values()) {
            if (fragments.getFragment().equals(fragment)) {
                return fragments.getDrawerItem();
            }
        }
        return DASHBOARD.getDrawerItem();
    }

    public static Fragments getFragmentFromInstance(IFragment fragment) {
        for (Fragments fragments : values()) {
            if (fragments.getFragment().equals(fragment)) {
                return fragments;
            }
        }
        return null;
    }

    public IDrawerItem<?> getDrawerItem() {
        return drawerItem;
    }

    public IFragment getFragment() {
        return fragment;
    }

    public int getIcon() {
        return icon;
    }
}
