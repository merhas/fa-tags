package fr.xorus.software.fa;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Xorus on 25/10/2015.
 */
public class FurAffinity {
    public static final String FA_URL = "https://www.furaffinity.net";
    public static final String LOGIN_ACTION = "/login/";
    public static final String FAVORITES_ACTION = "/favorites/";
    public static final String VIEW_ACTION = "/view/";
    public String user;
    private Session session = null;
    private Grabber grabber;

    public FurAffinity() {
        grabber = new Grabber();
    }

    public Boolean authenticate(String user, String password) {
        this.user = user;

        if (!isLoggedIn()) {
            try {
                session = grabber.login(user, password);
            } catch (IOException e) {
                System.err.println("Could not authenticate : " + e.getMessage());
            }
        }

        return isLoggedIn();
    }

    public List<String> getFavorites(String user) {
        try {
            return grabber.getFavorites(session, user);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getFavorites() {
        return getFavorites(user);
    }

    public List<String> getImageTags(String id) {
        try {
            return grabber.getImageTags(session, id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Boolean isLoggedIn() {
        return session != null;
    }
}
