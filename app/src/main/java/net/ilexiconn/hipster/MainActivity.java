package net.ilexiconn.hipster;

import android.os.Build;
import android.os.Bundle;
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
import net.ilexiconn.hipster.config.Config;
import net.ilexiconn.hipster.config.User;
import net.ilexiconn.hipster.fragment.Fragments;
import net.ilexiconn.hipster.util.ColorUtil;
import net.ilexiconn.hipster.util.ConfigUtil;

public class MainActivity extends AppCompatActivity {
    //private Magister magister;
    private Config config;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        config = ConfigUtil.loadConfig(this);

        User currentUser = config.getCurrentUser();
        if (currentUser != null) {
            //new LoginThread(this, currentUser);
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, HipsterBroadcastReceiver.class);
        intent.putExtra("magister", magister);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, AlarmManager.INTERVAL_FIFTEEN_MINUTES, AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    int id = item.getItemId();
                    setFragment(Fragments.getFragmentFromID(id));
                    return true;
                }
            });
        }

        int color = config.color;
        findViewById(R.id.toolbar).setBackgroundColor(color);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ColorUtil.darker(color, 0.75f));
        }

        setFragment(Fragments.DASHBOARD);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int color = config.color;
        findViewById(R.id.menu_header).setBackgroundColor(color);
        /*if (magister != null) {
            new DownloadImageThread(this, getMagister()).execute();
        }*/
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

    /*public Magister getMagister() {
        return magister;
    }*/
}
