package net.ilexiconn.hipster.notification;

public class HipsterNotification {
    private static int currentID = 0;

    public static int getUniqueID() {
        return currentID++;
    }
}
