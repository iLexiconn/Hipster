package net.ilexiconn.hipster.item;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import net.ilexiconn.hipster.R;

public class ItemHolder extends ViewHolder {
    public TextView string1;
    public TextView string2;
    public TextView string3;
    public TextView string4;
    public TextView special;

    public ItemHolder(LinearLayout row) {
        super(row);
        string1 = (TextView) row.findViewById(R.id.string1);
        string2 = (TextView) row.findViewById(R.id.string2);
        string3 = (TextView) row.findViewById(R.id.string3);
        string4 = (TextView) row.findViewById(R.id.string4);
        special = (TextView) row.findViewById(R.id.special);
    }

    public void removeSpecial() {
        itemView.findViewById(R.id.special_layout).setVisibility(View.GONE);
    }
}
