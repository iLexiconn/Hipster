package net.ilexiconn.hipster.fragment.main.tabs.settings;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.github.machinarius.preferencefragment.PreferenceFragment;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import net.ilexiconn.hipster.LoginActivity;
import net.ilexiconn.hipster.MainActivity;
import net.ilexiconn.hipster.R;
import net.ilexiconn.hipster.config.Config;
import net.ilexiconn.hipster.util.ColorUtil;
import net.ilexiconn.hipster.util.ConfigUtil;

import java.io.IOException;

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
                new LogoutThread().execute();
                config.currentUser = null;
                ConfigUtil.saveConfig(getActivity(), config);
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
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

    public class LogoutThread extends AsyncTask<Void, Void, Void> {
        @Override
        public Void doInBackground(Void... params) {
            try {
                ((MainActivity) getActivity()).getMagister().logout();
            } catch (IOException e) {

            }
            return null;
        }
    }
}
