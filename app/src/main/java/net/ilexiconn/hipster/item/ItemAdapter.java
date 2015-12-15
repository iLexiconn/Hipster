package net.ilexiconn.hipster.item;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import net.ilexiconn.hipster.R;

import java.util.List;

public class ItemAdapter extends Adapter<ItemHolder> {
    public List<Item> itemList;

    public ItemAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        LinearLayout row = (LinearLayout) inflater.inflate(R.layout.item, parent, false);
        return new ItemHolder(row);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        Item row = itemList.get(position);
        holder.string2.setTextColor(Color.parseColor(row.color));
        holder.string1.setText(row.string1);
        holder.string2.setText(row.string2);
        holder.string3.setText(row.string3);
        holder.string4.setText(row.string4);
        if (row.special.isEmpty()) {
            holder.itemView.findViewById(R.id.special_layout).setVisibility(View.GONE);
        } else {
            holder.special.setText(row.special);
        }
        if (position == getItemCount() - 1) {
            holder.itemView.findViewById(R.id.item_line).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}