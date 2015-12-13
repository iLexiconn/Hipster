package net.ilexiconn.hipster.thread;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import net.ilexiconn.hipster.R;
import net.ilexiconn.hipster.config.Config;
import net.ilexiconn.hipster.config.User;
import net.ilexiconn.hipster.fragment.Fragments;
import net.ilexiconn.hipster.fragment.IFragment;
import net.ilexiconn.hipster.fragment.ITabFragment;
import net.ilexiconn.hipster.util.ConfigUtil;
import net.ilexiconn.magister.Magister;
import net.ilexiconn.magister.container.School;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.text.ParseException;

public class LoginThread extends AsyncTask<Void, Void, Magister> {
    private static boolean loggedIn = false;
    private static Magister magister;

    public FragmentActivity activity;
    public String school;
    public String username;
    public String password;

    public Config config;
    public String error;

    public ProgressDialog dialog;

    public LoginThread(FragmentActivity activity, String school, String username, String password) {
        this.activity = activity;
        this.school = school;
        this.username = username;
        this.password = password;

        this.config = ConfigUtil.loadConfig(activity);
    }

    public LoginThread(FragmentActivity activity, User user) {
        this(activity, user.school, user.username, user.password);
    }

    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(activity, activity.getString(R.string.app_name), "Logging in...", true);
    }

    @Override
    protected Magister doInBackground(Void... params) {
        try {
            return Magister.login(School.findSchool(school.replaceAll(" ", "%20"))[0], username, password);
        } catch (IOException e) {
            Log.e("HIPSTER", "Unable to login", e);
            error = activity.getString(R.string.no_internet);
            return null;
        } catch (ParseException e) {
            Log.e("HIPSTER", "Unable to login", e);
            error = activity.getString(R.string.unknown_error);
            return null;
        } catch (InvalidParameterException e) {
            Log.e("HIPSTER", "Invalid credentials", e);
            error = activity.getString(R.string.invalid_password);
            return null;
        }
    }

    @Override
    protected void onPostExecute(Magister magister) {
        if (magister != null) {
            User user;
            if (config.getUser(magister.user.username) == null) {
                user = new User(magister.school.name, magister.user.username, magister.user.password, magister.profile.nickname);
                config.users.add(user);
            } else {
                user = config.getUser(magister.user.username);
            }
            config.currentUser = user.username;
            ConfigUtil.saveConfig(activity, config);
            loggedIn = true;
            LoginThread.magister = magister;
            new DownloadImageThread(activity).execute();
            Fragment currentFragment = activity.getSupportFragmentManager().findFragmentById(R.id.fragment_container_main);
            if (currentFragment instanceof IFragment) {
                IFragment fragment = (IFragment) currentFragment;
                for (ITabFragment tabFragment : fragment.getFragmentTabs()) {
                    tabFragment.refresh(magister);
                }
            }
            for (Fragments fragment : Fragments.values()) {
                if (fragment.getFragment() == currentFragment) {
                    continue;
                }
                for (ITabFragment fragmentTab : fragment.getFragment().getFragmentTabs()) {
                    fragmentTab.setForcedRefresh(true);
                }
            }
        } else {
            Log.e("HIPSTER", error);
            Snackbar.make(activity.getCurrentFocus(), error, Snackbar.LENGTH_LONG).show();
        }
        dialog.dismiss();
    }

    public static boolean isLoggedIn() {
        return loggedIn;
    }

    public static Magister getMagister() {
        return magister;
    }
}
