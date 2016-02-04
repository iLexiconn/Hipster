package net.ilexiconn.hipster.thread.fragment.dashboard;

import android.support.design.widget.Snackbar;
import android.widget.LinearLayout;

import net.ilexiconn.hipster.MainActivity;
import net.ilexiconn.hipster.R;
import net.ilexiconn.hipster.fragment.main.tabs.dashboard.RecentGradesTabFragment;
import net.ilexiconn.hipster.item.Item;
import net.ilexiconn.hipster.item.ItemAdapter;
import net.ilexiconn.hipster.thread.fragment.FragmentThread;
import net.ilexiconn.magister.container.Grade;
import net.ilexiconn.magister.handler.GradeHandler;

import org.ocpsoft.prettytime.PrettyTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GradeThread extends FragmentThread<RecentGradesTabFragment, Grade[]> {
    public GradeThread(RecentGradesTabFragment recentGradesTabFragment) {
        super(recentGradesTabFragment);
    }

    @Override
    protected void onPreExecute() {
        getTabFragment().swipeRefresh.setRefreshing(false);
    }

    @Override
    protected Grade[] onExecute() {
        GradeHandler gradeHandler = MainActivity.getMagister().getHandler(GradeHandler.class);
        try {
            return gradeHandler.getRecentGrades();
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Grade[] grades) {
        if (grades != null) {
            PrettyTime prettyTime = new PrettyTime(new Locale("nl"));
            List<Item> itemList = new ArrayList<>();
            for (Grade grade : grades) {
                String string1 = grade.subject.name;
                string1 = string1.substring(0, 1).toUpperCase() + string1.substring(1);
                String string2 = grade.grade;
                String string3 = grade.subject.abbreviation;
                String string4 = prettyTime.format(grade.filledInDate);
                boolean isSufficient = Float.parseFloat(string2.replaceAll(",", ".")) >= 5.5f;
                if (isSufficient) {
                    itemList.add(new Item(string1, string2, string3, string4));
                } else {
                    itemList.add(new Item(string1, string2, string3, string4, "#86FF0000"));
                }
            }
            if (itemList.isEmpty()) {
                itemList.add(new Item(getActivity().getString(R.string.no_appointments)));
            }
            LinearLayout gradesLayout = (LinearLayout) getTabFragment().view.findViewById(R.id.recent_grades_container);
            getTabFragment().populateLayout(gradesLayout, new ItemAdapter(itemList));
        } else {
            Snackbar.make(getTabFragment().view, getActivity().getString(R.string.no_internet), Snackbar.LENGTH_LONG);
        }
        getTabFragment().swipeRefresh.setRefreshing(false);
    }
}
