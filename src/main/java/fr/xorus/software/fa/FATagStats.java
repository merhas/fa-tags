package fr.xorus.software.fa;

import org.jsoup.nodes.Document;

import javax.swing.text.View;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by Xorus on 26/10/2015.
 */
public class FATagStats {
    FurAffinity fa;
    TagCleaner tagCleaner;
    Boolean logguedIn = false;
    Boolean downloadImages = false;

    public FATagStats() {
        fa = new FurAffinity();
        tagCleaner = new TagCleaner();
    }

    public void login(String user, String password) {
        Status.updateStatus("Login...");

        if (fa.authenticate(user, password)) {
            Status.updateStatus("Loggued in as " + user);
            logguedIn = true;
        } else {
            Status.updateStatus("Could not log you in : " + fa.lastError + "<br>Try retrying (wrong password maybe?)");
        }
    }

    public void generateTagStats(String grabUser) {
        Status.updateStatus("Grabbing favorites list");
        List<String> keywords = new ArrayList<>();
        List<String> imageIds = (grabUser.length() > 0) ? fa.getFavorites(grabUser) : fa.getFavorites();
        ImageSaver saver = new ImageSaver();

        int imNumber = imageIds.size();
        int i = 0;
        for (String imageId : imageIds) {
            Status.updateStatus("Grabbing image tags " + ++i + " / " + imNumber + "<br>Id " + imageId, (int) (((float) i / (float) imNumber) * 100));
            ViewPage viewPage = fa.getViewPage(imageId);
            if (viewPage == null) {
                continue;
            }

            keywords.addAll(viewPage.parseImageTags());

            if (downloadImages) {
                String link = viewPage.parseImageLink();
                if (link != null) {
                    String name = link.substring(link.lastIndexOf("/"), link.length());
                    Status.updateStatus("Downloading image " + i + " / " + imNumber + "<br>" + name, (int) (((float) i / (float) imNumber) * 100));
                    fa.downloadImage(saver, link, name);
                } else {
                    System.err.println("could not download image " + imageId);
                }
            }
        }

        Status.updateStatus("Done, cleaning up tag list");
        Map<String, Integer> count = new HashMap<>();
        Set<String> uniqueKeywords = new HashSet<>(keywords);
        for (String keyword : uniqueKeywords) {
            count.put(keyword, Collections.frequency(keywords, keyword));
        }

        tagCleaner.clean(count);

        count = MapUtils.sortByValue(count);

        String csvOut = "";

        for (Map.Entry<String, Integer> entry : count.entrySet()) {
            String line = entry.getKey() + ";" + entry.getValue() + "\n";
            csvOut += line;
            System.out.print(line);
        }

        File output = new File("fa-tags.csv");
        PrintWriter writer;
        try {
            writer = new PrintWriter(output);
            writer.print(csvOut);
            writer.close();
        } catch (FileNotFoundException e) {
            System.err.print("could not open output file");
            e.printStackTrace();
        }

        Status.updateStatus("Done :). Output: " + output.getAbsolutePath());
    }
}
