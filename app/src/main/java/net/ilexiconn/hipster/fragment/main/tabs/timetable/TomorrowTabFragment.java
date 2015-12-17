package net.ilexiconn.hipster.fragment.main.tabs.timetable;

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
import net.ilexiconn.magister.container.Appointment;
import net.ilexiconn.magister.handler.AppointmentHandler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TomorrowTabFragment extends TabFragment {
    private SwipeRefreshLayout swipeRefresh;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            view = inflater.inflate(R.layout.tab_timetable_tomorrow, container, false);

            swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.tab_tomorrow);
            swipeRefresh.setColorSchemeResources(R.color.primary);
            swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (LoginThread.isLoggedIn()) {
                        refresh(LoginThread.getMagister());
                    }
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
            LinearLayout todayLayout = (LinearLayout) view.findViewById(R.id.tomorrow_container);
            populateLayout(todayLayout, new ItemAdapter(new ArrayList<>(Collections.singletonList(new Item(getString(R.string.logged_off))))));
            return;
        }
        swipeRefresh.setRefreshing(true);
        new AppointmentThread().execute();
    }

    @Override
    public int getTitle() {
        return R.string.tomorrow;
    }

    public class AppointmentThread extends AsyncTask<Void, Void, Appointment[]> {
        @Override
        public Appointment[] doInBackground(Void... params) {
            AppointmentHandler appointmentHandler = LoginThread.getMagister().getHandler(AppointmentHandler.class);
            Calendar date = new GregorianCalendar();
            date.set(Calendar.HOUR_OF_DAY, 0);
            date.set(Calendar.MINUTE, 0);
            date.set(Calendar.SECOND, 0);
            date.set(Calendar.MILLISECOND, 0);
            date.add(Calendar.DAY_OF_WEEK, 1);
            Date tomorrow = date.getTime();

            try {
                return appointmentHandler.getAppointments(tomorrow, tomorrow);
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        public void onPostExecute(Appointment[] appointments) {
            if (appointments != null) {
                SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
                List<Item> itemList = new ArrayList<>();
                for (Appointment appointment : appointments) {
                    String string1 = null;
                    if (appointment.subjects.length > 0) {
                        string1 = appointment.subjects[0].name;
                    }
                    if (string1 != null) {
                        string1 = string1.substring(0, 1).toUpperCase() + string1.substring(1);
                    }
                    String string2 = appointment.location;
                    String string3 = appointment.teachers.length > 0 ? appointment.teachers[0].abbreviation : null;
                    String string4 = format.format(appointment.startDate) + " - " + format.format(appointment.endDate);
                    itemList.add(new Item(string1, string2, string3, string4));
                }
                if (itemList.isEmpty()) {
                    itemList.add(new Item(getString(R.string.no_appointments)));
                }
                LinearLayout todayLayout = (LinearLayout) view.findViewById(R.id.tomorrow_container);
                populateLayout(todayLayout, new ItemAdapter(itemList));
            } else {
                Snackbar.make(view, getString(R.string.no_internet), Snackbar.LENGTH_LONG);
            }
            swipeRefresh.setRefreshing(false);
        }
    }
}