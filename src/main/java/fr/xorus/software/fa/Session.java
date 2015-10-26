package fr.xorus.software.fa;

import org.jsoup.Connection;

import java.util.Map;

/**
 * Created by Xorus on 25/10/2015.
 */
public class Session {
    private Map<String, String> cookies;

    public Session(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public Connection prepare(Connection connection) {
        return connection.cookies(getCookies());
    }

    public void putAll(Map<String, String> cookies) {
        this.cookies.putAll(cookies);
    }
}
