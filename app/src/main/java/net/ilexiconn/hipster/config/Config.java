package net.ilexiconn.hipster.config;

import java.util.ArrayList;
import java.util.List;

public class Config {
    public List<User> users = new ArrayList<>();
    public int color = -16738597;
    public String currentUser;

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

    public User getUser(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        for (User user : users) {
            if (user.username.equals(name)) {
                return user;
            }
        }
        return null;
    }
}
