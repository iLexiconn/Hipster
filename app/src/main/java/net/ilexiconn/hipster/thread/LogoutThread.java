package net.ilexiconn.hipster.thread;

import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;

import net.ilexiconn.hipster.MainActivity;
import net.ilexiconn.hipster.R;
import net.ilexiconn.hipster.fragment.Fragments;
import net.ilexiconn.hipster.fragment.ITabFragment;

import java.io.IOException;

public class LogoutThread extends AsyncTask<Void, Void, Boolean> {
    public MainActivity activity;

    public String error;

    public LogoutThread(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        if (MainActivity.isLoggedIn()) {
            try {
                MainActivity.getMagister().logout();
                return true;
            } catch (IOException e) {
                error = activity.getString(R.string.no_internet);
                return false;
            }
        } else {
            error = activity.getString(R.string.unknown_error);
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean bool) {
        if (bool) {
            for (Fragments fragment : Fragments.values()) {
                for (Fragment tabFragment : fragment.getFragment().getFragmentTabs()) {
                    if (tabFragment instanceof ITabFragment) {
                        ((ITabFragment) tabFragment).setForcedRefresh(true);
                    }
                }
            }
            activity.setMagister(null);
            Snackbar.make(activity.getCurrentFocus(), "Uitgelogd", Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(activity.getCurrentFocus(), error, Snackbar.LENGTH_LONG).show();
        }
    }
}
