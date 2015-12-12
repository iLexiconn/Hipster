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

    public User getUserByID(String username) {
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

    public User getUserByName(String nickname) {
        if (nickname == null || nickname.isEmpty()) {
            return null;
        }
        for (User user : users) {
            if (user.nickname.equals(nickname)) {
                return user;
            }
        }
        return null;
    }
}
