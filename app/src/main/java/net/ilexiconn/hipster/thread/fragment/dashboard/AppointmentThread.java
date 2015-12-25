package net.ilexiconn.hipster.thread.fragment.dashboard;

import android.support.design.widget.Snackbar;
import android.widget.LinearLayout;
import net.ilexiconn.hipster.MainActivity;
import net.ilexiconn.hipster.R;
import net.ilexiconn.hipster.fragment.main.tabs.dashboard.AppointmentsTabFragment;
import net.ilexiconn.hipster.item.Item;
import net.ilexiconn.hipster.item.ItemAdapter;
import net.ilexiconn.hipster.thread.fragment.FragmentThread;
import net.ilexiconn.magister.container.Appointment;
import net.ilexiconn.magister.handler.AppointmentHandler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AppointmentThread extends FragmentThread<AppointmentsTabFragment, Appointment[]> {
    public AppointmentThread(AppointmentsTabFragment appointmentsTabFragment) {
        super(appointmentsTabFragment);
    }

    @Override
    protected void onPreExecute() {
        getTabFragment().swipeRefresh.setRefreshing(true);
    }

    @Override
    protected Appointment[] onExecute() {
        AppointmentHandler appointmentHandler = MainActivity.getMagister().getHandler(AppointmentHandler.class);
        try {
            return appointmentHandler.getAppointmentsOfToday();
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Appointment[] appointments) {
        if (appointments != null) {
            Date now = new Date();
            SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
            List<Item> itemList = new ArrayList<>();
            for (Appointment appointment : appointments) {
                if (appointment.endDate.after(now)) {
                    String string1 = appointment.subjects.length > 0 ? appointment.subjects[0].name == null ? "???" : appointment.subjects[0].name : "???";
                    string1 = string1.substring(0, 1).toUpperCase() + string1.substring(1).toLowerCase();
                    String string2 = appointment.location;
                    String string3 = appointment.teachers.length > 0 ? appointment.teachers[0].abbreviation : "???";
                    String string4 = format.format(appointment.startDate) + " - " + format.format(appointment.endDate);
                    itemList.add(new Item(string1, string2, string3, string4));
                }
            }
            if (itemList.isEmpty()) {
                itemList.add(new Item(getActivity().getString(R.string.no_appointments)));
            }
            LinearLayout todayLayout = (LinearLayout) getTabFragment().view.findViewById(R.id.appointments_container);
            getTabFragment().populateLayout(todayLayout, new ItemAdapter(itemList));
        } else {
            Snackbar.make(getTabFragment().view, getActivity().getString(R.string.no_internet), Snackbar.LENGTH_LONG);
        }
        getTabFragment().swipeRefresh.setRefreshing(false);
    }
}
