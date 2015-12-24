package net.ilexiconn.hipster.config;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.util.Log;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import net.ilexiconn.hipster.util.IMatcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {
    public List<User> users = new ArrayList<>();
    public String color = "#186dee";
    public String currentUser;
    public boolean toolbarAvatar = true;

    private static transient Map<String, IProfile<?>> profileMap = new HashMap<>();

    public void initProfiles(Activity activity, AccountHeaderBuilder accountHeaderBuilder) {
        File saveDir = new File(activity.getFilesDir(), "img");
        for (User user : users) {
            profileMap.put(user.username, new ProfileDrawerItem().withName(user.nickname));
            try {
                profileMap.get(user.username).withIcon(BitmapFactory.decodeStream(new FileInputStream(new File(saveDir, user.username + ".png"))));
            } catch (FileNotFoundException e) {}
            accountHeaderBuilder.addProfiles(profileMap.get(user.username));
        }
        checkProfiles("initProfiles");
    }

    public IProfile<?> getProfileForUser(User user) {
        checkProfiles("getProfileForUser");
        return profileMap.get(user.username);
    }

    public void addProfileForUser(Activity activity, AccountHeader accountHeader, User user) {
        File saveDir = new File(activity.getFilesDir(), "img");
        profileMap.put(user.username, new ProfileDrawerItem().withName(user.nickname));
        try {
            profileMap.get(user.username).withIcon(BitmapFactory.decodeStream(new FileInputStream(new File(saveDir, user.username + ".png"))));
        } catch (FileNotFoundException e) {}
        accountHeader.addProfile(profileMap.get(user.username), accountHeader.getProfiles().size() - 2);
        checkProfiles("addProfileForUser");
    }

    public void removeProfile(AccountHeader accountHeader, User user) {
        checkProfiles("removeProfile (1)");
        IProfile<?> profile = getProfileForUser(user);
        profileMap.remove(user.username);
        accountHeader.removeProfileByIdentifier(profile.getIdentifier());
        checkProfiles("removeProfile (2)");
    }

    public User getCurrentUser() {
        if (currentUser == null || currentUser.isEmpty()) {
            return null;
        }
        for (User user : users) {
            if (user.username.equals(currentUser)) {
                return user;
            }
        }
        return null;
    }

    public User getUser(String username) {
        if (username == null || username.isEmpty()) {
            return null;
        }
        for (User user : users) {
            if (user.username.equals(username)) {
                return user;
            }
        }
        return null;
    }

    public User getUser(IMatcher<User> matcher) {
        if (matcher == null) {
            return null;
        }
        for (User user : users) {
            if (matcher.matches(user)) {
                return user;
            }
        }
        return null;
    }

    public void checkProfiles(String method) {
        Log.i("HIPSTER", "CURRENT PROFILES (" + method + ")");
        for (Map.Entry<String, IProfile<?>> entry : profileMap.entrySet()) {
            Log.i("HIPSTER", " - " + entry.getKey());
        }
        Log.i("HIPSTER", "END");
    }
}
