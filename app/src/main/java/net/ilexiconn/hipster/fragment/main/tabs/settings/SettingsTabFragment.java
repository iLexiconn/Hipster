package net.ilexiconn.hipster.fragment.main.tabs.settings;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import com.github.machinarius.preferencefragment.PreferenceFragment;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import net.ilexiconn.hipster.R;
import net.ilexiconn.hipster.config.Config;
import net.ilexiconn.hipster.config.User;
import net.ilexiconn.hipster.thread.LoginThread;
import net.ilexiconn.hipster.util.ColorUtil;
import net.ilexiconn.hipster.util.ConfigUtil;
import net.ilexiconn.hipster.util.IMatcher;

public class SettingsTabFragment extends PreferenceFragment {
    private View view;
    private Config config;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);
        Preference logout = findPreference(getString(R.string.logout));
        logout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //logout
                return true;
            }
        });

        Preference setAccount = findPreference(getString(R.string.set_account));
        setAccount.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_item);

                for (User user : config.users) {
                    arrayAdapter.add(user.nickname);
                }

                builderSingle.setNegativeButton("terug", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, final int which) {
                        if (which != -1) {
                            User user = config.getUser(new IMatcher<User>() {
                                @Override
                                public boolean matches(User object) {
                                    return object.nickname.equals(arrayAdapter.getItem(which));
                                }
                            });
                            config.currentUser = user.username;
                            ConfigUtil.saveConfig(getActivity(), config);

                            new LoginThread(getActivity(), user);
                        }
                    }
                });

                builderSingle.show();

                return true;
            }
        });

        Preference addAccount = findPreference(getString(R.string.add_account));
        addAccount.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                View dialogView = layoutInflater.inflate(R.layout.dialog_login, null);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                dialogBuilder.setView(dialogView);
                dialogBuilder.setCancelable(false);

                final EditText school = (EditText) dialogView.findViewById(R.id.input_school);
                final EditText username = (EditText) dialogView.findViewById(R.id.input_username);
                final EditText password = (EditText) dialogView.findViewById(R.id.input_password);

                dialogBuilder.setPositiveButton("login", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new LoginThread(getActivity(), school.getText().toString(), username.getText().toString(), password.getText().toString());
                    }
                });

                dialogBuilder.setNegativeButton("terug", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                dialogBuilder.create().show();

                return true;
            }
        });

        Preference color = findPreference(getString(R.string.color));
        color.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                int color = config.color;
                int r = (color >> 16) & 0xFF;
                int g = (color >> 8) & 0xFF;
                int b = (color) & 0xFF;
                final ColorPicker colorPicker = new ColorPicker(getActivity(), r, g, b);
                colorPicker.show();
                Button close = (Button) colorPicker.findViewById(R.id.okColorButton);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int selectedColorRGB = colorPicker.getColor();

                        getActivity().findViewById(R.id.toolbar).setBackgroundColor(selectedColorRGB);
                        getActivity().findViewById(R.id.menu_header).setBackgroundColor(selectedColorRGB);
                        TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.settings_tabs);
                        tabLayout.setBackgroundColor(selectedColorRGB);
                        tabLayout.setSelectedTabIndicatorColor(selectedColorRGB);

                        if (Build.VERSION.SDK_INT >= 21) {
                            getActivity().getWindow().setStatusBarColor(ColorUtil.darker(selectedColorRGB, 0.75f));
                        }

                        config.color = selectedColorRGB;
                        ConfigUtil.saveConfig(getActivity(), config);

                        colorPicker.dismiss();
                    }
                });
                return true;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.tab_settings_settings, container, false);

            config = ConfigUtil.loadConfig(getActivity());
        }

        return view;
    }
}
