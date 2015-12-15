package net.ilexiconn.hipster.item.grade;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.TextView;
import net.ilexiconn.hipster.R;

public class ItemGradeHolder extends ViewHolder {
    public final TextView subject;
    public final TextView lastGrade;
    public final TextView averageGrade;

    public ItemGradeHolder(View itemView) {
        super(itemView);
        subject = (TextView) itemView.findViewById(R.id.subject_name);
        lastGrade = (TextView) itemView.findViewById(R.id.last_grade);
        averageGrade = (TextView) itemView.findViewById(R.id.average_grade);
    }
}
