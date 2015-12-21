package net.ilexiconn.hipster.config;

import net.ilexiconn.hipster.util.IMatcher;

import java.util.ArrayList;
import java.util.List;

public class Config {
    public List<User> users = new ArrayList<>();
    public int color = -16738597;
    public String currentUser;
    public boolean toolbarAvatar = true;

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
