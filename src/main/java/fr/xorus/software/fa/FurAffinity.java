package fr.xorus.software.fa;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Xorus on 25/10/2015.
 */
public class FurAffinity {
    public static final String FA_PROTOCOL = "https:";
    public static final String FA_URL = "https://www.furaffinity.net";
    public static final String LOGIN_ACTION = "/login/";
    public static final String FAVORITES_ACTION = "/favorites/";
    public static final String VIEW_ACTION = "/view/";
    public String user;
    private Session session = null;
    private Grabber grabber;
    public String lastError = "";

    public FurAffinity() {
        grabber = new Grabber();
    }

    public Boolean authenticate(String user, String password) {
        this.user = user;

        if (!isLoggedIn()) {
            try {
                session = grabber.login(user, password);
            } catch (IOException e) {
                lastError = e.getMessage();
                System.err.println("Could not authenticate : " + e.getMessage());
            } catch (LoginError loginError) {
                lastError = loginError.getMessage();
                return false;
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

    public ViewPage getViewPage(String id) {
        try {
            return new ViewPage(grabber.getImagePage(session, id));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void downloadImage(ImageSaver saver, String url, String id) {
        try {
            grabber.downloadImage(saver, session, url, id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Boolean isLoggedIn() {
        return session != null;
    }
}
