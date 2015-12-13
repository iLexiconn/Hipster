package net.ilexiconn.hipster.fragment.main.tabs.dashboard;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import net.ilexiconn.hipster.R;
import net.ilexiconn.hipster.fragment.TabFragment;
import net.ilexiconn.hipster.item.Item;
import net.ilexiconn.hipster.item.ItemAdapter;
import net.ilexiconn.hipster.thread.LoginThread;
import net.ilexiconn.magister.Magister;
import net.ilexiconn.magister.container.Grade;
import net.ilexiconn.magister.handler.GradeHandler;
import org.ocpsoft.prettytime.PrettyTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class RecentGradesTabFragment extends TabFragment {
    private SwipeRefreshLayout swipeRefresh;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.tab_dashboard_recent_grades, container, false);

            swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.tab_recent_grades);
            swipeRefresh.setColorSchemeResources(R.color.primary);
            swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refresh(LoginThread.getMagister());
                }
            });

            refresh(LoginThread.getMagister());
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
            LinearLayout todayLayout = (LinearLayout) view.findViewById(R.id.recent_grades_container);
            populateLayout(todayLayout, new ItemAdapter(new ArrayList<>(Collections.singletonList(new Item(getString(R.string.logged_off))))));
            return;
        }
        swipeRefresh.setRefreshing(true);
        new GradeThread().execute();
    }

    @Override
    public int getTitle() {
        return R.string.recent_grades;
    }

    public class GradeThread extends AsyncTask<Void, Void, Grade[]> {
        @Override
        public Grade[] doInBackground(Void... params) {
            GradeHandler gradeHandler = LoginThread.getMagister().getHandler(GradeHandler.class);
            try {
                return gradeHandler.getRecentGrades();
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        public void onPostExecute(Grade[] grades) {
            if (grades != null) {
                PrettyTime prettyTime = new PrettyTime(new Locale("nl"));
                List<Item> itemList = new ArrayList<>();
                for (Grade grade : grades) {
                    String string1 = grade.subject.name;
                    string1 = string1.substring(0, 1).toUpperCase() + string1.substring(1).toLowerCase();
                    String string2 = grade.grade == null ? "???" : grade.grade;
                    String string3 = grade.filledInBy == null ? "???" : grade.filledInBy;
                    String string4 = prettyTime.format(grade.filledInDate);
                    itemList.add(new Item(string1, string2, string3, string4));
                }
                if (itemList.isEmpty()) {
                    itemList.add(new Item(getString(R.string.no_appointments)));
                }
                LinearLayout gradesLayout = (LinearLayout) view.findViewById(R.id.recent_grades_container);
                populateLayout(gradesLayout, new ItemAdapter(itemList));
            } else {
                Snackbar.make(view, getString(R.string.no_internet), Snackbar.LENGTH_LONG);
            }
            swipeRefresh.setRefreshing(false);
        }
    }
}
