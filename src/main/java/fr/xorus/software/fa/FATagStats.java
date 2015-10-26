package fr.xorus.software.fa;

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

    public FATagStats() {
        fa = new FurAffinity();
        tagCleaner = new TagCleaner();
    }

    public void login(String user, String password) {
        fa.authenticate(user, password);
    }

    public void generateTagStats() {
        List<String> keywords = new ArrayList<>();
        List<String> imageIds = fa.getFavorites();

        int i = 0;
        for (String imageId : imageIds) {
            keywords.addAll(fa.getImageTags(imageId));
            if (++i >= 10) break;
        }

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

        PrintWriter writer;
        try {
            writer = new PrintWriter("fa-tags.csv", "UTF-8");
            writer.print(csvOut);
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            System.err.print("could not open output file");
            e.printStackTrace();
        }
    }
}
