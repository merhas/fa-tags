package fr.xorus.software.fa;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Xorus on 26/10/2015.
 *
 * TODO: Use a stemmer to remove similar tags ?
 */
public class TagCleaner {
    private List<String> stopwords;

    public TagCleaner() {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("stopwords.txt");
        StringWriter writer = new StringWriter();
        try {
            IOUtils.copy(is, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.stopwords = Arrays.asList(writer.toString().split(","));
    }

    public void clean(List<String> list) {
        list.removeAll(stopwords);
    }

    public void clean(Map<String, Integer> map) {
        for (String stopword : stopwords) {
            map.remove(stopword);
        }
    }
}
