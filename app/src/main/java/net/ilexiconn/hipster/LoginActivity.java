package net.ilexiconn.hipster;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import net.ilexiconn.hipster.config.Config;
import net.ilexiconn.hipster.config.User;
import net.ilexiconn.hipster.util.ColorUtil;
import net.ilexiconn.hipster.util.ConfigUtil;
import net.ilexiconn.magister.ParcelableMagister;
import net.ilexiconn.magister.container.School;

import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {
    private Config config;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        config = ConfigUtil.loadConfig(this);
        int color = config.color;
        findViewById(R.id.login_toolbar).setBackgroundColor(color);
        ((ImageView) findViewById(R.id.login_icon)).setColorFilter(color);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ColorUtil.darker(color, 0.75f));
        }

        User user = config.getCurrentUser();
        if (user != null) {
            new LoginThread().execute(user.school.replaceAll(" ", "%20"), user.username, user.password);
        }

        Button buttonLogin = (Button) findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInput()) {
                    login(v);
                }
            }
        });
    }

    public boolean checkInput() {
        EditText textSchool = (EditText) findViewById(R.id.input_school);

        AsyncTask<String, Void, Boolean> task = new CheckSchoolThread().execute(textSchool.getText().toString());
        try {
            if (!task.get()) {
                textSchool.setError(getString(R.string.invalid_school));
                return false;
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void login(View view) {
        Button buttonLogin = (Button) findViewById(R.id.button_login);
        EditText textSchool = (EditText) findViewById(R.id.input_school);
        EditText textUsername = (EditText) findViewById(R.id.input_username);
        EditText textPassword = (EditText) findViewById(R.id.input_password);

        buttonLogin.setEnabled(false);

        String school = textSchool.getText().toString();
        String username = textUsername.getText().toString();
        String password = textPassword.getText().toString();

        new LoginThread().execute(school, username, password);
    }

    public class CheckSchoolThread extends AsyncTask<String, Void, Boolean> {
        @Override
        public Boolean doInBackground(String... params) {
            School[] schools = School.findSchool(params[0]);
            return schools.length != 0;
        }
    }

    public class LoginThread extends AsyncTask<String, Void, ParcelableMagister> {
        public ProgressDialog progressDialog;

        @Override
        public void onPreExecute() {
            progressDialog = ProgressDialog.show(LoginActivity.this, "Hipster", "Logging in...", true);
        }

        @Override
        public void onPostExecute(ParcelableMagister magister) {
            Button buttonLogin = (Button) findViewById(R.id.button_login);
            EditText textPassword = (EditText) findViewById(R.id.input_password);

            if (magister == null) {
                buttonLogin.setEnabled(true);
                textPassword.setError(getString(R.string.invalid_password));
            } else {
                if (config.getUserByID(magister.user.username) == null) {
                    User user = new User(magister.school.name, magister.user.username, magister.user.password, magister.profile.nickname, magister.profile.id);
                    config.users.add(user);
                    config.currentUser = user.username;
                    ConfigUtil.saveConfig(LoginActivity.this, config);
                }
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("magister", magister);
                startActivity(intent);
                finish();
            }
            progressDialog.dismiss();
        }

        @Override
        public ParcelableMagister doInBackground(String... params) {
            try {
                School school = School.findSchool(params[0])[0];
                String username = params[1];
                String password = params[2];

                return ParcelableMagister.login(school, username, password);
            } catch (Exception e) {
                return null;
            }
        }
    }
}