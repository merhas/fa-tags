/**
 * Created by Xorus on 25/10/2015.
 */
package fr.xorus.software.fa;

import java.io.IOException;

public class Launcher {
    public static void main(String[] args) {
        FATagStats stats = new FATagStats();

        stats.login("user", "password");
        stats.generateTagStats();
    }
}