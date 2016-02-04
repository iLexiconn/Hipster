package net.ilexiconn.hipster.thread.fragment;

import android.os.AsyncTask;
import android.support.annotation.MainThread;
import android.support.v4.app.FragmentActivity;

import net.ilexiconn.hipster.fragment.TabFragment;

public abstract class FragmentThread<TYPE extends TabFragment, RESULT> extends AsyncTask<Void, Void, RESULT> {
    private TYPE tabFragment;

    public FragmentThread(TYPE type) {
        this.tabFragment = type;
    }

    @Override
    protected RESULT doInBackground(Void... params) {
        return onExecute();
    }

    protected TYPE getTabFragment() {
        return tabFragment;
    }

    protected FragmentActivity getActivity() {
        return tabFragment.getActivity();
    }

    @Override
    @MainThread
    protected abstract void onPreExecute();

    protected abstract RESULT onExecute();

    @Override
    @MainThread
    protected abstract void onPostExecute(RESULT result);
}
