package net.ilexiconn.hipster.broadcast;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import net.ilexiconn.hipster.R;
import net.ilexiconn.hipster.notification.HipsterNotification;
import net.ilexiconn.magister.Magister;
import net.ilexiconn.magister.container.Grade;
import net.ilexiconn.magister.handler.GradeHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HipsterBroadcastReceiver extends BroadcastReceiver {
    private NotificationManager notificationManager;
    private Magister magister;
    private Context context;
    private PowerManager.WakeLock wakeLock;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "HIPSTER");
        wakeLock.acquire();

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        magister = intent.getParcelableExtra("magister");

        new CheckGradesThread().execute();
    }

    public class CheckGradesThread extends AsyncTask<Void, Void, Grade[]> {
        @Override
        public void onPreExecute() {
            Log.d("HIPSTER", "Checking for new grades");
        }

        @Override
        public Grade[] doInBackground(Void... params) {
            try {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(Calendar.MINUTE, -15);
                Date date = calendar.getTime();
                List<Grade> gradeList = new ArrayList<>();
                for (Grade grade : magister.getHandler(GradeHandler.class).getRecentGrades()) {
                    if (grade.filledInDate.after(date)) {
                        gradeList.add(grade);
                    }
                }
                if (gradeList.isEmpty()) {
                    return null;
                }
                return gradeList.toArray(new Grade[gradeList.size()]);
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        public void onPostExecute(Grade[] grades) {
            if (grades != null) {
                Log.d("HIPSTER", "Found new grades, notifying user");
                for (Grade grade : grades) {
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(HipsterBroadcastReceiver.this.context).setSmallIcon(R.drawable.ic_people_black_24dp).setContentTitle("Hipster").setContentText("Je hebt een " + grade.grade + " voor " + grade.course.name + " gekregen.");
                    notificationManager.notify(HipsterNotification.getUniqueID(), builder.build());
                }
            } else {
                Log.d("HIPSTER", "No new grades found");
            }
            wakeLock.release();
        }
    }
}
