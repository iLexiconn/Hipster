package net.ilexiconn.hipster.fragment;

import android.support.annotation.StringRes;

import net.ilexiconn.magister.Magister;

public interface ITabFragment {
    void onReload();

    void refresh(Magister magister);

    boolean getForcedRefresh();

    void setForcedRefresh(boolean forcedRefresh);

    @StringRes
    int getTitle();
}
