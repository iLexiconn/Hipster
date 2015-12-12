package net.ilexiconn.hipster;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.*;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import net.ilexiconn.hipster.broadcast.HipsterBroadcastReceiver;
import net.ilexiconn.hipster.fragment.Fragments;
import net.ilexiconn.hipster.util.ColorUtil;
import net.ilexiconn.magister.Magister;
import net.ilexiconn.magister.ParcelableMagister;

import java.io.*;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ParcelableMagister magister;
    private SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        magister = getIntent().getParcelableExtra("magister");

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, HipsterBroadcastReceiver.class);
        intent.putExtra("magister", magister);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, AlarmManager.INTERVAL_FIFTEEN_MINUTES, AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }

        int color = preferences.getInt("color", -16738597);
        findViewById(R.id.toolbar).setBackgroundColor(color);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ColorUtil.darker(color, 0.75f));
        }

        setFragment(Fragments.DASHBOARD);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int color = preferences.getInt("color", -16738597);
        findViewById(R.id.menu_header).setBackgroundColor(color);
        new ImageThread().execute();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        setFragment(Fragments.getFragmentFromID(id));
        return true;
    }

    public void setFragment(Fragments fragment) {
        if (fragment == null) {
            return;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_main, fragment.getFragment());
        ImageView icon = (ImageView) findViewById(R.id.toolbar_icon);
        icon.setImageResource(fragment.getIcon());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        transaction.commit();
    }

    public Magister getMagister() {
        return magister;
    }

    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(0xff424242);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public class ImageThread extends AsyncTask<Void, Void, Bitmap> {
        @Override
        public Bitmap doInBackground(Void... params) {
            try {
                if (!new File(getFilesDir(), magister.profile.id + ".png").exists()) {
                    return (Bitmap) magister.getImage(200, 200, true).getImage();
                } else {
                    return null;
                }
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        public void onPostExecute(Bitmap bitmap) {
            Bitmap image;
            if (bitmap != null) {
                image = getCroppedBitmap(bitmap);
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(new File(getFilesDir(), magister.profile.id + ".png"));
                    image.compress(Bitmap.CompressFormat.PNG, 0, out);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                try {
                    image = BitmapFactory.decodeStream(new FileInputStream(new File(getFilesDir(), magister.profile.id + ".png")));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return;
                }
            }
            ImageView profilePicture = (ImageView) findViewById(R.id.profile_picture);
            profilePicture.setImageBitmap(image);
            TextView profileName = (TextView) findViewById(R.id.profile_name);
            profileName.setText(magister.profile.nickname);
        }
    }
}
