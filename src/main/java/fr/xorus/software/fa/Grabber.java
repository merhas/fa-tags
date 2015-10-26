package fr.xorus.software.fa;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xorus on 25/10/2015.
 */
public class Grabber {
    private static int TIMEOUT = 10000;

    public Session login(String user, String password) throws IOException {
        Connection.Response loginForm;
        String url = FurAffinity.FA_URL + FurAffinity.LOGIN_ACTION;

        loginForm = prepareConnection(url).method(Connection.Method.GET).execute();

        Session session = new Session(loginForm.cookies());

        Connection.Response loginAttempt = Jsoup.connect(url)
                .data("action", "login")
                .data("retard_protection", "1")
                .data("login", "Login to FurAffinity")
                .data("name", user)
                .data("pass", password)
                .cookies(loginForm.cookies())
                .timeout(TIMEOUT)
                .method(Connection.Method.POST)
                .execute();

        Document loginAttemptDoc = new Document(loginAttempt.body());
        // todo: handle login error
        session.putAll(loginAttempt.cookies());
        return session;
    }

    public List<String> getFavorites(Session session, String user) throws IOException {
        String url = FurAffinity.FA_URL + FurAffinity.FAVORITES_ACTION + user + "/";
        List<String> ids = new ArrayList<>();

        for (int i = 1; ; i++) {
            String pageUrl = url + i;
            Document favorites = prepareConnection(pageUrl, session).get();
            if (isError(favorites)) {
                break;
            }

            Elements elms = favorites.select("#favorites a[href^=\"/view/\"]");
            if (elms.size() == 0) {
                //System.out.println("Nothing to see here");
                break;
            }

            for (Element e : elms) {
                String href = e.attr("href");
                ids.add(href.substring("/view/" .length(), href.length() - 1));
            }

            //System.out.println(ids);
        }

        System.out.println(ids.size() + " faves found");
        return ids;
    }

    public List<String> getImageTags(Session session, String id) throws IOException {
        String url = FurAffinity.FA_URL + FurAffinity.VIEW_ACTION + id;

        Document artwork = prepareConnection(url, session).get();
        Elements keywords = artwork.select("#keywords a");

        List<String> keywordList = new ArrayList<String>();
        for (Element keyword : keywords) {
            keywordList.add(keyword.text().toLowerCase());
        }

        return keywordList;
    }

    private boolean isError(Document document) {
        if (document.select("html body div div > table.maintable > tbody tr:first-child td.cat b").html().equals("System Message")) {
            System.err.println("There was an error");
            System.err.println(document.select("html body div div table.maintable tbody tr td.alt1 b").text());

            return true;
        }
        return false;
    }

    private Connection prepareConnection(String url) {
        System.out.println("Preparing connection to " + url);
        return Jsoup.connect(url).timeout(TIMEOUT);
    }

    private Connection prepareConnection(String url, Session session) {
        return session.prepare(prepareConnection(url));
    }
}