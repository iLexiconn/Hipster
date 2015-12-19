package net.ilexiconn.hipster.thread.fragment.grades;

import android.support.design.widget.Snackbar;
import android.widget.LinearLayout;
import net.ilexiconn.hipster.MainActivity;
import net.ilexiconn.hipster.R;
import net.ilexiconn.hipster.fragment.main.tabs.grades.GradesTabFragment;
import net.ilexiconn.hipster.item.grade.ItemGrade;
import net.ilexiconn.hipster.item.grade.ItemGradeAdapter;
import net.ilexiconn.hipster.thread.fragment.FragmentThread;
import net.ilexiconn.magister.container.Grade;
import net.ilexiconn.magister.container.Subject;
import net.ilexiconn.magister.handler.GradeHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GradeThread extends FragmentThread<GradesTabFragment, Grade[]> {
    public GradeThread(GradesTabFragment gradesTabFragment) {
        super(gradesTabFragment);
    }

    @Override
    protected Grade[] onExecute() {
        GradeHandler gradeHandler = MainActivity.getMagister().getHandler(GradeHandler.class);
        try {
            return gradeHandler.getGrades(true, false, true);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Grade[] grades) {
        if (grades != null) {
            List<ItemGrade> itemList = new ArrayList<>();
            for (Grade grade : grades) {
                if (grade.grade == null) {
                    continue;
                }
                String subject = grade.subject.abbreviation;
                for (Subject s : MainActivity.getMagister().subjects) {
                    if (s.id == grade.subject.id) {
                        subject = s.description;
                    }
                }
                subject = subject.substring(0, 1).toUpperCase() + subject.substring(1);
                String lastGrade = "???";
                if (!grade.gradePeriod.gradePeriodName.equals("Eind") || grade.gradePeriod.gradePeriodName.equals("PTA")) {
                    continue; //???
                }
                String averageGrade = grade.grade;
                itemList.add(new ItemGrade(subject, lastGrade, averageGrade));
            }
            LinearLayout gradesLayout = (LinearLayout) getTabFragment().view.findViewById(R.id.grades_container);
            getTabFragment().populateLayout(gradesLayout, new ItemGradeAdapter(itemList));
        } else {
            Snackbar.make(getTabFragment().view, getActivity().getString(R.string.no_internet), Snackbar.LENGTH_LONG);
        }
        getTabFragment().swipeRefresh.setRefreshing(false);
    }
}
