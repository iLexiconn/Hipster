package net.ilexiconn.hipster.fragment;

import android.support.v4.app.Fragment;
import net.ilexiconn.hipster.thread.LoginThread;

public abstract class TabFragment extends Fragment implements ITabFragment {
    private boolean forcedRefresh = false;

    @Override
    public void onReload() {
        if (getView() != null) {
            if (getForcedRefresh()) {
                setForcedRefresh(false);
                refresh(LoginThread.getMagister());
            }
        }
    }

    @Override
    public void setForcedRefresh(boolean forcedRefresh) {
        this.forcedRefresh = forcedRefresh;
    }

    @Override
    public boolean getForcedRefresh() {
        return forcedRefresh;
    }

    @Override
    public Fragment getFragment() {
        return this;
    }
}
