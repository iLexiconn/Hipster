package net.ilexiconn.hipster.fragment;

import android.app.Fragment;
import net.ilexiconn.hipster.R;

public enum Fragments {
    DASHBOARD(R.id.nav_dashboard, new DashboardFragment(), R.drawable.ic_class_black_24dp),
    GRADES(R.id.nav_grades, new GradesFragment(), R.drawable.ic_done_all_black_24dp),
    TIMETABLE(R.id.nav_timetable, new TimetableFragment(), R.drawable.ic_watch_later_black_24dp),
    SETTINGS(R.id.nav_settings, new SettingsFragment(), R.drawable.ic_settings_black_24dp);

    private int id;
    private Fragment fragment;
    private int img;

    Fragments(int id, Fragment fragment, int img) {
        this.id = id;
        this.fragment = fragment;
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public int getImg() {
        return img;
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
