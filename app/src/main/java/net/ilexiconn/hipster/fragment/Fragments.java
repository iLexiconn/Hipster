package net.ilexiconn.hipster.fragment;

import net.ilexiconn.hipster.R;
import net.ilexiconn.hipster.fragment.main.DashboardFragment;
import net.ilexiconn.hipster.fragment.main.GradesFragment;
import net.ilexiconn.hipster.fragment.main.SettingsFragment;
import net.ilexiconn.hipster.fragment.main.TimetableFragment;

public enum Fragments {
    DASHBOARD(R.id.nav_dashboard,  DashboardFragment.class, R.drawable.ic_class_black_24dp),
    GRADES(R.id.nav_grades, GradesFragment.class, R.drawable.ic_done_all_black_24dp),
    TIMETABLE(R.id.nav_timetable, TimetableFragment.class, R.drawable.ic_watch_later_black_24dp),
    SETTINGS(R.id.nav_settings, SettingsFragment.class, R.drawable.ic_settings_black_24dp);

    private int id;
    private Class fragment;
    private int icon;

    Fragments(int id, Class fragment, int icon) {
        this.id = id;
        this.fragment = fragment;
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public IFragment getFragment() {
        try {
            return (IFragment) fragment.newInstance();
        }catch (Exception e){
            return null;
        }
    }

    public int getIcon() {
        return icon;
    }

    public static Fragments getFragmentFromID(int id) {
        for (Fragments fragments : values()) {
            if (fragments.id == id) {
                return fragments;
            }
        }
        return null;
    }
}
