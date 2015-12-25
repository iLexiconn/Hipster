package net.ilexiconn.hipster.fragment.main.tabs.timetable;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import net.ilexiconn.hipster.MainActivity;
import net.ilexiconn.hipster.R;
import net.ilexiconn.hipster.fragment.TabFragment;
import net.ilexiconn.hipster.item.Item;
import net.ilexiconn.hipster.item.ItemAdapter;
import net.ilexiconn.hipster.thread.fragment.timetable.TodayThread;
import net.ilexiconn.magister.Magister;

import java.util.ArrayList;
import java.util.Collections;

public class TodayTabFragment extends TabFragment {
    public SwipeRefreshLayout swipeRefresh;
    public View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            view = inflater.inflate(R.layout.tab_timetable_today, container, false);

            swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.tab_today);
            swipeRefresh.setColorSchemeResources(R.color.primary);
            swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refresh(MainActivity.getMagister());
                }
            });

            refresh(MainActivity.getMagister());
        }

        return view;
    }

    public void populateLayout(LinearLayout layout, RecyclerView.Adapter adapter) {
        layout.removeAllViewsInLayout();
        int count = adapter.getItemCount();

        for (int i = 0; i < count; i++) {
            RecyclerView.ViewHolder holder = adapter.onCreateViewHolder(layout, i);
            adapter.<RecyclerView.ViewHolder>onBindViewHolder(holder, i);

            layout.addView(holder.itemView);
        }
    }

    @Override
    public void refresh(Magister magister) {
        if (magister == null) {
            LinearLayout todayLayout = (LinearLayout) view.findViewById(R.id.today_container);
            populateLayout(todayLayout, new ItemAdapter(new ArrayList<>(Collections.singletonList(new Item(getString(R.string.logged_off))))));
        } else {
            new TodayThread(this).execute();
        }
    }

    @Override
    public int getTitle() {
        return R.string.today;
    }
}
