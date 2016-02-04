package net.ilexiconn.hipster.util;

import android.content.Context;
import android.util.Log;

import net.ilexiconn.hipster.config.Config;
import net.ilexiconn.magister.util.GsonUtil;

import java.io.*;

public class ConfigUtil {
    public static Config loadConfig(Context context) {
        File file = new File(context.getFilesDir(), "config.json");
        if (!file.exists()) {
            return new Config();
        } else {
            try {
                return GsonUtil.getGson().fromJson(new FileReader(file), Config.class);
            } catch (FileNotFoundException e) {
                Log.e("HIPSTER", "Unable to load config", e);
                return null;
            }
        }
    }

    public static void saveConfig(Context context, Config config) {
        File file = new File(context.getFilesDir(), "config.json");
        String json = GsonUtil.getGson().toJson(config);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(json);
            myOutWriter.close();
            fOut.close();
        } catch (IOException e) {
            Log.e("HIPSTER", "Unable to save config", e);
        }
    }
}
