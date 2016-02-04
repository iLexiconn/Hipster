package net.ilexiconn.hipster.item.grade;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import net.ilexiconn.hipster.R;

import java.util.List;

public class ItemGradeAdapter extends Adapter<ItemGradeHolder> {
    public List<ItemGrade> itemList;

    public ItemGradeAdapter(List<ItemGrade> itemList) {
        this.itemList = itemList;
    }

    @Override
    public ItemGradeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.item_grade, parent, false);
        return new ItemGradeHolder(linearLayout);
    }

    @Override
    public void onBindViewHolder(ItemGradeHolder holder, int position) {
        ItemGrade item = itemList.get(position);
        holder.subject.setText(item.subject);
        holder.lastGrade.setText(item.lastGrade);
        try {
            if (Float.parseFloat(item.lastGrade.replaceAll(",", ".")) < 5.5f) {
                holder.lastGrade.setTextColor(Color.parseColor("#86FF0000"));
            }
        } catch (NumberFormatException e) {
            if (item.lastGrade.equals("O")) {
                holder.lastGrade.setTextColor(Color.parseColor("#86FF0000"));
            }
        }
        holder.averageGrade.setText(item.averageGrade);
        try {
            if (Float.parseFloat(item.averageGrade.replaceAll(",", ".")) < 5.5f) {
                holder.averageGrade.setBackgroundColor(Color.parseColor("#86FF0000"));
            }
        } catch (NumberFormatException e) {
            if (item.averageGrade.equals("O")) {
                holder.averageGrade.setBackgroundColor(Color.parseColor("#86FF0000"));
            }
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
