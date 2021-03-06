package net.ilexiconn.hipster;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;

import io.fabric.sdk.android.Fabric;

import net.ilexiconn.hipster.config.Config;
import net.ilexiconn.hipster.config.User;
import net.ilexiconn.hipster.util.ConfigUtil;
import net.ilexiconn.magister.ParcelableMagister;
import net.ilexiconn.magister.container.School;

import java.io.IOException;
import java.text.ParseException;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        Config config = ConfigUtil.loadConfig(this);
        final User currentUser = config.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        setContentView(R.layout.activity_splash);
        TextView splashText = (TextView) findViewById(R.id.splash_text);
        TextView splashVersion = (TextView) findViewById(R.id.splash_version);
        splashVersion.setText(BuildConfig.VERSION_NAME + (BuildConfig.DEBUG ? "-DEBUG" : ""));
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/ProductSans-Regular.ttf");
        splashText.setTypeface(font);
        splashVersion.setTypeface(font);
        final AnimatedCircleLoadingView loadingView = (AnimatedCircleLoadingView) findViewById(R.id.circle_loading_view);
        loadingView.startIndeterminate();
        new AsyncTask<Void, Void, ParcelableMagister>() {
            @Override
            protected ParcelableMagister doInBackground(Void... params) {
                ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (networkInfo.isConnected()) {
                    try {
                        return ParcelableMagister.login(School.findSchool(currentUser.school)[0], currentUser.username, currentUser.password);
                    } catch (IOException | ParseException e) {
                        e.printStackTrace();
                        return null;
                    }
                } else {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(final ParcelableMagister magister) {
                if (magister != null) {
                    loadingView.stopOk();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            intent.putExtra("magister", magister);
                            startActivity(intent);
                            finish();
                        }
                    }, 5000);
                } else {
                    loadingView.stopFailure();
                }
            }
        }.execute();
    }
}
