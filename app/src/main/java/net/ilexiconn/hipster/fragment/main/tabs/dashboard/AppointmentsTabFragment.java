package net.ilexiconn.hipster.fragment.main.tabs.dashboard;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import net.ilexiconn.hipster.MainActivity;
import net.ilexiconn.hipster.R;
import net.ilexiconn.hipster.item.Item;
import net.ilexiconn.hipster.item.ItemAdapter;
import net.ilexiconn.magister.container.Appointment;
import net.ilexiconn.magister.handler.AppointmentHandler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AppointmentsTabFragment extends Fragment {
    private SwipeRefreshLayout swipeRefresh;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.tab_dashboard_appointments, container, false);

            swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.tab_appointments);
            swipeRefresh.setColorSchemeResources(R.color.primary);
            swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refresh();
                }
            });

            refresh();
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

    public void refresh() {
        swipeRefresh.setRefreshing(true);
        new AppointmentThread().execute();
    }

    public class AppointmentThread extends AsyncTask<Void, Void, Appointment[]> {
        @Override
        public Appointment[] doInBackground(Void... params) {
            AppointmentHandler appointmentHandler = ((MainActivity) getActivity()).getMagister().getHandler(AppointmentHandler.class);
            try {
                return appointmentHandler.getAppointmentsOfToday();
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        public void onPostExecute(Appointment[] appointments) {
            if (appointments != null) {
                Date now = new Date();
                SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
                List<Item> itemList = new ArrayList<>();
                for (Appointment appointment : appointments) {
                    if (appointment.endDate.after(now)) {
                        String string1 = appointment.courses.length > 0 ? appointment.courses[0].name : "";
                        String string2 = appointment.location;
                        String string3 = appointment.teachers.length > 0 ? appointment.teachers[0].abbreviation : "";
                        String string4 = format.format(appointment.startDate) + " - " + format.format(appointment.endDate);
                        itemList.add(new Item(string1, string2, string3, string4));
                    }
                }
                if (itemList.isEmpty()) {
                    itemList.add(new Item(getString(R.string.no_appointments)));
                }
                LinearLayout todayLayout = (LinearLayout) view.findViewById(R.id.appointments_container);
                populateLayout(todayLayout, new ItemAdapter(itemList));
            } else {
                Snackbar.make(view, getString(R.string.no_internet), Snackbar.LENGTH_LONG);
            }
            swipeRefresh.setRefreshing(false);
        }
    }
}
