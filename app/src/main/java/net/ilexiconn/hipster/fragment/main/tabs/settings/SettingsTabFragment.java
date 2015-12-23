package net.ilexiconn.hipster.fragment.main.tabs.settings;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.github.machinarius.preferencefragment.PreferenceFragment;
import net.ilexiconn.hipster.MainActivity;
import net.ilexiconn.hipster.R;
import net.ilexiconn.hipster.config.Config;
import net.ilexiconn.hipster.fragment.ITabFragment;
import net.ilexiconn.hipster.util.ConfigUtil;
import net.ilexiconn.magister.Magister;

public class SettingsTabFragment extends PreferenceFragment implements ITabFragment {
    public View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            addPreferencesFromResource(R.xml.settings);
        }

        Config config = ConfigUtil.loadConfig(getActivity());

        CheckBoxPreference toolbarAvatar = (CheckBoxPreference) findPreference(getString(R.string.toolbar_avatar));
        toolbarAvatar.setChecked(config.toolbarAvatar);
        toolbarAvatar.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Config config = ConfigUtil.loadConfig(getActivity());
                config.toolbarAvatar = !config.toolbarAvatar;
                ConfigUtil.saveConfig(getActivity(), config);
                ImageView toolbarPicture = (ImageView) getActivity().findViewById(R.id.toolbar_avatar);
                if (config.toolbarAvatar) {
                    toolbarPicture.setImageBitmap(MainActivity.getBitmap());
                } else {
                    toolbarPicture.setImageBitmap(null);
                }
                return true;
            }
        });

        /*Preference color = findPreference(getString(R.string.color));
        color.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                final Config config = ConfigUtil.loadConfig(getActivity());
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
        });*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.tab_settings_settings, container, false);
        }

        return view;
    }

    @Override
    public void onReload() {

    }

    @Override
    public void refresh(Magister magister) {

    }

    @Override
    public boolean getForcedRefresh() {
        return false;
    }

    @Override
    public void setForcedRefresh(boolean forcedRefresh) {

    }

    @Override
    public int getTitle() {
        return R.string.settings;
    }
}
