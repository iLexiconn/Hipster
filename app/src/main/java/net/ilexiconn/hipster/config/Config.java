package net.ilexiconn.hipster.config;

import android.app.Activity;
import android.graphics.BitmapFactory;
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
    public int color = 1601006;
    public String currentUser;
    public boolean toolbarAvatar = true;

    private transient Map<User, IProfile<?>> profileMap = new HashMap<>();

    public void initProfiles(Activity activity, AccountHeaderBuilder accountHeaderBuilder) {
        File saveDir = new File(activity.getFilesDir(), "img");
        for (User user : users) {
            profileMap.put(user, new ProfileDrawerItem().withName(user.nickname));
            try {
                profileMap.get(user).withIcon(BitmapFactory.decodeStream(new FileInputStream(new File(saveDir, user.username + ".png"))));
            } catch (FileNotFoundException e) {}
            accountHeaderBuilder.addProfiles(profileMap.get(user));
        }
    }

    public IProfile<?> getProfileForUser(User user) {
        return profileMap.get(user);
    }

    public void addProfileForUser(Activity activity, AccountHeader accountHeaderBuilder, User user) {
        File saveDir = new File(activity.getFilesDir(), "img");
        profileMap.put(user, new ProfileDrawerItem().withName(user.nickname));
        try {
            profileMap.get(user).withIcon(BitmapFactory.decodeStream(new FileInputStream(new File(saveDir, user.username + ".png"))));
        } catch (FileNotFoundException e) {}
        accountHeaderBuilder.addProfile(profileMap.get(user), accountHeaderBuilder.getProfiles().size() - 2);
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
}
