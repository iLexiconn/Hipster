package net.ilexiconn.hipster.fragment;

import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import net.ilexiconn.magister.Magister;

public interface ITabFragment {
    void onReload();

    void refresh(Magister magister);

    void setForcedRefresh(boolean forcedRefresh);

    boolean getForcedRefresh();

    Fragment getFragment();

    @StringRes
    int getTitle();
}
