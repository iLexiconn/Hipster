package net.ilexiconn.hipster;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import net.ilexiconn.hipster.broadcast.HipsterBroadcastReceiver;
import net.ilexiconn.hipster.config.Config;
import net.ilexiconn.hipster.config.User;
import net.ilexiconn.hipster.fragment.Fragments;
import net.ilexiconn.hipster.fragment.ITabFragment;
import net.ilexiconn.hipster.thread.LoginThread;
import net.ilexiconn.hipster.thread.LogoutThread;
import net.ilexiconn.hipster.util.ColorUtil;
import net.ilexiconn.hipster.util.ConfigUtil;
import net.ilexiconn.hipster.util.IMatcher;
import net.ilexiconn.magister.Magister;

public class MainActivity extends AppCompatActivity {
    public Drawer drawer;
    public AccountHeader accountHeader;

    private static Magister magister;
    private static Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final Config config = ConfigUtil.loadConfig(this);

        final User currentUser = config.getCurrentUser();
        Log.i("HIPSTER", "LOADING MAGISTER");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            magister = bundle.getParcelable("magister");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        final Intent intent = new Intent(this, HipsterBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, AlarmManager.INTERVAL_FIFTEEN_MINUTES, AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);

        AccountHeaderBuilder accountHeaderBuilder = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withDividerBelowHeader(false)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, final IProfile profile, boolean current) {
                        if (profile.getIdentifier() == 1) {
                            LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
                            View dialogView = layoutInflater.inflate(R.layout.dialog_login, null);
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                            dialogBuilder.setTitle(R.string.login);
                            dialogBuilder.setView(dialogView);
                            dialogBuilder.setCancelable(false);

                            final EditText school = (EditText) dialogView.findViewById(R.id.input_school);
                            final EditText username = (EditText) dialogView.findViewById(R.id.input_username);
                            final EditText password = (EditText) dialogView.findViewById(R.id.input_password);

                            dialogBuilder.setPositiveButton(R.string.login, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    LoginThread loginThread = new LoginThread(MainActivity.this, school.getText().toString(), username.getText().toString(), password.getText().toString());
                                    loginThread.newAccount = true;
                                    loginThread.execute();
                                }
                            });

                            dialogBuilder.setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                            dialogBuilder.create().show();
                        } else if (profile.getIdentifier() == 2) {
                            if (isLoggedIn()) {
                                final Config config = ConfigUtil.loadConfig(MainActivity.this);
                                final User oldUser = config.getCurrentUser();
                                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.select_dialog_item);

                                for (User user : config.users) {
                                    if (user != oldUser) {
                                        arrayAdapter.add(user.nickname);
                                    }
                                }

                                if (!arrayAdapter.isEmpty()) {
                                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                                    dialogBuilder.setTitle(R.string.login);
                                    dialogBuilder.setCancelable(false);

                                    dialogBuilder.setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });

                                    dialogBuilder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, final int which) {
                                            if (which != -1) {
                                                User newUser = config.getUser(new IMatcher<User>() {
                                                    @Override
                                                    public boolean matches(User object) {
                                                        return object.nickname.equals(arrayAdapter.getItem(which));
                                                    }
                                                });
                                                config.currentUser = newUser.username;
                                                config.users.remove(oldUser);
                                                ConfigUtil.saveConfig(MainActivity.this, config);

                                                new LoginThread(MainActivity.this, newUser).execute();
                                            }
                                        }
                                    });

                                    dialogBuilder.show();
                                } else {
                                    config.users.clear();
                                    accountHeader.removeProfile(config.getProfileForUser(currentUser));
                                    config.currentUser = null;
                                    ConfigUtil.saveConfig(MainActivity.this, config);

                                    new LogoutThread(MainActivity.this).execute();
                                }

                                return true;
                            } else {
                                Snackbar.make(view, "Je moet eerst inloggen", Snackbar.LENGTH_LONG).show();
                                return false;
                            }
                        } else if (!current) {
                            User user = config.getUser(new IMatcher<User>() {
                                @Override
                                public boolean matches(User object) {
                                    return object.nickname.equals(profile.getName().getText());
                                }
                            });
                            config.currentUser = user.username;
                            ConfigUtil.saveConfig(MainActivity.this, config);

                            new LoginThread(MainActivity.this, user).execute();
                        }
                        return true;
                    }
                });

        config.initProfiles(this, accountHeaderBuilder);

        accountHeaderBuilder.addProfiles(new ProfileSettingDrawerItem().withName("Account toevoegen").withIcon(R.drawable.ic_add_black_24dp).withIconColorRes(R.color.textPrimary).withIdentifier(1));
        accountHeaderBuilder.addProfiles(new ProfileSettingDrawerItem().withName("Uitloggen").withIcon(R.drawable.ic_settings_black_24dp).withIconColorRes(R.color.textPrimary).withIdentifier(2));

        accountHeader = accountHeaderBuilder.build();
        accountHeader.setActiveProfile(config.getProfileForUser(currentUser));

        DrawerBuilder drawerBuilder = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggleAnimated(true)
                .withSavedInstance(savedInstanceState)
                .withAccountHeader(accountHeader, true)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        setFragment(Fragments.getFragmentFromItem(drawerItem));
                        drawer.closeDrawer();
                        return true;
                    }
                });

        for (Fragments fragments : Fragments.values()) {
            drawerBuilder.addDrawerItems(fragments.getDrawerItem());
        }

        drawer = drawerBuilder.build();

        String color = config.color;
        findViewById(R.id.toolbar).setBackgroundColor(Color.parseColor(color));
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ColorUtil.darker(Color.parseColor(color), 0.75f));
        }

        setFragment(Fragments.DASHBOARD);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState = drawer.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (drawer != null && drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    public void setFragment(Fragments fragment) {
        if (fragment == null) {
            return;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_main, (Fragment) fragment.getFragment());
        ImageView icon = (ImageView) findViewById(R.id.toolbar_icon);
        icon.setImageResource(fragment.getIcon());
        transaction.commit();
        for (Fragment tabFragment : fragment.getFragment().getFragmentTabs()) {
            if (tabFragment instanceof ITabFragment) {
                ((ITabFragment) tabFragment).onReload();
            }
        }
    }

    public void setMagister(Magister magister) {
        MainActivity.magister = magister;
    }

    public void setBitmap(Bitmap bitmap) {
        MainActivity.bitmap = bitmap;
    }

    public static boolean isLoggedIn() {
        return magister != null;
    }

    public static Magister getMagister() {
        return magister;
    }

    public static Bitmap getBitmap() {
        return bitmap;
    }
}
