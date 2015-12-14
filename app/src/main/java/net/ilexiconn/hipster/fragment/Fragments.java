package net.ilexiconn.hipster.fragment;

import net.ilexiconn.hipster.R;
import net.ilexiconn.hipster.fragment.main.DashboardFragment;
import net.ilexiconn.hipster.fragment.main.GradesFragment;
import net.ilexiconn.hipster.fragment.main.SettingsFragment;
import net.ilexiconn.hipster.fragment.main.TimetableFragment;

public enum Fragments {
    DASHBOARD(R.id.nav_dashboard, new DashboardFragment(), R.drawable.ic_class_black_24dp),
    GRADES(R.id.nav_grades, new GradesFragment(), R.drawable.ic_done_all_black_24dp),
    TIMETABLE(R.id.nav_timetable, new TimetableFragment(), R.drawable.ic_watch_later_black_24dp),
    SETTINGS(R.id.nav_settings, new SettingsFragment(), R.drawable.ic_settings_black_24dp);

    private int id;
    private IFragment fragment;
    private int icon;

    Fragments(int id, IFragment fragment, int icon) {
        this.id = id;
        this.fragment = fragment;
        this.icon = icon;
    }

    public static Fragments getFragmentFromID(int id) {
        for (Fragments fragments : values()) {
            if (fragments.id == id) {
                return fragments;
            }
        }
        return null;
    }

    public static int getIDFromFragment(IFragment fragment) {
        for (Fragments fragments : values()) {
            if (fragments.getFragment().equals(fragment)) {
                return fragments.getId();
            }
        }
        return DASHBOARD.getId();
    }

    public int getId() {
        return id;
    }

    public IFragment getFragment() {
        return fragment;
    }

    public int getIcon() {
        return icon;
    }
}
