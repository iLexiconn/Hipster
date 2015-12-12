package net.ilexiconn.hipster.config;

public class User {
    public final String school;
    public final String username;
    public final String password;
    public final String nickname;
    public final int id;

    public User(String school, String username, String password, String nickname, int id) {
        this.school = school;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.id = id;
    }
}
