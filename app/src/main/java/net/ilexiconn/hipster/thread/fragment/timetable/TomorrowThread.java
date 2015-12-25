package net.ilexiconn.hipster.thread.fragment.timetable;

import android.support.design.widget.Snackbar;
import android.widget.LinearLayout;
import net.ilexiconn.hipster.MainActivity;
import net.ilexiconn.hipster.R;
import net.ilexiconn.hipster.fragment.main.tabs.timetable.TomorrowTabFragment;
import net.ilexiconn.hipster.item.Item;
import net.ilexiconn.hipster.item.ItemAdapter;
import net.ilexiconn.hipster.thread.fragment.FragmentThread;
import net.ilexiconn.magister.container.Appointment;
import net.ilexiconn.magister.handler.AppointmentHandler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TomorrowThread extends FragmentThread<TomorrowTabFragment, Appointment[]> {
    public TomorrowThread(TomorrowTabFragment tomorrowTabFragment) {
        super(tomorrowTabFragment);
    }

    @Override
    protected void onPreExecute() {
        getTabFragment().swipeRefresh.setRefreshing(false);
    }

    @Override
    protected Appointment[] onExecute() {
        AppointmentHandler appointmentHandler = MainActivity.getMagister().getHandler(AppointmentHandler.class);
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
    protected void onPostExecute(Appointment[] appointments) {
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
                itemList.add(new Item(getActivity().getString(R.string.no_appointments)));
            }
            LinearLayout todayLayout = (LinearLayout) getTabFragment().view.findViewById(R.id.tomorrow_container);
            getTabFragment().populateLayout(todayLayout, new ItemAdapter(itemList));
        } else {
            Snackbar.make(getTabFragment().view, getActivity().getString(R.string.no_internet), Snackbar.LENGTH_LONG);
        }
        getTabFragment().swipeRefresh.setRefreshing(false);
    }
}
